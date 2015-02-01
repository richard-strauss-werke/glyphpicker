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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import com.aerhard.oxygen.plugin.glyphpicker.controller.browser.BrowserController;
import com.aerhard.oxygen.plugin.glyphpicker.controller.user.UserCollectionController;
import com.aerhard.oxygen.plugin.glyphpicker.model.Config;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.view.ContainerPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.ControlPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphGrid;
import com.aerhard.oxygen.plugin.glyphpicker.view.MainPanel;

/**
 * The plugin's main controller.
 */
public class MainController implements PropertyChangeListener {

    /** The logger. */
    private static final Logger LOGGER = Logger.getLogger(MainController.class
            .getName());

    /** The property change support. */
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Adds a property change listener.
     *
     * @param l the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    /**
     * Removes a property change listener.
     *
     * @param l the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    /** The main panel. */
    private final MainPanel mainPanel;

    /** The config loader. */
    private final ConfigLoader configLoader;

    /** The browser controller. */
    private final BrowserController browserController;
    
    /** The user collection controller. */
    private final UserCollectionController userCollectionController;

    /** The browser panel. */
    private final ContainerPanel browserPanel;
    
    /** The user collection panel. */
    private final ContainerPanel userCollectionPanel;

    /**
     * Instantiates a new main controller.
     *
     * @param workspace oXygen's plugin workspace
     */
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

        browserPanel = new ContainerPanel(new ControlPanel(true));
        browserController = new BrowserController(browserPanel, config);
        browserController.addPropertyChangeListener(this);

        userCollectionPanel = new ContainerPanel(new ControlPanel(false));
        userCollectionController = new UserCollectionController(
                userCollectionPanel, config, properties, workspace);
        userCollectionController.addPropertyChangeListener(this);

        mainPanel = new MainPanel(userCollectionPanel, browserPanel);

        mainPanel.getTabbedPane().setSelectedIndex(config.getTabIndex());

        TabFocusHandler focusHandler = new TabFocusHandler(
                mainPanel.getTabbedPane());
        focusHandler.setTabComponentFocus(0, userCollectionPanel
                .getControlPanel().getAutoCompleteCombo());
        focusHandler.setTabComponentFocus(1, browserPanel.getControlPanel()
                .getAutoCompleteCombo());
    }

    /**
     * Gets the config loader.
     *
     * @return the config loader
     */
    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    /**
     * Gets the main panel.
     *
     * @return the main panel
     */
    public MainPanel getPanel() {
        return mainPanel;
    }

    /**
     * Loads glyph definition to user collection and browser tabs.
     */
    public void loadData() {
        userCollectionController.loadData();
        browserController.loadData();
    }

    /**
     * Saves config and user collection data.
     */
    public void saveData() {
        Config config = getConfigLoader().getConfig();
        config.setTabIndex(mainPanel.getTabbedPane().getSelectedIndex());
        config.setBrowserSearchFieldScopeIndex(browserPanel.getControlPanel()
                .getAutoCompleteScopeCombo().getSelectedIndex());
        config.setUserSearchFieldScopeIndex(userCollectionPanel
                .getControlPanel().getAutoCompleteScopeCombo()
                .getSelectedIndex());

        config.setBrowserViewIndex((browserPanel.getListComponent() instanceof GlyphGrid) ? 0
                : 1);
        config.setUserViewIndex((userCollectionPanel.getListComponent() instanceof GlyphGrid) ? 0
                : 1);

        getConfigLoader().save();
        userCollectionController.saveData();
    }

    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if ("insert".equals(e.getPropertyName())) {
            pcs.firePropertyChange(e);
        }

        else if ("copyToUserCollection".equals(e.getPropertyName())) {
            try {
                GlyphDefinition clone = ((GlyphDefinition) e.getNewValue())
                        .clone();
                userCollectionController.addGlyphDefinition(clone);
                mainPanel.highlightTabTitle(0);
            } catch (CloneNotSupportedException e1) {
                LOGGER.error(e1);
            }
        }
    }

}
