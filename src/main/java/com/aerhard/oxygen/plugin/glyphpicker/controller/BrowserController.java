package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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

import com.aerhard.oxygen.plugin.glyphpicker.action.ChangeViewAction;
import com.aerhard.oxygen.plugin.glyphpicker.controller.editor.DataSourceEditorController;
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
import com.aerhard.oxygen.plugin.glyphpicker.view.editor.DataSourceEditor;
import com.aerhard.oxygen.plugin.glyphpicker.view.renderer.GlyphRendererAdapter;

public class BrowserController extends Controller {

    // TODO use progress dialog when loading data

    private static final Logger LOGGER = Logger
            .getLogger(BrowserController.class.getName());

    private ListSelectionModel selectionModel;

    private EventList<GlyphDefinition> glyphList;
    private SortedList<GlyphDefinition> sortedList;
    private FilterList<GlyphDefinition> filterList;

    private ContainerPanel panel;
    private GlyphTable table;
    private GlyphGrid list;
    private DataSourceList dataSourceList;
    private GlyphDefinitionLoader loader;
    private boolean isLoading = false;

    private AbstractAction addAction;
    private AbstractAction insertAction;

    private ControlPanel controlPanel;

    private HighlightButton insertBtn;

    private CustomAutoCompleteSupport<String> autoCompleteSupport = null;

