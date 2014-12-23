package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.DefaultEventListModel;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

import com.aerhard.oxygen.plugin.glyphpicker.model.Config;
import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.model.DataSourceList;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.view.BrowserPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphGrid;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphTable;
import com.aerhard.oxygen.plugin.glyphpicker.view.renderer.GlyphShapeRenderer;

public class BrowserController extends Controller {

    private static final Logger LOGGER = Logger
            .getLogger(BrowserController.class.getName());

    private ListSelectionModel selectionModel;

    private EventList<GlyphDefinition> glyphList;
    private FilterList<GlyphDefinition> filterList;

    private BrowserPanel browserPanel;
    private GlyphTable table;
    private GlyphGrid list;
    private DataSourceList dataSourceList;
    private GlyphDefinitionLoader loader;
    private boolean isLoading = false;

    protected AbstractAction transferAction;
    protected AbstractAction insertAction;

    private int activeListIndex;

    @SuppressWarnings({ "unchecked" })
    public BrowserController(Config config) {

        browserPanel = new BrowserPanel();

        glyphList = new BasicEventList<GlyphDefinition>();
        JTextField ftTextField = browserPanel.getFtTextField();

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

        filterList = new FilterList<GlyphDefinition>(glyphList, filter);

        list = new GlyphGrid(new DefaultEventListModel<GlyphDefinition>(
                filterList));

       
//        GlyphTextRenderer r = new GlyphTextRenderer();
//        GlyphBitmapRenderer r = new GlyphBitmapRenderer();
        GlyphShapeRenderer r = new GlyphShapeRenderer();
        r.setPreferredSize(new Dimension(90, 90));
        list.setCellRenderer(r);

        TableFormat<GlyphDefinition> tf = new GlyphTableFormat();
        DefaultEventTableModel<GlyphDefinition> tableListModel = new DefaultEventTableModel<GlyphDefinition>(
                filterList, tf);
        table = new GlyphTable(tableListModel);

        // table.setTableIconRenderer(new TableIconBitmapRenderer());
        // table.setTableIconRenderer(new TableIconFontRenderer());
        table.setTableIconRenderer(new GlyphShapeRenderer());

        // sorter = new TableRowSorter<GlyphTableModel>(glyphTableModel);
        // sorter = new TableRowSorter<SharedListModel>(sharedListModel);
        // table.setRowSorter(sorter);

        browserPanel.setListComponent(list);

//        EventList<String> rangesNonUnique = new GlyphToRangeList(filterList);
//        UniqueList<String> rangesEventList = new UniqueList<String>(
//                rangesNonUnique);
//        browserPanel.getRangeCombo().setModel(
//                new DefaultEventComboBoxModel<String>(rangesEventList));

        // EventList<List<String>> classesNonUnique = new
        // GlyphToClassList(filterList);
        // UniqueList<String> classesEventList = new
        // UniqueList<String>(classesNonUnique);
        // DefaultEventComboBoxModel<String> classComboModel = new
        // DefaultEventComboBoxModel<String>(classesEventList);
        // browserPanel.getRangeCombo().setModel(rangeComboModel);

        dataSourceList = config.getDataSources();
        browserPanel.getPathCombo().setModel(dataSourceList);

//        browserPanel.getRangeCombo().setAction(new FilterAction());
//        browserPanel.getClassCombo().setAction(new FilterAction());
        browserPanel.getViewCombo().setAction(new ChangeViewAction());
        browserPanel.getBtnLoad().setAction(new LoadDataAction());

        transferAction = new AddToUserCollectionAction();
        insertAction = new InsertXmlAction();

        transferAction.setEnabled(false);
        insertAction.setEnabled(false);

        browserPanel.getBtnAdd().setAction(transferAction);
        browserPanel.getBtnInsert().setAction(insertAction);

        loader = new GlyphDefinitionLoader();

        selectionModel = new DefaultEventSelectionModel<GlyphDefinition>(
                filterList);
        list.setSelectionModel(selectionModel);

        table.setSelectionModel(selectionModel);

        selectionModel.addListSelectionListener(new GlyphSelectionListener());

        setListeners();

    }

    private class GlyphTableFormat implements TableFormat<GlyphDefinition> {

        public int getColumnCount() {
            return 2;
        }

        public String getColumnName(int column) {
            if (column == 0)
                return "Glyph";
            else if (column == 1)
                return "Description";

            throw new IllegalStateException();
        }

