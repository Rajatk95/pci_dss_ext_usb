package com.emirates.service.impl;

import com.emirates.service.GroupManager;
import com.emirates.model.Group;
import com.emirates.dao.GroupDao;

import java.util.List;

/**
 * Created by EA Team.
 * User: s726580
 * Date: 19-Mar-2009
 * Time: 08:50:33
 * To change this template use File | Settings | File Templates.
 */
public class GroupManagerImpl extends UniversalManagerImpl implements GroupManager {

    private GroupDao groupDao;

    public GroupDao getGroupDao() {
        return groupDao;
    }

    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public List<Group> getGroupList() {
        return groupDao.getGroupList();  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean deleteGroup(String groupId) {
        return groupDao.deleteGroup(groupId);
    }

    public boolean updateGroup(String groupId, String groupName) {
        return groupDao.updateGroup(groupId, groupName);
    }

    public boolean saveGroup(String groupName) {
        return groupDao.saveGroup(groupName);
    }

    public Group getGroup(String groupid, List<Group> groupList) {
        for (Group group : groupList) {
            if (group.getGroupId().equalsIgnoreCase(groupid)) {
                return group;
            }
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
