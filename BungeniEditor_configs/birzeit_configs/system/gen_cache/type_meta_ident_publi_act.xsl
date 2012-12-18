<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:meta="urn:oasis:names:tc:opendocument:xmlns:meta:1.0"
                xmlns:chart="urn:oasis:names:tc:opendocument:xmlns:chart:1.0"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:dr3d="urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0"
                xmlns:dc="http://purl.org/dc/elements/1.1/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:math="http://www.w3.org/1998/Math/MathML"
                xmlns:bungeni="http://editor.bungeni.org/1.0/anx/"
                xmlns:form="urn:oasis:names:tc:opendocument:xmlns:form:1.0"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:script="urn:oasis:names:tc:opendocument:xmlns:script:1.0"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:of="urn:oasis:names:tc:opendocument:xmlns:of:1.2"
                xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0"
                xmlns:ooo="http://openoffice.org/2004/office"
                xmlns:rdfa="http://docs.oasis-open.org/opendocument/meta/rdfa#"
                xmlns:style="urn:oasis:names:tc:opendocument:xmlns:style:1.0"
                xmlns:ooow="http://openoffice.org/2004/writer"
                xmlns:bodf="http://editor.bungeni.org/1.0/odf/"
                xmlns:field="urn:openoffice:names:experimental:ooo-ms-interop:xmlns:field:1.0"
                xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:oooc="http://openoffice.org/2004/calc"
                xmlns:rpt="http://openoffice.org/2005/report"
                xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0"
                xmlns:dom="http://www.w3.org/2001/xml-events"
                xmlns:anx="http://anx.akomantoso.org/1.0"
                xmlns:number="urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0"
                xmlns:draw="urn:oasis:names:tc:opendocument:xmlns:drawing:1.0"
                xmlns:xforms="http://www.w3.org/2002/xforms"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:svg="urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0"
                xmlns:fo="urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0"
                exclude-result-prefixes="xsl xsd xsi text office style table draw fo xlink dc meta number svg chart dr3d math form script ooo ooow oooc dom xforms rdfa of rdf anx"
                version="2.0"><!--
         Identity Template generator
     --><xsl:template match="@*|*|processing-instruction()|comment()">
      <xsl:copy>
         <xsl:apply-templates select="*|@*|text()|processing-instruction()|comment()"/>
      </xsl:copy>
   </xsl:template>
   <!--
          Root template generator
      --><xsl:template match="office:document-content">
      <root name="root" id="w5aa">
         <xsl:apply-templates/>
      </root>
   </xsl:template>
   <!--
          Meta Header template, Contains Executable code, 
          the references block is dynamically embedded into the
          template matcher below.
      --><xsl:template match="office:meta">
      <mcontainer name="meta">
         <mcontainer name="identification">
            <mcontainer name="all">

  
               <meta name="BungeniPublicationSrcName"
                     value="{//meta:user-defined[@name='BungeniPublicationSrcName']}"/>
   
               <meta name="BungeniPublicationDate"
                     value="{//meta:user-defined[@name='BungeniPublicationDate']}"/>
    
               <meta name="BungeniPublicationDateHijri"
                     value="{//meta:user-defined[@name='BungeniPublicationDateHijri']}"/>
    
               <meta name="BungeniPublicationArea"
                     value="{//meta:user-defined[@name='BungeniPublicationArea']}"/>
    
               <meta name="BungeniSourceType"
                     value="{//meta:user-defined[@name='BungeniSourceType']}"/>
    
               <meta name="BungeniSourceNo" value="{//meta:user-defined[@name='BungeniSourceNo']}"/>
    
               <meta name="BungeniPublicationSrcName_AN"
                     value="{//meta:user-defined[@name='BungeniPublicationSrcName_AN']}"/>
    
    
    
      
               <meta name="BungeniLanguageCode"
                     value="{//meta:user-defined[@name='BungeniLanguageCode']}"/>
    
               <meta name="BungeniActName" value="{//meta:user-defined[@name='BungeniActName']}"/>
    
               <meta name="BungeniActNo" value="{//meta:user-defined[@name='BungeniActNo']}"/>
    
               <meta name="BungeniActYear" value="{//meta:user-defined[@name='BungeniActYear']}"/>
    
               <meta name="BungeniActType" value="{//meta:user-defined[@name='BungeniActType']}"/>
    
               <meta name="BungeniActState" value="{//meta:user-defined[@name='BungeniActState']}"/>
    
               <meta name="BungeniActScope" value="{//meta:user-defined[@name='BungeniActScope']}"/>
    
               <meta name="BungeniActHistoricalPeriod"
                     value="{//meta:user-defined[@name='BungeniActHistoricalPeriod']}"/>
    
    
               <meta name="BungeniPageNo" value="{//meta:user-defined[@name='BungeniPageNo']}"/> 
     
               <meta name="BungeniActEffectiveDate"
                     value="{//meta:user-defined[@name='BungeniActEffectiveDate']}"/>
   
   
               <meta name="BungeniPageCount" value="{//meta:user-defined[@name='BungeniPageCount']}"/>
       
               <meta name="BungeniActFamily" value="{//meta:user-defined[@name='BungeniActFamily']}"/>
    
               <meta name="BungeniActFamilyPossible"
                     value="{//meta:user-defined[@name='BungeniActFamilyPossible']}"/>
    
    
               <meta name="BungeniActCategory"
                     value="{//meta:user-defined[@name='BungeniActCategory']}"/>
    
   
    
               <meta name="BungeniActRelease"
                     value="{//meta:user-defined[@name='BungeniActRelease']}"/>
    
               <meta name="BungeniActReleaseDate"
                     value="{//meta:user-defined[@name='BungeniActReleaseDate']}"/>
    
               <meta name="BungeniActReleaseDateHijri"
                     value="{//meta:user-defined[@name='BungeniActReleaseDateHijri']}"/>
    
               <meta name="BungeniActDeveloped"
                     value="{//meta:user-defined[@name='BungeniActDeveloped']}"/>
    
               <meta name="BungeniActDevelopedDate"
                     value="{//meta:user-defined[@name='BungeniActDevelopedDate']}"/>
    
               <meta name="BungeniActApproved"
                     value="{//meta:user-defined[@name='BungeniActApproved']}"/>
    
               <meta name="BungeniActApprovedDate"
                     value="{//meta:user-defined[@name='BungeniActApprovedDate']}"/>
    
               <meta name="BungeniWorkAuthor"
                     value="{//meta:user-defined[@name='BungeniWorkAuthor']}"/>
    
               <meta name="BungeniWorkURI" value="{//meta:user-defined[@name='BungeniWorkURI']}"/>
    
               <meta name="BungeniRefURI" value="{//meta:user-defined[@name='BungeniRefURI']}"/>
    
   
            </mcontainer>
            <mcontainer name="frbrwork">
               <meta name="uri" value="{//meta:user-defined[@name='BungeniWorkURI']}"/>
               <meta name="author" value="{//meta:user-defined[@name='BungeniWorkAuthor']}"/>
               <meta name="as" value="{//meta:user-defined[@name='BungeniWorkAuthorAs']}"/>
               <meta name="date" value="{//meta:user-defined[@name='BungeniWorkDate']}"/>
               <meta name="date_name" value="{//meta:user-defined[@name='BungeniWorkDateName']}"/>
            </mcontainer>
            <mcontainer name="frbrexpression">
               <meta name="uri" value="{//meta:user-defined[@name='BungeniExpURI']}"/>
               <meta name="author" value="{//meta:user-defined[@name='BungeniExpAuthor']}"/>
               <meta name="as" value="{//meta:user-defined[@name='BungeniExpAuthorAs']}"/>
               <meta name="date" value="{//meta:user-defined[@name='BungeniExpDate']}"/>
               <meta name="date_name" value="{//meta:user-defined[@name='BungeniExpDateName']}"/>
            </mcontainer>
            <mcontainer name="frbrmanifestation">
               <meta name="uri" value="{//meta:user-defined[@name='BungeniManURI']}"/>
               <meta name="author" value="{//meta:user-defined[@name='BungeniManAuthor']}"/>
               <meta name="as" value="{//meta:user-defined[@name='BungeniManAuthorAs']}"/>
               <meta name="date" value="{//meta:user-defined[@name='BungeniManDate']}"/>
               <meta name="date_name" value="{//meta:user-defined[@name='BungeniManDateName']}"/>
            </mcontainer>
         </mcontainer>
         <mcontainer name="publication">
            <meta name="name" value="{//meta:user-defined[@name='BungeniPublicationName']}"/>
            <meta name="date" value="{//meta:user-defined[@name='BungeniPublicationDate']}"/>
         </mcontainer>
      </mcontainer>
   </xsl:template>
   <!--System Provided Template Matchers--><xsl:template match="text:section">
      <container>
         <xsl:for-each select="@*[   local-name(.)!='name' and                       local-name(.)!='BungeniSectionType' and                       local-name(.)!='style-name']">
            <xsl:attribute name="metadatas">
               <xsl:value-of select="."/>
            </xsl:attribute>
         </xsl:for-each>
         <xsl:attribute name="id">
            <xsl:value-of select="@name"/>
         </xsl:attribute>
         <xsl:attribute name="class">
            <xsl:value-of select="@style-name"/>
         </xsl:attribute>
         <xsl:attribute name="name">
            <xsl:value-of select="./bungeni:bungenimeta/bungeni:BungeniSectionType"/>
         </xsl:attribute>
         <xsl:apply-templates/>
      </container>
   </xsl:template>
   <xsl:template match="text:meta">
      <inline>
         <xsl:attribute name="name">
            <xsl:value-of select="./bungeni:bungenimeta/bungeni:BungeniInlineType"/>
         </xsl:attribute>
         <xsl:element name="inlineContent">
            <xsl:value-of select="content"/>
         </xsl:element>
      </inline>
   </xsl:template>
   <xsl:template match="text:list">
      <container name="list">
         <xsl:attribute name="class" select="@style-name"/>
         <xsl:attribute name="id" select="generate-id(.)"/>
         <xsl:apply-templates/>
      </container>
   </xsl:template>
   <xsl:template match="text:list-header">
      <xsl:apply-templates/>
   </xsl:template>
   <xsl:template match="text:list-item">
      <container name="item">
         <xsl:attribute name="id" select="generate-id(.)"/>
         <xsl:apply-templates/>
      </container>
   </xsl:template>
   <xsl:template match="text:p">
      <block name="p">
         <xsl:attribute name="id" select="generate-id(.)"/>
         <xsl:attribute name="class" select="@style-name"/>
         <xsl:apply-templates/>
      </block>
   </xsl:template>
   <xsl:template match="text:span">
      <inline name="span">
         <xsl:attribute name="id" select="generate-id(.)"/>
         <xsl:attribute name="class" select="@style-name"/>
         <xsl:apply-templates/>
      </inline>
   </xsl:template>
   <xsl:template match="text:a">
      <inline name="a">
         <xsl:attribute name="id" select="generate-id(.)"/>
         <xsl:attribute name="href" select="tokenize(@href,':')[position()=2]"/>
         <xsl:apply-templates/>
      </inline>
   </xsl:template>
   <xsl:template match="text:p">
      <block name="p">
         <xsl:attribute name="id" select="generate-id(.)"/>
         <xsl:attribute name="class" select="@style-name"/>
         <xsl:apply-templates/>
      </block>
   </xsl:template>
   <xsl:template match="text:span">
      <inline name="span">
         <xsl:attribute name="id" select="generate-id(.)"/>
         <xsl:attribute name="class" select="@style-name"/>
         <xsl:apply-templates/>
      </inline>
   </xsl:template>
   <xsl:template match="text:a">
      <inline name="a">
         <xsl:attribute name="id" select="generate-id(.)"/>
         <xsl:attribute name="href" select="tokenize(@href,':')[position()=2]"/>
         <xsl:apply-templates/>
      </inline>
   </xsl:template>
   <xsl:template match="text:p[tokenize(@style-name,'_')[position()=1] = 'heading']">
      <htitle>
         <xsl:attribute name="id" select="generate-id(.)"/>
         <xsl:attribute name="name" select="tokenize(@style-name,'_')[position()=1]"/>
         <xsl:attribute name="class" select="tokenize(@style-name,'_')[position()=2]"/>
         <xsl:apply-templates/>
      </htitle>
   </xsl:template>
   <xsl:template match="text:p[tokenize(@style-name,'_')[position()=1] = 'subheading']">
      <htitle>
         <xsl:attribute name="id" select="generate-id(.)"/>
         <xsl:attribute name="name" select="tokenize(@style-name,'_')[position()=1]"/>
         <xsl:attribute name="class" select="tokenize(@style-name,'_')[position()=2]"/>
         <xsl:apply-templates/>
      </htitle>
   </xsl:template>
   <xsl:template match="text:p[tokenize(@style-name,'_')[position()=1] = 'num']">
      <htitle>
         <xsl:attribute name="id" select="generate-id(.)"/>
         <xsl:attribute name="name" select="tokenize(@style-name,'_')[position()=1]"/>
         <xsl:attribute name="class" select="tokenize(@style-name,'_')[position()=2]"/>
         <xsl:apply-templates/>
      </htitle>
   </xsl:template>
   <xsl:template match="text:p[tokenize(@style-name,'_')[position()=1] = 'sidenote']">
      <htitle>
         <xsl:attribute name="id" select="generate-id(.)"/>
         <xsl:attribute name="name" select="tokenize(@style-name,'_')[position()=1]"/>
         <xsl:attribute name="class" select="tokenize(@style-name,'_')[position()=2]"/>
         <xsl:apply-templates/>
      </htitle>
   </xsl:template>
   <xsl:template match="text:soft-page-break">
      <milestone name="eol">
         <xsl:attribute name="id" select="generate-id(.)"/>
         <xsl:apply-templates/>
      </milestone>
   </xsl:template>
   <xsl:template match="text:tab"/>
   <xsl:template match="text:s"/>
   <xsl:template match="text()">
      <xsl:value-of select="normalize-space(.)"/>
   </xsl:template>
</xsl:stylesheet>