var request = (function() {

	return {
		getContextPath : function() {
			if(typeof(contextPath) == "undefined") {
				var pathName = document.location.pathname;
				var index = pathName.substr(1).indexOf("/");
				window.contextPath = pathName.substr(0, index + 1);
			}
			return window.contextPath;
		},
	
		getQueryString : function(){
			var url = document.location.href;
			return url.indexOf("#")==-1?url.substr(url.indexOf("?")+1):url.substr(url.indexOf("?")+1,url.indexOf("#"));
		}
	};
})();
var stopDefault = function(e, noBubbles){
	var evt = e || window.event;
	evt.preventDefault ? evt.preventDefault() : evt.returnValue = false;
	noBubbles && evt.stopPropagation ? evt.stopPropagation() : evt.cancelBubble = true;
	return false;
};

jQuery.extend({
	
	includePath : /[a-zA-Z]:$/.test(request.getContextPath())?'../':window.location.protocol+'//' + window.location.host + request.getContextPath() + '/',//WebContent/

    userAgent : navigator.userAgent.toLowerCase(),

	/**
	 * js 加载 Css 及 JavaScript
	 */
	include : function(file) {
		var files = typeof file == "string" ? [ file ] : file;
		for ( var i = 0; i < files.length; i++) {
			var name = files[i].replace(/^\s|\s$/g, "");
			var att = name.split('.');
			var ext = att[att.length - 1].toLowerCase();
			var isCSS = ext == "css";
			var tag = isCSS ? "link" : "script";
			var attr = isCSS ? " type='text/css' rel='stylesheet' ":" language='javascript' type='text/javascript' ";
			var link = (isCSS ? "href" : "src") + "='" + $.includePath + name +"'";
			if ($(tag + "[" + link + "]").length == 0)
				document.write("<" + tag + attr + link + " charset=\"utf-8\"" + (tag == "link" ? "/>" : ("></" + tag + ">")));
		}
	},

    /**
     * 解决jQuery判断浏览器错误的问题
     */
    browser: {
        version: (navigator.userAgent.match(/.(?:rv|it|ra|ie)[\/: ]([\d.]+)/) || [])[1],
        safari: !/chrome/.test(navigator.userAgent) && /webkit/.test(navigator.userAgent),
        opera: /opera/.test(navigator.userAgent),
        msie: /msie/.test(navigator.userAgent) && !/opera/.test(navigator.userAgent),
        mozilla: /mozilla/.test(navigator.userAgent) && !/(compatible|webkit)/.test(navigator.userAgent)
    }

});

jQuery.include('static/js/fantasy/Fantasy.js');
jQuery.include('static/js/fantasy/String.js');
jQuery.include('static/js/fantasy/Array.js');
jQuery.include('static/js/fantasy/Date.js');
jQuery.include('static/js/fantasy/Object.js');
/***********************************************************\
 *			Fantasy.util									*
 *[描    述] JS 框架	公共包									    *
 \***********************************************************/
