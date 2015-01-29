package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.UniqueList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.DefaultEventListModel;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import ca.odell.glazedlists.swing.DefaultEventTableModel;

import com.aerhard.oxygen.plugin.glyphpicker.model.Config;
import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.model.DataSourceList;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.model.trans.AllSelector;
import com.aerhard.oxygen.plugin.glyphpicker.model.trans.CharNameSelector;
import com.aerhard.oxygen.plugin.glyphpicker.model.trans.CodePointSelector;
import com.aerhard.oxygen.plugin.glyphpicker.model.trans.IdSelector;
import com.aerhard.oxygen.plugin.glyphpicker.model.trans.PropertySelector;
import com.aerhard.oxygen.plugin.glyphpicker.model.trans.RangeSelector;
import com.aerhard.oxygen.plugin.glyphpicker.model.trans.TransformedGlyphList;
import com.aerhard.oxygen.plugin.glyphpicker.view.ControlPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.ContainerPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphGrid;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphTable;
import com.aerhard.oxygen.plugin.glyphpicker.view.HighlightButton;
import com.aerhard.oxygen.plugin.glyphpicker.view.renderer.GlyphRendererAdapter;

public class BrowserController extends Controller {

    // TODO use progress dialog when loading data

    private static final Logger LOGGER = Logger
            .getLogger(BrowserController.class.getName());

    private static final int LIST_ITEM_SIZE = 40;

    private EventList<GlyphDefinition> glyphList = new BasicEventList<GlyphDefinition>();
    private SortedList<GlyphDefinition> sortedList = new SortedList<GlyphDefinition>(
            glyphList, null);

    private final GlyphSelect glyphSelect = new GlyphSelect();
    private FilterList<GlyphDefinition> filterList = new FilterList<GlyphDefinition>(
            sortedList, glyphSelect);

    private DefaultEventSelectionModel<GlyphDefinition> selectionModel = new DefaultEventSelectionModel<GlyphDefinition>(
            filterList);

    private ContainerPanel panel;
    private GlyphTable table;
    private GlyphGrid list;
    private DataSourceList dataSourceList;
    private boolean isLoadingGlyphData = false;

    private AbstractAction addAction;
    private AbstractAction insertAction;

    private ControlPanel controlPanel;

    private HighlightButton insertBtn;

    private CustomAutoCompleteSupport<String> autoCompleteSupport = null;

    private GlyphBitmapBulkLoader bmpLoader = null;

