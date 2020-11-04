/*
* File name: UtilTool.java								
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
* 1.0			guofeilong		2019年3月19日
* ...			...			...
*
***************************************************/

package com.run.dataConversion.util;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.run.encryt.util.MD5;
import com.run.entity.common.RpcResponse;
import com.run.entity.tool.RpcResponseBuilder;

/**
* @Description:	
* @author: guofeilong
* @version: 1.0, 2019年3月19日
*/

public class UtilTool {

	/**
	 * 
	 * @Description:获取UUID
	 * @return String
	 */
	public static String getUuId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 
	 * 检测入参是否为标准的JSON字符串
	 * 
	 *
	 * @param param
	 * @return
	 */
	public static boolean isNotMatchJson(String param) {
		try {
			JSON.parseObject(param);
		} catch (Exception e) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @Description:判断是否包含参数,可以为空
	 * @param logger
	 * @param methodName
	 * @param json
	 * @param keys
	 * @return
	 */
	public static <T> RpcResponse<T> containsParamKey(Logger logger, String methodName, JSONObject json,
			String... keys) {
		for (String key : keys) {
			if (!json.containsKey(key)) {
				logger.error(String.format("[%s()->error:%s:%s]", methodName, "参数不存在！", key));
				return RpcResponseBuilder
						.buildErrorRpcResp(String.format("[%s:%s]", "参数不存在！", key));
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @Description:校验业务参数以及分页参数
	 * @param logger
	 * @param methodName
	 *            方法名
	 * @param obj
	 *            对象
	 * 
	 * @param keys
	 *            需要校验的业务参数key
	 * 
	 * @throws Exception
	 * 
	 */
	public static <T> RpcResponse<T> checkObjectBusinessKey(Logger logger, String methodName, Object obj,
			String... keys) throws Exception {

		// 校验对象
		if (obj == null) {
			return RpcResponseBuilder.buildErrorRpcResp(String.format("[%s:%s]", obj, "业务对象为null"));
		}

		// 校验业务参数
		JSONObject jsonParam = (JSONObject) JSONObject.toJSON(obj);
		for (String key : keys) {
			if (StringUtils.isBlank(jsonParam.getString(key))) {
				logger.error(String.format("[%s()->error:%s:%s]", methodName, "业务参数为null！", key));
				return RpcResponseBuilder
						.buildErrorRpcResp(String.format("[%s:%s]", "业务参数为null！", key));
			}
		}

		return null;
	}
	
	/**
	 * 
	 * @Description: 生成固定的设备id
	 * @param applicationId
	 *            应用id
	 * @param iotAppId
	 *            iotAppId
	 * @param iotAppKey
	 *            iotAppKey
	 * @param hardwareId
	 *            硬件id
	 * @return 设备id
	 */
	public static String getDeviceId(String applicationId, String iotAppId, String iotAppKey, String hardwareId) {

		StringBuffer deviceIdBuffer = new StringBuffer();
		String idStr = deviceIdBuffer.append(applicationId).append(iotAppId).append(iotAppKey).append(hardwareId)
				.toString();
		String id = MD5.encrytMD5(idStr);
		return id;
	}

}
