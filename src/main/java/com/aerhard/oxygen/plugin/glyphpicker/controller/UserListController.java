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
import com.aerhard.oxygen.plugin.glyphpicker.view.UserListPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.renderer.GlyphShapeRenderer;
import com.aerhard.oxygen.plugin.glyphpicker.view.renderer.ListItemRenderer;

public class UserListController extends Controller {

    private UserListPanel userListPanel;
    private UserListLoader userListLoader;
    private BasicEventList<GlyphDefinition> userListModel;
    private JList<GlyphDefinition> userList;
    protected boolean listInSync = true;
    
    private int activeListIndex;


    @SuppressWarnings("unchecked")
    public UserListController(StandalonePluginWorkspace workspace,
            Properties properties) {

        userListPanel = new UserListPanel();

        userListModel = new BasicEventList<GlyphDefinition>();

        userList = userListPanel.getUserList();
        
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        GlyphShapeRenderer r = new GlyphShapeRenderer();
        r.setPreferredSize(new Dimension(90, 90));
        userList.setCellRenderer(r);

        userList.setModel(new DefaultEventListModel<GlyphDefinition>(
                userListModel));
        
        
        
        
        userListPanel.getViewCombo().setAction(new ChangeViewAction());

        userListLoader = new UserListLoader(workspace, properties);

        setListeners();

    }

    public UserListPanel getPanel() {
        return userListPanel;
    }

    private void removeItemFromUserList() {
        int index = userList.getSelectedIndex();
        if (index != -1) {
            listInSync=false;
            userListModel.remove(index);
            index = Math.min(index, userListModel.size() - 1);
            if (index >= 0) {
                userList.setSelectedIndex(index);
            }
        }
    }

    private void insertGlyphFromUser() {
        int index = userList.getSelectedIndex();
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
                    userList.setCellRenderer(new ListItemRenderer());
                    userList.setLayoutOrientation(JList.VERTICAL);
                } else {
                    GlyphShapeRenderer r = new GlyphShapeRenderer();
                    r.setPreferredSize(new Dimension(90, 90));
                    userList.setCellRenderer(r);
                    userList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                }
                activeListIndex = comboIndex;
            }
        }
    }

    
    
    private void setListeners() {
        JButton btn;
        btn = userListPanel.getBtnRemove();
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeItemFromUserList();
            }
        });

        btn = userListPanel.getBtnInsert();
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertGlyphFromUser();
            }
        });

        btn = userListPanel.getBtnSave();
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData();
                userListPanel.enableSaveButtons(false);
            }
        });

        btn = userListPanel.getBtnReload();
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
                userListPanel.enableSaveButtons(false);
            }
        });

        userList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    userListPanel.getBtnInsert().highlight();
                    insertGlyphFromUser();
                }
            }
        });

        userList.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent event) {
                        if (!event.getValueIsAdjusting()) {
                            if (userList.getSelectedIndex() == -1) {
                                userListPanel.enableSelectionButtons(false);
                            } else {
                                userListPanel.enableSelectionButtons(true);
                            }
                        }
                    }
                });

        userListModel.addListEventListener(new ListEventListener<GlyphDefinition>() {
            @Override
            public void listChanged(ListEvent<GlyphDefinition> e) {
                if (listInSync) {
                    userListPanel.enableSaveButtons(false);
                } else {
                    userListPanel.enableSaveButtons(true);
                }
                
            }
        });

    }

    public BasicEventList<GlyphDefinition> getListModel() {
        return userListModel;
    }

    public UserListLoader getUserListLoader() {
        return userListLoader;
    }

    @Override
    public void loadData() {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                listInSync = true;
                userListModel.clear();
                userListModel.addAll(userListLoader.load().getData());    
            }
        });
    }

    @Override
    public void saveData() {
        userListLoader.save(new GlyphDefinitions(userListModel));
        listInSync=true;
    }

    @Override
    public void eventOccured(String type, GlyphDefinition model) {
        if ("export".equals(type)) {
            listInSync=false;
            userListModel.add(model);
        }

    }

}
