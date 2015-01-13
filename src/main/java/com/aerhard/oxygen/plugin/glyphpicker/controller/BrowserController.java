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
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.DefaultEventListModel;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

import com.aerhard.oxygen.plugin.glyphpicker.action.ChangeViewAction;
import com.aerhard.oxygen.plugin.glyphpicker.controller.editor.DataSourceEditorController;
import com.aerhard.oxygen.plugin.glyphpicker.model.Config;
import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.model.DataSourceList;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.view.BrowserControlPanel;
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

    private BrowserControlPanel controlPanel;

    private HighlightButton insertBtn;

    private JTextField ftTextField;

    @SuppressWarnings({ "unchecked" })
    public BrowserController(Config config) {

        controlPanel = new BrowserControlPanel();

        panel = new ContainerPanel(controlPanel);

        glyphList = new BasicEventList<GlyphDefinition>();

        ftTextField = controlPanel.getFulltextTextField();

        // EventComboBoxModel<GlyphDefinition> ftComboModel = new
        // EventComboBoxModel<GlyphDefinition>(gList);
        // final JComboBox ftTextField = browserPanel.getFtTextField();
        // ftTextField.setModel(ftComboModel);
        //
        // AutoCompleteSupport<GlyphDefinition> autocomplete;
        //
        // javax.swing.SwingUtilities.invokeLater(new Runnable() {
        // @Override
        // public void run() {
        // AutoCompleteSupport<GlyphDefinition> autocomplete =
        // AutoCompleteSupport.install(ftTextField, gList, new
        // GlyphTextFilterator());
        // // Try without the filterator to see the difference.
        // //AutoCompleteSupport autocomplete =
        // AutoCompleteSupport.install(stationsComboBox, stations);
        // autocomplete.setFilterMode(TextMatcherEditor.CONTAINS);
        //
        // }
        // });

        MatcherEditor<GlyphDefinition> filter = new TextComponentMatcherEditor<GlyphDefinition>(
                ftTextField, new GlyphTextFilterator());

        // Matcher<GlyphDefinition> glyphFilter = new Matcher<GlyphDefinition>()
        // {
        // public boolean matches(GlyphDefinition d) {
        // return d.getRange().startsWith("A");
        // }
        // };

        sortedList = new SortedList<GlyphDefinition>(glyphList, null);

        filterList = new FilterList<GlyphDefinition>(sortedList, filter);

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

        // sorter = new TableRowSorter<GlyphTableModel>(glyphTableModel);
        // sorter = new TableRowSorter<SharedListModel>(sharedListModel);
        // table.setRowSorter(sorter);

        panel.setListComponent(list);

        // EventList<String> rangesNonUnique = new GlyphToRangeList(filterList);
        // UniqueList<String> rangesEventList = new UniqueList<String>(
        // rangesNonUnique);
        // browserPanel.getRangeCombo().setModel(
        // new DefaultEventComboBoxModel<String>(rangesEventList));

        // EventList<List<String>> classesNonUnique = new
        // GlyphToClassList(filterList);
        // UniqueList<String> classesEventList = new
        // UniqueList<String>(classesNonUnique);
        // DefaultEventComboBoxModel<String> classComboModel = new
        // DefaultEventComboBoxModel<String>(classesEventList);
        // browserPanel.getRangeCombo().setModel(rangeComboModel);

        dataSourceList = config.getDataSources();
        controlPanel.getDataSourceCombo().setModel(dataSourceList);

        controlPanel.getBtnConfigure().setAction(new EditAction());

        // browserPanel.getRangeCombo().setAction(new FilterAction());
        // browserPanel.getClassCombo().setAction(new FilterAction());

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

        selectionModel.addListSelectionListener(new GlyphSelectionListener());

        filterList
                .addListEventListener(new ListEventListener<GlyphDefinition>() {
                    @Override
                    public void listChanged(ListEvent<GlyphDefinition> e) {
                        if (selectionModel.isSelectionEmpty()
                                && filterList.size() > 0) {
                            selectionModel.setSelectionInterval(0, 0);
                        }

                        // reevaluate list layout
                        if (list.isVisible()) {
                            list.fixRowCountForVisibleColumns();
                        }

                    }
                });

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

    // private class GlyphToRangeList extends
    // TransformedList<GlyphDefinition, String> {
    //
    // public GlyphToRangeList(EventList<GlyphDefinition> source) {
    // super(source);
    // source.addListEventListener(this);
    // }
    //
    // public String get(int index) {
    // return source.get(index).getRange();
    // }
    //
    // /**
    // * When the source issues list changes, propogate the exact same changes
    // * for the users list.
    // */
    // public void listChanged(ListEvent<GlyphDefinition> listChanges) {
    // updates.forwardEvent(listChanges);
    // }
    //
    // /** {@inheritDoc} */
    // protected boolean isWritable() {
    // return false;
    // }
    // }
    //
    // private class GlyphToClassList extends
    // TransformedList<GlyphDefinition, List<String>> {
    //
    // public GlyphToClassList(EventList<GlyphDefinition> source) {
    // super(source);
    // source.addListEventListener(this);
    // }
    //
    // public List<String> get(int index) {
    // return source.get(index).getClasses();
    // }
    //
    // /**
    // * When the source issues list changes, propogate the exact same changes
    // * for the users list.
    // */
    // public void listChanged(ListEvent<GlyphDefinition> listChanges) {
    // updates.forwardEvent(listChanges);
    // }
    //
    // /** {@inheritDoc} */
    // protected boolean isWritable() {
    // return false;
    // }
    // }

    public ContainerPanel getPanel() {
        return panel;
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

    // private class FilterAction extends AbstractAction {
    // private static final long serialVersionUID = 1L;
    //
    // @Override
    // public void actionPerformed(ActionEvent e) {
    // // newFilter();
    // }
    // }

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

                addAction.setEnabled(enableButtons);
                insertAction.setEnabled(enableButtons);
            }
        }
    }

    private void setListeners() {

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

        KeyAdapter ftKeyAdapter = new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    insertBtn.highlight();
                    insertGlyph();
                }

                else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    int i = selectionModel.getAnchorSelectionIndex();
                    if (i > 0) {
                        selectionModel.setSelectionInterval(i - 1, i - 1);
                    }
                }

                else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    int i = selectionModel.getAnchorSelectionIndex();
                    int size = filterList.size();
                    if (i < size - 1) {
                        selectionModel.setSelectionInterval(i + 1, i + 1);
                    }
                }

            }
        };

        ftTextField.addKeyListener(ftKeyAdapter);

        // sharedListModel.addTableModelListener(new TableModelListener() {
        // @Override
        // public void tableChanged(TableModelEvent e) {
        // // updateCombo(browserPanel.getRangeCombo(),
        // // sharedListModel.getUniqueRanges());
        // updateCombo(browserPanel.getClassCombo(),
        // sharedListModel.getUniqueClasses());
        // newFilter();
        // }
        // });

    }

    // private void newFilter() {
    //
    // // final String rangeValue = browserPanel.getRangeCombo()
    // // .getSelectedItem().toString();
    // final String classValue = browserPanel.getClassCombo()
    // .getSelectedItem().toString();
    //
    // // RowFilter<SharedListModel, Integer> rangeFilter = new
    // // RowFilter<SharedListModel, Integer>() {
    // // public boolean include(
    // // Entry<? extends SharedListModel, ? extends Integer> entry) {
    // // GlyphDefinition model = (GlyphDefinition) entry.getValue(0);
    // // String entryRange = model.getRange();
    // // if (entryRange != null && entryRange.startsWith(rangeValue)) {
    // // return true;
    // // }
    // // return false;
    // // }
    // // };
    //
    // // RowFilter<SharedListModel, Integer> classFilter = new
    // RowFilter<SharedListModel, Integer>() {
    // // public boolean include(
    // // Entry<? extends SharedListModel, ? extends Integer> entry) {
    // // GlyphDefinition model = (GlyphDefinition) entry.getValue(0);
    // // List<String> entryClasses = model.getClasses();
    // // for (String entryClass : entryClasses) {
    // // if (entryClass.startsWith(classValue)) {
    // // return true;
    // // }
    // // }
    // // return false;
    // // }
    // // };
    //
    // // if (!rangeValue.equals("") && !classValue.equals("")) {
    // // List<RowFilter<SharedListModel, Integer>> filters = new
    // // ArrayList<RowFilter<SharedListModel, Integer>>();
    // // filters.add(classFilter);
    // // filters.add(rangeFilter);
    // // sorter.setRowFilter(RowFilter.andFilter(filters));
    // // } else if (!rangeValue.equals("")) {
    // // sorter.setRowFilter(rangeFilter);
    // // } else
    // // if (!classValue.equals("")) {
    // // sorter.setRowFilter(classFilter);
    // // } else {
    // // sorter.setRowFilter(null);
    // // }
    //
    // }

    // @SuppressWarnings("unchecked")
    // private void updateCombo(AutoCompletionComboBox combo, List<String> data)
    // {
    // Collections.sort(data);
    // data.add(0, "");
    // String[] dataArray = data.toArray(new String[data.size()]);
    // combo.setModel(new DefaultComboBoxModel<String>(dataArray));
    // }

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
//        private JDialog dialog;

        public LoadWorker(DataSource dataSource) {
            this.dataSource = dataSource;
//            dialog = new JDialog();
//            dialog.setLocationRelativeTo(panel);
//            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//
//            });
        }

        @Override
        protected List<GlyphDefinition> doInBackground() {
//            dialog.setVisible(true);
            return loader.loadData(dataSource);
        }

        @Override
        protected void done() {
            try {
                processLoadedData(get());
            } catch (InterruptedException e) {
                LOGGER.error(e);
            } catch (ExecutionException e) {
                LOGGER.error(e);
            } finally {
                isLoading = false;
                panel.setMask(false);
//                dialog.dispose();
            }
        }
    }

    private void processLoadedData(List<GlyphDefinition> data) {

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
