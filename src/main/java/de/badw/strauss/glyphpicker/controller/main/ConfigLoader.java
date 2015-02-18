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
package de.badw.strauss.glyphpicker.controller.main;

import de.badw.strauss.glyphpicker.model.Config;
import de.badw.strauss.glyphpicker.model.DataSourceList;
import org.apache.log4j.Logger;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;
import java.io.File;
import java.net.URL;
import java.util.Properties;

/**
 * Loads and saves the plugin's config.
 */
public class ConfigLoader {

    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ConfigLoader.class
            .getName());

    /**
     * The folder path to the config file.
     */
    private final String pathName;

    /**
     * The name of the config file.
     */
    private final String fileName;

    /**
     * The config object.
     */
    private Config config = null;

    /**
     * Instantiates a new ConfigLoader.
     *
     * @param workspace  oXygen's plugin workspace
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
        if (path.exists() || path.mkdir()) {
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
        File path = new File(pathName);
        File file = new File(path, fileName);

        if (file.exists()) {
            try {
                config = JAXB.unmarshal(file, Config.class);
            } catch (DataBindingException e) {
                LOGGER.error("Error loading config.", e);
            }
        } else {
            try {
                URL resource = ConfigLoader.class
                        .getResource("/config.xml");
                config = JAXB.unmarshal(resource, Config.class);

                setDefaultShortcut();

            } catch (DataBindingException e) {
                LOGGER.error("Error loading default config.", e);
            }
        }

        if (config == null) {
            LOGGER.error("Could not unmarshal config file.");
        } else {
            config.setConfigDir(path);
            initDataSourceList();
        }
    }

    /**
     * Sets the default shortcut whose modifier depends on the OS
     */
    private void setDefaultShortcut() {
        String shortcutModifier = (System.getProperty("os.name")
                .toLowerCase().contains("mac")) ? "meta option" : "ctrl alt";

        config.setShortcut(shortcutModifier + " P");
    }

    /**
     * initializes the data source list in the config object
     */
    private void initDataSourceList() {
        DataSourceList dataSourceList = config.getDataSources();
        if (dataSourceList == null) {
            LOGGER.error("No data source list found in config.");
            dataSourceList = new DataSourceList();
        }
        dataSourceList.init();
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
