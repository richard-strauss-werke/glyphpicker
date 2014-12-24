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
    private Boolean inMapping = false;

    private List<GlyphDefinition> glyphDefinitions = new ArrayList<GlyphDefinition>();
    private Stack<String> elementStack = new Stack<String>();

    private GlyphDefinition currentGlyphDefinition;

    private String range = "";
    private DataSource dataSource;
    private List<String> classes;

    private String mappingAttName;
    private String mappingAttValue;

    private StringBuffer textContent = new StringBuffer();

    public GlyphDefinitionXmlHandler(DataSource dataSource) {
        this.dataSource = dataSource;
        this.mappingAttName = dataSource.getMappingAttName();
        this.mappingAttValue = dataSource.getMappingAttValue();
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

            if ("mapping".equals(qName) && mappingAttValue.equals(attrs.getValue(mappingAttName))) {
                inMapping = true;
              }
            
//            if ("mapping".equals(qName) && attrs.getValue(mappingAttName).equals(mappingAttValue)) {
//              System.out.println("matched");
//            }

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

            else if (inMapping) {
                currentGlyphDefinition.setCodePoint(textContent.toString());
                inMapping = false;
            }

            else if ("item".equals(qName)) {
                classes.add(textContent.toString());
            }
        }

        elementStack.pop();
    }
}