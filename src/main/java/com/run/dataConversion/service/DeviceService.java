package com.run.dataConversion.service;

import com.alibaba.fastjson.JSONObject;
import com.run.entity.common.Result;

public interface DeviceService {

	/**
	 * 向第三方IOT平台拉取设备信息
	 * 
	 * @param paramJson
	 * @return
	 */
	public <T> Result<T> getDeviceInfoFromIOT(JSONObject paramJson);



	/**
	 * 查询设备信息列表
	 * 
	 * @param paramJson
	 * @return
	 */
	public Result<?> getDeviceInfoList(JSONObject paramJson);



	/**
	 * 
	 * @Description:推送设备数据到locman,暂用
	 * @param paramJson
	 * @return
	 */
	public Result<String> pushDeviceToLocman(JSONObject paramJson);

}
