set JAVA_HOME="$JAVA_HOME"set LOG4J_PROPS=log4jdev.properties
%JAVA_HOME%\bin\java -Dlog4j.ignoreTCL -Dlog4j.configuration=%LOG4J_PROPS% -jar .\base\BungeniEditorClient.jar
