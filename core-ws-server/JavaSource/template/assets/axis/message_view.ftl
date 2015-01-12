<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript" src="${request.contextPath}/static/js/common/codemirror/mode/xml/xml.js"></script>
<script type="text/javascript">
    $(function(){
        window.editor = CodeMirror.fromTextArea(document.getElementById('code'), {
            indentWithTabs: true,
            smartIndent: true,
            lineNumbers: true,
            matchBrackets : true,
            autofocus: true
        });

        window.editor = CodeMirror.fromTextArea(document.getElementById('code1'), {
            indentWithTabs: true,
            smartIndent: true,
            lineNumbers: true,
            matchBrackets : true,
            autofocus: true
        });
    });
</script>
<div class="example-box" style="padding-left:10px;padding-right:10px;">
    <div class="example-code">
        <div class="content-box box-toggle">
            <h3 class="content-box-header primary-bg">
                <span class="float-left">Axis请求信息详情</span>
                <a href="javascript:;" class="btn small hover-black float-right back-page" title="返回" style="margin-top: 10px;margin-right: 30px">
                    <i class="glyph-icon icon-reply"></i>
                </a>
            </h3>
            <div class="content-box-wrapper">
                <div class="tabs">
                    <ul>
                        <li>
                            <a title="基本信息" href="#tabs-1">
                                基本信息
                            </a>
                        </li>
                        <li>
                            <a title="请求内容" href="#tabs-2">
                                请求内容
                            </a>
                        </li>
                        <li>
                            <a title="返回结果" href="#tabs-3">
                                返回结果
                            </a>
                        </li>
                    </ul>
                    <div id="tabs-1">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-row">
                                    <div class="form-label col-md-3">
                                        <label for="">
                                            调用接口的远程地址：
                                        </label>
                                    </div>
                                    <div class="form-input col-md-9">
                                        <div class="append-left">
                                            <@s.textfield name="remoteAddr" value="%{message.remoteAddr}" readonly="true"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-row">
                                    <div class="form-label col-md-3">
                                        <label for="">
                                            返回类型：
                                        </label>
                                    </div>
                                    <div class="form-input col-md-9">
                                        <div class="append-left">
                                             <@s.textfield name="result" value="%{message.result.value}" readonly="true"/>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <div class="form-row">
                                    <div class="form-label col-md-3">
                                        <label for="">
                                            调用类型：
                                        </label>
                                    </div>
                                    <div class="form-input col-md-9">
                                        <div class="append-left">
                                        <@s.textfield name="Type"  value="%{message.Type.value}" readonly="true"/>

                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>

                    <div id="tabs-2">
                        <textarea id="code"  name="in" style="width:100%;height:300px;"  value="${message.in}"></textarea>
                    </div>

                    <div id="tabs-3">
                        <textarea id="code1"  name="out" style="width:100%;height:300px;"  value="${message.out}"></textarea>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>