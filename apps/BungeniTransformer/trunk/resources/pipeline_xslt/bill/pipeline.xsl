<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
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

    <xsl:template match="bungeni:*">
      <!--We dont want to output the bungeni namespace metadata -->
    </xsl:template>

	<xsl:template match="*[@name='root']">
		<xslt step="0" name="root" href="pipeline_xslt/bill/root.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='body']">
		<xslt name="body" href="pipeline_xslt/bill/body.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Preface']">
		<xslt name="Preface" href="pipeline_xslt/bill/masthead.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Preamble']">
		<xslt name="Preamble" href="pipeline_xslt/bill/preamble.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Part']">
		<xslt name="Part" href="pipeline_xslt/bill/part.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Chapter']">
		<xslt name="Chapter" href="pipeline_xslt/bill/chapter.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='SubChapter']">
		<xslt name="SubChapter" href="pipeline_xslt/bill/subchapter.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Section']">
		<xslt name="Section" href="pipeline_xslt/bill/section.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Paragraph']">
		<xslt name="Paragraph" href="pipeline_xslt/bill/paragraph.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Title']">
		<xslt step="9" name="Title" href="pipeline_xslt/bill/title.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Book']">
		<xslt name="Book" href="pipeline_xslt/bill/book.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Tome']">
		<xslt name="Tome" href="pipeline_xslt/bill/tome.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Article']">
		<xslt name="Article" href="pipeline_xslt/bill/article.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Clause']">
		<xslt name="Clause" href="pipeline_xslt/bill/clause.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='SubSection']">
		<xslt name="SubSection" href="pipeline_xslt/bill/subsection.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='SubPart']">
		<xslt name="SubPart" href="pipeline_xslt/bill/subpart.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='SubParagraph']">
		<xslt name="SubParagraph" href="pipeline_xslt/bill/subparagraph.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='SubTitle']">
		<xslt name="SubTitle" href="pipeline_xslt/bill/subtitle.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='SubClause']">
		<xslt name="SubClause" href="pipeline_xslt/bill/subclause.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Conclusion']">
		<xslt name="Conclusion" href="pipeline_xslt/common/conclusion.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='span']">
		<xslt name="span" href="pipeline_xslt/common/span.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='p']">
		<xslt name="p" href="pipeline_xslt/common/p.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='ref']">
		<xslt name="ref" href="pipeline_xslt/bill/ref.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='heading']">
		<xslt name="heading" href="pipeline_xslt/common/heading.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='subheading']">
		<xslt name="subheading" href="pipeline_xslt/common/subheading.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='list']">
		<xslt name="list" href="pipeline_xslt/common/list.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='item']">
		<xslt name="item" href="pipeline_xslt/common/item.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='a']">
		<xslt name="a" href="pipeline_xslt/common/a.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='content']">
		<xslt name="content" href="pipeline_xslt/bill/content.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='meta']">
		<xslt name="meta" href="pipeline_xslt/common/meta.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='identification']">
		<xslt name="identification" href="pipeline_xslt/common/identification.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRWork']">
		<xslt name="FRBRWork" href="pipeline_xslt/common/frbrwork.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRExpression']">
		<xslt name="FRBRExpression" href="pipeline_xslt/common/frbrexpression.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRManifestation']">
		<xslt name="FRBRManifestation" href="pipeline_xslt/common/frbrmanifestation.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='this']">
		<xslt name="this" href="pipeline_xslt/common/this.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='uri']">
		<xslt name="uri" href="pipeline_xslt/common/uri.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='date']">
		<xslt name="date" href="pipeline_xslt/common/date.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='author']">
		<xslt name="author" href="pipeline_xslt/common/frbrauthor.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='publication_mcontainer']">
		<xslt name="publication_mcontainer" href="pipeline_xslt/common/publication_mcontainer.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='publication']">
		<xslt name="publication" href="pipeline_xslt/common/publication.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='references']">
		<xslt name="references" href="pipeline_xslt/common/references.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='TLCOrganization']">
		<xslt name="TLCOrganization" href="pipeline_xslt/common/tlcorganization.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='TLCPerson']">
		<xslt name="TLCPerson" href="pipeline_xslt/common/tlcperson.xsl" />
		<xsl:apply-templates />
	</xsl:template>


</xsl:stylesheet>