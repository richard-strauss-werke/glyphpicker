package com.aerhard.oxygen.plugin.glyphpicker.model;

import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "paths")
@XmlAccessorType(XmlAccessType.FIELD)
public class PathComboModel extends AbstractListModel<String> implements
        ComboBoxModel<String> {
    private static final long serialVersionUID = 1L;

    private static final int ITEM_MAX = 20;

    @XmlTransient
    private Object selectedItem;

    @XmlElement(name = "path")
    private List<String> data;

    public PathComboModel(List<String> arrayList) {
        data = arrayList;
        if (arrayList.size() > 0) {
            selectedItem = data.get(0);
        }
    }

    public PathComboModel() {
    }

    public void init() {
        if (data != null && data.size() > 0) {
            selectedItem = data.get(0);
        }
    }

    public void setFirstItem(String item) {
        data.add(0, item);
        for (int i = data.size() - 1; i > 0; i--) {
            if (item.equals(data.get(i)) || i > ITEM_MAX) {
                data.remove(i);
            }
        }
        fireContentsChanged(item, -1, -1);
    }

    public Object getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Object newValue) {
        selectedItem = newValue;
        fireContentsChanged(newValue, -1, -1);
    }

    public int getSize() {
        return data.size();
    }

    public String getElementAt(int i) {
        return data.get(i);
    }
}
