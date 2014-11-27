package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;

import com.aerhard.oxygen.plugin.glyphpicker.model.Config;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphTableModel;
import com.aerhard.oxygen.plugin.glyphpicker.model.PathComboModel;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphGridModel;
import com.aerhard.oxygen.plugin.glyphpicker.view.BrowserPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphGrid;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphTable;
import com.aerhard.oxygen.plugin.glyphpicker.view.renderer.GlyphShapeRenderer;
import com.jidesoft.swing.AutoCompletionComboBox;

public class BrowserController extends Controller {

    private static final Logger LOGGER = Logger
            .getLogger(BrowserController.class.getName());

    private BrowserPanel browserPanel;
    private GlyphTable table;
    private GlyphTableModel glyphTableModel;
    private GlyphGrid listGrid;
    private GlyphGridModel listGridModel;
    private TableRowSorter<GlyphTableModel> sorter;
    private PathComboModel pathComboModel;
    private GlyphDefinitionLoader dataStore;
    private boolean isLoading = false;

    protected AbstractAction addAction;

    protected AbstractAction insertAction;

    public BrowserController(Config config) {

        browserPanel = new BrowserPanel();

        glyphTableModel = new GlyphTableModel();

        table = new GlyphTable(glyphTableModel);

        // table.setTableIconRenderer(new TableIconBitmapRenderer());
        // table.setTableIconRenderer(new TableIconFontRenderer());
        table.setTableIconRenderer(new GlyphShapeRenderer());

        sorter = new TableRowSorter<GlyphTableModel>(glyphTableModel);
        table.setRowSorter(sorter);

        // TODO share model // synchronize filters // share selection model
        listGridModel = new GlyphGridModel();
        listGrid = new GlyphGrid(listGridModel);

        GlyphShapeRenderer r = new GlyphShapeRenderer();
        r.setPreferredSize(new Dimension(90, 90));
        listGrid.setCellRenderer(r);

        browserPanel.setListComponent(table);

        pathComboModel = config.getPaths();
        browserPanel.getPathCombo().setModel(pathComboModel);

        browserPanel.getRangeCombo().setAction(new FilterAction());
        browserPanel.getClassCombo().setAction(new FilterAction());
        browserPanel.getViewCombo().setAction(new ChangeViewAction());
        browserPanel.getBtnLoad().setAction(new LoadDataAction());

        addAction = new AddToUserListAction();
        insertAction = new InsertXmlAction();

        addAction.setEnabled(false);
        insertAction.setEnabled(false);

        browserPanel.getBtnAdd().setAction(addAction);
        browserPanel.getBtnInsert().setAction(insertAction);

        dataStore = new GlyphDefinitionLoader();

        ListSelectionModel selectionModel = listGrid.getSelectionModel();
        table.setSelectionModel(selectionModel);

        selectionModel.addListSelectionListener(new GlyphSelectionListener());

        setListeners();

    }

    public BrowserPanel getPanel() {
        return browserPanel;
    }

    public GlyphTableModel getTableModel() {
        return glyphTableModel;
    }

    private void insertGlyphFromBrowser() {
        int row = table.getSelectedRow();
        if (row != -1) {
            GlyphDefinition selectedModel = getTableModel().getModelAt(
                    table.convertRowIndexToModel(row));
            if (selectedModel != null) {
                fireEvent("insert", selectedModel);
            }
        }
    }

