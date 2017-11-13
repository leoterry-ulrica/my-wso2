package org.wso2.test;

import org.apache.axis2.client.ServiceClient;
import org.wso2.carbon.analytics.activitydashboard.stub.ActivityDashboardAdminServiceStub;
import org.wso2.carbon.utils.CarbonUtils;

public class DashboardAdminClient {
	/**
	 * 每个服务模块端点
	 */
	private static String endpoint = GlobalConf.AnalyticsEndUrl + "DashboardAdmin";
	private static ActivityDashboardAdminServiceStub dashboardAdmin;
	
	private static void init() throws Exception {
	     GlobalConf.setProperties();
		
	     dashboardAdmin = new ActivityDashboardAdminServiceStub(endpoint);
		ServiceClient serviceClient = dashboardAdmin._getServiceClient();

		CarbonUtils.setBasicAccessSecurityHeaders(GlobalConf.UserName, GlobalConf.Password, serviceClient);
		
	}
	public static void main(String[] args) throws Exception {
		init();
		
		String[] tables = dashboardAdmin.getAllTables();
		System.out.println(tables.length);
	}
}
