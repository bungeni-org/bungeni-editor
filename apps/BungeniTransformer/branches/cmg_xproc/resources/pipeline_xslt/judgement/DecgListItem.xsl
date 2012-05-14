<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:bp="http://www.bungeni.org/pipeline/1.0"
                xmlns:bodf="http://editor.bungeni.org/1.0/odf/"
                exclude-result-prefixes="bp"
                version="2.0">
    <xsl:output indent="yes" method="xml" encoding="UTF-8"/>

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*">
        <xsl:element name="{node-name(.)}">
            <xsl:for-each select="@*">
                <xsl:attribute name="{name(.)}">
                    <xsl:value-of select="."/>
                </xsl:attribute>
            </xsl:for-each>
            <xsl:apply-templates />
        </xsl:element>
    </xsl:template>

    <xsl:template match="*[@name='DecgListItem']" bp:name="DecgListItem">
        
        
        <item> 
           <xsl:if test="@id"> 
                <xsl:attribute name="id"><xsl:value-of select="concat(../@id,'-',@id)"/></xsl:attribute> 
           </xsl:if> 
           
           <xsl:apply-templates/>
        </item>
        
   
       
    </xsl:template>
    
    
   
    
    
    
    <xsl:template match="text()">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template> 

</xsl:stylesheet>
