/*
 *  Copyright (C) 2012 windows
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.bungeni.editor.panels.loadable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.bungeni.editor.document.DocumentSection;
import org.bungeni.editor.document.DocumentSectionsContainer;
import org.bungeni.editor.providers.DocumentSectionIterator;
import org.bungeni.numbering.impl.IGeneralNumberingScheme;
import org.bungeni.numbering.impl.INumberDecorator;
import org.bungeni.numbering.impl.NumberDecoratorFactory;
import org.bungeni.numbering.impl.NumberRange;
import org.bungeni.numbering.impl.NumberingSchemeFactory;
import org.bungeni.odfdom.section.BungeniOdfSectionHelper;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.element.text.TextSectionElement;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class numbers various sections of an ODF document (bill/judgement) based on the
 * decorators and numbering scheme assigned for the section. The numbering scheme for a
 * particular section is obtained from the section metadata while the numbering scheme is obtained
 * from the bill.xml file
 *
 * @author Reagan Mbitiru <reaganmbitiru@gmail.com>
 */
public final class NumberingAgent {

    private OdfDocument odfDocument ;
    private static Logger log = Logger.getLogger(NumberingAgent.class.getName());
    private BungeniOdfSectionHelper sectionHelper ;
    private String filePath ;
    private IGeneralNumberingScheme m_selectedNumberingScheme ;

