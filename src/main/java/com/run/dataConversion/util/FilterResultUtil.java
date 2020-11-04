/*
* File name: FilterResultUtil.java								
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
* 1.0			钟滨远		2020年8月5日
* ...			...			...
*
***************************************************/

package com.run.dataConversion.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.run.entity.common.Result;
import com.run.entity.common.ResultStatus;
import com.run.entity.tool.DateUtils;

/**
* @Description:	
* @author: 钟滨远
* @version: 1.0, 2020年8月5日
*/

public class FilterResultUtil {
	
	public static void getResultErrorResponse(HttpServletResponse response,String errorCode, String failMess) {
		ResultStatus status = new ResultStatus();
		status.setResultCode(errorCode);
		status.setResultMessage(failMess);
		status.setTimeStamp(DateUtils.stampToDate(System.currentTimeMillis() + ""));
		Result<String> result = new Result<String>();
		result.setResultStatus(status);
		String json = JSON.toJSONString(result);
		
		PrintWriter writer = null;
		
		response.setStatus(Integer.parseInt(errorCode));
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		try {
            writer = response.getWriter();
            writer.print(json);
 
        } catch (IOException e) {
        } finally {
            if (writer != null)
                writer.close();
        }
//		ctx.setSendZuulResponse(false);
//		ctx.setResponseStatusCode(Integer.parseInt(errorCode));
//		ctx.setResponseBody(json);
//		ctx.getResponse().setHeader("Access-Control-Allow-Origin", "*");
//		ctx.getResponse().setCharacterEncoding("utf-8");
//		ctx.getResponse().setContentType("application/json");
//		ctx.set("isSuccess", false);
	}

}
