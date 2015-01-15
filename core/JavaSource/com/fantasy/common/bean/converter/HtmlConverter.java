package com.fantasy.common.bean.converter;

import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.htmlcleaner.HtmlCleanerUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.framework.util.web.context.ActionContext;
import ognl.DefaultTypeConverter;
import org.htmlcleaner.TagNode;

import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.util.Map;

public class HtmlConverter extends DefaultTypeConverter {

    @SuppressWarnings("rawtypes")
    public Object convertValue(Map context, Object target, Member member, String propertyName, Object value, Class toType) {
        if (String.class.isAssignableFrom(toType)) {
            String html = StringUtil.nullValue(ClassUtil.isArray(value) ? Array.get(value, 0) : value);
            if (StringUtil.isBlank(html)){
                return html;
            }
            String contextPath = ActionContext.getContext().getHttpRequest().getContextPath();
            TagNode tagNode = HtmlCleanerUtil.htmlCleaner(html);
            if (StringUtil.isNotBlank(contextPath)) {
                for (TagNode node : HtmlCleanerUtil.findTagNodes(tagNode, "//a")) {
                    node.addAttribute("href", RegexpUtil.replace(StringUtil.nullValue(node.getAttributeByName("href")), contextPath, "{contextPath}"));
                }
                for (TagNode node : HtmlCleanerUtil.findTagNodes(tagNode, "//img")) {
                    node.addAttribute("src", RegexpUtil.replace(StringUtil.nullValue(node.getAttributeByName("src")), contextPath, "{contextPath}"));
                }
            }
            html = HtmlCleanerUtil.getAsString(HtmlCleanerUtil.findFristTagNode(tagNode, "//body"));
            html = RegexpUtil.replace(html, "^\n{0,}<body>", "");
            html = RegexpUtil.replace(html, "</body>$", "");
            return html;
        }
        return super.convertValue(context, target, member, propertyName, value, toType);
    }

}
