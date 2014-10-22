package com.fantasy.framework.jcaptcha;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.fantasy.framework.httpclient.HttpClientUtil;
import com.fantasy.framework.httpclient.Response;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.web.ServletUtils;
import com.octo.captcha.Captcha;
import com.octo.captcha.service.CaptchaService;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.CaptchaStore;

/**
 * 验证码生成类
 * 
 * @功能描述<br/>依赖 jcaptcha-1.0.jar jcaptcha-api-1.0 imaging.jar
 * @author 李茂峰
 * @since 2012-11-30 下午05:16:03
 * @version 1.0
 */
@Component("jcaptchaFilter")
public class JCaptchaFilter extends GenericFilterBean {

	@Autowired
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
			Response clientResponse = HttpClientUtil.doGet("http://translate.google.com/translate_tts?tl=zh&q=" + ((String)ClassUtil.getValue(captcha, "response")).replaceAll("", "、"));
			clientResponse.write(response.getOutputStream());
		}
	}

}