package com.aerhard.oxygen.plugin.glyphpicker.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "charDecl")
@XmlAccessorType(XmlAccessType.FIELD)
public class GlyphDefinitions {
    @XmlTransient
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "char")
    private List<GlyphDefinition> data = new ArrayList<>();

    public GlyphDefinitions(List<GlyphDefinition> data) {
        this.data = data;
    }

    public GlyphDefinitions() {
    }

    public void setData(List<GlyphDefinition> data) {
        this.data = data;
    }

    public List<GlyphDefinition> getData() {
        return data;
    }

}
