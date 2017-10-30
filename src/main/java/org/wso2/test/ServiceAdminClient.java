package org.wso2.test;

import java.rmi.RemoteException;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.wso2.carbon.service.mgt.stub.ServiceAdminStub;
import org.wso2.carbon.service.mgt.stub.types.carbon.ServiceMetaDataWrapper;

public class ServiceAdminClient {

	private String serviceAdminEndPoint;
	private final String serviceAdminServiceName = "ServiceAdmin";
	private ServiceAdminStub serviceAdminStub;

	public ServiceAdminClient(String backEndUrl, String sessionCookie) throws AxisFault {
		
		this.serviceAdminEndPoint = backEndUrl + "/services/" + serviceAdminServiceName;
		
		serviceAdminStub = new ServiceAdminStub(serviceAdminEndPoint); 
		// Authenticate Your stub from sessionCooke
		ServiceClient serviceAdminServiceClient;
		Options serviceAdminoption;
		serviceAdminServiceClient = serviceAdminStub._getServiceClient();
		serviceAdminoption = serviceAdminServiceClient.getOptions();
		serviceAdminoption.setManageSession(true);
		serviceAdminoption.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);
		
	}

	public void deleteService(String[] serviceGroup) throws RemoteException {
		serviceAdminStub.deleteServiceGroups(serviceGroup);

	}

	public ServiceMetaDataWrapper listServices() throws RemoteException {
		return serviceAdminStub.listServices("ALL", "*", 0);
	}
}
