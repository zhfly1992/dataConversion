package com.run.dataConversion.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.StatusLine;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ctg.ag.sdk.biz.AepDeviceManagementClient;
import com.ctg.ag.sdk.biz.AepProductManagementClient;
import com.ctg.ag.sdk.biz.aep_device_management.QueryDeviceListRequest;
import com.ctg.ag.sdk.biz.aep_device_management.QueryDeviceListResponse;
import com.ctg.ag.sdk.biz.aep_product_management.QueryProductRequest;
import com.ctg.ag.sdk.biz.aep_product_management.QueryProductResponse;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.run.dataConversion.constants.UrlConstants;
import com.run.dataConversion.entity.ApplicationEntity;
import com.run.dataConversion.entity.DeviceEntity;
import com.run.dataConversion.repository.ApplicationInfoRepository;
import com.run.dataConversion.repository.DeviceRepository;
import com.run.dataConversion.repository.DeviceTypeRepository;
import com.run.dataConversion.service.DeviceService;
import com.run.dataConversion.util.Constant;
import com.run.dataConversion.util.EHCacheUtil;
import com.run.dataConversion.util.HttpsUtil;
import com.run.dataConversion.util.JsonUtil;
import com.run.dataConversion.util.PostOrGet;
import com.run.dataConversion.util.RefreshTokenUtil;
import com.run.dataConversion.util.StreamClosedHttpResponse;
import com.run.dataConversion.util.UtilTool;
import com.run.entity.common.Result;
import com.run.entity.common.RpcResponse;
import com.run.entity.tool.DateUtils;
import com.run.entity.tool.ResultBuilder;

@Service
public class DeviceServiceImpl implements DeviceService {

	private static final Logger			logger	= Logger.getLogger(DeviceServiceImpl.class);

	@Autowired
	private DeviceRepository			deviceRepository;

	@Autowired
	private DeviceTypeRepository		deviceTypeRepository;
	@Autowired
	private ApplicationInfoRepository	applicationInfoRepository;



