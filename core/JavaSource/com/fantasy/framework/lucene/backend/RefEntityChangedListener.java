package com.fantasy.framework.lucene.backend;

import com.fantasy.framework.lucene.BuguIndex;
import com.fantasy.framework.lucene.annotations.IndexRef;
import com.fantasy.framework.lucene.annotations.IndexRefList;
import com.fantasy.framework.lucene.cache.DaoCache;
import com.fantasy.framework.lucene.cache.FieldsCache;
import com.fantasy.framework.lucene.dao.LuceneDao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

public class RefEntityChangedListener {
	private Set<Class<?>> refBySet;

	public RefEntityChangedListener(Set<Class<?>> refBySet) {
		this.refBySet = refBySet;
	}

	public void entityChange(Class<?> refClass, String id) {
		for (Class<?> cls : this.refBySet) {
			Field[] fields = FieldsCache.getInstance().get(cls);
			for (Field f : fields){
                processField(refClass, id, cls, f);
            }
		}
	}

	private void processField(Class<?> refClass, String id, Class<?> cls, Field f) {
		boolean match = false;
		String fieldName = f.getName();
		if (f.getType().equals(refClass) && (f.getAnnotation(IndexRef.class) != null)) {
			match = true;
		} else {
			if (f.getAnnotation(IndexRefList.class) != null) {
				Class<?> c;
				Class<?> type = f.getType();
				if (type.isArray()) {
					c = type.getComponentType();
				} else {
					ParameterizedType paramType = (ParameterizedType) f.getGenericType();
					Type[] types = paramType.getActualTypeArguments();
					if (types.length == 1){
                        c = (Class<?>) types[0];
                    }else {
                        c = (Class<?>) types[1];
                    }
				}
				if ((c != null) && (c.equals(refClass))) {
					match = true;
				}
			}
		}
		if (match) {
			LuceneDao<?> dao = DaoCache.getInstance().get(cls);
			// Object refObj = ReferenceUtil.toDbReference(cls, fieldName, refClass, id);
			// DBObject query = new BasicDBObject(fieldName, refObj);
			if(dao!=null){
				List<?> list = dao.findByField(fieldName, id);
				for (Object o : list) {
					IndexUpdateTask task = new IndexUpdateTask(o);
					BuguIndex.getInstance().getExecutor().execute(task);
				}
			}
		}

	}
}