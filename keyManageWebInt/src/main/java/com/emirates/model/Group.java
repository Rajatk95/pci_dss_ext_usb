package com.emirates.model;

/**
 * Created by EA Team.
 * User: s726580
 * Date: 19-Mar-2009
 * Time: 08:37:18
 * To change this template use File | Settings | File Templates.
 */
public class Group {

    private String groupId;
    private String groupName;

    public Group() {
    }

    public Group(String groupId, String groupName) {

        this.groupId = groupId;
        this.groupName = groupName;
    }

    public String getGroupId() {

        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
