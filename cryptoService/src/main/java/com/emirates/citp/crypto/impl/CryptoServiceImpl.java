package com.emirates.citp.crypto.impl;

import com.emirates.citp.crypto.CryptoCommons;
import com.emirates.citp.crypto.CryptoService;
import com.emirates.citp.crypto.AESKeyManager;
import com.emirates.citp.crypto.AppAuth;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appspring.util.DbUtil;
import org.appspring.util.MyThreadLocal;

import javax.crypto.spec.SecretKeySpec;
import javax.jws.WebService;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.crypto.BadPaddingException;

@WebService(serviceName = "Cryptography", endpointInterface = "com.emirates.citp.crypto.CryptoService")
public class CryptoServiceImpl implements CryptoService {
    protected final transient Log log = LogFactory.getLog(getClass());

    private String apps[] = {"App1", "App2", "App3", "App4", "App5", "App6",
            "App7", "App8", "App9", "App10", "App11", "App12", "App13",
            "App14", "App15"};

    private List rsaKeys = new ArrayList();

    private List<AppAuth> appAuthList = new ArrayList<AppAuth>();


    public List<AppAuth> getAppAuthList() {
        return appAuthList;
    }// symmKeys list would hold instances of Key class, whereas Key instance

    public void setAppAuthList(List<AppAuth> appAuthList) {
        this.appAuthList = appAuthList;
    }

    // would only contain a mapping between SecretKeySpec and appId.
    private List symmKeys = new ArrayList();

    private CryptoCommons cryptoCommon;

    private AESKeyManager aesKeyManager;

    private DbUtil dbUtil;
    private static final String AUTHENTICATION_FAILURE = "Authentication Failure";


    public AESKeyManager getAesKeyManager() {
        return aesKeyManager;
    }

    public void setAesKeyManager(AESKeyManager aesKeyManager) {
        this.aesKeyManager = aesKeyManager;
    }

    public CryptoCommons getCryptoCommon() {
        return cryptoCommon;
    }

    public void setCryptoCommon(CryptoCommons cryptoCommon) {
        this.cryptoCommon = cryptoCommon;
    }


    public DbUtil getDbUtil() {
        return dbUtil;
    }

