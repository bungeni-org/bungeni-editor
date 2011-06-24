<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ml="http://www.metalex.org/1.0"
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
		<xslt step="0" name="root" href="pipeline_xslt/judgement/root.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='body']">
		<xslt step="1" name="body" href="pipeline_xslt/judgement/body.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Header']">
		<xslt step="2" name="Header" href="pipeline_xslt/judgement/Header.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='Introduction']">
		<xslt step="3" name="Introduction" href="pipeline_xslt/judgement/introduction.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='Background']">
		<xslt step="4" name="Background" href="pipeline_xslt/judgement/background.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='Motivation']">
		<xslt step="5" name="Motivation" href="pipeline_xslt/judgement/motivation.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='Decision']">
		<xslt step="6" name="Decision" href="pipeline_xslt/judgement/decision.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	
	<xsl:template match="*[@name='Conclusion']">
		<xslt step="7" name="Conclusion" href="pipeline_xslt/judgement/Conclusion.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
		
	<xsl:template match="*[@name='Omissis']">
		<xslt step="8" name="Omissis" href="pipeline_xslt/judgement/omissis.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<!--

	<xsl:template match="*[@name='Observation']">
		<xslt step="3" name="Observation" href="pipeline_xslt/judgement/Observation.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Person']">
		<xslt step="20" name="Person" href="pipeline_xslt/judgement/Person.xsl" />
		<xsl:apply-templates />
	</xsl:template>
   

	<xsl:template match="*[@name='Conclusion']">
		<xslt step="23" name="Conclusion" href="pipeline_xslt/judgement/Conclusion.xsl" />
		<xsl:apply-templates />
	</xsl:template>
   -->
	<xsl:template match="*[@name='span']">
		<xslt step="24" name="span" href="pipeline_xslt/judgement/span.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='p']">
		<xslt step="25" name="p" href="pipeline_xslt/judgement/p.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='ref']">
		<xslt step="26" name="ref" href="pipeline_xslt/judgement/ref.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='heading']">
		<xslt step="27" name="heading" href="pipeline_xslt/judgement/heading.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='subheading']">
		<xslt step="28" name="subheading" href="pipeline_xslt/judgement/subheading.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='list']">
		<xslt step="29" name="list" href="pipeline_xslt/judgement/list.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='item']">
		<xslt step="30" name="item" href="pipeline_xslt/judgement/item.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='a']">
		<xslt step="31" name="a" href="pipeline_xslt/judgement/a.xsl" />
		<xsl:apply-templates />
	</xsl:template>
 
	<xsl:template match="*[@name='meta']">
		<xslt step="32" name="meta" href="pipeline_xslt/judgement/meta.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='identification']">
		<xslt step="33" name="identification" href="pipeline_xslt/judgement/identification.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRWork']">
		<xslt step="34" name="FRBRWork" href="pipeline_xslt/judgement/FRBRWork.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRExpression']">
		<xslt step="35" name="FRBRExpression" href="pipeline_xslt/judgement/FRBRExpression.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRManifestation']">
		<xslt step="36" name="FRBRManifestation" href="pipeline_xslt/judgement/FRBRManifestation.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='this']">
		<xslt step="37" name="this" href="pipeline_xslt/judgement/this.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='uri']">
		<xslt step="38" name="uri" href="pipeline_xslt/judgement/uri.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='date']">
		<xslt step="39" name="date" href="pipeline_xslt/judgement/date.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='author']">
		<xslt step="40" name="author" href="pipeline_xslt/judgement/author.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='publication_mcontainer']">
		<xslt step="41" name="publication_mcontainer" href="pipeline_xslt/judgement/publication_mcontainer.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='publication']">
		<xslt step="42" name="publication" href="pipeline_xslt/judgement/publication.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='references']">
		<xslt step="43" name="references" href="pipeline_xslt/judgement/references.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='TLCOrganization']">
		<xslt step="44" name="TLCOrganization" href="pipeline_xslt/judgement/TLCOrganization.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='TLCPerson']">
		<xslt step="45" name="TLCPerson" href="pipeline_xslt/judgement/TLCPerson.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='TLCEvent']">
		<xslt step="46" name="TLCEvent" href="pipeline_xslt/judgement/TLCEvent.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='TLCRole']">
		<xslt step="47" name="TLCRole" href="pipeline_xslt/judgement/TLCRole.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
    <xsl:template match="*[@name='TLCConcept']">
		<xslt step="48" name="TLCConcept" href="pipeline_xslt/judgement/TLCConcept.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
</xsl:stylesheet>
