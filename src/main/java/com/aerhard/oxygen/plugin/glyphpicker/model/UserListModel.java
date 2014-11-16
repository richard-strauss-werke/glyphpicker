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
public class UserListModel extends AbstractListModel<GlyphDefinition> implements GlyphListModel {
    @XmlTransient
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "char", namespace="http://www.tei-c.org/ns/1.0")
    private List<GlyphDefinition> data = new ArrayList<GlyphDefinition>();

    /**
     * specifies if the current data is in sync with the data storage.
     * The value is initially true, because each list model instance gets 
     * created by unmarshalling XML
     */
    private Boolean inSync = true;
    
    public Boolean isInSync() {
        return inSync;
    }
    
    public void setInSync(Boolean inSync) {
        this.inSync = inSync;
    }
    
    public UserListModel(List<GlyphDefinition> data) {
        this.data = data;
    }

    public UserListModel() {
    }

    public void addElement(GlyphDefinition item) {
        inSync = false;
        data.add(item);
        fireContentsChanged(this, 0, getSize());
    }

    public void removeElement(int index) {
        inSync = false;
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
    
    public void setData(List<GlyphDefinition> data) {
        inSync = true;
        this.data = data;
        fireContentsChanged(this, 0, getSize());
    }
    
    public void applyModel(GlyphListModel model) {
        data = model.getData();
        // TODO sync property
    }
    
    public List<GlyphDefinition> getData(){
        return data;
    }

 
}
