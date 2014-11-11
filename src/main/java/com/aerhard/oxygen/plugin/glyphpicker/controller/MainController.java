package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphTableModel;
import com.aerhard.oxygen.plugin.glyphpicker.model.PathComboModel;
import com.aerhard.oxygen.plugin.glyphpicker.model.tei.GlyphModel;
import com.aerhard.oxygen.plugin.glyphpicker.model.tei.UserListModel;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphTable;
import com.aerhard.oxygen.plugin.glyphpicker.view.MainPanel;
import com.jidesoft.swing.AutoCompletionComboBox;

public class MainController {

    private static final Logger LOGGER = Logger.getLogger(MainController.class
            .getName());
    private MainPanel mainPanel;
    private GlyphTable table;
    private GlyphTableModel glyphTableModel;
    private PathComboModel pathComboModel;
    private DataStore dataStore;
    private String baseUrl;
    private Boolean isLoading = false;
    private ConfigController configController;
    private JList<GlyphModel> userList;
    private UserListController userListController;
    private UserListModel userListModel;
    private Color tabForeground = null;
    private AutoCompletionComboBox rangeCombo;

    private List<InsertListener> listeners = new ArrayList<InsertListener>();

    private TableRowSorter<GlyphTableModel> sorter;

    public MainController(StandalonePluginWorkspace workspace) {
        configController = new ConfigController(workspace);
        configController.load();

        userListController = new UserListController(workspace);
        userListController.load();

        mainPanel = new MainPanel();
        userList = mainPanel.getUserList();
        table = mainPanel.getTable();
        glyphTableModel = new GlyphTableModel();

        // tableFilter = new TableFilterHeader(table, AutoChoices.ENABLED);

        dataStore = new DataStore();

        mainPanel.getTable().setModel(glyphTableModel);

        sorter = new TableRowSorter<GlyphTableModel>(glyphTableModel);
        table.setRowSorter(sorter);
        
        pathComboModel = configController.getConfig().getPaths();
        mainPanel.getPathCombo().setModel(pathComboModel);

        userListModel = userListController.getUserListModel();
        mainPanel.getUserList().setModel(userListModel);

        rangeCombo = mainPanel.getRangeFilterCombo();
        
        setBrowserListeners();
        setUserListListeners();

        loadData();

    }

    private void newFilter(final String value) {

        RowFilter<GlyphTableModel, Integer> glyphFilter = new RowFilter<GlyphTableModel, Integer>() {
            public boolean include(
                    Entry<? extends GlyphTableModel, ? extends Integer> entry) {

                String entryRange = ((GlyphModel) entry.getValue(0)).getRange();

                if (entryRange != null && entryRange.startsWith(value)) {
                    return true;
                }

                return false;
            }
        };

        sorter.setRowFilter(glyphFilter);

    }

    
    @SuppressWarnings("unchecked")
    private void updateRangeCombo() {
        List<String> ranges = glyphTableModel.getUniqueRanges();
        ranges.add(0, "");
        String[] rangesArray = ranges.toArray(new String[ranges.size()]);
        rangeCombo.setModel(new DefaultComboBoxModel<String>(rangesArray));
    }
    
    public void addListener(InsertListener toAdd) {
        listeners.add(toAdd);
    }

    public void fireInsertGlyph(GlyphModel model) {
        for (InsertListener il : listeners)
            il.insert(model);
    }

    private void insertGlyphFromBrowser() {
        int row = table.getSelectedRow();
        if (row != -1) {
            GlyphModel selectedModel = glyphTableModel.getModelAt(table
                    .convertRowIndexToModel(row));
            if (selectedModel != null) {
                fireInsertGlyph(selectedModel);
            }
        }
    }

    private void insertGlyphFromUser() {
        int index = userList.getSelectedIndex();
        if (index != -1) {
            fireInsertGlyph(userListModel.getElementAt(index));
        }
    }

