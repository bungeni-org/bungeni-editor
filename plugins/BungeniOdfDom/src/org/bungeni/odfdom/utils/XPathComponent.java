package org.bungeni.odfdom.utils;

//~--- JDK imports ------------------------------------------------------------

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that breaks down the XPath component into element / prefix / index
 * @author Ashok Hariharan
 */
public class XPathComponent {

    /**
     * if the xpathString was text:change[32]
     */
    String xpathElement;
    String xpathIndex;
    String xpathPrefix;
    String xpathString;

    public XPathComponent(String xpathString) {
        this.xpathString = xpathString;
        parseComponent();
    }

    public String getPrefix(){
        return xpathPrefix;
    }

    public String getElement(){
        return xpathElement;
    }

    public String getIndex(){
        return xpathIndex;
    }

    public String getXPathString(){
        return xpathString;
    }

    private void parseComponent() {
        String  elementPattern   = "([a-z]*?):([a-z-]*?)";
        String  depthPattern     = "\\[([0-9]*?)\\]";
        Pattern patElemDepth     = null;    // = Pattern.compile(elementPattern + depthPattern);
        Matcher matElemDepth     = null;    // = patElemDepth.matcher(xpathString);
        boolean bHasDepthPattern = false;

        if (xpathString.contains("[")) {
            patElemDepth     = Pattern.compile(elementPattern + depthPattern);
            bHasDepthPattern = true;
        } else {
            patElemDepth = Pattern.compile(elementPattern);
        }

        matElemDepth = patElemDepth.matcher(xpathString);

        String elementType  = "";
        String elementIndex = "";

        if (matElemDepth.find()) {
            if (bHasDepthPattern) {
                //match the index in the 3rd group
                this.xpathIndex = matElemDepth.group(3);
            }
            //find from the other 2 groups
            this.xpathPrefix  = matElemDepth.group(1);
            this.xpathElement = matElemDepth.group(2);
        }
    }
}
