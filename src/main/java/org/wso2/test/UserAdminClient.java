package org.wso2.test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.wso2.carbon.user.mgt.stub.UserAdminStub;
import org.wso2.carbon.user.mgt.stub.UserAdminUserAdminException;
import org.wso2.carbon.user.mgt.stub.types.carbon.UIPermissionNode;

public class UserAdminClient {

	private final String userAdminServiceName = "UserAdmin";
	private UserAdminStub userAdminStub;
	private String userAdminEndPoint;

	public UserAdminClient(String backEndUrl, String sessionCookie) throws AxisFault {
		this.userAdminEndPoint = backEndUrl + "/services/" + userAdminServiceName;
		
		userAdminStub = new UserAdminStub(userAdminEndPoint); 
		// Authenticate Your stub from sessionCooke
		ServiceClient userAdminServiceClient;
		Options option;
		userAdminServiceClient = userAdminStub._getServiceClient();
		option = userAdminServiceClient.getOptions();
		option.setManageSession(true);
		option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);
	
	}

	public void getRolePermissions(String role) throws RemoteException, UserAdminUserAdminException {
		List allowedPermissions = new ArrayList();
		UIPermissionNode uiPermissionNode = userAdminStub.getRolePermissions(role);
		getResourcePath(uiPermissionNode, allowedPermissions);
		System.out.println(allowedPermissions);
	}

	public void getResourcePath(UIPermissionNode uiPermissionNode, List allowedPermissions) {

		if (uiPermissionNode.getNodeList() != null) {
			UIPermissionNode[] uiPermissionNodes = uiPermissionNode.getNodeList();
			for (int i = 0; i < uiPermissionNodes.length; i++) {
				UIPermissionNode uPermissionNode1 = uiPermissionNodes[i];
				if (uPermissionNode1.getSelected()) {
					allowedPermissions.add(uPermissionNode1.getResourcePath());
				}
				getResourcePath(uPermissionNode1, allowedPermissions);
			}
		}
		return;
	}
}
