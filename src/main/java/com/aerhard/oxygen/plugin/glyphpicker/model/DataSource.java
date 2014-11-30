package com.aerhard.oxygen.plugin.glyphpicker.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dataSource")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataSource {
    
    public static final String DISPLAY_MODE_TEXT = "text";
    public static final String DISPLAY_MODE_BITMAP = "bitmap";
    public static final String DISPLAY_MODE_SHAPE = "shape";
    
    @XmlElement(name = "label")
    private String label;
    @XmlElement(name = "path")
    private String path;
    @XmlElement(name = "fontName")
    private String fontName;
    @XmlElement(name = "displayMode")
    private String displayMode;
    @XmlElement(name = "template")
    private String template;


    public DataSource () {
        
    }
    
    
    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
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
     * @param displayMode the displayMode to set
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    

    /**
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(String template) {
        this.template = template;
    }
}