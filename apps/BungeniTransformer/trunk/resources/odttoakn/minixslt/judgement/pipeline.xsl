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
		<xslt step="0" name="root" href="odttoakn/minixslt/judgement/root.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='body']">
		<xslt step="1" name="body" href="odttoakn/minixslt/judgement/body.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Header']">
		<xslt step="2" name="Header" href="odttoakn/minixslt/judgement/Header.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='Introduction']">
		<xslt step="3" name="Introduction" href="odttoakn/minixslt/judgement/introduction.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='Background']">
		<xslt step="4" name="Background" href="odttoakn/minixslt/judgement/background.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='Motivation']">
		<xslt step="5" name="Motivation" href="odttoakn/minixslt/judgement/motivation.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='Decision']">
		<xslt step="6" name="Decision" href="odttoakn/minixslt/judgement/decision.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	
	<xsl:template match="*[@name='Conclusion']">
		<xslt step="7" name="Conclusion" href="odttoakn/minixslt/judgement/Conclusion.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
		
	<xsl:template match="*[@name='Omissis']">
		<xslt step="8" name="Omissis" href="odttoakn/minixslt/judgement/omissis.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<!--

	<xsl:template match="*[@name='Observation']">
		<xslt step="3" name="Observation" href="odttoakn/minixslt/judgement/Observation.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Person']">
		<xslt step="20" name="Person" href="odttoakn/minixslt/judgement/Person.xsl" />
		<xsl:apply-templates />
	</xsl:template>
   

	<xsl:template match="*[@name='Conclusion']">
		<xslt step="23" name="Conclusion" href="odttoakn/minixslt/judgement/Conclusion.xsl" />
		<xsl:apply-templates />
	</xsl:template>
   -->
	<xsl:template match="*[@name='span']">
		<xslt step="24" name="span" href="odttoakn/minixslt/judgement/span.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='p']">
		<xslt step="25" name="p" href="odttoakn/minixslt/judgement/p.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='ref']">
		<xslt step="26" name="ref" href="odttoakn/minixslt/judgement/ref.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='heading']">
		<xslt step="27" name="heading" href="odttoakn/minixslt/judgement/heading.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='subheading']">
		<xslt step="28" name="subheading" href="odttoakn/minixslt/judgement/subheading.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='list']">
		<xslt step="29" name="list" href="odttoakn/minixslt/judgement/list.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='item']">
		<xslt step="30" name="item" href="odttoakn/minixslt/judgement/item.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='a']">
		<xslt step="31" name="a" href="odttoakn/minixslt/judgement/a.xsl" />
		<xsl:apply-templates />
	</xsl:template>
 
	<xsl:template match="*[@name='meta']">
		<xslt step="32" name="meta" href="odttoakn/minixslt/judgement/meta.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='identification']">
		<xslt step="33" name="identification" href="odttoakn/minixslt/judgement/identification.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRWork']">
		<xslt step="34" name="FRBRWork" href="odttoakn/minixslt/judgement/FRBRWork.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRExpression']">
		<xslt step="35" name="FRBRExpression" href="odttoakn/minixslt/judgement/FRBRExpression.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRManifestation']">
		<xslt step="36" name="FRBRManifestation" href="odttoakn/minixslt/judgement/FRBRManifestation.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='this']">
		<xslt step="37" name="this" href="odttoakn/minixslt/judgement/this.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='uri']">
		<xslt step="38" name="uri" href="odttoakn/minixslt/judgement/uri.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='date']">
		<xslt step="39" name="date" href="odttoakn/minixslt/judgement/date.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='author']">
		<xslt step="40" name="author" href="odttoakn/minixslt/judgement/author.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='publication_mcontainer']">
		<xslt step="41" name="publication_mcontainer" href="odttoakn/minixslt/judgement/publication_mcontainer.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='publication']">
		<xslt step="42" name="publication" href="odttoakn/minixslt/judgement/publication.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='references']">
		<xslt step="43" name="references" href="odttoakn/minixslt/judgement/references.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='TLCOrganization']">
		<xslt step="44" name="TLCOrganization" href="odttoakn/minixslt/judgement/TLCOrganization.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='TLCPerson']">
		<xslt step="45" name="TLCPerson" href="odttoakn/minixslt/judgement/TLCPerson.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='TLCEvent']">
		<xslt step="46" name="TLCEvent" href="odttoakn/minixslt/judgement/TLCEvent.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='TLCRole']">
		<xslt step="47" name="TLCRole" href="odttoakn/minixslt/judgement/TLCRole.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
    <xsl:template match="*[@name='TLCConcept']">
		<xslt step="48" name="TLCConcept" href="odttoakn/minixslt/judgement/TLCConcept.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
</xsl:stylesheet>
