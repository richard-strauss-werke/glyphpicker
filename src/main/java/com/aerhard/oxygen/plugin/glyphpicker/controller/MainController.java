package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JList;
import org.apache.log4j.Logger;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import com.aerhard.oxygen.plugin.glyphpicker.controller.browser.BrowserController;
import com.aerhard.oxygen.plugin.glyphpicker.controller.userlist.UserListController;
import com.aerhard.oxygen.plugin.glyphpicker.model.tei.GlyphItem;
import com.aerhard.oxygen.plugin.glyphpicker.view.MainPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.browser.BrowserPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.browser.GlyphTable;
import com.aerhard.oxygen.plugin.glyphpicker.view.userlist.UserListPanel;

public class MainController {

    private static final Logger LOGGER = Logger.getLogger(MainController.class
            .getName());
    private MainPanel mainPanel;
    private UserListPanel userListPanel;
    private JList<GlyphItem> userList;
    private BrowserPanel browserPanel;
    private GlyphTable table;

    private ConfigLoader configLoader;

    private BrowserController browserController;
    private UserListController userListController;

    private List<InsertListener> listeners = new ArrayList<InsertListener>();

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
        browserPanel = browserController.getPanel();
        table = browserPanel.getTable();

        userListController = new UserListController(workspace, properties);
        userListPanel = userListController.getPanel();
        userList = userListPanel.getUserList();

        mainPanel = new MainPanel(browserPanel, userListPanel);

        setListeners();

    }

    public void addListener(InsertListener toAdd) {
        listeners.add(toAdd);
    }

    public void fireInsertGlyph(GlyphItem model) {
        for (InsertListener il : listeners) {
            il.insert(model);
        }
    }

    private void insertGlyphFromUser() {
        int index = userList.getSelectedIndex();
        if (index != -1) {
            fireInsertGlyph(userListController.getListModel().getElementAt(
                    index));
        }
    }

    private void insertGlyphFromBrowser() {
        int row = table.getSelectedRow();
        if (row != -1) {
            GlyphItem selectedModel = browserController.getTableModel()
                    .getModelAt(table.convertRowIndexToModel(row));
            if (selectedModel != null) {
                fireInsertGlyph(selectedModel);
            }
        }
    }

    private void addItemToUserList() {
        int row = table.getSelectedRow();
        if (row != -1) {
            GlyphItem selectedModel = browserController.getTableModel()
                    .getModelAt(table.convertRowIndexToModel(row));
            if (selectedModel != null) {
                GlyphItem ch = new GlyphItem(selectedModel);
                userListController.getListModel().addElement(ch);

                // TODO add change listener
                mainPanel.highlightTabTitle(0);
            }
        }
    }

    private final void setListeners() {

        JButton btn;

        btn = browserPanel.getBtnAdd();
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItemToUserList();
            }
        });

        btn = browserPanel.getBtnInsert();
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertGlyphFromBrowser();
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    insertGlyphFromBrowser();
                }
            }
        });

        btn = userListPanel.getBtnInsert();
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertGlyphFromUser();
            }
        });

        userList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    insertGlyphFromUser();
                }
            }
        });

    }

    public BrowserController getBrowserController() {
        return browserController;
    }

    public UserListController getUserListController() {
        return userListController;
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }

}
