package com.emirates.service;

import java.util.List;

import com.emirates.model.Key;


public interface KeyManager extends UniversalManager {
    List<Key> getKeys();

    public Key getkey(String appid);

    public boolean updateUser(String appId, String userId);

    public boolean deactivateApplication(String appId);

    public boolean updateKey(Key key);
}
