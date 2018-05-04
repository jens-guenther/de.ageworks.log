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

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class LogMessageBuilderTest {

	@Test
	public void test_add_object_null() {
		Object o = null;
		
		assertEquals(
				LogMessageBuilder
				.opsLogMessage()
				.add("obj", o)
				.toString(), 
				"c=\"LogMessageBuilderTest\" p=\"test_add_object_null\" obj=\"null\"");
	}
	
	@Test
	public void test_error_withException() {
		assertEquals(
				LogMessageBuilder
				.opsLogMessage()
				.setExceptionWithoutTraceLog(new Exception("test"))
				.info()
				.toString(), 
				"c=\"LogMessageBuilderTest\" p=\"test_error_withException\" exc=\"java.lang.Exception\" excmsg=\"test\"");
		
	}
	
	@Test
	public void test_opsLogMessage_direct() {
		assertEquals(LogMessageBuilder.opsLogMessage().toString(), "c=\"LogMessageBuilderTest\" p=\"test_opsLogMessage_direct\"");
	}

	@Test
	public void test_opsLogMessage_innerClass() {
		assertEquals(new BaseLogger().baselog(), "c=\"LogMessageBuilderTest$BaseLogger\" p=\"baselog\"");
	}

	@Test
	public void test_opsLogMessage_innerClass_extends() {
		assertEquals(new Logger().baselog(), "c=\"LogMessageBuilderTest$BaseLogger\" p=\"baselog\"");
		assertEquals(new Logger().log(), "c=\"LogMessageBuilderTest$Logger\" p=\"log\"");
	}
	
	// ------------------------------------------------------------------------
	// inner class
	// ------------------------------------------------------------------------

	private static class BaseLogger {
		String baselog() {
			return LogMessageBuilder.opsLogMessage().toString(); 
		}
	}
	
	private static class Logger extends BaseLogger {
		String log() {
			return LogMessageBuilder.opsLogMessage().toString(); 
		}
		
	}
}
