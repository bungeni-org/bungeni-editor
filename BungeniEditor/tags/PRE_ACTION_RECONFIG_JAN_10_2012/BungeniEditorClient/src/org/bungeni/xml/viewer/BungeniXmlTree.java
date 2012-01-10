package org.bungeni.xml.viewer;

import java.io.ByteArrayInputStream;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * Description: The BungeniXmlTree class is an extension of the javax.swing.JTree class.
 * It behaves in every way like a JTree component, the difference is that additional
 * methods have been provided to facilitate the parsing of an XML document into a
 * DOM object and translating that DOM object into a viewable JTree structure.
 *
 * Adapted from original source :
 * Copyright (c) March 2001 Kyle Gabhart
 * author - Kyle Gabhart
 *
 * @author Ashok Hariharan
 * @version 1.0
 */

public class BungeniXmlTree extends JTree
{
  /**
   * This member stores the TreeNode object used to create the model for the JTree.
   * The DefaultMutableTreeNode class is defined in the javax.swing.tree package
   * and provides a default implementation of the MutableTreeNode interface.
   */
  private           DefaultMutableTreeNode      treeNode;

  /**
   * These three members are a part of the JAXP API and are used to parse the XML
   * text into a DOM object (of type Document).
   */
  private           DocumentBuilderFactory 	dbf;
  private           DocumentBuilder 		db;
  private           Document                    doc;

  /**
   * This constructor builds an BungeniXmlTree object using the XML text
   * passed in through the constructor.
   *
   * @param text A String of XML formatted text
   *
   * @exception ParserConfigurationException  This exception is potentially thrown if
   * the constructor configures the parser improperly.  It won't.
   */
  public BungeniXmlTree( String text ) throws ParserConfigurationException
  {
	this();
	refresh( text );
  } //end BungeniXmlTree( String text )

  /**
   * This constructor builds the generic portion of the BungeniXmlTree object that is true for
   * any BungeniXmlTree object.  It includes a default tree model
   *
   * @exception ParserConfigurationException  This exception is potentially thrown if
   * the constructor configures the parser improperly.  It won't.
   */
  public BungeniXmlTree() throws ParserConfigurationException
  {
      // Initialize the superclass portion of the object
      super();

      // Set basic properties for the Tree rendering
      getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
      setShowsRootHandles( true );
      setEditable( false ); // A more advanced version of this tool would allow the Tree to be editable

      // Begin by initializing the object's DOM parsing objects
      dbf = DocumentBuilderFactory.newInstance();
      dbf.setValidating( false );
      db = dbf.newDocumentBuilder();

      // Take the DOM root node and convert it to a Tree model for the JTree
      setModel( buildWelcomeTree() );
  } //end BungeniXmlTree()

   /**
    * This method takes the XML text string passed to it, parses the XML and
    * constructs the a DefaultTreeModel that can then be used to construct the
    * graphical tree structure from that data
    */
   private DefaultTreeModel buildTree( String text )
   {
      DefaultMutableTreeNode    localtreeNode;
	Node				  newNode;

      // Take the DOM root node and convert it to a Tree model for the JTree
	newNode = parseXml( text );

	if ( newNode != null )
	{
      	localtreeNode = createTreeNode( newNode );
      	return new DefaultTreeModel( localtreeNode );
	}
	else
		return null;
   } //end buildTree()

   /**
    * This method builds a default DefaultTreeModel that can then be used to construct
    * the graphical tree structure from that data
    */
   private DefaultTreeModel buildWelcomeTree()
   {
	DefaultMutableTreeNode		root;
	DefaultMutableTreeNode	 	instructions, openingDoc, editingDoc, savingDoc;
	DefaultMutableTreeNode		openingDocText, editingDocText, savingDocText;
	DefaultMutableTreeNode		development, addingFeatures, contactingKyle;

	root = new DefaultMutableTreeNode( "Welcome to XML View 1.0" );
	instructions = new DefaultMutableTreeNode( "Instructions" );
	openingDoc = new DefaultMutableTreeNode( "Opening XML Documents" );
	openingDocText = new DefaultMutableTreeNode( "When invoking the XmlEditor from the command-line, you must specify the filename." );
	editingDoc = new DefaultMutableTreeNode( "Editing an XML Document" );
	editingDocText = new DefaultMutableTreeNode( "XML text in the right hand frame can be edited directly.  The \"refresh\" button will rebuild the JTree in the left frame." );
	savingDoc = new DefaultMutableTreeNode( "Saving an XML Document" );
	savingDocText = new DefaultMutableTreeNode( "This iteration of the XmlEditor does not provide the ability to save your document.  That will come with the next article." );
	root.add( instructions );
	instructions.add( openingDoc );
	instructions.add( editingDoc );
	openingDoc.add( openingDocText );
	editingDoc.add( editingDocText );

	return new DefaultTreeModel( root );
   } //end buildWelcomeTree()

