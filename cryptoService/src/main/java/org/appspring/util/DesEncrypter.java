package org.appspring.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

public class DesEncrypter {
    protected final transient Log log = LogFactory.getLog(getClass());
    Cipher ecipher;
    Cipher dcipher;


    public String encrypt(String str, SecretKey key) {
        try {
            // Encode the string into bytes using utf-8
            ecipher = Cipher.getInstance("DES");
            ecipher.init(Cipher.ENCRYPT_MODE, key);


            byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            return new sun.misc.BASE64Encoder().encode(enc);
        } catch (javax.crypto.BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (Exception e) {
        }

        return null;
    }

    public String decrypt(String str, SecretKey key) {
        try {
            dcipher = Cipher.getInstance("DES");
            dcipher.init(Cipher.DECRYPT_MODE, key);


            // Decode base64 to get bytes
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);

            // Decode using utf-8
            return new String(utf8, "UTF8");
        } catch (javax.crypto.BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (Exception e) {
        }
        return null;
    }

    public static String decryptDbPassword(String encryptedPassword) {
        FileInputStream fis = null;
        ObjectInputStream obj_in_public = null;
        String decrypted = null;
        ClassPathResource classPathResource = new ClassPathResource("Database.Key");
        try {
            fis = new FileInputStream(classPathResource
                    .getFile());
            obj_in_public = new ObjectInputStream(fis);
            SecretKey key = (SecretKey) obj_in_public.readObject();
            DesEncrypter encrypter = new DesEncrypter();
            decrypted = encrypter.decrypt(encryptedPassword, key);
            obj_in_public.close();
            fis.close();

        } catch (Exception e) {
            try {
                // TODO: handle exception
                if (fis != null)
                    fis.close();
            } catch (Exception e2) {
                // TODO: handle exception
                System.out.println("decryptDbPassword while closing file connection : " + e2);
            }
            try {
                // TODO: handle exception
                if (obj_in_public != null)
                    obj_in_public.close();
            } catch (Exception e2) {
                // TODO: handle exception
                System.out.println("decryptDbPassword while closing object Inputstream : " + e2);
            }

            System.out.println("decryptDbPassword Exception : " + e);

        }
        return decrypted;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            // Generate a temporary key. In practice, you would save this key.
            // See also e464 Encrypting with DES Using a Pass Phrase.


            SecretKey key = KeyGenerator.getInstance("DES").generateKey();

            FileOutputStream fos = new FileOutputStream("/tmp/Database.Key");
            ObjectOutputStream obj_out = new ObjectOutputStream(fos);
            obj_out.writeObject(key);
            obj_out.close();
            fos.close();

            // Create encrypter/decrypter class
            DesEncrypter encrypter = new DesEncrypter();

            // Encrypt
            String encrypted = encrypter.encrypt("temp123", key);
            System.out.println("Encrypted String : " + encrypted);

            // Decrypt
            String decrypted = encrypter.decrypt(encrypted, key);
            System.out.println("Decrypted String : " + decrypted);
        } catch (Exception e) {
        }


    }

}
