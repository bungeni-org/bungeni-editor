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
    private static String NAME_ATTR="name";
    private static String TYPE_ATTR="type";

    OdfJDomElement(String nameAttribute, String typeAttribute) {
        super(ELEMENT_NAME);
        this.setAttribute(NAME_ATTR, nameAttribute);
        this.setAttribute(TYPE_ATTR, typeAttribute);
    }
        

}
