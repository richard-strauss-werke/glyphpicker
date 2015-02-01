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
package com.aerhard.oxygen.plugin.glyphpicker.model;

import java.util.List;

import javax.swing.ImageIcon;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The glyph definition model.
 */
@XmlRootElement(name = "char")
@XmlAccessorType(XmlAccessType.FIELD)
public class GlyphDefinition implements Cloneable {

    /** The id. */
    @XmlAttribute(name = "id", namespace = "http://www.w3.org/XML/1998/namespace")
    private String id;

    /** The char name. */
    @XmlElement(name = "charName")
    private String charName;

    /** The code point. */
    @XmlElement(name = "mapping")
    private String codePoint;

    /** The url. */
    @XmlElement(name = "graphic")
    private String url;

    /** The range. */
    @XmlElement(name = "range")
    private String range;

    /** The glyph references in the relevant <mapping> of the glyph definition. */
    @XmlElement(name = "g")
    @XmlElementWrapper(name = "glyphReferences")
    private List<GlyphReference> glyphReferences = null;

    /** The data source in which the current glyph definition originates. */
    @XmlElement(name = "dataSource")
    private DataSource dataSource;

    /** The icon component. */
    @XmlTransient
    private ImageIcon icon = null;

    /**
     * Instantiates a new GlyphDefinition.
     */
    public GlyphDefinition() {
    }

    /**
     * Gets the char name.
     *
     * @return the char name
     */
    public String getCharName() {
        return charName;
    }

    /**
     * Sets the char name.
     *
     * @param label the new char name
     */
    public void setCharName(String label) {
        this.charName = label;
    }

    /**
     * Gets the code point.
     *
     * @return the code point
     */
    public String getCodePoint() {
        return codePoint;
    }

    /**
     * Formats the code points as Strings "U+xxxx".
     *
     * @return the code point string
     */
    public String getCodePointString() {

        if (codePoint != null) {
            StringBuilder sb = new StringBuilder();
            for (Character c : codePoint.toCharArray()) {
                sb.append("U+");
                sb.append(String.format("%X", (int) c));
                sb.append(" ");
            }
            return sb.toString();
        }
        return null;

    }

    /**
     * Sets the code point.
     *
     * @param codePoint the new code point
     */
    public void setCodePoint(String codePoint) {
        this.codePoint = codePoint;
    }

    /**
     * Gets the range.
     *
     * @return the range
     */
    public String getRange() {
        return range;
    }

    /**
     * Sets the range.
     *
     * @param range the new range
     */
    public void setRange(String range) {
        this.range = range;
    }

    /**
     * Gets the glyph references.
     *
     * @return the glyph references
     */
    public List<GlyphReference> getGlyphReferences() {
        return glyphReferences;
    }

    /**
     * Sets the glyph references.
     *
     * @param glyphReferences the new glyph references
     */
    public void setGlyphReferences(List<GlyphReference> glyphReferences) {
        this.glyphReferences = glyphReferences;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the url.
     *
     * @param url the new url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the icon.
     *
     * @param image the new icon
     */
    public void setIcon(ImageIcon image) {
        this.icon = image;
    }

    /**
     * Gets the icon.
     *
     * @return the icon
     */
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * Gets the data source.
     *
     * @return the data source
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Sets the data source.
     *
     * @param dataSource the new data source
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Gets the string to insert into the XML document as value of the ref attribute.
     *
     * @return the string
     */
    public String getRefString() {

        String template = getDataSource().getTemplate();

        // if no template is specified, use default template
        if (template == null) {
            return getDataSource().getBasePath() + "#" + getId();
        }

        return template.replaceAll("\\$\\{basePath\\}",
                getDataSource().getBasePath()).replaceAll("\\$\\{id\\}",
                getId());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public GlyphDefinition clone() throws CloneNotSupportedException {
        return (GlyphDefinition) super.clone();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getCodePointString() + ": " + charName + " (" + range + ")";
    }

}
