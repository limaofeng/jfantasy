package com.fantasy.weixin.ws.server;

import com.fantasy.weixin.ws.IUserInfoService;
import com.fantasy.weixin.ws.dto.GroupDTO;
import com.fantasy.weixin.ws.dto.UserInfoDTO;
import com.fantasy.wx.bean.Group;
import com.fantasy.wx.bean.UserInfo;
import com.fantasy.wx.service.UserInfoWeiXinService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by zzzhong on 2015/1/6.
 */
@Component
public class UserInfoWeiXinWebService implements IUserInfoService {
    @Autowired
    public UserInfoWeiXinService userInfoWeiXinService;
    @Override
    public UserInfoDTO getUserInfo(String openId) {
        return transUser(userInfoWeiXinService.getUserInfo(openId));
    }


    @Override
    public UserInfoDTO authUserInfo(String code) {
        return transUser(userInfoWeiXinService.authUserInfo(code));
    }

    public UserInfoDTO transUser(UserInfo u){
        if (u == null) {
            return null;
        }
        UserInfoDTO user = new UserInfoDTO();
        user.setOpenId(u.getOpenId());
        user.setAvatar(u.getAvatar());
        user.setCity(u.getCity());
        user.setCountry(u.getCountry());
        user.setProvince(u.getProvince());
        user.setLanguage(u.getLanguage());
        user.setNickname(u.getNickname());
        user.setSex(u.getSex());
        user.setSubscribe(u.getSubscribe());
        user.setSubscribeTime(u.getSubscribeTime());
        user.setUnionId(u.getUnionId());
        if(u.getGroup()!=null){
            user.setGroup(transGroup(u.getGroup()));
        }
        return user;
    }

    public GroupDTO transGroup(Group g){
        if (g == null) {
            return null;
        }
        GroupDTO groupDTO=new GroupDTO();
        groupDTO.setCount(g.getCount());
        groupDTO.setId(g.getId());
        groupDTO.setName(g.getName());
        return groupDTO;
    }
}
