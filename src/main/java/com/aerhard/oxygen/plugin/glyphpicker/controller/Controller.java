package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public abstract class Controller implements GlyphEventListener {

    public abstract JComponent getPanel();

    public abstract void loadData();

    public abstract void saveData();

    private List<GlyphEventListener> listeners = new ArrayList<GlyphEventListener>();

    public void addListener(GlyphEventListener toAdd) {
        listeners.add(toAdd);
    }

    public void fireEvent(String type, GlyphDefinition model) {
        for (GlyphEventListener il : listeners) {
            il.eventOccured(type, model);
        }
    }

}
