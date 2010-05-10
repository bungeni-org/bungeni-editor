<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:ml="http://www.metalex.org/1.0"  
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
		<xslt step="0" name="root" href="odttoakn/minixslt/debaterecord/root.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='body']">
		<xslt step="1" name="body" href="odttoakn/minixslt/debaterecord/body.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Preface']">
		<xslt step="2" name="Preface" href="odttoakn/minixslt/debaterecord/MastHead.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Observation']">
		<xslt step="3" name="Observation" href="odttoakn/minixslt/debaterecord/Observation.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='PapersLaid']">
		<xslt step="4" name="PapersLaid" href="odttoakn/minixslt/debaterecord/TabledDocuments.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='PapersLaidList']">
		<xslt step="5" name="PapersLaidList" href="odttoakn/minixslt/debaterecord/TabledDocumentsList.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='GroupActivity']">
		<xslt step="6" name="GroupActivity" href="odttoakn/minixslt/debaterecord/GroupActivity.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Speech']">
		<xslt step="7" name="Speech" href="odttoakn/minixslt/debaterecord/Speech.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='QuestionsContainer']">
		<xslt step="8" name="QuestionsContainer" href="odttoakn/minixslt/debaterecord/QuestionsContainer.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='QuestionAnswer']">
		<xslt step="9" name="QuestionAnswer" href="odttoakn/minixslt/debaterecord/QuestionAnswer.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Question']">
		<xslt step="10" name="Question" href="odttoakn/minixslt/debaterecord/Question.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='PointOfOrder']">
		<xslt step="11" name="PointOfOrder" href="odttoakn/minixslt/debaterecord/PointOfOrder.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='PersonalStatement']">
		<xslt step="12" name="PersonalStatement" href="odttoakn/minixslt/debaterecord/PersonalStatement.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='MinisterialStatement']">
		<xslt step="13" name="MinisterialStatement" href="odttoakn/minixslt/debaterecord/MinisterialStatement.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
		<xsl:template match="*[@name='Petitions']">
		<xslt step="14" name="Petitions" href="odttoakn/minixslt/debaterecord/Petitions.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	
		<xsl:template match="*[@name='PetitionsList']">
		<xslt step="15" name="PetitionsList" href="odttoakn/minixslt/debaterecord/PetitionsList.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	

<xsl:template match="*[@name='PMotionsContainer']">
		<xslt step="16" name="PMotionsContainer" href="odttoakn/minixslt/debaterecord/PMotionsContainer.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='NMotionsContainer']">
		<xslt step="17" name="NMotionsContainer" href="odttoakn/minixslt/debaterecord/NMotionsContainer.xsl" />
		<xsl:apply-templates />
	</xsl:template>
 <!--
	<xsl:template match="*[@name='ProceduralMotion']">
		<xslt step="12" name="ProceduralMotion" href="odttoakn/minixslt/debaterecord/ProceduralMotion.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='MotionsContainer']">
		<xslt step="13" name="MotionsContainer" href="odttoakn/minixslt/debaterecord/MotionsContainer.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	-->

     <!-- changed to NoticeOfMotion 20/July/2009 -->
	<xsl:template match="*[@name='NoticeOfMotion']">
		<xslt step="18" name="NoticeOfMotion" href="odttoakn/minixslt/debaterecord/NoticeOfMotion.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='ProceduralMotion']">
		<xslt step="19" name="ProceduralMotion" href="odttoakn/minixslt/debaterecord/ProceduralMotion.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	

	<xsl:template match="*[@name='Person']">
		<xslt step="20" name="Person" href="odttoakn/minixslt/debaterecord/Person.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='ActionEvent']">
		<xslt step="21" name="ActionEvent" href="odttoakn/minixslt/debaterecord/ActionEvent.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Communication']">
		<xslt step="22" name="Communication" href="odttoakn/minixslt/debaterecord/Communication.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Conclusion']">
		<xslt step="23" name="Conclusion" href="odttoakn/minixslt/debaterecord/Conclusion.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='span']">
		<xslt step="24" name="span" href="odttoakn/minixslt/debaterecord/span.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='p']">
		<xslt step="25" name="p" href="odttoakn/minixslt/debaterecord/p.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='ref']">
		<xslt step="26" name="ref" href="odttoakn/minixslt/debaterecord/ref.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='heading']">
		<xslt step="27" name="heading" href="odttoakn/minixslt/debaterecord/heading.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='subheading']">
		<xslt step="28" name="subheading" href="odttoakn/minixslt/debaterecord/subheading.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='list']">
		<xslt step="29" name="list" href="odttoakn/minixslt/debaterecord/list.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='item']">
		<xslt step="30" name="item" href="odttoakn/minixslt/debaterecord/item.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='a']">
		<xslt step="31" name="a" href="odttoakn/minixslt/debaterecord/a.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='meta']">
		<xslt step="32" name="meta" href="odttoakn/minixslt/debaterecord/meta.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='identification']">
		<xslt step="33" name="identification" href="odttoakn/minixslt/debaterecord/identification.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRWork']">
		<xslt step="34" name="FRBRWork" href="odttoakn/minixslt/debaterecord/FRBRWork.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRExpression']">
		<xslt step="35" name="FRBRExpression" href="odttoakn/minixslt/debaterecord/FRBRExpression.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRManifestation']">
		<xslt step="36" name="FRBRManifestation" href="odttoakn/minixslt/debaterecord/FRBRManifestation.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='this']">
		<xslt step="37" name="this" href="odttoakn/minixslt/debaterecord/this.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='uri']">
		<xslt step="38" name="uri" href="odttoakn/minixslt/debaterecord/uri.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='date']">
		<xslt step="39" name="date" href="odttoakn/minixslt/debaterecord/date.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='author']">
		<xslt step="40" name="author" href="odttoakn/minixslt/debaterecord/author.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='publication_mcontainer']">
		<xslt step="41" name="publication_mcontainer" href="odttoakn/minixslt/debaterecord/publication_mcontainer.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='publication']">
		<xslt step="42" name="publication" href="odttoakn/minixslt/debaterecord/publication.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='references']">
		<xslt step="43" name="references" href="odttoakn/minixslt/debaterecord/references.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='TLCOrganization']">
		<xslt step="44" name="TLCOrganization" href="odttoakn/minixslt/debaterecord/TLCOrganization.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='TLCPerson']">
		<xslt step="45" name="TLCPerson" href="odttoakn/minixslt/debaterecord/TLCPerson.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='TLCEvent']">
		<xslt step="46" name="TLCEvent" href="odttoakn/minixslt/debaterecord/TLCEvent.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='TLCRole']">
		<xslt step="47" name="TLCRole" href="odttoakn/minixslt/debaterecord/TLCRole.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="*[@name='TLCReference']">
		<xslt step="48" name="TLCReference" href="odttoakn/minixslt/debaterecord/TLCReference.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	
	
	
	
	
	

</xsl:stylesheet>
