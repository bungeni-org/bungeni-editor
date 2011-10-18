<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : meta.xsl
    Created on : 17 October 2011, 13:17
    Author     : anthony
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:bp="http://www.bungeni.org/pipeline/1.0"
    xmlns:bu="http://portal.bungeni.org/1.0/"
    exclude-result-prefixes="bp"
    version="2.0"> 
    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>
    
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
            <xsl:apply-templates />
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="bu:meta" bp:name="meta">
        <xsl:variable name="parenttype"><xsl:value-of select="local-name(parent::node())"></xsl:value-of></xsl:variable>
        <xsl:variable name="contenttypename"><xsl:value-of select="./bu:field[@name='type']" /></xsl:variable>
        <xsl:variable name="contenturidate"><xsl:value-of select="./bu:field[@name='start_date']" /></xsl:variable>
        <xsl:variable name="contenturilang"><xsl:value-of select="./bu:field[@name='language']" /></xsl:variable>
        <xsl:variable name="contenttypename"><xsl:value-of select="./bu:field[@name='type']" /></xsl:variable>
        <xsl:variable name="grp_pri_id"><xsl:value-of select="./bu:field[@name='group_principal_id']" /></xsl:variable>
        <xsl:variable name="parl_id"><xsl:value-of select="./bu:field[@name='parliament_id']" /></xsl:variable>
        <xsl:variable name="election_date"><xsl:value-of select="//bu:field[@name='election_date']" /></xsl:variable>
        <xsl:variable name="dissolution_date"><xsl:value-of select="./bu:field[@name='dissolution_date']" /></xsl:variable>
        <xsl:variable name="results_date"><xsl:value-of select="./bu:field[@name='status_date']" /></xsl:variable>
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
                    <xsl:with-param name="contenturilang" select="$contenturilang" />
                </xsl:call-template>    
                
                <xsl:call-template name="frbrmanifestation">
                    <xsl:with-param name="contenttypename" select="$contenttypename" />
                    <xsl:with-param name="contenturidate" select="$contenturidate" />
                </xsl:call-template>
            </identification>
           
            <xsl:call-template name="workflow">
                <xsl:with-param name="election_date" select="$election_date" />
                <xsl:with-param name="dissolution_date" select="$dissolution_date" />
                <xsl:with-param name="results_date" select="$results_date" />
            </xsl:call-template>

            <xsl:call-template name="references" >
                <xsl:with-param name="parenttype" select="$parenttype" />
                <xsl:with-param name="parl_id" select="$parl_id" />
                <xsl:with-param name="grp_pri_id" select="$grp_pri_id" />
                <xsl:with-param name="parl_type" select="$contenttypename" />
            </xsl:call-template>
            
            <xsl:if test="$contenttypename = 'parliament'">
                <xsl:call-template name="proprietary" />
            </xsl:if>
        </meta>
    </xsl:template>

    <xsl:include href="../common/frbrwork.xsl" />
    
    <xsl:include href="../common/frbrexpression.xsl"/>    
    
    <xsl:include href="../common/frbrmanifestation.xsl"/>

    <xsl:template name="workflow" bp:name="meta">
        <xsl:param name="election_date"></xsl:param>
        <xsl:param name="dissolution_date"></xsl:param>
        <xsl:param name="results_date"></xsl:param>
        <workflow source="#parliament">
            <step id="ed" href="#election-date">
                <xsl:attribute name="date"><xsl:value-of select="$election_date" /></xsl:attribute>
            </step>
            
            <xsl:choose>
                <xsl:when test="$results_date">
                    <step id="rd" href="#status-date">
                        <xsl:attribute name="date"><xsl:value-of select="$results_date" /></xsl:attribute>
                    </step>                    
                </xsl:when>
                <xsl:otherwise>
                    <step id="dd" href="#dissolution_date">
                        <xsl:attribute name="date"><xsl:value-of select="$dissolution_date" /></xsl:attribute>
                    </step>
                </xsl:otherwise>
            </xsl:choose>
        </workflow>
    </xsl:template>
    
    <xsl:template name="references" bp:name="meta">
        <xsl:param name="parl_type" />
        <xsl:param name="parl_id" />
        <xsl:param name="grp_pri_id" />
        <xsl:param name="parenttype" />
        <references source="#parliament">
            <TLCConcept id="object-type">
                <xsl:attribute name="href" select="concat('/ontology/object/',$parenttype)" />
                <xsl:attribute name="showAs"><xsl:value-of select="//bu:field[@name='full_name']" /></xsl:attribute>
            </TLCConcept>
            <TLCConcept id="type">
                <xsl:attribute name="href"><xsl:value-of select="concat('/ontology/',$parl_type)" /></xsl:attribute>
                <xsl:attribute name="showAs"><xsl:value-of select="$parl_type" /></xsl:attribute>
            </TLCConcept>
            <TLCConcept id="status" href="status.active" showAs="active"/>
            <TLCConcept id="short-name" href="/ontology/concept/shortname">
                <xsl:attribute name="showAs"><xsl:value-of select="//bu:field[@name='short_name']" /></xsl:attribute>
            </TLCConcept>
            <TLCConcept id="full-name" href="/ontology/concept/fullname">
                <xsl:attribute name="showAs"><xsl:value-of select="//bu:field[@name='full_name']" /></xsl:attribute>
            </TLCConcept>
            <TLCObject id="group-principal-id" >
                <xsl:attribute name="href" select="concat('/ontology/parliament/ke/',$parl_id)" />
                <xsl:attribute name="showAs" select="$grp_pri_id" />
            </TLCObject>
            <TLCOrganization id="parliament" href="/ontology/parliament/ke" showAs="Parliament of Kenya"/>
            
            <TLCConcept id="start-date" href="/ontology/event/StartDate">
                <xsl:attribute name="showAs"><xsl:text>StartDate</xsl:text></xsl:attribute>
            </TLCConcept>
            <TLCConcept id="end-date" href="/ontology/event/EndDate">
                <xsl:attribute name="showAs"><xsl:text>EndDate</xsl:text></xsl:attribute>
            </TLCConcept>
            <TLCConcept id="status-date" href="/ontology/event/StatusDate">
                <xsl:attribute name="showAs"><xsl:text>StatusDate</xsl:text></xsl:attribute>
            </TLCConcept>     
            
            <!-- Calls parliament specific TLCs -->
            <xsl:if test="$parl_type = 'parliament'">
                <xsl:call-template name="parl-specific" />
            </xsl:if>
            
        </references>
    </xsl:template> 
    
    <xsl:template name="parl-specific" bp:name="meta">
        <TLCConcept id="election-date" href="#start-date" showAs="Election Date"/>
        <TLCConcept id="dissolution-date" href="#end-date" showAs="Dissolution Date"/>        
    </xsl:template>    
    
    <xsl:template name="proprietary" bp:name="meta">
        <bp:proprietary source="#parliament">
            <!-- this is bungeni implementation specific xml, used only for rendering logic not output -->
            <xsl:copy-of select="./bu:permissions" />
            <xsl:copy-of select="./bu:contained_groups" />
            <!-- for ui.xml rules -->
            <bp:rules>
                <bp:field name="start_date" refersTo="#start-date"></bp:field>
                <bp:field name="status_date" refersTo="#status-date"></bp:field>
                <bp:field name="short_name" refersTo="#short-name"></bp:field>
                <bp:field name="full_name" refersTo="#full-name"></bp:field>
            </bp:rules>
        </bp:proprietary>        
    </xsl:template>       
    
    <xsl:template name="frbrthis" bp:name="meta">
        <xsl:param name="contenttypename" />
        <xsl:param name="contenturidate" />
        
        <FRBRthis>
            <xsl:attribute name="value" select="concat('/ke/',$contenttypename,'/',$contenturidate)" />
        </FRBRthis> 
    </xsl:template>
    
    <xsl:template name="frbruri" bp:name="meta">
        <xsl:param name="contenttypename" />
        <xsl:param name="contenturidate" />
        
        <FRBRuri>
            <xsl:attribute name="value" select="concat('/ke/',$contenttypename,'/',$contenturidate,'/main')" />
        </FRBRuri> 
    </xsl:template>
    
    <xsl:template name="frbrdate" bp:name="meta">
        <xsl:param name="contenturidate" />
        
        <FRBRdate>
            <xsl:attribute name="date" select="$contenturidate" />
            <xsl:attribute name="name"><xsl:text>#startdate</xsl:text></xsl:attribute>
        </FRBRdate> 
    </xsl:template>
    
    <xsl:template name="frbrlang" bp:name="meta">
        <xsl:param name="contenturilang" />
        
        <FRBRlanguage>
            <xsl:attribute name="language" select="$contenturilang" />
        </FRBRlanguage> 
    </xsl:template>    
    
    <xsl:template name="frbrauthor" bp:name="meta">
        <FRBRauthor href="#Author">
        </FRBRauthor> 
    </xsl:template>
    
    <xsl:template match="text()">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template>    
    
</xsl:stylesheet>
