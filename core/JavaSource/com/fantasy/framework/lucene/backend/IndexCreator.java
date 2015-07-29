package com.fantasy.framework.lucene.backend;

import com.fantasy.framework.lucene.annotations.BoostSwitch;
import com.fantasy.framework.lucene.cache.FieldsCache;
import com.fantasy.framework.lucene.handler.FieldHandler;
import com.fantasy.framework.lucene.handler.FieldHandlerFactory;
import com.fantasy.framework.lucene.mapper.FieldUtil;
import org.apache.lucene.document.Document;

import java.lang.reflect.Field;

/**
 * 索引文件 生成器
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2013-1-27 上午09:46:11
 */
public class IndexCreator {
    protected Object obj;
    protected String prefix;

    public IndexCreator(Object obj, String prefix) {
        this.obj = obj;
        this.prefix = prefix;
    }

    public void create(Document doc) {
        Field[] fields = FieldsCache.getInstance().get(this.obj.getClass());
        for (Field f : fields) {
            BoostSwitch bs = f.getAnnotation(BoostSwitch.class);
            if (bs != null) {
                CompareChecker checker = new CompareChecker(this.obj);
                boolean fit = checker.isFit(f, bs.compare(), bs.value());
                if (fit) {
                    doc.setBoost(bs.fit());
                } else {
                    doc.setBoost(bs.unfit());
                }
            }
            Object objValue = FieldUtil.get(this.obj, f);
            if (objValue != null) {
                FieldHandler handler = FieldHandlerFactory.create(this.obj, f, this.prefix);
                if (handler != null) {
                    handler.handle(doc);
                }
            }
        }
    }
}