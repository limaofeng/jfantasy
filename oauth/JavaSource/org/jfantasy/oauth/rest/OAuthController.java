package org.jfantasy.oauth.rest;

import org.jfantasy.oauth.service.AccessTokenService;
import org.jfantasy.oauth.service.vo.TokenRequest;
import org.jfantasy.oauth.service.vo.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    @Autowired
    private AccessTokenService accessTokenService;

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public @ResponseBody TokenResponse token(@RequestBody TokenRequest request) {
        //0/a66002334cd88838b5f06633b26b738b
        //0/e6db1baa29d3df1eb307ff6a12c778da
        System.out.println(DigestUtils.md5DigestAsHex("12312312312".getBytes()));
        return accessTokenService.allocateToken(request);
    }

    @RequestMapping(value = "/authorize", method = RequestMethod.POST)
    public void authorize() {

    }

    @RequestMapping(value = "/deauthorize", method = RequestMethod.POST)
    public void deauthorize() {

    }

}
