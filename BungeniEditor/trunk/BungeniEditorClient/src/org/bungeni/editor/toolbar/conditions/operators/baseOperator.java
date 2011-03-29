
package org.bungeni.editor.toolbar.conditions.operators;

import org.bungeni.editor.toolbar.conditions.IBungeniToolbarCondition;

/**
 *
 * @author Administrator
 */
public class baseOperator {
      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(baseOperator.class.getName());
 
    /** Creates a new instance of baseOperator */
    public baseOperator() {
    }
    
    public static IBungeniToolbarCondition getConditionObject(String className) {
        IBungeniToolbarCondition iCondition = null ;
        try {
        Class conditionClass;
        conditionClass = Class.forName(className);
        iCondition = (IBungeniToolbarCondition) conditionClass.newInstance();
        } catch (InstantiationException ex) {
           log.debug("getConditionObject :"+ ex.getMessage());
           } catch (IllegalAccessException ex) {
           log.debug("getConditionObject :"+ ex.getMessage());
           }  catch (ClassNotFoundException ex) {
           log.debug("getConditionObject :"+ ex.getMessage());
          } finally {
              return iCondition;
          }
    }
}
