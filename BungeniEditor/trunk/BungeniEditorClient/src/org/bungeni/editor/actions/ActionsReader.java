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
public class ActionsReader {

    private static ActionsReader thisInstance = null;

    private SAXBuilder saxBuilder ;

    private ActionsReader() {
        saxBuilder = new SAXBuilder("org.apache.xerces.parsers.SAXParser",
                        false);

    }

    public static ActionsReader getInstance() {
        if (thisInstance == null ) {
            thisInstance = new ActionsReader();
        }
        return thisInstance ;
    }

    public Element getDocumentAction(String docType) throws JDOMException, IOException {
        String docActionsFolder = BungeniEditorProperties.get("documentActionsFolderRoot");
        String docActionsFile = CommonFileFunctions.convertRelativePathToFullPath(docActionsFolder) + File.separator + BungeniEditorPropertiesHelper.getCurrentDocType() + ".xml";
        Document doc = saxBuilder.build(new File(docActionsFile));
        XPath xPath = XPath.newInstance("//actions[@for='" + docType + "']");
        return (Element) xPath.selectSingleNode(doc);
    }


    public  Element getRouter(String routerName) throws JDOMException, IOException {
        XPath xPath = XPath.newInstance("//route[@name='"+ routerName+ "']");
        Element foundNode = (Element) xPath.selectSingleNode(getRouters());
        return foundNode;
    }

    public Document getRouters() throws JDOMException, IOException {
        String actionsFolder = BungeniEditorProperties.get("actionsFolderRoot");
        String routerFile = CommonFileFunctions.convertRelativePathToFullPath(actionsFolder) + File.separator + "routers.xml";
        Document doc = saxBuilder.build(new File(routerFile));
        return doc;
        
    }

    public Document getSelectorDialogs() throws JDOMException, IOException {
        String actionsFolder = BungeniEditorProperties.get("actionsFolderRoot");
        String dialogFile = CommonFileFunctions.convertRelativePathToFullPath(actionsFolder) + File.separator + "selector_dialogs.xml";
        Document doc = saxBuilder.build(new File(dialogFile));
        return doc;
    }

    public Element getSelectorDialog (String parentDialog) throws JDOMException, IOException {
        XPath xPath = XPath.newInstance("//dialog[@class='"+ parentDialog + "']");
        Element dialogNode = (Element) xPath.selectSingleNode(getSelectorDialogs());
        return dialogNode;
    }


}
