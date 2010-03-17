package org.bungeni.odfdom.utils;

//~--- JDK imports ------------------------------------------------------------

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that breaks down the XPath component into element / prefix / index
 * @author Ashok Hariharan
 */
class XPathComponent {
    String xpathElement;
    String xpathIndex;
    String xpathPrefix;
    String xpathString;

    XPathComponent(String xpathString) {
        this.xpathString = xpathString;
        parseComponent();
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

                // there are 3 groups
                elementType  = matElemDepth.group(2);
                elementIndex = matElemDepth.group(3);
            } else {

                // there are 2 groups
                elementType = matElemDepth.group(2);
            }
        }
    }
}
