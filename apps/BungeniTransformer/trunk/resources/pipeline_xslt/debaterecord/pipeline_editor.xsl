<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
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
		<xslt step="0" name="root" href="pipeline_xslt/debaterecord/root.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='body']">
		<xslt step="1" name="body" href="pipeline_xslt/debaterecord/body.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='MastHead']">
		<xslt step="2" name="MastHead" href="pipeline_xslt/debaterecord/MastHead.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Observation']">
		<xslt step="3" name="Observation" href="pipeline_xslt/debaterecord/Observation.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='PapersLaid']">
		<xslt step="4" name="PapersLaid" href="pipeline_xslt/debaterecord/TabledDocuments.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='PapersLaidList']">
		<xslt step="5" name="PapersLaidList" href="pipeline_xslt/debaterecord/TabledDocumentsList.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='GroupActivity']">
		<xslt step="6" name="GroupActivity" href="pipeline_xslt/debaterecord/GroupActivity.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Speech']">
		<xslt step="7" name="Speech" href="pipeline_xslt/debaterecord/Speech.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='QuestionsContainer']">
		<xslt step="8" name="QuestionsContainer" href="pipeline_xslt/debaterecord/QuestionsContainer.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='QuestionAnswer']">
		<xslt step="9" name="QuestionAnswer" href="pipeline_xslt/debaterecord/QuestionAnswer.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Question']">
		<xslt step="10" name="Question" href="pipeline_xslt/debaterecord/Question.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='PointOfOrder']">
		<xslt step="11" name="PointOfOrder" href="pipeline_xslt/debaterecord/PointOfOrder.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='ProceduralMotion']">
		<xslt step="12" name="ProceduralMotion" href="pipeline_xslt/debaterecord/ProceduralMotion.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='MotionsContainer']">
		<xslt step="13" name="MotionsContainer" href="pipeline_xslt/debaterecord/MotionsContainer.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Motion']">
		<xslt step="14" name="Motion" href="pipeline_xslt/debaterecord/Motion.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Person']">
		<xslt step="15" name="Person" href="pipeline_xslt/debaterecord/Person.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='ActionEvent']">
		<xslt step="16" name="ActionEvent" href="pipeline_xslt/debaterecord/ActionEvent.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Communication']">
		<xslt step="17" name="Communication" href="pipeline_xslt/debaterecord/Communication.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Conclusion']">
		<xslt step="18" name="Conclusion" href="pipeline_xslt/debaterecord/Conclusion.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='span']">
		<xslt step="19" name="span" href="pipeline_xslt/debaterecord/span.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='p']">
		<xslt step="20" name="p" href="pipeline_xslt/debaterecord/p.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='ref']">
		<xslt step="21" name="ref" href="pipeline_xslt/debaterecord/ref.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='heading']">
		<xslt step="22" name="heading" href="pipeline_xslt/debaterecord/heading.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='subheading']">
		<xslt step="23" name="subheading" href="pipeline_xslt/debaterecord/subheading.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='list']">
		<xslt step="24" name="list" href="pipeline_xslt/debaterecord/list.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='item']">
		<xslt step="25" name="item" href="pipeline_xslt/debaterecord/item.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='a']">
		<xslt step="26" name="a" href="pipeline_xslt/debaterecord/a.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='meta']">
		<xslt step="27" name="meta" href="pipeline_xslt/debaterecord/meta.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='identification']">
		<xslt step="28" name="identification" href="pipeline_xslt/debaterecord/identification.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRWork']">
		<xslt step="29" name="FRBRWork" href="pipeline_xslt/debaterecord/FRBRWork.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRExpression']">
		<xslt step="30" name="FRBRExpression" href="pipeline_xslt/debaterecord/FRBRExpression.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRManifestation']">
		<xslt step="31" name="FRBRManifestation" href="pipeline_xslt/debaterecord/FRBRManifestation.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='this']">
		<xslt step="32" name="this" href="pipeline_xslt/debaterecord/this.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='uri']">
		<xslt step="33" name="uri" href="pipeline_xslt/debaterecord/uri.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='date']">
		<xslt step="34" name="date" href="pipeline_xslt/debaterecord/date.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='author']">
		<xslt step="35" name="author" href="pipeline_xslt/debaterecord/author.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='publication_mcontainer']">
		<xslt step="36" name="publication_mcontainer" href="pipeline_xslt/debaterecord/publication_mcontainer.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='publication']">
		<xslt step="37" name="publication" href="pipeline_xslt/debaterecord/publication.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='references']">
		<xslt step="38" name="references" href="pipeline_xslt/debaterecord/references.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='TLCOrganization']">
		<xslt step="39" name="TLCOrganization" href="pipeline_xslt/debaterecord/TLCOrganization.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='TLCPerson']">
		<xslt step="40" name="TLCPerson" href="pipeline_xslt/debaterecord/TLCPerson.xsl" />
		<xsl:apply-templates />
	</xsl:template>

</xsl:stylesheet>
