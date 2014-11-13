package com.aerhard.oxygen.plugin.glyphpicker.controller.userlist;

import java.io.File;
import java.util.Properties;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;

import org.apache.log4j.Logger;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import com.aerhard.oxygen.plugin.glyphpicker.model.tei.UserListModel;

public class UserListLoader {

    private static final Logger LOGGER = Logger.getLogger(UserListLoader.class
            .getName());

    private String pathName;
    private String fileName;
    private UserListModel userListModel = null;

    public UserListLoader(StandalonePluginWorkspace workspace,
            Properties properties) {
        pathName = workspace.getPreferencesDirectory() + "/"
                + properties.getProperty("config.path");
        fileName = properties.getProperty("userdata.filename");
    }

    public void save() {
        File path = new File(pathName);
        Boolean pathExists = (path.exists()) ? true : path.mkdir();
        if (pathExists) {
            File file = new File(path, fileName);
            LOGGER.info("Storing user list.");
            try {
                JAXB.marshal(userListModel, file);
            } catch (DataBindingException e) {
                LOGGER.error("Error storing config.", e);
            }
        } else {
            LOGGER.error("Could not create folder " + pathName);
        }
    }

    public void load() {
        File file = new File(pathName + "/" + fileName);
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