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
import java.beans.PropertyChangeListener;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

/**
 * An action which picks the selected glyph definition from a list and returns
 * it as new value of the ADD property change event.
 */
public class AddAction extends AbstractPickerAction {

    private static final long serialVersionUID = 1L;

    /** The key of the action. */
    public static final String KEY = "addToUserCollection";

    /** The event selection model. */
    private final DefaultEventSelectionModel<GlyphDefinition> selectionModel;

    /** The name of the class. */
    private static final String className = AddAction.class.getSimpleName();

    /**
     * Instantiates a new AddAction.
     *
     * @param listener
     *            the property change listener to be added to this action
     * @param selectionModel
     *            the event selection model
     */
    public AddAction(PropertyChangeListener listener,
            DefaultEventSelectionModel<GlyphDefinition> selectionModel) {
        super(className, "/images/plus.png");

        addPropertyChangeListener(listener);
        this.selectionModel = selectionModel;
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