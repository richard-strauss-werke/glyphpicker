package com.aerhard.oxygen.plugin.glyphpicker.controller.main;

import java.awt.event.KeyEvent;
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
import com.aerhard.oxygen.plugin.glyphpicker.view.MainPanel;

public class MainController implements PropertyChangeListener {

    private static final Logger LOGGER = Logger.getLogger(MainController.class
            .getName());

    protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }
    
    private MainPanel mainPanel;

    private ConfigLoader configLoader;

    private BrowserController browserController;
    private UserCollectionController userCollectionController;
    
    private ContainerPanel browserPanel;
    private ContainerPanel userCollectionPanel;

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
        userCollectionController = new UserCollectionController(userCollectionPanel, config,
                properties, workspace);
        userCollectionController.addPropertyChangeListener(this);

        mainPanel = new MainPanel(userCollectionPanel, browserPanel);

        mainPanel.getTabbedPane().setSelectedIndex(config.getTabIndex());

        mainPanel.getTabbedPane().setMnemonicAt(0, KeyEvent.VK_U);
        mainPanel.getTabbedPane().setMnemonicAt(1, KeyEvent.VK_D);
        // mainPanel.getTabbedPane().setDisplayedMnemonicIndexAt(0, 0);

        new TabFocusHandler(mainPanel.getTabbedPane());

    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    public MainPanel getPanel() {
        return mainPanel;
    }

    public void loadData() {
        userCollectionController.loadData();
        browserController.loadData();
    }

    public void saveData() {
        Config config = getConfigLoader().getConfig();
        config.setTabIndex(mainPanel.getTabbedPane().getSelectedIndex());
        config.setBrowserSearchFieldScopeIndex(browserPanel
                .getControlPanel().getAutoCompleteScopeCombo()
                .getSelectedIndex());
        config.setUserSearchFieldScopeIndex(userCollectionPanel
                .getControlPanel().getAutoCompleteScopeCombo()
                .getSelectedIndex());

        getConfigLoader().save();
        userCollectionController.saveData();
    }

    
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if ("insert".equals(e.getPropertyName())) {
            pcs.firePropertyChange(e);
        }

        else if ("copyToUserCollection".equals(e.getPropertyName())) {
            try {
                GlyphDefinition clone = ((GlyphDefinition) e.getNewValue()).clone();
                userCollectionController.addGlyphDefinition(clone);
                mainPanel.highlightTabTitle(0);
            } catch (CloneNotSupportedException e1) {
                LOGGER.error(e1);
            }
        }
    }

}
