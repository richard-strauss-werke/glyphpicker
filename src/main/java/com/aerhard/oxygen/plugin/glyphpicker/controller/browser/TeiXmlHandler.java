package com.aerhard.oxygen.plugin.glyphpicker.controller.browser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.icl.saxon.aelfred.DefaultHandler;

public class TeiXmlHandler extends DefaultHandler {

    private Boolean inChar = false;
    private Boolean inMapping = false;

    private List<GlyphDefinition> glyphDefinitions = new ArrayList<GlyphDefinition>();
    private Stack<String> elementStack = new Stack<String>();

    private GlyphDefinition currentGlyphDefinition;

    private String range = "";
    private DataSource dataSource;

    private String mappingTypeValue;
    private String mappingSubTypeValue;

    private StringBuffer textContent = new StringBuffer();
    
    private MappingMatcher mappingMatcher;

    public TeiXmlHandler(DataSource dataSource) {
        this.dataSource = dataSource;
        mappingTypeValue = dataSource.getMappingTypeValue();
        if (mappingTypeValue == null) {
            mappingTypeValue = "";
        }
        mappingSubTypeValue = dataSource.getMappingSubTypeValue();
        if (mappingSubTypeValue == null) {
            mappingSubTypeValue = "";
        }
        
        if (!mappingTypeValue.isEmpty()) {
            if (!mappingTypeValue.isEmpty()) {
                mappingMatcher = new MappingBothMatcher();
            } else {
                mappingMatcher = new MappingTypeMatcher();
            }
        } else if (!mappingTypeValue.isEmpty()) {
            mappingMatcher = new MappingSubTypeMatcher();
        } else {
            mappingMatcher = new MappingAllMatcher();
        }
        
    }

    public interface MappingMatcher {
        boolean matches(Attributes attrs);
    }

    public class MappingTypeMatcher implements MappingMatcher {
        public boolean matches(Attributes attrs) {
            return mappingTypeValue.equals(attrs.getValue("type"));
        }
    }

    public class MappingSubTypeMatcher implements MappingMatcher {
        public boolean matches(Attributes attrs) {
            return mappingSubTypeValue.equals(attrs.getValue("subtype"));
        }
    }
    
    public class MappingBothMatcher implements MappingMatcher {
        public boolean matches(Attributes attrs) {
            return mappingTypeValue.equals(attrs.getValue("type"))
                    && mappingSubTypeValue.equals(attrs.getValue("subtype"));
        }
    }

    public class MappingAllMatcher implements MappingMatcher {
        public boolean matches(Attributes attrs) {
            return true;
        }
    }

    public List<GlyphDefinition> getGlyphDefinitions() {
        return glyphDefinitions;
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attrs) throws SAXException {

        if (inChar) {
            onStartElementInChar(qName, attrs);
        }

        else if ("char".equals(qName) || "glyph".equals(qName)) {
            currentGlyphDefinition = new GlyphDefinition();
            currentGlyphDefinition.setId(attrs.getValue("xml:id"));
            currentGlyphDefinition.setRange(range);
            currentGlyphDefinition.setDataSource(dataSource);
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

    private void onStartElementInChar(String qName, Attributes attrs) {
        if ("charName".equals(qName) || "mapping".equals(qName)
                || "desc".equals(qName)) {
            textContent.setLength(0);
        }

        else if ("graphic".equals(qName)) {
            currentGlyphDefinition.setUrl(attrs.getValue("url"));
        }

        if ("mapping".equals(qName)
                && mappingMatcher.matches(attrs)) {
            inMapping = true;
        }

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

        }

        elementStack.pop();
    }
}