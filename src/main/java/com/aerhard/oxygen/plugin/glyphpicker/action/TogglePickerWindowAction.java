package com.aerhard.oxygen.plugin.glyphpicker.action;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.aerhard.oxygen.plugin.glyphpicker.GlyphPickerPluginExtension;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

public class TogglePickerWindowAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    private StandalonePluginWorkspace workspace;

    private String viewId;
    
    private static ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");
    private static String className = TogglePickerWindowAction.class.getSimpleName();

    TogglePickerWindowAction(StandalonePluginWorkspace workspace, String icon, String viewId) {
        super("GyphPicker", new ImageIcon(
                GlyphPickerPluginExtension.class.getResource(icon)));

        this.workspace = workspace;
        this.viewId=viewId;

        String description = i18n.getString(className + ".description");
        
        putValue(SHORT_DESCRIPTION, description);

        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (workspace.isViewShowing(viewId)) {
            workspace.hideView(viewId);
        } else {
            workspace.showView(viewId, true);
        }
    }
}