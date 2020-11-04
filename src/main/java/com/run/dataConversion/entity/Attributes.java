/*
* File name: Attributes.java								
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
* 1.0			guofeilong		2020年7月15日
* ...			...			...
*
***************************************************/

package com.run.dataConversion.entity;

import org.apache.commons.lang3.StringUtils;

/**
* @Description:	
* @author: guofeilong
* @version: 1.0, 2020年7月15日
*/

public class Attributes {
	
	/** 主键 */
	private String id;
	/** iot属性点名称 */
	private String iotName;
	/** iot属性点标志 */
	private String iotMark;
	/** 设备类型id */
	private String deviceTypeId;
	/** 创建时间 */
	private String creatTime;
	/** 修改时间 */
	private String updateTime;
	/** 排序 */
	private String sort;
	/** 数据类型 */
	private String dataType;
	/** 读写类型 */
	private String rwType;
	/** 数据值取值范围 */
	private String dataRange;
	/** 启用/停用 */
	private String manageState;
	/** locman属性点名称 */
	private String locmanName;
	/** locman属性点标志*/
	private String locmanMark;
	/**
	 * 
	 */
	public Attributes() {
		super();
		
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the iotName
	 */
	public String getIotName() {
		return iotName;
	}
	/**
	 * @param iotName the iotName to set
	 */
	public void setIotName(String iotName) {
		this.iotName = iotName;
	}
	/**
	 * @return the iotMark
	 */
	public String getIotMark() {
		return iotMark;
	}
	/**
	 * @param iotMark the iotMark to set
	 */
	public void setIotMark(String iotMark) {
		this.iotMark = iotMark;
	}
	/**
	 * @return the deviceTypeId
	 */
	public String getDeviceTypeId() {
		return deviceTypeId;
	}
	/**
	 * @param deviceTypeId the deviceTypeId to set
	 */
	public void setDeviceTypeId(String deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}
	/**
	 * @return the creatTime
	 */
	public String getCreatTime() {
		return creatTime;
	}
	/**
	 * @param creatTime the creatTime to set
	 */
	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}
	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return the sort
	 */
	public String getSort() {
		return sort;
	}
	/**
	 * @param sort the sort to set
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}
	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}
	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	/**
	 * @return the rwType
	 */
	public String getRwType() {
		return rwType;
	}
	/**
	 * @param rwType the rwType to set
	 */
	public void setRwType(String rwType) {
		this.rwType = rwType;
	}
	/**
	 * @return the dataRange
	 */
	public String getDataRange() {
		return dataRange;
	}
	/**
	 * @param dataRange the dataRange to set
	 */
	public void setDataRange(String dataRange) {
		this.dataRange = dataRange;
	}
	/**
	 * @return the manageState
	 */
	public String getManageState() {
		return manageState;
	}
	/**
	 * @param manageState the manageState to set
	 */
	public void setManageState(String manageState) {
		this.manageState = manageState;
	}
	/**
	 * @return the locmanName
	 */
	public String getLocmanName() {
		return locmanName;
	}
	/**
	 * @param locmanName the locmanName to set
	 */
	public void setLocmanName(String locmanName) {
		this.locmanName = locmanName;
	}
	/**
	 * @return the locmanMark
	 */
	public String getLocmanMark() {
		return locmanMark;
	}
	/**
	 * @param locmanMark the locmanMark to set
	 */
	public void setLocmanMark(String locmanMark) {
		this.locmanMark = locmanMark;
	}
	

}
