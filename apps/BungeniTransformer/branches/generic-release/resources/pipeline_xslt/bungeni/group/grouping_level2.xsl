<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Oct 17, 2011</xd:p>
            <xd:p><xd:b>Author:</xd:b> anthony</xd:p>
            <xd:p></xd:p>
        </xd:desc>
    </xd:doc>

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
            <xsl:apply-templates />
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="field[@name='type']">
        <type>
            <xsl:value-of select="." />
        </type>
    </xsl:template>      
    
    <xsl:template match="field[@name='group_id']">
        <groupId>
            <xsl:value-of select="." />
        </groupId>
    </xsl:template>    
    
    <xsl:template match="field[@name='parliament_id']">
        <parliamentId>
            <xsl:value-of select="." />
        </parliamentId>
    </xsl:template>      
    
    <xsl:template match="field[@name='parent_group_id']">
        <parentGroupId>
            <xsl:value-of select="." />
        </parentGroupId>
    </xsl:template>
    
    <xsl:template match="field[@name='min_num_members']">
        <minNumMembers>
            <xsl:value-of select="." />
        </minNumMembers>
    </xsl:template>
    
    <xsl:template match="field[@name='num_researchers']">
        <numResearchers>
            <xsl:value-of select="." />
        </numResearchers>
    </xsl:template>    
    
    <xsl:template match="field[@name='start_date']">
        <startDate type="xs:date"><xsl:value-of select="." /></startDate>
    </xsl:template>
    
    <xsl:template match="field[@name='election_date']">
        <electionDate type="xs:date">
            <xsl:value-of select="." />
        </electionDate>
    </xsl:template>
    
    <xsl:template match="field[@name='dissolution_date']">
        <dissolutionDate>
            <xsl:value-of select="." />
        </dissolutionDate>
    </xsl:template>  
    
    <xsl:template match="field[@name='dissolution_date']">
        <resultsDate>
            <xsl:value-of select="." />
        </resultsDate>
    </xsl:template>     
    
    <xsl:template match="field[@name='num_members']">
        <numMembers>
            <xsl:value-of select="." />
        </numMembers>
    </xsl:template>    
    
    <xsl:template match="field[@name='quorum']">
        <quorum>
            <xsl:value-of select="." />
        </quorum>
    </xsl:template>

    <xsl:template match="field[@name='body_text']">
        <body>
            <xsl:value-of select="." />
        </body>
    </xsl:template>    

    <xsl:template match="field[@name='status']">
        <status>
            <xsl:value-of select="." />
        </status>
    </xsl:template>

    <xsl:template match="field[@name='question_number']">
        <itemNumber>
            <xsl:value-of select="." />
        </itemNumber>
    </xsl:template>

    <xsl:template match="field[@name='question_id']">
        <itemId>
            <xsl:value-of select="." />
        </itemId>
    </xsl:template>

    <xsl:template match="field[@name='registry_number']">
        <registryNumber>
            <xsl:value-of select="." />
        </registryNumber>
    </xsl:template>

    <xsl:template match="field[@name='parliamentary_item_id']">
        <legislativeItemId>
            <xsl:value-of select="." />
        </legislativeItemId>
    </xsl:template>
    
    <xsl:template match="field[@name='country_code']">
        <country>
            <xsl:attribute name="code">
                <xsl:value-of><xsl:text>KE</xsl:text></xsl:value-of>
            </xsl:attribute>
            <xsl:text>Kenya</xsl:text>
        </country>
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
    
    <xsl:template match="field[@name='description']">
        <description>
            <xsl:value-of select="." />
        </description>
    </xsl:template>     
    
    <xsl:template match="field[@name='language']">
        <language><xsl:value-of select="." /></language>
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

</xsl:stylesheet>