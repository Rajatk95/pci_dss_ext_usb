package com.emirates.citp.crypto.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class RSAKeyManager {
private static RSAKeyManager keyManager;


	private RSAKeyManager(){
		
	}
	
	public String encryptSymmetricKey(SecretKey symmetricKey, RSAPublicKey publicKey) throws Exception{
		Cipher rsaCipher =  javax.crypto.Cipher.getInstance("RSA");
		rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		//ByteArrayOutputStream bos = new ByteArrayOutputStream();
		//CipherOutputStream os = new CipherOutputStream(bos, rsaCipher);
		//os.write(symmetricKey.getEncoded());
		byte[] encryptedSymmKey = rsaCipher.doFinal(symmetricKey.getEncoded());
		
		return new String( encryptedSymmKey);
	}
	public byte[] encryptSymmetricKeyInBytes(SecretKey symmetricKey, RSAPublicKey publicKey) throws Exception{
		Cipher rsaCipher =  javax.crypto.Cipher.getInstance("RSA");
		//Cipher rsaCipher =  javax.crypto.Cipher.getInstance("RSA/ECB/NoPadding");
		rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		//ByteArrayOutputStream bos = new ByteArrayOutputStream();
		//CipherOutputStream os = new CipherOutputStream(bos, rsaCipher);
		//os.write(symmetricKey.getEncoded());
		byte[] encryptedSymmKey = rsaCipher.doFinal(symmetricKey.getEncoded());
		
		return encryptedSymmKey;
	}
	
	public byte[] encryptSymmetricKeyInBytes(SecretKeySpec symmetricKeySpec, RSAPublicKey publicKey) throws Exception{
		Cipher rsaCipher =  javax.crypto.Cipher.getInstance("RSA");
		//Cipher rsaCipher =  javax.crypto.Cipher.getInstance("RSA/ECB/NoPadding");
		rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		//ByteArrayOutputStream bos = new ByteArrayOutputStream();
		//CipherOutputStream os = new CipherOutputStream(bos, rsaCipher);
		//os.write(symmetricKey.getEncoded());
		byte[] encryptedSymmKey = rsaCipher.doFinal(symmetricKeySpec.getEncoded());
		
		return encryptedSymmKey;
	}
	
	public SecretKeySpec decryptSymmetricKey(String encryptedSymmKey, RSAPrivateKey privateKey) throws Exception{
		Cipher rsaCipher =  javax.crypto.Cipher.getInstance("RSA");
		rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
		javax.crypto.CipherInputStream is = new CipherInputStream(new ByteArrayInputStream(encryptedSymmKey.getBytes() ), rsaCipher);
		byte aesKey[] = new byte[128/8];
		is.read(aesKey);
		SecretKeySpec skeySpec = new SecretKeySpec(aesKey, "AES");
		return skeySpec;
	}
	
	 public static RSAKey generatePublicAndPrivateKeys() throws IOException{
		 return RSAKeyGenerator.getRSAKeyGenerator().generatePublicAndPrivateKeys();
	 }
	
	public static RSAKeyManager getRSAKeyManager(){
		if (keyManager == null){
			keyManager = new RSAKeyManager();
		}
	return keyManager;
	}
}
