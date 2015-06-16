<table id="brand_view" class="table table-hover mrg5B text-center">
    <thead>
    <tr>
        <th class="pad15L" style="width:20px;">
            <input id="allChecked" class="custom-checkbox bg-white" checkAll=".id"  type="checkbox"/>
        </th>
        <th>品牌名称</th>
        <th>品牌英文名称</th>
        <th>介绍</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <tr class="template" name="default">
        <td>
            <input class="id custom-checkbox" type="checkbox" value="{id}"/>
            <input type="hidden" name="brands[#index].id" value="{id}"/>
        </td>
        <td>{name}</td>
        <td>{engname}</td>
        <td>{introduction}</td>
        <td>
            <a title="" data-placement="top" class="btn small hover-blue up" href="javascript:;">
                <i class="glyph-icon icon-circle-arrow-up"></i>
            </a>
            <a title="" data-placement="top" class="btn small hover-green down" href="javascript:;">
                <i class="glyph-icon icon-circle-arrow-down"></i>
            </a>
        </td>
    </tr>
    </tbody>
</table>