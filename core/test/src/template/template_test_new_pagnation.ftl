<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <h2 style="color: red;">生成静态页面--分页页面</h2>
        <#list articles as article>
            <div>
                article.title=${article.title!' '}
            </div>
            <div>
                article.summary=${article.summary!' '}
            </div>
        </#list>
        <a href="${pager.currentPage-1}">上一页</a>
        <a href="${pager.currentPage+1}">下一页</a>
        <br/>
        <div>
            title=${title!' '}<br/>
        </div>
        <div>
            summary=${summary!' '}<br/>
        </div>
    </body>
</html>