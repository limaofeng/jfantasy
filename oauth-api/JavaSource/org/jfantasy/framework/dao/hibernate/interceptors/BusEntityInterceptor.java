package org.jfantasy.framework.dao.hibernate.interceptors;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体公共属性，自动填充拦截器
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-10-28 下午08:07:30
 */
public class BusEntityInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = -3073628114943223153L;

    /**
     * 默认编辑人
     */
    private String defaultModifier = "unknown";
    /**
     * 默认创建人
     */
    private String defaultCreator = "unknown";

    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        if (entity instanceof BaseBusEntity) {
            String modifier = defaultModifier;
            UserDetails userDetails = SpringSecurityUtils.getCurrentUser();
            if (ObjectUtil.isNotNull(userDetails)) {
                modifier = userDetails.getUsername();
            }
            int count = 0;
            for (int i = 0; i < propertyNames.length; i++) {
                if ("modifier".equals(propertyNames[i])) {
                    currentState[i] = modifier;
                    count++;
                } else if ("modifyTime".equals(propertyNames[i])) {
                    currentState[i] = DateUtil.now();
                    count++;
                }
                if (count >= 2) {
                    return true;
                }
            }
        }
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (entity instanceof BaseBusEntity) {
            String creator = defaultCreator;
            Date now = DateUtil.now();
            UserDetails userDetails = SpringSecurityUtils.getCurrentUser();
            if (ObjectUtil.isNotNull(userDetails)) {
                creator = userDetails.getUsername();
            } else if (StringUtil.isNotBlank(((BaseBusEntity) entity).getCreator())) {
                creator = ((BaseBusEntity) entity).getCreator();
            }
            int count = 0;
            for (int i = 0; i < propertyNames.length; i++) {
                if ("creator".equals(propertyNames[i])) {
                    state[i] = creator;
                    count++;
                } else if ("createTime".equals(propertyNames[i])) {
                    state[i] = now;
                    count++;
                } else if ("modifier".equals(propertyNames[i])) {
                    state[i] = creator;
                    count++;
                } else if ("modifyTime".equals(propertyNames[i])) {
                    state[i] = now;
                    count++;
                }
                if (count >= 4) {
                    return true;
                }
            }
        }
        return super.onSave(entity, id, state, propertyNames, types);
    }

    public void setDefaultModifier(String defaultModifier) {
        this.defaultModifier = defaultModifier;
    }

    public void setDefaultCreator(String defaultCreator) {
        this.defaultCreator = defaultCreator;
    }

}