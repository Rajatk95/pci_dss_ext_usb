package com.emirates.citp.crypto.impl;


import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.emirates.citp.crypto.AESKeyManager;

public class AESKeyManagerImpl implements AESKeyManager {
    private static KeyGenerator kgen;
    private static SecretKey skey;
    private static SecretKeySpec skeySpec;
    private static Cipher cipher;
//private static AESKeyManagerImpl keyManagerImpl;
/*
	private AESKeyManagerImpl() throws Exception{

	}*/

    public void init() throws Exception {
        generateKey(128);
    }
    /*
     public static AESKeyManagerImpl getAESKeyManager() throws Exception{
         if( keyManagerImpl == null)
             keyManagerImpl = new AESKeyManagerImpl();
     return keyManagerImpl;
     }*/

    /**
     * Turns array of bytes into string
     *
     * @param buf Array of bytes to convert to hex string
     * @return Generated hex string
     */
    public static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10)
                strbuf.append("0");

            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }

        return strbuf.toString();
    }


    public static void generateKey(int length) throws Exception {
        kgen = KeyGenerator.getInstance("AES");
        kgen.init(length); // 192 and 256 bits may not be available


        // Generate the secret key specs.
        skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();

        skeySpec = new SecretKeySpec(raw, "AES");


        // Instantiate the cipher

        cipher = Cipher.getInstance("AES");
    }

    public static SecretKey genearteKey(int length) throws Exception {
        SecureRandom random = new SecureRandom();
        kgen = KeyGenerator.getInstance("AES");
        kgen.init(length, random); // 192 and 256 bits may not be available


        // Generate the secret key specs.
        skey = kgen.generateKey();
        return skey;
    }


    public String doEncryption(String data) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return asHex(encrypted);
    }

    public String doDecryption(String encryptedData) throws Exception {
        Hex hex = new Hex();
        byte encrypted[] = hex.decode(encryptedData.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] original = cipher.doFinal(encrypted);
        return new String(original);
    }

    public String encrypt(SecretKeySpec secretKeySpec, String message) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cip = Cipher.getInstance("AES");
        cip.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encrypted = cip.doFinal(message.getBytes());
        return asHex(encrypted);
    }

    public String decrypt(SecretKeySpec secretKeySpec, String encryptedData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, DecoderException {
        Cipher cip = Cipher.getInstance("AES");
        cip.init(Cipher.DECRYPT_MODE, secretKeySpec);
        Hex hex = new Hex();
        byte encrypted[] = hex.decode(encryptedData.getBytes());
        byte[] original = cip.doFinal(encrypted);
        return new String(original);
    }


    public static void main(String[] args) throws Exception {
        //AESKeyManagerImpl keyManagerImpl = AESKeyManagerImpl.getAESKeyManager();
        //String message="Heeeeeeeeeellllllllllllllooooooooo World";
        //String encryptedData = keyManagerImpl.doEncryption(message);
        //String decryptedData = keyManagerImpl.doDecryption(encryptedData);
        //System.out.println(decryptedData);


        //String encryptedData = do
//
//       // Get the KeyGenerator
//
//       KeyGenerator kgen = KeyGenerator.getInstance("AES");
//       kgen.init(128); // 192 and 256 bits may not be available
//
//
//       // Generate the secret key specs.
//       SecretKey skey = kgen.generateKey();
//       byte[] raw = skey.getEncoded();
//
//       SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//
//
//       // Instantiate the cipher
//
//       Cipher cipher = Cipher.getInstance("AES");
//
//       cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
//
////       byte[] encrypted =
////         cipher.doFinal((args.length == 0 ?
////          "This is just an example" : args[0]).getBytes());
//       byte[] encrypted = cipher.doFinal(message.getBytes() );
//       
//       System.out.println("encrypted string: " + asHex(encrypted));
//
//       cipher.init(Cipher.DECRYPT_MODE, skeySpec);
//       byte[] original = cipher.doFinal(encrypted);
//       String originalString = new String(original);
//       System.out.println("Original string: " +
//         originalString + " " + asHex(original));
    }

    public Cipher getCipher() {
        return this.cipher;
    }

    public SecretKeySpec getSecretKeySpec() {
        return this.getSecretKeySpec();
    }

}
