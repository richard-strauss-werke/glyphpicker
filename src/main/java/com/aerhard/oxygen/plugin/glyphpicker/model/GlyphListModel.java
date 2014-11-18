package com.aerhard.oxygen.plugin.glyphpicker.model;

import java.util.List;

public interface GlyphListModel {
    void setData(List<GlyphDefinition> data);
    
    List<GlyphDefinition> getData();
}
