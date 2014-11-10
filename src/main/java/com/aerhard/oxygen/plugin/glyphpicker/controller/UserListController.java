package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.io.File;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;

import org.apache.log4j.Logger;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import com.aerhard.oxygen.plugin.glyphpicker.model.tei.UserListModel;

public class UserListController {

    private static final Logger LOGGER = Logger
            .getLogger(UserListController.class.getName());

    private String path;
    private UserListModel userListModel = null;

    public UserListController(StandalonePluginWorkspace workspace) {
        path = workspace.getPreferencesDirectory() + File.separator + "glyphpicker_user.xml";
    }

    public void save() {
        File file = new File(path);
        LOGGER.info("Storing user list.");
        try {
            JAXB.marshal(userListModel, file);
        } catch (DataBindingException e) {
            LOGGER.error("Error storing config.", e);
        }
    }

    public void load() {
        File file = new File(path);
        userListModel = null;
        try {
            userListModel = JAXB.unmarshal(file, UserListModel.class);
        } catch (DataBindingException e) {
            LOGGER.error("Error loading config.", e);
        }
        if (userListModel == null) {
            userListModel = new UserListModel();
        }
    }

    public UserListModel getUserListModel() {
        return userListModel;
    }

    public void setUserListModel(UserListModel userListModel) {
        this.userListModel = userListModel;
    }

}
