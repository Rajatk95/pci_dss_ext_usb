package com.emirates.citp.crypto.impl;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.*;

/**
 * A simple class to generate a set of RSA (public and private) keys and
 * write those keys to files for exchange with other environments. Four files will
 * be generated containing the public and private keys in byte and text(hex) formats.
 */

public class RSAKeyGenerator {
    private static RSAKeyGenerator keyGenerator = new RSAKeyGenerator();

    private RSAKeyGenerator() {
    }

    public static RSAKey generatePublicAndPrivateKeys() throws IOException {
        KeyPairGenerator keyGen = null;
        try {
            SecureRandom random = new SecureRandom();

            keyGen = KeyPairGenerator.getInstance("RSA");
            /* The 512 is the key size. For better encryption increase to 2048 */
            keyGen.initialize(new RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4), random);
        }
        catch (NoSuchAlgorithmException noAlgorithm) {
            System.out.println("No RSA provider available!");
            return null;
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithm) {
            System.out.println("Invalid algorithm for RSA!");
            return null;
        }

        KeyPair keyPair = keyGen.generateKeyPair();

        RSAPrivateKey secretKey = (RSAPrivateKey) keyPair.getPrivate();


        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        RSAKey rsa = new RSAKey();
        rsa.setPublicKey(publicKey);
        rsa.setSecretKey(secretKey);
        return rsa;
    }

    public static void main(String[] args) throws java.io.IOException {
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
            /* The 512 is the key size. For better encryption increase to 2048 */
            keyGen.initialize(new RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4));
        }
        catch (NoSuchAlgorithmException noAlgorithm) {
            System.out.println("No RSA provider available!");
            return;
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithm) {
            System.out.println("Invalid algorithm for RSA!");
            return;
        }

        KeyPair keyPair = keyGen.generateKeyPair();

        RSAPrivateKey secretKey = (RSAPrivateKey) keyPair.getPrivate();


        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        // Write out the ASN.1 and raw key files
        FileOutputStream fOut = new FileOutputStream("private_asn1.key");
        //char[] b64 = Base64Coder.encode(privKey.getEncoded());
        //fOut.write(Base64Coder.encode(secretKey.getEncoded() ));
        fOut.close();

        fOut = new FileOutputStream("public_asn1.key");
        fOut.write(publicKey.getEncoded());
        fOut.close();

        FileWriter fw = new FileWriter("private_raw.txt");
        fw.write(secretKey.getModulus().toString(16).toUpperCase());
        fw.write("\n");
        fw.write(secretKey.getPrivateExponent().toString(16).toUpperCase());
        fw.close();

        fw = new FileWriter("public_raw.txt");
        fw.write(publicKey.getModulus().toString(16).toUpperCase());
        fw.write("\n");
        fw.write(publicKey.getPublicExponent().toString(16).toUpperCase());
        fw.close();

        System.out.println("RSA keys generated successfully.");
    }

    public static RSAKeyGenerator getRSAKeyGenerator() {
        if (keyGenerator == null) {
            keyGenerator = new RSAKeyGenerator();
        }
        return keyGenerator;
    }
}
