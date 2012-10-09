package org.bungeni.extutils;

//~--- non-JDK imports --------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import javax.xml.transform.stream.StreamSource;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

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

    /**
     * Always loads a file as UTF-8
     * @param relativePathToFile
     * @return
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws JDOMException
     * @throws IOException
     */
    public static Document loadFile(String relativePathToFile) throws FileNotFoundException, UnsupportedEncodingException, JDOMException, IOException {
        String sFullPath = CommonFileFunctions.convertRelativePathToFullPath(relativePathToFile);
        File file = new File(sFullPath);
        Document doc = loadFile(file);
        return doc;
    }

    public static Document loadFile(File file) throws FileNotFoundException, UnsupportedEncodingException, JDOMException, IOException {
        InputStream inputStream= new FileInputStream(file);
        Reader reader = new InputStreamReader(inputStream,"UTF-8");
        InputSource is = new InputSource(reader);
        is.setEncoding("UTF-8");
        Document doc = getNonValidatingSaxBuilder().build(is);
        return doc;
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

  private static boolean validUTF8(byte[] input) {
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

    public static String getLocalizedChildElementValue(Element anElement, String localizedChild) {
        List<Element> childTitles = anElement.getChildren(localizedChild);
        //get the default
        String langCodeDefault = BungeniEditorProperties.getEditorProperty("locale.Language.iso639-2");
        String foundTitle = _findi18nTitle(childTitles, getIso3Language());
        if (foundTitle.equals("UNDEFINED")) {
            foundTitle = _findi18nTitle(childTitles, langCodeDefault );
        }
        //!+UTF8_CHECK(ah, 12-04-2012) *NO* converting to UTF-8, all XML configs
        // are read as UTF-8 and assumed to be in UTF-8
        return foundTitle;
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

    public StreamSource getFileAsStreamSource(String xsltPath ) throws FileNotFoundException {
        StreamSource sxslt = null;
        String sFullPath = CommonFileFunctions.convertRelativePathToFullPath(xsltPath);
        File xsltFile = new File(sFullPath);
        if (xsltFile.exists()) {
                sxslt = new StreamSource(xsltFile);
        } else {
                throw new FileNotFoundException("Xslt file :" + xsltPath + " not found on path : "+ sFullPath);
        }
        return sxslt;
}


}
