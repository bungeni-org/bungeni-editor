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

	<xsl:template match="*[@name='root']">
		<xslt step="0" name="root" href="pipeline_xslt/bill/root.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='body']">
		<xslt step="1" name="body" href="pipeline_xslt/bill/body.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Preface']">
		<xslt step="2" name="Preface" href="pipeline_xslt/bill/MastHead.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Preamble']">
		<xslt step="3" name="Preamble" href="pipeline_xslt/bill/Preamble.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Part']">
		<xslt step="4" name="Part" href="pipeline_xslt/bill/Part.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Chapter']">
		<xslt step="5" name="Chapter" href="pipeline_xslt/bill/Chapter.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='SubChapter']">
		<xslt step="6" name="SubChapter" href="pipeline_xslt/bill/SubChapter.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Section']">
		<xslt step="7" name="Section" href="pipeline_xslt/bill/Section.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Paragraph']">
		<xslt step="8" name="Paragraph" href="pipeline_xslt/bill/Paragraph.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Title']">
		<xslt step="9" name="Title" href="pipeline_xslt/bill/Title.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Book']">
		<xslt step="10" name="Book" href="pipeline_xslt/bill/Book.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Tome']">
		<xslt step="11" name="Tome" href="pipeline_xslt/bill/Tome.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Article']">
		<xslt step="12" name="Article" href="pipeline_xslt/bill/Article.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Clause']">
		<xslt step="13" name="Clause" href="pipeline_xslt/bill/Clause.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='SubSection']">
		<xslt step="14" name="SubSection" href="pipeline_xslt/bill/SubSection.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='SubPart']">
		<xslt step="15" name="SubPart" href="pipeline_xslt/bill/SubPart.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='SubParagraph']">
		<xslt step="16" name="SubParagraph" href="pipeline_xslt/bill/SubParagraph.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='SubTitle']">
		<xslt step="17" name="SubTitle" href="pipeline_xslt/bill/SubTitle.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='SubClause']">
		<xslt step="18" name="SubClause" href="pipeline_xslt/bill/SubClause.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Conclusion']">
		<xslt step="19" name="Conclusion" href="pipeline_xslt/bill/Conclusion.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='span']">
		<xslt step="20" name="span" href="pipeline_xslt/bill/span.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='p']">
		<xslt step="21" name="p" href="pipeline_xslt/bill/p.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='ref']">
		<xslt step="22" name="ref" href="pipeline_xslt/bill/ref.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='heading']">
		<xslt step="23" name="heading" href="pipeline_xslt/bill/heading.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='subheading']">
		<xslt step="24" name="subheading" href="pipeline_xslt/bill/subheading.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='list']">
		<xslt step="25" name="list" href="pipeline_xslt/bill/list.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='item']">
		<xslt step="26" name="item" href="pipeline_xslt/bill/item.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='a']">
		<xslt step="27" name="a" href="pipeline_xslt/bill/a.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='content']">
		<xslt step="28" name="content" href="pipeline_xslt/bill/content.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='meta']">
		<xslt step="29" name="meta" href="pipeline_xslt/bill/meta.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='identification']">
		<xslt step="30" name="identification" href="pipeline_xslt/bill/identification.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRWork']">
		<xslt step="31" name="FRBRWork" href="pipeline_xslt/bill/FRBRWork.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRExpression']">
		<xslt step="32" name="FRBRExpression" href="pipeline_xslt/bill/FRBRExpression.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRManifestation']">
		<xslt step="33" name="FRBRManifestation" href="pipeline_xslt/bill/FRBRManifestation.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='this']">
		<xslt step="34" name="this" href="pipeline_xslt/bill/this.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='uri']">
		<xslt step="35" name="uri" href="pipeline_xslt/bill/uri.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='date']">
		<xslt step="36" name="date" href="pipeline_xslt/bill/date.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='author']">
		<xslt step="37" name="author" href="pipeline_xslt/bill/author.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='publication_mcontainer']">
		<xslt step="38" name="publication_mcontainer" href="pipeline_xslt/bill/publication_mcontainer.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='publication']">
		<xslt step="39" name="publication" href="pipeline_xslt/bill/publication.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='references']">
		<xslt step="40" name="references" href="pipeline_xslt/bill/references.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='TLCOrganization']">
		<xslt step="41" name="TLCOrganization" href="pipeline_xslt/bill/TLCOrganization.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='TLCPerson']">
		<xslt step="42" name="TLCPerson" href="pipeline_xslt/bill/TLCPerson.xsl" />
		<xsl:apply-templates />
	</xsl:template>


</xsl:stylesheet>