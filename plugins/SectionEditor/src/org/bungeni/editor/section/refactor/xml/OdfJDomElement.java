/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.xml;

import org.jdom.Element;
import org.openoffice.odf.doc.element.text.OdfSection;

/**
 *
 * @author ashok
 */
public class OdfJDomElement extends Element {
    public final static String ELEMENT_NAME="section";
    public final static String NAME_ATTR="name";
    public final static String TYPE_ATTR="type";
    private final OdfSection textSection;

    OdfJDomElement(OdfSection aSection, String nameAttribute, String typeAttribute) {
        super(ELEMENT_NAME);
        textSection = aSection;
        this.setAttribute(NAME_ATTR, nameAttribute);
        this.setAttribute(TYPE_ATTR, typeAttribute);
    }

    public OdfSection getTextSection(){
        return textSection;
    }

}
