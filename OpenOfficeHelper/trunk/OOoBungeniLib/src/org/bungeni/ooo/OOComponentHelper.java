package org.bungeni.ooo;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.ooo.utils.CommonExceptionUtils;

//~--- JDK imports ------------------------------------------------------------

import com.sun.star.awt.Rectangle;
import com.sun.star.awt.XWindow;
import com.sun.star.beans.IllegalTypeException;
import com.sun.star.beans.PropertyExistException;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertyContainer;
import com.sun.star.beans.XPropertySet;
import com.sun.star.beans.XPropertySetInfo;
import com.sun.star.container.ElementExistException;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XEnumeration;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.container.XIndexAccess;
import com.sun.star.container.XNameAccess;
import com.sun.star.container.XNameContainer;
import com.sun.star.container.XNamed;
import com.sun.star.document.XDocumentInfo;
import com.sun.star.document.XDocumentInfoSupplier;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XController;
import com.sun.star.frame.XDispatchHelper;
import com.sun.star.frame.XDispatchProvider;
import com.sun.star.frame.XFrame;
import com.sun.star.frame.XModel;
import com.sun.star.frame.XStorable;
import com.sun.star.io.IOException;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.script.provider.XScript;
import com.sun.star.script.provider.XScriptProvider;
import com.sun.star.script.provider.XScriptProviderSupplier;
import com.sun.star.style.XStyle;
import com.sun.star.style.XStyleFamiliesSupplier;
import com.sun.star.text.XBookmarksSupplier;
import com.sun.star.text.XReferenceMarksSupplier;
import com.sun.star.text.XText;
import com.sun.star.text.XTextColumns;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextField;
import com.sun.star.text.XTextFieldsSupplier;
import com.sun.star.text.XTextGraphicObjectsSupplier;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextRangeCompare;
import com.sun.star.text.XTextSection;
import com.sun.star.text.XTextSectionsSupplier;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.text.XTextViewCursorSupplier;
import com.sun.star.uno.Any;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.Type;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XModifiable;
import com.sun.star.xml.AttributeData;

