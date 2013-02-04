<?xml version="1.0" encoding="UTF-8"?>
<!--
    This stylesheet expects the merged configuration document as an input 
    -->
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
            <xd:p>
                This template automates the generation of TLC meta-headers.
                This proto-template generates a XSLT template which is used
                by the Bungeni Transformer pipeline.
            </xd:p>
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
            <!-- 
                generate the keys for rendering the TLC meta 
                TLC meta is supported for :
                    sectionType
                    inlineType
                    annotationType
            -->
            <xmeta:key name="bySectionType" match="bungeni:bungenimeta" use="bungeni:BungeniSectionType" />
            <xmeta:key name="byInlineType" match="bungeni:bungenimeta" use="bungeni:BungeniInlineType" />
            <xmeta:key name="byAnnotationType" match="bungeni:bungenimeta" use="bungeni:BungeniAnnotationType" />
            <!--
                
                This is what a type element looks like in structure  : 
                We look for output/meta elements for all types , inline, section and annotation types

            <sectionType name="Speech" prefix="speech" numstyle="serial" background="url:/settings/configs/backgrounds/shade-1.png" indent-left=".6" indent-right="0" visibility="user">
                <metadatas>
                    <metadata name="BungeniSpeechBy" />
                    <metadata name="BungeniSpeechByURI" />
                    <metadata name="BungeniSpeechAs" />
                    <metadata name="BungeniSpeechAsDesc" />
                    <metadata name="BungeniSpeechAsURI" />
                    <metadata name="BungeniPersonId" />
                    <metadata name="BungeniPersonRole" />
                </metadatas>
                <output>
                    <meta>
                        <TLCPerson id="$BungeniPersonID" href="$BungeniSpeechByURI" showAs="$BungeniSpeechBy" />
                        <TLCRole id="$BungeniSpeechAs" href="$BungeniSpeechAsURI" showAs="$BungeniSpeechAsDesc" />
                    </meta>
                    <content>
                        <speech by="#$BungeniPersonID" as="#$BungeniSpeechAs" /> 
                    </content>
                </output>
            </sectionType>
            -->
            <xmeta:template match="office:meta">
                
                <mcontainer name="references">
                <!-- the meta sectionType config is a special case since its a 'virtual' section configuration 
                    and doesnt have a corresponding visual equivalent, the metadata output here is rendered from 
                    document level metadata properties unlike all the other metadata which is from the section 
                    level storage 
                    !+GLOBAL_META_SUPPORT(ah, 28-01-2013)
                -->
                <xsl:for-each select=".//output/meta/references[ancestor::sectionType[@name eq 'meta']]">
                    <!-- generate global metadata matcher -->
                    <xsl:for-each select="child::*">
                        <!-- example, <TLCPerson> -->
                        <xmeta:element name="{local-name()}" >
                            <!-- iterate through the attributes -->
                            <!-- attributes of the TLCPerson -->
                            <!-- Adapt config type attribute processor to work with this -->
                            <!-- add a check if metadata is referenced locally , if not , refer to global metadata -->
                            <xsl:call-template name="config-type-attribute-processor" />
                        </xmeta:element>
                    </xsl:for-each>
                </xsl:for-each>
                    
                <!-- look for everything except meta -->
                <xsl:for-each-group 
                    select=".//output/meta/references[ancestor::sectionType[@name ne 'meta']]" 
                    group-by="
                        ancestor::sectionType/@name | 
                        ancestor::inlineType/@name | 
                        ancestor::annotationType/@name
                        "
                    >
                    <!-- 
                        get the current grouping key, i.e. the name 
                        !+WARNING(not sure how this will behave if
                        we have section types and inline types sharing a
                        type name )
                        -->
                    <xsl:variable name="grp-key" select="current-grouping-key()" />
                    
                    <xsl:text>&#xa;</xsl:text>
                    <xsl:comment><xsl:value-of select="$grp-key" /></xsl:comment>
                    <xsl:text>&#xa;</xsl:text>
                   
                    <!--
                        We have 3 sets of generated keys,
                        we want to determine which is the key to query in the for loop.
                        We determine that by querying the ancestor type and setting the 
                        appropriate key to query 
                    -->
                    
                    <xsl:variable name="key-by-what">
                        <xsl:choose>
                            <xsl:when test="ancestor::sectionType">
                                <xsl:text>bySectionType</xsl:text>
                            </xsl:when>
                            <xsl:when test="ancestor::inlineType">
                                <xsl:text>byInlineType</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>byAnnotationType</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    
                    <!-- 
                        Run through each  type in configuration which has metadata headers 
                        Render the xsl:for-each as a xmeta:for-each which will be converted
                        to an XLST template
                    -->
                    <xmeta:for-each select="key('{$key-by-what}', '{$grp-key}')">
                        <!-- we group them by type -->
                        <xsl:for-each select="current-group()">
                            <!-- iterate through child elements -->
                            <xsl:for-each select="child::*">
                                <!-- example, <TLCPerson> -->
                                <xmeta:element name="{local-name()}" >
                                    <!-- iterate through the attributes -->
                                    <!-- attributes of the TLCPerson -->
                                    <!-- generates attribute values in place -->
                                    <xsl:call-template name="config-type-attribute-processor" />
                                </xmeta:element>
                            </xsl:for-each>
                        </xsl:for-each>
                    </xmeta:for-each>
                </xsl:for-each-group>
                </mcontainer>

                <mcontainer name="proprietary">
                    
                    <!--
                        Special treatment for proprietary section type
                        !+PROP_NS(ah, 04-02-2013)
                        -->
                        
                    <xsl:for-each select=".//output/meta/proprietary[ancestor::sectionType/@name eq 'meta']" >
                        <xsl:text>&#xa;</xsl:text>
                        <xsl:comment>meta section</xsl:comment>
                        <xsl:text>&#xa;</xsl:text>
                        <xsl:variable name="ns-uri" select="namespace-uri(./*[1])"></xsl:variable>
                        <!-- get the namespace prefix of the first child element -->
                        <xsl:variable name="ns-attr" select="name(namespace::*[string() eq $ns-uri])" />
                        <!-- find the matching namespace element in the proprietary block -->
                        <!-- the below is strictly not neccessary, we just resolve the ns prefix again from the 
                            namespace node -->
                        <xsl:variable name="local-ns" select="namespace::*[name() eq $ns-attr]" />
                        <xsl:variable name="local-ns-prefix" select="local-name($local-ns)" />
                        <!-- NOTE: we dont use the locally defined namespace prefix resolver ... only the prefix is important
                            and selected from allConfigs/outputs/namespace -->
                        <xsl:variable name="local-ns-url" select="/allConfigs//outputs/namespace[@prefix = $local-ns-prefix]/@uri" />
                        <xmeta:element name="proprietary">
                            <xmeta:namespace name="{$local-ns-prefix}"  select="'{$local-ns-url}'" />
                            <xsl:call-template name="proprietary-descendants-processor">
                                <xsl:with-param name="local-ns-prefix" select="$local-ns-prefix" />
                                <xsl:with-param name="local-ns-url" select="$local-ns-url" />
                            </xsl:call-template>
                        </xmeta:element>
                    </xsl:for-each>
                    
                    <!-- 
                        We dont want to output this for the meta section type since its a virtual section type
                        !+PROP_NS(ah, 04-02-2013)
                    -->
                    <xsl:for-each-group 
                        select=".//output/meta/proprietary" 
                        group-by="
                        ancestor::sectionType/@name[. ne 'meta'] | 
                        ancestor::inlineType/@name | 
                        ancestor::annotationType/@name
                        "
                        >
                        <!-- 
                            get the current grouping key, i.e. the name 
                            !+WARNING(not sure how this will behave if
                            we have section types and inline types sharing a
                            type name )
                        -->
                        <xsl:variable name="grp-key" select="current-grouping-key()" />
                        
                        <xsl:text>&#xa;</xsl:text>
                        <xsl:comment><xsl:value-of select="$grp-key" /></xsl:comment>
                        <xsl:text>&#xa;</xsl:text>
                        
                        <!--
                            We have 3 sets of generated keys,
                            we want to determine which is the key to query in the for loop.
                            We determine that by querying the ancestor type and setting the 
                            appropriate key to query 
                        -->
                        
                        <xsl:variable name="key-by-what">
                            <xsl:choose>
                                <xsl:when test="ancestor::sectionType">
                                    <xsl:text>bySectionType</xsl:text>
                                </xsl:when>
                                <xsl:when test="ancestor::inlineType">
                                    <xsl:text>byInlineType</xsl:text>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:text>byAnnotationType</xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:variable>
                        
                        <!-- 
                            Run through each  type in configuration which has metadata headers 
                            Render the xsl:for-each as a xmeta:for-each which will be converted
                            to an XLST template
                        -->
                        <xmeta:for-each select="key('{$key-by-what}', '{$grp-key}')">
                            <!-- we group them by type -->
                            <xsl:for-each select="current-group()">
                                <!-- iterate through child elements -->
                                <!-- <xsl:variable name="ns-attr" select="data(@ns)" /> -->
                                <!-- get the namespace uri of the first child element in the proprietary block -->
                                <xsl:variable name="ns-uri" select="namespace-uri(./*[1])"></xsl:variable>
                                <!-- get the namespace prefix of the first child element -->
                                <xsl:variable name="ns-attr" select="name(namespace::*[string() eq $ns-uri])" />
                                <!-- find the matching namespace element in the proprietary block -->
                                <!-- the below is strictly not neccessary, we just resolve the ns prefix again from the 
                                namespace node -->
                                <xsl:variable name="local-ns" select="namespace::*[name() eq $ns-attr]" />
                                <xsl:variable name="local-ns-prefix" select="local-name($local-ns)" />
                                <!-- NOTE: we dont use the locally defined namespace prefix resolver ... only the prefix is important
                                    and selected from allConfigs/outputs/namespace -->
                                <xsl:variable name="local-ns-url" select="/allConfigs//outputs/namespace[@prefix = $local-ns-prefix]/@uri" />
                                <xmeta:element name="proprietary">
                                    <xmeta:namespace name="{$local-ns-prefix}"  select="'{$local-ns-url}'" />
                                    <xsl:call-template name="proprietary-descendants-processor">
                                        <xsl:with-param name="local-ns-prefix" select="$local-ns-prefix" />
                                        <xsl:with-param name="local-ns-url" select="$local-ns-url" />
                                    </xsl:call-template>
                                </xmeta:element>
                            </xsl:for-each>
                        </xmeta:for-each>
                    </xsl:for-each-group>
                </mcontainer>
                
                <!--
                <mcontainer name="proprietary">
                    <xsl:for-each select=".//output/meta/proprietary">
                        <xsl:variable name="ns-attr" select="data(@ns)" />
                        <xsl:variable name="local-ns" select="namespace::*[name() eq $ns-attr]" />
                        <xsl:variable name="local-ns-prefix" select="local-name($local-ns)" />
                        <xsl:variable name="local-ns-url" select="/allConfigs//outputs/namespace[@prefix = $local-ns-prefix]/@uri" />
                        <xmeta:element name="proprietary">
                            <xmeta:namespace name="{$local-ns-prefix}"  select="'{$local-ns-url}'" />
                            <xsl:for-each select="child::*">
                                <xmeta:element name="{$local-ns-prefix}:{local-name()}" 
                                    namespace="{$local-ns-url}">
                                    <xsl:call-template name="config-type-attribute-processor" />
                                </xmeta:element>
                            </xsl:for-each>    
                        </xmeta:element>
                    </xsl:for-each>
                </mcontainer>
                -->
                
            </xmeta:template>
            
        </xmeta:stylesheet>
    </xsl:template>


    <!--
        Recursive proprietary element processor 
        -->
    <xsl:template name="proprietary-descendants-processor">
        <xsl:param name="local-ns-prefix"></xsl:param>
        <xsl:param name="local-ns-url"></xsl:param>
        <xsl:for-each select="child::*">
            <xmeta:element name="{$local-ns-prefix}:{local-name()}" 
                namespace="{$local-ns-url}">
                <xsl:call-template name="config-type-attribute-processor" />
                <xsl:call-template name="proprietary-descendants-processor">
                    <xsl:with-param name="local-ns-prefix" select="$local-ns-prefix" />
                    <xsl:with-param name="local-ns-url" select="$local-ns-url" />
                </xsl:call-template>
            </xmeta:element>
        </xsl:for-each>    
    </xsl:template>
     
     

    <!-- This template renders the XSLT template for output content and metadata -->
    <xsl:template name="config-type-attribute-processor">
        <!-- !+FIX_THIS(code_duplication, ah, 2012-06-26) copied from type_generator.xsl -->
        <xsl:variable name="dquote">&#39;</xsl:variable>
        <xsl:choose>
            <!-- special processing for meta section type 
                !+GLOBAL_META_SUPPORT(ah, 28-01-2013)
            -->
            <xsl:when test="ancestor::sectionType[@name eq 'meta']">
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
                                <!-- possibly add a check to see if the metadata exists in the parent ? -->
                                <xmeta:text>#</xmeta:text>
                                <xmeta:value-of select="{concat(
                                    '//meta:user-defined[@name=', 
                                    $dquote, 
                                    substring-after(.,'#$'), 
                                    $dquote,
                                    ']'
                                    )}" />
                            </xsl:when>
                            <xsl:when test="starts-with(.,'$')">
                                <xmeta:value-of select="{concat(
                                    '//meta:user-defined[@name=', 
                                    $dquote, 
                                    substring-after(.,'$'), 
                                    $dquote,
                                    ']'
                                    )}" />
                            </xsl:when>
                            <xsl:otherwise>
                                <!-- do nothing -->
                            </xsl:otherwise>
                        </xsl:choose>
                    </xmeta:attribute>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
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
                                <!-- possibly add a check to see if the metadata exists in the parent ? -->
                                <!-- !+NODEREF_FIX(ah, 10/10/2012) made node-ref from for-each iteration to 
                                    be relative to key matcher bungeni:bungenimeta -->
                                <xmeta:text>#</xmeta:text>
                                <xmeta:value-of select="{concat('./bungeni:', substring-after(.,'#$') )}" />
                            </xsl:when>
                            <xsl:when test="starts-with(.,'$')">
                                <xmeta:value-of select="{concat('./bungeni:', substring-after(.,'$') )}" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xmeta:text><xsl:value-of select="." /></xmeta:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xmeta:attribute>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
        
        
    </xsl:template>


</xsl:stylesheet>