    @SuppressWarnings("unchecked")
    public BrowserController(ContainerPanel panel, Config config) {

        this.panel = panel;

        controlPanel = panel.getControlPanel();

        final Map<String, PropertySelector> autoCompleteScope = new LinkedHashMap<String, PropertySelector>();
        autoCompleteScope.put("Range", new RangeSelector());
        autoCompleteScope.put("Char Name", new CharNameSelector());
        autoCompleteScope.put("xml:id", new IdSelector());
        autoCompleteScope.put("Codepoint", new CodePointSelector());
        autoCompleteScope.put("ALL FIELDS", new AllSelector());

        // TODO add entity field

        int scopeIndex = config.getBrowserSearchFieldScopeIndex();

        List<String> l = new ArrayList<String>(autoCompleteScope.keySet());

        PropertySelector initialPropertySelector = autoCompleteScope.get(l
                .get(scopeIndex));

        glyphSelect.setFilterator(new GlyphTextFilterator(
                initialPropertySelector));

        ((JTextField) controlPanel.getAutoCompleteCombo().getEditor()
                .getEditorComponent()).getDocument().addDocumentListener(
                glyphSelect);

        setAutoCompleteSupport(initialPropertySelector);

        DefaultComboBoxModel<String> autoCompleteScopeModel = new DefaultComboBoxModel<String>();

        for (String property : autoCompleteScope.keySet()) {
            autoCompleteScopeModel.addElement(property);
        }

        controlPanel.getAutoCompleteScopeCombo().setModel(
                autoCompleteScopeModel);

        controlPanel.getAutoCompleteScopeCombo().setSelectedIndex(scopeIndex);

        controlPanel.getAutoCompleteScopeCombo().addItemListener(
                new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            String item = (String) e.getItem();
                            PropertySelector selector = autoCompleteScope
                                    .get(item);
                            if (selector != null) {
                                setAutoCompleteSupport(selector);
                                glyphSelect
                                        .setFilterator(new GlyphTextFilterator(
                                                selector));
                            } else {
                                LOGGER.error("Item not found");
                            }
                        }
                    }
                });

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

        dataSourceList = config.getDataSources();
        controlPanel.getDataSourceCombo().setModel(dataSourceList);

        controlPanel.getBtnConfigure().setAction(
                new EditAction(this, panel, dataSourceList));

        controlPanel.getSortBtn().setAction(new SortAction());

        controlPanel.getViewBtn().setAction(
                new ChangeViewAction(panel, table, list));

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

        addAction = new AddToUserCollectionAction(this, selectionModel);
        addAction.setEnabled(false);
        controlPanel.addToToolbar(addAction, 1);
    }

    private void setAutoCompleteSupport(final PropertySelector propertySelector) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TransformedGlyphList propertyList = new TransformedGlyphList(
                        sortedList, propertySelector);
                UniqueList<String> uniquePropertyList = new UniqueList<String>(
                        propertyList);
                if (autoCompleteSupport != null) {
                    autoCompleteSupport.uninstall();
                }
                autoCompleteSupport = CustomAutoCompleteSupport.install(
                        controlPanel.getAutoCompleteCombo(), uniquePropertyList);
                autoCompleteSupport.setFilterMode(TextMatcherEditor.CONTAINS);
            }
        });
    }

    private void setListeners() {

        Set<Action> actions = new HashSet<Action>();
        actions.add(addAction);
        actions.add(insertAction);

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
                }

                else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    sortedList.setComparator(null);
                }
            }
        });

        controlPanel.getDataSourceCombo().addActionListener(
                new AbstractAction() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getModifiers() == AWTEvent.MOUSE_EVENT_MASK) {
                            loadData();
                        }
                    }
                });

        controlPanel.getDataSourceCombo().addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loadData();
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

    }

    @Override
    public void loadData() {

        int index = controlPanel.getDataSourceCombo().getSelectedIndex();

        if (index == -1) {
            index = 0;
        }

        if (index > dataSourceList.getSize() - 1) {
            JOptionPane.showMessageDialog(panel,
                    "No data source has been found.");
            glyphList.clear();
            return;
        }

        DataSource dataSource = dataSourceList.getDataSourceAt(index);

        if (dataSource == null) {
            JOptionPane.showMessageDialog(panel,
                    "No data source has been found.");
            glyphList.clear();
            return;
        }

        if (isLoadingGlyphData) {
            LOGGER.info("Skipping data loading.");
            return;
        }
        isLoadingGlyphData = true;
        panel.setMask(true);

        if (bmpLoader != null) {
            bmpLoader.cancel(true);
        }

        final GlyphDefinitionLoadWorker glyphDefinitionLoadWorker = new GlyphDefinitionLoadWorker(
                dataSource);

        // TODO cancel previous worker instead of skipping latest worker

        // TODO listen for cancel action

        glyphDefinitionLoadWorker
                .addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent e) {
                        if ("state".equals(e.getPropertyName().toString())
                                && "DONE".equals(e.getNewValue().toString())) {
                            isLoadingGlyphData = false;
                            displayLoadedData(glyphDefinitionLoadWorker
                                    .getResult());
                        }
                    }
                });

        glyphDefinitionLoadWorker.execute();

    }

    private void displayLoadedData(List<GlyphDefinition> data) {

        glyphList.clear();
        selectionModel.clearSelection();
        table.scrollRectToVisible(new Rectangle(0, 0));
        list.scrollRectToVisible(new Rectangle(0, 0));

        panel.setMask(false);

        if (data != null) {

            startBitmapLoadWorker(data);

            glyphList.addAll(data);

            // set the loading path as first item in the pathComboModel
            int index = controlPanel.getDataSourceCombo().getSelectedIndex();
            if (index != -1) {
                dataSourceList.setFirstIndex(index);
            }
        }

    }

    private void startBitmapLoadWorker(List<GlyphDefinition> data) {
        bmpLoader = new GlyphBitmapBulkLoader(data, LIST_ITEM_SIZE);

        bmpLoader.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("iconLoaded".equals(e.getPropertyName())) {
                    redrawIcon((GlyphDefinition) e.getNewValue());
                }
            }
        });
        bmpLoader.execute();
    }

    private void redrawIcon(GlyphDefinition d) {
        int index = filterList.indexOf(d);
        Component listComponent = panel.getListComponent();
        if (index != -1) {
            if (listComponent instanceof GlyphGrid) {
                list.repaint(list.getCellBounds(index, index));
            }

            else if (listComponent instanceof GlyphTable) {
                table.repaint(table.getCellRect(index, 0, true));
            }
        }
    }

    @Override
    public void saveData() {
    }

    @Override
    public void eventOccured(String type, GlyphDefinition model) {
    }

}
