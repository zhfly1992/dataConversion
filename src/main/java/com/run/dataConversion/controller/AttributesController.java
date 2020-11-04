/*
* File name: AttributesController.java								
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

package com.run.dataConversion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.run.dataConversion.constants.UrlConstants;
import com.run.dataConversion.entity.Attributes;
import com.run.dataConversion.service.AttributesService;
import com.run.entity.common.Result;

/**
* @Description:	
* @author: guofeilong
* @version: 1.0, 2020年7月15日
*/
@RestController
@CrossOrigin(value="*")
@RequestMapping(UrlConstants.DEVICETYPE_ATTRIBUTES)
public class AttributesController {

	@Autowired
	private AttributesService attributesService;
	
	@PostMapping(value=UrlConstants.ADD_ATTRIBUTES)
	public Result<String> addAttributes(@RequestBody Attributes attributes){
		return attributesService.addAttributes(attributes);
	}
	
	@PostMapping(value=UrlConstants.UPDATE_ATTRIBUTES)
	public Result<String> updateAttributes(@RequestBody Attributes attributes){
		return attributesService.updateAttributes(attributes);
	}
	
	@GetMapping(value=UrlConstants.GET_ATTRIBUTES_BY_DEVICETYPEID)
	public Result<List<Attributes>> getAttributesByDeviceType(@PathVariable("deviceTypeId") String deviceTypeId){
		return attributesService.getAttributesByDeviceType(deviceTypeId);
	}
	
	@GetMapping(value=UrlConstants.get_Attribute_By_Id)
	public Result<Attributes> getAttributeById(@PathVariable("id") String id){
		return attributesService.getAttributeById(id);
	}
}
