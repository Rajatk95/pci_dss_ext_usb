package com.emirates.model;

import java.io.File;

public class Key {

    private String app_id;
    private String name;
    private String group_id;
    private String groupname;
    private String certificate_id;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    private boolean passwordChangeRequired = false;
    private boolean regenerationMode = false;
    private File file;


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
    
    
    public boolean isPasswordChangeRequired() {
		return passwordChangeRequired;
	}

	public void setPasswordChangeRequired(boolean passwordChangeRequired) {
		this.passwordChangeRequired = passwordChangeRequired;
	}

	public boolean isRegenerationMode() {
        return regenerationMode;
    }

    public void setRegenerationMode(boolean regenerationMode) {
        this.regenerationMode = regenerationMode;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }


    public Key(String app_id, String name, String group_id, String groupname, String certificate_id) {
        this.app_id = app_id;
        this.name = name;
        this.group_id = group_id;
        this.groupname = groupname;
        this.certificate_id = certificate_id;
    }


    public String getCertificate_id() {
        return certificate_id;
    }

    public void setCertificate_id(String certificate_id) {
        this.certificate_id = certificate_id;
    }


    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public Key(String app_id, String name) {
        super();
        this.app_id = app_id;
        this.name = name;
    }

    public Key() {
        super();
        // TODO Auto-generated constructor stub
    }


}
