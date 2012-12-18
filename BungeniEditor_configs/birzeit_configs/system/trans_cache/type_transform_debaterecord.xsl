<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:bungeni="http://editor.bungeni.org/1.0/anx/" xmlns:bodf="http://editor.bungeni.org/1.0/odf/" version="2.0">
  <xsl:template match="*[@name='root']">
    <xsl:element name="akomaNtoso">
      <xsl:element name="debate">
        <xsl:apply-templates select="//*[@name='meta']" />
        <xsl:apply-templates select="//*[@name='Preface']" />
        <xsl:apply-templates select="//*[@name='DebateBody']" />
        <xsl:apply-templates select="//*[@name='Conclusions']" />
      </xsl:element>
    </xsl:element>
  </xsl:template>
  <!--
template for CoverPage
-->
  <xsl:template match="*[@name='CoverPage']">
    <xsl:element name="coverPage">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Preface
-->
  <xsl:template match="*[@name='Preface']">
    <xsl:element name="preface">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for meta
-->
  <xsl:template match="*[@name='meta']">
    <xsl:element name="meta">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for ActionEvent
-->
  <xsl:template match="*[@name='ActionEvent']">
    <xsl:element name="container">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for DebateBody
-->
  <xsl:template match="*[@name='DebateBody']">
    <xsl:element name="debateBody">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for DebateSection
-->
  <xsl:template match="*[@name='DebateSection']">
    <xsl:element name="debateSection">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Communication
-->
  <xsl:template match="*[@name='Communication']">
    <xsl:element name="communication">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Conclusions
-->
  <xsl:template match="*[@name='Conclusions']">
    <xsl:element name="conclusions">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for GroupActivity
-->
  <xsl:template match="*[@name='GroupActivity']">
    <xsl:element name="container">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for LongTitle
-->
  <xsl:template match="*[@name='LongTitle']">
    <xsl:element name="longtitle">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for MetadataContainer
-->
  <xsl:template match="*[@name='MetadataContainer']">
    <xsl:element name="container">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for MinisterialStatement
-->
  <xsl:template match="*[@name='MinisterialStatement']">
    <xsl:element name="ministerialStatements">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Motion
-->
  <xsl:template match="*[@name='Motion']">
    <xsl:element name="proceduralMotions">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for MotionsContainer
-->
  <xsl:template match="*[@name='MotionsContainer']">
    <xsl:element name="container">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for NMotionsContainer
-->
  <xsl:template match="*[@name='NMotionsContainer']">
    <xsl:element name="container">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Narrative
-->
  <xsl:template match="*[@name='Narrative']">
    <xsl:element name="container">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for NoticeOfMotion
-->
  <xsl:template match="*[@name='NoticeOfMotion']">
    <xsl:element name="noticesOfMotion">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for NoticeOfMotionDetails
-->
  <xsl:template match="*[@name='NoticeOfMotionDetails']">
    <xsl:element name="container">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Observation
-->
  <xsl:template match="*[@name='Observation']">
    <xsl:element name="container">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:attribute name="name">
        <xsl:text>observation</xsl:text>
      </xsl:attribute>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Other
-->
  <xsl:template match="*[@name='Other']">
    <xsl:element name="other">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for PMotionsContainer
-->
  <xsl:template match="*[@name='PMotionsContainer']">
    <xsl:element name="container">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for PapersLaid
-->
  <xsl:template match="*[@name='PapersLaid']">
    <xsl:element name="papersLaid">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for PapersLaidList
-->
  <xsl:template match="*[@name='PapersLaidList']">
    <xsl:element name="other">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for PersonalStatement
-->
  <xsl:template match="*[@name='PersonalStatement']">
    <xsl:element name="personalStatement">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Prayers
-->
  <xsl:template match="*[@name='Prayers']">
    <xsl:element name="prayers">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for QuestionAnswer
-->
  <xsl:template match="*[@name='QuestionAnswer']">
    <xsl:element name="container">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Speech
