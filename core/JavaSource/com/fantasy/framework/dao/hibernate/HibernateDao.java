package com.fantasy.framework.dao.hibernate;

import com.fantasy.attr.framework.DynaBean;
import com.fantasy.attr.framework.query.DynaBeanQueryManager;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.util.ReflectionUtils;
import com.fantasy.framework.dao.hibernate.util.TypeFactory;
import com.fantasy.framework.error.IgnoreException;
import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.*;
import org.hibernate.Query;
import org.hibernate.criterion.*;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.CriteriaImpl.OrderEntry;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Blob;
import java.util.*;
import java.util.regex.Matcher;

/**
 * hibernate Dao 的默认实现
 *
 * @param <T>
 * @param <PK>
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-11 下午4:17:39
 */
public abstract class HibernateDao<T, PK extends Serializable> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected SessionFactory sessionFactory;
    protected Class<T> entityClass;

    public HibernateDao() {
        this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 获取当前session
     *
     * @return 返回 session
     */
    protected Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    /**
     * 保存对象 实际调用的是 saveOrUpdate
     *
     * @param entity 保存的对象
     */
    public void save(T entity) {
        Assert.notNull(entity, "entity不能为空");
        try {
            getSession().saveOrUpdate(clean(entity));
        } catch (NonUniqueObjectException e) {
            getSession().merge(entity);
        }
        this.logger.debug("save entity: {}", entity);
    }

    /**
     * 更新对象
     *
     * @param entity 要更新的对象
     */
    public void update(T entity) {
        Assert.notNull(entity, "entity不能为空");
        try {
            getSession().update(entity);
        } catch (NonUniqueObjectException e) {
            getSession().merge(entity);
        }
        this.logger.debug("update entity: {}", entity);
    }

    /**
     * 合并对象
     *
     * @param entity 要合并的对象
     */
    public void merge(T entity) {
        Assert.notNull(entity, "entity不能为空");
        getSession().merge(entity);
        this.logger.debug("update entity: {}", entity);
    }

    /**
     * 待测试方法
     *
     * @param m      xxx
     * @param entity 要合并的对象
     */
    @Deprecated
    public void merge(String m, T entity) {
        Assert.notNull(entity, "entity不能为空");
        getSession().merge(m, entity);
        this.logger.debug("update entity: {}", entity);
    }

    /**
     * 把一个瞬态的实例持久化
     * <br/>
     * 1，persist把一个瞬态的实例持久化，但是并"不保证"标识符被立刻填入到持久化实例中，标识符的填入可能被推迟 到flush的时间。 <br/>
     * 2，persist"保证"，当它在一个transaction外部被调用的时候并不触发一个Sql Insert，这个功能是很有用的， 当我们通过继承Session/persistence context来封装一个长会话流程的时候，一个persist这样的函数是需要的。 <br/>
     * 3，save"不保证"第2条,它要返回标识符，所以它会立即执行Sql insert，不管是不是在transaction内部还是外部
     *
     * @param entity 需要持久化的对象
     */
    public void persist(T entity) {
        Assert.notNull(entity, "entity不能为空");
        getSession().persist(clean(entity));
    }

    /**
     * 自动将游离对象转为持久化对象
     * 需要实现的主要功能 <br/>
     * 1.合并对象<br/>
     * 2.游离对象创建<br/>
     * 3.游离对象自动转持久化对象<br/>
     *
     * @param entity 清理对象
     * @return <T>
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private T clean(T entity) {
        OgnlUtil ognlUtil = OgnlUtil.getInstance();
        Serializable id = getIdValue(this.entityClass, entity);
        T oldEntity = null;
        if (ObjectUtil.isNotNull(id)) {
            oldEntity = (T) getSession().get(this.entityClass, id);
        }// 添加新对象时
        if (entity == oldEntity) {
            return entity;
        }
        if (ObjectUtil.isNotNull(oldEntity)) {// 主键对应的数据存在
            // 为普通字段做值转换操作
            Field[] fields = ClassUtil.getDeclaredFields(this.entityClass, Column.class);
            for (Field field : fields) {
                String getterMethodName = (boolean.class.equals(field.getType()) ? "is" : "get") + StringUtils.capitalize(field.getName()) + "()";
                Object value = ognlUtil.getValue(getterMethodName, entity);
                if (value != null) {
                    ClassUtil.setValue(oldEntity, field.getName(), value);
                }
            }
            // 一对一关联关系的表
            fields = ClassUtil.getDeclaredFields(this.entityClass, OneToOne.class);
            for (Field field : fields) {
                OneToOne oneToOne = field.getAnnotation(OneToOne.class);
                if (!(ObjectUtil.indexOf(oneToOne.cascade(), CascadeType.ALL) > -1 || ObjectUtil.indexOf(oneToOne.cascade(), CascadeType.MERGE) > -1)) {
                    continue;
                }
                Object value = ClassUtil.getValue(entity, field.getName());
                if (value != null) {
                    Object oldValue = ClassUtil.getValue(oldEntity, field.getName());
                    if (oldValue == null) {
                        ClassUtil.setValue(oldEntity, field.getName(), value);
                    } else {
                        for (Field fkField : ClassUtil.getDeclaredFields(field.getType(), Column.class)) {
                            if (!fkField.isAnnotationPresent(Id.class)) {
                                Object fkValue = ClassUtil.getValue(value, fkField.getName());
                                if (fkValue != null) {
                                    if (fkValue instanceof Blob) {
                                        ClassUtil.setValue(oldValue, fkField.getName(), fkValue);
                                    } else {
                                        ognlUtil.setValue(field.getName() + "." + fkField.getName(), oldEntity, fkValue);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // 多对一关联关系的表
            Field[] manyToOneFields = ClassUtil.getDeclaredFields(this.entityClass, ManyToOne.class);
            for (Field field : manyToOneFields) {
                Object fk = ognlUtil.getValue(field.getName(), entity);
                if (fk == null) {
                    ognlUtil.setValue(field.getName(), entity, null);
                    continue;
                }
                Serializable fkId = getIdValue(field.getType(), fk);
                Object fkObj = fkId != null ? getSession().get(field.getType(), fkId) : null;
                ognlUtil.setValue(field.getName(), oldEntity, fkObj);
            }
            // 多对多关联关系的表
            Field[] manyToManyFields = ClassUtil.getDeclaredFields(this.entityClass, ManyToMany.class);
            for (Field field : manyToManyFields) {
                ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
                Class targetEntityClass = manyToMany.targetEntity();
                if (void.class == targetEntityClass) {
                    targetEntityClass = ClassUtil.getFieldGenericType(field);
                }
                if (manyToMany.cascade().length != 0 && !(ObjectUtil.indexOf(manyToMany.cascade(), CascadeType.ALL) > -1 || ObjectUtil.indexOf(manyToMany.cascade(), CascadeType.MERGE) > -1)) {
                    continue;
                }
                Object fks = ognlUtil.getValue(field.getName(), entity);
                if (ClassUtil.isList(fks)) {
                    List<Object> objects = (List<Object>) fks;
                    List<Object> addObjects = new ArrayList<Object>();
                    for (Object fk : objects) {
                        Serializable fkId = getIdValue(targetEntityClass, fk);
                        Object fkObj = fkId != null ? getSession().get(targetEntityClass, fkId) : null;
                        if (fkObj != null) {
                            addObjects.add(fkObj);
                        }
                    }
                    ognlUtil.setValue(field.getName(), oldEntity, addObjects);
                }
            }
            // 一对多关联关系的表
            Field[] oneToManyFields = ClassUtil.getDeclaredFields(this.entityClass, OneToMany.class);
            for (Field field : oneToManyFields) {
                OneToMany oneToMany = field.getAnnotation(OneToMany.class);
                Class targetEntityClass = oneToMany.targetEntity();
                if (void.class == targetEntityClass) {
                    targetEntityClass = ClassUtil.getFieldGenericType(field);
                }
                if (oneToMany.cascade().length != 0 && !(ObjectUtil.indexOf(oneToMany.cascade(), CascadeType.ALL) > -1 || ObjectUtil.indexOf(oneToMany.cascade(), CascadeType.MERGE) > -1)) {
                    continue;
                }
                Object fks = ognlUtil.getValue(field.getName(), entity);
                if (ClassUtil.isList(fks)) {
                    List<Object> objects = (List<Object>) fks;
                    List<Object> addObjects = new ArrayList<Object>();
                    for (Object fk : objects) {
                        Serializable fkId = getIdValue(targetEntityClass, fk);
                        Object fkObj = fkId != null ? getSession().get(targetEntityClass, fkId) : null;
                        if (fkObj != null) {
                            addObjects.add(BeanUtil.copyProperties(fkObj, fk));
                        } else {
                            addObjects.add(fk);
                        }
                    }
                    List<Object> _old_fks = (List<Object>) ognlUtil.getValue(field.getName(), oldEntity);
                    ognlUtil.setValue(field.getName(), oldEntity, addObjects);
                    //删除原有数据
                    for (Object _odl : _old_fks) {
                        if (ObjectUtil.find(addObjects, this.getIdName(targetEntityClass), this.getIdValue(targetEntityClass, _odl)) == null) {
                            this.getSession().delete(_odl);
                            System.out.println("删除数据" + this.getIdValue(targetEntityClass, _odl));
                        }
                    }
                }
            }
            // 用于回显
            fields = ClassUtil.getDeclaredFields(this.entityClass);
            for (Field field : fields) {
                if (field.getAnnotation(Transient.class) != null) {
                    continue;
                }
                ClassUtil.setValue(entity, field.getName(), ClassUtil.getValue(oldEntity, field.getName()));
            }
            return oldEntity;
        } else {
            // 为外键对象提供查询功能
            Field[] manyToOneFields = ClassUtil.getDeclaredFields(this.entityClass, ManyToOne.class);
            for (Field field : manyToOneFields) {
                Object fk = ognlUtil.getValue(field.getName(), entity);
                if (fk == null) {
                    continue;
                }
                Serializable fkId = getIdValue(field.getType(), fk);
                Object fkObj = fkId != null ? getSession().get(field.getType(), fkId) : null;
                ognlUtil.setValue(field.getName(), entity, fkObj);
            }
            // 多对多关联关系的表
            Field[] manyToManyFields = ClassUtil.getDeclaredFields(this.entityClass, ManyToMany.class);
            for (Field field : manyToManyFields) {
                ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
                Class targetEntityClass = manyToMany.targetEntity();
                if (void.class == targetEntityClass) {
                    targetEntityClass = ClassUtil.getFieldGenericType(field);
                }
                Object fks = ognlUtil.getValue(field.getName(), entity);
                if (ClassUtil.isList(fks)) {
                    List<Object> objects = (List<Object>) fks;
                    List<Object> addObjects = new ArrayList<Object>();
                    for (Object fk : objects) {
                        Serializable fkId = getIdValue(targetEntityClass, fk);
                        Object fkObj = fkId != null ? getSession().get(targetEntityClass, fkId) : null;
                        if (fkObj != null) {
                            addObjects.add(fkObj);
                        }
                    }
                    ognlUtil.setValue(field.getName(), entity, addObjects);
                }
            }
            // 一对多关联关系的表
            Field[] oneToManyFields = ClassUtil.getDeclaredFields(this.entityClass, OneToMany.class);
            for (Field field : oneToManyFields) {
                OneToMany oneToMany = field.getAnnotation(OneToMany.class);
                Class targetEntityClass = oneToMany.targetEntity();
                if (void.class == targetEntityClass) {
                    targetEntityClass = ClassUtil.getFieldGenericType(field);
                }
                if (oneToMany.cascade().length != 0 && !(ObjectUtil.indexOf(oneToMany.cascade(), CascadeType.ALL) > -1 || ObjectUtil.indexOf(oneToMany.cascade(), CascadeType.MERGE) > -1)) {
                    continue;
                }
                Object fks = ognlUtil.getValue(field.getName(), entity);
                if (ClassUtil.isList(fks)) {
                    List<Object> objects = (List<Object>) fks;
                    List<Object> addObjects = new ArrayList<Object>();
                    for (Object fk : objects) {
                        Serializable fkId = getIdValue(targetEntityClass, fk);
                        Object fkObj = fkId != null ? getSession().get(targetEntityClass, fkId) : null;
                        if (fkObj != null) {
                            addObjects.add(BeanUtil.copyProperties(fkObj, fk));
                        } else {
                            addObjects.add(fk);
                        }
                    }
                    ognlUtil.setValue(field.getName(), entity, addObjects);
                }
            }
            return entity;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Serializable getIdValue(Class entityClass, Object entity) {
        OgnlUtil ognlUtil = OgnlUtil.getInstance();
        Field[] idFields = ClassUtil.getDeclaredFields(entityClass, Id.class);
        if (idFields.length == 0) {
            return null;
        }
        if (idFields.length > 1) {
            IdClass idClass = ClassUtil.getClassGenricType(entityClass, IdClass.class);
            Serializable id = ClassUtil.newInstance((Class<Serializable>) idClass.value());
            for (Field idField : idFields) {
                ognlUtil.setValue(idField.getName(), id, ognlUtil.getValue(idField.getName(), entity));
            }
            return id;
        } else {
            return (Serializable) ClassUtil.getValue(entity, idFields[0].getName());
        }
    }

    /**
     * 删除对象
     *
     * @param entity 删除的实体
     */
    public void delete(T entity) {
        Assert.notNull(entity, "entity不能为空");
        getSession().delete(entity);
        this.logger.debug("delete entity: {}", entity);
    }

    /**
     * 通过id删除对象
     *
     * @param id 通过主键删除
     */
    public void delete(PK id) {
        Assert.notNull(id, "id不能为空");
        T t = get(id);
        if (t != null) {
            delete(t);
        }
        this.logger.debug("delete entity {},id is {}", this.entityClass.getSimpleName(), id);
    }

    /**
     * get 一个对象
     *
     * @param id 根据主键查询
     * @return <T>对象
     */
    @SuppressWarnings("unchecked")
    public T get(PK id) {
        Assert.notNull(id, "id不能为空");
        return (T) getSession().get(this.entityClass, id);
    }

    /**
     * load 一个对象
     *
     * @param id 根据主键查询
     * @return <T>对象
     */
    @SuppressWarnings("unchecked")
    public T load(PK id) {
        Assert.notNull(id, "id不能为空");
        return (T) getSession().load(this.entityClass, id);
    }

    /**
     * 查询全部数据
     *
     * @return List<T>
     */
    public List<T> getAll() {
        return find();
    }

    /**
     * 查询全部数据并排序
     *
     * @param orderBy 排序字段
     * @param isAsc   排序方向
     * @return 返回集合
     */
    @SuppressWarnings("unchecked")
    public List<T> getAll(String orderBy, boolean isAsc) {
        Criteria c = createCriteria(new Criterion[0], StringUtil.tokenizeToStringArray(orderBy));
        if (isAsc) {
            c.addOrder(Order.asc(orderByAlias(orderBy)));
        } else {
            c.addOrder(Order.desc(orderByAlias(orderBy)));
        }
        return c.list();
    }

    public List<T> findBy(String propertyName, Object value) {
        Assert.hasText(propertyName, "propertyName不能为空");
        return find(Restrictions.eq(propertyName, value));
    }

    /**
     * 查询对象返回唯一值,propertyName 值必须是唯一的，value重复将会抛出异常
     *
     * @param propertyName 属性名称
     * @param value        值
     * @return 返回唯一对象
     */
    @SuppressWarnings("unchecked")
    public T findUniqueBy(String propertyName, Object value) {
        Assert.hasText(propertyName, "propertyName不能为空");
        Criterion criterion = Restrictions.eq(propertyName, value);
        return (T) createCriteria(new Criterion[]{criterion}).uniqueResult();
    }

    /**
     * 通过主键查询对象,实际采用的是 id in (...) 操作
     *
     * @param ids 主键集合
     * @return 返回对象集合
     */
    public List<T> findByIds(List<PK> ids) {
        return find(Restrictions.in(getIdName(), ids));
    }

    /**
     * 通过主键查询对象,实际采用的是 id in (...) 操作
     *
     * @param ids id数组
     * @return 返回对象集合
     */
    public List<T> findByIds(PK... ids) {
        return find(Restrictions.in(getIdName(), ids));
    }

    /**
     * 使用hql查询对象,推荐使用 {@link @HibernateDao.find(String,Map<String, ?>)}
     *
     * @param hql    hql语句
     * @param values 参数
     * @return 返回集合
     */
    @SuppressWarnings("unchecked")
    public List<T> find(String hql, Object... values) {
        return createQuery(hql, values).list();
    }

    /**
     * 使用hql查询对象
     *
     * @param hql    hql语句
     * @param values 参数
     * @return 返回集合
     */
    @SuppressWarnings("unchecked")
    public List<T> find(String hql, Map<String, ?> values) {
        return createQuery(hql, values).list();
    }

    /**
     * 使用sql查询数据
     *
     * @param sql    sql语句
     * @param values 参数
     * @return 返回集合
     */
    @SuppressWarnings("unchecked")
    public List<T> findSQL(String sql, Map<String, ?> values) {
        return distinct(createSQLQuery(sql, values), entityClass).list();
    }

    /**
     * 查询时自定义返回结果集
     *
     * @param sql         sql语句
     * @param values      参数
     * @param resultClass 返回对象类型
     * @return 返回集合
     */
    @SuppressWarnings("unchecked")
    public <E> List<E> findSQL(String sql, Map<String, ?> values, Class<E> resultClass) {
        return distinct(createSQLQuery(sql, values), resultClass).list();
    }

    @SuppressWarnings("unchecked")
    public T findUnique(String hql, Object... values) {
        return (T) createQuery(hql, values).uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public T findUnique(String hql, Map<String, ?> values) {
        return (T) createQuery(hql, values).uniqueResult();
    }

    public int batchExecute(String hql, Object... values) {
        return createQuery(hql, values).executeUpdate();
    }

    public int batchExecute(String hql, Map<String, ?> values) {
        return createQuery(hql, values).executeUpdate();
    }

    public int batchSQLExecute(String sql, Object... values) {
        return createSQLQuery(sql, values).executeUpdate();
    }

    public int batchSQLExecute(String sql, Map<String, ?> values) {
        return createSQLQuery(sql, values).executeUpdate();
    }

    public Query createQuery(String queryString, Object... values) {
        Assert.hasText("queryString不能为空", queryString);
        Query query = getSession().createQuery(queryString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query;
    }

    public Query createQuery(String queryString, Map<String, ?> values) {
        Assert.hasText(queryString, "queryString不能为空");
        Query query = getSession().createQuery(queryString);
        if (values != null) {
            query.setProperties(values);
        }
        return query;
    }

    public SQLQuery createSQLQuery(String queryString, Object... values) {
        Assert.hasText(queryString, "queryString不能为空");
        SQLQuery query = getSession().createSQLQuery(queryString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query;
    }

    public SQLQuery createSQLQuery(String queryString, Map<String, ?> values) {
        Assert.hasText(queryString, "queryString不能为空");
        SQLQuery query = getSession().createSQLQuery(queryString);
        if (values != null) {
            query.setProperties(values);
        }
        return query;
    }

    @SuppressWarnings("unchecked")
    public T findUnique(Criterion... criterions) {
        return (T) createCriteria(criterions).uniqueResult();
    }

    public DetachedCriteria createDetachedCriteria(Criterion... criterions) {
        DetachedCriteria dc = DetachedCriteria.forClass(this.entityClass);
        Map<String, DetachedCriteria> cascadeCriterions = new HashMap<String, DetachedCriteria>();
        for (Criterion c : criterions) {
            if ((c instanceof NotExpression)) {
                Criterion criterion = (Criterion) ReflectionUtils.getFieldValue(c, "criterion");
                if ((criterion instanceof InExpression)) {
                    String propertyName = (String) ReflectionUtils.getFieldValue(criterion, "propertyName");
                    if (propertyName.lastIndexOf('.') > 0) {
                        addCriterion(dc, cascadeCriterions, c, propertyName);
                    }
                }
            } else {
                String propertyName = (String) ReflectionUtils.getFieldValue(c, "propertyName");
                if (propertyName.lastIndexOf('.') > 0) {
                    addCriterion(dc, cascadeCriterions, c, propertyName);
                } else {
                    dc.add(c);
                }
            }
        }
        return dc;
    }

    private void addCriterion(DetachedCriteria dc, Map<String, DetachedCriteria> cascadeCriterions, Criterion c, String propertyName) {
        String cascadeName = propertyName.substring(0, propertyName.lastIndexOf('.'));
        if (!cascadeCriterions.containsKey(cascadeName)) {
            cascadeCriterions.put(cascadeName, createDetachedCriteria(dc, cascadeName));
        }
        ReflectionUtils.setFieldValue(c, "propertyName", propertyName.substring(propertyName.lastIndexOf('.') + 1));
        ((Criteria) cascadeCriterions.get(cascadeName)).add(c);
    }

    private DetachedCriteria createDetachedCriteria(DetachedCriteria dc, String cascadeName) {
        String[] cascadeNames = cascadeName.split("[.]");
        DetachedCriteria temp = dc;
        for (String name : cascadeNames) {
            temp = dc.createCriteria(name);
        }
        return temp;
    }

    protected Criteria createCriteria(Criterion[] criterions, String... orderBys) {
        Criteria criteria = getSession().createCriteria(this.entityClass);
        if (DynaBean.class.isAssignableFrom(this.entityClass) && DynaBeanQueryManager.getManager().peek().isDynamicQuery()) {
            criteria = criteria.createAlias("attributeValues", "_attributeValues", JoinType.LEFT_OUTER_JOIN);
        }
        Set<String> alias = new HashSet<String>();
        for (Criterion c : criterions) {
            changePropertyName(criteria, alias, c);
            criteria.add(c);
        }
        for (String orderBy : orderBys) {
            createAlias(criteria, alias, orderBy);
        }
        criteria.setCacheable(true);
        return criteria;
    }

    @SuppressWarnings("unchecked")
    protected void changePropertyName(Criteria criteria, Set<String> alias, Criterion c) {
        if (c instanceof Disjunction) {
            List<Criterion> criterions = (List<Criterion>) ReflectionUtils.getFieldValue(c, "conditions");
            for (Criterion criterion : criterions) {
                changePropertyName(criteria, alias, criterion);
            }
        } else if (c instanceof SQLCriterion) {
            logger.debug("未处理：" + c.toString());
        } else if (c instanceof LogicalExpression) {
            changePropertyName(criteria, alias, (Criterion) ReflectionUtils.getFieldValue(c, "lhs"));
            changePropertyName(criteria, alias, (Criterion) ReflectionUtils.getFieldValue(c, "rhs"));
        } else if (c instanceof NotExpression) {
            Criterion criterion = (Criterion) ReflectionUtils.getFieldValue(c, "criterion");
            if (criterion instanceof InExpression) {
                changePropertyName(criteria, alias, criterion);
            }
        } else {
            String propertyName = (String) ReflectionUtils.getFieldValue(c, "propertyName");
            String newPropertyName = createAlias(criteria, alias, propertyName);
            ReflectionUtils.setFieldValue(c, "propertyName", newPropertyName);
        }
    }

    public void flush() {
        getSession().flush();
    }

    protected Query distinct(Query query) {
        query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return query;
    }

    protected <Q extends Query, C> Q distinct(Q query, Class<C> resultClass) {
        AliasToBeanResultTransformer transformer = new AliasToBeanResultTransformer(resultClass);
        if (query instanceof SQLQuery) {
            transformer.custom((SQLQuery) query);
        }
        return distinct(query, transformer);
    }

    protected <Q extends Query> Q distinct(Q query, ResultTransformer transformer) {
        query.setResultTransformer(transformer);
        return query;
    }

    protected Criteria distinct(Criteria criteria) {
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria;
    }

    protected DetachedCriteria distinct(DetachedCriteria dc) {
        dc.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        return dc;
    }

    public String getIdName(Class entityClass) {
        ClassMetadata meta = getSessionFactory().getClassMetadata(entityClass);
        return meta.getIdentifierPropertyName();
    }

    public String getIdName() {
        ClassMetadata meta = getSessionFactory().getClassMetadata(this.entityClass);
        return meta.getIdentifierPropertyName();
    }

    public Pager<T> getAll(Pager<T> pagination) {
        return findPager(pagination);
    }

    @SuppressWarnings("unchecked")
    public Pager<T> findPager(Pager<T> pager, String hql, Object... values) {
        pager = pager == null ? new Pager<T>() : pager;
        Query q = createQuery(hql, values);
        pager.setTotalCount(countHqlResult(hql, values));
        setPageParameter(q, pager);
        pager.setPageItems(distinct(q).list());
        return pager;
    }

    @SuppressWarnings("unchecked")
    public Pager<T> findPager(Pager<T> pager, String hql, Map<String, ?> values) {
        pager = pager == null ? new Pager<T>() : pager;
        Query q = createQuery(hql, values);
        pager.setTotalCount(countHqlResult(hql, values));
        setPageParameter(q, pager);
        pager.setPageItems(distinct(q).list());
        return pager;
    }

//    public <C> Pager<C> findPager(Pager<C> pager,Query q, Class<C> resultClass) {
//        setPageParameter(q, pager);
//        pager.setPageItems(distinct(q,resultClass).list());
//        return pager;
//    }

    /**
     * @param pager      翻页对象
     * @param criterions 查询条件
     * @return pager
     */
    @SuppressWarnings("unchecked")
    public Pager<T> findPager(Pager<T> pager, Criterion... criterions) {
        pager = pager == null ? new Pager<T>() : pager;
        Criteria c = distinct(createCriteria(criterions, StringUtil.tokenizeToStringArray(pager.getOrderBy())));
        pager.setTotalCount(countCriteriaResult(c));
        setPageParameter(c, pager);
        pager.setPageItems(c.list());
        return pager;
    }

    protected <C> Query setPageParameter(Query q, Pager<C> pagination) {
        q.setFirstResult(pagination.getFirst());
        q.setMaxResults(pagination.getPageSize());
        return q;
    }

    protected Criteria setPageParameter(Criteria c, Pager<T> pager) {
        c.setFirstResult(pager.getFirst());
        c.setMaxResults(pager.getPageSize());
        if (pager.isOrderBySetted()) {
            String[] orderByArray = StringUtil.tokenizeToStringArray(pager.getOrderBy());
            Assert.isTrue(orderByArray.length == pager.getOrders().length, "分页多重排序参数中,排序字段与排序方向的个数不相等");
            for (int i = 0; i < orderByArray.length; i++) {
                if ("asc".equals(pager.getOrders()[i].name())) {
                    c.addOrder(Order.asc(orderByAlias(orderByArray[i])));
                } else {
                    c.addOrder(Order.desc(orderByAlias(orderByArray[i])));
                }
            }
        }
        return c;
    }

    private String orderByAlias(String orderBy) {
        if (!orderBy.contains(".")) {
            return orderBy;
        }
        String newOrderBy = RegexpUtil.replace(RegexpUtil.replace(orderBy, "([a-zA-Z0-9]+)[.]", new RegexpUtil.AbstractReplaceCallBack() {

            @Override
            public String doReplace(String text, int index, Matcher matcher) {
                return (index == 0 ? "_" : "") + $(1) + "_";
            }

        }), "[_]([a-zA-Z0-9]+)$", ".$1");
        if (logger.isDebugEnabled()) {
            logger.debug("使用别名后的新的排序字段{new:" + newOrderBy + ",old:" + orderBy + "}");
        }
        return newOrderBy;
    }

    protected Criteria setPageParameter(Criteria c, String orderBy, String order) {
        String[] orderByArray = StringUtil.tokenizeToStringArray(orderBy);
        String[] orderArray = StringUtil.tokenizeToStringArray(order);
        Assert.isTrue(orderByArray.length == orderArray.length, "分页多重排序参数中,排序字段与排序方向的个数不相等");
        for (int i = 0; i < orderByArray.length; i++) {
            if ("asc".equals(orderArray[i])) {
                c.addOrder(Order.asc(orderByAlias(orderByArray[i])));
            } else {
                c.addOrder(Order.desc(orderByAlias(orderByArray[i])));
            }
        }
        return c;
    }

    protected int countHqlResult(String hql, Object... values) {
        String fromHql = hql;
        fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
        fromHql = StringUtils.substringBefore(fromHql, "order by");
        String countHql = "select count(*) " + fromHql;
        try {
            Long count = (Long) findUnique(countHql, values);
            return count.intValue();
        } catch (Exception e) {
            throw new IgnoreException("hql can't be auto count, hql is:" + countHql + "" + e.getMessage());
        }
    }

    protected int countHqlResult(String hql, Map<String, ?> values) {
        String fromHql = hql;
        fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
        fromHql = StringUtils.substringBefore(fromHql, "order by");
        String countHql = "select count(*) " + fromHql;
        try {
            Long count = (Long) findUnique(countHql, values);
            return count.intValue();
        } catch (Exception e) {
            throw new IgnoreException("hql can't be auto count, hql is:" + countHql + "" + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    protected int countCriteriaResult(Criteria c) {
        CriteriaImpl impl = (CriteriaImpl) c;
        Projection projection = impl.getProjection();
        ResultTransformer transformer = impl.getResultTransformer();
        List<OrderEntry> orderEntries = null;
        try {
            orderEntries = (List<OrderEntry>) ReflectionUtils.getFieldValue(impl, "orderEntries");
            ReflectionUtils.setFieldValue(impl, "orderEntries", new ArrayList<OrderEntry>());
        } catch (Exception e) {
            this.logger.error("不可能抛出的异常:{}", e.getMessage());
        }
        int totalCount = Integer.valueOf(c.setProjection(Projections.rowCount()).uniqueResult().toString());
        c.setProjection(projection);
        if (projection == null) {
            c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
        if (transformer != null) {
            c.setResultTransformer(transformer);
        }
        try {
            ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
        } catch (Exception e) {
            this.logger.error("不可能抛出的异常:{}", e.getMessage());
        }
        return totalCount;
    }

    public List<T> findBy(String propertyName, Object value, PropertyFilter.MatchType matchType) {
        Criterion criterion = buildPropertyFilterCriterion(propertyName, value, matchType);
        return find(criterion == null ? new Criterion[0] : new Criterion[]{criterion});
    }

    @SuppressWarnings("unchecked")
    public List<T> find(Criterion... criterions) {
        return distinct(createCriteria(criterions)).list();
    }

    @SuppressWarnings("unchecked")
    public List<T> find(Criterion[] criterions, String orderBy, String order) {
        Criteria c = createCriteria(criterions, StringUtil.tokenizeToStringArray(orderBy));
        setPageParameter(c, orderBy, order);
        return distinct(c).list();
    }

    @SuppressWarnings("unchecked")
    public List<T> find(Criterion[] criterions, String orderBy, String order, int start, int size) {
        Criteria c = createCriteria(criterions, StringUtil.tokenizeToStringArray(orderBy));
        c.setFirstResult(start);
        c.setMaxResults(size);
        setPageParameter(c, orderBy, order);
        return distinct(c).list();
    }

    @SuppressWarnings("unchecked")
    public List<T> find(Criterion[] criterions, int start, int size) {
        Criteria c = createCriteria(criterions);
        c.setFirstResult(start);
        c.setMaxResults(size);
        return distinct(c).list();
    }

    public List<T> find(List<PropertyFilter> filters) {
        return find(buildPropertyFilterCriterions(filters));
    }

    @SuppressWarnings("unchecked")
    public List<T> find(List<PropertyFilter> filters, String orderBy, String order) {
        Criteria c = createCriteria(buildPropertyFilterCriterions(filters), StringUtil.tokenizeToStringArray(orderBy));
        String[] orderByArray = StringUtil.tokenizeToStringArray(orderBy);
        String[] orderArray = StringUtil.tokenizeToStringArray(order);
        for (int i = 0; i < orderByArray.length; i++) {
            if ("asc".equals(orderArray[i])) {
                c.addOrder(Order.asc(orderByAlias(orderByArray[i])));
            } else {
                c.addOrder(Order.desc(orderByAlias(orderByArray[i])));
            }
        }
        return distinct(c).list();
    }

    @SuppressWarnings("unchecked")
    public List<T> find(List<PropertyFilter> filters, String orderBy, String order, int start, int size) {
        Criteria c = createCriteria(buildPropertyFilterCriterions(filters), StringUtil.tokenizeToStringArray(orderBy));
        c.setFirstResult(start);
        c.setMaxResults(size);
        setPageParameter(c, orderBy, order);
        return distinct(c).list();
    }

    @SuppressWarnings("unchecked")
    public List<T> find(List<PropertyFilter> filters, int start, int size) {
        Criterion[] criterions = buildPropertyFilterCriterions(filters);
        Criteria c = createCriteria(criterions);
        c.setFirstResult(start);
        c.setMaxResults(size);
        return distinct(c).list();
    }

    public Pager<T> findPager(Pager<T> pager, List<PropertyFilter> filters) {
        pager = pager == null ? new Pager<T>() : pager;
        filters = filters == null ? new ArrayList<PropertyFilter>() : filters;
        Criterion[] criterions = buildPropertyFilterCriterions(filters);
        return findPager(pager, criterions);
    }

    protected Criterion[] buildPropertyFilterCriterions(List<PropertyFilter> filters) {
        List<Criterion> criterionList = new ArrayList<Criterion>();
        for (PropertyFilter filter : filters) {
            if (StringUtil.isBlank(filter.getPropertyValue())) {
                continue;
            }
            if (!filter.isMultiProperty()) {
                Object propertyValue = getPropertyValue(filter);
                Criterion criterion = buildPropertyFilterCriterion(filter.getPropertyName(), propertyValue, filter.getMatchType());
                if (criterion == null) {
                    continue;
                }
                criterionList.add(criterion);
            } else {
                Disjunction disjunction = Restrictions.disjunction();
                for (String param : filter.getPropertyNames()) {
                    Criterion criterion = buildPropertyFilterCriterion(param, filter.getPropertyValue(), filter.getMatchType());
                    if (criterion == null) {
                        continue;
                    }
                    disjunction.add(criterion);
                }
                criterionList.add(disjunction);
            }
        }
        return criterionList.toArray(new Criterion[criterionList.size()]);
    }

    private Object getPropertyValue(PropertyFilter filter) {
        if (filter.getPropertyType() == null) {
            return null;
        }
        if (!filter.getPropertyType().isAssignableFrom(Enum.class)) {
            return filter.getPropertyValue();
        }
        Class<?> entityClass = this.entityClass;
        String[] propertyNames = filter.getPropertyName().split("\\.");
        for (int i = 0; i < propertyNames.length - 1; i++) {
            entityClass = ClassUtil.getProperty(entityClass, propertyNames[i]).getPropertyType();
        }
        return filter.getPropertyValue(ClassUtil.getProperty(entityClass, propertyNames[propertyNames.length - 1]).getPropertyType());
    }

    public static String getAlias(String property) {
        return property;
    }

    private String createAlias(Criteria criteria, Set<String> alias, String property) {
        String[] names = StringUtil.tokenizeToStringArray(property, ".");
        if (names.length > 1) {
            String aliasName = "", objeactName = "";
            for (int i = 0; i < names.length - 1; i++) {
                objeactName = objeactName + (StringUtils.isNotBlank(aliasName) ? "." : "") + names[i];
                aliasName = "_" + objeactName.replaceAll("\\.", "_");
                if (alias.add(aliasName)) {
                    criteria.createAlias(objeactName, aliasName);
                    if (logger.isDebugEnabled()) {
                        logger.debug("为查询建立别名:createAlias(" + objeactName + "," + aliasName + ")");
                    }
                }
            }
            String newProperty = aliasName + property.substring(property.lastIndexOf("."));
            if (logger.isDebugEnabled()) {
                logger.debug("使用别名时使用的属性名称:" + newProperty);
            }
            return newProperty;
        } else {
            return property;
        }
    }

    protected Criterion buildPropertyFilterCriterion(String propertyName, Object propertyValue, PropertyFilter.MatchType matchType) {
        Assert.hasText(propertyName, "propertyName不能为空");
        Criterion criterion = null;
        try {
            if (PropertyFilter.MatchType.EQ.equals(matchType)) {
                criterion = Restrictions.eq(propertyName, propertyValue);
            } else if (PropertyFilter.MatchType.LIKE.equals(matchType)) {
                criterion = Restrictions.like(propertyName, (String) propertyValue, MatchMode.ANYWHERE);
            } else if (PropertyFilter.MatchType.LE.equals(matchType)) {
                criterion = Restrictions.le(propertyName, propertyValue);
            } else if (PropertyFilter.MatchType.LT.equals(matchType)) {
                criterion = Restrictions.lt(propertyName, propertyValue);
            } else if (PropertyFilter.MatchType.GE.equals(matchType)) {
                criterion = Restrictions.ge(propertyName, propertyValue);
            } else if (PropertyFilter.MatchType.GT.equals(matchType)) {
                criterion = Restrictions.gt(propertyName, propertyValue);
            } else if (PropertyFilter.MatchType.IN.equals(matchType)) {
                if (Array.getLength(propertyValue) == 0) {
                    return null;
                }
                criterion = Restrictions.in(propertyName, (Object[]) propertyValue);
            } else if (PropertyFilter.MatchType.NOTIN.equals(matchType)) {
                if (Array.getLength(propertyValue) == 0) {
                    return null;
                }
                criterion = Restrictions.not(Restrictions.in(propertyName, (Object[]) propertyValue));
            } else if (PropertyFilter.MatchType.NE.equals(matchType)) {
                criterion = Restrictions.ne(propertyName, propertyValue);
            } else if (PropertyFilter.MatchType.NULL.equals(matchType)) {
                criterion = Restrictions.isNull(propertyName);
            } else if (PropertyFilter.MatchType.NOTNULL.equals(matchType)) {
                criterion = Restrictions.isNotNull(propertyName);
            } else if (PropertyFilter.MatchType.EMPTY.equals(matchType)) {
                criterion = Restrictions.isEmpty(propertyName);
            } else if (PropertyFilter.MatchType.NOTEMPTY.equals(matchType)) {
                criterion = Restrictions.isNotEmpty(propertyName);
            } else if (PropertyFilter.MatchType.BETWEEN.equals(matchType)) {
                criterion = Restrictions.between(propertyName, Array.get(propertyValue, 0), Array.get(propertyValue, 1));
            } else if (PropertyFilter.MatchType.SQL.equals(matchType)) {
                criterion = Restrictions.sqlRestriction("ERROR");
            }
        } catch (Exception e) {
            throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
        }
        return criterion;
    }

    public boolean isPropertyUnique(String propertyName, Object newValue, Object oldValue) {
        if ((newValue == null) || (newValue.equals(oldValue))) {
            return true;
        }
        Object object = findUniqueBy(propertyName, newValue);
        return object == null;
    }

    public int count(List<PropertyFilter> filters) {
        return countCriteriaResult(createCriteria(buildPropertyFilterCriterions(filters == null ? new ArrayList<PropertyFilter>() : filters)));
    }

    public int count(Criterion... criterions) {
        return countCriteriaResult(createCriteria(criterions));
    }

    public static Criterion sqlRestriction(Criterion criterion, String propertyNameSql) {
        if (criterion instanceof SimpleExpression) {
            Object value = ClassUtil.getValue(criterion, "value");
            return Restrictions.sqlRestriction(propertyNameSql + " " + ClassUtil.getValue(criterion, "op") + " ? ", value, TypeFactory.basic(ClassUtil.getRealClass(value).getName()));
        } else if (criterion instanceof BetweenExpression) {
            Object[] values = new Object[]{ClassUtil.getValue(criterion, "lo"), ClassUtil.getValue(criterion, "hi")};
            Type[] types = new Type[values.length];
            for (int i = 0; i < values.length; i++) {
                types[i] = TypeFactory.basic(ClassUtil.getRealClass(values[i]).getName());
            }
            return Restrictions.sqlRestriction(propertyNameSql + " between ? and ?", values, types);
        } else if (criterion instanceof LogicalExpression) {
            ClassUtil.setValue(criterion, "lhs", sqlRestriction((Criterion) ClassUtil.getValue(criterion, "lhs"), propertyNameSql));
            ClassUtil.setValue(criterion, "rhs", sqlRestriction((Criterion) ClassUtil.getValue(criterion, "rhs"), propertyNameSql));
            return criterion;
        }
        throw new IgnoreException("暂不支持  " + criterion.getClass() + " 到 SQLCriterion 的转换(" + criterion.toString() + ")");
    }

    @SuppressWarnings("unchecked")
    public List<Map> find(List<PropertyFilter> filters, Projection[] projections) {
        Criterion[] criterions = this.buildPropertyFilterCriterions(filters);
        Criteria c = createCriteria(criterions);
        ProjectionList projectionList = Projections.projectionList();
        for (Projection projection : projections) {
            projectionList.add(projection);
        }
        c.setProjection(projectionList);
        c.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return c.list();
    }

    public Map findUnique(List<PropertyFilter> filters, Projection[] projections) {
        Criterion[] criterions = this.buildPropertyFilterCriterions(filters);
        Criteria c = createCriteria(criterions);
        ProjectionList projectionList = Projections.projectionList();
        for (Projection projection : projections) {
            projectionList.add(projection);
        }
        c.setProjection(projectionList);
        c.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return (Map) c.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public <C> List<C> find(List<PropertyFilter> filters, Projection[] projections, Class<C> resultClass) {
        Criterion[] criterions = this.buildPropertyFilterCriterions(filters);
        Criteria c = createCriteria(criterions);
        ProjectionList projectionList = Projections.projectionList();
        for (Projection projection : projections) {
            projectionList.add(projection);
        }
        c.setProjection(projectionList);
        c.setResultTransformer(new AliasToBeanResultTransformer(resultClass));
        return c.list();
    }

}