package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.io.Serializable;
import java.util.Comparator;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class CodePointComparator implements Comparator<GlyphDefinition>,
        Serializable {
    private static final long serialVersionUID = 1L;

    public int compare(GlyphDefinition glyphA, GlyphDefinition glyphB) {

        String aString = glyphA.getCodePoint();
        String bString = glyphB.getCodePoint();

        return (aString != null && bString != null) ? aString
                .compareToIgnoreCase(bString) : -1;
    }
}