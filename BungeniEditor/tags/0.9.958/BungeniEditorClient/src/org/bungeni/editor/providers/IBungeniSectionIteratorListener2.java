/*
 * IBungeniSectionIteratorListener.java
 *
 * Created on May 29, 2008, 3:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.providers;

import com.sun.star.text.XTextSection;

/**
 * Used by DocumentSectionIterator2 - this iterator class provides a 
 * callback for iterating sections. Unlike the standard IBungeniSectionIterator
 * class it uses the section name as the callback parameter
 * @author Ashok Hariharan
 */
public interface IBungeniSectionIteratorListener2 {
    public boolean iteratorCallback(XTextSection xSection);
}
