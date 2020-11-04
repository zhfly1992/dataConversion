/*
 * File name: DeviceReportServiceImpl.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 guofeilong 2020年8月5日 ...
 * ... ...
 *
 ***************************************************/

package com.run.dataConversion.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.run.dataConversion.entity.ApplicationEntity;
import com.run.dataConversion.entity.Attributes;
import com.run.dataConversion.entity.Conversion;
import com.run.dataConversion.entity.DeviceEntity;
import com.run.dataConversion.repository.ApplicationInfoRepository;
import com.run.dataConversion.repository.AttributesRepository;
import com.run.dataConversion.repository.ConversionRepository;
import com.run.dataConversion.repository.DeviceReportRepository;
import com.run.dataConversion.repository.DeviceRepository;
import com.run.dataConversion.service.DeviceReportService;
import com.run.dataConversion.util.PostOrGet;
import com.run.dataConversion.util.UtilTool;
import com.run.entity.common.Result;
import com.run.entity.tool.DateUtils;
import com.run.entity.tool.ResultBuilder;

/**
 * @Description:
 * @author: guofeilong
 * @version: 1.0, 2020年8月5日
 */

@Service
public class DeviceReportServiceImpl implements DeviceReportService {

	private static final Logger		logger	= Logger.getLogger(DeviceReportServiceImpl.class);

	@Autowired
	private DeviceRepository		deviceRepository;
	@Autowired
	private ConversionRepository	conversionRepository;
	@Autowired
	private AttributesRepository	attributesRepository;
	@Autowired
	private ApplicationInfoRepository	applicationInfoRepository;

	private static Map<String, Object> num2Pt = Maps.newHashMap();
	{
		num2Pt.put("7", "balance sensor");
	}
	

