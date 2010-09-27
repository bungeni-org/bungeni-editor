/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.providers;

import org.bungeni.utils.BungeniBNode;

/**
 *
 * @author undesa
 */
public class DefaultSectionIteratorListener implements IBungeniSectionIteratorListener {

    public boolean iteratorCallback(BungeniBNode bNode) {
       return true;
    }

}
