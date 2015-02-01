package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.util.ResourceBundle;

import ca.odell.glazedlists.gui.TableFormat;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class GlyphTableFormat implements TableFormat<GlyphDefinition> {

    private String glyphLabel;
    private String descriptionLabel;

    public GlyphTableFormat() {
        ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");
        String className = this.getClass().getSimpleName();
        glyphLabel = i18n.getString(className + ".glyphLabel");
        descriptionLabel = i18n.getString(className + ".descriptionLabel");
    }

    public int getColumnCount() {
        return 2;
    }

    public String getColumnName(int column) {
        if (column == 0) {
            return glyphLabel;
        } else if (column == 1) {
            return descriptionLabel;
        }

        throw new IllegalStateException();
    }

    public Object getColumnValue(GlyphDefinition baseObject, int column) {

        if (column == 0 || column == 1) {
            return baseObject;
        }

        throw new IllegalStateException();
    }
}
