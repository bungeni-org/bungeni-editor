<?xml version="1.0" encoding="UTF-8"?>
<!-- We use the meta: prefix to distinguish between the xsl: namespace used by the xslt
    and the xslt we want to generate -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:xmeta="http://meta.w3.org/1999/XSL/Transform"
    xmlns:bodf="http://editor.bungeni.org/1.0/odf/" 
    exclude-result-prefixes="xs" version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> May 24, 2012</xd:p>
            <xd:p><xd:b>Author:</xd:b> Ashok Hariharan</xd:p>
            <xd:p>This is a meta template generator for Transforming ODF sections 
                into AKoma Ntoso Sections</xd:p>
        </xd:desc>
    </xd:doc>
    
    <xsl:output method="xml" indent="yes" />
    <!-- the namespace-alias call switches the output namespace back to the xsl namespace,
        this way for development we use the meta namespace and the output is always rendered
        as the xsl: namespace -->
    <xsl:namespace-alias stylesheet-prefix="xmeta" result-prefix="xsl"/>

    <xsl:template match="allConfigs">
        <xmeta:stylesheet
           xmlns:xs="http://www.w3.org/2001/XMLSchema"
           xmlns:meta="http://meta.w3.org/1999/XSL/Transform"
           xmlns:bungeni="http://editor.bungeni.org/1.0/anx/"
           xmlns:bodf="http://editor.bungeni.org/1.0/odf/"
           version="2.0"
        >
            <xsl:apply-templates />
        </xmeta:stylesheet>
    </xsl:template>

    <xsl:template match="inlineTypes">
        <!-- INLINE TYPES -->
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="sectionTypes">
        <!-- SECTION TYPES -->
        <xsl:apply-templates />
    </xsl:template>
    
    <xsl:template match="text()">
        <!-- strip extra whitespace from text nodes
            (including leading and trailing whitespace) -->
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template>
    
    
    <xsl:template match="sectionType">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="inlineType">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="metadatas">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="meta">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="output">
        <xsl:variable name="typename" select="ancestor::*/@name"/>
        <xsl:text>&#xa;</xsl:text>
        <xsl:comment>
            <xsl:text>&#xa;</xsl:text>
            <xsl:text>template for </xsl:text><xsl:value-of select="$typename" />
            <xsl:text>&#xa;</xsl:text>
        </xsl:comment>
        <xsl:text>&#xa;</xsl:text>
        
        <xmeta:template>
            <xsl:attribute name="match"> 
                <xsl:text>*[@name='</xsl:text>
                <xsl:value-of select="$typename" />
                <xsl:text>']</xsl:text>
            </xsl:attribute>
            <xsl:apply-templates />
        </xmeta:template>

    </xsl:template>

    <xsl:template match="content[parent::output]">
        <xsl:apply-templates />
    </xsl:template>

    <!-- this is the entry point of the root template generator -->
    <xsl:template match="sectionType[@name='root']">
        <!-- we use a rootCall mode here to apply special processing for 
        root generation -->
        <xsl:apply-templates select="output/content" mode="root-call"/>
    </xsl:template>
    
    <!-- generate the root template matcher -->
    <xsl:template match="content" mode="root-call">
        <xmeta:template match="*[@name='root']">
            <xsl:apply-templates select="child::*"  mode="root-call" />
        </xmeta:template>
    </xsl:template>
    
    <!-- matches all the children of the root template matcher -->
    <xsl:template match="*" mode="root-call">
        <xsl:choose>
            <!-- check if there is a mode=call attrib, if yes,
                then we use xsl:apply-templates -->
            <xsl:when test="@mode eq 'call'">
                <xsl:variable name="elem-name" select="local-name()" />
                <xsl:variable name="src_meta_type" select="//*[child::output/content/*[local-name() eq  $elem-name]]" />
                <xsl:choose>
                    <!-- output an error element in case there is an error condition -->
                    <!-- 1) if there is no type configuration at all for the element to be outputtted in the root -->
                    <xsl:when test="not($src_meta_type)">
                        <xmeta:error code="TG-0001" text="MISSING_META_TYPE_CONFIG" for="{$elem-name}" />
                    </xsl:when>
                    <!-- 2) if there is more than 1 type configuration using the element to be outputted in the root -->
                    <xsl:when test="count($src_meta_type) gt 1">
                        <xmeta:error code="TG-0002" text="DUPLICATE_META_TYPE_CONFIG" for="{$elem-name}" />
                    </xsl:when>
                    <!-- 3) if the error conditions are not hit, then we generate the template matcher using the type config
                        name -->
                    <xsl:otherwise>
                        <xmeta:apply-templates select="//*[@name='{$src_meta_type/@name}']" />
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <!-- 
                    otherwise we just generate a regular element and process its children -->
                <xmeta:element name="{local-name()}">
                    <xsl:apply-templates mode="root-call" />
                </xmeta:element>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


    <!-- This generates the apply-templates for the 
        elements with the mode attribute -->
   <xsl:template name="apply-templates-generator">
       <xsl:param name="element-name" />
       <xsl:variable name="configTypeForElementName"
           select="//*[child::output/content/child::*[
           local-name() eq $element-name
           ]]" />
       <xsl:variable name="configTypeName" select="data($configTypeForElementName/@name)"></xsl:variable>
        <xmeta:apply-templates select="*[@name eq {$configTypeName}]" />
 </xsl:template>

    <xsl:template match="*[parent::content]">
        <xsl:call-template name="process-content-descendant-element" />
    </xsl:template>
    
    <xsl:template name="process-content-descendant-element">
        
        <xsl:variable name="elemname"  select="local-name()"/>
        <xmeta:element name="{$elemname}">
            <xmeta:if test="@id">
                <xmeta:attribute name="bodf:sourceId" select="concat('{lower-case($elemname)}', '-', @id)"/>
            </xmeta:if>
            <xmeta:attribute name="id" select="concat('{lower-case($elemname)}', '-', @id)" />
            <xsl:for-each select="@*">
                <!-- process attributes -->
                <xmeta:attribute name="{local-name()}" >
                    <xsl:choose>
                        <!-- 
                            Attributes are processed as follows :
                            values starting with #$
                            values starting with $
                            values with literals
                        -->
                        <xsl:when test="starts-with(.,'#')">
                            <!-- possibly add a check to see if the metadata exists in the parent -->
                            <xmeta:text>#</xmeta:text>
                            <xmeta:value-of select="{concat('./bungeni:bungenimeta/bungeni:', substring-after(.,'#$') )}" />
                        </xsl:when>
                        <xsl:when test="starts-with(.,'$')">
                            <xmeta:value-of select="{concat('./bungeni:bungenimeta/bungeni:', substring-after(.,'$') )}" />
                        </xsl:when>
                        <xsl:otherwise>
                            <xmeta:text><xsl:value-of select="." /></xmeta:text>
                        </xsl:otherwise>
                    </xsl:choose>
                </xmeta:attribute>
            </xsl:for-each>
            
            <xsl:for-each select="child::*" >
                <xsl:call-template name="process-content-descendant-element" />
            </xsl:for-each>
            
            <xsl:if test="not(child::*)">
                <xmeta:apply-templates />
            </xsl:if>
        
        </xmeta:element>
        
    </xsl:template>

	<xsl:template match="outputs" />
	<xsl:template match="doctype" />

</xsl:stylesheet>
