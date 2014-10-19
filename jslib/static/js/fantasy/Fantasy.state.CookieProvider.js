Fantasy.util.jClass(Fantasy.state.Provider, {

    jClass: 'Fantasy.state.CookieProvider',
    
    initialize: function($super, config){		
		
        $super();
        var property = new Fantasy.data.Property(this, ['path', 'expires', 'domain', 'secure']);
        property.setPropertyValue(config, {
            path: '/',
            expires: new Date(new Date().getTime() + (1000 * 60 * 60 * 24 * 7)),
            domain: null,
            secure: false
        });
        this.state = this.readCookies();	
    },
    
    set: function($super, name, value){		
        if (typeof value == "undefined" || value === null) {
            this.clear(name);
            return;
        }
        this.setCookie(name, value);
        $super();
    },
    
    clear: function($super, name){		
        this.clearCookie(name);
        $super();
    },
    
    readCookies: function(){
        var cookies = {};
        var c = document.cookie + ";";
        var re = /\s?(.*?)=(.*?);/g;
        var matches;
        while ((matches = re.exec(c)) != null) {
            var name = matches[1];
            var value = matches[2];
            if (name && name.substring(0, 3) == "ys-") {
                cookies[name.substr(3)] = this.decodeValue(value);
            }
        }
        return cookies;
    },
    
    setCookie: function(name, value){
        document.cookie = "ys-" + name + "=" + this.encodeValue(value) +
        ((this.getExpires() == null) ? "" : ("; expires=" + this.getExpires().toGMTString())) +
        ((this.getPath() == null) ? "" : ("; path=" + this.getPath())) +
        ((this.getDomain() == null) ? "" : ("; domain=" + this.getDomain())) +
        ((this.getSecure() == true) ? "; secure" : "");        
        this.state = this.readCookies();
    },
    
    clearCookie: function(name){
        document.cookie = "ys-" + name + "=null; expires=Thu, 01-Jan-70 00:00:01 GMT" +
        ((this.getPath() == null) ? "" : ("; path=" + this.getPath())) +
        ((this.getDomain() == null) ? "" : ("; domain=" + this.getDomain())) +
        ((this.getSecure() == true) ? "; secure" : "");
    }
});
