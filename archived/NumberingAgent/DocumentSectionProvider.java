/*
 * DocumentSectionIterator.java
 *
 * Created on May 16, 2008, 1:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.providers;

import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.beans.XPropertySetInfo;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XEnumeration;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.container.XNamed;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.text.XText;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextSection;
import com.sun.star.uno.Any;
import com.sun.star.uno.UnoRuntime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.bungeni.utils.BungeniBNode;
import org.bungeni.utils.BungeniBTree;
import org.bungeni.utils.NodeDisplayTextSetter;
import org.odftoolkit.odfdom.doc.OdfDocument;

/**
 *
 * @author Ashok Hariharan
 */
public class DocumentSectionProvider {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DocumentSectionProvider.class.getName());
    // private static OOComponentHelper ooDocument;
    private static OdfDocument ooDocument ;
    private static BungeniBTree theSectionTree = new BungeniBTree();
    public static int TIMER_DELAY = 4000;
 //   static Timer sectionRefreshTimer;
    //private static ArrayList<DocumentSectionAdapterDefaultTreeModel> treeModelList = new ArrayList<DocumentSectionAdapterDefaultTreeModel>();
    private static ArrayList<IRefreshableSectionTreeModel> treeModelList = new ArrayList<IRefreshableSectionTreeModel>();

 
    /** Creates a new instance of DocumentSectionIterator */
    public DocumentSectionProvider() {
    }
    
    public static synchronized void updateOOoHandle(OdfDocument ooDoc) {
        ooDocument = ooDoc;
    }
    
    public static void initialize(OdfDocument ooDoc) {
        updateOOoHandle(ooDoc);
        buildSectionTree();
    }
    
    public static OdfDocument getOOoDocument(){
        return ooDocument;
    }
     public static void subscribeModel(IRefreshableSectionTreeModel model) {
        treeModelList.add(model);
    }
     
    public static void unsubscribeModel(IRefreshableSectionTreeModel model) {
        treeModelList.remove(model);
    }
     
    public static BungeniBTree getTree(){
        return theSectionTree;
    }
    
    public static BungeniBNode getTreeRoot(){
       
        if (theSectionTree.getTree().size() == 0 ) {
            SectionTree aTree = new SectionTree(ooDocument);
            theSectionTree = aTree.newTree();
        } 
        
        return theSectionTree.getTree().get(theSectionTree.getTree().firstKey());
            
    }

    public static BungeniBTree getNewTree(){
        SectionTree aTree = new SectionTree(ooDocument);
        BungeniBTree bTree = aTree.newTree();
        return bTree;
    }

    //AH-23-01-11
    /**
     * Create a tree with a list of exclusions
     * @param ignoreThese
     * @return
     */
    public static BungeniBTree getNewTree(String[] ignoreThese) {
        SectionTree aTree = new SectionTree(ooDocument, ignoreThese, true);
        BungeniBTree bTree = aTree.newTree();
        return bTree;
    }

    
   static class NewTreeAgent extends SwingWorker<BungeniBTree, Void> {
        BungeniBTree theTree = null;
        /**
         * 0 for normal section generation
         * 1 for friendly section generation
         */
        int friendlyFlag = 0;
        
        NewTreeAgent(int nFlag0forNormal1forFriendly){
            friendlyFlag = nFlag0forNormal1forFriendly;
        }
        
        @Override
        protected BungeniBTree doInBackground()  {
            BungeniBTree bTree = null;
            try {
                if (friendlyFlag == 1 ) {
                    bTree = generateFriendlySectionsTree(); 
                } else {
                    bTree = generateSectionsTree(null);
                }
            } catch (Exception ex) {
                log.error("NewTreeAgent: do : " + ex.getMessage());
            } finally {
                return bTree;
            }
        }
        
        @Override
        protected void done(){
            try {
                theTree = get();
            } catch (InterruptedException ex) {
                log.error("NewTreeAgent: done : " + ex.getMessage());
            } catch (ExecutionException ex) {
               log.error("NewTreeAgent: done : " + ex.getMessage());
            }
        }
        
    }
   
   public static BungeniBTree getNewFriendlyTree(){
       SectionTree aTree = new SectionTree(ooDocument);
       return aTree.newFriendlyTree();
   }
  
    private static void buildSectionTree() {
        //disable the global timer.s
       //s initTimer();
    }
    
    
  public static  class SectionTree {
        OdfDocument sectionTreeOpenDocument;
        //AH-23-11-01
        //this was a static type previously -- changing this to a private member 
        //variable ... by default numbered heading section types are ignored
        //numbered heading section types are named starting with num_
        private  ArrayList<String> ignoreTheseSections = new ArrayList<String>(){
        {
            add("num_");
        }
    };
    
        public SectionTree(OdfDocument ooDoc) {
            sectionTreeOpenDocument = ooDoc;
        }
      
        //AH-23-01-11 overriden constructor allows setting of ignorable sections
        //existing ignorable section defaults are included if the the clearExisting flag is set to 
        //false
        /**
         * Initialize SectionTree class
         * @param ooDoc OpenOffice document context
         * @param sectionsToIgnore array of string containing list of sections to ignore
         * @param clearExisting if set to true ignores the default list of exclusions, if set to false
         * existing set of ignorable sections are included.
         */
        public SectionTree(OdfDocument ooDoc, String[] sectionsToIgnore, boolean clearExisting) {
            this.sectionTreeOpenDocument = ooDoc;
            if (clearExisting) {
                this.ignoreTheseSections.clear();
            }
            for (String ignorethisSection : sectionsToIgnore) {
                if (!this.ignoreTheseSections.contains(ignorethisSection)) {
                    this.ignoreTheseSections.add(ignorethisSection);
                }
            }
        }

       private boolean sectionExclusions(String checkSection){
        for (String secName : ignoreTheseSections) {
           if (checkSection.startsWith(secName)) {
               return true;
           }
        }
        return false;
    }
       
        private  ArrayList<String> getParentChain(String Sectionname, OOComponentHelper ooDoc){
         ArrayList<String> nodeHierarchy = new ArrayList<String>();
             XTextSection currentSection = ooDoc.getSection(Sectionname);
              XTextSection sectionParent=currentSection.getParentSection();
              XNamed parentProps = ooQueryInterface.XNamed(sectionParent);
              String parentSectionname = parentProps.getName();
              String currentSectionname = Sectionname;
              //array list goes from child(0) to ancestors (n)
              log.debug("getParentChain: nodeHierarchy: Adding "+ currentSectionname);
              nodeHierarchy.add(currentSectionname);
              while (1==1) {
                  //go up the hierarchy until you reach root.
                  //break upon reaching the parent
                  if (parentSectionname.equals(BungeniEditorPropertiesHelper.getDocumentRoot())) {
                      nodeHierarchy.add(parentSectionname);
                      log.debug("getParentChain: nodeHierarchy: Adding "+ parentSectionname + " and breaking.");
                      break;
                  }
                 nodeHierarchy.add(parentSectionname);
                 log.debug("getParentChain: nodeHierarchy: Adding "+ parentSectionname + ".");
                 currentSectionname = parentSectionname;
                 sectionParent = sectionParent.getParentSection();
                 parentProps = ooQueryInterface.XNamed(sectionParent);
                 parentSectionname = parentProps.getName();
              } //end while (1== 1)
              if (nodeHierarchy.size() > 1 )
                Collections.reverse(nodeHierarchy);
         return nodeHierarchy;
    }
        
        private BungeniBTree buildTree(String objCallback){
                BungeniBTree treeRoot = new BungeniBTree();
                OdfDocument localOoDoc;
                TreeMap<Integer,String> namesMap = new TreeMap<Integer,String>();
                try {
                    localOoDoc = sectionTreeOpenDocument;
                    // if (!localOoDoc..isXComponentValid()) return treeRoot;
                            String documentRoot = BungeniEditorPropertiesHelper.getDocumentRoot();

                            /*
                            do a basic check to see if the root section exists
                            */
                            if (!localOoDoc.getTextSections().hasByName(documentRoot)) {
                                log.error("buildTree: no root section found");
                                return treeRoot;
                            }
                            /*
                            get the root section and it as the root node to the JTree
                            */
                            Object root = localOoDoc.getTextSections().getByName(documentRoot);
                            log.debug("buildTree: Adding root node");
                            BungeniBNode bnewRootnode = new BungeniBNode(documentRoot);
                            bnewRootnode.setAndRunNamedCallback(objCallback);
                            treeRoot.addRootNode(bnewRootnode);
                            //treeRoot.addRootNode(new String(documentRoot));
                            /*
                            now get the enumeration of the TextSection
                            */

                            int currentIndex = 0;
                            String parentObject = documentRoot;

                            // get the root section
                            XTextSection theSection = ooQueryInterface.XTextSection(root);
                            XTextRange range = theSection.getAnchor(); // getAnchor() get the start position for theSection
                            XText xText = range.getText(); // get the whole doc
                            XEnumerationAccess enumAccess =(XEnumerationAccess)UnoRuntime.queryInterface(XEnumerationAccess.class, xText);
                            //namesMap.put(currentIndex++, parentObject);
                            XEnumeration enumeration = enumAccess.createEnumeration(); // create enumeration of the whole document and its sections
                             log.debug("buildTree: starting Enumeration ");
                            /*
                            start the enumeration of sections first
                            */
                             /*
                              * This section of code extracts and adds the sections
                              */
                             while (enumeration.hasMoreElements()) {
                                 Object elem = enumeration.nextElement(); // get an element
                                 XPropertySet objProps = ooQueryInterface.XPropertySet(elem);  // get the section/element properties
                                 XPropertySetInfo objPropsInfo = objProps.getPropertySetInfo(); // load the properties to a PropertiesInfo Obj
                                 /*
                                  *enumerate only TextSection objects
                                  */
                                 if (objPropsInfo.hasPropertyByName("TextSection")) { // get the textSection Name from the properties info
                                     XTextSection xConSection = (XTextSection) ((Any)objProps.getPropertyValue("TextSection")).getObject();
                                     if (xConSection != null ) {
                                         /*
                                          *Get the section name 
                                          */   
                                         XNamed objSectProps = ooQueryInterface.XNamed(xConSection);
                                         String sectionName = objSectProps.getName();
                                         /*
                                          *only enumerate non root sections
                                          */ 
                                         if (!sectionName.equals(documentRoot)) {
                                             log.debug("buildTree: Found Section :"+ sectionName);
                                             //if section is among exclusions don't add to the names map
                                             if (sectionExclusions(sectionName) == false) {
                                                  /*
                                                  *check if the node exists in the tree
                                                  */
                                                  if (!namesMap.containsValue(sectionName)) {
                                                            namesMap.put(currentIndex++, sectionName);
                                                  }
                                             }
                                         } // if (!sectionName...)     
                                     } // if (xConSection !=...)
                                 } // if (objPropsInfo.hasProper....)
                             } // while (enumeration.hasMore.... )

                         /*
                          *now scan through the enumerated list of sections
                          */
                         Iterator namesIterator = namesMap.keySet().iterator();
                          while (namesIterator.hasNext()) {
                              Integer iOrder = (Integer) namesIterator.next();
                              String sectionName = namesMap.get(iOrder);
                              /*
                               *check if the sectionName exists in our section tree
                               */
                              BungeniBNode theNode = treeRoot.getNodeByName(sectionName);
                              if (theNode == null ) {
                                  /*
                                   *the node does not exist, build its parent chain
                                   */
                                   ArrayList<String> parentChain = getParentChain(sectionName, localOoDoc);
                                   /*
                                    *now iterate through the paren->child hierarchy of sections
                                    */
                                   Iterator<String> sections = parentChain.iterator();
                                   BungeniBNode currentNode = null, previousNode = null;
                                   while (sections.hasNext()) {
                                       String hierSection = sections.next();
                                       currentNode =  treeRoot.getNodeByName(hierSection);
                                       if (currentNode == null ) {
                                           /* the node doesnt exist in the tree */
                                           if (previousNode != null ) {
                                               BungeniBNode bhierNode = new BungeniBNode(hierSection);
                                               bhierNode.setAndRunNamedCallback(objCallback);
                                               treeRoot.addNodeToNamedNode(previousNode.getName(), bhierNode);
                                               //changed for callback
                                               // treeRoot.addNodeToNamedNode(previousNode.getName(), hierSection);
                                                previousNode = treeRoot.getNodeByName(hierSection);
                                                if (previousNode == null ) 
                                                    log.debug("previousNode was null");
                                           } else {
                                               log.info("The root section was not in the BungeniBTree hierarchy, this is an error condition");
                                           }
                                       } else {
                                           /* the node already exists...*/
                                           previousNode = currentNode;
                                       }
                                   }
                              }


                          }
             //  convertBTreetoJTreeNodes(treeRoot);
                } catch (NullPointerException ex) {
                    log.error("buildTree : null pointer exception");
                    log.error("buildTree: " + CommonExceptionUtils.getStackTrace(ex));
            } catch (NoSuchElementException ex) {
                log.error("buildTree :" + ex.getMessage());
            } catch (UnknownPropertyException ex) {
                log.error("buildTree :" + ex.getMessage());
            } catch (WrappedTargetException ex) {
                log.error("buildTree :" + ex.getMessage());
            } finally {
                return treeRoot;
            }
        }
            
        public BungeniBTree newTree(){
              BungeniBTree bnewTree = buildTree(null);
              return bnewTree;
        }
        
        public BungeniBTree newFriendlyTree(){
            NodeDisplayTextSetter txtSetter = new NodeDisplayTextSetter(this.sectionTreeOpenDocument);
            BungeniBNode.setINodeSetterCallback(txtSetter);
            BungeniBTree newTree = buildTree(txtSetter.getName());//txtSetter.getName());
            return newTree;
        }
        
    }
    private static final ArrayList<String> excludeTheseSections = new ArrayList<String>(){
        {
            add("num_");
        }
    };
    
    private static boolean inSectionExclusions(String checkSection){
        for (String secName : excludeTheseSections) {
           if (checkSection.startsWith(secName)) {
               return true;
           }
        }
        return false;
    }

      private static BungeniBTree generateSectionsTree(String objCallback){
        BungeniBTree treeRoot = new BungeniBTree();
        OOComponentHelper localOoDoc;
        TreeMap<Integer,String> namesMap = new TreeMap<Integer,String>();
        try {
            localOoDoc = ooDocument;
            if (!localOoDoc.isXComponentValid()) return treeRoot;
                    String documentRoot = BungeniEditorPropertiesHelper.getDocumentRoot();
                 
                    /*
                    do a basic check to see if the root section exists
                    */
                    if (!localOoDoc.getTextSections().hasByName(documentRoot)) {
                        log.error("generateSectionsTree: no root section found");
                        return treeRoot;
                    }
                    /*
                    get the root section and it as the root node to the JTree
                    */
                    Object root = localOoDoc.getTextSections().getByName(documentRoot);
                    log.debug("generateSectionsTree: Adding root node");
                    BungeniBNode bnewRootnode = new BungeniBNode(documentRoot);
                    bnewRootnode.setAndRunNamedCallback(objCallback);
                    treeRoot.addRootNode(bnewRootnode);
                    //treeRoot.addRootNode(new String(documentRoot));
                    /*
                    now get the enumeration of the TextSection
                    */

                    int currentIndex = 0;
                    String parentObject = documentRoot;
                    XTextSection theSection = ooQueryInterface.XTextSection(root);
                    XTextRange range = theSection.getAnchor();
                    XText xText = range.getText();
                    XEnumerationAccess enumAccess =(XEnumerationAccess)UnoRuntime.queryInterface(XEnumerationAccess.class, xText);
                    //namesMap.put(currentIndex++, parentObject);
                    XEnumeration enumeration = enumAccess.createEnumeration();
                     log.debug("generateSectionsTree: starting Enumeration ");
                    /*
                    start the enumeration of sections first
                    */ 
                     while (enumeration.hasMoreElements()) {
                         Object elem = enumeration.nextElement();
                         XPropertySet objProps = ooQueryInterface.XPropertySet(elem);
                         XPropertySetInfo objPropsInfo = objProps.getPropertySetInfo();
                         /*
                          *enumerate only TextSection objects
                          */
                         if (objPropsInfo.hasPropertyByName("TextSection")) {
                             XTextSection xConSection = (XTextSection) ((Any)objProps.getPropertyValue("TextSection")).getObject();
                             if (xConSection != null ) {
                                 /*
                                  *Get the section name 
                                  */   
                                 XNamed objSectProps = ooQueryInterface.XNamed(xConSection);
                                 String sectionName = objSectProps.getName();
                                 /*
                                  *only enumerate non root sections
                                  */ 
                                 if (!sectionName.equals(documentRoot)) {
                                     log.debug("generateSectionsTree: Found Section :"+ sectionName);
                                     //if section is among exclusions don't add to the names map
                                     if (inSectionExclusions(sectionName) == false) {
                                          /*
                                          *check if the node exists in the tree
                                          */
                                          if (!namesMap.containsValue(sectionName)) {
                                                    namesMap.put(currentIndex++, sectionName);
                                          }
                                     }
                                 } // if (!sectionName...)     
                             } // if (xConSection !=...)
                         } // if (objPropsInfo.hasProper....)
                     } // while (enumeration.hasMore.... )

                     /*
                      *now scan through the enumerated list of sections
                      */
                     Iterator namesIterator = namesMap.keySet().iterator();
                      while (namesIterator.hasNext()) {
                          Integer iOrder = (Integer) namesIterator.next();
                          String sectionName = namesMap.get(iOrder);
                          /*
                           *check if the sectionName exists in our section tree
                           */
                          BungeniBNode theNode = treeRoot.getNodeByName(sectionName);
                          if (theNode == null ) {
                              /*
                               *the node does not exist, build its parent chain
                               */
                               ArrayList<String> parentChain = buildParentChain(sectionName, localOoDoc);
                               /*
                                *now iterate through the paren->child hierarchy of sections
                                */
                               Iterator<String> sections = parentChain.iterator();
                               BungeniBNode currentNode = null, previousNode = null;
                               while (sections.hasNext()) {
                                   String hierSection = sections.next();
                                   currentNode =  treeRoot.getNodeByName(hierSection);
                                   if (currentNode == null ) {
                                       /* the node doesnt exist in the tree */
                                       if (previousNode != null ) {
                                           BungeniBNode bhierNode = new BungeniBNode(hierSection);
                                           bhierNode.setAndRunNamedCallback(objCallback);
                                           treeRoot.addNodeToNamedNode(previousNode.getName(), bhierNode);
                                           //changed for callback
                                           // treeRoot.addNodeToNamedNode(previousNode.getName(), hierSection);
                                            previousNode = treeRoot.getNodeByName(hierSection);
                                            if (previousNode == null ) 
                                                log.debug("previousNode was null");
                                       } else {
                                           log.info("The root section was not in the BungeniBTree hierarchy, this is an error condition");
                                       }
                                   } else {
                                       /* the node already exists...*/
                                       previousNode = currentNode;
                                   }
                               }
                          }


                      }
         //  convertBTreetoJTreeNodes(treeRoot);
        } catch (NoSuchElementException ex) {
            log.error("generateSectionsTree :" + ex.getMessage());
        } catch (UnknownPropertyException ex) {
            log.error("generateSectionsTree :" + ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error("generateSectionsTree :" + ex.getMessage());
        } finally {
            return treeRoot;
        }
    }

 private static ArrayList<String> buildParentChain(String Sectionname, OOComponentHelper ooDoc){
         ArrayList<String> nodeHierarchy = new ArrayList<String>();
             XTextSection currentSection = ooDoc.getSection(Sectionname);
              XTextSection sectionParent=currentSection.getParentSection();
              XNamed parentProps = ooQueryInterface.XNamed(sectionParent);
              String parentSectionname = parentProps.getName();
              String currentSectionname = Sectionname;
              //array list goes from child(0) to ancestors (n)
              log.debug("buildParentChain: nodeHierarchy: Adding "+ currentSectionname);
              nodeHierarchy.add(currentSectionname);
              while (1==1) {
                  //go up the hierarchy until you reach root.
                  //break upon reaching the parent
                  if (parentSectionname.equals(BungeniEditorPropertiesHelper.getDocumentRoot())) {
                      nodeHierarchy.add(parentSectionname);
                      log.debug("buildParentChain: nodeHierarchy: Adding "+ parentSectionname + " and breaking.");
                      break;
                  }
                 nodeHierarchy.add(parentSectionname);
                 log.debug("buildParentChain: nodeHierarchy: Adding "+ parentSectionname + ".");
                 currentSectionname = parentSectionname;
                 sectionParent = sectionParent.getParentSection();
                 parentProps = ooQueryInterface.XNamed(sectionParent);
                 parentSectionname = parentProps.getName();
              } //end while (1== 1)
              if (nodeHierarchy.size() > 1 )
                Collections.reverse(nodeHierarchy);
         return nodeHierarchy;
    }
 

    public static BungeniBTree generateFriendlySectionsTree() {
        /*
        final OOComponentHelper localOoDoc;
        synchronized(ooDocument) {
            localOoDoc = ooDocument;
        }
       NodeDisplayTextSetter txtSetter = new NodeDisplayTextSetter(localOoDoc);
       BungeniBNode.setINodeSetterCallback(txtSetter);
        ////check this code
       */
         BungeniBTree newTree = generateSectionsTree(null);//txtSetter.getName());
        return newTree;
    }
    
 
 
}
