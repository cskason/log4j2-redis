/**
 * 
 */
package com.jk.log4j2Redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kason
 *
 */
public class RedisAppenderTest {

	private static Logger logger = LoggerFactory.getLogger(RedisAppenderTest.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		for(int i=0;i<10;i++)
		{
			logger.info("test"+i);
		}
	}

}
