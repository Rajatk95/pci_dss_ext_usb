package org.appspring.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import java.io.IOException;
import java.security.cert.X509Certificate;

/**
 * Created by EA Team.
 * User: s726580
 * Date: 18-Mar-2009
 * Time: 07:34:09
 * To change this template use File | Settings | File Templates.
 */

public class SecurityFilter implements javax.servlet.Filter {
    protected final transient Log log = LogFactory.getLog(getClass());

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        X509Certificate[] certs = (X509Certificate[]) req.getAttribute("javax.net.ssl.peer_certificates");

        if (certs != null && certs.length > 0) {
            for (int i = 0; i < certs.length; i++) {
                X509Certificate cert = certs[i];
                MyThreadLocal.set(cert.getSerialNumber().toString());
                log.debug("Issuer DN :" + cert.getIssuerDN().getName());
                log.debug("Certificate Id :" + cert.getCriticalExtensionOIDs());
                log.debug("Principal Name :" + cert.getSubjectX500Principal().getName());
                log.debug("Serial Number :" + cert.getSerialNumber());
                //html.append("<tr><td>").append(cert.getType()).append("</td></tr>");
            }

        }


        // from 2.2 spec
        certs = (X509Certificate[]) req.getAttribute("javax.servlet.request.X509Certificate");
        if (certs != null && certs.length > 0) {
            for (int i = 0; i < certs.length; i++) {
                X509Certificate cert = certs[i];
                MyThreadLocal.set(cert.getSerialNumber().toString());
                log.debug("Issuer DN :" + cert.getIssuerDN().getName());
                log.debug("Certificate Id :" + cert.getCriticalExtensionOIDs());
                log.debug("Principal Name :" + cert.getSubjectX500Principal().getName());
                log.debug("Serial Number :" + cert.getSerialNumber());

                //html.append("<tr><td>").append(cert.getIssuerDN().getName()).append("</td></tr>");
            }
        }

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
