package com.aerhard.oxygen.plugin.glyphpicker.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "charDecl", namespace="http://www.tei-c.org/ns/1.0")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserListModel extends AbstractListModel<GlyphDefinition> {
    @XmlTransient
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "char", namespace="http://www.tei-c.org/ns/1.0")
    private List<GlyphDefinition> data = new ArrayList<GlyphDefinition>();

    public UserListModel(List<GlyphDefinition> arrayList) {
        data = arrayList;
    }

    public UserListModel() {
    }

    public void addElement(GlyphDefinition item) {
        data.add(item);
        fireContentsChanged(this, 0, getSize());
    }

    public void removeElement(int index) {
        GlyphDefinition removed = data.remove(index);
        if (removed != null) {
            fireContentsChanged(this, 0, getSize());
        }
        fireIntervalRemoved(index, 0, getSize());
    }

    public int getSize() {
        return data.size();
    }

    public GlyphDefinition getElementAt(int i) {
        return data.get(i);
    }

    /**
     * @return the data
     */
    public List<GlyphDefinition> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(List<GlyphDefinition> data) {
        this.data = data;
    }
}
