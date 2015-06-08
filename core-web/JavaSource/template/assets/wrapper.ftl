<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@s.set id="isAjax" value="@com.fantasy.framework.util.web.WebUtil@isAjax()"/>
<@override name="pageContentWrapper">
<div id="page-title">
    <h3>
    <@block name="pageTitle"></@block>
    </h3>
    <div id="breadcrumb-right" style="display:none;">
        <div id="sidebar-search">
            <input type="text" placeholder="Search..." class="autocomplete-input input tooltip-button" data-placement="bottom" title="Type &apos;jav&apos; to see the available tags..." id="" name="" />
            <i class="glyph-icon icon-search"></i>
        </div>
        <script type="text/javascript">
            $(function () {
                function a(a) {
                    return a.split(/,\s*/)
                }

                function b(b) {
                    return a(b).pop()
                }

                var c = ["ActionScript", "AppleScript", "Asp", "BASIC", "C", "C++", "Clojure", "COBOL", "ColdFusion", "Erlang", "Fortran", "Groovy", "Haskell", "Java", "JavaScript", "Lisp", "Perl", "PHP", "Python", "Ruby", "Scala", "Scheme"];
                $("#sidebar-search input").bind("keydown", function (a) {
                    a.keyCode === $.ui.keyCode.TAB && $(this).data("ui-autocomplete").menu.active && a.preventDefault()
                }).autocomplete({minLength: 0, source: function (a, d) {
                    d($.ui.autocomplete.filter(c, b(a.term)))
                }, focus: function () {
                    return!1
                }, select: function (b, c) {
                    var d = a(this.value);
                    return d.pop(), d.push(c.item.value), d.push(""), this.value = d.join(", "), !1
                }, messages: {noResults: "", results: function () {
                }}});
            });
        </script>
        <div class="float-right">
            <a href="javascript:;" class="btn medium bg-white tooltip-button black-modal-60 mrg5R" data-placement="bottom" title="关于本软件">
                <span class="button-content">
                    <i class="glyph-icon icon-question"></i>
                </span>
            </a>
            <#--
            <div class="dropdown">
                <a href="javascript:;" class="btn medium bg-white" title="Example dropdown" data-toggle="dropdown">
                        <span class="button-content">
                            <i class="glyph-icon icon-cog float-left"></i>
                            <i class="glyph-icon icon-caret-down float-right"></i>
                        </span>
                </a>
                <div class="dropdown-menu pad0A float-right">
                    <div class="medium-box">
                        <div class="bg-gray text-transform-upr font-size-12 font-bold font-gray-dark pad10A">Form example</div>
                        <div class="pad10A">
                            <p class="font-gray-dark pad0B">This <span class="label bg-blue-alt">dropdown box</span> uses the Twitter Bootstrap dropdown plugin.</p>
                            <div class="divider mrg10T mrg10B"></div>

                            <form id="demo-form" action="" class="col-md-12" method="" />
                            <div class="form-row">
                                <div class="form-label col-md-4">
                                    <label for="">
                                        Name:
                                        <span class="required">*</span>
                                    </label>
                                </div>
                                <div class="form-input col-md-8">
                                    <input type="text" id="email" name="email" data-type="email" data-trigger="change" data-required="true" class="parsley-validated" />
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="form-label col-md-4">
                                    <label for="">
                                        Email:
                                        <span class="required">*</span>
                                    </label>
                                </div>
                                <div class="form-input col-md-8">
                                    <input type="text" id="email" name="email" data-type="email" data-trigger="change" data-required="true" class="parsley-validated" />
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="form-label col-md-4">
                                    <label for="">
                                        Website:
                                    </label>
                                </div>
                                <div class="form-input col-md-8">
                                    <input type="text" id="website" name="website" data-trigger="change" data-type="url" class="parsley-validated" />
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="form-label col-md-4">
                                    <label for="" class="label-description">
                                        Message:
                                        <span>20 chars min, 200 max</span>
                                    </label>
                                </div>
                                <div class="form-input col-md-8">
                                    <textarea id="message" name="message" data-trigger="keyup" data-rangelength="[20,200]" class="parsley-validated"></textarea>
                                </div>
                            </div>
                            <div class="divider"></div>
                            <div class="form-row">
                                <input type="hidden" name="superhidden" id="superhidden" />
                                <div class="form-input col-md-8 col-md-offset-4">
                                    <a href="javascript:;" class="btn medium primary-bg radius-all-4" id="demo-form-valid" onclick="javascript:$(&apos;#demo-form&apos;).parsley( &apos;validate&apos; );" title="Validate!">
                                                                <span class="button-content">
                                                                    Validate the form above
                                                                </span>
                                    </a>
                                </div>
                            </div>

                            </form>

                        </div>

                        <div class="bg-black font-size-12 font-orange pad10A mrg5L mrg5R">Custom header example</div>
                        <div class="pad15A">
                            <p class="font-green text-center font-size-14 pad0B">Fides Admin comes with powerful helpers that you can use to create virtually any style you want. Read the documentation about helper classes to find out more!</p>
                        </div>
                    </div>
                </div>
            </div>
            -->
        </div>
    </div>
</div>
<!-- #page-title -->
<div id="page-content" style="padding-bottom: 0px;margin-bottom: -15px;">
    <div class="row">
        <@block name="pageContent"></@block>
    </div>
</div>
<!-- #page-content -->
</@override>
<#if isAjax == true >
    <@extends name="empty.ftl"/>
<#else>
    <@extends name="base.ftl"/>
</#if>