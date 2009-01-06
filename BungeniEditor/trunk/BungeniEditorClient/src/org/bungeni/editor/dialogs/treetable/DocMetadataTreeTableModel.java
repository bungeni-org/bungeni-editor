/*
 * DocMetadataTreeTableModel.java
 *
 * Created on February 25, 2008, 12:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.dialogs.treetable;

import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XNamed;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.text.XTextSection;
import java.util.HashMap;
import java.util.Iterator;
import org.bungeni.ooo.ooQueryInterface;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.apache.log4j.Logger;
import org.bungeni.editor.BungeniEditorPropertiesHelper;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public class DocMetadataTreeTableModel extends DefaultTreeTableModel {
    sectionHive rootHive;
    private OOComponentHelper ooDocument;
    private DefaultMutableTreeTableNode sectionRootNode = null;
    private boolean emptyRootNode = false;
    HashMap<String, String> sectionMetadataMap=new HashMap<String, String>();
    private static org.apache.log4j.Logger log = Logger.getLogger(DocMetadataTreeTableModel.class.getName());
    static protected String[]  cNames = {"Section", "Value"};
    /** Creates a new instance of DocMetadataTreeTableModel */
    public DocMetadataTreeTableModel(OOComponentHelper ooDoc, sectionHive hiveObj) {
          super((TreeTableNode)new sectionHiveNode(hiveObj, null));
         rootHive = hiveObj;
      //   buildTree(rootHive);
         this.ooDocument=ooDoc;
         //initTemp(rootHive);
         initSectionsArray(rootHive);
         //iterate through the tree built in the previous step and add metadata to the tree.
         //
        // getDocMeta(rootHive);
         this.setRoot((TreeTableNode)new sectionHiveNode(rootHive, null));
         
        
        
    }
    
    public void refreshModel(){
        log.debug("refreshModel: calling initSectionsArray");
        this.rootHive = new sectionHive(BungeniEditorPropertiesHelper.getDocumentRoot());
        initSectionsArray(rootHive);
        this.setRoot((TreeTableNode)new sectionHiveNode(rootHive, null));
        modelSupport.fireNewRoot();
        
        //modelSupport.
    }
    
    
     
     
    	public sectionHive getHive()
	{
		return rootHive;
	}

    @Override
	public int getColumnCount()
	{
		return cNames.length;
	}

    @Override
	public String getColumnName(int column)
	{
            return cNames[column];
	}

    @Override
	public Object getValueAt(Object aObject, int aColumn)
	{
		sectionHiveNode vTreeNode = (sectionHiveNode)aObject;
		sectionHive hive = vTreeNode.getHive();

                switch(aColumn) {
                case 0:
                    return hive.getName();
                case 1:
                    return hive.getValue();
                default:
                    return hive;
                }
	}
    

     
    public void buildTree(sectionHive root) {
        sectionHive Sect1 = new sectionHive("section1");
        sectionHive child0_Sect1 = new sectionHive("name", "Aesculapius");
        sectionHive child1_Sect1 = new sectionHive("address", "pobox 8, x-land");
        Sect1.addChildren(child0_Sect1);
        Sect1.addChildren(child1_Sect1);

        sectionHive Sect2 = new sectionHive("section2");
        sectionHive child0_Sect2 = new sectionHive("name", "Prometheus");
        sectionHive child1_Sect2 = new sectionHive("address", "pobox 32, Y-land");
        
        Sect2.addChildren(child0_Sect2);
        Sect2.addChildren(child1_Sect2);

            sectionHive child3_Sect2 = new sectionHive("section2.1");
            sectionHive child0_child3_Sect2 = new sectionHive("name", "Prometheus2");
            sectionHive child1_child3_Sect2 = new sectionHive("address", "pobox 323, Z-land");
            child3_Sect2.addChildren(child0_child3_Sect2);
            child3_Sect2.addChildren(child1_child3_Sect2); 

            
            sectionHive child4_Sect2 = new sectionHive("section2.2");
            sectionHive child0_child4_Sect2 = new sectionHive("name", "Prometheus2");
            sectionHive child1_child4_Sect2 = new sectionHive("address", "pobox 323, Z-land");
            child4_Sect2.addChildren(child0_child4_Sect2);
            child4_Sect2.addChildren(child1_child4_Sect2); 
            
            sectionHive child5_Sect2 = new sectionHive("section2.4");
            sectionHive child0_child5_Sect2 = new sectionHive("name", "Prometheus2");
            sectionHive child1_child5_Sect2 = new sectionHive("address", "pobox 323, Z-land");
            child5_Sect2.addChildren(child0_child5_Sect2);
            child5_Sect2.addChildren(child1_child5_Sect2); 
            
        Sect2.addChildren(child3_Sect2);
        Sect2.addChildren(child4_Sect2);
        Sect2.addChildren(child5_Sect2);
        
        root.addChildren(Sect1);
        root.addChildren(Sect2);
        
        
    }
    
 
    
    private void initSectionsArray(sectionHive root) {
        try {
            if (!ooDocument.isXComponentValid()) return;
            
           
            if (!ooDocument.getTextSections().hasByName(BungeniEditorPropertiesHelper.getDocumentRoot())) {
                log.debug("initSectionsArray : no root section found");
                return;
            }
            Object rootSection = ooDocument.getTextSections().getByName(BungeniEditorPropertiesHelper.getDocumentRoot());
            XTextSection theSection = ooQueryInterface.XTextSection(rootSection);
            if (theSection.getChildSections().length == 0) {
                //root is empty and has no children. 
                //set empty status 
                this.emptyRootNode = true;
            }
          root.sectionName = BungeniEditorPropertiesHelper.getDocumentRoot();
           //root.addChildren(Sect1);
           recurseSections (theSection, root);
            
            
        } catch (NoSuchElementException ex) {
            log.error("initSectionsArray : " + ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error("initSectionsArray : " + ex.getMessage());
        }
    }
    
    private void recurseSections (XTextSection theSection, sectionHive root ) {
            //recurse children
            XTextSection[] sections = theSection.getChildSections();
            if (sections != null ) {
                if (sections.length > 0 ) {
                    //start from last index and go to first
                    for (int nSection = sections.length - 1 ; nSection >=0 ; nSection--) {
                        log.debug ("section name = "+sections[nSection] );
                        //get the name for the section and add it to the root node.
                        XNamed xSecName = ooQueryInterface.XNamed(sections[nSection]);
                        String childSectionName = (String) xSecName.getName();
                        //check and see if the node starts 'meta' if it does do not display it
                     if(!childSectionName.startsWith("meta-")){
                       
                        sectionHive newNode=new sectionHive(childSectionName);
                        
                        sectionMetadataMap=ooDocument.getSectionMetadataAttributes(childSectionName);
                        log.debug("SectionMetadataLoad childSectionName: " + childSectionName);
                       if(sectionMetadataMap.size()>0){
                            
                        Iterator metaIterator = sectionMetadataMap.keySet().iterator();

                               while(metaIterator.hasNext()){
                                        for(int i=0; i< sectionMetadataMap.size(); i++) {
                                            String metaName = (String) metaIterator.next();
                                           // log.debug("sectionMetadataMap Values:" + metaIterator.next());
                                            sectionHive xNode=new sectionHive(metaName,sectionMetadataMap.get(metaName));
                                            newNode.addChildrenAt(xNode, 0);
                                        }
                                    }  
                            }
                       
                            root.addChildren(newNode);

                            recurseSections (sections[nSection], newNode);
                      }//end if for indexOf
                     }
                   
                } else 
                    return;
            } else 
                return;
         
        
        
    }
    
    
     
     
    
}