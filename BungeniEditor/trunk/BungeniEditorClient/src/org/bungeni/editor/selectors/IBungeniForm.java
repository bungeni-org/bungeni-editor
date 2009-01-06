/*
 * IBungeniForm.java
 *
 * Created on December 20, 2007, 3:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.selectors;

import java.util.HashMap;
import org.bungeni.editor.fragments.FragmentsFactory;

/**
 *
 * @author Administrator
 */
public interface IBungeniForm {
  public String getClassName();
 
  public boolean preFullEdit();
  public boolean postFullEdit();
  public boolean processFullEdit();

  
  public boolean preFullInsert();
  public boolean postFullInsert();
  public boolean processFullInsert();
  
  public boolean preSelectInsert();
  public boolean postSelectInsert();
  public boolean processSelectInsert();
  
  /*  
  public HashMap<String,String> getPreInsertMap();
  public HashMap<String,Object> getControlDataMap();
  */
}

