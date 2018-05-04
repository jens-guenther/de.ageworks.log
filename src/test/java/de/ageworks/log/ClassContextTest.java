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

class ClassContextTest {

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
