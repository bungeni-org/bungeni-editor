/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.xml;

import org.jdom.Element;

/**
 *
 * @author ashok
 */
public class OdfJDomElement extends Element {
    public final static String ELEMENT_NAME="section";
    public final static String NAME_ATTR="name";
    public final static String TYPE_ATTR="type";

    OdfJDomElement(String nameAttribute, String typeAttribute) {
        super(ELEMENT_NAME);
        this.setAttribute(NAME_ATTR, nameAttribute);
        this.setAttribute(TYPE_ATTR, typeAttribute);
    }

    

}
