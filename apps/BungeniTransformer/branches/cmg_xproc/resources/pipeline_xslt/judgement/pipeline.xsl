<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:ml="http://www.metalex.org/1.0" 
    xmlns:bungeni="http://editor.bungeni.org/1.0/anx/"
    xmlns:bp="http://www.bungeni.org/pipeline/1.0"
    xmlns:bodf="http://editor.bungeni.org/1.0/odf/"
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

    
    <bp:template name="root" href="pipeline_xslt/judgement/root.xsl"/>
    <bp:template name="JudgementBody" href="pipeline_xslt/judgement/body.xsl"/>
    <bp:template name="Header" href="pipeline_xslt/judgement/header.xsl"/>
    <bp:template name="Introduction" href="pipeline_xslt/judgement/introduction.xsl"/>
    <bp:template name="Background" href="pipeline_xslt/judgement/background.xsl"/>
    <bp:template name="Motivation" href="pipeline_xslt/judgement/motivation.xsl"/>
    <bp:template name="MotList" href="pipeline_xslt/judgement/MotList.xsl"/>
    <bp:template name="MotListItem" href="pipeline_xslt/judgement/MotListItem.xsl"/>
    <bp:template name="BackgList" href="pipeline_xslt/judgement/BackgList.xsl"/>
    <bp:template name="BackgListItem" href="pipeline_xslt/judgement/BackgListItem.xsl"/>
    <bp:template name="IntroList" href="pipeline_xslt/judgement/IntroList.xsl"/>
    <bp:template name="IntroListItem" href="pipeline_xslt/judgement/IntroListItem.xsl"/>
    <bp:template name="DeciList" href="pipeline_xslt/judgement/DeciList.xsl"/>
    <bp:template name="DecgListItem" href="pipeline_xslt/judgement/DecgListItem.xsl"/>
    
    
 
    <bp:template name="Decision" href="pipeline_xslt/judgement/decision.xsl"/>
    <bp:template name="Conclusion" href="pipeline_xslt/common/conclusion.xsl"/>
    <bp:template name="Omissis" href="pipeline_xslt/judgement/omissis.xsl"/>
    <bp:template name="span" href="pipeline_xslt/common/span.xsl"/>
    <bp:template name="p" href="pipeline_xslt/common/p.xsl"/>
    <bp:template name="ref" href="pipeline_xslt/judgement/ref.xsl"/>
    <bp:template name="heading" href="pipeline_xslt/common/heading.xsl"/>
    <bp:template name="subheading"  href="pipeline_xslt/common/subheading.xsl"/>
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
    <bp:template name="publication_mcontainer" href="pipeline_xslt/common/publication_mcontainer.xsl"/>
    <bp:template name="publication" href="pipeline_xslt/common/publication.xsl"/>
    <bp:template name="references" href="pipeline_xslt/common/references.xsl"/>
    <bp:template name="TLCOrganization" href="pipeline_xslt/common/tlcorganization.xsl"/>
    <bp:template name="TLCPerson" href="pipeline_xslt/common/tlcperson.xsl"/>
    <bp:template name="TLCEvent" href="pipeline_xslt/common/tlcevent.xsl"/>
    <bp:template name="TLCRole" href="pipeline_xslt/common/tlcrole.xsl"/>
    <bp:template name="TLCConcept" href="pipeline_xslt/common/tlcconcept.xsl"/>
   
   
   
   
   
    
    
   
    
    <!--

    PIPELINE TEMPLATES END 

    -->

    
</xsl:stylesheet>