        public Object getColumnValue(GlyphDefinition baseObject, int column) {
            GlyphDefinition issue = baseObject;

            if (column == 0)
                return issue;
            else if (column == 1)
                return issue;

            throw new IllegalStateException();
        }

    }

//    private class GlyphToRangeList extends
//            TransformedList<GlyphDefinition, String> {
//
//        public GlyphToRangeList(EventList<GlyphDefinition> source) {
//            super(source);
//            source.addListEventListener(this);
//        }
//
//        public String get(int index) {
//            return source.get(index).getRange();
//        }
//
//        /**
//         * When the source issues list changes, propogate the exact same changes
//         * for the users list.
//         */
//        public void listChanged(ListEvent<GlyphDefinition> listChanges) {
//            updates.forwardEvent(listChanges);
//        }
//
//        /** {@inheritDoc} */
//        protected boolean isWritable() {
//            return false;
//        }
//    }
//
//    private class GlyphToClassList extends
//            TransformedList<GlyphDefinition, List<String>> {
//
//        public GlyphToClassList(EventList<GlyphDefinition> source) {
//            super(source);
//            source.addListEventListener(this);
//        }
//
//        public List<String> get(int index) {
//            return source.get(index).getClasses();
//        }
//
//        /**
//         * When the source issues list changes, propogate the exact same changes
//         * for the users list.
//         */
//        public void listChanged(ListEvent<GlyphDefinition> listChanges) {
//            updates.forwardEvent(listChanges);
//        }
//
//        /** {@inheritDoc} */
//        protected boolean isWritable() {
//            return false;
//        }
//    }

    public class GlyphTextFilterator implements TextFilterator<GlyphDefinition> {

        @Override
        public void getFilterStrings(List<String> baseList,
                GlyphDefinition element) {
            baseList.add(element.getId());
            baseList.add(element.getRange());
            baseList.add(element.getCharName());
            baseList.add(element.getCodePoint());
        }
    }

    public BrowserPanel getPanel() {
        return browserPanel;
    }

    private void insertGlyphFromBrowser() {
        int row = selectionModel.getAnchorSelectionIndex();
        if (row != -1) {
            GlyphDefinition selectedModel = filterList.get(row);
            if (selectedModel != null) {
                fireEvent("insert", selectedModel);
            }
        }
    }

//    private class FilterAction extends AbstractAction {
//        private static final long serialVersionUID = 1L;
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            // newFilter();
//        }
//    }

    public class ChangeViewAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            int comboIndex = ((JComboBox<?>) e.getSource()).getSelectedIndex();
            if (activeListIndex != comboIndex) {
                if (comboIndex == 1) {
                    // NB get the old component's top row before the component
                    // is replaced!
                    int row = list.getTopVisibleRow();
                    browserPanel.setListComponent(table);
                    browserPanel.getInfoLabel().setVisible(false);
                    browserPanel.revalidate();
                    table.setTopVisibleRow(row);
                } else {
                    // NB get the old component's top row before the component
                    // is replaced!
                    int row = table.getTopVisibleRow();
                    browserPanel.setListComponent(list);
                    browserPanel.getInfoLabel().setVisible(true);
                    browserPanel.revalidate();
                    list.setTopVisibleRow(row);
                }
                activeListIndex = comboIndex;
            }
        }
    }

    private class LoadDataAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        private LoadDataAction() {
            super("Load");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            loadData();
        }
    }

    private class InsertXmlAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        private InsertXmlAction() {
            super("Insert XML");
            putValue(SHORT_DESCRIPTION,
                    "Insert the selected glyph to the document.");
            putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_I));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            insertGlyphFromBrowser();
        }
    }

    private class AddToUserCollectionAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        private AddToUserCollectionAction() {
            super("Transfer to User Collection");
            putValue(SHORT_DESCRIPTION,
                    "Add the selected glyph to the user collection.");
            putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_A));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = selectionModel.getAnchorSelectionIndex();
            if (row != -1) {
                GlyphDefinition selectedModel = filterList.get(row);
                if (selectedModel != null) {
                    fireEvent("transferToUserCollection", selectedModel);
                }
            }
        }
    }

    private class GlyphSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent event) {
            if (!event.getValueIsAdjusting()) {

                @SuppressWarnings("unchecked")
                int index = ((DefaultEventSelectionModel<GlyphDefinition>) event
                        .getSource()).getAnchorSelectionIndex();

                Boolean enableButtons = (index != -1);

                GlyphDefinition model = (index == -1) ? null : filterList
                        .get(index);

                if (model == null) {
                    browserPanel.getInfoLabel().setText(null);
                } else {
                    browserPanel.getInfoLabel().setText(
                            model.getCodePoint() + ": " + model.getCharName());
                }

                transferAction.setEnabled(enableButtons);
                insertAction.setEnabled(enableButtons);
            }
        }
    }

    private void setListeners() {

//        // TODO change to document listener
//        browserPanel.getPathCombo().getEditor().getEditorComponent()
//                .addKeyListener(new KeyAdapter() {
//                    public void keyReleased(KeyEvent e) {
//                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                            loadData();
//                        }
//                    }
//                });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    browserPanel.getBtnInsert().highlight();
                    insertGlyphFromBrowser();
                }
            }
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    browserPanel.getBtnInsert().highlight();
                    insertGlyphFromBrowser();
                }
            }
        });

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

