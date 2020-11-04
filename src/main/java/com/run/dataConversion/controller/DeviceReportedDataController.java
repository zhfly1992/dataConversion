/*
* File name: DeviceReportedDataReceive.java								
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

package com.run.dataConversion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.run.dataConversion.constants.UrlConstants;
import com.run.dataConversion.service.DeviceReportedDataService;
import com.run.entity.common.Result;

/**
* @Description:	
* @author: zh
* @version: 1.0, 2020年7月17日
*/
@RestController
@CrossOrigin("*")
@RequestMapping(value = UrlConstants.DATAREPORTED)
public class DeviceReportedDataController {
	
	@Autowired
	private DeviceReportedDataService deviceReportedDataService;
	
	/**
	 * 
	* @Description:用于接收华为iot上报数据
	* @param jsonObject
	* @return
	 */
	@PostMapping(value = UrlConstants.HUAWEIIOT)
	public Result<String> deviceReportedDataReceiveForHuaWei(@RequestBody JSONObject jsonObject){
		return deviceReportedDataService.huaWeiReportedDataReceive(jsonObject);
	}
	
	
	@PostMapping(value = UrlConstants.GETCONVERSIONINFO)
	public  Result<?> getReportedDataConversion(@RequestBody JSONObject paramJson) {
		return deviceReportedDataService.getConvsersion(paramJson);
	}
		
}
