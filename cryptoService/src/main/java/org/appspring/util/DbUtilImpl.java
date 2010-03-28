package org.appspring.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import com.emirates.citp.crypto.impl.Key;
import com.emirates.citp.crypto.AppAuth;
import sun.misc.BASE64Encoder;

public class DbUtilImpl extends HibernateDaoSupport implements DbUtil {
    //private static DbUtilImpl dbUtil;
    protected final transient Log log = LogFactory.getLog(getClass());


    private Connection connection;

    private String APP_KEY_TABLE = "SOA_APP_KEY";
    private static final String ADMIN_USER_ID = "-1";
    private static final String DEFAULT_GROUP_ID = "1";
    private static final String DEFAULT_CERTIFICATE_ID = "123456789";

    /*
     public Connection getConnection() {
         try {
             // Load the JDBC driver
             String driverName = "oracle.jdbc.driver.OracleDriver";
             Class.forName(driverName);

             // Create a connection to the database
             String serverName = "hqlinuxdevbl64.hq.emirates.com";
             String portNumber = "1526";
             String sid = "fossd";
             String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber
                     + ":" + sid;
             String username = "soa_ownr";
             String password = "temp123";
             connection = DriverManager.getConnection(url, username, password);
         } catch (ClassNotFoundException e) {
             e.printStackTrace();
             // Could not find the database driver
             return null;
         } catch (SQLException e) {
             // Could not connect to the database
             e.printStackTrace();
             return null;
         }
         return connection;
     }*/


