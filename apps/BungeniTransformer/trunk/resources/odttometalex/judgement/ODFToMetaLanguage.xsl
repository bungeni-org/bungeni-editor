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
                exclude-result-prefixes="xsl xsd xsi text office style table draw fo xlink dc meta number svg chart dr3d math form script ooo ooow oooc dom xforms"
				version="2.0">
    <xsl:output indent="yes" method="xml" />
    
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
        <xsl:variable name="WORKUri" select="substring-before(//meta:user-defined[@meta:name='BungeniWorkURI'],concat('/',//meta:user-defined[@meta:name='BungeniDocPart']))" />
        <xsl:variable name="EXPRESSIONUri" select="concat($WORKUri,'/',//meta:user-defined[@meta:name='BungeniLanguageCode'],'@')" />
        <xsl:variable name="MANIFESTATIONUri" select="concat($EXPRESSIONUri,'.akn')" />
       <mcontainer name="meta">
			<xsl:for-each select="@*">
		    	<xsl:attribute name="{name(.)}">
		        	<xsl:value-of select="."/>
		        </xsl:attribute>
			</xsl:for-each>
            <mcontainer name="identification" id="identification_{generate-id()}" source="#bungeni">
                <mcontainer name="FRBRWork" id="FRBRWork_{generate-id()}">
                    <meta name="this" id="FRBRWork_this_{generate-id()}" value="{//meta:user-defined[@meta:name='BungeniWorkURI']}"/>
                    <meta name="uri" id="FRBRWork_uri_{generate-id()}" value="{$WORKUri}"/>
                    <meta name="date" id="FRBRWork_date_{generate-id()}" contentName="{//meta:user-defined[@meta:name='BungeniWorkDateName']}" date="{//meta:user-defined[@meta:name='BungeniWorkDate']}"/>
                    <meta name="author" id="FRBRWork_author_{generate-id()}" href="#{//meta:user-defined[@meta:name='BungeniWorkAuthor']}"/>
                </mcontainer>
                <mcontainer name="FRBRExpression" id="FRBRExpression_{generate-id()}">
                    <meta name="this" id="FRBRExpression_this_{generate-id()}" value="{//meta:user-defined[@meta:name='BungeniExpURI']}" />
                    <meta name="uri" id="FRBRExpression_uri_{generate-id()}" value="{$EXPRESSIONUri}"/>
                    <meta name="date" id="FRBRExpression_date_{generate-id()}" contentName="{//meta:user-defined[@meta:name='BungeniExpDateName']}" date="{//meta:user-defined[@meta:name='BungeniExpDate']}"/>
                    <meta name="author" id="FRBRExpression_author_{generate-id()}" href="#{//meta:user-defined[@meta:name='BungeniExpAuthor']}"/>
                </mcontainer>
                <mcontainer name="FRBRManifestation" id="FRBRManifestation_{generate-id()}">
                    <meta name="this" id="FRBRManifestation_this_{generate-id()}" value="{//meta:user-defined[@meta:name='BungeniManURI']}"/>
                    <meta name="uri" id="FRBRManifestation_uri_{generate-id()}" value="{$MANIFESTATIONUri}"/>
                    <meta name="date" id="FRBRManifestation_date_{generate-id()}" contentName="{//meta:user-defined[@meta:name='BungeniManDateName']}" date="{//meta:user-defined[@meta:name='BungeniManDate']}" />
                    <meta name="author" id="FRBRManifestation_author_{generate-id()}" href="#{//meta:user-defined[@meta:name='BungeniManAuthor']}"/>
                </mcontainer>
            </mcontainer>
            <mcontainer name="publication_mcontainer" id="publication_container{generate-id()}">
                <meta id="publication_{generate-id()}" name="publication" contentName="{//meta:user-defined[@meta:name='BungeniPublicationName']}" date="{//meta:user-defined[@meta:name='BungeniPublicationDate']}"/>
            </mcontainer>
            <mcontainer id="references_{generate-id()}" name="references" source="#bungeni">
                <!-- <meta id="Parliament" name="TLCOrganization" href="{//meta:user-defined[@meta:name='BungeniParliamentID']}"  showAs="Parliament" /> -->
                <meta name="TLCConcept" id="judgementNo" href="/ontology/concept/judgement/Judgement/{//meta:user-defined[@meta:name='BungeniJudgementNo']}" showAs="{//meta:user-defined[@meta:name='BungeniJudgementNo']}" />
                <meta name="TLCPerson" id="{//meta:user-defined[@meta:name='BungeniWorkAuthor']}" href="{//meta:user-defined[@meta:name='BungeniWorkAuthorURI']}" showAs="Author"/>
                <meta name="TLCPerson" id="{//meta:user-defined[@meta:name='BungeniExpAuthor']}" href="{//meta:user-defined[@meta:name='BungeniExpAuthorURI']}" showAs="Author"/>
                <meta name="TLCPerson" id="{//meta:user-defined[@meta:name='BungeniManAuthor']}" href="{//meta:user-defined[@meta:name='BungeniManAuthorURI']}" showAs="Author"/>
                
                <xsl:for-each select="//meta:user-defined[starts-with(@meta:name, 'BungeniPartyName')]">
                  	<xsl:variable name="strHref"><xsl:value-of select="." /></xsl:variable>
				    <xsl:variable name="tokenizedHref" select="tokenize($strHref,'~')"/>
					<meta name="TLCPerson"  id="{$tokenizedHref[1]}" href="{$tokenizedHref[2]}" showAs="{$tokenizedHref[3]}" inrole="{$tokenizedHref[4]}" /> 
                	<meta name="TLCRole"  id="{$tokenizedHref[4]}" href="/ontology/judgement/role/{$tokenizedHref[4]}" showAs="{$tokenizedHref[4]}" /> 
                </xsl:for-each> 
                <xsl:for-each select="//meta:user-defined[starts-with(@meta:name, 'BungeniJudgeName')]">
                  	<xsl:variable name="strHref"><xsl:value-of select="." /></xsl:variable>
				    <xsl:variable name="tokenizedHref" select="tokenize($strHref,'~')"/>
					<meta name="TLCPerson"  id="{$tokenizedHref[1]}"   href="{$tokenizedHref[4]}" showAs="{$tokenizedHref[3]}" /> 
                </xsl:for-each> 
                <!--
                <xsl:for-each select="//*[@BungeniSectionType='ActionEvent']">
                    <meta name="TLCEvent" id="{@BungeniEventName}" href="{@BungeniOntology}" showAs="{@BungeniOntologyName}"/> 
                </xsl:for-each> 
                -->
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
		     	<xsl:value-of select="@text:name"/>
		    </xsl:attribute>
		    <xsl:attribute name="class">
		     	<xsl:value-of select="@text:style-name"/>
		    </xsl:attribute>
		    <xsl:attribute name="name">
		     	<xsl:value-of select="@BungeniSectionType"/>
		    </xsl:attribute>
			<xsl:apply-templates />
        </container>
    </xsl:template>

    <xsl:template match="text:list">
        <container name="list">
            <xsl:attribute name="class" select="@text:style-name" />
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
            <xsl:attribute name="class" select="@text:style-name" />
            <xsl:apply-templates />
        </block>
    </xsl:template>

    <xsl:template match="text:span">
        <inline name="span">
            <xsl:attribute name="id" select="generate-id(.)" />
            <xsl:attribute name="class" select="@text:style-name" />
            <xsl:apply-templates />
        </inline>
    </xsl:template>

    <xsl:template match="text:a">
        <inline name="a">
            <xsl:attribute name="id" select="generate-id(.)" />
            <xsl:attribute name="href" select="tokenize(@xlink:href,':')[position()=2]" />
            <xsl:apply-templates />
        </inline>
    </xsl:template>

    <xsl:template match="text:p[tokenize(@text:style-name,'_')[position()=1] = 'heading']">
        <htitle>
            <xsl:attribute name="id" select="generate-id(.)" />
            <xsl:attribute name="name" select="tokenize(@text:style-name,'_')[position()=1]" />
            <xsl:attribute name="class" select="tokenize(@text:style-name,'_')[position()=2]" />
            <xsl:apply-templates />
        </htitle>
    </xsl:template>

    <xsl:template match="text:p[tokenize(@text:style-name,'_')[position()=1] = 'subheading']">
        <htitle>
            <xsl:attribute name="id" select="generate-id(.)" />
            <xsl:attribute name="name" select="tokenize(@text:style-name,'_')[position()=1]" />
            <xsl:attribute name="class" select="tokenize(@text:style-name,'_')[position()=2]" />
            <xsl:apply-templates />
        </htitle>
    </xsl:template>

    <xsl:template match="text:p[tokenize(@text:style-name,'_')[position()=1] = 'num']">
        <htitle>
            <xsl:attribute name="id" select="generate-id(.)" />
            <xsl:attribute name="name" select="tokenize(@text:style-name,'_')[position()=1]" />
            <xsl:attribute name="class" select="tokenize(@text:style-name,'_')[position()=2]" />
            <xsl:apply-templates />
        </htitle>
    </xsl:template>

    <xsl:template match="text:p[tokenize(@text:style-name,'_')[position()=1] = 'sidenote']">
        <htitle>
            <xsl:attribute name="id" select="generate-id(.)" />
            <xsl:attribute name="name" select="tokenize(@text:style-name,'_')[position()=1]" />
            <xsl:attribute name="class" select="tokenize(@text:style-name,'_')[position()=2]" />
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
