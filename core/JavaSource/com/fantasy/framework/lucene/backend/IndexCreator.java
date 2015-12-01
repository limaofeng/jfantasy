package com.fantasy.framework.lucene.backend;

import com.fantasy.framework.lucene.annotations.BoostSwitch;
import com.fantasy.framework.lucene.cache.PropertysCache;
import com.fantasy.framework.lucene.handler.FieldHandler;
import com.fantasy.framework.lucene.handler.FieldHandlerFactory;
import com.fantasy.framework.util.reflect.Property;
import org.apache.lucene.document.Document;

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
        for (Property p : PropertysCache.getInstance().get(this.obj.getClass())) {
            BoostSwitch bs = p.getAnnotation(BoostSwitch.class);
            if (bs != null) {
                CompareChecker checker = new CompareChecker(this.obj);
                boolean fit = checker.isFit(p, bs.compare(), bs.value());
                if (fit) {
                    doc.setBoost(bs.fit());
                } else {
                    doc.setBoost(bs.unfit());
                }
            }
            if (FieldHandlerFactory.isHandler(p) && p.getValue(this.obj) != null) {
                FieldHandler handler = FieldHandlerFactory.create(this.obj, p, this.prefix);
                if (handler != null) {
                    handler.handle(doc);
                }
            }
        }
    }
}