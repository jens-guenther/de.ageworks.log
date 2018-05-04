package de.ageworks.splunk;

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

import java.io.Serializable;
import java.util.Map;

import com.splunk.logging.HttpEventCollectorEventInfo;

/**
 * Container for Splunk http event collector event data.
 */
public class HttpFlatEventCollectorEventInfo extends HttpEventCollectorEventInfo {
    private double eventTime; // time in fractional seconds since "unix epoch" format

    /**
     * Create a new HttpEventCollectorEventInfo container
     * @param severity of event
     * @param message is an event content
     */
    public HttpFlatEventCollectorEventInfo(
    		final double eventTime,
            final String severity,
            final String message,
            final String logger_name,
            final String thread_name,
            final Map<String, String> properties,
            final String exception_message,
            final Serializable marker
    ) {
        super(severity,message,logger_name,thread_name,properties,exception_message,marker);
        this.eventTime = eventTime;
    }

    /**
     * @return event timestamp in epoch format
     */
    @Override
    public double getTime() {
        return eventTime;
    }
}