	@SuppressWarnings("unchecked")
	@Override
	public Result<String> getDeviceInfoFromIOT(JSONObject paramJson) {
		logger.info(String.format("[addApplicationInfo()->拉取设备信息，参数：%s]", paramJson.toJSONString()));

		/*
		 * appId={appId} gatewayId={gatewayId} nodeType={nodeType}
		 * deviceType={deviceType} pageNo={pageNo} pageSize={pageSize}
		 * startTime={startTime } endTime={endTime} status={status} sort={sort}
		 * select={select}
		 */

		try {
			if (paramJson.isEmpty()) {
				return ResultBuilder.emptyResult();
			}
			if (!paramJson.containsKey("deviceTypeId")) {
				logger.error("[getDeviceInfoFromIOT()->error:参数无deviceTypeId]");
				return ResultBuilder.invalidResult();
			}
			String deviceTypeId = paramJson.getString("deviceTypeId");
			if (StringUtils.isEmpty(deviceTypeId)) {
				return ResultBuilder.failResult("设备类型ID不能为空");
			}
			if (!paramJson.containsKey("applicationId")) {
				logger.error("[getDeviceInfoFromIOT()->error:参数无applicationId]");
				return ResultBuilder.invalidResult();
			}
			String applicationId = paramJson.getString("applicationId");
			if (StringUtils.isEmpty(applicationId)) {
				return ResultBuilder.failResult("应用ID不能为空");
			}
			ApplicationEntity applicationEntity = applicationInfoRepository.getApplicationInfoById(applicationId);
			if (null == applicationEntity) {
				logger.error("[getDeviceInfoFromIOT()->error:查询不到application信息]");
				return ResultBuilder.failResult("查询不到应用信息");
			}
			String iotName = applicationEntity.getIotName();
			String iotType = applicationInfoRepository.getIotTypeByName(iotName);
			// 从wings拉取设备
			if (iotType.equalsIgnoreCase("1")) {
				return getDeviceFromWingsIot(deviceTypeId, applicationEntity);
			}

			String appId = applicationEntity.getIotAppId();
			String secret = applicationEntity.getIotAppIKey();
			String pullDeviceUrl = Constant.DEVICE_INFO_LIST;
			String refTokenUrl = Constant.APP_AUTH;

			Map<String, Object> deviceTypeMap = deviceTypeRepository.getDeviceTypeById(deviceTypeId, applicationId);
			if (deviceTypeMap == null || deviceTypeMap.isEmpty() || deviceTypeMap.size() < 0) {
				return ResultBuilder.failResult("设备类型查询失败");
			}
			String deviceTypeName = String.valueOf(deviceTypeMap.get("deviceTypeName"));
			HttpsUtil httpsUtil = new HttpsUtil();
			httpsUtil.initSSLConfigForTwoWay(applicationId);
			String accessToken = "";
			String refreshToken = "";
			String token = String.valueOf(EHCacheUtil.get("accessToken_" + applicationId));
			if (StringUtil.isNotEmpty(token) && !StringUtils.equals(token, "null")) {
				String[] tokens = token.split(":");
				accessToken = tokens[0];
				refreshToken = tokens[1];
			}
			Map<String, String> headerMap = new HashMap<>();
			Map<String, String> paramMap = new HashMap<>();
			int count = 1;
			DeviceEntity device = null;
			// 查询了设备类型名称之后，设备类型id从请求参数中删除
			paramJson.remove("deviceTypeId");
			// 把applicationID从请求接口中去除
			paramJson.remove("applicationId");
			boolean flag = false;
			for (int i = 0; i < count; i++) {
				headerMap.put(Constant.HEADER_APP_KEY, appId);
				headerMap.put(Constant.HEADER_APP_AUTH, "Bearer " + accessToken);
				paramJson.put("pageNo", i);
				paramJson.put("pageSize", 100);
				paramMap = JSONObject.parseObject(paramJson.toJSONString(), new TypeReference<Map<String, String>>() {
				});
				StreamClosedHttpResponse responseLogin = httpsUtil.doGetWithParasGetStatusLine(pullDeviceUrl, paramMap,
						headerMap);
				StatusLine statusLine = responseLogin.getStatusLine();
				int resultCode = statusLine.getStatusCode();
				if (resultCode == 200) {// 请求正常，获取数据，且通过总条数计算for循环条件count的值
					String content = responseLogin.getContent();
					if (StringUtils.isEmpty(content)) {
						break;
					}
					Map<String, Object> resultMap = JsonUtil.jsonString2SimpleObj(content, Map.class);
					if (i == 0) {
						int totalCount = (int) resultMap.get("totalCount");
						count = (int) Math.ceil(totalCount * 1.0 / 100);
					}

					// 解析返回数据存入device中
					List<Object> devices = (List<Object>) resultMap.get("devices");
					for (Object object : devices) {
						// 设备标记 如果设备已经存在 值为true，执行update 反之 值为false，执行insert
						boolean dFlag = true;
						Map<String, Object> deviceMap = (Map<String, Object>) object;
						String iotDeviceId = String.valueOf(deviceMap.get("deviceId"));
						device = deviceRepository.queryDeviceInfoByIOTDeviceId(iotDeviceId);
						if (device == null) {
							dFlag = false;
							device = new DeviceEntity();
							device.setIotDeviceId(iotDeviceId);
							device.setDeviceCreateTime(String.valueOf(deviceMap.get("createTime")));
						}
						device.setDeviceTypeId(deviceTypeId);
						device.setId(UtilTool.getUuId());
						Date date = new Date();
						device.setCreatTime(DateUtils.formatDate(date));
						device.setUpdateTime(DateUtils.formatDate(date));
						device.setApplicationId(applicationId);
						Map<String, Object> deviceInfo = (Map<String, Object>) deviceMap.get("deviceInfo");
						device.setNodeId(String.valueOf(deviceInfo.get("nodeId")));
						device.setManufacturerName(String.valueOf(deviceInfo.get("manufacturerName")));
						device.setDeviceType(deviceTypeName);
						if (deviceMap.containsKey("services")) {
							List<Map<String, Object>> serviceList = (List<Map<String, Object>>) deviceMap
									.get("services");
							for (Object serviceObj : serviceList) {
								JSONObject serviceJson = JSONObject.parseObject(JsonUtil.jsonObj2Sting(serviceObj));
								JSONObject dataJson = serviceJson.getJSONObject("data");
								if (dataJson.containsKey("hardwareid")) {
									device.setHardware(dataJson.getString("hardwareid"));

								}

								break;
							}
						}
						// 当设备未上报或没有hardware值时，给默认值
						String hardware = device.getHardware();
						if (StringUtils.isEmpty(hardware)) {
							device.setHardware("00000000");
						}
						// 根据标记判断执行insert还是update
						if (dFlag == false) {
							int addCount = deviceRepository.addDeviceInfo(device);
							if (addCount < 0) {
								logger.error("[getDeviceInfoFromIOT()->error:IOT设备ID为" + device.getIotDeviceId()
										+ "的设备插入数据库失败]");
							}
						} else {
							int updateCount = deviceRepository.updateDeviceInfo(device);
							if (updateCount < 0) {
								logger.error("[getDeviceInfoFromIOT()->error:IOT设备ID为" + device.getIotDeviceId()
										+ "的设备修改数据失败]");
							}
						}
					}

				} else if (resultCode == 401 || resultCode == 403) {// token信息异常，重新刷新或登录获取新token，回滚到上一次请求
					if (flag == true) {
						return ResultBuilder.failResult("appId异常");
					}
					Map<String, String> tokenMap = new HashMap<>();
					tokenMap.put("appId", appId);
					tokenMap.put("secret", secret);
					tokenMap.put("refreshToken", refreshToken);
					Map<String, String> resultToken = RefreshTokenUtil.refreshToken(httpsUtil, tokenMap, refTokenUrl);
					accessToken = resultToken.get("accessToken");
					refreshToken = resultToken.get("refreshToken");
					EHCacheUtil.set("accessToken_" + applicationId, accessToken + ":" + refreshToken);
					i--;
					flag = true;
					continue;
				} else if (resultCode == 500) {
					logger.error("[getDeviceInfoFromIOT()->error:第三方接口调用出现500异常]");
					return ResultBuilder.failResult("第三方应用不存在");
				} else if (resultCode == 400) {
					logger.error("[getDeviceInfoFromIOT()->error:请求第三方接口参数不合法]");
					return ResultBuilder.failResult("向第三方请求参数不合法");
				}
			}
			logger.info("[getDeviceInfoFromIOT()->设备数据拉取成功");
			return ResultBuilder.successResult(null, "设备数据拉取成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultBuilder.exceptionResult(e);
		}
	}



	@Override
	public Result<?> getDeviceInfoList(JSONObject paramJson) {
		logger.info(String.format("[getDeviceInfoList()->查询设备信息列表，参数：%s]", paramJson.toJSONString()));
		try {
			if (paramJson.isEmpty() || paramJson.size() < 0) {
				return ResultBuilder.emptyResult();
			}
			Map<String, Object> paramMap = new HashMap<>();
			if (!paramJson.containsKey("userId")) {
				return ResultBuilder.invalidResult();
			}
			String userId = paramJson.getString("userId");
			if (StringUtils.isEmpty(userId)) {
				return ResultBuilder.failResult("用户ID不能为空");
			}
			paramMap.put("userId", userId);
			if (!paramJson.containsKey("pageSize")) {
				logger.error("[getDeviceInfoList()->error:参数无pageSize]");
				return ResultBuilder.invalidResult();
			}
			String pageSize = String.valueOf(paramJson.get("pageSize"));
			if (StringUtils.isEmpty(pageSize)) {
				return ResultBuilder.failResult("pageSize不能为空");
			}
			int size = Integer.parseInt(pageSize);
			paramMap.put("pageSize", size);
			if (!paramJson.containsKey("pageNo")) {
				logger.error("[getDeviceInfoList()->error:参数无pageNo]");
				return ResultBuilder.invalidResult();
			}
			String pageNo = String.valueOf(paramJson.get("pageNo"));
			if (StringUtils.isEmpty(pageNo)) {
				return ResultBuilder.failResult("pageNo不能为空");
			}
			int num = Integer.parseInt(pageNo);
			if (num > 0) {
				num = (num - 1) * size;
			}
			paramMap.put("pageStart", num);
			String selectKey = "";
			if (paramJson.containsKey("selectKey")) {
				selectKey = paramJson.getString("selectKey");
			}
			paramMap.put("selectKey", selectKey);
			String deviceTypeId = "";
			if (paramJson.containsKey("deviceTypeId")) {
				deviceTypeId = paramJson.getString("deviceTypeId");
			}
			paramMap.put("deviceTypeId", deviceTypeId);
			List<Map<String, Object>> deviceList = deviceRepository.queryDeviceInfo(paramMap);
			PageInfo<Map<String, Object>> page = new PageInfo<>(deviceList);
			int total = deviceRepository.queryDeviceInfoCount(paramMap);
			page.setPageNum(num);
			page.setTotal(total);
			page.setPageSize(size);
			page.setPages((int) Math.ceil(total * 1.0 / size));
			logger.info("[getDeviceInfoList()->设备信息查询成功");
			return ResultBuilder.successResult(page, "查询成功");
		} catch (Exception e) {
			return ResultBuilder.exceptionResult(e);
		}
	}



	private Result<String> getDeviceFromWingsIot(String deviceTypeId, ApplicationEntity applicationEntity) {
		try {
			logger.info(
					String.format("[getDeviceFromWingsIot()->该设备类型从电信WingsIot拉取设备，deviceTypeId:%s,applicationId:%s]",
							deviceTypeId, applicationEntity.getId()));
			String applicationId = applicationEntity.getId();
			String appKey = applicationEntity.getIotAppIKey();
			// wingsIot的appSecret为appId
			String appSecret = applicationEntity.getIotAppId();

			Map<String, Object> deviceTypeMap = deviceTypeRepository.getDeviceTypeById(deviceTypeId, applicationId);
			if (deviceTypeMap == null || deviceTypeMap.isEmpty() || deviceTypeMap.size() < 0) {
				return ResultBuilder.failResult("设备类型查询失败");
			}

			String productId = String.valueOf(deviceTypeMap.get("iotDeviceTypeId"));
			String apiKey;
			int deviceCount = 0;

			// 查询该产品下的设备数量
			AepProductManagementClient client = AepProductManagementClient.newClient().appKey(appKey)
					.appSecret(appSecret).build();
			QueryProductRequest request = new QueryProductRequest();
			request.setParamProductId(productId);
			QueryProductResponse queryProduct = client.QueryProduct(request);
			// StatusCode不等于200,请求失败，所有失败信息在电信wingsIOt平台文档查阅
			if (queryProduct.getStatusCode() != 200) {
				System.out.println(queryProduct.getMessage());
				client.shutdown();
				return ResultBuilder.failResult(queryProduct.getMessage());
			} else {
				// 获取返回的信息体
				JSONObject jsonObject = JSONObject.parseObject(new String(queryProduct.getBody()));
				// code不为0，则获取失败
				if (jsonObject.getInteger("code") != 0) {
					System.out.println(jsonObject.getString("msg"));
					client.shutdown();
					return ResultBuilder.failResult(jsonObject.getString("msg"));
				} else {
					apiKey = jsonObject.getJSONObject("result").getString("apiKey");
					deviceCount = jsonObject.getJSONObject("result").getIntValue("deviceCount");
				}
			}

			// 产品信息查询成功，获取设备列表
			// 每次最大查询设备数量为100

			int page = deviceCount / 100 + 1;
			for (int i = 1; i <= page; i++) {
				AepDeviceManagementClient deviceClient = AepDeviceManagementClient.newClient().appKey(appKey)
						.appSecret(appSecret).build();

				QueryDeviceListRequest deviceRequest = new QueryDeviceListRequest();
				// set your request params here
				deviceRequest.setParamMasterKey(apiKey); // single value
				// request.addParamMasterKey(MasterKey); // or multi values
				deviceRequest.setParamProductId(productId); // single value
				// request.addParamProductId(productId); // or multi values
				// request.setParamSearchValue(searchValue); // single value
				// request.addParamSearchValue(searchValue); // or multi values
				deviceRequest.setParamPageNow(i); // single value
				// request.addParamPageNow(pageNow); // or multi values
				deviceRequest.setParamPageSize(100); // single value
				// request.addParamPageSize(pageSize); // or multi values
				// System.out.println(client.QueryDeviceList(request));
				System.out.println("========productId:" + productId);
				QueryDeviceListResponse queryDeviceList = deviceClient.QueryDeviceList(deviceRequest);
				if (queryDeviceList.getStatusCode() != 200) {
					System.out.println(queryDeviceList.getMessage());
					deviceClient.shutdown();
					return ResultBuilder.failResult(queryDeviceList.getMessage());
				} else {
					JSONObject jsonObject = JSONObject.parseObject(new String(queryDeviceList.getBody()));
					JSONArray jsonArray = jsonObject.getJSONObject("result").getJSONArray("list");
					System.out.println(jsonArray.size());

					if (jsonObject.getInteger("code") != 0) {
						deviceClient.shutdown();
						return ResultBuilder.failResult(jsonObject.getString("msg"));
					} else {
						DeviceEntity device = null;
						for (int m = 0; m < jsonArray.size(); m++) {
							JSONObject deviceJson = jsonArray.getJSONObject(m);
							boolean dFlag = true;

							String iotDeviceId = deviceJson.getString("deviceId");
							device = deviceRepository.queryDeviceInfoByIOTDeviceId(iotDeviceId);
							if (device == null) {
								dFlag = false;
								device = new DeviceEntity();
								device.setIotDeviceId(iotDeviceId);
								device.setDeviceCreateTime(DateUtils.stampToDate(deviceJson.getString("createTime")));
							}
							device.setDeviceTypeId(deviceTypeId);
							device.setId(UtilTool.getUuId());
							Date date = new Date();
							device.setCreatTime(DateUtils.formatDate(date));
							device.setUpdateTime(DateUtils.formatDate(date));
							device.setApplicationId(applicationId);
							device.setNodeId(deviceJson.getString("imei"));
					//		device.setNodeId(deviceJson.getString("deviceName"));
							device.setManufacturerName(deviceJson.getString("tenantId"));
							device.setDeviceType(String.valueOf(deviceTypeMap.get("deviceTypeName")));
							device.setHardware(deviceJson.getString("imei"));

							// 根据标记判断执行insert还是update
							if (dFlag == false) {
								int addCount = deviceRepository.addDeviceInfo(device);
								if (addCount < 0) {
									logger.error("[getDeviceFromWingsIot()->error:IOT设备ID为" + device.getIotDeviceId()
											+ "的设备插入数据库失败]");
								}
							} else {
								int updateCount = deviceRepository.updateDeviceInfo(device);
								if (updateCount < 0) {
									logger.error("[getDeviceFromWingsIot()->error:IOT设备ID为" + device.getIotDeviceId()
											+ "的设备修改数据失败]");
								}
							}
						}

					}
				}
			}
			logger.info("[getDeviceFromWingsIot()->设备数据拉取成功");
			return ResultBuilder.successResult(null, "设备数据拉取成功");

		} catch (Exception e) {
			e.printStackTrace();
			return ResultBuilder.exceptionResult(e);
		}

	}



	/**
	 * @see com.run.dataConversion.service.DeviceService#pushDeviceToLocman(com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public Result<String> pushDeviceToLocman(JSONObject paramJson) {
		// TODO Auto-generated method stub
		try {
			RpcResponse<String> containsParamKey = UtilTool.containsParamKey(logger, "pushDeviceToLocman", paramJson,
					"accessSecret", "applicationId", "DeviceTypeId", "locmanDeviceTypeId");
			if (null != containsParamKey) {
				logger.error("[pushDeviceToLocman()-->error]" + containsParamKey.getMessage());
				return ResultBuilder.failResult(containsParamKey.getMessage());
			}

			RpcResponse<String> checkObjectBusinessKey = UtilTool.checkObjectBusinessKey(logger, "pushDeviceToLocman",
					paramJson, "accessSecret", "applicationId", "DeviceTypeId");
			if (null != checkObjectBusinessKey) {
				logger.error("[pushDeviceToLocman()-->error]" + checkObjectBusinessKey.getMessage());
				return ResultBuilder.failResult(checkObjectBusinessKey.getMessage());
			}
			String applicationId = paramJson.getString("applicationId");
			String accessSecret = paramJson.getString("accessSecret");
			String DeviceTypeId = paramJson.getString("DeviceTypeId");
			String locmanDeviceTypeId = paramJson.getString("locmanDeviceTypeId");

			// 根据应用id和设备类型id查询设备
			List<JSONObject> queryDeviceByAppIdAndDeviceTypeId = deviceRepository
					.queryDeviceByAppIdAndDeviceTypeId(applicationId, DeviceTypeId);
			if (queryDeviceByAppIdAndDeviceTypeId == null || queryDeviceByAppIdAndDeviceTypeId.isEmpty()) {
				logger.error("[pushDeviceToLocman()->查询不到相应设备]");
				return ResultBuilder.failResult("查询不到相应application和deviceType对应设备");
			}

			// 根据applicationId获取推送地址
			ApplicationEntity applicationInfo = applicationInfoRepository.getApplicationInfoById(applicationId);
			// 根据applicationId查询不到状态为'enable'的应用，不进行推送
			if (null == applicationInfo) {
				logger.error("huaWeiReportedDataReceive()->根据设备applicationId查询不到相应可用application信息，不进行推送");
				return ResultBuilder.failResult("查询不到应用信息");
			}

			int totalNum = queryDeviceByAppIdAndDeviceTypeId.size();
			// 数据分批次发送
			logger.info("[pushDeviceToLocman()->查询到的设备总数为" + totalNum + "]");
			int loopCount = totalNum / 1000;
			for (int i = 0; i <= loopCount; i++) {
				JSONObject dataToPush = new JSONObject();
				dataToPush.put("accessSecret", accessSecret);
				dataToPush.put("locmanDeviceTypeId", locmanDeviceTypeId);
				dataToPush.put("applicationId", applicationId);
				dataToPush.put("dcDeviceTypeId", DeviceTypeId);
				List<JSONObject> subList = null;
				if (i == loopCount) {
					subList = queryDeviceByAppIdAndDeviceTypeId.subList(i * 1000,
							queryDeviceByAppIdAndDeviceTypeId.size());
				} else {
					subList = queryDeviceByAppIdAndDeviceTypeId.subList(i * 1000, (i + 1) * 1000);
				}
				dataToPush.put("deviceList", subList);

				Map<String, Object> headerMap = new HashMap<>();
				headerMap.put("Content-Type", "application/json");

				String url = applicationInfo.getLocmanUrl().trim() + UrlConstants.LCOMAN_DEVICEINFOSYN;
				JSONObject doPost = PostOrGet.doPost(url, dataToPush.toJSONString(), headerMap);
				if (doPost.getInteger("StatusCode") != 200) {
					return ResultBuilder.failResult(doPost.getString("responseBody"));
				}
			}
			logger.info("[pushDeviceToLocman()->已经成功推送]");
			return ResultBuilder.successResult(null, "success");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResultBuilder.exceptionResult(e);
		}

	}

}
