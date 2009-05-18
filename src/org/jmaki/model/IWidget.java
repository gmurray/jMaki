/*
 * jMaki Version 2
 *
 * Copyright (c) 2009 jmaki.org
 * 
 * Licensed under the MIT License:
 * 
 *  http://www.opensource.org/licenses/mit-license.php
 *
 */
package org.jmaki.model;

import java.util.List;
import org.jmaki.model.impl.ResourceURI;
import org.jmaki.model.impl.WidgetConfig;

public interface IWidget {

    public WidgetConfig getWidgetConfig();
    public void setWidgetConfig(WidgetConfig config);
    public Object getArgs();
    public void setArgs(Object args);
    public String getName();
    public String getPublish();
    public List<ResourceURI> getScripts();
    public void setScripts(List<ResourceURI> scripts);
    public String getService();
    public void setService(String service);
    public List<ResourceURI> getStyles();
    public void setStyles(List<ResourceURI> styles);
    public String getSubscribe();
    public void setSubscribe(String subscribe);
    public String getTemplate();
    public void setTemplate(String template);
    public String getUuid();
    public Object getValue();
    public void setValue(Object value);
}
