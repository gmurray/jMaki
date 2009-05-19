package org.jmaki;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jmaki.model.IWidget;
import org.jmaki.model.impl.GlobalConfig;
import org.jmaki.model.impl.WebContext;
import org.jmaki.model.impl.WidgetConfig;
import org.jmaki.model.impl.WidgetFactory;
import org.jmaki.model.impl.WidgetImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.protorabbit.model.IParameter;

import org.protorabbit.model.ITemplate;
import org.protorabbit.model.impl.BaseCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WidgetCommand extends BaseCommand {

    private static Logger logger = null;

    public static final Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger("org.protrabbit");
        }
        return logger;
    }

    @Override
    public void doProcess(OutputStream out) throws IOException {
        System.out.println("param 1 is " + params[0] + " type is " + params[0].getType());
        if (params.length < 1 || params[0].getType() != IParameter.OBJECT) {
            getLogger().severe("Widget requires at least a name property");
            return;
        }
        JSONObject jo = (JSONObject)params[0].getValue();
        String name = null;
        String uuid = null;
        if (jo.has("name")) {
            try {
                name = jo.getString("name");
            } catch (JSONException e) {
              // do nothing
            }
        }
        if (jo.has("id")) {
            try {
                uuid = jo.getString("id");
            } catch (JSONException e) {
                // do nothing
            }
        }
        if (name == null){
            getLogger().severe("Widget requires at least a name property");
            return;
        }
        System.out.println("name is " + name);
        GlobalConfig gcfg = new GlobalConfig();
        // get request and response off the protorabbit context
        HttpServletRequest req = ((org.protorabbit.servlet.WebContext)this.ctx).getRequest();
        HttpServletResponse resp =  ((org.protorabbit.servlet.WebContext)this.ctx).getResponse();
        // create a web context
        WebContext wc = new WebContext( req, resp, gcfg);
        if (uuid == null) {
            uuid = wc.generateUuid(name.replace(".", "_"));
        }
        WidgetConfig wcfg = WidgetFactory.loadConfig(wc,name);
        IWidget widget = new WidgetImpl(name, uuid);

        String tid = ctx.getTemplateId();
        ITemplate template = ctx.getConfig().getTemplate(tid);
        if (template != null) {
            // write the template
            out.write(WidgetFactory.getWidgetFragment(widget, wcfg, wc).toString().getBytes());
            // copy in the dependnecies
            List<org.protorabbit.model.impl.ResourceURI> scripts = template.getScripts();

            List<org.jmaki.model.impl.ResourceURI> wscripts = wcfg.getScripts();
            if (wscripts != null) {
                if (scripts == null) {
                    scripts = new ArrayList<org.protorabbit.model.impl.ResourceURI>();
                    template.setScripts(scripts);
                }
                for (org.jmaki.model.impl.ResourceURI ri : wscripts) {
                    scripts.add(new org.protorabbit.model.impl.ResourceURI(ri.getUri(), ri.getBaseURI(), ri.getType()));
                }
            }
            List<org.protorabbit.model.impl.ResourceURI> styles = template.getStyles();
            List<org.jmaki.model.impl.ResourceURI> wstyles = wcfg.getStyles();
            if (wstyles != null) {
                if (styles == null) {
                    styles = new ArrayList<org.protorabbit.model.impl.ResourceURI>();
                    template.setStyles(styles);
                }
                for (org.jmaki.model.impl.ResourceURI ri : wstyles) {
                    styles.add(new org.protorabbit.model.impl.ResourceURI(ri.getUri(), ri.getBaseURI(), ri.getType()));
                }
            }
        }
    }
}