-->
  <xsl:template match="*[@name='Speech']">
    <xsl:element name="speech">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:attribute name="as">
        <xsl:text>#</xsl:text>
        <xsl:value-of select="./bungeni:bungenimeta/bungeni:BungeniSpeechAs" />
      </xsl:attribute>
      <xsl:attribute name="asText">
        <xsl:value-of select="./bungeni:bungenimeta/bungeni:BungeniSpeechAs" />
      </xsl:attribute>
      <xsl:attribute name="by">
        <xsl:text>#</xsl:text>
        <xsl:value-of select="./bungeni:bungenimeta/bungeni:BungeniPersonID" />
      </xsl:attribute>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for body
-->
  <xsl:template match="*[@name='body']">
    <xsl:element name="debate">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for from
-->
  <xsl:template match="*[@name='from']">
    <xsl:element name="from">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for ref
-->
  <xsl:template match="*[@name='ref']">
    <xsl:element name="ref">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:attribute name="href">
        <xsl:value-of select="./bungeni:bungenimeta/bungeni:BungeniRefURI" />
      </xsl:attribute>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <xsl:template match="*[@name='identification']">
    <identification>
      <xsl:if test="@source">
        <xsl:attribute name="source">
          <xsl:value-of select="@source" />
        </xsl:attribute>
      </xsl:if>
      <xsl:apply-templates />
    </identification>
  </xsl:template>
  <xsl:template match="*[@name='frbrwork']">
    <!--
            This is what the template intends to match : 
            <mcontainer name="frbrwork">
            <meta name="uri" value="/ke/debaterecord/2012-10-27"/>
            <meta name="author" value="user.Ashok"/>
            <meta name="date" value="2012-10-27"/>
            <meta name="date_name" value="workDate"/>
            </mcontainer>
        -->
    <FRBRWork>
      <xsl:apply-templates select="./*[@name='uri']" mode="meta_gen" />
      <xsl:apply-templates select="./*[@name='author']" mode="meta_gen" />
      <xsl:call-template name="frbrdate_generator" />
      <xsl:call-template name="frbrcountry_generator" />
    </FRBRWork>
  </xsl:template>
  <xsl:template match="*[@name='frbrexpression']">
    <FRBRExpression>
      <xsl:apply-templates select="./*[@name='uri']" mode="meta_gen" />
      <xsl:apply-templates select="./*[@name='author']" mode="meta_gen" />
      <xsl:call-template name="frbrdate_generator" />
      <xsl:call-template name="frbrcountry_generator" />
      <xsl:call-template name="frbrlanguage_generator" />
    </FRBRExpression>
  </xsl:template>
  <xsl:template match="*[@name='frbrmanifestation']">
    <FRBRManifestation>
      <xsl:apply-templates select="./*[@name='uri']" mode="meta_gen" />
      <xsl:apply-templates select="./*[@name='author']" mode="meta_gen" />
      <xsl:call-template name="frbrdate_generator" />
      <xsl:call-template name="frbrcountry_generator" />
      <xsl:call-template name="frbrlanguage_generator" />
    </FRBRManifestation>
  </xsl:template>
  <xsl:template match="*[@name='uri']" mode="meta_gen">
    <FRBRthis value="{@value}/main" />
    <FRBRuri value="{@value}" />
  </xsl:template>
  <xsl:template match="*[@name='author']" mode="meta_gen">
    <FRBRauthor href="#parliament" as="#author" />
  </xsl:template>
  <xsl:template name="frbrdate_generator">
    <FRBRdate date="{./*[@name='date']/@value}" name="{./*[@name='date_name']/@value}" />
  </xsl:template>
  <xsl:template name="frbrcountry_generator">
    <FRBRcountry value="{//*[@name='BungeniCountryCode']/@value}" />
  </xsl:template>
  <xsl:template name="frbrlanguage_generator">
    <FRBRlanguage value="{//*[@name='BungeniLanguageCode']/@value}" />
  </xsl:template>
  <xsl:template match="*[@name='frbrthis']">
    <FRBRthis>
      <xsl:if test="@value">
        <xsl:attribute name="value">
          <xsl:value-of select="@value" />
        </xsl:attribute>
      </xsl:if>
      <xsl:apply-templates />
    </FRBRthis>
  </xsl:template>
  <xsl:template match="*[@name='frbruri']">
    <FRBRuri>
      <xsl:if test="@value">
        <xsl:attribute name="value">
          <xsl:value-of select="@value" />
        </xsl:attribute>
      </xsl:if>
      <xsl:apply-templates />
    </FRBRuri>
  </xsl:template>
  <xsl:template match="*[@name='frbrdate']">
    <FRBRdate>
      <xsl:if test="@date">
        <xsl:attribute name="date">
          <xsl:value-of select="@date" />
        </xsl:attribute>
      </xsl:if>
      <xsl:attribute name="name">
        <xsl:value-of select="@contentName" />
      </xsl:attribute>
      <xsl:apply-templates />
    </FRBRdate>
  </xsl:template>
  <xsl:template match="*[@name='frbrauthor']">
    <FRBRauthor>
      <xsl:if test="@href">
        <xsl:attribute name="href">
          <xsl:value-of select="@href" />
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="@as">
        <xsl:attribute name="as">
          <xsl:value-of select="@as" />
        </xsl:attribute>
      </xsl:if>
      <xsl:apply-templates />
    </FRBRauthor>
  </xsl:template>
  <xsl:template match="*[@name='publication']">
    <publication>
      <xsl:if test="./*[@name='date']">
        <xsl:attribute name="date">
          <xsl:value-of select="./*[@name='date']/@value" />
        </xsl:attribute>
      </xsl:if>
      <xsl:attribute name="name">
        <xsl:value-of select="./*[@name='name']/@value" />
      </xsl:attribute>
      <xsl:attribute name="number">
        <xsl:value-of select="./*[@name='number']/@value" />
      </xsl:attribute>
      <xsl:apply-templates />
    </publication>
  </xsl:template>
  <xsl:template match="*[@name='references']">
    <references>
      <xsl:if test="@source">
        <xsl:attribute name="source">
          <xsl:value-of select="@source" />
        </xsl:attribute>
      </xsl:if>
      <!-- we use copyof instead of applytemplates because the children are rendered 
                natively as akomaNtoso -->
      <xsl:copy-of select="child::*" />
    </references>
  </xsl:template>
  <xsl:template match="*[@name='proprietary']">
    <xsl:copy-of select="child::*" />
  </xsl:template>
  <xsl:template match="*[@name='TLCOrganization']">
    <TLCOrganization>
      <xsl:if test="@id">
        <xsl:attribute name="id">
          <xsl:value-of select="@id" />
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="@href">
        <xsl:attribute name="href">
          <xsl:value-of select="@href" />
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="@showAs">
        <xsl:attribute name="showAs">
          <xsl:value-of select="@showAs" />
        </xsl:attribute>
      </xsl:if>
      <xsl:apply-templates />
    </TLCOrganization>
  </xsl:template>
  <xsl:template match="*[@name='TLCEvent']">
    <TLCEvent>
      <xsl:if test="@id">
        <xsl:attribute name="id">
          <xsl:value-of select="@id" />
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="@href">
        <xsl:attribute name="href">
          <xsl:value-of select="@href" />
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="@showAs">
        <xsl:attribute name="showAs">
          <xsl:value-of select="@showAs" />
        </xsl:attribute>
      </xsl:if>
      <xsl:apply-templates />
    </TLCEvent>
  </xsl:template>
  <xsl:template match="*[@name='TLCReference']">
    <TLCReference>
      <xsl:if test="@id">
        <xsl:attribute name="id">
          <xsl:value-of select="@id" />
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="@href">
        <xsl:attribute name="href">
          <xsl:value-of select="@href" />
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="@showAs">
        <xsl:attribute name="showAs">
          <xsl:value-of select="@showAs" />
        </xsl:attribute>
      </xsl:if>
      <xsl:apply-templates />
    </TLCReference>
  </xsl:template>
  <xsl:template match="*[@name='span']">
    <span>
      <xsl:apply-templates />
    </span>
  </xsl:template>
  <xsl:template match="*[@name='p']">
    <p>
      <xsl:apply-templates />
    </p>
  </xsl:template>
  <xsl:template match="bungeni:bungenimeta" />
</xsl:stylesheet>

