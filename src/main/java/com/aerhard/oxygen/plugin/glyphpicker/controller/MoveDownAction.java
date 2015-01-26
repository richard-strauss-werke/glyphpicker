package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import ca.odell.glazedlists.EventList;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphGrid;

public class MoveDownAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private UserCollectionController controller;
    private EventList<GlyphDefinition> glyphList;
    private GlyphGrid list;

    public MoveDownAction(UserCollectionController controller,
            EventList<GlyphDefinition> glyphList, GlyphGrid list) {
        super(null, new ImageIcon(
                BrowserController.class.getResource("/images/arrow-270.png")));

        this.controller = controller;
        this.glyphList = glyphList;
        this.list = list;

        putValue(SHORT_DESCRIPTION, "Sorts the glyphs by code point (Alt+Arrow Down)");
        putValue(MNEMONIC_KEY, KeyEvent.VK_DOWN);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int index = list.getSelectedIndex();
        if (index != -1 && index < glyphList.size() - 1) {

            controller.setListInSync(false);
            GlyphDefinition item = glyphList.get(index);

            boolean itemRemoved = glyphList.remove(item);

            if (itemRemoved) {
                glyphList.add(index + 1, item);
                list.setSelectedIndex(index + 1);
            }
        }
    }
}
