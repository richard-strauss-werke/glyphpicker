package com.aerhard.oxygen.plugin.glyphpicker.controller.main;

import java.io.File;
import java.net.URL;
import java.util.Properties;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;

import org.apache.log4j.Logger;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import com.aerhard.oxygen.plugin.glyphpicker.controller.user.UserCollectionLoader;
import com.aerhard.oxygen.plugin.glyphpicker.model.Config;
import com.aerhard.oxygen.plugin.glyphpicker.model.DataSourceList;

public class ConfigLoader {

    private static final Logger LOGGER = Logger.getLogger(ConfigLoader.class
            .getName());

    private String pathName;
    private String fileName;
    private Config config = null;

    public ConfigLoader(StandalonePluginWorkspace workspace,
            Properties properties) {
        pathName = workspace.getPreferencesDirectory() + "/"
                + properties.getProperty("config.path");
        fileName = properties.getProperty("config.filename");
    }

    public void save() {
        File path = new File(pathName);
        Boolean pathExists = (path.exists()) ? true : path.mkdir();
        if (pathExists) {
            File file = new File(path, fileName);
            LOGGER.info("Storing config.");
            try {
                JAXB.marshal(config, file);
            } catch (DataBindingException e) {
                LOGGER.error("Error storing config.", e);
            }
        } else {
            LOGGER.error("Could not create folder " + pathName);
        }
    }

    public void load() {
        config = null;

        File file = new File(pathName + "/" + fileName);

        if (file.exists()) {
            try {
                config = JAXB.unmarshal(file, Config.class);
            } catch (DataBindingException e) {
                LOGGER.error("Error loading config.", e);
            }
        } else {
            try {
                URL resource = UserCollectionLoader.class
                        .getResource("/config.xml");
                config = JAXB.unmarshal(resource, Config.class);
            } catch (DataBindingException e) {
                LOGGER.error("Error loading config.", e);
            }
        }

        if (config == null) {
            LOGGER.error("Could not unmarshal config file.");
        } else {
            DataSourceList dataSourceList = config.getDataSources();

            if (dataSourceList == null) {
                LOGGER.error("No data source list found in config.");
            }

            dataSourceList.init();
        }

    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

}
