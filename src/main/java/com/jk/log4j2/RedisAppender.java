/**
 * 
 */
package com.jk.log4j2;



import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import com.jk.log4j2.redis.RedisFactrory;
import com.jk.log4j2.redis.RedisFactroryImpl;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kason
 *
 */
@Plugin(name = "redisAppender", category = "Core", elementType = "appender", printObject = true)
public class RedisAppender extends AbstractAppender {

	protected RedisAppender(String name, Filter filter,Layout<? extends Serializable> layout) {
		super(name, filter, layout);
		// TODO Auto-generated constructor stub
	}


	private static  RedisFactrory redisFactrory;
	
   private static String key;
    
   
    
	@Override
	public void append(LogEvent logEvent) {

        try {
            final byte[] bytes = getLayout().toByteArray(logEvent);//日志二进制文件，输出到指定位置就行
            
            redisFactrory.insertLog(key, new Date(logEvent.getTimeMillis())+" "+logEvent.getLevel()+" "+logEvent.getMessage().getFormattedMessage());

        } catch (Exception ex) {
            if (!ignoreExceptions()) {
                throw new AppenderLoggingException(ex);
            }
        } finally {

        }

    }
	
	
	@PluginFactory
	public  static RedisAppender createAppender(@PluginAttribute("name")  String name,
												@PluginAttribute("key")  String key,
			                                   @PluginElement("Filter")  Filter filter,
			                                   @PluginElement("Layout") Layout<? extends Serializable> layout,
			                                   @PluginElement("Properties") final Property[] properties
			                                   )
	{
		
		
		Map<String,String> proMap = new HashMap<String,String>();
		
		for (Property property : properties) {
			proMap.put(property.getName(), property.getValue());
		}
		String redisNodes = proMap.get("redisNodes");
		int timeout = Integer.valueOf(proMap.get("timeout"));
	    int maxRedirections = Integer.valueOf(proMap.get("maxRedirections"));
		String password = proMap.get("password");
		
		RedisAppender.key=key;
		
		redisFactrory = new RedisFactroryImpl(redisNodes,timeout,maxRedirections);
		
		
		return new RedisAppender(name,filter,layout);
		
		
	}
    
    

}