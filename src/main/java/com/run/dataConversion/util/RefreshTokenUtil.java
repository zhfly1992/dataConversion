package com.run.dataConversion.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.StatusLine;

public class RefreshTokenUtil {
	/**
	 * 登录华为平台获取token信息
	 * @param httpsUtil
	 * @param paramJson
	 * @param url
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> loginGetToken(HttpsUtil httpsUtil,Map<String, String> paramMap,String url) {
		Map<String, String> data = new HashMap<>();
		try {
			StreamClosedHttpResponse responseLogin = httpsUtil.doPostFormUrlEncodedGetStatusLine(url, paramMap);
			data = JsonUtil.jsonString2SimpleObj(responseLogin.getContent(), data.getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;

	}
	
	
	
	/**
	 * 刷新access token 如果refreshToken过期重新登录获取
	 * @param httpsUtil
	 * @param paramJson
	 * @param url
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> refreshToken(HttpsUtil httpsUtil,Map<String, String> paramJson,String url) {
		Map<String, String> resultMap = new HashMap<>();
        String jsonRequest = JsonUtil.jsonObj2Sting(paramJson);
        StreamClosedHttpResponse bodyRefreshToken = httpsUtil.doPostJsonGetStatusLine(url, jsonRequest);
        StatusLine result = bodyRefreshToken.getStatusLine();
        int resultCode = result.getStatusCode();
        if (resultCode == 200) {
			resultMap = JsonUtil.jsonString2SimpleObj(bodyRefreshToken.getContent(), Map.class);
		}else if(resultCode!=200) {
			paramJson.remove("refreshToken");
        	resultMap = loginGetToken(httpsUtil,paramJson,Constant.APP_AUTH);
        }
		return resultMap;
	}
	
	
}
