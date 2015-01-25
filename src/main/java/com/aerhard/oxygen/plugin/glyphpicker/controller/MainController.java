package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import com.aerhard.oxygen.plugin.glyphpicker.model.Config;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.view.MainPanel;

public class MainController extends Controller {

    private static final Logger LOGGER = Logger.getLogger(MainController.class
            .getName());
    private MainPanel mainPanel;

    private ConfigLoader configLoader;

    private BrowserController browserController;
    private UserCollectionController userCollectionController;

    public MainController(StandalonePluginWorkspace workspace) {

        Properties properties = new Properties();
        try {
            properties.load(MainController.class
                    .getResourceAsStream("/plugin.properties"));
        } catch (IOException e) {
            LOGGER.error("Could not read \"plugin.properties\".");
        }

        configLoader = new ConfigLoader(workspace, properties);
        configLoader.load();

        Config config = configLoader.getConfig();

        browserController = new BrowserController(config);
        browserController.addListener(this);

        userCollectionController = new UserCollectionController(config,
                properties, workspace);
        userCollectionController.addListener(this);
        addListener(userCollectionController);

        mainPanel = new MainPanel(browserController.getPanel(),
                userCollectionController.getPanel());

        mainPanel.getTabbedPane().setSelectedIndex(config.getTabIndex());

        mainPanel.getTabbedPane().setMnemonicAt(0, KeyEvent.VK_U);
        mainPanel.getTabbedPane().setMnemonicAt(1, KeyEvent.VK_D);
        // mainPanel.getTabbedPane().setDisplayedMnemonicIndexAt(0, 0);

        new TabFocusHandler(mainPanel.getTabbedPane());

    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    @Override
    public MainPanel getPanel() {
        return mainPanel;
    }

    @Override
    public void eventOccured(String type, GlyphDefinition glyphDefinition) {

        if ("insert".equals(type)) {
            fireEvent("insert", glyphDefinition);
        }

        else if ("copyToUserCollection".equals(type)) {
            try {
                GlyphDefinition clone = glyphDefinition.clone();
                fireEvent("copyToUserCollection", clone);
                mainPanel.highlightTabTitle(0);
            } catch (CloneNotSupportedException e) {
                LOGGER.error(e);
            }
        }

    }

    @Override
    public void loadData() {
        userCollectionController.loadData();
        browserController.loadData();
    }

    public void saveData() {
        Config config = getConfigLoader().getConfig();
        config.setTabIndex(mainPanel.getTabbedPane().getSelectedIndex());
        config.setBrowserSearchFieldScopeIndex(browserController
                .getControlPanel().getAutoCompleteScopeCombo()
                .getSelectedIndex());
        config.setUserSearchFieldScopeIndex(userCollectionController
                .getControlPanel().getAutoCompleteScopeCombo()
                .getSelectedIndex());

        getConfigLoader().save();
        userCollectionController.saveData();
    }

}
