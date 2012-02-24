/*
 * sectionNumbererPanel.java
 *
 * Created on March 27, 2008, 6:39 PM
 */
package org.bungeni.editor.panels.loadable;

import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XEnumeration;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.container.XNameAccess;
import com.sun.star.container.XNamed;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextField;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextSection;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.UnoRuntime;
import java.awt.Color;
import java.awt.Cursor;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;

import org.bungeni.editor.document.DocumentSection;
import org.bungeni.editor.document.DocumentSectionsContainer;
import org.bungeni.editor.panels.impl.BaseClassForITabbedPanel;
import org.bungeni.editor.providers.DocumentSectionIterator;
import org.bungeni.editor.providers.DocumentSectionProvider;
import org.bungeni.editor.providers.IBungeniSectionIteratorListener;
import org.bungeni.numbering.impl.IGeneralNumberingScheme;
import org.bungeni.numbering.impl.INumberDecorator;
import org.bungeni.numbering.impl.NumberDecoratorFactory;
import org.bungeni.numbering.impl.NumberRange;
import org.bungeni.numbering.impl.NumberingSchemeFactory;
import org.bungeni.editor.numbering.ooo.OOoNumberingHelper;

import org.apache.log4j.Logger;
import org.bungeni.editor.noa.DocumentComposition;
import org.bungeni.editor.panels.loadable.refmgr.referenceManager;
import org.bungeni.extutils.CommonFileFunctions;
import org.bungeni.ooo.ooDocMetadata;
import org.bungeni.ooo.ooQueryInterface;
import org.bungeni.utils.BungeniBNode;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.bungeni.extutils.FrameLauncher;
import org.bungeni.extutils.MessageBox;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.section.BungeniOdfSectionHelper;

import org.bungeni.odfdom.utils.BungeniOdfFileCopy;
import org.bungeni.ooo.OOComponentHelper;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.element.text.TextParagraphElementBase;
import org.odftoolkit.odfdom.dom.element.text.TextSectionElement;
import org.odftoolkit.simple.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.odftoolkit.simple.text.Paragraph;
import org.odftoolkit.simple.text.Section;

/**
 * This is the panel that does numbering / renumbering of bill elements
 * AH-23-11-01 -- The code in this class is an urgent candidate for a full rewrite.
 * Most of this code was written for openoffice 2.4 where character level styling support was poor.
 * OOo 3 has much improved character level styling and API support -- and the below method using
 * named references needs to be rewritten and simplified using character styles.
 * @author  Ashok Hariharan
 * @rewritecandidate
 */
/*
 * (rm, feb 2012) - This class is being rewritten to
 * 1. Perform the bill renumbering offScreen and display the numbered bill in an altertaive pane
 * 2. Utilise BungeniOdfDom to perform renumbering. This lib takes advantage of oOo 3.3 functionality
 * such as char style styling to perform the markup of documents
 *
 * The algorithm to perform bill numbering is
 * 1. A copy of the initial bill oOo document is cloned. The created file has a datetime String
 * appended to the file name
 * 2. The cloned doc is parsed and its sections obtained
 * 3. If a section is marked as having numbered children, then get the descedents and add the numbering
 * as defined in the decorators
 * 4. Open a new tabbed pane instance and add the marked up bill
 * 
 */
public class sectionNumbererPanel extends BaseClassForITabbedPanel {

    private static org.apache.log4j.Logger log = Logger.getLogger(sectionNumbererPanel.class.getName());
    private boolean m_useParentPrefix = false;
    private IGeneralNumberingScheme m_selectedNumberingScheme;
    private ArrayList<String> sectionTypeMatchedSections = new ArrayList<String>();
    private ArrayList<String> sectionTypeMatchedSectionsMissingNumbering = new ArrayList<String>();
    private ArrayList<String> sectionHierarchy = new ArrayList<String>();
    private int headCount = 1;
    private String numParentPrefix = "";
    private ArrayList<String> sectionTypesInDocument = new ArrayList<String>();
    private String selectSection = "";
    private DefaultMutableTreeNode sectionRootNode = null;
    private String[] m_validParentSections;
    private String selectedNodeName = "";
    private TreeMap<String, sectionHeadingReferenceMarks> refMarksMap = new TreeMap<String, sectionHeadingReferenceMarks>();
    private ArrayList<Object> refMarksInHeadingMatched = new ArrayList<Object>(0);
    private ArrayList<Object> refMarksForHeading = new ArrayList<Object>(0);
    private HashMap<String, DocumentSection> sectionTypesForDocumentType = new HashMap<String, DocumentSection>();
    private HashMap<String, String> defaultSectionMetadata = new HashMap<String, String>();
    private static String NUMBER_SPACE = " ";
    private static String PARENT_PREFIX_SEPARATOR = ".";
    private ooDocMetadata documentMetadata;
    private Timer timerSectionTypes;
    private DocumentComposition documentComposition ;

    private OdfDocument odfDocument ; // stores the copy of the bill  (rm, feb 2012)
    private OdfDocument odfCopyDocument ; // stores the copy of the original bill document

    private static String MARKED_FOR_RENUMBERING = "RENUMBERING...";

    /** Creates new form sectionNumbererPanel */
    public sectionNumbererPanel() {
        initComponents();
    }

    private void init() {
        //initComponents();
        initSectionTypesMap();
        //get all section types for the document tyep
        this.sectionTypesForDocumentType = DocumentSectionsContainer.getDocumentSectionsContainer();
        /*init parent prefix checkbox */
        checkbxUseParentPrefix.setSelected(false);
        m_useParentPrefix = false;
        checkbxUseParentPrefix.addActionListener(
                new java.awt.event.ActionListener() {

                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        javax.swing.AbstractButton btn = (javax.swing.AbstractButton) evt.getSource();
                        m_useParentPrefix = btn.getModel().isSelected();
                    }
                });

