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
    <!--    
        <bungeni>
        <country><xsl:value-of select="$country-code" /></country>
        <parliament href="{$for-parliament}" 
        isA="TLCOrganization" 
        date="{$parliament-election-date}" />
        </bungeni>
        -->
        
    <!-- These values are set in first input which is grouping_Level1 -->        
    <xsl:variable name="country-code" select="data(/ontology/bungeni/country)" />
    <xsl:variable name="parliament-election-date" select="data(/ontology/bungeni/parliament/@date)" />
    <xsl:variable name="for-parliament" select="data(/ontology/bungeni/parliament/@href)" />
    <xsl:variable name="parliament-id" select="data(/ontology/bungeni/@id)" />
    
    <xsl:function name="xbf:parse-date">
        <xsl:param name="input-date"/>
        <xsl:variable name="arrInputDate" select="tokenize($input-date,'\s+')" />
        <xsl:sequence select="concat($arrInputDate[1],'T',$arrInputDate[2])" />
    </xsl:function>
    
    
    <xsl:function name="xbf:parse-date-nomicrotime">
        <xsl:param name="input-date"/>
        <xsl:variable name="arrInputDate" select="tokenize($input-date,'\s+')" />
        <xsl:variable name="arrInputTime" select="substring-before(string($arrInputDate[2]), '.')" />
        <xsl:sequence select="concat($arrInputDate[1],'T',$arrInputTime)" />
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
    
    <xsl:template match="field[@name='body_text' or @name='body']">
        <body>
            <xsl:value-of select="." />
        </body>
    </xsl:template>    
    
    <xsl:template match="field[@name='assignment_id']">
        <assignmentId>
            <xsl:value-of select="." />
        </assignmentId>
    </xsl:template>  
    
    <xsl:template match="field[@name='user_id']">
        <userId>
            <xsl:value-of select="." />
        </userId>
    </xsl:template>  
    
    <xsl:template match="field[@name='parliament_id']">
        <parliamentId>
            <xsl:value-of select="."/>
        </parliamentId>
    </xsl:template>
    
    <xsl:template match="field[@name='action']">
        <action>
            <xsl:value-of select="." />
        </action>
    </xsl:template>
    
    <xsl:template match="field[@name='response_type']">
        <responseType>
            <xsl:value-of select="." />
        </responseType>
    </xsl:template>
    
    <xsl:template match="field[@name='question_type']">
        <questionType>
            <xsl:value-of select="." />
        </questionType>
    </xsl:template>    
    
    <xsl:template match="field[@name='start_date']">
        <startDate>
            <xsl:value-of select="." />
        </startDate>
    </xsl:template> 
    
    <xsl:template match="field[@name='group_id']">
        <groupId>
            <xsl:value-of select="." />
        </groupId>
    </xsl:template>   
    
    <xsl:template match="field[@name='item_id']">
        <itemId>
            <xsl:value-of select="." />
        </itemId>
    </xsl:template>     
    
    <xsl:template match="field[@name='notes']">
        <notes>
            <xsl:value-of select="." />
        </notes>
    </xsl:template> 
    
    <xsl:template match="field[@name='status']">
        <status>
            <xsl:value-of select="." />
        </status>
    </xsl:template>    
    
    <xsl:template match="field[@name='description']">
        <description>
            <xsl:value-of select="." />
        </description>
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
    
    <xsl:template match="field[@name='change_id']">
        <changeId>
            <xsl:value-of select="." />
        </changeId>
    </xsl:template>    
    
    <xsl:template match="field[@name='manual']">
        <manual>
            <xsl:value-of select="." />
        </manual>
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
    
    <xsl:template match="field[@name='file_title']">
        <fileTitle>
            <xsl:value-of select="." />
        </fileTitle>
    </xsl:template>   
    
    <xsl:template match="field[@name='file_mimetype']">
        <fileMimetype>
            <xsl:value-of select="." />
        </fileMimetype>
    </xsl:template>   
    
    <xsl:template match="field[@name='file_name']">
        <fileName>
            <xsl:value-of select="." />
        </fileName>
    </xsl:template>  
    
    <xsl:template match="field[@name='attached_file_type']">
        <attachedFileType>
            <xsl:value-of select="." />
        </attachedFileType>
    </xsl:template>    
    
    <xsl:template match="field[@name='attached_file_id']">
        <attachedFileId>
            <xsl:value-of select="." />
        </attachedFileId>
    </xsl:template>  
    
    <xsl:template match="field[@name='saved_file']">
        <savedFile>
            <xsl:value-of select="." />
        </savedFile>
    </xsl:template>  
    
    <xsl:template match="field[@name='att_uuid']">
        <attachedFileUuid>
            <xsl:value-of select="." />
        </attachedFileUuid>
    </xsl:template>    
    
    <xsl:template match="field[@name='language']">
        <language><xsl:value-of select="." /></language>
    </xsl:template>
    
    <xsl:template match="field[@name='uri']">
        <uri>
            <xsl:value-of select="." />
        </uri>
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
    
    <xsl:template match="field[@name='date_active']">
        <dateActive type="xs:dateTime">
            <xsl:variable name="active_date" select="." />
            <xsl:value-of select="xbf:parse-date($active_date)" />
        </dateActive>
    </xsl:template> 

    <xsl:template match="field[@name='date_audit']">
        <dateAudit type="xs:dateTime">
            <xsl:variable name="audit_date" select="." />
            <xsl:value-of select="xbf:parse-date($audit_date)" />
        </dateAudit>
    </xsl:template> 
    
    
    <!-- Only for question -->
    
    <xsl:template match="field[
        @name='question_number' or
        @name='motion_number' or
        @name='bill_number' or
        @name='tabled_document_number'
        ]">
        <itemNumber>
            <xsl:value-of select="." />
        </itemNumber>
    </xsl:template>    
    
    <xsl:template match="field[
        @name='question_id' or 
        @name='bill_id' or 
        @name='motion_id' or 
        @name='tabled_document_id'
        ]">
        <itemId>
            <xsl:value-of select="." />
        </itemId>
    </xsl:template>
    
    <xsl:template match="field[@name='tag']">
        <tag>
            <xsl:value-of select="." />
        </tag>
    </xsl:template>    
    
    <xsl:template match="field[@name='doc_type']">
        <xsl:variable name="parent-type" select="//legislativeItem/field[@name='type']" />
        <xsl:variable name="value" select="." />
        <xsl:element name="type">
            <xsl:attribute 
                name="href" 
                select="concat('/ontology/',$country-code, '/', $parent-type, '/', $value)"
                />
             <xsl:attribute 
                 name="showAs" 
                 select="$value" />
        </xsl:element>    
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
    
    <xsl:template match="itemsignatories">
        <signatories>
            <xsl:apply-templates />
        </signatories>
    </xsl:template>
    
    <!-- Only for events -->
    
    <xsl:template match="field[@name='short_title']">
        <shortTitle>
            <xsl:value-of select="." />
        </shortTitle>
    </xsl:template>  
    
    <xsl:template match="field[@name='long_title']">
        <longTitle>
            <xsl:value-of select="." />
        </longTitle>
    </xsl:template>       
    
    <xsl:template match="field[@name='doc_id']">
        <docId>
            <xsl:value-of select="." />
        </docId>
    </xsl:template> 
    
    <xsl:template match="field[@name='acronym']">
        <acronym>
            <xsl:value-of select="." />
        </acronym>
    </xsl:template>    
    
    <xsl:template match="field[@name='audit_date_active']">
        <xsl:variable name="audit_date_active" select="." />
        <auditDateActive type="xs:dateTime">
            <xsl:value-of select="xbf:parse-date($audit_date_active)" />
        </auditDateActive>
    </xsl:template>
    
    <!-- Causes Ambiguity witj a similar template on line 278 above... -->
    <!-- xsl:template match="field[@name='doc_type']">
        <docType>
            <xsl:value-of select="." />
        </docType>
    </xsl:template -->   
    
    <xsl:template match="field[@name='audit_action']">
        <audtiAction>
            <xsl:value-of select="." />
        </audtiAction>
    </xsl:template>  

    <xsl:template match="field[@name='audit_date']">
        <xsl:variable name="audit_date" select="." />
        <auditDate type="xs:dateTime">
            <xsl:value-of select="xbf:parse-date($audit_date)" />
        </auditDate>
    </xsl:template>   
    
    <xsl:template match="field[@name='audit_id']">
        <auditId>
            <xsl:value-of select="." />
        </auditId>
    </xsl:template>   
    
    <xsl:template match="field[@name='head_id']">
        <headId>
            <xsl:value-of select="." />
        </headId>
    </xsl:template>    
    
    <xsl:template match="field[@name='audit_head_id']">
        <auditHeadId>
            <xsl:value-of select="." />
        </auditHeadId>
    </xsl:template>    
    
    <xsl:template match="field[@name='audit_user_id']">
        <auditUserId>
            <xsl:value-of select="." />
        </auditUserId>
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
    
    <xsl:template match="field[@name='publication_date']">
        <publicationDate type="xs:date"><xsl:value-of select="." /></publicationDate>
    </xsl:template>
    
    <xsl:template match="events">
        <wfevents>
            <xsl:apply-templates mode="parent_is_events" />
        </wfevents>
    </xsl:template>
    
    <xsl:template match="event" mode="parent_is_events">
        <xsl:variable name="event-identifier" select="field[@name='doc_id']" />
        <xsl:variable name="event-date" select="field[@name='status_date']" />
        <xsl:variable name="event-lang" select="field[@name='language']" />
        <wfevent 
            href="{concat('/',$country-code,'/event/',$event-identifier, '/', $event-lang)}" 
            isA="TLCEvent"
            showAs="{field[@name='short_title']}" 
            date="{xbf:parse-date($event-date)}" 
        >
            <xsl:apply-templates />
        </wfevent>
    </xsl:template>
    
    <xsl:template match="versions">
        <versions>
            <xsl:apply-templates />
        </versions>
    </xsl:template>
    
    <xsl:template match="version">
        <xsl:variable name="doc-uri" select="//legislativeItem/@uri" />
        <xsl:variable name="status-date" select="field[@name='status_date']" />
        <xsl:variable name="version-status-date" select="xbf:parse-date-nomicrotime($status-date)" />

        <version isA="TLCConcept" 
            uri="{concat($doc-uri, '@', $version-status-date)}" 
            id="ver-{data(field[@name='version_id'])}"
            user-generated="{lower-case(data(field[@name='manual']))}"
            >

            <xsl:apply-templates />
        
        </version>
   
    </xsl:template>
    
    <xsl:variable name="parl_id" select="field[@name='owner_id']" />
    
    <xsl:template name="user_render">
        <xsl:param name="typeOf" select="string('unknown')" />
        <xsl:variable name="showAs" select="concat(field[@name='last_name'], ', ' , field[@name='first_name'])" />
        <xsl:variable name="isA" select="string('user')" />
        <xsl:element name="{$typeOf}">
            <xsl:attribute name="isA" select="$isA" />
            <xsl:attribute name="showAs" select="$showAs" />
            <xsl:attribute name="href" select="concat($for-parliament,'/',2,'/member/', field[@name='user_id'])" />
        </xsl:element>
    </xsl:template>
    
    <!-- for <event>s -->
    <xsl:template match="//head/field[@name='type']">
        <eventOf>
            <xsl:value-of select="." />
        </eventOf>
    </xsl:template>

    <!--+FIX_THIS content_id, ministry_id is suppresed for versions -->
    <xsl:template match="field[
        @name='content_id' or
        @name='timestamp' or 
        @name='ministry_id' or
        @name='version_id' 
        ]">
        
        <!-- dont emit -->
        
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