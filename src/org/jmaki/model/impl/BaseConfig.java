package org.jmaki.model.impl;

import java.util.HashMap;
import java.util.Map;

import org.jmaki.model.IConfig;

public class BaseConfig implements IConfig {

    public static final String MEDIA_TYPE = "media_type";

    protected Map<String,Object> attributes;

    public BaseConfig() {
    	attributes = new HashMap<String,Object>();
    }

    void init() {
    	attributes.put(MEDIA_TYPE, "screen, projection");
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void setAttribute(String key, Object value) {
    	attributes.put(key, value);
        
    }

}