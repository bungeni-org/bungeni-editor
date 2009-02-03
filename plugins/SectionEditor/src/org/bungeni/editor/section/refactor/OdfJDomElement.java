/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor;

import org.jdom.Element;

/**
 *
 * @author ashok
 */
public class OdfJDomElement extends Element {
    private static String ELEMENT_NAME="section";

    OdfJDomElement(String strElement) {
        super(ELEMENT_NAME);
        this.setText(strElement);
    }
        

}
