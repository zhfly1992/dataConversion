/*
* File name: UserAdminController.java								
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

package com.run.dataConversion.controller;



import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.run.dataConversion.constants.UrlConstants;
import com.run.dataConversion.service.UserAdmindService;
import com.run.entity.common.Result;
import com.run.entity.tool.ResultBuilder;


/**
* @Description:	
* @author: 钟滨远
* @version: 1.0, 2020年7月8日
*/
@RestController
@CrossOrigin(value="*")
@RequestMapping(UrlConstants.USER)
public class UserAdminController {
	
	@Autowired
	public UserAdmindService userAdmindService;
	
	@PostMapping(value=UrlConstants.LOGIN)
	public Result<Map<String,Object>> UserAdminLogin(@RequestBody JSONObject json){
		return userAdmindService.doUserAdminLogin(json);
		
	}
	
	@GetMapping(value=UrlConstants.LOGINOUT)
	public Result<String> userAdminLoginOut(){
		return ResultBuilder.successResult(null, "退出登录成功！");
	}
	
	
	
	@GetMapping
    @RequestMapping(UrlConstants.GET_PIC)
    public @ResponseBody Result<Map<String,Object>>  getPic() {
        return userAdmindService.getPic();
    }

	@PostMapping
	@RequestMapping(UrlConstants.CHECKCAPCODE)
    public Result<Map<String,Object>> checkcapcode(@RequestBody JSONObject json) {
        return userAdmindService.checkcapcode(json);
    }   
}
