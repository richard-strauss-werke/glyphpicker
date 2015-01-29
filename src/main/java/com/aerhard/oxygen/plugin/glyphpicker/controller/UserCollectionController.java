package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.swing.DefaultEventListModel;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import ca.odell.glazedlists.swing.DefaultEventTableModel;

import com.aerhard.oxygen.plugin.glyphpicker.controller.autocomplete.AutoCompleteController;
import com.aerhard.oxygen.plugin.glyphpicker.controller.bitmap.GlyphBitmapBulkLoader;
import com.aerhard.oxygen.plugin.glyphpicker.model.Config;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinitions;
import com.aerhard.oxygen.plugin.glyphpicker.view.ContainerPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.ControlPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphGrid;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphTable;
import com.aerhard.oxygen.plugin.glyphpicker.view.HighlightButton;
import com.aerhard.oxygen.plugin.glyphpicker.view.renderer.GlyphRendererAdapter;

public class UserCollectionController extends Controller {

    private static final int LIST_ITEM_SIZE = 40;

    private EventList<GlyphDefinition> glyphList = new BasicEventList<GlyphDefinition>();
    private SortedList<GlyphDefinition> sortedList = new SortedList<GlyphDefinition>(
            glyphList, null);

    private FilterList<GlyphDefinition> filterList;

    private DefaultEventSelectionModel<GlyphDefinition> selectionModel;

    private ContainerPanel panel;

    private GlyphTable table;
    private UserCollectionLoader loader;

    private GlyphGrid list;
    private boolean listInSync = true;

    private AbstractAction saveAction;
    private AbstractAction reloadAction;
    private AbstractAction removeAction;
    private AbstractAction insertAction;
    private MoveUpAction moveUpAction;
    private MoveDownAction moveDownAction;

    private HighlightButton insertBtn;

    private ControlPanel controlPanel;

    private GlyphBitmapBulkLoader bmpLoader = null;
    
    private AutoCompleteController autoCompleteController;

    @SuppressWarnings("unchecked")
    public UserCollectionController(ContainerPanel panel, Config config,
            Properties properties, StandalonePluginWorkspace workspace) {

        this.panel = panel;

        controlPanel = panel.getControlPanel();

        autoCompleteController = new AutoCompleteController(config.getUserSearchFieldScopeIndex(),
                controlPanel.getAutoCompleteCombo(),
                controlPanel.getAutoCompleteScopeCombo(), sortedList);

        filterList = new FilterList<GlyphDefinition>(
                sortedList, autoCompleteController.getGlyphSelect());
        
        selectionModel = new DefaultEventSelectionModel<GlyphDefinition>(
                filterList);
        
        list = new GlyphGrid(new DefaultEventListModel<GlyphDefinition>(
                filterList));
        GlyphRendererAdapter r = new GlyphRendererAdapter(list);
        r.setPreferredSize(new Dimension(LIST_ITEM_SIZE, LIST_ITEM_SIZE));
        list.setFixedSize(LIST_ITEM_SIZE);
        list.setCellRenderer(r);

        DefaultEventTableModel<GlyphDefinition> tableListModel = new DefaultEventTableModel<GlyphDefinition>(
                filterList, new GlyphTableFormat());
        table = new GlyphTable(tableListModel);
        r = new GlyphRendererAdapter(table);
        r.setPreferredSize(new Dimension(LIST_ITEM_SIZE, LIST_ITEM_SIZE));
        table.setRowHeight(90);
        table.setTableIconRenderer(r);

        panel.setListComponent(list);

        controlPanel.getSortBtn().setAction(new SortAction());

        controlPanel.getViewBtn().setAction(
                new ChangeViewAction(panel, table, list));

        loader = new UserCollectionLoader(workspace, properties);

        selectionModel
                .setSelectionMode(DefaultEventSelectionModel.SINGLE_SELECTION);
        list.setSelectionModel(selectionModel);
        table.setSelectionModel(selectionModel);

        setActions();

        setListeners();

    }

    private void setActions() {

        insertAction = new InsertXmlAction(this, selectionModel);
        insertAction.setEnabled(false);
        insertBtn = new HighlightButton(insertAction);
        controlPanel.addToToolbar(insertBtn, 0);

        removeAction = new RemoveFromUserCollectionAction(this, glyphList,
                filterList, list);
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

        Set<Action> actions = new HashSet<Action>();
        actions.add(removeAction);
        actions.add(insertAction);
        actions.add(moveUpAction);
        actions.add(moveDownAction);

        selectionModel
                .addListSelectionListener(new GlyphSelectionChangeHandler(panel
                        .getInfoLabel(), sortedList, filterList, actions));

        filterList
                .addListEventListener(new ListEventListener<GlyphDefinition>() {
                    @Override
                    public void listChanged(ListEvent<GlyphDefinition> e) {

                        if (filterList.size() == 0) {
                            panel.getInfoLabel().setText(null);
                        }

                        else if (selectionModel.isSelectionEmpty()) {
                            selectionModel.setSelectionInterval(0, 0);
                        }

                        // reevaluate list layout
                        if (list.isVisible()) {
                            list.fixRowCountForVisibleColumns();
                        }

                    }
                });

        controlPanel.getSortBtn().addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    sortedList.setComparator(new CodePointComparator());
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    sortedList.setComparator(null);
                }
            }
        });

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    insertBtn.highlight();
                    insertAction.actionPerformed(null);
                }
            }
        };

        table.addMouseListener(mouseAdapter);
        list.addMouseListener(mouseAdapter);

        KeyAdapter enterKeyAdapter = new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    insertBtn.highlight();
                    insertAction.actionPerformed(null);
                }
            }
        };

        table.addKeyListener(enterKeyAdapter);
        list.addKeyListener(enterKeyAdapter);
        ((JTextField) controlPanel.getAutoCompleteCombo().getEditor()
                .getEditorComponent()).addKeyListener(enterKeyAdapter);

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

    @Override
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
    
    private void startBitmapLoadWorker(List<GlyphDefinition> data) {
        bmpLoader = new GlyphBitmapBulkLoader(data, LIST_ITEM_SIZE);
        bmpLoader.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("iconLoaded".equals(e.getPropertyName())) {
                    panel.redrawIconInList(filterList.indexOf((GlyphDefinition) e.getNewValue()));
                }
            }
        });
        bmpLoader.execute();
    }
    
    @Override
    public void saveData() {
        loader.save(new GlyphDefinitions(glyphList));
        listInSync = true;
    }

    @Override
    public void eventOccured(String type, GlyphDefinition model) {
        if ("copyToUserCollection".equals(type)) {
            listInSync = false;
            glyphList.add(model);
        }

    }

}
