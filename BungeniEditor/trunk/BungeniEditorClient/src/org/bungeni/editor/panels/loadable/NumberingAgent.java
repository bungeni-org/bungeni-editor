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
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.bungeni.editor.document.DocumentSection;
import org.bungeni.editor.document.DocumentSectionsContainer;
import org.bungeni.numbering.impl.IGeneralNumberingScheme;
import org.bungeni.numbering.impl.INumberDecorator;
import org.bungeni.numbering.impl.NumberDecoratorFactory;
import org.bungeni.numbering.impl.NumberRange;
import org.bungeni.numbering.impl.NumberingSchemeFactory;
import org.bungeni.odfdom.section.BungeniOdfSectionHelper;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.element.text.TextSectionElement;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class numbers various sections of an ODF document (bill/judgement) based on the
 * decorators and numbering scheme assigned for the section. The numbering scheme for a
 * particular section is obtained from the section metadata while the numbering scheme is obtained
 * from the bill.xml file
 *
 * ***** CODE LOGIC ************
 * 1. As the bill OdfDocument is parsed through, a 'Section' of the document (Part, Section e.t.c)
 * is noted.
 * 2. The Numbering for the section is determined.
 * 3. A sentinel 'Counter' value (static) is applied to the 'Section'. This sentinel value
 * is incremented for similar sections
 * 4. The 'Section' is numbered based on the applied sentinel value
 * 5. Child 'Sections' for the current 'Section' are determined and GOTO (2)
 *
 * @author Reagan Mbitiru <reaganmbitiru@gmail.com>
 */
public final class NumberingAgent {

    private OdfDocument odfDocument ;
    private static Logger log = Logger.getLogger(NumberingAgent.class.getName());
    private BungeniOdfSectionHelper sectionHelper ;
    private String filePath ;
    private HashMap<String,Integer> sectionGIntegers ; // stores the maximum
                            // value for each of the sections in the document
                            // However on entering child sections, the maximum
                            // value for the iteration of that specific section
                            // type is reset
    private int sectionCounter = 0 ; // initialised...this is incremented to
                            //  determine the number that should be applied to a section
    private ArrayList<SectionElementContext> parsedSections ;  // stores the sections that have been
                            // parsed to determine if the section should have
                            // numbering applied to it
    private HashMap<String, DocumentSection> sectionTypesForDocumentType = new HashMap<String, DocumentSection>();
    private boolean isDocumentNumbered = false ; // indicates whetheer the current bill document
                     // has been numbered before

    public NumberingAgent(String filePath, boolean wasDocumentNumbered) {
        try {
            odfDocument = OdfDocument.loadDocument(filePath) ;
            this.filePath = filePath ;
            isDocumentNumbered = wasDocumentNumbered ;

            init() ;

        } catch (Exception ex) {
            log.error(ex);
        }
    }

    public NumberingAgent (File file, boolean wasDocumentNumbered) {
        try {
            odfDocument = OdfDocument.loadDocument(file) ;
            this.filePath = file.getAbsolutePath() ;
            isDocumentNumbered = wasDocumentNumbered ;
            
            init() ;

        } catch (Exception ex) {
            log.error(ex);
        }
    }

    private void init() {
        sectionGIntegers = new HashMap<String, Integer>(0) ;
        parsedSections = new ArrayList<SectionElementContext>(0)  ;

        // initialise the various section types for the document
        this.sectionTypesForDocumentType = DocumentSectionsContainer.getDocumentSectionsContainer();
    }

    /**
     * This method initializes a swing worker thread and
     * uses it to number the document
     * @return returns the fileName of the saved document
     */
    public boolean numberDocument() {
        boolean numberingOK = false ;

        // get the document sections names that need to be renumbered
        ArrayList <String> numberedContainers = applyNumberingToContainers (odfDocument) ;

        // now save the document
        try {
            // save the document
            odfDocument.save(filePath);
        } catch (Exception ex) {
            log.error(ex);
        }
        numberingOK = true ;


        return numberingOK ;
    }

    /**
     * This method searches for all the containers who section
     * meta-data as stored in the RDF file indicates that they are to have
     * a numbering scheme applied
     * @param odfDoc
     * @return
     */
    private ArrayList<String> applyNumberingToContainers(OdfDocument odfDoc) {
        ArrayList<String> nContainers = new ArrayList<String>(0) ; // stores the numbered containers

        // loop through each of the document sections
        // reviewing the metadata and finding out if the sections require numbering
       sectionHelper = new BungeniOdfSectionHelper(odfDoc) ;

        // get the sections
        NodeList docSections = sectionHelper.getDocumentSections() ;

        // get the root  section
        TextSectionElement currSection = (TextSectionElement) docSections.item(0) ;

        // get the section type
        String sectionType = sectionHelper.getSectionType(currSection) ;

        // start from the root element
        if ( sectionType.equals("body") ) {
            markSection(currSection,"",0) ; // parent section marked as ""
        }

        return nContainers ;
    }

