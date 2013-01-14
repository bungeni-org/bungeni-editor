<?xml version="1.0" encoding="UTF-8"?>
<!--
    This stylesheet expects the merged configuration document as an input 
    -->
<!-- We use the meta: prefix to distinguish between the xsl: namespace used by the xslt
    and the xslt we want to generate -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:xmeta="http://meta.w3.org/1999/XSL/Transform"
    xmlns:bodf="http://editor.bungeni.org/1.0/odf/" 
    exclude-result-prefixes="xs" version="2.0">
    <xsl:import href="../../system/generators/type_tlc_generator.xsl" />
</xsl:stylesheet>
