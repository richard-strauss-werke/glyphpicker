package com.aerhard.oxygen.plugin.glyphpicker.controller;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public interface ControllerEventListener {

    void eventOccured(String type, GlyphDefinition model);

}
