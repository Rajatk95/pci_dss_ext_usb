package org.appspring.util;

/**
 * Created by EA Team.
 * User: s726580
 * Date: 18-Mar-2009
 * Time: 09:54:06
 * To change this template use File | Settings | File Templates.
 */
public class MyThreadLocal {


    private MyThreadLocal() {
    }

    private static ThreadLocal tLocal = new ThreadLocal();


    public static void set(String issuerDn) {
        tLocal.set(issuerDn);
    }

    public static String get() {
        return (String) tLocal.get();
    }

}
