window.tablebuilder = function() {

    var _header;
    var _rows = [];

    this.setHeader = function(_headers) {
        _header = "<tr><th>" + _headers.join("</th><th>") + "</tr>";
    };

    this.addRow = function(_cells) {
        _rows.push("<tr><td>" + _cells.join("</td><td>") + "</td></tr>");
    };

    this.toString = function() {
        return  "<table>" + _header + 
          "<tbody>" + _rows.join("") + "</tbody>" +
          "</table>";
    };
};

var blockWidth = 500;

var content = {
                 "text/css" : 0,
                 "text/javascript" : 0,
                 "text/html" : 0,
                 "html fragments" : 0
              };

var accessCounts = [];

var propTypes = {
 1 : 'insert',
 2 : 'include'

};

function createResourcesBlock(t) {

    var te = document.createElement("div");
    te.className = "cacheBlockBox";
    document.body.appendChild(te);
    
    var title = document.createElement("div");
    title.innerHTML = "Resources";
    title.className = "blockTitle";
    te.appendChild(title);
  
    var bb = document.createElement("div");
    bb.className = "cacheBodyBlock";
    te.appendChild(bb);

    var globalContentLength = 0;

    var cachedResources = document.createElement("div");
    cachedResources.innerHTML = "Cached Resources";
    cachedResources.className = "propertiesTitle";
    bb.appendChild(cachedResources);

    var s3tableElement = document.createElement("div");
    if (t.cachedResources) {
        s3tableElement.className = "blockTable";
        var s3table = new tablebuilder();
        s3table.setHeader(["id", "Content Type", "expires", "created", "Max Age", "Last Accessed", "timeout", "Status", "Content Length", "Gzip Content Length", "Access Count", "Gzip Access Count"]);
        var rowCount = 0;
        for (var i in t.cachedResources) {
            var s = t.cachedResources[i];
            var cc = s.cacheContext;
            var cl =  s.contentLength;
            var gcl = s.gzipContentLength;
            if (typeof cl == "number") {
                globalContentLength += cl;
            }
            if (s.contentType) {
                var ctotal = s.gzipContentLength || 0;
                ctotal += s.contentLength || 0;
                content[s.contentType] += ctotal;
            }
            if (typeof gcl == "number") {
                globalContentLength += gcl;
            }
            if (s.accessCount || s.gzipAccessCount) {
                var total = s.accessCount || 0;
                total += s.gzipAccessCount;
                accessCounts.push({ label : i, value : total});
            }
            s3table.addRow([   i || '',
                              s.contentType || '',
                              cc.expires|| '',
                              (new Date(cc.created)),
                              cc.maxAge || '',
                              (new Date(s.lastAccessed)),
                              s.timeout || '',
                              s.status || '',
                              cl || '',
                              gcl || '',
                              s.accessCount,
                              s.gzipAccessCount
                              ]);
            rowCount += 1;
        }
        if (rowCount > 0) {
            s3tableElement.innerHTML = s3table.toString();
        } else {
            s3tableElement.innerHTML = "N/A";
        }

    } else {
      s3tableElement.innerHTML = "N/A";  
    }
    bb.appendChild(s3tableElement);

    var s6TitleElement = document.createElement("div");
    s6TitleElement.className = "propertiesTitle";
    s6TitleElement.innerHTML = "Cached Templates";
    bb.appendChild(s6TitleElement);
    
    var templateResources = [];
    for (var k in t.templates) {
        if (t.templates[k].templateResource) {
            templateResources.push({ id: k, templateResource : t.templates[k].templateResource});
        }
    }
   var s6tableElement = document.createElement("div");

    if (templateResources.length > 0) {
        s6tableElement.className = "blockTable";
        var s6table = new tablebuilder();
        s6table.setHeader(["id", "Content Type", "expires", "created", "Max Age", "Last Accessed", "timeout", "Status", "Content Length", "Gzip Content Length", "Access Count", "Gzip Access Count"]);
        for (var l=0; l < templateResources.length; l+=1 ) {
            var s = templateResources[l].templateResource;
            var cl =  s.contentLength;
            var gcl = s.gzipContentLength;

            if (s.accessCount || s.gzipAccessCount) {
                var total = s.accessCount || 0;
                total += s.gzipAccessCount;
                accessCounts.push({ label : templateResources[l].id, value : total});
            }

            if (s.contentType) {
                var ctotal = cl || 0;
                ctotal += gcl || 0;
                content[s.contentType] += ctotal;
                globalContentLength += ctotal;
            }
            s6table.addRow([   templateResources[l].id || '',
                               s.contentType,
                               s.cacheContext.expires,
                               (new Date(s.cacheContext.created)),
                               s.cacheContext.maxAge,
                               (new Date(s.lastAccessed)),
                               s.timeout || '',
                               s.status,
                               cl || '',
                               gcl || '',
                               s.accessCount,
                               s.gzipAccessCount
                              ]);
        }
        s6tableElement.innerHTML = s6table.toString();
    } else {
      s6tableElement.innerHTML = "N/A";
    }
    bb.appendChild(s6tableElement);

    // list the fragments
    var cachedFragments = document.createElement("div");
    cachedFragments.innerHTML = "Cached Fragments";
    cachedFragments.className = "propertiesTitle";
    bb.appendChild(cachedFragments);
    
    var s4tableElement = document.createElement("div");
    if (t.includeFiles) {
        s4tableElement.className = "blockTable";
        var s4table = new tablebuilder();
        s4table.setHeader(["id", "timeout", "Created", "Last Refresh", "Content Length"]);
        var rowCount = 0;
        for (var i in t.includeFiles) {
            var s =t.includeFiles[i];
            var cl =  s.contentLength;

            if (typeof cl == "number") {
                globalContentLength += cl;
                content["html fragments"] += cl;
            }
            s4table.addRow([   i || '',
                               s.timeout || '',
                              (new Date(s.created)),
                              (new Date(s.lastRefresh)),
                              cl || ''
                              ]);
            rowCount +=1;
        }
        if (rowCount > 0) {
            s4tableElement.innerHTML = s4table.toString();
        } else {
            s4tableElement.innerHTML = "N/A";
        }
    } else {
      s4tableElement.innerHTML = "N/A";
    }
    bb.appendChild(s4tableElement);

    var cachedTemplates = document.createElement("div");
    cachedTemplates.innerHTML = "Cached Template Skeletons (excluding fragments)";
    cachedTemplates.className = "propertiesTitle";
    bb.appendChild(cachedTemplates);
    
    var s5tableElement = document.createElement("div");
    if (t.templates) {
        s5tableElement.className = "blockTable";
        var s5table = new tablebuilder();
        s5table.setHeader(["id", "Created", "Last Refresh", "Content Length"]);
        var rowCount = 0;
        for (var i in t.templates) {
            var s = t.templates[i].documentContext;
            if (s == null) {
                continue;
            }
            var cl =  s.contentLength;

            if (s.contentType) {
                var ctotal = cl || 0;
                content[s.contentType] += ctotal;
                globalContentLength += ctotal;
            }
            s5table.addRow([   i || '',
                              (new Date(s.created)),
                              (new Date(s.lastRefresh)),
                              cl || ''
                              ]);
            rowCount +=1;
        }
        if (rowCount > 0) {
            s5tableElement.innerHTML = s5table.toString();
        } else {
            s5tableElement.innerHTML = "N/A";
        }
    } else {
      s5tableElement.innerHTML = "N/A";
    }
    bb.appendChild(s5tableElement);

    var total = document.getElementById("totalResources");
    var gcl = globalContentLength + " bytes";
    if (globalContentLength > 1024) {
        gcl = (globalContentLength / 1024).toFixed(2) + " kb";
    }
    total.innerHTML = "Total : " + gcl;


}

