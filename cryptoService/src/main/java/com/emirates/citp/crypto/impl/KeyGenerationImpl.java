package com.emirates.citp.crypto.impl;

import com.emirates.citp.crypto.CryptoCommons;
import com.emirates.citp.crypto.KeyGeneration;
import com.astrel.io.atomic.TransactionManager;
import com.astrel.io.atomic.Transaction;
import com.astrel.io.atomic.TransactionException;
import com.astrel.io.atomic.InconsistentStateException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appspring.util.DbUtil;
import org.appspring.util.DateUtil;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.jws.WebService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


@WebService(serviceName = "KeyGenereation", endpointInterface = "com.emirates.citp.crypto.KeyGeneration")
public class KeyGenerationImpl implements KeyGeneration {
    protected final transient Log log = LogFactory.getLog(getClass());

    private String newapps[] = {"App1", "App2", "App3", "App4", "App5", "App6",
            "App7", "App8", "App9", "App10", "App11", "App12", "App13",
            "App14", "App15"};


    private String apps[] = {"App1", "App2", "App3", "App4", "App5", "App6",
            "App7", "App8", "App9", "App10", "App11", "App12", "App13",
            "App14", "App15"};

    private List rsaKeys = new ArrayList();

    // symmKeys list would hold instances of Key class, whereas Key instance
    // would only contain a mapping between SecretKeySpec and appId.
    private List symmKeys = new ArrayList();

    private DbUtil dbUtil;

    private CryptoCommons cryptoCommon;

    private String privateKeyFolder;

    private String publicKeyFolder;

    private String txBackup;

    private String txJournal;

    public String getTxBackup() {
        return txBackup;
    }

    public void setTxBackup(String txBackup) {
        this.txBackup = txBackup;
    }

    public String getTxJournal() {
        return txJournal;
    }

    public void setTxJournal(String txJournal) {
        this.txJournal = txJournal;
    }

    public String getPrivateKeyFolder() {
        return privateKeyFolder;
    }

    public void setPrivateKeyFolder(String privateKeyFolder) {
        this.privateKeyFolder = privateKeyFolder;
    }

    public String getPublicKeyFolder() {
        return publicKeyFolder;
    }

    public void setPublicKeyFolder(String publicKeyFolder) {
        this.publicKeyFolder = publicKeyFolder;
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
        log.debug("Setter Method Called");
        this.dbUtil = dbUtil;
    }

    public void init() throws Exception {

        String checkapp[] = dbUtil.getAllApplicationIdentifiers();
        if (checkapp.length > 0) {
            cryptoCommon.loadRSAKeysFromFiles(apps, rsaKeys);
            cryptoCommon.getSymmetricKeysFromDatabase(symmKeys, rsaKeys, apps);
        }

    }


