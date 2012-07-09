<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:bdates="http://www.bungeni.org/xml/dates/1.0"
    xmlns:busers="http://www.bungeni.org/xml/users/1.0"
    xmlns:bctypes="http://www.bungeni.org/xml/contenttypes/1.0"
    exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:import href="resources/pipeline_xslt/bungeni/common/func_dates.xsl" />
    <xsl:import href="resources/pipeline_xslt/bungeni/common/func_users.xsl" />
    <xsl:import href="resources/pipeline_xslt/bungeni/common/func_content_types.xsl" />
     
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
    <xsl:variable name="country-code" select="data(/ontology/legislature/country)" />
    <xsl:variable name="parliament-election-date" select="data(/ontology/bungeni/parliament/@date)" />
    <xsl:variable name="for-parliament" select="data(/ontology/bungeni/parliament/@href)" />
    <xsl:variable name="parliament-id" select="data(/ontology/bungeni/@id)" />
    <xsl:variable name="type-mappings" select="//custom/value" />
    
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
         <body >
            <xsl:value-of select="." />
        </body>
    </xsl:template>    
    
    <!--
    <xsl:template match="field[@name='assignment_id']">
        <assignmentId>
            <xsl:value-of select="." />
        </assignmentId>
    </xsl:template>  -->
    
    <xsl:template match="field[@name='user_id']">
        <userId type="xs:integer">
            <xsl:value-of select="." />
        </userId>
    </xsl:template>  
    
    <xsl:template match="field[@name='parliament_id']">
        <parliamentId type="xs:integer">
            <xsl:value-of select="."/>
        </parliamentId>
    </xsl:template>
    
    <!-- !+NOTES (ao, 10-Apr-2012) audit_action is to catch date_active 
        embedded in event type documents -->    

    <xsl:template match="_vp_response_type">
        <responseType isA="TLCTerm">
            <value type="xs:string">
                <xsl:value-of select="field[@name='value']" />
            </value>
        </responseType>
    </xsl:template>
    
    <xsl:template match="field[@name='doc_type']">
        <docSubType isA="TLCTerm">
            <value type="xs:string"><xsl:value-of select="." /></value>
        </docSubType>
    </xsl:template>    
    
    <xsl:template match="field[@name='attachment_id']">
        <attachmentId key="true" type="xs:integer">
            <xsl:value-of select="." />
        </attachmentId>
    </xsl:template>
    
    <xsl:template match="field[@name='type_number']" >
        <progressiveNumber type="xs:integer"><xsl:value-of select="." /></progressiveNumber>
    </xsl:template>
        
    <xsl:template match="field[@name='start_date']">
        <startDate type="xs:dateTime">
            <xsl:variable name="start_date" select="." />
            <xsl:value-of select="bdates:parse-date($start_date)" />
        </startDate>
    </xsl:template> 
    
    <xsl:template match="field[@name='group_id']">
        <groupId type="xs:integer">
            <xsl:value-of select="." />
        </groupId>
    </xsl:template>   
    
    <xsl:template match="field[@name='item_id']">
        <itemId type="xs:integer">
            <xsl:value-of select="." />
        </itemId>
    </xsl:template>     
    
    <xsl:template match="field[@name='notes']">
        <notes type="xs:string">
            <xsl:value-of select="." />
        </notes>
    </xsl:template> 
    
    <xsl:template match="field[@name='status']">
        <status isA="TLCTerm">
            <value type="xs:string"><xsl:value-of select="." /></value>
        </status>
    </xsl:template>    
    
    <xsl:template match="field[@name='timestamp']">
        <xsl:variable name="timestampDate" select="bdates:parse-date(data(.))" />
       <timestampDate type="xs:dateTime">
           <xsl:value-of select="$timestampDate" />         
        </timestampDate>
    </xsl:template>

    
    
    <xsl:template match="field[@name='registry_number']">
        <registryNumber type="xs:string">
            <xsl:value-of select="." />
        </registryNumber>
    </xsl:template>    
    <!--
    <xsl:template match="field[@name='parliamentary_item_id']">
        <legislativeItemId>
            <xsl:value-of select="." />
        </legislativeItemId>
    </xsl:template> -->
    
    <xsl:template match="field[@name='change_id']">
        <changeId type="xs:string">
            <xsl:value-of select="." />
        </changeId>
    </xsl:template>    
    
    <xsl:template match="field[@name='manual']">
        <manual>
            <xsl:value-of select="." />
        </manual>
    </xsl:template>     
    
    <xsl:template match="field[@name='title']">
        <title type="xs:string">
            <xsl:value-of select="." />
        </title>
    </xsl:template>  
    
    <xsl:template match="field[@name='long_title']">
        <longTitle type="xs:string">
            <xsl:value-of select="." />
        </longTitle>
    </xsl:template>  
    
    <xsl:template match="field[@name='mimetype']">
        <mimetype isA="TLCTerm">
            <value type="xs:string"><xsl:value-of select="." /></value>
        </mimetype>
    </xsl:template>   
    
    <xsl:template match="field[@name='name']">
        <name type="xs:string">
            <xsl:value-of select="." />
        </name>
    </xsl:template>  
    
    <xsl:template match="field[@name='type']">
        <type isA="TLCTerm">
            <value type="xs:string">
                <xsl:value-of select="bctypes:get_content_type_element_name(., $type-mappings)" />
            </value>
        </type>
    </xsl:template>    
    
    
    <xsl:template match="field[@name='audit_type']">
        <auditFor isA="TLCTerm">
            <value type="xs:string">
                <xsl:value-of select="bctypes:get_content_type_element_name(., $type-mappings)" />
            </value>
        </auditFor>
    </xsl:template>    
    
    
    
    <xsl:template match="field[@name='saved_file']">
        <savedFile type="xs:string">
            <xsl:value-of select="." />
        </savedFile>
    </xsl:template>  
    
    <xsl:template match="field[@name='att_uuid']">
        <fileUuid type="xs:string">
            <xsl:value-of select="." />
        </fileUuid>
    </xsl:template>
    
    <xsl:template match="field[@name='language']">
        <!-- !+RENDERED NOW as xml:lang on the legislativeItem
         <language type="xs"><xsl:value-of select="." /></language>
        -->
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
            <xsl:value-of select="bdates:parse-date($status_date)" />
        </statusDate>
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
        <tag isA="TLCTerm">
            <value type="xs:string">
            <xsl:value-of select="." />
            </value>    
        </tag>
    </xsl:template>    
    
    <!--
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
    </xsl:template> -->
    
    <xsl:template match="permissions">
        <permissions>
            <xsl:apply-templates />
        </permissions>
    </xsl:template>
    
    <xsl:template match="permissions[parent::document]">
        <permissions id="documentPermissions">
            <xsl:apply-templates />
        </permissions>
    </xsl:template>

    <xsl:template match="permission">
        <permission 
            setting="{field[@name='setting']}" 
            name="{field[@name='permission']}"  
            role="{field[@name='role']}" />
    </xsl:template>
    
    <xsl:template match="item_signatories">
        <xsl:if test="child::*">
        <signatories>
            <xsl:apply-templates />
        </signatories>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="item_signatorie">
        <signatory isA="TLCReference">
            <xsl:apply-templates />
        </signatory>
    </xsl:template>     
    
    <!-- Only for events -->
    
    
    <xsl:template match="field[@name='doc_id']">
        <docId type="xs:integer" key="true">
            <xsl:value-of select="." />
        </docId>
    </xsl:template> 
    
    <xsl:template match="field[@name='signatory_id']">
        <signatoryId type="xs:integer" key="true">
            <xsl:value-of select="." />
        </signatoryId>
    </xsl:template>     
    
    <xsl:template match="field[@name='acronym']">
        <acronym isA="TLCTerm">
            <value type="xs:string"><xsl:value-of select="." /></value>
        </acronym>
    </xsl:template>    
    
    <!-- Causes Ambiguity witj a similar template on line 278 above... -->
    <!-- xsl:template match="field[@name='doc_type']">
        <docType>
            <xsl:value-of select="." />
        </docType>
        </xsl:template -->  
    
    
    <!-- !+BEGIN(AUDIT) AUDIT, CHANGE AND VERSION TEMPLATES -->
    
    
    <xsl:template match="versions">
        <versions>
            <xsl:apply-templates />
        </versions>
    </xsl:template>
  
    <xsl:template match="versions[parent::document]">
        <versions id="documentVersions">
            <xsl:apply-templates />
        </versions>
    </xsl:template>
    
    
    <xsl:template match="version">
        <!--
        <xsl:variable name="doc-uri" select="//legislativeItem/@uri" />
        <xsl:variable name="active-date" select="field[@name='date_active']" />
        <xsl:variable name="version-status-date" select="bdates:parse-date($active-date)" />
        -->
        <version isA="TLCObject">
            <xsl:apply-templates />
        </version>
        
    </xsl:template>
    

    <xsl:template match="field[@name='seq']">
        <sequence type="xs:integer"><xsl:value-of select="." /></sequence>    
    </xsl:template>
    
    <xsl:template match="field[@name='date_active']">
        <activeDate type="xs:dateTime">
            <xsl:variable name="active_date" select="." />
            <xsl:value-of select="bdates:parse-date($active_date)" />
        </activeDate>
    </xsl:template> 
    
    <xsl:template match="field[@name='date_audit']">
        <auditDate type="xs:dateTime">
            <xsl:variable name="audit_date" select="." />
            <xsl:value-of select="bdates:parse-date($audit_date)" />
        </auditDate>
    </xsl:template> 
    
    <xsl:template match="field[@name='action']">
        <auditAction isA="TLCEvent">
            <value type="xs:string">
            <xsl:value-of select="." />
            </value>
        </auditAction>
    </xsl:template>
    
    
    <xsl:template match="field[@name='audit_id']">
        <auditId type="xs:integer">
            <xsl:value-of select="." />
        </auditId>
    </xsl:template>   
    
    <xsl:template match="field[@name='head_id']">
        <headId type="xs:integer">
            <xsl:value-of select="." />
        </headId>
    </xsl:template>    
    
    
    
    <!--
    <xsl:template match="field[@name='audit_head_id']">
        <auditHeadId type="xs:integer">
            <xsl:value-of select="." />
        </auditHeadId>
    </xsl:template>    
    -->
    
    <xsl:template match="field[@name='audit_user_id']">
        <auditUserId>
            <xsl:value-of select="." />
        </auditUserId>
    </xsl:template>  
    
    
    <xsl:template match="field[@name='description']">
        <description>
            <xsl:value-of select="." />
        </description>
    </xsl:template>     
    
    <xsl:template match="field[@name='procedure']">
        <procedureType isA="TLCTerm">
            <value type="xs:string"><xsl:value-of select="." /></value>
        </procedureType>
    </xsl:template>
    
    
    <xsl:template match="changes[parent::document]">
        <changes id="documentChanges">
            <xsl:apply-templates />
        </changes>
    </xsl:template>
    
    <xsl:template match="audits[parent::document]">
        <audits id="documentAudits">
            <xsl:apply-templates />
        </audits>
    </xsl:template>
    
    <xsl:template match="head">
       <eventOf isA="TLCObject">
            <refersTo href="!+FIX_THIS_PUT_SOURCE_URI_HERE" />
            <xsl:apply-templates />
       </eventOf>    
    </xsl:template>
    
    
    
    <!-- !+END_(AUDIT) -->
    <!--
    <xsl:template match="itemsignatorie">
        <xsl:call-template name="user_render">
            <xsl:with-param name="typeOf" select="string('signatory')" />
        </xsl:call-template> -->
        <!--
        <xsl:variable name="showAs" select="concat(field[@name='last_name'], ', ' , field[@name='first_name'])" />
        <xsl:variable name="isA" select="string('user')" />
        <signatory isA="{$isA}" href="{concat($for-parliament, '/user/', field[@name='user_id'])}" showAs="{$showAs}" />
        -->
    <!-- </xsl:template> -->
    
    
    
    <xsl:template match="owner">
        <xsl:variable name="first-name" select="data(field[@name='first_name'])" />
        <xsl:variable name="last-name" select="data(field[@name='last_name'])" />
        <xsl:variable name="user-id" select="data(field[@name='user_id'])" />
        <xsl:variable name="yyyy-mm-dd-dob" select="bdates:parse-datepart-only(data(field[@name='date_of_birth']))" />
        <xsl:variable name="user-identifier" select="busers:get_user_identifer(
                    $country-code, 
                    $last-name, 
                    $first-name, 
                    $user-id, 
                    $yyyy-mm-dd-dob
                    )" 
        />
        <xsl:variable name="user-uri" select="busers:get_user_uri($country-code, $user-identifier)" />
   
        <owner isA="TLCPerson">
            <person href="{$user-uri}" showAs="{concat($last-name, ' ,', $first-name)}" />
            <role type="TLCConcept">
                <value type="xs:string">
                    <xsl:value-of select="bctypes:get_content_type_uri_name(
                        'member_of_parliament',
                        $type-mappings
                        )"></xsl:value-of>
                </value>
            </role>
        </owner>
        <!--
        <xsl:call-template name="user_render">
            <xsl:with-param name="typeOf" select="string('owner')" />
        </xsl:call-template> -->
        <!--
        <xsl:variable name="showAs" select="concat(field[@name='last_name'], ', ' , field[@name='first_name'])" />
        <xsl:variable name="isA" select="string('user')" />
        <owner isA="{$isA}" href="{concat($for-parliament, '/user/', field[@name='user_id'])}" showAs="{$showAs}" />
        -->
    </xsl:template>
    
    <xsl:template match="field[@name='publication_date']">
        <publicationDate type="xs:date"><xsl:value-of select="." /></publicationDate>
    </xsl:template>
    
    
    <!-- !+BEGIN(WORKFLOW) -->
    
    <xsl:template match="sa_events">
        <workflowEvents>
            <xsl:apply-templates mode="parent_is_events" />
        </workflowEvents>
    </xsl:template>
    
    
    
    <xsl:template match="sa_events[parent::document]">
        <workflowEvents id="documentEvents">
            <xsl:apply-templates mode="parent_is_events" />
        </workflowEvents>
    </xsl:template>
    
    
    
    
    <xsl:template match="sa_event" mode="parent_is_events">
        <!-- these URIs will be rewritten in idgenerate.xsl -->
        <!--@            href="{concat('/',$country-code,'/event/',$event-identifier, '/', $event-lang)}" 
            -->
        <workflowEvent 
            isA="TLCEvent"
            showAs="{field[@name='title']}" 
        >
            <xsl:apply-templates />
        </workflowEvent>
    </xsl:template>

    <!-- !+END(WORKFLOW) -->
    
    <!--
    <xsl:template name="user_render">
        <xsl:param name="typeOf" select="string('unknown')" />
        <xsl:variable name="showAs" select="concat(field[@name='last_name'], ', ' , field[@name='first_name'])" />
        <xsl:variable name="isA" select="string('TLCPerson')" />
        <xsl:variable name="full-user-identifier"
            select="concat($country-code, '.',
            field[@name='last_name'], '.', 
            field[@name='first_name'], '.', 
            field[@name='date_of_birth'], '.', 
            field[@name='user_id'])" />
        <xsl:element name="{$typeOf}">
            <xsl:attribute name="isA" select="$isA" />
            <xsl:attribute name="showAs" select="$showAs" />
            <xsl:attribute name="href" select="concat('/ontology/Person/',
                $country-code, '/', 
                'ParliamentMember/', 
                $for-parliament, '/', 
                $parliament-election-date, '/',
                $full-user-identifier)"             
            />
        </xsl:element> 
    </xsl:template> -->
    
    <!-- for <event>s -->


    <!--+FIX_THIS content_id, ministry_id is suppresed for versions -->
    <xsl:template match="field[
        @name='content_id' or
        @name='ministry_id' or
        @name='version_id' 
        ]">
        
        <!-- dont emit -->
        
    </xsl:template>
    
    <xsl:template match="field[@name='owner_id'] | field[@name='receive_notification']">
        <!-- Dont emit owner id -->
    </xsl:template>
    
    <!--
    <xsl:template match="custom">
      <xsl:copy-of select="$type-mappings"></xsl:copy-of>
    </xsl:template> -->
    
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