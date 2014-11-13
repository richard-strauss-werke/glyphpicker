package com.aerhard.oxygen.plugin.glyphpicker.model.tei;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "charDecl")
@XmlAccessorType(XmlAccessType.FIELD)
public class GlyphList {

    @XmlElement(name = "desc")
    private String desc;
    @XmlElement(name = "char")
    private List<GlyphItem> chars = new ArrayList<GlyphItem>();

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc
     *            the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return the chars
     */
    public List<GlyphItem> getChars() {
        return chars;
    }

    /**
     * @param chars
     *            the chars to set
     */
    public void setChars(List<GlyphItem> chars) {
        this.chars = chars;
    }

    public GlyphList() {

    }

    public void addAdditionalData(String baseUrl) {
        for (GlyphItem model : chars) {
            model.setBaseUrl(baseUrl);
            model.setRange(desc);
        }

    }

}
