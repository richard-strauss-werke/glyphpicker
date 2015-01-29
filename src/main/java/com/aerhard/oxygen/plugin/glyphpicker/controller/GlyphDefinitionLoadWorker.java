package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class GlyphDefinitionLoadWorker extends SwingWorker<List<GlyphDefinition>, Void> {

    private static final Logger LOGGER = Logger
            .getLogger(GlyphDefinitionLoadWorker.class.getName());
    
    private DataSource dataSource;
    
    private List<GlyphDefinition> result = null;
    
    public List<GlyphDefinition> getResult() {
        return result;
    }

    // private JDialog dialog;

    public GlyphDefinitionLoadWorker(DataSource dataSource) {
        this.dataSource = dataSource;
        // dialog = new JDialog();
        // dialog.setLocationRelativeTo(panel);
        // dialog.addWindowListener(new java.awt.event.WindowAdapter() {
        //
        // });
    }

    @Override
    protected List<GlyphDefinition> doInBackground() {
        // dialog.setVisible(true);
        GlyphDefinitionLoader loader = new GlyphDefinitionLoader();
        return loader.loadData(dataSource);
    }

    @Override
    protected void done() {
        try {
            result = get();
        } catch (InterruptedException e) {
            LOGGER.error(e);
        } catch (ExecutionException e) {
            LOGGER.error(e);
        } 
    }
}
