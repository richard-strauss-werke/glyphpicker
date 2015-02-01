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

import com.aerhard.oxygen.plugin.glyphpicker.action.MoveDownAction;
import com.aerhard.oxygen.plugin.glyphpicker.action.MoveUpAction;
import com.aerhard.oxygen.plugin.glyphpicker.action.ReloadAction;
import com.aerhard.oxygen.plugin.glyphpicker.action.RemoveAction;
import com.aerhard.oxygen.plugin.glyphpicker.action.SaveAction;
import com.aerhard.oxygen.plugin.glyphpicker.controller.TabController;
import com.aerhard.oxygen.plugin.glyphpicker.controller.GlyphSelectionChangeHandler;
import com.aerhard.oxygen.plugin.glyphpicker.model.Config;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinitions;
import com.aerhard.oxygen.plugin.glyphpicker.view.ContainerPanel;

public class UserCollectionController extends TabController {

    private AbstractAction saveAction;
    private AbstractAction reloadAction;
    private AbstractAction removeAction;
    private MoveUpAction moveUpAction;
    private MoveDownAction moveDownAction;

    private UserCollectionLoader loader;
    private boolean listInSync = true;

    public UserCollectionController(ContainerPanel panel, Config config,
            Properties properties, StandalonePluginWorkspace workspace) {

        super(panel, config.getUserSearchFieldScopeIndex(), config
                .getUserViewIndex());

        loader = new UserCollectionLoader(workspace, properties);

        setActions();

        setListeners();

    }

    private void setActions() {

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

        Set<Action> actions = new HashSet<Action>();

        saveAction = new SaveAction(this, actions);
        saveAction.setEnabled(false);
        controlPanel.addToToolbar(saveAction, 4);

        reloadAction = new ReloadAction(this, actions);
        reloadAction.setEnabled(false);
        controlPanel.addToToolbar(reloadAction, 5);

        actions.add(saveAction);
        actions.add(reloadAction);
    }

    public void setListInSync(boolean listInSync) {
        this.listInSync = listInSync;
    }

    private void setListeners() {

        Set<Action> selectionDependentActions = new HashSet<Action>();
        selectionDependentActions.add(removeAction);
        selectionDependentActions.add(insertAction);
        selectionDependentActions.add(moveUpAction);
        selectionDependentActions.add(moveDownAction);

        selectionModel
                .addListSelectionListener(new GlyphSelectionChangeHandler(panel
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

    public void loadData() {
        panel.setMask(true);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                List<GlyphDefinition> data = loader.load().getData();

                listInSync = true;
                glyphList.clear();

                startBitmapLoadWorker(data);

                glyphList.addAll(data);
                panel.setMask(false);
            }
        });
    }

    public void saveData() {
        loader.save(new GlyphDefinitions(glyphList));
        listInSync = true;
    }

    public void addGlyphDefinition(GlyphDefinition d) {
        setListInSync(false);
        glyphList.add(d);
    }

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
