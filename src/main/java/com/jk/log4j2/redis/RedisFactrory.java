/**
 * 
 */
package com.jk.log4j2.redis;

/**
 * @author kason
 *
 */
public interface RedisFactrory {
	
	/**
	 * 插入数据，字符
	 * @param key
	 * @param msg
	 */
	public void insertLog(String key,String msg);
	
	
	/**
	 * 插入数据，对象
	 * @param key
	 * @param data
	 */
	public void insertLog(String key,byte[] data);
	
	
	
		

}
