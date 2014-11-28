package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.io.File;
import java.util.Properties;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;

import org.apache.log4j.Logger;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinitions;

public class UserListLoader {

    private static final Logger LOGGER = Logger.getLogger(UserListLoader.class
            .getName());

    private String pathName;
    private String fileName;

    public UserListLoader(StandalonePluginWorkspace workspace,
            Properties properties) {
        pathName = workspace.getPreferencesDirectory() + "/"
                + properties.getProperty("config.path");
        fileName = properties.getProperty("userdata.filename");
    }

    public void save(GlyphDefinitions glyphDefinitions) {
        File path = new File(pathName);
        Boolean pathExists = (path.exists()) ? true : path.mkdir();
        if (pathExists) {
            File file = new File(path, fileName);
            LOGGER.info("Storing user list.");
            try {
                JAXB.marshal(glyphDefinitions, file);
            } catch (DataBindingException e) {
                LOGGER.error("Error storing config.", e);
            }
        } else {
            LOGGER.error("Could not create folder " + pathName);
        }
    }

    public GlyphDefinitions load() {
        File file = new File(pathName + "/" + fileName);
        GlyphDefinitions glyphDefinitions = null;
        try {
            glyphDefinitions = JAXB.unmarshal(file, GlyphDefinitions.class);
        } catch (DataBindingException e) {
            LOGGER.error("Error loading config.", e);
        }
        if (glyphDefinitions == null) {
            glyphDefinitions = new GlyphDefinitions();
        }
        return glyphDefinitions;
    }

}
