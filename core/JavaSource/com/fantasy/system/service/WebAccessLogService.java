package com.fantasy.system.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.framework.util.web.WebUtil.Browser;
import com.fantasy.system.bean.ChartSource;
import com.fantasy.system.bean.WebAccessLog;
import com.fantasy.system.dao.WebAccessLogDao;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class WebAccessLogService {

    @Autowired
    private WebAccessLogDao webAccessLogDao;

    public void log(HttpServletRequest request) {
        String userIp = WebUtil.getRealIpAddress(request);
        Browser browser = WebUtil.browser(request);
        String browserVersion = WebUtil.getBrowserVersion(browser, request);
        String osVersion = WebUtil.getOsVersion(request);
        String contextPath = request.getContextPath();
        String sessionId = request.getSession().getId();
        String url = request.getRequestURI().replace(contextPath, "");
        String parameter = JSON.serialize(getParameterMap(request));
        String referer = WebUtil.getReferer(request);

        WebAccessLog accessLog = new WebAccessLog();
        accessLog.setUrl(url);
        accessLog.setReferer(referer);
        accessLog.setUserIp(userIp);
        accessLog.setBrowser(browser.toString());
        accessLog.setBrowserVersion(browserVersion);
        accessLog.setOsVersion(osVersion);
        accessLog.setSessionId(sessionId);
        accessLog.setParameter(parameter);
        webAccessLogDao.save(accessLog);
    }

    public void log(HttpServletRequest request, UserDetails details) {
        String userIp = WebUtil.getRealIpAddress(request);
        Browser browser = WebUtil.browser(request);
        String browserVersion = WebUtil.getBrowserVersion(browser, request);
        String osVersion = WebUtil.getOsVersion(request);
        String contextPath = request.getContextPath();
        String sessionId = WebUtil.getSessionId(request);
        String url = request.getRequestURI().replace(contextPath, "");
        String parameter = JSON.serialize(getParameterMap(request));
        String referer = WebUtil.getReferer(request);

        WebAccessLog accessLog = new WebAccessLog();
        accessLog.setUrl(url);
        accessLog.setReferer(referer);
        accessLog.setUserIp(userIp);
        accessLog.setBrowser(browser.toString());
        accessLog.setBrowserVersion(browserVersion);
        accessLog.setOsVersion(osVersion);
        accessLog.setSessionId(sessionId);
        accessLog.setParameter(parameter);

        accessLog.setCreator(details.getUsername());

        webAccessLogDao.save(accessLog);
    }

    public int count(List<PropertyFilter> filters) {
        return this.webAccessLogDao.count(filters);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String[]> properties = request.getParameterMap();
        Map<String, String> returnMap = new HashMap<String, String>();
        for (Map.Entry<String, String[]> entry : properties.entrySet()) {
            String name = entry.getKey();
            String value = "";
            String[] values = entry.getValue();
            for (String v : values) {
                value += (v + ",");
            }
            value = value.substring(0, value.length() - 1);
            returnMap.put(name, value);
        }
        return returnMap;
    }


    public Pager<WebAccessLog> findPager(Pager<WebAccessLog> pager, List<PropertyFilter> filters) {
        return webAccessLogDao.findPager(pager, filters);
    }

    /**
     * 删除
     *
     * @param ids
     */
    public void delete(Long[] ids) {
        for (Long id : ids) {
            this.webAccessLogDao.delete(id);
        }
    }

    /**
     * 查询各个浏览器的数量
     *
     * @return
     */
    public List<ChartSource> queryBrowser() {
        return this.webAccessLogDao.queryBrowser();
    }

    /**
     * 查询每天访问数量
     *
     * @return
     */
    public List<ChartSource> queryNum() {
        return this.webAccessLogDao.queryNum();
    }

    /**
     * 查询每天独立IP数
     *
     * @return
     */
    public List<ChartSource> queryIpClickNum() {
        return this.webAccessLogDao.queryIpClickNum();
    }
}