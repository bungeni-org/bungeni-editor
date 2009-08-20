package org.bungeni.ooo.transforms.loadable;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.transforms.impl.BungeniDocTransform;
import org.bungeni.ooo.utils.CommonExceptionUtils;

//~--- JDK imports ------------------------------------------------------------

import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XStorable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Ashok
 */
public class ODTSaveTransform extends BungeniDocTransform {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ODTSaveTransform.class.getName());

    /** Creates a new instance of PDFTransform */
    public ODTSaveTransform() {
        super();
    }

    public boolean transform(OOComponentHelper ooDocument) {
        boolean bState = false;

        try {

            // if (!ooDocument.isDocumentOnDisk()) {
            // document already exists
            // getParams().containsKey("StoreToURL");
            HashMap<String, Object> saveParms = getParams();
            XStorable               docStore  = ooDocument.getStorable();

            if (getParams().containsKey("StoreToURL")) {
                if (ooDocument.isDocumentOnDisk()) {
                    bState = ooDocument.saveDocument();
                } else {
                    String          urlString = (String) saveParms.get("StoreToURL");
                    PropertyValue[] props     =
                        getTransformProps().toArray(new PropertyValue[getTransformProps().size()]);

                    docStore.storeToURL(urlString, props);
                    bState = true;
                }
            } else if (getParams().containsKey("StoreAsURL")) {
                String          urlString = (String) saveParms.get("StoreAsURL");
                PropertyValue[] props     = getTransformProps().toArray(new PropertyValue[getTransformProps().size()]);

                docStore.storeAsURL(urlString, props);
                bState = true;
            }

            // } else {

            // ooDocument.saveDocument();
            // }
        } catch (com.sun.star.io.IOException ex) {
            log.error("transform : " + ex.getMessage());
            log.error("transform : " + CommonExceptionUtils.getStackTrace(ex));
            bState = false;
        } finally {
            return bState;
        }
    }

    private ArrayList<PropertyValue> getTransformProps() {
        ArrayList<PropertyValue> props = new ArrayList<PropertyValue>(0);

        // return an empty array to use native format
        return props;
    }
}
