<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="2.0" 
    exclude-result-prefixes="#all" 
    xmlns="http://www.akomantoso.org/2.0" xpath-default-namespace="http://www.akomantoso.org/2.0" 
    >    
        <!-- identity transform -->
        <xsl:template match="@*|*|processing-instruction()|comment()">
            <xsl:copy>
                <xsl:apply-templates select="*|@*|text()|processing-instruction()|comment()"/>
            </xsl:copy>
        </xsl:template>
        
        
        <xsl:template match="meta">
            <meta>
                <xsl:apply-templates select="@* | node()" />
                <xsl:copy-of select="//outOfLines"/>
            </meta>
            
            
        </xsl:template>
        
        <xsl:template match="outOfLines"></xsl:template>
    
</xsl:stylesheet>