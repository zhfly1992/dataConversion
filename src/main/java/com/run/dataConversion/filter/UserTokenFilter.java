/*
* File name: UserTokenFilter.java								
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
* 1.0			钟滨远		2020年8月3日
* ...			...			...
*
***************************************************/

package com.run.dataConversion.filter;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.run.dataConversion.util.FilterResultUtil;

/**
* @Description:	
* @author: 钟滨远
* @version: 1.0, 2020年8月3日
*/
@Component
public class UserTokenFilter extends HandlerInterceptorAdapter{
	private static final Logger		logger	= Logger.getLogger("UserTokenFilter");
	
	@Autowired
	@Qualifier("functionDomainRedisTemplate")
	private RedisTemplate<String, String>  redisTemplate;
	
	private Long timeout=7200L;

	/**
	 * @return 
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		try {
			logger.info("进入拦截器");
	        String method = request.getMethod().toUpperCase();

	        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
	        // 自适应所有自定义头
	        response.setHeader("Access-Control-Allow-Headers",  "Content-Type,Token");
	        String headers = request.getHeader("Access-Control-Request-Headers");
			 if(StringUtils.isNotBlank(headers)) {
	             response.setHeader("Access-Control-Allow-Headers", headers);
	             response.setHeader("Access-Control-Expose-Headers", headers);
	         }
			 // 允许跨域的请求方法类型
		        response.setHeader("Access-Control-Allow-Methods", "*");
				 // 预检命令（OPTIONS）缓存时间，单位：秒
//		        response.setHeader("Access-Control-Max-Age", "600");
		        // 明确许可客户端发送Cookie，不允许删除字段即可
				 response.setHeader("Access-Control-Allow-Credentials", "true");
//	        response.setHeader("Access-Control-Allow-Methods", "*");
//	        response.setHeader("Access-Control-Allow-Credentials", "true");
//        	response.setHeader("Access-Control-Allow-Headers",  " Content-Type,Token");
//        	response.setHeader("Access-Control-Max-Age", "3600");
//        	response.setHeader("Access-Control-Expose-Headers", "*");
	        if("OPTIONS".equals(method)) {

//	        	response.setHeader("Access-Control-Allow-Credentials", "true");
//	        	response.setHeader("Access-Control-Allow-Headers",  " Content-Type,Token");
//	        	response.setHeader("Access-Control-Max-Age", "3600");
//	        	response.setHeader("Access-Control-Expose-Headers", "*");
	        
	        	response.setStatus(200);
	        	return false;
	        }
			String url=request.getServerName();
			if("localhost".equals(url)) {
				return true;
			}
			String token = request.getHeader("Token");
			 logger.error("method:"+method+"token:"+token);
			if(!StringUtils.isEmpty(token)) {
				
				//token校验 ,不存在则过期
				String userId= redisTemplate.opsForValue().get(token);
				if(!StringUtils.isEmpty(userId)) {
					logger.info("刷新token时间，继续执行拦截器");
					redisTemplate.expire(token, timeout, TimeUnit.SECONDS);
					return true;
				}else {
					logger.error("token已经失效!");
					FilterResultUtil.getResultErrorResponse(response, "301", "token已经失效！");
					return false;
				}
			}else {
				logger.error("token为空!");
				FilterResultUtil.getResultErrorResponse(response, "301", "token不能为空！");
				return false;
			}
		}catch(Exception e) {
			logger.error("token异常!");
			FilterResultUtil.getResultErrorResponse(response, "301", "token异常！");
			return false;
		}
	}

	/**
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
