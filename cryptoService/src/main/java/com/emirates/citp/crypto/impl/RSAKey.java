package com.emirates.citp.crypto.impl;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSAKey {
private RSAPrivateKey secretKey;

public RSAPublicKey publicKey;

private String appId;

public String getAppId() {
	return appId;
}

public void setAppId(String appId) {
	this.appId = appId;
}

public RSAPrivateKey getSecretKey() {
	return secretKey;
}

public void setSecretKey(RSAPrivateKey secretKey) {
	this.secretKey = secretKey;
}

public RSAPublicKey getPublicKey() {
	return publicKey;
}

public void setPublicKey(RSAPublicKey publicKey) {
	this.publicKey = publicKey;
}


}
