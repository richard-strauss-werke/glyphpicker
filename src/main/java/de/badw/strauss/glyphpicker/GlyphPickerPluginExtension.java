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

package de.badw.strauss.glyphpicker;

import de.badw.strauss.glyphpicker.controller.MainController;
import de.badw.strauss.glyphpicker.controller.action.InsertXmlAction;
import de.badw.strauss.glyphpicker.model.GlyphDefinition;
import de.badw.strauss.glyphpicker.view.MainPanel;
import org.apache.log4j.Logger;
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

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Position;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The oXygen plugin extension.
 */
public class GlyphPickerPluginExtension implements
        WorkspaceAccessPluginExtension {

    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger
            .getLogger(GlyphPickerPluginExtension.class.getName());

    /**
     * The path of the plugin's icon.
     */
    static final String PLUGIN_ICON = "/images/bravura/g.png";

    /**
     * The plugin's view ID in oXygen.
     */
    private static final String VIEW_ID = "GlyphPicker";

    /**
     * The plugin title.
     */
    private static final String TITLE = "GlyphPicker";

    /**
     * The main controller.
     */
    private MainController mainController;

    /**
     * The main panel.
     */
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
                    viewInfo.setTitle(TITLE);

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
                mainMenu.getMenu(1).add(new ShowPanelAction(workspace, PLUGIN_ICON,
                        VIEW_ID, mainPanel, mainController.getConfig()));
            }
        });

    }

    /**
     * Inserts a text fragment into a text or author editor pane.
     *
     * @param workspace oXygen's plugin workspace
     * @param d         the glyph definition
     */
    private void insertFragment(StandalonePluginWorkspace workspace,
                                GlyphDefinition d) {
        WSEditorPage currentPage = workspace.getCurrentEditorAccess(
                PluginWorkspace.MAIN_EDITING_AREA).getCurrentPage();
        if (currentPage instanceof WSTextEditorPage) {
            insertIntoTextEditorPage(d.getXmlString(), (WSTextEditorPage) currentPage);
        } else if (currentPage instanceof WSAuthorEditorPage) {
            insertIntoAuthorPage(d.getXmlString(), (WSAuthorEditorPage) currentPage);
        } else {
            JOptionPane.showMessageDialog(mainPanel, "No editor pane found to insert the glyph.");
        }
        if (mainController.getConfig().shouldTransferFocusAfterInsert()) {
            transferFocus();
        }
    }

    /**
     * transfers the focus of the GlyphPicker panel to the previously focused component
     */
    private void transferFocus() {
        // TODO find the parent component in oXygen, get action map, trigger event
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ESCAPE);
        } catch (AWTException e) {
            JOptionPane.showMessageDialog(mainPanel, e.toString());
        }
    }

    /**
     * Inserts a text fragment into a text editor page.
     *
     * @param str  the input string
     * @param page the editor page
     */
    private void insertIntoTextEditorPage(String str, WSTextEditorPage page) {

        if (str != null && !str.isEmpty()) {
            page.beginCompoundUndoableEdit();
            int selectionOffset = page.getSelectionStart();
            page.deleteSelection();
            try {
                page.getDocument().insertString(selectionOffset,
                        str,
                        javax.swing.text.SimpleAttributeSet.EMPTY);

            } catch (BadLocationException e) {
                LOGGER.error(e);
            }
            page.endCompoundUndoableEdit();
        }
    }

    /**
     * Inserts a text fragment into a author editor page.
     *
     * @param str  the input string
     * @param page the editor page
     */
    private void insertIntoAuthorPage(String str, WSAuthorEditorPage page) {

        String stringWithNs = addNamespace(str);

        if (stringWithNs != null && !stringWithNs.isEmpty()) {
            try {
                AuthorAccess authorAccess = page.getAuthorAccess();

                if (authorAccess.getEditorAccess().hasSelection()) {
                    authorAccess.getEditorAccess().deleteSelection();
                }

                int offset = authorAccess.getEditorAccess().getCaretOffset();
                Position endOffsetPos = authorAccess.getDocumentController()
                        .createPositionInContent(offset + 1);

                authorAccess.getDocumentController().insertXMLFragment(stringWithNs, offset);

                int endOffset = endOffsetPos != null ? endOffsetPos.getOffset() - 1
                        : offset;
                authorAccess.getEditorAccess().setCaretPosition(endOffset);
            } catch (BadLocationException e1) {
                LOGGER.error(e1);
            } catch (AuthorOperationException e1) {
                LOGGER.error(e1);
            }
        }
    }

    /**
     * Adds a namespace declaration to a string.
     *
     * @param str the input string
     * @return the string with added namespace or - if it doesn't match the pattern <[^/^>]+ or is null - the original string
     */
    public String addNamespace(String str) {
        if (str != null && !str.isEmpty()) {
            String ns = " xmlns=\"http://www.tei-c.org/ns/1.0\"";
            Pattern p = Pattern.compile("(<[^/^>]+)");
            Matcher m = p.matcher(str);
            if (m.find()) {
                String match = m.group();
                String replacement;
                int spaceIndex = match.indexOf(' ');
                if (spaceIndex == -1) {
                    replacement = match + ns;
                } else {
                    replacement = match.substring(0, spaceIndex) + ns + match.substring(spaceIndex);
                }
                return m.replaceFirst(replacement);
            }
        }
        return str;
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
