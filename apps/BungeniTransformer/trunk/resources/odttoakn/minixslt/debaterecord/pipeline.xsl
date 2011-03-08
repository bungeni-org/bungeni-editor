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
        <xslt name="root" href="odttoakn/minixslt/debaterecord/root.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='body']">
        <xslt name="body" href="odttoakn/minixslt/debaterecord/debate_body.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Preface']">
        <xslt name="Preface" href="odttoakn/minixslt/debaterecord/preface.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Observation']">
        <xslt name="Observation" href="odttoakn/minixslt/debaterecord/observation.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='PapersLaid']">
        <xslt name="PapersLaid" href="odttoakn/minixslt/debaterecord/tableddocuments.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='PapersLaidList']">
        <xslt name="PapersLaidList" href="odttoakn/minixslt/debaterecord/tableddocumentslist.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='GroupActivity']">
        <xslt name="GroupActivity" href="odttoakn/minixslt/debaterecord/groupactivity.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Speech']">
        <xslt name="Speech" href="odttoakn/minixslt/debaterecord/speech.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='QuestionsContainer']">
        <xslt name="QuestionsContainer" href="odttoakn/minixslt/debaterecord/questionscontainer.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='QuestionAnswer']">
        <xslt name="QuestionAnswer" href="odttoakn/minixslt/debaterecord/questionanswer.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Question']">
        <xslt name="Question" href="odttoakn/minixslt/debaterecord/question.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='PointOfOrder']">
        <xslt name="PointOfOrder" href="odttoakn/minixslt/debaterecord/pointoforder.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='PersonalStatement']">
        <xslt name="PersonalStatement" href="odttoakn/minixslt/debaterecord/personalstatement.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='MinisterialStatement']">
        <xslt name="MinisterialStatement"
            href="odttoakn/minixslt/debaterecord/ministerialstatement.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Petitions']">
        <xslt name="Petitions" href="odttoakn/minixslt/debaterecord/petitions.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>


    <xsl:template match="*[@name='PetitionsList']">
        <xslt name="PetitionsList" href="odttoakn/minixslt/debaterecord/petitionslist.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>


    <xsl:template match="*[@name='PMotionsContainer']">
        <xslt name="PMotionsContainer" href="odttoakn/minixslt/debaterecord/pmotionscontainer.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='NMotionsContainer']">
        <xslt name="NMotionsContainer" href="odttoakn/minixslt/debaterecord/nmotionsContainer.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>
    <!--
	<xsl:template match="*[@name='ProceduralMotion']">
		<xslt step name="ProceduralMotion" href="odttoakn/minixslt/debaterecord/ProceduralMotion.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='MotionsContainer']">
		<xslt step name="MotionsContainer" href="odttoakn/minixslt/debaterecord/MotionsContainer.xsl" />
		<xsl:apply-templates />
	</xsl:template>
	-->

    <!-- changed to NoticeOfMotion 20/July/2009 -->
    <xsl:template match="*[@name='NoticeOfMotion']">
        <xslt name="NoticeOfMotion" href="odttoakn/minixslt/debaterecord/noticeofmotion.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='ProceduralMotion']">
        <xslt name="ProceduralMotion" href="odttoakn/minixslt/debaterecord/proceduralmotion.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>


    <xsl:template match="*[@name='Person']">
        <xslt name="Person" href="odttoakn/minixslt/debaterecord/person.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='ActionEvent']">
        <xslt name="ActionEvent" href="odttoakn/minixslt/debaterecord/actionevent.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Communication']">
        <xslt name="Communication" href="odttoakn/minixslt/debaterecord/communication.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Conclusion']">
        <xslt name="Conclusion" href="odttoakn/minixslt/debaterecord/conclusion.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='span']">
        <xslt name="span" href="odttoakn/minixslt/debaterecord/span.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='p']">
        <xslt name="p" href="odttoakn/minixslt/debaterecord/p.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='ref']">
        <xslt name="ref" href="odttoakn/minixslt/debaterecord/ref.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='heading']">
        <xslt name="heading" href="odttoakn/minixslt/debaterecord/heading.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='subheading']">
        <xslt name="subheading" href="odttoakn/minixslt/debaterecord/subheading.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='list']">
        <xslt name="list" href="odttoakn/minixslt/debaterecord/list.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='item']">
        <xslt name="item" href="odttoakn/minixslt/debaterecord/item.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='a']">
        <xslt name="a" href="odttoakn/minixslt/debaterecord/a.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='meta']">
        <xslt name="meta" href="odttoakn/minixslt/debaterecord/meta.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='identification']">
        <xslt name="identification" href="odttoakn/minixslt/debaterecord/identification.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='FRBRWork']">
        <xslt name="FRBRWork" href="odttoakn/minixslt/debaterecord/frbrwork.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='FRBRExpression']">
        <xslt name="FRBRExpression" href="odttoakn/minixslt/debaterecord/frbrexpression.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='FRBRManifestation']">
        <xslt name="FRBRManifestation" href="odttoakn/minixslt/debaterecord/frbrmanifestation.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='this']">
        <xslt name="this" href="odttoakn/minixslt/debaterecord/this.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='uri']">
        <xslt name="uri" href="odttoakn/minixslt/debaterecord/uri.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='date']">
        <xslt name="date" href="odttoakn/minixslt/debaterecord/date.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='author']">
        <xslt name="FRBRauthor" href="odttoakn/minixslt/debaterecord/frbrauthor.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='publication_mcontainer']">
        <xslt name="publication_mcontainer"
            href="odttoakn/minixslt/debaterecord/publication_mcontainer.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='publication']">
        <xslt name="publication" href="odttoakn/minixslt/debaterecord/publication.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='references']">
        <xslt name="references" href="odttoakn/minixslt/debaterecord/references.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='TLCOrganization']">
        <xslt name="TLCOrganization" href="odttoakn/minixslt/debaterecord/tlcorganization.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='TLCPerson']">
        <xslt name="TLCPerson" href="odttoakn/minixslt/debaterecord/tlcperson.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='TLCEvent']">
        <xslt name="TLCEvent" href="odttoakn/minixslt/debaterecord/tlcevent.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='TLCRole']">
        <xslt name="TLCRole" href="odttoakn/minixslt/debaterecord/tlcrole.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='TLCReference']">
        <xslt name="TLCReference" href="odttoakn/minixslt/debaterecord/tlcreference.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>
</xsl:stylesheet>
