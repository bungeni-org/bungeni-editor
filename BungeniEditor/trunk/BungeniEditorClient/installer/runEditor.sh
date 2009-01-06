export JAVA_HOME=$JAVA_HOME
export BUNGENI_EDITOR_HOME=$INSTALL_PATH/base
export LOG4J_PROPS=file://$BUNGENI_EDITOR_HOME/settings/log4j.properties
$JAVA_HOME/bin/java -Dlog4j.ignoreTCL=true -Dlog4j.configuration=$LOG4J_PROPS -jar ./base/BungeniEditorClient.jar
