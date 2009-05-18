jmaki.namespace("jmaki.widgets.jmaki.menu2");

jmaki.widgets.jmaki.menu2.Widget = function(wargs) {
    
    var _widget = this;
    this.model = null;	
    var container = document.getElementById(wargs.uuid);

    var topMenus = [];
    var navMenus = [];
    var menus = [];
    var hideTimer;
    var menu = [];
    var publish = "/jmaki/menu2";
    
    var themes = {
    	  kame : 'green',
    	  ocean : 'blue'
    	};
    var currentTheme = themes['ocean'];
    if (jmaki.config && jmaki.config.globalTheme)  {
        if (themes[jmaki.config.globalTheme]) currentTheme = themes[jmaki.config.globalTheme];
    }
    container.className += " jmk-menu2-title-" + currentTheme;

    function addStyle(style, nStyle){
       if (style.indexOf(nStyle) != -1) return style;
       if (style.length > 0) style += " ";
       return (style + nStyle);
    }

    function removeStyle(style, oStyle){
        if (style.indexOf(oStyle) == -1) return style;
        var styles = style.split(' ');
        var nStyle = "";
        for (var i = 0; i < styles.length; i++) {
            if (styles[i] != oStyle) nStyle += styles[i] + " ";
        }
        return nStyle;
    }

    function showMenu(e){
        hideMenus();
        var src = (typeof window.event == 'undefined') ? e.target : window.event.srcElement;
        var pos = jmaki.getPosition(topMenus[src.id]);
        navMenus[src.id].style.top = pos.y + container.clientHeight - 2 + "px";
        navMenus[src.id].style.left = pos.x + "px";
        navMenus[src.id].style.display = "block";
    }

    function processActions(_t, _pid, _type, _value) {
    	jmaki.processActions( {
    		value : _value,
    		type : _type,
    		action : _t.action,
    		widgetId : wargs.uud,
    		targetId : _pid,
    		topic : publish
    	});
    }

    function labelSelect(e) {
        hideMenus();
        var src = (typeof window.event == 'undefined') ? e.target : window.event.srcElement;
        var sp = src.id.split("_");
        var url = menu[sp[0]].url;
	if (typeof url == 'undefined') {
	    var href = menu[sp[0]].href;
	} else {
	    var href = url;
	    jmaki.log("jMaki menu: widget uses deprecated url property. Use href instead. ");
	}
        if (href) {
                window.location.href = href;
        }else if (menu[sp[0]].action ) {
            processActions(menu[sp[0]], menu[sp[0]].targetId);

        } 
    }

    function menuSelect(e){
        hideMenus();
        var src = (typeof window.event == 'undefined') ? e.target : window.event.srcElement;
        var sp = src.id.split("_");
        var url = menu[sp[0]].menu[sp[1]].url;
	if (typeof url == 'undefined') {
	    var href = menu[sp[0]].menu[sp[1]].href;
	} else {
	    var href = url;
	    jmaki.log("jMaki menu: widget uses deprecated url property. Use href instead. ");
	}
        if (href) {
                window.location.href = href;
        }else if (menu[sp[0]].menu[sp[1]].action ) {
            processActions(menu[sp[0]].menu[sp[1]], menu[sp[0]].menu[sp[1]].targetId);
        }
    }
    function hideMenus(){
        for (var _i=0; _i < menus.length;_i++) {
            menus[_i].style.display = "none";
            topMenus[_i].className = removeStyle(topMenus[_i].className, "jmk-menu2-bg-hover");
        }
    }

    function startHide() {
        hideTimer = setTimeout(hideMenus, 2000);
    }

    function stopHide() {
        if (hideTimer != null)  clearTimeout(hideTimer);
    }

    function menuList(menuStyle) {
        var menuList = document.createElement("li");
        menuList.className = menuStyle;
        menuList.onmouseout = startHide;
        menuList.onmousemove = stopHide;
	return menuList;

    }

    function menuElement(label, menuStyle, id) {
        var menuE = document.createElement("ul", menuStyle);
        menuE.className = menuStyle;
        menuE.id = id;
        // need to support menus that are just menubar
        var tmi = document.createTextNode(label);
        menuE.appendChild(tmi);
	    return menuE;
    }
    
    this.clear = function() {
        //TODO : make this use dom remove
       container.innerHTML = "";
       topMenus = [];
       navMenus = [];
       menus = [];
       menu = [];
       hideTimer = null;       
    };

    this.init = function(value) {
        menu = value.menu;
        var selectedIndex = 0;
        
        if (wargs.args && wargs.args.topic) {
            publish = wargs.args.topic;
        } else if (wargs.publish) {
	    publish = wargs.publishes;
        }

        if (typeof wargs.args != 'undefined' && 
            typeof wargs.args.selectedIndex != 'undefined') {
            selectedIndex = Number(wargs.args.selectedIndex);
        }
        
        // since we add right to left reverse the ordering
        menu = menu.reverse();

        var endSpacer = document.createElement("li");
        endSpacer.className = "jmk-menu2Top jmk-menu2EndSpacer";
        container.appendChild(endSpacer);

        for (var i=0; i < menu.length; i++) {
	    var me = menuElement(menu[i].label, "jmk-menu2Top", i + '');
            if ( typeof menu[i].menu != 'undefined') {
                me.onmouseover = showMenu;

            } else {
                me.onclick = labelSelect;
            }
                me.onmousemove = function(e){            
                    var src = (typeof window.event == 'undefined') ? e.target : window.event.srcElement;
                    src.className = removeStyle(src.className, "jmk-menu2Top");
                    src.className = addStyle(src.className, "jmk-menu2TopHover");
                }
                me.onmouseout = function(e){                 
                    var src = (typeof window.event == 'undefined') ? e.target : window.event.srcElement;
                     src.className = removeStyle(src.className, "jmk-menu2TopHover");
                   src.className = addStyle(src.className, "jmk-menu2Top");    
                }
            container.appendChild(me);
            topMenus.push(me);

	        var ml = menuList("jmk-menu2Container jmk-menu2Container-" + currentTheme);
            navMenus.push(ml);
            container.appendChild(ml);
            menus.push(ml);
            if (menu[i].id) menu[i].targetId = menu[i].id;
                else menu[i].targetId = wargs.uuid+"_menu_"+i;

            if (i < menu.length -1) {
                var spacerDiv = document.createElement("li");
                spacerDiv.appendChild(document.createTextNode("|"));
                spacerDiv.className = "jmk-menu2Separator";
                container.appendChild(spacerDiv);
            }
        }

        for(var oi=0; oi<menu.length; ++oi) {
            var mis = menu[oi].menu;
	    if ( typeof mis != 'undefined') { // not just a label
            for (var ii=0; ii < mis.length; ii++){
		var mi = menuElement(mis[ii].label, "jmk-menu2Item-" + currentTheme, oi + "_" + ii); 
                mi.onclick = menuSelect;
                mi.onmouseout = function(e){
                    var src = (typeof window.event == 'undefined') ? e.target : window.event.srcElement;
                    src.className = removeStyle(src.className, "jmk-menu2-bg-hover");
                };
                mi.onmousemove = function(e){
                    var src = (typeof window.event == 'undefined') ? e.target : window.event.srcElement;
                    src.className = addStyle(src.className, "jmk-menu2-bg-hover");
                }
                navMenus[oi].appendChild(mi);

          if (mis[ii].id) mis[ii].targetId = mis[ii].id;
          else mis[ii].targetId = wargs.uuid+"_menu_"+ii;
            }
	  }
        }
    };
    this.postLoad = function() {
            // pull in the arguments
        if (wargs.publish) publish = wargs.publish;
        if (wargs.value) {
            _widget.init(wargs.value);
        } else if (wargs.service) {
                jmaki.doAjax({url: wargs.service, callback: function(req) {
            if (req.readyState == 4) {
                if (req.status == 200) {
                  var data = eval('(' + req.responseText + ')');
   
                  _widget.init(data);
              }
            }
          }});
        }
        
    };
};