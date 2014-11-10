package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;

import org.apache.log4j.Logger;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import com.aerhard.oxygen.plugin.glyphpicker.model.Config;
import com.aerhard.oxygen.plugin.glyphpicker.model.PathComboModel;

public class ConfigController {

    private static final Logger LOGGER = Logger
            .getLogger(ConfigController.class.getName());

    private String path;
    private Config config = null;
    
    public ConfigController(StandalonePluginWorkspace workspace) {
        path = workspace.getPreferencesDirectory() + File.separator + "glyphpicker_config.xml";
    }

    public void save() {
        File file = new File(path);
        LOGGER.info("Storing config.");
        try {
            JAXB.marshal(config, file);
        } catch (DataBindingException e) {
            LOGGER.error("Error storing config.", e);
        }
    }

    public void load() {
        File file = new File(path);
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
        return new PathComboModel(
                new ArrayList<String>(
                        Arrays.asList(new String[] {
                                "/Users/Ahlse/Desktop/SMuFL-Browser-1.0/data/charDecl.xml",
                                "/Users/Ahlse/Desktop/gBankImages/gBank.xml",
                                "/Users/Ahlse/Desktop/SMuFL-Browser-1.0/data/smufl.json",
                                "http://localhost:8080/exist/apps/smufl-browser/list/",
                                "http://localhost:8080/exist/apps/smufl-browser/data/charDecl.xml" })));
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

}
