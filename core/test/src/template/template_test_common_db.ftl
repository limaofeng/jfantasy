<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <table class="" id="view">
            <#list title as art>
                <tr class="model">
                    <td>
                        title.title=${art.title!' '}
                    </td>
                    <td>
                        title.summary=${art.summary!' '}
                    </td>
                </tr>
            </#list>
            <tr>
                <td>
                    summary.title=${summary!' '}
                </td>
            </tr>
        </table>
    </body>
</html>