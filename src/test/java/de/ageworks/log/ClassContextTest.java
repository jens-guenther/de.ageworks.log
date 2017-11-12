package de.ageworks.log;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class ClassContextTest {

	@Test
	public void test_getViaSecurityManagerClassCalling_direct() {
		assertEquals(CalledClass.callDirectViaSecurityManager(), ClassContextTest.class);
	}

	@Test
	public void test_getViaSecurityManagerClassCalling_indirect() {
		assertEquals(CalledClass.callIndirectViaSecurityManager(), ClassContextTest.class);
	}

	@Test
	public void test_getViaStackTraceClassCalling_direct() {
		assertEquals(CalledClass.callDirectViaStackTrace(), ClassContextTest.class);
	}

	@Test
	public void test_getViaStackTraceClassCalling_indirect() {
		assertEquals(CalledClass.callIndirectViaStackTrace(), ClassContextTest.class);
	}

	// ------------------------------------------------------------------------
	// inner class - CallingClass
	// ------------------------------------------------------------------------

	private static class CalledClass {
		
		static Class<?> callDirectViaSecurityManager() {
			return ClassContext.getViaSecurityManagerClassCalling(CalledClass.class);
		}
		
		static Class<?> callDirectViaStackTrace() {
			return ClassContext.getViaStackTraceClassCalling(CalledClass.class);
		}
		
		static Class<?> callIndirectViaSecurityManager() {
			return callDirectViaSecurityManager();
		}
		
		static Class<?> callIndirectViaStackTrace() {
			return callDirectViaStackTrace();
		}
	}
}
