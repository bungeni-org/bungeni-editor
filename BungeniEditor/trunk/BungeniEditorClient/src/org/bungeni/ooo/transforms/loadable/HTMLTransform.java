/*
 * HTMLTransform.java
 *
 * Created on June 3, 2008, 4:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.ooo.transforms.loadable;

import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XStorable;
import com.sun.star.io.IOException;
import java.util.ArrayList;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.transforms.impl.BungeniDocTransform;

/**
 *
 * @author Administrator
 */
public class HTMLTransform extends BungeniDocTransform {
        private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HTMLTransform.class.getName());
 
    /** Creates a new instance of HTMLTransform */
    public HTMLTransform() {
        super();
    }

    public boolean transform(OOComponentHelper ooDocument) {
        boolean bState = false;
        try {
             XStorable docStore =ooDocument.getStorable();
            String urlString = (String) getParams().get("StoreToUrl");
            docStore.storeToURL(urlString, getTransformProps().toArray(new PropertyValue[getTransformProps().size()]));
            bState= true;
        } catch (IOException ex) {
            log.error("transform : " + ex.getMessage());
        }
        return bState;
    }
    
        
    private ArrayList<PropertyValue> getTransformProps(){
        ArrayList<PropertyValue> props = new ArrayList<PropertyValue>();
        PropertyValue prop0 = new PropertyValue();
        prop0.Name  = "FilterName";
        prop0.Value = "HTML (StarWriter)";
        props.add(prop0);
        return props;
    }
}
