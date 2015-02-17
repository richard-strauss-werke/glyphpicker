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

import de.badw.strauss.glyphpicker.view.TabPanel;
import ro.sync.ui.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * An action to focus the search text field of a panel.
 */
public class FocusSearchComboAction extends AbstractPickerAction {

    private static final long serialVersionUID = 1L;

    /**
     * The name of the class.
     */
    private static final String CLASS_NAME = FocusSearchComboAction.class.getSimpleName();

    /**
     * the auto complete combo box
     */
    private final JComboBox<?> autoCompleteCombo;

    /**
     * Instantiates a new SortAction.
     *
     * @param panel The container tab panel
     */
    public FocusSearchComboAction(TabPanel panel) {
        super("ctrl F");
        this.autoCompleteCombo = panel.getControlPanel().getAutoCompleteCombo();
        bindAcceleratorToComponent(this, panel);
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        autoCompleteCombo.requestFocusInWindow();
    }
}
