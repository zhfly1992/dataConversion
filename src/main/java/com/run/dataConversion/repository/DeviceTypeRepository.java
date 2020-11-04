/*
 * File name: DeviceTypeRepository.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 guofeilong 2020年7月13日 ...
 * ... ...
 *
 ***************************************************/

package com.run.dataConversion.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * @Description:
 * @author: guofeilong
 * @version: 1.0, 2020年7月13日
 */

public interface DeviceTypeRepository {

	/**
	 * 
	 * @param string
	 * @Description:
	 * @param
	 * @return
	 */
	Integer createDeviceType(Map<String, Object> paramers);



	/**
	 * 
	 * @param searchKey 
	 * @Description:
	 * @param
	 * @return
	 */

	List<Map<String, Object>> getAllDeviceTypes(@Param("userId")String userId, @Param("searchKey")String searchKey);



	/**
	 * 
	 * @Description:
	 * @param
	 * @return
	 */
	Map<String, Object> getDeviceTypeById(@Param("id") String id, @Param("applicationId") String applicationId);



	/**
	  * 
	  * @Description:
	  * @param 
	  * @return
	  */
	
	Integer updateDeviceType(Map<String, Object> paramers);
}
