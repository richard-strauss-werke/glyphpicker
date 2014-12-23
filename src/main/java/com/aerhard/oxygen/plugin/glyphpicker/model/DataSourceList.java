package com.aerhard.oxygen.plugin.glyphpicker.model;

import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "dataSources")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataSourceList extends AbstractListModel<String> implements
        ComboBoxModel<String> {
    private static final long serialVersionUID = 1L;

    private static final int ITEM_MAX = 20;

    @XmlTransient
    private Object selectedItem;

    @XmlElement(name = "dataSource")
    private List<DataSource> data;

    public DataSourceList(List<DataSource> arrayList) {
        data = arrayList;
        if (arrayList.size() > 0) {
            selectedItem = data.get(0).getLabel();
        }
    }

    public DataSourceList() {
    }

    public void init() {
        if (data != null && data.size() > 0) {
            selectedItem = data.get(0).getLabel();
        }
    }

    public void setFirstIndex(int index) {
        
        DataSource item = getDataSourceAt(index);
        
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
        return data.get(i).getLabel();
    }

    public DataSource getDataSourceAt(int i) {
        return data.get(i);
    }
    
}
