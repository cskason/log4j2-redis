/**
 * 
 */
package com.jk.log4j2;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import com.jk.log4j2.redis.RedisFactrory;
import com.jk.log4j2.redis.ThreadLocalHelper;
import com.jk.log4j2.redis.impl.RedisFactroryImpl;

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

	protected RedisAppender(String name, Filter filter,
			Layout<? extends Serializable> layout) {
		super(name, filter, layout);
		// TODO Auto-generated constructor stub
	}

	private static RedisFactrory redisFactrory;

	private static String key;
	
	
	
	public static String[] getTraceId() {
		Object id = ThreadLocalHelper.get("TRACE_ID");
		if (id != null && id instanceof String) {
		} else {
			id = StringHelper.uuid();
			ThreadLocalHelper.set("TRACE_ID", id);
		}
		return id.toString().split("\\/");
	}
	

	@Override
	public void append(LogEvent event) {
		
		
		{
			if (event == null)
				return;

			

				try {

					
					
					String renderedMessage = event.getMessage().getFormattedMessage();
					

					
					Map<String,String> map = new HashMap<String,String>();
					
					{/*extends logs by tylerchen*/
						String[] traceId = getTraceId();
						if (traceId.length > 0) {
							String uuid = traceId[0];
							map.put("uuid", uuid);
						}
						if (traceId.length > 1) {
							String sessionId = traceId[1];
							map.put("sessionId", sessionId);
						}
						if (traceId.length > 2) {
							String userName =  traceId[2];
							map.put("userName", userName);
						}
						/*split message like:[#name=value#]*/
						renderedMessage = renderedMessage == null ? "" : renderedMessage;
						int len = renderedMessage.length();
						int enterMark = -1;/* 0:enterName, 1: enterValue*/
						StringBuilder name = new StringBuilder(256);
						StringBuilder value = new StringBuilder(256);
						for (int index = 0; index < len; index++) {
							char c = renderedMessage.charAt(index);
							if (enterMark == -1 && c == '[' && index < len - 1
									&& renderedMessage.charAt(index + 1) == '#') {/*start with [#*/
								name.setLength(0);
								name.append('#');
								value.setLength(0);
								enterMark = 0;/*enter name*/
								index = index + 1;
								continue;
							} else if (enterMark == 0 && c == '=') {/*enter value*/
								enterMark = 1;
								continue;
							} else if (enterMark == 1 && c == '#' && index < len - 1
									&& renderedMessage.charAt(index + 1) == ']') {/*end with #]*/
								map.put(name.toString(), value.toString());
								name.setLength(0);
								value.setLength(0);
								enterMark = -1;
								index = index + 1;
								continue;
							}
							if (enterMark == 0) {
								name.append(c);
							} else if (enterMark == 1) {
								value.append(c);
							}
						}
					}

					//final byte[] bytes = getLayout().toByteArray(event);
					//redisFactrory.insertLog(key, new Date(event.getTimeMillis())+ " " + event.getLevel() + " "+ event.getMessage().getFormattedMessage());
					
					StringBuilder msg = new StringBuilder();
					msg.append(DateUtil.format(new Date(event.getTimeMillis()), DateUtil.YEAR_TO_MS)+"  uuid:"+map.get("uuid") +"  sessionId:"+map.get("sessionId")+" userName:"+map.get("userName"));
					redisFactrory.insertLog(key,msg.toString());
					
				} catch (Exception e) {
					
					
					
				}

		}
		
		
		
		

	
	}

	@PluginFactory
	public static RedisAppender createAppender(
			@PluginAttribute("name") String name,
			@PluginAttribute("key") String key,
			@PluginElement("Filter") Filter filter,
			@PluginElement("Layout") Layout<? extends Serializable> layout,
			@PluginElement("Properties") final Property[] properties) {

		Map<String, String> proMap = new HashMap<String, String>();

		for (Property property : properties) {
			proMap.put(property.getName(), property.getValue());
		}
		String redisNodes = proMap.get("redisNodes");
		int timeout = Integer.valueOf(proMap.get("timeout"));
		int maxRedirections = Integer.valueOf(proMap.get("maxRedirections"));
		String password = proMap.get("password");

		RedisAppender.key = key;

		redisFactrory = new RedisFactroryImpl(redisNodes, timeout,
				maxRedirections);

		return new RedisAppender(name, filter, layout);

	}

}