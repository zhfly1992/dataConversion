
/*
 * File name: UrlConstants.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 zhaoweizhi 2019年3月15日 ...
 * ... ...
 *
 ***************************************************/

package com.run.dataConversion.constants;



/**
 * @Description: url 路径管理
 * @author: zhaoweizhi
 * @version: 1.0, 2019年3月15日
 */

public class UrlConstants {
	/** 用户根路径 */
	public static final String	USER							= "/user";
	/** 登录 */
	public static final String	LOGIN							= "/login";
	/** 退出登录 */
	public static final String	LOGINOUT						= "/loginOut";
	/**获取登录验证图片*/
	public static final String  GET_PIC							="/getPic";
	/**图片滑动验证操作*/
	public static final String CHECKCAPCODE						="/checkcapcode";

	/** 应用根路径 */
	public static final String	APPLICATION						= "/application";
	/** 应用新增 */
	public static final String	ADDAPPLICATIONINFO				= "/addApplicationInfo";
	/** 应用列表分页 */
	public static final String	GETAPPLICATIONINFO				= "/getApplicationInfo";
	/** 应用修改 */
	public static final String	UPDATEAPPLICATIONINFO			= "/updateApplicationInfo";
	/** 停用启用 */
	public static final String	DISABLEAPPLICATIONINFO			= "/disableApplicationInfo";
	/** iot应用名称下拉框 */
	public static final String	IOT_NAME_LIST					= "/iotNameList";
	
	/** 设备类型根路径 */
	public static final String	DEVICETYPE						= "/deviceType";
	/** 新增 */
	public static final String	ADD_DEVICETYPE					= "/add";
	/** 修改 */
	public static final String	UPDATE_DEVICETYPE				= "/update";
	/** 同步 */
	public static final String	SYNCH_DEVICETYPE				= "/synch";
	/** 查询用户所有设备类型 */
	public static final String	ALL_DEVICETYPES					= "/all/{userId}";
	/** 用id查询设备类型 */
	public static final String	GET_DEVICETYPE_BY_ID			= "/{applicationId}/{id}";

	/** 设备信息根路径 */
	public static final String	DEVICE							= "/device";
	/** 拉取设备信息 */
	public static final String	PULLDEVICE						= "/pullDevice";
	/** 查询设备列表 */
	public static final String	GET_DEVICEINFO_LIST				= "/getDeviceInfoList";
	/** 设备推送接口*/
	public static final String  PUSH_DEVICE                     = "/pushDevice";

	/** 公共类根目录 */
	public static final String	COMMON							= "/common";

	/** 设备类型属性点根路径 */
	public static final String	DEVICETYPE_ATTRIBUTES			= "/deviceTypeAttributes";
	/** 添加设备类型属性点 */
	public static final String	ADD_ATTRIBUTES					= "/add";
	/** 添加设备类型属性点 */
	public static final String	UPDATE_ATTRIBUTES				= "/update";
	/** 查询设备类型属性点 */
	public static final String	GET_ATTRIBUTES_BY_DEVICETYPEID	= "/all/{deviceTypeId}";
	/** 添加设备类型属性点 */
	public static final String	get_Attribute_By_Id				= "/one/{id}";

	/** 接受上报数据的根路径 */
	public static final String	DATAREPORTED					= "/dataReported";
	/** 华为iot上报数据接收地址*/
	public static final String  HUAWEIIOT                       = "/huaWeiIot";
	/** 查询上报后的转换信息*/
	public static final String  GETCONVERSIONINFO               = "/getConversionsInfo";


	/** 设备数据上报根目录 */
	public static final String	DEVICE_REPORT					= "/deviceReport";
	/** 设备数据上报接收 */
	public static final String	RECEIVE							= "/receive";
	
	
	/** locman拼接地址分类*/
	/** 上报数据推送地址*/
	public static final String 	LOCMAN_REPORTEDDATA                  = "/dataConversion/deviceReported";
	/** 设备推送地址*/
	public static final String  LCOMAN_DEVICEINFOSYN                = "/dataConversion/deviceInfoSyn";
}
