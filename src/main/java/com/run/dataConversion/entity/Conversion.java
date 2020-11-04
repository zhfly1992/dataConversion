/*
* File name: Conversion.java								
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
* 1.0			Administrator		2020年8月4日
* ...			...			...
*
***************************************************/

package com.run.dataConversion.entity;

/**
* @Description:	数据转换实体类
* @author: zh
* @version: 1.0, 2020年8月4日
*/

public class Conversion {
	//主键id
	private String id;
	//设备id
	private String deviceId;
	//硬件id
	private String hardwareId;
	//接受到的上报数据，原格式
	private String receiveData;
	//接收到的上报数据时间
	private String receiveTime;
	//转换后的数据
	private String conversionData;
	//转发到locman的时间
	private String sendToLocmanTime;
	//locman返回数据
	private String responseData;
	//locman返回状态码
	private String responseStatusCode;
	//应用id
	private String applicationId;

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
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the hardwareId
	 */
	public String getHardwareId() {
		return hardwareId;
	}

	/**
	 * @param hardwareId the hardwareId to set
	 */
	public void setHardwareId(String hardwareId) {
		this.hardwareId = hardwareId;
	}

	/**
	 * @return the receiveData
	 */
	public String getReceiveData() {
		return receiveData;
	}

	/**
	 * @param receiveData the receiveData to set
	 */
	public void setReceiveData(String receiveData) {
		this.receiveData = receiveData;
	}

	/**
	 * @return the receiveTime
	 */
	public String getReceiveTime() {
		return receiveTime;
	}

	/**
	 * @param receiveTime the receiveTime to set
	 */
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	/**
	 * @return the conversionData
	 */
	public String getConversionData() {
		return conversionData;
	}

	/**
	 * @param conversionData the conversionData to set
	 */
	public void setConversionData(String conversionData) {
		this.conversionData = conversionData;
	}

	/**
	 * @return the sendToLocmanTime
	 */
	public String getSendToLocmanTime() {
		return sendToLocmanTime;
	}

	/**
	 * @param sendToLocmanTime the sendToLocmanTime to set
	 */
	public void setSendToLocmanTime(String sendToLocmanTime) {
		this.sendToLocmanTime = sendToLocmanTime;
	}

	/**
	 * @return the responseData
	 */
	public String getResponseData() {
		return responseData;
	}

	/**
	 * @param responseData the responseData to set
	 */
	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	/**
	 * @return the responseStatusCode
	 */
	public String getResponseStatusCode() {
		return responseStatusCode;
	}

	/**
	 * @param responseStatusCode the responseStatusCode to set
	 */
	public void setResponseStatusCode(String responseStatusCode) {
		this.responseStatusCode = responseStatusCode;
	}

	/**
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * @param applicationId the applicationId to set
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

}
