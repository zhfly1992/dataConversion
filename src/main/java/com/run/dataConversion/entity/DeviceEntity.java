package com.run.dataConversion.entity;


/**
 * @Description 设备实体类
 * @author liaodan
 *
 */

public class DeviceEntity {

	// 设备ID
	private String id;
	// 华为平台设备ID
	private String iotDeviceId;
	// 硬件编码
	private String hardware;
	// （未知、预留）
	private String TDengineMark;
	// 所属应用ID
	private String applicationId;
	// 所属设备类型ID
	private String deviceTypeId;
	// 设备信息创建时间
	private String creatTime;
	// 设备信息修改时间
	private String updateTime;
	// 设备类型
	private String deviceType;
	// 设备识别码
	private String nodeId;
	// 厂商
	private String manufacturerName;
	// 设备创建时间（IOT）
	private String deviceCreateTime;
	//设备最新上报时间
	private String receiveTime;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIotDeviceId() {
		return iotDeviceId;
	}

	public void setIotDeviceId(String iotDeviceId) {
		this.iotDeviceId = iotDeviceId;
	}

	public String getHardware() {
		return hardware;
	}

	public void setHardware(String hardware) {
		this.hardware = hardware;
	}

	public String getTDengineMark() {
		return TDengineMark;
	}

	public void setTDengineMark(String tDengineMark) {
		TDengineMark = tDengineMark;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(String deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getDeviceCreateTime() {
		return deviceCreateTime;
	}

	public void setDeviceCreateTime(String deviceCreateTime) {
		this.deviceCreateTime = deviceCreateTime;
	}

	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

}
