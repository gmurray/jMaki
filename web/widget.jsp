<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@page import="org.jmaki.model.impl.*"%><html>
<%@page import="org.jmaki.model.*"%><html>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Widget Test</title>
</head>
<body>


<%

GlobalConfig gcfg = new GlobalConfig();
WebContext wc = new WebContext(request, response, gcfg);
WidgetConfig wcfg = WidgetFactory.loadConfig(wc, "jmaki.menu2");
IWidget widget = new WidgetImpl("jmaki.menu2", wc.generateUuid("jmaki_menu2"));
%>

<h2>Widget widget.json Test</h2>

<p>
Testing :
</p>

<pre>
GlobalConfig gcfg = new GlobalConfig();
WebContext wc = new WebContext(request, response, gcfg);
WidgetConfig wcfg = WidgetFactory.loadConfig(wc, "jmaki.menu2");
IWidget widget = new WidgetImpl("jmaki.menu2", "id123");
</pre>


<h3>Config is: </h3>
<p>
<%=wcfg.toString().replace("<", "&lt;").replace(">", "&gt;") %>
</p>

<h3>Rendered template is: </h3>
<p>
<%=WidgetFactory.getWidgetFragment(widget, wcfg, wc).toString().replace("<", "&lt;").replace(">", "&gt;") %>
</p>

<h3> Scripts </h3>
<%
if (wcfg.getScripts() != null) {
	out.println("<ul>");
	for ( ResourceURI ri : wcfg.getScripts()) {
		out.print("<li>" + ri.getFullURI() + "</li>" );
	}
    out.println("</ul>");
} else {
	   out.println("N/A");
}
%>

<h3> Styles </h3>
<%
if (wcfg.getStyles() != null) {
    out.println("<ul>");
    for ( ResourceURI ri : wcfg.getStyles()) {
        out.print("<li>" + ri.getFullURI() + "</li>" );
    }
    out.println("</ul>");
} else {
       out.println("N/A");
}
%>

</body>
</html>
