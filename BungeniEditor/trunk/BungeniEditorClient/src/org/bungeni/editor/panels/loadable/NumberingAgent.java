/*
 *  Copyright (C) 2012 UNDESA
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
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
import org.bungeni.editor.BungeniEditorClient;
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

/**
 * This class numbers/renumbers the various sections of an ODF document (bill/judgement) based on the
 * decorators and numbering scheme assigned for the section in the relevant documents type's XML
 * file definitions. The document is numbered/renumbered based on the metadata for the document
 *
 * <ol> The ***** CODE LOGIC ***** for the class is as follows:
 *  <li>The main 'body' element of the document is extracted as a TextSectionElement.</li>
 *  <li>The top level children for the document are determined from the 'body' element.</li>
 *  <li>Each of these children and formatted based on the type of section.</li>
 *  <li>The children for each of the top level  children are recursively formatted
 * based on their type. </li>
 * </ol>
 * 
 * @author Reagan Mbitiru <reaganmbitiru@gmail.com> (march, 2012)
 */
public final class NumberingAgent {

    // stores a copy of the cloned document to be numbered/renumbered
    private OdfDocument odfDocument = null ;

    // output logs
    private static Logger log = Logger.getLogger(NumberingAgent.class.getName());

    // Allows for the extraction of the various sections in the OdfDocument
    private BungeniOdfSectionHelper sectionHelper ;

    // stores the path to the document to be numbered/renumbered
    private String filePath ;

    // stores the value with which a section should be numbered with...
    private int sectionCounter = 0 ;

    // stores all the sections that have been numbered
    private ArrayList<SectionElementContext> parsedSections ;

    // flag to indicate if the document was earlier numbered or not
    private boolean isDocumentNumbered = false ;

    // stores the various document type's section names, numbering
    // schemes and decorator types
    private HashMap<String, DocumentSection> sectionTypesForDocumentType;

    /**
     * This constructor initializes the OdfDocument to be
     * renumbered and whether or not the document was initially numbered.
     * @param filePath path to the file to be numbered/renumbered
     * @param wasDocumentNumbered status on whether the document was earlier
     * numbered/renumbered
     */
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

    /**
     * This constructor initializes the OdfDocument to be
     * renumbered and whether or not the document was initially numbered.
     * @param odfDoc file to be numbered/renumbered
     * @param filePath path to the file to be numbered/renumbered
     * @param wasDocumentNumbered status on whether the document was earlier
     * numbered/renumbered
     */
    public NumberingAgent(OdfDocument odfDoc, String filePath, boolean wasDocumentNumbered) {
        try {            
            odfDocument = odfDoc ;
            this.filePath = filePath ;
            isDocumentNumbered = wasDocumentNumbered ;

            init() ;

        } catch (Exception ex) {
            log.error(ex);
        }
    }

    /**
     * This constructor initializes the OdfDocument to be
     * renumbered and whether or not the document was initially numbered.
     * @param file file object of the file to be numbered/renumbered
     * @param wasDocumentNumbered status on whether the document was earlier
     * numbered/renumbered
     */
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

    /**
     * This method initializes the HashMap storing the sections that
     * will be numbered and obtains the section types' numbering schemes and
     * decorators
     */
    private void init() {             
        parsedSections = new ArrayList<SectionElementContext>(0)  ;

        // initialise the various section types for the document
        this.sectionTypesForDocumentType = DocumentSectionsContainer.getDocumentSectionsContainer();
    }
    
    /**
     * This method numbers/renumbers the document
     * @return success of numbering/renumbering the document
     */
    public boolean numberDocument() {
        boolean numberingOK = true ;

        // get the document sections names that need to be renumbered
        boolean numberedContainersOK = applyNumberingToContainers (odfDocument) ;

        if (numberedContainersOK) {            
            try {
                // save the document with the numbering
                // or renumbering changes
                odfDocument.save(filePath);

            } catch (Exception ex) {
                log.error(ex);
            }
        }
        
        return numberingOK ;
    }

