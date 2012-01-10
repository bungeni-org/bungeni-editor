/*
 * IDialogProcessing.java
 *
 * Created on December 7, 2007, 5:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.selectors;

/**
 *
 * @author Administrator
 */
public interface IDialogProcessing {
    /**mandatory interfaces***/
    public boolean preFullInsert();
    public boolean processFullInsert();
    public boolean postFullInsert();
    
    public boolean preFullEdit();
    public boolean processFullEdit();
    public boolean postFullEdit();
    
    public boolean preSelectInsert();
    public boolean processSelectInsert();
    public boolean postSelectInsert() ;
}
