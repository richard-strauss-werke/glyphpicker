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

package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.*;
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
import com.aerhard.oxygen.plugin.glyphpicker.controller.bitmap.ImageCacheAccess;
import com.aerhard.oxygen.plugin.glyphpicker.controller.options.OptionsAction;
import com.aerhard.oxygen.plugin.glyphpicker.model.Config;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.view.TabPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.ControlPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphGrid;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphTable;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphTableFormat;
import com.aerhard.oxygen.plugin.glyphpicker.view.HighlightButton;
import com.aerhard.oxygen.plugin.glyphpicker.view.renderer.GlyphRendererAdapter;

/**
 * An abstract controller of a tab panel.
 */
public abstract class TabController implements PropertyChangeListener {

    /**
     * The pixel height and width of a rendered glyph.
     */
    public static final int LIST_ITEM_SIZE = 40;

    public static final String DATA_LOADED = "dataLoaded";

    /**
     * The property change support.
     */
    protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * The glyph list.
     */
    protected final EventList<GlyphDefinition> glyphList = new BasicEventList<>();

    /**
     * The sorted glyph list.
     */
    protected final SortedList<GlyphDefinition> sortedList = new SortedList<>(
            glyphList, null);

    /**
     * The filtered glyph list.
     */
    protected final FilterList<GlyphDefinition> filterList;

    /**
     * The glyph list's selection model.
     */
    protected final DefaultEventSelectionModel<GlyphDefinition> selectionModel;

    /**
     * The container panel in the tab.
     */
    protected final TabPanel tabPanel;

    /**
     * The control panel in the tab.
     */
    protected final ControlPanel controlPanel;

    /**
     * The glyph table component.
     */
    protected final GlyphTable table;

    /**
     * The glyph list component.
     */
    protected final GlyphGrid list;

    /**
     * The image cache access
     */
    private final ImageCacheAccess imageCacheAccess;

    /**
     * The bmp load worker.
     */
    protected BitmapLoadWorker bmpLoader = null;

    /**
     * The listener to property changes in the bmp load worker.
     */
    private PropertyChangeListener bmpListener;

    /**
     * The auto complete controller.
     */
    protected final AutoCompleteController autoCompleteController;

    /**
     * The action to trigger the insertion of a glyph reference into an XML document.
     */
    protected AbstractAction insertAction;

    /**
     * The button to trigger the insert action.
     */
    protected HighlightButton insertBtn;

    /**
     * Instantiates a new tab controller.
     * @param tabPanel              The container panel in the tab
     * @param config                The plugin config
     * @param searchFieldScopeIndex the index of the auto complete scope checkbox which should be selected initially
     * @param listViewIndex         the index of the list view to show initially. Set 0 for the grid and 1 for the table component.
     * @param imageCacheAccess            the image cache
     */
    @SuppressWarnings("unchecked")
    public TabController(final TabPanel tabPanel,
                         Config config, int searchFieldScopeIndex, int listViewIndex, ImageCacheAccess imageCacheAccess) {

        this.tabPanel = tabPanel;
        this.imageCacheAccess = imageCacheAccess;

        controlPanel = tabPanel.getControlPanel();

        autoCompleteController = new AutoCompleteController(
                searchFieldScopeIndex, controlPanel.getAutoCompleteCombo(),
                controlPanel.getAutoCompleteScopeCombo(), sortedList);

        filterList = new FilterList<>(sortedList,
                autoCompleteController.getGlyphSelect());

        selectionModel = new DefaultEventSelectionModel<>(
                filterList);

        list = new GlyphGrid(new DefaultEventListModel<>(
                filterList));
        GlyphRendererAdapter r = new GlyphRendererAdapter(list);
        r.setPreferredSize(new Dimension(LIST_ITEM_SIZE, LIST_ITEM_SIZE));
        list.setFixedSize(LIST_ITEM_SIZE);
        list.setCellRenderer(r);

        DefaultEventTableModel<GlyphDefinition> tableListModel = new DefaultEventTableModel<>(
                filterList, new GlyphTableFormat());
        table = new GlyphTable(tableListModel);
        r = new GlyphRendererAdapter(table);
        r.setPreferredSize(new Dimension(LIST_ITEM_SIZE, LIST_ITEM_SIZE));
        table.setRowHeight(90);
        table.setGlyphRenderer(r);

        if (listViewIndex == 0) {
            tabPanel.setListComponent(list);
        } else {
            tabPanel.setListComponent(table);
            tabPanel.getInfoPanel().setVisible(false);
        }

        selectionModel
                .setSelectionMode(DefaultEventSelectionModel.SINGLE_SELECTION);
        list.setSelectionModel(selectionModel);
        table.setSelectionModel(selectionModel);

        controlPanel.getOptionsBtn().setAction(new OptionsAction(this, config, tabPanel, imageCacheAccess));

        controlPanel.getSortBtn().setAction(new SortAction());

        controlPanel.getViewBtn().setAction(
                new ChangeViewAction(tabPanel, table, list));

        filterList
                .addListEventListener(new ListEventListener<GlyphDefinition>() {
                    @Override
                    public void listChanged(ListEvent<GlyphDefinition> e) {

                        if (filterList.size() == 0) {
                            tabPanel.getInfoLabel().setText(null);
                        } else if (selectionModel.isSelectionEmpty()) {
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

        initKeyAndMouseListeners();

    }

    /**
     * Initializes the keyboard and mouse listeners.
     */
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
        controlPanel.getAutoCompleteCombo().getEditor()
                .getEditorComponent().addKeyListener(enterKeyAdapter);
    }

    /**
     * Starts the bitmap load worker.
     *
     * @param data the glyph definitions containing the bitmap's paths
     */
    public void startBitmapLoadWorker(List<GlyphDefinition> data) {

        bmpLoader = new BitmapLoadWorker(data, LIST_ITEM_SIZE, imageCacheAccess);

        bmpListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("iconLoaded".equals(e.getPropertyName())) {
                    tabPanel.redrawIconInList(filterList
                            .indexOf(e.getNewValue()));
                }
            }
        };

        bmpLoader.addPropertyChangeListener(bmpListener);
        bmpLoader.execute();
    }

    /**
     * Cancels the bitmap load worker.
     */
    public void cancelBitmapLoadWorker() {
        if (bmpLoader != null) {
            bmpLoader.shutdownExecutor();
            bmpLoader.cancel(true);
            bmpLoader.removePropertyChangeListener(bmpListener);
            bmpLoader = null;
        }
    }

    /**
     * Adds a property change listener to the controller.
     *
     * @param l the listener
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    /**
     * Removes a property change listener from the controller.
     *
     * @param l the listener
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

}