    /**
     * The method is to be invoked the first time. The method does the following -
     * 1. Generate pub/pvt keys for all the applications.
     * 2. Generate corresponding symmetric keys for all the applications.
     */
    public String generatePrivateKeys() {

        Connection connection = null;
        TransactionManager tmprivate = null;
        Transaction tprivate = null;
        TransactionManager tmpublic = null;
        Transaction tpublic = null;
        try {

            log.debug("Generation Of Keys Started");
            List<Key> keys = generateSymmAndPubPvtKeysForApplications();
            log.debug("Pub/Pvt Keys and Symm Keys generated");
            //delete old keys from atabase
            connection = dbUtil.getConnection();
            deleteAllSymmetricKeysFromDatabase(connection);
            log.debug("Old Symm keys deleted");
            saveKeysInDatabase(keys, connection);
            log.debug("Keys saved in database");
            tmprivate = new TransactionManager(this.txJournal, this.txBackup);
            tprivate = tmprivate.beginTransaction();
            tmpublic = new TransactionManager(this.txJournal, this.txBackup);
            ;
            tpublic = tmpublic.beginTransaction();

            saveRSAKeysInFile(keys, tprivate, tpublic);
            log.debug("RSA keys saved in files/usb");
            dbUtil.commit(connection);
            tprivate.shouldCommit(true);
            tpublic.shouldCommit(true);
            cryptoCommon.loadRSAKeysFromFiles(newapps, rsaKeys);//This will put the RSA pvt keys into application memory
            log.debug("RSA keys loaded from files/usb");
            cryptoCommon.getSymmetricKeysFromDatabase(symmKeys, rsaKeys, newapps);//This will put the Symmetric keys into application memory
            log.debug("Generation Of Keys Completed");


            return "Key Generation Succeeded";
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error Occured During Generation Of Keys ", e);
            dbUtil.rollback(connection);
            return e.getMessage();
        } finally {
            try {
                tprivate.end();
                tpublic.end();
            } catch (TransactionException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InconsistentStateException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


        }

    }

    /**
     * The method is to be invoked whenever the pub/private keys needs to be regenerated later on. The method does the following -
     * 1. Re-generate pub/pvt keys for all the applications.
     * 2. Fetch emcrypted symmetric keys from database.
     * 3. Decrypt encrypted symmetric keys fetched above with the old private keys.
     * 4. Encrypt the symmetric keys got in step 3 with the newly generated private keys.
     * 5. Save the newly generate pub/pvt keys in files/usb
     * 6. Save the newly encrypted symmetric keys into database.
     */
    public String regeneratePrivateKeys() {
        Connection connection = null;
        TransactionManager tmprivate = null;
        Transaction tprivate = null;
        TransactionManager tmpublic = null;
        Transaction tpublic = null;

        try {
            log.debug("ReGeneration Of Keys Started");
            List newPubPvtKeys = regeneratePubPvtKeysAndEncryptSymmeticKeys();
            log.debug("Pub/Pvt Keys re-generated");
            //delete old keys
            connection = dbUtil.getConnection();
            deleteAllSymmetricKeysFromDatabase(connection);
            log.debug("Old Symm keys deleted");
            saveKeysInDatabase(newPubPvtKeys, connection);
            log.debug("Keys saved in database");
            tmprivate = new TransactionManager(this.txJournal, this.txBackup);
            tprivate = tmprivate.beginTransaction();
            tmpublic = new TransactionManager(this.txJournal, this.txBackup);
            ;
            tpublic = tmpublic.beginTransaction();

            /**
             * This method has to be updated to save the pub/pvt keys in a usb drive.
             */
            saveRSAKeysInFile(newPubPvtKeys, tprivate, tpublic);
            log.debug("RSA keys saved in files/usb");
            dbUtil.commit(connection);
            tprivate.shouldCommit(true);
            tpublic.shouldCommit(true);
            cryptoCommon.loadRSAKeysFromFiles(apps, rsaKeys);//This will put the RSA pvt keys into application memory
            log.debug("RSA keys loaded from files/usb");//This will put the Symmetric keys into application memory
            cryptoCommon.getSymmetricKeysFromDatabase(symmKeys, rsaKeys, apps);
            log.debug("Generation Of Keys Completed");

            return "Key Re-Generation Succeeded";
        }
        catch (Exception e) {
            // TODO: handle exception
            log.error("Error Occured During ReGeneration Of Keys ", e);
            dbUtil.rollback(connection);
            return e.getMessage();
        } finally {
            try {
                tprivate.end();
                tpublic.end();
            } catch (TransactionException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InconsistentStateException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


        }
    }

    /**
     * Will do the following -
     * 1. Generate a pub/pvt RSA key pair for the application.
     * 2. Generate a symmetric AES key for the application
     * 3. Encrypt the symmetric key using the generated pub/pvt RSA key pair
     * 4. Store the symmetric key in database in encrypted format.
     * 5. Store the pub/pvt key in filesystem/usb
     * 6. Hold the pub/pvt key in application memory
     *
     * @param appId
     * @return
     */

    public String generatePrivateKey(String appName, String certificateId, String groupId) {
        TransactionManager tmprivate = null;
        Transaction tprivate = null;
        TransactionManager tmpublic = null;
        Transaction tpublic = null;

        if (Util.getUtil().checkIfExisting(getRsaKeys(), appName)) {
            return "Application Identifier already existing. Please suggest another name";
        }
        Connection connection = null;
        try {
            RSAKey rsaKey = RSAKeyManager.getRSAKeyManager().generatePublicAndPrivateKeys();
            rsaKey.setAppId(appName);
            SecretKey symmetricKey = AESKeyManagerImpl.genearteKey(256);

            byte encryptedSymmKey[] = RSAKeyManager.getRSAKeyManager()
                    .encryptSymmetricKeyInBytes(symmetricKey,
                            rsaKey.getPublicKey());
            Key key = new Key();
            key.setEncryptedSymmetricKeyBytes(encryptedSymmKey);
            key.setRsaKey(rsaKey);
            key.setAppId(appName);
            List keys = new ArrayList();
            keys.add(key);
            //Store the symm key in database
            connection = dbUtil.getConnection();
            dbUtil.saveSymmetricKey(key, certificateId, groupId, connection);
            tmprivate = new TransactionManager(this.txJournal, this.txBackup);
            tprivate = tmprivate.beginTransaction();
            tmpublic = new TransactionManager(this.txJournal, this.txBackup);
            tpublic = tmpublic.beginTransaction();

            //Store the pub/pvt key in file system
            saveRSAKeysInFile(keys, tprivate, tpublic);
            log.debug("RSA key saved in files/usb");
            dbUtil.commit(connection);
            tprivate.shouldCommit(true);
            tpublic.shouldCommit(true);

            //loadRSAKeysFromFiles();// This is needed as newly created RSA pub/pvt key can be held in application memory
            List rsakeys = Util.getUtil().loadRSAKeyFromFile(appName, getRsaKeys(), publicKeyFolder, privateKeyFolder);//This is needed as newly created RSA pub/pvt key can be held in application memory
            setRsaKeys(rsakeys);
            log.debug("RSA key loaded from files/usb");
            cryptoCommon.getSymmetricKeysFromDatabase(symmKeys, rsaKeys, apps);// This is needed as newly created symmetric key can be held in application memory

            return "Key Generation for AppId - " + appName + " succeeded";
        }
        catch (Exception e) {
            e.printStackTrace();
            dbUtil.rollback(connection);
            return e.getMessage();
        } finally {
            try {
                tprivate.end();
                tpublic.end();
            } catch (TransactionException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InconsistentStateException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


        }
    }

    /**
     * Will do the following -
     * 1. Generate a pub/pvt RSA key pair for the application.
     * 2. Retrieve the symmetric key corresponding to the application Id
     * 3. Encrypt the symmetric key using the generated pub RSA key
     * 4. Store the symmetric key in database in encrypted format.
     * 5. Store the pub/pvt key in filesystem/usb
     * 6. Hold the pub/pvt key in application memory
     *
     * @param appId
     * @return
     */
    public String regeneratePrivateKey(String appId) {
        TransactionManager tmprivate = null;
        Transaction tprivate = null;
        TransactionManager tmpublic = null;
        Transaction tpublic = null;

        if (!Util.getUtil().checkIfExisting(getRsaKeys(), appId)) {
            return "Application Identifier not existing.";
        }
        Connection connection = null;
        try {
            RSAKey rsaKey = RSAKeyManager.getRSAKeyManager().generatePublicAndPrivateKeys();
            rsaKey.setAppId(appId);
            //Get symmetric key
            SecretKeySpec symmetricKeySpec = getDecryptedActualSymmetricKeySpec(appId);
            RSAPublicKey rsaPublicKey = rsaKey.getPublicKey();
            //Encrypt the symmetric key using the generated pub RSA key
            /**
             * The RSA public key is used to encrypt the symmetric key.
             */
            byte[] encryptedSymmKey = RSAKeyManager.getRSAKeyManager().encryptSymmetricKeyInBytes(symmetricKeySpec, rsaPublicKey);
            Key key = new Key();
            key.setEncryptedSymmetricKeyBytes(encryptedSymmKey);
            key.setRsaKey(rsaKey);
            key.setAppId(appId);
            connection = dbUtil.getConnection();
            //Store the symmetric key in database in encrypted format.
            dbUtil.deleteSymmetricKeyFromDatabase(appId, connection); //Deleting the old symmetric key
            List keys = new ArrayList();
            keys.add(key);
            dbUtil.saveSymmetricKeysInDatabase(keys, connection); // This will save the keys
            tmprivate = new TransactionManager(this.txJournal, this.txBackup);
            tprivate = tmprivate.beginTransaction();
            tmpublic = new TransactionManager(this.txJournal, this.txBackup);
            tpublic = tmpublic.beginTransaction();

            saveRSAKeysInFile(keys, tprivate, tpublic);// This will save the RSA keys into file system/usb
            log.debug("RSA key saved in files/usb");
            dbUtil.commit(connection);
            tprivate.shouldCommit(true);
            tpublic.shouldCommit(true);

            //loadRSAKeysFromFiles();// This will load all the RSA keys from the filesystem/usb into application memory
            List rsakeys = Util.getUtil().loadRSAKeyFromFile(appId, getRsaKeys(), publicKeyFolder, privateKeyFolder);//This is needed as newly created RSA pub/pvt key can be held in application memory
            setRsaKeys(rsakeys);
            log.debug("RSA key loaded from files/usb");
            //This will fetch all the symmetric keys from database and decrypt it and hold it in application memory
            cryptoCommon.getSymmetricKeysFromDatabase(symmKeys, rsaKeys, apps);
            return "Key Re-Generation for AppId - " + appId + " succeeded";
        }
        catch (Exception e) {
            e.printStackTrace();
            dbUtil.rollback(connection);
            return e.getMessage();
        } finally {
            try {
                tprivate.end();
                tpublic.end();
            } catch (TransactionException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InconsistentStateException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


        }

    }

    /**
     * The method does fokllowing -
     * Re-generate pub/pvt keys for all the applications.
     * Fetch emcrypted symmetric keys from database.
     * Decrypt encrypted symmetric keys fetched above with the old private keys.
     * Encrypt the symmetric keys got in step 3 with the newly generated private keys.
     */
    private List regeneratePubPvtKeysAndEncryptSymmeticKeys() throws Exception {
        List newPubPvtKeys = generatePubPvtKeysForApplications();//Regenerate new pub/pvt keys
        boolean success = cryptoCommon.getSymmetricKeysFromDatabase(symmKeys, rsaKeys, apps);// Get encrypted symmetric keys from database
        List newEncryptedSymmetricKeys = new ArrayList();
        if (success && cryptoCommon.loadRSAKeysFromFiles(apps, rsaKeys)) {// Load the pub/pvt keys from files/USB drives
            apps = dbUtil.getAllApplicationIdentifiers();
            //for(int i=0;i<apps.length;i++ ){
            for (int i = 0; i < apps.length; i++) {
                RSAPrivateKey privateKey = cryptoCommon.getRSAPrivateKey(apps[i], rsaKeys, apps);//Get existing/old private key
                SecretKeySpec symmetricKeySpec = getDecryptedActualSymmetricKeySpec(apps[i]);
                RSAKey rsaKey = getRSAKey(apps[i], newPubPvtKeys);
                RSAPublicKey rsaPublicKey = rsaKey.getPublicKey();
                /**
                 * The RSA public key is used to encrypt the symmetric key.
                 */
                byte[] encryptedSymmKey = RSAKeyManager.getRSAKeyManager().encryptSymmetricKeyInBytes(symmetricKeySpec, rsaPublicKey);
                Key key = new Key();
                key.setEncryptedSymmetricKeyBytes(encryptedSymmKey);
                key.setRsaKey(rsaKey);
                key.setAppId(apps[i]);
                newEncryptedSymmetricKeys.add(key);


            }
            //Call a save routine to save RSA keys into file system/usb drive.
            //saveRSAKeysInFile(newEncryptedSymmetricKeys);
            //Call the save routine to save it into d/b. Unit test after usb drive integration
            //saveKeysInDatabase(newEncryptedSymmetricKeys);


        }
        return newEncryptedSymmetricKeys;
    }

    /**
     *
     */
    private SecretKeySpec getDecryptedActualSymmetricKeySpec(String appId) {
        Iterator iterator = symmKeys.iterator();
        while (iterator.hasNext()) {
            Key key = (Key) iterator.next();
            if (key.getAppId() != null
                    && key.getAppId().equalsIgnoreCase(appId)) {
                return key.getAesKeySpec();
            }
        }
        return null;
    }
    /*
      *
      */

    /**
     * The method will generate public and private keys.
     * The method is required as subsequently, only pub/pvt keys needs to be regenerated.
     * Symmetric keys are generated only once.
     */
    private List generatePubPvtKeysForApplications() {
        try {
            List keys = new ArrayList();
            //String apps1[] = {"App1", "App2", "App3"};

            ///If the method is called after the 'first' time, it will generate the keys based of
            //app identifiers present in database.
            String appsFromDb[] = dbUtil.getAllApplicationIdentifiers();
            if (appsFromDb.length != 0) {
                apps = appsFromDb;
            }

            for (int i = 0; i < apps.length; i++) {
                RSAKey rsaKey = RSAKeyManager.getRSAKeyManager().generatePublicAndPrivateKeys();
                rsaKey.setAppId(apps[i]);
                keys.add(rsaKey);
            }
            return keys;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * The method generates the symmetric and pub/pvt keys for all the applications for the first time
     */
    private List generateSymmAndPubPvtKeysForApplications() {
        try {
            List keys = new ArrayList();

            ///If the method is called after the 'first' time, it will generate the keys based of
            //app identifiers present in database.
            String appsFromDb[] = dbUtil.getAllApplicationIdentifiers();
            if (appsFromDb.length > 0) {
                apps = appsFromDb;
            }
            log.info("Application From DB :: " + appsFromDb.length);
            log.info("Application From List :: " + this.apps.length);

            for (int i = 0; i < this.apps.length; i++) {
                RSAKey rsaKey = RSAKeyManager.getRSAKeyManager()
                        .generatePublicAndPrivateKeys();
                rsaKey.setAppId(apps[i]);
                SecretKey symmetricKey = AESKeyManagerImpl.genearteKey(256);

                byte encryptedSymmKey[] = RSAKeyManager.getRSAKeyManager()
                        .encryptSymmetricKeyInBytes(symmetricKey,
                                rsaKey.getPublicKey());
                Key key = new Key();
                key.setEncryptedSymmetricKeyBytes(encryptedSymmKey);
                key.setRsaKey(rsaKey);
                key.setAppId(apps[i]);
                keys.add(key);
                log.info("Creation of key " + i + "Successfull");
            }
            return keys;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean saveKeysInDatabase(List keys, Connection connection) throws Exception {
        dbUtil.saveSymmetricKeysInDatabase(keys, connection);
        return false;
    }

    private boolean deleteAllSymmetricKeysFromDatabase(Connection connection) throws Exception {
        return dbUtil.deleteAllSymmetricKeysFromDatabase(connection);
    }

    private boolean saveRSAKeysInFile(List keys, Transaction tprivate, Transaction tpublic) throws Exception {
        Iterator iterator = keys.iterator();
        checkIfDirectoryExists();

        try {
            while (iterator.hasNext()) {
                Key key = (Key) iterator.next();
                RSAPrivateKey privateKey = key.getRsaKey().getSecretKey();
                //FileOutputStream fos = new FileOutputStream("private/"
                //FileOutputStream fos = new FileOutputStream("private/" + key.getAppId() + ".pvtKeyObj");
                //frmprivate.createResource(txId,privateKeyFolder + key.getAppId() + ".pvt",true);
                //OutputStream os =  frmprivate.writeResource(txId,privateKeyFolder + key.getAppId() + ".pvt");
                //FileOutputStream fos = new FileOutputStream(privateKeyFolder + key.getAppId() + ".pvtKeyObj");
                FileOutputStream fos = tprivate.openOutputStream(privateKeyFolder + key.getAppId() + ".pvtKeyObj");
                ObjectOutputStream obj_out = new ObjectOutputStream(fos);
                obj_out.writeObject(privateKey);
                //fos.write(privateKey.getEncoded());
                fos.close();
                //os.close();
                obj_out.close();
                RSAPublicKey publicKey = key.getRsaKey().getPublicKey();
                //fos = ne w FileOutputStream("public/" + key.getAppId()
                //fos = new FileOutputStream("public/" + key.getAppId() + ".publicKeyObj");
                fos = tpublic.openOutputStream(publicKeyFolder + key.getAppId() + ".publicKeyObj");
                //creating exception for checking transaction condition
                //fos = new FileOutputStream("/dada/dad/ada/" + key.getAppId() + ".publicKeyObj");
                ObjectOutputStream obj_out_public = new ObjectOutputStream(fos);
                obj_out_public.writeObject(publicKey);
                //fos.write(publicKey.getEncoded());
                fos.close();
                obj_out_public.close();

            }
            //frmprivate.commitTransaction(txId);
        } catch (IOException e) {
            //frmprivate.rollbackTransaction(txId);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw e;
        }

        return true;
    }


    public boolean keyDeletion(String appid) throws Exception {
        return moveKeystoArchive(this.rsaKeys, appid);
    }

    private boolean moveKeystoArchive(List keys, String appid) throws Exception {
        Iterator iterator = keys.iterator();
        checkIfArchiveDirectoryExists();
        TransactionManager tmprivate = null;
        Transaction tprivate = null;
        TransactionManager tmpublic = null;
        Transaction tpublic = null;


        try {
            tmprivate = new TransactionManager(this.txJournal, this.txBackup);
            tprivate = tmprivate.beginTransaction();

            RSAPrivateKey privateKey = cryptoCommon.getRSAPrivateKey(appid, rsaKeys, apps);
            //FileOutputStream fos = new FileOutputStream("private/"
            //FileOutputStream fos = new FileOutputStream("private/" + key.getAppId() + ".pvtKeyObj");

            FileOutputStream fos = tprivate.openOutputStream(privateKeyFolder + "/Archives/" + appid + "-" + DateUtil.getDate(new Date()) + ".pvtKeyObj");
            ObjectOutputStream obj_out = new ObjectOutputStream(fos);
            obj_out.writeObject(privateKey);
            //fos.write(privateKey.getEncoded());
            fos.close();
            obj_out.close();
            tprivate.delete(privateKeyFolder + appid + ".pvtKeyObj");

            tprivate.shouldCommit(true);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        } catch (InconsistentStateException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        } catch (TransactionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        } finally {
            tprivate.end();

        }
        return true;
    }


    private void checkIfDirectoryExists() {
        File privateFolderFile = new File(this.privateKeyFolder);
        File publicFolderFile = new File(this.publicKeyFolder);

        if (!privateFolderFile.exists()) {
            boolean success = privateFolderFile.mkdirs();
            if (success) {
                log.info("Private Directory Creation Successfull");
            } else {
                log.info("Private Directory Creation Failure");
            }
        } else {
            log.info("Private Folder Already Exists.");
        }
        if (!publicFolderFile.exists()) {
            boolean success = publicFolderFile.mkdirs();
            if (success) {
                log.info("Private Directory Creation Successfull");
            } else {
                log.info("Private Directory Creation Failure");
            }
        } else {
            log.info("Private Folder Already Exists.");
        }

    }


    private void checkIfArchiveDirectoryExists() {
        File privateFolderFile = new File(this.privateKeyFolder + "/Archives/");
        File publicFolderFile = new File(this.publicKeyFolder + "/Archives/");

        if (!privateFolderFile.exists()) {
            boolean success = privateFolderFile.mkdirs();
            if (success) {
                log.info("Private Directory Creation Successfull");
            } else {
                log.info("Private Directory Creation Failure");
            }
        } else {
            log.info("Private Folder Already Exists.");
        }
        if (!publicFolderFile.exists()) {
            boolean success = publicFolderFile.mkdirs();
            if (success) {
                log.info("Private Directory Creation Successfull");
            } else {
                log.info("Private Directory Creation Failure");
            }
        } else {
            log.info("Private Folder Already Exists.");
        }

    }

    /**
     * Return a RSA public key against the given app Id.
     */
    private RSAKey getRSAKey(String appId, List newKeys) {
        Iterator iterator = newKeys.iterator();
        while (iterator.hasNext()) {
            RSAKey rsaKey = (RSAKey) iterator.next();
            if (rsaKey.getAppId() != null
                    && rsaKey.getAppId().equalsIgnoreCase(appId)) {
                return rsaKey;
            }
        }
        return null;
    }


    public String[] getApps() {
        return apps;
    }

    public void setApps(String[] apps) {
        this.apps = apps;
    }

    public List getRsaKeys() {
        return rsaKeys;
    }

    public void setRsaKeys(List rsaKeys) {
        this.rsaKeys = rsaKeys;
    }


}
