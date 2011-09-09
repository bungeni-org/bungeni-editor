<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:ml="http://www.metalex.org/1.0" >
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
			<xsl:if test="@class">
				<xsl:choose>
					<!--check if speechBy attribute exists then generate a 'from' -->
					<xsl:when test="@class='BungeniSpeechBy'">
						<from>
							<xsl:value-of select="."/>
						</from>
					</xsl:when>
					<!--sBillRef : section Bill Ref -->
					<xsl:when test="@class='sBillRef'">
						<entity xsl:exclude-result-prefixes="#all" >
							<!-- eg. BungeniBill.1;#1 -->
							<xsl:variable name="strHref"><xsl:value-of select="@href" /></xsl:variable>
							<xsl:variable name="tokenizedHref" select="tokenize($strHref,';')"/>
							<!-- "BungeniBill.1" -->
							<xsl:variable name="refersToBillSectionMeta" select="$tokenizedHref[1]" />
							<!-- now do a select of the bill meta from the section meta , -->
							<!-- BillSectionMeta will have Finance Bill;/ke/bill/2008-02-03/12;/ontology/Expression/ke.bill.2008-02-03.12 now -->
							<xsl:variable name="BillSectionMeta" select="ancestor::ml:container/attribute::node()[name() = $refersToBillSectionMeta][1]" /> 
							<!-- Now tokenize the section metadata -->
							<xsl:variable name="tokenizedBillSectionMeta" select="tokenize($BillSectionMeta,';')" />
							<xsl:variable name="BillUri" select="$tokenizedBillSectionMeta[2]" /> 
							<xsl:variable name="BillOntology" select="$tokenizedBillSectionMeta[3]" /> 
							<xsl:attribute name="id">
								<xsl:value-of select="generate-id(node())" />
							</xsl:attribute>
							<xsl:attribute name="refersTo">
								 <xsl:text>#</xsl:text>
								 <xsl:value-of select="translate($BillUri, '/', '')" />
							</xsl:attribute>
							<ref>
							  	<xsl:attribute name="href">
							  		<xsl:value-of select="$BillUri" />
							  	</xsl:attribute>
							    <xsl:value-of select="."/>
							</ref>
						</entity>
					</xsl:when>
                                        <!-- just output the text contained by house closing time
                                            the markup needs to be fixed to set more information about house closing -->
                                        <xsl:when test="@class='BungeniHouseClosingTime'">
							<xsl:value-of select="."/>
					</xsl:when>
				<!-- otherwise generate normal reference -->
					<xsl:otherwise>
					     <ref>
					       <xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
					     </ref>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
		</xsl:if>
		<!-- <xsl:apply-templates/> -->
	</xsl:template>
	<xsl:template match="text()">
		<xsl:value-of select="normalize-space(.)"/>
	</xsl:template>
</xsl:stylesheet>
