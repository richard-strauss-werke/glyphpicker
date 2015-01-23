package com.aerhard.oxygen.plugin.glyphpicker.controller;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.model.trans.PropertySelector;

import ca.odell.glazedlists.matchers.Matcher;

public class GlyphMatcher implements Matcher<GlyphDefinition> {
    
    private String query;
    
    private PropertySelector propertySelector;
    
    /**
     * Create a new {@link IssuesForUsersMatcher} that matches only {@link Issue}s
     * that have one or more user in the specified list.
     */
    public GlyphMatcher(PropertySelector propertySelector, String query) {
        this.propertySelector = propertySelector;
        this.query = query;
    }
    
    /**
     * Test whether to include or not include the specified issue based
     * on whether or not their user is selected.
     */
    public boolean matches(GlyphDefinition d) {
        if(query.isEmpty()) return true; 
        
        String property = propertySelector.get(d);
       
        if(d == null || property == null) return false;
        
        return property.contains(query);
    }
}