    public NumberingAgent(String filePath) {
        try {
            odfDocument = OdfDocument.loadDocument(filePath) ;
            this.filePath = filePath ;
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    public NumberingAgent (File file) {
        try {
            odfDocument = OdfDocument.loadDocument(file) ;
            this.filePath = file.getAbsolutePath() ;

        } catch (Exception ex) {
            log.error(ex);
        }
    }

    /**
     * This method initializes a swing worker thread and
     * uses it to number the document
     * @return returns the fileName of the saved document
     */
    public boolean numberDocument() {
        boolean numberingOK = false ;

        // get the document sections names that need to be renumbered
        ArrayList <String> numberedContainers = findNumberedContainers (odfDocument) ;

        if ( numberedContainers.size() > 0 ) {

            // apply the renumbering
            numberingOK = applyNumberingToSections(numberedContainers) ;

            if ( numberingOK ) {
                try {
                    // save the document
                    odfDocument.save(filePath) ;
                    numberingOK = true ;
                } catch (Exception ex) {
                    log.error(ex);
                }
            }
        }
        return numberingOK ;
    }

    /**
     * This method searches for all the containers who section
     * meta-data as stored in the RDF file indicates that they are to have
     * a numbering scheme applied
     * @param odfDoc
     * @return
     */
    private ArrayList<String> findNumberedContainers(OdfDocument odfDoc) {
        ArrayList<String> nContainers = new ArrayList<String>(0) ; // stores the numbered containers


        // loop through each of the document sections
        // reviewing the metadata and finding out if the sections require numbering
       sectionHelper = new BungeniOdfSectionHelper(odfDoc) ;

        // get the sections
        NodeList docSections = sectionHelper.getDocumentSections() ;

        // loop through the sections finding out if they
        // have a NUMBERING_SCHEME
        for ( int i = 0 ; i < docSections.getLength() ; i ++ ) {
            TextSectionElement currSection = (TextSectionElement) docSections.item(i) ;
            String sectionType = sectionHelper.getSectionType(currSection) ;

            if ( sectionType.length() > 0 && !sectionType.equals("body") ) {

                // get the NUMBERING_SCHEME attribute from the bill.xml file
                // String numberingScheme = sectionHelper.getSectionMetadataValue(currSection, "BungeniNumberingScheme") ;
                String numberingScheme = "bill" ;

                // add the section to the numberedList container arraylist
                if ( numberingScheme.length() > 0 )
                {
                        String sectionName = currSection.getTextNameAttribute() ;
                        nContainers.add(sectionName);
                }
            }
        }

        return nContainers ;
    }

    /**
     * This method actually applies the numbering to the sections in the document
     * that need to be renumbered. It determines the decorator to use for the renumbering
     * based on the contents of the bill.xml file
     * @param numberedContainers
     * @return
     */
    private boolean applyNumberingToSections(ArrayList<String> numberedContainers) {
        boolean numberingOK = true ;

        // iterate through the numbered containers applying the markup at the
        //  beginning of the document
        for ( String containersCounter : numberedContainers )  {

            // get the section text
            TextSectionElement section = sectionHelper.getSection(containersCounter) ;

            // get the section type
            String sectionType = sectionHelper.getSectionType(section);

            // add the Node to section type
            if ( !addNumbering(section) )
            {
                numberingOK = false ;
                break ;
            }
        }

        return numberingOK ;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String odfDocPath = "c:/text.odt" ;
        NumberingAgent nAgent = new NumberingAgent(odfDocPath) ;

        if ( nAgent.numberDocument() ) {
            JOptionPane.showMessageDialog(null, "Document " + odfDocPath
                    + " was numbered OK" , "Numbering Completed" , JOptionPane.OK_OPTION);
        }
        else {
            JOptionPane.showMessageDialog(null, "There was an error marking up the Document " + odfDocPath
                     , "Error Numbering Document" , JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method finds the first text node to a section
     * and adds the numbering to it
     * @param section
     * @param decoratorType
     */
    private boolean addNumbering(TextSectionElement section) {
      
        Node elemNode = section.getFirstChild() ;
        String nodeText = elemNode.getTextContent() ;
        
        // get the section type
        String sectionType = sectionHelper.getSectionType(section) ;

        // elemNode.setTextContent("!+ " + nodeText) ;
        
        // get the numbering scheme for the section type
        ArrayList<String> matchingSectionTypes =  getSectionsMatchingType(sectionType, odfDocument) ;

        if ( matchingSectionTypes.isEmpty() )
            return false ;

        // get all the document section types
        HashMap<String, DocumentSection> sectionTypesForDocumentType = new HashMap<String, DocumentSection>();
        sectionTypesForDocumentType = DocumentSectionsContainer.getDocumentSectionsContainer();

        // get the numbering scheme details for the section type
        String numberingSchemeForType = sectionTypesForDocumentType.get(sectionType).getNumberingScheme();
        String numberDecoratorForType = sectionTypesForDocumentType.get(sectionType).getNumberDecorator();

        INumberDecorator numDecor = null;

        if (!numberDecoratorForType.equals("none")) {
            numDecor = NumberDecoratorFactory.getNumberDecorator(numberDecoratorForType);
        }

        initNumberingSchemeGenerator(numberingSchemeForType, 1, matchingSectionTypes.size());

        for (String matchingSection : matchingSectionTypes ) {
            TextSectionElement matchedSection = sectionHelper.getSection(matchingSection);

            // intialise the context sections
            String parentSectionName = "", prevParentSectionName = "";
           
            parentSectionName = matchedSection.getParentNode().getNodeName();

            if (!parentSectionName.equals(prevParentSectionName)) {
                this.m_selectedNumberingScheme.sequence_initIterator();
            }

            String theNumber = this.m_selectedNumberingScheme.sequence_next();
            long lBaseIndex = this.m_selectedNumberingScheme.sequence_base_index(theNumber);

            updateNumberInSection3(odfDocument, matchedSection, theNumber, numDecor, lBaseIndex);

            prevParentSectionName = parentSectionName;
        } 

        
       return true ;
    }

    /**
     * This method finds the decorator types for a particular type of section
     * @param sectionType
     * @return
     */
    public ArrayList<String> getSectionsMatchingType(String sectionType, OdfDocument odfDoc) {
        ArrayList<String> sectionTypes = null ;

        sectionTypeIteratorListener typeIterator = new sectionTypeIteratorListener(sectionType, odfDoc);
        DocumentSectionIterator sectionTypeIterator = new DocumentSectionIterator(typeIterator);
        sectionTypeIterator.startIterator();
        sectionTypes =  typeIterator.sectionsMatchingType;

        return sectionTypes ;
    }

    private void initNumberingSchemeGenerator(String schemeName, long startRange, long endRange) {
        
        try {           
            m_selectedNumberingScheme = NumberingSchemeFactory.getNumberingScheme(schemeName);
            m_selectedNumberingScheme.setRange(new NumberRange(startRange, endRange));
            m_selectedNumberingScheme.generateSequence();
            m_selectedNumberingScheme.sequence_initIterator();
        } catch (Exception ex) {
            log.error("initNumberingSchemeGenerator : " + ex.getMessage());
        }

    }

    private void updateNumberInSection3(OdfDocument ooDoc, TextSectionElement numberedChild,
            String theNumber, INumberDecorator numberDecorator, long lNumBaseIndex) {

        // get the section metadata attributes for the section
        NodeList sectionAttributes = sectionHelper.getSectionMetadataAttributes(numberedChild) ;

        // get the section UUID
        String sectionUUID ;
        
        for ( int i = 0, allAtts = sectionAttributes.getLength() ; i < allAtts ;i ++ )  {
            Node currNode =  sectionAttributes.item(i);

            if ( currNode.hasAttributes() ) {
                NamedNodeMap atts = currNode.getAttributes() ;

                for ( int k = 0 , s =  atts.getLength(); k < s  ; k ++ ) {
                    if ( atts.item(k).getNodeName().equals("BungeniSectionID")) {
                        sectionUUID = atts.item(k).getNodeValue() ;
                    }
                }
            }
        }

        if (numberDecorator != null) {
            theNumber = numberDecorator.decorate(theNumber);
        }
    }
}
