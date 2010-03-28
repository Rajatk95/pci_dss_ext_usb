package com.emirates.citp.crypto.impl;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Key {
    private SecretKey aesKey;
    private SecretKeySpec aesKeySpec;
    private RSAKey rsaKey;
    private String encryptedSymmetricKey;
    private String appId;
    private byte[] encryptedSymmetricKeyBytes;
    private int soaGroupId;
    private String certificateid;

    public String getCertificateid() {
        return certificateid;
    }

    public void setCertificateid(String certificateid) {
        this.certificateid = certificateid;
    }

    public int getSoaGroupId() {
        return soaGroupId;
    }

    public void setSoaGroupId(int soaGroupId) {
        this.soaGroupId = soaGroupId;
    }

    public String getEncryptedSymmetricKey() {
        return encryptedSymmetricKey;
    }

    public void setEncryptedSymmetricKey(String encryptedSymmetricKey) {
        this.encryptedSymmetricKey = encryptedSymmetricKey;
    }

    public SecretKey getAesKey() {
        return aesKey;
    }

    public void setAesKey(SecretKey aesKey) {
        this.aesKey = aesKey;
    }

    public RSAKey getRsaKey() {
        return rsaKey;
    }

    public void setRsaKey(RSAKey rsaKey) {
        this.rsaKey = rsaKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public SecretKeySpec getAesKeySpec() {
        return aesKeySpec;
    }

    public void setAesKeySpec(SecretKeySpec aesKeySpec) {
        this.aesKeySpec = aesKeySpec;
    }

    public byte[] getEncryptedSymmetricKeyBytes() {
        return encryptedSymmetricKeyBytes;
    }

    public void setEncryptedSymmetricKeyBytes(byte[] encryptedSymmetricKeyBytes) {
        this.encryptedSymmetricKeyBytes = encryptedSymmetricKeyBytes;
    }

}
