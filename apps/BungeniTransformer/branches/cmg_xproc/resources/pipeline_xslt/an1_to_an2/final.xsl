<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="2.0" 
    xmlns:xhtml="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="#all" 
    xmlns="http://www.akomantoso.org/2.0" xpath-default-namespace="http://www.akomantoso.org/2.0" 
    >
    
    
    <!-- identity transform -->
    <xsl:template match="@*|*|processing-instruction()|comment()">
        <xsl:copy>
            <xsl:apply-templates select="*|@*|text()|processing-instruction()|comment()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="*[not(node())] | *[not(node()[2]) and node()/self::text() and not(normalize-space())]"/>
    
</xsl:stylesheet>