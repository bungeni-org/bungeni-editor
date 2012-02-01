/*
 *  Copyright (C) 2012 Africa i-Parliaments
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

package org.bungeni.editor.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.extutils.CommonFileFunctions;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

/**
 *
 * @author Ashok
 */
public class DocumentActionsReader {

    private static DocumentActionsReader thisInstance = null;

    private SAXBuilder saxBuilder ;

    private HashMap<String,Document> cachedActions = new HashMap<String,Document>();

    /**
    private Document routerDocument = null;
    **/
    
    private Document selectorDialogsDocument = null;

    private DocumentActionsReader() {
        saxBuilder = new SAXBuilder("org.apache.xerces.parsers.SAXParser",
                        false);

    }

    public static DocumentActionsReader getInstance() {
        if (thisInstance == null ) {
            thisInstance = new DocumentActionsReader();
        }
        return thisInstance ;
    }

    public Element getDocumentActionByName(String actionName) throws JDOMException, IOException {
       String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
       if (!this.cachedActions.containsKey(docType)) {
            String docActionsFolder = BungeniEditorProperties.get("documentActionsFolderRoot");
            String docActionsFile = CommonFileFunctions.convertRelativePathToFullPath(docActionsFolder) + File.separator + docType + ".xml";
            this.cachedActions.put(docType, saxBuilder.build(new File(docActionsFile)));
        }
        XPath xPath = XPath.newInstance("//actions[@for='" + docType + "']/action[@name='"+actionName+"']");
        return (Element) xPath.selectSingleNode(this.cachedActions.get(docType));

    }


    public  Element getRouter(String actionName) throws JDOMException, IOException {
        Element actionElement = getDocumentActionByName(actionName);
        Element actionRouter = actionElement.getChild("router");
        return actionRouter;
    }

     /**
    public Document getRouters() throws JDOMException, IOException {
        if (this.routerDocument == null) {
            String actionsFolder = BungeniEditorProperties.get("actionsFolderRoot");
            String routerFile = CommonFileFunctions.convertRelativePathToFullPath(actionsFolder) + File.separator + "routers.xml";
            this.routerDocument  = saxBuilder.build(new File(routerFile));
        }
        return this.routerDocument;
    }
    **/

    public Document getSelectorDialogs() throws JDOMException, IOException {
        if (this.selectorDialogsDocument == null) {
            String actionsFolder = BungeniEditorProperties.get("selectorDialogsFile");
            String dialogFile = CommonFileFunctions.convertRelativePathToFullPath(actionsFolder) + File.separator  
                    +"settings" + File.separator + "actions" + File.separator + "selector_dialogs.xml";
            this.selectorDialogsDocument = saxBuilder.build(new File(dialogFile));
        }
        return this.selectorDialogsDocument;
    }

    public Element getSelectorDialog (String parentDialog) throws JDOMException, IOException {
        XPath xPath = XPath.newInstance("//dialog[@class='"+ parentDialog + "']");
        Element dialogNode =  (Element) xPath.selectSingleNode(getSelectorDialogs());

        return dialogNode;
    }

    // (rm, jan 2012) - this method obtains the child 
    // dialogs of the main dialog
    public ArrayList getChildDialogs(String parentDialog) throws JDOMException, IOException {
        XPath xPath = XPath.newInstance("//dialog[@class='"+ parentDialog + "']/dialog");
        ArrayList selectorDialogs = (ArrayList) xPath.selectNodes(getSelectorDialogs());

        return selectorDialogs ;
    }


}
