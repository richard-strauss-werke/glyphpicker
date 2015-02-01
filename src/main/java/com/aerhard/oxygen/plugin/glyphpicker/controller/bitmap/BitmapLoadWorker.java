package com.aerhard.oxygen.plugin.glyphpicker.controller.bitmap;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingWorker;

import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class BitmapLoadWorker extends
        SwingWorker<List<GlyphDefinition>, Void> {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private final List<GlyphDefinition> glyphDefinitions;
    private final int size;

    public BitmapLoadWorker(List<GlyphDefinition> glyphDefinitions,
            int size) {
        this.glyphDefinitions = glyphDefinitions;
        this.size = size;
    }

    public void shutdownExecutor() {
        executorService.shutdownNow();
    }

    @Override
    protected List<GlyphDefinition> doInBackground() {

        for (GlyphDefinition gd : glyphDefinitions) {
            if (isCancelled()) {
                return null;
            }
            if (DataSource.DISPLAY_MODE_BITMAP.equals(gd.getDataSource()
                    .getDisplayMode())) {
                float factor = gd.getDataSource().getSizeFactor();
                executorService.submit(new BitmapLoader(this, gd, Math
                        .round(size * factor)));
            }
        }

        return null;
    }

}
