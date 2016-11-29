/**
 * 
 */
package com.jk.log4j2.redis.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import com.jk.log4j2.redis.BinaryJedisCluster;
import com.jk.log4j2.redis.RedisFactrory;

import redis.clients.jedis.HostAndPort;

/**
 * @author kason
 *
 */
public class RedisFactroryImpl implements RedisFactrory{

	BinaryJedisCluster	jedisCluster ;
	
	
	public RedisFactroryImpl(String redisNodes,int timeout,int maxRedirections)
	{
		if(jedisCluster==null)
		{
			try {
				init(redisNodes,timeout,maxRedirections);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/*
	 * 解析集群的node
	 */
		public void init(String redisClusterNodes,int timeout,int maxRedirections) throws Exception {
			
			if(jedisCluster!=null){
				 return;
			}
			
			try {
				 Pattern p = Pattern.compile("^.+[:]\\d{1,5}\\s*$");
				Set<HostAndPort> haps = new HashSet<HostAndPort>();
				
				String[] hosts = redisClusterNodes.split("\\|");
				
				for (String host :hosts) {


					boolean isIpPort = p.matcher(host).matches();

					if (!isIpPort) {
						throw new IllegalArgumentException("ip 或 port 不合法");
					}
					String[] ipAndPort = host.split(":");

					HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
					haps.add(hap);
				}
				
				
				jedisCluster = new BinaryJedisCluster(haps, 5000, 10);

			} catch (IllegalArgumentException ex) {
				throw ex;
			} catch (Exception ex) {
				throw new Exception("解析 jedis 配置文件失败", ex);
			}
		}
	
	
	@Override
	public void insertLog(String key, byte[] data) {
		// TODO Auto-generated method stub
		if(jedisCluster==null)
		{
			try {
				//init("");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		jedisCluster.lpush(key, data);
		
	}

	@Override
	public void insertLog(String key, String msg) {
		// TODO Auto-generated method stub
		jedisCluster.lpush(key, msg);
	}

}
