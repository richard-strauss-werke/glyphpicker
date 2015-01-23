package com.aerhard.oxygen.plugin.glyphpicker.model.trans;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class RangeSelector implements PropertySelector {

    @Override
    public String get(GlyphDefinition d) {
        return d.getRange();
    }

    
}
