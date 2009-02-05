/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.xml;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;

/**
 * Filters a JDOM stream for elements having a specific attribute
 * @author Ashok Hariharan
 */
 public class ElementAttributeFilter extends ElementFilter {

     /**
      * override the default constructor to be able to instantiate the ElementAttributeFilter object
      * @param elemFilter - the element name we want to filter for
      */
        public ElementAttributeFilter(String elemFilter) {
            super(elemFilter);
        }

        /**
         * Variables for capturing filtered attribute name and filtered attribute value
         */
        private String FILTER_ATTRIBUTE_NAME="";
        private String FILTER_ATTRIBUTE_VALUE="";

        /**
         * Specifies the filters to be used in terms of attribute name & value
         * @param attributeName - attribute name to be filtered for
         * @param filterAttributeValue - attribute value to be filtered for
         */
        public void filterBy(String attributeName, String filterAttributeValue ) {
                this.FILTER_ATTRIBUTE_NAME = attributeName;
                this.FILTER_ATTRIBUTE_VALUE = filterAttributeValue;

        }

        @Override
        public boolean matches(Object filterObj) {
            boolean bret = super.matches(filterObj);
            if (bret) {
                Element matchedElement = (Element) filterObj;
                //get the matching attribute if it exists
                Attribute nameAttr = matchedElement.getAttribute(FILTER_ATTRIBUTE_NAME, matchedElement.getNamespace());
                if (nameAttr == null) {
                    return false;
                }
                if (nameAttr.getValue().equals(FILTER_ATTRIBUTE_VALUE)) {
                    return true;
                } else
                    return false;
            } else
                return false;
        }
    }