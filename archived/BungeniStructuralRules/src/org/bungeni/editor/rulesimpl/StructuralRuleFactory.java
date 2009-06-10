/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.rulesimpl;

/**
 *
 * @author undesa
 */
public class StructuralRuleFactory {
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(StructuralRuleFactory.class.getName());


    public static IStructuralRule getStructuralRule(String ruleClass){
            IStructuralRule irule = null;
            try {
                Class clsRule = Class.forName(ruleClass);
                irule = (IStructuralRule) clsRule.newInstance();
             } catch (InstantiationException ex) {
               log.debug("getStructuralRule :"+ ex.getMessage());
               } catch (IllegalAccessException ex) {
               log.debug("getStructuralRule :"+ ex.getMessage());
               }  catch (ClassNotFoundException ex) {
               log.debug("getStructuralRule :"+ ex.getMessage());
              } catch (NullPointerException ex) {
               log.debug("getStructuralRule :"+ ex.getMessage());
              } finally {
                  return irule;
              }
        }
}
