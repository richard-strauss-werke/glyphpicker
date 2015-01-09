package com.aerhard.oxygen.plugin.glyphpicker.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import com.aerhard.oxygen.plugin.glyphpicker.view.ContainerPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphGrid;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphTable;

public class ChangeViewAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    private int activeListIndex = 0;

    private ContainerPanel panel;
    private GlyphTable table;
    private GlyphGrid list;

    public ChangeViewAction(ContainerPanel panel, GlyphTable table,
            GlyphGrid list) {
        super("Switch View");
        this.panel = panel;
        this.table = table;
        this.list = list;
        putValue(SHORT_DESCRIPTION, "Switches the glyph list view style.");
        putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_V));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (activeListIndex == 0) {
            // NB get the old component's top row before the component
            // is replaced!
            int row = list.getTopVisibleRow();
            panel.setListComponent(table);
            panel.getInfoLabel().setVisible(false);
            panel.revalidate();
            table.setTopVisibleRow(row);
            activeListIndex = 1;
        } else {
            // NB get the old component's top row before the component
            // is replaced!
            int row = table.getTopVisibleRow();
            panel.setListComponent(list);
            panel.getInfoLabel().setVisible(true);
            panel.revalidate();
            list.setTopVisibleRow(row);
            activeListIndex = 0;
        }
    }

}
