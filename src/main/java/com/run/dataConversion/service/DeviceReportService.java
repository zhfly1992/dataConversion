/*
 * File name: DeviceReportService.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 guofeilong 2020年8月5日 ...
 * ... ...
 *
 ***************************************************/

package com.run.dataConversion.service;

import com.alibaba.fastjson.JSONObject;
import com.run.entity.common.Result;

/**
 * @Description:
 * @author: guofeilong
 * @version: 1.0, 2020年8月5日
 */

public interface DeviceReportService {
	
	Result<String> receiveReportInfo(String json);
	
}
