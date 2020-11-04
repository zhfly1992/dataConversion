/*
 * File name: PostOrGet.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 zhaoweizhi 2018年10月12日 ...
 * ... ...
 *
 ***************************************************/

package com.run.dataConversion.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * @Description:
 * @author: zh
 * @version: 1.0, 2020年08月05日
 */

public class PostOrGet {

	/**
	 * 
	 * @Description:返回状态码封装字段"StatusCode",响应数据封装字段"responseBody"
	 * @param url
	 * @param content
	 * @param headerMap
	 * @return
	 */
	public static JSONObject doPost(String url, String content, Map<String, Object> headerMap) {
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;
		if (null != headerMap && headerMap.size() > 0) {
			Iterator<Map.Entry<String, Object>> it = headerMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> entry = it.next();
				httpPost.addHeader(entry.getKey(), (String) entry.getValue());
			}
		}
		try {
			httpPost.setEntity(new StringEntity(content, "UTF-8"));

			HttpResponse httpResponse = HttpClients.createDefault().execute(httpPost);

			jsonObject = new JSONObject();
			//状态码封装
			jsonObject.put("StatusCode", httpResponse.getStatusLine().getStatusCode());

			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				String result = EntityUtils.toString(entity, "UTF-8");
				//返回数据封装
				jsonObject.put("responseBody", JSONObject.parseObject(result));
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;

	}



	public static JSONObject doGet(String url, Map<String, String> headerMap) {
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		if (null != headerMap && headerMap.size() > 0) {
			Iterator<Map.Entry<String, String>> it = headerMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				httpGet.addHeader(entry.getKey(), entry.getValue());
			}
		}
		try {
			HttpResponse httpResponse = HttpClients.createDefault().execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				String result = EntityUtils.toString(entity, "UTF-8");
				jsonObject = JSONObject.parseObject(result);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}



	public static String doGetString(String url) {
		HttpGet httpGet = new HttpGet(url);
		String result = null;
		try {
			HttpResponse httpResponse = HttpClients.createDefault().execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
