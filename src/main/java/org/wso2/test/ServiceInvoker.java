package org.wso2.test;

import java.rmi.RemoteException;

import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;
import org.wso2.carbon.user.mgt.stub.UserAdminUserAdminException;

public class ServiceInvoker {
	
    public static void main(String[] args) throws RemoteException,
            LoginAuthenticationExceptionException,
            LogoutAuthenticationExceptionException {
        String path = "G:/wso2esb-5.0.1-SNAPSHOT/repository/resources/security/client-truststore.jks";
        System.setProperty("javax.net.ssl.trustStore", path);
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        String backEndUrl = "https://localhost:9443";

        LoginAdminServiceClient login = new LoginAdminServiceClient(backEndUrl);
        String sessionCookie = login.authenticate("admin", "admin");
        UserAdminClient serviceAdminClient = new UserAdminClient(
                backEndUrl, sessionCookie);

        try {
            serviceAdminClient.getRolePermissions("admin");
        } catch (UserAdminUserAdminException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        login.logOut();

    }
}
