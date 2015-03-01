/**
 * Copyright 2015 Alexander Erhard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.badw.strauss.glyphpicker.model;

import javax.swing.*;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The data source combo box model.
 */
@XmlRootElement(name = "glyphTables")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataSourceList extends AbstractListModel<String> implements
        ComboBoxModel<String> {

    private static final long serialVersionUID = 1L;

    /**
     * The maximum number of items in the list.
     */
    private static final int ITEM_MAX = 20;

    /**
     * The selected item.
     */
    @XmlTransient
    private Object selectedItem;

    /**
     * The data sources.
     */
    @XmlElement(name = "glyphTable")
    private List<DataSource> data = new ArrayList<DataSource>();

    /**
     * Instantiates a new DataSourceList.
     */
    public DataSourceList() {
    }

    /**
     * Initializes the model.
     */
    public void init() {
        if (data != null && data.size() > 0) {
            selectedItem = data.get(0).getLabel();
        }
    }

    /**
     * Sets the first index.
     *
     * @param index the new first index
     */
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

    /* (non-Javadoc)
     * @see javax.swing.ComboBoxModel#getSelectedItem()
     */
    public Object getSelectedItem() {
        return selectedItem;
    }

    /* (non-Javadoc)
     * @see javax.swing.ComboBoxModel#setSelectedItem(java.lang.Object)
     */
    public void setSelectedItem(Object newValue) {
        selectedItem = newValue;
        fireContentsChanged(newValue, -1, -1);
    }

    /* (non-Javadoc)
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize() {
        return data.size();
    }

    /**
     * Gets the data source's label at the specified index.
     *
     * @param i the index
     * @return the label
     */
    public String getElementAt(int i) {
        return data.get(i).getLabel();
    }

    /**
     * Gets the data source at the specified index.
     *
     * @param i the index
     * @return the data source
     */
    public DataSource getDataSourceAt(int i) {
        return data.get(i);
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public List<DataSource> getData() {
        return data;
    }

}
