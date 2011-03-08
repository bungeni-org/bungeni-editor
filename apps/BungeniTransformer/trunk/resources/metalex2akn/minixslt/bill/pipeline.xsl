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
		<xslt step="0" name="root" href="metalex2akn/minixslt/bill/root.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='body']">
		<xslt step="1" name="body" href="metalex2akn/minixslt/bill/body.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Preface']">
		<xslt step="2" name="Preface" href="metalex2akn/minixslt/bill/MastHead.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Preamble']">
		<xslt step="3" name="Preamble" href="metalex2akn/minixslt/bill/Preamble.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Part']">
		<xslt step="4" name="Part" href="metalex2akn/minixslt/bill/Part.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Chapter']">
		<xslt step="5" name="Chapter" href="metalex2akn/minixslt/bill/Chapter.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='SubChapter']">
		<xslt step="6" name="SubChapter" href="metalex2akn/minixslt/bill/SubChapter.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Section']">
		<xslt step="7" name="Section" href="metalex2akn/minixslt/bill/Section.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Paragraph']">
		<xslt step="8" name="Paragraph" href="metalex2akn/minixslt/bill/Paragraph.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Title']">
		<xslt step="9" name="Title" href="metalex2akn/minixslt/bill/Title.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Book']">
		<xslt step="10" name="Book" href="metalex2akn/minixslt/bill/Book.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Tome']">
		<xslt step="11" name="Tome" href="metalex2akn/minixslt/bill/Tome.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Article']">
		<xslt step="12" name="Article" href="metalex2akn/minixslt/bill/Article.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Clause']">
		<xslt step="13" name="Clause" href="metalex2akn/minixslt/bill/Clause.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='SubSection']">
		<xslt step="14" name="SubSection" href="metalex2akn/minixslt/bill/SubSection.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='SubPart']">
		<xslt step="15" name="SubPart" href="metalex2akn/minixslt/bill/SubPart.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='SubParagraph']">
		<xslt step="16" name="SubParagraph" href="metalex2akn/minixslt/bill/SubParagraph.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='SubTitle']">
		<xslt step="17" name="SubTitle" href="metalex2akn/minixslt/bill/SubTitle.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='SubClause']">
		<xslt step="18" name="SubClause" href="metalex2akn/minixslt/bill/SubClause.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='Conclusion']">
		<xslt step="19" name="Conclusion" href="metalex2akn/minixslt/bill/Conclusion.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='span']">
		<xslt step="20" name="span" href="metalex2akn/minixslt/bill/span.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='p']">
		<xslt step="21" name="p" href="metalex2akn/minixslt/bill/p.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='ref']">
		<xslt step="22" name="ref" href="metalex2akn/minixslt/bill/ref.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='heading']">
		<xslt step="23" name="heading" href="metalex2akn/minixslt/bill/heading.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='subheading']">
		<xslt step="24" name="subheading" href="metalex2akn/minixslt/bill/subheading.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='list']">
		<xslt step="25" name="list" href="metalex2akn/minixslt/bill/list.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='item']">
		<xslt step="26" name="item" href="metalex2akn/minixslt/bill/item.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='a']">
		<xslt step="27" name="a" href="metalex2akn/minixslt/bill/a.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='content']">
		<xslt step="28" name="content" href="metalex2akn/minixslt/bill/content.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='meta']">
		<xslt step="29" name="meta" href="metalex2akn/minixslt/bill/meta.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='identification']">
		<xslt step="30" name="identification" href="metalex2akn/minixslt/bill/identification.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRWork']">
		<xslt step="31" name="FRBRWork" href="metalex2akn/minixslt/bill/FRBRWork.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRExpression']">
		<xslt step="32" name="FRBRExpression" href="metalex2akn/minixslt/bill/FRBRExpression.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='FRBRManifestation']">
		<xslt step="33" name="FRBRManifestation" href="metalex2akn/minixslt/bill/FRBRManifestation.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='this']">
		<xslt step="34" name="this" href="metalex2akn/minixslt/bill/this.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='uri']">
		<xslt step="35" name="uri" href="metalex2akn/minixslt/bill/uri.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='date']">
		<xslt step="36" name="date" href="metalex2akn/minixslt/bill/date.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='author']">
		<xslt step="37" name="author" href="metalex2akn/minixslt/bill/author.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='publication_mcontainer']">
		<xslt step="38" name="publication_mcontainer" href="metalex2akn/minixslt/bill/publication_mcontainer.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='publication']">
		<xslt step="39" name="publication" href="metalex2akn/minixslt/bill/publication.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='references']">
		<xslt step="40" name="references" href="metalex2akn/minixslt/bill/references.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='TLCOrganization']">
		<xslt step="41" name="TLCOrganization" href="metalex2akn/minixslt/bill/TLCOrganization.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*[@name='TLCPerson']">
		<xslt step="42" name="TLCPerson" href="metalex2akn/minixslt/bill/TLCPerson.xsl" />
		<xsl:apply-templates />
	</xsl:template>


</xsl:stylesheet>