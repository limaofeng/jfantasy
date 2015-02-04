package com.fantasy.file.manager;

import com.fantasy.file.FileItem;
import com.fantasy.file.FileItemFilter;
import com.fantasy.file.FileItemSelector;
import com.fantasy.file.FileManager;
import com.fantasy.framework.util.common.JdbcUtil;
import com.fantasy.framework.util.common.StreamUtil;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.*;
import java.sql.*;
import java.util.List;

/**
 * 使用db存储文件
 */
public class JDBCFileManager implements FileManager, InitializingBean {

    private DataSource dataSource;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.dataSource, "Property 'dataSource' is required");
    }

    /**
     * 判断文件是否存在
     *
     * @param absolutePath 文件绝对路径
     * @return {boolean}
     * @throws IOException
     */
    private boolean isExistFileByPath(String absolutePath) throws IOException {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        boolean isExist = false;
        try {
            con = this.dataSource.getConnection();
            con.setAutoCommit(false);
            st = con.createStatement();
            rs = st.executeQuery("select count(*) count from F_FILE where PATH='" + absolutePath + "'");
            if (rs.next()) {
                int count = rs.getInt("count");
                isExist = count != 0;
            }
            con.commit();
        } catch (SQLException e) {
            JdbcUtil.rollback(con);
            throw new IOException(e.getMessage());
        } finally {
            JdbcUtil.closeResultSet(rs);
            JdbcUtil.closeStatement(st);
            JdbcUtil.closeConnection(con);
        }
        return isExist;
    }

    public void readFile(String remotePath, String localPath) throws IOException {
        readFile(remotePath, new FileOutputStream(localPath));
    }

    public void readFile(String remotePath, OutputStream out) throws IOException {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            con.setAutoCommit(false);
            st = con.createStatement();
            rs = st.executeQuery("select BLOBATTR from F_FILE where PATH='" + remotePath + "'");
            if (rs.next()) {
                Blob blob = rs.getBlob("BLOBATTR");
                InputStream in = blob.getBinaryStream();
                writeFile(out, in);
            }
            con.commit();
        } catch (IOException e) {
            JdbcUtil.rollback(con);
            throw new IOException(e.getMessage());
        } catch (SQLException e) {
            JdbcUtil.rollback(con);
            throw new IOException(e.getMessage());
        } finally {
            JdbcUtil.closeResultSet(rs);
            JdbcUtil.closeStatement(st);
            JdbcUtil.closeConnection(con);
        }
    }

    public InputStream readFile(String remotePath) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        readFile(remotePath, out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    public void writeFile(String absolutePath, File file) throws IOException {
        writeFile(absolutePath, new FileInputStream(file));
    }

    @SuppressWarnings("deprecation")
    public void writeFile(String absolutePath, InputStream in) throws IOException {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            con.setAutoCommit(false);
            st = con.createStatement();
            if (isExistFileByPath(absolutePath)) {
                st.executeUpdate("update F_FILE set BLOBATTR = empty_blob() where PATH='" + absolutePath + "'");
            } else {
                st.executeUpdate("insert into F_FILE (PATH, BLOBATTR) values ('" + absolutePath + "', empty_blob())");
            }
            rs = st.executeQuery("select BLOBATTR from F_FILE where PATH='" + absolutePath + "' for update");
            if (rs.next()) {
                writeFile(rs.getBlob("BLOBATTR").setBinaryStream(in.available()), in);
            }
            con.commit();
        } catch (IOException e) {
            JdbcUtil.rollback(con);
            throw new IOException(e.getMessage());
        } catch (SQLException e) {
            JdbcUtil.rollback(con);
            throw new IOException(e.getMessage());
        } finally {
            JdbcUtil.closeResultSet(rs);
            JdbcUtil.closeStatement(st);
            JdbcUtil.closeConnection(con);
        }
    }

    private void writeFile(OutputStream out, InputStream in) throws IOException {
        StreamUtil.copy(in, out);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public OutputStream writeFile(String remotePath) throws IOException {
        return null;
    }

    public List<FileItem> listFiles() {
        return null;
    }

    public List<FileItem> listFiles(String remotePath) {
        return null;
    }

    public FileItem getFileItem(String remotePath) {
        return null;
    }

    public List<FileItem> listFiles(FileItemSelector selector) {
        return null;
    }

    public List<FileItem> listFiles(String remotePath, FileItemSelector selector) {
        return null;
    }

    public List<FileItem> listFiles(FileItemFilter filter) {
        return null;
    }

    public List<FileItem> listFiles(String remotePath, FileItemFilter filter) {
        return null;
    }

    public void removeFile(String remotePath) {
    }
}