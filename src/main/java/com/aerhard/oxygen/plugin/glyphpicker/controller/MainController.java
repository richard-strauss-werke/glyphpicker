package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.view.MainPanel;

public class MainController extends Controller {

    private static final Logger LOGGER = Logger.getLogger(MainController.class
            .getName());
    private MainPanel mainPanel;

    private ConfigLoader configLoader;

    private Controller browserController;
    private Controller userCollectionController;

    public MainController(StandalonePluginWorkspace workspace) {

        Properties properties = new Properties();
        try {
            properties.load(ConfigLoader.class
                    .getResourceAsStream("/plugin.properties"));
        } catch (IOException e) {
            LOGGER.error("Could not read \"plugin.properties\".");
        }

        configLoader = new ConfigLoader(workspace, properties);
        configLoader.load();

        browserController = new BrowserController(configLoader.getConfig());
        browserController.addListener(this);

        userCollectionController = new UserCollectionController(workspace,
                properties);
        userCollectionController.addListener(this);
        addListener(userCollectionController);

        mainPanel = new MainPanel(browserController.getPanel(),
                userCollectionController.getPanel());

        mainPanel.getTabbedPane().setSelectedIndex(1);
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    @Override
    public MainPanel getPanel() {
        return mainPanel;
    }

    @Override
    public void eventOccured(String type, GlyphDefinition model) {

        if ("insert".equals(type)) {
            fireEvent("insert", model);
        }

        else if ("copyToUserCollection".equals(type)) {
            GlyphDefinition clone = new GlyphDefinition(model);
            fireEvent("copyToUserCollection", clone);
            mainPanel.highlightTabTitle(0);
        }

    }

    @Override
    public void loadData() {
        userCollectionController.loadData();
        browserController.loadData();
    }

    public void saveData() {
        // TODO check + ask when data has changed
        getConfigLoader().save();
        userCollectionController.saveData();
    }

}
