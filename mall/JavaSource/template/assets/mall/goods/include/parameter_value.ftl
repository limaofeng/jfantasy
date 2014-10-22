<@s.if test="category.goodsParameters.size > 0">
<table class="formTable mb3">
    <tbody>
        <@s.iterator value="category.goodsParameters" var="goodsParameter" status="st">
            <@s.if test="%{#st.odd}">
            <tr>
            </@s.if>
            <td class="formItem_title w100">
                <@s.hidden value="%{#goodsParameter.id}">
                    <@s.param name="name">
                        goodsParameterValues[<@s.property value="#st.index"/>].id
                    </@s.param>
                </@s.hidden>
                <@s.property value="#goodsParameter.name"/>
            </td>
            <td class="formItem_content" >
                <@s.textfield name="goodsParameterValues[%{#st.index}].value" value="%{@com.fantasy.framework.util.common.ObjectUtil@find(goods.goodsParameterValues,'id',#goodsParameter.id).value}" cssClass="w250" placeholder="%{#goodsParameter.format}"/>
            </td>
            <@s.if test="%{#st.even}">
            </tr>
            </@s.if>
        </@s.iterator>
        <@s.if test="%{category.goodsParameters.size % 2 != 0}">
        <td class="formItem_title w100"></td>
        <td class="formItem_content"></td>
        </tr>
        </@s.if>
    </tbody>
</table>
</@s.if>