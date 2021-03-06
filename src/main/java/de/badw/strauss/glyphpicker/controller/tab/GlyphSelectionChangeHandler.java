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

package de.badw.strauss.glyphpicker.controller.tab;

import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import de.badw.strauss.glyphpicker.controller.action.MoveDownAction;
import de.badw.strauss.glyphpicker.controller.action.MoveUpAction;
import de.badw.strauss.glyphpicker.model.GlyphDefinition;
import de.badw.strauss.glyphpicker.view.renderer.DescriptionRenderer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.Set;

/**
 * A handler for selection changes in the glyph event models on which glyph
 * grids and glyph tables are based.
 */
public class GlyphSelectionChangeHandler implements ListSelectionListener {

    /**
     * The panel containing the glyph info in grid view.
     */
    private final JLabel infoLabel;

    /**
     * The actions affected by selection changes.
     */
    private final Set<Action> actions;

    /**
     * The filtered glyph list.
     */
    private final FilterList<GlyphDefinition> filterList;

    /**
     * The sorted glyph list.
     */
    private final SortedList<GlyphDefinition> sortedList;

    /**
     * Instantiates a new GlyphSelectionChangeHandler.
     *  @param infoLabel  The panel containing the glyph info in grid view
     * @param sortedList The sorted glyph list
     * @param filterList The filtered glyph list
     * @param actions    The actions affected by selection changes
     */
    public GlyphSelectionChangeHandler(JLabel infoLabel,
                                       SortedList<GlyphDefinition> sortedList,
                                       FilterList<GlyphDefinition> filterList, Set<Action> actions) {
        this.infoLabel = infoLabel;
        this.sortedList = sortedList;
        this.filterList = filterList;
        this.actions = actions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
     * .ListSelectionEvent)
     */
    @Override
    public void valueChanged(ListSelectionEvent event) {

        if (!event.getValueIsAdjusting()) {
            Boolean enableActions;

            @SuppressWarnings("unchecked")
            DefaultEventSelectionModel<GlyphDefinition> model = ((DefaultEventSelectionModel<GlyphDefinition>) event
                    .getSource());

            if (model.isSelectionEmpty()) {
                enableActions = false;
            } else {
                GlyphDefinition d = model.getSelected().get(0);

                if (d == null) {
                    infoLabel.setText(null);
                    enableActions = false;
                } else {
                    infoLabel.setText(DescriptionRenderer.getHTML(d));
                    enableActions = true;
                }
            }

            adjustActions(enableActions, model);
        }
    }

    /**
     * Adjust the affected actions to the glyph selection change.
     *
     * @param enableActions indicates if the actions should be enabled or disabled
     * @param model         the selection event's origin model
     */
    private void adjustActions(Boolean enableActions,
                               DefaultEventSelectionModel<GlyphDefinition> model) {
        boolean isSortingOrFiltering = sortedList.getComparator() != null
                || sortedList.size() > filterList.size();

        for (Action action : actions) {

            if (isSortingOrFiltering
                    && (action instanceof MoveUpAction || action instanceof MoveDownAction)) {
                action.setEnabled(false);
            } else if (action instanceof MoveUpAction && model.isSelectedIndex(0)) {
                action.setEnabled(false);
            } else if (action instanceof MoveDownAction
                    && model.isSelectedIndex(filterList.size() - 1)) {
                action.setEnabled(false);
            } else {
                action.setEnabled(enableActions);
            }
        }
    }
}