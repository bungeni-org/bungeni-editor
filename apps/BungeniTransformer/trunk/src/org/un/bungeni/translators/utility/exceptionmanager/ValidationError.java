package org.un.bungeni.translators.utility.exceptionmanager;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;

public class ValidationError {
    ArrayList<String> fullErrorMessage = new ArrayList<String>(0);
    int               colNo;
    int               lineNo;
    String            missingAtributeFor;
    String            missingAttribute;
    String            parentSection;
    String            parentSectionType;
    String            sectionId;
    String            sectionType;
    String            startingWords;

    public ValidationError() {}

    public int getLineNo() {
        return lineNo;
    }

    public String Xml_lineNo() {
        return "<line>" + lineNo + "</line>\n";
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    public int getColNo() {
        return colNo;
    }

    public String Xml_colNo() {
        return "<col>" + colNo + "</col>\n";
    }

    public void setColNo(int colNo) {
        this.colNo = colNo;
    }

    public ArrayList<String> getFullErrorMessage() {
        return fullErrorMessage;
    }

    public String Xml_fullErrorMessage() {
        StringBuffer sb = new StringBuffer();

        sb.append("<msgs>\n");

        for (String msg : fullErrorMessage) {
            sb.append("\t\t<msg>" + msg + "</msg>\n");
        }

        sb.append("</msgs>\n");

        return sb.toString();
    }

    public void setFullErrorMessage(ArrayList<String> fullErrorMessage) {
        this.fullErrorMessage = fullErrorMessage;
    }

    public String getSectionType() {
        return sectionType;
    }

    public String Xml_sectionType() {
        return "<sectionType>" + sectionType + "</sectionType>\n";
    }

    public void setSectionType(String sectionType) {
        this.sectionType = sectionType;
    }

    public String getSectionId() {
        return sectionId;
    }

    public String Xml_sectionId() {
        return "<sectionId>" + getSectionId() + "</sectionId>\n";
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getStartingWords() {
        return startingWords;
    }

    public String Xml_startingWords() {
        return "<startingWords>" + getStartingWords() + "</startingWords>\n";
    }

    public void setStartingWords(String startingWords) {
        this.startingWords = startingWords;
    }

    public String getParentSection() {
        return parentSection;
    }

    public String Xml_parentSection() {
        return "<parentSection>" + getParentSection() + "</parentSection>\n";
    }

    public void setParentSection(String parentSection) {
        this.parentSection = parentSection;
    }

    public String getParentSectionType() {
        return parentSectionType;
    }

    public String Xml_parentSectionType() {
        return "<parentSectionType>" + getParentSectionType() + "</parentSectionType>\n";
    }

    public void setParentSectionType(String parentSectionType) {
        this.parentSectionType = parentSectionType;
    }

    public String getMissingAttribute() {
        return missingAttribute;
    }

    public String Xml_missingAttribute() {
        return "<attributeMissing>" + getMissingAttribute() + "</attributeMissing>\n";
    }

    public void setMissingAttribute(String missingAttribute) {
        this.missingAttribute = missingAttribute;
    }

    public String getMissingAtributeFor() {
        return missingAtributeFor;
    }

    public String Xml_missingAttributeFor() {
        return "<attributeMissingFor>" + getMissingAtributeFor() + "</attributeMissingFor>\n";
    }

    public void setMissingAtributeFor(String missingAtributeFor) {
        this.missingAtributeFor = missingAtributeFor;
    }

    public String getXmlString() {
        StringBuffer sbf = new StringBuffer();

        sbf.append("<validationError>\n");
        sbf.append(this.Xml_sectionType());
        sbf.append(this.Xml_sectionId());
        sbf.append(this.Xml_startingWords());
        sbf.append(this.Xml_parentSection());
        sbf.append(this.Xml_parentSectionType());
        sbf.append(this.Xml_startingWords());
        sbf.append(this.Xml_missingAttribute());
        sbf.append(this.Xml_missingAttributeFor());
        sbf.append(this.Xml_lineNo());
        sbf.append(this.Xml_colNo());
        sbf.append(this.Xml_fullErrorMessage());
        sbf.append("</validationError>");

        return sbf.toString();
    }
}
