<div class="hide" id="ftp-dialog" title="<@s.text name="file.ftp.dialog.title"/>">
    <div class="pad10A template" name="default">
        <@s.hidden name="id"/>
        <div class="col-md-12">
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="file.ftp.dialog.name"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="name" cssClass="view-field"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="file.ftp.dialog.controlEncoding"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                    <@s.select name="controlEncoding" list=r"#{'UTF-8','GBK'}" listValue="key" cssClass="chosen-select view-field" data_placeholder="请选择编码格式"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="file.ftp.dialog.hostname"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                    <@s.textfield name="hostname" cssClass="view-field"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="file.ftp.dialog.port"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                    <@s.textfield name="port" cssClass="view-field"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="file.ftp.dialog.username"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                    <@s.textfield name="username" cssClass="view-field"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="file.ftp.dialog.password"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.password name="password"  cssClass="view-field"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="file.ftp.dialog.defaultDir"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                    <@s.textfield name="defaultDir" cssClass="view-field"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="file.ftp.dialog.description"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                         <@s.textarea name="description" cssStyle="height: 118px;" cssClass="view-field"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>