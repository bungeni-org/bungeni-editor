export JAVA_HOME=$JAVA_HOME
export BUNGENI_EDITOR_HOME=$INSTALL_PATH/base
export LOG4J_PROPS=log4jdev.properties
export EDITOR_MEM=196m
$JAVA_HOME/bin/java -Dlog4j.ignoreTCL=true -Dlog4j.configuration=$LOG4J_PROPS  -Xmx$EDITOR_MEM  -jar ./base/BungeniEditorClient.jar
