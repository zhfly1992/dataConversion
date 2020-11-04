/*
 * File name: DeviceTypeController.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 guofeilong 2020年7月10日 ...
 * ... ...
 *
 ***************************************************/

package com.run.dataConversion.controller;

import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.run.dataConversion.constants.UrlConstants;
import com.run.dataConversion.service.DeviceTypeService;
import com.run.entity.common.Result;

/**
 * @Description:
 * @author: guofeilong
 * @version: 1.0, 2020年7月10日
 */
@RestController
@CrossOrigin(value = "*")
@RequestMapping(UrlConstants.DEVICETYPE)
public class DeviceTypeController {

	@Autowired
	public DeviceTypeService deviceTypeService;



	@PostMapping(value = UrlConstants.ADD_DEVICETYPE)
	public Result<String> createDeviceType(@RequestBody JSONObject json) {
		return deviceTypeService.createDeviceType(json);

	}



	@PostMapping(value = UrlConstants.SYNCH_DEVICETYPE)
	public Result<String> synchDeviceType(@RequestBody JSONObject json) {
		return deviceTypeService.synchDeviceType(json);

	}



	@GetMapping(value = UrlConstants.ALL_DEVICETYPES)
	public Result<PageInfo<Map<String, Object>>> getAllDeviceTypes(@PathVariable("userId") String userId,
			@PathParam("pageNum") Integer pageNum, @PathParam("pageSize") Integer pageSize,
			@PathParam("searchKey") String searchKey) {
		return deviceTypeService.getAllDeviceTypes(userId, pageNum, pageSize,searchKey);

	}



	@GetMapping(value = UrlConstants.GET_DEVICETYPE_BY_ID)
	public Result<Map<String, Object>> getDeviceTypeById(@PathVariable("applicationId") String applicationId,
			@PathVariable("id") String id) {
		return deviceTypeService.getDeviceTypeById(applicationId, id);

	}
	
	
	
	@PostMapping(value = UrlConstants.UPDATE_DEVICETYPE)
	public Result<String> updateDeviceType(@RequestBody JSONObject json) {
		return deviceTypeService.updateDeviceType(json);

	}

}