    /**
     * Recursive method that parses through the document sections, numbering
     * the document. It reviews earlier sections in the document to determine the
     * number with which to mark the document
     * @param sections
     * @param parentSectionName
     * @return
     */
    private SectionElementContext markSection (TextSectionElement section,
            String parentSectionName, int numberingStartIndex) {

        SectionElementContext cSection = null ;
        int currSectionIndex = numberingStartIndex ;

        // determine if the section has already been numbered
        SectionElementContext similarSection
                = findSectionElemContextFromName(section.getTextNameAttribute()) ;

        if (similarSection == null) {

            // create SectionElementContext Object
            SectionElementContext newSectionElementContext
                    = new SectionElementContext(section.getTextNameAttribute(),
                    sectionHelper.getSectionType(section), parentSectionName,
                            currSectionIndex) ;

            // add this to the ArrayList with all the SectionElementContext
            // objects
            parsedSections.add(newSectionElementContext) ;

            // mark the current section
            if (!newSectionElementContext.getSectionType().equals("body"))
                addNumbering(section, currSectionIndex);

            // recurse for the sections children and add marking to
            // the children as well
            ArrayList<TextSectionElement> cSections
                    = sectionHelper.getChildSections(section) ;

            if ( cSections.size() > 0 ) {
                // loop through each of the child sections
                // marking them up as well
                int initIndex = 1 ;

                for ( TextSectionElement elem : cSections ) {
                    markSection(elem, section.getTextNameAttribute(),initIndex++) ;
                }
            }

            // increment the numbering start Index
            currSectionIndex ++ ;
        }

        return cSection ;
    }

    /**
     * This method determines if there exists a section with a similar type
     * already added to the ArrayList with all sections. The returned section
     * is the one with the largest section counter value
     * @param sectionType
     * @return
     */
    private SectionElementContext findSectionElemContextFromType(String sectionType) {
        SectionElementContext section = null ;
        int sectionCounterValue = 0 ;

        for ( SectionElementContext sectionElem : parsedSections ) {
            if ( sectionType.equals(sectionElem.getSectionType())) {

                // check if the section value is greater than the
                // largest section value
                if( sectionElem.getSectionCounter() > sectionCounterValue ) {
                    section = sectionElem ;
                }
            }
        }

        return section ;
    }

    /**
     * This method determines if there exists a section with the defined section
     * name and if found, this is returned
     * @param sectionType
     * @return
     */
    private SectionElementContext findSectionElemContextFromName(String sectionName) {
        SectionElementContext section = null ;

        for ( SectionElementContext sectionElem : parsedSections ) {
            if ( sectionName.equals(sectionElem.getSectionName())) {

                // check if the section value is greater than the
                // largest section value
                section = sectionElem ;
                break ;
            }
        }

        return section ;
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

            // set the section counter value and pass to addNumbering
            int currSectionNumber = sectionCounter ++ ;

            // add the Node to section type
            if (!addNumbering(section, currSectionNumber))
            {
                numberingOK = false ;
                break ;
            }
        }

        return numberingOK ;
    }

    /**
     * This method finds the first text node to a section
     * and adds the numbering to it
     * @param section
     * @param decoratorType
     */
    private boolean addNumbering(TextSectionElement section, int sectionNumber) {
        Node elemNode = section.getFirstChild() ;
        String nodeText = elemNode.getTextContent() ;        
       
        // format the output number
        String formattedNo = formatNumber(section, sectionNumber) ;

        // if the section starts with a similar numbering scheme,
        // remove the numbering string
        if (isDocumentNumbered) {

            // remove earlier formatting
            String [] nodeTextTokens = nodeText.split("\\s", 1) ;
            nodeText.replace(nodeText, nodeText.substring(nodeTextTokens[0].length()));
        }

        elemNode.setTextContent(formattedNo + " "
                + nodeText) ;

        return true ;
    }

    /**
     * This method gets the numbering scheme for a particular
     * section and formats the given number to the desired
     * numbering scheme
     * @param section
     * @return
     */
    private String formatNumber(TextSectionElement section, int sectionNumber) {
        String formattedNumber = null ;
        
        // get the numbering scheme
        String numberingSchemeForType
                = this.sectionTypesForDocumentType.get(sectionHelper.getSectionType(section))
                    .getNumberingScheme();

        String numberDecoratorForType
                = this.sectionTypesForDocumentType.get(sectionHelper.getSectionType(section))
                    .getNumberDecorator();

        // convert the number to desired numbering scheme
        IGeneralNumberingScheme inumScheme = null;
        inumScheme = NumberingSchemeFactory.getNumberingScheme(numberingSchemeForType);

        // @TODO : refactor API to add functionality to generate
        // decorated number
        inumScheme.setRange(new NumberRange((long) sectionNumber, (long) sectionNumber));
        inumScheme.generateSequence();

        ArrayList<String> seq = inumScheme.getGeneratedSequence();
        Iterator<String> iter = seq.iterator();
        
        while (iter.hasNext()) {
            formattedNumber = iter.next().toString() ;
        }
        // END/@TODO

        // decorate the number with the decorator scheme
        INumberDecorator idecScheme = null;
        if (!numberDecoratorForType.equals("none")) {
            idecScheme = NumberDecoratorFactory.getNumberDecorator(numberDecoratorForType);
            formattedNumber = idecScheme.decorate(formattedNumber) ;
        }

        return formattedNumber ;
    }

    /**
     * Test the code
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String odfDocPath = "c:/text.odt" ;
        NumberingAgent nAgent = new NumberingAgent(odfDocPath, false) ;

        if ( nAgent.numberDocument() ) {
            JOptionPane.showMessageDialog(null, "Document " + odfDocPath
                    + " was numbered OK" , "Numbering Completed" , JOptionPane.OK_OPTION);
        }
        else {
            JOptionPane.showMessageDialog(null, "There was an error marking up the Document " + odfDocPath
                     , "Error Numbering Document" , JOptionPane.ERROR_MESSAGE);
        }
    }
}