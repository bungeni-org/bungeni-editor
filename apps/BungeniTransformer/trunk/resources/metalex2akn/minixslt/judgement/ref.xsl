<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ml="http://www.metalex.org/1.0" >
	<xsl:output indent="yes" method="xml" encoding="UTF-8"/>
	
<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>
	
	
	<xsl:template match="*">
		<xsl:element name="{node-name(.)}">
			<xsl:for-each select="@*">
				<xsl:attribute name="{name(.)}">
					<xsl:value-of select="."/>
				</xsl:attribute>
			</xsl:for-each>
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>

	<xsl:template match="*[@name='ref']">
		<xsl:if test="@href">
			<xsl:if test="@class" >
				<xsl:choose>
					<!--check if caseNumber attribute exists then generate a 'caseNumber' element -->
					<xsl:when test="@class='mBungeniCaseNo'">
						<caseNumber xsl:exclude-result-prefixes="#all">
							<xsl:value-of select="."/>
						</caseNumber>
					</xsl:when>
					<!-- the party element -->
					<xsl:when test="@class='BungeniPartyName'">
						<party xsl:exclude-result-prefixes="#all" >
						<!-- we use the tokenize() function to extract the refersTo attrib 
						from the reference href the id is a generated one -->
						<xsl:variable name="strHref"><xsl:value-of select="@href" /></xsl:variable>
						<xsl:variable name="tokenizedHref" select="tokenize($strHref,';')"/>
						<xsl:variable name="refersToParty" select="$tokenizedHref[1]" />
							<xsl:variable name="metadataRefRole" select="//ml:meta[@id=$refersToParty]/@inrole" />
						<xsl:attribute name="id">
						   <xsl:value-of select="generate-id()" />
						</xsl:attribute>
						<xsl:attribute name="refersTo">
						   <xsl:text>#</xsl:text><xsl:value-of select="$refersToParty" />
						</xsl:attribute>
						<xsl:attribute name="as">
						   <xsl:text>#</xsl:text><xsl:value-of select="$metadataRefRole" />
						</xsl:attribute>
						 <xsl:value-of select="."/>
						</party>
					</xsl:when>	
					<!-- Add judge element translation -->
					<xsl:when test="@class='BungeniJudgeName'">
						<judge xsl:exclude-result-prefixes="#all">
						<!-- we use the tokenize() function to extract the refersTo attrib 
						from the reference href the id is a generated one -->
						<xsl:attribute name="id">
						   <xsl:value-of select="generate-id()" />
						</xsl:attribute>
						<xsl:variable name="strHref"><xsl:value-of select="@href" /></xsl:variable>
						<xsl:variable name="tokenizedHref" select="tokenize($strHref,';')"/>
						<xsl:attribute name="refersTo">
						   <xsl:text>#</xsl:text><xsl:value-of select="$tokenizedHref[1]" />
						</xsl:attribute>
						 <xsl:value-of select="."/>
						</judge>
					</xsl:when>	
					<!-- the neutralCitation element -->
					<xsl:when test="@class='mNeutralCitation'">
						<neutralCitation xsl:exclude-result-prefixes="#all">
						 <xsl:value-of select="."/>
						</neutralCitation>
					</xsl:when>	
										
					<xsl:when test="@class='mBungeniJudgementNo'">
						<docNumber xsl:exclude-result-prefixes="#all">
							<xsl:attribute name="id">
								<xsl:text>judgement-no-</xsl:text><xsl:value-of select="generate-id()" />
							</xsl:attribute>
							<xsl:attribute name="refersTo">
								<xsl:text>#judgementNo</xsl:text>
							</xsl:attribute>
						 <xsl:value-of select="."/>
						</docNumber>
					</xsl:when>	
	
					<!-- otherwise generate normal reference -->
					<xsl:otherwise>
						<ref xsl:exclude-result-prefixes="#all">
					       <xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
					     </ref>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
		</xsl:if>
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="text()">
		<xsl:value-of select="normalize-space(.)"/>
	</xsl:template>
</xsl:stylesheet>
