<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <table class="" id="view">
            <#list articles as article>
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
            <a href="${prePager}">上一页</a>
            <a href="${nextPager}">下一页</a>
        </table>

    </body>
</html>