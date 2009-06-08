package org.bungeni.editor.toolbar.conditions.runnable;

/**
 * Custom exception thrown by condition evaluator
 * @author Ashok Hariharan
 */
public class ConditionFailureException extends Exception {
        public ConditionFailureException(String exceptionMsg) {
            super(exceptionMsg);
        }
}
