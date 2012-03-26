<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Oct 26, 2011</xd:p>
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
    
    <xsl:template match="field[@name='active_p']">
        <status>
            <xsl:variable name="field_active" select="." />
            <xsl:choose >
                <xsl:when test="$field_active eq 'A'">active</xsl:when>
                <xsl:otherwise>inactive</xsl:otherwise>
            </xsl:choose>
        </status>
    </xsl:template>
    
    <xsl:template match="field[@name='first_name']">
        <firstName>
            <xsl:value-of select="." />
        </firstName>
    </xsl:template>   
    
    <xsl:template match="field[@name='last_name']">
        <lastName>
            <xsl:value-of select="." />
        </lastName>
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
    
    <xsl:template match="field[@name='user_id']">
        <userId>
            <xsl:value-of select="." />
        </userId>
    </xsl:template> 
    
    <xsl:template match="field[@name='group_id']">
        <groupId>
            <xsl:value-of select="." />
        </groupId>
    </xsl:template> 
    
    <xsl:template match="field[@name='party_id']">
        <partyId>
            <xsl:value-of select="." />
        </partyId>
    </xsl:template>    
    
    <xsl:template match="field[@name='description']">
        <description>
            <xsl:value-of select="." />
        </description>
    </xsl:template>
    
    <xsl:template match="field[@name='language']">
        <language>
            <xsl:value-of select="." />
        </language>
    </xsl:template>    
    
    <xsl:template match="field[@name='gender']">
        <xsl:variable name="field_gender" select="." />
        <gender>
            <xsl:choose >
                <xsl:when test="$field_gender eq 'M'">male</xsl:when>
                <xsl:when test="$field_gender eq 'F'">female</xsl:when>
                <xsl:otherwise>unknown</xsl:otherwise>
            </xsl:choose>
        </gender>
    </xsl:template>
    
    <xsl:template match="field[@name='date_of_birth']">
        <dateOfBirth>
            <xsl:value-of select="." />
        </dateOfBirth>
    </xsl:template>   
    
    <xsl:template match="field[@name='titles']">
        <titles>
            <xsl:value-of select="." />
        </titles>
    </xsl:template>  
    
    <xsl:template match="field[@name='birth_country']">
        <birthCountry>
            <xsl:value-of select="." />
        </birthCountry>
    </xsl:template>
    
    <xsl:template match="field[@name='national_id']">
        <nationalId>
            <xsl:value-of select="." />
        </nationalId>
    </xsl:template>   
    
    <xsl:template match="field[@name='login']">
        <login>
            <xsl:value-of select="." />
        </login>
    </xsl:template>    
    
    <xsl:template match="field[@name='password']">
        <password>
            <xsl:value-of select="." />
        </password>
    </xsl:template> 
    
    <xsl:template match="field[@name='salt']">
        <salt>
            <xsl:value-of select="." />
        </salt>
    </xsl:template>    
    
    <xsl:template match="field[@name='email']">
        <email>
            <xsl:value-of select="." />
        </email>
    </xsl:template> 
    
    <xsl:template match="field[@name='type']">
        <type>
            <xsl:value-of select="." />
        </type>
    </xsl:template>     
    
    <xsl:template match="field[@name='birth_nationality']">
        <birthNationality>
            <xsl:value-of select="." />
        </birthNationality>
    </xsl:template>    
    
    <xsl:template match="field[@name='current_nationality']">
        <currentNationality>
            <xsl:value-of select="." />
        </currentNationality>
    </xsl:template> 
    
    <xsl:template match="field[@name='partymember']">
        <partyMember>
            <xsl:value-of select="."/>
        </partyMember>
    </xsl:template>
    
    <xsl:template match="field[@name='receive_notification']">
        <receiveNotification>
            <xsl:value-of select="."/>
        </receiveNotification>
    </xsl:template>
    
    <xsl:template match="field[@name='status']">
        <status>
            <xsl:value-of select="."/>
        </status>
    </xsl:template>    
    
    <xsl:template match="field[@name='start_date']">
        <startDate type="xs:date"><xsl:value-of select="." /></startDate>
    </xsl:template>    
    
    <xsl:template match="field[@name='status_date']">
        <xsl:variable name="status_date" select="." />
        <xsl:variable name="arrStatusDate" select="tokenize($status_date,'\s+')" />
        <statusDate type="xs:dateTime"><xsl:value-of select="concat($arrStatusDate[1],'T',$arrStatusDate[2])" /></statusDate>
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