    /**
     * This method finds the root element of the document and uses
     * this element as the starting point to numbering/renumbering
     * a document
     * @param odfDoc document to be numbered
     * @return status of the numbering
     */
    private boolean applyNumberingToContainers(OdfDocument odfDoc) {
        boolean numberingOK = false ;

        // create the BungeniSectionHelper object
        sectionHelper = new BungeniOdfSectionHelper(odfDoc) ;
       
        // get the root  section
        TextSectionElement currSection = (TextSectionElement) sectionHelper.getDocumentSections().item(0);

        // get the root section type
        String sectionType = sectionHelper.getSectionType(currSection) ;

        // start from the root element to number/mark the various sections
        // of the document
        if ( sectionType.equals("body") ) {
            markSection(currSection,"",0) ; // parent section marked as ""
            numberingOK = true ;
        }

        return numberingOK ;
    }

    /**
     * Recursive method that numbers/renumbers a TextSectionElement
     * and any TextSectionElement children that the section has
     * @param section 'root' section for the current iteration that is being numbered
     * @param parentSectionName name of the parent section of the current section
     * @return status of the numbering/renumbering process
     */
    private void markSection (TextSectionElement section,
            String parentSectionName, int numberingStartIndex) {

        // intialize vars
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
            // objects that have been parsed
            parsedSections.add(newSectionElementContext) ;

            // add numbering to the current section
            if (!newSectionElementContext.getSectionType().equals("body"))
                addNumbering(section, currSectionIndex);

            // recurse through the cildren for the current section and
            // number them as well
            ArrayList<TextSectionElement> cSections
                    = sectionHelper.getChildSections(section) ;

            if ( cSections.size() > 0 ) {
                
                int initIndex = 1 ;

                for ( TextSectionElement elem : cSections ) {
                    markSection(elem, section.getTextNameAttribute(),initIndex++) ;
                }
            }

            // increment the numbering Index
            currSectionIndex ++ ;
        }
    }

    /**
     * This method determines if there exists a section with a similar type
     * already added to the ArrayList with all parsed sections. The returned section
     * is the one with the largest section counter value
     * @param sectionType type of the section
     * @return SectionElementContext for a section with a similar type
     */
    private SectionElementContext findSectionElemContextFromType(String sectionType) {
        SectionElementContext section = null ;
        int sectionCounterValue = 0 ;

        for ( SectionElementContext sectionElem : parsedSections ) {
            if ( sectionType.equals(sectionElem.getSectionType())) {

                // check if the section value is greater than the
                // largest section value...pick this instead
                if( sectionElem.getSectionCounter() > sectionCounterValue ) {
                    section = sectionElem ;
                }
            }
        }

        return section ;
    }

    /**
     * This method determines if there exists a section with the defined section
     * name parsed (numbered) and if found, the SectionElementContext for this section is returned
     * @param sectionName name of the current section
     * @return instance of the section, if initially numbered and added to numbered ArrayList store
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
     * This method actually applies the numbering/renumbering to a section in the document
     * It determines and applies the numbering scheme and decorator to use on the section
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
     * and adds the numbering to it. This method is able to number/renumber a
     * document. In the case of renumbering a document, this method assumes that
     * whitespace (\s) existed between the number applied earlier to the section
     * and the rest of the text in the section
     * @param section section to be numbered
     */
    private boolean addNumbering(TextSectionElement section, int sectionNumber) {
        
        String nodeText = section.getFirstChild().getTextContent() ;
       
        // format the output number
        String formattedNo = formatNumber(section, sectionNumber) ;

        // if the section starts with a similar numbering scheme,
        // remove the numbering string
        if (isDocumentNumbered) {

            // remove earlier formatting
            String [] nodeTextTokens = nodeText.split("\\s", 2) ;
            nodeText = nodeTextTokens[1];
        }

        section.getFirstChild().setTextContent(formattedNo + " "
                + nodeText) ;

        return true ;
    }

    /**
     * This method uses the numbering scheme and decorators for a particular
     * section to format a section.
     * @param section TextSectionElement to be numbered
     * @param sectionNumber number to be prefixed to the section
     * @return The numbered section
     */
    private String formatNumber(TextSectionElement section, int sectionNumber) {
        String formattedNumber = null ;
        
        // get the numbering scheme for the section type
        String numberingSchemeForType
                = this.sectionTypesForDocumentType.get(sectionHelper.getSectionType(section))
                    .getNumberingScheme();

        // get the decorator for the section type
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

        // decorate the number
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
    public static void not_main(String[] args) {
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