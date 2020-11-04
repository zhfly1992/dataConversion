/*
* File name: ApplicationService.java								
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
* 1.0			钟滨远		2020年7月8日
* ...			...			...
*
***************************************************/

package com.run.dataConversion.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.run.entity.common.Result;

/**
* @Description:	
* @author: 钟滨远
* @version: 1.0, 2020年7月8日
*/

public interface ApplicationInfoService {
	
	public Result<String> addApplicationInfo(JSONObject json);
	
	public Result<PageInfo<Map<String,Object>>> getApplicationInfo(JSONObject json);
	
	public Result<String> updateApplicationInfo(JSONObject json);
	
	public Result<String> disableApplicationInfo(JSONObject json);

	/**
	  * 
	  * @Description:
	  * @param 
	  * @return
	  */
	
	public Result<List<Map<String, Object>>> getIotNameList();
	

}
