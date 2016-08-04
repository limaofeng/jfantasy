package org.jfantasy.oauth.service;

import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.service.MemberService;
import org.jfantasy.oauth.bean.ApiKey;
import org.jfantasy.oauth.bean.Application;
import org.jfantasy.oauth.bean.enums.TokenType;
import org.jfantasy.oauth.service.vo.AccessToken;
import org.jfantasy.oauth.service.vo.TokenRequest;
import org.jfantasy.oauth.service.vo.TokenResponse;
import org.jfantasy.oauth.userdetails.OAuthUserDetails;
import org.jfantasy.oauth.userdetails.enums.Scope;
import org.jfantasy.security.bean.User;
import org.jfantasy.security.data.SecurityStorage;
import org.jfantasy.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AccessTokenService {

    @Autowired
    private ApiKeyService apiKeyService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private MemberService memberService;

    @Transactional
    @SuppressWarnings("unchecked")
    public TokenResponse allocateToken(TokenRequest request) {
        AccessToken accessToken = new AccessToken();
        ApiKey apiKey = apiKeyService.get(request.getApiKey());

        if (apiKey == null) {
            throw new RestException(" client_id 无效");
        }

        Long appId = apiKey.getApplication().getId();
        accessToken.setApiKey(apiKey.getKey());
        accessToken.setAppId(appId);
        accessToken.setType(TokenType.bearer);
        accessToken.setGrantType(request.getGrantType());
        accessToken.setRefreshToken(request.getRefreshToken());
        //TODO 后期可以改为单独配置的属性
        accessToken.setExpires(30 * 60 * 60);
        accessToken.setReExpires(40 * 60 * 60);

        ValueOperations valueOper = redisTemplate.opsForValue();
        HashOperations hashOper = redisTemplate.opsForHash();
        SetOperations setOper = redisTemplate.opsForSet();

        OAuthUserDetails userDetails = new OAuthUserDetails(appId, apiKey.getApplication().getName(), apiKey.getKey(), apiKey.getDescription(),apiKey.getPlatform());

        String token = UUID.randomUUID().toString() + "_" + request.getGrantType() + "_" + apiKey.getKey();

        switch (request.getGrantType()) {
            case authorization_code:
                Object value = valueOper.get(SecurityStorage.AUTHORIZATION_CODE_PREFIX + request.getCode());
                if (value == null) {
                    throw new RestException(" authorization code 无效");
                }
                redisTemplate.delete(SecurityStorage.AUTHORIZATION_CODE_PREFIX + request.getCode());
                if (value instanceof User) {
                    retrieveUser(userDetails, (User) value);
                } else if (value instanceof Member) {
                    retrieveUser(userDetails, (Member) value);
                }
                token += ("_" + request.getCode());
                break;
            case client_credentials:
                retrieveUser(userDetails, apiKey);
                break;
            case password:
                switch (request.getScope()) {
                    case "user":
                        retrieveUser(userDetails, userService.login(request.getUsername(), request.getPassword()));
                        break;
                    case "member":
                        retrieveUser(userDetails, memberService.login(request.getUsername(), request.getPassword()));
                        break;
                }
                redisTemplate.delete(SecurityStorage.AUTHORIZATION_CODE_PREFIX + request.getCode());
                break;
            case refresh_token:
                AccessToken oldaccessToken = (AccessToken) hashOper.get(SecurityStorage.REFRESH_TOKEN_PREFIX + accessToken.getRefreshToken(), "token");
                if (oldaccessToken == null) {
                    throw new RestException(" refresh_token 无效");
                }
                if (apiKey.getKey().equals(oldaccessToken.getKey())) {
                    throw new RestException(" apikey 与原值不匹配 ");
                }
                retrieveUser(userDetails, (OAuthUserDetails) hashOper.get(SecurityStorage.REFRESH_TOKEN_PREFIX + accessToken.getRefreshToken(), "user"));
                redisTemplate.delete(SecurityStorage.REFRESH_TOKEN_PREFIX + accessToken.getRefreshToken());
                break;
        }

        accessToken.setKey(appId + "/" + DigestUtils.md5DigestAsHex(token.getBytes()));
        accessToken.setRefreshToken(appId + "/" + DigestUtils.md5DigestAsHex((token + "RefreshToken").getBytes()));
        accessToken.setTokenCreationTime(DateUtil.now());

        String redisAccessTokenKey = SecurityStorage.ASSESS_TOKEN_PREFIX + accessToken.getKey();
        hashOper.put(redisAccessTokenKey, "token", accessToken);//保存 assesstoken 到 redis
        hashOper.put(redisAccessTokenKey, "user", userDetails); //保存 userdetails 到 redis
        redisTemplate.expire(redisAccessTokenKey, accessToken.getExpires(), TimeUnit.SECONDS);

        //保存 refreshtoken 到 redis
        String redisRefreshTokenKey = SecurityStorage.REFRESH_TOKEN_PREFIX + accessToken.getRefreshToken();
        hashOper.put(redisRefreshTokenKey, "token", accessToken);
        hashOper.put(redisRefreshTokenKey, "user", userDetails);
        redisTemplate.expire(redisRefreshTokenKey, accessToken.getReExpires(), TimeUnit.SECONDS);

        //缓存 token 记录 . 避免重复生成 token 的问题
        String key = apiKey.getKey() + userDetails.getScope() + userDetails.getId();
        Set<String> tokens = setOper.members(key);
        for (String _token : tokens) {
            setOper.remove(key, _token);
        }
        tokens.add(key);
        redisTemplate.delete(tokens);
        setOper.add(key, redisAccessTokenKey, redisRefreshTokenKey);

        return new TokenResponse(accessToken);
    }

    public OAuthUserDetails details(String token) {
        HashOperations hashOper = redisTemplate.opsForHash();
        OAuthUserDetails userDetails = (OAuthUserDetails) hashOper.get(SecurityStorage.ASSESS_TOKEN_PREFIX + token, "user");
        if (userDetails == null) {
            throw new UsernameNotFoundException(" Token Invalid ");
        }
        return userDetails;
    }

    private void retrieveUser(OAuthUserDetails userDetails, OAuthUserDetails user) {
        userDetails.setId(user.getId());
        userDetails.setUsername(user.getUsername());
        userDetails.setUserType(user.getUserType());
        userDetails.setScope(user.getScope());
        userDetails.setNickName(user.getNickName());

        userDetails.setType(user.getType());
        userDetails.setKey(user.getKey());

        userDetails.setEnabled(user.isEnabled());
        userDetails.setAccountNonExpired(user.isAccountNonExpired());
        userDetails.setAccountNonLocked(user.isAccountNonLocked());
        userDetails.setCredentialsNonExpired(user.isCredentialsNonExpired());

        userDetails.setAuthorities(new ArrayList<>(user.getAuthorities()));
    }

    private void retrieveUser(OAuthUserDetails userDetails, ApiKey apiKey) {
        userDetails.setId(0L);
        userDetails.setUsername(apiKey.getKey());
        userDetails.setUserType(null);
        userDetails.setScope(Scope.apiKey);
        userDetails.setNickName(apiKey.getDescription());

        Application application = apiKey.getApplication();

        userDetails.setType(OAuthUserDetails.Type.app);
        userDetails.setKey(OAuthUserDetails.Type.app.name() + ":" + application.getId());

        userDetails.setEnabled(true);
        userDetails.setAccountNonExpired(true);
        userDetails.setAccountNonLocked(true);
        userDetails.setCredentialsNonExpired(true);
    }

    private void retrieveUser(OAuthUserDetails userDetails, User user) {
        userDetails.setId(user.getId());
        userDetails.setUsername(user.getUsername());
        userDetails.setUserType(user.getUserType());
        userDetails.setScope(Scope.user);
        userDetails.setNickName(user.getNickName());

        userDetails.setType(OAuthUserDetails.Type.user);
        userDetails.setKey(OAuthUserDetails.Type.user.name() + ":" + user.getUsername());

        userDetails.setEnabled(user.isEnabled());
        userDetails.setAccountNonExpired(user.isAccountNonExpired());
        userDetails.setAccountNonLocked(user.isAccountNonLocked());
        userDetails.setCredentialsNonExpired(user.isCredentialsNonExpired());

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String authority : user.getAuthorities()) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        userDetails.setAuthorities(authorities);

    }

    private void retrieveUser(OAuthUserDetails userDetails, Member member) {
        userDetails.setId(member.getId());
        userDetails.setUsername(member.getUsername());
        userDetails.setUserType(member.getType());
        userDetails.setScope(Scope.member);
        userDetails.setNickName(member.getNickName());

        userDetails.setType(OAuthUserDetails.Type.member);
        userDetails.setKey(OAuthUserDetails.Type.member.name() + ":" + member.getUsername());

        userDetails.setEnabled(member.isEnabled());
        userDetails.setAccountNonExpired(member.isAccountNonExpired());
        userDetails.setAccountNonLocked(member.isAccountNonLocked());
        userDetails.setCredentialsNonExpired(member.isCredentialsNonExpired());

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String authority : member.getAuthorities()) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        userDetails.setAuthorities(authorities);
    }

}
