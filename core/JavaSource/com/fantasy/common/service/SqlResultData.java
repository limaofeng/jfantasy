package com.fantasy.common.service;


import com.fantasy.framework.util.common.StringUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlResultData {

    private String sql;
    private int maxRow;

    public enum Type {
        select, insert, delete, update, other
    }

    private Type type;

    private List<String> columnNames;

    private List<List<String>> rowData;

    private String message;

    private SqlResultData(String sql, int maxRow) {
        this.sql = sql;
        this.maxRow = maxRow == 0 ? 20 : maxRow;
    }

    public static SqlResultData newInstance(String sql, int maxRow) {
        String sqlType = StringUtil.trim(sql).substring(0, 6);
        SqlResultData resultData = new SqlResultData(sql, maxRow);
        if ("select".equalsIgnoreCase(sqlType)) {
            resultData.type = Type.select;
        } else if ("insert".equalsIgnoreCase(sqlType)) {
            resultData.type = Type.insert;
        } else if ("update".equalsIgnoreCase(sqlType)) {
            resultData.type = Type.update;
        } else if ("delete".equalsIgnoreCase(sqlType)) {
            resultData.type = Type.delete;
        } else {
            resultData.type = Type.other;
        }
        return resultData;
    }

    public void execute(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        if (Type.select == this.type) {
            ResultSet rs = stmt.executeQuery(this.sql);
            ResultSetMetaData md = rs.getMetaData();
            int mdc = md.getColumnCount();
            for (int i = 1; i <= mdc; i++) {
                this.addColumnName(md.getColumnName(i));
            }
            while (rs.next() && this.rowData.size() < this.maxRow) {
                List<String> row = this.createRow();
                for (int i = 1; i <= mdc; i++) {
                    row.add(StringUtil.escapeHtml(rs.getString(i)));
                }
            }
        } else if (Type.insert == this.type) {
            this.message = "受影响的行数:" + stmt.executeUpdate(this.sql);
        } else if (Type.update == this.type || Type.delete == this.type) {
            if (this.sql.contains("where") || sql.contains("WHERE")) {
                this.message = "受影响的行数:" + stmt.executeUpdate(this.sql);
            } else {
                this.message = "为了防止误操作，UPDATE ,DELETE 语句必须包含WHERE条件，不需要WHERE条件可以用 WHERE 1=1";
            }
        } else {
            stmt.execute(this.sql);
            ResultSet rs = stmt.getResultSet();
            if (rs != null) {
                ResultSetMetaData md = rs.getMetaData();
                int mdc = md.getColumnCount();
                for (int i = 1; i <= mdc; i++) {
                    this.addColumnName(md.getColumnName(i));
                }
                while (rs.next() && this.rowData.size() < this.maxRow) {
                    List<String> row = this.createRow();
                    for (int i = 1; i <= mdc; i++) {
                        row.add(StringUtil.escapeHtml(rs.getString(i)));
                    }
                }
            } else {
                this.message = stmt.getWarnings().getMessage();
            }
        }
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public List<List<String>> getRowData() {
        return rowData;
    }

    public void setRowData(List<List<String>> rowData) {
        this.rowData = rowData;
    }

    public void addColumnName(String columnName) {
        this.columnNames.add(columnName);
    }

    public List<String> createRow() {
        List<String> row = new ArrayList<String>();
        this.rowData.add(row);
        return row;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMaxRow() {
        return maxRow;
    }

    public void setMaxRow(int maxRow) {
        this.maxRow = maxRow;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
