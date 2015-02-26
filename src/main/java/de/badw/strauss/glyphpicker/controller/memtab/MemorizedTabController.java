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
package de.badw.strauss.glyphpicker.controller.memtab;

import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import de.badw.strauss.glyphpicker.controller.action.*;
import de.badw.strauss.glyphpicker.controller.bitmap.ImageCache;
import de.badw.strauss.glyphpicker.controller.tab.AbstractTabController;
import de.badw.strauss.glyphpicker.controller.tab.GlyphSelectionChangeHandler;
import de.badw.strauss.glyphpicker.model.Config;
import de.badw.strauss.glyphpicker.model.GlyphDefinition;
import de.badw.strauss.glyphpicker.model.GlyphDefinitions;
import de.badw.strauss.glyphpicker.view.TabPanel;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * The memorized tab tab controller.
 */
public class MemorizedTabController extends AbstractTabController {

    /**
     * The memorized tab data loader.
     */
    private final MemorizedCharactersLoader loader;
    /**
     * The set of actions whose activation depends on the sync status of memory / disk lists.
     */
    private final Set<Action> syncDependentActions = new HashSet<Action>();
    /**
     * The set of actions whose activation depends on whether there is a list selection or not.
     */
    private final Set<Action> selectionDependentActions = new HashSet<Action>();
    /**
     * Indicates if the list in memory is in sync with the list stored on disk.
     */
    private boolean listInSync = true;
    /**
     * The move-down action
     */
    private MoveDownAction moveDownAction;

    /**
     * Instantiates a new MemorizedTabController.
     *
     * @param panel      the memorized tab tab's container panel
     * @param config     the plugin config
     * @param properties the plugin properties
     * @param workspace  the workspace
     * @param imageCache the image cache
     */
    public MemorizedTabController(TabPanel panel, Config config,
                                  Properties properties, StandalonePluginWorkspace workspace, ImageCache imageCache) {

        super(panel, config.getUserSearchFieldScopeIndex(), config
                .getUserViewIndex(), imageCache);

        loader = new MemorizedCharactersLoader(workspace, properties);

        setAdditionalActions();

        setAdditionalListeners();

    }

    /**
     * Sets the additional actions.
     */
    private void setAdditionalActions() {

        int position = 0;

        MoveUpAction moveUpAction = new MoveUpAction(tabPanel, this, glyphList, list);
        moveUpAction.setEnabled(false);
        controlPanel.addToToolbar(moveUpAction, position++);

        moveDownAction = new MoveDownAction(tabPanel, this, glyphList, list);
        moveDownAction.setEnabled(false);
        controlPanel.addToToolbar(moveDownAction, position++);

        AbstractAction removeAction = new RemoveAction(tabPanel, this, glyphList, filterList, list);
        removeAction.setEnabled(false);
        controlPanel.addToToolbar(removeAction, position++);

        controlPanel.addToToolbar(new JToolBar.Separator(null), position++);

        AbstractAction saveAction = new SaveAction(tabPanel, this, syncDependentActions);
        saveAction.setEnabled(false);
        controlPanel.addToToolbar(saveAction, position++);

        AbstractAction reloadAction = new ReloadAction(tabPanel, this, syncDependentActions);
        reloadAction.setEnabled(false);
        controlPanel.addToToolbar(reloadAction, position);

        syncDependentActions.add(saveAction);
        syncDependentActions.add(reloadAction);

        selectionDependentActions.add(removeAction);
        selectionDependentActions.add(insertAction);
        selectionDependentActions.add(moveUpAction);
        selectionDependentActions.add(moveDownAction);

        controlPanel.setToolBarWidthThreshold();
    }

    /**
     * Sets the indicator of memory-disk list synchronization.
     *
     * @param listInSync true if the list is in sync, false otherwise
     */
    public void setListInSync(boolean listInSync) {
        this.listInSync = listInSync;
    }

    /**
     * Sets the additional listeners.
     */
    private void setAdditionalListeners() {

        selectionModel
                .addListSelectionListener(new GlyphSelectionChangeHandler(tabPanel
                        .getInfoLabel(), sortedList, filterList,
                        selectionDependentActions));

        glyphList
                .addListEventListener(new ListEventListener<GlyphDefinition>() {
                    @Override
                    public void listChanged(ListEvent<GlyphDefinition> e) {
                        boolean enableActions = !listInSync;
                        for (Action a : syncDependentActions) {
                            a.setEnabled(enableActions);
                        }
                    }
                });

    }

    /**
     * Loads the memorized tab data.
     */
    public void loadData() {
        tabPanel.setMask(true);
        final MemorizedTabController thisController = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                List<GlyphDefinition> data = loader.load().getData();

                listInSync = true;
                glyphList.clear();

                startBitmapLoadWorker(data);

                glyphList.addAll(data);
                tabPanel.setMask(false);

                pcs.firePropertyChange(DATA_LOADED, null, thisController);
            }
        });
    }

    /**
     * Saves the memorized tab data.
     */
    public void saveData() {
        loader.save(new GlyphDefinitions(glyphList));
        listInSync = true;
    }

    /**
     * Adds a glyph definition to the memorized tab.
     *
     * @param d the glyph definition to add
     */
    public void addGlyphDefinition(GlyphDefinition d) {
        setListInSync(false);
        glyphList.add(d);
        if (glyphList.size() == 2
                && selectionModel.isSelectedIndex(0)
                ) {
            moveDownAction.setEnabled(true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
     * PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {

        if (InsertXmlAction.KEY.equals(e.getPropertyName())) {
            pcs.firePropertyChange(e);
        } else if (ReloadAction.KEY.equals(e.getPropertyName())) {
            loadData();
        } else if (SaveAction.KEY.equals(e.getPropertyName())) {
            saveData();
        } else if (MoveDownAction.KEY.equals(e.getPropertyName())
                || MoveUpAction.KEY.equals(e.getPropertyName())
                || RemoveAction.KEY.equals(e.getPropertyName())) {
            setListInSync(false);
        }

    }

}
