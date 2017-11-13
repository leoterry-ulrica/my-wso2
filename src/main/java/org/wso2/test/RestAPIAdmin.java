package org.wso2.test;

import org.apache.axis2.client.ServiceClient;
import org.wso2.carbon.rest.api.stub.RestApiAdminStub;
import org.wso2.carbon.rest.api.stub.types.carbon.APIData;
import org.wso2.carbon.rest.api.stub.types.carbon.ResourceData;
import org.wso2.carbon.utils.CarbonUtils;

public class RestAPIAdmin {

	/**
	 * 每个服务模块端点
	 */
	private static String endpoint = GlobalConf.BackEndUrl + "RestApiAdmin";
	private static RestApiAdminStub restApiAdmin = null;
	
	private static void init() throws Exception {
	     GlobalConf.setProperties();
		
		restApiAdmin = new RestApiAdminStub(endpoint);
		ServiceClient serviceClient = restApiAdmin._getServiceClient();

		CarbonUtils.setBasicAccessSecurityHeaders(GlobalConf.UserName, GlobalConf.Password, serviceClient);
	}
	public static void main(String[] args) throws Exception {
		
		init();
		
		// APIData apiData = addAPI();
		// updateAPI();
		deleteAPI();
	}
	/**
	 * 添加api
	 * @return
	 * @throws Exception 
	 */
	private static APIData addAPI() throws Exception {

		int apiCount = restApiAdmin.getAPICount();
		System.out.println(">>>api个数："+apiCount);
		
		APIData apiData = new APIData();
		apiData.setName("RestAPI");
		apiData.setContext("/rest");
		// 创建成功前，这个设置无效
	/*	apiData.setStatisticsEnable(true);
		apiData.setTracingEnable(true);*/
		// apiData.setHost("");
		// apiData.setPort(0);
		ResourceData resData = new ResourceData();
		apiData.addResources(resData);
		resData.addMethods("POST");
		resData.addMethods("GET");
		resData.setUriTemplate("/{city}");
		resData.setInSeqXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?><inSequence xmlns=\"http://ws.apache.org/ns/synapse\"><send><endpoint><address uri=\"http://192.168.200.118:8080/CQDGServer/rest/stat/cxgh/\"/></endpoint></send></inSequence>");
	
		if(restApiAdmin.addApi(apiData)) {
			System.out.println(">>>成功创建api："+apiData.getName());
			// 创建完成，再设置起效
			restApiAdmin.enableStatistics(apiData.getName());
			restApiAdmin.enableTracing(apiData.getName());
			// 必须cleanup，否则不保存进去
			restApiAdmin.cleanup();
		}
		return apiData;
		/*apiData.setStatisticsEnable(true);
		apiData.setTracingEnable(true);
		if(restApiAdmin.updateApi(apiData.getName(), apiData)) {
			System.out.println(">>>成功更新api："+apiData.getName());
		}*/
	}
	/**
	 * 更新api
	 * @throws Exception 
	 * @throws  
	 */
	public static void updateAPI() throws Exception {
		
		APIData apiData = restApiAdmin.getApiByName("RestAPI");
		ResourceData[] resDatas = apiData.getResources();
		resDatas[0].setMethods(new String[]{"POST"});
		apiData.setResources(resDatas);
		restApiAdmin.updateApi(apiData.getName(), apiData);
	}
	/**
	 * 删除api
	 * @throws Exception
	 */
	public static void deleteAPI() throws Exception {
		
		restApiAdmin.deleteApi("RestAPI");
		restApiAdmin.deleteAllApi();
		System.out.println(">>>完成删除api");
	}
}
