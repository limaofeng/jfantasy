if(window.jQuery) (function($){
 $.extend({
  xml2json: function(xml, extended) {
   if(!xml) return {};
   function parseXML(node, simple){
    if(!node) return null;
    var txt = '', obj = null, att = null;
    var nt = node.nodeType, nn = jsVar(node.localName || node.nodeName);
    var nv = node.text || node.nodeValue || '';
    if(node.childNodes){
     if(node.childNodes.length>0){
      $.each(node.childNodes, function(n,cn){
       var cnt = cn.nodeType, cnn = jsVar(cn.localName || cn.nodeName);
       var cnv = cn.text || cn.nodeValue || '';
       if(cnt == 8){
        return;
       }
       else if(cnt == 3 || cnt == 4 || !cnn){
        if(cnv.match(/^\s+$/)){
        	return;
        };
        txt += cnv.replace(/^\s+/,'').replace(/\s+$/,'');
       }
       else{
        obj = obj || {};
        if(obj[cnn]){
         if(!obj[cnn].length) obj[cnn] = myArr(obj[cnn]);
         obj[cnn][ obj[cnn].length ] = parseXML(cn, true/* simple */);
         obj[cnn].length = obj[cnn].length;
        }
        else{
         obj[cnn] = parseXML(cn);
        };
       };
      });
     };
    };
    if(node.attributes){
     if(node.attributes.length>0){
      att = {}; obj = obj || {};
      $.each(node.attributes, function(a,at){
       var atn = jsVar(at.name), atv = at.value;
       att[atn] = atv;
       if(obj[atn]){
        if(!obj[atn].length) obj[atn] = myArr(obj[atn]);
        obj[atn][ obj[atn].length ] = atv;
        obj[atn].length = obj[atn].length;
       }
       else{
        obj[atn] = atv;
       };
      });
     };
    };
    if(obj){
     obj = $.extend( (txt!='' ? new String(txt) : {}),/* {text:txt},*/ obj || {}/*, att || {}*/);
     txt = (obj.text) ? (typeof(obj.text)=='object' ? obj.text : [obj.text || '']).concat([txt]) : txt;
     if(txt) obj.text = txt;
     txt = '';
    };
    var out = obj || txt;
    if(extended){
     if(txt) out = {};
     txt = out.text || txt || '';
     if(txt) out.text = txt;
     if(!simple) out = myArr(out);
    };
    return out;
   };
   var jsVar = function(s){ return String(s || '').replace(/-/g,"_"); };
   var isNum = function(s){ return (typeof s == "number") || String((s && typeof s == "string") ? s : '').test(/^((-)?([0-9]*)((\.{0,1})([0-9]+))?$)/); };
   var myArr = function(o){
    if(!o.length) o = [ o ]; o.length=o.length;
    return o;
   };
   if(typeof xml=='string') xml = $.text2xml(xml);
   if(!xml.nodeType) return;
   if(xml.nodeType == 3 || xml.nodeType == 4) return xml.nodeValue;
   var root = (xml.nodeType == 9) ? xml.documentElement : xml;
   var out = parseXML(root, true /* simple */);
   xml = null; root = null;
   return out;
  },
  text2xml: function(str) {
   var out;
   try{
    var xml = ($.browser.msie)?new ActiveXObject("Microsoft.XMLDOM"):new DOMParser();
    xml.async = false;
   }catch(e){ throw new Error("XML Parser could not be instantiated") };
   try{
    if($.browser.msie) out = (xml.loadXML(str))?xml:false;
    else out = xml.parseFromString(str, "text/xml");
   }catch(e){ throw new Error("Error parsing XML string") };
   return out;
  }
 }); 
})(jQuery);