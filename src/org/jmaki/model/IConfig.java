package org.jmaki.model;

public interface IConfig {
    
    public static final String CONTEXT_ROOT = "CONTEXT_ROOT";
    public static final String MEDIA_TYPE = "MEDIA_TYPE";
    public static final String RESOURCE_ROOT = "RESOURCE_ROOT";
    public static final String ENCODING = "ENCODING";
    public static final String DEV_MODE = "DEV_MODE";

    public Object getAttribute(String key);
    public void setAttribute(String key, Object value);
}
