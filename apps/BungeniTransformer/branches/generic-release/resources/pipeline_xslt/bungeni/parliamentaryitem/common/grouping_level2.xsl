<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:xbf="http://bungeni.org/xslt/functions"
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
    
    <xsl:variable name="country-code" select="string('ke')" />
    <xsl:variable name="parliament-election-date" select="string('2011-03-02')" />
    <xsl:variable name="for-parliament" select="concat('/ke/parliament/', $parliament-election-date)" />
    
    
    <xsl:function name="xbf:parse-date">
        <xsl:param name="input-date"/>
        <xsl:variable name="arrInputDate" select="tokenize($input-date,'\s+')" />
        <xsl:sequence select="concat($arrInputDate[1],'T',$arrInputDate[2])" />
    </xsl:function>
    
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
    
    <xsl:template match="field[@name='language']">
        <language><xsl:value-of select="." /></language>
    </xsl:template>
    
    <xsl:template match="field[@name='status_date']">
        <xsl:variable name="status_date" select="." />
        <!--
        <xsl:variable name="arrStatusDate" select="tokenize($status_date,'\s+')" />
        -->
        <statusDate type="xs:dateTime">
            <xsl:value-of select="xbf:parse-date($status_date)" />
        </statusDate>
    </xsl:template>
    
    <xsl:template match="field[@name='timestamp' or 
        @name='date_active' or 
        @name='date_audit']">
        <xsl:element name="{local-name()}" >
            <xsl:attribute name="name" select="@name" />      
            
            <xsl:variable name="status_date" select="." />
            <xsl:value-of select="xbf:parse-date($status_date)" />
            <!--
            <xsl:variable name="arrStatusDate" select="tokenize($status_date,'\s+')" />
            <xsl:value-of select="concat($arrStatusDate[1],'T',$arrStatusDate[2])" />
            -->
        </xsl:element>
    </xsl:template>    
    
    
    <!-- Only for question -->
    
    <xsl:template match="field[@name='question_number']">
        <itemNumber>
            <xsl:value-of select="." />
        </itemNumber>
    </xsl:template>    
    
    <xsl:template match="field[
        @name='question_id' or 
        @name='bill_id']">
        <itemId>
            <xsl:value-of select="." />
        </itemId>
    </xsl:template>
    
    <xsl:template match="permissions">
        <permisssions>
            <xsl:apply-templates />
        </permisssions>
    </xsl:template>
    
    <xsl:template match="permission">
        <permission 
            setting="{field[@name='setting']}" 
            name="{field[@name='permission']}"  
            role="{field[@name='role']}" />
    </xsl:template>
    
    <xsl:template match="itemsignatories">
        <signatories>
            <xsl:apply-templates />
        </signatories>
    </xsl:template>
    
    <xsl:template match="itemsignatorie">
        <xsl:call-template name="user_render">
            <xsl:with-param name="typeOf" select="string('signatory')" />
        </xsl:call-template>
        <!--
        <xsl:variable name="showAs" select="concat(field[@name='last_name'], ', ' , field[@name='first_name'])" />
        <xsl:variable name="isA" select="string('user')" />
        <signatory isA="{$isA}" href="{concat($for-parliament, '/user/', field[@name='user_id'])}" showAs="{$showAs}" />
        -->
     </xsl:template>
    
    <xsl:template match="owner">
        <xsl:call-template name="user_render">
            <xsl:with-param name="typeOf" select="string('owner')" />
        </xsl:call-template>
        <!--
        <xsl:variable name="showAs" select="concat(field[@name='last_name'], ', ' , field[@name='first_name'])" />
        <xsl:variable name="isA" select="string('user')" />
        <owner isA="{$isA}" href="{concat($for-parliament, '/user/', field[@name='user_id'])}" showAs="{$showAs}" />
        -->
    </xsl:template>
    
    <xsl:template match="events">
        <events>
            <xsl:apply-templates />
        </events>
    </xsl:template>
    
    <xsl:template match="event">
        <xsl:variable name="event-identifier" select="field[@name='event_item_id']" />
        <xsl:variable name="event-date" select="field[@name='event_date']" />
        <xsl:variable name="event-lang" select="field[@name='language']" />
        <event 
            href="{concat($for-parliament,'/event/',$event-identifier, '/', $event-lang)}" 
            showAs="{field[@name='short_name']}" 
            date="{xbf:parse-date($event-date)}" 
        />
    </xsl:template>
    
    
    <xsl:template name="user_render">
        <xsl:param name="typeOf" select="string('unknown')" />
        <xsl:variable name="showAs" select="concat(field[@name='last_name'], ', ' , field[@name='first_name'])" />
        <xsl:variable name="isA" select="string('user')" />
        <xsl:element name="{$typeOf}">
            <xsl:attribute name="isA" select="$isA" />
            <xsl:attribute name="showAs" select="$showAs" />
            <xsl:attribute name="href" select="concat($for-parliament, '/user/', field[@name='user_id'])" />
        </xsl:element>
    </xsl:template>
    
    
    
    <xsl:template match="field[@name='owner_id'] | field[@name='receive_notification']">
        <!-- Dont emit owner id -->
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