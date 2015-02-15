package com.aerhard.oxygen.plugin.glyphpicker.view.options;

import com.jidesoft.swing.JideButton;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class OptionsEditor extends JPanel {

    /**
     * The component's preferred width.
     */
    private static final int PREFERRED_WIDTH = 350;

    /**
     * The component's preferred height.
     */
    private static final int PREFERRED_HEIGHT = 70;

    /**
     * The text field to edit the shortcut
     */
    private final JTextField shortcutTextField;

    /**
     * The button to apply a new shortcut
     */
    private final JideButton applyShortcutButton;

    /**
     * The button to clear the cache
     */
    private final JideButton clearCacheButton;

    /**
     * The label component displaying the number of images in the cache
     */
    private final JLabel cacheCountLabel;

    /**
     * The string appended to the cache count, singular
     */
    private final String cacheCountLabelSuffixSingular;

    /**
     * The string appended to the cache count, plural
     */
    private final String cacheCountLabelSuffixPlural;

    public OptionsEditor() {

        ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");
        String className = this.getClass().getSimpleName();

        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));

        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWidths = new int[]{102, 46, 46};
        gbl.rowHeights = new int[]{20, 0};
        gbl.columnWeights = new double[]{0.0, 1.0, 0.0};
        gbl.rowWeights = new double[]{0.0, 0.0};
        setLayout(gbl);

        cacheCountLabelSuffixSingular = i18n.getString(className + ".image");
        cacheCountLabelSuffixPlural = i18n.getString(className + ".images");

        JLabel shortcutLabel = new JLabel(i18n.getString(className + ".keyboardShortcutLabel"));
        GridBagConstraints gbcShortcutLabel = new GridBagConstraints();
        gbcShortcutLabel.insets = new Insets(4, 5, 4, 5);
        gbcShortcutLabel.fill = GridBagConstraints.BOTH;
        gbcShortcutLabel.gridx = 0;
        gbcShortcutLabel.gridy = 0;
        add(shortcutLabel, gbcShortcutLabel);

        shortcutTextField = new JTextField();
        GridBagConstraints gbcShortcutTextField = new GridBagConstraints();
        gbcShortcutTextField.insets = new Insets(4, 5, 4, 5);
        gbcShortcutTextField.fill = GridBagConstraints.BOTH;
        gbcShortcutTextField.weightx = 1.0;
        gbcShortcutTextField.gridx = 1;
        gbcShortcutTextField.gridy = 0;
        add(shortcutTextField, gbcShortcutTextField);

        applyShortcutButton = new JideButton();
        GridBagConstraints gbcApplyButton = new GridBagConstraints();
        gbcApplyButton.insets = new Insets(4, 5, 4, 5);
        gbcApplyButton.fill = GridBagConstraints.BOTH;
        gbcApplyButton.gridx = 2;
        gbcApplyButton.gridy = 0;
        add(applyShortcutButton, gbcApplyButton);

        JLabel cacheLabel = new JLabel(i18n.getString(className + ".imageCacheLabel"));
        GridBagConstraints gbcCacheLabel = new GridBagConstraints();
        gbcCacheLabel.insets = new Insets(4, 5, 4, 5);
        gbcCacheLabel.fill = GridBagConstraints.BOTH;
        gbcCacheLabel.gridx = 0;
        gbcCacheLabel.gridy = 1;
        add(cacheLabel, gbcCacheLabel);

        cacheCountLabel = new JLabel();
        GridBagConstraints gbcCacheData = new GridBagConstraints();
        gbcCacheData.insets = new Insets(4, 5, 4, 5);
        gbcCacheData.fill = GridBagConstraints.BOTH;
        gbcCacheData.gridx = 1;
        gbcCacheData.gridy = 1;
        add(cacheCountLabel, gbcCacheData);

        clearCacheButton = new JideButton();
        GridBagConstraints gbcCacheButton = new GridBagConstraints();
        gbcCacheButton.insets = new Insets(4, 5, 4, 5);
        gbcCacheButton.fill = GridBagConstraints.BOTH;
        gbcCacheButton.gridx = 2;
        gbcCacheButton.gridy = 1;
        add(clearCacheButton, gbcCacheButton);
    }

    /**
     * Gets the shortcut text field
     *
     * @return the text field component
     */
    public JTextField getShortcutTextField() {
        return shortcutTextField;
    }

    /**
     * Updates the cache-item-count component
     *
     * @param count the number of items in the cache
     */
    public void updateCacheItemCount(int count) {
        cacheCountLabel.setText(count + " " +
                ((count == 1) ? cacheCountLabelSuffixSingular : cacheCountLabelSuffixPlural));
    }

    /**
     * Gets the apply-shortcut button
     *
     * @return the button
     */
    public JButton getApplyShortcutButton() {
        return applyShortcutButton;
    }

    /**
     * Gets the clear-cache button
     *
     * @return the button
     */
    public JButton getClearCacheButton() {
        return clearCacheButton;
    }

}
