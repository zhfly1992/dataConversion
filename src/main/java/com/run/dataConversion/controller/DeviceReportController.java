/*
* File name: DeviceReportController.java								
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
* 1.0			guofeilong		2020年8月5日
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


import com.run.dataConversion.constants.UrlConstants;
import com.run.dataConversion.service.DeviceReportService;
import com.run.entity.common.Result;

/**
* @Description:	
* @author: guofeilong
* @version: 1.0, 2020年8月5日
*/
@RestController
@CrossOrigin(value = "*")
@RequestMapping(UrlConstants.DEVICE_REPORT)
public class DeviceReportController {
	
	@Autowired
	private DeviceReportService deviceReportService;
	
	@PostMapping(value = UrlConstants.RECEIVE)
	public Result<String> receiveReportInfo(@RequestBody String reportJson) {
		return deviceReportService.receiveReportInfo(reportJson);

	}
}
