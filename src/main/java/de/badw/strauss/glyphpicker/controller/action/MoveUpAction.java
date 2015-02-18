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
package de.badw.strauss.glyphpicker.controller.action;

import ca.odell.glazedlists.EventList;
import de.badw.strauss.glyphpicker.model.GlyphDefinition;
import de.badw.strauss.glyphpicker.view.GlyphGrid;
import de.badw.strauss.glyphpicker.view.TabPanel;
import ro.sync.ui.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;

/**
 * An action to move a glyph definition up in the list.
 */
public class MoveUpAction extends AbstractPickerAction {

    private static final long serialVersionUID = 1L;

    /**
     * The key of the action.
     */
    public static final String KEY = "moveUp";

    /**
     * The glyph list model.
     */
    private final EventList<GlyphDefinition> glyphList;

    /**
     * The list component.
     */
    private final GlyphGrid list;

    /**
     * The name of the class.
     */
    private static final String CLASS_NAME = MoveUpAction.class.getSimpleName();

    /**
     * Instantiates a new MoveUpAction.
     *
     * @param panel     The container tab panel
     * @param listener  the property change listener to be added to this action
     * @param glyphList the glyph list model
     * @param list      the list component
     */
    public MoveUpAction(TabPanel panel, PropertyChangeListener listener,
                        EventList<GlyphDefinition> glyphList, GlyphGrid list) {
        super(I18N.getString(CLASS_NAME + ".label"), Icons.getIcon(Icons.MOVE_UP_MENU));

        this.addPropertyChangeListener(listener);
        this.glyphList = glyphList;
        this.list = list;

        String description = I18N.getString(CLASS_NAME + ".description");

        putValue(SHORT_DESCRIPTION, description + " (" + MENU_SHORTCUT_NAME + "+↑)");
        putValue(MNEMONIC_KEY, KeyEvent.VK_DOWN);

        String osIndependentAccelerator = (IS_MAC) ? "command UP" : "ctrl UP";
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(osIndependentAccelerator));
        bindAcceleratorToComponent(this, panel);
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        int index = list.getSelectedIndex();
        if (index > 0) {
            GlyphDefinition item = glyphList.get(index);
            if (glyphList.remove(item)) {
                glyphList.add(index - 1, item);
                list.setSelectedIndex(index - 1);
                firePropertyChange(KEY, null, null);
            }
        }
    }
}
