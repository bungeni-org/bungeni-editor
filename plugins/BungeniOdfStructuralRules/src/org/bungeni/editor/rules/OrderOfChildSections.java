/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.rules;

import org.bungeni.editor.rulesimpl.BaseStructuralRule;

/**
 *
 * @author Ashok Hariharan
 */
public class OrderOfChildSections extends BaseStructuralRule {
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(OrderOfChildSections.class.getName());

    @Override
    public boolean applyRule(String forThisSectionName) {
     ///   if (setup(forThiSectionName)) {
     ///       return checkAllowChildSections();
      ///  }
        return false;
    }

  //  private boolean setup(String thisSectionName) {

  //  }



  
}
