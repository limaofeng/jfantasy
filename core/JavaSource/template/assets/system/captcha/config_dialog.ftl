<div class="hide" id="config-dialog" title="短信配置">
    <div class="pad10A template" name="default">
        <div class="col-md-12">
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        配置ID：
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="id" cssStyle="width:80%;" maxlength="32" cssClass="view-field"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        服务器名称：
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="serverName" cssStyle="width:80%;" maxlength="50" cssClass="view-field"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        随机字符串：
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="randomWord" cssStyle="width:80%;" maxlength="50" cssClass="view-field"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        验证码长度：
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="wordLength" cssStyle="width:80%;" maxlength="2" cssClass="view-field"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        失效时间：
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="expires" cssStyle="width:80%;" maxlength="8"  cssClass="view-field"/>毫秒
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        重复生成时间：
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="active" cssStyle="width:80%;" maxlength="8"  cssClass="view-field"/>毫秒
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        重试次数：
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="retry" cssStyle="width:80%;" maxlength="8"  cssClass="view-field"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                        短信内容：
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textarea name="template" cssStyle="height: 100px;width:80%;" maxlength="50" cssClass="view-field"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
    </div>
</div>