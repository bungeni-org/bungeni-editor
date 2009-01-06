set JAVA_HOME=$JAVA_HOME
REM set LOG4J_PROPS="file:///e:/projects/workingprojects/bungenieditor/trunk/BungeniEditorClient/dist/settings/log4j.properties"
set LOG4J_PROPS="file:///$INSTALL_PATH/base/settings/log4j.properties"
%JAVA_HOME%\bin\java -Dlog4j.ignoreTCL -Dlog4j.configuration=%LOG4J_PROPS% -jar .\base\BungeniEditorClient.jar
