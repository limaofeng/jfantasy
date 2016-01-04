package org.jfantasy.system.bean;

import org.jfantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "SYS_WEB_ACCESS_LOG")
public class WebAccessLog extends BaseBusEntity {

    private static final long serialVersionUID = -8993535557174819747L;

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 访问的url
     */
    @Column(name = "URL", length = 200)
    private String url;
    /**
     * 请求参数
     */
    @Column(name = "PARAMETER", length = 1000)
    private String parameter;
    /**
     *
     */
    @Column(name = "REFERER", length = 100)
    private String referer;
    /**
     * sessionId
     */
    @Column(name = "SESSION_ID", length = 50)
    private String sessionId;
    /**
     * 访问Ip
     */
    @Column(name = "USER_IP", length = 20)
    private String userIp;
    /**
     * 浏览器
     */
    @Column(name = "BROWSER", length = 20)
    private String browser;
    /**
     * 浏览器版本
     */
    @Column(name = "BROWSER_VERSION", length = 20)
    private String browserVersion;
    /**
     * 操作系统
     */
    @Column(name = "OS_VERSION", length = 20)
    private String osVersion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

}
