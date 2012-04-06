package org.bungeni.translators.process.actions;


import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.bungeni.translators.configurations.steps.OAProcessStep;
import org.bungeni.translators.translator.OADocumentBuilder;
import org.bungeni.translators.utility.transformer.GenericTransformer;
import org.bungeni.translators.utility.xpathresolver.XPathResolver;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * This action unescapes escaped HTML content in a XML document xpath
 * It also cleans up the HTML to ensure valid xml is generated.
 * The html cleaning is implemented using JSoup
 *
 * @author Ashok
 */
public class ProcessUnescape implements IProcessAction {

       private static org.apache.log4j.Logger log
           = Logger.getLogger(ProcessUnescape.class.getName());



    public Document process(Document inputDocument, OAProcessStep processInfo) throws TransformerException, SAXException, IOException {
        Document outputDocument = inputDocument;

        try {
            //get the xpath from the process node
            String xPath = processInfo.getParam();
            NodeList nlMatching = (NodeList) XPathResolver.getInstance().evaluate(outputDocument,
                                xPath,
                                XPathConstants.NODESET);
            //use the cached generic transformer
            GenericTransformer genTrans = GenericTransformer.getInstance();
            Transformer transformer = genTrans.getTransformer();
            //we change the output properties -- remember to change it back in finally {}
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            for (int i=0 ; i < nlMatching.getLength() ; i++ ) {
                // this is the node whose children will be unescaped
                Node nNodetoUnescape = nlMatching.item(i);
                if (nNodetoUnescape.hasChildNodes()) {
                    //get all the children
                    NodeList childNodes = nNodetoUnescape.getChildNodes();
                    StringWriter swOutputChildren = new StringWriter();
                    //Serialize the children to a String strem
                    for (int c=0; c <  childNodes.getLength() ; c++ ) {
                        Node nchild = childNodes.item(c);
                        // !+SAXON_VERSION (ah, nov-2011) -- warning !!! this does not work with Saxon 9.2
                        // See <http://sourceforge.net/tracker/?func=detail&aid=2898131&group_id=29872&atid=397617>
                        // to make this work i had to switch to saxon 9.3 -- though it works with the default
                        // jaxp transformer
                        transformer.transform(new DOMSource(nchild), new StreamResult(swOutputChildren));
                    }
                    //unescape the HTML
                    String unescapedHTML = unescapeHtml(swOutputChildren.toString());
                    //cleanup the HTML to correct xml, and wrap it in a XHTML namespace div
                    org.jsoup.nodes.Document jsoup = Jsoup.parse(
                           "<div xmlns=\"http://www.w3.org/1999/xhtml/\">" +
                           unescapedHTML +
                           "</div>"
                           );
                    String jsoup_html = jsoup.body().html();
                    //get the body field of the jsoup rendered html
                    Document docJsoupNode = OADocumentBuilder.getInstance().getDocumentBuilder().parse(
                           new InputSource(new StringReader(jsoup_html))
                           );
                    //the body element is to be imported into the target document
                    Node importThisNode = docJsoupNode.getDocumentElement();
                    //remove all child nodes -- which is basically unescaped content
                    while (nNodetoUnescape.hasChildNodes()) {
                        nNodetoUnescape.removeChild(nNodetoUnescape.getLastChild());
                    }
                    //now import the node into the document
                    outputDocument.adoptNode(importThisNode);
                    //and append the new node to the unescaped node
                    nNodetoUnescape.appendChild(importThisNode);
                }
            }
        } catch (TransformerConfigurationException ex) {
           log.error("Error while processing UNESCAPE", ex);
        } catch (XPathExpressionException ex) {
           log.error("Error while processing UNESCAPE", ex);
        } finally {
            try {
                GenericTransformer.getInstance().getTransformer().setOutputProperty(
                        OutputKeys.OMIT_XML_DECLARATION,
                        "no"
                        );
            } catch (TransformerConfigurationException ex) {
               log.error("Error while resetting transformer configuration", ex);
            }
        }
        return outputDocument;
        
    }

    private Pattern pentity = Pattern.compile("&[a-zA-Z]+;");

    /***
     * Recursive UN-ESCAPER ; unescape escaped entities e.g. &amp;nbsp;
     * @param inputHtml
     * @return
     */
    private String unescapeHtml(String inputHtml){
        String sUnescaped = StringEscapeUtils.unescapeHtml(inputHtml);
        Matcher mentity = pentity.matcher(sUnescaped);
        for (;true == mentity.find();) {
            sUnescaped = StringEscapeUtils.unescapeHtml(sUnescaped);
            mentity = pentity.matcher(sUnescaped);
        }
        return sUnescaped;
    }

}
