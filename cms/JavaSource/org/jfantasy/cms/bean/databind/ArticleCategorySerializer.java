package org.jfantasy.cms.bean.databind;

import org.jfantasy.cms.bean.ArticleCategory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ArticleCategorySerializer extends JsonSerializer<ArticleCategory> {

    @Override
    public void serialize(ArticleCategory category, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (category == null)
            jgen.writeNull();
        else
            jgen.writeString(category.getCode());
    }

}
