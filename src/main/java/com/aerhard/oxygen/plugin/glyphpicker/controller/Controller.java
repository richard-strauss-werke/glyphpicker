package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public abstract class Controller implements ControllerEventListener {

    public abstract JComponent getPanel();

    public abstract void loadData();

    public abstract void saveData();

    private List<ControllerEventListener> listeners = new ArrayList<ControllerEventListener>();

    public void addListener(ControllerEventListener toAdd) {
        listeners.add(toAdd);
    }

    public void fireEvent(String type, GlyphDefinition model) {
        for (ControllerEventListener il : listeners) {
            il.eventOccured(type, model);
        }
    }

}
