/*
 * Copyright (C) 2013 undesa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.bungeni.translators.configurations;

import javax.xml.transform.TransformerConfigurationException;
import org.bungeni.translators.utility.dom.DOMUtility;
import org.w3c.dom.Element;

/**
 *
 * @author Ashok Hariharan
 */
public class Parameter extends Object {
    
      private static org.apache.log4j.Logger log  =
        org.apache.log4j.Logger.getLogger(Parameter.class.getName());

    
      private String name ;   

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type
     */
    public ParameterType getType() {
        return type;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }
      
      public enum ParameterType {
        text,
        xml
      };
      
      private ParameterType type;

      private Object value;

      public Parameter (String name, String type) {
          this(name, type, "");
      }
      
      public Parameter(String name, String type, String value) {
          this.name = name;
          try {
            this.type = ParameterType.valueOf(type);
          } catch (IllegalArgumentException ex) {
            this.type = ParameterType.text;  
          }
          this.value = value;
      }
      
      
      
      public void setValue(String inputValue) {
          if(getType().equals(ParameterType.text)) {
              this.value = inputValue;
          } else {
              net.sf.saxon.dom.DocumentWrapper dw = null;
              try {
                dw =  DOMUtility.getInstance().getSaxonDocumentWrapperForString(inputValue);
              } catch (TransformerConfigurationException ex) {
                  log.error("Error while getting Saxon Document Wrapper !", ex);
              }
              if (null != dw){
                  this.value = dw;
              }
          }
      }
      
      public void setValue(Element inputValue) {
          if (getType().equals(ParameterType.xml)) {
              net.sf.saxon.dom.DocumentWrapper dw = null;
              try {
                dw =  DOMUtility.getInstance().getSaxonDocumentWrapperForNode(inputValue);
              } catch (TransformerConfigurationException ex) {
                  log.error("Error while getting Saxon Document Wrapper !", ex);
              }
              if (null != dw){
                  this.value = dw;
              }
          } else {
              log.error("Wrong type specification ! text, setValue Expected xml ");
          }
      }

}