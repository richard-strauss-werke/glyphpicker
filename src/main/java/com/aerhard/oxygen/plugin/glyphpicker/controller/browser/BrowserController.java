package com.aerhard.oxygen.plugin.glyphpicker.controller.browser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;

import com.aerhard.oxygen.plugin.glyphpicker.model.Config;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphTableModel;
import com.aerhard.oxygen.plugin.glyphpicker.model.PathComboModel;
import com.aerhard.oxygen.plugin.glyphpicker.model.tei.GlyphItem;
import com.aerhard.oxygen.plugin.glyphpicker.view.browser.BrowserPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.browser.GlyphTable;
import com.jidesoft.swing.AutoCompletionComboBox;

public class BrowserController {

    private static final Logger LOGGER = Logger
            .getLogger(BrowserController.class.getName());

    private BrowserPanel browserPanel;
    private GlyphTable table;
    private GlyphTableModel glyphTableModel;
    private TableRowSorter<GlyphTableModel> sorter;
    private PathComboModel pathComboModel;
    private AutoCompletionComboBox rangeCombo;
    private AutoCompletionComboBox classCombo;
    private SourceDataLoader dataStore;
    private boolean isLoading = false;

    public BrowserController(Config config) {

        browserPanel = new BrowserPanel();

        browserPanel.enableBrowserButtons(false);

        table = browserPanel.getTable();
        glyphTableModel = new GlyphTableModel();
        table.setModel(glyphTableModel);
        table.getColumnModel().getColumn(0).setMinWidth(70);
        table.getColumnModel().getColumn(0).setMaxWidth(70);
        sorter = new TableRowSorter<GlyphTableModel>(glyphTableModel);
        table.setRowSorter(sorter);
        pathComboModel = config.getPaths();
        browserPanel.getPathCombo().setModel(pathComboModel);

        rangeCombo = browserPanel.getRangeCombo();
        classCombo = browserPanel.getClassCombo();

        dataStore = new SourceDataLoader();

        setListeners();

    }

    public BrowserPanel getPanel() {
        return browserPanel;
    }

    public GlyphTableModel getTableModel() {
        return glyphTableModel;
    }

    private final void setListeners() {
        browserPanel.getPathCombo().getEditor().getEditorComponent()
                .addKeyListener(new KeyAdapter() {
                    public void keyReleased(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            loadData();
                        }
                    }
                });

        rangeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newFilter();
            }
        });

        classCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newFilter();
            }
        });

        JButton btn;

        btn = browserPanel.getBtnLoad();
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
                        if (!event.getValueIsAdjusting()) {
                            if (table.getSelectedRow() == -1) {
                                browserPanel.enableBrowserButtons(false);
                            } else {
                                browserPanel.enableBrowserButtons(true);
                            }
                        }
                    }
                });

        glyphTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                updateRangeCombo();
                updateClassCombo();
                newFilter();
            }
        });

    }

    private void newFilter() {

        final String rangeValue = rangeCombo.getSelectedItem().toString();
        final String classValue = classCombo.getSelectedItem().toString();

        RowFilter<GlyphTableModel, Integer> rangeFilter = new RowFilter<GlyphTableModel, Integer>() {
            public boolean include(
                    Entry<? extends GlyphTableModel, ? extends Integer> entry) {
                GlyphItem model = (GlyphItem) entry.getValue(0);
                String entryRange = model.getRange();
                if (entryRange != null && entryRange.startsWith(rangeValue)) {
                    return true;
                }
                return false;
            }
        };

        RowFilter<GlyphTableModel, Integer> classFilter = new RowFilter<GlyphTableModel, Integer>() {
            public boolean include(
                    Entry<? extends GlyphTableModel, ? extends Integer> entry) {
                GlyphItem model = (GlyphItem) entry.getValue(0);
                List<String> entryClasses = model.getClasses();
                for (String entryClass : entryClasses) {
                    if (entryClass.startsWith(classValue)) {
                        return true;
                    }
                }
                return false;
            }
        };

        if (!rangeValue.equals("") && !classValue.equals("")) {
            List<RowFilter<GlyphTableModel, Integer>> filters = new ArrayList<RowFilter<GlyphTableModel, Integer>>();
            filters.add(classFilter);
            filters.add(rangeFilter);
            sorter.setRowFilter(RowFilter.andFilter(filters));
        } else if (!rangeValue.equals("")) {
            sorter.setRowFilter(rangeFilter);
        } else if (!classValue.equals("")) {
            sorter.setRowFilter(classFilter);
        } else {
            sorter.setRowFilter(null);
        }

    }

    @SuppressWarnings("unchecked")
    private void updateRangeCombo() {
        List<String> ranges = glyphTableModel.getUniqueRanges();
        Collections.sort(ranges);
        ranges.add(0, "");
        String[] rangesArray = ranges.toArray(new String[ranges.size()]);
        rangeCombo.setModel(new DefaultComboBoxModel<String>(rangesArray));
    }

    @SuppressWarnings("unchecked")
    private void updateClassCombo() {
        List<String> classes = glyphTableModel.getUniqueClasses();
        Collections.sort(classes);
        classes.add(0, "");
        String[] classesArray = classes.toArray(new String[classes.size()]);
        classCombo.setModel(new DefaultComboBoxModel<String>(classesArray));
    }

    public void loadData() {

        final String path = browserPanel.getPathCombo().getSelectedItem()
                .toString();

        if (isLoading) {
            LOGGER.info("Skipping data loading.");
            return;
        }
        isLoading = true;
        // mainPanel.getLoadingMask().start();

        SwingWorker<List<GlyphItem>, Void> worker = new SwingWorker<List<GlyphItem>, Void>() {
            @Override
            public List<GlyphItem> doInBackground() {
                return dataStore.loadData(path);
            }

            @Override
            public void done() {
                try {
                    List<GlyphItem> data = get();
                    glyphTableModel.setData(data);

                    // when loading was successful, set the loading path as
                    // first item in the pathComboModel
                    if (data != null) {
                        pathComboModel.setFirstItem(path);
                    }
                    // mainPanel.getLoadingMask().stop();
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

}
