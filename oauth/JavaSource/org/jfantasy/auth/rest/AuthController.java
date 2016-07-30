package org.jfantasy.auth.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.auth.rest.models.LoginForm;
import org.jfantasy.auth.rest.models.LogoutForm;
import org.jfantasy.auth.rest.models.Scope;
import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.rest.MemberController;
import org.jfantasy.member.service.MemberService;
import org.jfantasy.security.bean.User;
import org.jfantasy.security.rest.UserController;
import org.jfantasy.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用于处理登陆及退出
 */
@Api(value = "auth", description = "用户登录退出")
@RestController
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "用户登录", notes = "用户登录接口")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResultResourceSupport login(@Validated(RESTful.POST.class) @RequestBody LoginForm loginForm) {
        if (StringUtil.isBlank(loginForm.getUserType())) {
            loginForm.setUserType(Scope.member == loginForm.getScope() ? Member.MEMBER_TYPE_PERSONAL : null);
        }
        switch (loginForm.getScope()) {
            case user:
                User user = this.userService.login(loginForm.getUsername(), loginForm.getPassword());
                if (StringUtil.isNotBlank(loginForm.getUserType()) && !loginForm.getUserType().equals(user.getUserType())) {
                    throw new RestException("UserType 不一致");
                }
                return UserController.assembler.toResource(user);
            case member:
                Member member = memberService.login(loginForm.getUsername(), loginForm.getPassword());
                if (StringUtil.isNotBlank(loginForm.getUserType()) && !loginForm.getUserType().equals(member.getType())) {
                    throw new RestException("UserType 不一致");
                }
                return MemberController.assembler.toResource(member);
            default:
                throw new RestException("不能识别的 scope 类型");
        }
    }

    @ApiOperation(value = "用户登出", notes = "用户登出接口")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestBody LogoutForm loginForm) {
        switch (loginForm.getScope()) {
            case user:
                this.userService.logout(loginForm.getUsername());
                break;
            case member:
                memberService.logout(loginForm.getUsername());
                break;
            default:
                throw new RestException("不能识别的 scope 类型");
        }
    }

}
