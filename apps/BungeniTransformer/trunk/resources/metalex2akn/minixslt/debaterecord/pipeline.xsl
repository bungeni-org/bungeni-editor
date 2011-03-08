<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:ml="http://www.metalex.org/1.0" xmlns:bungeni="http://editor.bungeni.org/1.0/anx/"
    version="2.0">
    <xsl:output indent="yes" method="xml"/>

    <xsl:template match="/">
        <stylesheets>
            <xsl:apply-templates/>
        </stylesheets>
    </xsl:template>

    <xsl:template match="*">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="text()">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template>

    <xsl:template match="*[@name='root']">
        <xslt name="root" href="metalex2akn/minixslt/debaterecord/root.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='body']">
        <xslt name="body" href="metalex2akn/minixslt/debaterecord/debate_body.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Preface']">
        <xslt name="Preface" href="metalex2akn/minixslt/debaterecord/preface.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Observation']">
        <xslt name="Observation" href="metalex2akn/minixslt/debaterecord/observation.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='PapersLaid']">
        <xslt name="PapersLaid" href="metalex2akn/minixslt/debaterecord/tableddocuments.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='PapersLaidList']">
        <xslt name="PapersLaidList" href="metalex2akn/minixslt/debaterecord/tableddocumentslist.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='GroupActivity']">
        <xslt name="GroupActivity" href="metalex2akn/minixslt/debaterecord/groupactivity.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Speech']">
        <xslt name="Speech" href="metalex2akn/minixslt/debaterecord/speech.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='QuestionsContainer']">
        <xslt name="QuestionsContainer" href="metalex2akn/minixslt/debaterecord/questionscontainer.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='QuestionAnswer']">
        <xslt name="QuestionAnswer" href="metalex2akn/minixslt/debaterecord/questionanswer.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Question']">
        <xslt name="Question" href="metalex2akn/minixslt/debaterecord/question.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='PointOfOrder']">
        <xslt name="PointOfOrder" href="metalex2akn/minixslt/debaterecord/pointoforder.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='PersonalStatement']">
        <xslt name="PersonalStatement" href="metalex2akn/minixslt/debaterecord/personalstatement.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='MinisterialStatement']">
        <xslt name="MinisterialStatement"
            href="metalex2akn/minixslt/debaterecord/ministerialstatement.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Petitions']">
        <xslt name="Petitions" href="metalex2akn/minixslt/debaterecord/petitions.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>


    <xsl:template match="*[@name='PetitionsList']">
        <xslt name="PetitionsList" href="metalex2akn/minixslt/debaterecord/petitionslist.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>


    <xsl:template match="*[@name='PMotionsContainer']">
        <xslt name="PMotionsContainer" href="metalex2akn/minixslt/debaterecord/pmotionscontainer.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='NMotionsContainer']">
        <xslt name="NMotionsContainer" href="metalex2akn/minixslt/debaterecord/nmotionscontainer.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>
    
    <!--
	<xsl:template match="*[@name='ProceduralMotion']">
		<xslt step name="ProceduralMotion" href="metalex2akn/minixslt/debaterecord/ProceduralMotion.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='MotionsContainer']">
		<xslt step name="MotionsContainer" href="metalex2akn/minixslt/debaterecord/MotionsContainer.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	-->

    <!-- changed to NoticeOfMotion 20/July/2009 -->
    <xsl:template match="*[@name='NoticeOfMotion']">
        <xslt name="NoticeOfMotion" href="metalex2akn/minixslt/debaterecord/noticeofmotion.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='ProceduralMotion']">
        <xslt name="ProceduralMotion" href="metalex2akn/minixslt/debaterecord/proceduralmotion.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>


    <xsl:template match="*[@name='Person']">
        <xslt name="Person" href="metalex2akn/minixslt/debaterecord/person.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='ActionEvent']">
        <xslt name="ActionEvent" href="metalex2akn/minixslt/debaterecord/actionevent.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Communication']">
        <xslt name="Communication" href="metalex2akn/minixslt/debaterecord/communication.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Conclusion']">
        <xslt name="Conclusion" href="metalex2akn/minixslt/debaterecord/conclusion.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='span']">
        <xslt name="span" href="metalex2akn/minixslt/debaterecord/span.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='p']">
        <xslt name="p" href="metalex2akn/minixslt/debaterecord/p.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='ref']">
        <xslt name="ref" href="metalex2akn/minixslt/debaterecord/ref.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='heading']">
        <xslt name="heading" href="metalex2akn/minixslt/debaterecord/heading.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='subheading']">
        <xslt name="subheading" href="metalex2akn/minixslt/debaterecord/subheading.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='list']">
        <xslt name="list" href="metalex2akn/minixslt/debaterecord/list.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='item']">
        <xslt name="item" href="metalex2akn/minixslt/debaterecord/item.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='a']">
        <xslt name="a" href="metalex2akn/minixslt/debaterecord/a.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='meta']">
        <xslt name="meta" href="metalex2akn/minixslt/debaterecord/meta.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='identification']">
        <xslt name="identification" href="metalex2akn/minixslt/debaterecord/identification.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='FRBRWork']">
        <xslt name="FRBRWork" href="metalex2akn/minixslt/debaterecord/frbrwork.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='FRBRExpression']">
        <xslt name="FRBRExpression" href="metalex2akn/minixslt/debaterecord/frbrexpression.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='FRBRManifestation']">
        <xslt name="FRBRManifestation" href="metalex2akn/minixslt/debaterecord/frbrmanifestation.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='this']">
        <xslt name="this" href="metalex2akn/minixslt/debaterecord/this.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='uri']">
        <xslt name="uri" href="metalex2akn/minixslt/debaterecord/uri.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='date']">
        <xslt name="date" href="metalex2akn/minixslt/debaterecord/date.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='author']">
        <xslt name="author" href="metalex2akn/minixslt/debaterecord/frbrauthor.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='publication_mcontainer']">
        <xslt name="publication_mcontainer"
            href="metalex2akn/minixslt/debaterecord/publication_mcontainer.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='publication']">
        <xslt name="publication" href="metalex2akn/minixslt/debaterecord/publication.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='references']">
        <xslt name="references" href="metalex2akn/minixslt/debaterecord/references.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='TLCOrganization']">
        <xslt name="TLCOrganization" href="metalex2akn/minixslt/debaterecord/tlcorganization.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='TLCPerson']">
        <xslt name="TLCPerson" href="metalex2akn/minixslt/debaterecord/tlcperson.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='TLCEvent']">
        <xslt name="TLCEvent" href="metalex2akn/minixslt/debaterecord/tlcevent.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='TLCRole']">
        <xslt name="TLCRole" href="metalex2akn/minixslt/debaterecord/tlcrole.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='TLCReference']">
        <xslt name="TLCReference" href="metalex2akn/minixslt/debaterecord/tlcreference.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>
</xsl:stylesheet>
