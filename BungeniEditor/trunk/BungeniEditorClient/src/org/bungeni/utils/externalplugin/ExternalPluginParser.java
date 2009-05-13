
package org.bungeni.utils.externalplugin;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.CommonFileFunctions;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import org.jdom.Element;

/**
 *
 * @author Ashok Hariharan
 */
public class ExternalPluginParser {
    private static org.apache.log4j.Logger log =
        org.apache.log4j.Logger.getLogger(ExternalPluginParser.class.getName());

    /**
     * Sample plugin config file :
     * <plugin name="ConvertToPlain" base="convert_to_plain" >
     * <main>BungeniSectionPlain.jar</main>
     * <required>
     *   <lib>bungeniodfdom.jar</lib>
     *   <lib>odfdom.jar</lib>
     *   <lib>log4j.jar</lib>
     * </required>
     * </plugin>
     */
    String PLUGIN_BASE         = "/plugin/@base";
    String PLUGIN_MAIN_JAR     = "/plugin/main";
    String PLUGIN_NAME         = "/plugin/@name";
    String PLUGIN_REQUIRED_JAR = "/plugin/required/lib";

    /**
     * Current document being parsed
     */
    private Document   parsedPluginDocument = null;
    private SAXBuilder pluginBuilder        = null;
    private String     pluginConfigFile     = "";

    public ExternalPluginParser() {
        pluginBuilder = new SAXBuilder(BungeniEditorProperties.SAX_PARSER_DRIVER, false);
    }

    public boolean parsePluginFile(String pluginConfigFile) {
        boolean bState = false;

        try {
            this.pluginConfigFile = pluginConfigFile;

            String       sFileContents = CommonFileFunctions.getFileAsString(pluginConfigFile);
            StringReader fReader       = new StringReader(sFileContents);

            this.parsedPluginDocument = pluginBuilder.build(fReader);
            bState                    = true;
        } catch (JDOMException ex) {
            log.error("parsePluginFile : " + ex.getMessage());
        } catch (IOException ex) {
            log.error("parsePluginFile : " + ex.getMessage());
        } finally {
            return bState;
        }
    }

    public String getPluginBase() {
        String pluginBase = "";

        try {
            XPath     grpPath  = XPath.newInstance(PLUGIN_BASE);
            Attribute baseAttr = (Attribute) grpPath.selectSingleNode(this.parsedPluginDocument);

            pluginBase = baseAttr.getValue();
        } catch (JDOMException ex) {
            log.error("getPluginBase : " + ex.getMessage());
        } finally {
            return pluginBase;
        }
    }

    public String getPluginMainJar() {
        String pluginMain = "";
        try {
            XPath     grpPath  = XPath.newInstance(PLUGIN_MAIN_JAR);
            Element foundNode = (Element) grpPath.selectSingleNode(this.parsedPluginDocument);
            pluginMain = foundNode.getValue();
        } catch (JDOMException ex) {
            log.error("getPluginName : " + ex.getMessage());
        } finally {
            return pluginMain;
        }
    }

    public ArrayList<String> getPluginReqdJar(){
        ArrayList<Element> actionGrpElements = new ArrayList<Element>(0);
        ArrayList<String> reqdLibs = new ArrayList<String>(0);
        try {
            XPath grpPath = XPath.newInstance(PLUGIN_REQUIRED_JAR);
            actionGrpElements = (ArrayList<Element>) grpPath.selectNodes(this.parsedPluginDocument);
            for (Element elem : actionGrpElements) {
                String libName = elem.getValue();
                reqdLibs.add(libName);
            }
        } catch (JDOMException ex) {
            log.error("getTabElements : " + ex.getMessage());
        } finally {
            return reqdLibs;
        }
    }

     public String getPluginName() {
        String pluginBase = "";

        try {
            XPath     grpPath  = XPath.newInstance(PLUGIN_NAME);
            Attribute baseAttr = (Attribute) grpPath.selectSingleNode(this.parsedPluginDocument);

            pluginBase = baseAttr.getValue();
        } catch (JDOMException ex) {
            log.error("getPluginName : " + ex.getMessage());
        } finally {
            return pluginBase;
        }
    }



    public static void main(String[] args) {
        ExternalPluginParser pp = new ExternalPluginParser();

        pp.parsePluginFile(
            "/home/undesa/Projects/Bungeni/BungeniEditor/trunk/BungeniEditorClient/dist/plugins/plugin_converttoplain.xml");
        System.out.println("plugin base = " + pp.getPluginBase());
        System.out.println("plugin name = " + pp.getPluginName());
        System.out.println("plugin main = " + pp.getPluginMainJar());
        System.out.println("plugin reqd = " + pp.getPluginReqdJar());

    }
}


//~ Formatted by Jindent --- http://www.jindent.com