        documentMetadata = new org.bungeni.ooo.ooDocMetadata(ooDocument);
    //all commented below ... not required ??
    //checkbxUseParentPrefix.addItemListener(new ParentSchemeListener());
    //packReferences();
    //initTree();
    //initSectionTree();
    //initNumberingSchemesCombo();
    // initTimer();
    //the following is commented becuase its definitely not required !
    //findBrokenReferences();
    }

    private void refreshSectionTypesList() {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                //String selectedItem = (String) listSectionTypes.getSelectedValue();
                initSectionTypesMap();
            //initSectionTypesListBox();
            //if (selectedItem != null )
            ///  listSectionTypes.setSelectedValue(selectedItem, true);
            }
        });
    }

    class numberingSchemeSelection extends Object {

        String schemeName;
        String schemeDesc;
        String schemeClass;

        public numberingSchemeSelection(String name, String desc, String strClass) {
            this.schemeName = name;
            this.schemeDesc = desc;
            this.schemeClass = strClass;
        }

        public String toString() {
            return schemeName;
        }
    }

    /*
     *gets the selected numbering scheme in the numbering scheme combo box 
     */
    /*
     *create a numbering scheme object from the selected scheme 
     */
    /* private IGeneralNumberingScheme createSchemeFromSelection() {
    numberingSchemeSelection schemeSelection = getSelectedNumberingScheme();
    IGeneralNumberingScheme inumScheme = NumberingSchemeFactory.getNumberingScheme(schemeSelection.schemeName);
    return inumScheme;
    }*/
    /*
     *initiliazes the numbering scheme combo by setting values in it
     */
    /*
    private void initNumberingSchemesCombo(){

    Iterator<String> schemeIterator = NumberingSchemeFactory.numberingSchemes.keySet().iterator();
    numberingSchemeSelection[] sels = new numberingSchemeSelection[NumberingSchemeFactory.numberingSchemes.size()];
    int i = 0;
    while (schemeIterator.hasNext()) {
    String aScheme = schemeIterator.next();
    sels[i] = new numberingSchemeSelection(aScheme, "", NumberingSchemeFactory.numberingSchemes.get(aScheme));
    i++;
    }
    this.cboNumberingScheme.setModel(new DefaultComboBoxModel(sels));
    }
     */
    private void initSectionTypesMap() {
        this.sectionTypesInDocument.clear();
        XNameAccess docSections = ooDocument.getTextSections();
        String[] sectionNames = docSections.getElementNames();
        for (String aSection : sectionNames) {
            HashMap<String, String> sectionMeta = ooDocument.getSectionMetadataAttributes(aSection);
            if (sectionMeta.containsKey("BungeniSectionType")) {
                String sectionType = sectionMeta.get("BungeniSectionType");
                if (!this.sectionTypesInDocument.contains(sectionType)) {
                    this.sectionTypesInDocument.add(sectionType);
                }
            }
        }
        if (sectionTypesInDocument.size() > 0) {
            Collections.sort(sectionTypesInDocument);
        }
    }

    // (rm, feb 2012) - deprecating method, it's unused
    /*
    private void applyNumberingScheme() {
        m_bFoundHeading = false;
        /*  if (listSectionTypes.getSelectedIndex() == -1 ) {
        MessageBox.OK(parentFrame, "Please select a section type to apply numbering upon !");
        return;
        }
        
        String sectionType= listSectionTypes.getSelectedValue().toString();            */
    /*
        String sectionType = "";
        ////find all sections matching that section type, and populate arraylist
        ///was called readSection()
        initNumbering();
        buildArrayOfSectionsMatchingType(sectionType);
        System.out.println("matched sections =  " + this.sectionTypeMatchedSections);
        //findSectionsMatchingSectionType(sectionType);
        // iterate through arraylist and set numberingscheme metadata to matching sections
        // was called applyNumberingScheme()
        if (checkIfSectionsHaveNumberingScheme() == true) {
            MessageBox.OK(parentFrame, bundle.getString("section_type_has_num_scheme"));
            return;
        }
        //setNumberingSchemeMetadataIntoMatchingSections();
        ///why is the above being done...when the same section is iterated over again ??? 
        this.IterateSectionTypesForNumberedHeadings();
    /*
    matchHeadingsInTypedSections();
    if (! m_bFoundHeading ) {
    MessageBox.OK(parentFrame, "No headings were found to apply numbering upon !");
    return;
    }
     *//*
    }
    */
    
    private void initNumbering() {

        this.sectionTypeMatchedSections.clear();
        this.sectionTypeMatchedSectionsMissingNumbering.clear();

    }

    private void buildArrayOfSectionsMatchingType(String sectionType) {

        BungeniBNode bNode = DocumentSectionProvider.getTreeRoot();
        recurseNodes(bNode, sectionType);
    }

    private void recurseNodes(BungeniBNode theNode, String filterSectionType) {
        BungeniBNode theBNode = theNode;
        String sectionName = theBNode.getName();
        String sectionType = ooDocument.getSectionType(sectionName);
        if (sectionType != null) {
            if (sectionType.equals(filterSectionType)) {
                this.sectionTypeMatchedSections.add(sectionName);
            }
        }
        if (theBNode.hasChildren()) {
            TreeMap<Integer, BungeniBNode> children = theBNode.getChildrenByOrder();
            Iterator<Integer> childIterator = children.keySet().iterator();
            while (childIterator.hasNext()) {
                Integer nodeKey = childIterator.next();
                BungeniBNode childNode = children.get(nodeKey);
                recurseNodes(childNode, filterSectionType);
            }
        }

    }

 
    // (rm, feb 2012) -refactored to use Simple OO API
    private ArrayList<String> findNumberedContainers(OdfDocument ooDoc) {
         log.debug("findNumberedContainers : starting ");
        findNumberedContainersListener findNumberedContainers = new findNumberedContainersListener(ooDoc);
        //AH-23-01-11 removed this call since ignoreTheseSections is now a private member
        //DocumentSectionProvider.SectionTree.ignoreTheseSections = new ArrayList<String>(0);
        //AH-02-0-11 - added extra ignorethese parameter to reset the ignore sections array for the
        //section iterator
        String[] ignoreThese = {};
        DocumentSectionIterator iterateNumberedContainers = new DocumentSectionIterator(findNumberedContainers, ignoreThese);
        iterateNumberedContainers.startIterator();
        log.debug("findNumberedContainers : returning numbered containers : " + findNumberedContainers.numberedContainers.toString());
        return findNumberedContainers.numberedContainers;
    }

    // (rm, feb 2012) - refactored code to allow class to use more than just the ooDocument
    //  declared globally for this class
    class findNumberedContainersListener implements IBungeniSectionIteratorListener {

        ArrayList<String> numberedContainers = new ArrayList<String>(0);
        OdfDocument currOoDocument = null ;

        private findNumberedContainersListener (OdfDocument ooDoc)    {
            currOoDocument = ooDoc ;
        }

        public boolean iteratorCallback(BungeniBNode bNode) {
            String sectionName = bNode.getName();

            if (hasNumberingScheme(sectionName, currOoDocument))
                numberedContainers.add(sectionName);
                        
            return true;
        }
    }

    /**
     * (rm, feb 2012) - This method determines from bill.xml if the section has a numbering scheme
     * attached to it.
     * Refactoring the code to use bungeniodfdom (!+REASON : faster processing of 
     * document in the off screen buffer)
     * @param sectionName
     * @return
     */
    private boolean hasNumberingScheme(String sectionName, OdfDocument ooDoc) {
        
        boolean hasNumberingScheme = false ;

            String sectionType = null ;
            
            // get the section Type
            if (sectionName.equals("bill")) {                 
                 hasNumberingScheme =  false;
            }

            // get the section helper
            BungeniOdfSectionHelper sectionHelper = new BungeniOdfDocumentHelper(ooDoc).getSectionHelper();

            
        return hasNumberingScheme ;
    }        

    /**
     * (rm, feb 2012) - this method parses through the numbered containers and appends the
     * numbering scheme to the initial paragraph depending on the numbering
     * scheme added to the metadata
     * @param numberedContainers
     * @param ooDoc
     */
    // (rm, feb 2012) - refactoring method to allow any ooDoc object to be parsed
    //
    private void applyNumberingMarkupToNonNumberedContainers(ArrayList<String> numberedContainers,
            Document ooDoc, OdfDocument odfDoc) {

        // create a BungeniSectionHelper instance
        BungeniOdfSectionHelper odfDocSectionHelper = new BungeniOdfSectionHelper(odfDoc) ;

        log.debug("applyNumberingMarkupToNonNumberedContainers : starting");
        // loop through the identified containers, renumbering
        for (String containerSection : numberedContainers) {
            // get the TextSectionElement from the currentSection
            TextSectionElement currSectionElem = odfDocSectionHelper.getSection(containerSection) ;

            log.debug("applyNumberingMarkupToNonNumberedContainers : processing for : " + containerSection);            

            // check if the section should have a numberer metadata element applied
            if  ( !isSectionContainingAppliedNumber(currSectionElem, odfDocSectionHelper ) ) {
                log.debug("applyNumberingMarkupToNonNumberedContainers : " + containerSection + bundle.getString("no_applied_number"));
                this.markupNumberedHeading(containerSection, currSectionElem, MARKED_FOR_RENUMBERING, odfDocSectionHelper, ooDoc);
                
            } else {
                log.debug("applyNumberingMarkupToNonNumberedContainers : " + containerSection + bundle.getString("contains_applied_number"));
            }
        }
    }

    private boolean isSectionContainingAppliedNumber (TextSectionElement numberedSection,
                BungeniOdfSectionHelper odfDocSectionHelper)
    {
        boolean bState = false ;

        // get the section metadata attributes
        NodeList sectionAttributes = odfDocSectionHelper.getSectionMetadataAttributes(numberedSection) ;
        
        /** (ah, feb 2012) - obtains the NUMBERING SCHEME
         *
        String sectionNumberingScheme = odfDocSectionHelper.getSectionMetadataValue(numberedSection,
                OOoNumberingHelper.numberingMetadata.get("NUMBERING_SCHEME")) ;

        if ( sectionNumberingScheme.length() > 0 )
            bState = true ;
        // get the numbering keys to look for
        Set<String> numberingMeta = OOoNumberingHelper.numberingMetadata.keySet();
         * 
        **/
        
        // get the numbering keys to look for
        Set<String> numberingMeta = OOoNumberingHelper.numberingMetadata.keySet();

        // find out if any of the numbering metadata attributes are included
        // within the section
        for ( int sectionAttsCounter = 0 , sectionAtts = sectionAttributes.getLength() ;
                sectionAttsCounter < sectionAtts ; sectionAttsCounter ++ )
        {
            // get the section value
            Node sectionNode = sectionAttributes.item(sectionAttsCounter) ;
            
            // get the node attributes
            if ( sectionNode.hasAttributes() )
            {
                NamedNodeMap nodeAtts = sectionNode.getAttributes() ;
                
                // find out if the section Attribute Name is similar to any 
                // of the numbering metadata attribute names
                for ( int attsCounter = 0 , allAtts = nodeAtts.getLength() ;
                    attsCounter < allAtts ; attsCounter ++ )
                {
                    for (String numberMetaKey :  numberingMeta )
                    {
                        if ( numberMetaKey.equals(nodeAtts.item(attsCounter)))
                        {
                            bState = true ;
                            break ;
                        }
                    }
                }
            }
        }

        return bState ;
    }
    
    ///replace later with proper factory provider class
    ArrayList<String> validNumberedSectionTypes = new ArrayList<String>() {

        {
            add("Article");
            add("Clause");
        }
    };

    private void buildValidNumberingSectionTypes() {
    }
    
    private static final ResourceBundle bundle = ResourceBundle.getBundle("org/bungeni/editor/panels/loadable/Bundle");

    // (rm, feb 2012) - refactoring code to allow for any ooDoc to have numbering applied on
    // method refactored to use BungeniOdfDom lib
    private void reApplyNumberingOnNumberedContainers( OOComponentHelper ooDoc) {
        //now iterate through the numbered sections and apply 
        //       sectionRenumberingIteratorListener sril = new sectionRenumberingIteratorListener();
        //     DocumentSectionIterator sectionIterator = new DocumentSectionIterator(sril);
        //   sectionIterator.startIterator();
       /* for (String numberedSectionType : sril.numberTheseSectionTypes) {
        updateNumbersByType(numberedSectionType);
        }*/
        ArrayList<String> numberTheseSectionTypes = new ArrayList<String>();
        Iterator<String> sectionTypesIterator = this.sectionTypesForDocumentType.keySet().iterator();
        while (sectionTypesIterator.hasNext()) {
            String matchedSectionType = sectionTypesIterator.next();


            DocumentSection matchSection = this.sectionTypesForDocumentType.get(matchedSectionType);
            if (!matchSection.getNumberingScheme().equalsIgnoreCase("none")) {
                //its a numbering scheeme section type
                numberTheseSectionTypes.add(matchedSectionType);
            }
        }
        for (String sectionType : numberTheseSectionTypes) {
            updateNumbersByType(sectionType, ooDoc);
        }
    }

    private void reApplyNumberingOnNumberedContainers( BungeniOdfSectionHelper ooDocSecHelper ) {
         ArrayList<String> numberTheseSectionTypes = new ArrayList<String>();
        Iterator<String> sectionTypesIterator = this.sectionTypesForDocumentType.keySet().iterator();
        while (sectionTypesIterator.hasNext()) {
            String matchedSectionType = sectionTypesIterator.next();

            // add the sections with a numbering scheme to the ArrayList container for
            // all objects with numbering schemes
            DocumentSection matchSection = this.sectionTypesForDocumentType.get(matchedSectionType);
            if (!matchSection.getNumberingScheme().equalsIgnoreCase("none")) {
                //its a numbering scheeme section type
                numberTheseSectionTypes.add(matchedSectionType);
            }
        }

        // now add the numbering to the various sections
        for (String sectionType : numberTheseSectionTypes) {
           // ----> updateNumbersByType(sectionType, ooDocSecHelper);
        }
    }
    /*
    class sectionRenumberingIteratorListener implements IBungeniSectionIteratorListener{
    ArrayList<String> numberTheseSectionTypes = new ArrayList<String>(0) ;
    //return tru to continue , false to break
    public boolean iteratorCallback(BungeniBNode bNode) {
    String sectionName = bNode.getName();
    log.debug("reApplyNumberingonNumberedContainers, iterator callback : "+ sectionName);
    String sectionType = ooDocument.getSectionType(sectionName);
    if (sectionType != null ) {
    if (validNumberedSectionTypes.contains(sectionType)) {
    //this type of section can be numbered
    if (!numberTheseSectionTypes.contains(sectionType)) { //has it been already numbered ?
    log.debug("reApplyNumberingonNumberedContainers, iterator numbering "+ sectionType);
    // renumberSectionType (sectionType);
    numberTheseSectionTypes.add(sectionType);
    }
    if (numberTheseSectionTypes.size() == validNumberedSectionTypes.size())
    return false;
    }
    }
    return true;
    }
    }
     */

    /**
     * This method inserts the numbering into the document as defined by the numbering scheme
     * @param sectionType
     * @param ooDoc
     */
    private void updateNumbersByType (String sectionType, OdfDocument ooDoc) {
        ArrayList<String> sectionsMatchingType = getSectionsMatchingType(sectionType);
            //no sections found matching type, so return

            if (sectionsMatchingType.isEmpty()) {
                return;
            }
        
        String numberingSchemeForType = this.sectionTypesForDocumentType.get(sectionType).getNumberingScheme();
        String numberDecoratorForType = this.sectionTypesForDocumentType.get(sectionType).getNumberDecorator();
        INumberDecorator numDecor = null;
        
        if (!numberDecoratorForType.equals("none")) {
            numDecor = NumberDecoratorFactory.getNumberDecorator(numberDecoratorForType);
        }

        this.initNumberingSchemeGenerator(numberingSchemeForType, 1, sectionsMatchingType.size());

        for (String matchingSection : sectionsMatchingType) {
                // XTextSection matchedSection = ooDoc.getSection(matchingSection);
                BungeniOdfSectionHelper sectionHelper = new BungeniOdfSectionHelper(ooDoc) ;
                TextSectionElement matchedSection = sectionHelper.getSection(matchingSection) ;

                // intialise the context sections
                String parentSectionName = "", prevParentSectionName = "";

                // XTextSection parentofMatchedSection = matchedSection.getParentSection();
                parentSectionName = matchedSection.getParentNode().getNodeName() ;
                // XNamed parentSec = ooQueryInterface.XNamed(parentofMatchedSection);
                // parentSectionName = parentSec.getName();               

                if (!parentSectionName.equals(prevParentSectionName)) {
                    this.m_selectedNumberingScheme.sequence_initIterator();
                }

                String theNumber = this.m_selectedNumberingScheme.sequence_next();
                long lBaseIndex = this.m_selectedNumberingScheme.sequence_base_index(theNumber);

                updateNumberInSection3(ooDoc, matchedSection, theNumber, numDecor, lBaseIndex);
                    
                prevParentSectionName = parentSectionName;
            }
    }
    
    // (rm, feb 2012) - this method has been refactored to use bungeniOdfDom lib
    private void updateNumbersByType(String sectionType, OOComponentHelper ooDoc) {
        try {
            ArrayList<String> sectionsMatchingType = getSectionsMatchingType(sectionType);
            //no sections found matching type, so return

            if (sectionsMatchingType.isEmpty()) {
                return;
            }

            String numberingSchemeForType = this.sectionTypesForDocumentType.get(sectionType).getNumberingScheme();
            String numberDecoratorForType = this.sectionTypesForDocumentType.get(sectionType).getNumberDecorator();
            INumberDecorator numDecor = null;
            if (!numberDecoratorForType.equals("none")) {
                numDecor = NumberDecoratorFactory.getNumberDecorator(numberDecoratorForType);
            }
            this.initNumberingSchemeGenerator(numberingSchemeForType, 1, sectionsMatchingType.size());
            //this.initializeNumberingSchemeGenerator(1, sectionsMatchingType.size());
            String parentSectionName = "", prevParentSectionName = "";
            //findSectionsMatchingSectionType(sectionType);
            ///why is the above being done...when the same section is iterated over again ???
            for (String matchingSection : sectionsMatchingType) {
                XTextSection matchedSection = ooDoc.getSection(matchingSection);
                XTextSection parentofMatchedSection = matchedSection.getParentSection();
                XNamed parentSec = ooQueryInterface.XNamed(parentofMatchedSection);
                parentSectionName = parentSec.getName();

                if (!parentSectionName.equals(prevParentSectionName)) {
                    this.m_selectedNumberingScheme.sequence_initIterator();
                }

               //
                //XTextSection numberedChild = ooDoc.getChildSectionByType(matchedSection, OOoNumberingHelper.NUMBERING_SECTION_TYPE);
                //if (numberedChild != null) {
                    String theNumber = this.m_selectedNumberingScheme.sequence_next();
                    long lBaseIndex = this.m_selectedNumberingScheme.sequence_base_index(theNumber);
                    // ooDoc.protectSection(numberedChild, false);
                    ooDoc.protectSection(matchedSection, true);
                    // ooDoc.protectSection(numberedChild, true);

                    updateNumberInSection3(ooDoc, matchedSection, theNumber, numDecor, lBaseIndex);
                    // updateNumberInSection3(ooDoc, numberedChild, theNumber, numDecor, lBaseIndex);

                    ////update the field here ooDocument.getTextFields();
                    //ooDoc.protectSection(numberedChild, true);
                    ooDoc.protectSection(matchedSection, false);
                    // ooDoc.protectSection(numberedChild, false);
               // }
                prevParentSectionName = parentSectionName;
            }
        } catch (Exception ex) {
            log.error("updateNumbersByType : " + ex.getMessage());
            log.error("updateNumbersByType : " + CommonExceptionUtils.getStackTrace(ex));
        }

    }

    /**
     * This method inserts the numbering to the sections marked as due for numbering
     * @param ooDoc
     * @param numberedChild
     * @param theNumber
     * @param numberDecorator
     * @param lNumBaseIndex
     */
    private void updateNumberInSection3(OdfDocument ooDoc, TextSectionElement numberedChild,
            String theNumber, INumberDecorator numberDecorator, long lNumBaseIndex) {
       
        // get the section metadata attributes for the section
        BungeniOdfSectionHelper sectionHelper = new BungeniOdfSectionHelper(ooDoc) ;
        NodeList sectionAttributes = sectionHelper.getSectionMetadataAttributes(numberedChild) ;

        String sectionUUID ;

        // get the section UUID
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

        // String thisRefMark = OOoNumberingHelper.NUMBER_REF_PREFIX + sectionUUID;
        if (numberDecorator != null) {
            theNumber = numberDecorator.decorate(theNumber);
        }

      

        //update the OOo Metadata witht the numbering value
        // documentMetadata.AddProperty(OOoNumberingHelper.META_PREFIX_NUMBER + sectionUUID, theNumber);
        //this.updateSectionNumberingMetadata(ooDoc, numberedChild, numberedParentType, theNumber, lNumBaseIndex);
    }

    // (rm, feb 2012- ADDED Comments : This method actually adds the numbering to the various parts
    // method refactored to use BungeniOdfDom lib instead
    private void updateNumberInSection3(OOComponentHelper ooDoc, XTextSection numberedChild, String theNumber, INumberDecorator numberDecorator, long lNumBaseIndex) {
        try {

            HashMap<String, String> childMeta = ooDoc.getSectionMetadataAttributes(numberedChild);
            // String sectionUUID = childMeta.get("BungeniSectionUUID");
            String sectionUUID = childMeta.get("BungeniSectionID");
            String thisRefMark = OOoNumberingHelper.NUMBER_REF_PREFIX + sectionUUID;
            if (numberDecorator != null) {
                theNumber = numberDecorator.decorate(theNumber);
            }
            XNameAccess refMarks = ooDoc.getReferenceMarks();
            if (refMarks.hasByName(thisRefMark)) {
                Object oRefMark = refMarks.getByName(thisRefMark);
                XTextContent oRefContent = ooQueryInterface.XTextContent(oRefMark);
                XTextRange refInternalRange = oRefContent.getAnchor();
                int nReferenceMarkLength = refInternalRange.getString().length();
                XTextCursor refCursor = ooDoc.getTextDocument().getText().createTextCursor();
                refCursor.gotoRange(refInternalRange.getEnd(), false);
                refCursor.setString(theNumber);
                refCursor.gotoRange(refInternalRange.getStart(), false);
                refCursor.goRight((short) nReferenceMarkLength, true);
                refCursor.setString("");
                XTextSection numberedParent = numberedChild.getParentSection();
                String numberedParentType = ooDoc.getSectionType(numberedParent);

                //update the OOo Metadata witht the numbering value
                documentMetadata.setOODocument(ooDoc);
                documentMetadata.AddProperty(OOoNumberingHelper.META_PREFIX_NUMBER + sectionUUID, theNumber);
                this.updateSectionNumberingMetadata(ooDoc, numberedChild, numberedParentType, theNumber, lNumBaseIndex);
            }
        } catch (NoSuchElementException ex) {
            log.error("updateNumberInSection - " + ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error("updateNumberInSection - " + ex.getMessage());
        } catch (NullPointerException ex) {
            log.error("updateNumberInSection - " + ex.getMessage());
            log.error("updateNumberInSection - " + CommonExceptionUtils.getStackTrace(ex));
        }

    }

    // (rm, feb 2012) - method deprecated since it is unused
    /**
    private void updateNumberInSection2(XTextSection numberedChild, String theNumber, INumberDecorator numberDecorator, long lNumBaseIndex) {
        try {

            HashMap<String, String> childMeta = ooDocument.getSectionMetadataAttributes(numberedChild);
            String sectionUUID = childMeta.get("BungeniSectionUUID");
            //   String fieldToUpdate= OOoNumberingHelper.NUM_FIELD_PREFIX + sectionUUID;
            //   XTextField aField = ooDocument.getTextFieldByName(fieldToUpdate);
            //    if (aField == null) {
            //        log.error("updateNumberInSection :field object found was null");
            //        return;
            //    }
            XTextRange numberRange = numberedChild.getAnchor();
            String fullHeading = numberRange.getString();
            if (numberDecorator != null) {
                theNumber = numberDecorator.decorate(theNumber);
            }
            //the text is covered by a reference so we update just the number...
            //position cursor at start
            XTextCursor refHeadCursor = ooDocument.getTextDocument().getText().createTextCursor();
            refHeadCursor.gotoRange(numberRange.getStart(), true);
            refHeadCursor.goRight((short) 0, false);
            //first find boundary of number 
            int nStartNum = fullHeading.indexOf(OOoNumberingHelper.NUMBERED_PREFIX);
            int nEndNum = fullHeading.indexOf(OOoNumberingHelper.NUMBERED_SUFFIX);
            //first we go to endnum, then we insert the new number there,
            refHeadCursor.goRight((short) nEndNum, false);

            //then we move left = length of new number
            //then we move left = endnum -1 
            //we setstring(0) for that range
            //found index is 1 less than cursor movement index... 
            refHeadCursor.goRight((short) (nStartNum + 1), false);
            refHeadCursor.goRight((short) (nEndNum - (nStartNum + 1)), true);
            //update number over cursor
            refHeadCursor.setString(theNumber);
            /*
            String fullHeading = numberRange.getString();
            int nHeadStart = fullHeading.indexOf(NUMBER_HEADING_BOUNDARY);
            int nHeadEnd = fullHeading.lastIndexOf(NUMBER_HEADING_BOUNDARY);
            String headingText = fullHeading.substring(nHeadStart, nHeadEnd+1);
            String newNumber = NUMBERED_PREFIX + theNumber + NUMBERED_SUFFIX;
            numberRange.setString(newNumber + " " + headingText);
             */
    /*
            XTextSection numberedParent = numberedChild.getParentSection();
            String numberedParentType = ooDocument.getSectionType(numberedParent);
            this.updateSectionNumberingMetadata(numberedChild, numberedParentType, theNumber, lNumBaseIndex);
        } catch (NullPointerException ex) {
            log.error("updateNumberInSection - " + ex.getMessage());
            log.error("updateNumberInSection - " + CommonExceptionUtils.getStackTrace(ex));
        }

    }
    **/

    // (rm, feb 2012) - method deprecated since it is unused
    /*
    private void updateNumberInSection(XTextSection numberedChild, String theNumber, INumberDecorator numberDecorator, long lNumBaseIndex) {
        try {
            HashMap<String, String> childMeta = ooDocument.getSectionMetadataAttributes(numberedChild);
            String sectionUUID = childMeta.get("BungeniSectionUUID");
            String fieldToUpdate = OOoNumberingHelper.NUM_FIELD_PREFIX + sectionUUID;
            XTextField aField = ooDocument.getTextFieldByName(fieldToUpdate);
            if (aField == null) {
                log.error("updateNumberInSection :field object found was null");
                return;
            }
            XPropertySet aFieldSet = ooQueryInterface.XPropertySet(aField);
            if (numberDecorator != null) {
                theNumber = numberDecorator.decorate(theNumber);
            }
            aFieldSet.setPropertyValue("Content", theNumber);
            XTextSection numberedParent = numberedChild.getParentSection();
            String numberedParentType = ooDocument.getSectionType(numberedParent);
            this.updateSectionNumberingMetadata(numberedChild, numberedParentType, theNumber, lNumBaseIndex);
            ooDocument.refreshTextField(aField);
        } catch (UnknownPropertyException ex) {
            log.error(ex.getClass().getName() + " - " + ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error(ex.getClass().getName() + " - " + ex.getMessage());
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error(ex.getClass().getName() + " - " + ex.getMessage());
        } catch (PropertyVetoException ex) {
            log.error(ex.getClass().getName() + " - " + ex.getMessage());
        } catch (NullPointerException ex) {
            log.error("updateNumberInSection - " + ex.getMessage());
            log.error("updateNumberInSection - " + CommonExceptionUtils.getStackTrace(ex));
        }

    }
    */
    
    private ArrayList<String> getSectionsMatchingType(String sectionType) {
        //  ArrayList<String> sectionsMatchingType = new ArrayList<String>(0);
        sectionTypeIteratorListener typeIterator = new sectionTypeIteratorListener(sectionType, ooDocument);
        DocumentSectionIterator sectionTypeIterator = new DocumentSectionIterator(typeIterator);
        sectionTypeIterator.startIterator();
        return typeIterator.sectionsMatchingType;
    }

   
    private frameBrokenReferences2 brokenReferencesFrame = null;

    private void applyFixBrokenReferences() {
        /*
        this.orphanedReferences.clear();

        findBrokenReferences();
        
        if (this.orphanedReferences.size() > 0  ) {
        if (brokenReferencesFrame != null  ) {
        // if (brokenReferencesFrame.getLaunchedState()){
        brokenReferencesFrame.dispose();
        // }
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
        frameBrokenReferences2.LaunchMode mode = frameBrokenReferences2.LaunchMode.BrowseBroken;
        brokenReferencesFrame = frameBrokenReferences2.Launch(ooDocument,  parentFrame, orphanedReferences, mode);
        }
        });
        } else {
        MessageBox.OK(this, "No Broken references found !");
        }
         */
        launchReferenceManager("brokenReferences");
    }

    private void applyInsertCrossReferences() {
        /*
        this.orphanedReferences.clear();
        findBrokenReferences();
        if (brokenReferencesFrame != null  ) {
        if (brokenReferencesFrame.getLaunchedState()){
        brokenReferencesFrame.dispose();
        }
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
        frameBrokenReferences2.LaunchMode mode = frameBrokenReferences2.LaunchMode.CrossReferences;
        brokenReferencesFrame = frameBrokenReferences2.Launch(ooDocument,  parentFrame, orphanedReferences, mode);
        }
        });
         */
        launchReferenceManager("browseReferences");

    }

    private void applyInsertExternalReference() {
        launchReferenceManager("externalReferences");
    }

    private void launchReferenceManager(String sMode) {
        referenceManager mgr = new referenceManager();
        mgr.setLaunchMode(sMode);
        JFrame f = FrameLauncher.InitializeFrame(referenceManager.__TITLE__, mgr, referenceManager.FRAME_DIMENSION);

        mgr.setParentWindowHandle(f);
        mgr.setOOComponentHandle(ooDocument);
        mgr.initUI();

        FrameLauncher.LaunchFrame(f, true, true);
        FrameLauncher.CenterFrame(f);

    }

    // (rm, feb 2012) - deprecated as unused
    /**
    private boolean checkIfSectionsHaveNumberingScheme() {
        for (String matchedSection : sectionTypeMatchedSections) {
            //has child heading marked for numbering
            XTextSection numberedSection = ooDocument.getChildSectionByType(ooDocument.getSection(matchedSection), OOoNumberingHelper.NUMBERING_SECTION_TYPE);
            if (numberedSection != null) { // it has a numbered heading  so fail
                //check if section marked for numbering actually has a number 
                if (this.isSectionContainingAppliedNumber(numberedSection)) {
                    return true;
                }
            }
        }
        return false;
    }
    **/
    
    private void getParentFromSection(XTextRange aTextRange) {

        String prevParent = "";
        Iterator typedMatchSectionItr = sectionTypeMatchedSections.iterator();
        while (typedMatchSectionItr.hasNext()) {
            Object matchedSectionElem = typedMatchSectionItr.next();
            try {
                Object rootSection = ooDocument.getTextSections().getByName(matchedSectionElem.toString());
                XTextSection theSection = ooQueryInterface.XTextSection(rootSection);

                XNamed xParentSecName = ooQueryInterface.XNamed(theSection.getParentSection());
                String currentParent = (String) xParentSecName.getName();
                if (!currentParent.equalsIgnoreCase(prevParent)) {
                    //restart numbering here
                    headCount = 1;
                    System.out.println("different parent" + "testCount " + headCount);

                } else {
                    //continue numbering
                    headCount++;

                    System.out.println("same parent" + "testCount " + headCount);
                }
                prevParent = (String) xParentSecName.getName();

            } catch (NoSuchElementException ex) {
                ex.printStackTrace();
            } catch (WrappedTargetException ex) {
                ex.printStackTrace();
            }


        }


    }
    private ArrayList<XTextField> orphanedReferences = new ArrayList<XTextField>();

    class BrokenRefsAgent extends SwingWorker<Object, Void> {

        @Override
        protected Object doInBackground() throws Exception {
            throw new UnsupportedOperationException(bundle.getString("not_supported_yet"));
        }

        @Override
        protected void done() {
        }
    }

    private void findBrokenReferences() {
        try {
            XTextDocument oDoc = ooDocument.getTextDocument();
            //get reference mark objects
            XNameAccess refMarks = ooDocument.getReferenceMarks();
            //get text fields
            XEnumerationAccess refFields = ooDocument.getTextFields();
            XEnumeration fieldEnum = refFields.createEnumeration();
            while (fieldEnum.hasMoreElements()) {
                Object objField = fieldEnum.nextElement();
                XServiceInfo servInfo = ooDocument.getServiceInfo(objField);
                if (servInfo.supportsService("com.sun.star.text.TextField.GetReference")) {
                    /*
                    if oTextField.ReferenceFieldSource = com.sun.star.text.ReferenceFieldSource.REFERENCE_MARK then
                    if not oRefMarks.hasByName(oTextField.Sourcename) then
                    nCount = nCount + 1
                    subOrphan(oTextField)
                    end if
                    else*/
                    XTextField foundField = ooQueryInterface.XTextField(objField);
                    XPropertySet propSet = ooDocument.getObjectPropertySet(foundField);
                    short refFieldSource = AnyConverter.toShort(propSet.getPropertyValue("ReferenceFieldSource"));
                    String refSourceName;
                    refSourceName = AnyConverter.toString(propSet.getPropertyValue("SourceName"));
                    switch (refFieldSource) {
                        case com.sun.star.text.ReferenceFieldSource.REFERENCE_MARK:
                            if (!refMarks.hasByName(refSourceName)) {
                                //this is a dead reference
                                addOrphanedField(foundField);
                            }
                            break;

                        case com.sun.star.text.ReferenceFieldSource.BOOKMARK:

                            break;

                        case com.sun.star.text.ReferenceFieldSource.SEQUENCE_FIELD:

                            break;


                        case com.sun.star.text.ReferenceFieldSource.FOOTNOTE:

                            break;

                        case com.sun.star.text.ReferenceFieldSource.ENDNOTE:

                            break;



                    }

                }
            }
        } catch (WrappedTargetException ex) {
            log.error(ex.getClass().getName() + " - " + ex.getMessage());
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error(ex.getClass().getName() + " - " + ex.getMessage());
        } catch (UnknownPropertyException ex) {
            log.error(ex.getClass().getName() + " - " + ex.getMessage());
        } catch (com.sun.star.container.NoSuchElementException ex) {
            log.error(ex.getClass().getName() + " - " + ex.getMessage());
        }
    }

    private void addOrphanedField(XTextField field) {
        orphanedReferences.add(field);
    }


    // was
    //private void insertNumberOnRenumbering(XTextRange aTextRange, int testCount, Object elem, Object refMark){
    //was
    //private ArrayList getNumberReferenceFromHeading(Object elem){
//removed removeNumberFromHeading();
    //Returns 1st heading in the section
    //      was private Object getNumberedHeadingsInsertCrossRef(String results) ;
    //was 
    //private ArrayList<String> getReferenceMarksOnCross(Object elem);
    //method to get reference mark from heading when renumbering
    // was private void setReferenceMarkOnRenumbering(XTextRange aTextRange, Object elem, Object refMark)
    //was private void insertReferenceMarkOnReNumbering(XTextRange aTextRange, Object elem, int refLength, Object refMark)
    private XTextSection getChildSectionByType(XTextSection parentSection) {
        XTextSection[] childSections = parentSection.getChildSections();
        for (XTextSection childSection : childSections) {
            HashMap<String, String> childMeta = ooDocument.getSectionMetadataAttributes(childSection);
            if (childMeta.containsKey("BungeniSectionType")) {
                String sectionType = childMeta.get("BungeniSectionType");
                if (sectionType.equals("NumberedContainer")) {
                    return childSection;
                }
            }
        }
        return null;
    }

    // (rm, feb 2012) - deprecating method since it is unused
    /*
    private void IterateSectionTypesForNumberedHeadings() {
        try {
            String prevParent = "";
            //set member variable that stores current numbering scheme
            //and then generate sequence.. with the upper range set to number of matched sections
            //initializeNumberingSchemeGenerator((long)1, (long) sectionTypeMatchedSections.size() );
            //check if parent prefix was selected

            // iterate through the sectionTypeMatchedSections and look for heading in section
            Iterator<String> typedMatchSectionItr = sectionTypeMatchedSections.iterator();
            while (typedMatchSectionItr.hasNext()) {

                String sectionName = typedMatchSectionItr.next();
                // get the XTextSection object of the matching section
                XTextSection theSection = ooDocument.getSection(sectionName);
                // get the parent of the matching section
                XTextSection theSectionsParent = theSection.getParentSection();
                // get the child numbered heading
                XTextSection childSection = this.getChildSectionByType(theSection);
                if (childSection == null) {
                    this.sectionTypeMatchedSectionsMissingNumbering.add(sectionName);
                } else {
                    //valid numbering header found
                    //unprotect section
                    ooDocument.protectSection(childSection, false);
                    applyNumberToNumberContainer(theSection, theSectionsParent, childSection, prevParent);
                    //prootct section again
                    ooDocument.protectSection(childSection, true);
                }

                // get the anchor of the matching section

                prevParent = ooQueryInterface.XNamed(theSectionsParent).getName();
            }
        } catch (Exception ex) {
            log.error("matchHeadingsInTypedSections : " + ex.getMessage());
            log.error("matchHeadingsInTypedSections : " + CommonExceptionUtils.getStackTrace(ex));
        }

    }
     */

    // ( rm, feb 2012) - deprecating method since it is unused
    /*
    private void applyNumberToNumberContainer(XTextSection theSection, XTextSection parentSection, XTextSection childSection, String prevParent) {
        //get the name of the current parent
        String currentParent = "";
        XNamed parentName = ooQueryInterface.XNamed(parentSection);
        currentParent = parentName.getName();
        if (!currentParent.equals(prevParent)) {
            //reset iterator
            this.m_selectedNumberingScheme.sequence_initIterator();
        }
        markHeadingAndApplyNumber(theSection, parentSection, childSection, prevParent);

    }
     * */     

    // (rm ,feb 2012) -  deprecating method since it is unused
    /*
    private void markHeadingAndApplyNumber(XTextSection theCurrentSection, XTextSection parentSection, XTextSection childSection, String prevParent) {
        //get the current numbering
        //restart numbering, by resetting the iterator
        //this.m_selectedNumberingScheme.sequence_initIterator();
        //get the next number in the sequence
        String theNumber = this.m_selectedNumberingScheme.sequence_next();
        String parentPrefix = "";
        //if number has parent prefix
            /*
        if (this.m_useParentPrefix) {
        //get parent prefix
        //attache the parent prefix to the number.
        parentPrefix =   getParentPrefix(theCurrentSection, parentSection);
        }*/
        // we want insert  number + space before heading
        // and set a reference mark over the number
        /*
        markupNumberedHeading(childSection, theNumber);
    /*
    HashMap<String, String> numberedHeadingMap = ooDocument.getSectionMetadataAttributes(childSection);
    //get section UUID
    String sectionUUID = numberedHeadingMap.get("BungeniSectionUUID");
    //get the anchor to the numbered heading section
    XTextRange sectionRange = childSection.getAnchor();
    //get the text of the heading in the section
    String headingInSection = sectionRange.getString();
    //create a cursor to walk the heading
    XTextCursor sectionCursor = ooDocument.getTextDocument().getText().createTextCursor();
    //map the cursor to the heading range
    sectionCursor.gotoRange(sectionRange, false);
    //insert a field for the number
    insertField(sectionCursor.getStart(), OOoNumberingHelper.NUM_FIELD_PREFIX, sectionUUID, theNumber);
    sectionCursor.goLeft( (short) 0,false);
    sectionCursor.getText().insertString(sectionCursor, " ", true);
    sectionCursor.goLeft((short) 0, false);
    sectionCursor.goRight((short) 1, false);
    sectionCursor.gotoRange(sectionRange.getEnd(), true);
    //insert a field for the heading
    insertField(sectionCursor, OOoNumberingHelper.HEAD_FIELD_PREFIX, sectionUUID, headingInSection);
    //finally create a reference for the complete heading
    sectionCursor.gotoRange(childSection.getAnchor(), true);
    insertReferenceMark(sectionCursor, sectionUUID);
    updateSectionNumberingMetadata(childSection, theNumber);
     *//*
    }
     */

     private void markupNumberedHeading(String sectionName, TextSectionElement sectionElem,
                String theNumber, BungeniOdfSectionHelper odfDocSectionHelper, Document ooDoc)
    {
        // get the section type
         String childType = odfDocSectionHelper.getSectionType(sectionElem) ;

         // get the section UUID
         String sectionUUID = odfDocSectionHelper.getSectionMetadataValue(sectionElem, "BungeniSectionID") ;

         // apply the reference mark on section
         // insertReferenceMarkOnNumberedHeading(childSection.getAnchor(), sectionUUID, ooDoc);
         // !+ (rm, feb 2012) - @TODO : references use is deprecated..check if step is necessary
         insertReferenceMarkOnNumberedHeading(sectionElem, sectionName, sectionUUID, ooDoc );

         // !+ (rm, feb 2012) - CHECK IF NECESSARY - update the metadata for the section
     }

     /**
      * This method inserts the string that marks up a section for numbering
      * The characters in this string are parsed for to determine the numbering to be applied
      * @param cursorRange
      * @param fieldContent
      * @param isNumber
      */
    private void insertMarkedText(XTextRange cursorRange, String fieldContent, boolean isNumber) {
        
        if (isNumber) {
            fieldContent = OOoNumberingHelper.NUMBERED_PREFIX + fieldContent + OOoNumberingHelper.NUMBERED_SUFFIX;
        } else {
            fieldContent = OOoNumberingHelper.NUMBER_HEADING_BOUNDARY + fieldContent + OOoNumberingHelper.NUMBER_HEADING_BOUNDARY;
        }
        
        cursorRange.getText().insertString(cursorRange, fieldContent, true);
    }

    private void insertField(XTextRange cursorRange, String fieldPrefix, String uuidOfField, String fieldContent) {
        String nameOfField = fieldPrefix + uuidOfField;
        Object refField = ooDocument.createInstance("com.sun.star.text.TextField.Input");
        XPropertySet propSet = ooQueryInterface.XPropertySet(refField);
        try {
            propSet.setPropertyValue("Hint", nameOfField);
            propSet.setPropertyValue("Content", fieldContent);
            //insert the field into the document
            XTextContent fieldContentObject = ooQueryInterface.XTextContent(refField);
            cursorRange.getText().insertTextContent(cursorRange, fieldContentObject, true);

        } catch (PropertyVetoException ex) {
            log.error("InsertField :( " + ex.getClass().getName() + ")" + ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error("InsertField :( " + ex.getClass().getName() + ")" + ex.getMessage());
        } catch (UnknownPropertyException ex) {
            log.error("InsertField :( " + ex.getClass().getName() + ")" + ex.getMessage());
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error("InsertField :( " + ex.getClass().getName() + ")" + ex.getMessage());
        }
    }

    // !+ (rm, feb 2012) -refactored method to use BungeniOdfDom lib
    // @TODO : need for BungeniOdfDom API to add reference mark
    // !+ Substituting the logic in this code section -- will now append the numbering
    private void insertReferenceMarkOnNumberedHeading (TextSectionElement sectionElem, String sectionName,
            String uuIDStr, Document ooDoc)
    {
        // get the section from the document
       Section currSection = ooDoc.getSectionByName(sectionName) ;
       Paragraph p = Paragraph.getInstanceof((TextParagraphElementBase) currSection.getParagraphContainerElement()) ;

       // get the text from the paragraph
       String paragraphText = p.getTextContent() ;
       
       // add a number to the extracted text
       String newParagraph = "(1) " + uuIDStr + " " + paragraphText ;

       // add the text back to the paragraph
       sectionElem.setTextContent(newParagraph);
    }
    
    private void insertReferenceMark2(XTextCursor thisCursor, String referenceName) {
        Object referenceMark = ooDocument.createInstance("com.sun.star.text.ReferenceMark");
        XNamed xRefMark = ooQueryInterface.XNamed(referenceMark);
        xRefMark.setName(referenceName);
        XTextContent xContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xRefMark);
        try {
            thisCursor.getText().insertTextContent(thisCursor, xContent, true);
        } catch (Exception ex) {
            log.error("insertReferenceMark :" + ex.getMessage());
        }
    }

    // (rm,, feb 2012) - this method inserts a reference mark on the document section that is being selected
    // and adds this value to the textfield
    private void insertReferenceMark(XTextCursor thisCursor, String uuidStr) {
        Object referenceMark = ooDocument.createInstance("com.sun.star.text.ReferenceMark");
        XNamed xRefMark = ooQueryInterface.XNamed(referenceMark);
        String refMarkName = OOoNumberingHelper.HEADING_REF_PREFIX + uuidStr;
        xRefMark.setName(refMarkName);
        XTextContent xContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xRefMark);
        try {
            thisCursor.getText().insertTextContent(thisCursor, xContent, true);
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error("insertReferenceMark :" + ex.getMessage());
        }
    }

    private void updateSectionNumberingMetadata(OOComponentHelper ooDoc, XTextSection childSection, String numberedParentType, String theNumber, long lTrueNumber) {
        HashMap<String, String> sectionMeta = new HashMap<String, String>();
        sectionMeta.put(OOoNumberingHelper.numberingMetadata.get("APPLIED_NUMBER"), theNumber);
        //sectionMeta.put(OOoNumberingHelper.numberingMetadata.get("NUMBERING_SCHEME"), /*this.getSelectedNumberingScheme().schemeName*/
        //getNumberingSchemeForSectionType(numberedParentType));
        sectionMeta.put(OOoNumberingHelper.numberingMetadata.get("PARENT_PREFIX_NUMBER"), "");
        String trueNumber = "";
        if (!theNumber.equals(MARKED_FOR_RENUMBERING)) {
            trueNumber = (new Long(lTrueNumber)).toString();
        } else {
            trueNumber = "";
        }
        sectionMeta.put(OOoNumberingHelper.numberingMetadata.get("APPLIED_TRUE_NUMBER"), trueNumber);
        ooDoc.setSectionMetadataAttributes(childSection, sectionMeta);
    }

    // this method gets the numbering scheme for a specific section type
    private String getNumberingSchemeForSectionType(String type) {
        DocumentSection ds = this.sectionTypesForDocumentType.get(type);
        return ds.getNumberingScheme();
    }
    //method to get heading from section with selected sectionType
    ///variable sectionName added below for compilation success
    /*
     *
     *  1) initialize numbering scheme generator
     *  2) enumerate sections matching sectionType attribute
     *      2.1) for each section enumerate section content
     *          call enumerateSectionContent()
     *  3
     *
     */
    /*
    private void matchHeadingsInTypedSections() {
    //  private void getHeadingInSection( ) {
    try {
    String prevParent="";
    //set member variable that stores current numbering scheme
    //and then generate sequence.. with the upper range set to number of matched sections
    initializeNumberingSchemeGenerator((long)1, (long) sectionTypeMatchedSections.size() );
    //check if parent prefix was selected

    //iterate through the sectionTypeMatchedSections and look for heading in section
    Iterator<String> typedMatchSectionItr = sectionTypeMatchedSections.iterator();
    while(typedMatchSectionItr.hasNext()){

    String sectionName = typedMatchSectionItr.next();
    Object sectionObject = ooDocument.getTextSections().getByName(sectionName);
    //get the XTextSection object of the matching section
    XTextSection theSection = ooQueryInterface.XTextSection(sectionObject);
    //get the parent of the matching section
    XTextSection theSectionsParent = theSection.getParentSection();
    //get the anchor of the matching section
    XTextRange range = theSection.getAnchor();
    //get the enumeration object of the section
    log.debug("matchHeadinsinTypedSection: enumerating section content for :" + theSection + ", prevParent = " + prevParent);
    enumerateSectionContent (range, theSection, prevParent);
    //set prevparent to the name of the previous parent section
    prevParent = ooQueryInterface.XNamed(theSectionsParent).getName();
    }
    } catch (Exception ex) {
    log.error("matchHeadingsInTypedSections : " + ex.getMessage());
    log.error("matchHeadingsInTypedSections : " + CommonExceptionUtils.getStackTrace(ex));
    }
    }
     */

    /*
     *section1
     *  section1.1
     *  section1.2
     *  section1.3
     *section2
     *  section2.1
     *  section2.2
     *
     *the above sections ... section1 and section will be numbered 1 & 2
     *section1.1, section1.2 and section1.3 will be numbered serially
     */
    /*
    private void getHeadingInMatchingSections() throws NoSuchElementException, WrappedTargetException, UnknownPropertyException, com.sun.star.lang.IllegalArgumentException {
    String prevParent="";
    int parentPrefix=0;;
    ///iterate through the sectionTypeMatchedSections and look for heading in section
    Iterator<String> typedMatchSectionItr = sectionTypeMatchedSections.iterator();


    //set member variable that stores current numbering scheme
    //and then generate sequence.. with the upper range set to number of matched sections
    initializeNumberingSchemeGenerator((long)1, (long) sectionTypeMatchedSections.size() );


    while(typedMatchSectionItr.hasNext()){
    String sectionName = typedMatchSectionItr.next();
    Object sectionObject = ooDocument.getTextSections().getByName(sectionName);
    XTextSection theSection = ooQueryInterface.XTextSection(sectionObject);
    XTextSection theSectionsParent = theSection.getParentSection();
    XTextRange range = theSection.getAnchor();
    enumerateSectionContent (range, theSection, prevParent);
    prevParent = ooQueryInterface.XNamed(theSectionsParent).getName();
    }
    }
     */
    private void initNumberingSchemeGenerator(String schemeName, long startRange, long endRange) {
        //       String schemeName;
        try {
            //         schemeName = ooDocument.getPropertyValue("BungeniDocNumberingScheme");
            m_selectedNumberingScheme = NumberingSchemeFactory.getNumberingScheme(schemeName);
            m_selectedNumberingScheme.setRange(new NumberRange(startRange, endRange));
            m_selectedNumberingScheme.generateSequence();
            m_selectedNumberingScheme.sequence_initIterator();
        } catch (Exception ex) {
            log.error("initNumberingSchemeGenerator : " + ex.getMessage());
        }

    }
    /*
    private void initializeNumberingSchemeGenerator(long startRange, long endRange) {
    m_selectedNumberingScheme = createSchemeFromSelection();
    m_selectedNumberingScheme.setRange(new NumberRange(startRange, endRange));
    m_selectedNumberingScheme.generateSequence();
    m_selectedNumberingScheme.sequence_initIterator();
    }*/

    /*
     * 1) within the section look for paragraphs
     *  1.1) enumerate the paragrap
     *      enumerateParagraphInSectionContent
     *
     *
     */
    private String enumerateSectionContent(XTextRange sectionRange, XTextSection theSection, String prevParent) {
        try {
            XEnumerationAccess enumAcc = (XEnumerationAccess) UnoRuntime.queryInterface(XEnumerationAccess.class, sectionRange);
            XEnumeration xEnum = enumAcc.createEnumeration();
            /*enumerate the elements in the section */
            while (xEnum.hasMoreElements()) {
                /*get the next enumerated element*/
                Object elem = xEnum.nextElement();
                /*query the matching element for its service info */
                XServiceInfo xInfo = (XServiceInfo) UnoRuntime.queryInterface(XServiceInfo.class, elem);
                /*if paragraph */
                boolean breakFromLoop = false;
                //enumerate the paragraph to get the heading
                if (xInfo.supportsService("com.sun.star.text.Paragraph")) {
                    //call paragraph enumerator
                    breakFromLoop = enumerateParagraphInSectionContent(xInfo, elem, theSection, prevParent);
                } /*for the future ... else if ("com.sun.star.text.TextTable*/
                if (breakFromLoop) {
                    break;
                }
            //
            //prevParent=(String)xParentSecName.getName();
            //break;
            }
        } catch (WrappedTargetException ex) {
            log.error("enumerateSectionContent : " + ex.getMessage());
        } finally {
            return new String("");
        }
    }
    private boolean m_bFoundHeading = false;

    private boolean enumerateParagraphInSectionContent(XServiceInfo xInfo, Object elemParagraph, XTextSection theSection, String previousParent) {
        //we want to match only the first heading
        boolean bMatched = false;
        // m_bFoundHeading = false;
        try {
            /*get the properties of the paragraph */
            XPropertySet objProps = ooQueryInterface.XPropertySet(xInfo);
            short nLevel = -1;
            /*get the paragraphs numbering level */
            nLevel = com.sun.star.uno.AnyConverter.toShort(objProps.getPropertyValue("ParaChapterNumberingLevel"));
            /*check if the paragraph is a heading type nLevel >= 0 */
            if (nLevel >= 0) {
                //first heading has been matched
                bMatched = m_bFoundHeading = true;
                //get the content object we want to enumerate
                XTextContent xContent = ooDocument.getTextContent(elemParagraph);
                enumerateHeadingInParagraph(xContent, theSection, previousParent);
            }
        } catch (UnknownPropertyException ex) {
            log.error("enumerateParagraphInSectionContent: " + ex.getMessage());
        } finally {
            return bMatched;
        }
    }

    private String enumerateHeadingInParagraph(XTextContent xContent, XTextSection theSection, String previousParent) {
        // get the current section name
        int parentPrefix = 0;
        int headCount = 0;
        String sectionName = ooQueryInterface.XNamed(theSection).getName();
        // get the heading text of the matching heading
        XTextRange aTextRange = xContent.getAnchor();
        String strHeading = aTextRange.getString();
        log.debug("getHeadingInSection: heading found " + strHeading);
        // get the parent section of the section containing the heading
        XNamed xParentSecName = ooQueryInterface.XNamed(theSection.getParentSection());
        String currentParent = (String) xParentSecName.getName();
        log.debug("getHeadingInSection" + currentParent);
        // check if the currentParent of the seciton is equal to the previous matching parent
        // if they are not equal we need to restart numbering
        // else we continue numbering
        if (!currentParent.equalsIgnoreCase(previousParent)) {
            restartNumbering(aTextRange, theSection, currentParent);
        } else {
            continueNumbering(aTextRange, theSection, currentParent);
        }

        return new String("");
    }

    private void restartNumbering(XTextRange aRange, XTextSection theCurrentSection, String parentSection) {
        //get the current numbering
        //restart numbering, by resetting the iterator
        this.m_selectedNumberingScheme.sequence_initIterator();
        //get the next number in the sequence
        String theNumber = this.m_selectedNumberingScheme.sequence_next();
        String parentPrefix = "";
        //if number has parent prefix
        if (this.m_useParentPrefix) {
            //get parent prefix
            //attache the parent prefix to the number.
            parentPrefix = getParentPrefix(theCurrentSection, parentSection);
        }
    // we want insert  number + space before heading
    // and set a reference mark over the number
    /////COMMENTED TEMPORARILAY insertNumberForHeading(aRange, theNumber, parentPrefix, theCurrentSection);
    //   insertAppliedNumberToMetadata(matchedSectionElem,headCount);
    }

    private void continueNumbering(XTextRange aRange, XTextSection theCurrentSection, String parentSection) {
        String theNumber = this.m_selectedNumberingScheme.sequence_next();
        // if currentParent is document root use 1 as the starting point for numbering
        // we want insert  number + space before heading
        // and set a reference mark over the number
        String parentPrefix = "";
        if (this.m_useParentPrefix) {
            //get parent prefix
            //attache the parent prefix to the number.
            parentPrefix = getParentPrefix(theCurrentSection, parentSection);
        }
    /////COMMEnTED TEMPORARILY insertNumberForHeading(aRange, theNumber, parentPrefix, theCurrentSection);
    }

    private String getParentPrefix(XTextSection theCurrentSection, String parentSectionName) {
        //get the parent
        //get the number set in the parent.
        return OOoNumberingHelper.getSectionAppliedNumber(ooDocument, parentSectionName);
    }

    private void createReferenceMarkOverCursor(String refName, XTextCursor thisCursor) {
        Object referenceMark = ooDocument.createInstance("com.sun.star.text.ReferenceMark");
        XNamed xRefMark = ooQueryInterface.XNamed(referenceMark);
        xRefMark.setName(refName);
        XTextContent xContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xRefMark);
        try {
            thisCursor.getText().insertTextContent(thisCursor, xContent, true);
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error("createReferenceMarkOverCursor :" + ex.getMessage());
        }
    }

    private void insertParentPrefix(String sectionElement, int parentPrefix) {
        String strParentPrefix = "" + parentPrefix + "";
        //clear the metadata map
        HashMap<String, String> metadata = new HashMap<String, String>();
        //insert key=>value attribute into metadata map
        metadata.put("ParentPrefix", strParentPrefix);
        System.out.println("insertParentPrefix function " + metadata + " to " + sectionElement.toString());
    //insert the applied number into the metadata
    //ooDocument.setSectionMetadataAttributes(sectionElement.toString(),metadata);

    }

    class sectionHeadingReferenceMarks {

        public String sectionName = "";
        public Integer nOrder = 0;
        public Object containedHeading = null;
        public ArrayList<String> refMarks = new ArrayList<String>(0);

        public sectionHeadingReferenceMarks() {
            sectionName = "";
            nOrder = 0;
            refMarks = new ArrayList<String>(0);
        }

        public sectionHeadingReferenceMarks(String sectionName, int order, Object heading) {
            this.sectionName = sectionName;
            this.nOrder = order;
            this.refMarks = new ArrayList<String>(0);
            this.containedHeading = heading;
        }

        public String toString() {
            return sectionName;
        }
    }

    private Object getHeadingFromMatchedSection(Object matchedSectionElem) {

        Object objHeading = null;

        try {

            Object sectionName = ooDocument.getTextSections().getByName(matchedSectionElem.toString());
            XTextSection theSection = ooQueryInterface.XTextSection(sectionName);
            XTextRange range = theSection.getAnchor();

            XEnumerationAccess enumAcc = (XEnumerationAccess) UnoRuntime.queryInterface(XEnumerationAccess.class, range);
            XEnumeration xEnum = enumAcc.createEnumeration();


            while (xEnum.hasMoreElements()) {
                Object elem = xEnum.nextElement();
                XServiceInfo xInfo = (XServiceInfo) UnoRuntime.queryInterface(XServiceInfo.class, elem);
                if (xInfo.supportsService("com.sun.star.text.Paragraph")) {
                    XPropertySet objProps = ooQueryInterface.XPropertySet(xInfo);

                    short nLevel = -1;
                    nLevel = com.sun.star.uno.AnyConverter.toShort(objProps.getPropertyValue("ParaChapterNumberingLevel"));
                    if (nLevel >= 0) {
                        //XTextContent xContent = ooDocument.getTextContent(elem);
                        //XTextRange aTextRange =   xContent.getAnchor();
                        // String strHeading = aTextRange.getString();


                        //  docListReferences.add(strHeading);

                        // removeNumberFromHeading2(aTextRange, elem);
                        objHeading = elem;
                        break;
                    }

                }

            }

        } catch (NoSuchElementException ex) {
            log.error(ex.getClass().getName() + " - " + ex.getMessage());
            log.error(ex.getClass().getName() + " - " + CommonExceptionUtils.getStackTrace(ex));
        } catch (WrappedTargetException ex) {
            log.error(ex.getClass().getName() + " - " + ex.getMessage());
            log.error(ex.getClass().getName() + " - " + CommonExceptionUtils.getStackTrace(ex));
        } catch (UnknownPropertyException ex) {
            log.error(ex.getClass().getName() + " - " + ex.getMessage());
            log.error(ex.getClass().getName() + " - " + CommonExceptionUtils.getStackTrace(ex));
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error(ex.getClass().getName() + " - " + ex.getMessage());
            log.error(ex.getClass().getName() + " - " + CommonExceptionUtils.getStackTrace(ex));
        } finally {
            return objHeading;
        }


    }

    private sectionNumbererPanel self() {
        return this;
    }

    public String getSectionHierarchy(XTextSection thisSection) {
        String sectionName = "";
        sectionName = ooQueryInterface.XNamed(thisSection).getName();
        if (thisSection.getParentSection() != null) {

            sectionName = getSectionHierarchy(thisSection.getParentSection()) + ">" + sectionName;

        } else {
            return sectionName;
        }
        return sectionName;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        checkbxUseParentPrefix = new javax.swing.JCheckBox();
        btnRenumberSections = new javax.swing.JButton();
        btnInsertCrossReference = new javax.swing.JButton();
        btnfixBrokenReferences = new javax.swing.JButton();
        progressNumbering = new javax.swing.JProgressBar();
        btnExternalReference = new javax.swing.JButton();

        checkbxUseParentPrefix.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/panels/loadable/Bundle"); // NOI18N
        checkbxUseParentPrefix.setText(bundle.getString("sectionNumbererPanel.checkbxUseParentPrefix.text")); // NOI18N
        checkbxUseParentPrefix.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        btnRenumberSections.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        btnRenumberSections.setText(bundle.getString("sectionNumbererPanel.btnRenumberSections.text")); // NOI18N
        btnRenumberSections.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenumberSectionsActionPerformed(evt);
            }
        });

        btnInsertCrossReference.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        btnInsertCrossReference.setText(bundle.getString("sectionNumbererPanel.btnInsertCrossReference.text")); // NOI18N
        btnInsertCrossReference.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertCrossReferenceActionPerformed(evt);
            }
        });

        btnfixBrokenReferences.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        btnfixBrokenReferences.setText(bundle.getString("sectionNumbererPanel.btnfixBrokenReferences.text")); // NOI18N
        btnfixBrokenReferences.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfixBrokenReferencesActionPerformed(evt);
            }
        });

        btnExternalReference.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        btnExternalReference.setText(bundle.getString("sectionNumbererPanel.btnExternalReference.text")); // NOI18N
        btnExternalReference.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExternalReferenceActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(progressNumbering, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE).add(checkbxUseParentPrefix).add(btnRenumberSections, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE).add(btnInsertCrossReference, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE).add(btnExternalReference, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, btnfixBrokenReferences, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(19, 19, 19).add(checkbxUseParentPrefix).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btnRenumberSections).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(progressNumbering, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btnInsertCrossReference).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(btnExternalReference).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(btnfixBrokenReferences).addContainerGap(112, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents

    private void btnfixBrokenReferencesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfixBrokenReferencesActionPerformed
// TODO add your handling code here:
        applyFixBrokenReferences();
    }//GEN-LAST:event_btnfixBrokenReferencesActionPerformed

    private void btnInsertCrossReferenceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertCrossReferenceActionPerformed
