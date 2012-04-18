<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:bctype="http://www.bungeni.org/xml/contenttypes/1.0"
                exclude-result-prefixes="xs bctype"
                version="2.0">
    
    <xsl:import href="resources/pipeline_xslt/bungeni/common/func_content_types.xsl" />
    
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Oct 17, 2011</xd:p>
            <xd:p><xd:b>Author:</xd:b> anthony</xd:p>
            <xd:p></xd:p>
        </xd:desc>
    </xd:doc>
    
    
    <!-- INPUT PARAMETERS TO TRANSFORM-->
    
    <xsl:param name="country-code"  />
    <xsl:param name="parliament-id" />
    <xsl:param name="parliament-election-date"  />
    <xsl:param name="for-parliament" />
    <xsl:param name="type-mappings" />
    
    <!-- INCLUDE FUNCTIONS -->


    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>

    
    <!-- Content Type matcher -->
    <xsl:template match="contenttype">
  
        <!-- this field identifies the type of the input xml bill, question , motion etc. -->
        <xsl:variable name="bungeni-content-type" select="field[@name='type']" />
        <!-- We map the bungeni internal content type name to a alternative name to prevent tie-in to internal representations -->
        <!-- the type mapping specifies both the name in the URI and the Element name -->
        <xsl:variable name="content-type-element-name" select="bctype:get_content_type_element_name($bungeni-content-type, $type-mappings)" />
        <xsl:variable name="content-type-uri-name" select="bctype:get_content_type_uri_name($bungeni-content-type, $type-mappings)" />
        <xsl:variable name="language" select="field[@name='language']" />
 
 
        <!-- ROOT ELEMENT OF DOCUMENT -->
        <ontology for="document">
            
            <!-- 
            Test for and calculate the item_number for the item
            this is available only after a certain stage of the workflow 
            -->
            <xsl:variable name="item_number">
                <xsl:choose>
                    <!--+FIX_THIS is event_item_id the stable identifier for events ? -->
                    <xsl:when test="field[@name='doc_id']" >
                        <xsl:value-of select="field[@name='doc_id']" />
                    </xsl:when>
                    <!--+NOTE (ao,22 Feb 2012) <heading> types pass here too :),
                        since they are a special case without registry_number... They
                        end up with a broken @uri, its known. -->
                    <xsl:otherwise>
                        <xsl:value-of select="field[@name='registry_number']" />
                    </xsl:otherwise>
                </xsl:choose>
                
            </xsl:variable>
            <document id="bungeniDocument" isA="TLCConcept">
                <xsl:attribute name="xml:lang">
                    <xsl:value-of select="field[@name='language']" />
                </xsl:attribute>
                <xsl:attribute name="uri" 
                    select="concat(
                    '/', $country-code,'/', 
                    $content-type-uri-name,'/', 
                    $item_number,'/', 
                    $language
                    )" />
                <docType isA="TLCTerm">
                    <value type="xs:string"> <xsl:attribute name="type" select="$content-type-uri-name" /></value>
                </docType>
               
                
                
      
                
                <!-- !+URI_GENERATOR,!+FIX_THIS(ah,nov-2011) this logic needs to be eventually
                    factored out -->
         
                
                <xsl:copy-of select="field[
                    @name='status_date' or 
                    @name='registry_number' or 
                    @name='doc_id'
                    ] | 
                    changes |
                    audits |
                    sa_events |
                    versions |
                    owner" />
                
                
                <!-- for <event> -->
                <!--
                    <xsl:copy-of select="head" />    
                -->
                
                <xsl:copy-of select="field[
                    @name='status' or 
                    @name='short_title' or 
                    @name='full_title' or 
                    @name='body' or 
                    @name='language'  or 
                    @name='owner_id' or
                    @name='type'
                    ]" />
                
                <!-- NUMBER AND IDENTIFIERS -->
                
                <xsl:copy-of select="field[
                    @name='type_number' 
                    ]" />                  
                
                <!-- for <tableddocument> 
                    <xsl:copy-of select="field[
                    @name='tabled_document_id' or 
                    @name='tabled_document_number' 
                    ]" />    
                    
                    
                    <xsl:copy-of select="field[
                    @name='bill_id' or 
                    @name='bill_type_id' or 
                    @name='bill_number'
                    ]" />
                    
                    <xsl:copy-of select="field[
                    @name='motion_id' or 
                    @name='motion_number'
                    ]" />
                -->
                
                <!-- for <motion> & <bill> !+FIX_THIS(ah,17-04-2012)
                    <xsl:copy-of select="field[
                    @name='publication_date' or
                    @name='doc_type' 
                    ]" />  -->
                
                <!-- for <event> -->
                <xsl:copy-of select="field[
                    @name='doc_type' or 
                    @name='short_title' or 
                    @name='doc_id' or 
                    @name='acronym' or 
                    @name='long_title'
                    ]" />                
                
                <!-- PERMISSIONS -->
                <xsl:copy-of select="permissions" />
                
                <!-- for <question> !+FIX_THIS(ah,17-04-2012)
                    <xsl:if test="ministry/*">
                    <assignedTo group="ministry">
                    <xsl:copy-of select="ministry" />
                    </assignedTo>
                    </xsl:if>                 
                -->               
                
            </document>
            <legislature isA="TLCConcept" href="{$for-parliament}">
               <electionDate type="xs:date" select="{$parliament-election-date}"></electionDate> 
                <country isA="TLCLocation">
                    <value type="xs:string"><xsl:value-of select="$country-code" /></value>
                </country>
            </legislature>
            <bungeni id="bungeniMeta" showAs="Bungeni Specific info" isA="TLCObject">
                <xsl:copy-of select="tags" />
                <xsl:copy-of select="field[@name='timestamp']" />
            </bungeni>
                        
            <!-- 
            e.g. <question> or <motion> or <tableddocument> or <bill> are Bungeni "object" concepts and not 
            really documents so we model them as TLCObject items
            -->
            
            <xsl:element name="{$content-type-element-name}" >
                
                <xsl:attribute name="isA"><xsl:text>TLCObject</xsl:text></xsl:attribute>
                
                <document href="#bungeniDocument" />
                
                <xsl:copy-of select="field[
                        @name='doc_type'
                        ] |
                        _vp_response_type " /> <!-- was question_type and response_tyep -->
                
                <!-- This is a reference to a group from the parliamentary item -->         
                <xsl:if test="group">
                   <xsl:for-each select="group">
                       
                       <group isA="TLCReference">
                           
                           <xsl:variable name="group-id" select="field[@name='group_id']" />
                           
                           <xsl:variable name="group-type" select="field[@name='type']" />
                           
                           <xsl:variable name="group-type-element-name" select="bctype:get_content_type_element_name(
                               $group-type, 
                               $type-mappings
                               )" />
                           
                           <xsl:variable name="group-type-uri-name" select="bctype:get_content_type_uri_name(
                               $group-type, 
                               $type-mappings
                               )" />
                           
                           <xsl:variable name="full-group-identifier" select="bctype:generate-group-identifier(
                               $group-type-uri-name, 
                               $for-parliament, 
                               $parliament-election-date, 
                               $parliament-id, 
                               $group-id
                               )" />
                           
                           <xsl:attribute name="href" select="bctype:generate-group-uri(
                               $group-type-uri-name, 
                               $full-group-identifier
                               )" />
                           
                       </group>
                       
                   </xsl:for-each>
                </xsl:if>
                
                <!-- for <bill> and <tableddocument> and <user> -->
                <!-- DEPRECATE
                <xsl:if test="item_assignments/* or item_assignments/text()">
                    <xsl:copy-of select="item_assignments" />
                </xsl:if>
                -->
                
                
                <!-- for <user> -->
                <!--
                <xsl:copy-of select="field[
                                            @name='first_name' or 
                                            @name='last_name' or 
                                            @name='user_id' or 
                                            @name='description' or 
                                            @name='gender' or 
                                            @name='active_p' or 
                                            @name='date_of_birth' or 
                                            @name='titles' or 
                                            @name='birth_country' or 
                                            @name='national_id' or 
                                            @name='login' or 
                                            @name='password' or 
                                            @name='salt' or 
                                            @name='email' or 
                                            @name='birth_nationality' or 
                                            @name='current_nationality' 
                                            ] |
                                       subscriptions | 
                                       user_addresses " />     -->
                
            </xsl:element>

            <legislativeItem isA="TLCConcept" >
                <!-- This has become <document> -->
            </legislativeItem>
            
            <!-- End of Legislative Item -->
            
            <xsl:copy-of select="attachments" />
           
            <xsl:copy-of select="item_signatories" />        
            
            <custom>
                <xsl:copy-of select="$type-mappings" />
            </custom>
            
           </ontology>
    </xsl:template>

</xsl:stylesheet>