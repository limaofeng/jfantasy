package org.jfantasy.oauth.userdetails;


import org.jfantasy.security.data.SecurityStorage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class OAuthUserDetailsService implements UserDetailsService {

    private SecurityStorage storage;

    public OAuthUserDetailsService(SecurityStorage storage){
        this.storage = storage;
    }

    @SuppressWarnings("unchecked")
    @Override
    public UserDetails loadUserByUsername(String accessToken) throws UsernameNotFoundException {
        return storage.loadUserByUsername(accessToken);
    }

}
