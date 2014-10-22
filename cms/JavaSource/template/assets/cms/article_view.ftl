<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<div class="pad10L pad10R">
    <div class="infobox warning-bg mrg10B">
        <p><i class="glyph-icon icon-exclamation mrg10R"></i>To view the available grid system options &amp; configurations you can visit the <a title="Fides Admin Grid System documentation" target="_blank" href="grid.html">Fides Admin Grid System documentation</a> page.</p>
    </div>
    <div class="example-box">
    <@s.form id="saveForm" namespace="/cms" action="article_save" method="post" cssClass="center-margin">
        <a href="javascript:;" class="btn small hover-black float-right back-page" title="" style="margin-top: -30px;margin-right: 30px">
            <i class="glyph-icon icon-reply"></i>
        </a>
        <div class="col-md-6 pad10T">
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="article.title"/>：
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.property value="art.title"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="article.summary"/>：
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.property value="art.summary"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="article.keywords"/>：
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.property value="art.keywords"/>
                    </div>
                </div>
            </div>

            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="article.releaseDate"/>：
                    </label>
                </div>
                <div class="form-input col-md-10 ">
                    <div class="append-left">
                        <@s.property value="art.releaseDate"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="article.author"/>：
                    </label>
                </div>
                <div class="form-input col-md-10 ">
                    <div class="append-left">
                        <@s.property value="art.author"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        <@s.text name="article.issue"/>：
                    </label>
                </div>
                <div class="form-checkbox-radio col-md-10 ">
                    <div class="append-left">
                        <@s.radio list=r"#{true:'是',false:'否'}"  name="issue" cssStyle="width:20px;" value="%{art.issue}" disabled="true"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-6 form-vertical">
            <div class="form-row">
                <div class="form-label col-md-2"></div>
                <div class="form-input col-md-10">
                    <@s.img src="%{art.logoImage.absolutePath}" ratio="160x160" cssClass="img-thumbnail"/>
                </div>
            </div>
        </div>
        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                </label>
            </div>
            <div class="form-input col-md-10">
                <@s.textarea cssClass="ckeditor" name="content.content" cssStyle="width:900px;height:360px;"  value="%{art.content.content}" disabled="true"/>
            </div>
        </div>
        <div class="form-row" style="text-align: center;">
            <div>
                <div style="float: left;padding-right: 50px;">
                    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title="保存"  >
                        <span class="glyph-icon icon-separator">
                             <i class="glyph-icon icon-save"></i>
                        </span>
                         <span class="button-content">
                             <@s.text name="operation.save"/>
                         </span>
                    </a>
                </div>
                <div style="float: left;">
                    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="返回" >
                        <span class="glyph-icon icon-separator">
                              <i class="glyph-icon icon-reply"></i>
                        </span>
                         <span class="button-content">
                             <@s.text name="operation.back"/>
                         </span>
                    </a>
                </div>
            </div>
        </div>
    </@s.form>
    </div>
</div>
