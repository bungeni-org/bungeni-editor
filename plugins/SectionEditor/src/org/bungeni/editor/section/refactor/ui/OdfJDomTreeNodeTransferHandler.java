package org.bungeni.editor.section.refactor.ui;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NodeMoveTransferHandler.java
 *
 * Created on October 4, 2007, 11:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */



import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;
import javax.swing.AbstractAction;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.bungeni.editor.section.refactor.xml.OdfJDomElement;

/**
 *
 * @author Administrator
 */
public class OdfJDomTreeNodeTransferHandler extends TransferHandler {
   private static org.apache.log4j.Logger log = Logger.getLogger(OdfJDomTreeNodeTransferHandler.class.getName());
   private final Integer MOVE_BEFORE=0;
   private final Integer MOVE_AFTER=1;
   private JPopupMenu moveMenu = new JPopupMenu();
   private panelSectionRefactor panelRefactor;
   private String odfFile = "";
   //private OdfRefactor odfRefactor = null;
  // private BungeniPopupMenuHelper menuHelper;
   private TreeMap<String,String> theMenu = new TreeMap<String,String>(){
       {
                put("0_MOVE_BEFORE", "Move Before Section");
                put("1_MOVE_AFTER", "Move After Section");
                put("2_MOVE_INSIDE", "Place Inside Section");
                put("3_CANCEL_ACTION", "Cancel");
       };
   };
  /**
   * constructor
   */
  public OdfJDomTreeNodeTransferHandler(panelSectionRefactor frm) {
    super();
    this.panelRefactor = frm;
   }

  
  public void setPanelForm(panelSectionRefactor panelFrm ){
   this.panelRefactor  = panelFrm;
   //this.odfRefactor = new OdfRefactor(odfFile);
  }
  
  /**
   * create a transferable that contains all paths that are currently selected in
   * a given tree
   * @see javax.swing.TransferHandler#createTransferable(javax.swing.JComponent)
   * @return  all selected paths in the given tree
   * (or null if the given component is not a tree)
   */
    @Override
  protected Transferable createTransferable(JComponent c) {
  	Transferable t = null;
	  if(c instanceof JTree) {
              JTree tree = (JTree) c;
              t = new OdfJDomTransferable(tree.getSelectionPaths());
              dragPath = tree.getSelectionPath();
              if (dragPath != null) {
                  Object obj = dragPath.getLastPathComponent();
                 // System.out.println("obj class = " + obj.getClass().getName());
                draggedNode = (OdfJDomTreeNode) dragPath.getLastPathComponent();
              }
	  }
	  return t;
  }

  /**
   * move selected paths when export of drag is done
   * @param source  the component that was the source of the data
   * @param data  the data that was transferred or possibly null if the action is NONE.
   * @param action  the actual action that was performed
   */
    @Override
  protected void exportDone(JComponent source, Transferable data, int action) {
      try {
  	if(source instanceof JTree) {
              JTree tree = (JTree) source;
              OdfJDomTreeModel model = (OdfJDomTreeModel) tree.getModel();
              TreePath currentPath = tree.getSelectionPath();
              Rectangle rectCoords = tree.getPathBounds(currentPath);
  		if(currentPath != null) {
                    //get the drop target node
                    OdfJDomTreeNode thisNode = (OdfJDomTreeNode) currentPath.getLastPathComponent();
                    //we dont handle multiple selections of the source, so the source node is always a single one.
                    TreePath[] movedPaths = (TreePath[]) data.getTransferData(OdfJDomTransferable.localObjectFlavor);
                    OdfJDomTreeNode fromNode = (OdfJDomTreeNode) movedPaths[0].getLastPathComponent();
                    /*
                     *we dont actually move the nodes on the tree, since the nodes get refreshed from the document
                     *we change the document and let the tree refreshe itself from the changed document
                     */
                    
                    String sourceSection = (String)fromNode.node.getAttribute(OdfJDomElement.NAME_ATTR).getValue();
                    String targetSection = (String) thisNode.node.getAttribute(OdfJDomElement.NAME_ATTR).getValue();
                    //call the macro implementation now to move the actual sections...
                    //prompt a warning yes/no before doing the move.
                    //addNodes(currentPath, model, data);
                   // Frame frame = JOptionPane.getFrameForComponent(tree);
                    createPopupMenu (sourceSection, targetSection);
                    Point ptPopup = RectToPoint(rectCoords);
                    moveMenu.show(source, ptPopup.x,  ptPopup.y);
                  
  		}
  	}
  	draggedNode = null;
  	super.exportDone(source, data, action);
       } catch (IOException ex) {
         log.error("exportDone: "+ex.getMessage());
       } catch (UnsupportedFlavorException ex) {
         log.error("exportDone:" + ex.getMessage());
       }

  }

