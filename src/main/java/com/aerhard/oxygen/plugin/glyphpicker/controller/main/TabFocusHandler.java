package com.aerhard.oxygen.plugin.glyphpicker.controller.main;

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
    
    // request focus on tab change
    public void stateChanged(ChangeEvent e) {
        Component container = tabbedPane
                .getComponentAt(tabbedPane.getSelectedIndex());
        if (container != null) {
            Component component = tabFocus.get(container);
            if (component == null) {
                container.transferFocus();
                tabFocus.put(container, null);
            } else {
                component.requestFocusInWindow();
            }    
        }
    }

    public void propertyChange(PropertyChangeEvent e) {

        Component container = tabbedPane
                .getComponentAt(tabbedPane.getSelectedIndex());

        Component component = (Component) e.getNewValue();

        if (component != null && SwingUtilities.isDescendingFrom(component, container)) {
            tabFocus.put(container, component);
        }
    }
}
