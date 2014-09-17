<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><@s.property value="@com.fantasy.system.util.SettingUtil@getValue('title')"/></title>
    <link rel="stylesheet" href="${request.contextPath}/static/css/admin-base-1.0.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/admin-commonui.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/admin-home.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/uniform.css" />
    <link rel="stylesheet" href="${request.contextPath}/static/css/select2.css" />
    <script type="text/javascript">window.contextPath = '${request.contextPath}';</script>
    <script type="text/javascript" src="${request.contextPath}/static/js/jquery.js"></script>
    <script type="text/javascript" src="${request.contextPath}/static/js/common.js"></script>
    <script type="text/javascript" src="${request.contextPath}/static/js/common/formvalidator/formValidator-4.1.3.js"></script>
    <script type="text/javascript" src="${request.contextPath}/static/js/common/formvalidator/formValidatorRegex.js"></script>
    <script type="text/javascript" src="${request.contextPath}/static/js/common/formvalidator/themes/Default/js/theme.js"></script>
    <link rel="stylesheet" href="${request.contextPath}/static/js/common/formvalidator/themes/SolidBox/style/style.css" />
    <link rel="stylesheet" href="${request.contextPath}/static/css/pageStyle.css" />
    <script type="text/javascript">

        function add_loader(){
            if(top != window){
                top.add_loader();
            }
        }
        function remove_loader(){
            if(top != window){
                top.remove_loader();
            }
        }
        $(function(){
            Fantasy.apply(Fantasy.awt.Pager, {}, {
                getArray : function(totalPage, currentPage, pageNumber) {
                    var arraylist = new Array();
                    // 循环产生页码 产生数量最多为11个 前1个 4 《当前页》4 后一个
                    for ( var i = 1, size = 1; i <= totalPage && size <= (pageNumber * 2 + 3); i++) {
                        if (i == 1) {// 第一页 必须
                            arraylist.push(i);
                            size++;
                        } else if ((currentPage > totalPage - (pageNumber + 1) && i + (pageNumber * 2 + 2) > totalPage)// 当前页在前5位
                                || (i > currentPage - (pageNumber + 1) && i < currentPage)) {
                            arraylist.push(i);
                            size++;
                        } else if (i == currentPage) {// 添加当前页
                            arraylist.push(i);
                            size++;
                        } else if (i > currentPage && i < currentPage + (pageNumber + 1) && size < (pageNumber * 2 + 2)) {// 当前页在后5位
                            arraylist.push(i);
                            size++;
                        } else if (size > (pageNumber + 1) && size <= (pageNumber * 2 + 2)) {
                            arraylist.push(i);
                            size++;
                        } else if (i == totalPage) {// 最后一个必须
                            arraylist.push(i);
                            size++;
                        }
                    }
                    if (totalPage > 1 && !(arraylist[1] === 2)) {// 添加空位
                        arraylist.splice(1, 0, "...");
                    }
                    if (totalPage > 1 && !(arraylist[arraylist.length - 2] === (totalPage - 1))) {// 添加空位
                        arraylist.splice(arraylist.length - 1, 0, "...");
                    }
                    return arraylist;
                },
                template : $.Sweet('<a href="?page=<[=currentPage==1?currentPage:currentPage-1]>">«上一页</a>'+
                        '<['+
							   'var array = Fantasy.awt.Pager.getArray(totalPage, currentPage, 5);'+
							   'foreach( array as item){'+
							   'if(\'...\'==item){]>'+
                        '<[=item]>'+
                        '<[}else if(currentPage == item){]>'+
                        '<span class="current"><[=item]></span>'+
                        '<[}else{]>'+
                        '<a href="?page=<[=item]>"><[=item]></a>'+
                        '<[}}]>'+
                        '<a href="?page=<[=currentPage==totalPage?totalPage:currentPage+1]>">下一页 »</a>')
            });
        });
    </script>
    <@block name="head"></@block>
</head>
<body class="iframe-body">
<div id="container" style="width:100%;height:100%;display: block;">
<@block name="container"></@block>
</div>
</body>
</html>