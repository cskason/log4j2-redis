# log4j2-redis


###实现log4j2日志写入到redis中	

1、采用slf4j作为抽象入口
2、实现采用log4j2
3、redis支持集群


####代码：
    
    private static Logger logger = LoggerFactory.getLogger(RedisAppenderTest.class);
	logger.info("test");

    

#####例子参考
参考
com-jk-log4jredis/src/test/java/com/jk/log4j2Redis/RedisAppenderTest.java


######log4j2.xml 配置：

    <Appenders>
		<Console name="console" target="SYSTEM_OUT">
			<PatternLayout pattern="%m%n" />
		</Console>
		<redisAppender name="redisAppender"  key="key1">
		   <!-- redisNodes 支持集群，多个节点用竖杠| 分隔 -->
			<Property name="redisNodes">localhost:7000</Property> 
			<Property name="timeout">5000</Property>
			<Property name="maxRedirections">20</Property>
			<Property name="password"></Property>
			
			<PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %class{36} %L %M - %msg%xEx%n"/>
		</redisAppender>
	</Appenders>