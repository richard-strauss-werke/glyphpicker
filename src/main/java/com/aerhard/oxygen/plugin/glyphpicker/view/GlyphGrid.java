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

    private int size = 0;
    
    public GlyphGrid(ListModel<GlyphDefinition> listModel) {
        setLayoutOrientation(JList.HORIZONTAL_WRAP);

        setModel(listModel);

        addComponentListener(new ResizeAdapter());
    }

    public void setFixedSize(int size) {
        this.size = size;
        setFixedCellWidth(size);
        setFixedCellHeight(size);
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
        return Math.max ((int) Math.floor(getVisibleRect().width / size), 1);
    }
    
    
 // @Override
    // public String getToolTipText(MouseEvent e) {
    // String tip = null;
    // java.awt.Point p = e.getPoint();
    // int rowIndex = rowAtPoint(p);
    // int colIndex = columnAtPoint(p);
    //
    //
    // try {
    // //comment row, exclude heading
    // if(rowIndex >= 0){
    // tip = getValueAt(rowIndex, colIndex).toString() + rowIndex;
    // }
    // } catch (RuntimeException e1) {
    // //catch null pointer exception if mouse is over an empty line
    // }
    //
    // return tip;
    // }

}
