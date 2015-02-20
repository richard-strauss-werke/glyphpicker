/**
 * Copyright 2015 Alexander Erhard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.badw.strauss.glyphpicker.view;

import de.badw.strauss.glyphpicker.model.GlyphDefinition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * The glyph grid component.
 */
public class GlyphGrid extends JList<GlyphDefinition> {

    private static final long serialVersionUID = 1L;

    /**
     * The item size.
     */
    private int size = 0;

    /**
     * Instantiates a new GlyphGrid.
     *
     * @param listModel the list model
     */
    public GlyphGrid(ListModel<GlyphDefinition> listModel) {
        setLayoutOrientation(JList.HORIZONTAL_WRAP);
        setModel(listModel);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addComponentListener(new ResizeAdapter());
    }

    /**
     * Sets fixed cell dimensions.
     *
     * @param size the new fixed height/width
     */
    public void setFixedSize(int size) {
        this.size = size;
        setFixedCellWidth(size);
        setFixedCellHeight(size);
    }

    /**
     * Gets the top visible row.
     *
     * @return the top visible row
     */
    public int getTopVisibleRow() {
        JViewport viewport = (JViewport) getParent();
        Point pt = viewport.getViewPosition();
        return locationToIndex(pt);
    }

    /**
     * Sets the top visible row.
     *
     * @param row the new top visible row
     */
    public void setTopVisibleRow(int row) {
        Rectangle cellBounds = getCellBounds(row, row);
        int h = getVisibleRect().height;
        Rectangle targetViewRect = new Rectangle(cellBounds.x - h
                + cellBounds.height, cellBounds.y, cellBounds.width, h);
        scrollRectToVisible(targetViewRect);
    }

    /**
     * Fixes the row count for visible columns.
     */
    public void fixRowCountForVisibleColumns() {
        int nCols = computeVisibleColumnCount();
        int nItems = getModel().getSize();
        int nRows = nItems / nCols;
        if (nItems % nCols > 0) {
            nRows++;
        }
        setVisibleRowCount(nRows);
    }

    /**
     * Computes the number of visible columns.
     *
     * @return the number of visible columns
     */
    private int computeVisibleColumnCount() {
        return Math.max(
                (int) Math.floor(getVisibleRect().width / (double) size), 1);
    }

    /**
     * A ComponentAdapter to adjust the row / columns count when the component is resized.
     */
    private class ResizeAdapter extends ComponentAdapter {

        /* (non-Javadoc)
         * @see java.awt.event.ComponentAdapter#componentResized(java.awt.event.ComponentEvent)
         */
        @Override
        public void componentResized(ComponentEvent e) {
            fixRowCountForVisibleColumns();
        }
    }

}
