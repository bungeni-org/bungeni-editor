<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:bungeni="http://editor.bungeni.org/1.0/anx/" xmlns:bodf="http://editor.bungeni.org/1.0/odf/" version="2.0">
  <xsl:template match="*[@name='root']">
    <xsl:element name="akomaNtoso">
      <xsl:element name="judgement">
        <xsl:apply-templates select="//*[@name='meta']" />
        <xsl:apply-templates select="//*[@name='Heading']" />
        <xsl:apply-templates select="//*[@name='contents']" />
        <xsl:apply-templates select="//*[@name='conclusions']" />
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
template for contents
-->
  <xsl:template match="*[@name='contents']">
    <xsl:element name="contents">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for conclusions
-->
  <xsl:template match="*[@name='conclusions']">
    <xsl:element name="conclusions">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for casename
-->
  <xsl:template match="*[@name='casename']">
    <xsl:element name="casename">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Judges
-->
  <xsl:template match="*[@name='Judges']">
    <xsl:element name="judges">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Judge
-->
  <xsl:template match="*[@name='Judge']">
    <xsl:element name="judge">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for Parties
-->
  <xsl:template match="*[@name='Parties']">
    <xsl:element name="parties">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for basic-info
-->
  <xsl:template match="*[@name='basic-info']">
    <xsl:element name="basic-info">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for casesubject
-->
  <xsl:template match="*[@name='casesubject']">
    <xsl:element name="casesubject">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for casedatec
-->
  <xsl:template match="*[@name='casedatec']">
    <xsl:element name="casedatec">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for casenumberc
-->
  <xsl:template match="*[@name='casenumberc']">
    <xsl:element name="casenumberc">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for casecityc
-->
  <xsl:template match="*[@name='casecityc']">
    <xsl:element name="casecityc">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for casedatea
-->
  <xsl:template match="*[@name='casedatea']">
    <xsl:element name="casedatea">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for casenumbera
-->
  <xsl:template match="*[@name='casenumbera']">
    <xsl:element name="casenumbera">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for casecitya
-->
  <xsl:template match="*[@name='casecitya']">
    <xsl:element name="casecitya">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for casedateh
-->
  <xsl:template match="*[@name='casedateh']">
    <xsl:element name="casedateh">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for casenumberh
-->
  <xsl:template match="*[@name='casenumberh']">
    <xsl:element name="casenumberh">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for casecityh
-->
  <xsl:template match="*[@name='casecityh']">
    <xsl:element name="casecityh">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for casepriority
-->
  <xsl:template match="*[@name='casepriority']">
    <xsl:element name="casepriority">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for content
-->
  <xsl:template match="*[@name='content']">
    <xsl:element name="content">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for conclusionf
-->
  <xsl:template match="*[@name='conclusionf']">
    <xsl:element name="conclusionf">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for con-date
-->
  <xsl:template match="*[@name='con-date']">
    <xsl:element name="con-date">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for con-dateh
-->
  <xsl:template match="*[@name='con-dateh']">
    <xsl:element name="con-dateh">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for con-judges
-->
  <xsl:template match="*[@name='con-judges']">
    <xsl:element name="con-judges">
      <xsl:if test="@id">
        <xsl:attribute name="bodf:sourceId" select="@id" />
      </xsl:if>
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
  <!--
template for con-notes
-->
  <xsl:template match="*[@name='con-notes']">
    <xsl:element name="con-notes">
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
      <xsl:call-template name="frbrauthor_generator" />
      <xsl:call-template name="frbrdate_generator" />
    </FRBRWork>
  </xsl:template>
  <xsl:template match="*[@name='frbrexpression']">
    <FRBRExpression>
      <xsl:apply-templates select="./*[@name='uri']" mode="meta_gen" />
      <xsl:call-template name="frbrauthor_generator" />
      <xsl:call-template name="frbrdate_generator" />
    </FRBRExpression>
  </xsl:template>
  <xsl:template match="*[@name='frbrmanifestation']">
    <FRBRManifestation>
      <xsl:apply-templates select="./*[@name='uri']" mode="meta_gen" />
      <xsl:call-template name="frbrauthor_generator" />
      <xsl:call-template name="frbrdate_generator" />
    </FRBRManifestation>
  </xsl:template>
  <xsl:template match="*[@name='uri']" mode="meta_gen">
    <FRBRthis value="{@value}/main" />
    <FRBRuri value="{@value}" />
  </xsl:template>
  <xsl:template name="frbrauthor_generator">
    <FRBRauthor href="#{./*[@name='author']/@value}" as="#{./*[@name='as']/@value}" />
  </xsl:template>
  <xsl:template name="frbrdate_generator">
    <FRBRdate date="{./*[@name='date']/@value}" name="{./*[@name='date_name']/@value}" />
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

