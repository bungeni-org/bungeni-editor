<?xml version="1.0" encoding="UTF-8"?>
<!--
This template :
  ** attaches the Akoma Ntoso namespace as the default namespace to the root element
  ** attaches the bungeni-ODF namespace with the bodf prefix to the root element
  ** Information in the anx and bodf namespaces are not carried over in the final output
  xml , so the assumption is there are no elements with such prefixes in the input xml to
  this xslt.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:bodf="http://editor.bungeni.org/1.0/odf/"
    version="2.0">
    <xsl:import href="../../system/transformer/xsl/add_bungeni_ns.xsl"/>
    
</xsl:stylesheet>
