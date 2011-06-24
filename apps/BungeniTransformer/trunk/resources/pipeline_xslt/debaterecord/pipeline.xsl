<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:ml="http://www.metalex.org/1.0" 
    xmlns:bungeni="http://editor.bungeni.org/1.0/anx/"
    version="2.0">
    <xsl:output indent="yes" method="xml"/>

    <!--

    Default templates :
    These templates are required

    -->

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


    <xsl:template match="bungeni:*">
      <!--We dont want to output the bungeni namespace metadata -->
    </xsl:template>

    <!--

    PIPELINE TEMPLATES FOLLOW

    -->

    <xsl:template match="*[@name='root']">
        <xslt name="root" href="pipeline_xslt/debaterecord/root.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='body']">
        <xslt name="body" href="pipeline_xslt/debaterecord/debate_body.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Preface']">
        <xslt name="Preface" href="pipeline_xslt/debaterecord/preface.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Observation']">
        <xslt name="Observation" href="pipeline_xslt/debaterecord/observation.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='PapersLaid']">
        <xslt name="PapersLaid" href="pipeline_xslt/debaterecord/tableddocuments.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='PapersLaidList']">
        <xslt name="PapersLaidList" href="pipeline_xslt/debaterecord/tableddocumentslist.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='GroupActivity']">
        <xslt name="GroupActivity" href="pipeline_xslt/debaterecord/groupactivity.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Speech']">
        <xslt name="Speech" href="pipeline_xslt/debaterecord/speech.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='QuestionsContainer']">
        <xslt name="QuestionsContainer" href="pipeline_xslt/debaterecord/questionscontainer.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='QuestionAnswer']">
        <xslt name="QuestionAnswer" href="pipeline_xslt/debaterecord/questionanswer.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Question']">
        <xslt name="Question" href="pipeline_xslt/debaterecord/question.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='PointOfOrder']">
        <xslt name="PointOfOrder" href="pipeline_xslt/debaterecord/pointoforder.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='PersonalStatement']">
        <xslt name="PersonalStatement" href="pipeline_xslt/debaterecord/personalstatement.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='MinisterialStatement']">
        <xslt name="MinisterialStatement"
            href="pipeline_xslt/debaterecord/ministerialstatement.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Petitions']">
        <xslt name="Petitions" href="pipeline_xslt/debaterecord/petitions.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>


    <xsl:template match="*[@name='PetitionsList']">
        <xslt name="PetitionsList" href="pipeline_xslt/debaterecord/petitionslist.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>


    <xsl:template match="*[@name='PMotionsContainer']">
        <xslt name="PMotionsContainer" href="pipeline_xslt/debaterecord/pmotionscontainer.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='NMotionsContainer']">
        <xslt name="NMotionsContainer" href="pipeline_xslt/debaterecord/nmotionscontainer.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="*[@name='NoticeOfMotion']">
        <xslt name="NoticeOfMotion" href="pipeline_xslt/debaterecord/noticeofmotion.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='ProceduralMotion']">
        <xslt name="ProceduralMotion" href="pipeline_xslt/debaterecord/proceduralmotion.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>


    <xsl:template match="*[@name='Person']">
        <xslt name="Person" href="pipeline_xslt/debaterecord/person.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='ActionEvent']">
        <xslt name="ActionEvent" href="pipeline_xslt/debaterecord/actionevent.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Communication']">
        <xslt name="Communication" href="pipeline_xslt/debaterecord/communication.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='Conclusion']">
        <xslt name="Conclusion" href="pipeline_xslt/debaterecord/conclusion.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='span']">
        <xslt name="span" href="pipeline_xslt/debaterecord/span.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='p']">
        <xslt name="p" href="pipeline_xslt/debaterecord/p.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='ref']">
        <xslt name="ref" href="pipeline_xslt/debaterecord/ref.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='heading']">
        <xslt name="heading" href="pipeline_xslt/debaterecord/heading.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='subheading']">
        <xslt name="subheading" href="pipeline_xslt/debaterecord/subheading.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='list']">
        <xslt name="list" href="pipeline_xslt/debaterecord/list.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='item']">
        <xslt name="item" href="pipeline_xslt/debaterecord/item.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='a']">
        <xslt name="a" href="pipeline_xslt/debaterecord/a.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='meta']">
        <xslt name="meta" href="pipeline_xslt/debaterecord/meta.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='identification']">
        <xslt name="identification" href="pipeline_xslt/debaterecord/identification.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='FRBRWork']">
        <xslt name="FRBRWork" href="pipeline_xslt/debaterecord/frbrwork.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='FRBRExpression']">
        <xslt name="FRBRExpression" href="pipeline_xslt/debaterecord/frbrexpression.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='FRBRManifestation']">
        <xslt name="FRBRManifestation" href="pipeline_xslt/debaterecord/frbrmanifestation.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='this']">
        <xslt name="this" href="pipeline_xslt/debaterecord/this.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='uri']">
        <xslt name="uri" href="pipeline_xslt/debaterecord/uri.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='date']">
        <xslt name="date" href="pipeline_xslt/debaterecord/date.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='author']">
        <xslt name="author" href="pipeline_xslt/debaterecord/frbrauthor.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='publication_mcontainer']">
        <xslt name="publication_mcontainer"
            href="pipeline_xslt/debaterecord/publication_mcontainer.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='publication']">
        <xslt name="publication" href="pipeline_xslt/debaterecord/publication.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='references']">
        <xslt name="references" href="pipeline_xslt/debaterecord/references.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='TLCOrganization']">
        <xslt name="TLCOrganization" href="pipeline_xslt/debaterecord/tlcorganization.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='TLCPerson']">
        <xslt name="TLCPerson" href="pipeline_xslt/debaterecord/tlcperson.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='TLCEvent']">
        <xslt name="TLCEvent" href="pipeline_xslt/debaterecord/tlcevent.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='TLCRole']">
        <xslt name="TLCRole" href="pipeline_xslt/debaterecord/tlcrole.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[@name='TLCReference']">
        <xslt name="TLCReference" href="pipeline_xslt/debaterecord/tlcreference.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <!--

    PIPELINE TEMPLATES END 

    -->

    
</xsl:stylesheet>
