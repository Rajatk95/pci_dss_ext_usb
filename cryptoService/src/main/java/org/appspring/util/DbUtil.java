package org.appspring.util;

import com.emirates.citp.crypto.AppAuth;
import com.emirates.citp.crypto.impl.Key;

import java.util.List;
import java.sql.Connection;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

public interface DbUtil {

    //@Transactional(propagation = Propagation.NESTED)
    //public boolean saveSymmetricKeysInDatabase2(List keys,Connection connection);


    public boolean saveSymmetricKeysInDatabase(List keys, Connection connection) throws Exception;

    public boolean saveSymmetricKey(Key key, String certificateId, String groupId, Connection connection) throws Exception;

    public List readSymmetricKeysFromDatabase();

    public boolean deleteAllSymmetricKeysFromDatabase(Connection connection) throws Exception;

    public boolean deleteSymmetricKeyFromDatabase(String appId, Connection connection) throws Exception;

    public String[] getAllApplicationIdentifiers();

    public boolean getApplicationAuthList(List<AppAuth> authList);

    public void commit(Connection connection);

    public void rollback(Connection connection);

    public Connection getConnection();


}
