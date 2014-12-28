package com.aerhard.oxygen.plugin.glyphpicker.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dataSource")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataSource {

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
    @XmlElement(name = "template")
    private String template;
    @XmlElement(name = "mappingAttName")
    private String mappingAttName;
    @XmlElement(name = "mappingAttValue")
    private String mappingAttValue;

    public DataSource() {

    }

    
    /**
     * @return the mappingAttName
     */
    public String getMappingAttName() {
        return mappingAttName;
    }

    /**
     * @param mappingAttName the mappingAttName to set
     */
    public void setMappingAttName(String mappingAttName) {
        this.mappingAttName = mappingAttName;
    }

    /**
     * @return the mappingAttValue
     */
    public String getMappingAttValue() {
        return mappingAttValue;
    }

    /**
     * @param mappingAttValue the mappingAttValue to set
     */
    public void setMappingAttValue(String mappingAttValue) {
        this.mappingAttValue = mappingAttValue;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label
     *            the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the displayMode
     */
    public String getDisplayMode() {
        return displayMode;
    }

    /**
     * @param displayMode
     *            the displayMode to set
     */
    public void setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
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

    /**
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * @param template
     *            the template to set
     */
    public void setTemplate(String template) {
        this.template = template;
    }
    
    public String toString() {
        return label;
    }
    
    public DataSource clone() {
        DataSource dataSource = new DataSource();
        dataSource.setBasePath(basePath);
        dataSource.setDisplayMode(displayMode);
        dataSource.setFontName(fontName);
        dataSource.setLabel(label);
        dataSource.setMappingAttName(mappingAttName);
        dataSource.setMappingAttValue(mappingAttValue);
        dataSource.setTemplate(template);
        return dataSource;
    }
}