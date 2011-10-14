<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:ml="http://www.metalex.org/1.0" 
    xmlns:bungeni="http://editor.bungeni.org/1.0/anx/"
    xmlns:bp="http://www.bungeni.org/pipeline/1.0"
    exclude-result-prefixes="bp"
    version="2.0">
    <xsl:output indent="yes" method="xml"/>

    <!--

    Default templates :
    These templates are required

    -->

    <xsl:template match="/">

        <bp:stylesheets>
            <xsl:apply-templates/>
        </bp:stylesheets>
        
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

    
    <bp:template name="root" href="pipeline_xslt/debaterecord/root.xsl"/>
    
    <bp:template name="body" href="pipeline_xslt/debaterecord/debate_body.xsl"/>
    
    
    
    <bp:template name="Preface" href="pipeline_xslt/common/preface.xsl"/>

    
    <bp:template name="Observation" href="pipeline_xslt/common/observation.xsl"/>
    
    <bp:template name="PapersLaid" href="pipeline_xslt/debaterecord/tableddocuments.xsl"/>
    
    <bp:template name="PapersLaidList" href="pipeline_xslt/debaterecord/tableddocumentslist.xsl"/>
    
    <bp:template name="GroupActivity" href="pipeline_xslt/debaterecord/groupactivity.xsl"/>
    
    <bp:template name="Speech" href="pipeline_xslt/debaterecord/speech.xsl"/>
    
    <bp:template name="QuestionsContainer" href="pipeline_xslt/debaterecord/questionscontainer.xsl"/>
    
    <bp:template name="QuestionAnswer" href="pipeline_xslt/debaterecord/questionanswer.xsl"/>
    
    <bp:template name="Question" href="pipeline_xslt/debaterecord/question.xsl"/>
    
    <bp:template name="PointOfOrder" href="pipeline_xslt/debaterecord/pointoforder.xsl"/>
    
    <bp:template name="PersonalStatement" href="pipeline_xslt/debaterecord/personalstatement.xsl"/>
    
    <bp:template name="MinisterialStatement"
        href="pipeline_xslt/debaterecord/ministerialstatement.xsl"/>
    
    <bp:template name="Petitions" href="pipeline_xslt/debaterecord/petitions.xsl"/>
    
    <bp:template name="PetitionsList" href="pipeline_xslt/debaterecord/petitionslist.xsl"/>
    
    <bp:template name="PMotionsContainer" href="pipeline_xslt/debaterecord/pmotionscontainer.xsl"/>
    
    <bp:template name="NMotionsContainer" href="pipeline_xslt/debaterecord/nmotionscontainer.xsl"/>
    
    <bp:template name="NoticeOfMotion" href="pipeline_xslt/debaterecord/noticeofmotion.xsl"/>
    
    <bp:template name="ProceduralMotion" href="pipeline_xslt/debaterecord/proceduralmotion.xsl"/>
    
    <bp:template name="Person" href="pipeline_xslt/common/person.xsl"/>
    
    <bp:template name="ActionEvent" href="pipeline_xslt/debaterecord/actionevent.xsl"/>
    
    <bp:template name="Communication" href="pipeline_xslt/debaterecord/communication.xsl"/>
    
    <bp:template name="Conclusion" href="pipeline_xslt/common/conclusion.xsl"/>
    
    <bp:template name="span" href="pipeline_xslt/common/span.xsl"/>
    
    <bp:template name="p" href="pipeline_xslt/debaterecord/p.xsl"/>
    
    <bp:template name="ref" href="pipeline_xslt/debaterecord/ref.xsl"/>
    
    <bp:template name="heading" href="pipeline_xslt/common/heading.xsl"/>
    
    <bp:template name="subheading" href="pipeline_xslt/common/subheading.xsl"/>
    
    <bp:template name="list" href="pipeline_xslt/common/list.xsl"/>
    
    <bp:template name="item" href="pipeline_xslt/common/item.xsl"/>
    
    <bp:template name="a" href="pipeline_xslt/common/a.xsl"/>
    
    <bp:template name="meta" href="pipeline_xslt/common/meta.xsl"/>
    
    <bp:template name="identification" href="pipeline_xslt/common/identification.xsl"/>
    
    <bp:template name="FRBRWork" href="pipeline_xslt/common/frbrwork.xsl"/>
    
    <bp:template name="FRBRExpression" href="pipeline_xslt/common/frbrexpression.xsl"/>
    
    <bp:template name="FRBRManifestation" href="pipeline_xslt/common/frbrmanifestation.xsl"/>
    
    <bp:template name="this" href="pipeline_xslt/common/this.xsl"/>
    
    <bp:template name="uri" href="pipeline_xslt/common/uri.xsl"/>
    
    <bp:template name="date" href="pipeline_xslt/common/date.xsl"/>
    
    <bp:template name="author" href="pipeline_xslt/common/frbrauthor.xsl"/>
    
    <bp:template name="publication_mcontainer"
        href="pipeline_xslt/common/publication_mcontainer.xsl"/>
    
    <bp:template name="publication" href="pipeline_xslt/common/publication.xsl"/>
    
    <bp:template name="references" href="pipeline_xslt/common/references.xsl"/>
    
    <bp:template name="TLCOrganization" href="pipeline_xslt/common/tlcorganization.xsl"/>
    
    <bp:template name="TLCPerson" href="pipeline_xslt/common/tlcperson.xsl"/>
    
    <bp:template name="TLCEvent" href="pipeline_xslt/common/tlcevent.xsl"/>
    
    <bp:template name="TLCRole" href="pipeline_xslt/common/tlcrole.xsl"/>
    
    <bp:template name="TLCReference" href="pipeline_xslt/common/tlcreference.xsl"/>
    
    <!--

    PIPELINE TEMPLATES END 

    -->

    
</xsl:stylesheet>
