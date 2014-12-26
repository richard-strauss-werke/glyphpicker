package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.swing.DefaultEventListModel;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinitions;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphGrid;
import com.aerhard.oxygen.plugin.glyphpicker.view.UserCollectionPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.renderer.GlyphRendererAdapter;
import com.aerhard.oxygen.plugin.glyphpicker.view.renderer.ListItemRenderer;

public class UserCollectionController extends Controller {

    private UserCollectionPanel userCollectionPanel;
    private UserCollectionLoader userCollectionLoader;
    private BasicEventList<GlyphDefinition> userCollectionModel;
    private GlyphGrid list;
    protected boolean listInSync = true;

    private int activeListIndex;

    @SuppressWarnings("unchecked")
    public UserCollectionController(StandalonePluginWorkspace workspace,
            Properties properties) {

        userCollectionPanel = new UserCollectionPanel();

        userCollectionModel = new BasicEventList<GlyphDefinition>();

        list = userCollectionPanel.getUserCollection();

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        GlyphRendererAdapter r = new GlyphRendererAdapter(list);
        r.setPreferredSize(new Dimension(90, 90));
        list.setFixedSize(90);
        list.setCellRenderer(r);
        
        
        list.setModel(new DefaultEventListModel<GlyphDefinition>(
                userCollectionModel));

        userCollectionPanel.getViewCombo().setAction(new ChangeViewAction());

        userCollectionLoader = new UserCollectionLoader(workspace, properties);

        setListeners();

    }

    public UserCollectionPanel getPanel() {
        return userCollectionPanel;
    }

    private void removeItemFromUserCollection() {
        int index = list.getSelectedIndex();
        if (index != -1) {
            listInSync = false;
            userCollectionModel.remove(index);
            index = Math.min(index, userCollectionModel.size() - 1);
            if (index >= 0) {
                list.setSelectedIndex(index);
            }
        }
    }

    private void insertGlyphFromUser() {
        int index = list.getSelectedIndex();
        if (index != -1) {
            fireEvent("insert", getListModel().get(index));
        }
    }

    private class ChangeViewAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            int comboIndex = ((JComboBox<?>) e.getSource()).getSelectedIndex();
            if (activeListIndex != comboIndex) {
                if (comboIndex == 1) {
                    
                    // TODO make user list view work with glyphrenderer!
                    
//                    list.setCellRenderer(new GlyphRenderer());
                    list.setCellRenderer(new ListItemRenderer());
                    
                    list.setLayoutOrientation(JList.VERTICAL);
                } else {
                    GlyphRendererAdapter r = new GlyphRendererAdapter(list);
                    r.setPreferredSize(new Dimension(90, 90));
                    list.setFixedSize(90);
                    list.setCellRenderer(r);
                    list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                }
                activeListIndex = comboIndex;
            }
        }
    }

    private void setListeners() {
        JButton btn;
        btn = userCollectionPanel.getBtnRemove();
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeItemFromUserCollection();
            }
        });

        btn = userCollectionPanel.getBtnInsert();
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertGlyphFromUser();
            }
        });

        btn = userCollectionPanel.getBtnSave();
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData();
                userCollectionPanel.enableSaveButtons(false);
            }
        });

        btn = userCollectionPanel.getBtnReload();
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
                userCollectionPanel.enableSaveButtons(false);
            }
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    userCollectionPanel.getBtnInsert().highlight();
                    insertGlyphFromUser();
                }
            }
        });

        list.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent event) {
                        if (!event.getValueIsAdjusting()) {
                            if (list.getSelectedIndex() == -1) {
                                userCollectionPanel
                                        .enableSelectionButtons(false);
                            } else {
                                userCollectionPanel
                                        .enableSelectionButtons(true);
                            }
                        }
                    }
                });

        userCollectionModel
                .addListEventListener(new ListEventListener<GlyphDefinition>() {
                    @Override
                    public void listChanged(ListEvent<GlyphDefinition> e) {
                        if (listInSync) {
                            userCollectionPanel.enableSaveButtons(false);
                        } else {
                            userCollectionPanel.enableSaveButtons(true);
                        }

                    }
                });

    }

    public BasicEventList<GlyphDefinition> getListModel() {
        return userCollectionModel;
    }

    public UserCollectionLoader getUserCollectionLoader() {
        return userCollectionLoader;
    }

    @Override
    public void loadData() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                listInSync = true;
                userCollectionModel.clear();
                userCollectionModel.addAll(userCollectionLoader.load()
                        .getData());
            }
        });
    }

    @Override
    public void saveData() {
        userCollectionLoader.save(new GlyphDefinitions(userCollectionModel));
        listInSync = true;
    }

    @Override
    public void eventOccured(String type, GlyphDefinition model) {
        if ("copyToUserCollection".equals(type)) {
            listInSync = false;
            userCollectionModel.add(model);
        }

    }

}
