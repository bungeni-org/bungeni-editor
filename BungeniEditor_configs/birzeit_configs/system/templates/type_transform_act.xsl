<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:bungeni="http://editor.bungeni.org/1.0/anx/" xmlns:bodf="http://editor.bungeni.org/1.0/odf/" version="2.0">
  <xsl:template match="*[@name='root']">
    <xsl:element name="akomaNtoso">
      <xsl:element name="act">
        <xsl:apply-templates select="//*[@name='meta']" />
        <xsl:apply-templates select="//*[@name='Preface']" />
        <xsl:apply-templates select="//*[@name='Preamble']" />
        <xsl:apply-templates select="//*[@name='ActBody']" />
        <xsl:apply-templates select="//*[@name='Conclusions']" />
      </xsl:element>
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
template for ActBody
-->
  <xsl:template match="*[@name='ActBody']">
    <xsl:element name="body">
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
template for Books
-->
  <xsl:template match="*[@name='Books']">
    <xsl:element name="hcontainer">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Book
-->
  <xsl:template match="*[@name='Book']">
    <xsl:element name="book">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Parts
-->
  <xsl:template match="*[@name='Parts']">
    <xsl:element name="hcontainer">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Chapters
-->
  <xsl:template match="*[@name='Chapters']">
    <xsl:element name="hcontainer">
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
template for Sections
-->
  <xsl:template match="*[@name='Sections']">
    <xsl:element name="hcontainer">
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
template for Articles
-->
  <xsl:template match="*[@name='Articles']">
    <xsl:element name="hcontainer">
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
template for Num
-->
  <xsl:template match="*[@name='Num']">
    <xsl:element name="num">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Heading
-->
  <xsl:template match="*[@name='Heading']">
    <xsl:element name="heading">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Subheading
-->
  <xsl:template match="*[@name='Subheading']">
    <xsl:element name="subheading">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Content
-->
  <xsl:template match="*[@name='Content']">
    <xsl:element name="content">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Ref
-->
  <xsl:template match="*[@name='Ref']">
    <xsl:element name="ref">
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

