package org.bungeni.utils;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.utils.BungeniEditorProperties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.bungeni.extutils.CommonXmlUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

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

    public static String getLocalizedChildElementValue(Element anElement, String localizedChild) {
        List<Element> childTitles = anElement.getChildren(localizedChild);
        //get the default
        String langCodeDefault = BungeniEditorProperties.getEditorProperty("locale.Language.iso639-2");
        String foundTitle = CommonXmlUtils._findi18nTitle(childTitles, CommonXmlUtils.getIso3Language());
        if (foundTitle.equals("UNDEFINED")) {
            foundTitle = CommonXmlUtils._findi18nTitle(childTitles, langCodeDefault );
        }
        //!+UTF8_CHECK(ah, 12-04-2012) *NO* converting to UTF-8, all XML configs
        // are read as UTF-8 and assumed to be in UTF-8
        return foundTitle;
    }


   

}
