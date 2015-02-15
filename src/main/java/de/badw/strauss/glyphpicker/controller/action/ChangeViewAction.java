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

import java.awt.event.ActionEvent;

import de.badw.strauss.glyphpicker.view.TabPanel;
import de.badw.strauss.glyphpicker.view.GlyphGrid;
import de.badw.strauss.glyphpicker.view.GlyphTable;
import ro.sync.ui.Icons;

/**
 * An action to toggle between grid and table view.
 */
public class ChangeViewAction extends AbstractPickerAction {

    private static final long serialVersionUID = 1L;

    /**
     * The panel containing table and list view.
     */
    private final TabPanel panel;

    /**
     * The table component.
     */
    private final GlyphTable table;

    /**
     * The list component.
     */
    private final GlyphGrid list;

    /**
     * The class name.
     */
    private static final String CLASS_NAME = ChangeViewAction.class.getSimpleName();

    /**
     * Instantiates a new ChangeViewAction.
     *
     * @param panel The panel containing table and list view
     * @param table the table component
     * @param list  the list component
     */
    public ChangeViewAction(TabPanel panel, GlyphTable table,
                            GlyphGrid list) {
        super(CLASS_NAME, Icons.TABLE_MENU);
        this.panel = panel;
        this.table = table;
        this.list = list;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        boolean restoreFocus = panel.getListComponent().hasFocus();

        if (panel.getListComponent() instanceof GlyphGrid) {
            // NB get the old component's top row before the component
            // is replaced!
            int row = list.getTopVisibleRow();
            panel.setListComponent(table);
            panel.getInfoPanel().setVisible(false);
            panel.revalidate();
            table.setTopVisibleRow(row);
        } else {
            // NB get the old component's top row before the component
            // is replaced!
            int row = table.getTopVisibleRow();
            panel.setListComponent(list);
            panel.getInfoPanel().setVisible(true);
            panel.revalidate();
            list.setTopVisibleRow(row);
        }

        if (restoreFocus) {
            panel.getListComponent().requestFocusInWindow();
        }
    }

}
