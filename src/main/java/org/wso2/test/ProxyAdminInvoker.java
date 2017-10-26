package org.wso2.test;

import org.apache.axis2.client.ServiceClient;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;
import org.wso2.carbon.proxyadmin.stub.Exception;
import org.wso2.carbon.proxyadmin.stub.ProxyServiceAdminStub;
import org.wso2.carbon.proxyadmin.stub.types.carbon.ProxyData;
import org.wso2.carbon.service.mgt.stub.*;
import org.wso2.carbon.service.mgt.stub.types.carbon.ServiceMetaData;
import org.wso2.carbon.service.mgt.stub.types.carbon.ServiceMetaDataWrapper;
import org.wso2.carbon.utils.CarbonUtils;
//import org.wso2.carbon.admin.service.utils.AuthenticateStub;

import java.rmi.RemoteException;

public class ProxyAdminInvoker {

	static String backEndUrl = "https://localhost:9443/services/";
	static String userName = "admin";
	static String password = "admin";

	private static void setProperties() {
		// Set client trust store
		System.setProperty("javax.net.ssl.trustStore",
				"G:/wso2esb-5.0.1-SNAPSHOT/repository/resources/security/client-truststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
		System.setProperty("javax.net.ssl.trustStoreType", "JKS");
	}

	public static void main(String[] args) throws Exception, RemoteException, LoginAuthenticationExceptionException, LogoutAuthenticationExceptionException {

		//listServicesEx();
		listServices();
	}

	public static void addProxy() {

		try {

			// Add the service URL
			String serviceEndPoint = "http://server.arcgisonline.com/arcgis/rest/services/World_Street_Map/MapServer";
			// Proxy Admin service's endpoing URL
			// 特别注意：这里的地址是管理端服务地址和端口，并不是listAdminServices出来的endpoint
			String endPoint = backEndUrl + "ProxyServiceAdmin";

			setProperties();

			ProxyServiceAdminStub proxyServiceAdminStub = new ProxyServiceAdminStub(endPoint);

			CarbonUtils.setBasicAccessSecurityHeaders(userName, password, proxyServiceAdminStub._getServiceClient());

			// Set proxy configuration data
			String[] transport = { "http", "https" };
			ProxyData data = new ProxyData();
			data.setName("TestProxy2");
			// data.setWsdlURI("http://localhost:8281/services/echo?wsdl");
			data.setTransports(transport);
			data.setStartOnLoad(true);
			// data.setInSeqKey("SeqIn");
			data.setOutSeqKey("SeqOut");
			// data.setOutSeqXML("<outSequence
			// xmlns=\"http://ws.apache.org/ns/synapse\"> <send/>
			// </outSequence>");
			data.setEnableStatistics(true);
			data.setEnableTracing(true);
			data.setEndpointXML("<endpoint xmlns=\"http://ws.apache.org/ns/synapse\">  " + " <address uri=\""
					+ serviceEndPoint + "\"> " + " </address> </endpoint>");

			data.setEnableSecurity(false);

			String result = proxyServiceAdminStub.addProxy(data);
			System.out.println("添加完成：" + result);

		} catch (RemoteException | Exception ex) {
			ex.printStackTrace();
		}

	}

	public static void listServices() {

		String endPoint = backEndUrl + "ServiceAdmin";

		setProperties();

		try {
			ServiceAdminStub serviceAdminStub = new ServiceAdminStub(endPoint);
			ServiceClient serviceClient = serviceAdminStub._getServiceClient();

			serviceClient = serviceAdminStub._getServiceClient();
			CarbonUtils.setBasicAccessSecurityHeaders(userName, password, serviceClient);

			// ServiceGroupMetaData groupMetadata =
			// serviceAdminStub.listServiceGroup("World_Street_Map_Proxy"); //
			// 输入正确才能正常调用，否则抛出异常
			// ServiceMetaData[] services1 = groupMetadata.getServices();

			// 服务类型：proxy、axis2和sts，所有传入：ALL
			// 三个参数：serviceTypeFilter、serviceSearchString、pageNumber
			ServiceMetaDataWrapper metadataWrapper = serviceAdminStub.listServices("ALL", "*", 0);
			ServiceMetaData[] services = metadataWrapper.getServices();
			for (ServiceMetaData serviceData : services) {
				System.out.println(serviceData.getName());
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}
		// endpointAdminStub.saveDynamicEndpoint("yy", "https://www.baidu.com");

	}

	public static void listServicesEx()
			throws RemoteException, LoginAuthenticationExceptionException, LogoutAuthenticationExceptionException {

		setProperties();

		LoginAdminServiceClient login = new LoginAdminServiceClient(backEndUrl);
		String session = login.authenticate("admin", "admin");
		ServiceAdminClient serviceAdminClient = new ServiceAdminClient(backEndUrl, session);
		ServiceMetaDataWrapper serviceList = serviceAdminClient.listServices();
		System.out.println("Service Names:");
		for (ServiceMetaData serviceData : serviceList.getServices()) {
			System.out.println(serviceData.getName());
		}

		login.logOut();
	}

}
