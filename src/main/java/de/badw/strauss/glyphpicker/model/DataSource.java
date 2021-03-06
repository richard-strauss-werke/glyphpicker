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
package de.badw.strauss.glyphpicker.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A model containing data about a single data source.
 */
@XmlRootElement(name = "glyphTable")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataSource implements Cloneable {

    /**
     * The vector glyph renderer code.
     */
    public static final String GLYPH_VECTOR_RENDERER = "vector";
    /**
     * The scaled vector glyph renderer code.
     */
    public static final String GLYPH_SCALED_VECTOR_RENDERER = "scaled vector";
    /**
     * The bitmap glyph renderer code.
     */
    public static final String GLYPH_BITMAP_RENDERER = "bitmap";
    /**
     * The default glyph display size factor.
     */
    private static final float DEFAULT_SIZE_FACTOR = 0.5f;
    /**
     * The label.
     */
    @XmlElement(name = "name")
    private String label;

    /**
     * The base path.
     */
    @XmlElement(name = "path")
    private String basePath = "";

    /**
     * The font name.
     */
    @XmlElement(name = "font")
    private String fontName;

    /**
     * The glyph renderer.
     */
    @XmlElement(name = "renderer")
    private String glyphRenderer = "bitmap";

    /**
     * The size factor.
     */
    @XmlElement(name = "sizeFactor")
    private Float sizeFactor = DEFAULT_SIZE_FACTOR;

    /**
     * The template.
     */
    @XmlElement(name = "template")
    private String template = "";

    /**
     * The value of the type attribute to be matched to get the relevant mapping element.
     */
    @XmlElement(name = "typeAttributeValue")
    private String typeAttributeValue = null;

    /**
     * The value of the subtype attribute to be matched to get the relevant mapping element.
     */
    @XmlElement(name = "subtypeAttributeValue")
    private String subtypeAttributeValue = null;

    /**
     * Indicates if the string in <mapping> needs to be parsed in order to obtain the actual characters.
     */
    @XmlElement(name = "parseMapping")
    private boolean parseMapping = false;

    /**
     * Instantiates a new DataSource.
     */
    public DataSource() {
    }

    /**
     * Gets the mapping/@type value.
     *
     * @return the mapping/@type value
     */
    public String getTypeAttributeValue() {
        return typeAttributeValue;
    }

    /**
     * Sets the mapping/@type value.
     *
     * @param mappingAttName the new mapping/@type value
     */
    public void setTypeAttributeValue(String mappingAttName) {
        this.typeAttributeValue = mappingAttName;
    }

    /**
     * Gets the mapping/@subtype value.
     *
     * @return the mapping subtype value
     */
    public String getSubtypeAttributeValue() {
        return subtypeAttributeValue;
    }

    /**
     * Sets the mapping/@subtype value.
     *
     * @param mappingAttValue the new mapping sub type value
     */
    public void setSubtypeAttributeValue(String mappingAttValue) {
        this.subtypeAttributeValue = mappingAttValue;
    }

    /**
     * Gets the value of parseMapping.
     *
     * @return the value of parseMapping
     */
    public boolean getParseMapping() {
        return parseMapping;
    }

    /**
     * Sets the value of parseMapping.
     *
     * @param parseMapping the new mapping as char string
     */
    public void setParseMapping(boolean parseMapping) {
        this.parseMapping = parseMapping;
    }

    /**
     * Gets the label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label.
     *
     * @param label the new label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Gets the display mode.
     *
     * @return the display mode
     */
    public String getGlyphRenderer() {
        return glyphRenderer;
    }

    /**
     * Sets the display mode.
     *
     * @param glyphRenderer the new display mode
     */
    public void setGlyphRenderer(String glyphRenderer) {
        this.glyphRenderer = glyphRenderer;
    }

    /**
     * Gets the size factor.
     *
     * @return the size factor
     */
    public Float getSizeFactor() {
        return sizeFactor;
    }

    /**
     * Sets the size factor.
     *
     * @param sizeFactor the new size factor
     */
    public void setSizeFactor(Float sizeFactor) {
        this.sizeFactor = sizeFactor;
    }

    /**
     * Gets the font name.
     *
     * @return the font name
     */
    public String getFontName() {
        return fontName;
    }

    /**
     * Sets the font name.
     *
     * @param fontName the new font name
     */
    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    /**
     * Gets the base path.
     *
     * @return the base path
     */
    public String getBasePath() {
        return basePath;
    }

    /**
     * Sets the base path.
     *
     * @param path the new base path
     */
    public void setBasePath(String path) {
        this.basePath = path;
    }

    /**
     * Gets the template.
     *
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Sets the template.
     *
     * @param template the new template
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return label;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public DataSource clone() throws CloneNotSupportedException {
        return (DataSource) super.clone();
    }

    private boolean equalsWithNulls(Object a, Object b) {
        return a == b || !((a == null) || (b == null)) && a.equals(b);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof DataSource)) {
            return false;
        }
        DataSource ds = (DataSource) obj;

        return equalsWithNulls(label, ds.getLabel())
                && equalsWithNulls(basePath, ds.getBasePath())
                && equalsWithNulls(fontName, ds.getFontName())
                && equalsWithNulls(glyphRenderer, ds.getGlyphRenderer())
                && equalsWithNulls(sizeFactor, ds.getSizeFactor())
                && equalsWithNulls(template, ds.getTemplate())
                && equalsWithNulls(typeAttributeValue, ds.getTypeAttributeValue())
                && equalsWithNulls(subtypeAttributeValue, ds.getSubtypeAttributeValue())
                && equalsWithNulls(parseMapping, ds.getParseMapping());

    }
}