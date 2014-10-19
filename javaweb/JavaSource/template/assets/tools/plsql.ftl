<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript" src="${request.contextPath}/static/js/common/codemirror/mode/sql/sql.js"></script>
<script type="text/javascript">
    $(function(){

        var mime = 'text/x-mariadb';
        // get mime type
        if (window.location.href.indexOf('mime=') > -1) {
            mime = window.location.href.substr(window.location.href.indexOf('mime=') + 5);
        }
        window.editor = CodeMirror.fromTextArea(document.getElementById('code'), {
            mode: mime,
            indentWithTabs: true,
            smartIndent: true,
            lineNumbers: true,
            matchBrackets : true,
            autofocus: true
        });

        $('.flexme').flexigrid();

    });
</script>
<textarea id="code" cols="120" rows="50">
    -- SQL Mode for CodeMirror
    SELECT SQL_NO_CACHE DISTINCT
    @var1 AS `val1`, @'val2', @global.'sql_mode',
    1.1 AS `float_val`, .14 AS `another_float`, 0.09e3 AS `int_with_esp`,
    0xFA5 AS `hex`, x'fa5' AS `hex2`, 0b101 AS `bin`, b'101' AS `bin2`,
    DATE '1994-01-01' AS `sql_date`, { T "1994-01-01" } AS `odbc_date`,
    'my string', _utf8'your string', N'her string',
    TRUE, FALSE, UNKNOWN
    FROM DUAL
    -- space needed after '--'
    # 1 line comment
    /* multiline
    comment! */
    LIMIT 1 OFFSET 0;

</textarea>
<p><strong>MIME types defined:</strong>
    <code><a href="?mime=text/x-sql">text/x-sql</a></code>,
    <code><a href="?mime=text/x-mysql">text/x-mysql</a></code>,
    <code><a href="?mime=text/x-mariadb">text/x-mariadb</a></code>,
    <code><a href="?mime=text/x-cassandra">text/x-cassandra</a></code>,
    <code><a href="?mime=text/x-plsql">text/x-plsql</a></code>,
    <code><a href="?mime=text/x-mssql">text/x-mssql</a></code>,
    <code><a href="?mime=text/x-hive">text/x-hive</a></code>.
<table class="flexme">
    <thead>
    <tr>
        <th width="100">Col 1</th>
        <th width="100">Col 2</th>
        <th width="100">Col 3 is a long header name</th>
        <th width="300">Col 4</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>This is data 1 with overflowing content</td>
        <td>This is data 2</td>
        <td>This is data 3</td>
        <td>This is data 4</td>
    </tr>
    <tr>
        <td>This is data 1</td>
        <td>This is data 2</td>
        <td>This is data 3</td>
        <td>This is data 4</td>
    </tr>
    <tr>
        <td>This is data 1</td>
        <td>This is data 2</td>
        <td>This is data 3</td>
        <td>This is data 4</td>
    </tr>
    <tr>
        <td>This is data 1</td>
        <td>This is data 2</td>
        <td>This is data 3</td>
        <td>This is data 4</td>
    </tr>
    <tr>
        <td>This is data 1</td>
        <td>This is data 2</td>
        <td>This is data 3</td>
        <td>This is data 4</td>
    </tr>
    <tr>
        <td>This is data 1</td>
        <td>This is data 2</td>
        <td>This is data 3</td>
        <td>This is data 4</td>
    </tr>
    <tr>
        <td>This is data 1</td>
        <td>This is data 2</td>
        <td>This is data 3</td>
        <td>This is data 4</td>
    </tr>
    <tr>
        <td>This is data 1</td>
        <td>This is data 2</td>
        <td>This is data 3</td>
        <td>This is data 4</td>
    </tr>
    <tr>
        <td>This is data 1</td>
        <td>This is data 2</td>
        <td>This is data 3</td>
        <td>This is data 4</td>
    </tr>
    <tr>
        <td>This is data 1</td>
        <td>This is data 2</td>
        <td>This is data 3</td>
        <td>This is data 4</td>
    </tr>
    <tr>
        <td>This is data 1</td>
        <td>This is data 2</td>
        <td>This is data 3</td>
        <td>This is data 4</td>
    </tr>
    <tr>
        <td>This is data 1</td>
        <td>This is data 2</td>
        <td>This is data 3</td>
        <td>This is data 4</td>
    </tr>
    </tbody>
</table>