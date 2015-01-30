package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class Controller implements PropertyChangeListener {

    protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

}
