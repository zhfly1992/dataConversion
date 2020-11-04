package com.run.dataConversion.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.alibaba.fastjson.JSONObject;
import com.run.dataConversion.constants.UrlConstants;
import com.run.dataConversion.service.DeviceService;
import com.run.entity.common.Result;


@RestController
@CrossOrigin("*")
@RequestMapping(UrlConstants.DEVICE)
public class DeviceController {

	@Autowired
	public DeviceService deviceService;
	
//	private static ExecutorService		executorService	= Executors.newFixedThreadPool(3);

	
	@PostMapping(UrlConstants.PULLDEVICE)
	public  Result<String> getDeviceInfoFromIOT(@RequestBody JSONObject paramJson) {
		return deviceService.getDeviceInfoFromIOT(paramJson);
	}
	
	
	
	@PostMapping(UrlConstants.GET_DEVICEINFO_LIST)
	public Result<?> getDeviceInfoList(@RequestBody JSONObject paramJson){
		return deviceService.getDeviceInfoList(paramJson);
		
	}
	
	@PostMapping(value = UrlConstants.PUSH_DEVICE)
	public Result<String> pushDeviceToLocman(@RequestBody JSONObject paramJson){
		return deviceService.pushDeviceToLocman(paramJson);
	}
	
//	@PostMapping(value = "/reveive")
//	public Result<String> reveive(@RequestBody JSONObject paramJson){
//
//		executorService.execute(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				try {
//					Thread.sleep(10000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				JSONArray jsonArray = paramJson.getJSONArray("deviceList");
//
//				Iterator<Object> iterator = jsonArray.iterator();
//				while(iterator.hasNext()){
//					JSONObject jsonObject = (JSONObject)iterator.next();
//					System.out.println(jsonObject.toJSONString());
//				}
//				System.out.println("接受到数据" + Thread.currentThread().getId());
//				System.out.println(jsonArray.size());
//			}
//		});
//		
//		return ResultBuilder.successResult(null, "success");
//	}

}
