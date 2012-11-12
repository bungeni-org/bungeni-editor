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
      <mcontainer name="references"/>
   </xsl:template>
</xsl:stylesheet>