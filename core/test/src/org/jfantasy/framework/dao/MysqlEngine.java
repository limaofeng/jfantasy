package org.jfantasy.framework.dao;

import org.jfantasy.framework.util.common.JdbcUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.sql.DataSource;
import java.sql.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:backup/testconfig/spring/applicationContext.xml"})
public class MysqlEngine {

    private static final Log LOG = LogFactory.getLog(MysqlEngine.class);

    @Autowired
    private DataSource dataSource;

    private final static String SCHEMA = "website";

    @Test
    public void change() throws SQLException {
        Connection connection = dataSource.getConnection();
        try {
            //查询 非 InnoDB 的表
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select TABLE_NAME,ENGINE from information_schema.TABLES where TABLE_SCHEMA='" + SCHEMA + "' and ENGINE<>'InnoDB'");
            while (resultSet.next()) {
                LOG.debug(resultSet.getString("TABLE_NAME") + " \t " + resultSet.getString("ENGINE"));
                Connection _connection = dataSource.getConnection();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("alter table " + SCHEMA + "." + resultSet.getString("TABLE_NAME") + " engine=InnoDB;");
                    preparedStatement.executeUpdate();
                } finally {
                    JdbcUtil.closeConnection(_connection);
                }
            }
        } finally {
            JdbcUtil.closeConnection(connection);
        }
    }

}
