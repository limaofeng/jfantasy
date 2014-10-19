/**
 * SyntaxHighlighter
 * http://alexgorbatchev.com/
 *
 * SyntaxHighlighter is donationware. If you are using it, please donate.
 * http://alexgorbatchev.com/wiki/SyntaxHighlighter:Donate
 *
 * @version
 * 2.0.320 (May 03 2009)
 * 
 * @copyright
 * Copyright (C) 2004-2009 Alex Gorbatchev.
 *
 * @license
 * This file is part of SyntaxHighlighter.
 * 
 * SyntaxHighlighter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SyntaxHighlighter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SyntaxHighlighter.  If not, see <http://www.gnu.org/copyleft/lesser.html>.
 */
if (!window.SyntaxHighlighter) {
    var SyntaxHighlighter = new Syntax();
}

	function Syntax(){
        var sh = {
            defaults: {
                "class-name": "",
                "first-line": 1,
                "highlight": null,
                "smart-tabs": true,
                "tab-size": 4,
                "ruler": false,
                "gutter": true,
                "toolbar": true,
                "collapse": false,
                "auto-links": true,
                "light": false,
                "wrap-lines": true
            },
            config: {
                clipboardSwf: null,
                toolbarItemWidth: 16,
                toolbarItemHeight: 16,
                bloggerMode: false,
                stripBrs: false,
                tagName: "pre",
                strings: {
                    expandSource: "expand source",
                    viewSource: "查看源码",
                    copyToClipboard: "复制到剪切板",
                    copyToClipboardConfirmation: "该代码现在已复制到剪贴板",
                    print: "打印",
                    help: "?",
                    alert: "",
                    noBrush: "Can't find brush for: ",
                    brushNotHtmlScript: "Brush wasn't configured for html-script option: ",
                    aboutDialog: "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title>About SyntaxHighlighter</title></head><body style=\"font-family:Geneva,Arial,Helvetica,sans-serif;background-color:#fff;color:#000;font-size:1em;text-align:center;\"><div style=\"text-align:center;margin-top:3em;\"><div style=\"font-size:xx-large;\">SyntaxHighlighter</div><div style=\"font-size:.75em;margin-bottom:4em;\"><div>version 2.0.320 (May 03 2009)</div><div><a href=\"http://alexgorbatchev.com\" target=\"_blank\" style=\"color:#0099FF;text-decoration:none;\">http://alexgorbatchev.com</a></div></div><div>JavaScript code syntax highlighter.</div><div>Copyright 2004-2009 Alex Gorbatchev.</div></div></body></html>"
                },
                debug: false
            },
            vars: {
                discoveredBrushes: null,
                spaceWidth: null,
                printFrame: null,
                highlighters: {}
            },
            brushes: {},
            regexLib: {
                multiLineCComments: /\/\*[\s\S]*?\*\//gm,
                singleLineCComments: /\/\/.*$/gm,
                singleLinePerlComments: /#.*$/gm,
                doubleQuotedString: /"(?:\.|(\\\")|[^\""\n])*"/g,
                singleQuotedString: /'(?:\.|(\\\')|[^\''\n])*'/g,
                multiLineDoubleQuotedString: /"(?:\.|(\\\")|[^\""])*"/g,
                multiLineSingleQuotedString: /'(?:\.|(\\\')|[^\''])*'/g,
                url: /\w+:\/\/[\w-.\/?%&=]*/g,
                phpScriptTags: {
                    left: /(&lt;|<)\?=?/g,
                    right: /\?(&gt;|>)/g
                },
                aspScriptTags: {
                    left: /(&lt;|<)%=?/g,
                    right: /%(&gt;|>)/g
                },
                scriptScriptTags: {
                    left: /(&lt;|<)\s*script.*?(&gt;|>)/gi,
                    right: /(&lt;|<)\/\s*script\s*(&gt;|>)/gi
                }
            },
            toolbar: {
                create: function(_2) {
                    var _3 = document.createElement("DIV"),
                    _4 = sh.toolbar.items;
                    _3.className = "toolbar";
                    for (var _5 in _4) {
                        var _6 = _4[_5],
                        _7 = new _6(_2),
                        _8 = _7.create();
                        _2.toolbarCommands[_5] = _7;
                        if (_8 == null) {
                            continue
                        }
                        if (typeof(_8) == "string") {
                            _8 = sh.toolbar.createButton(_8, _2.id, _5)
                        }
                        _8.className += "item " + _5;
                        _3.appendChild(_8)
                    }
                    return _3
                },
                createButton: function(_9, _a, _b) {
                    var a = document.createElement("a"),
                    _d = a.style,
                    _e = sh.config,
                    _f = _e.toolbarItemWidth,
                    _10 = _e.toolbarItemHeight;
                    a.href = "#" + _b;
                    a.title = _9;
                    a.highlighterId = _a;
                    a.commandName = _b;
                    a.innerHTML = _9;
                    if (isNaN(_f) == false) {
                        _d.width = _f + "px"
                    }
                    if (isNaN(_10) == false) {
                        _d.height = _10 + "px"
                    }
                    a.onclick = function(e) {
                        try {
                            sh.toolbar.executeCommand(this, e || window.event, this.highlighterId, this.commandName)
                        } catch(e) {
                            sh.utils.alert(e.message)
                        }
                        return false
                    };
                    return a
                },
                executeCommand: function(_12, _13, _14, _15, _16) {
                    var _17 = sh.vars.highlighters[_14],
                    _18;
                    if (_17 == null || (_18 = _17.toolbarCommands[_15]) == null) {
                        return null
                    }
                    return _18.execute(_12, _13, _16)
                },
                items: {
                    expandSource: function(_19) {
                        this.create = function() {
                            if (_19.getParam("collapse") != true) {
                                return
                            }
                            return sh.config.strings.expandSource
                        };
                        this.execute = function(_1a, _1b, _1c) {
                            var div = _19.div;
                            _1a.parentNode.removeChild(_1a);
                            div.className = div.className.replace("collapsed", "")
                        }
                    },
                    viewSource: function(_1e) {
                        this.create = function() {
                            return sh.config.strings.viewSource
                        };
                        this.execute = function(_1f, _20, _21) {
                            var _22 = sh.utils.fixInputString(_1e.originalCode).replace(/</g, "&lt;"),
                            wnd = sh.utils.popup("", "_blank", 750, 400, "location=0, resizable=1, menubar=0, scrollbars=1");
                            _22 = sh.utils.unindent(_22);
                            wnd.document.write("<pre>" + _22 + "</pre>");
                            wnd.document.close()
                        }
                    },
                    copyToClipboard: function(_24) {
                        var _25,
                        _26,
                        _27 = _24.id;
                        this.create = function() {
                            var _28 = sh.config;
                            if (_28.clipboardSwf == null) {
                                return null
                            }
                            function params(_29) {
                                var _2a = "";
                                for (var _2b in _29) {
                                    _2a += "<param name='" + _2b + "' value='" + _29[_2b] + "'/>"
                                }
                                return _2a
                            };
                            function attributes(_2c) {
                                var _2d = "";
                                for (var _2e in _2c) {
                                    _2d += " " + _2e + "='" + _2c[_2e] + "'"
                                }
                                return _2d
                            };
                            var _2f = {
                                width: _28.toolbarItemWidth,
                                height: _28.toolbarItemHeight,
                                id: _27 + "_clipboard",
                                type: "application/x-shockwave-flash",
                                title: sh.config.strings.copyToClipboard
                            },
                            _30 = {
                                allowScriptAccess: "always",
                                wmode: "transparent",
                                flashVars: "highlighterId=" + _27,
                                menu: "false"
                            },
                            swf = _28.clipboardSwf,
                            _32;
                            if (/msie/i.test(navigator.userAgent)) {
                                _32 = "<object" + attributes({
                                    classid: "clsid:d27cdb6e-ae6d-11cf-96b8-444553540000",
                                    codebase: "http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,0,0"
                                }) + attributes(_2f) + ">" + params(_30) + params({
                                    movie: swf
                                }) + "</object>"
                            } else {
                                _32 = "<embed" + attributes(_2f) + attributes(_30) + attributes({
                                    src: swf
                                }) + "/>"
                            }
                            _25 = document.createElement("div");
                            _25.innerHTML = _32;
                            return _25
                        };
                        this.execute = function(_33, _34, _35) {
                            var _36 = _35.command;
                            switch (_36) {
                            case "get":
                                var _37 = sh.utils.unindent(sh.utils.fixInputString(_24.originalCode).replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&amp;/g, "&"));
                                if (window.clipboardData) {
                                    window.clipboardData.setData("text", _37)
                                } else {
                                    return sh.utils.unindent(_37)
                                }
                            case "ok":
                                sh.utils.alert(sh.config.strings.copyToClipboardConfirmation);
                                break;
                            case "error":
                                sh.utils.alert(_35.message);
                                break
                            }
                        }
                    },
                    printSource: function(_38) {
                        this.create = function() {
                            return sh.config.strings.print
                        };
                        this.execute = function(_39, _3a, _3b) {
                            var _3c = document.createElement("IFRAME"),
                            doc = null;
                            if (sh.vars.printFrame != null) {
                                document.body.removeChild(sh.vars.printFrame)
                            }
                            sh.vars.printFrame = _3c;
                            _3c.style.cssText = "position:absolute;width:0px;height:0px;left:-500px;top:-500px;";
                            document.body.appendChild(_3c);
                            doc = _3c.contentWindow.document;
                            copyStyles(doc, window.document);
                            doc.write("<div class=\"" + _38.div.className.replace("collapsed", "") + " printing\">" + _38.div.innerHTML + "</div>");
                            doc.close();
                            _3c.contentWindow.focus();
                            _3c.contentWindow.print();
                            function copyStyles(_3e, _3f) {
                                var _40 = _3f.getElementsByTagName("link");
                                for (var i = 0; i < _40.length; i++) {
                                    if (_40[i].rel.toLowerCase() == "stylesheet" && /shCore\.css$/.test(_40[i].href)) {
                                        _3e.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + _40[i].href + "\"></link>")
                                    }
                                }
                            }
                        }
                    },
                    about: function(_42) {
                        this.create = function() {
                            return sh.config.strings.help
                        };
                        this.execute = function(_43, _44) {
                            var wnd = sh.utils.popup("", "_blank", 500, 250, "scrollbars=0"),
                            doc = wnd.document;
                            doc.write(sh.config.strings.aboutDialog);
                            doc.close();
                            wnd.focus()
                        }
                    }
                }
            },
            utils: {
                guid: function(_47) {
                    return _47 + Math.round(Math.random() * 1000000).toString()
                },
                merge: function(_48, _49) {
                    var _4a = {},
                    _4b;
                    for (_4b in _48) {
                        _4a[_4b] = _48[_4b]
                    }
                    for (_4b in _49) {
                        _4a[_4b] = _49[_4b]
                    }
                    return _4a
                },
                toBoolean: function(_4c) {
                    switch (_4c) {
                    case "true":
                        return true;
                    case "false":
                        return false
                    }
                    return _4c
                },
                popup: function(url, _4e, _4f, _50, _51) {
                    var x = (screen.width - _4f) / 2,
                    y = (screen.height - _50) / 2;
                    _51 += ", left=" + x + ", top=" + y + ", width=" + _4f + ", height=" + _50;
                    _51 = _51.replace(/^,/, "");
                    var win = window.open(url, _4e, _51);
                    win.focus();
                    return win
                },
                addEvent: function(obj, _56, _57) {
                    if (obj.attachEvent) {
                        obj["e" + _56 + _57] = _57;
                        obj[_56 + _57] = function() {
                            obj["e" + _56 + _57](window.event)
                        };
                        obj.attachEvent("on" + _56, obj[_56 + _57])
                    } else {
                        obj.addEventListener(_56, _57, false)
                    }
                },
                alert: function(str) {
                    alert(sh.config.strings.alert + str)
                },
                findBrush: function(_59, _5a) {
                    var _5b = sh.vars.discoveredBrushes,
                    _5c = null;
                    if (_5b == null) {
                        _5b = {};
                        for (var _5d in sh.brushes) {
                            var _5e = sh.brushes[_5d].aliases;
                            if (_5e == null) {
                                continue
                            }
                            for (var i = 0; i < _5e.length; i++) {
                                _5b[_5e[i]] = _5d
                            }
                        }
                        sh.vars.discoveredBrushes = _5b
                    }
                    _5c = sh.brushes[_5b[_59]];
                    if (_5c == null && _5a != false) {
                        sh.utils.alert(sh.config.strings.noBrush + _59)
                    }
                    return _5c
                },
                eachLine: function(str, _61) {
                    var _62 = str.split("\n");
                    for (var i = 0; i < _62.length; i++) {
                        _62[i] = _61(_62[i])
                    }
                    return _62.join("\n")
                },
                createRuler: function() {
                    var div = document.createElement("div"),
                    _65 = document.createElement("div"),
                    _66 = 10,
                    i = 1;
                    while (i <= 150) {
                        if (i % _66 === 0) {
                            div.innerHTML += i;
                            i += (i + "").length
                        } else {
                            div.innerHTML += "&middot;";
                            i++
                        }
                    }
                    _65.className = "ruler line";
                    _65.appendChild(div);
                    return _65
                },
                trimFirstAndLastLines: function(str) {
                    return str.replace(/^[ ]*[\n]+|[\n]*[ ]*$/g, "")
                },
                parseParams: function(str) {
                    var _6a,
                    _6b = {},
                    _6c = new XRegExp("^\\[(?<values>(.*?))\\]$"),
                    _6d = new XRegExp("(?<name>[\\w-]+)" + "\\s*:\\s*" + "(?<value>" + "[\\w-%#]+|" + "\\[.*?\\]|" + "\".*?\"|" + "'.*?'" + ")\\s*;?", "g");
                    while ((_6a = _6d.exec(str)) != null) {
                        var _6e = _6a.value.replace(/^['"]|['"]$/g, "");
                        if (_6e != null && _6c.test(_6e)) {
                            var m = _6c.exec(_6e);
                            _6e = m.values.length > 0 ? m.values.split(/\s*,\s*/) : []
                        }
                        _6b[_6a.name] = _6e
                    }
                    return _6b
                },
                decorate: function(str, css) {
                    if (str == null || str.length == 0 || str == "\n") {
                        return str
                    }
                    str = str.replace(/</g, "&lt;");
                    str = str.replace(/ {2,}/g, 
                    function(m) {
                        var _73 = "";
                        for (var i = 0; i < m.length - 1; i++) {
                            _73 += "&nbsp;"
                        }
                        return _73 + " "
                    });
                    if (css != null) {
                        str = sh.utils.eachLine(str, 
                        function(_75) {
                            if (_75.length == 0) {
                                return ""
                            }
                            var _76 = "";
                            _75 = _75.replace(/^(&nbsp;| )+/, 
                            function(s) {
                                _76 = s;
                                return ""
                            });
                            if (_75.length == 0) {
                                return _76
                            }
                            return _76 + "<code class=\"" + css + "\">" + _75 + "</code>"
                        })
                    }
                    return str
                },
                padNumber: function(_78, _79) {
                    var _7a = _78.toString();
                    while (_7a.length < _79) {
                        _7a = "0" + _7a
                    }
                    return _7a
                },
                measureSpace: function() {
                    var _7b = document.createElement("div"),
                    _7c,
                    _7d = 0,
                    _7e = document.body,
                    id = sh.utils.guid("measureSpace"),
                    _80 = "<div class=\"",
                    _81 = "</div>",
                    _82 = "</span>";
                    _7b.innerHTML = _80 + "syntaxhighlighter\">" + _80 + "lines\">" + _80 + "line\">" + _80 + "content" + "\"><span class=\"block\"><span id=\"" + id + "\">&nbsp;" + _82 + _82 + _81 + _81 + _81 + _81;
                    _7e.appendChild(_7b);
                    _7c = document.getElementById(id);
                    if (/opera/i.test(navigator.userAgent)) {
                        var _83 = window.getComputedStyle(_7c, null);
                        _7d = parseInt(_83.getPropertyValue("width"))
                    } else {
                        _7d = _7c.offsetWidth
                    }
                    _7e.removeChild(_7b);
                    return _7d
                },
                processTabs: function(_84, _85) {
                    var tab = "";
                    for (var i = 0; i < _85; i++) {
                        tab += " "
                    }
                    return _84.replace(/\t/g, tab)
                },
                processSmartTabs: function(_88, _89) {
                    var _8a = _88.split("\n"),
                    tab = "\t",
                    _8c = "";
                    for (var i = 0; i < 50; i++) {
                        _8c += "                    "
                    }
                    function insertSpaces(_8e, pos, _90) {
                        return _8e.substr(0, pos) + _8c.substr(0, _90) + _8e.substr(pos + 1, _8e.length)
                    };
                    _88 = sh.utils.eachLine(_88, 
                    function(_91) {
                        if (_91.indexOf(tab) == -1) {
                            return _91
                        }
                        var pos = 0;
                        while ((pos = _91.indexOf(tab)) != -1) {
                            var _93 = _89 - pos % _89;
                            _91 = insertSpaces(_91, pos, _93)
                        }
                        return _91
                    });
                    return _88
                },
                fixInputString: function(str) {
                    var br = /<br\s*\/?>|&lt;br\s*\/?&gt;/gi;
                    if (sh.config.bloggerMode == true) {
                        str = str.replace(br, "\n")
                    }
                    if (sh.config.stripBrs == true) {
                        str = str.replace(br, "")
                    }
                    return str
                },
                trim: function(str) {
                    return str.replace(/\s*$/g, "").replace(/^\s*/, "")
                },
                unindent: function(str) {
                    var _98 = sh.utils.fixInputString(str).split("\n"),
                    _99 = new Array(),
                    _9a = /^\s*/,
                    min = 1000;
                    for (var i = 0; i < _98.length && min > 0; i++) {
                        var _9d = _98[i];
                        if (sh.utils.trim(_9d).length == 0) {
                            continue
                        }
                        var _9e = _9a.exec(_9d);
                        if (_9e == null) {
                            return str
                        }
                        min = Math.min(_9e[0].length, min)
                    }
                    if (min > 0) {
                        for (var i = 0; i < _98.length; i++) {
                            _98[i] = _98[i].substr(min)
                        }
                    }
                    return _98.join("\n")
                },
                matchesSortCallback: function(m1, m2) {
                    if (m1.index < m2.index) {
                        return - 1
                    } else {
                        if (m1.index > m2.index) {
                            return 1
                        } else {
                            if (m1.length < m2.length) {
                                return - 1
                            } else {
                                if (m1.length > m2.length) {
                                    return 1
                                }
                            }
                        }
                    }
                    return 0
                },
                getMatches: function(_a1, _a2) {
                    function defaultAdd(_a3, _a4) {
                        return [new sh.Match(_a3[0], _a3.index, _a4.css)]
                    };
                    var _a5 = 0,
                    _a6 = null,
                    _a7 = [],
                    _a8 = _a2.func ? _a2.func: defaultAdd;
                    while ((_a6 = _a2.regex.exec(_a1)) != null) {
                        _a7 = _a7.concat(_a8(_a6, _a2))
                    }
                    return _a7
                },
                processUrls: function(_a9) {
                    return _a9.replace(sh.regexLib.url, 
                    function(m) {
                        return "<a href=\"" + m + "\">" + m + "</a>"
                    })
                }
            },
            highlight: function(_ab, _ac) {
                function toArray(_ad) {
                    var _ae = [];
                    for (var i = 0; i < _ad.length; i++) {
                        _ae.push(_ad[i])
                    }
                    return _ae
                };
                var _b0 = _ac ? [_ac] : toArray(document.getElementsByTagName(sh.config.tagName)),
                _b1 = "innerHTML",
                _b2 = null;
                if (_b0.length === 0) {
                    return
                }
                for (var i = 0; i < _b0.length; i++) {
                    var _b4 = _b0[i],
                    _b5 = sh.utils.parseParams(_b4.className),
                    _b6;
                    _b5 = sh.utils.merge(_ab, _b5);
                    _b6 = _b5["brush"];
                    if (_b6 == null) {
                        continue
                    }
                    if (_b5["html-script"] == "true") {
                        _b2 = new sh.HtmlScript(_b6)
                    } else {
                        var _b7 = sh.utils.findBrush(_b6);
                        if (_b7) {
                            _b2 = new _b7()
                        } else {
                            continue
                        }
                    }
                    _b2.highlight(_b4[_b1], _b5);
                    var _b8 = _b2.div;
                    if (sh.config.debug) {
                        _b8 = document.createElement("textarea");
                        _b8.value = _b2.div.innerHTML;
                        _b8.style.width = "70em";
                        _b8.style.height = "30em"
                    }
                    _b4.parentNode.replaceChild(_b8, _b4)
                }
            },
            all: function(_b9) {
                sh.utils.addEvent(window, "load", 
                function() {
                    sh.highlight(_b9)
                })
            }
        };
        sh.Match = function(_ba, _bb, css) {
            this.value = _ba;
            this.index = _bb;
            this.length = _ba.length;
            this.css = css
        };
        sh.Match.prototype.toString = function() {
            return this.value
        };
        sh.HtmlScript = function(_bd) {
            var _be = sh.utils.findBrush(_bd),
            _bf = new sh.brushes.Xml(),
            _c0 = null;
            if (_be == null) {
                return
            }
            _be = new _be();
            this.xmlBrush = _bf;
            if (_be.htmlScript == null) {
                sh.utils.alert(sh.config.strings.brushNotHtmlScript + _bd);
                return
            }
            _bf.regexList.push({
                regex: _be.htmlScript.code,
                func: process
            });
            function offsetMatches(_c1, _c2) {
                for (var j = 0; j < _c1.length; j++) {
                    _c1[j].index += _c2
                }
            };
            function process(_c4, _c5) {
                var _c6 = _c4.code,
                _c7 = [],
                _c8 = _be.regexList,
                _c9 = _c4.index + _c4.left.length,
                _ca = _be.htmlScript,
                _cb;
                for (var i = 0; i < _c8.length; i++) {
                    _cb = sh.utils.getMatches(_c6, _c8[i]);
                    offsetMatches(_cb, _c9);
                    _c7 = _c7.concat(_cb)
                }
                if (_ca.left != null && _c4.left != null) {
                    _cb = sh.utils.getMatches(_c4.left, _ca.left);
                    offsetMatches(_cb, _c4.index);
                    _c7 = _c7.concat(_cb)
                }
                if (_ca.right != null && _c4.right != null) {
                    _cb = sh.utils.getMatches(_c4.right, _ca.right);
                    offsetMatches(_cb, _c4.index + _c4[0].lastIndexOf(_c4.right));
                    _c7 = _c7.concat(_cb)
                }
                return _c7
            }
        };
        sh.HtmlScript.prototype.highlight = function(_cd, _ce) {
            this.xmlBrush.highlight(_cd, _ce);
            this.div = this.xmlBrush.div
        };
        sh.Highlighter = function() {};
        sh.Highlighter.prototype = {
            getParam: function(_cf, _d0) {
                var _d1 = this.params[_cf];
                return sh.utils.toBoolean(_d1 == null ? _d0: _d1)
            },
            create: function(_d2) {
                return document.createElement(_d2)
            },
            findMatches: function(_d3, _d4) {
                var _d5 = [];
                if (_d3 != null) {
                    for (var i = 0; i < _d3.length; i++) {
                        _d5 = _d5.concat(sh.utils.getMatches(_d4, _d3[i]))
                    }
                }
                _d5 = _d5.sort(sh.utils.matchesSortCallback);
                return _d5
            },
            removeNestedMatches: function() {
                var _d7 = this.matches;
                for (var i = 0; i < _d7.length; i++) {
                    if (_d7[i] === null) {
                        continue
                    }
                    var _d9 = _d7[i],
                    _da = _d9.index + _d9.length;
                    for (var j = i + 1; j < _d7.length && _d7[i] !== null; j++) {
                        var _dc = _d7[j];
                        if (_dc === null) {
                            continue
                        } else {
                            if (_dc.index > _da) {
                                break
                            } else {
                                if (_dc.index == _d9.index && _dc.length > _d9.length) {
                                    this.matches[i] = null
                                } else {
                                    if (_dc.index >= _d9.index && _dc.index < _da) {
                                        this.matches[j] = null
                                    }
                                }
                            }
                        }
                    }
                }
            },
            createDisplayLines: function(_dd) {
                var _de = _dd.split(/\n/g),
                _df = parseInt(this.getParam("first-line")),
                _e0 = (_df + _de.length).toString().length,
                _e1 = this.getParam("highlight", []);
                _dd = "";
                for (var i = 0; i < _de.length; i++) {
                    var _e3 = _de[i],
                    _e4 = /^(&nbsp;|\s)+/.exec(_e3),
                    _e5 = "line alt" + (i % 2 == 0 ? 1: 2),
                    _e6 = sh.utils.padNumber(_df + i, _e0),
                    _e7 = _e1.indexOf((_df + i).toString()) != -1,
                    _e8 = null;
                    if (_e4 != null) {
                        _e8 = _e4[0].toString();
                        _e3 = _e3.substr(_e8.length);
                        _e8 = _e8.replace(/&nbsp;/g, " ");
                        _e4 = sh.vars.spaceWidth * _e8.length
                    } else {
                        _e4 = 0
                    }
                    _e3 = sh.utils.trim(_e3);
                    if (_e3.length == 0) {
                        _e3 = "&nbsp;"
                    }
                    if (_e7) {
                        _e5 += " highlighted"
                    }
                    _dd += "<div class=\"" + _e5 + "\">" + "<code class=\"number\">" + _e6 + ".</code>" + "<span class=\"content\">" + (_e8 != null ? "<code class=\"spaces\">" + _e8.replace(/\s/g, "&nbsp;") + "</code>": "") + "<span class=\"block\" style=\"margin-left: " + _e4 + "px !important;\">" + _e3 + "</span>" + "</span>" + "</div>"
                }
                return _dd
            },
            processMatches: function(_e9, _ea) {
                var pos = 0,
                _ec = "",
                _ed = sh.utils.decorate;
                for (var i = 0; i < _ea.length; i++) {
                    var _ef = _ea[i];
                    if (_ef === null || _ef.length === 0) {
                        continue
                    }
                    _ec += _ed(_e9.substr(pos, _ef.index - pos), "plain") + _ed(_ef.value, _ef.css);
                    pos = _ef.index + _ef.length
                }
                _ec += _ed(_e9.substr(pos), "plain");
                return _ec
            },
            highlight: function(_f0, _f1) {
                var _f2 = sh.config,
                _f3 = sh.vars,
                div,
                _f5,
                _f6,
                _f7 = "important";
                this.params = {};
                this.div = null;
                this.lines = null;
                this.code = null;
                this.bar = null;
                this.toolbarCommands = {};
                this.id = sh.utils.guid("highlighter_");
                _f3.highlighters[this.id] = this;
                if (_f0 === null) {
                    _f0 = ""
                }
                if (_f3.spaceWidth === null) {
                    _f3.spaceWidth = sh.utils.measureSpace()
                }
                this.params = sh.utils.merge(sh.defaults, _f1 || {});
                if (this.getParam("light") == true) {
                    this.params.toolbar = this.params.gutter = false
                }
                this.div = div = this.create("DIV");
                this.lines = this.create("DIV");
                this.lines.className = "lines";
                className = "syntaxhighlighter";
                div.id = this.id;
                if (this.getParam("collapse")) {
                    className += " collapsed"
                }
                if (this.getParam("gutter") == false) {
                    className += " nogutter"
                }
                if (this.getParam("wrap-lines") == false) {
                    this.lines.className += " no-wrap"
                }
                className += " " + this.getParam("class-name");
                div.className = className;
                this.originalCode = _f0;
                this.code = sh.utils.trimFirstAndLastLines(_f0).replace(/\r/g, " ");
                _f6 = this.getParam("tab-size");
                this.code = this.getParam("smart-tabs") == true ? sh.utils.processSmartTabs(this.code, _f6) : sh.utils.processTabs(this.code, _f6);
                this.code = sh.utils.unindent(this.code);
                if (this.getParam("toolbar")) {
                    this.bar = this.create("DIV");
                    this.bar.className = "bar";
                    this.bar.appendChild(sh.toolbar.create(this));
                    div.appendChild(this.bar);
                    var bar = this.bar;
                    function hide() {
                        bar.className = bar.className.replace("show", "")
                    };
                    div.onmouseover = function() {
                        hide();
                        bar.className += " show"
                    };
                    div.onmouseout = function() {
                        hide()
                    }
                }
                if (this.getParam("ruler")) {
                    div.appendChild(sh.utils.createRuler())
                }
                div.appendChild(this.lines);
                this.matches = this.findMatches(this.regexList, this.code);
                this.removeNestedMatches();
                _f0 = this.processMatches(this.code, this.matches);
                _f0 = this.createDisplayLines(sh.utils.trim(_f0));
                if (this.getParam("auto-links")) {
                    _f0 = sh.utils.processUrls(_f0)
                }
                this.lines.innerHTML = _f0
            },
            getKeywords: function(str) {
                str = str.replace(/^\s+|\s+$/g, "").replace(/\s+/g, "\\b|\\b");
                return "\\b" + str + "\\b"
            },
            forHtmlScript: function(_fa) {
                this.htmlScript = {
                    left: {
                        regex: _fa.left,
                        css: "script"
                    },
                    right: {
                        regex: _fa.right,
                        css: "script"
                    },
                    code: new XRegExp("(?<left>" + _fa.left.source + ")" + "(?<code>.*?)" + "(?<right>" + _fa.right.source + ")", "sgi")
                }
            }
        };
        return sh
    }

