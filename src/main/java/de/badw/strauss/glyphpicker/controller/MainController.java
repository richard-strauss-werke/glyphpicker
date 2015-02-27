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
package de.badw.strauss.glyphpicker.controller;

import de.badw.strauss.glyphpicker.controller.action.AbstractPickerAction;
import de.badw.strauss.glyphpicker.controller.action.InsertXmlAction;
import de.badw.strauss.glyphpicker.controller.action.MemorizeAction;
import de.badw.strauss.glyphpicker.controller.alltab.AllTabController;
import de.badw.strauss.glyphpicker.controller.bitmap.ImageCache;
import de.badw.strauss.glyphpicker.controller.memtab.MemorizedTabController;
import de.badw.strauss.glyphpicker.controller.settings.SettingsDialogAction;
import de.badw.strauss.glyphpicker.controller.tab.AbstractTabController;
import de.badw.strauss.glyphpicker.controller.tab.TabFocusHandler;
import de.badw.strauss.glyphpicker.model.Config;
import de.badw.strauss.glyphpicker.model.GlyphDefinition;
import de.badw.strauss.glyphpicker.view.ControlPanel;
import de.badw.strauss.glyphpicker.view.GlyphGrid;
import de.badw.strauss.glyphpicker.view.MainPanel;
import de.badw.strauss.glyphpicker.view.TabPanel;
import org.apache.log4j.Logger;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.Properties;

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
     * The allTab controller.
     */
    private final AllTabController allTabController;
    /**
     * The memorized tab controller.
     */
    private final MemorizedTabController memorizedTabController;
    /**
     * The allTab panel.
     */
    private final TabPanel allTabPanel;
    /**
     * The memorized tab panel.
     */
    private final TabPanel memorizedTabPanel;

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
        } catch (Exception e) {
            LOGGER.error("Could not read \"plugin.properties\".");
        }

        configLoader = new ConfigLoader(workspace, properties);
        configLoader.load();

        config = configLoader.getConfig();

        ImageCache imageCache = createImageCache(config);

        allTabPanel = new TabPanel(new ControlPanel(true));
        allTabController = new AllTabController(allTabPanel, config, imageCache);
        allTabController.addPropertyChangeListener(this);

        memorizedTabPanel = new TabPanel(new ControlPanel(false));
        memorizedTabController = new MemorizedTabController(
                memorizedTabPanel, config, properties, workspace, imageCache);
        memorizedTabController.addPropertyChangeListener(this);

        mainPanel = new MainPanel(memorizedTabPanel, allTabPanel,
                AbstractPickerAction.MENU_SHORTCUT_NAME);

        double maximumToolbarPreferredWidth = Math.max(
                memorizedTabPanel.getControlPanel().getToolbarPreferredWidth(),
                allTabPanel.getControlPanel().getToolbarPreferredWidth());
        memorizedTabPanel.getControlPanel().setToolBarWidthThreshold(maximumToolbarPreferredWidth);
        allTabPanel.getControlPanel().setToolBarWidthThreshold(maximumToolbarPreferredWidth);


        SettingsDialogAction settingsDialogAction = new SettingsDialogAction(mainPanel,
                allTabController, config, imageCache, allTabController.getDataSourceList());
        allTabPanel.getControlPanel().getOptionsBtn().setAction(settingsDialogAction);
        memorizedTabPanel.getControlPanel().getOptionsBtn().setAction(settingsDialogAction);


        final JTabbedPane tabbedPane = mainPanel.getTabbedPane();
        tabbedPane.setSelectedIndex(config.getTabIndex());

        final TabFocusHandler focusHandler = new TabFocusHandler(tabbedPane);
        focusHandler.setTabComponentFocus(0, memorizedTabPanel
                .getControlPanel().getAutoCompleteCombo());
        focusHandler.setTabComponentFocus(1, allTabPanel.getControlPanel()
                .getAutoCompleteCombo());

        mainPanel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                Component container = tabbedPane.getComponentAt(tabbedPane
                        .getSelectedIndex());
                Component component = focusHandler.getFocusComponentForContainer(container);
                if (component != null) {
                    component.requestFocusInWindow();
                }
            }
        });

        setTabAccelerator(tabbedPane, 0);
        setTabAccelerator(tabbedPane, 1);
    }

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
     * Sets the accelerator ctrl key + tab index / command key + tab index for a specified index in a JTabbedPane
     *
     * @param tabbedPane  the tabbed pane
     * @param actualIndex the actual index, starting with 0
     */
    private void setTabAccelerator(final JTabbedPane tabbedPane, final int actualIndex) {
        int index = actualIndex + 1;
        String actionKey = "select_tab_" + index;
        String modifier = (AbstractPickerAction.IS_MAC) ? "meta" : "ctrl";
        mainPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(modifier + " " + index),
                actionKey);
        mainPanel.getActionMap().put(actionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane.setSelectedIndex(actualIndex);
            }
        });
    }

    /**
     * Creates the image cache folder and the corresponding ImageCache object
     *
     * @param config the plugin config
     * @return the image cache or null if no cache folder could be created
     */
    private ImageCache createImageCache(Config config) {
        File cacheFolder = new File(config.getConfigDir(), "cache");
        if ((cacheFolder.exists() && cacheFolder.isDirectory()) || cacheFolder.mkdirs()) {
            return new ImageCache(cacheFolder);
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
     * Loads glyph definition to memorized tab and allTab tabs.
     */
    public void loadData() {
        memorizedTabController.loadData();
        allTabController.loadData();
    }

    /**
     * Saves config and memorized tab data.
     */
    public void saveData() {
        config.setTabIndex(mainPanel.getTabbedPane().getSelectedIndex());
        config.setAllTabSearchFieldScopeIndex(allTabPanel.getControlPanel()
                .getAutoCompleteScopeCombo().getSelectedIndex());
        config.setUserSearchFieldScopeIndex(memorizedTabPanel
                .getControlPanel().getAutoCompleteScopeCombo()
                .getSelectedIndex());

        config.setAllTabViewIndex((allTabPanel.getListComponent() instanceof GlyphGrid) ? 0
                : 1);
        config.setUserViewIndex((memorizedTabPanel.getListComponent() instanceof GlyphGrid) ? 0
                : 1);

        configLoader.save();
        memorizedTabController.saveData();
    }

    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (InsertXmlAction.KEY.equals(e.getPropertyName())) {
            pcs.firePropertyChange(e);
        } else if (MemorizeAction.KEY.equals(e.getPropertyName())) {
            try {
                GlyphDefinition clone = ((GlyphDefinition) e.getNewValue())
                        .clone();
                memorizedTabController.addGlyphDefinition(clone);
                mainPanel.highlightTabTitle(0);
            } catch (CloneNotSupportedException e1) {
                LOGGER.error(e1);
            }
        } else if (AbstractTabController.DATA_LOADED.equals(e.getPropertyName())) {

            int selectedIndex = mainPanel.getTabbedPane().getSelectedIndex();

            if (e.getNewValue() instanceof AllTabController && selectedIndex == 1) {
                allTabPanel.getControlPanel().getAutoCompleteCombo().requestFocusInWindow();
            } else if (e.getNewValue() instanceof MemorizedTabController && selectedIndex == 0) {
                memorizedTabPanel.getControlPanel().getAutoCompleteCombo().requestFocusInWindow();
            }

        }
    }

}
