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

/**
 * Utility class to access information about the current class calling context.
 */
class ClassContext {

    private static ClassContextSecurityManager SECURITY_MANAGER;
    private static boolean SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = false;

    private static ClassContextSecurityManager getSecurityManager() {
        if (SECURITY_MANAGER != null)
            return SECURITY_MANAGER;
        else if (SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED)
            return null;
        else {
            SECURITY_MANAGER = safeCreateSecurityManager();
            SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = true;
            return SECURITY_MANAGER;
        }
    }

    private static ClassContextSecurityManager safeCreateSecurityManager() {
        try {
            return new ClassContextSecurityManager();
        } catch (java.lang.SecurityException sm) {
            return null;
        }
    }

	/**
	 * Returns the class which called the given calledClass within the current execution stack.
	 * 
	 * @param calledClass
	 * @return
	 */
	public static Class<?> getClassCalling(Class<?> calledClass) {
		Class<?> clazz = getViaSecurityManagerClassCalling(calledClass);
		if(clazz != null) {
			return clazz;
		}
		
		return getViaStackTraceClassCalling(calledClass);
	}
	
	protected static Class<?> getViaSecurityManagerClassCalling(Class<?> calledClass) {
        ClassContextSecurityManager securityManager = getSecurityManager();
        if (securityManager == null) {
            return null;
        }
        
        Class<?>[] trace = securityManager.getClassContext();
        String calledClassName = calledClass.getName();

        int i;
        int found = -1;
        for (i = 0; i < trace.length; i++) {
            if (calledClassName.equals(trace[i].getName())) {
            	found = i;
            	continue;
            }
            
            if (found != -1) {
            	break;
            }
        }

        if (found == -1 || found == trace.length - 1) {
            throw new IllegalStateException("Failed to find "+ calledClassName +" or its caller in the stack; this should not happen");
        }

        return trace[found+1];
	}
	
	protected static Class<?> getViaStackTraceClassCalling(Class<?> calledClass) {
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		
        String calledClassName = calledClass.getName();
        int i;
        int found = -1;
        for (i = 0; i < trace.length; i++) {
            if (calledClassName.equals(trace[i].getClassName())) {
            	found = i;
            	continue;
            }
            
            if (found != -1) {
            	break;
            }
        }

        if (found == -1 || found == trace.length - 1) {
            throw new IllegalStateException("Failed to find "+ calledClassName +" or its caller in the stack; this should not happen");
        }
        
        String callingClassName = trace[found+1].getClassName();
        
        try {
			return Thread.currentThread().getContextClassLoader().loadClass(callingClassName);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	// ------------------------------------------------------------------------
	// inner class - ClassContextSecurityManager
	// ------------------------------------------------------------------------

    private static final class ClassContextSecurityManager extends SecurityManager {
        protected Class<?>[] getClassContext() {
            return super.getClassContext();
        }
    }

}
