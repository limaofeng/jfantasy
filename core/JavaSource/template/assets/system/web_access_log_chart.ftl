<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
网站访问记录
<small>
    ...
</small>
</@override>
<@override name="head">

<script type="text/javascript" src="${request.contextPath}/assets/js/minified/core/raphael.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/assets/js/minified/widgets/charts-morris.min.js"></script>
<#--
<script type="text/javascript" src="${request.contextPath}/assets/js/minified/demo/charts-morris-demo.min.js"></script>-->
<script type="text/javascript">
    $(function(){
        var browser = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(browser)" escapeHtml="false"/>
        var num = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(num)" escapeHtml="false"/>
        var ipnum = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(ipnum)" escapeHtml="false"/>
        browser.each(function(){
            this['label'] = this.key;
        });

        num.each(function(){
            this['x'] = this.key;
            this['y'] = this.value;
        });
        ipnum.each(function(){
            this['x'] = this.key;
            this['y'] = this.value;
        });
        Morris.Donut({
            element: "donut",
            backgroundColor: "#fff",
            labelColor: "#ccc",
            colors: ["#4fb2ff", "#929292", "#67C69D", "#ff9393"],
            data: browser,
            formatter: function(a, b) {
                return "使用次数："+a;
            }
        });

        window.m = Morris.Line({
            element: "decimal-data",
            data: num,
            xkey: "x",
            ykeys: ["y"],
            labels: ["数量"],
            parseTime: !1,
            hoverCallback: function(a, b, c) {
                var d = b.data[a];
                return  d.x+"\n"+"访问数量："+ d.y;
            },
            xLabelMargin: 10,
            integerYLabels: !0
        });

        window.m = Morris.Line({
            element: "ipnum",
            data: ipnum,
            xkey: "x",
            ykeys: ["y"],
            labels: ["数量"],
            parseTime: !1,
            hoverCallback: function(a, b, c) {
                var d = b.data[a];
                return  d.x+"\n"+"IP数量："+ d.y;
            },
            xLabelMargin: 10,
            integerYLabels: !0
        });


    });

</script>

</@override>
<@override name="pageContent">
<div class="example-box">
<div class="example-code">
    <div class="row">
        <div class="col-md-4">

            <h5 class="text-transform-upr text-center mrg0T mrg15B font-gray font-size-14">浏览器访问数统计</h5>

            <div class="content-box">
                <div id="donut"></div>
            </div>

        </div>
        <div class="col-md-4">

            <h5 class="text-transform-upr text-center mrg0T mrg15B font-gray font-size-14">每日用户访问数统计</h5>

            <div class="content-box">
                <div id="decimal-data"></div>
            </div>

        </div>
        <div class="col-md-4">

            <h5 class="text-transform-upr text-center mrg0T mrg15B font-gray font-size-14">每日独立IP访问数统计</h5>

            <div class="content-box">
                <div id="ipnum"></div>
            </div>

        </div>
    </div>

</div>

</div>
</@override>
<@extends name="../wrapper.ftl"/>