package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class TabFocusHandler implements ChangeListener, PropertyChangeListener {

    private Map<Component, Component> tabFocus = new HashMap<Component, Component>();
    private JTabbedPane tabbedPane;

    public TabFocusHandler(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        tabbedPane.addChangeListener(this);
        KeyboardFocusManager focusManager = KeyboardFocusManager
                .getCurrentKeyboardFocusManager();
        focusManager.addPropertyChangeListener("permanentFocusOwner", this);
    }

    public void stateChanged(ChangeEvent e) {
        Component key = tabbedPane
                .getComponentAt(tabbedPane.getSelectedIndex());
        if (key != null) {
            Component value = tabFocus.get(key);
            if (value == null) {
                key.transferFocus();
                tabFocus.put(key, value);
            } else {
                value.requestFocusInWindow();
            }    
        }
    }

    public void propertyChange(PropertyChangeEvent e) {

        Component key = tabbedPane
                .getComponentAt(tabbedPane.getSelectedIndex());

        Component value = (Component) e.getNewValue();

        if (value != null && SwingUtilities.isDescendingFrom(value, key)) {
            tabFocus.put(key, value);
        }
    }
}
