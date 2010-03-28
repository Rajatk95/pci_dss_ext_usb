package com.emirates.dao;

import com.emirates.model.Group;

import java.util.List;

/**
 * Created by EA Team.
 * User: s726580
 * Date: 19-Mar-2009
 * Time: 08:43:19
 * To change this template use File | Settings | File Templates.
 */
public interface GroupDao {
    List<Group> getGroupList();

    boolean deleteGroup(String groupId);

    boolean updateGroup(String groupId, String groupName);

    boolean saveGroup(String groupName);
}
