package org.jfantasy.framework.jcaptcha;

import org.jfantasy.framework.httpclient.HttpClientUtil;
import org.jfantasy.framework.httpclient.Response;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.web.ServletUtils;
import com.octo.captcha.Captcha;
import com.octo.captcha.service.CaptchaService;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.CaptchaStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 验证码生成类
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述<br/>依赖 jcaptcha-1.0.jar jcaptcha-api-1.0 imaging.jar
 * @since 2012-11-30 下午05:16:03
 */
@Component("jcaptchaFilter")
public class JCaptchaFilter extends GenericFilterBean {

    @Autowired(required = false)
    private CaptchaService captchaService;

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
    }

    public void doFilter(ServletRequest theRequest, ServletResponse theResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) theRequest;
        HttpServletResponse response = (HttpServletResponse) theResponse;
        if (request.getRequestURI().endsWith(".jpg")) {
            genernateCaptchaImage(request, response);
        } else if (request.getRequestURI().endsWith(".mpeg")) {
            genernateCaptchaAudio(request, response);
        } else {
            chain.doFilter(theRequest, theResponse);
        }
    }

    /**
     * 生成验证码
     *
     * @param request
     * @param response
     * @throws IOException
     */
    protected void genernateCaptchaImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 禁止浏览器缓存
        ServletUtils.setNoCacheHeader(response);
        response.setContentType("image/jpeg");

        ServletOutputStream out = response.getOutputStream();
        try {
            String captchaId = request.getSession(true).getId();
            BufferedImage challenge = (BufferedImage) this.captchaService.getChallengeForID(captchaId, request.getLocale());
            ImageIO.write(challenge, "jpg", out);
            out.flush();
        } catch (CaptchaServiceException e) {
            logger.error(e.getMessage(), e);
        } finally {
            out.close();
        }
    }

    public void genernateCaptchaAudio(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CaptchaStore captchaStore = (CaptchaStore) ClassUtil.getValue(this.captchaService, "store");
        Captcha captcha = captchaStore.getCaptcha(request.getSession().getId());
        if (captcha != null) {
            response.setContentType("audio/mpeg");
            Response clientResponse = HttpClientUtil.doGet("http://translate.google.com/translate_tts?tl=zh&q=" + ((String) ClassUtil.getValue(captcha, "response")).replaceAll("", "、"));
            clientResponse.write(response.getOutputStream());
        }
    }

}