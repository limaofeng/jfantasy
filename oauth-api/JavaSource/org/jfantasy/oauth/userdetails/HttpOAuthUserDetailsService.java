package org.jfantasy.oauth.userdetails;


import org.jfantasy.framework.httpclient.HttpClientUtil;
import org.jfantasy.framework.httpclient.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;

public class HttpOAuthUserDetailsService implements UserDetailsService {

    private String apiUrl;

    public HttpOAuthUserDetailsService(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    @SuppressWarnings("unchecked")
    @Override
    public UserDetails loadUserByUsername(String accessToken) throws UsernameNotFoundException {
        try {
            Response response = HttpClientUtil.doGet(apiUrl + "/oauth/user-details/" + accessToken);
            if (response.getStatusCode() != HttpStatus.OK.value()) {
                throw new UsernameNotFoundException(" Token Invalid ");
            }
            return response.getBody(OAuthUserDetails.class);
        } catch (IOException e) {
            throw new UsernameNotFoundException(" Token Invalid ");
        }
    }
}