import java.awt.Dimension;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * OpenOffice component helper class.
 * This class takes an XComponent object as input, and provides various document level helper functions via a singleton.
 * The UNO api is a complex hierarchical API, which this class attempts to simplify by providing a single point of
 * access to the API. The API attempts to mimic the flavour of the UNO API provided by OpenOffice basic - and it is fairly
 * easy to rewrite/translate OpenOffice Basic code into Java code , see {@link http://code.google.com/p/bungeni-editor/wiki/UsingTheOocomponentHelperClass }.
 * <p>
 *
 * @author Ashok Hariharan
 */
public class OOComponentHelper {

    /**
     * Semantic namespace used for setting document metadata
     */
    public static String                   ATTRIBUTE_NAMESPACE =
        "urn:akomantoso:names:tc:opendocument:xmlns:semantic-text:1.0";
    private static long                    MARGIN_MEASURE_BASE = 254;
    private static org.apache.log4j.Logger log                 =
        org.apache.log4j.Logger.getLogger(OOComponentHelper.class.getName());
    private boolean                        isXComponentNull    = true;
    private XComponent                     m_xComponent;
    private XComponentContext              m_xComponentContext;
    private xComponentListener             xEventListener;

    /**
     * Creates a new instance of OOComponentHelper
     * <p>
     * @param xComponent XComponent handle of open document
     * @param xComponentContext XComponentContext handle of openoffice controller
     */
    public OOComponentHelper(XComponent xComponent, XComponentContext xComponentContext) {
        try {
            isXComponentNull = false;
            m_xComponent     = xComponent;

            // add listener to listen for document closing events.

            /*
             * xEventListener = new xComponentListener();
             * m_xComponent.addEventListener(xEventListener);
             * m_xComponentContext = xComponentContext;
             */
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * Queries an input Object for an XServiceInfo interface, if found, returns the interface.
     * @param obj An object for which the XServiceInfo interface will be returned
     * @return The XServiceInfo interface of object passed into the function.
     */
    public XServiceInfo getServiceInfo(Object obj) {
        XServiceInfo xInfo = ooQueryInterface.XServiceInfo(obj);    // UnoRuntime.queryInterface(XServiceInfo.class, obj);

        return xInfo;
    }

    /**
     * Gets the XTextContent from the input Object. Input object is queried for XTextContent.
     * @param element Input object to getTextContent()
     * @return return an XTextContent object
     */
    public XTextContent getTextContent(Object element) {
        XTextContent xContent = ooQueryInterface.XTextContent(element);    // (XTextContent) UnoRuntime.queryInterface(XTextContent.class, element);

        return xContent;
    }

    /**
     * Returns the XTextDocument of the current openoffice document.
     * @return XTextDocument the XTextDocument handle of the current document
     */
    public XTextDocument getTextDocument() {
        XTextDocument xTextDoc = null;

        try {
            xTextDoc = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, this.m_xComponent);

            if (xTextDoc == null) {
                log.debug("getTextDocument is null");
            } else {
                log.debug("getTextDocument is not null");
            }
        } catch (Exception ex) {
            log.error("getTextDocument " + ex.getMessage());
            log.error("getTextDocument = stacktrace, " + CommonExceptionUtils.getStackTrace(ex));
        } finally {
          
        }
          return xTextDoc;
    }

    /**
     * Gets the XModel interface of the current document controller. The controller provides of the Model.
     *
     * @return the document Model (XModel) of the documents controller.
     */
    public XModel getDocumentModel() {
        return (XModel) UnoRuntime.queryInterface(XModel.class, this.m_xComponent);
    }

    /**
     * Provides access to the service manager for creating new UNO services
     * @return the service factory class for UNO
     */
    public XMultiServiceFactory getDocumentFactory() {
        XMultiServiceFactory xFactory = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class,
                                            getTextDocument());

        return xFactory;
    }

    /**
     * Creates a named UNO instance
     * @param instanceName - the full hierarchal name of the UNO instance you want to create
     * @return The created instance returned as an object
     */
    public Object createInstance(String instanceName) {
        Object newInstance = null;

        try {
            newInstance = getDocumentFactory().createInstance(instanceName);
        } catch (com.sun.star.uno.Exception ex) {
            log.error("createInstance(instanceName=" + instanceName + "); error message follows :"
                      + ex.getLocalizedMessage(), ex);
        } finally {
            
        }
        return newInstance;
    }

    /**
     * Creates a new instance of the classified instance name (e.g. com.sun.star.text.Paragraph)
     * @param instanceName - the classified instance name
     * @return a handle to the object created out of the classified instance
     */
    public Object createInstanceWithContext(String instanceName) {
        Object newInstance = null;

        try {
            newInstance = this.m_xComponentContext.getServiceManager().createInstanceWithContext(
                "com.sun.star.frame.DispatchHelper", this.m_xComponentContext);
        } catch (com.sun.star.uno.Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
        } finally {
           
        }
         return newInstance;
    }

    /**
     * Creates a text section and returns a Text Content handle to the Text section
     * @param sectionName - the name of the section to be created
     * @param numberOfColumns - the number of columns
     * @param cBackColor - the background color for the section
     * @return XTextContent handle of the section
     */
    public XTextContent createTextSection(String sectionName, short numberOfColumns, Integer cBackColor) {
        XNamed       xNamedSection   = null;
        XTextContent xSectionContent = null;

        try {

            // create a new section instance
            Object newSection = createInstance("com.sun.star.text.TextSection");

            // set the name
            xNamedSection = (XNamed) UnoRuntime.queryInterface(XNamed.class, newSection);
            xNamedSection.setName(sectionName);

            // XTextContent xSectionContent = (XTextContent)UnoRuntime.queryInterface(XTextContent.class, newSection);
            // create column instance, and set column count
            XTextColumns xColumns = (XTextColumns) UnoRuntime.queryInterface(XTextColumns.class,
                                        createInstance("com.sun.star.text.TextColumns"));

            xColumns.setColumnCount(numberOfColumns);

            // set the column count to the section
            getObjectPropertySet(xNamedSection).setPropertyValue(ooProperties.TEXT_COLUMNS, xColumns);
            getObjectPropertySet(xNamedSection).setPropertyValue(ooProperties.SECTION_BACK_COLOR, cBackColor);
            xSectionContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xNamedSection);
        } catch (PropertyVetoException ex) {
            log.error(ex.getLocalizedMessage());
        } catch (WrappedTargetException ex) {
            log.error(ex.getLocalizedMessage());
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error(ex.getLocalizedMessage());
        } catch (UnknownPropertyException ex) {
            log.error(ex.getLocalizedMessage());
        } finally {
          
        }
          return xSectionContent;
    }

    /**
     * Creates a section in the document, and returns an XTextSection object.
     * @param sectionName Name of the section to be crea
     * @param numberOfColumns Number of columns for the new section
     * @return returns an XTextContent object of the newly created section
     */
    public XTextContent createTextSection(String sectionName, short numberOfColumns) {
        XNamed       xNamedSection   = null;
        XTextContent xSectionContent = null;

        try {

            // create a new section instance
            Object newSection = createInstance("com.sun.star.text.TextSection");

            // set the name
            xNamedSection = (XNamed) UnoRuntime.queryInterface(XNamed.class, newSection);
            xNamedSection.setName(sectionName);

            // XTextContent xSectionContent = (XTextContent)UnoRuntime.queryInterface(XTextContent.class, newSection);
            // create column instance, and set column count
            XTextColumns xColumns = (XTextColumns) UnoRuntime.queryInterface(XTextColumns.class,
                                        createInstance("com.sun.star.text.TextColumns"));

            xColumns.setColumnCount(numberOfColumns);

            // set the column count to the section
            getObjectPropertySet(xNamedSection).setPropertyValue(ooProperties.TEXT_COLUMNS, xColumns);
            xSectionContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xNamedSection);
        } catch (PropertyVetoException ex) {
            log.error(ex.getLocalizedMessage());
        } catch (WrappedTargetException ex) {
            log.error(ex.getLocalizedMessage());
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error(ex.getLocalizedMessage());
        } catch (UnknownPropertyException ex) {
            log.error(ex.getLocalizedMessage());
        } finally {
           
        }
         return xSectionContent;
    }

    /**
     * Creates a sectoin with a background as an image.
     * The URL to the background image is passed as a parameter
     * @param sectionName
     * @param numberOfColumns
     * @param backGraphicURL
     * @return
     */
    public XTextContent createTextSection(String sectionName, short numberOfColumns, String backGraphicURL) {
        XNamed       xNamedSection   = null;
        XTextContent xSectionContent = null;

        try {

            // create the section
            Object newSection = createInstance("com.sun.star.text.TextSection");

            // set the name
            xNamedSection = (XNamed) UnoRuntime.queryInterface(XNamed.class, newSection);
            xNamedSection.setName(sectionName);

            // XTextContent xSectionContent = (XTextContent)UnoRuntime.queryInterface(XTextContent.class, newSection);
            // create column instance, and set column count
            XTextColumns xColumns = (XTextColumns) UnoRuntime.queryInterface(XTextColumns.class,
                                        createInstance("com.sun.star.text.TextColumns"));

            xColumns.setColumnCount(numberOfColumns);

            // set the column count to the section
            getObjectPropertySet(xNamedSection).setPropertyValue(ooProperties.TEXT_COLUMNS, xColumns);
            xSectionContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xNamedSection);

            // get the newly created section
            XPropertySet xProps     = ooQueryInterface.XPropertySet(newSection);
            String       graphicURL = this.loadGraphic(backGraphicURL);

            xProps.setPropertyValue("BackGraphic", graphicURL);
            xProps.setPropertyValue("BackGraphicFilter", "PNG - Portable Network Graphic");
            xProps.setPropertyValue("BackGraphicLocation", com.sun.star.style.GraphicLocation.TILED);
        } catch (UnknownPropertyException ex) {
            log.error("createTextSection (graphic = " + backGraphicURL + ")" + ex.getMessage());
        } catch (PropertyVetoException ex) {
            log.error("createTextSection (graphic = " + backGraphicURL + ")" + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("createTextSection (graphic = " + backGraphicURL + ")" + ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error("createTextSection (graphic = " + backGraphicURL + ")" + ex.getMessage());
        } finally {
          
        }
          return xSectionContent;
    }

    /**
     * Adds a section over the current selection (the "View" cursor)
     * @param sectionName - the name of the section to be created
     * @return the XTextContent handle to the newly created section
     */
    public XTextContent addViewSection(String sectionName) {
        XTextViewCursor viewCursor      = getViewCursor();
        XText           xText           = getViewCursor().getText();
        XTextContent    xSectionContent = createTextSection(sectionName, (short) 1);

        try {
            xText.insertTextContent(viewCursor, xSectionContent, true);
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error("addViewSection:" + ex.getMessage());
        } finally {
          
        }
          return xSectionContent;
    }

    /**
     *
     * @param sectionName
     * @param cBackColor
     * @return
     */
    public XTextContent addViewSection(String sectionName, Integer cBackColor) {
        XTextViewCursor viewCursor      = getViewCursor();
        XText           xText           = getViewCursor().getText();
        XTextContent    xSectionContent = createTextSection(sectionName, (short) 1, cBackColor);

        try {
            xText.insertTextContent(viewCursor, xSectionContent, true);
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.debug("addViewSection:" + ex.getMessage());
        } finally {
           
        }
         return xSectionContent;
    }

    /**
     *
     * @param obj
     * @return
     */
    public XPropertySet getObjectPropertySet(Object obj) {
        XPropertySet xObjProps = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, obj);

        return xObjProps;
    }

    /**
     * Checks if the XComponent handle is valid for the current object
     * @return true if valid, false if invalid
     */
    public boolean isXComponentValid() {
        return !isXComponentNull;
    }

    /**
     * This API allows adding metadata to a document section
     * @param theSection
     * @param metadataMap
     */
    public void setSectionMetadataAttributes(XTextSection theSection, HashMap<String, String> metadataMap) {
        try {

            // get the propertySet Handle for the section
            XPropertySet   theProperties = ooQueryInterface.XPropertySet(theSection);
            XNameContainer attrContainer = _getAttributeContainer(theProperties,
                                               ooProperties.SECTION_USERDEFINED_ATTRIBUTES);

            // get attribute element names
            String[] attributeNames = attrContainer.getElementNames();
            Iterator keyIter        = metadataMap.keySet().iterator();

            while (keyIter.hasNext()) {
                String mapkey   = (String) keyIter.next();
                String mapValue = metadataMap.get(mapkey);

                if (attrContainer.hasByName(mapkey)) {
                    AttributeData attrValue = (AttributeData) AnyConverter.toObject(new Type(AttributeData.class),
                                                  attrContainer.getByName(mapkey));

                    attrValue.Type  = "CDATA";
                    attrValue.Value = mapValue;
                    attrContainer.replaceByName(mapkey, attrValue);
                } else {
                    AttributeData attrNewAttribute = new AttributeData();

                    attrNewAttribute.Type  = "CDATA";
                    attrNewAttribute.Value = mapValue;
                    attrContainer.insertByName(mapkey, attrNewAttribute);
                }
            }

            theProperties.setPropertyValue(ooProperties.SECTION_USERDEFINED_ATTRIBUTES, attrContainer);
        } catch (NoSuchElementException ex) {
            log.error(ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error(ex.getMessage());
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error(ex.getMessage());
        } finally {
            return;
        }
    }

    /**
     *
     * @param sectionName
     * @param metadataMap
     */
    public void setSectionMetadataAttributes(String sectionName, HashMap<String, String> metadataMap) {
        HashMap<String, String> metadata = null;

        try {

            // get the section handle
            Object       section    = this.getTextSections().getByName(sectionName);
            XTextSection theSection = ooQueryInterface.XTextSection(section);

            // get the propertySet Handle for the section
            XPropertySet   theProperties = ooQueryInterface.XPropertySet(theSection);
            XNameContainer attrContainer = _getAttributeContainer(theProperties,
                                               ooProperties.SECTION_USERDEFINED_ATTRIBUTES);

            // get attribute element names
            String[] attributeNames = attrContainer.getElementNames();
            Iterator keyIter        = metadataMap.keySet().iterator();

            while (keyIter.hasNext()) {
                String mapkey   = (String) keyIter.next();
                String mapValue = metadataMap.get(mapkey);

                if (attrContainer.hasByName(mapkey)) {
                    AttributeData attrValue = (AttributeData) AnyConverter.toObject(new Type(AttributeData.class),
                                                  attrContainer.getByName(mapkey));

                    attrValue.Type  = "CDATA";
                    attrValue.Value = mapValue;
                    attrContainer.replaceByName(mapkey, attrValue);
                } else {
                    AttributeData attrNewAttribute = new AttributeData();

                    attrNewAttribute.Type  = "CDATA";
                    attrNewAttribute.Value = mapValue;
                    attrContainer.insertByName(mapkey, attrNewAttribute);
                }
            }

            theProperties.setPropertyValue(ooProperties.SECTION_USERDEFINED_ATTRIBUTES, attrContainer);
        } catch (NoSuchElementException ex) {
            log.error(ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error(ex.getMessage());
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error(ex.getMessage());
        } finally {
            return;
        }
    }

    /**
     *
     * @param sectionName
     * @return
     */
    public String getSectionType(String sectionName) {
        HashMap<String, String> metamap = getSectionMetadataAttributes(sectionName);

        if (metamap.containsKey("BungeniSectionType")) {
            return metamap.get("BungeniSectionType");
        } else {
            return null;
        }
    }

    /**
     *
     * @param sect
     * @return
     */
    public String getSectionType(XTextSection sect) {
        HashMap<String, String> metaMap = getSectionMetadataAttributes(sect);

        if (metaMap.containsKey("BungeniSectionType")) {
            return metaMap.get("BungeniSectionType");
        } else {
            return null;
        }
    }

    /**
     *Returns the section's custom metadata attributes in key-value map
     * @param theSection - the XTextSection handle to the section
     * @return HashMap containing the section metadata attributes
     */
    public HashMap<String, String> getSectionMetadataAttributes(XTextSection theSection) {
        HashMap<String, String> metadata = new HashMap<String, String>();

        try {
            XPropertySet   theProperties = ooQueryInterface.XPropertySet(theSection);
            XNameContainer attrContainer = _getAttributeContainer(theProperties,
                                               ooProperties.SECTION_USERDEFINED_ATTRIBUTES);

            // get attribute element names
            if (attrContainer.getElementNames().length == 0)    // no attributes available
            {
                log.debug("getSectionMetadataAttributes: no attributes available in section metadata");

                return metadata;
            }

            String[] attributeNames = attrContainer.getElementNames();

            metadata = new HashMap<String, String>();

            for (int i = 0; i < attributeNames.length; i++) {

                // get values for each attribute name
                AttributeData attrValue = (AttributeData) AnyConverter.toObject(new Type(AttributeData.class),
                                              attrContainer.getByName(attributeNames[i]));
                String strValue = attrValue.Value;

                metadata.put(attributeNames[i], strValue);
            }
        } catch (NoSuchElementException ex) {
            log.error(ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error(ex.getMessage());
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error(ex.getMessage());
        }

        return metadata;

    }

    /**
     * Wrapper API to getSectionMetadataAttributes(XTextSection)
     * @param sectionName - String name of section
     * @return
     */
    public HashMap<String, String> getSectionMetadataAttributes(String sectionName) {
        HashMap<String, String> metadata = new HashMap<String, String>();

        try {

            // get the section handle
            Object       section    = this.getTextSections().getByName(sectionName);
            XTextSection theSection = ooQueryInterface.XTextSection(section);

            metadata = getSectionMetadataAttributes(theSection);
        } catch (NoSuchElementException ex) {
            log.error(ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error(ex.getMessage());
        }    /*
              * catch (com.sun.star.lang.IllegalArgumentException ex){
              * log.error(ex.getMessage());
              * }
              */
        finally {
          
        }

          return metadata;
    }

    /**
     * Renames a section
     * @param oldName
     * @param newName
     * @return
     */
    public boolean renameSection(String oldName, String newName) {
        XTextSection renameThisSection = getSection(oldName);

        if (getTextSections().hasByName(newName)) {
            log.debug("renameSection: section with name = " + newName + " already exists");

            return false;
        }

        boolean stateProtected = false;

        if (isSectionProtected(renameThisSection)) {
            stateProtected = true;
            this.protectSection(renameThisSection, false);
        }

        XNamed sectName = ooQueryInterface.XNamed(renameThisSection);

        sectName.setName(newName);

        if (stateProtected) {
            this.protectSection(renameThisSection, true);
        }

        return true;
    }

    /**
     * Gets the current view cursor.
     * @return
     */
    public XTextViewCursor getViewCursor() {
        XTextViewCursorSupplier xViewCursorSupplier =
            (XTextViewCursorSupplier) UnoRuntime.queryInterface(XTextViewCursorSupplier.class,
                getDocumentModel().getCurrentController());
        XTextViewCursor xViewCursor = xViewCursorSupplier.getViewCursor();

        return xViewCursor;
    }

    /**
     * Provides a named access interface (XNameAccess) to the style families of the document
     * @return XNameAccess interface to the style families of the document
     */
    public XNameAccess getStyleFamilies() {
        XStyleFamiliesSupplier xStyleFamiliesSupplier = ooQueryInterface.XStyleFamiliesSupplier(getTextDocument());

        return xStyleFamiliesSupplier.getStyleFamilies();
    }

    /**
     * Returns the XDocumentInfo interface of the document
     * @return
     */
    public XDocumentInfo getDocumentInfo() {
        XDocumentInfoSupplier xdisInfoProvider =
            (XDocumentInfoSupplier) UnoRuntime.queryInterface(XDocumentInfoSupplier.class, getTextDocument());

        return xdisInfoProvider.getDocumentInfo();
    }

    /**
     * Adds a new property to the document
     * @param propertyName Property Name to Add
     * @param value Value of property to be added
     */
    public void addProperty(String propertyName, String value) {
        XPropertyContainer xDocPropertiesContainer =
            (XPropertyContainer) UnoRuntime.queryInterface(XPropertyContainer.class, getDocumentInfo());

        try {
            xDocPropertiesContainer.addProperty(propertyName, (short) 0, new Any(com.sun.star.uno.Type.STRING, value));
        } catch (PropertyExistException ex) {
            log.error("Property " + propertyName + " already Exists");
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error(ex.getLocalizedMessage(), ex);
        } catch (IllegalTypeException ex) {
            log.error(ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * Retrieves the document's properties as an array of Property objects
     * @return
     */
    public com.sun.star.beans.Property[] getDocumentProperties() {
        XDocumentInfo xInfo   = this.getDocumentInfo();
        XPropertySet  xDocSet = ooQueryInterface.XPropertySet(xInfo);

        return xDocSet.getPropertySetInfo().getProperties();
    }

    /**
     * Sets an existing property value to the input value.
     * @param propertyName Property name to to which value is to be set.
     * @param propertyValue Property value that is to be set.
     */
    public void setPropertyValue(String propertyName, String propertyValue) {
        XDocumentInfo xdi            = getDocumentInfo();
        XPropertySet  xDocProperties = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xdi);

        try {
            xDocProperties.setPropertyValue(propertyName, propertyValue);
        } catch (UnknownPropertyException ex) {
            log.error(ex.getLocalizedMessage());
        } catch (WrappedTargetException ex) {
            log.error(ex.getLocalizedMessage());
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error(ex.getLocalizedMessage());
        } catch (PropertyVetoException ex) {
            log.error(ex.getLocalizedMessage());
        }
    }

    /**
     * Retrieves a single named document property value
     * @param propertyName - name of the property to retrieve
     * @return - returns a string representation of the property value
     */
    public String getPropertyValue(String propertyName) {
        XDocumentInfo xdi            = getDocumentInfo();
        String        value          = "";
        XPropertySet  xDocProperties = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xdi);

        try {
            value = (String) xDocProperties.getPropertyValue(propertyName);

            // / value = anyUnoValue.toString();
        } catch (UnknownPropertyException ex) {
            log.error("Property " + propertyName + " does not exit");
        } catch (WrappedTargetException ex) {
            log.error(ex.getLocalizedMessage(), ex);
        } finally {
            
        }
        return value;
    }

    /**
     * Gets a named access interface to all the sections in the document
     * @return
     */
    public XNameAccess getTextSections() {
        XNameAccess xNamedSections = null;

        try {

            // get the text document, XText object
            // query interface for the textsections supplier
            XTextDocument         xDoc    = getTextDocument();
            XTextSectionsSupplier oTSSupp =
                (XTextSectionsSupplier) UnoRuntime.queryInterface(XTextSectionsSupplier.class, xDoc);

            xNamedSections = oTSSupp.getTextSections();
        } catch (Exception ex) {
            log.error("getTextSections = " + ex.getMessage());
            log.error("getTextSections , stacktrace = " + CommonExceptionUtils.getStackTrace(ex));
        } finally {
            
        }
        return xNamedSections;
    }

    /**
     * Checks if the document has a section, section name is passed as parameter.
     * @param sectionName The name of the section to be checked for existence
     * @return returns true if section exists, otherwise returns false
     */
    public boolean hasSection(String sectionName) {
        boolean bResult = false;

        try {
            if (getTextSections().hasByName(sectionName.trim())) {
                bResult = true;
            } else {
                bResult = false;
            }
        } catch (Exception ex) {
            log.error("hasSection = " + ex.getMessage());
            log.error("hasSection, stackTrace = " + CommonExceptionUtils.getStackTrace(ex));
        } finally {
          
        }
          return bResult;
    }

    /**
     * Returns the XComponent handle of the current document
     * @return
     */
    public synchronized XComponent getComponent() {
        return this.m_xComponent;
    }

    /**
     * Returns the remote service manager object for the current component context
     * @return
     */
    public XMultiComponentFactory getRemoteServiceManager() {
        return this.m_xComponentContext.getServiceManager();
    }

    /**
     * Executes a dispatch command on the currently active document component
     * @param cmd - name of the command
     * @param oProperties - command parameters
     */
    public void executeDispatch(String cmd, PropertyValue[] oProperties) {
        try {
            log.debug("executeDispatch : " + cmd);
            log.debug("executeDispatch: oProperties0 : " + oProperties[0].Name + " , " + oProperties[0].Value);
            log.debug("executeDispatch: oProperties1 : " + oProperties[1].Name + " , " + oProperties[1].Value);

            final XModel docModel = this.getDocumentModel();

            log.debug("executeDispath: modelURL " + docModel.getURL());
            log.debug("executeDispatch : getting controller");

            XController  docController = docModel.getCurrentController();
            final XFrame docFrame      = docController.getFrame();

            log.debug("executeDispatch : getting dispatch provider");

            XDispatchProvider docDispatchProvider = ooQueryInterface.XDispatchProvider(docFrame);

            log.debug("executeDispatch : getting dispatchhelper");

            final Object oDispatchHelper =
                this.getRemoteServiceManager().createInstanceWithContext("com.sun.star.frame.DispatchHelper",
                    this.m_xComponentContext);

            if (oDispatchHelper == null) {
                log.debug("executeDispatch oDispatchHelper is null!!");
            }

            XDispatchHelper xdispatchHelper = ooQueryInterface.XDispatchHelper(oDispatchHelper);

            xdispatchHelper.executeDispatch(docDispatchProvider, cmd, "", 0, oProperties);
        } catch (com.sun.star.uno.Exception ex) {
            log.error("error in exeucteDispatch " + ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * Returns a handle to the current selection
     * @return
     */
    public Object getCurrentSelection() {
        XController                          xDocController = this.getDocumentModel().getCurrentController();
        com.sun.star.view.XSelectionSupplier xSelSupplier   = ooQueryInterface.XSelectionSupplier(xDocController);
        Object                               oSelection     = xSelSupplier.getSelection();

        return oSelection;
    }

    /**
     * Get the selection range by index from the current selection
     * @param nIndex
     * @return
     */
    public XTextRange getSelectionRangeIndex (int nIndex) {
        Object selection = getCurrentSelection();
        XTextRange xSelectionRange = null;
        XServiceInfo xSelInfo = ooQueryInterface.XServiceInfo(selection);
         if ( xSelInfo.supportsService("com.sun.star.text.TextRanges") ){
                XIndexAccess xIndexAccess = ooQueryInterface.XIndexAccess(selection);
                int count = xIndexAccess.getCount();
                try {
                    //check if index is within the selection range count
                    if (nIndex <= count - 1) {
                        Object singleSelection;
                        singleSelection = xIndexAccess.getByIndex(nIndex);
                        //get selection ranges
                        xSelectionRange = ooQueryInterface.XTextRange(singleSelection);
                    }
                } catch (Exception ex) {
                    log.error("getSelectionRangeIndex", ex);
                }
         }
        return xSelectionRange;
      }
    
    /**
     * Returns a handle to the current section. Current section is determined by cursor position in the document
     * @return
     */
    public XTextSection currentSection() {
        XTextSection currentSection = null;

        try {
            XTextViewCursor loXTextCursor  = getViewCursor();
            XPropertySet    loXPropertySet = ooQueryInterface.XPropertySet(loXTextCursor);

            if (loXPropertySet.getPropertySetInfo().hasPropertyByName("TextSection")) {
                currentSection = (XTextSection) ((Any) loXPropertySet.getPropertyValue("TextSection")).getObject();
            }
        } catch (WrappedTargetException ex) {
            log.error("curentSection :  " + ex.getMessage());
        } catch (UnknownPropertyException ex) {
            log.error("curentSection :  " + ex.getMessage());
        } finally {
            
        }
        return currentSection;
    }

    /**
     * Similar to currentSection(), but returns the current section name instead of the section handle
     * @return
     */
    public synchronized String currentSectionName() {
        XTextSection    loXTextSection;
        XTextViewCursor loXTextCursor;
        XPropertySet    loXPropertySet;
        String          lstrSectionName = "";

        try {
            loXTextCursor  = getViewCursor();
            loXPropertySet = ooQueryInterface.XPropertySet(loXTextCursor);

            XPropertySetInfo xInfo = loXPropertySet.getPropertySetInfo();

            if (!xInfo.hasPropertyByName("TextSection")) {
                return lstrSectionName;
            }

            loXTextSection = (XTextSection) ((Any) loXPropertySet.getPropertyValue("TextSection")).getObject();

            if (loXTextSection != null) {
                loXPropertySet  = ooQueryInterface.XPropertySet(loXTextSection);
                lstrSectionName = (String) loXPropertySet.getPropertyValue("LinkDisplayName");
            }
        } catch (java.lang.Exception poException) {
            log.error("currentSectionName:" + poException.getLocalizedMessage());
            log.error("currentSectionName : " + CommonExceptionUtils.getStackTrace(poException));
        } finally {
           
        }
         return lstrSectionName;
    }

    /**
     * Returns the metadata namespace used for the document.
     * @return
     */
    public static String getMetadataNameSpace() {
        return ATTRIBUTE_NAMESPACE;
    }

    /**
     *
     * @return
     */
    public HashMap<String, Object> getSingleSelectionRange() {
        Object                  selection = this.getCurrentSelection();
        HashMap<String, Object> rangeMap  = new HashMap<String, Object>();
        XTextRange              xRange    = null;

        try {
            if (selection == null) {
                log.debug("getSelectedText: nothing was selected");

                return null;
            }

            XServiceInfo xSelInfo = ooQueryInterface.XServiceInfo(selection);

            if (xSelInfo.supportsService("com.sun.star.text.TextRanges")) {
                XIndexAccess xIndexAccess = ooQueryInterface.XIndexAccess(selection);
                int          count        = xIndexAccess.getCount();

                if (count == 1) {
                    Object singleSelection;

                    singleSelection = xIndexAccess.getByIndex(0);
                    xRange          = ooQueryInterface.XTextRange(singleSelection);
                    rangeMap.put("XTextRange", xRange);

                    // get the cursor for the selected range
                    XTextCursor xRangeCursor = xRange.getText().createTextCursorByRange(xRange);

                    rangeMap.put("XTextCursor", xRangeCursor);

                    // get the range comparator
                    XTextRangeCompare comparer = ooQueryInterface.XTextRangeCompare(xRange.getText());

                    rangeMap.put("XTextRangeCompare", comparer);
                }
            }
        } catch (Exception ex) {
            log.error("getSingleSelectionCursor: " + ex.getMessage());
        } finally {
           
        }
         return rangeMap;
    }

    /**
     * Determines if any text has been selected in the currently active document
     * @return true - if text was selected
     * false - if text was not selected.
     */
    public boolean isTextSelected() {
        HashMap<String, Object> rangeMap = null;

        rangeMap = getSingleSelectionRange();

        if (rangeMap == null) {
            return false;
        }

        XTextCursor rangeCursor = (XTextCursor) rangeMap.get("XTextCursor");

        if (rangeCursor.isCollapsed()) {
            return false;
        } else {
            return true;
        }
    }

    /*
     * nEdge = 0 , means left edge
     * nEdge = 1 , means right edge
     *
     */

    /**
     *
     * @param nEdge
     * @return
     */
    public XTextCursor getCursorEdgeSelection(int nEdge) {
        HashMap<String, Object> rangeSelectionMap = new HashMap<String, Object>();
        XTextRange              edgeRange         = null;
        XTextCursor             edgeCursor        = null,
                                rangeCursor       = null;
        XTextRange              theRange          = null;

        try {
            rangeSelectionMap = this.getSingleSelectionRange();

            if (rangeSelectionMap.size() > 0) {
                theRange = (XTextRange) rangeSelectionMap.get("XTextRange");

                XText theRangeText = theRange.getText();

                rangeCursor = (XTextCursor) rangeSelectionMap.get("XTextCursor");

                XTextRangeCompare rangeCompare = (XTextRangeCompare) rangeSelectionMap.get("XTextRangeCompare");

                if (rangeCompare.compareRegionStarts(rangeCursor.getEnd(), theRange) >= 0) {
                    edgeRange = ((nEdge == 0)
                                 ? theRange.getEnd()
                                 : theRange.getStart());
                } else {
                    edgeRange = ((nEdge == 0)
                                 ? theRange.getStart()
                                 : theRange.getEnd());
                }

                edgeCursor = theRangeText.createTextCursorByRange(edgeRange);

                if (nEdge == 0) {
                    edgeCursor.goRight((short) 0, false);
                } else {
                    edgeCursor.goLeft((short) 0, false);
                }
            }
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error("getCursorLeftSelection: " + ex.getMessage());
        }

        return edgeCursor;
    }

    /**
     *
     * @param xmlAttributesMap
     * @param backColor
     */
    public void setAttributesToSelectedText(HashMap xmlAttributesMap, Integer backColor) {
        Object oSelection = this.getCurrentSelection();

        try {
            if (oSelection == null) {
                log.debug("getSelectedText: nothing was selected");
            }

            XServiceInfo xSelInfo = ooQueryInterface.XServiceInfo(oSelection);

            if (xSelInfo.supportsService("com.sun.star.text.TextRanges")) {
                XIndexAccess                 xIndexAccess = ooQueryInterface.XIndexAccess(oSelection);
                int                          count        = xIndexAccess.getCount();
                com.sun.star.text.XTextRange xTextRange   = null;

                if (count == 1) {
                    Object singleSelection;

                    singleSelection = xIndexAccess.getByIndex(0);
                    xTextRange      = ooQueryInterface.XTextRange(singleSelection);

                    // get the cursor for the selected range
                    XTextCursor xRangeCursor = xTextRange.getText().createTextCursorByRange(xTextRange);

                    // get cursor propertyes
                    _setAttributesToSelectedRange(xRangeCursor, xmlAttributesMap, backColor);
                    log.debug("after adding attribute");

                    // XPropertySet xCursorProps = ooQueryInterface.XPropertySet(xRangeCursor);
                    // xCursorProps.setPropertyValue("CharBackColor", charBackColor);
                    return;
                }
            }
        } catch (com.sun.star.lang.IndexOutOfBoundsException ex) {
            log.error("setAttributesToSelectedText: " + ex.getLocalizedMessage());
        } catch (WrappedTargetException ex) {
            log.error("setAttributesToSelectedText: " + ex.getLocalizedMessage());
        }
    }

    private void _setAttributesToSelectedRange(XTextCursor xRangeCursor, HashMap xmlAttributesMap, Integer backColor) {
        XPropertySet xCursorProperties = ooQueryInterface.XPropertySet(xRangeCursor);

        log.debug("adding attribute");

        // get the attribute container from the cursor
        // the attribute container stores multiple XML properties to be set to the fragment of text
        XNameContainer nameContainer = _getAttributeContainer(xCursorProperties,
                                           ooProperties.TEXT_USERDEFINED_ATTRIBUTES);

        // parse hashmap for the different properties to be set
        Set      attribKeys  = xmlAttributesMap.keySet();
        Iterator keyIterator = attribKeys.iterator();

        while (keyIterator.hasNext()) {
            String key   = (String) keyIterator.next();
            String value = (String) xmlAttributesMap.get(key);

            // helper function converts value to AttributeData type
            nameContainer = _addAttributeToContainer(nameContainer, key, value);
        }

        _addAttributesToText(xCursorProperties, ooProperties.TEXT_USERDEFINED_ATTRIBUTES, nameContainer);

        if (backColor == 0) {
            this._addAttributeToText(xCursorProperties, "CharBackColor", backColor);
        }
    }

    private XNameContainer _getAttributeContainer(XPropertySet xProperties, String attrName) {
        XNameContainer attributeContainer = null;

        try {

            // get cursor property set
            // we wante the textuserdefinedattributes propertycontainer
            attributeContainer = (XNameContainer) AnyConverter.toObject(new Type(XNameContainer.class),
                    xProperties.getPropertyValue(attrName));
        } catch (WrappedTargetException ex) {
            log.error("getAttributeContainer:" + ex.getLocalizedMessage());
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error("getAttributeContainer:" + ex.getLocalizedMessage());
        } catch (UnknownPropertyException ex) {
            log.error("getAttributeContainer:" + ex.getLocalizedMessage());
        } finally {
            
        }
        return attributeContainer;
    }

    /**
     *
     * @param xmlAttrName
     * @param xmlAttrVal
     * @throws com.sun.star.beans.UnknownPropertyException
     * @throws com.sun.star.beans.PropertyVetoException
     * @throws com.sun.star.lang.IllegalArgumentException
     * @throws java.lang.Exception
     */
    public void setAttributeToSelectedText(String xmlAttrName, String xmlAttrVal)
            throws UnknownPropertyException, PropertyVetoException, com.sun.star.lang.IllegalArgumentException,
                   Exception {
        try {
            XController                          xDocController = this.getDocumentModel().getCurrentController();
            com.sun.star.view.XSelectionSupplier xSelSupplier   = ooQueryInterface.XSelectionSupplier(xDocController);
            Object                               oSelection     = xSelSupplier.getSelection();

            if (oSelection == null) {
                log.debug("getSelectedText: nothing was selected");
            }

            XServiceInfo xSelInfo = ooQueryInterface.XServiceInfo(oSelection);

            if (xSelInfo.supportsService("com.sun.star.text.TextRanges")) {
                XIndexAccess                 xIndexAccess = ooQueryInterface.XIndexAccess(oSelection);
                int                          count        = xIndexAccess.getCount();
                com.sun.star.text.XTextRange xTextRange   = null;

                if (count == 1) {

                    // a single range has been selected, set the textattribute metadata for it.
                    Object singleSelection = xIndexAccess.getByIndex(0);

                    xTextRange = ooQueryInterface.XTextRange(singleSelection);

                    XTextCursor xRangeCursor = xTextRange.getText().createTextCursorByRange(xTextRange);

                    log.debug("adding attribute");
                    _addAttributeToText(xRangeCursor, xmlAttrName, xmlAttrVal);
                    log.debug("after adding attribute");

                    // XPropertySet xCursorProps = ooQueryInterface.XPropertySet(xRangeCursor);
                    // xCursorProps.setPropertyValue("CharBackColor", charBackColor);
                    return;
                } else {

                    // multiple ranges have been selected, you must the textattribute metadata for it.
                    for (int i = 0; i < count; i++) {
                        xTextRange = ooQueryInterface.XTextRange(xIndexAccess.getByIndex(i));
                        log.debug("You have selected a text range: \"" + xTextRange.getString() + "\".");
                    }

                    log.debug("Multiple Selection Attributes have not been implemented yet");
                }
            }

            if (xSelInfo.supportsService("com.sun.star.text.TextGraphicObject")) {
                log.debug("You have selected a graphics.");
            }

            if (xSelInfo.supportsService("com.sun.star.text.TextTableCursor")) {
                log.debug("You have selected a text table.");
            }
        } catch (WrappedTargetException ex) {
            log.error("in getselectedtext" + ex.getLocalizedMessage());
        } catch (com.sun.star.lang.IndexOutOfBoundsException ex) {
            log.error("in getselectedtext" + ex.getLocalizedMessage());
        }
    }

    private XNameContainer _addAttributeToContainer(XNameContainer nameContainer, String xmlAttrName,
            String xmlAttrVal) {
        AttributeData attrValue = _makeAttributeCDATAvalue(xmlAttrVal);

        nameContainer = _addToContainer(nameContainer, xmlAttrName, attrValue);

        return nameContainer;
    }

    private void _addAttributeToText(XTextCursor xTextCursor, String XMLAttrName, String XMLAttrVal)
            throws UnknownPropertyException, PropertyVetoException, com.sun.star.lang.IllegalArgumentException,
                   WrappedTargetException, Exception {
        log.debug("addAtributetoText : " + xTextCursor.getString());

        XPropertySet xCursorProps = ooQueryInterface.XPropertySet(xTextCursor);

        log.debug("creating attribute");

        XNameContainer uda = createAttribute(xCursorProps, "TextUserDefinedAttributes", "CDATA", XMLAttrName,
                                 XMLAttrVal);

        log.debug("after creating attribute");
        xCursorProps.setPropertyValue("TextUserDefinedAttributes", uda);
        log.debug("after setting textAttribute");
    }

    private void _addAttributeToText(XPropertySet xCursorProperties, String propertyName, Object propertyValue) {
        try {
            xCursorProperties.setPropertyValue(propertyName, propertyValue);
        } catch (PropertyVetoException ex) {
            log.error("_addAttributeToText (" + propertyName + "): " + ex.getLocalizedMessage());
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error("_addAttributeToText (" + propertyName + "): " + ex.getLocalizedMessage());
        } catch (WrappedTargetException ex) {
            log.error("_addAttributeToText (" + propertyName + "): " + ex.getLocalizedMessage());
        } catch (UnknownPropertyException ex) {
            log.error("_addAttributeToText (" + propertyName + "): " + ex.getLocalizedMessage());
        }
    }

    private void _addAttributesToText(XPropertySet xCursorProperties, String propertyName,
                                      XNameContainer attrContainer) {
        try {
            log.debug("addAttributeStoText");
            xCursorProperties.setPropertyValue(propertyName, attrContainer);
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error("addAttributesToText : " + ex.getLocalizedMessage());
        } catch (PropertyVetoException ex) {
            log.error("addAttributesToText : " + ex.getLocalizedMessage());
        } catch (WrappedTargetException ex) {
            log.error("addAttributesToText : " + ex.getLocalizedMessage());
        } catch (UnknownPropertyException ex) {
            log.error("addAttributesToText : " + ex.getLocalizedMessage());
        }

        log.debug("end addAttributeStoText");
    }

    /**
     * Changes the background color of the selected text by setting the charbackcolor property
     * @param color the color as an integer 0x hex color code
     * @throws com.sun.star.lang.IndexOutOfBoundsException
     * @throws com.sun.star.lang.WrappedTargetException
     * @throws com.sun.star.beans.UnknownPropertyException
     * @throws com.sun.star.beans.PropertyVetoException
     * @throws com.sun.star.lang.IllegalArgumentException
     */
    public void setSelectedTextBackColor(Integer color)
            throws com.sun.star.lang.IndexOutOfBoundsException, WrappedTargetException, UnknownPropertyException,
                   PropertyVetoException, com.sun.star.lang.IllegalArgumentException {
        XController                          xDocController = this.getDocumentModel().getCurrentController();
        com.sun.star.view.XSelectionSupplier xSelSupplier   = ooQueryInterface.XSelectionSupplier(xDocController);
        Object                               oSelection     = xSelSupplier.getSelection();

        if (oSelection == null) {
            log.debug("getSelectedText: nothing was selected");
        }

        XServiceInfo xSelInfo = ooQueryInterface.XServiceInfo(oSelection);

        if (xSelInfo.supportsService("com.sun.star.text.TextRanges")) {
            XIndexAccess                 xIndexAccess = ooQueryInterface.XIndexAccess(oSelection);
            int                          count        = xIndexAccess.getCount();
            com.sun.star.text.XTextRange xTextRange   = null;

            if (count == 1) {

                // a single range has been selected, set the textattribute metadata for it.
                Object singleSelection = xIndexAccess.getByIndex(0);

                xTextRange = ooQueryInterface.XTextRange(singleSelection);

                XTextCursor  xRangeCursor = xTextRange.getText().createTextCursorByRange(xTextRange);
                XPropertySet xCursorProps = ooQueryInterface.XPropertySet(xRangeCursor);

                xCursorProps.setPropertyValue(ooProperties.CHAR_BACK_COLOR, color);
            }
        }
    }

    private XNameContainer _addToContainer(XNameContainer xContainer, String attrName, AttributeData attribute) {
        try {
            if (xContainer.hasByName(attrName)) {
                xContainer.removeByName(attrName);
            }

            xContainer.insertByName(attrName, attribute);
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error("addToContainer : " + ex.getLocalizedMessage());
        } catch (ElementExistException ex) {
            log.error("addToContainer : " + ex.getLocalizedMessage());
        } catch (WrappedTargetException ex) {
            log.error("addToContainer : " + ex.getLocalizedMessage());
        } catch (NoSuchElementException ex) {
            log.error("addToContainer : " + ex.getLocalizedMessage());
        }

        return xContainer;
    }

    /**
     *
     * @param xmlAttrValue
     * @return
     */
    public AttributeData _makeAttributeCDATAvalue(String xmlAttrValue) {
        String        nameSpace = ATTRIBUTE_NAMESPACE;
        AttributeData attr      = new AttributeData();

        attr.Namespace = nameSpace;
        attr.Type      = "CDATA";
        attr.Value     = xmlAttrValue;

        return attr;
    }

    /**
     *
     * @param xSet
     * @param propertyName
     * @param XMLAttrType
     * @param XMLAttrName
     * @param XMLAttrValue
     * @return
     * @throws com.sun.star.beans.UnknownPropertyException
     * @throws com.sun.star.lang.WrappedTargetException
     * @throws java.lang.Exception
     */
    public XNameContainer createAttribute(XPropertySet xSet, String propertyName, String XMLAttrType,
            String XMLAttrName, String XMLAttrValue)
            throws UnknownPropertyException, WrappedTargetException, Exception {
        String nameSpace = "urn:akomantoso:names:tc:opendocument:xmlns:semantic-text:1.0";    // "urn:ooo:names:tc:opendocument:xmlns:semantic-text:1.0";
        AttributeData attr = new AttributeData();

        attr.Namespace = nameSpace;
        attr.Type      = XMLAttrType;
        attr.Value     = XMLAttrValue;

        XNameContainer uda = null;

        try {
            uda = (XNameContainer) AnyConverter.toObject(new Type(XNameContainer.class),
                    xSet.getPropertyValue(propertyName));

            if (uda.hasByName(XMLAttrName)) {
                log.debug("attribute already set");

                throw new Exception("Attribute name:" + XMLAttrName + " already exists");
            }

            uda.insertByName(XMLAttrName, attr);
        } catch (com.sun.star.lang.IllegalArgumentException e) {

            // TODO Auto-generated catch block
            log.error(e.getLocalizedMessage());
        } catch (ElementExistException e) {

            // TODO Auto-generated catch block
            log.error(e.getLocalizedMessage());
        }

        return uda;
    }

    /**
     *
     */
    public void getSelectedText() {
        try {
            XController                          xDocController = this.getDocumentModel().getCurrentController();
            com.sun.star.view.XSelectionSupplier xSelSupplier   = ooQueryInterface.XSelectionSupplier(xDocController);
            Object                               oSelection     = xSelSupplier.getSelection();

            if (oSelection == null) {
                log.debug("getSelectedText: nothing was selected");
            }

            XServiceInfo xSelInfo = ooQueryInterface.XServiceInfo(oSelection);

            if (xSelInfo.supportsService("com.sun.star.text.TextRanges")) {
                XIndexAccess                 xIndexAccess = ooQueryInterface.XIndexAccess(oSelection);
                int                          count        = xIndexAccess.getCount();
                com.sun.star.text.XTextRange xTextRange   = null;

                if (count == 1) {
                    Object singleSelection = xIndexAccess.getByIndex(0);

                    xTextRange = ooQueryInterface.XTextRange(singleSelection);

                    XTextCursor xRangeCursor = xTextRange.getText().createTextCursorByRange(xTextRange);

                    log.debug("xRangecursor output = " + xRangeCursor.getString());

                    return;
                } else {
                    for (int i = 0; i < count; i++) {
                        xTextRange = ooQueryInterface.XTextRange(xIndexAccess.getByIndex(i));
                        log.debug("You have selected a text range: \"" + xTextRange.getString() + "\".");
                    }
                }
            }

            if (xSelInfo.supportsService("com.sun.star.text.TextGraphicObject")) {
                log.debug("You have selected a graphics.");
            }

            if (xSelInfo.supportsService("com.sun.star.text.TextTableCursor")) {
                log.debug("You have selected a text table.");
            }
        } catch (WrappedTargetException ex) {
            log.error("in getselectedtext" + ex.getLocalizedMessage());
        } catch (com.sun.star.lang.IndexOutOfBoundsException ex) {
            log.error("in getselectedtext" + ex.getLocalizedMessage());
        }
    }

    /**
     * Checks if a property Exists in the document
     * @param propertyName
     * @return
     */
    public boolean propertyExists(String propertyName) {
        XDocumentInfo xdi            = getDocumentInfo();
        boolean       bExists        = false;
        XPropertySet  xDocProperties = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xdi);

        try {
            Object objValue = xDocProperties.getPropertyValue(propertyName);

            bExists = true;
            log.debug("property Exists - value : " + AnyConverter.toString(objValue));
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            bExists = false;
            log.error("propertyExists - unknown property exception");
        } catch (UnknownPropertyException ex) {
            log.error("propertyExists - unknown property exception");

            // property does not exist
            bExists = false;
        } catch (WrappedTargetException ex) {
            bExists = false;
        } finally {
            return bExists;
        }
    }

    /**
     * @param strMacroName The full macro uri to call
     * @param aParams A list of string parameters
     * @return Returns the return value of the macro.
     */
    public Object executeMacro(String strMacroName, Object[] aParams) {
        try {
            System.out.println("Running macro = " + strMacroName);

            for (int i = 0; i < aParams.length; i++) {
                System.out.println("param " + i + " = " + aParams[i]);
            }

            XScriptProviderSupplier xScriptPS =
                (XScriptProviderSupplier) UnoRuntime.queryInterface(XScriptProviderSupplier.class, this.m_xComponent);

            log.debug("executemacro : " + ((xScriptPS != null)
                                           ? "null"
                                           : "not null"));

            String strScriptTemplate = "vnd.sun.star.script:BungeniLibs.Common." + strMacroName
                                       + "?language=Basic&location=application";

            log.debug("executemacro :" + strScriptTemplate);

            XScriptProvider xScriptProvider = xScriptPS.getScriptProvider();
            XScript         xScript         = xScriptProvider.getScript(strScriptTemplate);
            short[][]       aOutParamIndex  = new short[1][1];
            Object[][]      aOutParam       = new Object[1][1];

            return xScript.invoke(aParams, aOutParamIndex, aOutParam);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param inches
     * @return
     */
    public long inchesToOOoMeasure(long inches) {
        return OOComponentHelper.MARGIN_MEASURE_BASE * inches;
    }

    /**
     *
     * @param sectionName
     * @return
     */
    public boolean isSectionProtected(String sectionName) {
        XTextSection section = getSection(sectionName);

        return isSectionProtected(section);
    }

    /**
     * Checks if a section is protected
     * @param section
     * @return
     */
    public boolean isSectionProtected(XTextSection section) {
        boolean isProtected = false;

        try {
            XPropertySet childProperties = ooQueryInterface.XPropertySet(section);

            isProtected = AnyConverter.toBoolean(childProperties.getPropertyValue("IsProtected"));
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error(ex.getMessage());
        } catch (UnknownPropertyException ex) {
            log.error(ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error(ex.getMessage());
        } finally {
           
        }
         return isProtected;
    }

    /**
     *
     * @param sectionName
     * @return
     */
    public XTextSection getSection(String sectionName) {
        XTextSection section = null;

        try {
            section = ooQueryInterface.XTextSection(getTextSections().getByName(sectionName));
        } catch (WrappedTargetException ex) {
            log.error(ex.getMessage());
        } catch (NoSuchElementException ex) {
            log.error(ex.getMessage());
        } finally {
           
        }
         return section;
    }

    /**
     * Function that matches a descendant section, and uses two filters to match the descendant section :
     * 1) the section type containing the descendant section
     * 2) a name prefix for the section to match
     * @param parentSection name of section whose children need to be searched for the immediateParentType and childPrefix
     * @param immediateParentType Section type "BungeniSectionType" attribute for a containing section
     * @param childPrefix prefix of section name to match
     * @return The name of the matched section
     */
    public String getFirstMatchingDescendantSection(String parentSection, String immediateParentType,
            String childPrefix) {
        String matching = "";

        try {
            boolean      bMatchedOriginSection   = false;
            boolean      bMatchedImmediateParent = false;
            XTextSection xParentSection          = null;
            String[]     allSections             = this.getTextSections().getElementNames();

            for (String aSection : allSections) {
                if (aSection.startsWith(childPrefix)) {

                    // check if parent is actually the parentSection ..
                    XTextSection aNamedSection =
                        ooQueryInterface.XTextSection(this.getTextSections().getByName(aSection));

                    while (1 == 1) {

                        // get the parent section of the namedsection
                        xParentSection = aNamedSection.getParentSection();

                        // if the parent exists
                        if (xParentSection != null) {
                            XNamed parentNamed = ooQueryInterface.XNamed(xParentSection);
                            String parentName  = parentNamed.getName();
                            String sectionType = getSectionType(parentName);

                            if (parentName.equals(parentSection)) {

                                // parentName is the name of the parent of the matched section
                                bMatchedOriginSection = true;
                            }

                            if (sectionType != null) {
                                if (sectionType.equals(immediateParentType)) {
                                    bMatchedImmediateParent = true;
                                }
                            }

                            if (bMatchedOriginSection && bMatchedImmediateParent) {    // exit condition was satisifed...
                                break;
                            } else {

                                // parent name was not matched, go up one level again...
                                aNamedSection = xParentSection;
                            }
                        } else {

                            // nothing matched yet
                            // break from while loop
                            break;
                        }
                    }

                    // check if bMatched was true when it exited loop
                    if (bMatchedOriginSection && bMatchedImmediateParent) {    // matching parent was found , so return success
                        matching = aSection;

                        return matching;
                    }
                }
            }
        } catch (WrappedTargetException ex) {
            log.error(ex.getMessage());
        } catch (NoSuchElementException ex) {
            log.error(ex.getMessage());
        } finally {
           
        }
         return matching;
    }

    /**
     *
     * @param parentSection
     * @param childPrefix
     * @return
     */
    public String getFirstMatchingDescendantSection(String parentSection, String childPrefix) {
        String matching = "";

        try {
            boolean      bMatched       = false;
            XTextSection xParentSection = null;
            String[]     allSections    = this.getTextSections().getElementNames();

            for (String aSection : allSections) {
                if (aSection.startsWith(childPrefix)) {

                    // check if parent is actually the parentSection ..
                    XTextSection aNamedSection =
                        ooQueryInterface.XTextSection(this.getTextSections().getByName(aSection));

                    while (1 == 1) {

                        // get the parent section of the namedsection
                        xParentSection = aNamedSection.getParentSection();

                        // if the parent exists
                        if (xParentSection != null) {
                            XNamed parentNamed = ooQueryInterface.XNamed(xParentSection);
                            String parentName  = parentNamed.getName();

                            if (parentName.equals(parentSection)) {

                                // parentName is the name of the parent of the matched section
                                bMatched = true;

                                break;
                            } else {    // parent name was not matched, go up one level again...
                                aNamedSection = xParentSection;
                            }
                        } else {

                            // nothing matched yet
                            bMatched = false;

                            // break from while loop
                            break;
                        }
                    }

                    // check if bMatched was true when it exited loop
                    if (bMatched) {    // matching parent was found , so return success
                        matching = aSection;

                        return matching;
                    }
                }
            }
        } catch (WrappedTargetException ex) {
            log.error(ex.getMessage());
        } catch (NoSuchElementException ex) {
            log.error(ex.getMessage());
        } finally {
    
        }
                return matching;
    }

    /**
     *
     * @param sectionName
     * @param childPrefix
     * @return
     */
    public String getMatchingChildSection(String sectionName, String childPrefix) {
        String matching = "";

        try {
            XTextSection   section  = getSection(sectionName);
            XTextSection[] sections = section.getChildSections();

            for (int i = 0; i < sections.length; i++) {
                String childName = ooQueryInterface.XNamed(sections[i]).getName();

                if (childName.startsWith(childPrefix)) {
                    matching = childName;
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        } finally {
          
        }
          return matching;
    }

    /**
     *
     * @param sectionName
     * @param toState
     */
    public void protectSection(String sectionName, boolean toState) {
        try {
            XTextSection childSection;

            childSection = ooQueryInterface.XTextSection(getTextSections().getByName(sectionName));
            protectSection(childSection, toState);
        } catch (WrappedTargetException ex) {
            log.error(ex.getMessage());
        } catch (NoSuchElementException ex) {
            log.error(ex.getMessage());
        }
    }

    /**
     *
     * @param sParentSection
     * @param lookForSectionType
     * @return
     */
    public String getChildSectionByType(String sParentSection, String lookForSectionType) {
        XTextSection parentSection = getSection(sParentSection);

        if (parentSection == null) {
            return null;
        }

        XTextSection childSection = getChildSectionByType(parentSection, lookForSectionType);

        if (childSection == null) {
            return null;
        }

        String childName = ooQueryInterface.XNamed(childSection).getName();

        return childName;
    }

    /**
     *
     * @param parentSection
     * @param lookForSectionType
     * @return
     */
    public XTextSection getChildSectionByType(XTextSection parentSection, String lookForSectionType) {
        XTextSection[] childSections = parentSection.getChildSections();

        for (XTextSection childSection : childSections) {
            HashMap<String, String> childMeta = getSectionMetadataAttributes(childSection);

            if (childMeta.containsKey("BungeniSectionType")) {
                String sectionType = childMeta.get("BungeniSectionType");

                if (sectionType.equals(lookForSectionType)) {
                    return childSection;
                }
            }
        }

        return null;
    }


    public boolean referenceExists(String refName ) {
        return getReferenceMarks().hasByName(refName);
    }

    /**
     *
     * @return
     */
    public XNameAccess getReferenceMarks() {
        XReferenceMarksSupplier refSupplier = ooQueryInterface.XReferenceMarksSupplier(getTextDocument());
        XNameAccess             nameAccess  = refSupplier.getReferenceMarks();

        return nameAccess;
    }

    /**
     *
     * @return
     */
    public XEnumerationAccess getTextFields() {
        XTextFieldsSupplier txtSupplier = ooQueryInterface.XTextFieldsSupplier(this.m_xComponent);
        XEnumerationAccess  fieldAccess = txtSupplier.getTextFields();

        return fieldAccess;
    }

    /**
     *
     * @param section
     * @param toState
     */
    public void protectSection(XTextSection section, boolean toState) {
        try {
            XPropertySet childProperties = ooQueryInterface.XPropertySet(section);

            childProperties.setPropertyValue("IsProtected", toState);
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error(ex.getMessage());
        } catch (UnknownPropertyException ex) {
            log.error(ex.getMessage());
        } catch (PropertyVetoException ex) {
            log.error(ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error(ex.getMessage());
        }
    }

/*
    *
    *returns changed sections
    *
 */

    /**
     *
     * @return
     */
    public XNameAccess getGraphicObjects() {
        XTextGraphicObjectsSupplier gobSupplier = ooQueryInterface.XTextGraphicObjectsSupplier(this.getTextDocument());

        return gobSupplier.getGraphicObjects();
    }

    /**
     *
     * @return
     */
    public boolean isTextGraphicObjectSelected() {
        boolean         bReturn    = false;
        XTextViewCursor viewCursor = this.getViewCursor();
        Object          selection  = this.getCurrentSelection();
        XServiceInfo    xSelInfo   = ooQueryInterface.XServiceInfo(selection);

        if (xSelInfo.supportsService("com.sun.star.text.TextGraphicObject")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @return
     */
    public String getSelectedTextImageName() {
        String selectedImage = "";

        if (this.isTextGraphicObjectSelected()) {
            XTextViewCursor viewCursor = this.getViewCursor();
            Object          selection  = this.getCurrentSelection();
            XNamed          xGraphName = ooQueryInterface.XNamed(selection);

            selectedImage = xGraphName.getName();

            return selectedImage;
        } else {
            return selectedImage;
        }
    }

    /**
     *
     * @param newName
     * @return
     */
    public int setSelectedTextImageName(String newName) {
        int nReturn = -1;

        try {
            XTextViewCursor viewCursor = this.getViewCursor();
            Object          selection  = this.getCurrentSelection();
            XServiceInfo    xSelInfo   = ooQueryInterface.XServiceInfo(selection);

            if (xSelInfo.supportsService("com.sun.star.text.TextGraphicObject")) {
                XNamed xGraphName = ooQueryInterface.XNamed(selection);

                xGraphName.setName(newName);
                nReturn = 0;
            }
        } catch (Exception ex) {
            log.error("changeSelectedTextImageName: " + ex.getMessage());
            nReturn = -1;
        } finally {
            return nReturn;
        }
    }

    public String getSelectedTextStyle() {
        String selectedTextStyle = "";

        try {
            Object oSelection = this.getCurrentSelection();

            if (oSelection != null) {
                XServiceInfo xSelInfo = ooQueryInterface.XServiceInfo(oSelection);

                if (xSelInfo.supportsService("com.sun.star.text.TextRanges")) {
                    XIndexAccess                 xIndexAccess = ooQueryInterface.XIndexAccess(oSelection);
                    int                          count        = xIndexAccess.getCount();
                    com.sun.star.text.XTextRange xTextRange   = null;

                    if (count == 1) {
                        Object singleSelection;

                        singleSelection = xIndexAccess.getByIndex(0);
                        xTextRange      = ooQueryInterface.XTextRange(singleSelection);

                        XPropertySet     rangeProps = ooQueryInterface.XPropertySet(xTextRange);
                        XPropertySetInfo xPropsInfo = rangeProps.getPropertySetInfo();

                        if (xPropsInfo.hasPropertyByName("ParaStyleName")) {
                            selectedTextStyle = (String) rangeProps.getPropertyValue("ParaStyleName");
                        }
                    }
                }
            }
        } catch (com.sun.star.lang.IndexOutOfBoundsException ex) {
            log.error(ex.getClass().getName() + " " + ex.getMessage());
        } catch (UnknownPropertyException ex) {
            log.error(ex.getClass().getName() + " " + ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error(ex.getClass().getName() + " " + ex.getMessage());
        } finally {
            
        }
        return selectedTextStyle;
    }

    /**
     *
     * @param styleName
     * @return
     */
    public boolean setSelectedTextStyle(String styleName) {
        Object  oSelection = null;
        boolean bState     = true;

        try {
            oSelection = this.getCurrentSelection();

            if (oSelection != null) {
                XServiceInfo xSelInfo = ooQueryInterface.XServiceInfo(oSelection);

                if (xSelInfo.supportsService("com.sun.star.text.TextRanges")) {
                    XIndexAccess                 xIndexAccess = ooQueryInterface.XIndexAccess(oSelection);
                    int                          count        = xIndexAccess.getCount();
                    com.sun.star.text.XTextRange xTextRange   = null;

                    if (count == 1) {
                        Object singleSelection;

                        singleSelection = xIndexAccess.getByIndex(0);
                        xTextRange      = ooQueryInterface.XTextRange(singleSelection);

                        XPropertySet     rangeProps = ooQueryInterface.XPropertySet(xTextRange);
                        XPropertySetInfo xPropsInfo = rangeProps.getPropertySetInfo();

                        if (xPropsInfo.hasPropertyByName("ParaStyleName")) {
                            rangeProps.setPropertyValue("ParaStyleName", styleName);
                        }
                    }
                }
            }
        } catch (com.sun.star.lang.IndexOutOfBoundsException ex) {
            log.error(ex.getClass().getName() + " " + ex.getMessage());
            bState = false;
        } catch (UnknownPropertyException ex) {
            log.error(ex.getClass().getName() + " " + ex.getMessage());
            bState = false;
        } catch (PropertyVetoException ex) {
            log.error(ex.getClass().getName() + " " + ex.getMessage());
            bState = false;
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error(ex.getClass().getName() + " " + ex.getMessage());
            bState = false;
        } catch (WrappedTargetException ex) {
            log.error(ex.getClass().getName() + " " + ex.getMessage());
            bState = false;
        } finally {
           
        }
         return bState;
    }

    /**
     *
     * @return
     */
    public String getDocumentTitle() {
        String        strTitle = "";
        XTextDocument xDoc     = this.getTextDocument();

        strTitle = getFrameTitle(xDoc);

        return strTitle;
    }

    /**
     *
     */
    public void textFieldsRefresh() {
        XEnumerationAccess enumFields = this.getTextFields();

        ooQueryInterface.XRefreshable(enumFields).refresh();
    }

    /**
     * Gets the title of an openoffice document being viewed in the editor
     * @param xDoc XTextDocument handle of the document being viewed in the editor
     * @return returns a String containing the title of the docuemnt.
     */
    public static String getFrameTitle(XTextDocument xDoc) {
        String strTitle = "";

        try {
            XFrame xframe = xDoc.getCurrentController().getFrame();

            strTitle = (String) ooQueryInterface.XPropertySet(xframe).getPropertyValue("Title");

            int dashIndex = strTitle.lastIndexOf("-");

            if (dashIndex != -1) {
                strTitle = strTitle.substring(0, dashIndex);
            }
        } catch (WrappedTargetException ex) {
            log.error(ex.getMessage());
        } catch (UnknownPropertyException ex) {
            log.error(ex.getMessage());
        } finally {
         
        }
           return strTitle;
    }

    /**
     *
     * @param fieldName
     * @return
     */
    public XTextField getTextFieldByName(String fieldName) {
        XTextField   returnField      = null;
        XEnumeration fieldEnumeration = getTextFields().createEnumeration();

        try {
            while (fieldEnumeration.hasMoreElements()) {
                Object       aField     = fieldEnumeration.nextElement();
                XTextField   foundField = ooQueryInterface.XTextField(aField);
                XPropertySet fieldSet   = ooQueryInterface.XPropertySet(foundField);

                if (fieldSet.getPropertySetInfo().hasPropertyByName("Hint")) {
                    String foundFieldName = AnyConverter.toString(fieldSet.getPropertyValue("Hint"));

                    if (foundFieldName.equals(fieldName)) {
                        returnField = foundField;

                        break;
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("getTextFieldByName :(" + ex.getClass().getName() + ") " + ex.getMessage());
            log.debug("getTextFieldByName : " + CommonExceptionUtils.getStackTrace(ex));
        } finally {
       
        }
             return returnField;
    }

    /**
     *
     * @param aField
     */
    public void refreshTextField(XTextField aField) {
        com.sun.star.util.XUpdatable updateField = ooQueryInterface.XUpdatable(aField);

        updateField.update();
    }

    /**
     *
     * @return
     */
    public XStorable getStorable() {
        XComponent doc      = getComponent();
        XStorable  docStore = ooQueryInterface.XStorable(doc);

        return docStore;
    }

    /*
     * Dim oPageStyles , oStandardPageStyles
     * oPageStyles= thisComponent.getStyleFamilies().getByName("PageStyles")
     * oStandardPageStyles = oPageStyles.getByName("Standard")
     * Dim oColumns
     * oColumns = oStandardPageStyles.TextColumns
     * oColumns.setColumnCount(2)
     * oStandardPageStyles.TextColumns = oColumns
     */

    /**
     *
     * @return
     */
    public short getPageColumns() {
        short columns = 0;

        try {
            Object         pageStyles   = getStyleFamilies().getByName("PageStyles");
            XNameContainer xStyleFamily = ooQueryInterface.XNameContainer(pageStyles);
            Object         objStyle     = xStyleFamily.getByName("Standard");
            XStyle         theStyle     = ooQueryInterface.XStyle(objStyle);
            XPropertySet   pageProps    = ooQueryInterface.XPropertySet(theStyle);
            XTextColumns   pageColumns;

            pageColumns = (XTextColumns) AnyConverter.toObject(XTextColumns.class,
                    pageProps.getPropertyValue("TextColumns"));
            columns = pageColumns.getColumnCount();
        } catch (WrappedTargetException ex) {
            log.error("setPageColumns : " + ex.getClass().getName() + " -- " + ex.getMessage());
        } catch (NoSuchElementException ex) {
            log.error("setPageColumns : " + ex.getClass().getName() + " -- " + ex.getMessage());
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error("setPageColumns : " + ex.getClass().getName() + " -- " + ex.getMessage());
        } catch (UnknownPropertyException ex) {
            log.error("setPageColumns : " + ex.getClass().getName() + " -- " + ex.getMessage());
        } finally {
           
        }
         return columns;
    }

    /**
     *
     * @param nColumns
     */
    public void setPageColumns(short nColumns) {
        Object pageStyles = null;

        try {
            pageStyles = getStyleFamilies().getByName("PageStyles");

            XNameContainer xStyleFamily = ooQueryInterface.XNameContainer(pageStyles);
            Object         objStyle     = xStyleFamily.getByName("Standard");
            XStyle         theStyle     = ooQueryInterface.XStyle(objStyle);
            XPropertySet   pageProps    = ooQueryInterface.XPropertySet(theStyle);
            XTextColumns   pageColumns;

            pageColumns = (XTextColumns) AnyConverter.toObject(XTextColumns.class,
                    pageProps.getPropertyValue("TextColumns"));
            pageColumns.setColumnCount(nColumns);
            pageProps.setPropertyValue("TextColumns", pageColumns);
        } catch (WrappedTargetException ex) {
            log.error("setPageColumns : " + ex.getClass().getName() + " -- " + ex.getMessage());
        } catch (NoSuchElementException ex) {
            log.error("setPageColumns : " + ex.getClass().getName() + " -- " + ex.getMessage());
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error("setPageColumns : " + ex.getClass().getName() + " -- " + ex.getMessage());
        } catch (UnknownPropertyException ex) {
            log.error("setPageColumns : " + ex.getClass().getName() + " -- " + ex.getMessage());
        } catch (PropertyVetoException ex) {
            log.error("setPageColumns : " + ex.getClass().getName() + " -- " + ex.getMessage());
        }
    }

    /**
     * Function that opens an Openoffice document from a specific path.
     * This function must be run from a SwingWorker thread to avoid hanging the Swing event queue.
     * @param documentPath - path to the openoffice document.
     * @return
     */
    public static XComponent openExistingDocument(String documentPath) {
        XComponent xComponent = null;

        try {
            documentPath = BungenioOoHelper.convertPathToURL(documentPath);

            if (documentPath.length() > 0) {
                PropertyValue[] loadProps     = new com.sun.star.beans.PropertyValue[1];
                PropertyValue   xOpenProperty = new com.sun.star.beans.PropertyValue();

                xOpenProperty.Name  = "MacroExecutionMode";
                xOpenProperty.Value = com.sun.star.document.MacroExecMode.ALWAYS_EXECUTE;
                loadProps[0]        = xOpenProperty;
                xComponent          = BungenioOoHelper.getComponentLoader().loadComponentFromURL(documentPath,
                        "_blank", 0, loadProps);
            }
        } catch (Exception ex) {
            log.error("openExistingDocument : " + ex.getMessage());
            log.error("openExistingDocument : " + CommonExceptionUtils.getStackTrace(ex));
        } finally {

        }
        return xComponent;
    }

    /**
     *
     * @param documentPath
     * @return
     */
    public static XComponent openTemplate(String documentPath) {
        XComponent xComponent = null;

        try {
            documentPath = BungenioOoHelper.convertPathToURL(documentPath);

            if (documentPath.length() > 0) {
                PropertyValue[] loadProps     = new com.sun.star.beans.PropertyValue[2];
                PropertyValue   xOpenProperty = new com.sun.star.beans.PropertyValue();

                xOpenProperty.Name  = "MacroExecutionMode";
                xOpenProperty.Value = com.sun.star.document.MacroExecMode.ALWAYS_EXECUTE;
                loadProps[0]        = xOpenProperty;

                PropertyValue xTemplateProperty = new com.sun.star.beans.PropertyValue();

                xOpenProperty.Name  = "AsTemplate";
                xOpenProperty.Value = false;
                loadProps[1]        = xTemplateProperty;
                xComponent          = BungenioOoHelper.getComponentLoader().loadComponentFromURL(documentPath,
                        "_blank", 0, loadProps);
            }
        } catch (Exception ex) {
            log.error("openExistingDocument : " + ex.getMessage());
            log.error("openExistingDocument : " + CommonExceptionUtils.getStackTrace(ex));
        } finally {
          
        }
          return xComponent;
    }

    /**
     * Check if the document has been loaded from disk i.e. if it has been saved on the disk
     * @return true if the document has a valid location on disk
     */
    public boolean isDocumentOnDisk() {
        XStorable xStore = ooQueryInterface.XStorable(this.m_xComponent);
        return xStore.hasLocation();
    }

    /**
     * Checks if a document has been modifed i.e. if it requires saving
     * @return true if it requires saving
     */
    public boolean isModified() {
       XModifiable xMod = ooQueryInterface.XModifiable(this.m_xComponent);
       return xMod.isModified();
    }

    /**
     * Helper API that combines both the isModified() and isDocumentonDisk() APIs.
     *
     * @return true - if the document is not on disk. returns true if the document is on disk and has been modified, else returns false
     */
    public boolean documentRequiresSaving() {
        if (isDocumentOnDisk()) {
            return isModified();
        } else {
            return true;
        }
    }

    /**
     * Saves the currently open document associated with the OOComponentHelper object
     * @return
     */
    public boolean saveDocument() {
        boolean bState = false;

        try {
            XStorable xStore = ooQueryInterface.XStorable(this.m_xComponent);

            if (xStore.hasLocation()) {
                xStore.store();
            }

            bState = true;
        } catch (IOException ex) {
            bState = false;
            log.error("saveDocument : " + ex.getMessage());
        } finally {
            return true;
        }
    }

    /**
     * Returns the URL to the location of the document.
     * @return
     */
    public String getDocumentURL() {
        XStorable xStore = ooQueryInterface.XStorable(this.m_xComponent);

        if (xStore.hasLocation()) {
            return xStore.getLocation();
        } else {
            return null;
        }
    }

    /**
     * Checks if a particular bookmark exists in the document
     * @param bookmarkName - bookmark name to check for existence
     * @return
     */
    public boolean bookmarkExists(String bookmarkName) {
        return getBookmarks().hasByName(bookmarkName);
    }

    /**
     * Returns named index access to the Bookmarks present in the document
     * @return
     */
    public XNameAccess getBookmarks() {
        XNameAccess bookNameAccess = null;

        try {
            XBookmarksSupplier bookSupplier = ooQueryInterface.XBookmarksSupplier(this.m_xComponent);

            bookNameAccess = bookSupplier.getBookmarks();
        } catch (Exception ex) {
            log.error("getBookmarks : " + ex.getMessage());
        } finally {
           
        }
         return bookNameAccess;
    }

    public String loadGraphic(String urlToGraphic) {
        String graphicURL = "";

        try {
            Object         bitmapTable   = createInstance("com.sun.star.drawing.BitmapTable");
            XNameContainer nameContainer = ooQueryInterface.XNameContainer(bitmapTable);
            String         graphicName   = randomString();

            nameContainer.insertByName(graphicName, urlToGraphic);
            graphicURL = (String) nameContainer.getByName(graphicName);
        } catch (NoSuchElementException ex) {
            log.error("loadGraphic : " + urlToGraphic + " " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("loadGraphic : " + urlToGraphic + " " + ex.getMessage());
        } catch (ElementExistException ex) {
            log.error("loadGraphic : " + urlToGraphic + " " + ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error("loadGraphic : " + urlToGraphic + " " + ex.getMessage());
        } finally {
          
        }
          return graphicURL;
    }

    private String randomString() {
        Random r = new Random();

        return Long.toString(Math.abs(r.nextLong()), 36);
    }

    /**
     *
     * @param bookmarkName
     * @return
     */
    public boolean deleteBookmark(String bookmarkName) {
        boolean bState = false;

        try {
            Object       objBookmark     = getBookmarks().getByName(bookmarkName);
            XTextContent bookmarkContent = ooQueryInterface.XTextContent(objBookmark);

            bookmarkContent.dispose();
            bState = true;
        } catch (Exception ex) {
            log.error("deleteBookmark :" + ex.getMessage());
        } finally {
           
        }
         return bState;
    }

    /**
     *
     * @param templatePath
     * @return
     */
    public static XComponent newDocument(String templatePath) {
        XComponent xComponent = null;

        try {
            PropertyValue[] loadProps         = new com.sun.star.beans.PropertyValue[2];
            PropertyValue   xTemplateProperty = new com.sun.star.beans.PropertyValue();

            xTemplateProperty.Name  = "Template";
            xTemplateProperty.Value = true;
            loadProps[0]            = xTemplateProperty;

            com.sun.star.beans.PropertyValue xMacroExecProperty = new com.sun.star.beans.PropertyValue();

            xMacroExecProperty.Name  = "MacroExecutionMode";
            xMacroExecProperty.Value = com.sun.star.document.MacroExecMode.ALWAYS_EXECUTE;
            loadProps[1]             = xMacroExecProperty;

            if (templatePath.equals("")) {
                templatePath = "private:factory/swriter";
            } else {
                templatePath = BungenioOoHelper.convertPathToURL(templatePath);
            }

            // launch window
            xComponent = BungenioOoHelper.getComponentLoader().loadComponentFromURL(templatePath, "_blank", 0,
                    loadProps);
        } catch (Exception ex) {
            log.error("newDocument : " + ex.getMessage());
        } finally {
            
        }
        return xComponent;
    }


    /**
     * Create a reference mark over the cursor seletion
     * @param thisCursor - the cursor over which to insert
     * @param referenceName - the reference name
     */
   public void insertReferenceMark (XTextCursor thisCursor, String referenceName ) {
        //create reference mark instance
       Object referenceMark = this.createInstance("com.sun.star.text.ReferenceMark");
       XNamed xRefMark = ooQueryInterface.XNamed(referenceMark);
       //set the name for the reference mark
       xRefMark.setName(referenceName);
       XTextContent xContent = ooQueryInterface.XTextContent(xRefMark);
       try {
            //set the reference over the cursor range.
            thisCursor.getText().insertTextContent(thisCursor, xContent, true);
       } catch (Exception ex) {
           log.error("insertReferenceMark :" + ex.getMessage());
       }
   }

    /**
     *
     * @param documentPath
     * @param loader
     * @return
     */
    public static XComponent openExistingDocument(String documentPath, XComponentLoader loader) {
        XComponent xComponent = null;

        try {
            documentPath = BungenioOoHelper.convertPathToURL(documentPath);

            if (documentPath.length() > 0) {
                PropertyValue[] loadProps     = new com.sun.star.beans.PropertyValue[1];
                PropertyValue   xOpenProperty = new com.sun.star.beans.PropertyValue();

                xOpenProperty.Name  = "MacroExecutionMode";
                xOpenProperty.Value = com.sun.star.document.MacroExecMode.ALWAYS_EXECUTE;
                loadProps[0]        = xOpenProperty;
                xComponent          = loader.loadComponentFromURL(documentPath, "_blank", 0, loadProps);
            }
        } catch (Exception ex) {
            log.error("openExistingDocument : " + ex.getMessage());
            log.error("openExistingDocument : " + CommonExceptionUtils.getStackTrace(ex));
        } finally {
      
        }
              return xComponent;
    }

    /**
     * Maximises a openoffice document window frame
     * @param aComponent component handle of the window frame to be maximized.
     * @param screenSize
     */
    public static void positionOOoWindow(XComponent aComponent, Dimension screenSize) {
        try {
            XModel    aModel    = ooQueryInterface.XModel(aComponent);
            XFrame    aFrame    = aModel.getCurrentController().getFrame();
            XWindow   xWind     = aFrame.getContainerWindow();
            Rectangle rect      = xWind.getPosSize();
            int       intXPos   = rect.X;
            int       intYPos   = rect.Y;
            int       intHeight = screenSize.height - 30;    // 712;
            int       intWidth  = screenSize.width;          //
            short     nSetpos   = 15;

            xWind.setPosSize(intXPos, intYPos, intWidth, intHeight, nSetpos);
        } catch (Exception ex) {
            log.error("positionoOoWinow : " + ex.getMessage());
        }
    }

    /*
     * disabled temporarily
     *
     * public void detachListener() {
     *   m_xComponent.removeEventListener(xEventListener);
     * }
     *
     * public void attachListener() {
     *   m_xComponent.addEventListener(xEventListener);
     * }
     * disabled temporarily
     */
    class xComponentListener implements com.sun.star.lang.XEventListener {
        public void disposing(EventObject eventObject) {

            // document window is closing
            log.debug("xComponentListner : the document window is closing");
            isXComponentNull = true;
        }
    }
}
