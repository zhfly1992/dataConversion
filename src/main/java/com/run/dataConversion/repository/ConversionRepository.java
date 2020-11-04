/*
* File name: ConversionRepository.java								
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

package com.run.dataConversion.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.run.dataConversion.entity.Conversion;


/**
* @Description:	
* @author: Administrator
* @version: 1.0, 2020年8月4日
*/
@Repository
public interface ConversionRepository {
	
	Integer addConversion(Conversion conversion);
	
	List<Conversion> getConversonByDeviceId(@Param("deviceId")String deviceId);
}