jQuery.include('static/js/fantasy/Fantasy.util.jClass.js');
jQuery.include('static/js/fantasy/Fantasy.util.Object.js');
jQuery.include('static/js/fantasy/Fantasy.util.Class.js'); 
jQuery.include('static/js/fantasy/Fantasy.util.Observable.js'); 
jQuery.include('static/js/fantasy/Fantasy.data.Property.js'); 
jQuery.include('static/js/fantasy/Fantasy.util.Collection.js'); 
jQuery.include('static/js/fantasy/Fantasy.util.Map.js'); 
jQuery.include('static/js/fantasy/Fantasy.util.Proxy.js'); 
jQuery.include('static/js/fantasy/Fantasy.util.Invocation.js'); 
jQuery.include('static/js/fantasy/Fantasy.util.StringUtil.js'); 
jQuery.include('static/js/fantasy/Fantasy.util.ObjectUtil.js'); 
jQuery.include('static/js/fantasy/Fantasy.util.MathUtil.js'); 
jQuery.include('static/js/fantasy/Fantasy.util.DateUtil.js'); 
jQuery.include('static/js/fantasy/Fantasy.util.JSON.js');
jQuery.include('static/js/fantasy/Fantasy.util.Format.js');
jQuery.include('static/js/fantasy/Fantasy.util.Template.js');
jQuery.include('static/js/fantasy/Fantasy.util.Drag.js');
jQuery.include('static/js/fantasy/Fantasy.util.Resize.js');
jQuery.include('static/js/fantasy/Fantasy.util.Cache.js');
jQuery.include('static/js/fantasy/Fantasy.awt.ImgCropper.js');
jQuery.include('static/js/fantasy/Fantasy.state.Provider.js');
jQuery.include('static/js/fantasy/Fantasy.state.CookieProvider.js');
jQuery.include('static/js/fantasy/Fantasy.util.Cookie.js'); 
jQuery.include('static/js/fantasy/Fantasy.data.SortTypes.js');
jQuery.include('static/js/fantasy/Fantasy.data.Field.js');
jQuery.include('static/js/fantasy/Fantasy.Ajax.js');
jQuery.include('static/js/fantasy/Fantasy.awt.Field.js');
jQuery.include('static/js/fantasy/Fantasy.awt.Element.js');
jQuery.include('static/js/fantasy/Fantasy.awt.Template.js');
jQuery.include('static/js/fantasy/Fantasy.awt.SweetTemplate.js');
jQuery.include('static/js/fantasy/Fantasy.awt.View.js');
jQuery.include('static/js/fantasy/Fantasy.awt.Form.js');
jQuery.include('static/js/fantasy/Fantasy.awt.Grid.js');
jQuery.include('static/js/fantasy/Fantasy.awt.Pager.js');
jQuery.include('static/js/fantasy/Fantasy.awt.Box.js');
jQuery.include('static/js/fantasy/Fantasy.awt.ConfirmBox.js');
jQuery.include('static/js/fantasy/Fantasy.awt.SelectMenu.js');
//jQuery.include('static/js/fantasy/Fantasy.awt.AutoComplete.js');
jQuery.include(['static/js/fantasy/Fantasy.awt.Node.js','static/js/fantasy/Fantasy.awt.Tree.js']);
jQuery.include('static/js/fantasy/Fantasy.awt.Upload.js');
jQuery.include('static/js/fantasy/Fantasy.awt.Target.js');

//jQuery.include(['static/js/common/uniform/css/uniform.css']);
//jQuery.include(['static/js/jquery/jquery.uniform.js']);
//jQuery.include(['static/js/common/select2/css/select2.css','static/js/common/select2/css/select2-bootstrap.css']);
//jQuery.include(['static/js/common/select2/select2.js','static/js/common/select2/select2_locale_zh-CN.js']);
jQuery.include(['static/js/common/tagsinput/jquery.tagsinput.css','static/js/common/tagsinput/jquery.tagsinput.min.js']);

//jQuery.include(['static/bootstrap/css/bootstrap.min.css','static/bootstrap/js/bootstrap.min.js']);

jQuery.include(['static/js/common/datepicker/datepicker.css','static/js/common/datepicker/datepicker.js']);

jQuery.include(['static/js/holder.js']);

jQuery.include(['static/js/jquery/jquery.maskedinput.js']);//jquery.mask.js

jQuery.include('static/js/jquery/jquery.form.js');

//jQuery.include(['static/js/common/My97DatePicker/WdatePicker.js']);
//import artDialog 4.1.6
//jQuery.include(['static/js/common/artDialog/skins/default.css','static/js/common/artDialog/jquery.artDialog.source.js','static/js/common/artDialog/plugins/iframeTools.source.js']);
//js 模板插件
jQuery.include('static/js/jquery/sweet-jquery-plugin-v1.1.js');

//jQuery.include(['static/js/common/scroll/touchScroll.dev.js']);

jQuery.include(['static/js/common/zTree/css/zTreeStyle/zTreeStyle.css','static/js/common/zTree/jquery.ztree.core-3.1.js','static/js/common/zTree/jquery.ztree.excheck-3.1.js','static/js/common/zTree/jquery.ztree.exedit-3.1.js']);

//jQuery.include(['static/js/common/tip/jquery.tipTip.min.js','static/js/common/tip/tip.css']);

//jQuery.include(['static/js/common/syntaxhighlighter/styles/syntaxHighlighter.css','static/js/common/syntaxhighlighter/scripts/shCore.js']);

//jQuery.include(['static/js/common/highcharts/highcharts.src.js','static/js/common/highcharts/modules/exporting.src.js']);

//jQuery.include(['static/js/common/swfupload/default.css','static/js/common/swfupload/swfupload.js','static/js/common/swfupload/swfupload.queue.js','static/js/common/swfupload/fileprogress.js','static/js/common/swfupload/handlers.js']);

