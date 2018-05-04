package de.ageworks.log;

/**
 * @copyright
 *
 * Copyright 2017-2018 age works GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"): you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builds log strings following a key=value format and removes the need for specifying a logger.</br>
 * </br>
 * <b>Example</b>
 * <pre>
 * class Foo {
 *   
 *   ...
 *   
 *   void fooMethod() {
 *     
 *     ...
 *     
 *     LogMessageBuilder.opsLogMessage()
 *     .add("key", "value")
 *     .add("num", 2.0)
 *     .setMsg("message")
 *     .debug();
 *   }
 * }
 * </pre>
 * would be equally to
 * <pre>
 * org.slf4j.LoggerFactory.getLogger(Foo.class)
 * .debug("p=\"Foo\" c=\"fooMethod\" key=\"value\" num=2.0 msg=\"message\"");
 * </pre>
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

	/**
	 * Creates a new LogMessageBuilder having the fields</br>
	 * <ul>
	 *   <li>p=${classNameOfTheLoggingMethod}</li>
	 *   <li>c=${methodNameOfTheLoggingMethod}</li>
	 * </ul>
	 * already set.
	 * 
	 * @see #withOpsLogComponent(String)
	 * @see #withOpsLogProcess(String)
	 */
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
	
	/**
	 * Sets the log component which defaults to the method calling this LogMessageBuilder.</br>
	 * Will appear as {@code c="${component}"} at the log.
	 *  
	 * @param component
	 * 	the component to be logged
	 * 
	 * @return this
	 */
	public LogMessageBuilder withOpsLogComponent(String component) {
		objects.put(KEY_COMPONENT, component);
		return this;
	}

	/**
	 * Sets the log process which defaults to the class calling this LogMessageBuilder.</br>
	 * Will appear as {@code p="${process}"} at the log.
	 *  
	 * @param process
	 * 	the process to be logged
	 * 
	 * @return this
	 */
	public LogMessageBuilder withOpsLogProcess(String process) {
		objects.put(KEY_PROCESS, process);
		return this;
	}

	/**
	 * Adds a {@code ${key}="${value.toString()}"} part to be logged.</br>
	 *
	 * @param key
	 * 	the key to be logged
	 * 
	 * @param value
	 * 	the value to be logged</br>
	 *  If value is {@code null} then {@code ${key}="null"} will be logged.
	 * 
	 * @return this
	 */
	public LogMessageBuilder add(String key, Object value) {
		objects.put(key, value == null ? "null" : value.toString());
		return this;
	}

	/**
	 * Adds a {@code ${key}=${number}} part to be logged.</br>
	 *
	 * @param key
	 * 	the key to be logged
	 * 
	 * @param number
	 * 	the value to be logged</br>
	 *  If number is {@code null} then {@code ${key}="null"} will be logged.
	 * 
	 * @return this
	 */
	public LogMessageBuilder add(String key, Number number) {
		objects.put(key, number == null ? "null" : number);
		return this;
	}
	
	/**
	 * Forwards to {@link #add(String, LocalDateTime)}.
	 * 
	 * @return this
	 * 
	 * @see #add(String, LocalDateTime)
	 */
	public LogMessageBuilder add(String key, Date date) {
		return add(key, date == null ? null : date.toInstant().atZone(UTC).toLocalDateTime());
	}

	
	/**
	 * Adds a {@code ${key}="${dateTime}"} part to be logged.</br>
	 * </br>
	 * The {@code dateTime} will be formated to
	 * <pre>
	 * yyyy-MM-dd HH:mm:ss,SSSZ
	 * </pre>
	 *
	 * @param key
	 * 	the key to be logged
	 * 
	 * @param dateTime
	 * 	the dateTime to be logged</br>
	 *  If dateTime is {@code null} then {@code ${key}="null"} will be logged.
	 * 
	 * @return this
	 */
	public LogMessageBuilder add(String key, LocalDateTime dateTime) {
		objects.put(key, dateTime == null ? "null" : dateTime.atZone(UTC).format(DATE_TIME_FORMATTER));
		return this;
	}

	/**
	 * Adds a {@code ${key}=${value.toString()}} part to be logged. Note the absence of the surrounding ".</br>
	 *
	 * @param key
	 * 	the key to be logged
	 * 
	 * @param value
	 * 	the value to be logged</br>
	 *  If value is {@code null} then {@code ${key}="null"} will be logged.
	 * 
	 * @return this
	 */
	public LogMessageBuilder addAsObject(String key, Object value) {
		objects.put(key, value);
		return this;
	}

	/**
	 * Forwards to {@code setException(e, true)}
	 * 
	 * @return this
	 */
	public LogMessageBuilder setException(Exception e) {
		return setException(e, true);
	}

	/**
	 * Forwards to {@code setException(e, false)}
	 * 
	 * @return this
	 */
	public LogMessageBuilder setExceptionWithoutTraceLog(Exception e) {
		return setException(e, false);
	}

	/**
	 * Sets the exception to be logged. </br>
	 * An exception will be logged as
	 * <pre>
	 * exc="${e.getClass().getName()}" excmsg="${e.getMessage()}"
	 * </pre>
	 * You can control whether or not to append the stack trace by setting the flag {@code logStackTrace}. The stack
	 * trace will be appended as individual lines to the log
	 * 
	 * @param e
	 * 	the exception to be logged, must not be null
	 * 
	 * @param logStackTrace
	 * 	whether or not the stack trace should be logged.
	 * 
	 * @return this
	 */
	public LogMessageBuilder setException(Exception e, boolean logStackTrace) {
		this.exc = logStackTrace ? e : null;
		objects.put(KEY_EXC, e.getClass().getName());
		objects.put(KEY_EXCMSG, e.getMessage());
		return this;
	}
	
	/**
	 * Sets the message to be logged. This will add / set the message part like
	 * <pre>
	 * msg="${message}"
	 * </pre>
	 *  
	 * @param message
	 * 	the message to be logged
	 * 
	 * @return this
	 */
	public LogMessageBuilder setMessage(final String message) {
		objects.put(KEY_MESSAGE, message);
		return this;
	}

	/**
	 * Issues the logger for the class this method is called from to trace-log this log.
	 * 
	 * @return this
	 */
	public LogMessageBuilder trace() {
		Logger logger = getLogger();
		if(logger.isTraceEnabled()) {
			logger.trace(toLineBrokenStringInCaseOfExc(), exc);
		}
		
		return this;
	}

	/**
	 * Issues the logger for the class this method is called from to debug-log this log.
	 * 
	 * @return this
	 */
	public LogMessageBuilder debug() {
		Logger logger = getLogger();
		if(logger.isDebugEnabled()) {
			logger.debug(toLineBrokenStringInCaseOfExc(), exc);
		}
		
		return this;
	}

	/**
	 * Issues the logger for the class this method is called from to info-log this log.
	 * 
	 * @return this
	 */
	public LogMessageBuilder info() {
		Logger logger = getLogger();
		if(logger.isInfoEnabled()) {
			logger.info(toLineBrokenStringInCaseOfExc(), exc);
		}
		
		return this;
	}

	/**
	 * Issues the logger for the class this method is called from to warn-log this log.
	 * 
	 * @return this
	 */
	public LogMessageBuilder warn() {
		Logger logger = getLogger();
		if(logger.isWarnEnabled()) {
			logger.warn(toLineBrokenStringInCaseOfExc(), exc);
		}
		
		return this;
	}

	/**
	 * Issues the logger for the class this method is called from to error-log this log.
	 * 
	 * @return this
	 */
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
	
	/**
	 * @return the log message as currently being set up
	 */
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
