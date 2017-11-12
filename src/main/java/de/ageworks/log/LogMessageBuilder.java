package de.ageworks.log;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builds log strings following a key=value format.
 *
 */
public class LogMessageBuilder {

	// ------------------------------------------------------------------------
	// static context
	// ------------------------------------------------------------------------

	private static final String KEY_COMPONENT = "c";
	private static final String KEY_EXC = "exc";
	private static final String KEY_EXCMSG = "excmsg";
	private static final String KEY_MESSAGE = "msg";
	private static final String KEY_PROCESS = "p";

	private static final String QUOTE_CHAR = "\"";
	
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSSZ");
	private static final ZoneId UTC = ZoneId.of("UTC");

	private static final Logger LOG = LoggerFactory.getLogger(LogMessageBuilder.class);

	private static Class<?> getCallingClass() {
		return ClassContext.getClassCalling(LogMessageBuilder.class);
	}
	
	private static Logger getLogger() {
		Logger logger = LoggerFactory.getLogger(getCallingClass());
		return (logger == null)? LOG : logger;
	}

	public static LogMessageBuilder opsLogMessage() {
		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		String className = getCallingClass().getName();
		String component = className.substring(className.lastIndexOf('.') + 1);
		String process = ste.getMethodName();

		return new LogMessageBuilder()
				.withOpsLogComponent(component)
				.withOpsLogProcess(process);
	}

	// ------------------------------------------------------------------------
	// instances context
	// ------------------------------------------------------------------------

	private final Map<String, Object> objects = new LinkedHashMap<>();
	private Exception exc = null;
	
	private LogMessageBuilder() {
	}
	
	public LogMessageBuilder withOpsLogComponent(String component) {
		objects.put(KEY_COMPONENT, component);
		return this;
	}

	public LogMessageBuilder withOpsLogProcess(String process) {
		objects.put(KEY_PROCESS, process);
		return this;
	}

	public LogMessageBuilder add(String key, Object value) {
		objects.put(key, value == null ? "null" : value.toString());
		return this;
	}

	public LogMessageBuilder add(String key, Number number) {
		objects.put(key, number == null ? "null" : number);
		return this;
	}
	
	public LogMessageBuilder add(String key, Date date) {
		return add(key, date == null ? null : date.toInstant().atZone(UTC).toLocalDateTime());
	}

	public LogMessageBuilder add(String key, LocalDateTime dateTime) {
		objects.put(key, dateTime == null ? "null" : dateTime.atZone(UTC).format(DATE_TIME_FORMATTER));
		return this;
	}

	public LogMessageBuilder addAsObject(String key, Object value) {
		objects.put(key, value);
		return this;
	}

	public LogMessageBuilder setException(Exception e) {
		return setException(e, true);
	}

	public LogMessageBuilder setExceptionWithoutTraceLog(Exception e) {
		return setException(e, false);
	}

	private LogMessageBuilder setException(Exception e, boolean logStackTrace) {
		this.exc = logStackTrace ? e : null;
		objects.put(KEY_EXC, e.getClass().getName());
		objects.put(KEY_EXCMSG, e.getMessage());
		return this;
	}
	
	public LogMessageBuilder setMessage(final String message) {
		objects.put(KEY_MESSAGE, message);
		return this;
	}

	public LogMessageBuilder trace() {
		Logger logger = getLogger();
		if(logger.isTraceEnabled()) {
			logger.trace(toLineBrokenStringInCaseOfExc(), exc);
		}
		
		return this;
	}

	public LogMessageBuilder debug() {
		Logger logger = getLogger();
		if(logger.isDebugEnabled()) {
			logger.debug(toLineBrokenStringInCaseOfExc(), exc);
		}
		
		return this;
	}

	public LogMessageBuilder info() {
		Logger logger = getLogger();
		if(logger.isInfoEnabled()) {
			logger.info(toLineBrokenStringInCaseOfExc(), exc);
		}
		
		return this;
	}

	public LogMessageBuilder warn() {
		Logger logger = getLogger();
		if(logger.isWarnEnabled()) {
			logger.warn(toLineBrokenStringInCaseOfExc(), exc);
		}
		
		return this;
	}

	public LogMessageBuilder error() {
		Logger logger = getLogger();
		if(logger.isErrorEnabled()) {
			logger.error(toLineBrokenStringInCaseOfExc(), exc);
		}
		
		return this;
	}
	
	private String toLineBrokenStringInCaseOfExc() {
		return (exc == null)? toString() : toString() + "\n";
	}
	
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (Map.Entry<String, Object> entry : objects.entrySet()) {
			if (entry.getValue() != null) {
				if (!first) {
					builder.append(" ");
				} else {
					first = false;
				}
				
	            // Escape any " that appear in the key or value.
	            String key = entry.getKey().replaceAll("\"", "\\\\\"");
	            Object value = entry.getValue();

				builder.append(key).append('=');

				// add quotes for strings
				final boolean isString = (value instanceof String);
				if (isString) {
					builder.append(QUOTE_CHAR);
		            value = value.toString().replaceAll("\"", "\\\\\"");
				}

				builder.append(value);
				
				// add quotes for strings
				if (isString) {
					builder.append(QUOTE_CHAR);
				}
			}
		}

		return builder.toString();
	}

}