//jQuery.include(['static/js/dsb/SelectTree.js']);

//jQuery.include(['static/js/common/kindeditor/kindeditor.js']);

jQuery.include(['static/js/common/ckeditor/ckeditor.js','static/js/common/ckeditor/adapters/jquery.js']);

jQuery.include(['static/js/common/msgbox/msgbox.css','static/js/common/msgbox/msgbox.js']);

//jQuery.include(['static/js/common/formvalidator/formValidator-4.1.3.js','static/js/common/formvalidator/formValidatorRegex.js']);

//简化select级联操作
jQuery.include('static/js/jquery/jquery.select3.js');

jQuery.include('static/js/jquery/jquery.target.js');

//jQuery.include(['static/js/common/formvalidator/themes/Default/js/theme.js']);//,'static/js/common/formvalidator/themes/SolidBox/style/style.css'

//jQuery.include(['static/js/file/swfobject.js','static/js/file/flexpaper_flash.js']);
//jQuery.include(['static/js/jquery/jquery.qtip.min.js','static/css/jquery.qtip.min.css']);

//jQuery.include('static/js/fantasy/style/pageStyle.css');

//jQuery.include(['static/js/common/thickbox/thickbox-compressed.js','static/js/common/thickbox/thickbox.css']);
//jQuery.include(['static/js/common/applelike/styles.css','static/js/common/applelike/script.js']);

//jQuery.include(['static/js/common/modernizr/modernizr.custom.js','static/js/common/modernizr/waypoints.js']);

//jQuery.include(['static/js/common/mobiscroll/css/mobiscroll-2.0.1.full.min.css']);
//jQuery.include(['static/js/common/mobiscroll/dev/js/mobiscroll.core.js']);
//jQuery.include(['static/js/common/mobiscroll/dev/js/mobiscroll.datetime.js']);
//jQuery.include(['static/js/common/mobiscroll/dev/js/mobiscroll.select.js']);
////jQuery.include(['static/js/common/mobiscroll/dev/js/mobiscroll.zepto.js']);
//jQuery.include(['static/js/common/mobiscroll/dev/js/mobiscroll.ios.js']);
//jQuery.include(['static/js/common/mobiscroll/dev/js/mobiscroll.jqm.js']);
//jQuery.include(['static/js/common/mobiscroll/dev/js/mobiscroll.android.js']);
//jQuery.include(['static/js/common/mobiscroll/dev/js/mobiscroll.android-ics.js']);
///*'static/js/common/autocomplete/jquery.autocomplete.css',*/
//jQuery.include(['static/js/common/autocomplete/jquery.autocomplete.js']);

jQuery.include(['static/js/jquery/jquery.util.js','static/js/jquery/jquery.selection.box.js','assets/js/initialize.js']);

jQuery.include(['static/js/common/upload/jquery.upload.js','static/js/common/upload/css/upload.css']);

jQuery.include(['assets/js/jquery.advsearch.js','assets/js/jquery.dataGrid.js']);

//外部插件
//jQuery.include('static/js/jquery/jquery.ui.custom.js');
//jQuery.include('static/js/jquery/jquery.validate.js');
//jQuery.include('static/js/jquery/jquery.validate.messages_cn.js');
//jQuery.include('static/js/jquery/jquery.wizard.js');

//
jQuery.include('static/js/common/hashme/hashme.js');
//jQuery.include('static/js/common/hashme/filedrag.js');
jQuery.include('static/js/common/hashme/md5.js');

//jQuery.include(['static/js/common/emailInput/css/emailInput.css','static/js/common/emailInput/jquery.email.js']);

//jQuery.include(['static/js/common/sly/jquery.sly.js','static/js/jquery/jquery.easing-1.3.min.js','static/js/common/sly/css/style.css']);

//flexigrid 插件
jQuery.include(['static/js/common/flexigrid/css/flexigrid.css','static/js/common/flexigrid/js/flexigrid.js']);
//jQuery.include(['static/js/common/flexigrid/css/flexigrid.pack.css','static/js/common/flexigrid/js/flexigrid.pack.js']);

jQuery.include(['static/js/common/codemirror/codemirror.css','static/js/common/codemirror/codemirror.js']);

//jQuery.include('static/js/config.js?'+Math.floor(Math.random() * 100));
//jQuery.include('static/js/area.js?'+Math.floor(Math.random() * 100));