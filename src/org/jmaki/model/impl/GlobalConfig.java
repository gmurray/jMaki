/*
 * jMaki Version 2
 *
 * Copyright (c) 2009 jmaki.org
 * 
 * Licensed under the MIT License:
 * 
 *  http://www.opensource.org/licenses/mit-license.php
 *
 */package org.jmaki.model.impl;

import java.util.Map;

public class GlobalConfig extends BaseConfig {

    public GlobalConfig() {
        init();
    }

    public GlobalConfig(Map<String,Object> attributes) {
        this.attributes = attributes;
        init();
    }

    void init() {
        if (getAttribute(RESOURCE_ROOT) == null) {
            setAttribute(RESOURCE_ROOT, "resources");
        }
        if (getAttribute(ENCODING) == null) {
            setAttribute(ENCODING, "UTF-8");
        }
        if (getAttribute(DEV_MODE) == null) {
            setAttribute(DEV_MODE, new Boolean(false));
        }
    }

}
