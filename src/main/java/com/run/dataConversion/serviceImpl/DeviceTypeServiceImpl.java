/*
 * File name: DeviceTypeServiceImpl.java
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

package com.run.dataConversion.serviceImpl;


import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.run.dataConversion.repository.DeviceTypeRepository;
import com.run.dataConversion.service.DeviceService;
import com.run.dataConversion.service.DeviceTypeService;
import com.run.dataConversion.util.UtilTool;
import com.run.entity.common.Result;
import com.run.entity.common.RpcResponse;
import com.run.entity.tool.ResultBuilder;

/**
 * @Description:
 * @author: guofeilong
 * @version: 1.0, 2020年7月10日
 */
@Service
public class DeviceTypeServiceImpl implements DeviceTypeService {

	private static final Logger		logger	= Logger.getLogger(DeviceTypeServiceImpl.class);

	@Autowired
	private DeviceTypeRepository	deviceTypeRepository;

	@Autowired
	private DeviceService			deviceService;



	/**
	 * @see com.run.dataConversion.service.DeviceTypeService#createDeviceType(com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public Result<String> createDeviceType(JSONObject json) {
		logger.info(String.format("[createDeviceType()->方法开始执行！参数：%s]", json));
		try {
			if (json.isEmpty()) {
				logger.error("传入json为空");
				return ResultBuilder.emptyResult();
			}
			RpcResponse<String> containsParamKey = UtilTool.checkObjectBusinessKey(logger, "createDeviceType", json,
					"deviceTypeName", "applicationId", "iotDeviceTypeId", "iotDeviceTypeName");
			if (null != containsParamKey) {
				logger.error("传入json为空");
				return ResultBuilder.emptyResult();
			}
			String id = UtilTool.getUuId();
			Map<String, Object> paramers = Maps.newHashMap();
			paramers.put("id", id);
			paramers.put("deviceTypeName", json.getString("deviceTypeName"));
			paramers.put("applicationId", json.getString("applicationId"));
			paramers.put("iotDeviceTypeId", json.getString("iotDeviceTypeId"));
			paramers.put("iotDeviceTypeName", json.getString("iotDeviceTypeName"));
			Integer result = deviceTypeRepository.createDeviceType(paramers);
			if (result > 0) {
				logger.info("[createDeviceType()->新增设备类型成功！");
				return ResultBuilder.successResult(id, "新增设备类型成功");
			} else {
				logger.error("[createDeviceType()->新增设备类型失败！");
				return ResultBuilder.failResult("新增设备类型失败");
			}
		} catch (Exception e) {
			logger.error("错误信息:" + e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @see com.run.dataConversion.service.DeviceTypeService#synchDeviceType(com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public Result<String> synchDeviceType(JSONObject json) {
		try {
			RpcResponse<String> containsParamKey = UtilTool.checkObjectBusinessKey(logger, "synchDeviceType", json,
					"deviceTypeId", "applicationId");
			if (null != containsParamKey) {
				return ResultBuilder.emptyResult();
			}

			String applicationId = json.getString("applicationId");
			String deviceTypeId = json.getString("deviceTypeId");
			Map<String, Object> deviceTypeInfo = deviceTypeRepository.getDeviceTypeById(deviceTypeId, applicationId);

			if (null == deviceTypeInfo || deviceTypeInfo.isEmpty()) {
				logger.error("没有查询到设备类型信息");
				return ResultBuilder.failResult("同步失败,设备类型信息查询失败");
			}
			Object deviceTypeNameObject = deviceTypeInfo.get("iotDeviceTypeName");
			if (null == deviceTypeNameObject || StringUtils.isBlank(deviceTypeNameObject + "")) {
				logger.error(String.format("iot设备类型信息名称为空:s%", deviceTypeNameObject));
				return ResultBuilder.failResult("同步失败,设备类型信息查询失败");
			}
			json.put("deviceTypeName", deviceTypeInfo.get("iotDeviceTypeName"));
			Result<String> deviceInfoFromIOT = deviceService.getDeviceInfoFromIOT(json);
			logger.info(String.format("synchDeviceType()-->%s", deviceInfoFromIOT));
			if ("0000".equals(deviceInfoFromIOT.getResultStatus().getResultCode())) {
				logger.info("synchDeviceType()-->同步成功");
				return ResultBuilder.successResult("同步成功", "同步成功");
			} else {
				logger.error("synchDeviceType()-->同步失败" + deviceInfoFromIOT.getResultStatus().getResultMessage());
				return ResultBuilder.failResult("同步失败" + deviceInfoFromIOT.getResultStatus().getResultMessage());
			}
		} catch (Exception e) {
			logger.error("错误信息:" + e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @see com.run.dataConversion.service.DeviceTypeService#getAllDeviceTypes(java.lang.String)
	 */
	@Override
	public Result<PageInfo<Map<String, Object>>> getAllDeviceTypes(String userId, Integer pageNum,
			Integer pageSize,String searchKey) {

		try {
			logger.info(String.format("[getAllDeviceTypes()->方法开始执行！userId：%s]", userId));
			if (StringUtils.isBlank(userId)) {
				logger.error("userId不能为空");
				return ResultBuilder.emptyResult();
			}
			if (pageNum == null || pageSize == null) {
				pageNum = 0;
				pageSize = 0;
			}
			if (pageNum < 0) {
				pageNum = 0;
			}
			if (pageSize < 0) {
				pageSize = 10;
			}
			if (StringUtils.isNotBlank(searchKey)) {
				if (searchKey.length() > 20) {
					logger.error(String.format("searchKey: 长度超出限制", searchKey));
					return ResultBuilder.invalidResult();
				}
			}

			PageHelper.startPage(pageNum, pageSize);
			List<Map<String, Object>> allDeviceTypes = deviceTypeRepository.getAllDeviceTypes(userId, searchKey);
			if (null != allDeviceTypes) {
				logger.info("getAllDeviceTypes查询成功!!!");
				PageInfo<Map<String, Object>> page = new PageInfo<>(allDeviceTypes);
				return ResultBuilder.successResult(page, "");
			} else {
				return ResultBuilder.failResult("查询失败");
			}

		} catch (Exception e) {
			logger.error("错误信息:" + e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @see com.run.dataConversion.service.DeviceTypeService#getDeviceTypeById(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Result<Map<String, Object>> getDeviceTypeById(String applicationId, String id) {
		try {
			logger.info(String.format("[getDeviceTypeById()->方法开始执行！applicationId：%s, id：%s]", applicationId, id));
			if (StringUtils.isBlank(applicationId)) {
				logger.error("applicationId不能为空");
				return ResultBuilder.emptyResult();
			}
			if (StringUtils.isBlank(id)) {
				logger.error("id不能为空");
				return ResultBuilder.emptyResult();
			}
			Map<String, Object> deviceTypeInfo = deviceTypeRepository.getDeviceTypeById(id, applicationId);
			if (null != deviceTypeInfo) {
				logger.info("deviceTypeInfo-->查询成功!!!");
				return ResultBuilder.successResult(deviceTypeInfo, "");
			} else {
				return ResultBuilder.failResult("查询失败");
			}

		} catch (Exception e) {
			logger.error("错误信息:" + e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @see com.run.dataConversion.service.DeviceTypeService#updateDeviceType(com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public Result<String> updateDeviceType(JSONObject json) {
		logger.info(String.format("[updateDeviceType()-->方法开始执行！参数：%s]", json));
		try {
			if (json.isEmpty()) {
				logger.error("updateDeviceType()-->传入json为空");
				return ResultBuilder.emptyResult();
			}
			RpcResponse<String> containsParamKey = UtilTool.checkObjectBusinessKey(logger, "updateDeviceType", json,
					"deviceTypeName", "applicationId", "iotDeviceTypeId", "iotDeviceTypeName", "id");
			if (null != containsParamKey) {
				logger.error("updateDeviceType()-->" + containsParamKey.getMessage());
				return ResultBuilder.failResult(containsParamKey.getMessage());
			}
			Map<String, Object> paramers = Maps.newHashMap();
			paramers.put("id", json.getString("id"));
			paramers.put("deviceTypeName", json.getString("deviceTypeName"));
			paramers.put("applicationId", json.getString("applicationId"));
			paramers.put("iotDeviceTypeId", json.getString("iotDeviceTypeId"));
			paramers.put("iotDeviceTypeName", json.getString("iotDeviceTypeName"));
			Integer result = deviceTypeRepository.updateDeviceType(paramers);
			if (result > 0) {
				logger.info("[updateDeviceType()->修改设备类型成功！");
				return ResultBuilder.successResult(json.getString("id"), "修改设备类型成功");
			} else {
				logger.error("[updateDeviceType()->修改设备类型失败！");
				return ResultBuilder.failResult("修改设备类型失败");
			}
		} catch (Exception e) {
			logger.error("错误信息:" + e);
			return ResultBuilder.exceptionResult(e);
		}
	}

}
