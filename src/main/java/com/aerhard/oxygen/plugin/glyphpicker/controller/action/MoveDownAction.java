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
package com.aerhard.oxygen.plugin.glyphpicker.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ImageIcon;

import ca.odell.glazedlists.EventList;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphGrid;
import ro.sync.ui.Icons;

/**
 * An action to move a glyph definition down in the list.
 */
public class MoveDownAction extends AbstractPickerAction {
    
    private static final long serialVersionUID = 1L;

    /** The key of the action. */
    public static final String KEY = "moveDown";

    /** The glyph list model. */
    private final EventList<GlyphDefinition> glyphList;
    
    /** The list component. */
    private final GlyphGrid list;

    /** The name of the class. */
    private static final String CLASS_NAME = MoveDownAction.class.getSimpleName();

    /**
     * Instantiates a MoveDownAction.
     *
     * @param listener the property change listener to be added to this action
     * @param glyphList the glyph list model
     * @param list the list component
     */
    public MoveDownAction(PropertyChangeListener listener,
            EventList<GlyphDefinition> glyphList, GlyphGrid list) {
        super(I18N.getString(CLASS_NAME + ".label"), Icons.getIcon(Icons.MOVE_DOWN_MENU));

        this.addPropertyChangeListener(listener);
        this.glyphList = glyphList;
        this.list = list;

        String description = I18N.getString(CLASS_NAME + ".description");

        putValue(SHORT_DESCRIPTION, description + " (" + MODIFIER_NAME + "+↓)");
        putValue(MNEMONIC_KEY, KeyEvent.VK_DOWN);
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        int index = list.getSelectedIndex();
        if (index != -1 && index < glyphList.size() - 1) {
            GlyphDefinition item = glyphList.get(index);
            if (glyphList.remove(item)) {
                glyphList.add(index + 1, item);
                list.setSelectedIndex(index + 1);
                firePropertyChange(KEY, null, null);
            }
        }
    }
}
