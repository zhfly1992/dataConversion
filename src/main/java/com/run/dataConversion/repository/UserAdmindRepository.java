/*
* File name: UserAdmindRepository.java								
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

/**
* @Description:	
* @author: 钟滨远
* @version: 1.0, 2020年7月8日
*/

public interface UserAdmindRepository {
	
	Map<String,Object>  findUserByAcc(String account);
	
	List<Map<String,Object>> findApplicationIdByaccount(String account);
}
