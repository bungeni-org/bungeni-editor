/*
 * NodeMoveTransferHandler.java
 *
 * Created on October 4, 2007, 11:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.dialogs.tree;

import com.sun.star.animations.Event;
import java.awt.AlphaComposite;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;
import javax.swing.AbstractAction;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.bungeni.editor.macro.ExternalMacro;
import org.bungeni.editor.macro.ExternalMacroFactory;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.utils.BungeniPopupMenuHelper;
import org.bungeni.utils.MessageBox;

/**
 *
 * @author Administrator
 */
public class NodeMoveTransferHandler extends TransferHandler {
   private static org.apache.log4j.Logger log = Logger.getLogger(NodeMoveTransferHandler.class.getName());
   private org.bungeni.editor.dialogs.editorTabbedPanel parentPanel;
   private final Integer MOVE_BEFORE=0;
   private final Integer MOVE_AFTER=1;
   private JPopupMenu moveMenu = new JPopupMenu();
  // private BungeniPopupMenuHelper menuHelper;
   private TreeMap<String,String> theMenu;
  /**
   * constructor
   */
  public NodeMoveTransferHandler() {
    super();
  }

  public NodeMoveTransferHandler(org.bungeni.editor.dialogs.editorTabbedPanel mainObj) {
      super();
      parentPanel = mainObj;
      BungeniPopupMenuHelper menuHelper = new BungeniPopupMenuHelper("MoveSectionsDropMenu");
      theMenu = menuHelper.getMenus();
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
              t = new GenericTransferable(tree.getSelectionPaths());
              dragPath = tree.getSelectionPath();
              if (dragPath != null) {
                draggedNode = (MutableTreeNode) dragPath.getLastPathComponent();
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
  protected void exportDone(JComponent source, Transferable data, int action) {
      try {
  	if(source instanceof JTree) {
  		JTree tree = (JTree) source;
               DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
  	       TreePath currentPath = tree.getSelectionPath();
               Rectangle rectCoords = tree.getPathBounds(currentPath);
  		if(currentPath != null) {
                    //get the drop target node
                    DefaultMutableTreeNode thisNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();
                    //we dont handle multiple selections of the source, so the source node is always a single one.
                    TreePath[] movedPaths = (TreePath[]) data.getTransferData(DataFlavor.stringFlavor);
                    DefaultMutableTreeNode fromNode = (DefaultMutableTreeNode) movedPaths[0].getLastPathComponent();
                    /*
                     *we dont actually move the nodes on the tree, since the nodes get refreshed from the document
                     *we change the document and let the tree refreshe itself from the changed document 
                     */
                    String sourceSection = (String)fromNode.getUserObject();
                    String targetSection = (String) thisNode.getUserObject();
                    //call the macro implementation now to move the actual sections...
                    //prompt a warning yes/no before doing the move.
                    //addNodes(currentPath, model, data);
                    Frame frame = JOptionPane.getFrameForComponent(tree);
                    createPopupMenu (sourceSection, targetSection);
                    Point ptPopup = RectToPoint(rectCoords);
                    moveMenu.show(source, ptPopup.x,  ptPopup.y);
                    /*
                     *commented temporarily
                     *
                    int ret = MessageBox.Confirm(frame, "This will move Section: " + sourceSection + " and sections " +
                            "contained within it, to the position after Section: " + targetSection +".  Proceed ?", 
                            "Confirmation Required");
                    if (ret == JOptionPane.YES_OPTION) {
                        ExternalMacro MoveSection = ExternalMacroFactory.getMacroDefinition("MoveSection");
                        MoveSection.addParameter(ooDocument.getComponent());
                        MoveSection.addParameter(sourceSection);
                        MoveSection.addParameter(targetSection);
                        MoveSection.addParameter(MOVE_AFTER);
                        ooDocument.executeMacro(MoveSection.toString(), MoveSection.getParams());
                    } else if (ret == JOptionPane.NO_OPTION) {
                        //dont do anything.
                    }
                     **
                     *commented temporarily
                     */
                    parentPanel.uncheckEditModeButton();
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
   * add a number of given nodes 
   * @param currentPath  the tree path currently selected
   * @param model  tree model containing the nodes
   * @param data  nodes to add
   */
  /*
  private void addNodes(TreePath currentPath, DefaultTreeModel model, Transferable data) {
		MutableTreeNode targetNode = (MutableTreeNode) currentPath.getLastPathComponent();
		try {
			TreePath[] movedPaths = (TreePath[]) data.getTransferData(DataFlavor.stringFlavor);
			for(int i = 0; i < movedPaths.length; i++) {
				MutableTreeNode moveNode = (MutableTreeNode) movedPaths[i].getLastPathComponent();
				if(!moveNode.equals(targetNode)) {
					model.removeNodeFromParent(moveNode);
					model.insertNodeInto(moveNode, targetNode, targetNode.getChildCount());
				}
			}
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
  }
  */
  /**
   * insert a number of given nodes
   * @param tree  the tree showing the nodes
   * @param model  the model containing the nodes
   * @param data  the nodes to insert
   */
  /*
  private void insertNodes(JTree tree, DefaultTreeModel model, Transferable data) {
		Point location = ((TreeDropTarget) tree.getDropTarget()).getMostRecentDragLocation();
		TreePath path = tree.getClosestPathForLocation(location.x, location.y);
		MutableTreeNode targetNode = (MutableTreeNode) path.getLastPathComponent();
		MutableTreeNode parent = (MutableTreeNode) targetNode.getParent();
		try {
			TreePath[] movedPaths = (TreePath[]) data.getTransferData(DataFlavor.stringFlavor);
			for(int i = 0; i < movedPaths.length; i++) {
				MutableTreeNode moveNode = (MutableTreeNode) movedPaths[i].getLastPathComponent();
				if(!moveNode.equals(targetNode)) {
					model.removeNodeFromParent(moveNode);
					model.insertNodeInto(moveNode, parent, model.getIndexOfChild(parent, targetNode));
				}
			}
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
  }
*/
	/**
   * Returns the type of transfer actions supported by the source. 
   * This transfer handler supports moving of tree nodes so it returns MOVE.
   * 
   * @return TransferHandler.MOVE
   */
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
        DefaultTreeModel m = (DefaultTreeModel)tree.getModel();
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
              final OOComponentHelper ooDocument = parentPanel.getOODocumentObject();
               if (action_id.equals("0_MOVE_BEFORE")) {
                
                org.bungeni.utils.Clipboard.clear();
                
                ExternalMacro MoveSection = ExternalMacroFactory.getMacroDefinition("MoveSection");
                MoveSection.addParameter(ooDocument.getComponent());
                MoveSection.addParameter(sectionFrom);
                MoveSection.addParameter(sectionTo);
                MoveSection.addParameter(MOVE_BEFORE);
                ooDocument.executeMacro(MoveSection.toString(), MoveSection.getParams());
                
              } else if (action_id.equals("1_MOVE_AFTER")) {
              
                org.bungeni.utils.Clipboard.clear();
                
                ExternalMacro MoveSection = ExternalMacroFactory.getMacroDefinition("MoveSection");
                MoveSection.addParameter(ooDocument.getComponent());
                MoveSection.addParameter(sectionFrom);
                MoveSection.addParameter(sectionTo);
                MoveSection.addParameter(MOVE_AFTER);
                ooDocument.executeMacro(MoveSection.toString(), MoveSection.getParams());
              
              } else if (action_id.equals("2_MOVE_INSIDE")) {

              } else if (action_id.equals("3_CANCEL_ACTION")) {
                    return;
              }
          }
    }
     
     
  /** remember the path to the currently dragged node here (got from createTransferable) */
  private MutableTreeNode draggedNode;
  /** remember the currently dragged node here (got from createTransferable) */
  private TreePath dragPath;

}
