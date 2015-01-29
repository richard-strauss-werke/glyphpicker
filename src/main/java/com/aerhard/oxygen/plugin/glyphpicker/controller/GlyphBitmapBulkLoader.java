package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.util.List;

import javax.swing.SwingWorker;

import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class GlyphBitmapBulkLoader extends
        SwingWorker<List<GlyphDefinition>, Void> {

    private List<GlyphDefinition> glyphDefinitions;
    private int size;

    public GlyphBitmapBulkLoader(List<GlyphDefinition> glyphDefinitions,
            int size) {
        this.glyphDefinitions = glyphDefinitions;
        this.size = size;
    }

    @Override
    protected List<GlyphDefinition> doInBackground() throws Exception {

        for (GlyphDefinition gd : glyphDefinitions) {
            if (DataSource.DISPLAY_MODE_BITMAP.equals(gd.getDataSource()
                    .getDisplayMode())) {
                float factor = gd.getDataSource().getSizeFactor();
                new GlyphBitmapIconLoader(this, gd, Math.round(size
                        * factor)).execute();
            } else if (isCancelled()) {
                return null;
            }
        }

        return null;
    }

}
