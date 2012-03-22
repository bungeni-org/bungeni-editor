package org.bungeni.extutils;

//~--- non-JDK imports --------------------------------------------------------

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author Ashok Hariharan
 */
public class CommonXmlUtils {

   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CommonXmlUtils.class.getName());


    private static SAXBuilder nonValidatingSaxBuilder = null;
    private static SAXBuilder validatingSaxBuilder    = null;
    private static String iso3Language = null;
    private static XMLOutputter outputter = null;

    public static SAXBuilder getNonValidatingSaxBuilder() {
        if (nonValidatingSaxBuilder == null) {
            nonValidatingSaxBuilder = new SAXBuilder(BungeniEditorProperties.SAX_PARSER_DRIVER, false);
        }

        return nonValidatingSaxBuilder;
    }

    public static SAXBuilder getValidatingSaxBuilder() {
        if (validatingSaxBuilder == null) {
            validatingSaxBuilder = new SAXBuilder(BungeniEditorProperties.SAX_PARSER_DRIVER, true);
        }

        return validatingSaxBuilder;
    }

    public static String getIso3Language(){
        if (iso3Language == null) {
            Locale defLocale = Locale.getDefault();
            //we capture the iso3 default language for the current locale
            //xml config uses iso3 language representation
            iso3Language = defLocale.getISO3Language();
        }
        return iso3Language;
    }

    private static String utf8String(String text){
        String returnText = "";
        try {
            returnText = new String(text.getBytes(), "UTF8");
        } catch (UnsupportedEncodingException ex) {
            returnText = text;
        }
        return returnText;
    }

    public static String getLocalizedChildElement(Element anElement, String localizedChild) {
        List<Element> childTitles = anElement.getChildren(localizedChild);
        //get the default
        String langCodeDefault = BungeniEditorProperties.getEditorProperty("locale.Language.iso639-2");
        String foundTitle = _findi18nTitle(childTitles, getIso3Language());
        if (foundTitle.equals("UNDEFINED")) {
            foundTitle = _findi18nTitle(childTitles, langCodeDefault );
        }
        return utf8String(foundTitle);
    }

     private static String _findi18nTitle(List<Element> childTitles, String langCode) {
         for (Element title : childTitles) {
           String foundLang =  title.getAttributeValue("lang", Namespace.XML_NAMESPACE);
           //AH-25-02-11 if the xml:lang attribute is not set on title or tooltip, foundLang
           //is set to null -- added a check to log such errors and continue processing
           if (foundLang == null) {
               String sError = "";
               Element pElement = title.getParentElement();
               if (pElement != null ) {
                   String s = pElement.getAttributeValue("name");
                   if (s != null) {
                       sError = s;
                   }
               }
               log.error("language was not specified for :" + sError + " returning title anyway");
               return outputElementContentAsRawXMLorString(title);
           }
           if (foundLang.equals(langCode)) {
             return outputElementContentAsRawXMLorString(title);
           }
        }
       return "UNDEFINED";
    }

    public static XMLOutputter getXmlOutputter(){
        if (outputter == null) {
            outputter = new XMLOutputter();
        }
        return outputter;
    }

    public static String outputElementContentAsRawXMLorString(Element forElement) {
        Element childHtml = forElement.getChild("html");
        if (childHtml == null) {
            return forElement.getTextNormalize();
        } else {
            return getXmlOutputter().outputString(childHtml);
        }
    }


}
