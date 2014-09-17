<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
    <@s.text name="security.role.title"/>
    <small>
        <@s.text name="security.role.description"/>
    </small>
</@override>
<@override name="head">
<script type="text/javascript">
$(function(){
    /*
    window.roleDetails = $('#roleDetails').form();
    window.roleDetails.on('change',function(data,templateName,element){
        console.log(data);
        var $saveForm = $(".saveForm",element.target).ajaxForm(function(data){
            $('#pager').data('pager').reload();
            $('#rolelist').show().siblings().hide();
            $.msgbox({
                msg : '角色'+data.name+'</b> 保存成功',
                type : 'success'
            });
        });
        $('.to-list',element.target).click(function(e){
            e.preventDefault();
            $('#ddlist').show().siblings().hide();
        });
        $('.opt-doc',element.target).click(function(e){
            e.preventDefault();
            $('#ddtDetails').show().nextAll().hide();
            ddtDetails.setTemplate('default');
        });
        $('.dd-save',element.target).click(function(e){
            e.preventDefault();
            $saveForm.submit();
        });
    });*/
    //当浏览器窗口发生变化时,自动调整布局的js代码
    var _$gridPanel = $('.grid-panel');
    var _resize = function () {
        _$gridPanel.css('minHeight', $(window).height() - (_$gridPanel.offset().top + 15));
        _$gridPanel.triggerHandler('resize');
    };
    $(window).resize(_resize);
    $page$.on('destroy',function(){
        $(window).unbind('resize',_resize);
    });
    var $advsearch = $('.propertyFilter').advsearch({
        filters : [{
            name : 'S_code',
            text : '编码',
            type : 'input',
            matchType :['EQ','LIKE','LT','GT']
        },{
            name : 'S_name',
            text : '名称',
            type : 'input',
            matchType :['EQ','LIKE','GT']
        }]
    });
    //列表初始化
    var pager=<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
    var $grid = $('#view').dataGrid($('#searchFormPanel'),$('#batchBtns'));
    $grid.data('grid').view().on('add',function(data){
        var _$actions = $('.actions',this.target);
        $('.delete',_$actions).click(function(){
            deleteMethod([data.code]);
            _$actions.removeClass('open');
            return false;
        });
    });
    var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$grid.data('grid').pager(),'code','<@s.text name="security.role.delete.alert"><@s.param name="0" value="'{name}'"/></@s.text>',function(){
        $.msgbox({
            msg : "<@s.text name="security.role.delete.success"/>",
            type : "success"
        });
    });
    $grid.setJSON(pager);
});
</script>
</@override>
<@override name="pageContent">
<div id="searchFormPanel" class="button-panel pad5A">
    <@s.form id="searchForm" namespace="/security/role" action="search" method="post">
        <a title="<@s.text name="security.role.button.add"/>" class="btn medium primary-bg" href="<@s.url namespace="/security/role" action="add"/>"  target="after:closest('#page-content')">
            <span class="button-content">
                <i class="glyph-icon icon-plus float-left"></i>
                <@s.text name="security.role.button.add"/>
            </span>
        </a>
        <input name="EQS_type" type="hidden"/>
        <div class="propertyFilter">
        </div>
        <div class="form-search">
            <input type="text" name="LIKES_name_OR_code" title="" data-placement="bottom" class="input tooltip-button ac_input" placeholder="Search..." autocomplete="off" style="display: inline-block; width: 200px;">
            <i class="glyph-icon icon-search"></i>
        </div>
    </@s.form>
</div>
<div id="batchBtns" style="float:left;">
    <a title="<@s.text name="security.role.button.batchDelete"/>" class="btn small primary-bg batchDelete" href="<@s.url namespace="/security/role" action="delete"/>">
        <span class="button-content">
            <i class="glyph-icon icon-trash float-left"></i>
            <@s.text name="security.role.button.batchDelete"/>
        </span>
    </a>
