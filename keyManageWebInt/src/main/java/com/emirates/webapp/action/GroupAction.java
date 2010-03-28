package com.emirates.webapp.action;

import com.emirates.model.Group;
import com.emirates.service.GroupManager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by EA Team.
 * User: s726580
 * Date: 19-Mar-2009
 * Time: 08:36:28
 * To change this template use File | Settings | File Templates.
 */
public class GroupAction extends BaseAction {

    private List<Group> groupList;

    private GroupManager groupManager;

    private Group group;


    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    public String list() {
        this.groupList = groupManager.getGroupList();
        return SUCCESS;
    }

    public String delete() {
        HttpServletRequest request = getRequest();
        String groupid = request.getParameter("id");
        if (groupid != null) {
            log.debug("deleteKey :: " + groupid);
            if (groupManager.deleteGroup(groupid)) {
                saveMessage("Successfull Delete Group : " + groupid);
            } else {
                saveMessage("Falied To Delete Group : " + groupid + ".Please check application logs for details");
            }
        } else {
            saveMessage("Falied To Delete Group : " + groupid + ".Please check application logs for details");
        }
        return list();
    }

    public String createNewGroup() {
        this.group = new Group();
        return SUCCESS;
    }

    public String editNewGroup() {
        HttpServletRequest request = getRequest();
        String groupid = request.getParameter("id");
        if (groupid != null) {
            this.group = groupManager.getGroup(groupid, groupManager.getGroupList());

        }
        return SUCCESS;
    }

    public String save() {


        if (this.group.getGroupId().trim().length() == 0) {
            //save new group
            if (groupManager.saveGroup(this.group.getGroupName())) {
                saveMessage("New Group Created");
            } else {
                saveMessage("Failed to create new Group .Please check application logs for details ");
            }
        } else {
            //edit existing group
            log.info("Group Id to Be Edited : " + this.group.getGroupId());
            if (groupManager.updateGroup(this.group.getGroupId(), this.group.getGroupName())) {
                saveMessage("Group Was Editied Successfully");
            } else {
                saveMessage("Failed to Edit Group .Please check application logs for details ");
            }

        }
        return list();
    }

}