if (!Array.indexOf) {
    Array.prototype.indexOf = function(_fb, _fc) {
        _fc = Math.max(_fc || 0, 0);
        for (var i = _fc; i < this.length; i++) {
            if (this[i] == _fb) {
                return i
            }
        }
        return - 1
    }
}
if (!window.XRegExp) { (function() {
        var _fe = {
            exec: RegExp.prototype.exec,
            match: String.prototype.match,
            replace: String.prototype.replace,
            split: String.prototype.split
        },
        lib = {
            part: /(?:[^\\([#\s.]+|\\(?!k<[\w$]+>|[pP]{[^}]+})[\S\s]?|\((?=\?(?!#|<[\w$]+>)))+|(\()(?:\?(?:(#)[^)]*\)|<([$\w]+)>))?|\\(?:k<([\w$]+)>|[pP]{([^}]+)})|(\[\^?)|([\S\s])/g,
            replaceVar: /(?:[^$]+|\$(?![1-9$&`']|{[$\w]+}))+|\$(?:([1-9]\d*|[$&`'])|{([$\w]+)})/g,
            extended: /^(?:\s+|#.*)+/,
            quantifier: /^(?:[?*+]|{\d+(?:,\d*)?})/,
            classLeft: /&&\[\^?/g,
            classRight: /]/g
        },
        _100 = function(_101, item, from) {
            for (var i = from || 0; i < _101.length; i++) {
                if (_101[i] === item) {
                    return i
                }
            }
            return - 1
        },
        _105 = /()??/.exec("")[1] !== undefined,
        _106 = {};
        XRegExp = function(_107, _108) {
            if (_107 instanceof RegExp) {
                if (_108 !== undefined) {
                    throw TypeError("can't supply flags when constructing one RegExp from another")
                }
                return _107.addFlags()
            }
            var _108 = _108 || "",
            _109 = _108.indexOf("s") > -1,
            _10a = _108.indexOf("x") > -1,
            _10b = false,
            _10c = [],
            _10d = [],
            part = lib.part,
            _10f,
            cc,
            len,
            _112,
            _113;
            part.lastIndex = 0;
            while (_10f = _fe.exec.call(part, _107)) {
                if (_10f[2]) {
                    if (!lib.quantifier.test(_107.slice(part.lastIndex))) {
                        _10d.push("(?:)")
                    }
                } else {
                    if (_10f[1]) {
                        _10c.push(_10f[3] || null);
                        if (_10f[3]) {
                            _10b = true
                        }
                        _10d.push("(")
                    } else {
                        if (_10f[4]) {
                            _112 = _100(_10c, _10f[4]);
                            _10d.push(_112 > -1 ? "\\" + (_112 + 1) + (isNaN(_107.charAt(part.lastIndex)) ? "": "(?:)") : _10f[0])
                        } else {
                            if (_10f[5]) {
                                _10d.push(_106.unicode ? _106.unicode.get(_10f[5], _10f[0].charAt(1) === "P") : _10f[0])
                            } else {
                                if (_10f[6]) {
                                    if (_107.charAt(part.lastIndex) === "]") {
                                        _10d.push(_10f[6] === "[" ? "(?!)": "[\\S\\s]");
                                        part.lastIndex++
                                    } else {
                                        cc = XRegExp.matchRecursive("&&" + _107.slice(_10f.index), lib.classLeft, lib.classRight, "", {
                                            escapeChar: "\\"
                                        })[0];
                                        _10d.push(_10f[6] + cc + "]");
                                        part.lastIndex += cc.length + 1
                                    }
                                } else {
                                    if (_10f[7]) {
                                        if (_109 && _10f[7] === ".") {
                                            _10d.push("[\\S\\s]")
                                        } else {
                                            if (_10a && lib.extended.test(_10f[7])) {
                                                len = _fe.exec.call(lib.extended, _107.slice(part.lastIndex - 1))[0].length;
                                                if (!lib.quantifier.test(_107.slice(part.lastIndex - 1 + len))) {
                                                    _10d.push("(?:)")
                                                }
                                                part.lastIndex += len - 1
                                            } else {
                                                _10d.push(_10f[7])
                                            }
                                        }
                                    } else {
                                        _10d.push(_10f[0])
                                    }
                                }
                            }
                        }
                    }
                }
            }
            _113 = RegExp(_10d.join(""), _fe.replace.call(_108, /[sx]+/g, ""));
            _113._x = {
                source: _107,
                captureNames: _10b ? _10c: null
            };
            return _113
        };
        XRegExp.addPlugin = function(name, o) {
            _106[name] = o
        };
        RegExp.prototype.exec = function(str) {
            var _117 = _fe.exec.call(this, str),
            name,
            i,
            r2;
            if (_117) {
                if (_105 && _117.length > 1) {
                    r2 = new RegExp("^" + this.source + "$(?!\\s)", this.getNativeFlags());
                    _fe.replace.call(_117[0], r2, 
                    function() {
                        for (i = 1; i < arguments.length - 2; i++) {
                            if (arguments[i] === undefined) {
                                _117[i] = undefined
                            }
                        }
                    })
                }
                if (this._x && this._x.captureNames) {
                    for (i = 1; i < _117.length; i++) {
                        name = this._x.captureNames[i - 1];
                        if (name) {
                            _117[name] = _117[i]
                        }
                    }
                }
                if (this.global && this.lastIndex > (_117.index + _117[0].length)) {
                    this.lastIndex--
                }
            }
            return _117
        }
    })()
}
RegExp.prototype.getNativeFlags = function() {
    return (this.global ? "g": "") + (this.ignoreCase ? "i": "") + (this.multiline ? "m": "") + (this.extended ? "x": "") + (this.sticky ? "y": "")
};
RegExp.prototype.addFlags = function(_11b) {
    var _11c = new XRegExp(this.source, (_11b || "") + this.getNativeFlags());
    if (this._x) {
        _11c._x = {
            source: this._x.source,
            captureNames: this._x.captureNames ? this._x.captureNames.slice(0) : null
        }
    }
    return _11c
};
RegExp.prototype.call = function(_11d, str) {
    return this.exec(str)
};
RegExp.prototype.apply = function(_11f, args) {
    return this.exec(args[0])
};
XRegExp.cache = function(_121, _122) {
    var key = "/" + _121 + "/" + (_122 || "");
    return XRegExp.cache[key] || (XRegExp.cache[key] = new XRegExp(_121, _122))
};
XRegExp.escape = function(str) {
    return str.replace(/[-[\]{}()*+?.\\^$|,#\s]/g, "\\$&")
};
XRegExp.matchRecursive = function(str, left, _127, _128, _129) {
    var _129 = _129 || {},
    _12a = _129.escapeChar,
    vN = _129.valueNames,
    _128 = _128 || "",
    _12c = _128.indexOf("g") > -1,
    _12d = _128.indexOf("i") > -1,
    _12e = _128.indexOf("m") > -1,
    _12f = _128.indexOf("y") > -1,
    _128 = _128.replace(/y/g, ""),
    left = left instanceof RegExp ? (left.global ? left: left.addFlags("g")) : new XRegExp(left, "g" + _128),
    _127 = _127 instanceof RegExp ? (_127.global ? _127: _127.addFlags("g")) : new XRegExp(_127, "g" + _128),
    _130 = [],
    _131 = 0,
    _132 = 0,
    _133 = 0,
    _134 = 0,
    _135,
    _136,
    _137,
    _138,
    _139,
    esc;
    if (_12a) {
        if (_12a.length > 1) {
            throw SyntaxError("can't supply more than one escape character")
        }
        if (_12e) {
            throw TypeError("can't supply escape character when using the multiline flag")
        }
        _139 = XRegExp.escape(_12a);
        esc = new RegExp("^(?:" + _139 + "[\\S\\s]|(?:(?!" + left.source + "|" + _127.source + ")[^" + _139 + "])+)+", _12d ? "i": "")
    }
    while (true) {
        left.lastIndex = _127.lastIndex = _133 + (_12a ? (esc.exec(str.slice(_133)) || [""])[0].length: 0);
        _137 = left.exec(str);
        _138 = _127.exec(str);
        if (_137 && _138) {
            if (_137.index <= _138.index) {
                _138 = null
            } else {
                _137 = null
            }
        }
        if (_137 || _138) {
            _132 = (_137 || _138).index;
            _133 = (_137 ? left: _127).lastIndex
        } else {
            if (!_131) {
                break
            }
        }
        if (_12f && !_131 && _132 > _134) {
            break
        }
        if (_137) {
            if (!_131++) {
                _135 = _132;
                _136 = _133
            }
        } else {
            if (_138 && _131) {
                if (!--_131) {
                    if (vN) {
                        if (vN[0] && _135 > _134) {
                            _130.push([vN[0], str.slice(_134, _135), _134, _135])
                        }
                        if (vN[1]) {
                            _130.push([vN[1], str.slice(_135, _136), _135, _136])
                        }
                        if (vN[2]) {
                            _130.push([vN[2], str.slice(_136, _132), _136, _132])
                        }
                        if (vN[3]) {
                            _130.push([vN[3], str.slice(_132, _133), _132, _133])
                        }
                    } else {
                        _130.push(str.slice(_136, _132))
                    }
                    _134 = _133;
                    if (!_12c) {
                        break
                    }
                }
            } else {
                left.lastIndex = _127.lastIndex = 0;
                throw Error("subject data contains unbalanced delimiters")
            }
        }
        if (_132 === _133) {
            _133++
        }
    }
    if (_12c && !_12f && vN && vN[0] && str.length > _134) {
        _130.push([vN[0], str.slice(_134), _134, str.length])
    }
    left.lastIndex = _127.lastIndex = 0;
    return _130
};