// TODO add your handling code here:
        // crossRef();
        applyInsertCrossReferences();
    }//GEN-LAST:event_btnInsertCrossReferenceActionPerformed

    public static  File convertUrlToFile(String sUrl) {
        File f = null;
        URL url = null;
        try {
            url = new URL(sUrl);
        } catch (MalformedURLException ex) {
            log.error("convertUrlToFile: "+ ex.getMessage());
            return null;
        }
        try {
            f = new File(url.toURI());
        } catch(URISyntaxException e) {
            f = new File(url.getPath());
        } finally {
            return f;
        }
    }


    private void btnRenumberSectionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenumberSectionsActionPerformed
// TODO add your handling code here:
        UIManager.put("ProgressBar.selectionBackground", Color.black);
        UIManager.put("ProgressBar.selectionForeground", Color.white);
        UIManager.put("ProgressBar.foreground", new Color(8, 32, 128));
        parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        progressNumbering.setMinimum(0);
        progressNumbering.setMinimum(100);
        progressNumbering.setIndeterminate(true);
        //  progressNumbering.setStringPainted(true);
        // progressNumbering.setString("Processing...");
        // now renumber the client

        /**
         * (rm, feb 2012) - this section has been re-edited to take advantage of
         * bungeniodfdom for the renumbering
         */
        // check if the document has been saved to file
        if ( ooDocument.isDocumentOnDisk() && !ooDocument.isModified() ) {                        
            odfDocument = null ;               
                try {
                    // create the ODF Document
                    odfDocument = OdfDocument.loadDocument(convertUrlToFile(ooDocument.getDocumentURL()));

                    //  clone the currently open file using the document package
                    BungeniOdfFileCopy fcp = new BungeniOdfFileCopy(odfDocument.getPackage());

                    // copy the document with the date time as suffix
                    File origFileCopy = fcp.copyTo("_numbered", true);

                    // capture the name of the new file as created
                    String outputFilePath = (String) CommonFileFunctions.getFileAuthorityURL(origFileCopy);

                    // create the Document object from the _numbered copy
                    Document docToNumber = Document.loadDocument(origFileCopy) ;                  

                    // create an instance of odfDocument
                    odfCopyDocument = OdfDocument.loadDocument(origFileCopy);

                    // pass the document composition to the renumbering agent
                    RenumberingAgent agent = new RenumberingAgent(docToNumber, odfCopyDocument);
                    agent.execute();

                    // display the renumbered bill document
                    if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(parentPanel,
                            "Do you wish to view the renumbered document?",
                            "View numbered document", JOptionPane.YES_NO_OPTION)) {

                        // save the copy of the numbered document
                        
                        // validate the tab that hosts the numbered document

                        // add panel to the tabbed pane and display it
                      
                    }
                    else  {
                        // delete the instance of the tabbed pane object with the
                        // numbered document
                        
                    }
                    
                } catch (Exception ex) {
                    log.error(ex);
                }                
        }
        else {
            // ask the user to save the document first
            MessageBox.OK(parentFrame, bundle.getString("Please_save_the_document"), bundle.getString("Save_the_document"), JOptionPane.ERROR_MESSAGE);

            progressNumbering.setIndeterminate(false);
        }       

        /*
         RenumberingAgent agent = new RenumberingAgent(ooDocCopy);
                agent.execute();
         */
    //  progressNumbering.setIndeterminate(false);
    //  progressNumbering.setString("Completed!");
    //  parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btnRenumberSectionsActionPerformed

    // !+ (rm, feb 2012) - error due to created multiple inheritance
    /**
    private DocumentComposition createDocumentComposition(BungeniNoaPanel panel, String filePath) {
        DocumentComposition dc = null ;
        try {
            BungeniNoaNativeView nativeView = new BungeniNoaNativeView();
            nativeView.attachContainerToNativeView(panel.getPanel());
            BungeniNoaOfficeFrame oooFrame = new BungeniNoaOfficeFrame(nativeView);
            panel.getPanel().validate();

            dc = new DocumentComposition(oooFrame, nativeView, panel, null);
          } catch (Exception ex) {
            log.error("There was an error constructing the Openoffice frame", ex);
        }
        return dc ;
    }
    **/
    
    private void btnExternalReferenceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExternalReferenceActionPerformed
