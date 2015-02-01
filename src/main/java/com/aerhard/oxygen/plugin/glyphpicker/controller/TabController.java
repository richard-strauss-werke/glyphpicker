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
import java.beans.PropertyChangeSupport;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JTextField;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.swing.DefaultEventListModel;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import ca.odell.glazedlists.swing.DefaultEventTableModel;

import com.aerhard.oxygen.plugin.glyphpicker.controller.action.ChangeViewAction;
import com.aerhard.oxygen.plugin.glyphpicker.controller.action.InsertXmlAction;
import com.aerhard.oxygen.plugin.glyphpicker.controller.action.SortAction;
import com.aerhard.oxygen.plugin.glyphpicker.controller.autocomplete.AutoCompleteController;
import com.aerhard.oxygen.plugin.glyphpicker.controller.bitmap.BitmapLoadWorker;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.view.ContainerPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.ControlPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphGrid;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphTable;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphTableFormat;
import com.aerhard.oxygen.plugin.glyphpicker.view.HighlightButton;
import com.aerhard.oxygen.plugin.glyphpicker.view.renderer.GlyphRendererAdapter;

public abstract class TabController implements PropertyChangeListener {

    public static final int LIST_ITEM_SIZE = 40;

    protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    protected EventList<GlyphDefinition> glyphList = new BasicEventList<GlyphDefinition>();
    protected SortedList<GlyphDefinition> sortedList = new SortedList<GlyphDefinition>(
            glyphList, null);

    protected FilterList<GlyphDefinition> filterList;

    protected DefaultEventSelectionModel<GlyphDefinition> selectionModel;

    protected ContainerPanel panel;
    protected GlyphTable table;
    protected GlyphGrid list;

    protected ControlPanel controlPanel;
    protected BitmapLoadWorker bmpLoader = null;
    private PropertyChangeListener bmpListener;
    protected AutoCompleteController autoCompleteController;

    protected AbstractAction insertAction;
    protected HighlightButton insertBtn;

    @SuppressWarnings("unchecked")
    public TabController(final ContainerPanel panel,
            int userSearchFieldScopeIndex, int listViewIndex) {

        this.panel = panel;

        controlPanel = panel.getControlPanel();

        autoCompleteController = new AutoCompleteController(
                userSearchFieldScopeIndex, controlPanel.getAutoCompleteCombo(),
                controlPanel.getAutoCompleteScopeCombo(), sortedList);

        filterList = new FilterList<GlyphDefinition>(sortedList,
                autoCompleteController.getGlyphSelect());

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

        if (listViewIndex == 0) {
            panel.setListComponent(list);
        } else {
            panel.setListComponent(table);
            panel.getInfoPanel().setVisible(false);
        }

        selectionModel
                .setSelectionMode(DefaultEventSelectionModel.SINGLE_SELECTION);
        list.setSelectionModel(selectionModel);
        table.setSelectionModel(selectionModel);

        controlPanel.getSortBtn().setAction(new SortAction());

        controlPanel.getViewBtn().setAction(
                new ChangeViewAction(panel, table, list));

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
                }

                else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    sortedList.setComparator(null);
                }
            }
        });

        initKeyAndMouseListeners();

    }

    private void initKeyAndMouseListeners() {
        insertAction = new InsertXmlAction(this, selectionModel);
        insertAction.setEnabled(false);
        insertBtn = new HighlightButton(insertAction);

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
    }

    public void startBitmapLoadWorker(List<GlyphDefinition> data) {
        bmpLoader = new BitmapLoadWorker(data, LIST_ITEM_SIZE);

        bmpListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("iconLoaded".equals(e.getPropertyName())) {
                    panel.redrawIconInList(filterList
                            .indexOf((GlyphDefinition) e.getNewValue()));
                }
            }
        };

        bmpLoader.addPropertyChangeListener(bmpListener);
        bmpLoader.execute();
    }

    public void cancelBitmapLoadWorker() {
        if (bmpLoader != null) {
            bmpLoader.shutdownExecutor();
            bmpLoader.cancel(true);
            bmpLoader.removePropertyChangeListener(bmpListener);
            bmpLoader = null;
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

}
