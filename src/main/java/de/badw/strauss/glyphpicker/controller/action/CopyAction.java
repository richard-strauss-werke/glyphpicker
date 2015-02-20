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

import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import de.badw.strauss.glyphpicker.model.GlyphDefinition;
import de.badw.strauss.glyphpicker.view.TabPanel;
import ro.sync.ui.Icons;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

/**
 * An action which picks the selected glyph definition from a list and returns
 * it as new value of the ADD property change event.
 */
public class CopyAction extends AbstractPickerAction {

    /**
     * The key of the action.
     */
    public static final String KEY = "copyToUserCollection";
    private static final long serialVersionUID = 1L;
    /**
     * The name of the class.
     */
    private static final String CLASS_NAME = CopyAction.class.getSimpleName();
    /**
     * The event selection model.
     */
    private final DefaultEventSelectionModel<GlyphDefinition> selectionModel;

    /**
     * Instantiates a new CopyAction.
     *
     * @param panel          The container tab panel
     * @param listener       the property change listener to be added to this action
     * @param selectionModel the event selection model
     */
    public CopyAction(TabPanel panel, PropertyChangeListener listener,
                      DefaultEventSelectionModel<GlyphDefinition> selectionModel) {
        super(CLASS_NAME, Icons.COPY_MENU, "ctrl ENTER");

        addPropertyChangeListener(listener);
        this.selectionModel = selectionModel;
        bindAcceleratorToComponent(this, panel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (!selectionModel.isSelectionEmpty()) {
            GlyphDefinition d = selectionModel.getSelected().get(0);
            if (d != null) {
                firePropertyChange(KEY, null, d);
            }
        }
    }
}