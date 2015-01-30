package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.util.Set;

import javax.swing.Action;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;

import com.aerhard.oxygen.plugin.glyphpicker.action.MoveDownAction;
import com.aerhard.oxygen.plugin.glyphpicker.action.MoveUpAction;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.view.renderer.DescriptionRenderer;

public class GlyphSelectionChangeHandler implements ListSelectionListener {

    private JTextPane infoLabel;
    private Set<Action> actions;
    private FilterList<GlyphDefinition> filterList;
    private SortedList<GlyphDefinition> sortedList;

    public GlyphSelectionChangeHandler(JTextPane infoLabel,
            SortedList<GlyphDefinition> sortedList,
            FilterList<GlyphDefinition> filterList, Set<Action> actions) {
        this.infoLabel = infoLabel;
        this.sortedList = sortedList;
        this.filterList = filterList;
        this.actions = actions;
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {

        if (!event.getValueIsAdjusting()) {
            Boolean enableButtons;

            @SuppressWarnings("unchecked")
            DefaultEventSelectionModel<GlyphDefinition> model = ((DefaultEventSelectionModel<GlyphDefinition>) event
                    .getSource());

            if (model.isSelectionEmpty()) {
                enableButtons = false;
            } else {
                GlyphDefinition d = model.getSelected().get(0);

                if (d == null) {
                    infoLabel.setText(null);
                    enableButtons = false;
                } else {
                    infoLabel.setText(DescriptionRenderer.getHTML(d));
                    enableButtons = true;
                }
            }

            boolean isSortingOrFiltering = sortedList.getComparator() != null
                    || sortedList.size() > filterList.size();

            for (Action action : actions) {

                if (isSortingOrFiltering
                        && (action instanceof MoveUpAction || action instanceof MoveDownAction)) {
                    action.setEnabled(false);
                }

                else if (action instanceof MoveUpAction
                        && model.isSelectedIndex(0)) {
                    action.setEnabled(false);
                }

                else if (action instanceof MoveDownAction
                        && model.isSelectedIndex(filterList.size() - 1)) {
                    action.setEnabled(false);
                }

                else {
                    action.setEnabled(enableButtons);
                }

            }
        }
    }
}