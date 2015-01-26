package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

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
        super(null, new ImageIcon(
                ChangeViewAction.class.getResource("/images/grid.png")));
        this.panel = panel;
        this.table = table;
        this.list = list;
        
        String mnemonic = "T";
        
        putValue(SHORT_DESCRIPTION, "Toggles between glyph list view styles (Alt+"+mnemonic+")");
        putValue(MNEMONIC_KEY, KeyStroke.getKeyStroke(mnemonic).getKeyCode());
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
