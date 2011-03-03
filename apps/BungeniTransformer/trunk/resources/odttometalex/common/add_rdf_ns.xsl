<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
 
    <!-- ***DOC***
        Append the rdf namespace to the odf document-contenent root element 
        -->
 
    <!-- special rule for the document element -->
    <xsl:template match="/*">
        <xsl:copy>
            <!-- Add a namespace node -->
            <xsl:namespace name="rdf" select="'http://www.w3.org/1999/02/22-rdf-syntax-ns#'"/>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>
    
    <!-- the identity template -->
    <xsl:template match="@* | node()">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>
    
   
    
</xsl:stylesheet>
