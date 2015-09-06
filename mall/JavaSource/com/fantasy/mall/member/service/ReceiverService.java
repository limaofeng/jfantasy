package com.fantasy.mall.member.service;

import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.mall.member.bean.Receiver;
import com.fantasy.mall.member.dao.ReceiverDao;
import com.fantasy.member.bean.Member;
import com.fantasy.member.userdetails.MemberUser;
import com.fantasy.security.SpringSecurityUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReceiverService {

    @Autowired
    private ReceiverDao receiverDao;

    public Receiver save(Receiver receiver) {
        int count = this.receiverDao.count(Restrictions.eq("member.id", receiver.getMember().getId()));
        if (count == 0) {
            receiver.setIsDefault(true);
        } else {
            List<Receiver> receivers = this.receiverDao.find(Restrictions.eq("member.id", receiver.getMember().getId()), Restrictions.eq("isDefault", true));
            if (ObjectUtil.defaultValue(receiver.getIsDefault(), false)) {
                for (Receiver ver : receivers) {
                    ver.setIsDefault(false);
                    receiverDao.save(ver);
                }
            }
        }
        return receiverDao.save(receiver);
    }

    public List<Receiver> find(Criterion[] criterions, String orderBy, String order) {
        return this.receiverDao.find(criterions, orderBy, order);
    }

    public List<Receiver> find(List<PropertyFilter> filters, String orderBy, String order) {
        return this.receiverDao.find(filters, orderBy, order);
    }

    public Receiver get(Long id) {
        return this.receiverDao.get(id);
    }

    public void deltele(Long id) {
        Receiver receiver = get(id);
        Member member = SpringSecurityUtils.getCurrentUser(MemberUser.class).getUser();
        List<Receiver> receivers = this.receiverDao.find(new Criterion[]{Restrictions.eq("member.id", member.getId())}, "isDefault", "desc");
        this.receiverDao.delete(receiver);
        if (receivers.size() == 1) {
            receivers.get(0).setIsDefault(true);
            this.receiverDao.save(receivers.get(0));
        }
    }

}
