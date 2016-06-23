package org.jfantasy.oauth.boot;

import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.jfantasy.oauth.PermissionRule;
import org.jfantasy.oauth.UrlResource;
import org.jfantasy.security.bean.Permission;
import org.jfantasy.security.bean.Resource;
import org.jfantasy.security.data.SecurityStorage;
import org.jfantasy.security.service.PermissionService;
import org.jfantasy.security.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 负责在应用启动时,将权限记录写入 redis 缓存中
 */
@Component
public class PermissionConfiguration implements CommandLineRunner {

    @Autowired
    private RedisTemplate<String, UrlResource> redisTemplate;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public void run(String... args) throws Exception {
        ValueOperations<String, UrlResource> valueOper = redisTemplate.opsForValue();
        SetOperations setOper = redisTemplate.opsForSet();

        List<String> des = new ArrayList(setOper.members(SecurityStorage.RESOURCE_IDS));
        des.add(SecurityStorage.RESOURCE_IDS);
        redisTemplate.delete(des);

        for (Resource resource : resourceService.find(Restrictions.eq("enabled", Boolean.TRUE))) {
            UrlResource urlResource = new UrlResource();
            urlResource.setId(resource.getId());

            List<Permission> permissions = permissionService.find(Restrictions.eq("resource.id",resource.getId()),Restrictions.eq("enabled", Boolean.TRUE));

            for (String _rules : StringUtil.tokenizeToStringArray(resource.getValue(), "\n")) {
                PermissionRule rule = new PermissionRule();
                rule.setId(_rules.substring(0, _rules.indexOf(" ")));
                String[] arrays = StringUtil.tokenizeToStringArray(_rules.substring(_rules.indexOf(" ") + 1), ":");
                rule.setMethod(arrays[0]);
                rule.setPattern(arrays[1]);

                for (Permission permission : permissions) {
                    for (String _prules : StringUtil.tokenizeToStringArray(permission.getValue(), "\n")) {
                        String[] ids = StringUtil.tokenizeToStringArray(_prules.substring(0, _prules.indexOf(":")), ",");
                        if (ObjectUtil.exists(ids, rule.getId()) || (ids.length == 1 && "*".equals(ids[0]))) {
                            for (RegexpUtil.Group group : RegexpUtil.parseGroups(_prules.substring(_prules.indexOf(":") + 1), "(role|group|ip)\\((\\S+)\\)")) {
                                for (String pl : StringUtil.tokenizeToStringArray(group.$(2), ",")) {
                                    rule.addAccess(group.$(1).toLowerCase() + "_" + pl);
                                }
                            }
                        }
                    }
                }

                urlResource.addRule(rule);
            }
            valueOper.set(SecurityStorage.RESOURCE_PREFIX + urlResource.getId(), urlResource);
            setOper.add(SecurityStorage.RESOURCE_IDS, SecurityStorage.RESOURCE_PREFIX + urlResource.getId());
        }

    }

}
