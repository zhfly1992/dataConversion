/*
* File name: DeviceTypeService.java								
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
* 1.0			guofeilong		2020年7月10日
* ...			...			...
*
***************************************************/

package com.run.dataConversion.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.run.entity.common.Result;

/**
* @Description:	
* @author: guofeilong
* @version: 1.0, 2020年7月10日
*/

public interface DeviceTypeService {
	
	Result<String> createDeviceType(JSONObject json);

	/**
	  * 
	  * @Description:
	  * @param 
	  * @return
	  */
	
	Result<String> synchDeviceType(JSONObject json);

	/**
	  * 
	  * @param pageSize 
	 * @param pageNum 
	 * @Description:
	  * @param 
	  * @return
	  */
	
	Result<PageInfo<Map<String, Object>>> getAllDeviceTypes(String userId, Integer pageNum, Integer pageSize,String searchKey);

	/**
	  * 
	  * @Description:
	  * @param 
	  * @return
	  */
	
	Result<Map<String, Object>> getDeviceTypeById(String applicationId, String id);

	/**
	  * 
	  * @Description:
	  * @param 
	  * @return
	  */
	
	Result<String> updateDeviceType(JSONObject json);
}