</div>
<div class="grid-panel">
<table id="view" class="table table-hover mrg5B text-center">
    <thead>
    <tr>
        <th class="pad15L" style="width:20px;">
            <input id="allChecked" class="custom-checkbox bg-white" checkAll=".id" type="checkbox"/>
        </th>
        <th class="sort" orderBy="name"><@s.text name="security.role.list.name"/></th>
        <th class="sort" orderBy="code"><@s.text name="security.role.list.code"/></th>
        <th class="sort"><@s.text name="security.role.list.type"/></th>
        <th class="text-center"><@s.text name="security.role.list.actions"/></th>
    </tr>
    </thead>
    <tbody>
    <tr class="template" name="default">
        <td><input class="id custom-checkbox" type="checkbox" value="{code}"/></td>
        <td class="font-bold">{name}</td>
        <td>{code}</td>
        <td>{type:configTypeName()}</td>
        <td class="pad0T pad0B text-center">
            <div class="dropdown actions">
                <a href="javascript:;" title="" class="btn medium bg-blue" data-toggle="dropdown">
                            <span class="button-content">
                                <i class="glyph-icon font-size-11 icon-cog"></i>
                                <i class="glyph-icon font-size-11 icon-chevron-down"></i>
                            </span>
                </a>
                <ul class="dropdown-menu float-right">
                    <li>
                        <a href="<@s.url namespace="/security/role" action="edit?id={code}"/>" title="" class="edit" target="after:closest('#page-content')">
                            <i class="glyph-icon icon-edit mrg5R"></i>
                            <@s.text name="security.role.list.actions.edit"/>
                        </a>
                    </li>
                    <li class="divider"></li>
                    <li>
                        <a href="<@s.url namespace="/security/role" action="delete?ids={code}"/>" class="font-red delete">
                            <i class="glyph-icon icon-remove mrg5R"></i>
                            <@s.text name="security.role.list.actions.delete"/>
                        </a>
                    </li>
                </ul>
            </div>
        </td>
    </tr>
    </tbody>
</table>
</div>
<div class="divider mrg0T"></div>
<div id="roleDetails" style="display:none;">
<div class="template mrg5A" name="default">
    <div class="infobox warning-bg mrg10B">
        <p><i class="glyph-icon icon-exclamation mrg10R"></i>编码新增后不能修改,请谨慎填写。</p>
    </div>
    <@s.form namespace="/security/role" action="save" cssClass="form-bordered saveForm" method="post">
    <#--
    <input type="hidden" class="view-field" name="parent.code" mapping="parentCode"/>
    -->
        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                    所选分类:
                </label>
            </div>
            <div class="form-input col-md-10">
                <input type="hidden" class="view-field" name="type"/>
                <input type="text" name="typeName" disabled="disabled" value="{type:typeName()}"/>
            </div>
        </div>
        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                    编码:
                </label>
            </div>
            <div class="form-input col-md-10">
                <input type="text" class="view-field" name="code"/>
            </div>
        </div>
        <div class="form-row">
            <div class="form-label col-md-2">
                <label for="">
                    名称:
                </label>
            </div>
            <div class="form-input col-md-10">
                <input type="text" class="view-field" name="name"/>
            </div>
        </div>
        <div class="form-row pad0B">
            <div class="form-label col-md-2">
                <label for="">
                    描述:
                </label>
            </div>
            <div class="form-input col-md-10">
                <input name="description" class="view-field small-textarea"/>
            </div>
        </div>
        <div class="form-row">
            <div class="form-input col-md-10 col-md-offset-2">
                <a title="返回列表" class="btn medium primary-bg radius-all-4 to-list" href="javascript:;">
                                <span class="glyph-icon icon-separator">
                                    <i class="glyph-icon icon-reply"></i>
                                </span>
                                <span class="button-content">
                                    返回列表
                                </span>
                </a>
                <a title="保存修改" class="btn medium primary-bg radius-all-4 dd-save" href="javascript:;">
                                <span class="glyph-icon icon-separator">
                                    <i class="glyph-icon icon-save"></i>
                                </span>
                                <span class="button-content">
                                    保存修改
                                </span>
                </a>
                <a title="查看操作说明" class="btn medium bg-gray radius-all-4 hover-white opt-doc" href="javascript:;">
                                <span class="glyph-icon icon-separator">
                                    <i class="glyph-icon icon-exclamation font-gray-dark"></i>
                                </span>
                                <span class="button-content">
                                    查看操作说明
                                </span>
                </a>
            </div>
        </div>
    </@s.form>
</div>
</@override>
<@extends name="../wrapper.ftl"/>