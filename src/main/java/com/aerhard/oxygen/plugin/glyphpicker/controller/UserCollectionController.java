package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.swing.DefaultEventListModel;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import ca.odell.glazedlists.swing.DefaultEventTableModel;

import com.aerhard.oxygen.plugin.glyphpicker.action.ChangeViewAction;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinitions;
import com.aerhard.oxygen.plugin.glyphpicker.view.ContainerPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphGrid;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphTable;
import com.aerhard.oxygen.plugin.glyphpicker.view.HighlightButton;
import com.aerhard.oxygen.plugin.glyphpicker.view.UserCollectionControlPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.renderer.GlyphRendererAdapter;

public class UserCollectionController extends Controller {

    private ListSelectionModel selectionModel;

    private ContainerPanel panel;

    private GlyphTable table;
    private UserCollectionLoader loader;
    private BasicEventList<GlyphDefinition> glyphList;
    private GlyphGrid list;
    private boolean listInSync = true;

    private AbstractAction saveAction;
    private AbstractAction reloadAction;
    private AbstractAction removeAction;
    private AbstractAction insertAction;

    private HighlightButton insertBtn;

    @SuppressWarnings("unchecked")
    public UserCollectionController(StandalonePluginWorkspace workspace,
            Properties properties) {

        UserCollectionControlPanel controlPanel = new UserCollectionControlPanel();

        panel = new ContainerPanel(controlPanel);

        glyphList = new BasicEventList<GlyphDefinition>();

        list = new GlyphGrid(new DefaultEventListModel<GlyphDefinition>(
                glyphList));
        GlyphRendererAdapter r = new GlyphRendererAdapter(list);
        r.setPreferredSize(new Dimension(40, 40));
        list.setFixedSize(40);
        list.setCellRenderer(r);

        DefaultEventTableModel<GlyphDefinition> tableListModel = new DefaultEventTableModel<GlyphDefinition>(
                glyphList, new GlyphTableFormat());
        table = new GlyphTable(tableListModel);
        r = new GlyphRendererAdapter(table);
        r.setPreferredSize(new Dimension(40, 40));
        table.setRowHeight(90);
        table.setTableIconRenderer(r);

        panel.setListComponent(list);

        controlPanel.getToggleBtn().setAction(
                new ChangeViewAction(panel, table, list));

        setButtons();

        loader = new UserCollectionLoader(workspace, properties);

        selectionModel = new DefaultEventSelectionModel<GlyphDefinition>(
                glyphList);
        selectionModel
                .setSelectionMode(DefaultEventSelectionModel.SINGLE_SELECTION);
        list.setSelectionModel(selectionModel);
        table.setSelectionModel(selectionModel);

        selectionModel.addListSelectionListener(new GlyphSelectionListener());

       glyphList.addListEventListener(new ListEventListener<GlyphDefinition>() {
            @Override
            public void listChanged(ListEvent<GlyphDefinition> e) {
                if (selectionModel.isSelectionEmpty() && glyphList.size() > 0 ) {
                    selectionModel.setSelectionInterval(0, 0);
                }
                
             // TODO reevaluate list layout
                
            }
        });
        
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

    public ContainerPanel getPanel() {
        return panel;
    }

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
            GlyphDefinition selectedModel = glyphList.get(row);
            if (selectedModel != null) {
                fireEvent("insert", selectedModel);
            }
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
                            : glyphList.get(index);

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

                removeAction.setEnabled(enableButtons);
                insertAction.setEnabled(enableButtons);
            }
        }
    }

    private void setListeners() {

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

    public BasicEventList<GlyphDefinition> getListModel() {
        return glyphList;
    }

    public UserCollectionLoader getUserCollectionLoader() {
        return loader;
    }

    @Override
    public void loadData() {
        panel.getOverlayable().setOverlayVisible(true);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                listInSync = true;
                glyphList.clear();
                glyphList.addAll(loader.load().getData());
                panel.getOverlayable().setOverlayVisible(false);
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
