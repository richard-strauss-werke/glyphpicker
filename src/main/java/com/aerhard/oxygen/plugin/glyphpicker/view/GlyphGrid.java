package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JList;
import javax.swing.JViewport;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class GlyphGrid extends JList<GlyphDefinition> {

    private static final long serialVersionUID = 1L;

    public GlyphGrid(ListModel<GlyphDefinition> filteredListModel) {
        setLayoutOrientation(JList.HORIZONTAL_WRAP);

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setModel(filteredListModel);

        addComponentListener(new ResizeAdapter());
    }

    private class ResizeAdapter extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {
            fixRowCountForVisibleColumns();
        }        
    }
    
    
    public GlyphGrid() {
        setLayoutOrientation(JList.HORIZONTAL_WRAP);

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                fixRowCountForVisibleColumns();
            }
        });
    }
    
    public int getTopVisibleRow() {
        JViewport viewport = (JViewport) getParent();
        Point pt = viewport.getViewPosition();
        return locationToIndex(pt);
    }
    
    public void setTopVisibleRow(int row) {
        Rectangle cellBounds = getCellBounds(row, row);
        int h = getVisibleRect().height;
        Rectangle targetViewRect = new Rectangle(cellBounds.x - h
                + cellBounds.height, cellBounds.y, cellBounds.width, h);
        scrollRectToVisible(targetViewRect);
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
        Rectangle bounds = getCellBounds(0, 0);
        System.out.println(bounds);
        if (bounds != null) {
            int cellWidth = Math.max(getCellBounds(0, 0).width, 1);
            int width = getVisibleRect().width;
            return Math.max(width / cellWidth, 1);
        }
        return 1;
    }

}
