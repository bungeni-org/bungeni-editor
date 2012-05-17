<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:ml="http://www.metalex.org/1.0" 
                xmlns:bungeni="http://editor.bungeni.org/1.0/anx/"
                xmlns:bp="http://www.bungeni.org/pipeline/1.0"
                xmlns:bodf="http://editor.bungeni.org/1.0/odf/"
                exclude-result-prefixes="bp"
                version="2.0">
    <xsl:output indent="yes" method="xml"/>

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
		<bp:template name="root" href="pipeline_xslt/act/root.xsl" />
		  
		<bp:template name="body" href="pipeline_xslt/act/body.xsl" />
                
                <bp:template name="Preface" href="pipeline_xslt/act/preface.xsl" />
                <bp:template name="docTitle" href="pipeline_xslt/act/docTitle.xsl" />
                <bp:template name="docType" href="pipeline_xslt/act/docType.xsl" />
                <bp:template name="docNumber" href="pipeline_xslt/act/docNumber.xsl" />
                <bp:template name="docDate" href="pipeline_xslt/act/docDate.xsl" />
                
                <bp:template name="Preamble" href="pipeline_xslt/act/preamble.xsl" />
		
                <bp:template name="Book" href="pipeline_xslt/act/book.xsl" />
		  
		<bp:template name="Part" href="pipeline_xslt/act/part.xsl" />
		
		<bp:template name="Chapter" href="pipeline_xslt/act/chapter.xsl" />
	 
		<bp:template name="Section" href="pipeline_xslt/act/section.xsl" />
		  
		<bp:template name="Article" href="pipeline_xslt/act/article.xsl" />
		  
		<bp:template name="Conclusions" href="pipeline_xslt/common/conclusions.xsl" />
		  
		<bp:template name="num" href="pipeline_xslt/act/num.xsl" />
		  
		<bp:template name="heading" href="pipeline_xslt/act/heading.xsl" />
		 
		<bp:template name="content" href="pipeline_xslt/act/content.xsl" />
		  
                <bp:template name="Book" href="pipeline_xslt/act/speech.xsl" />
                  
		<bp:template name="meta" href="pipeline_xslt/common/meta.xsl" />
		  
		<bp:template name="identification" href="pipeline_xslt/common/identification.xsl" />
		  
		<bp:template name="FRBRWork" href="pipeline_xslt/common/frbrwork.xsl" />
		  
		<bp:template name="FRBRExpression" href="pipeline_xslt/common/frbrexpression.xsl" />
		  
		<bp:template name="FRBRManifestation" href="pipeline_xslt/common/frbrmanifestation.xsl" />
		
		<bp:template name="author" href="pipeline_xslt/common/frbrauthor.xsl" />
		  
                <bp:template name="this" href="pipeline_xslt/common/this.xsl"/>

                <bp:template name="uri" href="pipeline_xslt/common/uri.xsl"/>

                <bp:template name="date" href="pipeline_xslt/common/date.xsl"/>
                
		<bp:template name="publication_mcontainer" href="pipeline_xslt/common/publication_mcontainer.xsl" />
		  
		<bp:template name="publication" href="pipeline_xslt/common/publication.xsl" />
		  
		<bp:template name="references" href="pipeline_xslt/common/references.xsl" />
		  
		<bp:template name="TLCOrganization" href="pipeline_xslt/common/tlcorganization.xsl" />
		 
		<bp:template name="TLCPerson" href="pipeline_xslt/common/tlcperson.xsl" />
                
                
	
</xsl:stylesheet>