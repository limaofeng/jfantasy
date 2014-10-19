//package com.fantasy.member.ws;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.axis2.context.MessageContext;
//import org.apache.axis2.context.ServiceContext;
//import org.springframework.context.support.MessageSourceAccessor;
//import org.springframework.security.core.SpringSecurityMessageSource;
//
//public class LoginService {
//
//	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
//
//	public User login(String username, String password) {
//		MessageContext mc = MessageContext.getCurrentMessageContext();
//		ServiceContext sc = mc.getServiceContext();
//		sc.setProperty("login", "成功登陆!");
//		return new User(username, password);
//	}
//
//	public String getLoginMsg() {
//		MessageContext mc = MessageContext.getCurrentMessageContext();
//
//		ServiceContext sc = mc.getServiceContext();
//
//		return (String) sc.getProperty("login");
//	}
//
//	public List<User> getUsers() {
//		List<User> users = new ArrayList<User>();
//		users.add(new User("hl", "hl123456"));
//		users.add(new User("limaofeng", "limaofeng123456"));
//		return users;
//	}
//
//	public void loadUsers(User[] users) {
//		try {
//			for (Object user : users)
//				System.out.println(user);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//}
