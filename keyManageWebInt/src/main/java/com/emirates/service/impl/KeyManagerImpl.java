package com.emirates.service.impl;

import java.util.List;

import com.emirates.dao.KeyDao;
import com.emirates.model.Key;
import com.emirates.service.KeyManager;

public class KeyManagerImpl extends UniversalManagerImpl implements KeyManager {

    private KeyDao keyDao;

    public KeyDao getKeyDao() {
        return keyDao;
    }

    public void setKeyDao(KeyDao keyDao) {
        this.keyDao = keyDao;
    }

    public List<Key> getKeys() {
        // TODO Auto-generated method stub
        return keyDao.getkeyList();
    }


    public Key getkey(String appid) {
        return keyDao.getkey(appid);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean updateUser(String appId, String userId) {
        return keyDao.updateUser(appId, userId);
    }

    public boolean deactivateApplication(String appId) {
        return keyDao.deactivateApplication(appId);  //To change body of implemented methods use File | Settings | File Templates.
    }


    public boolean updateKey(Key key) {
        return keyDao.updateKey(key);  //To change body of implemented methods use File | Settings | File Templates.
    }

}
