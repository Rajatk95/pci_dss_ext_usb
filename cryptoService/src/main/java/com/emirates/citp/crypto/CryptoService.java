package com.emirates.citp.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.jws.WebService;

@WebService
public interface CryptoService {

	public String hash(String appId, String text)
			throws NoSuchAlgorithmException, NoSuchProviderException;

	public String encrypt(String appId, String message) throws Exception;

	public String decrypt(String appId, String encryptedData) throws Exception;

	public String reintialize();

}
