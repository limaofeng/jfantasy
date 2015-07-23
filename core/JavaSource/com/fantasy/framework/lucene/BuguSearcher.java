package com.fantasy.framework.lucene;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.lucene.cache.DaoCache;
import com.fantasy.framework.lucene.cache.FieldsCache;
import com.fantasy.framework.lucene.cache.IndexSearcherCache;
import com.fantasy.framework.lucene.dao.LuceneDao;
import com.fantasy.framework.lucene.exception.FieldException;
import com.fantasy.framework.lucene.exception.IdException;
import com.fantasy.framework.lucene.mapper.FieldUtil;
import com.fantasy.framework.lucene.mapper.MapperUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Lucene 查询接口
 *
 * @param <T>
 * @author 李茂峰
 * @version 1.0
 * @since 2013-5-24 上午10:57:11
 */
@SuppressWarnings("unchecked")
public abstract class BuguSearcher<T> {
    private static final Logger LOGGER = Logger.getLogger(BuguSearcher.class);
    private Class<T> entityClass;
    private String idName;
    private LoadEntityMode loadMode;

    private LuceneDao<T> luceneDao;

    protected BuguSearcher(LoadEntityMode loadMode) {
        this.loadMode = loadMode;
        // 通过泛型获取需要查询的对象
        this.entityClass = (Class<T>) ClassUtil.getSuperClassGenricType(ClassUtil.getRealClass(getClass()));
        try {
            // 获取对象的主键idName
            idName = FieldsCache.getInstance().getIdField(this.entityClass).getName();
        } catch (IdException e) {
            LOGGER.error(e.getMessage(), e);
        }
        this.luceneDao = DaoCache.getInstance().get(this.entityClass);
    }

    protected BuguSearcher() {
        this(LoadEntityMode.dao);
    }

    /**
     * 打开 IndexReader
     *
     * @return IndexSearcher
     * 每次查询开始前调用
     */
    protected IndexSearcher open() {
        IndexSearcher searcher = IndexSearcherCache.getInstance().get(MapperUtil.getEntityName(this.entityClass));
        IndexReader reader = searcher.getIndexReader();
        reader.incRef();
        return searcher;
    }

    /**
     * 关闭 IndexReader
     * <p/>
     * 查询结束时调用
     *
     * @param searcher IndexSearcher
     */
    protected void close(IndexSearcher searcher) {
        try {
            searcher.getIndexReader().decRef();
        } catch (IOException ex) {
            LOGGER.error("Something is wrong when decrease the reference of IndexReader", ex);
        }
    }