function getPosition(_e){
         var pX = 0;
         var pY = 0;
         if(_e.offsetParent) {
             while(true){
                 pY += _e.offsetTop;
                 pX += _e.offsetLeft;
                 if(_e.offsetParent === null){
                     break;
                 }
                 _e = _e.offsetParent;
             }
         } else if(_e.y) {
                 pY += _e.y;
                 pX += _e.x;
         }
         return  {x: pX, y: pY};
}

function getXHR () {
    if (window.XMLHttpRequest) {
        return new window.XMLHttpRequest();
    } else if (window.ActiveXObject) {
        return new window.ActiveXObject("Microsoft.XMLHTTP");
    } else {
        return null;
    }
}

function ajax(args) {
    var _req = getXHR();
    _req.onreadystatechange = function() { 
        if (_req.readyState == 4) {
            if ((_req.status == 200 || _req.status === 0) &&
                    args.callback) {
              args.callback(_req);
            } else if (_req.status != 200) {
                alert("Error making request. Please try again later");
            }
        }
    };
    _req.open("GET", args.url, true);
    _req.send(null);
}

function loadData() {
    var req = new ajax({ 
            url : "../prt?command=stats",
            callback : function(req) {
                var model = eval("(" + req.responseText + ")");
                createResourcesBlock(model);
                createChart(model);
            }
        }); 
}

function createChart(model) {
   var chart = jmaki.getWidget("dispo");
   var data = [];
   for (var i in content) {
      if (content[i] > 0) {
          data.push({ label : i, values : [ { value : content[i] }] });
      }
   }
   chart.setValue(data);
   var rchart = jmaki.getWidget("topResources");
   var hdata = [];

   function sortByValue(a, b) {
        var y = a.value;
        var x = b.value;
        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
   }
   accessCounts.sort(sortByValue);
   // only list the top 10 resources
   for (var i=0; i < accessCounts.length && i < 10; i+= 1) {
      hdata.push({ label : accessCounts[i].label , values : [ { value : accessCounts[i].value }] });
   }
   rchart.setValue(hdata);
}