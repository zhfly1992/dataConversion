/*
* File name: ApplicationController.java								
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

package com.run.dataConversion.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.run.dataConversion.constants.UrlConstants;
import com.run.dataConversion.service.ApplicationInfoService;
import com.run.entity.common.Result;


/**
* @Description:	应用
* @author: 钟滨远
* @version: 1.0, 2020年7月8日
*/
@RestController
@CrossOrigin(value="*")
@RequestMapping(UrlConstants.APPLICATION)
public class ApplicationInfoController {
	
	@Autowired
	private ApplicationInfoService		applicationInfoService;
	
	
	@PostMapping(UrlConstants.ADDAPPLICATIONINFO)
	public Result<String> addApplicationInfo(@RequestBody JSONObject json){
		
		return applicationInfoService.addApplicationInfo(json);
		
	}
	
	@PostMapping(UrlConstants.GETAPPLICATIONINFO)
	public Result<PageInfo<Map<String,Object>>> getApplicationInfo(@RequestBody JSONObject json){
		
		return applicationInfoService.getApplicationInfo(json);
		
	}
	@PostMapping(UrlConstants.UPDATEAPPLICATIONINFO)
	public Result<String> updateApplicationInfo(@RequestBody JSONObject json){
		
		return applicationInfoService.updateApplicationInfo(json);
		
	}
	@PostMapping(UrlConstants.DISABLEAPPLICATIONINFO)
	public Result<String> disableApplicationInfo(@RequestBody JSONObject json){
		
		return applicationInfoService.disableApplicationInfo(json);
		
	}
	
	@GetMapping(UrlConstants.IOT_NAME_LIST)
	public Result<List<Map<String, Object>>> getIotNameList(){
		return applicationInfoService.getIotNameList();
	}
	

}
