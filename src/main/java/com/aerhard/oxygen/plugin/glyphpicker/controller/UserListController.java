package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphGridModel;
import com.aerhard.oxygen.plugin.glyphpicker.view.UserListPanel;

public class UserListController extends Controller {

    private UserListPanel userListPanel;
    private UserListLoader userListLoader;
    private GlyphGridModel userListModel;
    private JList<GlyphDefinition> userList;

    public UserListController(StandalonePluginWorkspace workspace,
            Properties properties) {

        userListPanel = new UserListPanel();
        userList = userListPanel.getUserList();

        userListModel = new GlyphGridModel();
        userList.setModel(userListModel);

        userListLoader = new UserListLoader(workspace, properties);

        setListeners();

    }

    public UserListPanel getPanel() {
        return userListPanel;
    }

    private void removeItemFromUserList() {
        int index = userList.getSelectedIndex();
        if (index != -1) {
            userListModel.removeElement(index);
            index = Math.min(index, userListModel.getSize() - 1);
            if (index >= 0) {
                userList.setSelectedIndex(index);
            }
        }
    }

    private void insertGlyphFromUser() {
        int index = userList.getSelectedIndex();
        if (index != -1) {
            fireEvent("insert", getListModel().getElementAt(index));
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

        userListModel.addListDataListener(new ListDataListener(){
            @Override
            public void contentsChanged(ListDataEvent e) {
                if (((GlyphGridModel) e.getSource()).isInSync()) {
                    userListPanel.enableSaveButtons(false);    
                } else {
                    userListPanel.enableSaveButtons(true);
                }
            }

            @Override
            public void intervalAdded(ListDataEvent arg0) {
            }

            @Override
            public void intervalRemoved(ListDataEvent arg0) {
            }
        });
        
    }

    public GlyphGridModel getListModel() {
        return userListModel;
    }

    public UserListLoader getUserListLoader() {
        return userListLoader;
    }

    @Override
    public void loadData() {
        userListModel.setData(userListLoader.load().getData());
    }

    @Override
    public void saveData() {
        userListLoader.save(userListModel);
        userListModel.setInSync(true);
    }

    @Override
    public void eventOccured(String type, GlyphDefinition model) {
        if ("export".equals(type)) {
            getListModel().addElement(model);
        }

    }

}
