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
package com.aerhard.oxygen.plugin.glyphpicker.controller.browser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphReference;
import com.icl.saxon.aelfred.DefaultHandler;

/**
 * The TEI XML handler.
 */
public class TeiXmlHandler extends DefaultHandler {

    /** The logger. */
    private static final Logger LOGGER = Logger.getLogger(TeiXmlHandler.class
            .getName());

    /** indicates ancestor-or-self::char. */
    private Boolean inChar = false;

    /** indicates ancestor-or-self::mapping. */
    private Boolean inMapping = false;

    /** The resulting glyph definitions. */
    private final List<GlyphDefinition> glyphDefinitions = new ArrayList<>();

    /** An element stack of all ancestors and the current element. */
    private final Stack<String> elementStack = new Stack<>();

    /** The list of all glyph references in the current mapping. */
    private List<GlyphReference> currentGlyphReferences = new ArrayList<>();

    /** A list of the glyph definitions with glyph references in <mapping>. */
    private final List<GlyphDefinition> referencingGlyphDefinitions = new ArrayList<>();

    /** The current glyph definition. */
    private GlyphDefinition currentGlyphDefinition;

    /** The range property. */
    private String range = "";

    /** The data source providing handler parameters. */
    private final DataSource dataSource;

    /** A string buffer for the text content of elements. */
    private final StringBuffer textContent = new StringBuffer();

    /** The mapping matcher. */
    private final MappingMatcher mappingMatcher;

    /** The mapping parser. */
    private final MappingParser mappingParser;

    /**
     * Instantiates a new TeiXmlHandler.
     *
     * @param dataSource
     *            The data source providing handler parameters
     */
    public TeiXmlHandler(DataSource dataSource) {
        this.dataSource = dataSource;
        String mappingTypeValue = dataSource.getTypeAttributeValue();
        if (mappingTypeValue == null) {
            mappingTypeValue = "";
        }
        String mappingSubTypeValue = dataSource.getSubtypeAttributeValue();
        if (mappingSubTypeValue == null) {
            mappingSubTypeValue = "";
        }

        if (!mappingTypeValue.isEmpty()) {
            if (!mappingSubTypeValue.isEmpty()) {
                mappingMatcher = new MappingDoubleMatcher("type",
                        mappingTypeValue, "subtype", mappingSubTypeValue);
            } else {
                mappingMatcher = new MappingSingleMatcher("type",
                        mappingTypeValue);
            }
        } else if (!mappingSubTypeValue.isEmpty()) {
            mappingMatcher = new MappingSingleMatcher("subtype",
                    mappingSubTypeValue);
        } else {
            mappingMatcher = new MappingAllMatcher();
        }

        if (dataSource.getParseMapping()) {
            mappingParser = new MappingUPlusParser();
        } else {
            mappingParser = new MappingNoParser();
        }

    }

    /**
     * The Interface MappingMatcher.
     */
    public interface MappingMatcher {

        /**
         * Matches.
         *
         * @param attrs
         *            the attrs
         * @return true, if successful
         */
        boolean matches(Attributes attrs);
    }

    /**
     * A MappingMatcher matching a single attribute.
     */
    public static class MappingSingleMatcher implements MappingMatcher {

        /** The key. */
        private final String key;

        /** The value. */
        private final String value;

        /**
         * Instantiates a new MappingSingleMatcher.
         *
         * @param key
         *            the key = attribute name
         * @param value
         *            the value = attribute value
         */
        public MappingSingleMatcher(String key, String value) {
            this.key = key;
            this.value = value;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.aerhard.oxygen.plugin.glyphpicker.controller.browser.TeiXmlHandler
         * .MappingMatcher#matches(org.xml.sax.Attributes)
         */
        @Override
        public boolean matches(Attributes attrs) {
            return value.equals(attrs.getValue(key));
        }
    }

    /**
     * A MappingMatcher matching two attributes.
     */
    public static class MappingDoubleMatcher implements MappingMatcher {

        /** The first attribute's name. */
        private final String key1;

        /** The first attribute's value. */
        private final String value1;

        /** The second attribute's name. */
        private final String key2;

        /** The second attribute's value. */
        private final String value2;

        /**
         * Instantiates a new MappingDoubleMatcher.
         *
         * @param key1
         *            the first attribute's name
         * @param value1
         *            the first attribute's value
         * @param key2
         *            the second attribute's name
         * @param value2
         *            the second attribute's value
         */
        public MappingDoubleMatcher(String key1, String value1, String key2,
                String value2) {
            this.key1 = key1;
            this.value1 = value1;
            this.key2 = key2;
            this.value2 = value2;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.aerhard.oxygen.plugin.glyphpicker.controller.browser.TeiXmlHandler
         * .MappingMatcher#matches(org.xml.sax.Attributes)
         */
        @Override
        public boolean matches(Attributes attrs) {
            return value1.equals(attrs.getValue(key1))
                    && value2.equals(attrs.getValue(key2));
        }
    }

    /**
     * A MappingMatcher always returning true.
     */
    public static class MappingAllMatcher implements MappingMatcher {

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.aerhard.oxygen.plugin.glyphpicker.controller.browser.TeiXmlHandler
         * .MappingMatcher#matches(org.xml.sax.Attributes)
         */
        @Override
        public boolean matches(Attributes attrs) {
            return true;
        }
    }

