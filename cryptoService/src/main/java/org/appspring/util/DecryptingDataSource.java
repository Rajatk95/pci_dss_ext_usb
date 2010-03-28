package org.appspring.util;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.DelegatingDataSource;

public class DecryptingDataSource extends DelegatingDataSource {
    protected final transient Log log = LogFactory.getLog(getClass());

    public Connection getConnection() throws SQLException {
        log.info("Get Connection Called without username and password");
        BasicDataSource ds = (BasicDataSource) getTargetDataSource();
        if (ds.getPassword().length() != 0) {
            return getConnection(ds.getUsername(), ds.getPassword());
        }
        log.info("Username Before Decryption" + ds.getUsername());
        log.info("password Before Decryption" + ds.getPassword());
        //String decryptedPassword = DesEncrypter.decryptDbPassword(ds.getPassword());
        //super.setPassword(decryptedPassword);
        //ds.setPassword(decryptedPassword);
        log.info("Username After Decryption" + ds.getUsername());
        log.info("password After Decryption" + ds.getPassword());

        return ds.getConnection();
    }

    public Connection getConnection(String username, String password) throws SQLException {

        log.info("Get Connection Called with username and password");
        BasicDataSource ds = (BasicDataSource) getTargetDataSource();
        log.info("Username Before Decryption" + username);
        log.info("password Before Decryption" + password);

        String decryptedPassword = DesEncrypter.decryptDbPassword(password);
        ds.setPassword(decryptedPassword);
        log.info("Username After Decryption" + username);
        log.info("password Length After Decryption" + decryptedPassword.length());
        log.info("password After Decryption" + decryptedPassword);

        return ds.getConnection(username, decryptedPassword.trim());
        //return super.getConnection(username, decryptedPassword);
    }


}
