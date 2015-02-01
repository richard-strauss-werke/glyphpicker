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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A model containing data of a single data source.
 */
@XmlRootElement(name = "dataSource")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataSource implements Cloneable {

    /** The default glyph display size factor. */
    private static final float DEFAULT_SIZE_FACTOR = 0.5f;
    
    /** The display mode value "proportional". */
    public static final String DISPLAY_MODE_VECTOR_PROPORTIONAL = "proportional";
    
    /** The display mode value "fit". */
    public static final String DISPLAY_MODE_VECTOR_FIT = "fit";
    
    /** The display mode value "bitmap". */
    public static final String DISPLAY_MODE_BITMAP = "bitmap";

    /** The label. */
    @XmlElement(name = "label")
    private String label;
    
    /** The base path. */
    @XmlElement(name = "basePath")
    private String basePath;
    
    /** The font name. */
    @XmlElement(name = "fontName")
    private String fontName;
    
    /** The display mode. */
    @XmlElement(name = "displayMode")
    private String displayMode;
    
    /** The size factor. */
    @XmlElement(name = "sizeFactor")
    private Float sizeFactor = DEFAULT_SIZE_FACTOR;
    
    /** The template. */
    @XmlElement(name = "template")
    private String template = "";
    
    /** The value of the type attribute to be matched to get the relevant mapping element. */
    @XmlElement(name = "mappingTypeValue")
    private String mappingTypeValue = null;
    
    /** The value of the subtype attribute to be matched to get the relevant mapping element. */
    @XmlElement(name = "mappingSubTypeValue")
    private String mappingSubTypeValue = null;
    
    /** Indicates if the string in <mapping> needs to be parsed in order to obtain code points. */
    @XmlElement(name = "mappingAsCharString")
    private boolean mappingAsCharString = false;

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
    public String getMappingTypeValue() {
        return mappingTypeValue;
    }

    /**
     * Sets the mapping/@type value.
     *
     * @param mappingAttName the new mapping/@type value
     */
    public void setMappingTypeValue(String mappingAttName) {
        this.mappingTypeValue = mappingAttName;
    }

    /**
     * Gets the mapping/@subtype value.
     *
     * @return the mapping subtype value
     */
    public String getMappingSubTypeValue() {
        return mappingSubTypeValue;
    }

    /**
     * Sets the mapping/@subtype value.
     *
     * @param mappingAttValue the new mapping sub type value
     */
    public void setMappingSubTypeValue(String mappingAttValue) {
        this.mappingSubTypeValue = mappingAttValue;
    }

    /**
     * Gets the value of mappingAsCharString.
     *
     * @return the value of mappingAsCharString
     */
    public boolean getMappingAsCharString() {
        return mappingAsCharString;
    }

    /**
     * Sets the value of mappingAsCharString.
     *
     * @param mappingAsCharString the new mapping as char string
     */
    public void setMappingAsCharString(boolean mappingAsCharString) {
        this.mappingAsCharString = mappingAsCharString;
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
    public String getDisplayMode() {
        return displayMode;
    }

    /**
     * Sets the display mode.
     *
     * @param displayMode the new display mode
     */
    public void setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
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
}