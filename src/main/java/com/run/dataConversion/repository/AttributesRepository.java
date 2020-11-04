/*
* File name: AttributesRepository.java								
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

package com.run.dataConversion.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.run.dataConversion.entity.Attributes;

/**
* @Description:	
* @author: guofeilong
* @version: 1.0, 2020年7月15日
*/

public interface AttributesRepository {
	
	/**
	  * 
	  * @Description:
	  * @param 
	  * @return
	  */
	
	Integer addAttributes(Attributes attributes);

	/**
	  * 
	  * @Description:
	  * @param 
	  * @return
	  */
	
	List<Attributes> getAttributesByDeviceType(@Param("deviceTypeId")String deviceTypeId, @Param("id")String id);

	/**
	  * 
	  * @Description:
	  * @param 
	  * @return
	  */
	
	Integer updateAttributes(Attributes attributes);

	/**
	  * 
	  * @Description:
	  * @param 
	  * @return
	  */
	
	Attributes getAttributeById(String id);

	/**
	  * 
	  * @Description:
	  * @param 
	  * @return
	  */
	
	List<Attributes> getAttributeByApplicationId(@Param("applicationId")String applicationId, @Param("hardware")String hardware);
}
