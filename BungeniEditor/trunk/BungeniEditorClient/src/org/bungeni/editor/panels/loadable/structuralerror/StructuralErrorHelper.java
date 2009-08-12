package org.bungeni.editor.panels.loadable.structuralerror;

import com.thoughtworks.xstream.XStream;

/**
 *
 * @author Ashok Hariharan
 */
public class StructuralErrorHelper {

    public static void structuralErrorAlias(XStream xst) {
        xst.alias("structuralError", StructuralError.class);
    }

}
