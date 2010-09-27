

package org.bungeni.editor.selectors;

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

