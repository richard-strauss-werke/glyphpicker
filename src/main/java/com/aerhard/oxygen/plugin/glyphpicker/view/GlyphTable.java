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
package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.AWTKeyStroke;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.aerhard.oxygen.plugin.glyphpicker.view.renderer.DescriptionRenderer;

/**
 * The glyph table component.
 */
public class GlyphTable extends JTable {


    private static final long serialVersionUID = 1L;
    
    /** The preferred width of the first column. */
    private static final int COL_1_PREFERRED_WIDTH = 70;
    
    /** The minimum width of the first column. */
    private static final int COL_1_MIN_WIDTH = 30;
    
    /** The preferred width of the second column. */
    private static final int COL_2_PREFERRED_WIDTH = 600;
    
    /** The minimum width of the second column. */
    private static final int COL_2_MIN_WIDTH = 10;

    /** The glyph renderer. */
    private TableCellRenderer glyphRenderer;
    
    /** The glyph description renderer. */
    private final TableCellRenderer glyphDescriptionRenderer;

    /**
     * Instantiates a new glyph table.
     *
     * @param tableModel the table model
     */
    public GlyphTable(TableModel tableModel) {
        glyphDescriptionRenderer = new DescriptionRenderer();
        getTableHeader().setReorderingAllowed(false);
        setShowVerticalLines(false);
        setIntercellSpacing(new Dimension(0, 1));

        setFillsViewportHeight(true);

        setModel(tableModel);

        setDefaultFocusTraversal();

        getColumnModel().getColumn(0).setPreferredWidth(COL_1_PREFERRED_WIDTH);
        getColumnModel().getColumn(0).setMinWidth(COL_1_MIN_WIDTH);
        getColumnModel().getColumn(1).setPreferredWidth(COL_2_PREFERRED_WIDTH);
        getColumnModel().getColumn(1).setMinWidth(COL_2_MIN_WIDTH);

    }

    /**
     * Sets default focus traversal rules: tab and shift tab should select new siblings rather than other parts of the table.
     */
    private void setDefaultFocusTraversal() {
        Set<AWTKeyStroke> forward = new HashSet<>(
                getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        forward.add(KeyStroke.getKeyStroke("TAB"));
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                forward);
        Set<AWTKeyStroke> backward = new HashSet<>(
                getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
        backward.add(KeyStroke.getKeyStroke("shift TAB"));
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
                backward);
    }

    /**
     * Gets the top visible row.
     *
     * @return the top visible row
     */
    public int getTopVisibleRow() {
        JViewport viewport = (JViewport) getParent();
        Point pt = viewport.getViewPosition();
        return rowAtPoint(pt);
    }

    /**
     * Sets the top visible row.
     *
     * @param row the new top visible row
     */
    public void setTopVisibleRow(int row) {
        Rectangle cellBounds = getCellRect(row, 0, true);
        int h = getVisibleRect().height;
        Rectangle targetViewRect = new Rectangle(cellBounds.x - h
                + cellBounds.height, cellBounds.y, cellBounds.width, h);
        scrollRectToVisible(targetViewRect);
    }

    /**
     * Sets the glyph renderer.
     *
     * @param renderer the new glyph renderer
     */
    public void setGlyphRenderer(TableCellRenderer renderer) {
        glyphRenderer = renderer;
    }

    /* (non-Javadoc)
     * @see javax.swing.JTable#getCellRenderer(int, int)
     */
    public TableCellRenderer getCellRenderer(int row, int column) {
        if (column == 0) {
            return glyphRenderer;
        }
        if (column == 1) {
            return glyphDescriptionRenderer;
        }
        return super.getCellRenderer(row, column);
    }

}
