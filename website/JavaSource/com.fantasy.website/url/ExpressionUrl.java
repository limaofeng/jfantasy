package org.jfantasy.website.url;

import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.jfantasy.website.OutPutUrl;

import java.util.Map;
import java.util.regex.Matcher;

/**
 * 可以通过表达式从数据中生成url
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-3-28 下午02:40:33
 */
public class ExpressionUrl implements OutPutUrl {

    private String url;

    public ExpressionUrl(String url) {
        this.url = url;
    }

    public ExpressionUrl() {
    }

    public String getUrl(final Map<String, Object> data) {
        return RegexpUtil.replace(url, "\\$\\{([a-zA-z0-9_]+)\\}", new RegexpUtil.AbstractReplaceCallBack() {

            @Override
            public String doReplace(String text, int index, Matcher matcher) {
                Object val = OgnlUtil.getInstance().getValue($(1), data);
                return StringUtil.defaultValue(val, "");
            }

        });
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
