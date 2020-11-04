/*
* File name: UserRepository.java								
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

package com.run.dataConversion.repository;

import java.util.List;
import java.util.Map;

import com.run.dataConversion.entity.ApplicationEntity;

/**
* @Description:	
* @author: 钟滨远
* @version: 1.0, 2020年7月8日
*/

public interface ApplicationInfoRepository {
	
	Integer addApplicationInfo(ApplicationEntity applicationEntity);
	
	List<Map<String,Object>> getApplicationInfo(Map<String,Object> map);
	
	Integer updateApplicationInfo(ApplicationEntity applicationEntity);
	
	Integer disableApplicationInfo(Map<String,Object> map);
	
	ApplicationEntity getApplicationInfoById(String id);
	
	String getIotTypeByName(String id);

	/**
	  * 
	  * @Description:
	  * @param 
	  * @return
	  */
	
	List<Map<String, Object>> getIotNameList();
}
