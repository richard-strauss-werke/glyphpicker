package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;

import org.apache.log4j.Logger;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import com.aerhard.oxygen.plugin.glyphpicker.model.Config;
import com.aerhard.oxygen.plugin.glyphpicker.model.PathComboModel;

public class ConfigController {

    private static final Logger LOGGER = Logger
            .getLogger(ConfigController.class.getName());

    private String pathName;
    private String fileName;
    private Config config = null;

    private Properties properties;
    
    public ConfigController(StandalonePluginWorkspace workspace, Properties properties) {
        this.properties = properties;
        pathName = workspace.getPreferencesDirectory() + "/" + properties.getProperty("config.path");
        fileName = properties.getProperty("config.filename");
    }

    public void save() {
        File path = new File(pathName);
        path.mkdir();
        File file = new File(path, fileName);
        LOGGER.info("Storing config.");
        try {
            JAXB.marshal(config, file);
        } catch (DataBindingException e) {
            LOGGER.error("Error storing config.", e);
        }
    }

    public void load() {
        File file = new File(pathName + "/" + fileName);
        config = null;
        try {
            config = JAXB.unmarshal(file, Config.class);
        } catch (DataBindingException e) {
            LOGGER.error("Error loading config.", e);
        }
        if (config == null) {
            config = new Config();
        }
        PathComboModel pathComboModel = config.getPaths();
        if (pathComboModel == null || pathComboModel.getSize() == 0) {
            pathComboModel = getDefaultPaths();
            config.setPaths(pathComboModel);
        }
        pathComboModel.init();
    }

    private PathComboModel getDefaultPaths() {
        String[] pathArray;
        try {
            String paths = properties.getProperty("config.defaultpaths");
            pathArray = paths.split(",");
        } catch (Exception e) {
            LOGGER.error("Could not read config.defaultpaths");
            pathArray = new String[]{""};
        }
        return new PathComboModel(new ArrayList<String>(
                Arrays.asList(pathArray)));
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

}
