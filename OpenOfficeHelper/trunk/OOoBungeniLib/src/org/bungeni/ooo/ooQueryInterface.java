package org.bungeni.ooo;

import com.sun.star.bridge.XUnoUrlResolver;

import com.sun.star.awt.*;
import com.sun.star.beans.*;
import com.sun.star.chart.*;
import com.sun.star.container.*;
import com.sun.star.document.*;
import com.sun.star.drawing.*;
import com.sun.star.frame.*;
import com.sun.star.lang.*;
import com.sun.star.rdf.*;
import com.sun.star.sheet.*;
import com.sun.star.style.XStyle;
import com.sun.star.style.XStyleFamiliesSupplier;
//import com.sun.star.style.*;
import com.sun.star.table.*;
import com.sun.star.text.*;
import com.sun.star.uno.*;
import com.sun.star.util.*;
import com.sun.star.view.*;


/**
 * This class provides a short hand for doing UNO QueryInterfacing
 * @author Ashok Hariharan
 */
public class ooQueryInterface {
    
    /** Creates a new instance of ooQueryInterface */
    public ooQueryInterface() {
    }
        
// The following are syntax sugar for UnoRuntime.queryInterface().
   
   
   
    //--------------------------------------------------
    //  Beans                   com.sun.star.beans.*
    //--------------------------------------------------
   
    static public XPropertySet XPropertySet( Object obj ) {
        return (XPropertySet) UnoRuntime.queryInterface( XPropertySet.class, obj );
    }
   
    //--------------------------------------------------
    //  Bridge                  com.sun.star.bridge.*
    //--------------------------------------------------
   
    static public XUnoUrlResolver XUnoUrlResolver( Object obj ) {
        return (XUnoUrlResolver) UnoRuntime.queryInterface( XUnoUrlResolver.class, obj );
    }
   
    //--------------------------------------------------
    //  Chart                   com.sun.star.chart.*
    //--------------------------------------------------
   
    static public XChartDocument XChartDocument( Object obj ) {
        return (XChartDocument) UnoRuntime.queryInterface( XChartDocument.class, obj );
    }
   
    static public XDiagram XDiagram( Object obj ) {
        return (XDiagram) UnoRuntime.queryInterface( XDiagram.class, obj );
    }
   
    //--------------------------------------------------
    //  Container               com.sun.star.container.*
    //--------------------------------------------------
   
    static public XIndexAccess XIndexAccess( Object obj ) {
        return (XIndexAccess) UnoRuntime.queryInterface( XIndexAccess.class, obj );
    }
   
    static public XNameAccess XNameAccess( Object obj ) {
        return (XNameAccess) UnoRuntime.queryInterface( XNameAccess.class, obj );
    }
   
    static public XNameContainer XNameContainer( Object obj ) {
        return (XNameContainer) UnoRuntime.queryInterface( XNameContainer.class, obj );
    }
   
    static public XNameReplace XNameReplace( Object obj ) {
        return (XNameReplace) UnoRuntime.queryInterface( XNameReplace.class, obj );
    }
   
    static public XNamed XNamed( Object obj ) {
        return (XNamed) UnoRuntime.queryInterface( XNamed.class, obj );
    }
   
    static public XContentEnumerationAccess XContentEnumerationAccess (Object obj) {
        return (XContentEnumerationAccess) UnoRuntime.queryInterface( XContentEnumerationAccess.class, obj );
    }
    //--------------------------------------------------
    //  Document                com.sun.star.document.*
    //--------------------------------------------------
   
    static public XEmbeddedObjectSupplier XEmbeddedObjectSupplier( Object obj ) {
        return (XEmbeddedObjectSupplier) UnoRuntime.queryInterface( XEmbeddedObjectSupplier.class, obj );
    }
   
    //--------------------------------------------------
    //  Drawing                 com.sun.star.drawing.*
    //--------------------------------------------------
   
    static public XDrawPage XDrawPage( Object obj ) {
        return (XDrawPage) UnoRuntime.queryInterface( XDrawPage.class, obj );
    }
   
    static public XDrawPageSupplier XDrawPageSupplier( Object obj ) {
        return (XDrawPageSupplier) UnoRuntime.queryInterface( XDrawPageSupplier.class, obj );
    }
   
    static public XDrawPages XDrawPages( Object obj ) {
        return (XDrawPages) UnoRuntime.queryInterface( XDrawPages.class, obj );
    }
   
    static public XDrawPagesSupplier XDrawPagesSupplier( Object obj ) {
        return (XDrawPagesSupplier) UnoRuntime.queryInterface( XDrawPagesSupplier.class, obj );
    }
   
