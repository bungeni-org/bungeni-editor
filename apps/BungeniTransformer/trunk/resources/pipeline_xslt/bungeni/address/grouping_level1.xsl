<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Jan 24, 2012</xd:p>
            <xd:p><xd:b>Author:</xd:b> Anthony</xd:p>
            <xd:p></xd:p>
        </xd:desc>
    </xd:doc>

    <!-- these are input parameters to the transformation a-->
    <!-- these are input parameters to the transformation a-->
    <xsl:param name="country-code" />
    <xsl:param name="parliament-election-date" />
    <xsl:param name="for-parliament" />
    
    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>
    
    <xsl:template match="contenttype">
        <xsl:variable name="address_id" select="field[@name='address_id']" />
        <xsl:variable name="group_id" select="field[@name='group_id']" />
        <xsl:variable name="content-type" select="@name" />
        <xsl:variable name="group-type" select="item/field[@name='type']" />
        <!-- 
            !+NOTE (ao, jan-2012)
            Take country_id as opposed to $country-code as with other documents 
        -->
        <ontology type="{$content-type}">
            <address>
                <!-- !+NOTE (ao, jan-2012) 
                    This generally takes the $group-type but its not being always set for address document
                     such as those for individuals like a clerk. This is simply stop-gap measure whilst this 
                     issue is pending. -->
                <xsl:attribute name="type" select="$content-type" />

                <!-- !+URI_GENERATOR,!+FIX_THIS(ah,nov-2011) use ontology uri
                for group since its non-document entity -->
                <!-- In relation to the !+NOTE above... -->
                <xsl:attribute name="uri">
                    <xsl:choose>
                        <xsl:when test="field[@name='group_id']">
                            <xsl:value-of select="concat(
                                         '/ontology/',
                                         'group/',
                                         $country-code,'/',
                                         $group-type,'/',
                                         $group_id
                                         )" />
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="concat(
                                         $for-parliament,'/',
                                         'user/',
                                         field[@name='user_id']
                                         )">          
                            </xsl:value-of>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute>
                <!--<xsl:attribute name="uri" 
                    select="concat(
                     '/ontology/',
                     $content-type,'/',
                     $country_id, '/',
                     $address_id
                     )" />-->

                <xsl:attribute name="id" select="$address_id" />
                
                <xsl:copy-of select="field[ @name='parent_group_id' or 
                                            @name='group_principal_id' or 
                                            @name='min_num_members' or 
                                            @name='num_researchers' or 
                                            @name='num_members' or 
                                            @name='quorum' or 
                                            @name='start_date' or 
                                            @name='election_date' or 
                                            @name='titles' or 
                                            @name='first_name' or
                                            @name='last_name' or 
                                            @name='short_name' or 
                                            @name='full_name' or                                         
                                            @name='city' or 
                                            @name='country_name' or 
                                            @name='postal_address_type' or 
                                            @name='zipcode' or 
                                            @name='logical_address_type' or 
                                            @name='phone' or 
                                            @name='street' or 
                                            @name='fax' or 
                                            @name='email' or 
                                            @name='country_id' ]"></xsl:copy-of>
            </address>
            <bungeni>
                <xsl:copy-of select="tags"/>
                <xsl:copy-of select="field[ 
                    @name='active_p' or 
                    @name='language' or
                    @name='status' or 
                    @name='iso_name' or 
                    @name='numcode' or 
                    @name='full_name' or                     
                    @name='iso3' ]" 
                />                    
                <xsl:copy-of select="permissions" />
                <group-type>
                    <xsl:value-of select="$group-type"></xsl:value-of>
                </group-type>
            </bungeni> 
            
            <!--    !+FIX_THIS (ao, jan 2012. Some address documents for individuals like clerk dont have 'type' field and 
                    this broke the pipeline processor
                    
            <xsl:element name="{$group-type}">
                <xsl:attribute name="isA">TLCOrganization</xsl:attribute>
                <xsl:attribute name="refersTo" select="concat('#', $address_id)" />
            </xsl:element>
            -->
            <!-- i.e. not yet grouped ontologically (ao, Jan 2012) -->
            <descriptors>
                <xsl:copy-of select="field[  
                    @name='description' or 
                    @name='parliament_id' or 
                    @name='type' or 
                    @name='proportional_presentation' or 
                    @name='status_date' ] | parent_group" 
                />             
            </descriptors> 
        </ontology>
    </xsl:template>
    
</xsl:stylesheet>