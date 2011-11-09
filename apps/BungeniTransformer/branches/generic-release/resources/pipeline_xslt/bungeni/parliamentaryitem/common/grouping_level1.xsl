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
    
    <!-- these are input parameters to the transformation -->
    
    <xsl:variable name="country-code" select="string('ke')" />
    <xsl:variable name="parliament-election-date" select="string('2011-03-02')" />
    <xsl:variable name="for-parliament" select="concat('/ke/parliament/', $parliament-election-date)" />
    
    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="contenttype">
        <xsl:variable name="content-type" select="field[@name='type']" />
        <xsl:variable name="language" select="field[@name='language']" />
        <ontology type="document">
            <document>
                <xsl:attribute name="type" select="$content-type" />
               
            </document>
            <bungeni>
               <country><xsl:value-of select="$country-code" /></country>
                <parliament href="{$for-parliament}" 
                    isA="TLCOrganization" 
                    date="{$parliament-election-date}" />
            </bungeni>
            
            <!-- e.g. <question> or <motion> or <tableddocument> or <bill> -->
            <xsl:element name="{$content-type}">
                <xsl:attribute name="isA" select="string('TLCConcept')" />
                
                <xsl:copy-of select="question_type | response_type" />
                
                <!-- for <bill> -->
                <xsl:copy-of select="field[
                                            @name='bill_id' or 
                                            @name='bill_type_id'
                                            ]" />
                <xsl:if test="field[@name='ministry_id']">
                  <!-- render only if ministry or other group ,rendered as generic group reference -->
                  <group>
                      <xsl:variable name="ministry_id" select="field[@name='ministry_id']" />
                      <xsl:attribute name="isA" select="string('ministry')" />
                      <xsl:attribute name="href" select="concat($for-parliament, '/group/', $ministry_id)" />
                      <!-- !+FIX_THIS figure out what to do with this -->
                      <!-- <xsl:copy-of select="field[@name='ministry_submit_date']"></xsl:copy-of> -->
                  </group> 
                </xsl:if>
                
                <!-- for <bill> and <tableddocument> and <user> -->
                <xsl:if test="item_assignments/* or item_assignments/text()">
                    <!-- copy only if the element is not empty -->
                    <xsl:copy-of select="item_assignments" />
                </xsl:if>
                
                <!-- for <tableddocument> -->
                <xsl:copy-of select="field[
                                        @name='tabled_document_id' or 
                                        @name='tabled_document_number' 
                                       ]" />                
                
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
                                            @name='current_nationality' or 
                                            @name='tabled_document_number' 
                                            ] |
                                       subscriptions | 
                                       user_addresses " />     
                
            </xsl:element>

            <legislativeItem isA="TLCConcept">
                
                <!-- this is available only after a certain stage of the workflow -->
                <xsl:variable name="item_number">
                        <xsl:choose>
                            <!--+FIX_THIS is event_item_id the stable identifier for events ? -->
                            <xsl:when test="field[@name='event_item_id']" >
                               <xsl:value-of select="field[@name='event_item_id']" />
                            </xsl:when>
                            
                            <xsl:otherwise>
                                <xsl:value-of select="field[@name='registry_number']" />
                            </xsl:otherwise>
                        </xsl:choose>
                 
                 </xsl:variable>
                
                <xsl:attribute name="uri" 
                    select="concat($for-parliament, '/', 
                    $content-type, '/', 
                    $item_number, '/', 
                    $language)" />
                
                <xsl:copy-of select="field[
                                     @name='status_date' or 
                                     @name='registry_number' or 
                                     @name='parliamentary_item_id'
                                     ] | 
                                     changes | 
                                     events |
                                     versions" />
                
                <xsl:copy-of select="field[
                    @name='status' or 
                    @name='short_name' or 
                    @name='full_name' or 
                    @name='body_text' or 
                    @name='language' 
                    ]" />
                
                <xsl:copy-of select="field[
                    @name='question_number' or
                    @name='question_id' or 
                    @name='owner_id'
                    ]" />                  
                
                <!-- for <motion> & <bill> -->
                <xsl:copy-of select="field[@name='publication_date']" />  
                
                <!-- for <motion> or <bill> -->
                <xsl:copy-of select="owner" />                
                
            </legislativeItem>
            
            <xsl:copy-of select="permissions | 
                                 attached_files" />
            
            <!-- for <motion> & <bill> -->
            <xsl:copy-of select="itemsignatories" />
         
            <xsl:copy-of select="ministry" />
            
           </ontology>
    </xsl:template>

</xsl:stylesheet>