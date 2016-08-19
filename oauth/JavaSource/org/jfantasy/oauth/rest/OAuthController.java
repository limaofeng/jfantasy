package org.jfantasy.oauth.rest;

import org.jfantasy.oauth.bean.enums.GrantType;
import org.jfantasy.oauth.service.AccessTokenService;
import org.jfantasy.oauth.service.vo.TokenRequest;
import org.jfantasy.oauth.service.vo.TokenResponse;
import org.jfantasy.oauth.userdetails.OAuthUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    private final AccessTokenService accessTokenService;

    @Autowired
    public OAuthController(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    @RequestMapping(value = "/user-details/{token}", method = RequestMethod.POST)
    @ResponseBody
    public OAuthUserDetails details(@PathVariable("token") String token) {
        return accessTokenService.details(token);
    }

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    @ResponseBody
    public TokenResponse token(@RequestBody TokenRequest request) {
        return accessTokenService.allocateToken(request);
    }

    @RequestMapping(value = "/access_token", method = RequestMethod.POST)
    @ResponseBody
    public TokenResponse accessToken(@RequestParam("grant_type") GrantType grantType, @RequestParam("scope") String scope, HttpServletRequest request) {
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setGrantType(grantType);
        tokenRequest.setScope(scope);
        return null;
    }

    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
    public void authorize() {
    }

    @RequestMapping(value = "/deauthorize", method = RequestMethod.POST)
    public void deauthorize() {

    }

}
