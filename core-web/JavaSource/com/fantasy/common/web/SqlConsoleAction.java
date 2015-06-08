package com.fantasy.common.web;

import com.fantasy.common.service.DataSourceFactory;
import com.fantasy.common.service.SqlResultData;
import com.fantasy.framework.struts2.ActionSupport;

import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SqlConsoleAction extends ActionSupport {

    @Autowired
    private DataSourceFactory dataSourceFactory;

    public String run(Long id, String sql, int maxRow) throws SQLException {
        SqlResultData resultData = SqlResultData.newInstance(sql, maxRow);
        DataSource dataSource = dataSourceFactory.getDataSource(id);
        Connection conn = dataSource.getConnection();
        try {
            resultData.execute(conn);
            this.attrs.put(ROOT, resultData);
        } finally {
            conn.close();
        }
        return JSONDATA;
    }

}