// TODO add your handling code here:
        applyInsertExternalReference();

    }//GEN-LAST:event_btnExternalReferenceActionPerformed

    @Override
    public void initialize() {
        super.initialize();
        init();
    }

    public void refreshPanel() {
        //the timer does automatic refreshes....
    }

    /**
     * This method implements the renumbering on the copy of the current
     * bill document. This is done in an off screen buffer
     */
    public class RenumberingAgent extends SwingWorker<Boolean, Void> {

        private Document ooDocCopy = null ;
        private OdfDocument odfDoc ;
        
        public RenumberingAgent(Document ooDCopy, OdfDocument odfD) {
            ooDocCopy = ooDCopy ;
            odfDoc = odfD ;
        }

        String errorMessage = "";
        @Override
        protected Boolean doInBackground() {
            boolean bState = true;
            try {
                bState = applyRenumberingScheme(ooDocCopy, odfDoc);
            } catch (Exception ex) {
                log.error("applyRenumberingScheme :" + ex.getMessage());
                log.error("applyRenumberingScheme : " + CommonExceptionUtils.getStackTrace(ex));
            } finally {
                return bState;
            }
        }

        /**
         * This is the container method that finds the containers that require renumbering, marks them
         * for renumbering and renumbering the various children in the containers with numbering
         * @param ooDocCopy
         * @return
         */
        private boolean applyRenumberingScheme(Document ooDocCopy, OdfDocument odfDoc) {

            // find the containers that need renumbering
            log.debug("applyRenumberingScheme : invoking findNumberedContainers");
            ArrayList<String> numberedContainers = findNumberedContainers(odfDoc);
            if (numberedContainers.isEmpty()) {
                this.errorMessage =  bundle.getString("no_headings_for_numbering");
                return false;
            }

            // apply the numbering to the container identified as requiring renumbering
            log.debug("applyRenumberingScheme : invoking applyNumberingMarkupToNonNumberedContainers");
            applyNumberingMarkupToNonNumberedContainers(numberedContainers, ooDocCopy, odfDoc);
        
            log.debug("applyRenumberingScheme : invoking reApplyNumberingOnNumberedContainers");
            // reApplyNumberingOnNumberedContainers(ooDocCopy);

        return true;
    }

        @Override
        protected void done() {
            progressNumbering.setIndeterminate(false);
            progressNumbering.setValue(100);
            progressNumbering.setString((bundle.getString("completed")));
            boolean bState = true;
             parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            try {
                bState = get();
            } catch (InterruptedException ex) {
                log.error(ex.getMessage());
            } catch (ExecutionException ex) {
                log.error(ex.getMessage());
            }
            if (!bState) {
                MessageBox.OK("There was an error : \n" + errorMessage);
            }
           
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExternalReference;
    private javax.swing.JButton btnInsertCrossReference;
    private javax.swing.JButton btnRenumberSections;
    private javax.swing.JButton btnfixBrokenReferences;
    private javax.swing.JCheckBox checkbxUseParentPrefix;
    private javax.swing.JProgressBar progressNumbering;
    // End of variables declaration//GEN-END:variables
}
