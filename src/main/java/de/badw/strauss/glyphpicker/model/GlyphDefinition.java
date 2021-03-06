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

import javax.swing.*;
import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * The glyph definition model.
 */
@XmlRootElement(name = "char")
@XmlAccessorType(XmlAccessType.FIELD)
public class GlyphDefinition implements Cloneable {

    /**
     * The id.
     */
    @XmlAttribute(name = "id", namespace = "http://www.w3.org/XML/1998/namespace")
    private String id;

    /**
     * The description.
     */
    @XmlElement(name = "charName")
    private String description;


    /**
     * The entity field.
     */
    @XmlElement(name = "entity")
    private String entity;
    
    /**
     * The characters contained or referenced in `<mapping>`.
     */
    @XmlElement(name = "mapping")
    private String mappedChars;

    /**
     * The url.
     */
    @XmlElement(name = "graphic")
    private String url;

    /**
     * The range.
     */
    @XmlElement(name = "range")
    private String range;

    /**
     * The glyph references in the relevant <mapping> of the glyph definition.
     */
    @XmlElement(name = "g")
    @XmlElementWrapper(name = "glyphReferences")
    private List<GlyphReference> glyphReferences = null;

    /**
     * The data source in which the current glyph definition originates.
     */
    @XmlElement(name = "dataSource")
    private DataSource dataSource;

    /**
     * The icon component.
     */
    @XmlTransient
    private ImageIcon icon = null;

    /**
     * Instantiates a new GlyphDefinition.
     */
    public GlyphDefinition() {
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param label the new description
     */
    public void setDescription(String label) {
        this.description = label;
    }


    /**
     * gets the entity field
     * @return the content of the entity field
     */
    public String getEntity() {
        return entity;
    }

    /**
     * sets the value of the entity field
     * @param entity the new value
     */
    public void setEntity(String entity) {
        this.entity = entity;
    }
    
    /**
     * Gets the mapped characters.
     *
     * @return the mapped characters
     */
    public String getMappedChars() {
        return mappedChars;
    }

    /**
     * Sets the mapped characters.
     *
     * @param mappedChars the mapped characters
     */
    public void setMappedChars(String mappedChars) {
        this.mappedChars = mappedChars;
    }

    /**
     * Formats the mapped characters as Strings "U+xxxx".
     *
     * @return the formatted string
     */
    public String getCodePointString() {
        if (mappedChars != null) {
            StringBuilder sb = new StringBuilder();
            for (Character c : mappedChars.toCharArray()) {
                sb.append(String.format("U+%X ", (int) c));
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * Formats the mapped characters as numeric character references "&#xYYYY;".
     *
     * @return the formatted string
     */
    public String getNumericCharRef() {
        if (mappedChars != null) {
            StringBuilder sb = new StringBuilder();
            for (Character c : mappedChars.toCharArray()) {
                sb.append(String.format("&#x%X;", (int) c));
            }
            return sb.toString();
        }
        return null;
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
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
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
     * Gets the icon.
     *
     * @return the icon
     */
    public ImageIcon getIcon() {
        return icon;
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
     * Gets the string to insert into the XML document.
     *
     * @return the string
     */
    public String getXmlString() {

        String template = getDataSource().getTemplate();

        // use a default template if no template is specified
        if (template == null) {
            return String.format("<g ref=\"%s#%s\"/>", getDataSource().getBasePath(), getId());
        }

        return template
                .replaceAll("\\$\\{basePath\\}", getDataSource().getBasePath() == null ? "" : getDataSource().getBasePath())
                .replaceAll("\\$\\{id\\}", getId() == null ? "" : getId())
                .replaceAll("\\$\\{char\\}", getMappedChars() == null ? "" : getMappedChars())
                .replaceAll("\\$\\{num\\}", getNumericCharRef() == null ? "" : getNumericCharRef());
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
        return getCodePointString() + ": " + description + " (" + range + ")";
    }

}
