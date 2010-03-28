package com.emirates.service;

import com.emirates.model.Group;

import java.util.List;

/**
 * Created by EA Team.
 * User: s726580
 * Date: 19-Mar-2009
 * Time: 08:40:57
 * To change this template use File | Settings | File Templates.
 */
public interface GroupManager extends UniversalManager {

    List<Group> getGroupList();

    boolean deleteGroup(String groupId);

    boolean updateGroup(String groupId, String groupName);

    boolean saveGroup(String groupName);

    Group getGroup(String groupid, List<Group> groupList);

}
