package com.aerhard.oxygen.plugin.glyphpicker.controller;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.model.trans.PropertySelector;

import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import ca.odell.glazedlists.matchers.Matcher;

public class GlyphSelect extends AbstractMatcherEditor<GlyphDefinition>
        implements DocumentListener {

    private PropertySelector propertySelector;
    private JTextField textField;

    public GlyphSelect(PropertySelector propertySelector, JTextField textField) {
        this.propertySelector = propertySelector;
        this.textField = textField;

    }

    public void setPropertySelector(PropertySelector propertySelector) {
        this.propertySelector = propertySelector;
    }

    public void performUpdate(final DocumentEvent e) {
 
        String q;
        try {
            q = e.getDocument().getText(0, e.getDocument().getLength());


            
            
//            q = q.substring(0, textField.getSelectionStart())
//                    + q.substring(textField.getSelectionEnd());

//            System.out.println(textField.getSelectionStart());

        } catch (BadLocationException e1) {
            q = "";
        }

        final String query = q;
        

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                

                
                Matcher<GlyphDefinition> newMatcher = new GlyphMatcher(
                        propertySelector, query);
                fireChanged(newMatcher);
            }
        });

    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        performUpdate(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        performUpdate(e);
    }
}
