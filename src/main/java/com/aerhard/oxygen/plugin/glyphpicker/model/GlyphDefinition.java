package com.aerhard.oxygen.plugin.glyphpicker.model;

import javax.swing.ImageIcon;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "char", namespace = "http://www.tei-c.org/ns/1.0")
@XmlAccessorType(XmlAccessType.FIELD)
public class GlyphDefinition implements Cloneable {

    @XmlAttribute(name = "id", namespace = "http://www.w3.org/XML/1998/namespace")
    private String id;

    @XmlElement(name = "charName", namespace = "http://www.tei-c.org/ns/1.0")
    private String charName;

    @XmlElement(name = "mapping", namespace = "http://www.tei-c.org/ns/1.0")
    private String codePoint;

    @XmlElement(name = "graphic", namespace = "http://www.tei-c.org/ns/1.0")
    private String url;

    @XmlElement(name = "range")
    private String range;

    @XmlElement(name = "dataSource")
    private DataSource dataSource;

    @XmlTransient
    private ImageIcon icon = null;

    public GlyphDefinition() {
    }

    public GlyphDefinition(String id, String name, String codepoint,
            String range, String url, DataSource dataSource) {
        this.id = id;
        this.charName = name;
        this.codePoint = codepoint;
        this.range = range;
        this.url = url;
        this.dataSource = dataSource;
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

    public String getCharString() {
        try {
            String cp = codePoint.substring(2);
            int c = Integer.parseInt(cp, 16);
            return Character.toString((char) c);
        } catch (Exception e) {
            return null;
        }
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

    public String getHTML() {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><p>");

        if (getCharName() != null) {
            sb.append("<nobr><b>");
            sb.append(getCharName());
            sb.append("</b></nobr><br>");
        }

        if (getCodePoint() != null) {
            sb.append("<nobr>Codepoint: ");
            sb.append(getCodePoint());
            sb.append("</nobr><br>");
        }

        if (getRange() != null) {
            sb.append("<nobr>Range: ");
            sb.append(getRange());
            sb.append("</nobr><br>");
        }

        if (getId() != null) {
            sb.append("<nobr>xml:id: <em>");
            sb.append(getId());
            sb.append("</em></nobr><br>");
        }

        sb.append("</p></html>");

        return sb.toString();

    }

    @Override
    public GlyphDefinition clone() throws CloneNotSupportedException {
        return (GlyphDefinition) super.clone();
    }

    @Override
    public String toString() {
        return codePoint + ": " + charName + " (" + range + ")";
    }

}
