package org.bungeni.ooo.transforms.loadable;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.transforms.impl.BungeniDocTransform;
import org.bungeni.ooo.utils.CommonExceptionUtils;

//~--- JDK imports ------------------------------------------------------------

import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XStorable;

import java.util.ArrayList;

/**
 *
 * @author ashok hariharan
 */
public class PDFTransform extends BungeniDocTransform {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PDFTransform.class.getName());

    /** Creates a new instance of PDFTransform */
    public PDFTransform() {
        super();
    }

    public boolean transform(OOComponentHelper ooDocument) {
        boolean bState = false;

        try {
            XStorable       docStore  = ooDocument.getStorable();
            String          urlString = (String) getParams().get("StoreToUrl");
            PropertyValue[] props     = getTransformProps().toArray(new PropertyValue[getTransformProps().size()]);

            docStore.storeToURL(urlString, props);
            bState = true;
        } catch (com.sun.star.io.IOException ex) {
            log.error("transform : " + ex.getMessage());
            log.error("transform : " + CommonExceptionUtils.getStackTrace(ex));
        }

        return bState;
    }

    private ArrayList<PropertyValue> getTransformProps() {
        ArrayList<PropertyValue> props = new ArrayList<PropertyValue>();
        PropertyValue            prop0 = new PropertyValue();

        prop0.Name  = "FilterName";
        prop0.Value = "writer_pdf_Export";
        props.add(prop0);

        return props;
    }
}
