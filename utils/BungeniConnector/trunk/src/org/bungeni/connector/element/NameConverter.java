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
public class NameConverter implements Converter {

       
       public boolean canConvert(Class clazz) {
                return clazz.equals(Name.class);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer,
                        MarshallingContext context) {
                Name Name = (Name) value;
                writer.startNode("name");
                writer.addAttribute("lang", Name.getLang());
                writer.setValue(Name.getValue());
            
                writer.endNode();
        }

        public Object unmarshal(HierarchicalStreamReader reader,
                        UnmarshallingContext context) {
                Name name = new Name();
                
                name.setLang(reader.getAttribute("lang"));
                name.setValue(reader.getValue());
                
                return name;
        }
}
