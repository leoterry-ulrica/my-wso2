package org.wso2.test;

import org.apache.axis2.client.ServiceClient;
import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.analytics.webservice.stub.AnalyticsWebServiceStub;
import org.wso2.carbon.analytics.webservice.stub.beans.RecordBean;
import org.wso2.carbon.utils.CarbonUtils;

import junit.framework.Assert;

/**
 * 统计分析
 * @author weifj
 *
 */
public class AnalyticsWebServiceClient {

	/**
	 * 每个服务模块端点
	 */
	private static String endpoint = GlobalConf.AnalyticsEndUrl + "AnalyticsWebService";
	
	private static AnalyticsWebServiceStub analyticsWebServiceStub;
	private static void init() throws Exception {
	     GlobalConf.setProperties();
		
	     analyticsWebServiceStub = new AnalyticsWebServiceStub(endpoint);
		ServiceClient serviceClient = analyticsWebServiceStub._getServiceClient();

		CarbonUtils.setBasicAccessSecurityHeaders(GlobalConf.UserName, GlobalConf.Password, serviceClient);
	}
	
	public static void main(String[] args) throws Exception {
		// 初始化
		init();
		
		 // getRecordCount("ORG_WSO2_ESB_ANALYTICS_STREAM_EVENT", 1493568000000L, 1504195200000L);
		 // listTables();
		// test();
		getServiceRequestCount("Proxy Service", "testproxy", 1493568000000L, 1504195200000L);
	}
	public static void listTables() throws Exception {
		String[] tables =analyticsWebServiceStub.listTables();
		for(String tb : tables) {
			System.out.println("表：" + tb);
		}
	}
	/**
	 * 获取记录数（有问题）
	 * @param tableName
	 * @param timeFrom
	 * @param timeTo
	 * @return
	 * @throws Exception
	 */
	public static void getRecordCount(String tableName, long timeFrom, long timeTo) throws Exception {
		
		long count = analyticsWebServiceStub.getRecordCount(tableName, timeFrom, timeTo);
		System.out.println(">>>个数："+count);
	}
	/**
	 * 查询区间内服务请求次数
	 * @param componentType 类型不能为空
	 * @param componentName 可为空。空的情况下表示所有指定类型的服务；否则，表示指定服务
	 * @param timeFrom 开始时间，以毫秒为单位
	 * @param timeTo 结束时间，以毫秒为单位
	 * @throws Exception
	 */
	public static void getServiceRequestCount(String componentType, String componentName, long timeFrom, long timeTo) throws Exception {
		
		Assert.assertNotNull(componentType);
		StringBuilder buf = new StringBuilder();

		buf.append(" componentType: \"" + componentType + "\"");
		
		if(!StringUtils.isEmpty(componentName)) {
			buf.append(" and componentName: \"" + componentName + "\"");
		}
		buf.append(" and startTime : [" + timeFrom + " TO " + timeTo + "]");
		RecordBean[] beans = analyticsWebServiceStub.search("ORG_WSO2_ESB_ANALYTICS_STREAM_EVENT", buf.toString(), 0, 100000);
		System.out.println(">>>数据量：" + beans.length);
	}
}
