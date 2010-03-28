package com.emirates.citp.crypto;

import org.apache.commons.codec.DecoderException;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;

/**
 * Created by IntelliJ IDEA.
 * User: s726580
 * Date: 22-Feb-2009
 * Time: 14:45:11
 * To change this template use File | Settings | File Templates.
 */
public interface AESKeyManager {

    public String encrypt(SecretKeySpec secretKeySpec, String message) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException;

    public String decrypt(SecretKeySpec secretKeySpec, String encryptedData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, DecoderException;

}
