package com.aerhard.oxygen.plugin.glyphpicker.controller.browser;

import java.awt.AWTEvent;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import com.aerhard.oxygen.plugin.glyphpicker.action.AddToUserCollectionAction;
import com.aerhard.oxygen.plugin.glyphpicker.action.EditAction;
import com.aerhard.oxygen.plugin.glyphpicker.controller.TabController;
import com.aerhard.oxygen.plugin.glyphpicker.controller.GlyphSelectionChangeHandler;
import com.aerhard.oxygen.plugin.glyphpicker.model.Config;
import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.model.DataSourceList;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.view.ContainerPanel;

public class BrowserController extends TabController {

    private DataSourceList dataSourceList;

    private AbstractAction addAction;

    private TeiLoadWorker teiLoadWorker = null;
    private PropertyChangeListener teiLoadListener;

    public BrowserController(ContainerPanel panel, Config config) {

        super(panel, config.getBrowserSearchFieldScopeIndex(), config
                .getBrowserViewIndex());

        dataSourceList = config.getDataSources();
        controlPanel.getDataSourceCombo().setModel(dataSourceList);

        controlPanel.getBtnConfigure().setAction(
                new EditAction(this, panel, dataSourceList));

        setActions();

        setListeners();

    }

    private void setActions() {
        controlPanel.addToToolbar(insertBtn, 0);

        addAction = new AddToUserCollectionAction(this, selectionModel);
        addAction.setEnabled(false);
        controlPanel.addToToolbar(addAction, 1);
    }

    private void setListeners() {

        Set<Action> selectionDependentActions = new HashSet<Action>();
        selectionDependentActions.add(addAction);
        selectionDependentActions.add(insertAction);

        selectionModel
                .addListSelectionListener(new GlyphSelectionChangeHandler(panel
                        .getInfoLabel(), sortedList, filterList,
                        selectionDependentActions));

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

    }

    public void loadData() {

        int index = controlPanel.getDataSourceCombo().getSelectedIndex();

        if (index == -1) {
            index = 0;
        }

        if (index > dataSourceList.getSize() - 1) {
            showNoDataSourceDialog();
            return;
        }

        DataSource dataSource = dataSourceList.getDataSourceAt(index);

        if (dataSource == null) {
            showNoDataSourceDialog();
            return;
        }

        cancelTeiLoadWorker();
        cancelBitmapLoadWorker();

        panel.setMask(true);

        teiLoadWorker = new TeiLoadWorker(dataSource);

        teiLoadListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("state".equals(e.getPropertyName().toString())
                        && "DONE".equals(e.getNewValue().toString())) {
                    displayLoadedData(teiLoadWorker.getResult());
                }
            }
        };

        teiLoadWorker.addPropertyChangeListener(teiLoadListener);

        teiLoadWorker.execute();

    }

    private void showNoDataSourceDialog() {
        JOptionPane.showMessageDialog(
                panel,
                ResourceBundle.getBundle("GlyphPicker").getString(this.getClass().getSimpleName()
                        + ".noDataSource"));
        glyphList.clear();
    }

    public void cancelTeiLoadWorker() {
        if (teiLoadWorker != null) {
            // teiLoadWorker.shutdownExecutor();
            teiLoadWorker.cancel(true);
            teiLoadWorker.removePropertyChangeListener(teiLoadListener);
            teiLoadWorker = null;
        }
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

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if ("copyToUserCollection".equals(e.getPropertyName())
                || "insert".equals(e.getPropertyName())) {
            pcs.firePropertyChange(e);
        }

        else if ("editChanges".equals(e.getPropertyName())) {
            loadData();
        }

    }

}