    public void setDbUtil(DbUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    public void init() throws Exception {
        log.debug("int method called ");
        String checkapp[] = dbUtil.getAllApplicationIdentifiers();
        log.debug("Application identifiers got");
        if (checkapp.length > 0) {
            this.rsaKeys.clear();
            this.symmKeys.clear();
            this.appAuthList.clear();
            cryptoCommon.loadRSAKeysFromFiles(apps, rsaKeys);
            cryptoCommon.getSymmetricKeysFromDatabase(symmKeys, rsaKeys, apps);
            //dbUtil.getApplicationAuthList(appAuthList);

        }

    }


    public String reintialize() {
        try {
            init();
            return "reintialize() Successfull";
        } catch (Exception e) {
            log.error("Error Occured During reintialize Phase", e);
            return "Error during reintialize() " + e.getMessage();
        }
    }


    public String hash(String appId, String text) throws NoSuchAlgorithmException, NoSuchProviderException {
        String certificateId = null;
        if (MyThreadLocal.get() != null) {
            certificateId = MyThreadLocal.get();
            log.debug("Certificate ID : " + certificateId);
        } else {
            log.debug("Issuer Name Is Null ");
            return AUTHENTICATION_FAILURE;
        }
        if (!checkForAuthentication(appId, certificateId)) {
            return AUTHENTICATION_FAILURE;
        }
        String result = text;
        MessageDigest messageDigest = null;
        if (text != null) {
            StringBuffer code = new StringBuffer(); // the hash code
            messageDigest = MessageDigest.getInstance("SHA-512");
            String plain = text;
            byte bytes[] = plain.getBytes();
            byte digest[] = messageDigest.digest(bytes); // create code
            for (int i = 0; i < digest.length; ++i) {
                code.append(Integer.toHexString(0x0100 + (digest[i] & 0x00FF))
                        .substring(1));
            }
            result = code.toString();
        }
        return result;
    }

    private String encrypt(SecretKeySpec secretKeySpec, String message) throws Exception {

        return this.aesKeyManager.encrypt(secretKeySpec, message);

    }

    private String decrypt(SecretKeySpec secretKeySpec, String encryptedData, String appId)
            throws Exception {
        try {
            return this.aesKeyManager.decrypt(secretKeySpec,
                    encryptedData);
        }
        catch (BadPaddingException e) {
            log.debug("BadPaddingException occured for appId " + appId);
            //getGroupOfKeys()
            /*This exception is thrown when a message is encrypted with one key and decrypted with another.
                   This situation is possible when more than one applications belong to one group and it is acceptable for
                   one application encrypting the message and another application decrypting the message, provided
                   both applications belong to same group.*/
            //step 1. get all the keys corresponding to applications belonging to same group.
            List groupOfKeys = getGroupOfKeys(appId);
            //step 2. Take each of the applications keys in a loop
            String ids = "";
            if (groupOfKeys != null && groupOfKeys.size() != 0) {
                Iterator iterator = groupOfKeys.iterator();
                while (iterator.hasNext()) {
                    Key key = (Key) iterator.next();
                    SecretKeySpec secretKeySpec1 = getDecryptedSymmetricKey(key.getAppId());
                    try {
                        return this.aesKeyManager.decrypt(secretKeySpec1, encryptedData);
                    } catch (BadPaddingException e1) {
                        log.debug("BadPaddingException occured for appId " + key.getAppId());
                        ids += ". " + key.getAppId();
                    }


                }
                return "Not able to decrypt with following appIds " + ids;
            }

        }
        return null;
    }

    public String decrypt(String appId, String encryptedData) throws Exception {
        String certificateId = null;
        if (MyThreadLocal.get() != null) {
            certificateId = MyThreadLocal.get();
            log.debug("Certificate ID : " + certificateId);
        } else {
            log.debug("Issuer Name Is Null ");
            return AUTHENTICATION_FAILURE;
        }
        if (!checkForAuthentication(appId, certificateId)) {
            return AUTHENTICATION_FAILURE;
        }
        SecretKeySpec secretKeySpec = getDecryptedSymmetricKey(appId);
        return decrypt(secretKeySpec, encryptedData, appId);
    }

    /**
     * Returns a list of all Keys that share the same group Id
     *
     * @param appId
     * @return
     */
    private List getGroupOfKeys(String appId) {
        int groupId = getGroupId(appId);
        Iterator iterator = symmKeys.iterator();
        List group = new ArrayList();
        if (groupId == 0)
            return group;
        while (iterator.hasNext()) {
            Key key = (Key) iterator.next();
            if (key.getSoaGroupId() == groupId) {
                group.add(key);
                //log.debug("key (appln) added to a grouip "+key.getAppId() );
            }
        }

        return group;
    }

    /**
     * Get the group Id against the appId passed. (More that one application can belong to the same group)
     *
     * @param appId
     * @return 0 if application id passed does not correspond to any group.
     */
    private int getGroupId(String appId) {
        Iterator iterator = symmKeys.iterator();
        List group = new ArrayList();
        while (iterator.hasNext()) {
            Key key = (Key) iterator.next();
            if (key.getAppId() != null && key.getAppId().equalsIgnoreCase(appId)) {
                log.debug(" Group Id returned is " + key.getSoaGroupId());
                return key.getSoaGroupId();
            }
        }
        return 0;
    }

    public String encrypt(String appId, String message) throws Exception {
        log.debug("In encrypt method appId is ......" + appId + " message is " + message);
        String certificateId = null;
        if (MyThreadLocal.get() != null) {
            certificateId = MyThreadLocal.get();
            log.debug("Certificate ID : " + certificateId);
        } else {
            log.debug("Issuer Name Is Null ");
            return AUTHENTICATION_FAILURE;
        }
        if (!checkForAuthentication(appId, certificateId)) {
            return AUTHENTICATION_FAILURE;
        }

        /*
        * Get the corresponding private Key to decrypt the encrypted symmetric key
        */
        SecretKeySpec secretKeySpec = getDecryptedSymmetricKey(appId);
        log.debug(" appId passed is " + appId + " secretKeySpec is " + secretKeySpec);
        /*
               * Decrypt the encrypted symmetric key using the private key.
               */
        return encrypt(secretKeySpec, message);

    }

    /**
     * The method decrypts the symmetric jkey got from the database.
     */
    private SecretKeySpec getDecryptedSymmetricKey(String appId) {
        try {
            if (rsaKeys.isEmpty()) {
                cryptoCommon.loadRSAKeysFromFiles(apps, rsaKeys);
            }
            if (symmKeys.isEmpty()) {
                cryptoCommon.getSymmetricKeysFromDatabase(symmKeys, rsaKeys, apps);

            }
            Iterator iterator = symmKeys.iterator();
            while (iterator.hasNext()) {
                Key key = (Key) iterator.next();
                if (key.getAppId() != null
                        && key.getAppId().equalsIgnoreCase(appId)) {
                    log.debug(" appId is " + key.getAppId());
                    return key.getAesKeySpec();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }


    private boolean checkForAuthentication(String appId, String certificateId) {
        log.debug("In checkForAuthentication method");
        log.debug("this.appAuthList size " + this.appAuthList.size());
        boolean checkAuth = false;
        Iterator iterator = symmKeys.iterator();
        while (iterator.hasNext()) {
            Key key = (Key) iterator.next();
            if (key.getCertificateid() != null
                    && key.getCertificateid().equalsIgnoreCase(certificateId)
                    && key.getAppId().equalsIgnoreCase(appId)) {
                checkAuth = true;
            }
        }

        return checkAuth;

    }

    private String encrypt(String text) {
        String result = text;
        MessageDigest messageDigest = null;
        if (text != null) {
            StringBuffer code = new StringBuffer(); // the hash code
            try {
                messageDigest = MessageDigest.getInstance("SHA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            String plain = text;
            byte bytes[] = plain.getBytes();
            byte digest[] = messageDigest.digest(bytes); // create code
            for (int i = 0; i < digest.length; ++i) {
                code.append(Integer.toHexString(0x0100 + (digest[i] & 0x00FF))
                        .substring(1));
            }
            result = code.toString();
        }
        return result;

    }


}
