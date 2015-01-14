/**
 * Copyright 2014 Alexander Erhard
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

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Position;

import org.apache.log4j.Logger;

import com.aerhard.oxygen.plugin.glyphpicker.controller.ControllerEventListener;
import com.aerhard.oxygen.plugin.glyphpicker.controller.MainController;
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

    private static final String PLUGIN_ICON = "/images/grid.png";
    private static final String VIEW_ID = "GlyphPicker";

    private MainController mainController;

    private MainPanel mainPanel;

    private class TogglePickerWindowAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        private StandalonePluginWorkspace workspace;
        
        TogglePickerWindowAction(StandalonePluginWorkspace workspace) {
            super("GyphPicker", new ImageIcon(
                    GlyphPickerPluginExtension.class.getResource(PLUGIN_ICON)));

            this.workspace = workspace;
            
            putValue(SHORT_DESCRIPTION, "Shows / hides the GlyphPicker window");

         // TODO make compatible with osx
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control P"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (workspace.isViewShowing(VIEW_ID)) {
                workspace.hideView(VIEW_ID);
            } else {
                workspace.showView(VIEW_ID, true);                
            };
        }
    }

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
        mainController.addListener(new ControllerEventListener() {
            @Override
            public void eventOccured(String type, GlyphDefinition model) {
                if ("insert".equals(type)) {
                    insertFragment(workspace, model);
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
                    viewInfo.setIcon(new ImageIcon(GlyphPickerPluginExtension.class
                            .getResource(PLUGIN_ICON)));
                }
            }
        });
        
        workspace.addMenuBarCustomizer(new MenuBarCustomizer() {
            @Override
            public void customizeMainMenu(JMenuBar mainMenu) {
                mainMenu.getMenu(1).addSeparator();
                mainMenu.getMenu(1).add(new TogglePickerWindowAction(workspace));
            }
        });

    }

    private String getXmlString(GlyphDefinition model, Boolean setNs) {
        String ns = (setNs) ? " xmlns=\"http://www.tei-c.org/ns/1.0\"" : "";
        return "<g" + ns + " ref=\"" + model.getRefString() + "\"/>";
    }

    private void insertFragment(StandalonePluginWorkspace workspace,
            GlyphDefinition model) {
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
                        getXmlString(model, false),
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
                                getXmlString(model, true), offset);

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
    };

}
