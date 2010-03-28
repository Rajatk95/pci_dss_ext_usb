package com.emirates.citp.crypto.impl;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Iterator;
import java.util.List;

public class Util {
    private static Util util;

    private Util() {

    }

    /**
     * To check if the appId (- application identifier0 exists in an array of appId's
     *
     * @param rsaKeys
     * @param appId
     * @return true if appId exists in an array of app passed
     */
    public static boolean checkIfExisting(List rsaKeys, String appId) {
        boolean status = false;

        if (rsaKeys == null || appId == null)
            return false;

        for (int i = 0; i < rsaKeys.size(); i++) {
            RSAKey key = (RSAKey) rsaKeys.get(i);
            if (key.getAppId() != null && key.getAppId().trim().equalsIgnoreCase(appId.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * The method is primary used to load a newly created RSA -pub/pvt key from file system/usb into application memory.
     * It does the following -
     * 1. It reads the RSA pub/pvt key files into RSAKey object.
     * 2. It scans the list holding RSAKey objects and replaces the old RSAKey object, it it exist, with the
     * new one for the given application identifier - appId.
     * 3. It return the list of held RSAKey objects
     */
    public List loadRSAKeyFromFile(String appId, List rsaKeys, String publicFolder, String privateFolder) throws Exception {


        FileInputStream fis = new FileInputStream(publicFolder + appId + ".publicKeyObj");

        ObjectInputStream obj_in_public = new ObjectInputStream(fis);
        RSAPublicKey publicKey = (RSAPublicKey) obj_in_public.readObject();


        fis = new FileInputStream(privateFolder + appId + ".pvtKeyObj");
        ObjectInputStream obj_in_private = new ObjectInputStream(fis);
        RSAPrivateKey privateKey = (RSAPrivateKey) obj_in_private.readObject();


        fis.close();
        RSAKey rsaKey = new RSAKey();
        rsaKey.setAppId(appId);
        rsaKey.setPublicKey(publicKey);
        rsaKey.setSecretKey(privateKey);

        Iterator iterator = rsaKeys.iterator();
        boolean isOld = false;
        for (int i = 0; i < rsaKeys.size(); i++) {
            RSAKey rKey = (RSAKey) rsaKeys.get(i);
            if (rKey != null && rKey.getAppId() != null && rKey.getAppId().trim().equalsIgnoreCase(appId.trim())) {
                rsaKeys.remove(i);//Replace
                rsaKeys.add(rsaKey);
                isOld = true;// This indicates that RSAKey object has to be reloaded.
                break;
            }
        }
        if (!isOld) {//means this is a representation of a newly created RSAKey.
            rsaKeys.add(rsaKey);
        }
        return rsaKeys;
    }


    public static Util getUtil() {
        if (util == null) {
            util = new Util();
        }
        return util;
    }

}
