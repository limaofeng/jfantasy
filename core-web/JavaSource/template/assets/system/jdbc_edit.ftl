<div class="hide" id="jdbc-dialog" title="<@s.text name="common.jdbc.dialog.title" />">
    <div class="pad10A template" name="default">
        <@s.hidden name="id"/>
        <div class="col-md-12">
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="common.jdbc.dialog.name" />
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
                    <@s.text name="common.jdbc.dialog.type" />
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.select  name="type" list=r"@com.fantasy.common.bean.JdbcConfig$DataBaseType@values()" cssClass="chosen-select view-field" listValue="value"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="common.jdbc.dialog.hostname" />
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="hostname"  cssClass="view-field"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="common.jdbc.dialog.port" />
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="port" value="3306" cssClass="view-field"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="common.jdbc.dialog.username" />
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
                    <@s.text name="common.jdbc.dialog.password" />
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
                        <@s.text name="common.jdbc.dialog.database" />
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                        <@s.textfield name="database" cssClass="view-field"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>