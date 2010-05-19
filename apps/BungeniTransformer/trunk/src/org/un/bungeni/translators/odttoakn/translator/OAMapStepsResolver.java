package org.un.bungeni.translators.odttoakn.translator;

//~--- non-JDK imports --------------------------------------------------------

import org.un.bungeni.translators.odttoakn.map.OAMap;
import org.un.bungeni.translators.odttoakn.steps.OAMapStep;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.Iterator;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;
import org.apache.log4j.Logger;

/**
 * ASHOK ::: THIS DOES NOT APPEAR TO BE USED MARK FOR DELETION 
 * Used to resolve the MAP STEPS of a configuration file
 */
public final class OAMapStepsResolver {
 private static org.apache.log4j.Logger log                   = Logger.getLogger(OAMapStepsResolver.class.getName());

    /**
     * Return the StreamSource obtained after all the MAP steps of the given
     * configuration Document are applied to the given Stream source of the document
     * @param aMap the map file that contains the MAP STEPS
     * @return a new StreamSource Obtained applying all the steps of the configuration to the
     *                      given StreamSource
     * @throws XPathExpressionException
     * @throws TransformerException
     */
    protected static StreamSource resolve(OAMap aMap) throws XPathExpressionException, TransformerException {
    log.debug("resolving map steps ");
        // get the map steps from the map
        HashMap<Integer, OAMapStep> mapSteps = aMap.getMapSteps();

        // create an iterator on the hash map
        Iterator<OAMapStep> mapStepsIterator = mapSteps.values().iterator();

        // while the Iterator has steps apply the transformation
        while (mapStepsIterator.hasNext()) {

            // get the next step
            OAMapStep nextMapStep = (OAMapStep) mapStepsIterator.next();
            // the hash map that will cont ains the parametes
            HashMap<String, Object> paramMap = new HashMap<String, Object>();

            // get the map step info and fill them into the params map
            paramMap.put("id", (Integer) nextMapStep.getId());
            paramMap.put("bungeniSectionType", (String) nextMapStep.getBungeniSectionType());
            paramMap.put("result", (String) nextMapStep.getResult());
            log.debug(" map step for :" + paramMap.get("bungeniSectionType") + " , result = " + paramMap.get("result"));
        }

        // return the transformed document
        return null;
    }
}
