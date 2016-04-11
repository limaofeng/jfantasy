package org.jfantasy.wx.service;

import org.jfantasy.filestore.service.FileUploadService;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.member.service.MemberService;
import org.jfantasy.wx.bean.User;
import org.jfantasy.wx.bean.UserKey;
import org.jfantasy.wx.dao.UserDao;
import org.jfantasy.wx.framework.exception.WeiXinException;
import org.jfantasy.wx.framework.factory.WeiXinSessionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service("wx.userService")
@Transactional
public class UserService implements InitializingBean {

    private final static Log LOG = LogFactory.getLog(UserService.class);

    @Autowired
    private UserDao userDao;
    @Resource
    private MemberService memberService;
    @Resource
    private transient FileUploadService fileUploadService;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public User get(UserKey key) {
        return userDao.get(key);
    }

    public User save(User ui) {
        return userDao.save(ui);
    }

    public void delete(UserKey... keys) {
        for (UserKey key : keys) {
            userDao.delete(key);
        }
    }

    public void delete(UserKey key) {
        this.userDao.delete(key);
    }

    public Pager<User> findPager(Pager<User> pager, List<PropertyFilter> filters) {
        return this.userDao.findPager(pager, filters);
    }

    /**
     * 刷新所有的用户信息
     *
     * @throws WeiXinException
     */
    public void refresh() throws WeiXinException {
        List<org.jfantasy.wx.framework.message.user.User> list = WeiXinSessionUtils.getCurrentSession().getUsers();
        for (org.jfantasy.wx.framework.message.user.User u : list) {
//            User ui = this.get(u.getOpenId());
//            User unew = transfiguration(u);
//            if (ui != null) unew.setId(ui.getId());
//            this.userInfoDao.save(unew);
        }
    }

    /**
     * 通过openId刷新用户信息
     */
    private User refresh(String appid,String openId) throws WeiXinException {
        User ui = get(UserKey.newInstance(appid,openId));
        if (ui != null) {
            return ui;
        }
        org.jfantasy.wx.framework.message.user.User user = WeiXinSessionUtils.getCurrentSession().getUser(openId);
        if (user == null) {
            return null;
        }
        ui = transfiguration(user);
        ui.setAppId(appid);
        this.userDao.save(ui);
        return ui;
    }


    /**
     * 设置用户列表的未读信息数量
     *
     * @param list
     */
    public void countUnReadSize(List<User> list) {
        for (User u : list) {
            setUnReadSize(u);
        }
    }

    /**
     * 设置用户的未读信息数量
     *
     * @param u
     */
    public void setUnReadSize(User u) {
        List query = userDao.createSQLQuery("SELECT COUNT(*) c FROM wx_message WHERE create_time>? and openid=?", u.getLastLookTime(), u.getOpenId()).list();
        if (query.size() != 0) {
            u.setUnReadSize(Integer.parseInt(query.get(0).toString()));
        }
    }

    /**
     * 刷新最后查看事件
     *
     * @param ui
     */
    public void refreshMessage(User ui) {
        userDao.batchSQLExecute("update wx_user_info set last_look_time=last_message_time  where openid=?", ui.getOpenId());
        ui.setLastLookTime(ui.getLastMessageTime());
    }

    /**
     * 转换user到userinfo
     *
     * @param u
     * @return
     */
    public User transfiguration(org.jfantasy.wx.framework.message.user.User u) {
        if (u == null) {
            return null;
        }
        User user = new User();
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

    public User findUnique(Criterion... criterions) {
        return this.userDao.findUnique(criterions);
    }

    public User checkCreateMember(String appid,String openId) throws WeiXinException {
        return refresh(appid,openId);
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

}
