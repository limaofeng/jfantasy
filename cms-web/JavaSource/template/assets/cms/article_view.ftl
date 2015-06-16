<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<div class="pad10L pad10R">
    <div class="example-box">
        <a href="javascript:;" class="btn small hover-black float-right back-page" title="" style="margin-top: -0px;margin-right: 30px">
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
                        <@s.textfield name="title" id="title" value="%{art.title}" disabled="true"/>
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
                        <@s.textfield name="summary" id="summary" value="%{art.summary}" disabled="true"/>
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
                        <@s.textfield name="keywords" value="%{art.keywords}" disabled="true"/>
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
                        <input type="text" class="datepicker" data-date-format="yyyy-mm-dd" value="<@s.property value="art.releaseDate"/>" name="releaseDate" disabled="true"/>
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
                        <@s.textfield name="author" value="%{art.author}" disabled="true"/>
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
                <div class="form-input col-md-10" id="imageUploader">
                <@s.img src="%{art.articleImage.absolutePath}" ratio="160x160" cssClass="img-thumbnail"/>
                </div>
            </div>
        </div>
        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                </label>
            </div>
            <div class="form-input col-md-10">
                <@s.textarea cssClass="ckeditor" name="content" cssStyle="width:900px;height:360px;"  value="%{art.content.text}" disabled="true"/>
            </div>
        </div>
    </div>
</div>
