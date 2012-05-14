<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:ml="http://www.metalex.org/1.0" 
    xmlns:bungeni="http://editor.bungeni.org/1.0/anx/"
    xmlns:bp="http://www.bungeni.org/pipeline/1.0"
    xmlns:bodf="http://editor.bungeni.org/1.0/odf/"
    exclude-result-prefixes="bp" 
    version="2.0">
    <xsl:output indent="yes" method="xml"/>
    

    <xsl:template match="/">
        <stylesheets>
        	<xsl:apply-templates/>
        </stylesheets>
    </xsl:template>

	<xsl:template match="*">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="text()">
       <xsl:value-of select="normalize-space(.)" />
    </xsl:template> 

	<xsl:template match="*[@name='root']">
		<xslt name="root" href="pipeline_xslt/judgement/root.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='body']">
		<xslt name="body" href="pipeline_xslt/judgement/body.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Header']">
		<xslt name="Header" href="pipeline_xslt/judgement/header.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='Introduction']">
		<xslt name="Introduction" href="pipeline_xslt/judgement/introduction.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='Background']">
		<xslt name="Background" href="pipeline_xslt/judgement/background.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='Motivation']">
		<xslt name="Motivation" href="pipeline_xslt/judgement/motivation.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='Decision']">
		<xslt name="Decision" href="pipeline_xslt/judgement/decision.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	
	<xsl:template match="*[@name='Conclusion']">
		<xslt name="Conclusion" href="pipeline_xslt/common/conclusion.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
		
	<xsl:template match="*[@name='Omissis']">
		<xslt name="Omissis" href="pipeline_xslt/judgement/omissis.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	

	<xsl:template match="*[@name='span']">
		<xslt name="span" href="pipeline_xslt/common/span.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='p']">
		<xslt name="p" href="pipeline_xslt/common/p.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='ref']">
		<xslt name="ref" href="pipeline_xslt/judgement/ref.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='heading']">
		<xslt name="heading" href="pipeline_xslt/common/heading.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='subheading']">
		<xslt name="subheading" href="pipeline_xslt/common/subheading.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='list']">
		<xslt name="list" href="pipeline_xslt/common/list.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='item']">
		<xslt name="item" href="pipeline_xslt/common/item.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='a']">
		<xslt name="a" href="pipeline_xslt/common/a.xsl" />
		<xsl:apply-templates />
	</xsl:template>
 
	<xsl:template match="*[@name='meta']">
		<xslt name="meta" href="pipeline_xslt/common/meta.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='identification']">
		<xslt name="identification" href="pipeline_xslt/common/identification.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRWork']">
		<xslt name="FRBRWork" href="pipeline_xslt/common/frbrwork.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRExpression']">
		<xslt name="FRBRExpression" href="pipeline_xslt/common/frbrexpression.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRManifestation']">
		<xslt name="FRBRManifestation" href="pipeline_xslt/common/frbrmanifestation.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='this']">
		<xslt name="this" href="pipeline_xslt/common/this.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='uri']">
		<xslt name="uri" href="pipeline_xslt/common/uri.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='date']">
		<xslt name="date" href="pipeline_xslt/common/date.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='author']">
		<xslt name="author" href="pipeline_xslt/common/frbrauthor.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='publication_mcontainer']">
		<xslt name="publication_mcontainer" href="pipeline_xslt/common/publication_mcontainer.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='publication']">
		<xslt name="publication" href="pipeline_xslt/common/publication.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='references']">
		<xslt name="references" href="pipeline_xslt/common/references.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='TLCOrganization']">
		<xslt name="TLCOrganization" href="pipeline_xslt/common/tlcorganization.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='TLCPerson']">
		<xslt name="TLCPerson" href="pipeline_xslt/common/tlcperson.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='TLCEvent']">
		<xslt name="TLCEvent" href="pipeline_xslt/common/tlcevent.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='TLCRole']">
		<xslt name="TLCRole" href="pipeline_xslt/common/tlcrole.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
    <xsl:template match="*[@name='TLCConcept']">
		<xslt name="TLCConcept" href="pipeline_xslt/common/tlcconcept.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
</xsl:stylesheet>
