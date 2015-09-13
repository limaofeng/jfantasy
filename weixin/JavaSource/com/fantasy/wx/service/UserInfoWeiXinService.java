package com.fantasy.wx.service;

import com.fantasy.file.service.FileUploadService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.member.service.MemberService;
import com.fantasy.wx.bean.UserInfo;
import com.fantasy.wx.dao.UserInfoDao;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.factory.WeiXinSessionUtils;
import com.fantasy.wx.framework.message.user.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
@Transactional
public class UserInfoWeiXinService implements InitializingBean {

    private final static Log LOG = LogFactory.getLog(UserInfoWeiXinService.class);

    @Autowired
    private UserInfoDao userInfoDao;
    @Resource
    private MemberService memberService;
    @Resource
    private transient FileUploadService fileUploadService;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public UserInfo getUserInfo(String openId) {
        return userInfoDao.findUniqueBy("openId", openId);
    }

    public UserInfo save(UserInfo ui) {
        return userInfoDao.save(ui);
    }

    public void delete(Long... ids) {
        for (Long id : ids) {
            userInfoDao.delete(id);
        }
    }

    public void deleteByOpenId(String openId) {
        UserInfo ui = this.getUserInfo(openId);
        this.delete(ui.getId());
    }

    public Pager<UserInfo> findPager(Pager<UserInfo> pager, List<PropertyFilter> filters) {
        return this.userInfoDao.findPager(pager, filters);
    }

    /**
     * 刷新所有的用户信息
     *
     * @throws WeiXinException
     */
    public void refresh() throws WeiXinException {
        List<User> list = WeiXinSessionUtils.getCurrentSession().getUsers();
        for (User u : list) {
            UserInfo ui = this.getUserInfo(u.getOpenId());
            UserInfo unew = transfiguration(u);
            if (ui != null) unew.setId(ui.getId());
            this.userInfoDao.save(unew);
        }
    }

    /**
     * 通过openId刷新用户信息
     */
    private UserInfo refresh(String openId) throws WeiXinException {
        UserInfo ui = getUserInfo(openId);
        if (ui != null) {
            return ui;
        }
        User user = WeiXinSessionUtils.getCurrentSession().getUser(openId);
        if (user == null) {
            return null;
        }
        ui = transfiguration(user);
        this.userInfoDao.save(ui);
        return ui;
    }


    /**
     * 设置用户列表的未读信息数量
     *
     * @param list
     */
    public void countUnReadSize(List<UserInfo> list) {
        for (UserInfo u : list) {
            setUnReadSize(u);
        }
    }

    /**
     * 设置用户的未读信息数量
     *
     * @param u
     */
    public void setUnReadSize(UserInfo u) {
        List query = userInfoDao.createSQLQuery("SELECT COUNT(*) c FROM wx_message WHERE create_time>? and openid=?", u.getLastLookTime(), u.getOpenId()).list();
        if (query.size() != 0) {
            u.setUnReadSize(Integer.parseInt(query.get(0).toString()));
        }
    }

    /**
     * 刷新最后查看事件
     *
     * @param ui
     */
    public void refreshMessage(UserInfo ui) {
        userInfoDao.batchSQLExecute("update wx_user_info set last_look_time=last_message_time  where openid=?", ui.getOpenId());
        ui.setLastLookTime(ui.getLastMessageTime());
    }

    /**
     * 转换user到userinfo
     *
     * @param u
     * @return
     */
    public UserInfo transfiguration(User u) {
        if (u == null) {
            return null;
        }
        UserInfo user = new UserInfo();
        user.setOpenId(u.getOpenId());
        user.setAvatar(u.getAvatar());
        user.setCity(u.getCity());
        user.setCountry(u.getCountry());
        user.setProvince(u.getProvince());
        user.setLanguage(u.getLanguage());
        user.setNickname(u.getNickname());
        user.setSex(u.getSex());
        user.setSubscribe(u.isSubscribe());
        if (u.getSubscribeTime() != null) {
            user.setSubscribeTime(u.getSubscribeTime().getTime());
        }
        user.setUnionId(u.getUnionid());
        return user;
    }

    public UserInfo checkCreateMember(String openId) throws WeiXinException {
        return refresh(openId);
        /* TODO 关注微信号时,是否自动创建会员记录
        if (ui != null) {
            String bex64OpenId = StringUtil.hexTo64(MessageDigestUtil.getInstance().get(ui.getOpenId()));
            Member member = new Member();
            member.setUsername(bex64OpenId);
            member.setPassword("123456");
            member.setNickName(ui.getNickname());

            member.setEnabled(true);
            member.setAccountNonExpired(false);
            member.setCredentialsNonExpired(false);
            member.setAccountNonLocked(false);

            MemberDetails details = member.getDetails();
            if (details == null) {
                details = new MemberDetails();
            }
            if (StringUtil.isBlank(details.getScore())) {
                details.setScore(0);
            }
            //会员头像
            File file = null;
            try {
                file = FileUtil.tmp();//临时文件
                HttpClientUtil.doGet(ui.getAvatar()).writeFile(file);
                String mimeType = FileUtil.getMimeType(file);
                String fileName = file.getName() + "." + mimeType.replace("image/", "");
                FileDetail fileDetail = fileUploadService.upload(file, mimeType, fileName, "avatar");
                LOG.debug("头像上传成功:" + fileDetail);
                details.setAvatarStore(JSON.serialize(new FileDetail[]{fileDetail}));
            } catch (FileNotFoundException e) {
                LOG.error(e.getMessage(), e);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            } finally {
                if (file != null) {
                    FileUtil.delFile(file);//删除临时文件
                }
            }
            member.setDetails(details);
            memberService.save(member);
            ui.setMember(member);
            save(ui);
            return ui;
        }
        return ui;
        */
    }

    public UserInfo get(Long id) {
        return this.userInfoDao.get(id);
    }
}
