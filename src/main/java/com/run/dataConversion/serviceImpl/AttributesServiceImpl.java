/*
 * File name: AttributesServiceImpl.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 guofeilong 2020年7月15日 ...
 * ... ...
 *
 ***************************************************/

package com.run.dataConversion.serviceImpl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.run.dataConversion.entity.Attributes;
import com.run.dataConversion.repository.AttributesRepository;
import com.run.dataConversion.service.AttributesService;
import com.run.dataConversion.util.UtilTool;
import com.run.entity.common.Result;
import com.run.entity.tool.ResultBuilder;

/**
 * @Description:
 * @author: guofeilong
 * @version: 1.0, 2020年7月15日
 */

@Service
public class AttributesServiceImpl implements AttributesService {

	private static final Logger		logger	= Logger.getLogger(AttributesServiceImpl.class);

	@Autowired
	private AttributesRepository	attributesRepository;



	/**
	 * @see com.run.dataConversion.service.AttributesService#addAttributes(com.run.dataConversion.entity.Attributes)
	 */
	@Override
	public Result<String> addAttributes(Attributes attributes) {

		try {
			Result<String> checkParams = checkParams(attributes);
			if (null != checkParams) {
				return checkParams;
			}
			attributes.setId(UtilTool.getUuId());
			attributes.setManageState("enable");
			Integer addAttributes = attributesRepository.addAttributes(attributes);
			if (addAttributes > 0) {
				logger.info("addAttributes()-->属性点添加成功");
				return ResultBuilder.successResult(attributes.getId(), "添加成功");
			} else {
				logger.error("addAttributes()-->属性点添加失败");
				return ResultBuilder.failResult("添加失败");
			} 
		} catch (Exception e) {
			logger.error("错误信息:" + e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * 
	 * @Description:
	 * @param
	 * @return
	 */

	private Result<String> checkParams(Attributes attributes) {
		if (StringUtils.isBlank(attributes.getIotName())) {
			logger.error("addAttributes()-->IotName不能为空");
			return ResultBuilder.emptyResult();
		}
		if (StringUtils.isBlank(attributes.getIotMark())) {
			logger.error("addAttributes()-->IotMark不能为空");
			return ResultBuilder.emptyResult();
		}
		if (StringUtils.isBlank(attributes.getDeviceTypeId())) {
			logger.error("addAttributes()-->DeviceTypeId不能为空");
			return ResultBuilder.emptyResult();
		}
		if (StringUtils.isBlank(attributes.getSort())) {
			logger.error("addAttributes()-->Sort不能为空");
			return ResultBuilder.emptyResult();
		}
		if (StringUtils.isBlank(attributes.getDataType())) {
			logger.error("addAttributes()-->DataType不能为空");
			return ResultBuilder.emptyResult();
		}
		List<Attributes> attributesList = attributesRepository.getAttributesByDeviceType(attributes.getDeviceTypeId(), attributes.getId());

		for (Attributes attributesInDb : attributesList) {
			if (attributes.getIotName().equals(attributesInDb.getIotName())) {
				return ResultBuilder.failResult(String.format("iot属性点名称:%s 已存在,不能重复", attributes.getIotName()));
			}
			if (attributes.getIotMark().equals(attributesInDb.getIotMark())) {
				return ResultBuilder.failResult(String.format("iot属性点标志:%s 已存在,不能重复", attributes.getIotMark()));
			}
			if (attributes.getLocmanName().equals(attributesInDb.getLocmanName())) {
				return ResultBuilder.failResult(String.format("locman属性点名称:%s 已存在,不能重复", attributes.getLocmanName()));
			}
			if (attributes.getLocmanMark().equals(attributesInDb.getLocmanMark())) {
				return ResultBuilder.failResult(String.format("locman属性点标志:%s 已存在,不能重复", attributes.getLocmanMark()));
			}
		}
		return null;
	}




	/**
	 * @see com.run.dataConversion.service.AttributesService#getAttributesByDeviceType(java.lang.String)
	 */
	@Override
	public Result<List<Attributes>> getAttributesByDeviceType(String deviceTypeId) {
		try {
			if (StringUtils.isBlank(deviceTypeId)) {
				logger.error("getAttributesByDeviceType()-->deviceTypeId不能为空");
				return ResultBuilder.emptyResult();
			}
			logger.info(String.format("getAttributesByDeviceType()-->deviceTypeId:%s", deviceTypeId));
			List<Attributes> attributesList = attributesRepository.getAttributesByDeviceType(deviceTypeId, null);
			if (null == attributesList) {
				logger.error("getAttributesByDeviceType()-->查询失败,返回值为null");
				return ResultBuilder.failResult("查询失败");
			} else {
				logger.info("getAttributesByDeviceType()-->查询成功");
				return ResultBuilder.successResult(attributesList, "查询成功");
			}
		} catch (Exception e) {
			logger.error("错误信息:" + e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @see com.run.dataConversion.service.AttributesService#updateAttributes(com.run.dataConversion.entity.Attributes)
	 */
	@Override
	public Result<String> updateAttributes(Attributes attributes) {
		try {
			Result<String> checkUpdateParams = checkUpdateParams(attributes);
			if (null != checkUpdateParams) {
				return checkUpdateParams;
			}
			Integer updateAttributes = attributesRepository.updateAttributes(attributes);
			if (updateAttributes > 0) {
				if (StringUtils.isNotBlank(attributes.getManageState())) {
					logger.info("updateAttributes()-->属性点删除(停用)成功");
					return ResultBuilder.successResult(attributes.getId(), "删除成功");
				}

				logger.info("updateAttributes()-->属性点修改成功");
				return ResultBuilder.successResult(attributes.getId(), "修改成功");
			} else {
				if (StringUtils.isNotBlank(attributes.getManageState())) {
					logger.info("updateAttributes()-->属性点删除(停用)失败");
					return ResultBuilder.failResult("删除失败");
				}
				logger.error("updateAttributes()-->属性点修改失败");
				return ResultBuilder.failResult("修改失败");
			}
		} catch (Exception e) {
			logger.error("错误信息:" + e);
			return ResultBuilder.exceptionResult(e);
		} 
	}



	/**
	  * 
	  * @Description:
	  * @param 
	  * @return
	  */
	
	private Result<String> checkUpdateParams(Attributes attributes) {
		if (StringUtils.isBlank(attributes.getId())) {
			logger.error("updateAttributes()-->id不能为空");
			return ResultBuilder.emptyResult();
		}
		
		String manageState = attributes.getManageState();
		if (StringUtils.isNotBlank(manageState) && "disable".equals(manageState)) {
			logger.info("updateAttributes()-->删除属性点,只校验id和管理状态参数");
			return null;
		} else {
			return checkParams(attributes);
		}
	}



	/**
	 * @see com.run.dataConversion.service.AttributesService#getAttributeById(java.lang.String)
	 */
	@Override
	public Result<Attributes> getAttributeById(String id) {
		try {
			if (StringUtils.isBlank(id)) {
				logger.error("updateAttributes()-->id不能为空");
				return ResultBuilder.emptyResult();
			}
			Attributes attributes = attributesRepository.getAttributeById(id);
			
			if (null != attributes) {
				logger.info("getAttributeById()-->查询属性点成功");
				return ResultBuilder.successResult(attributes, "查询成功");
			} else {
				logger.error("getAttributeById()-->查询属性点成功");
				return ResultBuilder.failResult("查询失败");
			}
		} catch (Exception e) {
			logger.error("错误信息:" + e);
			return ResultBuilder.exceptionResult(e);
		}
	}

}
