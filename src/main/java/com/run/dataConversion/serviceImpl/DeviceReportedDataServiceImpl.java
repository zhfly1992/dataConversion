/*
 * File name: DeviceReportedDataServiceImpl.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 Administrator 2020年7月17日
 * ... ... ...
 *
 ***************************************************/

package com.run.dataConversion.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.run.dataConversion.constants.UrlConstants;
import com.run.dataConversion.entity.ApplicationEntity;
import com.run.dataConversion.entity.Attributes;
import com.run.dataConversion.entity.Conversion;
import com.run.dataConversion.entity.DeviceEntity;
import com.run.dataConversion.repository.ApplicationInfoRepository;
import com.run.dataConversion.repository.AttributesRepository;
import com.run.dataConversion.repository.ConversionRepository;
import com.run.dataConversion.repository.DeviceRepository;
import com.run.dataConversion.service.DeviceReportedDataService;
import com.run.dataConversion.util.PostOrGet;
import com.run.dataConversion.util.UtilTool;
import com.run.entity.common.Result;
import com.run.entity.tool.DateUtils;
import com.run.entity.tool.ResultBuilder;

/**
 * @Description:
 * @author: Administrator
 * @version: 1.0, 2020年7月17日
 */
@Service
public class DeviceReportedDataServiceImpl implements DeviceReportedDataService {

	private static final Logger			logger	= Logger.getLogger(DeviceReportedDataServiceImpl.class);

	@Autowired
	private DeviceRepository			deviceRepository;
	@Autowired
	private AttributesRepository		attributesRepository;
	@Autowired
	private ConversionRepository		conversionRepository;
	@Autowired
	private ApplicationInfoRepository	applicationInfoRepository;



