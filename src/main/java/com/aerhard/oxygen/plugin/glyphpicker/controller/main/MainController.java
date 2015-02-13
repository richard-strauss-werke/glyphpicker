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

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.aerhard.oxygen.plugin.glyphpicker.controller.TabController;
import com.aerhard.oxygen.plugin.glyphpicker.controller.action.CopyAction;
import com.aerhard.oxygen.plugin.glyphpicker.controller.action.InsertXmlAction;
import com.aerhard.oxygen.plugin.glyphpicker.controller.bitmap.ImageCacheAccess;
import org.apache.log4j.Logger;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import com.aerhard.oxygen.plugin.glyphpicker.controller.browser.BrowserController;
import com.aerhard.oxygen.plugin.glyphpicker.controller.user.UserCollectionController;
import com.aerhard.oxygen.plugin.glyphpicker.model.Config;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.view.TabPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.ControlPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphGrid;
import com.aerhard.oxygen.plugin.glyphpicker.view.MainPanel;

import javax.swing.*;

/**
 * The plugin's main controller.
 */
public class MainController implements PropertyChangeListener {

    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(MainController.class
            .getName());

    /**
     * The property change support.
     */
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

    /**
     * The plugin's config
     */
    private final Config config;

    /**
     * The main panel.
     */
    private final MainPanel mainPanel;

    /**
     * The config loader.
     */
    private final ConfigLoader configLoader;

    /**
     * The browser controller.
     */
    private final BrowserController browserController;

    /**
     * The user collection controller.
     */
    private final UserCollectionController userCollectionController;

    /**
     * The browser panel.
     */
    private final TabPanel browserPanel;

    /**
     * The user collection panel.
     */
    private final TabPanel userCollectionPanel;

    /**
     * Instantiates a new main controller.
     *
     * @param workspace oXygen's plugin workspace
     */
    public MainController(final StandalonePluginWorkspace workspace) {

        Properties properties = new Properties();
        try {
            properties.load(MainController.class
                    .getResourceAsStream("/plugin.properties"));
        } catch (IOException e) {
            LOGGER.error("Could not read \"plugin.properties\".");
        }

        configLoader = new ConfigLoader(workspace, properties);
        configLoader.load();

        config = configLoader.getConfig();

        ImageCacheAccess imageCacheAccess = createImageCache(config);

        browserPanel = new TabPanel(new ControlPanel(true));
        browserController = new BrowserController(browserPanel, config, imageCacheAccess);
        browserController.addPropertyChangeListener(this);

        userCollectionPanel = new TabPanel(new ControlPanel(false));
        userCollectionController = new UserCollectionController(
                userCollectionPanel, config, properties, workspace, imageCacheAccess);
        userCollectionController.addPropertyChangeListener(this);

        mainPanel = new MainPanel(userCollectionPanel, browserPanel);

        mainPanel.getTabbedPane().setSelectedIndex(config.getTabIndex());

        final TabFocusHandler focusHandler = new TabFocusHandler(
                mainPanel.getTabbedPane());
        focusHandler.setTabComponentFocus(0, userCollectionPanel
                .getControlPanel().getAutoCompleteCombo());
        focusHandler.setTabComponentFocus(1, browserPanel.getControlPanel()
                .getAutoCompleteCombo());

        mainPanel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JTabbedPane tabbedPane = mainPanel.getTabbedPane();
                Component container = tabbedPane.getComponentAt(tabbedPane
                        .getSelectedIndex());
                Component component = focusHandler.getFocusComponentForContainer(container);
                if (component != null) {
                    component.requestFocusInWindow();
                }
            }
        });

    }

    /**
     * Creates the image cache folder and the corresponding ImageCache object
     * @param config the plugin config
     * @return the image cache or null if no cache folder could be created
     */
    private ImageCacheAccess createImageCache(Config config) {
        File cacheFolder = new File(config.getConfigDir(), "cache");
        if ((cacheFolder.exists() && cacheFolder.isDirectory()) || cacheFolder.mkdirs()) {
            return new ImageCacheAccess(cacheFolder);
        }
        LOGGER.error(String.format("Could not create image cache folder at %s", cacheFolder.toString()));
        return null;
    }

    /**
     * Gets the plugin's config.
     *
     * @return the plugin's config
     */
    public Config getConfig() {
        return config;
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

        configLoader.save();
        userCollectionController.saveData();
    }

    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (InsertXmlAction.KEY.equals(e.getPropertyName())) {
            pcs.firePropertyChange(e);
        } else if (CopyAction.KEY.equals(e.getPropertyName())) {
            try {
                GlyphDefinition clone = ((GlyphDefinition) e.getNewValue())
                        .clone();
                userCollectionController.addGlyphDefinition(clone);
                mainPanel.highlightTabTitle(0);
            } catch (CloneNotSupportedException e1) {
                LOGGER.error(e1);
            }
        } else if (TabController.DATA_LOADED.equals(e.getPropertyName())) {

            int selectedIndex = mainPanel.getTabbedPane().getSelectedIndex();

            if (e.getNewValue() instanceof BrowserController && selectedIndex == 1) {
                browserPanel.getControlPanel().getAutoCompleteCombo().requestFocusInWindow();
            } else if (e.getNewValue() instanceof UserCollectionController && selectedIndex == 0) {
                userCollectionPanel.getControlPanel().getAutoCompleteCombo().requestFocusInWindow();
            }

        }
    }

}
