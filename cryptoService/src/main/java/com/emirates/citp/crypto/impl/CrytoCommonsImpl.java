package com.emirates.citp.crypto.impl;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appspring.util.DbUtil;

import com.emirates.citp.crypto.CryptoCommons;

public class CrytoCommonsImpl implements CryptoCommons {
    protected final transient Log log = LogFactory.getLog(getClass());

    private DbUtil dbUtil;

    private String privateKeyFolder;

    private String publicKeyFolder;

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

    public DbUtil getDbUtil() {
        return dbUtil;
    }

    public void setDbUtil(DbUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    public boolean getSymmetricKeysFromDatabase(List symmKeys, List rsaKeys, String[] apps) throws Exception {
        List keys = dbUtil.readSymmetricKeysFromDatabase();
        Iterator iterator = keys.iterator();
        symmKeys.clear();
        for (int i = 0; i < keys.size(); i++) {
            Key key = (Key) iterator.next();

            //String encryptedSymmKey = key.getEncryptedSymmetricKey();
            byte eSK[] = key.getEncryptedSymmetricKeyBytes();

            //Decrypt the encrypted symmetric key
            log.debug("Application Id : " + key.getAppId());
            RSAPrivateKey rsaPrivateKey = getRSAPrivateKey(key.getAppId(), rsaKeys, apps);

            //Cipher rsaCipher = Cipher.getInstance("RSA/ECB/NoPadding");

            Cipher rsaCipher = Cipher.getInstance("RSA");
            // read AES key
            rsaCipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
            byte aesKey[] = new byte[256 / 8]; //128 is the aes key length

            aesKey = rsaCipher.doFinal(eSK);


            log.debug(" aesKey is " + new String(aesKey));
            //SecretKeyFactory factory = SecretKeyFactory.getInstance("AES");
            SecretKeySpec aeskeySpec = new SecretKeySpec(aesKey, "AES");
            //SecretKeySpec aeskeySpec = factory.generateSecret(keySpec)
            Key ky = new Key();
            ky.setAppId(key.getAppId());
            ky.setAesKeySpec(aeskeySpec);
            ky.setSoaGroupId(key.getSoaGroupId());//adding group id as well
            ky.setCertificateid(key.getCertificateid());
            symmKeys.add(ky);

        }
        return true;
    }

    public boolean loadRSAKeysFromFiles(String[] apps, List rsaKeys) throws Exception {
        rsaKeys.clear();
        apps = dbUtil.getAllApplicationIdentifiers();
        for (int i = 0; i < apps.length; i++) {
            //for(int i=0;i<appIds.length;i++ ){
            //System.
            /*
               ClassPathResource classPathResource = new ClassPathResource("public/sample.txt");
               //URLConnection urlConnection = url.openConnection();
               //InputStream fis = urlConnection.getInputStream();
               FileInputStream fis = new FileInputStream(classPathResource.getFile());
               byte[] encodedString = new byte[fis.available()];
               fis.read(encodedString);
               String dispFile = new String(encodedString);
               log.debug("Sample String:: " + dispFile);*/


            //public keys
            log.debug("reading objects...............");
            //ClassPathResource classPathResource = new ClassPathResource("public/" + apps[i] + ".publicKeyObj");
            //FileInputStream fis = new FileInputStream(classPathResource.getFile());
            //FileInputStream fis = new FileInputStream("public/" + apps[i] + ".publicKeyObj");
            FileInputStream fis = new FileInputStream(this.publicKeyFolder + apps[i] + ".publicKeyObj");
            //log.debug("Public File Available :: " + fis.available());
            //log.debug("Public File Length :: " + classPathResource.getFile().length());
            //FileInputStream fis = new FileInputStream(classPathResource.getFile());
            //byte[] encodedKey = new byte[fis.available()];
            //fis.read(encodedKey);
            ObjectInputStream obj_in_public = new ObjectInputStream(fis);
            RSAPublicKey publicKey = (RSAPublicKey) obj_in_public.readObject();
//			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
//			KeyFactory kf = KeyFactory.getInstance("RSA");
//			PublicKey pk = kf.generatePublic(publicKeySpec);
//			RSAPublicKey publicKey = (RSAPublicKey)pk;
//			fis.close();

            //private Keys
            //classPathResource = new ClassPathResource("private/" + apps[i] + ".pvtKeyObj");
            //fis = new FileInputStream(classPathResource.getFile());
            //fis = new FileInputStream("private/" + apps[i] + ".pvtKeyObj");
            fis = new FileInputStream(this.privateKeyFolder + apps[i] + ".pvtKeyObj");
            ObjectInputStream obj_in_private = new ObjectInputStream(fis);
            RSAPrivateKey privateKey = (RSAPrivateKey) obj_in_private.readObject();
            //encodedKey = new byte[fis.available()];
            //fis.read(encodedKey);

            //log.debug("Private File Available :: " + fis.available());
            //log.debug("Private Byte[] Length :: " + encodedKey.length);
//		    PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedKey);
//		    kf = KeyFactory.getInstance("RSA");
//		    PrivateKey pvtk = kf.generatePrivate(privateKeySpec);
            //RSAPrivateKey privateKey = (RSAPrivateKey) pvtk;
            fis.close();
            RSAKey rsaKey = new RSAKey();
            rsaKey.setAppId(apps[i]);
            rsaKey.setPublicKey(publicKey);
            rsaKey.setSecretKey(privateKey);
            rsaKeys.add(rsaKey);
        }
        return true;
    }

    public RSAPrivateKey getRSAPrivateKey(String appId, List rsaKeys, String[] apps) {
        try {
            if (rsaKeys.isEmpty()) {
                loadRSAKeysFromFiles(apps, rsaKeys);
            }
            Iterator iterator = rsaKeys.iterator();
            while (iterator.hasNext()) {
                RSAKey rsaKey = (RSAKey) iterator.next();
                if (rsaKey.getAppId() != null
                        && rsaKey.getAppId().equalsIgnoreCase(appId)) {
                    return rsaKey.getSecretKey();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }


}
