package org.jfantasy.oauth.rest;

import org.jfantasy.oauth.service.AccessTokenService;
import org.jfantasy.oauth.service.vo.TokenRequest;
import org.jfantasy.oauth.service.vo.TokenResponse;
import org.jfantasy.oauth.userdetails.OAuthUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    @Autowired
    private AccessTokenService accessTokenService;

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

    @RequestMapping(value = "/authorize", method = RequestMethod.POST)
    public void authorize() {

    }

    @RequestMapping(value = "/deauthorize", method = RequestMethod.POST)
    public void deauthorize() {

    }

}
