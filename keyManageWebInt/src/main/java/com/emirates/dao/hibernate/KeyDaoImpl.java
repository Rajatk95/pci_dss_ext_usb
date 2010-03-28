package com.emirates.dao.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.dao.DataAccessException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.emirates.dao.KeyDao;
import com.emirates.model.Key;

import javax.sql.DataSource;

public class KeyDaoImpl extends HibernateDaoSupport implements KeyDao {
    protected final transient Log log = LogFactory.getLog(getClass());
    private String APP_KEY_TABLE = "SOA_APP_KEY";

    public List<Key> getkeyList() {
        // TODO Auto-generated method stub
        SimpleJdbcTemplate jdbcTemplate =
                new SimpleJdbcTemplate(SessionFactoryUtils.getDataSource(getSessionFactory()));
        HashMap args = new HashMap();
        return jdbcTemplate.query("select a.APP_ID,a.name,a.CERTIFICATEID,a.soa_group_id,c.soa_group_name from " + APP_KEY_TABLE + " a , SOA_GROUP c where status='Active' and a.soa_group_id=c.soa_group_id"
                , new ParameterizedRowMapper<Key>() {
                    public Key mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Key(rs.getString("APP_ID"), rs.getString("name"), rs.getString("soa_group_id"), rs.getString("soa_group_name"), rs.getString("CERTIFICATEID"));
                    }
                }, args);
    }

    public Key getkey(String appid) {
        // TODO Auto-generated method stub
        log.debug(getSessionFactory() == null ? "Session Factory is null" : "Session Factory is Not null");
        log.debug("Application id :" + appid);

        try {
            SimpleJdbcTemplate jdbcTemplate =
                    new SimpleJdbcTemplate(SessionFactoryUtils.getDataSource(getSessionFactory()));
            Map args = new HashMap();
            args.put("in1", new String(appid));

            return jdbcTemplate.queryForObject("select a.APP_ID,a.name,a.CERTIFICATEID,a.soa_group_id,c.soa_group_name from " + APP_KEY_TABLE + " a , SOA_GROUP c where a.status='Active' and a.soa_group_id=c.soa_group_id  and a.APP_ID=?"
                    , new ParameterizedRowMapper<Key>() {
                        public Key mapRow(ResultSet rs, int rowNum) throws SQLException {
                            return new Key(rs.getString("APP_ID"), rs.getString("name"), rs.getString("soa_group_id"), rs.getString("soa_group_name"), rs.getString("CERTIFICATEID"));
                        }
                    }, appid);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;//To change body of catch statement use File | Settings | File Templates.
        }
    }


    public boolean updateKey(Key key) {
        try {
            log.debug(getSessionFactory() == null ? "Session Factory is null" : "Session Factory is Not null");
            DataSource dsCitp = SessionFactoryUtils
                    .getDataSource(getSessionFactory());
            log.debug(dsCitp == null ? "DataSource is null" : "DataSource is Not null");
            Connection connection = dsCitp.getConnection();
            //String sql = "update " + APP_KEY_TABLE + " set name=?,CERTIFICATEID=?,soa_group_id=? where app_id=? ";
            String sql = "update " + APP_KEY_TABLE + " set soa_group_id=? where app_id=? ";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            //pstmt.setString(1, key.getName());
            //pstmt.setString(2, key.getCertificate_id());
            pstmt.setString(1, key.getGroup_id());
            pstmt.setString(2, key.getApp_id());
            pstmt.executeUpdate();
            pstmt.close();
            log.debug("exiting 'updateUser' method...");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error 'updateUser' method...", e);
            return false;
        }
    }

    public boolean updateUser(String appId, String userId) {
        try {
            log.debug(getSessionFactory() == null ? "Session Factory is null" : "Session Factory is Not null");
            DataSource dsCitp = SessionFactoryUtils
                    .getDataSource(getSessionFactory());
            log.debug(dsCitp == null ? "DataSource is null" : "DataSource is Not null");
            Connection connection = dsCitp.getConnection();
            String sql = "update " + APP_KEY_TABLE + " set app_user_id=? where app_id=? ";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, appId);
            pstmt.executeUpdate();
            pstmt.close();
            log.debug("exiting 'updateUser' method...");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error 'updateUser' method...", e);
            return false;
        }
    }


    public boolean deactivateApplication(String appId) {
        try {

            log.debug("Application Id For Deletion : " + appId);
            log.debug(getSessionFactory() == null ? "Session Factory is null" : "Session Factory is Not null");
            DataSource dsCitp = SessionFactoryUtils
                    .getDataSource(getSessionFactory());
            log.debug(dsCitp == null ? "DataSource is null" : "DataSource is Not null");
            Connection connection = dsCitp.getConnection();
            String sql = "update " + APP_KEY_TABLE + " set status='Inactive' where name=? ";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, appId);
            pstmt.executeUpdate();
            pstmt.close();
            log.debug("exiting 'updateUser' method...");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error 'updateUser' method...", e);
            return false;
        }
    }


}