//    private void newFilter() {
//
//        // final String rangeValue = browserPanel.getRangeCombo()
//        // .getSelectedItem().toString();
//        final String classValue = browserPanel.getClassCombo()
//                .getSelectedItem().toString();
//
//        // RowFilter<SharedListModel, Integer> rangeFilter = new
//        // RowFilter<SharedListModel, Integer>() {
//        // public boolean include(
//        // Entry<? extends SharedListModel, ? extends Integer> entry) {
//        // GlyphDefinition model = (GlyphDefinition) entry.getValue(0);
//        // String entryRange = model.getRange();
//        // if (entryRange != null && entryRange.startsWith(rangeValue)) {
//        // return true;
//        // }
//        // return false;
//        // }
//        // };
//
////        RowFilter<SharedListModel, Integer> classFilter = new RowFilter<SharedListModel, Integer>() {
////            public boolean include(
////                    Entry<? extends SharedListModel, ? extends Integer> entry) {
////                GlyphDefinition model = (GlyphDefinition) entry.getValue(0);
////                List<String> entryClasses = model.getClasses();
////                for (String entryClass : entryClasses) {
////                    if (entryClass.startsWith(classValue)) {
////                        return true;
////                    }
////                }
////                return false;
////            }
////        };
//
//        // if (!rangeValue.equals("") && !classValue.equals("")) {
//        // List<RowFilter<SharedListModel, Integer>> filters = new
//        // ArrayList<RowFilter<SharedListModel, Integer>>();
//        // filters.add(classFilter);
//        // filters.add(rangeFilter);
//        // sorter.setRowFilter(RowFilter.andFilter(filters));
//        // } else if (!rangeValue.equals("")) {
//        // sorter.setRowFilter(rangeFilter);
//        // } else
//        // if (!classValue.equals("")) {
//        // sorter.setRowFilter(classFilter);
//        // } else {
//        // sorter.setRowFilter(null);
//        // }
//
//    }

//    @SuppressWarnings("unchecked")
//    private void updateCombo(AutoCompletionComboBox combo, List<String> data) {
//        Collections.sort(data);
//        data.add(0, "");
//        String[] dataArray = data.toArray(new String[data.size()]);
//        combo.setModel(new DefaultComboBoxModel<String>(dataArray));
//    }

    @Override
    public void loadData() {

        final String path;
        
        int index = browserPanel.getPathCombo().getSelectedIndex();
        
        System.out.println(index);
        if (index == -1) {
            index = 0;
        }
        
        DataSource ds = dataSourceList.getDataSourceAt(index);
        
        path = (ds != null) ? ds.getPath() : "";

        if (isLoading) {
            LOGGER.info("Skipping data loading.");
            return;
        }
        isLoading = true;
        // mainPanel.getLoadingMask().start();

        SwingWorker<List<GlyphDefinition>, Void> worker = new SwingWorker<List<GlyphDefinition>, Void>() {
            @Override
            public List<GlyphDefinition> doInBackground() {
                return loader.loadData(path);
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
                        
                        int index = browserPanel.getPathCombo().getSelectedIndex();
                        if (index != -1) {
                            dataSourceList.setFirstIndex(index);
                        }
                        
                    }
                    // mainPanel.getLoadingMask().stop();
                } catch (InterruptedException e) {
                    LOGGER.error(e);
                } catch (ExecutionException e) {
                    LOGGER.error(e);
                } finally {
                    isLoading = false;
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
