/*
* File name: Test.java								
*
* Purpose:
*
* Functions used and called:	
* Name			Purpose
* ...			...
*
* Additional Information:
*
* Development History:
* Revision No.	Author		Date
* 1.0			Administrator		2020年8月7日
* ...			...			...
*
***************************************************/

package com.run;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

import com.ctg.ag.sdk.biz.AepProductManagementClient;

import com.ctg.ag.sdk.biz.aep_product_management.QueryProductRequest;
import com.ctg.ag.sdk.biz.aep_product_management.QueryProductResponse;
import com.github.pagehelper.StringUtil;
import com.run.dataConversion.util.Constant;
import com.run.dataConversion.util.EHCacheUtil;
import com.run.dataConversion.util.HttpsUtil;
import com.run.dataConversion.util.JsonUtil;
import com.run.dataConversion.util.PostOrGet;
import com.run.dataConversion.util.StreamClosedHttpResponse;
import com.run.entity.tool.DateUtils;



/**
* @Description:	
* @author: Administrator
* @version: 1.0, 2020年8月7日
*/

public class Test {
	public static void getProduct(String productId) throws Exception{
		AepProductManagementClient  client = AepProductManagementClient .newClient()
				  .appKey("cOw8ZbRuTK8").appSecret("DPw1zlldmK")
				  .build();
		QueryProductRequest request = new QueryProductRequest();
		request.setParamProductId(productId);
		QueryProductResponse queryProduct = client.QueryProduct(request);
		JSONObject jsonObject = JSONObject.parseObject(new String(queryProduct.getBody()));
		JSONObject jsonObject2 = jsonObject.getJSONObject("result");
		String string = jsonObject2.getString("apiKey");
		String s = jsonObject2.getString("deviceCount");
		System.out.println(string + "--------" + s);
		System.out.println(jsonObject.toJSONString());
		
		// 进行推送
		Map<String, Object> headermap = new HashMap<String, Object>();
		headermap.put("Content-Type", "application/json");
		String sendTime = DateUtils.formatDate(new Date());

		JSONObject doPost = PostOrGet.doPost("http://localhost:8018/device/reveive", jsonObject.toJSONString(),
				headermap);
	}
	
	public static void main(String[] args) throws Exception {
	//	getProduct("10053083");
		getDeviceTest();
	}
	
	
	public static void getDeviceTest() throws Exception{
		Map<String, String> headerMap = new HashMap<>();
		Map<String, String> paramMap = new HashMap<>();
		HttpsUtil httpsUtil = new HttpsUtil();
		httpsUtil.initSSLConfigForTwoWay("");
        String appId = Constant.APPID;
        String secret = Constant.SECRET;
        String urlLogin = Constant.APP_AUTH;

        Map<String, String> param = new HashMap<>();
        param.put("appId", appId);
        param.put("secret", secret);

        StreamClosedHttpResponse responseLogin = httpsUtil.doPostFormUrlEncodedGetStatusLine(urlLogin, param);
        Map<String, String> data = new HashMap<>();
        data = JsonUtil.jsonString2SimpleObj(responseLogin.getContent(), data.getClass());
        String accessToken = data.get("accessToken");
		String pullDeviceUrl = Constant.DEVICE_INFO_LIST;
		headerMap.put(Constant.HEADER_APP_KEY, Constant.APPID);
		headerMap.put(Constant.HEADER_APP_AUTH, "Bearer " + accessToken);
		paramMap.put("pageNo", "1");
		paramMap.put("pageSize", "20");
		
		StreamClosedHttpResponse aa = httpsUtil.doGetWithParasGetStatusLine(pullDeviceUrl, paramMap,
				headerMap);
		String content = aa.getContent();
		Map<String, Object> resultMap = JsonUtil.jsonString2SimpleObj(content, Map.class);
		System.out.println(123);
		List<Map<String, Object>> lisrt = (List<Map<String, Object>>) resultMap.get("devices");
		for (Map<String, Object> deviceMap : lisrt) {
			if (deviceMap.containsKey("services")) {
				List<Map<String, Object>> serviceList = (List<Map<String, Object>>) deviceMap
						.get("services");
				for (Object serviceObj : serviceList) {
					JSONObject serviceJson = JSONObject.parseObject(JsonUtil.jsonObj2Sting(serviceObj));
					JSONObject dataJson = serviceJson.getJSONObject("data");
					System.out.println(dataJson.toJSONString());

					break;
				}
			}
		}
		
		
		
		
	}
}
