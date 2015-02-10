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
            <tr class="model">
                <td>
                    ${title!' '}
                </td>
                <#--<td>-->
                    <#--${summary!' '}-->
                <#--</td>-->
                <#--<td>-->
                    <#--${content!' '}-->
                <#--</td>-->
            </tr>
        </table>
    </body>
</html>