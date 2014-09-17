<div class="hide" id="website-dialog" title="<@s.text name="system.website.dialog.title"/>">
    <div class="pad10A template" name="default">
    <@s.hidden name="id"/>
        <div class="col-md-12">
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="system.website.dialog.code"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                    <@s.textfield name="key" cssClass="view-field" />
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="system.website.dialog.name"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                    <@s.textfield name="name" cssClass="view-field" />
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="system.website.dialog.url"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                    <@s.textfield name="web"  cssClass="view-field"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="system.website.dialog.menuroot"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                    <@s.select name="rootMenu.id"  list="@com.fantasy.security.service.MenuService@rootMenuList()" listKey="id" listValue="name" cssClass="chosen-select view-field" data_placeholder="请选择菜单根节点"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                    <@s.text name="system.website.dialog.defaultFileManager"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                    <@s.select name="defaultFileManager.id"  list="@com.fantasy.file.service.FileManagerFactory@getFileManagers()" listKey="id" listValue="name" cssClass="chosen-select view-field" data_placeholder="请选择文件管理器"/>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label col-md-2">
                    <label for="">
                       <@s.text name="system.website.dialog.defaultUploadFileManager"/>
                    </label>
                </div>
                <div class="form-input col-md-10">
                    <div class="append-left">
                    <@s.select name="defaultUploadFileManager.id"  list="@com.fantasy.file.service.FileManagerFactory@getFileManagers()" listKey="id" listValue="name" cssClass="chosen-select view-field" data_placeholder="请选择文件上传管理器"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>