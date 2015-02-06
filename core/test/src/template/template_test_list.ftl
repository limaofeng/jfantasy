<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <table class="" id="view">
            <#list articles.pageImtes as article>
                <tr class="model">
                    <td>
                        ${article.title!' '}
                    </td>
                    <td>
                        ${article.summary!' '}
                    </td>
                    <td>
                        ${article.content!' '}
                    </td>
                </tr>
            </#list>
                <tr>
                    <td>
                        <a href="${articles.currentPage}">上一页</a>
                        <a href="${articles.currentPage}">下一页</a>
                    </td>
                </tr>
        </table>
    </body>
</html>