    public void commit(Connection connection) {
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public Connection getConnection() {
        log.debug(getSessionFactory() == null ? "Session Factory is null" : "Session Factory is Not null");
        DataSource dsCitp = SessionFactoryUtils
                .getDataSource(getSessionFactory());
        log.debug(dsCitp == null ? "DataSource is null" : "DataSource is Not null");
        try {
            connection = dsCitp.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return connection;
    }

    public void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public boolean saveSymmetricKeysInDatabase(List keys, Connection connection) throws Exception {
        log.debug("entering 'saveSymmetricKeysInDatabase' method...");
        try {
            /*
            log.debug(getSessionFactory() == null ? "Session Factory is null" : "Session Factory is Not null");
            DataSource dsCitp = SessionFactoryUtils
                    .getDataSource(getSessionFactory());
            log.debug(dsCitp == null ? "DataSource is null" : "DataSource is Not null");
            connection = dsCitp.getConnection();
            connection.setAutoCommit(false);*/
            String sql = "INSERT INTO " + APP_KEY_TABLE + " (name ,CERTIFICATEID,status, SVN_KEY,create_date,modify_date, soa_group_id) VALUES(?,?,'Active', ?,SYSDATE(), SYSDATE(), ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            Iterator iterator = keys.iterator();
            for (int i = 0; i < keys.size(); i++) {
                Key key = (Key) iterator.next();
                pstmt.setString(1, key.getAppId());
                pstmt.setString(2, DEFAULT_CERTIFICATE_ID);
                pstmt.setObject(3, key.getEncryptedSymmetricKeyBytes());
                pstmt.setString(4, DEFAULT_GROUP_ID);
                pstmt.executeUpdate();
            }
            pstmt.close();
            log.debug("exiting 'saveSymmetricKeysInDatabase' method...");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error 'saveSymmetricKeysInDatabase' method...", e);
            throw e;
        }
    }

    public boolean saveSymmetricKey(Key key, String certificateId, String groupId, Connection connection) throws Exception {
        log.debug("entering 'saveSymmetricKeysInDatabase' method...");
        try {
            String sql = "INSERT INTO " + APP_KEY_TABLE + " (name ,CERTIFICATEID,status, SVN_KEY,create_date,modify_date, soa_group_id) VALUES(?,?,'Active', ?,SYSDATE(), SYSDATE(), ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, key.getAppId());
            pstmt.setString(2, certificateId);
            pstmt.setObject(3, key.getEncryptedSymmetricKeyBytes());
            pstmt.setString(4, groupId);
            pstmt.executeUpdate();
            pstmt.close();
            log.debug("exiting 'saveSymmetricKeysInDatabase' method...");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error 'saveSymmetricKeysInDatabase' method...", e);
            throw e;
        }
    }


    public boolean deleteAllSymmetricKeysFromDatabase(Connection connection) throws Exception {
        try {
            String sql = "delete from " + APP_KEY_TABLE;
            Statement stmt = connection.createStatement();
            boolean result = stmt.execute(sql);
            stmt.close();

            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean deleteSymmetricKeyFromDatabase(String appId, Connection connection) throws Exception {
        try {
            String sql = "delete from " + APP_KEY_TABLE + " where name = '" + appId + "'";
            Statement stmt = connection.createStatement();
            boolean result = stmt.execute(sql);
            stmt.close();
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


    }

    /**
     * @return a array of Application Identifiers from database
     */
    public String[] getAllApplicationIdentifiers() {
        try {
            log.debug(getSessionFactory() == null ? "Session Factory is null" : "Session Factory is Not null");
            DataSource dsCitp = SessionFactoryUtils
                    .getDataSource(getSessionFactory());
            log.debug(dsCitp == null ? "DataSource is null" : "DataSource is Not null");
            connection = dsCitp.getConnection();
            String sql = "Select name, SVN_KEY from " + APP_KEY_TABLE + " where status='Active'";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            List appIds = new ArrayList();
            while (rs.next()) {
                String appId = rs.getString("name");
                appIds.add(appId);
            }
            String[] ret = new String[appIds.size()];
            for (int i = 0; i < ret.length; i++) {
                ret[i] = (String) appIds.get(i);
            }
            //return (String[])appIds.toArray();
            return ret;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean getApplicationAuthList(List<AppAuth> authList) {
        try {
            log.debug(getSessionFactory() == null ? "Session Factory is null" : "Session Factory is Not null");
            DataSource dsCitp = SessionFactoryUtils
                    .getDataSource(getSessionFactory());
            log.debug(dsCitp == null ? "DataSource is null" : "DataSource is Not null");
            connection = dsCitp.getConnection();
            String sql = "select a.name,b.username,b.password from SOA_APP_KEY a,app_user b where a.app_user_id=b.id and a.status='Active'";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                AppAuth appauth = new AppAuth();
                appauth.setAppName(rs.getString("name"));
                appauth.setUsername(rs.getString("username"));
                appauth.setPassword(rs.getString("password"));
                authList.add(appauth);
                log.info("Application id :: " + appauth.getAppName());
                log.info("UserName :: " + appauth.getUsername());
                log.info("Password :: " + appauth.getPassword());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public List readSymmetricKeysFromDatabase() {
        log.debug("entering 'readSymmetricKeysFromDatabase' method...");
        List keys = new ArrayList();
        try {
            log.debug(getSessionFactory() == null ? "Session Factory is null" : "Session Factory is Not null");
            DataSource dsCitp = SessionFactoryUtils
                    .getDataSource(getSessionFactory());
            log.debug(dsCitp == null ? "DataSource is null" : "DataSource is Not null");
            connection = dsCitp.getConnection();
            String sql = "Select name,CERTIFICATEID, SVN_KEY, SOA_GROUP_ID from " + APP_KEY_TABLE + " where status='Active'";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Key key = new Key();
                key.setAppId(rs.getString("name"));
                //key.setEncryptedSymmetricKeyBytes(rs.getBytes("SVN_KEY"));
                key.setCertificateid(rs.getString("CERTIFICATEID"));
                Blob blob = rs.getBlob("SVN_KEY");
                //int soaGroupId = rs.
                InputStream inputStream = blob.getBinaryStream();
                int inByte;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte buffer[] = new byte[256];
                while ((inByte = inputStream.read(buffer)) != -1) {
                    bos.write(buffer);
                }
                key.setEncryptedSymmetricKeyBytes(bos.toByteArray());

                int soaGroupId = rs.getInt("SOA_GROUP_ID");

                log.debug(" app id " + key.getAppId() + " group id " + soaGroupId);
                key.setSoaGroupId(soaGroupId);

                inputStream.close();
                bos.close();
                keys.add(key);
            }
            log.debug("exiting 'readSymmetricKeysFromDatabase' method...");
            return keys;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error 'readSymmetricKeysFromDatabase' method...", e);
            return null;
        }

    }
/*
	public static DbUtilImpl getDbUtil() {
		if (dbUtil == null) {
			dbUtil = new DbUtilImpl();
		}
		return dbUtil;
	}
*/


    public static void main(String[] args) {
        DbUtilImpl dbutil = new DbUtilImpl();
    }
}
