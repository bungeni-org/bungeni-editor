/*
 *  Copyright (C) 2012 windows
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

package org.bungeni.editor.panels.loadable;

/**
 * This is a composite object that stores the
 * section details/components for a specific section
 * @author Reagan Mbitiru
 */
public class SectionElementContext {

    private String textSectionElementName ;
    private String textSectionType ;
    private String textSectionParentName ;
    private int textSectionCounter ;

    public SectionElementContext(String tSectionName, String tSectionType, String tSectionParent,
            int tSectionNo) {
        textSectionElementName = tSectionName ;
        textSectionParentName = tSectionParent ;
        textSectionCounter = tSectionNo ;
        textSectionType = tSectionType ;
    }

    public String getSectionName() {
        return textSectionElementName ;
    }

    public String getSectionParentName () {
        return textSectionParentName ;
    }

    public int getSectionCounter() {
        return textSectionCounter ;
    }

   public String getSectionType() {
        return textSectionType ;
    }

    public void sectionName(String nSectionName) {
        textSectionElementName = nSectionName ;
    }

    public void setSectionParentName (String nParentSectionName) {
        textSectionParentName = nParentSectionName ;
    }

    public void setSectionCounter(int nSectionCounter) {
        textSectionCounter = nSectionCounter ;
    }

    private void setSectionType (String sectionType) {
        textSectionType =  sectionType ;
    }
}
