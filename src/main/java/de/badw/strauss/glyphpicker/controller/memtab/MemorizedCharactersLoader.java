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
package de.badw.strauss.glyphpicker.controller.memtab;

import de.badw.strauss.glyphpicker.model.GlyphDefinitions;
import org.apache.log4j.Logger;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import javax.swing.*;
import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;
import java.io.File;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Loads and saves the memorized tab data.
 */
public class MemorizedCharactersLoader {

    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger
            .getLogger(MemorizedCharactersLoader.class.getName());

    /**
     * The folder path to the memorized tab data file.
     */
    private final String pathName;

    /**
     * The name of the memorized tab data file.
     */
    private final String fileName;

    /**
     * The i18n resource bundle.
     */
    private final ResourceBundle i18n;

    /**
     * The plugin properties
     */
    private final Properties properties;

    /**
     * Instantiates a new MemorizedCharactersLoader.
     *
     * @param workspace  oXygen's plugin workspace
     * @param properties the plugin's properties
     */
    public MemorizedCharactersLoader(StandalonePluginWorkspace workspace,
                                     Properties properties) {

        this.properties = properties;

        i18n = ResourceBundle.getBundle("GlyphPicker");

        pathName = workspace.getPreferencesDirectory() + "/"
                + properties.getProperty("config.path");
        fileName = properties.getProperty("userdata.filename");
    }

    /**
     * Saves the memorized tab.
     *
     * @param glyphDefinitions the glyph definitions
     */
    public void save(GlyphDefinitions glyphDefinitions) {
        File path = new File(pathName);
        glyphDefinitions.setVersion(properties.getProperty("config.version"));
        Boolean pathExists = path.exists() || path.mkdir();
        if (pathExists) {
            File file = new File(path, fileName);
            LOGGER.info("Storing user list.");
            try {
                JAXB.marshal(glyphDefinitions, file);
            } catch (DataBindingException e) {
                LOGGER.error("Error storing config.", e);
            }
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    String.format(
                            i18n.getString(this.getClass().getSimpleName()
                                    + ".couldNotCreateFolder"), pathName),
                    i18n.getString(this.getClass().getSimpleName()
                            + ".storeError"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads the memorized tab.
     *
     * @return the glyph definitions
     */
    public GlyphDefinitions load() {
        File file = new File(pathName + "/" + fileName);
        GlyphDefinitions glyphDefinitions = null;

        if (file.exists()) {
            try {
                glyphDefinitions = JAXB.unmarshal(file, GlyphDefinitions.class);
            } catch (DataBindingException e) {
                LOGGER.error("Error unmarshalling user data.", e);
            }
        }

        if (glyphDefinitions == null) {
            glyphDefinitions = new GlyphDefinitions();
        }
        return glyphDefinitions;
    }

}
