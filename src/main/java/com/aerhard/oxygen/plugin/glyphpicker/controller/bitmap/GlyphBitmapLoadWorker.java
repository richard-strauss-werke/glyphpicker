package com.aerhard.oxygen.plugin.glyphpicker.controller.bitmap;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingWorker;

import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class GlyphBitmapLoadWorker extends
        SwingWorker<List<GlyphDefinition>, Void> {

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private List<GlyphDefinition> glyphDefinitions;
    private int size;

    public GlyphBitmapLoadWorker(List<GlyphDefinition> glyphDefinitions,
            int size) {
        this.glyphDefinitions = glyphDefinitions;
        this.size = size;
    }

    public void shutdownExecutor() {
       executorService.shutdownNow();
    }
    
    @Override
    protected List<GlyphDefinition> doInBackground() throws Exception {

        for (GlyphDefinition gd : glyphDefinitions) {
            if (isCancelled()) {
                return null;
            }
            if (DataSource.DISPLAY_MODE_BITMAP.equals(gd.getDataSource()
                    .getDisplayMode())) {
                float factor = gd.getDataSource().getSizeFactor();
                executorService.submit(new GlyphBitmapLoader(this, gd, Math.round(size
                        * factor)));
            }
        }

        return null;
    }

}
