/*
 *  Copyright (C) 2011 undesa
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
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

package org.bungeni.editor.selectors.debaterecord.question;

/**
 *
 * @author Ashok Hariharan
 */
public class ObjectQuestionType {
    private String ontologyURI;
    private String ontologyShowAs;

    public ObjectQuestionType(){
        ontologyURI = "";
        ontologyShowAs = "";
    }

    public ObjectQuestionType(String uri, String showAs) {
        this.ontologyURI = uri;
        this.ontologyShowAs = showAs;
    }

    /**
     * @return the ontologyURI
     */
    public String getOntologyURI() {
        return ontologyURI;
    }

    /**
     * @param ontologyURI the ontologyURI to set
     */
    public void setOntologyURI(String ontologyURI) {
        this.ontologyURI = ontologyURI;
    }

    /**
     * @return the ontologyShowAs
     */
    public String getOntologyShowAs() {
        return ontologyShowAs;
    }

    /**
     * @param ontologyShowAs the ontologyShowAs to set
     */
    public void setOntologyShowAs(String ontologyShowAs) {
        this.ontologyShowAs = ontologyShowAs;
    }

    @Override
    public String toString(){
        return this.ontologyShowAs;
    }

    public boolean compare(Object o){
        ObjectQuestionType oq = (ObjectQuestionType) o;
        if (this.ontologyURI.equals(oq.ontologyURI)) {
            return true;
        } else
            return false;
    }
    

}