    /**
     * The Interface MappingParser.
     */
    public interface MappingParser {

        /**
         * Parses the specified string to a new string.
         *
         * @param str
         *            the input string
         * @return the output string
         */
        String parse(String str);
    }

    /**
     * A MappingParser rendering strings like "U+E002" to codepoint strings.
     */
    public static class MappingUPlusParser implements MappingParser {

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.aerhard.oxygen.plugin.glyphpicker.controller.browser.TeiXmlHandler
         * .MappingParser#parse(java.lang.String)
         */
        @Override
        public String parse(String str) {
            if (str == null) {
                return null;
            }
            try {
                String cp = str.substring(2);
                int c = Integer.parseInt(cp, 16);
                return Character.toString((char) c);
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * A MappingParser returning the input.
     */
    public static class MappingNoParser implements MappingParser {

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.aerhard.oxygen.plugin.glyphpicker.controller.browser.TeiXmlHandler
         * .MappingParser#parse(java.lang.String)
         */
        @Override
        public String parse(String str) {
            return str;
        }
    }

    /**
     * Gets the resulting glyph definitions.
     *
     * @return the glyph definitions
     */
    public List<GlyphDefinition> getGlyphDefinitions() {
        return glyphDefinitions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
     * java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
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

    /**
     * Handles element starts within <char> elements.
     *
     * @param qName
     *            the qName
     * @param attrs
     *            the attributes
     */
    private void onStartElementInChar(String qName, Attributes attrs) {
        if ("charName".equals(qName) || "mapping".equals(qName)
                || "desc".equals(qName)) {
            textContent.setLength(0);

            if ("mapping".equals(qName) && mappingMatcher.matches(attrs)) {
                inMapping = true;
            }
        }

        else if ("graphic".equals(qName)) {
            currentGlyphDefinition.setUrl(attrs.getValue("url"));
        }

        else if ("g".equals(qName) && inMapping) {
            String targetId = attrs.getValue("ref");

            currentGlyphReferences.add(new GlyphReference(textContent.length(), targetId
                    .substring(1)));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        textContent.append(ch, start, length);
    }

    /**
     * Checks if the parent element's name matches the provided string.
     *
     * @param qName
     *            the name
     * @return true, if it matches
     */
    private boolean isParent(String qName) {
        return qName.equals(elementStack.get(elementStack.size() - 2));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        if ("desc".equals(qName)) {
            if (isParent("charDecl")) {
                range = textContent.toString();
            } else if (isParent("char")) {
                currentGlyphDefinition.setCharName(textContent.toString());
            }
        }

        else if ("char".equals(qName) || "glyph".equals(qName)) {
            glyphDefinitions.add(currentGlyphDefinition);
            inChar = false;
        }

        else if (inChar) {
            onEndElementInChar(qName);
        }

        elementStack.pop();
    }

    /**
     * Handles element ends within <char> elements.
     *
     * @param qName
     *            the qName
     */
    private void onEndElementInChar(String qName) {
        if ("charName".equals(qName)) {
            currentGlyphDefinition.setCharName(textContent.toString());
        }

        else if (inMapping && "mapping".equals(qName)) {
            currentGlyphDefinition.setCodePoint(mappingParser.parse(textContent
                    .toString()));

            if (!currentGlyphReferences.isEmpty()) {
                currentGlyphDefinition.setGlyphReferences(currentGlyphReferences);
                currentGlyphReferences = new ArrayList<>();
                referencingGlyphDefinitions.add(currentGlyphDefinition);
            }

            inMapping = false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#endDocument()
     */
    public void endDocument() throws SAXException {
        if (!referencingGlyphDefinitions.isEmpty()) {
            try {
                setReferencedTargets(createIdHashMap());
                addTargetCodepoints();
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }
    }

    /**
     * Creates a hash map with glyph ids as key and glyph definitions as value.
     *
     * @return the map
     */
    private Map<String, GlyphDefinition> createIdHashMap() {
        Map<String, GlyphDefinition> ids = new HashMap<>();
        for (GlyphDefinition d : glyphDefinitions) {
            ids.put(d.getId(), d);
        }
        return ids;
    }

    /**
     * Searches for referenced glyph definitions by their ID and adds them to
     * the referring glyph definition.
     *
     * @param ids
     *            the ids
     */
    private void setReferencedTargets(Map<String, GlyphDefinition> ids) {
        for (GlyphDefinition r : referencingGlyphDefinitions) {
            for (GlyphReference ref : r.getGlyphReferences()) {
                GlyphDefinition target = ids.get(ref.getTargetId());
                if (target != null) {
                    ref.setTarget(target);
                }
            }
        }
    }

    /**
     * Adds the codepoints of referenced glyphs to the referring glyph
     * definition's codePoint field.
     */
    private void addTargetCodepoints() {
        // TODO recurse glyphdefinitions in order to include references
        // of references

        for (GlyphDefinition r : referencingGlyphDefinitions) {
            int offset = 0;
            StringBuilder sb = new StringBuilder(r.getCodePoint());
            for (GlyphReference ref : r.getGlyphReferences()) {
                GlyphDefinition target = ref.getTarget();
                if (target != null) {
                    String additionalString = target.getCodePoint();
                    ref.incrementIndex(offset);
                    sb.insert(ref.getIndex(), additionalString);
                    offset += additionalString.length();
                }
            }
            r.setCodePoint(sb.toString());
        }
    }

}