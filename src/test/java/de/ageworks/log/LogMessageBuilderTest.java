package de.ageworks.log;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class LogMessageBuilderTest {

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
