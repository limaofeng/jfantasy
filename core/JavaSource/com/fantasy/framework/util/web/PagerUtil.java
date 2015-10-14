package com.fantasy.framework.util.web;

import com.fantasy.framework.util.common.NumberUtil;
import com.fantasy.framework.util.common.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PagerUtil {

    /**
     * @param curPage    当前页数
     * @param totalPage  总页数
     * @param pageNumber 当前面前面显示几个，后面显示几个
     * @return {List<String>}
     */
    private PagerUtil() {
    }

    public static List<String> getPageArray(int curPage, int totalPage, int pageNumber) {
        List<String> arraylist = new ArrayList<String>();
        for (int i = 1, size = 1; i <= totalPage && size <= (pageNumber * 2 + 3); i++) {
            if (i == 1) {
                arraylist.add(i + "");
                size++;
            } else if ((curPage > totalPage - (pageNumber + 1) && i + (pageNumber * 2 + 2) > totalPage)// 当前页在前5位
                    || (i > curPage - (pageNumber + 1) && i < curPage)) {
                arraylist.add(i + "");
                size++;
            } else if (i == curPage) {
                arraylist.add(i + "");
                size++;
            } else if (i > curPage && i < curPage + (pageNumber + 1) && size < (pageNumber * 2 + 2)) {
                arraylist.add(i + "");
                size++;
            } else if (size > (pageNumber + 1) && size <= (pageNumber * 2 + 2)) {
                arraylist.add(i + "");
                size++;
            } else if (i == totalPage) {
                arraylist.add(i + "");
                size++;
            }
        }
        if (totalPage > 1 && !(NumberUtil.toInteger(arraylist.get(1)) == 2)) {
            arraylist.set(1, "...");
        }
        if (totalPage > 1 && !(NumberUtil.toInteger(arraylist.get(arraylist.size() - 2)) == (totalPage - 1))) {
            arraylist.set(arraylist.size() - 2, "...");
        }
        return arraylist;
    }

    public static String getPagerUrl(String requestUri, String queryString, String currentPage, String currentPageName) {
        Map<String, String[]> params = WebUtil.parseQuery(queryString);
        if (!params.containsKey(currentPageName)) {
            params.put(currentPageName, new String[]{currentPage});
        } else {
            params.put(currentPageName, new String[]{currentPage});
        }
        queryString = WebUtil.getQueryString(params);
        return requestUri + (StringUtil.isNotBlank(queryString) ? "?" + queryString : "");
    }

    public static String getPagerUrl(String requestUri, String queryString, String currentPage) {
        return getPagerUrl(requestUri, queryString, currentPage, "pager.currentPage");
    }

}

