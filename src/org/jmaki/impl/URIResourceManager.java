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
package org.jmaki.impl;

import java.util.Iterator;
import java.util.List;

import org.jmaki.model.IContext;
import org.jmaki.model.impl.GlobalConfig;
import org.jmaki.model.impl.ResourceURI;

public class URIResourceManager {

    public static String generateReferences(IContext ctx, List<ResourceURI> resources, int type) {
        String buff = "";

            if (resources != null) {

                Iterator<ResourceURI> it = resources.iterator();
                while (it.hasNext()) {

                    ResourceURI ri = it.next();
                    if (ri.isWritten()) continue;
                    String resource = ri.getURI();
                    String baseURI =  ctx.getContextRoot();

                    if (!ri.isExternal()){
                        // map to root
                        if (resource.startsWith("/")) {
                            baseURI = ctx.getContextRoot();
                        } else {
                           baseURI +=  ri.getBaseURI();
                        }
                    } else {
                        baseURI = "";
                    }
                    if (type == ResourceURI.SCRIPT) {
                            buff += "<script type=\"text/javascript\" src=\"" + baseURI + resource + "\"></script>\n";
                    } else if (type == ResourceURI.LINK) {
                        String mediaType = ri.getMediaType();
                        if (mediaType == null){
                            mediaType = (String) ctx.getGlobalConfig().getAttribute(GlobalConfig.MEDIA_TYPE);
                        }
                        buff += "<link rel=\"stylesheet\" type=\"text/css\"  href=\"" + baseURI + resource + "\" media=\"" + mediaType + "\" />\n";
                    }
                }
            }
            return buff;
    }

}
