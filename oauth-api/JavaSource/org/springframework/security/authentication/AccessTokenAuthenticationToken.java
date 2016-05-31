package org.springframework.security.authentication;

import org.springframework.security.core.SpringSecurityCoreVersion;

public class AccessTokenAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    public AccessTokenAuthenticationToken(Object token) {
        super(token, null);
    }

}
