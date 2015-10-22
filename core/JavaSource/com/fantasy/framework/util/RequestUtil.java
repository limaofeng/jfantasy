package com.fantasy.framework.util;

import com.fantasy.framework.util.common.StringUtil;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Deprecated
public class RequestUtil extends ServletRequestUtils {

    public static String getStringsParameter(HttpServletRequest request, String name, String defaultVal) {
        String strResult;
        StringBuilder bufResult = new StringBuilder();
        String[] arrayTemp = getStringParameters(request, name);
        if ((arrayTemp != null) && (arrayTemp.length > 0)) {
            for (int num = 0; num < arrayTemp.length; num++) {
                if (num == arrayTemp.length - 1) {
                    bufResult.append(arrayTemp[num]);
                } else {
                    bufResult.append(arrayTemp[num]).append(",");
                }

            }
        }
        strResult = bufResult.toString();
        return StringUtil.defaultValue(strResult, defaultVal);
    }

    public static String getStrParam(ServletRequest request, String name, String defaultVal) throws UnsupportedEncodingException {
        String str = getStringParameter(request, name, defaultVal);
        str = new String(str.getBytes("8859_1"), "UTF-8");
        return str;
    }

}
