package com.emirates.util;

import java.security.cert.Certificate;
import java.io.File;

/**
 * Created by EA Team.
 * User: s726580
 * Date: 23-Mar-2009
 * Time: 13:43:28
 * To change this template use File | Settings | File Templates.
 */
public interface KeyStoreUtil {
    public boolean saveCertificateInKeyStore(String appName, Certificate cert) throws Exception;
    public Certificate getCertificateFromFile(File uploadedCert) throws Exception;
}