    /**
     * 返回查询的结果
     *
     * @param query 查询条件
     * @param size  返回条数
     * @return List<T>
     */
    public List<T> search(Query query, int size) {
        IndexSearcher searcher = open();
        List<T> data = new ArrayList<T>();
        try {
            TopDocs topDocs = searcher.search(query, size);
            for (int i = 0; i < topDocs.scoreDocs.length; i++) {
                ScoreDoc sdoc = topDocs.scoreDocs[i];
                Document doc = searcher.doc(sdoc.doc);
                data.add(this.build(doc));
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            close(searcher);
        }
        return data;
    }

    /**
     * 支持翻页及高亮查询
     *
     * @param pager       翻页对象
     * @param query       查询条件
     * @param highlighter 关键字高亮
     * @return Pager<T>
     */
    public Pager<T> search(Pager<T> pager, Query query, BuguHighlighter highlighter) {
        IndexSearcher searcher = open();
        int between = 0;
        try {
            TopDocs hits;
            if (pager.isOrderBySetted()) {// TODO 多重排序等HIbernateDao优化好之后再实现
                hits = searcher.search(query, pager.getCurrentPage() * pager.getPageSize(), new Sort(new SortField(pager.getOrderBy(), getSortField(pager.getOrderBy()), Pager.Order.asc == pager.getOrders()[0])));
            } else {
                hits = searcher.search(query, pager.getCurrentPage() * pager.getPageSize());
                int index = (pager.getCurrentPage() - 1) * pager.getPageSize();
                if (hits.totalHits > 0 && hits.scoreDocs.length > 0) {
                    ScoreDoc scoreDoc = index > 0 ? hits.scoreDocs[index - 1] : null;
                    hits = searcher.searchAfter(scoreDoc, query, pager.getPageSize());
                }
                between = index;
            }
            pager.setTotalCount(hits.totalHits);
            List<T> data = new ArrayList<T>();
            for (int i = (pager.getFirst() - between); i < hits.scoreDocs.length && hits.totalHits > 0; i++) {
                ScoreDoc sdoc = hits.scoreDocs[i];
                Document doc = searcher.doc(sdoc.doc);
                data.add(this.build(doc));
            }
            pager.setPageItems(data);
            if (highlighter != null) {
                for (T obj : pager.getPageItems()) {
                    highlightObject(highlighter, obj);
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            close(searcher);
        }
        return pager;
    }

    /**
     * @param pager   翻页对象
     * @param query   查询条件
     * @param fields  需要高亮显示的字段
     * @param keyword 高亮关键字
     * @return Pager<T>
     */
    public Pager<T> search(Pager<T> pager, Query query, String[] fields, String keyword) {
        if (StringUtil.isNotBlank(keyword)) {
            return this.search(pager, query, new BuguHighlighter(fields, keyword));
        } else {
            return this.search(pager, query);
        }
    }

    /**
     * @param pager 翻页对象
     * @param query 查询条件
     * @return Pager<T>
     */
    public Pager<T> search(Pager<T> pager, Query query) {
        return this.search(pager, query, null);
    }

    /**
     * 将javaType 转换为 SortField
     *
     * @param fieldName 字段名称
     * @return int
     */
    private int getSortField(String fieldName) {
        try {
            Field field = FieldsCache.getInstance().getField(this.entityClass, fieldName);
            if (field.getType().isAssignableFrom(Long.class)) {
                return SortField.LONG;
            } else if (field.getType().isAssignableFrom(Integer.class)) {
                return SortField.INT;
            } else if (field.getType().isAssignableFrom(Double.class)) {
                return SortField.DOUBLE;
            } else if (field.getType().isAssignableFrom(Float.class)) {
                return SortField.FLOAT;
            } else {
                return SortField.STRING;
            }
        } catch (FieldException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return SortField.STRING;
    }

    private void highlightObject(BuguHighlighter highlighter, Object obj) {
        String[] fields = highlighter.getFields();
        for (String fieldName : fields) {
            if (!fieldName.contains(".")) {
                Field field = null;
                try {
                    field = FieldsCache.getInstance().getField(this.entityClass, fieldName);
                } catch (FieldException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
                Object fieldValue = FieldUtil.get(obj, field);
                if (fieldValue != null) {
                    String result = null;
                    try {
                        result = highlighter.getResult(fieldName, fieldValue.toString());
                    } catch (Exception ex) {
                        LOGGER.error("Something is wrong when getting the highlighter result", ex);
                    }
                    if (!StringUtil.isEmpty(result)) {
                        FieldUtil.set(obj, field, result);
                    }
                }
            }
        }
    }

    private T build(Document doc) {
        if (LoadEntityMode.dao == this.loadMode) {
            return this.luceneDao.get(doc.get(idName));
        } else {
            T object = ClassUtil.newInstance(this.entityClass);
            for (Fieldable fieldable : doc.getFields()) {
                try {
                    Field field = FieldsCache.getInstance().getField(this.entityClass, fieldable.name());
                    if (Date.class.isAssignableFrom(field.getType())) {
                        OgnlUtil.getInstance().setValue(fieldable.name(), object, new Date(Long.valueOf(fieldable.stringValue())));
                    } else {
                        OgnlUtil.getInstance().setValue(fieldable.name(), object, fieldable.stringValue());
                    }
                } catch (FieldException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
            return object;
        }
    }

    public enum LoadEntityMode {
        lucene, dao
    }

}