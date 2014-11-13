package com.aerhard.oxygen.plugin.glyphpicker.model.tei;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "charDecl")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserListModel extends AbstractListModel<GlyphItem> {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "char")
    private List<GlyphItem> data = new ArrayList<GlyphItem>();

    public UserListModel(List<GlyphItem> arrayList) {
        data = arrayList;
    }

    public UserListModel() {
    }

    public void addElement(GlyphItem item) {
        data.add(item);
        fireContentsChanged(this, 0, getSize());
    }

    public void removeElement(int index) {
        GlyphItem removed = data.remove(index);
        if (removed != null) {
            fireContentsChanged(this, 0, getSize());
        }
        fireIntervalRemoved(index, 0, getSize());
    }

    public int getSize() {
        return data.size();
    }

    public GlyphItem getElementAt(int i) {
        return data.get(i);
    }
}
