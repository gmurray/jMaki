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
package org.jmaki.model.impl;

import java.util.List;

/*
 *  WidgetConfig represents meta data about a widget that is not instance specific
 * 
 */
public class WidgetConfig extends BaseConfig {

    private boolean hasCSS = false;
    private boolean hasTemplate = false;
    private String widgetDir = null;
    private List<ResourceURI> scripts = null;
    private List<ResourceURI> styles = null;
    
    private StringBuffer template = new StringBuffer("<div id=\"${uuid}\"></div>");

    public void setHasTemplate(boolean hasTemplate) {
        this.hasTemplate = hasTemplate;
    }

    public void setHasCss(boolean hasCSS) {
        this.hasCSS = hasCSS;
    }

    public void setWidgetDir(String widgetDir) {
        this.widgetDir = widgetDir;
    }

    public String getWidgetDir() {
        return widgetDir;
    }

    public StringBuffer getTemplate() {
        return template;
    }

    public void setTemplate(StringBuffer template) {
        this.template = template;
    }

    public String toString() {
        return "WidgetConfig { hasCss : " + hasCSS +
               ", hasTemplate : " + hasTemplate+
               ", template : " + template + 
               ", widgetDir : " + widgetDir + "}";
    }

    public void setScripts(List<ResourceURI> scripts) {
        this.scripts = scripts;
    }

    public List<ResourceURI> getScripts() {
        return scripts;
    }

    public void setStyles(List<ResourceURI> styles) {
        this.styles = styles;
    }

    public List<ResourceURI> getStyles() {
        return styles;
    }

}
