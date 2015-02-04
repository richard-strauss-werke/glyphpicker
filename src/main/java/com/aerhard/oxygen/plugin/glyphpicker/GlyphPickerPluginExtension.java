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

package com.aerhard.oxygen.plugin.glyphpicker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.text.BadLocationException;
import javax.swing.text.Position;

import com.aerhard.oxygen.plugin.glyphpicker.controller.action.InsertXmlAction;
import org.apache.log4j.Logger;

import com.aerhard.oxygen.plugin.glyphpicker.controller.main.MainController;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.view.MainPanel;

import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorOperationException;
import ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension;
import ro.sync.exml.workspace.api.PluginWorkspace;
import ro.sync.exml.workspace.api.editor.page.WSEditorPage;
import ro.sync.exml.workspace.api.editor.page.author.WSAuthorEditorPage;
import ro.sync.exml.workspace.api.editor.page.text.WSTextEditorPage;
import ro.sync.exml.workspace.api.standalone.MenuBarCustomizer;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
import ro.sync.exml.workspace.api.standalone.ViewComponentCustomizer;
import ro.sync.exml.workspace.api.standalone.ViewInfo;

/**
 * The oXygen plugin extension.
 */
public class GlyphPickerPluginExtension implements
        WorkspaceAccessPluginExtension {

    /** The logger. */
    private static final Logger LOGGER = Logger
            .getLogger(GlyphPickerPluginExtension.class.getName());

    /** The path of the plugin's icon. */
    private static final String PLUGIN_ICON = "/images/grid.png";
    
    /** The plugin's view ID in oXygen. */
    private static final String VIEW_ID = "GlyphPicker";

    /** The main controller. */
    private MainController mainController;

    /** The main panel. */
    private MainPanel mainPanel;

    /*
     * (non-Javadoc)
     * 
     * @see ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension#
     * applicationStarted
     * (ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace)
     */
    @Override
    public void applicationStarted(final StandalonePluginWorkspace workspace) {

        mainController = new MainController(workspace);
        mainController.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (InsertXmlAction.KEY.equals(e.getPropertyName())) {
                    insertFragment(workspace, (GlyphDefinition) e.getNewValue());
                }
            }
        });

        mainPanel = mainController.getPanel();
        mainController.loadData();

        workspace.addViewComponentCustomizer(new ViewComponentCustomizer() {
            /**
             * @see ro.sync.exml.workspace.api.standalone.ViewComponentCustomizer#customizeView(ro.sync.exml.workspace.api.standalone.ViewInfo)
             */
            @Override
            public void customizeView(ViewInfo viewInfo) {
                if (VIEW_ID.equals(viewInfo.getViewID())) {

                    viewInfo.setComponent(mainPanel);
                    viewInfo.setTitle("GlyphPicker");

                    // TODO use custom icon
                    viewInfo.setIcon(new ImageIcon(
                            GlyphPickerPluginExtension.class
                                    .getResource(PLUGIN_ICON)));
                }
            }
        });

        workspace.addMenuBarCustomizer(new MenuBarCustomizer() {
            @Override
            public void customizeMainMenu(JMenuBar mainMenu) {
                mainMenu.getMenu(1).addSeparator();
                mainMenu.getMenu(1).add(
                        new ToggleWindowAction(workspace, PLUGIN_ICON,
                                VIEW_ID));
            }
        });

    }

    /**
     * Creates an XML string from a GlyphDefinition object.
     *
     * @param d the glyph definition
     * @param setNs if true, the TEI namespace will be added to the XML string.
     * @return the XML string
     */
    private String createXmlString(GlyphDefinition d, Boolean setNs) {
        String ns = (setNs) ? " xmlns=\"http://www.tei-c.org/ns/1.0\"" : "";
        return "<g" + ns + " ref=\"" + d.getRefString() + "\"/>";
    }

    private int getNsPosition(String str) {


        return -1;
    }

    /**
     * Inserts a text fragment into a text or author editor pane.
     *
     * @param workspace oXygen's plugin workspace
     * @param d the glyph definition
     */
    private void insertFragment(StandalonePluginWorkspace workspace,
            GlyphDefinition d) {
        WSEditorPage currentPage = workspace.getCurrentEditorAccess(
                PluginWorkspace.MAIN_EDITING_AREA).getCurrentPage();
        if (currentPage instanceof WSTextEditorPage) {
            WSTextEditorPage page = (WSTextEditorPage) currentPage;

            // TODO make this namespace aware

            page.beginCompoundUndoableEdit();
            int selectionOffset = page.getSelectionStart();
            page.deleteSelection();
            try {
                page.getDocument().insertString(selectionOffset,
                        createXmlString(d, false),
                        javax.swing.text.SimpleAttributeSet.EMPTY);
            } catch (BadLocationException e) {
                LOGGER.error(e);
            }
            page.endCompoundUndoableEdit();

        } else if (currentPage instanceof WSAuthorEditorPage) {
            WSAuthorEditorPage page = (WSAuthorEditorPage) currentPage;

            AuthorAccess authorAccess = page.getAuthorAccess();
            try {
                int offset = authorAccess.getEditorAccess().getCaretOffset();
                Position endOffsetPos = null;
                try {
                    endOffsetPos = authorAccess.getDocumentController()
                            .createPositionInContent(offset + 1);
                } catch (BadLocationException e1) {
                    LOGGER.error(e1);
                }

                authorAccess.getDocumentController()
                        .insertXMLFragmentSchemaAware(
                                createXmlString(d, true), offset);

                int endOffset = endOffsetPos != null ? endOffsetPos.getOffset() - 1
                        : offset;
                authorAccess.getEditorAccess().setCaretPosition(endOffset);
            } catch (AuthorOperationException e) {
                LOGGER.error(e);
            }

        } else {
            workspace
                    .showErrorMessage("No editor pane found to insert the glyph.");
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension#
     * applicationClosing()
     */
    @Override
    public boolean applicationClosing() {

        mainController.saveData();

        return true;
    }

}
