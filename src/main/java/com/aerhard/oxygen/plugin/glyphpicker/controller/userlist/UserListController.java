package com.aerhard.oxygen.plugin.glyphpicker.controller.userlist;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import com.aerhard.oxygen.plugin.glyphpicker.model.tei.GlyphItem;
import com.aerhard.oxygen.plugin.glyphpicker.model.tei.UserListModel;
import com.aerhard.oxygen.plugin.glyphpicker.view.userlist.UserListPanel;

public class UserListController {

    private UserListPanel userListPanel;
    private UserListLoader userListLoader;
    private UserListModel userListModel;
    private JList<GlyphItem> userList;

    public UserListController(StandalonePluginWorkspace workspace,
            Properties properties) {

        userListPanel = new UserListPanel();
        userList = userListPanel.getUserList();

        userListLoader = new UserListLoader(workspace, properties);
        userListLoader.load();

        userListModel = userListLoader.getUserListModel();
        userList.setModel(userListModel);

        setListeners();

    }

    public UserListPanel getPanel() {
        return userListPanel;
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

        userList.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent event) {
                        if (!event.getValueIsAdjusting()) {
                            if (userList.getSelectedIndex() == -1) {
                                userListPanel.enableUserButtons(false);
                            } else {
                                userListPanel.enableUserButtons(true);
                            }
                        }
                    }
                });

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

    public UserListModel getListModel() {
        return userListModel;
    }

    public UserListLoader getUserListLoader() {
        return userListLoader;
    }

}
