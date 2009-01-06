/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.frbr;

import org.bungeni.editor.metadata.*;
import java.util.ArrayList;

/**
 *
 * @author undesa
 */
public class ANMetadataModel {

    FRBRwork Work;
    FRBRexpression Expression;
    FRBRmanifestation Manifestation;
    CountryCode countryCode;
    ArrayList<LanguageCode> languageCodes = new ArrayList<LanguageCode>(0);
    DocumentPart Part;
    
    public ANMetadataModel(){
        Work = new FRBRwork();
        Expression = new FRBRexpression();
        Manifestation = new FRBRmanifestation();
        countryCode = new CountryCode();
        Part = new DocumentPart();
    }
}
