<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Mar 29, 2012</xd:p>
            <xd:p><xd:b>Author:</xd:b> anthony</xd:p>
            <xd:p></xd:p>
        </xd:desc>
    </xd:doc>

    <xsl:output indent="yes" method="xml" encoding="UTF-8"/>
    
    <!-- These values are set in first input which is grouping_Level1 -->        
    <xsl:variable name="country-code" select="data(/ontology/bungeni/country)" />
    <xsl:variable name="parliament-election-date" select="data(/ontology/bungeni/parliament/@date)" />
    <xsl:variable name="for-parliament" select="data(/ontology/bungeni/parliament/@href)" />  
    <xsl:variable name="parliament-id" select="data(/ontology/bungeni/@id)" />

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
    
    <xsl:template match="field[@name='type']">
        <type>
            <xsl:value-of select="." />
        </type>
    </xsl:template> 
    
    <xsl:template match="field[@name='tag']">
        <tag>
            <xsl:value-of select="." />
        </tag>
    </xsl:template>    
 
    <xsl:template match="field[@name='parliament_id']">
        <parliamentId>
            <xsl:value-of select="." />
        </parliamentId>
    </xsl:template> 
    
    <xsl:template match="field[@name='short_name']">
        <shortName>
            <xsl:value-of select="." />
        </shortName>
    </xsl:template>  
    
    <xsl:template match="field[@name='full_name']">
        <fullName>
            <xsl:value-of select="." />
        </fullName>
    </xsl:template>     
    
    <xsl:template match="field[@name='group_principal_id']">
        <groupPrincipalId>
            <xsl:value-of select="." />
        </groupPrincipalId>
    </xsl:template>  
    
    <xsl:template match="field[@name='group_id']">
        <groupId>
            <xsl:value-of select="." />
        </groupId>
    </xsl:template>       
    
    <xsl:template match="field[@name='election_date']">
        <electionDate type="xs:date">
            <xsl:value-of select="." />
        </electionDate>
    </xsl:template>  
    
    <xsl:template match="field[@name='start_date']">
        <startDate type="xs:date">
            <xsl:value-of select="." />
        </startDate>
    </xsl:template>     
    
    <xsl:template match="field[@name='description']">
        <description>
            <xsl:value-of select="." />
        </description>
    </xsl:template>    
    
    <xsl:template match="permissions">
        <permissions>
            <xsl:apply-templates />
        </permissions>
    </xsl:template>
    
    <xsl:template match="permission">
        <permission 
            setting="{field[@name='setting']}" 
            name="{field[@name='permission']}"  
            role="{field[@name='role']}" />
    </xsl:template>    
    
    <xsl:template match="field[@name='text']">
        <text>
            <xsl:value-of select="." />
        </text>
    </xsl:template>  
    
    <xsl:template match="field[@name='language']">
        <language>
            <xsl:attribute name="showAs" select="@displayAs"/>
            <xsl:value-of select="." />
        </language>
    </xsl:template>    

    <xsl:template match="field[@name='status_date']">
        <xsl:variable name="status_date" select="." />
        <xsl:variable name="arrStatusDate" select="tokenize($status_date,'\s+')" />
        <statusDate type="xs:dateTime"><xsl:value-of select="concat($arrStatusDate[1],'T',$arrStatusDate[2])" /></statusDate>
    </xsl:template>
    
    <xsl:template match="field[@name='timestamp' or 
        @name='date_active' or 
        @name='date_audit']">
        <xsl:element name="{local-name()}" >
            <xsl:variable name="status_date" select="." />
            <xsl:variable name="arrStatusDate" select="tokenize($status_date,'\s+')" />
            <xsl:attribute name="name" select="@name" />      
            <xsl:value-of select="concat($arrStatusDate[1],'T',$arrStatusDate[2])" />
        </xsl:element>
    </xsl:template>   
    
    <xsl:template match="field[@name='status']">
        <status><xsl:value-of select="."/></status>
    </xsl:template>     
   
</xsl:stylesheet>