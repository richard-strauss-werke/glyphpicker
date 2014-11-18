package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphGridModel;

public class GlyphGrid extends JList<GlyphDefinition> {

    private static final long serialVersionUID = 1L;

    public GlyphGrid(GlyphGridModel listGridModel) {
        setLayoutOrientation(JList.HORIZONTAL_WRAP);
        
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        setModel(listGridModel);
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                fixRowCountForVisibleColumns();
            }
        });
    }

    private void fixRowCountForVisibleColumns() {
        int nCols = computeVisibleColumnCount();
        int nItems = getModel().getSize();
        int nRows = nItems / nCols;
        if (nItems % nCols > 0) {
            nRows++;
        }
        setVisibleRowCount(nRows);
    }

    private int computeVisibleColumnCount() {
        int cellWidth = Math.max(getCellBounds(0, 0).width, 1);
        int width = getVisibleRect().width;
        return Math.max(width / cellWidth, 1);
    }

}
