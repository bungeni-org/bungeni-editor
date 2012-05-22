<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
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
    <xsl:output indent="yes" method="xml" />
    
    <xsl:key name="bySectionType" match="bungeni:bungenimeta" use="bungeni:BungeniSectionType" />
    
    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>
    
	<xsl:template match="*">
        <xsl:element name="{name()}">
			<xsl:for-each select="@*">
		    	<xsl:attribute name="{local-name(.)}">
		        	<xsl:value-of select="."/>
		        </xsl:attribute>
			</xsl:for-each>
			<xsl:apply-templates />
		</xsl:element>    
	</xsl:template>

    <xsl:template match="office:document-content">
        <root name="root" id="{generate-id(.)}">
            <xsl:for-each select="@*">
                <xsl:attribute name="{local-name(.)}">
                    <xsl:value-of select="." />
                </xsl:attribute>
            </xsl:for-each>
            <xsl:apply-templates />
        </root>
    </xsl:template>

    <xsl:template match="office:meta">
        <xsl:variable name="WORKUri" select="substring-before(//meta:user-defined[@name='BungeniWorkURI'],concat('/',//meta:user-defined[@name='BungeniDocPart']))" />
        <xsl:variable name="EXPRESSIONUri" select="concat($WORKUri,'/',//meta:user-defined[@name='BungeniLanguageCode'],'@')" />
        <xsl:variable name="MANIFESTATIONUri" select="concat($EXPRESSIONUri,'.akn')" />
       <mcontainer name="meta">
			<xsl:for-each select="@*">
		    	<xsl:attribute name="{name(.)}">
		        	<xsl:value-of select="."/>
		        </xsl:attribute>
			</xsl:for-each>
            <mcontainer name="identification" id="identification_{generate-id()}" source="#bungeni">
                <mcontainer name="FRBRWork" id="FRBRWork_{generate-id()}">
                    <meta name="this" id="FRBRWork_this_{generate-id()}" value="{//meta:user-defined[@name='BungeniWorkURI']}"/>
                    <meta name="uri" id="FRBRWork_uri_{generate-id()}" value="{$WORKUri}"/>
                    <meta name="date" id="FRBRWork_date_{generate-id()}" contentName="{//meta:user-defined[@name='BungeniWorkDateName']}" date="{//meta:user-defined[@name='BungeniWorkDate']}"/>
                    <meta name="author" id="FRBRWork_author_{generate-id()}" href="#{//meta:user-defined[@name='BungeniWorkAuthor']}"/>
                </mcontainer>
                <mcontainer name="FRBRExpression" id="FRBRExpression_{generate-id()}">
                    <meta name="this" id="FRBRExpression_this_{generate-id()}" value="{//meta:user-defined[@name='BungeniExpURI']}" />
                    <meta name="uri" id="FRBRExpression_uri_{generate-id()}" value="{$EXPRESSIONUri}"/>
                    <meta name="date" id="FRBRExpression_date_{generate-id()}" contentName="{//meta:user-defined[@name='BungeniExpDateName']}" date="{//meta:user-defined[@name='BungeniExpDate']}"/>
                    <meta name="author" id="FRBRExpression_author_{generate-id()}" href="#{//meta:user-defined[@name='BungeniExpAuthor']}"/>
                </mcontainer>
                <mcontainer name="FRBRManifestation" id="FRBRManifestation_{generate-id()}">
                    <meta name="this" id="FRBRManifestation_this_{generate-id()}" value="{//meta:user-defined[@name='BungeniManURI']}"/>
                    <meta name="uri" id="FRBRManifestation_uri_{generate-id()}" value="{$MANIFESTATIONUri}"/>
                    <meta name="date" id="FRBRManifestation_date_{generate-id()}" contentName="{//meta:user-defined[@name='BungeniManDateName']}" date="{//meta:user-defined[@name='BungeniManDate']}" />
                    <meta name="author" id="FRBRManifestation_author_{generate-id()}" href="#{//meta:user-defined[@name='BungeniManAuthor']}"/>
                </mcontainer>
            </mcontainer>
            <mcontainer name="publication_mcontainer" id="publication_container{generate-id()}">
                <meta id="publication_{generate-id()}" name="publication" contentName="{//meta:user-defined[@name='BungeniPublicationName']}" date="{//meta:user-defined[@name='BungeniPublicationDate']}"/>
            </mcontainer>
            <mcontainer id="references_{generate-id()}" name="references" source="#bungeni">
               
               
   <!--          <meta id="Parliament" name="Parliament" ParliamentID="{//meta:user-defined[@name='BungeniParliamentID']}" ParliamentSitting="{//meta:user-defined[@name='BungeniParliamentSitting']}" ParliamentSession="{//meta:user-defined[@name='BungeniParliamentSession']}" />
                 <meta id="Act" name="Act" ActName="{//meta:user-defined[@name='BungeniActName']}" ActNo="{//meta:user-defined[@name='BungeniActNo']}" />
               -->
                
                
               <meta name="TLCPerson" id="{//meta:user-defined[@name='BungeniWorkAuthor']}" href="{//meta:user-defined[@name='BungeniWorkAuthorURI']}" showAs="Author"/>
                <meta name="TLCPerson" id="{//meta:user-defined[@name='BungeniExpAuthor']}" href="{//meta:user-defined[@name='BungeniExpAuthorURI']}" showAs="Editor"/>
                <meta name="TLCPerson" id="{//meta:user-defined[@name='BungeniManAuthor']}" href="{//meta:user-defined[@name='BungeniManAuthorURI']}" showAs="Publisher"/>
               
                
                <xsl:comment> TLCPerson </xsl:comment>
                 <xsl:for-each select="key('bySectionType', 'Speech')">
                    <xsl:element name="meta">
                        <xsl:attribute name="name">TLCPerson</xsl:attribute>
                        <xsl:attribute name="id">
                            <xsl:value-of select="./bungeni:BungeniPersonID"></xsl:value-of>
                        </xsl:attribute>
                        <xsl:attribute name="href">
                            <xsl:value-of select="./bungeni:BungeniSpeechByURI"></xsl:value-of>
                        </xsl:attribute>
                        <xsl:attribute name="showAs">
                            <xsl:value-of select="./bungeni:BungeniSpeechBy"></xsl:value-of>
                         </xsl:attribute>
                    </xsl:element>
                </xsl:for-each>
           
           
           
                <xsl:comment> TLCEvent </xsl:comment>
                
                <xsl:for-each select="key('bySectionType', 'ActionEvent')">
                    
                    <xsl:element name="meta">
                        <xsl:attribute name="name">TLCEvent</xsl:attribute>
                        <xsl:attribute name="id">
                            <xsl:value-of select="./bungeni:BungeniEventName"></xsl:value-of>
                        </xsl:attribute>
                        <xsl:attribute name="href">
                            <xsl:value-of select="./bungeni:BungeniOntology"></xsl:value-of>
                        </xsl:attribute>
                        <xsl:attribute name="showAs">
                            <xsl:value-of select="./bungeni:BungeniOntologyName"></xsl:value-of>
                        </xsl:attribute>
                    </xsl:element>
                </xsl:for-each>

             

                <xsl:comment>  TLCRole  </xsl:comment>
               
                
                <xsl:for-each select="key('bySectionType', 'Speech')">
                    
                    <xsl:element name="meta">
                        <xsl:attribute name="name">TLCRole</xsl:attribute>
                        <xsl:attribute name="id">
                            <xsl:value-of select="./bungeni:BungeniSpeechAs"></xsl:value-of>
                        </xsl:attribute>
                        <xsl:attribute name="href">
                            <xsl:value-of select="./bungeni:BungeniSpeechAsURI"></xsl:value-of>
                        </xsl:attribute>
                        <xsl:attribute name="showAs">
                            <xsl:value-of select="./bungeni:BungeniSpeechAsDesc"></xsl:value-of>
                        </xsl:attribute>
                    </xsl:element>
                </xsl:for-each>
                
                <xsl:comment>  TLCReference  </xsl:comment>
              
                <xsl:for-each select="key('bySectionType', 'Ref')">
                    
                    <xsl:element name="meta">
                        <xsl:attribute name="name">TLCReference</xsl:attribute>
                        <xsl:attribute name="id">
                            <xsl:value-of select="@id"></xsl:value-of>
                        </xsl:attribute>
                        <xsl:attribute name="href">
                            <xsl:value-of select="./bungeni:BungeniRefURI"></xsl:value-of>
                        </xsl:attribute>
                        <xsl:attribute name="showAs"> <xsl:value-of>Destination</xsl:value-of>
                        </xsl:attribute>
                    </xsl:element>
                </xsl:for-each>
                
                <!-- adding support for bill references -->
                <!-- first we filter the nodes for the ones that have the attribute starting with bungeni bill 
                    then we filter the attribute nodes for the ones starting with bungeni bill -->
                <xsl:for-each select="//*[attribute::node()[starts-with(name() ,'BungeniBill')]]/attribute::node()[starts-with(name(), 'BungeniBill')]" > 
                    <xsl:variable name="BillSectionMeta"><xsl:value-of select="." /></xsl:variable>
                    <xsl:variable name="tokenizedBillSectionMeta" select="tokenize($BillSectionMeta,';')" />
                    <xsl:variable name="BillShowAs" select="$tokenizedBillSectionMeta[1]" />
                    <xsl:variable name="BillUri" select="$tokenizedBillSectionMeta[2]" /> 
                    <xsl:variable name="BillOntology" select="$tokenizedBillSectionMeta[3]" /> 
                    <xsl:element name="meta">
                        <xsl:attribute name="name">TLCReference</xsl:attribute>
                        <xsl:attribute name="href"><xsl:value-of select="$BillOntology" /> </xsl:attribute>
                        <xsl:attribute name="showAs"><xsl:value-of select="$BillShowAs" /> </xsl:attribute>
                        <xsl:attribute name="id"><xsl:value-of select="translate($BillUri, '/', '')" /> </xsl:attribute>
                     </xsl:element>
                </xsl:for-each> 
              
            </mcontainer>
        </mcontainer> 
    </xsl:template>

    <xsl:template match="text:section">
        <container>
                    <xsl:for-each select="@*[   local-name(.)!='name' and 
			                        local-name(.)!='BungeniSectionType' and 
			                        local-name(.)!='style-name']">
		    	<xsl:attribute name="{local-name(.)}">
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
                        <!-- outputting comment -->
			<xsl:apply-templates />
        </container>
    </xsl:template>
    
     <xsl:template match="text:list">
        <container name="list">
            <xsl:attribute name="class" select="@style-name" />
            <xsl:attribute name="id" select="generate-id(.)" />
            <xsl:apply-templates />
        </container>
    </xsl:template>
    
    <xsl:template match="text:list-header">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="text:list-item">
        <container name="item">
            <xsl:for-each select="@*">
                <xsl:attribute name="{local-name(.)}">
                    <xsl:value-of select="." />
                </xsl:attribute>
            </xsl:for-each>
            <xsl:attribute name="id" select="generate-id(.)" />
            <xsl:apply-templates />
        </container>
    </xsl:template>

    <xsl:template match="text:p">
        <block name="p">
            <xsl:attribute name="id" select="generate-id(.)" />
            <xsl:attribute name="class" select="@style-name" />
            <xsl:apply-templates />
        </block>
    </xsl:template>

    <xsl:template match="text:span">
        <inline name="span">
            <xsl:attribute name="id" select="generate-id(.)" />
            <xsl:attribute name="class" select="@style-name" />
            <xsl:apply-templates />
        </inline>
    </xsl:template>

    <xsl:template match="text:a">
        <inline name="a">
            <xsl:attribute name="id" select="generate-id(.)" />
            <xsl:attribute name="href" select="tokenize(@href,':')[position()=2]" />
            <xsl:apply-templates />
        </inline>
    </xsl:template>

    <xsl:template match="text:p[tokenize(@style-name,'_')[position()=1] = 'heading']">
        <htitle>
            <xsl:attribute name="id" select="generate-id(.)" />
            <xsl:attribute name="name" select="tokenize(@style-name,'_')[position()=1]" />
            <xsl:attribute name="class" select="tokenize(@style-name,'_')[position()=2]" />
            <xsl:apply-templates />
        </htitle>
    </xsl:template>

    <xsl:template match="text:p[tokenize(@style-name,'_')[position()=1] = 'subheading']">
        <htitle>
            <xsl:attribute name="id" select="generate-id(.)" />
            <xsl:attribute name="name" select="tokenize(@style-name,'_')[position()=1]" />
            <xsl:attribute name="class" select="tokenize(@style-name,'_')[position()=2]" />
            <xsl:apply-templates />
        </htitle>
    </xsl:template>

    <xsl:template match="text:p[tokenize(@style-name,'_')[position()=1] = 'num']">
        <htitle>
            <xsl:attribute name="id" select="generate-id(.)" />
            <xsl:attribute name="name" select="tokenize(@style-name,'_')[position()=1]" />
            <xsl:attribute name="class" select="tokenize(@style-name,'_')[position()=2]" />
            <xsl:apply-templates />
        </htitle>
    </xsl:template>

    <xsl:template match="text:p[tokenize(@style-name,'_')[position()=1] = 'sidenote']">
        <htitle>
            <xsl:attribute name="id" select="generate-id(.)" />
            <xsl:attribute name="name" select="tokenize(@style-name,'_')[position()=1]" />
            <xsl:attribute name="class" select="tokenize(@style-name,'_')[position()=2]" />
            <xsl:apply-templates />
        </htitle>
    </xsl:template>

    <xsl:template match="text:soft-page-break">
        <milestone name="eol">
            <xsl:attribute name="id" select="generate-id(.)" />
            <xsl:apply-templates />
        </milestone>
    </xsl:template>

    <xsl:template match="text:tab"></xsl:template>

    <xsl:template match="text:s"></xsl:template>

    <xsl:template match="text()">
        <xsl:value-of select="normalize-space(.)" />
    </xsl:template>

</xsl:stylesheet>