    private class FilterAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            newFilter();
        }
    }

    private class ChangeViewAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            int index = ((JComboBox<?>) e.getSource()).getSelectedIndex();

            // TODO add condition: if selection then ensure selection visible
            // else
            // some other row

            if (index == 0) {
                browserPanel.setListComponent(table);
                table.scrollRectToVisible(new Rectangle(table.getCellRect(1000,
                        0, true)));
            } else {

                browserPanel.setListComponent(listGrid);

                // TODO put this in a listener and call after component is
                // initiated
                // listGrid.ensureIndexIsVisible(1000);
                // listGrid.scrollRectToVisible(listGrid.getCellBounds(1000,
                // 1000));

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

    private class AddToUserListAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        private AddToUserListAction() {
            super("Add to Collection");
            putValue(SHORT_DESCRIPTION,
                    "Add the selected glyph to the user collection.");
            putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_A));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = table.getSelectedRow();
            if (row != -1) {
                GlyphDefinition selectedModel = getTableModel().getModelAt(
                        table.convertRowIndexToModel(row));
                if (selectedModel != null) {
                    fireEvent("export", selectedModel);
                }
            }
        }
    }

    private class GlyphSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent event) {
            if (!event.getValueIsAdjusting()) {
                // int index = ((DefaultListSelectionModel) event.getSource())
                // .getMinSelectionIndex();

                // TODO make this work with filters and sorting in table and list

                
//                int index = event.getFirstIndex();
                int index = listGrid.getSelectedIndex();
                
//                index = table.convertRowIndexToModel(index);
                
                Boolean enableButtons = (index != -1);

                // GlyphDefinition model = getTableModel().getModelAt(
                // table.convertRowIndexToModel(index));

                GlyphDefinition model = (index == -1) ? null : listGridModel
                        .getElementAt(index);

                if (model == null) {
                    browserPanel.getInfoLabel().setText(null);
                } else {
                    browserPanel.getInfoLabel().setText(
                            model.getCodePoint() + ": " + model.getCharName());
                }

                addAction.setEnabled(enableButtons);
                insertAction.setEnabled(enableButtons);
            }
        }
    }

    private void setListeners() {
        // TODO change to document listener
        browserPanel.getPathCombo().getEditor().getEditorComponent()
                .addKeyListener(new KeyAdapter() {
                    public void keyReleased(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            loadData();
                        }
                    }
                });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    browserPanel.getBtnInsert().highlight();
                    insertGlyphFromBrowser();
                }
            }
        });

        glyphTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                updateCombo(browserPanel.getRangeCombo(),
                        glyphTableModel.getUniqueRanges());
                updateCombo(browserPanel.getClassCombo(),
                        glyphTableModel.getUniqueClasses());
                newFilter();
            }
        });

    }

    private void newFilter() {

        final String rangeValue = browserPanel.getRangeCombo()
                .getSelectedItem().toString();
        final String classValue = browserPanel.getClassCombo()
                .getSelectedItem().toString();

        RowFilter<GlyphTableModel, Integer> rangeFilter = new RowFilter<GlyphTableModel, Integer>() {
            public boolean include(
                    Entry<? extends GlyphTableModel, ? extends Integer> entry) {
                GlyphDefinition model = (GlyphDefinition) entry.getValue(0);
                String entryRange = model.getRange();
                if (entryRange != null && entryRange.startsWith(rangeValue)) {
                    return true;
                }
                return false;
            }
        };

        RowFilter<GlyphTableModel, Integer> classFilter = new RowFilter<GlyphTableModel, Integer>() {
            public boolean include(
                    Entry<? extends GlyphTableModel, ? extends Integer> entry) {
                GlyphDefinition model = (GlyphDefinition) entry.getValue(0);
                List<String> entryClasses = model.getClasses();
                for (String entryClass : entryClasses) {
                    if (entryClass.startsWith(classValue)) {
                        return true;
                    }
                }
                return false;
            }
        };

        if (!rangeValue.equals("") && !classValue.equals("")) {
            List<RowFilter<GlyphTableModel, Integer>> filters = new ArrayList<RowFilter<GlyphTableModel, Integer>>();
            filters.add(classFilter);
            filters.add(rangeFilter);
            sorter.setRowFilter(RowFilter.andFilter(filters));
        } else if (!rangeValue.equals("")) {
            sorter.setRowFilter(rangeFilter);
        } else if (!classValue.equals("")) {
            sorter.setRowFilter(classFilter);
        } else {
            sorter.setRowFilter(null);
        }

    }

    @SuppressWarnings("unchecked")
    private void updateCombo(AutoCompletionComboBox combo, List<String> data) {
        Collections.sort(data);
        data.add(0, "");
        String[] dataArray = data.toArray(new String[data.size()]);
        combo.setModel(new DefaultComboBoxModel<String>(dataArray));
    }

    @Override
    public void loadData() {

        final String path = browserPanel.getPathCombo().getSelectedItem()
                .toString();

        if (isLoading) {
            LOGGER.info("Skipping data loading.");
            return;
        }
        isLoading = true;
        // mainPanel.getLoadingMask().start();

        SwingWorker<List<GlyphDefinition>, Void> worker = new SwingWorker<List<GlyphDefinition>, Void>() {
            @Override
            public List<GlyphDefinition> doInBackground() {
                return dataStore.loadData(path);
            }

            @Override
            public void done() {
                try {
                    List<GlyphDefinition> data = get();

                    // TODO create shared model
                    glyphTableModel.setData(data);
                    listGridModel.setData(data);

                    // when loading was successful, set the loading path as
                    // first item in the pathComboModel
                    if (data != null) {
                        pathComboModel.setFirstItem(path);
                    }
                    // mainPanel.getLoadingMask().stop();
                    isLoading = false;
                } catch (InterruptedException e) {
                    LOGGER.error(e);
                } catch (ExecutionException e) {
                    LOGGER.error(e);
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