    static public XLayerManager XLayerManager( Object obj ) {
        return (XLayerManager) UnoRuntime.queryInterface( XLayerManager.class, obj );
    }
   
    static public XLayerSupplier XLayerSupplier( Object obj ) {
        return (XLayerSupplier) UnoRuntime.queryInterface( XLayerSupplier.class, obj );
    }
   
    static public XShape XShape( Object obj ) {
        return (XShape) UnoRuntime.queryInterface( XShape.class, obj );
    }
   
    static public XShapes XShapes( Object obj ) {
        return (XShapes) UnoRuntime.queryInterface( XShapes.class, obj );
    }
   
    //--------------------------------------------------
    //  Frame                   com.sun.star.frame.*
    //--------------------------------------------------
   
    static public XComponentLoader XComponentLoader( Object obj ) {
        return (XComponentLoader) UnoRuntime.queryInterface( XComponentLoader.class, obj );
    }
   
    static public XDispatchHelper XDispatchHelper( Object obj ) {
        return (XDispatchHelper) UnoRuntime.queryInterface( XDispatchHelper.class, obj );
    }
   
    static public XDispatchProvider XDispatchProvider( Object obj ) {
        return (XDispatchProvider) UnoRuntime.queryInterface( XDispatchProvider.class, obj );
    }
   
    static public XModel XModel( Object obj ) {
        return (XModel) UnoRuntime.queryInterface( XModel.class, obj );
    }
   
    static public XStorable XStorable( Object obj ) {
        return (XStorable) UnoRuntime.queryInterface( XStorable.class, obj );
    }
   
    //--------------------------------------------------
    //  Lang                    com.sun.star.lang.*
    //--------------------------------------------------
   
    static public XMultiComponentFactory XMultiComponentFactory( Object obj ) {
        return (XMultiComponentFactory) UnoRuntime.queryInterface( XMultiComponentFactory.class, obj );
    }
   
    static public XMultiServiceFactory XMultiServiceFactory( Object obj ) {
        return (XMultiServiceFactory) UnoRuntime.queryInterface( XMultiServiceFactory.class, obj );
    }
   
   static public XServiceInfo XServiceInfo(Object obj){
        XServiceInfo xObj = (XServiceInfo)UnoRuntime.queryInterface(XServiceInfo.class, obj);
        return xObj;
    }
    //--------------------------------------------------
    //  Sheet                   com.sun.star.sheet.*
    //--------------------------------------------------
   
    static public XCellRangeAddressable XCellRangeAddressable( Object obj ) {
        return (XCellRangeAddressable) UnoRuntime.queryInterface( XCellRangeAddressable.class, obj );
    }
   
    static public XSpreadsheet XSpreadsheet( Object obj ) {
        return (XSpreadsheet) UnoRuntime.queryInterface( XSpreadsheet.class, obj );
    }
   
    static public XSpreadsheetDocument XSpreadsheetDocument( Object obj ) {
        return (XSpreadsheetDocument) UnoRuntime.queryInterface( XSpreadsheetDocument.class, obj );
    }
   
    //--------------------------------------------------
    //  Table                   com.sun.star.table.*
    //--------------------------------------------------
   
    static public XTableChart XTableChart( Object obj ) {
        return (XTableChart) UnoRuntime.queryInterface( XTableChart.class, obj );
    }
   
    static public XTableCharts XTableCharts( Object obj ) {
        return (XTableCharts) UnoRuntime.queryInterface( XTableCharts.class, obj );
    }
   
    static public XTableChartsSupplier XTableChartsSupplier( Object obj ) {
        return (XTableChartsSupplier) UnoRuntime.queryInterface( XTableChartsSupplier.class, obj );
    }
   
    //--------------------------------------------------
    //  Text                    com.sun.star.text.*
    //--------------------------------------------------
   
    static public XText XText( Object obj ) {
        return (XText) UnoRuntime.queryInterface( XText.class, obj );
    }
   
    static public XTextContent XTextContent( Object obj ) {
        return (XTextContent) UnoRuntime.queryInterface( XTextContent.class, obj );
    }
   
    
    static public XRelativeTextContentInsert XRelativeTextContentInsert( Object obj ) {
        return (XRelativeTextContentInsert) UnoRuntime.queryInterface( XRelativeTextContentInsert.class, obj );
    }
   
    static public XTextRange XTextRange(Object obj){
        XTextRange xObj = (XTextRange)UnoRuntime.queryInterface(XTextRange.class, obj);
        return xObj;
    }

    static public XTextRangeCompare XTextRangeCompare(Object obj){
        XTextRangeCompare xObj = (XTextRangeCompare)UnoRuntime.queryInterface(XTextRangeCompare.class, obj);
        return xObj;
    }

