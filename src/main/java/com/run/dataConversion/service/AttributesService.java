/*
* File name: AttributesService.java								
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

package com.run.dataConversion.service;

import java.util.List;

import com.run.dataConversion.entity.Attributes;
import com.run.entity.common.Result;

/**
* @Description:	
* @author: guofeilong
* @version: 1.0, 2020年7月15日
*/

public interface AttributesService {

	/**
	  * 
	  * @Description:
	  * @param 
	  * @return
	  */
	
	Result<String> addAttributes(Attributes attributes);

	/**
	  * 
	  * @param applicationId 
	 * @Description:
	  * @param 
	  * @return
	  */
	
	Result<List<Attributes>> getAttributesByDeviceType(String deviceTypeId);

	/**
	  * 
	  * @Description:
	  * @param 
	  * @return
	  */
	
	Result<String> updateAttributes(Attributes attributes);

	/**
	  * 
	  * @Description:
	  * @param 
	  * @return
	  */
	
	Result<Attributes> getAttributeById(String id);

}
