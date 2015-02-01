/**
 * Copyright 2015 Alexander Erhard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

/**
 * Loads and saves the plugin's config.
 */
public class ConfigLoader {

    /** The logger. */
    private static final Logger LOGGER = Logger.getLogger(ConfigLoader.class
            .getName());

    /** The folder path to the config file. */
    private final String pathName;
    
    /** The name of the config file. */
    private final String fileName;
    
    /** The config object. */
    private Config config = null;

    /**
     * Instantiates a new ConfigLoader.
     *
     * @param workspace oXygen's plugin workspace
     * @param properties the plugin's properties file
     */
    public ConfigLoader(StandalonePluginWorkspace workspace,
            Properties properties) {
        pathName = workspace.getPreferencesDirectory() + "/"
                + properties.getProperty("config.path");
        fileName = properties.getProperty("config.filename");
    }

    /**
     * Saves the config.
     */
    public void save() {
        File path = new File(pathName);
        Boolean pathExists = path.exists() || path.mkdir();
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

    /**
     * Loads the config.
     */
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
                dataSourceList = new DataSourceList();
            }

            dataSourceList.init();
        }

    }

    /**
     * Gets the config.
     *
     * @return the config
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Sets the config.
     *
     * @param config the new config
     */
    public void setConfig(Config config) {
        this.config = config;
    }

}
