# Logging Utilities for Java

## Utilities

### LogMessageBuilder

Small utility class to streamline logging in a consistent manner.

- removes the need for declaring a Logger
- fluent builder for ```key=value``` log elements

## Runtime Dependencies

- ch.qos.logback:logback-classic:1.2.3
- ch.qos.logback:logback-access:1.2.3
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
 
## Version History 0.2
- removed: de.ageworks.splunk.HttpFlatEventCollectorLogbackAppender
- updated to Java 14

## Version History 0.1 

- new: de.ageworks.log.LogMessageBuilder
- new: de.ageworks.splunk.HttpFlatEventCollectorLogbackAppender

## Copyright

Copyright 2021, Jens Guenther, github@age-works.de

Licensed under the Apache License, Version 2.0 (the "License"): you may
not use this file except in compliance with the License. You may obtain
a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations
under the License.
