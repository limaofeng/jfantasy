package org.jfantasy.security.data;


import org.jfantasy.oauth.userdetails.OAuthUserDetails;
import org.jfantasy.security.matcher.UrlResourceRequestMatcher;
import org.springframework.security.access.ConfigAttribute;

import java.util.Collection;

public interface SecurityStorage {

    String RESOURCE_IDS = "URLRESOURCEIDS:";
    String RESOURCE_PREFIX = "URLRESOURCEID:";
    String ASSESS_TOKEN_PREFIX = "assess_token:";
    String REFRESH_TOKEN_PREFIX = "refresh_token:";
    String AUTHORIZATION_CODE_PREFIX = "authorization_code:";
    String ALL_PERMISSION = "ALL_PERMISSION:";

    OAuthUserDetails loadUserByUsername(String token);

    UrlResourceRequestMatcher[] getRequestMatchers();

    Collection<ConfigAttribute> getAllPermissions();

}
