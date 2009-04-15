set JAVA_HOME="$JAVA_HOME"
set LOG4J_PROPS=log4jdev.properties
set EDITOR_MEM=196m
%JAVA_HOME%\bin\java -Dlog4j.ignoreTCL -Dlog4j.configuration=%LOG4J_PROPS%  -Xmx%EDITOR_MEM% -jar .\base\BungeniEditorClient.jar
