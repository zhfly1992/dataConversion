package com.run.dataConversion.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.run.dataConversion.entity.DeviceEntity;

@Repository
public interface DeviceRepository {
	
	Integer addDeviceInfo(DeviceEntity deviceEntity);
	
	DeviceEntity queryDeviceInfoByIOTDeviceId(String iotDeviceId);
	
	Integer updateDeviceInfo(DeviceEntity deviceEntity);
	
	List<Map<String, Object>> queryDeviceInfo(Map<String,Object> paramMap);
	
	Integer queryDeviceInfoCount(Map<String,Object> paramMap);
	
	List<JSONObject> queryDeviceByAppIdAndDeviceTypeId(@Param("applicationId")String applicationId,@Param("deviceTypeId")String deviceTypeId);

}
