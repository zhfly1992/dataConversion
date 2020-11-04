/*
* File name: UserAdmindService.java								
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

package com.run.dataConversion.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.run.entity.common.Result;

/**
* @Description:	
* @author: 钟滨远
* @version: 1.0, 2020年7月8日
*/

public interface UserAdmindService {
	
	Result<Map<String, Object>> doUserAdminLogin(JSONObject json);
	
	
	/**
	 * 获取登录验证图片
	 * @return
	 */
	Result<Map<String, Object>> getPic();
	
	
	/**
	 * 图片验证
	 * @return
	 */
	Result<Map<String, Object>> checkcapcode(JSONObject json);
}
