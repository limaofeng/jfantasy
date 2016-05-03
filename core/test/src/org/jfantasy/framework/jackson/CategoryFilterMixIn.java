package org.jfantasy.framework.jackson;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("category")
public interface CategoryFilterMixIn extends FilterMixIn{
}
