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
package de.badw.strauss.glyphpicker.controller.alltab;

import de.badw.strauss.glyphpicker.controller.action.MemorizeAction;
import de.badw.strauss.glyphpicker.controller.action.InsertXmlAction;
import de.badw.strauss.glyphpicker.controller.bitmap.ImageCache;
import de.badw.strauss.glyphpicker.controller.settings.SettingsDialogAction;
import de.badw.strauss.glyphpicker.controller.tab.AbstractTabController;
import de.badw.strauss.glyphpicker.controller.tab.GlyphSelectionChangeHandler;
import de.badw.strauss.glyphpicker.model.Config;
import de.badw.strauss.glyphpicker.model.DataSource;
import de.badw.strauss.glyphpicker.model.DataSourceList;
import de.badw.strauss.glyphpicker.model.GlyphDefinition;
import de.badw.strauss.glyphpicker.view.TabPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * The allTab tab controller.
 */
public class AllTabController extends AbstractTabController {

    /**
     * The list of all data sources.
     */
    private final DataSourceList dataSourceList;

    /**
     * The add action.
     */
    private AbstractAction addAction;

    /**
     * The worker loading glyph definitions from a document or online resource.
     */
    private TeiLoadWorker teiLoadWorker = null;

    /**
     * A property change listener attached to the teiLoadWorker.
     */
    private PropertyChangeListener teiLoadListener;

    /**
     * Instantiates a new AllTabController.
     *
     * @param panel      the allTab tab's container panel
     * @param config     the plugin config
     * @param imageCache the image cache
     */
    public AllTabController(TabPanel panel, Config config, ImageCache imageCache) {

        super(panel, config.getAllTabSearchFieldScopeIndex(), config
                .getAllTabViewIndex(), imageCache);

        dataSourceList = config.getGlyphTables();
        controlPanel.getDataSourceCombo().setModel(dataSourceList);

        setAdditionalActions();

        setAdditionalListeners();

    }

    /**
     * gets the glyph table list
     *
     * @return the list
     */
    public DataSourceList getDataSourceList() {
        return dataSourceList;
    }

    /**
     * Sets the allTab panel's additional actions.
     */
    private void setAdditionalActions() {
        addAction = new MemorizeAction(tabPanel, this, selectionModel);
        addAction.setEnabled(false);
        controlPanel.addToToolbar(addAction, 0);
    }

    /**
     * Sets the panel' additional listeners.
     */
    private void setAdditionalListeners() {

        Set<Action> selectionDependentActions = new HashSet<Action>();
        selectionDependentActions.add(addAction);
        selectionDependentActions.add(insertAction);

        selectionModel
                .addListSelectionListener(new GlyphSelectionChangeHandler(tabPanel
                        .getInfoLabel(), sortedList, filterList,
                        selectionDependentActions));

        controlPanel.getDataSourceCombo().addActionListener(
                new AbstractAction() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getModifiers() == InputEvent.BUTTON1_MASK) {
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

    /**
     * Loads the TEI data.
     */
    public final void loadData() {

        if (dataSourceList.getSize() == 0) {
            showNoDataSourceMessage();
            return;
        }
        
        int index = controlPanel.getDataSourceCombo().getSelectedIndex();

        if (index == -1 || index > dataSourceList.getSize() - 1) {
            controlPanel.getDataSourceCombo().setSelectedIndex(0);
            index = 0;
        }

        DataSource dataSource = dataSourceList.getDataSourceAt(index);

        cancelTeiLoadWorker();
        cancelBitmapLoadWorker();

        tabPanel.setMask(true);

        teiLoadWorker = new TeiLoadWorker(dataSource);

        teiLoadListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("state".equals(e.getPropertyName())
                        && "DONE".equals(e.getNewValue().toString())) {
                    displayLoadedData(teiLoadWorker.getResult());
                }
            }
        };

        teiLoadWorker.addPropertyChangeListener(teiLoadListener);

        teiLoadWorker.execute();

    }

    /**
     * Shows a popup frame with the message that no data source has been found.
     */
    private void showNoDataSourceMessage() {
        JOptionPane.showMessageDialog(
                tabPanel,
                ResourceBundle.getBundle("GlyphPicker").getString(
                        this.getClass().getSimpleName() + ".noDataSource"));
        glyphList.clear();
    }

    /**
     * Cancels loading of the TEI data.
     */
    public void cancelTeiLoadWorker() {
        if (teiLoadWorker != null) {
            teiLoadWorker.cancel(true);
            teiLoadWorker.removePropertyChangeListener(teiLoadListener);
            teiLoadWorker = null;
        }
    }

    /**
     * Displays the loaded data.
     *
     * @param data the data
     */
    private void displayLoadedData(List<GlyphDefinition> data) {

        glyphList.clear();
        selectionModel.clearSelection();
        table.scrollRectToVisible(new Rectangle(0, 0));
        list.scrollRectToVisible(new Rectangle(0, 0));

        tabPanel.setMask(false);

        if (data != null) {

            startBitmapLoadWorker(data);

            glyphList.addAll(data);

        }

        // set the loading path as first item in the pathComboModel
        int index = controlPanel.getDataSourceCombo().getSelectedIndex();
        if (index != -1) {
            dataSourceList.setFirstIndex(index);
        }
        
        pcs.firePropertyChange(DATA_LOADED, null, this);
    }

    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (MemorizeAction.KEY.equals(e.getPropertyName())
                || InsertXmlAction.KEY.equals(e.getPropertyName())) {
            pcs.firePropertyChange(e);
        } else if (SettingsDialogAction.EDITING_OCCURRED.equals(e.getPropertyName())) {
            loadData();
        }

    }

}
