package com.aerhard.oxygen.plugin.glyphpicker.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dataSource")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataSource implements Cloneable {

    public static final String DISPLAY_MODE_VECTOR_PROPORTIONAL = "proportional";
    public static final String DISPLAY_MODE_VECTOR_FIT = "fit";
    public static final String DISPLAY_MODE_BITMAP = "bitmap";

    @XmlElement(name = "label")
    private String label;
    @XmlElement(name = "basePath")
    private String basePath;
    @XmlElement(name = "fontName")
    private String fontName;
    @XmlElement(name = "displayMode")
    private String displayMode;
    @XmlElement(name = "sizeFactor")
    private Float sizeFactor = 0.5f;
    @XmlElement(name = "template")
    private String template = "";
    @XmlElement(name = "mappingTypeValue")
    private String mappingTypeValue = null;
    @XmlElement(name = "mappingSubTypeValue")
    private String mappingSubTypeValue = null;

    public DataSource() {
    }

    public String getMappingTypeValue() {
        return mappingTypeValue;
    }

    public void setMappingTypeValue(String mappingAttName) {
        this.mappingTypeValue = mappingAttName;
    }

    public String getMappingSubTypeValue() {
        return mappingSubTypeValue;
    }

    public void setMappingSubTypeValue(String mappingAttValue) {
        this.mappingSubTypeValue = mappingAttValue;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
    }

    public Float getSizeFactor() {
        return sizeFactor;
    }

    public void setSizeFactor(Float sizeFactor) {
        this.sizeFactor = sizeFactor;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String path) {
        this.basePath = path;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public DataSource clone() throws CloneNotSupportedException {
        return (DataSource) super.clone();
    }
}