	/**
	 * @see com.run.dataConversion.service.DeviceReportService#receiveReportInfo(java.lang.String,
	 *      com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public Result<String> receiveReportInfo(String reportStr) {
		try {
			if (StringUtils.isBlank(reportStr)) {
				logger.error("receiveReportInfo()-->上报数据为空");
				return ResultBuilder.failResult("上报数据不能为空");
			}
			logger.info(String.format("[receiveReportInfo()->接收到的数据:%s]", reportStr));
			if (UtilTool.isNotMatchJson(reportStr)) {
				logger.error("receiveReportInfo()-->上报数据格式不是json");
				return ResultBuilder.failResult("上报数据不是json格式");
			}
			// 需要保存到conversion表的数据
			Conversion conversion = new Conversion();
			conversion.setId(UtilTool.getUuId());
			conversion.setReceiveData(reportStr);
			String receiveTime = DateUtils.formatDate(new Date());
			
			

			JSONObject reportJson = JSONObject.parseObject(reportStr);
			String timestamp = reportJson.getString("timestamp");
			String hardware = reportJson.getString("IMEI");
			String iotDeviceId = reportJson.getString("deviceId");

			// 根据iot的deviceId获取设备信息
			DeviceEntity deviceEntity = deviceRepository.queryDeviceInfoByIOTDeviceId(iotDeviceId);
			if (null == deviceEntity) {
				logger.error(String.format("receiveReportInfo()->error:未查询到设备信息,iotDeviceId: %s", iotDeviceId));
				return ResultBuilder.successResult(null, "success");
			}
			String deviceTypeId = deviceEntity.getDeviceTypeId();
			String applicationId = deviceEntity.getApplicationId();

			
			 conversion.setApplicationId(applicationId);
			 conversion.setDeviceId(deviceEntity.getId());
			 conversion.setHardwareId(deviceEntity.getHardware());
			 conversion.setReceiveTime(receiveTime);
			 
			
			if (StringUtils.isBlank(deviceTypeId)) {
				logger.error(String.format("receiveReportInfo()->error:设备类型为空,deviceId: %s", deviceEntity.getId()));
				conversionRepository.addConversion(conversion);
				return ResultBuilder.successResult(null, "success");
			}

			// 存放转换后的数据
			JSONObject convertedData = new JSONObject();
			convertedData.put("deviceId", reportJson.getString("deviceId"));
			convertedData.put("productId", reportJson.getString("productId"));
			convertedData.put("timestamp", timestamp);
			convertedData.put("messageType", reportJson.getString("messageType"));
			convertedData.put("tenantId", reportJson.getString("tenantId"));
			// 为数据中添加things字段，暂时使用，用于匹配接受老iot数据的代码
			JSONObject things = new JSONObject();
			things.put("thingType", "WingsIot");
			things.put("subThingId", hardware);
			things.put("gatewayId", reportJson.getString("productId"));
			convertedData.put("things", things);

			JSONObject payloadJson = reportJson.getJSONObject("payload");
			List<Attributes> iotRsLocmanAttributes = attributesRepository.getAttributeByApplicationId(applicationId,
					hardware);
			Map<String, Attributes> iotMarkRsAttributes = Maps.newHashMap();
			for (Attributes attribute : iotRsLocmanAttributes) {
				if (StringUtils.isNotBlank(attribute.getIotMark())) {
					iotMarkRsAttributes.put(attribute.getIotMark(), attribute);
				}
			}
			// 用于存储转换后的数据点信息
			JSONArray attributeInfoArray = new JSONArray();
			Set<Entry<String, Object>> entrySet = payloadJson.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				JSONObject attributeJson = new JSONObject();
				
				if (iotMarkRsAttributes.containsKey(entry.getKey())) {
					Attributes attribute = iotMarkRsAttributes.get(entry.getKey());
					attributeJson.put("attributeName", attribute.getLocmanMark());
					attributeJson.put("attributeAlias", attribute.getLocmanName());
					attributeJson.put("attributeReportedValue", entry.getValue());
				} else {
					attributeJson.put("attributeName", entry.getKey());
					attributeJson.put("attributeAlias", entry.getKey());
					attributeJson.put("attributeReportedValue", entry.getValue());
				}

				//TODO 转换
				if ("dt".equals(entry.getKey())) {
					attributeJson.put("attributeReportedValue", "device common protocol");
				}
				if ("pt".equals(entry.getKey())) {
					Object value = entry.getValue();
					if (num2Pt.containsKey(value)) {
						attributeJson.put("attributeReportedValue", num2Pt.get(value));
					} else {
						attributeJson.put("attributeReportedValue", entry.getValue());
					}
				}
				attributeJson.put("attributeReportedTime", timestamp);
				attributeInfoArray.add(attributeJson);
			}

			convertedData.put("attributeInfo", attributeInfoArray);

			conversion.setConversionData(convertedData.toJSONString());
			// 根据applicationId获取推送地址
			ApplicationEntity applicationInfo = applicationInfoRepository.getApplicationInfoById(applicationId);
			// 根据applicationId查询不到状态为'enable'的应用，不进行推送
			if (null == applicationInfo) {
				logger.error("receiveReportInfo()->根据设备applicationId查询不到相应可用application信息，不进行推送");
				conversion.setResponseData("不进行推送");
				conversion.setResponseStatusCode(null);
				conversion.setSendToLocmanTime(null);
			} else {
				// 进行推送
				Map<String, Object> headermap = new HashMap<String, Object>();
				headermap.put("Content-Type", "application/json");
				String sendTime = DateUtils.formatDate(new Date());

				JSONObject doPost = PostOrGet.doPost(applicationInfo.getLocmanUrl().trim(), convertedData.toJSONString(),
						headermap);
				conversion.setSendToLocmanTime(sendTime);
				if (null == doPost) {
					logger.error("receiveReportInfo()->推送数据失败" + conversion);
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
				logger.info("数据添加成功:" + conversion.getId());
				return ResultBuilder.successResult(conversion.getId(), "success");
			} else {
				logger.error("数据添加失败:" + conversion);
				return ResultBuilder.successResult(conversion.getId(), "success");
			}
			
		} catch (Exception e) {
			logger.error("receiveReportedData()->转换数据错误", e);
			return ResultBuilder.failResult("转换数据错误");
		}
	}

}
