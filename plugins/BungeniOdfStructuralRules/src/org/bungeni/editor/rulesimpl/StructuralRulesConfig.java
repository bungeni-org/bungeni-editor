package org.bungeni.editor.rulesimpl;

import java.io.File;

/**
 *
 * @author Ashok Hariharan
 */
public class StructuralRulesConfig {
        //usually settings/structural_rules
        public static String APPLN_PATH_PREFIX = "";
        public static String DOCSTRUCTURE_RULES_PATH_PREFIX   = "doc_rules"+ File.separator;
        public static String RULE_ENGINES_PATH_PREFIX = "engine_rules" + File.separator;
        public static String LOG_PATH_PREFIX = "log" + File.separator;

        public static String getDocRulesPath(){
            return APPLN_PATH_PREFIX + File.separator + DOCSTRUCTURE_RULES_PATH_PREFIX ;
        }

        public static String getRuleEnginesPath(){
            return APPLN_PATH_PREFIX + File.separator + RULE_ENGINES_PATH_PREFIX ;
        }

        public static String getLogPath(){
            return APPLN_PATH_PREFIX + File.separator + LOG_PATH_PREFIX;
        }

}
