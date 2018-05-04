# Splunk Logging Utilities for Java

## Utilities

### LogMessageBuilder

Small utility class to streamline logging in a Splunk compatible manner.

- removes the need for declaring a Logger
- fluent builder for ```key=value``` log elements

### HttpFlatEventCollectorLogbackAppender

LogbackAppender to log plain event messages to splunk via the Splunk HTTP event
collector.

```
<appender name="log" class="de.ageworks.splunk.HttpFlatEventCollectorLogbackAppender">
	<url>https://YOUR_HOST:8088</url>
	<token>YOUR_TOKEN</token>
	<source>YOUR_SOURCE_NAME</source>
	<layout class="ch.qos.logback.classic.PatternLayout">
		<pattern>%d{"yyyy-MM-dd HH:mm:ss,SSSZ"} thread="%thread" severity="%level" %msg</pattern>
	</layout>
</appender>

```

## Runtime Dependencies

- ch.qos.logback:logback-classic:1.2.3
- ch.qos.logback:logback-access:1.2.3
- com.splunk.logging:splunk-library-javalogging:1.5.2
- org.slf4j:slf4j-api:1.7.25


## Ideas

### Autowired ObjectFormatters

- clients can register ObjectFormatters which are used to add fields to the log message when logged
- also, handing over control of value formatting to clients
- registration either directly, via configuration, annotations, spring annotations (spring module?)
- another option would be an interface, however, this would clutter the logging stuff into the objects itself, hence I prefer the ObjectFormatter approach
- then, logging of objects would be simply

	logMessageBuilder.add(object);
	
- logging of exceptions could be "overloaded"


## Version History 0.1 

- new: de.ageworks.log.LogMessageBuilder
- new: de.ageworks.splunk.HttpFlatEventCollectorLogbackAppender


