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

public class GlyphTable extends JTable {

    private static final long serialVersionUID = 1L;

    private TableCellRenderer tableIconRenderer;
    private TableCellRenderer tableDescriptionRenderer;

    public GlyphTable(TableModel tableModel) {
        tableDescriptionRenderer = new DescriptionRenderer();
        getTableHeader().setReorderingAllowed(false);
        setShowVerticalLines(false);
        setIntercellSpacing(new Dimension(0, 1));

        setFillsViewportHeight(true);

        setModel(tableModel);

        setDefaultFocusTraversal();

        getColumnModel().getColumn(0).setPreferredWidth(70);
        getColumnModel().getColumn(0).setMinWidth(30);
        getColumnModel().getColumn(1).setPreferredWidth(600);
        getColumnModel().getColumn(1).setMinWidth(10);

    }

    private void setDefaultFocusTraversal() {
        Set<AWTKeyStroke> forward = new HashSet<AWTKeyStroke>(
                getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        forward.add(KeyStroke.getKeyStroke("TAB"));
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                forward);
        Set<AWTKeyStroke> backward = new HashSet<AWTKeyStroke>(
                getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
        backward.add(KeyStroke.getKeyStroke("shift TAB"));
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
                backward);
    }

    public int getTopVisibleRow() {
        JViewport viewport = (JViewport) getParent();
        Point pt = viewport.getViewPosition();
        return rowAtPoint(pt);
    }

    public void setTopVisibleRow(int row) {
        Rectangle cellBounds = getCellRect(row, 0, true);
        int h = getVisibleRect().height;
        Rectangle targetViewRect = new Rectangle(cellBounds.x - h
                + cellBounds.height, cellBounds.y, cellBounds.width, h);
        scrollRectToVisible(targetViewRect);
    }

    public void setTableIconRenderer(TableCellRenderer renderer) {
        tableIconRenderer = renderer;
    }

    public TableCellRenderer getTableIconRenderer() {
        return tableIconRenderer;
    }

    public TableCellRenderer getCellRenderer(int row, int column) {
        if (column == 0) {
            return tableIconRenderer;
        }
        if (column == 1) {
            return tableDescriptionRenderer;
        }
        return super.getCellRenderer(row, column);
    }

}
