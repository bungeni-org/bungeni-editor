cd $APP_HOME && $JAVA_HOME/bin/java  -Xmx$APP_MEM -Dlog4j.ignoreTCL=true -Dlog4j.configuration=$LOG4J_PROPS -jar ./TrackChangesManager.jar $RUN_PARAMS
