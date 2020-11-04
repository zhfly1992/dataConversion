/*
* File name: ApplicationServiceImpl.java								
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

package com.run.dataConversion.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.run.dataConversion.entity.ApplicationEntity;
import com.run.dataConversion.repository.ApplicationInfoRepository;
import com.run.dataConversion.service.ApplicationInfoService;
import com.run.dataConversion.util.UtilTool;
import com.run.entity.common.Result;
import com.run.entity.tool.DateUtils;
import com.run.entity.tool.ResultBuilder;

/**
* @Description:	
* @author: 钟滨远
* @version: 1.0, 2020年7月8日
*/
@Service
public class ApplicationInfoServiceImpl implements ApplicationInfoService{
	
	private static final Logger	logger	= LoggerFactory.getLogger(UserAdmindServiceImpl.class);
	
	@Autowired
	private	ApplicationInfoRepository		applicationInfoRepository;

	/**
	 * @see com.run.dataConversion.service.ApplicationInfoService#addApplicationInfo(com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public Result<String> addApplicationInfo(JSONObject json) {
		logger.info(String.format("[addApplicationInfo()->新增应用，参数：%s]", json.toJSONString()));
		try {
			String platformName=json.getString("platformName");
			if(StringUtils.isBlank(platformName)) {
				return ResultBuilder.failResult("应用名称不能为空！");
			}
			String platformUrl=json.getString("platformUrl");
			if(StringUtils.isBlank(platformUrl)) {
				return ResultBuilder.failResult("平台对外回调地址不能为空！");
			}
			String userId=json.getString("userId");
			if(StringUtils.isBlank(userId)) {
				return ResultBuilder.failResult("用户ID不能为空！");
			}
			String locmanUrl=json.getString("locmanUrl");
			if(StringUtils.isBlank(locmanUrl)) {
				return ResultBuilder.failResult("locman回调地址不能为空！");
			}
			String iotName=json.getString("iotName");
			if(StringUtils.isBlank(iotName)) {
				return ResultBuilder.failResult("iot平台名称不能为空！");
			}
			String iotAppId=json.getString("iotAppId");
			if(StringUtils.isBlank(iotAppId)) {
				return ResultBuilder.failResult("iotAppId不能为空！");
			}
			String iotAppIKey=json.getString("iotAppIKey");
			if(StringUtils.isBlank(iotAppIKey)) {
				return ResultBuilder.failResult("iotAppIKey不能为空！");
			}
		
			ApplicationEntity applicationEntity =new ApplicationEntity();
			applicationEntity.setId(UtilTool.getUuId());
			applicationEntity.setPlatformName(platformName);
			applicationEntity.setPlatformUrl(platformUrl);
			applicationEntity.setUserId(userId);
			applicationEntity.setLocmanUrl(locmanUrl);
			applicationEntity.setIotName(iotName);
//			applicationEntity.setIotCertificate(json.getString("iotCertificate")+"");
			applicationEntity.setIotAppId(iotAppId);
			applicationEntity.setIotAppIKey(iotAppIKey);
			applicationEntity.setCreatTime(DateUtils.formatDate(new Date()));
			applicationEntity.setCreatUser(userId);
			applicationEntity.setManageState("enable");
			applicationEntity.setIotUrl(json.getString("iotUrl"));
			
			
			Integer addApplicationInfo = applicationInfoRepository.addApplicationInfo(applicationEntity);
			
			if(addApplicationInfo <0) {
				logger.error("[addApplicationInfo()->新增应用失败]");
				return ResultBuilder.failResult("新增应用失败！");
			}
			logger.info("[addApplicationInfo()->新增应用成功！");
			return ResultBuilder.successResult(applicationEntity.getId(), "新增应用成功");
		}catch(Exception e) {
			return ResultBuilder.exceptionResult(e);
		}
	}

	/**
	 * @see com.run.dataConversion.service.ApplicationInfoService#getApplicationInfo(com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public Result<PageInfo<Map<String, Object>>> getApplicationInfo(JSONObject json) {
		logger.info(String.format("[getApplicationInfo()->进入查询方法，参数：%s]", json.toJSONString()));
		try {
			if (!json.containsKey("pageNum")) {
				logger.error("[getApplicationInfo()->error:参数无pageNum]");
				return ResultBuilder.emptyResult();
			}
			if (!json.containsKey("pageSize")) {
				logger.error("[getApplicationInfo()->error:参数无pageSize]");
				return ResultBuilder.emptyResult();
			}
			if (!json.containsKey("userId")) {
				logger.error("[getApplicationInfo()->error:参数无userId]");
				return ResultBuilder.emptyResult();
			}

			if (!StringUtils.isNumeric(json.getString("pageNum"))
					|| !StringUtils.isNumeric(json.getString("pageSize"))) {
				logger.error("[getApplicationInfo()->error:pageNum和pageSize必须为数字]");
				return ResultBuilder.invalidResult();
			}
			
			String searchKey=json.getString("searchKey");
			int pageNum=Integer.valueOf(json.getString("pageNum"));
			int pageSize=Integer.valueOf(json.getString("pageSize"));
			String userId=json.getString("userId");
			PageHelper.startPage(pageNum, pageSize);
			
			HashMap<String, Object> hashMap = new HashMap<String,Object>();
			hashMap.put("searchKey", searchKey);
			hashMap.put("pageNum", pageNum);
			hashMap.put("pageSize", pageSize);
			hashMap.put("userId", userId);
			
			List<Map<String, Object>> applicationInfo = applicationInfoRepository.getApplicationInfo(hashMap);
			if(null!=applicationInfo &&applicationInfo.size()>0) {
				PageInfo<Map<String, Object>> page = new PageInfo<>(applicationInfo);
				return ResultBuilder.successResult(page, "查询成功！");
			}
			logger.error("[getApplicationInfo()->error:应用列表查询失败！]");
			return ResultBuilder.failResult("查询失败！");
			
		}catch(Exception e) {
			return ResultBuilder.exceptionResult(e);
		}
	}

	/**
	 * @see com.run.dataConversion.service.ApplicationInfoService#updateApplicationInfo(com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public Result<String> updateApplicationInfo(JSONObject json) {
		logger.info(String.format("[updateApplicationInfo()->进入修改方法，参数：%s]", json.toJSONString()));
		try {
			
			String id=json.getString("id");
			if(StringUtils.isBlank(id)) {
				return ResultBuilder.failResult("应用ID不能为空！");
			}
			String platformName=json.getString("platformName");
			if(StringUtils.isBlank(platformName)) {
				return ResultBuilder.failResult("应用名称不能为空！");
			}
			String platformUrl=json.getString("platformUrl");
			if(StringUtils.isBlank(platformUrl)) {
				return ResultBuilder.failResult("平台对外回调地址不能为空！");
			}
			String userId=json.getString("userId");
			if(StringUtils.isBlank(userId)) {
				return ResultBuilder.failResult("用户ID不能为空！");
			}
			String locmanUrl=json.getString("locmanUrl");
			if(StringUtils.isBlank(locmanUrl)) {
				return ResultBuilder.failResult("locman回调地址不能为空！");
			}
			String iotName=json.getString("iotName");
			if(StringUtils.isBlank(iotName)) {
				return ResultBuilder.failResult("iot平台名称不能为空！");
			}
			String iotAppId=json.getString("iotAppId");
			if(StringUtils.isBlank(iotAppId)) {
				return ResultBuilder.failResult("iotAppId不能为空！");
			}
			String iotAppIKey=json.getString("iotAppIKey");
			if(StringUtils.isBlank(iotAppIKey)) {
				return ResultBuilder.failResult("iotAppIKey不能为空！");
			} 
			
			ApplicationEntity applicationEntity =new ApplicationEntity();
			applicationEntity.setId(id);
			applicationEntity.setPlatformName(platformName);
			applicationEntity.setPlatformUrl(platformUrl);
			applicationEntity.setLocmanUrl(locmanUrl);
			applicationEntity.setIotName(iotName);
			applicationEntity.setIotAppId(iotAppId);
			applicationEntity.setIotAppIKey(iotAppIKey);
			applicationEntity.setUpdateUser(userId);
			applicationEntity.setUpdateTime(DateUtils.formatDate(new Date()));
			applicationEntity.setIotUrl(json.getString("iotUrl"));;
			Integer updateApplicationInfo = applicationInfoRepository.updateApplicationInfo(applicationEntity);
			
			if(updateApplicationInfo <0) {
				logger.error("[updateApplicationInfo()->error:修改应用失败！]");
				return ResultBuilder.failResult("修改失败！");
			}
			return ResultBuilder.successResult(platformName, "修改成功！");
		}catch(Exception e) {
			return ResultBuilder.exceptionResult(e);
		}
	}

	/**
	 * @see com.run.dataConversion.service.ApplicationInfoService#disableApplicationInfo(com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public Result<String> disableApplicationInfo(JSONObject json) {
		logger.info(String.format("[updateApplicationInfo()->进入修改方法，参数：%s]", json.toJSONString()));
		try {
			
			String id=json.getString("id");
			if(StringUtils.isBlank(id)) {
				return ResultBuilder.failResult("应用ID不能为空！");
			}
			String manageState=json.getString("manageState");
			if(StringUtils.isBlank(manageState)) {
				return ResultBuilder.failResult("manageState不能为空！");
			} 
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id", id);
			map.put("manageState", manageState);
			Integer updateApplicationInfo = applicationInfoRepository.disableApplicationInfo(map);
			
			if(updateApplicationInfo <0) {
				logger.error("[updateApplicationInfo()->error:操作应用失败！]");
				return ResultBuilder.failResult("操作失败！");
			}
			return ResultBuilder.successResult(id, "操作成功！");
		}catch(Exception e) {
			return ResultBuilder.exceptionResult(e);
		}
	}

	/**
	 * @see com.run.dataConversion.service.ApplicationInfoService#getIotNameList()
	 */
	@Override
	public Result<List<Map<String, Object>>> getIotNameList() {
		logger.info("getIotNameList()->进入方法");
		try {
			
			List<Map<String, Object>> resultMap = applicationInfoRepository.getIotNameList();
			
			if(null == resultMap ) {
				logger.error("[getIotNameList()->error:查询失败,返回值为null！]");
				return ResultBuilder.failResult("查询失败！");
			} else {
				logger.info("getIotNameList()-->查询成功");
				return ResultBuilder.successResult(resultMap, "查询成功!");
			}
			
		}catch(Exception e) {
			logger.error("getIotNameList()-->错误", e);
			return ResultBuilder.exceptionResult(e);
		}
	}

}
