package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.icl.saxon.aelfred.DefaultHandler;

public class GlyphDefinitionXmlHandler extends DefaultHandler {

    private Boolean inChar = false;

    private List<GlyphDefinition> glyphDefinitions = new ArrayList<GlyphDefinition>();
    private Stack<String> elementStack = new Stack<String>();

    private GlyphDefinition currentGlyphDefinition;

    private String range = "";
    private DataSource dataSource;
    private List<String> classes;

    private StringBuffer textContent = new StringBuffer();

    public GlyphDefinitionXmlHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<GlyphDefinition> getGlyphDefinitions() {
        return glyphDefinitions;
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attrs) throws SAXException {

        if (inChar) {

            if ("charName".equals(qName) || "mapping".equals(qName)
                    || "item".equals(qName) || "desc".equals(qName)) {
                textContent.setLength(0);
            }

            else if ("graphic".equals(qName)) {
                currentGlyphDefinition.setUrl(attrs.getValue("url"));
            }

        }

        else if ("char".equals(qName) || "glyph".equals(qName)) {
            currentGlyphDefinition = new GlyphDefinition();
            currentGlyphDefinition.setId(attrs.getValue("xml:id"));
            currentGlyphDefinition.setRange(range);
            currentGlyphDefinition.setDataSource(dataSource);
            classes = new ArrayList<String>();
            inChar = true;
        }

        else if ("charDecl".equals(qName)) {
            range = "";
        }

        else if ("desc".equals(qName)) {
            textContent.setLength(0);
        }

        elementStack.push(qName);
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        textContent.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        if ("desc".equals(qName)) {
            if ("charDecl".equals(elementStack.get(elementStack.size() - 2))) {
                range = textContent.toString();
            } else if ("char".equals(elementStack.get(elementStack.size() - 2))) {
                currentGlyphDefinition.setCharName(textContent.toString());
            }
        }

        else if ("char".equals(qName) || "glyph".equals(qName)) {
            currentGlyphDefinition.setClasses(classes);
            glyphDefinitions.add(currentGlyphDefinition);
            inChar = false;
        }

        else if (inChar) {
            if ("charName".equals(qName)) {
                currentGlyphDefinition.setCharName(textContent.toString());
            }

            // currently only the first mapping is read
            // TODO handle multiple mappings + type / subtype (hashmap?)
            else if ("mapping".equals(qName)
                    && currentGlyphDefinition.getCodePoint() == null) {

                currentGlyphDefinition.setCodePoint(textContent.toString());
            }

            else if ("item".equals(qName)) {
                classes.add(textContent.toString());
            }
        }

        elementStack.pop();
    }
}