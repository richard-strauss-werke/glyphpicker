package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
import ca.odell.glazedlists.BasicEventList;
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
import com.aerhard.oxygen.plugin.glyphpicker.controller.BrowserController.GlyphComparator;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinitions;
import com.aerhard.oxygen.plugin.glyphpicker.model.trans.AllSelector;
import com.aerhard.oxygen.plugin.glyphpicker.model.trans.CharNameSelector;
import com.aerhard.oxygen.plugin.glyphpicker.model.trans.CodePointSelector;
import com.aerhard.oxygen.plugin.glyphpicker.model.trans.IdSelector;
import com.aerhard.oxygen.plugin.glyphpicker.model.trans.PropertySelector;
import com.aerhard.oxygen.plugin.glyphpicker.model.trans.RangeSelector;
import com.aerhard.oxygen.plugin.glyphpicker.model.trans.TransformedGlyphList;
import com.aerhard.oxygen.plugin.glyphpicker.view.ContainerPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.ControlPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphGrid;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphTable;
import com.aerhard.oxygen.plugin.glyphpicker.view.HighlightButton;
import com.aerhard.oxygen.plugin.glyphpicker.view.renderer.GlyphRendererAdapter;

public class UserCollectionController extends Controller {

    private static final Logger LOGGER = Logger
            .getLogger(UserCollectionController.class.getName());
    
    private ListSelectionModel selectionModel;

    private ContainerPanel panel;

    private GlyphTable table;
    private UserCollectionLoader loader;
    
    private BasicEventList<GlyphDefinition> glyphList;
    private SortedList<GlyphDefinition> sortedList;
    private FilterList<GlyphDefinition> filterList;
    
    private GlyphGrid list;
    private boolean listInSync = true;

    private AbstractAction saveAction;
    private AbstractAction reloadAction;
    private AbstractAction removeAction;
    private AbstractAction insertAction;

    private HighlightButton insertBtn;

    private ControlPanel controlPanel;

    private CustomAutoCompleteSupport<String> autoCompleteSupport = null;

    @SuppressWarnings("unchecked")
    public UserCollectionController(StandalonePluginWorkspace workspace,
            Properties properties) {

         controlPanel = new ControlPanel(false);

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

        PropertySelector initialPropertySelector = autoCompleteScope
                .get("Range");

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

        controlPanel.getSortBtn().setAction(new SortAction());
        
        controlPanel.getViewBtn().setAction(
                new ChangeViewAction(panel, table, list));

        setButtons();

        loader = new UserCollectionLoader(workspace, properties);

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

        removeAction = new RemoveFromUserCollectionAction();
        removeAction.setEnabled(false);
        panel.addToButtonPanel(removeAction);

        saveAction = new SaveAction();
        saveAction.setEnabled(false);
        panel.addToButtonPanel(saveAction);

        reloadAction = new ReloadAction();
        reloadAction.setEnabled(false);
        panel.addToButtonPanel(reloadAction);
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

    public ContainerPanel getPanel() {
        return panel;
    }

    // TODO adjust to filter and sorting!!
    private void removeItemFromUserCollection() {
        int index = list.getSelectedIndex();
        if (index != -1) {
            listInSync = false;
            glyphList.remove(index);
            index = Math.min(index, glyphList.size() - 1);
            if (index >= 0) {
                list.setSelectedIndex(index);
            }
        }
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
    
    private final class SaveAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        private SaveAction() {
            super("Save Collection", new ImageIcon(
                    UserCollectionController.class
                            .getResource("/images/disk.png")));
            putValue(SHORT_DESCRIPTION, "Save the User Collection.");
            putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_S));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            saveData();
            saveAction.setEnabled(false);
            reloadAction.setEnabled(false);
        }
    }

    private final class ReloadAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        private ReloadAction() {
            super("Reload Collection", new ImageIcon(
                    ChangeViewAction.class
                            .getResource("/images/arrow-circle-225-left.png")));
            putValue(SHORT_DESCRIPTION, "Reload the User Collection.");
            putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_L));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            loadData();
            saveAction.setEnabled(false);
            reloadAction.setEnabled(false);
        }
    }

    private final class RemoveFromUserCollectionAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        private RemoveFromUserCollectionAction() {
            super("Remove Item", new ImageIcon(
                    ChangeViewAction.class.getResource("/images/minus.png")));
            putValue(SHORT_DESCRIPTION,
                    "Remove the selected glyph from the user collection.");
            putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_R));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            removeItemFromUserCollection();
        }
    }

    private class InsertXmlAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        InsertXmlAction() {
            super("Insert XML", new ImageIcon(
                    UserCollectionController.class
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

                    int index = model.getAnchorSelectionIndex();

                    enableButtons = (index != -1);

                    GlyphDefinition glyphDefinition = (index == -1) ? null
                            : filterList.get(index);

                    if (glyphDefinition == null) {
                        panel.getInfoLabel().setText(null);
                        panel.getInfoLabel2().setText(null);
                    } else {
                        String charName = glyphDefinition.getCharName();
                        panel.getInfoLabel().setText(
                                glyphDefinition.getCodePoint()
                                        + (charName == null ? "" : ": "
                                                + charName.replaceAll(
                                                        "\\s\\s+", " ")));
                        panel.getInfoLabel2().setText(
                                glyphDefinition.getRange());
                    }
                    
                }

                removeAction.setEnabled(enableButtons);
                insertAction.setEnabled(enableButtons);
            }
        }
    }

    private void setListeners() {

        selectionModel.addListSelectionListener(new GlyphSelectionListener());

        filterList.addListEventListener(new ListEventListener<GlyphDefinition>() {
            @Override
            public void listChanged(ListEvent<GlyphDefinition> e) {
                if (selectionModel.isSelectionEmpty() && filterList.size() > 0 ) {
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
                listInSync = true;
                glyphList.clear();
                glyphList.addAll(loader.load().getData());
                panel.setMask(false);
            }
        });
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
