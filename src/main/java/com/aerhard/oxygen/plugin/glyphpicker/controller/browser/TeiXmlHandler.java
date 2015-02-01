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
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphRef;
import com.icl.saxon.aelfred.DefaultHandler;

public class TeiXmlHandler extends DefaultHandler {

    private static final Logger LOGGER = Logger.getLogger(TeiXmlHandler.class
            .getName());

    private Boolean inChar = false;
    private Boolean inMapping = false;

    private List<GlyphDefinition> glyphDefinitions = new ArrayList<GlyphDefinition>();
    private Stack<String> elementStack = new Stack<String>();

    private List<GlyphRef> currentGlyphRefs = new ArrayList<GlyphRef>();
    private List<GlyphDefinition> referencingGlyphDefinitions = new ArrayList<GlyphDefinition>();

    private GlyphDefinition currentGlyphDefinition;

    private String range = "";
    private DataSource dataSource;

    private StringBuffer textContent = new StringBuffer();

    private MappingMatcher mappingMatcher;
    private MappingParser mappingParser;

    public TeiXmlHandler(DataSource dataSource) {
        this.dataSource = dataSource;
        String mappingTypeValue = dataSource.getMappingTypeValue();
        if (mappingTypeValue == null) {
            mappingTypeValue = "";
        }
        String mappingSubTypeValue = dataSource.getMappingSubTypeValue();
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

        if (dataSource.getMappingAsCharString()) {
            mappingParser = new MappingUPlusParser();
        } else {
            mappingParser = new MappingNoParser();
        }

    }

    public interface MappingMatcher {
        boolean matches(Attributes attrs);
    }

    public static class MappingSingleMatcher implements MappingMatcher {

        private String key;
        private String value;

        public MappingSingleMatcher(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean matches(Attributes attrs) {
            return value.equals(attrs.getValue(key));
        }
    }

    public static class MappingDoubleMatcher implements MappingMatcher {

        private String value1;
        private String key1;
        private String value2;
        private String key2;

        public MappingDoubleMatcher(String key1, String value1, String key2,
                String value2) {
            this.key1 = key1;
            this.value1 = value1;
            this.key2 = key2;
            this.value2 = value2;
        }

        @Override
        public boolean matches(Attributes attrs) {
            return value1.equals(attrs.getValue(key1))
                    && value2.equals(attrs.getValue(key2));
        }
    }

    public static class MappingAllMatcher implements MappingMatcher {
        @Override
        public boolean matches(Attributes attrs) {
            return true;
        }
    }

    public interface MappingParser {
        String parse(String str);
    }

    public static class MappingUPlusParser implements MappingParser {
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

    public static class MappingNoParser implements MappingParser {
        @Override
        public String parse(String str) {
            return str;
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

            if ("mapping".equals(qName) && mappingMatcher.matches(attrs)) {
                inMapping = true;
            }
        }

        else if ("graphic".equals(qName)) {
            currentGlyphDefinition.setUrl(attrs.getValue("url"));
        }

        else if ("g".equals(qName) && inMapping) {
            String targetId = attrs.getValue("ref");

            currentGlyphRefs.add(new GlyphRef(textContent.length(), targetId
                    .substring(1)));

        }

    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        textContent.append(ch, start, length);
    }

    private boolean isParent(String qName) {
        return qName.equals(elementStack.get(elementStack.size() - 2));
    }

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

    private void onEndElementInChar(String qName) {
        if ("charName".equals(qName)) {
            currentGlyphDefinition.setCharName(textContent.toString());
        }

        else if (inMapping && "mapping".equals(qName)) {
            currentGlyphDefinition.setCodePoint(mappingParser.parse(textContent
                    .toString()));

            if (!currentGlyphRefs.isEmpty()) {
                currentGlyphDefinition.setGlyphRefs(currentGlyphRefs);
                currentGlyphRefs = new ArrayList<GlyphRef>();
                referencingGlyphDefinitions.add(currentGlyphDefinition);
            }

            inMapping = false;
        }
    }

    public void endDocument() throws SAXException {
        if (!referencingGlyphDefinitions.isEmpty()) {
            try {
                setReferencedTargets(createIdHashMap());
                addTargetsToRefs();
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }
    }

    private Map<String, GlyphDefinition> createIdHashMap() {
        Map<String, GlyphDefinition> ids = new HashMap<String, GlyphDefinition>();
        for (GlyphDefinition d : glyphDefinitions) {
            ids.put(d.getId(), d);
        }
        return ids;
    }

    private void setReferencedTargets(Map<String, GlyphDefinition> ids) {
        for (GlyphDefinition r : referencingGlyphDefinitions) {
            for (GlyphRef ref : r.getGlyphRefs()) {
                GlyphDefinition target = ids.get(ref.getTargetId());
                if (target != null) {
                    ref.setTarget(target);
                }
            }
        }
    }

    private void addTargetsToRefs() {
        // TODO recurse glyphdefinitions in order to include references
        // of
        // references

        for (GlyphDefinition r : referencingGlyphDefinitions) {
            int offset = 0;
            StringBuilder sb = new StringBuilder(r.getCodePoint());
            for (GlyphRef ref : r.getGlyphRefs()) {
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