  private Point RectToPoint(Rectangle bounds) {
            return new Point(bounds.x + bounds.width - 35,
                    bounds.y + bounds.height / 2);
        }

  private void createPopupMenu (String source, String target) {
      this.moveMenu.removeAll();
      Iterator<String> keys = theMenu.keySet().iterator();
      while (keys.hasNext()) {
             String key = keys.next();
             moveMenu.add(new moveSectionAction(key, theMenu.get(key), source, target));
      }
  }

	/**
   * Returns the type of transfer actions supported by the source.
   * This transfer handler supports moving of tree nodes so it returns MOVE.
   *
   * @return TransferHandler.MOVE
   */
    @Override
  public int getSourceActions(JComponent c) {
    return TransferHandler.MOVE;
  }

  /**
   * get a drag image from the currently dragged node (if any)
   * @param tree  the tree showing the node
   * @return  the image to draw during drag
   */
  public BufferedImage getDragImage(JTree tree) {
    BufferedImage image = null;
    try {
      if (dragPath != null) {
        Rectangle pathBounds = tree.getPathBounds(dragPath);
        TreeCellRenderer r = tree.getCellRenderer();
        OdfJDomTreeModel m = (OdfJDomTreeModel)tree.getModel();
        boolean nIsLeaf = m.isLeaf(dragPath.getLastPathComponent());
        JComponent lbl = (JComponent)r.getTreeCellRendererComponent(tree, draggedNode, false ,
        		tree.isExpanded(dragPath),nIsLeaf, 0,false);
        lbl.setBounds(pathBounds);
        image = new BufferedImage(lbl.getWidth(), lbl.getHeight(),
        		java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D graphics = image.createGraphics();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        lbl.setOpaque(false);
        lbl.paint(graphics);
        graphics.dispose();
      }
    }
    catch (RuntimeException re) {}
    return image;
  }

     class moveSectionAction extends AbstractAction {

          moveSectionAction () {
          }

          moveSectionAction (String actionId, String actionText, String sectionFrom, String sectionTo) {
                super(actionText);
                putValue("ACTION_ID", actionId);
                putValue("FROM_SECTION", sectionFrom);
                putValue("TO_SECTION", sectionTo);
            }

          public void actionPerformed(ActionEvent e) {
              Object sFrom = getValue("FROM_SECTION");
              Object sTo = getValue("TO_SECTION");
              Object action_id = getValue("ACTION_ID");
              if (sFrom != null && sTo != null ) {
                  processPopupSelection((String)sFrom, (String)sTo, (String) action_id);
              }
            }

          public void processPopupSelection(String sectionFrom, String sectionTo, String action_id ) {
              //go to selected range
              try {
                 //move action is routed through panel UI dialog as we 
                 //want to display an option to add a note
                  if (action_id.equals("0_MOVE_BEFORE")) {
                      panelRefactor.moveBefore(sectionFrom, sectionTo);
                        //move before section
                       // ref.saveDocument();
                   } else if (action_id.equals("1_MOVE_AFTER")) {
                        //move after section
                      panelRefactor.moveAfter(sectionFrom, sectionTo);
                   } else if (action_id.equals("2_MOVE_INSIDE")) {
                          //move inside section
                   } else if (action_id.equals("3_CANCEL_ACTION")) {
                          //dont do anything
                        return;
                   }
              } catch (Exception ex) {
                  
              } finally {
                  
              }
          }
    }


  /** remember the path to the currently dragged node here (got from createTransferable) */
  private OdfJDomTreeNode draggedNode;
  /** remember the currently dragged node here (got from createTransferable) */
  private TreePath dragPath;

}
