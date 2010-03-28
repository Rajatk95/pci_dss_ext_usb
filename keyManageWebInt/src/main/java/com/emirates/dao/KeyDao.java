package com.emirates.dao;

import java.util.List;

import com.emirates.model.Key;

public interface KeyDao {
    List<Key> getkeyList();

    public boolean updateUser(String appId, String userId);

    public Key getkey(String appid);

    public boolean updateKey(Key key);

    public boolean deactivateApplication(String appId);
}
