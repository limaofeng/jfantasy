<div class="hide" id="file-dialog" title="<@s.text name="file.filemanager.dialog.title"/>">
    <div class="pad10A template" name="default">
        <div class="col-md-12">
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="file.filemanager.dialog.sn"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                    <@s.textfield name="id" cssClass="view-field"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="file.filemanager.dialog.name"/>
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
                    <@s.text name="file.filemanager.dialog.type"/>
                    </label>
                </div>
                <div class="form-checkbox-radio col-md-10">
                    <div class="append-left">
                    <@s.radio name="type"  list="@com.fantasy.file.bean.enums.FileManagerType@values()"  listValue="value" cssClass="view-field" id="type"/>
                    </div>
                </div>
            </div>
            <div class="form-row" id="ftp" style="display: none;">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="file.filemanager.dialog.ftpConfig"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                    <@s.select cssClass="chosen-select view-field" name="ftpConfig.id" list="@com.fantasy.common.service.FtpConfigService@findftps()" listKey="id" listValue="name" />
                    </div>
                </div>
            </div>
            <div class="form-row" id="local">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="file.filemanager.dialog.localDefaultDir"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                    <@s.textfield name="localDefaultDir" cssClass="view-field"/>
                    </div>
                </div>
            </div>
            <div class="form-row"  id="jdbc" style="display:none;">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="file.filemanager.dialog.jdbcConfig"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                    <@s.select cssClass="chosen-select view-field" name="jdbcConfig.id" list="@com.fantasy.common.service.JdbcConfigService@jdbcConfigs()" listKey="id" listValue="name" />
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="file.filemanager.dialog.description"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                    <@s.textarea name="description" cssClass="view-field" />
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>