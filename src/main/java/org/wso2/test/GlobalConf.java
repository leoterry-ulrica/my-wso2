package org.wso2.test;

import java.io.File;

public class GlobalConf {

	public static String BackEndUrl = "https://192.168.1.166:9443/services/";
	public static String UserName = "admin";
	public static String Password = "admin";
	
	public static void setProperties() {
		// Set client trust store
		System.setProperty("javax.net.ssl.trustStore",
				new File("src/main/resources/foo.jks").getAbsolutePath());//"classpath:/client-truststore.jks"
		System.out.println(System.getProperty("javax.net.ssl.trustStore"));
		System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
		System.setProperty("javax.net.ssl.trustStoreType", "JKS");
	}
}
