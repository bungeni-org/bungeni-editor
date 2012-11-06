/*
 * imageSelected.java
 *
 * Created on January 26, 2008, 10:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.toolbar.conditions.runnable;

import org.bungeni.editor.config.BungeniEditorProperties;
import org.bungeni.editor.toolbar.conditions.BungeniToolbarCondition;
import org.bungeni.editor.toolbar.conditions.IBungeniToolbarCondition;
import org.bungeni.ooo.OOComponentHelper;

/**
 * Contextual evaluator - checks if an image has been selected in the document,
 * and that the selected image does not have a particular name.
 * 
 * e.g. imageSelectedIsNot:main_logo
 * will evaluate to false if the selected image is = main_logo
 * will evaluate to true if the selected image is not = main_logo
 * will evaluate to false if no image has been selected.
 * @author Administrator
 */
public class imageSelectedIsNot extends baseRunnableCondition {
    /** Creates a new instance of imageSelected */
    public imageSelectedIsNot() {
    }

    boolean check_imageSelectedisNot (OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
        String imageName =  condition.getConditionValue();
        boolean bObjSelected = ooDocument.isTextGraphicObjectSelected();
        if (bObjSelected) {
            //get name of the selected object
            String selectedImage = ooDocument.getSelectedTextImageName();
            if (selectedImage.equals(imageName)) 
                return false;
            else 
                return true;
        } else {
            return false;
        }
    }
    
    @Override
    public boolean runCondition(OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
        return check_imageSelectedisNot(ooDocument, condition);
    }
        



 }
