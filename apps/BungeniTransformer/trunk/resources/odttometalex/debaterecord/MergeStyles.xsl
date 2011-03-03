<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0"
    xmlns:style="urn:oasis:names:tc:opendocument:xmlns:style:1.0"
    xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
    xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0"
    xmlns:draw="urn:oasis:names:tc:opendocument:xmlns:drawing:1.0"
    xmlns:fo="urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0"
    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:meta="urn:oasis:names:tc:opendocument:xmlns:meta:1.0"
    xmlns:number="urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0"
    xmlns:svg="urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0"
    xmlns:chart="urn:oasis:names:tc:opendocument:xmlns:chart:1.0"
    xmlns:dr3d="urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0"
    xmlns:math="http://www.w3.org/1998/Math/MathML"
    xmlns:form="urn:oasis:names:tc:opendocument:xmlns:form:1.0"
    xmlns:script="urn:oasis:names:tc:opendocument:xmlns:script:1.0"
    xmlns:ooo="http://openoffice.org/2004/office" xmlns:ooow="http://openoffice.org/2004/writer"
    xmlns:oooc="http://openoffice.org/2004/calc" xmlns:dom="http://www.w3.org/2001/xml-events"
    xmlns:xforms="http://www.w3.org/2002/xforms" xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:field="urn:openoffice:names:experimental:ooo-ms-interop:xmlns:field:1.0"
    xmlns:rpt="http://openoffice.org/2005/report" xmlns:anx="http://anx.akomantoso.org/1.0"
    xmlns:of="urn:oasis:names:tc:opendocument:xmlns:of:1.2"
    xmlns:rdfa="http://docs.oasis-open.org/opendocument/meta/rdfa#" version="2.0"
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
    <xsl:output indent="yes" method="xml"/>

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>
    <!-- ***DOC***
	in the original method metadata for a section was stored in style properties. 
	The section was associated with a style name. This style sheet copied over the metadata
	attributes over from the style property into the text:section

	We now change that to copy over the rdf metadata properties into the section, perhaps into 
	a sub-container metadata structure instead of an attribute to account for future requirement
	for hierarchical metadata
	
	For this to work the rdf namespace must be added to the odf document namespace
	
	-->

    <xsl:template match="*">
        <xsl:element name="{node-name(.)}">
            <!-- ***DOC*** 
                only for elements which hav an id attribute 
            -->
            <xsl:if test="@id"> <xsl:variable name="itemid" select="@id"/>
                <!-- ***DOC*** 
                    copy all the current attributes 
                -->
                <xsl:for-each select="@*">
                    <xsl:attribute name="{name(.)}">
                        <xsl:value-of select="."/>
                    </xsl:attribute>
                </xsl:for-each>
                <!--  ***DOC***
                    copy over the rdf metadata 
                -->
                <!-- ***DOC*** 
                    1) we generate the rdf id to search for 
                -->
                <xsl:variable name="rdfid">
                    <xsl:text>../content.xml#</xsl:text>
                    <xsl:value-of select="$itemid"/>
                </xsl:variable>
                <!-- ***DOC***
                    2) we generate the new rdf metadata container 
                -->
                <!-- ***DOC***
                    only if there is the correspoinding rdf metadata graph 
                -->
                <xsl:if test="//rdf:RDF/rdf:Description[@rdf:about=$rdfid]">
                    <xsl:element name="bungenimeta">
                        <xsl:attribute name="for">
                            <xsl:text>#</xsl:text>
                            <xsl:value-of select="$itemid"/>
                        </xsl:attribute>
                        <!-- ***DOC*** 
                            there should be only one rdf:Description element matching this id, we look 
                            for all its child elements in the editor.bungeni.org/1.0/anx namespace
                        -->
                        <xsl:for-each
                            select="//rdf:RDF/rdf:Description[@rdf:about=$rdfid]//child::*[namespace-uri()='http://editor.bungeni.org/1.0/anx/']">
                            <!-- ***DOC***
                                we select all the child metadata metadata elements
                            -->
                            <xsl:element name="{local-name()}">
                                <xsl:value-of select="."/>
                            </xsl:element>
                        </xsl:for-each>
                    </xsl:element>
                </xsl:if>
            </xsl:if> 
            
            <!-- ***DOC*** 
                
            The above translates an ODF section with rdf metadata into : 
            
            <text:section
                name="preface" style-name="Sect2" id="id1821486612">
                <bungenimeta for="#id1821486612">
                    <BungeniSectionID>C3PLT/lrRSmqOHSpUzmgEQ</BungeniSectionID>
                    <BungeniSectionType>Preface</BungeniSectionType>
                    <hiddenBungeniMetaEditable>false</hiddenBungeniMetaEditable>
                </bungenimeta>
                <text:p>NATIONAL ASSEMBLY</text:p>
                <text:p/>
                <text:p>OFFICIAL REPORT</text:p>
                <text:p/>
                <text:p>Wednesday, 20th March, 1991</text:p>
                <text:p>The House met at thirty minutes past Two o’ clock.</text:p>
                <text:p/>
                <text:section name="comment2" style-name="Sect4" id="id2142572486">
                    <bungenimeta for="#id2142572486">
                        <BungeniSectionID>waTKMDxqTKWqfXmy1yFBsA</BungeniSectionID>
                        <BungeniSectionType>Observation</BungeniSectionType>
                        <hiddenBungeniMetaEditable>false</hiddenBungeniMetaEditable>
                    </bungenimeta>
                    <text:p>[Mr Speaker in the Chair]</text:p>
                </text:section>
                <text:p/>
            </text:section>
            
            -->
            
            
            <xsl:apply-templates/>
        </xsl:element>

    </xsl:template>

    <xsl:template match="office:scripts"> </xsl:template>

    <xsl:template match="office:styles"> </xsl:template>

    <xsl:template match="office:automatic-styles"> </xsl:template>

    <xsl:template match="office:font-face-decls"> </xsl:template>

    <xsl:template match="office:forms"> </xsl:template>

    <xsl:template match="rdf:RDF"/>

    <xsl:template match="office:body">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="office:text">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="office:document-meta">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="text:sequence-decls"> </xsl:template>


    <xsl:template match="text()">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template>

</xsl:stylesheet>
