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

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.swing.DefaultEventListModel;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import de.badw.strauss.glyphpicker.controller.action.ChangeViewAction;
import de.badw.strauss.glyphpicker.controller.action.FocusSearchComboAction;
import de.badw.strauss.glyphpicker.controller.action.InsertXmlAction;
import de.badw.strauss.glyphpicker.controller.action.SortAction;
import de.badw.strauss.glyphpicker.controller.autocomplete.AutoCompleteController;
import de.badw.strauss.glyphpicker.controller.bitmap.BitmapLoadWorker;
import de.badw.strauss.glyphpicker.controller.bitmap.ImageCache;
import de.badw.strauss.glyphpicker.model.Config;
import de.badw.strauss.glyphpicker.model.GlyphDefinition;
import de.badw.strauss.glyphpicker.view.*;
import de.badw.strauss.glyphpicker.view.renderer.GlyphRendererAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * An abstract controller of a tab panel.
 */
public abstract class AbstractTabController implements PropertyChangeListener {

    /**
     * The pixel height and width of a rendered glyph.
     */
    public static final int LIST_ITEM_SIZE = 40;

    /**
     * The key of the data-loaded event
     */
    public static final String DATA_LOADED = "dataLoaded";

    /**
     * the height of a single table row
     */
    private static final int TABLE_ROW_HEIGHT = 70;

    /**
     * The property change support.
     */
    protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * The glyph list.
     */
    protected final EventList<GlyphDefinition> glyphList = new BasicEventList<GlyphDefinition>();

    /**
     * The sorted glyph list.
     */
    protected final SortedList<GlyphDefinition> sortedList = new SortedList<GlyphDefinition>(
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
     * The auto complete controller.
     */
    protected final AutoCompleteController autoCompleteController;
    /**
     * The action to trigger the insertion of a glyph reference into an XML document.
     */
    protected final Action insertAction;
    /**
     * The bitmap image cache
     */
    private final ImageCache imageCache;
    /**
     * The bmp load worker.
     */
    protected BitmapLoadWorker bmpLoader = null;
    /**
     * The listener to property changes in the bmp load worker.
     */
    private PropertyChangeListener bmpListener;

    /**
     * Instantiates a new tab controller.
     *
     * @param tabPanel              The container panel in the tab
     * @param searchFieldScopeIndex the index of the auto complete scope checkbox which should be selected initially
     * @param listViewIndex         the index of the list view to show initially. Set 0 for the grid and 1 for the table component.
     * @param imageCache            the image cache
     */
    @SuppressWarnings("unchecked")
    public AbstractTabController(final TabPanel tabPanel, int searchFieldScopeIndex, int listViewIndex, ImageCache imageCache) {

        this.tabPanel = tabPanel;
        this.imageCache = imageCache;

        controlPanel = tabPanel.getControlPanel();

        autoCompleteController = new AutoCompleteController(
                searchFieldScopeIndex, controlPanel.getAutoCompleteCombo(),
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
        table.setRowHeight(TABLE_ROW_HEIGHT);
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

        controlPanel.getSortBtn().setAction(new SortAction(tabPanel, controlPanel.getSortBtn()));
        controlPanel.getViewBtn().setAction(new ChangeViewAction(tabPanel, table, list));

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

        insertAction = new InsertXmlAction(this, selectionModel);
        insertAction.setEnabled(false);

        new FocusSearchComboAction(tabPanel);

        initMouseListeners();

        setEnterKeyAction(list);
        setEnterKeyAction(table);
        setEnterKeyAction((JComponent) controlPanel.getAutoCompleteCombo().getEditor()
                .getEditorComponent());
    }

    /**
     * Initializes the mouse listeners.
     */
    private void initMouseListeners() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    insertAction.actionPerformed(null);
                }
            }
        };
        list.addMouseListener(mouseAdapter);
        table.addMouseListener(mouseAdapter);
    }

    /**
     * Associated the enter key with the InsertXmlAction in a component.
     *
     * @param component the component
     */
    private void setEnterKeyAction(JComponent component) {
        component.getInputMap().put(KeyStroke.getKeyStroke("ENTER"),
                "insert");
        component.getActionMap().put("insert", insertAction);
    }

    /**
     * Starts the bitmap load worker.
     *
     * @param data the glyph definitions containing the bitmap's paths
     */
    public void startBitmapLoadWorker(List<GlyphDefinition> data) {

        bmpLoader = new BitmapLoadWorker(data, LIST_ITEM_SIZE, imageCache);

        bmpListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (BitmapLoadWorker.ICON_LOADED.equals(e.getPropertyName())) {
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
