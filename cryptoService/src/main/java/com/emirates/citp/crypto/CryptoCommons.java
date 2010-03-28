package com.emirates.citp.crypto;

import java.security.interfaces.RSAPrivateKey;
import java.util.List;

public interface CryptoCommons {
    public boolean getSymmetricKeysFromDatabase(List symmKeys, List rsaKeys, String[] apps) throws Exception;

    public boolean loadRSAKeysFromFiles(String[] apps, List rsaKeys) throws Exception;

    public RSAPrivateKey getRSAPrivateKey(String appId, List rsaKeys, String[] apps);

}
