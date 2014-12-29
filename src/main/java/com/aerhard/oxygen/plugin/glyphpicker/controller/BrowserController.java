package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
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

// TODO use accelerators instead of mnemonic keys + add infos in tooltips!
// + move actions to toolbar?

// TODO size in prozent angeben in der config!

// grid size vielleicht in voreinstellungen konfigurbar machen!

public class BrowserController extends Controller {

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

    protected AbstractAction addAction;
    protected AbstractAction insertAction;

    private BrowserControlPanel controlPanel;

    private HighlightButton insertBtn;

    @SuppressWarnings({ "unchecked" })
    public BrowserController(Config config) {

        controlPanel = new BrowserControlPanel();

        panel = new ContainerPanel(controlPanel);

        glyphList = new BasicEventList<GlyphDefinition>();

        JTextField ftTextField = controlPanel.getFtTextField();

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

        sortedList = new SortedList<GlyphDefinition>(glyphList,
                new GlyphComparator());

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
        table.setRowHeight(90);
        table.setTableIconRenderer(new GlyphRendererAdapter(table));

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
        controlPanel.getToggleBtn().setAction(
                new ChangeViewAction(panel, table, list));

        setButtons();

        loader = new GlyphDefinitionLoader();

        selectionModel = new DefaultEventSelectionModel<GlyphDefinition>(
                filterList);
        selectionModel.setSelectionMode(DefaultEventSelectionModel.SINGLE_SELECTION);
        list.setSelectionModel(selectionModel);
        table.setSelectionModel(selectionModel);

        selectionModel.addListSelectionListener(new GlyphSelectionListener());

        setListeners();

    }

    private void setButtons() {
        addAction = new AddToUserCollectionAction();
        addAction.setEnabled(false);
        panel.addToButtonPanel(addAction);

        insertAction = new InsertXmlAction();
        insertAction.setEnabled(false);
        insertBtn = new HighlightButton(insertAction);
        panel.addToButtonPanel(insertBtn);
    }

    public class GlyphComparator implements Comparator<GlyphDefinition> {
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

    private class EditAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        private EditAction() {
            super("Edit");
            putValue(SHORT_DESCRIPTION, "Edit data sources.");
            putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_E));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            List<DataSource> result = new DataSourceEditorController(
                    new DataSourceEditor(), panel).load(dataSourceList.getData());
            
            if (result != null) {
                dataSourceList.getData().clear();
                dataSourceList.getData().addAll(result);
                loadData();
            }
        }
    }

    private class AddToUserCollectionAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        private AddToUserCollectionAction() {
            super("Copy to User Collection");
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
            super("Insert XML");
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
                    } else {
                        String charName = glyphDefinition.getCharName();
                        panel.getInfoLabel().setText(
                                glyphDefinition.getCodePoint()
                                        + (charName == null ? "" : ": "
                                                + charName.replaceAll(
                                                        "\\s\\s+", " ")));
                    }
                }

                addAction.setEnabled(enableButtons);
                insertAction.setEnabled(enableButtons);
            }
        }
    }

    private void setListeners() {

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
        
        if (index > dataSourceList.getSize() -1) {
            JOptionPane.showMessageDialog(panel, "No data source has been found.");
            glyphList.clear();
            return;
        }

        final DataSource ds = dataSourceList.getDataSourceAt(index);

        if (ds == null) {
            JOptionPane.showMessageDialog(panel, "No data source has been found.");
            glyphList.clear();
            return;
        }

        if (isLoading) {
            LOGGER.info("Skipping data loading.");
            return;
        }
        isLoading = true;
        panel.getOverlayable().setOverlayVisible(true);

        SwingWorker<List<GlyphDefinition>, Void> worker = new SwingWorker<List<GlyphDefinition>, Void>() {
            @Override
            public List<GlyphDefinition> doInBackground() {
                return loader.loadData(ds);
            }
            
            @Override
            public void done() {
                try {
                    List<GlyphDefinition> data = get();
                    glyphList.clear();

                    // when loading was successful, set the loading path as
                    // first item in the pathComboModel
                    if (data != null) {
                        glyphList.addAll(data);

                        int index = controlPanel.getDataSourceCombo()
                                .getSelectedIndex();
                        if (index != -1) {
                            dataSourceList.setFirstIndex(index);
                        }
                    }
                } catch (InterruptedException e) {
                    LOGGER.error(e);
                } catch (ExecutionException e) {
                    LOGGER.error(e);
                } finally {
                    isLoading = false;
                    panel.getOverlayable().setOverlayVisible(false);
                }
            }
        };
        worker.execute();
    }

    @Override
    public void saveData() {
    }

    @Override
    public void eventOccured(String type, GlyphDefinition model) {
    }

}
