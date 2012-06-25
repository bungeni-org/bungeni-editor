<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="2.0">
    <!-- the input document for this template is the metadatas configuration file
            settings/config/metadata/<doctype>.xml -->
    <xsl:template match="metadatas">
       <!-- the below template autogenerates the odf -> meta  header for all metadata -->
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
             
             <!--
             <meta name="uri" value="{//meta:user-defined[@name='BungeniWorkURI']}" />
             <meta name="author" value="{//meta:user-defined[@name='BungeniWorkAuthor']}" />
             <meta name="date" 
                 value="{//meta:user-defined[@name='BungeniWorkDate']}" 
             />-->
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
        
    </xsl:template>
    
    <xsl:template match="metadata" mode="all">
        <xsl:call-template name="meta-outputter">
            <xsl:with-param name="meta-name" select="@name" />
            <xsl:with-param name="meta-value" select="@name" />
         </xsl:call-template>
        <!--
       <meta name="{@name}">
            <xsl:attribute name="value">
                <xsl:text>{//meta:user-defined[@name='</xsl:text>
                <xsl:value-of select="@name" />
                <xsl:text>']}</xsl:text>
            </xsl:attribute>
        </meta>
        -->
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