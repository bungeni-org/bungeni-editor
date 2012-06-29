<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xmeta="http://meta.w3.org/1999/XSL/Transform"
    version="2.0">
    
    <xsl:output indent="yes" method="xml" />
    <xsl:namespace-alias stylesheet-prefix="xmeta" result-prefix="xsl"/>
    
    
    <!-- the input document for this template is the metadatas configuration file
            settings/config/metadata/<doctype>.xml -->
    <xsl:template match="metadatas">
     <xmeta:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
            xmlns:xs="http://www.w3.org/2001/XMLSchema"
            xmlns:xmeta="http://meta.w3.org/1999/XSL/Transform"
            xmlns:bodf="http://editor.bungeni.org/1.0/odf/" 
            xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0"
            xmlns:style="urn:oasis:names:tc:opendocument:xmlns:style:1.0"
            xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
            xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0"
            xmlns:draw="urn:oasis:names:tc:opendocument:xmlns:drawing:1.0"
            xmlns:fo="urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0"
            xmlns:xlink="http://www.w3.org/1999/xlink" 
            xmlns:dc="http://purl.org/dc/elements/1.1/"
            xmlns:meta="urn:oasis:names:tc:opendocument:xmlns:meta:1.0"
            xmlns:number="urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0"
            xmlns:svg="urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0"
            xmlns:chart="urn:oasis:names:tc:opendocument:xmlns:chart:1.0"
            xmlns:dr3d="urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0"
            xmlns:math="http://www.w3.org/1998/Math/MathML"
            xmlns:form="urn:oasis:names:tc:opendocument:xmlns:form:1.0"
            xmlns:script="urn:oasis:names:tc:opendocument:xmlns:script:1.0"
            xmlns:ooo="http://openoffice.org/2004/office" 
            xmlns:ooow="http://openoffice.org/2004/writer"
            xmlns:oooc="http://openoffice.org/2004/calc" 
            xmlns:dom="http://www.w3.org/2001/xml-events"
            xmlns:xforms="http://www.w3.org/2002/xforms" 
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xmlns:of="urn:oasis:names:tc:opendocument:xmlns:of:1.2"
            xmlns:rdfa="http://docs.oasis-open.org/opendocument/meta/rdfa#"
            xmlns:field="urn:openoffice:names:experimental:ooo-ms-interop:xmlns:field:1.0"
            xmlns:rpt="http://openoffice.org/2005/report"
            xmlns:anx="http://anx.akomantoso.org/1.0"
            xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" 
            xmlns:bungeni="http://editor.bungeni.org/1.0/anx/"
            exclude-result-prefixes="xsl xsd xsi text office style table draw fo xlink dc meta number svg chart dr3d math form script ooo ooow oooc dom xforms rdfa of rdf anx"
            version="2.0">
            
       <!-- the below template autogenerates the odf -> meta  header for all metadata -->
       <xmeta:template match="office:meta">
        <mcontainer name="meta">
        <mcontainer name="identification">
         <mcontainer name="all">
           <xsl:apply-templates  mode="all" />
         </mcontainer>
        
         <!-- the below expects some mandatory metadata to be set --> 
         <mcontainer name="frbrwork">
             
             <xsl:call-template name="meta-outputter">
                 <xsl:with-param name="meta-name">
                     <xsl:text>uri</xsl:text>
                 </xsl:with-param>
                 <xsl:with-param name="meta-value">
                     <xsl:text>BungeniWorkURI</xsl:text>
                 </xsl:with-param>
             </xsl:call-template>
             
             <xsl:call-template name="meta-outputter">
                 <xsl:with-param name="meta-name">
                     <xsl:text>author</xsl:text>
                 </xsl:with-param>
                 <xsl:with-param name="meta-value">
                     <xsl:text>BungeniWorkAuthor</xsl:text>
                 </xsl:with-param>
             </xsl:call-template>
             
             <xsl:call-template name="meta-outputter">
                 <xsl:with-param name="meta-name">
                     <xsl:text>date</xsl:text>
                 </xsl:with-param>
                 <xsl:with-param name="meta-value">
                     <xsl:text>BungeniWorkDate</xsl:text>
                 </xsl:with-param>
             </xsl:call-template>
             
             <xsl:call-template name="meta-outputter">
                 <xsl:with-param name="meta-name">
                     <xsl:text>date_name</xsl:text>
                 </xsl:with-param>
                 <xsl:with-param name="meta-value">
                     <xsl:text>BungeniWorkDateName</xsl:text>
                 </xsl:with-param>
             </xsl:call-template>
             
         </mcontainer>
         
         <!-- the below expects some mandatory metadata to be set -->
         <mcontainer name="frbrexpression">
             <xsl:call-template name="meta-outputter">
                 <xsl:with-param name="meta-name">
                     <xsl:text>uri</xsl:text>
                 </xsl:with-param>
                 <xsl:with-param name="meta-value">
                     <xsl:text>BungeniExpURI</xsl:text>
                 </xsl:with-param>
             </xsl:call-template>
             
             <xsl:call-template name="meta-outputter">
                 <xsl:with-param name="meta-name">
                     <xsl:text>author</xsl:text>
                 </xsl:with-param>
                 <xsl:with-param name="meta-value">
                     <xsl:text>BungeniExpAuthor</xsl:text>
                 </xsl:with-param>
             </xsl:call-template>
             
             <xsl:call-template name="meta-outputter">
                 <xsl:with-param name="meta-name">
                     <xsl:text>date</xsl:text>
                 </xsl:with-param>
                 <xsl:with-param name="meta-value">
                     <xsl:text>BungeniExpDate</xsl:text>
                 </xsl:with-param>
             </xsl:call-template>
 
             <xsl:call-template name="meta-outputter">
                 <xsl:with-param name="meta-name">
                     <xsl:text>date_name</xsl:text>
                 </xsl:with-param>
                 <xsl:with-param name="meta-value">
                     <xsl:text>BungeniExpDateName</xsl:text>
                 </xsl:with-param>
             </xsl:call-template>
             
             
         </mcontainer>
        
         <!-- the below expects some mandatory metadata to be set -->
         <mcontainer name="frbrmanifestation">
             <xsl:call-template name="meta-outputter">
                 <xsl:with-param name="meta-name">
                     <xsl:text>uri</xsl:text>
                 </xsl:with-param>
                 <xsl:with-param name="meta-value">
                     <xsl:text>BungeniManURI</xsl:text>
                 </xsl:with-param>
             </xsl:call-template>
             
             <xsl:call-template name="meta-outputter">
                 <xsl:with-param name="meta-name">
                     <xsl:text>author</xsl:text>
                 </xsl:with-param>
                 <xsl:with-param name="meta-value">
                     <xsl:text>BungeniManAuthor</xsl:text>
                 </xsl:with-param>
             </xsl:call-template>
             
             <xsl:call-template name="meta-outputter">
                 <xsl:with-param name="meta-name">
                     <xsl:text>date</xsl:text>
                 </xsl:with-param>
                 <xsl:with-param name="meta-value">
                     <xsl:text>BungeniManDate</xsl:text>
                 </xsl:with-param>
             </xsl:call-template>
             
             <xsl:call-template name="meta-outputter">
                 <xsl:with-param name="meta-name">
                     <xsl:text>date_name</xsl:text>
                 </xsl:with-param>
                 <xsl:with-param name="meta-value">
                     <xsl:text>BungeniManDateName</xsl:text>
                 </xsl:with-param>
             </xsl:call-template>
             
         </mcontainer>
           
       </mcontainer>
        <mcontainer name="publication">
            <xsl:call-template name="meta-outputter">
                <xsl:with-param name="meta-name">
                    <xsl:text>name</xsl:text>
                </xsl:with-param>
                <xsl:with-param name="meta-value">
                    <xsl:text>BungeniPublicationName</xsl:text>
                </xsl:with-param>
            </xsl:call-template>
            
        </mcontainer>
        </mcontainer> 
       </xmeta:template>
     </xmeta:stylesheet>
    </xsl:template>
    
    <xsl:template match="metadata" mode="all">
        <xsl:call-template name="meta-outputter">
            <xsl:with-param name="meta-name" select="@name" />
            <xsl:with-param name="meta-value" select="@name" />
         </xsl:call-template>
    </xsl:template>

   <xsl:template name="meta-outputter">
       <xsl:param name="meta-name" />
       <xsl:param name="meta-value" />
       <meta name="{$meta-name}">
           <xsl:attribute name="value">
               <xsl:text>{//meta:user-defined[@name='</xsl:text>
               <xsl:value-of select="$meta-value" />
               <xsl:text>']}</xsl:text>
           </xsl:attribute>
       </meta>       
   </xsl:template>
 
</xsl:stylesheet>