    @SuppressWarnings("unchecked")
    public BrowserController(Config config) {

        controlPanel = new ControlPanel(true);

        panel = new ContainerPanel(controlPanel);

        glyphList = new BasicEventList<GlyphDefinition>();

        sortedList = new SortedList<GlyphDefinition>(glyphList, null);

        final Map<String, PropertySelector> autoCompleteScope = new LinkedHashMap<String, PropertySelector>();
        autoCompleteScope.put("Range", new RangeSelector());
        autoCompleteScope.put("Char Name", new CharNameSelector());
        autoCompleteScope.put("xml:id", new IdSelector());
        autoCompleteScope.put("Codepoint", new CodePointSelector());
        autoCompleteScope.put("ALL FIELDS", new AllSelector());

        // TODO add entity field

        int scopeIndex = config.getBrowserSearchFieldScopeIndex();
        
        List<String> l = new ArrayList<String>(autoCompleteScope.keySet());
        
        PropertySelector initialPropertySelector = autoCompleteScope
                .get(l.get(scopeIndex));

        final GlyphSelect glyphSelect = new GlyphSelect();
        glyphSelect.setFilterator(new GlyphTextFilterator(initialPropertySelector));
        glyphSelect.setMode(TextMatcherEditor.CONTAINS);
        
        filterList = new FilterList<GlyphDefinition>(sortedList, glyphSelect);

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
                                glyphSelect.setFilterator(new GlyphTextFilterator(selector));
                            } else {
                                LOGGER.error("Item not found");
                            }
                        }
                    }
                });

        list = new GlyphGrid(new DefaultEventListModel<GlyphDefinition>(
                filterList));
        GlyphRendererAdapter r = new GlyphRendererAdapter(list);
        r.setPreferredSize(new Dimension(40, 40));
        list.setFixedSize(40);
        list.setCellRenderer(r);

        DefaultEventTableModel<GlyphDefinition> tableListModel = new DefaultEventTableModel<GlyphDefinition>(
                filterList, new GlyphTableFormat());
        table = new GlyphTable(tableListModel);
        r = new GlyphRendererAdapter(table);
        r.setPreferredSize(new Dimension(40, 40));
        table.setRowHeight(90);
        table.setTableIconRenderer(r);

        panel.setListComponent(list);

        dataSourceList = config.getDataSources();
        controlPanel.getDataSourceCombo().setModel(dataSourceList);

        controlPanel.getBtnConfigure().setAction(new EditAction());

        controlPanel.getSortBtn().setAction(new SortAction());

        controlPanel.getViewBtn().setAction(
                new ChangeViewAction(panel, table, list));

        setButtons();

        loader = new GlyphDefinitionLoader();

        selectionModel = new DefaultEventSelectionModel<GlyphDefinition>(
                filterList);
        selectionModel
                .setSelectionMode(DefaultEventSelectionModel.SINGLE_SELECTION);
        list.setSelectionModel(selectionModel);
        table.setSelectionModel(selectionModel);

        setListeners();

    }

    private void setButtons() {
        insertAction = new InsertXmlAction();
        insertAction.setEnabled(false);
        insertBtn = new HighlightButton(insertAction);
        panel.addToButtonPanel(insertBtn);

        addAction = new AddToUserCollectionAction();
        addAction.setEnabled(false);
        panel.addToButtonPanel(addAction);
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

    public static class GlyphComparator implements Comparator<GlyphDefinition>,
            Serializable {
        private static final long serialVersionUID = 1L;

        public int compare(GlyphDefinition glyphA, GlyphDefinition glyphB) {

            String aString = glyphA.getCodePoint();
            String bString = glyphB.getCodePoint();

            return (aString != null && bString != null) ? aString
                    .compareToIgnoreCase(bString) : -1;
        }
    }

    public ContainerPanel getPanel() {
        return panel;
    }
    
    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    protected void insertGlyph() {
        int row = selectionModel.getAnchorSelectionIndex();
        if (row != -1) {
            GlyphDefinition selectedModel = filterList.get(row);
            if (selectedModel != null) {
                fireEvent("insert", selectedModel);
            }
        }
    }

    private class SortAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        SortAction() {
            super("Sort by Codepoint", new ImageIcon(
                    BrowserController.class.getResource("/images/sort.png")));
            putValue(SHORT_DESCRIPTION, "Sorts the glyphs by code point.");
            putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_O));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }

    private final class EditAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        private EditAction() {
            super("Edit", new ImageIcon(
                    BrowserController.class.getResource("/images/gear.png")));
            putValue(SHORT_DESCRIPTION, "Edit data sources.");
            putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_E));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            List<DataSource> result = new DataSourceEditorController(
                    new DataSourceEditor(), panel).load(dataSourceList
                    .getData());

            if (result != null) {
                dataSourceList.getData().clear();
                dataSourceList.getData().addAll(result);
                if (dataSourceList.getSize() > 0) {
                    dataSourceList.setSelectedItem(dataSourceList
                            .getElementAt(0));
                }
                loadData();
            }
        }
    }

    private final class AddToUserCollectionAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        private AddToUserCollectionAction() {
            super("Add to User Collection", new ImageIcon(
                    ChangeViewAction.class.getResource("/images/plus.png")));
            putValue(SHORT_DESCRIPTION,
                    "Add the selected glyph to the user collection.");
            putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_C));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = selectionModel.getAnchorSelectionIndex();
            if (row != -1) {
                GlyphDefinition selectedModel = filterList.get(row);
                if (selectedModel != null) {
                    fireEvent("copyToUserCollection", selectedModel);
                }
            }
        }
    }

    private class InsertXmlAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        InsertXmlAction() {
            super("Insert XML", new ImageIcon(
                    BrowserController.class
                            .getResource("/images/blue-document-import.png")));
            putValue(SHORT_DESCRIPTION,
                    "Insert the selected glyph to the document.");
            putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_I));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            insertGlyph();
        }
    }

    private class GlyphSelectionListener implements ListSelectionListener {
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
                    GlyphDefinition glyphDefinition = model.getSelected().get(0);

                    if (glyphDefinition == null) {
                        panel.getInfoLabel().setText(null);
                        enableButtons = false;
                    } else {
                        panel.getInfoLabel().setText(glyphDefinition.getHTML());
                        enableButtons = true;
                    }
                }

                addAction.setEnabled(enableButtons);
                insertAction.setEnabled(enableButtons);
            }
        }
    }

    private void setListeners() {
        
        selectionModel.addListSelectionListener(new GlyphSelectionListener());

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
                    sortedList.setComparator(new GlyphComparator());
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
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
                    insertGlyph();
                }
            }
        };

        table.addMouseListener(mouseAdapter);
        list.addMouseListener(mouseAdapter);

        KeyAdapter enterKeyAdapter = new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    insertBtn.highlight();
                    insertGlyph();
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

        if (isLoading) {
            LOGGER.info("Skipping data loading.");
            return;
        }
        isLoading = true;
        panel.setMask(true);

        SwingWorker<List<GlyphDefinition>, Void> worker = new LoadWorker(
                dataSource);

        // TODO cancel previous worker instead of skipping latest worker

        worker.execute();
    }

    private class LoadWorker extends SwingWorker<List<GlyphDefinition>, Void> {

        private DataSource dataSource;

        // private JDialog dialog;

        public LoadWorker(DataSource dataSource) {
            this.dataSource = dataSource;
            // dialog = new JDialog();
            // dialog.setLocationRelativeTo(panel);
            // dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            //
            // });
        }

        @Override
        protected List<GlyphDefinition> doInBackground() {
            // dialog.setVisible(true);
            return loader.loadData(dataSource);
        }

        @Override
        protected void done() {
            try {
                onDataLoaded(get());
            } catch (InterruptedException e) {
                LOGGER.error(e);
            } catch (ExecutionException e) {
                LOGGER.error(e);
            } finally {
                isLoading = false;
                panel.setMask(false);
                // dialog.dispose();
            }
        }
    }

    private void onDataLoaded(List<GlyphDefinition> data) {
        glyphList.clear();

        selectionModel.clearSelection();
        table.scrollRectToVisible(new Rectangle(0, 0));
        list.scrollRectToVisible(new Rectangle(0, 0));

        // when loading was successful, set the loading path as
        // first item in the pathComboModel
        if (data != null) {
            glyphList.addAll(data);

            int index = controlPanel.getDataSourceCombo().getSelectedIndex();
            if (index != -1) {
                dataSourceList.setFirstIndex(index);
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
