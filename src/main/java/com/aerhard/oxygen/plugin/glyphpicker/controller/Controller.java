package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.util.ArrayList;
import java.util.List;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public abstract class Controller implements ControllerEventListener {

    public abstract void loadData();

    public abstract void saveData();

    private List<ControllerEventListener> listeners = new ArrayList<ControllerEventListener>();

    public void addListener(ControllerEventListener listener) {
        listeners.add(listener);
    }

    public void fireEvent(String type, GlyphDefinition model) {
        for (ControllerEventListener listener : listeners) {
            listener.eventOccured(type, model);
        }
    }

}