    static public XTextSection XTextSection(Object obj){
        XTextSection xObj = (XTextSection)UnoRuntime.queryInterface(XTextSection.class, obj);
        return xObj;
    }

    static public XTextDocument XTextDocument(Object obj) {
       return (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, obj);
    }
    
    static public XBookmarksSupplier XBookmarksSupplier(Object m_xComponent) {
       return (XBookmarksSupplier)UnoRuntime.queryInterface(XBookmarksSupplier.class, m_xComponent);
    }

    //--------------------------------------------------
    //  Util                    com.sun.star.util.*
    //--------------------------------------------------
   
    static public XCloseable XCloseable( Object obj ) {
        return (XCloseable) UnoRuntime.queryInterface( XCloseable.class, obj );
    }
   
    static public XNumberFormatsSupplier XNumberFormatsSupplier( Object obj ) {
        return (XNumberFormatsSupplier) UnoRuntime.queryInterface( XNumberFormatsSupplier.class, obj );
    }
   
    static public XNumberFormatTypes XNumberFormatTypes( Object obj ) {
        return (XNumberFormatTypes) UnoRuntime.queryInterface( XNumberFormatTypes.class, obj );
    }
   
    static public XRefreshable XRefreshable( Object obj ) {
        return (XRefreshable) UnoRuntime.queryInterface( XRefreshable.class, obj );
    }
    
    //--------------------------------------------------
    //  View                    com.sun.star.view.*
    //--------------------------------------------------
   
    static public XPrintable XPrintable( Object obj ) {
        return (XPrintable) UnoRuntime.queryInterface( XPrintable.class, obj );
    }
    static public XSelectionSupplier XSelectionSupplier( Object obj ) {
        return (XSelectionSupplier) UnoRuntime.queryInterface( XSelectionSupplier.class, obj );
    } 
     //--------------------------------------------------
    //  View                    com.sun.star.style.*
    //--------------------------------------------------
    
    static public XStyleFamiliesSupplier XStyleFamiliesSupplier (Object obj){
        return (XStyleFamiliesSupplier) UnoRuntime.queryInterface( XStyleFamiliesSupplier.class, obj );
    }

     //--------------------------------------------------
    //  awt                    com.sun.star.awt.*
    //--------------------------------------------------
    
    static public XTopWindow XTopWindow(Object obj) {
        return (XTopWindow) UnoRuntime.queryInterface(XTopWindow.class, obj);
    }
    
    static public XTextFieldsSupplier XTextFieldsSupplier(Object obj) {
        return (XTextFieldsSupplier)UnoRuntime.queryInterface(XTextFieldsSupplier.class, obj);
    }
    
    
    static public XTextField XTextField(Object obj) {
        return (XTextField)UnoRuntime.queryInterface(XTextField.class, obj);
    }
    
    static public XTextGraphicObjectsSupplier XTextGraphicObjectsSupplier(Object obj) {
        return (XTextGraphicObjectsSupplier)UnoRuntime.queryInterface(XTextGraphicObjectsSupplier.class, obj);
    }

    static public XEnumerationAccess XEnumerationAccess(Object obj) {
        return (XEnumerationAccess)UnoRuntime.queryInterface(XEnumerationAccess.class, obj);
    }

    static public XReferenceMarksSupplier XReferenceMarksSupplier(Object obj) {
        return (XReferenceMarksSupplier)UnoRuntime.queryInterface(XReferenceMarksSupplier.class, obj);
    }

    public static XUpdatable XUpdatable(Object obj) {
        return (XUpdatable) UnoRuntime.queryInterface(XUpdatable.class, obj);
    }

    static XModifiable XModifiable(Object obj) {
        return (XModifiable) UnoRuntime.queryInterface(XModifiable.class, obj);
    }

    public static XStyle XStyle(Object object) {
        return (XStyle) UnoRuntime.queryInterface(XStyle.class, object);
    }
    
    
    public static XParagraphCursor XParagraphCursor(Object object) {
        return (XParagraphCursor) UnoRuntime.queryInterface(XParagraphCursor.class, object);
    }

    
    public static XIndexReplace XIndexReplace(Object numIndexAccess) {
         return (XIndexReplace) UnoRuntime.queryInterface(XIndexReplace.class, numIndexAccess);
    }

    /*
     *
     * com.sun.star.rdf
     *
     *
     */

    public static XDocumentMetadataAccess XDocumentMetadataAccess(Object xModel) {
          return (XDocumentMetadataAccess) UnoRuntime.queryInterface(XDocumentMetadataAccess.class, xModel);
    }

    public static XMetadatable XMetadatable(Object xObject) {
        return (XMetadatable) UnoRuntime.queryInterface(XMetadatable.class, xObject);
    }
}