# README.md

## Ideas

### Autowired ObjectFormatters

- clients can register ObjectFormatters which are used to add fields to the log message when logged
- also, handing over control of value formatting to clients
- registration either directly, via configuration, annotations, spring annotations (spring module?)
- another option would be an interface, however, this would clutter the logging stuff into the objects itself, hence I prefer the ObjectFormatter approach
- then, logging of objects would be simply

	logMessageBuilder.add(object);
	
- logging of exceptions could be "overloaded"
