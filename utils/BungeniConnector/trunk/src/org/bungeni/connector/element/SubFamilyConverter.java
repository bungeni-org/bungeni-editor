/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bungeni.connector.element;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 *
 * @author bzuadmin
 */
public class SubFamilyConverter implements Converter {
     public boolean canConvert(Class clazz) {
                return clazz.equals(SubFamily.class);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer,
                        MarshallingContext context) {
                SubFamily subFamily = (SubFamily) value;
                writer.startNode("subFamily");
                writer.addAttribute("lang", subFamily.getLang());
                writer.setValue(subFamily.getValue());
            
                writer.endNode();
        }


        public Object unmarshal(HierarchicalStreamReader reader,
                        UnmarshallingContext context) {
                SubFamily subFamily = new SubFamily();
                
                subFamily.setLang(reader.getAttribute("lang"));
                subFamily.setValue(reader.getValue());
                
                return subFamily;
        }
}
