package com.fantasy.contacts.service.handler;

import com.fantasy.contacts.service.AddressBookService;
import com.fantasy.framework.util.common.ClassUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class AddressBookLoginSuccessHandler implements AuthenticationSuccessHandler {

	public static final String CURRENT_USER_BOOK_KEY = "contacts";

	@Autowired(name="ab.AddressBookService")
	private AddressBookService bookService;

	@SuppressWarnings("unchecked")
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		UserDetails details = (UserDetails) authentication.getPrincipal();
		if (details != null) {
			Map<String, Object> data = (Map<String, Object>) ClassUtil.getValue(details, "data");
			if (data != null && !data.containsKey(CURRENT_USER_BOOK_KEY)) {
				data.put(CURRENT_USER_BOOK_KEY, bookService.myBook(details.getUsername()));
			}
		}
	}

}
