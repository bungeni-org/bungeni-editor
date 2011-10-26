<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                exclude-result-prefixes="xs"
                version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Oct 17, 2011</xd:p>
            <xd:p><xd:b>Author:</xd:b> anthony</xd:p>
            <xd:p></xd:p>
        </xd:desc>
    </xd:doc>
    
    <!-- these are input parameters to the transformation -->
    
    <xsl:variable name="country-code" select="string('ke')" />
    <xsl:variable name="for-parliament" select="string('/ke/parliament/2011-03-02')" />
    
    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="contenttype">
        <xsl:variable name="content-type" select="field[@name='type']" />
        <ontology type="document">
            <document>
                <xsl:attribute name="type" select="$content-type" />
               
            </document>
            <bungeni>
               <country><xsl:value-of select="$country-code" /></country>
                <parliament href="{$for-parliament}" />
            </bungeni>
            
            <!-- e.g. <question> or <motion> -->
            <xsl:element name="{$content-type}">
                <xsl:attribute name="isA" select="string('TLCObject')" />
                <xsl:attribute name="uri" select="field[@name='uri']" />
                <xsl:copy-of select="field[
                    @name='status' or 
                    @name='short_name' or 
                    @name='full_name' or 
                    @name='body_text'
                    ]" />
                <xsl:copy-of select="field[
                    @name='question_number' or
                    @name='question_id'
                    ]" />    
                <xsl:copy-of select="owner" />
            </xsl:element>

            <legislativeItem isA="TLCConcept">
                <xsl:copy-of select="field[
                                     @name='status_date' or 
                                     @name='registry_number' or 
                                     @name='parliamentary_item_id'
                                     ] | 
                                     changes |
                                     versions" />
                
            </legislativeItem>
            
            <xsl:copy-of select="permissions | 
                                 attached_files |
                                 itemsignatories" />
         
            <group>
                <xsl:copy-of select="ministry" />
            </group>
            
           </ontology>
    </xsl:template>

</xsl:stylesheet>