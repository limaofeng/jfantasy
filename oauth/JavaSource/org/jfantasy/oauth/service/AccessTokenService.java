package org.jfantasy.oauth.service;

import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.service.MemberService;
import org.jfantasy.oauth.bean.ApiKey;
import org.jfantasy.oauth.bean.enums.TokenType;
import org.jfantasy.oauth.service.vo.AccessToken;
import org.jfantasy.oauth.service.vo.TokenRequest;
import org.jfantasy.oauth.service.vo.TokenResponse;
import org.jfantasy.oauth.userdetails.OAuthUserDetails;
import org.jfantasy.oauth.userdetails.enums.Scope;
import org.jfantasy.security.bean.User;
import org.jfantasy.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

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

    private final static String REDIS_ASSESS_TOKEN_PREFIX = "assess_token:";
    private final static String REDIS_REFRESH_TOKEN_PREFIX = "refresh_token:";
    public final static String REDIS_AUTHORIZATION_CODE_PREFIX = "authorization_code:";

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
        //TODO 后期可以改为单独配置的属性
        accessToken.setExpires(30 * 60);
        accessToken.setReExpires(40 * 60);

        ValueOperations valueOper = redisTemplate.opsForValue();
        HashOperations hashOper = redisTemplate.opsForHash();
        SetOperations setOper = redisTemplate.opsForSet();

        OAuthUserDetails userDetails = new OAuthUserDetails(appId, apiKey.getApplication().getName(), apiKey.getKey(), apiKey.getDescription());

        String token = UUID.randomUUID().toString() + "_" + request.getGrantType() + "_" + apiKey.getKey();

        switch (request.getGrantType()) {
            case authorization_code:
                Object value = valueOper.get(REDIS_AUTHORIZATION_CODE_PREFIX + request.getCode());
                if (value == null) {
                    throw new RestException(" authorization code 无效");
                }
                redisTemplate.delete(REDIS_AUTHORIZATION_CODE_PREFIX + request.getCode());
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
                redisTemplate.delete(REDIS_AUTHORIZATION_CODE_PREFIX + request.getCode());
                break;
            case refresh_token:
                AccessToken oldaccessToken = (AccessToken) hashOper.get(REDIS_REFRESH_TOKEN_PREFIX + accessToken.getRefreshToken(), "token");
                if (oldaccessToken == null) {
                    throw new RestException(" refresh_token 无效");
                }
                if (apiKey.getKey().equals(oldaccessToken.getKey())) {
                    throw new RestException(" apikey 与原值不匹配 ");
                }
                retrieveUser(userDetails, (OAuthUserDetails) hashOper.get(REDIS_REFRESH_TOKEN_PREFIX + accessToken.getRefreshToken(), "user"));
                redisTemplate.delete(REDIS_AUTHORIZATION_CODE_PREFIX + accessToken.getRefreshToken());
                break;
        }

        accessToken.setKey(appId + "/" + DigestUtils.md5DigestAsHex(token.getBytes()));
        accessToken.setRefreshToken(appId + "/" + DigestUtils.md5DigestAsHex((token + "RefreshToken").getBytes()));
        accessToken.setTokenCreationTime(DateUtil.now());

        String redisAccessTokenKey = REDIS_ASSESS_TOKEN_PREFIX + accessToken.getKey();
        hashOper.put(redisAccessTokenKey, "token", accessToken);//保存 assesstoken 到 redis
        hashOper.put(redisAccessTokenKey, "user", userDetails); //保存 userdetails 到 redis
        redisTemplate.expire(redisAccessTokenKey, accessToken.getExpires(), TimeUnit.SECONDS);

        //保存 refreshtoken 到 redis
        String redisRefreshTokenKey = REDIS_REFRESH_TOKEN_PREFIX + accessToken.getKey();
        hashOper.put(redisRefreshTokenKey, "token", accessToken);
        hashOper.put(redisRefreshTokenKey, "user", userDetails);
        redisTemplate.expire(redisRefreshTokenKey, accessToken.getReExpires(), TimeUnit.SECONDS);

        //缓存 token 记录 . 避免重复生成 token 的问题
        String key = apiKey.getKey() + userDetails.getScope() + userDetails.getId();
        Set<String> tokens = setOper.members(key);
        for(String _token : tokens){
            redisTemplate.delete(_token);
        }
        setOper.add(key, redisAccessTokenKey, redisRefreshTokenKey);

        return new TokenResponse(accessToken);
    }

    private void retrieveUser(OAuthUserDetails userDetails, OAuthUserDetails user) {
        userDetails.setId(user.getId());
        userDetails.setUsername(user.getUsername());
        userDetails.setUserType(user.getUserType());
        userDetails.setScope(user.getScope());
        userDetails.setNickName(user.getNickName());
    }

    private void retrieveUser(OAuthUserDetails userDetails, ApiKey apiKey) {
        userDetails.setId(0L);
        userDetails.setUsername(apiKey.getKey());
        userDetails.setUserType(null);
        userDetails.setScope(Scope.apiKey);
        userDetails.setNickName(apiKey.getDescription());
    }

    private void retrieveUser(OAuthUserDetails userDetails, User user) {
        userDetails.setId(user.getId());
        userDetails.setUsername(user.getUsername());
        userDetails.setUserType(user.getUserType());
        userDetails.setScope(Scope.user);
        userDetails.setNickName(user.getNickName());
    }

    private void retrieveUser(OAuthUserDetails userDetails, Member member) {
        userDetails.setId(member.getId());
        userDetails.setUsername(member.getUsername());
        userDetails.setUserType(member.getMemberType());
        userDetails.setScope(Scope.member);
        userDetails.setNickName(member.getNickName());
    }

}
