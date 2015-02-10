package com.fantasy.framework.lucene.handler;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import com.fantasy.framework.lucene.mapper.FieldUtil;

public class IdFieldHandler extends AbstractFieldHandler {

	public IdFieldHandler(Object obj, java.lang.reflect.Field field, String prefix) {
		super(obj, field, prefix);
	}

	public void handle(Document doc) {
		String fieldName = this.field.getName();
		doc.add(new Field(fieldName, getEntityId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
	}
	
	private String getEntityId(){
		Object id = FieldUtil.get(this.obj,this.field);
		if(id==null){
            throw new RuntimeException("要索引的对象@Id字段不能为空");
        }
		return String.valueOf(id);
	}
	
}
