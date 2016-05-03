package org.jfantasy.framework.jackson;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("article")
public interface ArticleFilterMixIn extends FilterMixIn{
}
