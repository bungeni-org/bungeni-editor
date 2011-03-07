<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:bungeni="http://editor.bungeni.org/1.0/anx/">

    <!-- ***DOC***
        Append the rdf namespace to the odf document-contenent root element 
        -->

    <!-- special rule for the root document element -->
    <xsl:template match="/*">
        <xsl:copy>
            <!-- Add a namespace node -->
            <xsl:namespace name="bungeni" select="'http://editor.bungeni.org/1.0/anx/'"/>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- the identity template -->
    <xsl:template match="@* | node()">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>


    <!-- append ns prefix to all elements matching namespace -->
    <xsl:template match="bungeni:*">
        <xsl:element name="bungeni:{local-name()}">
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </xsl:element>
    </xsl:template>
    
    
</xsl:stylesheet>
