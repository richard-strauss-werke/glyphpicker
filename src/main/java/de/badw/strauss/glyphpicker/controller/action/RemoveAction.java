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
import ca.odell.glazedlists.FilterList;
import de.badw.strauss.glyphpicker.model.GlyphDefinition;
import de.badw.strauss.glyphpicker.view.GlyphGrid;
import de.badw.strauss.glyphpicker.view.TabPanel;
import ro.sync.ui.Icons;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

/**
 * An action which removes the selected item from a glyph list model.
 */
public class RemoveAction extends AbstractPickerAction {

    private static final long serialVersionUID = 1L;

    /**
     * The key of the action.
     */
    public static final String KEY = "remove";

    /**
     * The glyph list model.
     */
    private final EventList<GlyphDefinition> glyphList;

    /**
     * The filter list model.
     */
    private final FilterList<GlyphDefinition> filterList;

    /**
     * The list component.
     */
    private final GlyphGrid list;

    /**
     * The name of the class.
     */
    private static final String CLASS_NAME = RemoveAction.class.getSimpleName();

    /**
     * Instantiates a new RemoveAction.
     *
     * @param panel      The container tab panel
     * @param listener   the property change listener to be added to this action
     * @param glyphList  the glyph list model
     * @param filterList the filter list model
     * @param list       the list component
     */
    public RemoveAction(TabPanel panel, PropertyChangeListener listener,
                        EventList<GlyphDefinition> glyphList,
                        FilterList<GlyphDefinition> filterList, GlyphGrid list) {
        super(CLASS_NAME, Icons.REMOVE_MENU, "ctrl D");
        this.addPropertyChangeListener(listener);
        this.glyphList = glyphList;
        this.filterList = filterList;
        this.list = list;
        bindAcceleratorToComponent(this, panel);
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        int index = list.getSelectedIndex();
        if (index != -1) {
            firePropertyChange(KEY, null, null);
            GlyphDefinition item = filterList.get(index);

            boolean itemRemoved = glyphList.remove(item);

            if (itemRemoved) {
                index = Math.min(index, glyphList.size() - 1);
                if (index >= 0) {
                    list.setSelectedIndex(index);
                }
            }
        }
    }
}