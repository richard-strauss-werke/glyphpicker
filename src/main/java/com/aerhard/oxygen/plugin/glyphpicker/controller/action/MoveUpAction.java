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

/**
 * An action to move a glyph definition up in the list.
 */
public class MoveUpAction extends AbstractPickerAction {
    
    private static final long serialVersionUID = 1L;
    
    /** The glyph list model. */
    private final EventList<GlyphDefinition> glyphList;
    
    /** The list component. */
    private final GlyphGrid list;

    /** The name of the class. */
    private static final String className = MoveUpAction.class.getSimpleName();

    /**
     * Instantiates a new MoveUpAction.
     *
     * @param listener the property change listener to be added to this action
     * @param glyphList the glyph list model
     * @param list the list component
     */
    public MoveUpAction(PropertyChangeListener listener,
            EventList<GlyphDefinition> glyphList, GlyphGrid list) {
        super(I18N.getString(className + ".label"), new ImageIcon(
                MoveUpAction.class.getResource("/images/arrow-090.png")));

        this.addPropertyChangeListener(listener);
        this.glyphList = glyphList;
        this.list = list;

        String description = I18N.getString(className + ".description");

        putValue(SHORT_DESCRIPTION, description + " (" + MODIFIER_NAME + "+↑)");
        putValue(MNEMONIC_KEY, KeyEvent.VK_UP);
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        int index = list.getSelectedIndex();
        if (index > 0) {

            firePropertyChange("listInSync", null, false);
            GlyphDefinition item = glyphList.get(index);

            boolean itemRemoved = glyphList.remove(item);

            if (itemRemoved) {
                glyphList.add(index - 1, item);
                list.setSelectedIndex(index - 1);
            }
        }
    }
}
