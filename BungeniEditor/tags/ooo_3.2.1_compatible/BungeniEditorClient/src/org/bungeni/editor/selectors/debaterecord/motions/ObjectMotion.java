/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.selectors.debaterecord.motions;

/**
 *
 * @author undesa
 */
public class ObjectMotion {
    String motionId;
    String motionTitle;
    String motionName;
    String motionText;
    String motionUri;
    
    public static ObjectMotion NewMotion(String motionId, String motionTitle, String motionName, String motionText, String motionUri) {
        return new ObjectMotion( motionId,  motionTitle,  motionName,  motionText,  motionUri);
    }
    
    public ObjectMotion(String motionId, String motionTitle, String motionName, String motionText, String motionUri) {
        this.motionId =motionId;
        this.motionTitle = motionTitle;
        this.motionName = motionName;
        this.motionText = motionText;
        this.motionUri = motionUri;
    }
    
    @Override
    public String toString(){
        return this.motionName;
    }
}
