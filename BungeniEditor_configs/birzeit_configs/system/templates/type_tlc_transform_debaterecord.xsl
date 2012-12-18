<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:bungeni="http://editor.bungeni.org/1.0/anx/"
                xmlns:bodf="http://editor.bungeni.org/1.0/odf/"
                version="2.0">
   <xsl:key name="bySectionType" match="bungeni:bungenimeta"
            use="bungeni:BungeniSectionType"/>
   <xsl:key name="byInlineType" match="bungeni:bungenimeta" use="bungeni:BungeniInlineType"/>
   <xsl:key name="byAnnotationType" match="bungeni:bungenimeta"
            use="bungeni:BungeniAnnotationType"/>
   <xsl:template match="office:meta">
      <mcontainer name="references">
<!--Speech-->
<xsl:for-each select="key('bySectionType', 'Speech')">
            <xsl:element name="TLCPerson">
               <xsl:attribute name="href">
                  <xsl:value-of select="./bungeni:BungeniSpeechByURI"/>
               </xsl:attribute>
               <xsl:attribute name="id">
                  <xsl:value-of select="./bungeni:BungeniPersonID"/>
               </xsl:attribute>
               <xsl:attribute name="showAs">
                  <xsl:value-of select="./bungeni:BungeniSpeechBy"/>
               </xsl:attribute>
            </xsl:element>
            <xsl:element name="TLCRole">
               <xsl:attribute name="href">
                  <xsl:value-of select="./bungeni:BungeniSpeechAsURI"/>
               </xsl:attribute>
               <xsl:attribute name="id">
                  <xsl:value-of select="./bungeni:BungeniSpeechAs"/>
               </xsl:attribute>
               <xsl:attribute name="showAs">
                  <xsl:value-of select="./bungeni:BungeniSpeechAsDesc"/>
               </xsl:attribute>
            </xsl:element>
         </xsl:for-each>
      </mcontainer>
      <mcontainer name="proprietary">
<!--Speech-->
<xsl:for-each select="key('bySectionType', 'Speech')">
            <xsl:element name="proprietary">
               <xsl:namespace name="bg" select="''"/>
               <xsl:element name="bg:meta" namespace="">
                  <xsl:attribute name="person">
                     <xsl:value-of select="./bungeni:BungeniSpeechBy"/>
                  </xsl:attribute>
               </xsl:element>
            </xsl:element>
         </xsl:for-each>
      </mcontainer>
   </xsl:template>
</xsl:stylesheet>