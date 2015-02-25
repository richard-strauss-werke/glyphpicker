package de.badw.strauss.glyphpicker.view.settings;

import com.jidesoft.swing.JideButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ResourceBundle;

public class OptionsEditor extends JPanel {

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

    /**
     * A checkbox to adjust the tranferFocusOnInsert option
     */
    private final JCheckBox transferFocusCheckBox;

    public OptionsEditor() {

        ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");
        String className = this.getClass().getSimpleName();


        cacheCountLabelSuffixSingular = i18n.getString(className + ".image");
        cacheCountLabelSuffixPlural = i18n.getString(className + ".images");

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel innerPanel = new JPanel();

        JLabel shortcutLabel = new JLabel(i18n.getString(className + ".keyboardShortcutLabel"));
        innerPanel.add(shortcutLabel);

        shortcutTextField = new JTextField();
        shortcutTextField.setColumns(20);
        innerPanel.add(shortcutTextField);

        applyShortcutButton = new JideButton();
        innerPanel.add(applyShortcutButton);

        innerPanel.setMaximumSize(new Dimension(600, innerPanel.getPreferredSize().height));
        add(innerPanel);

        innerPanel = new JPanel();

        cacheCountLabel = new JLabel();
        innerPanel.add(cacheCountLabel);

        clearCacheButton = new JideButton();
        innerPanel.add(clearCacheButton);

        innerPanel.setMaximumSize(new Dimension(600, innerPanel.getPreferredSize().height));
        add(innerPanel);

        innerPanel = new JPanel();

        JLabel restoreFocusLabel = new JLabel(i18n.getString(className + ".transferFocusLabel"));
        innerPanel.add(restoreFocusLabel);

        transferFocusCheckBox = new JCheckBox();
        innerPanel.add(transferFocusCheckBox);

        add(innerPanel);

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


    /**
     * Gets the restore-focus check box
     *
     * @return the check box
     */
    public JCheckBox getTransferFocusCheckBox() {
        return transferFocusCheckBox;
    }

}
