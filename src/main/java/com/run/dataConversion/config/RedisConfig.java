/*
* File name: RedisConfig.java								
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

package com.run.dataConversion.config;



import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisPoolConfig;

/**
* @Description:	
* @author: 钟滨远
* @version: 1.0, 2020年8月3日
*/
@Component
@Configuration("classpath:application.properties")
public class RedisConfig {
	
	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private String port;
	//连接超时时间（毫秒）
	@Value("${spring.redis.timeout}")
	private String timeout;
	@Value("${spring.redis.password}")
	private String password;
	
	/*连接池中的最大连接数*/
	@Value("${spring.redis.poolMaxTotal}")
	private String poolMaxTotal;
	
	/*连接池中的最大空闲连接*/
	@Value("${spring.redis.poolMaxIdle}")
	private String poolMaxIdle;
	
	/*连接池最大阻塞等待时间*/
	@Value("${spring.redis.poolMaxWait}")
	private String poolMaxWait;
	/** 尝试二次连接 */
	@Value("${spring.redis.testOnBorrow}")
	private String testOnBorrow;
	/** 空闲时检查有效性 */
	@Value("${spring.redis.testWhileIdle}")
	private String testWhileIdle;
	/**集群节点*/
	@Value("${spring.redis.clusternodes}")
	private String clusterNodes;
	
	/**
	 * 
	* @Description:配置jedis连接池
	* @return
	 */
	@Bean
	public JedisPoolConfig  jedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(Integer.valueOf(poolMaxTotal));
		jedisPoolConfig.setMaxIdle(Integer.valueOf(poolMaxIdle));
		jedisPoolConfig.setMaxWaitMillis(Long.valueOf(poolMaxWait));
		// 是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
		jedisPoolConfig.setTestOnBorrow(Boolean.valueOf(testOnBorrow));
		// 在空闲时检查有效性, 默认false
		jedisPoolConfig.setTestWhileIdle(Boolean.valueOf(testWhileIdle));
		
		return jedisPoolConfig;
	}
	
	@Bean
	public RedisClusterConfiguration redisClusterConfiguration() {
		RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
		String[] serverArray = clusterNodes.split(",");
		Set<RedisNode> nodes = new HashSet<RedisNode>();
		for (String ipPort : serverArray) {
			String[] ipAndPort = ipPort.split(":");
			nodes.add(new RedisNode(ipAndPort[0].trim(), Integer.valueOf(ipAndPort[1])));
		}
		redisClusterConfiguration.setClusterNodes(nodes);
		redisClusterConfiguration.setMaxRedirects(2);

		return redisClusterConfiguration;
	}
	
/*	@Bean
	public JedisConnectionFactory  jedisConnectionFactory(JedisPoolConfig jedisPoolConfig){
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
		//单点使用
		jedisConnectionFactory.setHostName(host);
		jedisConnectionFactory.setPassword(password);
		jedisConnectionFactory.setPort(Integer.valueOf(port));
		return jedisConnectionFactory;
	}*/
//	@Bean
//	public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig,
//			RedisClusterConfiguration redisClusterConfiguration) {
//		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration,
//				jedisPoolConfig);
//		return jedisConnectionFactory;
//	}
//	
	/**
	 * 
	* @Description:redisTemplate 模板
	* @param redisConnectionFactory
	* @return
	 */
	@Bean
	public RedisTemplate<String,String>  functionDomainRedisTemplate(RedisConnectionFactory redisConnectionFactory){
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		initDomainRedisTemplate(redisTemplate,redisConnectionFactory);
		return redisTemplate;
		
	}
	
	/**
	 * 
	* @Description:redis 存储格式
	* @param redisTemplate
	* @param factory
	 */
	private void initDomainRedisTemplate(RedisTemplate<String, String> redisTemplate, RedisConnectionFactory factory) {
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		// 开启事务
		redisTemplate.setEnableTransactionSupport(true);
		redisTemplate.setConnectionFactory(factory);
	}

}
