/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.rulesimpl;

import java.io.File;

/**
 *
 * @author undesa
 */
public class StructuralRulesConfig {
        public static String APPLN_PATH_PREFIX = "";
        public static String DOCSTRUCTURE_RULES_PATH_PREFIX   = "doc_rules"+ File.separator;
        public static String RULE_ENGINES_PATH_PREFIX = "engine_rules" + File.separator;

        public static String getDocRulesPath(){
            return APPLN_PATH_PREFIX + DOCSTRUCTURE_RULES_PATH_PREFIX ;
        }

        public static String getRuleEnginesPath(){
            return APPLN_PATH_PREFIX + RULE_ENGINES_PATH_PREFIX ;
        }

}
