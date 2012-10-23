package org.bungeni.utils;

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
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import org.bungeni.extutils.CommonXmlConfigParams;
import org.bungeni.extutils.CommonXmlUtils;

/**
 *
 * @author Ashok Hariharan
 */
public class CommonEditorXmlUtils {

   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CommonEditorXmlUtils.class.getName());


    private static SAXBuilder nonValidatingSaxBuilder = null;
    private static SAXBuilder validatingSaxBuilder    = null;
    private static String iso3Language = null;
    private static XMLOutputter outputter = null;

    public static SAXBuilder getNonValidatingSaxBuilder() {
        if (nonValidatingSaxBuilder == null) {
            nonValidatingSaxBuilder = new SAXBuilder(CommonXmlConfigParams.SAX_PARSER_DRIVER, false);
        }

        return nonValidatingSaxBuilder;
    }

    public static SAXBuilder getValidatingSaxBuilder() {
        if (validatingSaxBuilder == null) {
            validatingSaxBuilder = new SAXBuilder(CommonXmlConfigParams.SAX_PARSER_DRIVER, true);
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
        //String sFullPath = CommonFileFunctions.convertRelativePathToFullPath(relativePathToFile);
        File file = new File(relativePathToFile);
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

    /**
     * Get a i18n-ised message value for a configuration string
     *
     * @param langCodeDefault language code in iso639-2 format
     * @param anElement the config element containging the nodes to be localized
     * @param localizedChild the child element to be localized
     * @return
     */
    public static String getLocalizedChildElementValue(String langCodeDefault, Element anElement, String localizedChild) {
        List<Element> childTitles = anElement.getChildren(localizedChild);
        String foundTitle = CommonXmlUtils._findi18nTitle(childTitles, CommonXmlUtils.getIso3Language());
        if (foundTitle.equals("UNDEFINED")) {
            foundTitle = CommonXmlUtils._findi18nTitle(childTitles, langCodeDefault );
        }
        //!+UTF8_CHECK(ah, 12-04-2012) *NO* converting to UTF-8, all XML configs
        // are read as UTF-8 and assumed to be in UTF-8
        return foundTitle;
    }


   

}
