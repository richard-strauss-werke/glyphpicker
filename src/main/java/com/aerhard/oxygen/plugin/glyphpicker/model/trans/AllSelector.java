package com.aerhard.oxygen.plugin.glyphpicker.model.trans;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class AllSelector implements PropertySelector {

    @Override
    public String get(GlyphDefinition d) {
        return d.getCodePointString() + " " + d.getId() + " " + d.getRange() + " " + d.getCharName();
    }
    
}
