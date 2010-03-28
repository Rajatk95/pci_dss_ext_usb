package org.appspring.util;

/**
 * Created by EA Team.
 * User: s726580
 * Date: 17-Mar-2009
 * Time: 15:43:59
 * To change this template use File | Settings | File Templates.
 */

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.security.cert.*;

public class SecurityAttribs extends HttpServlet {
    static final String CONTENT_TYPE = "text/html";

    /**
     * This method is called once per instance of the servlet class.
     * Use this method to allocate any needed resources that should
     * be preserved for the life of the servlet instance.
     */
    public void init(ServletConfig config)
            throws ServletException {
        super.init(config);
    }


    // Handle the HTTP GET request
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);

        StringBuffer html = new StringBuffer();
        html.append("<html><head><title>SecureServlet</title></head><body>");

        checkSSLAttributes(request, html);
        checkAuthType(request, html);

        loopThroughAttribs(request, html);
        loopThroughHeaders(request, html);
        checkClientCerts(request, html);

        html.append("</body></html>");

        PrintWriter out = response.getWriter();
        response.setContentLength(html.length());
        out.println(html.toString());
    }


    // Handle the HTTP POST request
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();

        /** @todo Process the HTTP "POST" request here, and write the proper
        response to the PrintWriter "out". */

        out.println("<html><head><title>SecurityAttribs</title></head><body>");
        out.println("<p>Servlet SecurityAttribs has received an HTTP POST.</p>");
        out.println("<p>The servlet generated this page in response to the request.</p>");
        out.println("</body></html>");
    }


    private void checkAuthType(HttpServletRequest request, StringBuffer html) {
        try {
            //request.BASIC_AUTH, DIGEST_AUTH, CLIENT_CERT_AUTH, FORM_AUTH
            html.append("<P>request authorization type is <B>").append(request.getAuthType()).append("</B></p>");

            // simplified type check
            if (request.getAuthType() == request.BASIC_AUTH) {
                html.append("<P>this is only basic authorization !</P>");
            }

            html.append("<P>request.getAuth    ==> <B>").append(request.getAuthType()).append("</B></p>");
            html.append("<p>(request.BASIC_AUTH ==> ").append(request.BASIC_AUTH).append(")</p>");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkSSLAttributes(HttpServletRequest request, StringBuffer html) {

        // security checks
        try {
            String cyphersuite = (String) request.getAttribute("javax.servlet.request.cipher_suite");

            if (cyphersuite != null) {
                html.append("<P>javax.servlet.request.cipher_suite is <B>").append(cyphersuite).append("</B></P>");
            }

            cyphersuite = (String) request.getAttribute("javax.net.ssl.cipher_suite");

            if (cyphersuite != null) {
                html.append("<P>javax.net.ssl.cipher_suite is <B>").append(cyphersuite).append("</B></P>");
            }

            Object o = request.getAttribute("javax.servlet.request.key_size");
            if (o != null) {
                Integer size = (Integer) o;

                html.append("<P>javax.servlet.request.key_size is <B>").append(size.intValue()).append("</B></P>");
            } else {
                html.append("<P>javax.servlet.request.key_size not present in this request</P>");
            }


            // from 2.1 spec
            X509Certificate[] certs = (X509Certificate[]) request.getAttribute("javax.net.ssl.peer_certificates");

            if (certs != null && certs.length > 0) {
                html.append("<P><B>Client Certs 2.1</B><table border='1'><tr><th>type</th></tr>");
                for (int i = 0; i < certs.length; i++) {
                    X509Certificate cert = certs[i];
                    html.append("<tr><td>").append(cert.getType()).append("</td></tr>");
                }

                html.append("</table></p>");
            }


            // from 2.2 spec
            certs = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
            if (certs != null && certs.length > 0) {
                html.append("<P><B>Client Certs 2.2</B><table border='1'><tr><th>type</th></tr>");
                for (int i = 0; i < certs.length; i++) {
                    X509Certificate cert = certs[i];
                    html.append("<tr><td>").append(cert.getType()).append("</td></tr>");
                    html.append("<tr><td>").append(cert.getIssuerDN().getName()).append("</td></tr>");
                    html.append("<tr><td>").append(cert.getSubjectDN().getName()).append("</td></tr>");

                }

                html.append("</table></p>");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            html.append("<p>error accessing javax.servlet.request.key_size : ").append(e.getMessage()).append("</p>");
        }
    }


    private void loopThroughAttribs(HttpServletRequest request, StringBuffer html) {
        try {
            html.append("<P><B>Attribs</B><table border='1'><tr><th>Name</th><th>Value</th><th>Class name</th></tr>");
            for (Enumeration en = request.getAttributeNames(); en.hasMoreElements();) {
                String name = (String) en.nextElement();

                html.append("<tr><td>").append(name).append("</td><td>").append(request.getAttribute(name)).append("</td><td>").append((request.getAttribute(name)).getClass().getName()).append("</td></tr>");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        html.append("</table></p>");
    }

    private void loopThroughHeaders(HttpServletRequest request, StringBuffer html) {
        try {
            html.append("<P><B>Headers</B><table border='1'><tr><th>Name</th><th>Value</th></tr>");
            for (Enumeration en = request.getHeaderNames(); en.hasMoreElements();) {
                String name = (String) en.nextElement();

                html.append("<tr><td>").append(name).append("</td><td>").append(request.getHeader(name)).append("</td></tr>");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        html.append("</table></P>");
    }

    private void checkClientCerts(HttpServletRequest request, StringBuffer html) {
        java.security.cert.X509Certificate[] rst = (java.security.cert.X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");

        if (rst != null && rst.length > 0) {
            html.append("<P><B>Client Certs</B><table border='1'><tr><th>Type</th></tr>");
            for (int i = 0; i < rst.length; i++) {
                java.security.cert.X509Certificate clientCert = rst[i];
                html.append("<tr><td>").append(clientCert.getType()).append("</td></tr>");
            }

            html.append("</table></P>");
        }
    }
}