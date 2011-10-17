<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : contenttype.xsl
    Created on : 13 October 2011, 13:12
    Author     : anthony
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:bu="http://portal.bungeni.org/1.0/" xmlns:bp="http://www.bungeni.org/pipeline/1.0"
    xmlns="http://www.akomantoso.org/2.0" version="2.0">

    <xsl:output indent="yes" method="xml" encoding="UTF-8"/>

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*">
        <xsl:element name="{node-name(.)}">
            <xsl:for-each select="@*">
                <xsl:attribute name="{name(.)}">
                    <xsl:value-of select="."/>
                </xsl:attribute>
            </xsl:for-each>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="bu:contenttype" bp:name="root">
        <xsl:variable name="contenttypename"><xsl:value-of select="./bu:field[@name='type']" /></xsl:variable>
        <xsl:variable name="contenturidate"><xsl:value-of select="./bu:field[@name='start_date']" /></xsl:variable>
        <akomaNtoso>
            <doc>
                <xsl:attribute name="name"><xsl:value-of select="$contenttypename" /></xsl:attribute>
                <meta>
                    <identification source="#bungeni">
                        <xsl:call-template name="frbrwork">
                            <xsl:with-param name="contenttypename" select="$contenttypename" />
                            <xsl:with-param name="contenturidate" select="$contenturidate" />                            
                        </xsl:call-template>
                        
                        <xsl:call-template name="frbrexpression">
                            <xsl:with-param name="contenttypename" select="$contenttypename" />
                            <xsl:with-param name="contenturidate" select="$contenturidate" />                            
                        </xsl:call-template>    
                        
                        <xsl:call-template name="frbrmanifestation">
                            <xsl:with-param name="contenttypename" select="$contenttypename" />
                            <xsl:with-param name="contenturidate" select="$contenturidate" />
                        </xsl:call-template>
                        <!--
                        <FRBRExpession>indentification.xsl</FRBRExpession>
                        <FRBRManifestation>indentification.xsl</FRBRManifestation>
                        <FRBRItem>indentification.xsl</FRBRItem>
                        -->
                    </identification>
                </meta>
            </doc>
        </akomaNtoso>
    </xsl:template>

    <xsl:template name="frbrwork" bp:name="root">
        <xsl:param name="contenttypename" />
        <xsl:param name="contenturidate" />
        
        <FRBRWork>
          <xsl:call-template name="frbrauthor" />
            <xsl:call-template name="frbrdate" >
                <xsl:with-param name="contenturidate" select="$contenturidate" />
            </xsl:call-template>
            <xsl:call-template name="frbruri" >
                <xsl:with-param name="contenttypename"  select="$contenttypename"/>
                <xsl:with-param name="contenturidate"  select="$contenturidate"/>
            </xsl:call-template>
            <xsl:call-template name="frbrthis" >
                <xsl:with-param name="contenttypename"  select="$contenttypename"/>
            </xsl:call-template>
        </FRBRWork>      
    </xsl:template>
    
    <xsl:template name="frbrexpression" bp:name="root">
        <xsl:param name="contenttypename" />
        <xsl:param name="contenturidate" />
        
        <FRBRExpression>
            <xsl:call-template name="frbrauthor" />
            <xsl:call-template name="frbrdate" >
                <xsl:with-param name="contenturidate" select="$contenturidate" />
            </xsl:call-template>
            <xsl:call-template name="frbruri" >
                <xsl:with-param name="contenttypename"  select="$contenttypename"/>
                <xsl:with-param name="contenturidate"  select="$contenturidate"/>
            </xsl:call-template>
            <xsl:call-template name="frbrthis" >
                <xsl:with-param name="contenttypename"  select="$contenttypename"/>
            </xsl:call-template>
        </FRBRExpression>      
    </xsl:template>    
    
    <xsl:template name="frbrmanifestation" bp:name="root">
        <xsl:param name="contenttypename" />
        <xsl:param name="contenturidate" />
        
        <FRBRManifestation>
            <xsl:call-template name="frbrauthor" />
            <xsl:call-template name="frbrdate" >
                <xsl:with-param name="contenturidate" select="$contenturidate" />
            </xsl:call-template>
            <xsl:call-template name="frbruri" >
                <xsl:with-param name="contenttypename"  select="$contenttypename"/>
                <xsl:with-param name="contenturidate"  select="$contenturidate"/>
            </xsl:call-template>
            <xsl:call-template name="frbrthis" >
                <xsl:with-param name="contenttypename"  select="$contenttypename"/>
            </xsl:call-template>
        </FRBRManifestation>      
    </xsl:template>       

    <xsl:template name="frbrthis" bp:name="root">
        <xsl:param name="contenttypename" />
        <xsl:param name="contenturidate" />
        
        <FRBRthis>
            <xsl:attribute name="value" select="concat('/ken/',$contenttypename,'/',$contenturidate)" />
        </FRBRthis> 
     </xsl:template>
    
    <xsl:template name="frbruri" bp:name="root">
        <xsl:param name="contenttypename" />
        <xsl:param name="contenturidate" />
        
        <FRBRuri>
            <xsl:attribute name="value" select="concat('/ken/',$contenttypename,'/',$contenturidate,'/main')" />
        </FRBRuri> 
    </xsl:template>
    
    <xsl:template name="frbrdate" bp:name="root">
        <xsl:param name="contenturidate" />
        
        <FRBRdate>
            <xsl:attribute name="date" select="$contenturidate" />
            <xsl:attribute name="name"><xsl:text>#startdate</xsl:text></xsl:attribute>
        </FRBRdate> 
    </xsl:template>

    <xsl:template name="frbrauthor" bp:name="root">
        <FRBRauthor href="#Author">
        </FRBRauthor> 
    </xsl:template>

    <xsl:template match="text()">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template>

</xsl:stylesheet>
