/*
 * OOoNumberingHelper.java
 *
 * Created on May 18, 2008, 8:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.numbering.ooo;

import java.util.HashMap;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Administrator
 */
public class OOoNumberingHelper {
       
    public static HashMap<String, String> numberingMetadata =
            new HashMap<String,String>()  {
                {
                    put("APPLIED_NUMBER", "BungeniAppliedNumber" );
                    put("NUMBERING_SCHEME", "BungeniNumberingScheme" );
                    put("APPLIED_TRUE_NUMBER", "BungeniAppliedTrueNumber");
                    put("PARENT_PREFIX_NUMBER", "BungeniParentPrefix");
                }
    };
    
    public final static String NUMBERING_SEPARATOR_DEFAULT=".";
    public final static String NUMBERING_SECTION_TYPE ="NumberedContainer";
    public final static String SECTION_IDENTIFIER = "BungeniSectionUUID";
    
    public final static String NUM_FIELD_PREFIX = "fldnum_";
    public final static String HEAD_FIELD_PREFIX = "fldhead_";
    public final static String HEADING_REF_PREFIX = "hdrf_";
    public final static String NUMBER_REF_PREFIX = "nrf_";
    
    public final static String NUMBERED_PREFIX = "<";
    public final static String NUMBERED_SUFFIX = ">";
    public final static String NUMBER_HEADING_BOUNDARY="~";
    
    public final static String INTERNAL_REF_PREFIX="rf:";
    public final static String EXTERNAL_REF_PREFIX="erf:";
    public final static String URI_REF_PREFIX="uri:";
    
    public final static String META_PREFIX_NUMBER = INTERNAL_REF_PREFIX + NUMBER_REF_PREFIX;
    public final static String META_PREFIX_HEAD = INTERNAL_REF_PREFIX + HEADING_REF_PREFIX;

    /** Creates a new instance of OOoNumberingHelper */
    public OOoNumberingHelper() {
    }
    
    public static String getSectionAppliedNumber(OOComponentHelper ooDoc, String sectionName) {
        String appliedNumber = "";
        HashMap<String,String> sectionMeta = ooDoc.getSectionMetadataAttributes(sectionName);
        if (sectionMeta.containsKey(numberingMetadata.get("APPLIED_NUMBER"))) {
            appliedNumber = sectionMeta.get(numberingMetadata.get("APPLIED_NUMBER"));
        }
        return appliedNumber ;
    }
    
    
}
