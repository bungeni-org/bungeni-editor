package org.bungeni.extutils;

//~--- non-JDK imports --------------------------------------------------------

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author Ashok Hariharan
 */
public class CommonXmlUtils {

   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CommonXmlUtils.class.getName());


    private static String iso3Language = null;
    private static XMLOutputter outputter = null;


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
            byte[] bytes = text.getBytes();
            //!+UTF8_CHECK (ah, 12-04-2012) Do a convert to UTF8 only after
            //checking if the string is NOT in UTF8 - otherwise encoding a UTF8
            // string into UTF8 twices causes weird errors
            // Issue reported by samar ayash.
            if (validUTF8(bytes)){
               returnText = text;
            } else {
                returnText = new String(text.getBytes(), "UTF8");
            }
        } catch (UnsupportedEncodingException ex) {
            returnText = text;
        }
        return returnText;
    }

  public static boolean validUTF8(byte[] input) {
      int i = 0;
      // Check for BOM
      if (input.length >= 3 && (input[0] & 0xFF) == 0xEF
        && (input[1] & 0xFF) == 0xBB & (input[2] & 0xFF) == 0xBF) {
       i = 3;
      }

      int end;
      for (int j = input.length; i < j; ++i) {
       int octet = input[i];
       if ((octet & 0x80) == 0) {
        continue; // ASCII
       }

       // Check for UTF-8 leading byte
       if ((octet & 0xE0) == 0xC0) {
        end = i + 1;
       } else if ((octet & 0xF0) == 0xE0) {
        end = i + 2;
       } else if ((octet & 0xF8) == 0xF0) {
        end = i + 3;
       } else {
        // Java only supports BMP so 3 is max
        return false;
       }

       while (i < end) {
        i++;
        octet = input[i];
        if ((octet & 0xC0) != 0x80) {
         // Not a valid trailing byte
         return false;
        }
       }
      }
      return true;
 }


     public static String _findi18nTitle(List<Element> childTitles, String langCode) {
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
