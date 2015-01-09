package com.fantasy.security.service;

import com.fantasy.framework.cache.Cache;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.security.bean.Resource;
import com.fantasy.security.bean.enums.ResourceType;
import com.fantasy.security.dao.ResourceDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("fantasy.auth.ResourceService")
@Transactional
public class ResourceService {

	public static final String RESOURCE_ALL_CACHE_KEY = "RESOURCE_ALL_CACHE_KEY";

	public static final String RESOURCE_TREE_CACHE_KEY = "RESOURCE_TREE_CACHE_KEY";

	@javax.annotation.Resource(name = "defaultCache")
	private Cache cache;

	@javax.annotation.Resource
	private ResourceDao resourceDao;
	
	@javax.annotation.Resource
	private UserGroupService userGroupService;

	/**
	 * 搜索
	 * @param pager 翻页对象
	 * @param filters 筛选条件
	 * @return {Pager}
	 */
	public Pager<Resource> search(Pager<Resource> pager,List<PropertyFilter> filters){
		return this.resourceDao.findPager(pager, filters);
	}

	public void save(Resource resource) {
		this.resourceDao.save(resource);
        cache.removeObject(RESOURCE_TREE_CACHE_KEY);
        cache.removeObject(RESOURCE_ALL_CACHE_KEY);
    }
	
	public Resource get(Long id){
		return this.resourceDao.get(id);
	}
	
	public void delete(Long[] ids){
		for(Long id:ids){
			this.resourceDao.delete(id);
		}
        cache.removeObject(RESOURCE_TREE_CACHE_KEY);
        cache.removeObject(RESOURCE_ALL_CACHE_KEY);
	}

	@SuppressWarnings("unchecked")
    @Transactional(readOnly = true,propagation = Propagation.NOT_SUPPORTED)
	public List<Resource> getTreeResource() {
		Object data = cache.getObject(RESOURCE_TREE_CACHE_KEY);
		if (ObjectUtil.isNull(data)) {
			data = cache.getObject(RESOURCE_TREE_CACHE_KEY);
			if (ObjectUtil.isNull(data)) {
				List<Resource> resources = new ArrayList<Resource>();
                for(Resource resource : resourceDao.getAll()){
                    resources.add(resource.clone());
                }
				Map<Long, Resource> resourceMap = new HashMap<Long, Resource>();
				for (Resource resource : resources) {
					resourceMap.put(resource.getId(), resource);
				}
				Map<Long, List<Long>> relations = getRelations();
				// 被引用的资源
				List<Long> reIds = new ArrayList<Long>();
				for (Resource resource : resources) {
					if (relations.containsKey(resource.getId())) {
						for (Long resourceId : relations.get(resource.getId())) {
							resource.addResource(resourceMap.get(resourceId));
							if (reIds.indexOf(resourceId) == -1) {
								reIds.add(resourceId);
							}
						}
					}
					// 移除非GROUP资源
					if (!ResourceType.group.equals(resource.getType()) && reIds.indexOf(resource.getId()) == -1) {
							reIds.add(resource.getId());
					}
				}
				// 移除被引用的资源
				for (Long reid : reIds) {
					if (resourceMap.containsKey(reid)) {
						resourceMap.remove(reid);
					}
				}
				data = ObjectUtil.sort(new ArrayList<Resource>(resourceMap.values()), "id");
				cache.putObject(RESOURCE_TREE_CACHE_KEY, data);
			}
		}
		return (List<Resource>) data;
	}

	public Map<String, String> loadUrlAuthorities() {
		Map<String, String> urlAuthorities = new HashMap<String, String>();
		for (Resource resource : loadResourcesByUrl()) {
			if (!resource.isEnabled()){
                continue;
            }
			if (urlAuthorities.containsKey(resource.getValue())){
                continue;
            }
			if (StringUtils.isNotBlank(resource.getAuthorities())){
                urlAuthorities.put(resource.getValue(), resource.getAuthorities());
            }
		}
		return urlAuthorities;
	}

	public List<Resource> loadResourcesByUrl() {
		List<Resource> resources = new ArrayList<Resource>();
		for (Resource resource : getAllResource().values()) {
//			if (ResourceType.url.equals(resource.getType()))
				resources.add(resource);
		}
		return resources;
	}

	@SuppressWarnings("unchecked")
	public Map<Long, Resource> getAllResource() {
		Object data = cache.getObject(RESOURCE_ALL_CACHE_KEY);
		if (ObjectUtil.isNull(data)) {
			List<Resource> resources = new ArrayList<Resource>();
            for(Resource resource : resourceDao.getAll()){
            	Resource newRes = resource.clone();
            	newRes.setUserGroups(resource.getUserGroups());
            	newRes.setRoles(resource.getRoles());
                resources.add(newRes);
            }
			Map<Long, Resource> resourceMap = new HashMap<Long, Resource>();
			for (Resource resource : resources) {
				resourceMap.put(resource.getId(), resource);
			}
			Map<Long, List<Long>> relations = getRelations();
			for (Resource resource : resources) {
				// 组合资源的上下级关系
				if (relations.containsKey(resource.getId())) {
					for (Long resourceId : relations.get(resource.getId())) {
						resource.addResource(resourceMap.get(resourceId));
					}
				}
				// 组合资源与用户组的关系
				resource.setUserGroups(userGroupService.getUserGroupsByResourceId(resource.getId()));
			}
			data = resourceMap;
			cache.putObject(RESOURCE_ALL_CACHE_KEY, data);
		}
		return (Map<Long, Resource>) data;
	}

	public Map<Long, List<Long>> getRelations() {
		return this.resourceDao.getRelations();
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

}