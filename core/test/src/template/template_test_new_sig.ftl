<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <h2 style="color: red;">生成静态页面--单页面</h2>
        <#list articles as article>
            <div>
                article.title=${article.title!' '}
            </div>
            <div>
                article.summary=${article.summary!' '}
            </div>
        </#list>
        <br/>
        <div>
            article.title=${article.title!' '}<br/>
            article.summary=${article.summary!' '}
        </div>
        <div>
            title=${title!' '}<br/>
        </div>
        <div>
            summary=${summary!' '}<br/>
        </div>
    </body>
</html>