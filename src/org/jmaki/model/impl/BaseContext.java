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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.jmaki.model.IContext;

public abstract class BaseContext implements IContext {

    protected Map<String, Object>attributes;

    public BaseContext(){
        this.attributes = new HashMap<String, Object>();
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public Map<String, ?> getAttributes() {
        return attributes;
    }

    public synchronized void setAttribute(String key, Object value) {
        attributes.put(key, value);

    }

    public Object parseExpression(String expression) {
        return getAttribute(expression);
    }

    /*
     * Find a right or left value in an expression like
     * 
     * 'foo' == 'bar'
     * 
     * Where the text to the left of the == is passed in or the right
     * 
     *  This method can also parse expressions like ${foo.bar}
     * 
     */
    public Object findValue(String value) {
        value = value.trim();
        // look for expressions
        int current = value.indexOf("${");
        if (current != -1) {
            String evalue = "";
            int index = 0;
            while (current != -1) {
                evalue += value.substring(index,current);
                int currentEnd = value.indexOf("}", current + 2);
                if (currentEnd != -1) {
                    String expression = value.substring(current + 2,currentEnd);
                    Object o =  parseExpression(expression);
                    return o;
                } else {
                    getLogger().warning("Non fatal error parsing " + value + " no closing bracket.");
                    return null;
                }
            }
        }

        // look for booleans
        if ("true".equals(value.toLowerCase())) {
            return new Boolean(true);
        }
        if ("false".equals(value.toLowerCase())) {
            return new Boolean(false);
        }
        // look for numbers
        try {
            NumberFormat nf = NumberFormat.getInstance();
            Object o = nf.parse(value);
            return o;
        } catch(ParseException pe) {
            // do nothing
        }
        // look for strings
        int start = value.indexOf("'");
        int end = value.indexOf("'", start + 1);
        if (start != -1 && end != -1 && start < end) {
            return value.substring(start + 1,end);
        }
        return null;
    }

    private static Logger logger;

    static Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger("org.jmaki");
        }
        return logger;
    }
}
