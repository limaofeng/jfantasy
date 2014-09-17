package com.fantasy.framework.util.generate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.mybatis.sqlmapper.SqlMapper;
import com.fantasy.framework.util.generate.model.Column;

@Repository
public abstract interface ColumnMapper extends SqlMapper {
    public abstract List<Column> getTabColumnsByTabName(String paramString);

    public abstract String getTabCommentsByTabName(String paramString);
}