package com.emirates.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.KeyStore;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;

/**
 * Created by EA Team.
 * User: s726580
 * Date: 23-Mar-2009
 * Time: 11:51:27
 * To change this template use File | Settings | File Templates.
 */
public class KeyStoreUtilImpl implements KeyStoreUtil{

     protected final transient Log log = LogFactory.getLog(getClass());

    private String keyStoreLocation;
    private String keyStorePassword;


    public String getKeyStoreLocation() {
        return keyStoreLocation;
    }

    public void setKeyStoreLocation(String keyStoreLocation) {
        this.keyStoreLocation = keyStoreLocation;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public boolean saveCertificateInKeyStore(String appName,Certificate cert) throws Exception {
        //CREATE A KEYSTORE OF TYPE "Java Key Store"
        KeyStore ks = KeyStore.getInstance("JKS");
        FileInputStream fisKs = new FileInputStream(this.keyStoreLocation);
        ks.load(fisKs, this.keyStorePassword.toCharArray());
        /*
        FileInputStream fisCert = new FileInputStream("c:/certificates/client2.cer");
        BufferedInputStream bis = new BufferedInputStream(fisCert);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate cert = null;

        while (bis.available() > 0) {
            cert = cf.generateCertificate(bis);
            X509Certificate xcert = (X509Certificate)cert;
            System.out.println("Certificate Serial Number :: " + xcert.getSerialNumber());
            ks.setCertificateEntry(appName, cert);
        } */
        ks.setCertificateEntry(appName, cert);
        ks.store(new FileOutputStream(this.keyStoreLocation), this.keyStorePassword.toCharArray());
        return true ;
    }

    public Certificate getCertificateFromFile(File uploadedCert) throws Exception{
        FileInputStream fisCert = new FileInputStream(uploadedCert);
        BufferedInputStream bis = new BufferedInputStream(fisCert);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate cert = null;
        while (bis.available() > 0) {
            cert = cf.generateCertificate(bis);
            X509Certificate xcert = (X509Certificate)cert;
            log.info("Certificate Serial Number :: " + xcert.getSerialNumber());
        }

        return cert;

    }

    public static void main(String[] args) {
         KeyStoreUtilImpl utilImpl = new KeyStoreUtilImpl();
        try {
            //utilImpl.saveCertificateInKeyStore("App3");
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
