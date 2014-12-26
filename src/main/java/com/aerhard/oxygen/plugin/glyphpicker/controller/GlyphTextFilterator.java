package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.util.List;

import ca.odell.glazedlists.TextFilterator;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class GlyphTextFilterator implements TextFilterator<GlyphDefinition> {

    @Override
    public void getFilterStrings(List<String> baseList,
            GlyphDefinition element) {
        baseList.add(element.getId());
        baseList.add(element.getRange());
        baseList.add(element.getCharName());
        baseList.add(element.getCodePoint());
    }
}