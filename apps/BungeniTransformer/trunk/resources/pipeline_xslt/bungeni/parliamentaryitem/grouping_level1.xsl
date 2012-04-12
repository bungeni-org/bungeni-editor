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
    
    <!-- these are input parameters to the transformation a-->
    <xsl:param name="country-code"  />
    <xsl:param name="parliament-id" />
    <xsl:param name="parliament-election-date"  />
    <xsl:param name="for-parliament" />
    <xsl:param name="type-mappings" />
    
    
    <!--
    <xsl:variable name="country-code" select="string('ke')" />
    <xsl:variable name="parliament-election-date" select="string('2011-03-02')" />
    <xsl:variable name="for-parliament" select="concat('/ke/parliament/', $parliament-election-date)" />
    -->
    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="contenttype">
  
        <xsl:variable name="bungeni-content-type" select="field[@name='type']" />
        
        <xsl:variable name="content-type-element-name">
            <xsl:variable name="tmp-content-type" select="$type-mappings//map[@from=$bungeni-content-type]/@element-name" />
            <xsl:choose>
                <xsl:when test="string-length($tmp-content-type) eq 0">
                    <xsl:value-of select="$bungeni-content-type" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$tmp-content-type" />
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
      
        <xsl:variable name="content-type-uri-name">
            <xsl:variable name="tmp-content-type" select="$type-mappings//map[@from=$bungeni-content-type]/@uri-name" />
            <xsl:choose>
                <xsl:when test="string-length($tmp-content-type) eq 0">
                    <xsl:value-of select="$bungeni-content-type" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$tmp-content-type" />
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        
      
        <xsl:variable name="language" select="field[@name='language']" />
        <ontology type="document" isA="TLCObject">
            <document>
                <xsl:attribute name="type" select="$content-type-uri-name" />
            </document>
            <bungeni isA="TLCObject">
                <xsl:attribute name="id" select="$parliament-id"/>
                <country><xsl:value-of select="$country-code" /></country>
                <parliament href="{$for-parliament}" 
                    isA="TLCOrganization" 
                    date="{$parliament-election-date}" />
                <xsl:copy-of select="tags"/>
                
                <!-- for <heading> -->
                <xsl:copy-of select="field[@name='timestamp'] | event_items" />
            </bungeni>
            
            <!-- e.g. <question> or <motion> or <tableddocument> or <bill> -->
            <xsl:element name="{$content-type-element-name}">
                <xsl:attribute name="isA" select="string('TLCConcept')" />
                <xsl:attribute name="COUNT" select="count($type-mappings/value)" />
                <xsl:copy-of select="field[
                        @name='question_type' or 
                        @name='response_type']" />
       
                
                <xsl:if test="field[@name='ministry_id']">
                  <!-- render only if ministry or other group ,rendered as generic group reference -->
                   <!--!+URI_REWORK(ah, mar-2012) the group is a reference here and not the group definition
                       so we say that explictly via a isA qualifier -->
                  <group isA="TLCReference">
                      <xsl:variable name="ministry_id" select="field[@name='ministry_id']" />
                      <xsl:attribute name="isA" select="string('ministry')" />
                      <xsl:variable name="full-group-identifier" select="concat('group.',$for-parliament,'-',$parliament-election-date,'-',$parliament-id, '.ministry.',$ministry_id  )" />
                      <xsl:attribute name="href" select="concat('/ontology/ministry/',$full-group-identifier)" />
                      <!-- !+FIX_THIS figure out what to do with this -->
                      <!-- <xsl:copy-of select="field[@name='ministry_submit_date']"></xsl:copy-of> -->
                  </group> 
                </xsl:if>
                
                <!-- for <bill> and <tableddocument> and <user> -->
                <xsl:if test="item_assignments/* or item_assignments/text()">
                    <!-- copy only if the element is not empty -->
                    <xsl:copy-of select="item_assignments" />
                </xsl:if>
                
                
                
                <!-- for <user> -->
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
                                       user_addresses " />     
                
            </xsl:element>

            <legislativeItem isA="TLCConcept">
                
                <!-- this is available only after a certain stage of the workflow -->
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

                <!-- !+URI_GENERATOR,!+FIX_THIS(ah,nov-2011) this logic needs to be eventually
                factored out -->
                <xsl:attribute name="uri" 
                    select="concat(
                        '/', $country-code,'/', 
                        $content-type-uri-name,'/', 
                        $item_number,'/', 
                        $language
                    )" />
                
                <xsl:copy-of select="field[
                                     @name='status_date' or 
                                     @name='registry_number' or 
                                     @name='parliamentary_item_id'
                                     ] | 
                                     changes |
                                     events |
                                     versions |
                                     owner" />
                
                
                <!-- for <event> -->
                <xsl:copy-of select="head" />    
                
                <xsl:copy-of select="field[
                    @name='status' or 
                    @name='short_name' or 
                    @name='full_name' or 
                    @name='body_text' or 
                    @name='body' or 
                    @name='language'  or 
                    @name='owner_id' or
                    @name='type'
                    ]" />
                
                <!-- NUMBER AND IDENTIFIERS -->
                
                <xsl:copy-of select="field[
                    @name='question_number' or
                    @name='question_id'
                    ]" />                  
                
                <!-- for <tableddocument> -->
                <xsl:copy-of select="field[
                    @name='tabled_document_id' or 
                    @name='tabled_document_number' 
                    ]" />    
                
                <!-- for <bill> -->
                <xsl:copy-of select="field[
                    @name='bill_id' or 
                    @name='bill_type_id' or 
                    @name='bill_number'
                    ]" />
                
                <!-- for <motion> -->
                <xsl:copy-of select="field[
                    @name='motion_id' or 
                    @name='motion_number'
                    ]" />
                
                <!-- for <motion> & <bill> -->
                <xsl:copy-of select="field[
                    @name='publication_date' or
                    @name='doc_type' 
                    ]" />  
                
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
                
                <!-- for <question> -->
                <xsl:if test="ministry/*">
                    <assignedTo group="ministry">
                        <!-- copy only if the element is not empty -->
                        <xsl:copy-of select="ministry" />
                    </assignedTo>
                </xsl:if>                 
                
            </legislativeItem>
            
            <!-- End of Legislative Item -->
            
            <xsl:copy-of select="attached_files" />
            
            <!-- for <motion> & <bill> & <heading> -->
            <xsl:copy-of select="itemsignatories" />        
            
           </ontology>
    </xsl:template>

</xsl:stylesheet>