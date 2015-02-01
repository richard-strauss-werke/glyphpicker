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

@XmlRootElement(name = "char")
@XmlAccessorType(XmlAccessType.FIELD)
public class GlyphDefinition implements Cloneable {

    @XmlAttribute(name = "id", namespace = "http://www.w3.org/XML/1998/namespace")
    private String id;

    @XmlElement(name = "charName")
    private String charName;

    @XmlElement(name = "mapping")
    private String codePoint;

    @XmlElement(name = "graphic")
    private String url;

    @XmlElement(name = "range")
    private String range;

    @XmlElement(name = "g")
    @XmlElementWrapper(name = "glyphRefs")
    private List<GlyphRef> glyphRefs = null;

    @XmlElement(name = "dataSource")
    private DataSource dataSource;

    @XmlTransient
    private ImageIcon icon = null;

    public GlyphDefinition() {
    }

    public String getCharName() {
        return charName;
    }

    public void setCharName(String label) {
        this.charName = label;
    }

    public String getCodePoint() {
        return codePoint;
    }

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

    public void setCodePoint(String codePoint) {
        this.codePoint = codePoint;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public List<GlyphRef> getGlyphRefs() {
        return glyphRefs;
    }

    public void setGlyphRefs(List<GlyphRef> glyphRefs) {
        this.glyphRefs = glyphRefs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setIcon(ImageIcon image) {
        this.icon = image;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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

    @Override
    public GlyphDefinition clone() throws CloneNotSupportedException {
        return (GlyphDefinition) super.clone();
    }

    @Override
    public String toString() {
        return getCodePointString() + ": " + charName + " (" + range + ")";
    }

}
