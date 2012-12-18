<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:bungeni="http://editor.bungeni.org/1.0/anx/" xmlns:bodf="http://editor.bungeni.org/1.0/odf/" version="2.0">
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
template for Preamble
-->
  <xsl:template match="*[@name='Preamble']">
    <xsl:element name="preamble">
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
template for Recital
-->
  <xsl:template match="*[@name='Recital']">
    <xsl:element name="recital">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Container
-->
  <xsl:template match="*[@name='Container']">
    <xsl:element name="container">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:attribute name="name">
        <xsl:value-of select="./bungeni:bungenimeta/bungeni:BungeniContainerName" />
      </xsl:attribute>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Block
-->
  <xsl:template match="*[@name='Block']">
    <xsl:element name="container">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:attribute name="name">
        <xsl:value-of select="./bungeni:bungenimeta/bungeni:BungeniBlockName" />
      </xsl:attribute>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Recitals
-->
  <xsl:template match="*[@name='Recitals']">
    <xsl:element name="recitals">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Recital
-->
  <xsl:template match="*[@name='Recital']">
    <xsl:element name="recital">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Article
-->
  <xsl:template match="*[@name='Article']">
    <xsl:element name="article">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Chapter
-->
  <xsl:template match="*[@name='Chapter']">
    <xsl:element name="chapter">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Clause
-->
  <xsl:template match="*[@name='Clause']">
    <xsl:element name="clause">
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
    <xsl:element name="longTitle">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Paragraph
-->
  <xsl:template match="*[@name='Paragraph']">
    <xsl:element name="paragraph">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Part
-->
  <xsl:template match="*[@name='Part']">
    <xsl:element name="part">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Section
-->
  <xsl:template match="*[@name='Section']">
    <xsl:element name="section">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for ShortTitle
-->
  <xsl:template match="*[@name='ShortTitle']">
    <xsl:element name="shortTitle">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for SubChapter
-->
  <xsl:template match="*[@name='SubChapter']">
    <xsl:element name="subchapter">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for SubClause
-->
  <xsl:template match="*[@name='SubClause']">
    <xsl:element name="subclause">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for SubParagraph
-->
  <xsl:template match="*[@name='SubParagraph']">
    <xsl:element name="subparagraph">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for SubSection
-->
  <xsl:template match="*[@name='SubSection']">
    <xsl:element name="subsection">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Title
-->
  <xsl:template match="*[@name='Title']">
    <xsl:element name="title">
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
        <xsl:value-of select="./bungeni:bungenimeta/bungeni:BungeniRefHref" />
      </xsl:attribute>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for num
-->
  <xsl:template match="*[@name='num']">
    <xsl:element name="num">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
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

