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
package com.aerhard.oxygen.plugin.glyphpicker.controller.user;

import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

import com.aerhard.oxygen.plugin.glyphpicker.controller.TabController;
import com.aerhard.oxygen.plugin.glyphpicker.controller.GlyphSelectionChangeHandler;
import com.aerhard.oxygen.plugin.glyphpicker.controller.action.MoveDownAction;
import com.aerhard.oxygen.plugin.glyphpicker.controller.action.MoveUpAction;
import com.aerhard.oxygen.plugin.glyphpicker.controller.action.ReloadAction;
import com.aerhard.oxygen.plugin.glyphpicker.controller.action.RemoveAction;
import com.aerhard.oxygen.plugin.glyphpicker.controller.action.SaveAction;
import com.aerhard.oxygen.plugin.glyphpicker.model.Config;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinitions;
import com.aerhard.oxygen.plugin.glyphpicker.view.TabPanel;

/**
 * The user collection tab controller.
 */
public class UserCollectionController extends TabController {

    /** The save action. */
    private AbstractAction saveAction;

    /** The reload action. */
    private AbstractAction reloadAction;

    /** The remove action. */
    private AbstractAction removeAction;

    /** The move-up action. */
    private MoveUpAction moveUpAction;

    /** The move-down action. */
    private MoveDownAction moveDownAction;

    /** The user collection data loader. */
    private final UserCollectionLoader loader;

    /** Indicates if the list in memory is in sync with the list stored on disk. */
    private boolean listInSync = true;

    /**
     * Instantiates a new UserCollectionController.
     *
     * @param panel
     *            the user collection tab's container panel
     * @param config
     *            the plugin config
     * @param properties
     *            the plugin properties
     * @param workspace
     *            the workspace
     */
    public UserCollectionController(TabPanel panel, Config config,
            Properties properties, StandalonePluginWorkspace workspace) {

        super(panel, config.getUserSearchFieldScopeIndex(), config
                .getUserViewIndex());

        loader = new UserCollectionLoader(workspace, properties);

        setAdditionalActions();

        setAdditionalListeners();

    }

    /**
     * Sets the additional actions.
     */
    private void setAdditionalActions() {

        controlPanel.addToToolbar(insertBtn, 0);

        removeAction = new RemoveAction(this, glyphList, filterList, list);
        removeAction.setEnabled(false);
        controlPanel.addToToolbar(removeAction, 1);

        moveUpAction = new MoveUpAction(this, glyphList, list);
        moveUpAction.setEnabled(false);
        controlPanel.addToToolbar(moveUpAction, 2);

        moveDownAction = new MoveDownAction(this, glyphList, list);
        moveDownAction.setEnabled(false);
        controlPanel.addToToolbar(moveDownAction, 3);

        Set<Action> actions = new HashSet<>();

        saveAction = new SaveAction(this, actions);
        saveAction.setEnabled(false);
        controlPanel.addToToolbar(saveAction, 4);

        reloadAction = new ReloadAction(this, actions);
        reloadAction.setEnabled(false);
        controlPanel.addToToolbar(reloadAction, 5);

        actions.add(saveAction);
        actions.add(reloadAction);
    }

    /**
     * Sets the indicator of memory-disk list synchronization.
     *
     * @param listInSync
     *            true if the list is in sync, false otherwise
     */
    public void setListInSync(boolean listInSync) {
        this.listInSync = listInSync;
    }

    /**
     * Sets the additional listeners.
     */
    private void setAdditionalListeners() {

        Set<Action> selectionDependentActions = new HashSet<>();
        selectionDependentActions.add(removeAction);
        selectionDependentActions.add(insertAction);
        selectionDependentActions.add(moveUpAction);
        selectionDependentActions.add(moveDownAction);

        selectionModel
                .addListSelectionListener(new GlyphSelectionChangeHandler(tabPanel
                        .getInfoLabel(), sortedList, filterList,
                        selectionDependentActions));

        glyphList
                .addListEventListener(new ListEventListener<GlyphDefinition>() {
                    @Override
                    public void listChanged(ListEvent<GlyphDefinition> e) {
                        if (listInSync) {
                            saveAction.setEnabled(false);
                            reloadAction.setEnabled(false);
                        } else {
                            saveAction.setEnabled(true);
                            reloadAction.setEnabled(true);
                        }
                    }
                });

    }

    /**
     * Loads the user collection data.
     */
    public void loadData() {
        tabPanel.setMask(true);
        final UserCollectionController thisController = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                List<GlyphDefinition> data = loader.load().getData();

                listInSync = true;
                glyphList.clear();

                startBitmapLoadWorker(data);

                glyphList.addAll(data);
                tabPanel.setMask(false);

                pcs.firePropertyChange("dataLoaded", null, thisController);
            }
        });
    }

    /**
     * Saves the user collection data.
     */
    public void saveData() {
        loader.save(new GlyphDefinitions(glyphList));
        listInSync = true;
    }

    /**
     * Adds a glyph definition to the user collection.
     *
     * @param d
     *            the glyh definition to add
     */
    public void addGlyphDefinition(GlyphDefinition d) {
        setListInSync(false);
        glyphList.add(d);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
     * PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {

        if ("insert".equals(e.getPropertyName())) {
            pcs.firePropertyChange(e);
        }

        else if ("reload".equals(e.getPropertyName())) {
            loadData();
        }

        else if ("saveData".equals(e.getPropertyName())) {
            saveData();
        }

        else if ("listInSync".equals(e.getPropertyName())) {
            setListInSync((boolean) e.getNewValue());
        }

    }

}