    public void highlightTabButton(int index) {
        final Component tab = mainPanel.getTabbedPane()
                .getTabComponentAt(index);
        if (tabForeground == null) {
            tabForeground = tab.getForeground();
        }
        tab.setForeground(Color.GRAY);
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                tab.setForeground(tabForeground);
            }
        };
        Timer timer = new Timer(300, taskPerformer);
        timer.setRepeats(false);
        timer.start();
    }

    private void addItemToUserList() {
        int row = table.getSelectedRow();
        if (row != -1) {
            GlyphModel selectedModel = glyphTableModel.getModelAt(table
                    .convertRowIndexToModel(row));
            if (selectedModel != null) {
                GlyphModel ch = new GlyphModel(selectedModel);
                userListModel.addElement(ch);
                highlightTabButton(0);
            }
        }
    }

    private void removeItemFromUserList() {
        int index = userList.getSelectedIndex();
        if (index != -1) {
            userListModel.removeElement(index);
            index = Math.min(index, userList.getModel().getSize() - 1);
            if (index >= 0) {
                userList.setSelectedIndex(index);
            }
        }
    }

    private void setBrowserListeners() {
        mainPanel.getPathCombo().getEditor().getEditorComponent()
                .addKeyListener(new KeyAdapter() {
                    public void keyReleased(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            loadData();
                        }
                    }
                });

        
        rangeCombo.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                newFilter(rangeCombo.getSelectedItem().toString());
            }
            
        });
        
        JButton btn;

        btn = mainPanel.getBrowserButtonAdd();
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItemToUserList();
            }
        });

        btn = mainPanel.getBrowserButtonInsert();
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertGlyphFromBrowser();
            }
        });

        // btn = mainPanel.getBrowserButtonClose();
        // btn.addActionListener(new ActionListener() {
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // // mainPanel.dispose();
        // }
        // });

        btn = mainPanel.getBrowserButtonLoad();
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });

        table.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent event) {
                        if (event.getValueIsAdjusting() == false) {
                            int row = table.getSelectedRow();
                            if (row == -1) {
                                mainPanel.enableBrowserButtons(false);
                            } else {
                                mainPanel.enableBrowserButtons(true);
                            }
                        }

                    }

                });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    insertGlyphFromBrowser();
                }
            }
        });

    }

    private void setUserListListeners() {

        JButton btn;

        btn = mainPanel.getUserButtonRemove();
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeItemFromUserList();
            }
        });

        btn = mainPanel.getUserButtonInsert();
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertGlyphFromUser();
            }
        });

        // btn = mainPanel.getUserButtonClose();
        // btn.addActionListener(new ActionListener() {
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // // mainPanel.dispose();
        // }
        // });

        userList.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent event) {
                        if (event.getValueIsAdjusting() == false) {
                            int index = userList.getSelectedIndex();
                            if (index == -1) {
                                mainPanel.enableUserButtons(false);
                            } else {
                                mainPanel.enableUserButtons(true);
                            }
                        }

                    }

                });

        userList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    insertGlyphFromUser();
                }
            }
        });
    }

    private void loadData() {

        final String path = mainPanel.getPathCombo().getSelectedItem()
                .toString();

        if (isLoading) {
            LOGGER.info("Skipping data loading.");
            return;
        }
        isLoading = true;
        mainPanel.getLoadingMask().start();

        SwingWorker<GlyphModel[], Void> worker = new SwingWorker<GlyphModel[], Void>() {
            @Override
            public GlyphModel[] doInBackground() {
                GlyphModel[] data = dataStore.loadData(path);
                return data;
            }

            @Override
            public void done() {
                try {
                    GlyphModel[] data = get();
                    if (data == null) {
                        LOGGER.warn("Data is null");
                        glyphTableModel.clear();
                    } else {
                        glyphTableModel.setData(data);
                        updateRangeCombo();
                        pathComboModel.setFirstItem(path);
                    }
                    newFilter("");
                    setBaseUrl(path);
                    mainPanel.getLoadingMask().stop();
                    isLoading = false;
                } catch (InterruptedException e) {
                    LOGGER.error(e);
                } catch (ExecutionException e) {
                    LOGGER.error(e);
                }
            }
        };
        worker.execute();
    }

    public ConfigController getConfigController() {
        return configController;
    }

    public UserListController getUserListController() {
        return userListController;
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

}
