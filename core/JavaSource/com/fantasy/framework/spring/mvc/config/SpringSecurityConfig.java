/*
 * COPYRIGHT Beijing NetQin-Tech Co.,Ltd.                                   *
 ****************************************************************************
 * 源文件名:  web.config.SecurityConfig.java 													       
 * 功能: cpframework框架													   
 * 版本:	@version 1.0	                                                                   
 * 编制日期: 2014年8月18日 上午11:09:27 						    						                                        
 * 修改历史: (主要历史变动原因及说明)		
 * YYYY-MM-DD |    Author      |	 Change Description		      
 * 2014年8月18日    |    Administrator     |     Created 
 */
package com.fantasy.framework.spring.mvc.config;

import com.fantasy.security.userdetails.SimpleUserDetailsService;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.event.LoggerListener;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: <类功能描述>. <br>
 * <p>
 * <使用说明>
 * </p>
 * Makedate:2014年8月18日 上午11:09:27
 * 
 * @author Administrator
 * @version V1.0
 */
@Configuration
@EnableWebMvcSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger logger = Logger.getLogger(SpringSecurityConfig.class);

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 设置拦截规则
		// http.authorizeRequests().antMatchers("/**/*.do*").hasRole("USER")
		// .antMatchers("/**/*.doc").hasRole("ADMIN");

		// 自定义accessDecisionManager访问控制器,并开启表达式语言
		http.authorizeRequests().accessDecisionManager(accessDecisionManager())
				.expressionHandler(webSecurityExpressionHandler())
				.antMatchers("/**/*.do*").hasRole("USER")
				.antMatchers("/**/*.htm").hasRole("ADMIN").and()
				.exceptionHandling().accessDeniedPage("/login");

		// 开启默认登录页面
		// http.formLogin();

		// 自定义登录页面
		http.csrf().disable().formLogin().loginPage("/login")
				.failureUrl("/login?error=1")
				.loginProcessingUrl("/j_spring_security_check")
				.usernameParameter("j_username")
				.passwordParameter("j_password").permitAll();

		// 自定义注销
		http.logout().logoutUrl("/logout").logoutSuccessUrl("/login").invalidateHttpSession(true);

		// session管理
//		http.sessionManagement().sessionFixation().changeSessionId().maximumSessions(1).expiredUrl("/");

		// RemeberMe
		http.rememberMe().key("webmvc#FD637E6D9C0F1A5A67082AF56CE32485");

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		// 设置内存用户角色
		// auth.inMemoryAuthentication().withUser("user").password("123456")
		// .roles("USER").and().withUser("admin").password("654321")
		// .roles("USER", "ADMIN");

		// 自定义UserDetailsService
		auth.userDetailsService(userDetailsService()).passwordEncoder(
				new Md5PasswordEncoder());

	}

	@Bean
	public UserDetailsService userDetailsService() {
		logger.info("CP_UserDetailsService");
		return new SimpleUserDetailsService();
	}

	@Bean
	public LoggerListener loggerListener() {
		logger.info("org.springframework.security.authentication.event.LoggerListener");
		return new LoggerListener();
	}

	@Bean
	public org.springframework.security.access.event.LoggerListener eventLoggerListener() {
		logger.info("org.springframework.security.access.event.LoggerListener");
		return new org.springframework.security.access.event.LoggerListener();
	}

	/*
	 * 
	 * 这里可以增加自定义的投票器
	 */
	@SuppressWarnings("rawtypes")
	@Bean(name = "accessDecisionManager")
	public AccessDecisionManager accessDecisionManager() {
		logger.info("AccessDecisionManager");
		List<AccessDecisionVoter> decisionVoters = new ArrayList<AccessDecisionVoter>();
		decisionVoters.add(new RoleVoter());
		decisionVoters.add(new AuthenticatedVoter());
		decisionVoters.add(webExpressionVoter());// 启用表达式投票器

		AffirmativeBased accessDecisionManager = new AffirmativeBased(
				decisionVoters);

		return accessDecisionManager;
	}

	/*
	 * 表达式控制器
	 */
	@Bean(name = "expressionHandler")
	public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
		logger.info("DefaultWebSecurityExpressionHandler");
		DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
		return webSecurityExpressionHandler;
	}

	/*
	 * 表达式投票器
	 */
	@Bean(name = "expressionVoter")
	public WebExpressionVoter webExpressionVoter() {
		logger.info("WebExpressionVoter");
		WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
		webExpressionVoter.setExpressionHandler(webSecurityExpressionHandler());
		return webExpressionVoter;
	}

}