	/**
	 * @see com.run.dataConversion.service.DeviceReportedDataService#reportedDataReceive(com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public Result<String> huaWeiReportedDataReceive(JSONObject jsonObject) {

		try {
			if (null == jsonObject || jsonObject.isEmpty()) {
				logger.error("huaWeiReportedDataReceive()->error:传入数据为空");
				return ResultBuilder.failResult("传入数据为空");
			}
			String receiveTime = DateUtils.formatDate(new Date());
			logger.info(String.format("huaWeiReportedDataReceive()->info:接收到的上报信息---%s", jsonObject.toJSONString()));
			String iotDeviceId = jsonObject.getString("deviceId");

			// 根据iot的deviceId获取设备信息
			DeviceEntity deviceEntity = deviceRepository.queryDeviceInfoByIOTDeviceId(iotDeviceId);
			if (null == deviceEntity) {
				logger.error(String.format("huaWeiReportedDataReceive()->error:查询不到设备信息,iotDeviceId: %s", iotDeviceId));
				// TODO
				return ResultBuilder.successResult(null, "success");
			}
			String deviceTypeId = deviceEntity.getDeviceTypeId();

			String applicaionId = deviceEntity.getApplicationId();

			if (StringUtils.isBlank(deviceTypeId)) {
				logger.error(
						String.format("huaWeiReportedDataReceive()->error:设备类型为空,deviceId: %s", deviceEntity.getId()));
				// TODO
				return ResultBuilder.successResult(null, "success");
			}

			// 根据设备类型获取属性点，用于上报数据格式转换
			List<Attributes> attributesByDeviceType = attributesRepository.getAttributesByDeviceType(deviceTypeId,
					null);
			JSONObject transedData = dealAttributes(attributesByDeviceType, jsonObject);
			logger.info(String.format("huaWeiReportedDataReceive()->数据转换成功，转换后的数据：%s", transedData.toJSONString()));

			Conversion conversion = new Conversion();
			conversion.setId(UtilTool.getUuId());
			conversion.setApplicationId(applicaionId);
			conversion.setConversionData(transedData.toJSONString());
			conversion.setDeviceId(deviceEntity.getId());
			conversion.setHardwareId(deviceEntity.getHardware());
			conversion.setReceiveData(jsonObject.toJSONString());
			conversion.setReceiveTime(receiveTime);
			conversion.setResponseData(null);
			conversion.setResponseStatusCode(null);
			conversion.setSendToLocmanTime(null);

			// 根据applicationId获取推送地址
			ApplicationEntity applicationInfo = applicationInfoRepository.getApplicationInfoById(applicaionId);
			// 根据applicationId查询不到状态为'enable'的应用，不进行推送
			if (null == applicationInfo) {
				logger.error("huaWeiReportedDataReceive()->根据设备applicationId查询不到相应可用application信息，不进行推送");
				conversion.setResponseData("不进行推送");
				conversion.setResponseStatusCode(null);
				conversion.setSendToLocmanTime(null);
			}
			else {
				// 进行推送
				Map<String, Object> headermap = new HashMap<String, Object>();
				headermap.put("Content-Type", "application/json");
				String sendTime = DateUtils.formatDate(new Date());
				String url = applicationInfo.getLocmanUrl().trim() + UrlConstants.LOCMAN_REPORTEDDATA;
				System.out.println("推送地址：" + url);
				logger.info("[huaWeiReportedDataReceive()->推送url:" + url + "]");
				JSONObject doPost = PostOrGet.doPost(url, transedData.toJSONString(),
						headermap);
				conversion.setSendToLocmanTime(sendTime);
				if (null == doPost) {
					logger.error("huaWeiReportedDataReceive()->推送数据失败");
					conversion.setResponseData("推送数据失败");
					conversion.setResponseStatusCode(null);
				}
				else{
					conversion.setResponseData(doPost.getString("responseBody"));
					conversion.setResponseStatusCode(doPost.getString("StatusCode"));
				}
			}

			Integer addConversion = conversionRepository.addConversion(conversion);
			if (addConversion > 0) {
				logger.info("huaWeiReportedDataReceive()->数据转换信息存储成功");
			} else {
				logger.error("huaWeiReportedDataReceive()->数据转换信息存储失败");
			}
			return ResultBuilder.successResult(null, "success");
		} catch (Exception e) {
			logger.error("huaWeiReportedDataReceive()->exception: " + e);
			return ResultBuilder.successResult(null, "success");
		}
	}



	private JSONObject dealAttributes(List<Attributes> attributesList, JSONObject receiveData) throws Exception {
		// 属性点标识和中文名对应表
		Map<String, String> paraNameTransTable = new HashMap<>();
		// key值对应关系表
		Map<String, String> keyTransTable = new HashMap<>();
		for (Attributes attributes : attributesList) {
			keyTransTable.put(attributes.getIotMark(), attributes.getLocmanMark());
			paraNameTransTable.put(attributes.getIotMark(), attributes.getLocmanName());
		}

		JSONObject finalData = new JSONObject();
		// 获取上报数据中的数据点数据
		JSONObject data = receiveData.getJSONObject("service").getJSONObject("data");

		// TODO 这里的deviceId需要带确认
		finalData.put("deviceId", receiveData.getString("deviceId"));

		long timestamp = data.getLongValue("sgt");

		finalData.put("timestamp", timestamp);

		// 为数据中添加things字段，暂时使用，用于匹配接受老iot数据的代码,后续更改后删除
		JSONObject things = new JSONObject();
		things.put("thingType", "HUAWEIiot");
		things.put("subThingId", data.getString("hardwareid"));
		things.put("gatewayId", "");
		finalData.put("things", things);

		// 用于存储转换后的数据点信息
		JSONArray attributeInfoArray = new JSONArray();

		for (Map.Entry entry : data.entrySet()) {
			JSONObject attribute = new JSONObject();
			String iotkey = (String) entry.getKey();

			if (paraNameTransTable.containsKey(iotkey)) {
				attribute.put("attributeName", keyTransTable.get(iotkey));
				attribute.put("attributeAlias", paraNameTransTable.get(iotkey));
				attribute.put("attributeReportedTime", timestamp);
				attribute.put("attributeReportedValue", entry.getValue());
				attributeInfoArray.add(attribute);
			}
		}
		finalData.put("attributeInfo", attributeInfoArray);
		return finalData;
	}



	/**
	 * @see com.run.dataConversion.service.DeviceReportedDataService#getConvsersion(com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public  Result<?> getConvsersion(JSONObject paramJson) {
		// TODO Auto-generated method stub
		try {
			logger.info(String.format("[getConvsersion()->parm:%s]", paramJson.toJSONString()));
			if (null == paramJson || paramJson.isEmpty()) {
				logger.error(String.format("getConvsersion()->error:%s", "请求数据为空"));
				return ResultBuilder.invalidResult();
			}


			if (!paramJson.containsKey("deviceId")) {
				logger.error("[getConvsersion()->error:参数无deviceId]");
				return ResultBuilder.invalidResult();
			}
			String deviceId = paramJson.getString("deviceId");
			if (StringUtils.isEmpty(deviceId)) {
				return ResultBuilder.failResult("deviceId不能为空");
			}
			List<Conversion> conversonByDeviceId = conversionRepository.getConversonByDeviceId(deviceId);
			if (null == conversonByDeviceId) {
				logger.error("[getConvsersion()->error:查询错误]");
				return ResultBuilder.failResult("查询失败");
			}
			logger.info(String.format("[getConvsersion()->查询成功:查询数据量%s]", conversonByDeviceId.size()));
			return ResultBuilder.successResult(conversonByDeviceId, "查询成功");
			
		} catch (Exception e) {
			logger.error("getConvsersion()->exception: " + e);
			return ResultBuilder.exceptionResult(e);
		}
	}

}