   /**
    * This method is public because it will be called by outside classes.
    * Calling this method will cause the text in the JTextArea to be read,
    * parsed into a Node object, and used to create a DefaultTreeModel
    * which is then used to create the graphical tree structure.
    */
   public void refresh( String text )
   {
	TreeModel		treeMod;

	try
	{
		// Take the DOM root node and convert it to a Tree model for the JTree
		treeMod = buildTree( text );
		if ( treeMod != null )
      		setModel( treeMod );
	}
	catch ( Throwable ex )
	{
		System.out.println ( ex.getMessage() );
	}
   } //end refresh()

   /**
    * This takes a DOM Node and recurses through the children until each one is added
    * to a DefaultMutableTreeNode. The JTree then uses this object as a tree model.
    *
    * @param root org.w3c.Node.Node
    *
    * @return Returns a DefaultMutableTreeNode object based on the root Node passed in
    */
   private DefaultMutableTreeNode createTreeNode( Node root )
   {
      DefaultMutableTreeNode  localtreeNode = null;
      String                  type, name, value;
      NamedNodeMap            attribs;
      Node                    attribNode;

      // Get data from root node
      type = getNodeType( root );
      name = root.getNodeName();
      value = root.getNodeValue();

      // Special case for TEXT_NODE
      localtreeNode = new DefaultMutableTreeNode( root.getNodeType() == Node.TEXT_NODE ? value : name );

      // Display the attributes if there are any
      attribs = root.getAttributes();
      if( attribs != null )
      {
         for( int i = 0; i < attribs.getLength(); i++ )
         {
            attribNode = attribs.item(i);
            name = attribNode.getNodeName().trim();
            value = attribNode.getNodeValue().trim();

            if ( value != null )
            {
               if ( value.length() > 0 )
               {
                  localtreeNode.add( new DefaultMutableTreeNode( "@" + name + "=\"" + value + "\"" ) );
               } //end if ( value.length() > 0 )
            } //end if ( value != null )
         } //end for( int i = 0; i < attribs.getLength(); i++ )
      } //end if( attribs != null )

      // Recurse children nodes if any exist
      if( root.hasChildNodes() )
      {
         NodeList             children;
         int                  numChildren;
         Node                 node;
         String               data;

         children = root.getChildNodes();
         // Only recurse if Child Nodes are non-null
         if( children != null )
         {
            numChildren = children.getLength();

            for (int i=0; i < numChildren; i++)
            {
               node = children.item(i);
               if( node != null )
               {
                  // A special case could be made for each Node type.
                  if( node.getNodeType() == Node.ELEMENT_NODE )
                  {
                     localtreeNode.add( createTreeNode(node) );
                  } //end if( node.getNodeType() == Node.ELEMENT_NODE )

                  data = node.getNodeValue();

                  if( data != null )
                  {
                     data = data.trim();
                     if ( !data.equals("\n") && !data.equals("\r\n") && data.length() > 0 )
                     {
                        localtreeNode.add(createTreeNode(node));
                     } //end if ( !data.equals("\n") && !data.equals("\r\n") && data.length() > 0 )
                  } //end if( data != null )
               } //end if( node != null )
            } //end for (int i=0; i < numChildren; i++)
         } //end if( children != null )
      } //end if( root.hasChildNodes() )
      return localtreeNode;
   } //end createTreeNode( Node root )

   /**
    * This method returns a string representing the type of node passed in.
    *
    * @param node org.w3c.Node.Node
    *
    * @return Returns a String representing the node type
    */
   private String getNodeType( Node node )
   {
      String type;

      switch( node.getNodeType() )
      {
         case Node.ELEMENT_NODE:
         {
            type = "Element";
            break;
         }
         case Node.ATTRIBUTE_NODE:
         {
            type = "Attribute";
            break;
         }
         case Node.TEXT_NODE:
         {
            type = "Text";
            break;
         }
         case Node.CDATA_SECTION_NODE:
         {
            type = "CData section";
            break;
         }
         case Node.ENTITY_REFERENCE_NODE:
         {
            type = "Entity reference";
            break;
         }
         case Node.ENTITY_NODE:
         {
            type = "Entity";
            break;
         }
         case Node.PROCESSING_INSTRUCTION_NODE:
         {
            type = "Processing instruction";
            break;
         }
         case Node.COMMENT_NODE:
         {
            type = "Comment";
            break;
         }
         case Node.DOCUMENT_NODE:
         {
            type = "Document";
            break;
         }
         case Node.DOCUMENT_TYPE_NODE:
         {
            type = "Document type";
            break;
         }
         case Node.DOCUMENT_FRAGMENT_NODE:
         {
            type = "Document fragment";
            break;
         }
         case Node.NOTATION_NODE:
         {
            type = "Notation";
            break;
         }
         default:
         {
            type = "???";
            break;
         }
      }// end switch( node.getNodeType() )
      return type;
   } //end getNodeType()

   /**
    * This method performs the actual parsing of the XML text
    *
    * @param text A String representing an XML document
    * @return Returns an org.w3c.Node.Node object
    */
   private Node parseXml( String xml )
   {
	ByteArrayInputStream	byteStream;

	byteStream = new ByteArrayInputStream( xml.getBytes() );

      try
      {
         doc = db.parse( byteStream );
      }
      catch ( Exception e )
      {
        System.out.println( e.getMessage() );
	  return null;
      }
      return ( Node )doc.getDocumentElement();
   } //end parseXml

} //end class BungeniXmlTree