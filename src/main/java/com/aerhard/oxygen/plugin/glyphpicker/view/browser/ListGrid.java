package com.aerhard.oxygen.plugin.glyphpicker.view.browser;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JList;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class ListGrid extends JList<GlyphDefinition> {

    private static final long serialVersionUID = 1L;

    public ListGrid() {
        setLayoutOrientation(JList.HORIZONTAL_WRAP);
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
