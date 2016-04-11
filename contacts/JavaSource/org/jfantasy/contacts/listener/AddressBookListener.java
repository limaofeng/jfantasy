package org.jfantasy.contacts.listener;

import org.jfantasy.contacts.service.AddressBookService;
import org.jfantasy.framework.util.common.ClassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AddressBookListener implements ApplicationListener<AuthenticationSuccessEvent> {

    public static final String CURRENT_USER_BOOK_KEY = "contacts";

    @Autowired
    private AddressBookService bookService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        UserDetails details = (UserDetails) event.getAuthentication().getPrincipal();
        if (details != null) {
            Map<String, Object> data = ClassUtil.getValue(details, "data");
            if (data != null && !data.containsKey(CURRENT_USER_BOOK_KEY)) {
                data.put(CURRENT_USER_BOOK_KEY, bookService.myBook(details.getUsername()));
            }
        }
    }
}
