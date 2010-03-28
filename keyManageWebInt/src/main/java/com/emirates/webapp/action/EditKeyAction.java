package com.emirates.webapp.action;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.codehaus.xfire.client.Client;

import com.emirates.Constants;
import com.emirates.model.Group;
import com.emirates.model.Key;
import com.emirates.service.GroupManager;
import com.emirates.service.KeyManager;

public class EditKeyAction extends BaseAction {
	
	
    private Key key;
    public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	@SkipValidation
    public String editKey() {
        HttpServletRequest request = getRequest();
        String appid = request.getParameter("id");
        this.groupList = groupManager.getGroupList();
        this.key = keyManager.getkey(appid);
        return SUCCESS;
    }
    
    public String save() {
        //log.debug("Saved the application : " + key.getApp_id());
        if (keyManager.updateKey(key)) {
            saveMessage("Keys Have been edited Successfully");
        } else {
            saveMessage("Failed to edit the keys .please check the logs for more details");
        }
        return list();
    }
    
	public String cancel() {
		return "cancel";
	}
    /**
     * Grab the entity from the database before populating with request parameters
     */
    public void prepare() {
        if (getRequest().getMethod().equalsIgnoreCase("post")) {
            // prevent failures on new
        	log.info("Key Application Id For Prepare :: " + getRequest().getParameter("key.app_id"));
            if (!"".equals(getRequest().getParameter("key.app_id"))) {
                key = keyManager.getkey(getRequest().getParameter("key.app_id"));
            }
        }
    }




}
