package de.badw.strauss.glyphpicker.controller.main;

import java.awt.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Handles component focus in a JTabbedPane
 */
public class TabFocusHandler implements ChangeListener, PropertyChangeListener {

    /**
     * Contains pairs of tab container panels and the corresponding child
     * components which should gain focus.
     */
    private final Map<Component, Component> tabFocus = new HashMap<>();

    /**
     * The tabbed pane.
     */
    private final JTabbedPane tabbedPane;

    /**
     * Instantiates a new TabFocusHandler.
     *
     * @param tabbedPane the tabbed pane
     */
    public TabFocusHandler(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        tabbedPane.addChangeListener(this);
        KeyboardFocusManager focusManager = KeyboardFocusManager
                .getCurrentKeyboardFocusManager();
        focusManager.addPropertyChangeListener("permanentFocusOwner", this);
    }

    /**
     * Sets the focus in tab with the provided index to the provided component.
     *
     * @param index     the index
     * @param component the component
     */
    public void setTabComponentFocus(int index, Component component) {
        tabFocus.put(tabbedPane.getComponentAt(index), component);
    }

    /**
     * Gets the component to be focused in the specified container
     *
     * @param container the container
     * @return the component to be focused
     */
    public Component getFocusComponentForContainer(Component container) {
        return tabFocus.get(container);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent
     * )
     */
    public void stateChanged(ChangeEvent e) {
        Component container = tabbedPane.getComponentAt(tabbedPane
                .getSelectedIndex());
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

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
     * PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent e) {

        Component container = tabbedPane.getComponentAt(tabbedPane
                .getSelectedIndex());

        Component component = (Component) e.getNewValue();

        if (component != null
                && SwingUtilities.isDescendingFrom(component, container)) {
            tabFocus.put(container, component);
        }
    }
}
