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
    
    <xsl:template match="field[@name='status_date']">
        <xsl:variable name="status_date" select="." />
        <xsl:variable name="arrStatusDate" select="tokenize($status_date,'\s+')" />
        <statusDate type="xs:dateTime"><xsl:value-of select="concat($arrStatusDate[1],'T',$arrStatusDate[2])" /></statusDate>
    </xsl:template>
  
    <xsl:template match="field[@name='body_text']">
        <body>
            <xsl:value-of select="." />
        </body>
    </xsl:template>
  
  
    <!-- Only for question -->
  
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
    
    
    
  
    <!--!+MINISTRY_MATCH (ah, oct-2011) removing this for now  
    <xsl:template match="ministry">
        <xsl:variable name="parliament-href" select="/ontology/bungeni/parliament/@href" />
        <xsl:variable name="group_principal_id" select="field[@name='group_principal_id']" />
        <group 
            href="{concat($parliament-href,'/', $group_principal_id)}" 
            isA="TLCOrganization" 
            showAs="{field[@name='short_name']}">
          
        </group>
            
        
    </xsl:template>
    -->
    
</xsl:stylesheet>