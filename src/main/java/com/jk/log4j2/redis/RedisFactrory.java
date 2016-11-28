/**
 * 
 */
package com.jk.log4j2.redis;

/**
 * @author kason
 *
 */
public interface RedisFactrory {
	
	
	public void insertLog(String key,String msg);
	
	public void insertLog(String key,byte[] data);
	
	
	
		

}
