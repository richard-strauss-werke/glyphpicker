package com.aerhard.oxygen.plugin.glyphpicker.model;

import java.util.List;

public interface GlyphListModel {
    public void setData(List<GlyphDefinition> data);
    
    public List<GlyphDefinition> getData();
    
    public void applyModel(GlyphListModel model);
}
