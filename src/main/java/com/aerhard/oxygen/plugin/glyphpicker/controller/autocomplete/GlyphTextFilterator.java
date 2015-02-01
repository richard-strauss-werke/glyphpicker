package com.aerhard.oxygen.plugin.glyphpicker.controller.autocomplete;

import java.util.List;

import ca.odell.glazedlists.TextFilterator;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.model.trans.PropertySelector;

public class GlyphTextFilterator implements TextFilterator<GlyphDefinition> {

    private PropertySelector propertySelector;

    public GlyphTextFilterator(PropertySelector propertySelector) {
        this.propertySelector = propertySelector;
    }

    @Override
    public void getFilterStrings(List<String> baseList, GlyphDefinition element) {
        baseList.add(propertySelector.get(element));
    }
}