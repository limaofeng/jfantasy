package org.jfantasy.framework.lucene.handler;

import org.apache.lucene.document.Document;

public interface FieldHandler {

	public void handle(Document paramDocument);

}
