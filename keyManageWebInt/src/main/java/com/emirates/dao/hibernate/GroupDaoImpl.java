package com.emirates.dao.hibernate;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.emirates.dao.GroupDao;
import com.emirates.model.Group;
import com.emirates.model.Key;

import javax.sql.DataSource;
import java.util.List;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by EA Team.
 * User: s726580
 * Date: 19-Mar-2009
 * Time: 08:45:59
 * To change this template use File | Settings | File Templates.
 */
public class GroupDaoImpl extends HibernateDaoSupport implements GroupDao {
    protected final transient Log log = LogFactory.getLog(getClass());


    public List<Group> getGroupList() {
        SimpleJdbcTemplate jdbcTemplate =
                new SimpleJdbcTemplate(SessionFactoryUtils.getDataSource(getSessionFactory()));
        HashMap args = new HashMap();
        return jdbcTemplate.query("select * from SOA_GROUP"
                , new ParameterizedRowMapper<Group>() {
                    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Group(rs.getString("soa_group_id"), rs.getString("soa_group_name"));
                    }
                }, args);

    }

    public boolean deleteGroup(String groupId) {
        try {

            log.debug("Group Id For Deletion : " + groupId);
            log.debug(getSessionFactory() == null ? "Session Factory is null" : "Session Factory is Not null");
            DataSource dsCitp = SessionFactoryUtils
                    .getDataSource(getSessionFactory());
            log.debug(dsCitp == null ? "DataSource is null" : "DataSource is Not null");
            Connection connection = dsCitp.getConnection();
            String sql = "delete from soa_group where soa_group_id=? ";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, groupId);
            pstmt.executeUpdate();
            pstmt.close();
            log.debug("exiting 'deleteGroup' method...");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error 'deleteGroup' method...", e);
            return false;
        }

    }

    public boolean updateGroup(String groupId, String groupName) {
        try {

            log.debug("updateGroup Id For  : " + groupId);
            log.debug(getSessionFactory() == null ? "Session Factory is null" : "Session Factory is Not null");
            DataSource dsCitp = SessionFactoryUtils
                    .getDataSource(getSessionFactory());
            log.debug(dsCitp == null ? "DataSource is null" : "DataSource is Not null");
            Connection connection = dsCitp.getConnection();
            String sql = "update soa_group set soa_group_name=? where soa_group_id=? ";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, groupName);
            pstmt.setString(2, groupId);
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

    public boolean saveGroup(String groupName) {
        try {

            log.debug("New Group Created For  : " + groupName);
            log.debug(getSessionFactory() == null ? "Session Factory is null" : "Session Factory is Not null");
            DataSource dsCitp = SessionFactoryUtils
                    .getDataSource(getSessionFactory());
            log.debug(dsCitp == null ? "DataSource is null" : "DataSource is Not null");
            Connection connection = dsCitp.getConnection();
            String sql = "insert into soa_group (soa_group_id,soa_group_name) values(group_seq.nextval,?) ";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, groupName);
            pstmt.executeUpdate();
            pstmt.close();
            log.debug("exiting 'saveGroup' method...");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error 'SaveGroup' method...", e);
            return false;
        }
    }
}
