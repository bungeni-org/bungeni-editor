<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:bodf="http://editor.bungeni.org/1.0/odf/"
    version="2.0">
    <xsl:output indent="yes" method="xml"/>

  <xsl:template match="@*|*|processing-instruction()|comment()">
    <xsl:copy>
      <xsl:apply-templates select="*|@*|text()|processing-instruction()|comment()"/>
    </xsl:copy>
  </xsl:template>


  <xsl:template match="p[child::from]">
    <xsl:copy-of select="from" />
  </xsl:template>
    
</xsl:stylesheet>