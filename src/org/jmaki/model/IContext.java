/*
 * jMaki Version 2
 *
 * Copyright (c) 2009 Greg Murray (jmaki.org)
 * 
 * Licensed under the MIT License:
 * 
 *  http://www.opensource.org/licenses/mit-license.php
 *
 */
package org.jmaki.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface IContext {

    public boolean isDevMode();

    public String generateUuid(String prefix);

    public long getLastUpdated(String fullURI);

    public boolean isUpdated(String fullURI, long lastUpdated);

    public String getContextRoot();

    public String getWebRoot();

    public IConfig getGlobalConfig();
    
    public Map<String, ?> getAttributes();

    public Object getGlobalAttribute(String key);

    public void setGlobalAttribute(String key, Object value);

    public Object getAttribute(String key);

    public void setAttribute(String key, Object value);

    public StringBuffer getResource(String baseDir, String name) throws IOException;

    public InputStream getResourceAsStream(String name) throws IOException;

    public Object parseExpression(String expression); 

    public boolean resourceExists(String name);

    public boolean uaTest(String test);

    public boolean test(String test);
}
