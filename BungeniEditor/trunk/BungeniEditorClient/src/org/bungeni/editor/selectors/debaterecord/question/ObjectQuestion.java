/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.selectors.debaterecord.question;

/**
 *
 * @author undesa
 */
public class ObjectQuestion {
    String questionId;
    String questionTitle;
    String questionFrom;
    String questionTo;
    String questionText;
    
    public ObjectQuestion(String questionId, String questionTitle, String questionFrom, String questionTo, String questionText) {
        this.questionId = questionId;
        this.questionTitle =  questionTitle;
        this.questionFrom = questionFrom;
        this.questionTo = questionTo;
        this.questionText = questionText;
    }
    
    @Override
    public String toString(){
        return questionTitle;
    }
    
   
    
    public boolean compare(Object o){
        ObjectQuestion oq = (ObjectQuestion) o;
        if (this.questionId.equals(oq.questionId)) {
            return true;
        } else
            return false;
    }
}
