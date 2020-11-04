/*
* File name: DeviceReportedData.java								
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
* 1.0			Administrator		2020年7月17日
* ...			...			...
*
***************************************************/

package com.run.dataConversion.service;

import com.alibaba.fastjson.JSONObject;
import com.run.entity.common.Result;

/**
* @Description:	
* @author: zh
* @version: 1.0, 2020年7月17日
*/

public interface DeviceReportedDataService {
	
	public Result<String> huaWeiReportedDataReceive(JSONObject jsonObject);
	
	
	public  Result<?> getConvsersion(JSONObject paramJson);
}
