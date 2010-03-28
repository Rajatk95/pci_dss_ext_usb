package com.emirates.citp.crypto;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import javax.jws.WebService;

@WebService
public interface KeyGeneration {

    @Transactional
    public String generatePrivateKeys();

    public String regeneratePrivateKeys();

    //@Transactional(propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
    //public String generatePrivateKey(String appId);
    public String generatePrivateKey(String appName, String certificateId, String groupId);

    public String regeneratePrivateKey(String appId);

    public boolean keyDeletion(String appid) throws Exception;
}
