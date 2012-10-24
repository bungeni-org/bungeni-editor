<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:bdates="http://www.bungeni.org/xml/dates/1.0"
    xmlns:bctypes="http://www.bungeni.org/xml/contenttypes/1.0"
    exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:import href="resources/pipeline_xslt/bungeni/common/func_dates.xsl" />
    <xsl:import href="resources/pipeline_xslt/bungeni/common/func_content_types.xsl" />
    
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Oct 17, 2011</xd:p>
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
    
    <xsl:template match="field[@name='type']">
        <type isA="TLCTerm">
            <value type="xs:string">
                <xsl:value-of select="." />
            </value>
        </type>    
    </xsl:template>     
    
    <xsl:template match="field[@name='tag']">
        <tag isA="TLCTerm">
            <value type="xs:string">
                <xsl:value-of select="." />
            </value>
        </tag>
    </xsl:template>    
    
    <xsl:template match="field[@name='active_p']">
        <status type="xs:string">
            <xsl:variable name="field_active" select="." />
            <xsl:choose >
                <xsl:when test="$field_active eq 'A'">active</xsl:when>
                <xsl:otherwise>inactive</xsl:otherwise>
            </xsl:choose>
        </status>
    </xsl:template>   
    
    <xsl:template match="field[@name='user_id']">
        <userId type="xs:integer">
            <xsl:value-of select="." />
        </userId>
    </xsl:template>     
    
    <xsl:template match="field[@name='group_id']">
        <groupId type="xs:integer">
            <xsl:value-of select="." />
        </groupId>
    </xsl:template>    
    
    <xsl:template match="field[@name='committee_id']">
        <committeeId type="xs:integer">
            <xsl:value-of select="." />
        </committeeId>
    </xsl:template>   

    <xsl:template match="field[@name='membership_id']">
        <membershipId type="xs:integer">
            <xsl:value-of select="." />
        </membershipId>
    </xsl:template>  
    
    <xsl:template match="field[@name='parliament_id']">
        <parliamentId type="xs:integer">
            <xsl:value-of select="." />
        </parliamentId>
    </xsl:template>      
    
    <xsl:template match="field[@name='parent_group_id']">
        <parentGroupId type="xs:integer">
            <xsl:value-of select="." />
        </parentGroupId>
    </xsl:template>
    
    <xsl:template match="field[@name='group_principal_id']">
        <partyId type="xs:string">
            <xsl:value-of select="."/>
        </partyId>
    </xsl:template>    
    
    <xsl:template match="field[@name='party_id']">
        <partyId type="xs:integer">
            <xsl:value-of select="."/>
        </partyId>
    </xsl:template>
    
    <xsl:template match="parent_group">
        <parentGroup>
            <xsl:apply-templates />
        </parentGroup>
    </xsl:template>
    
    <xsl:template match="field[@name='notes']">
        <notes>
            <xsl:value-of select="." />
        </notes>
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
    
    <xsl:template match="field[@name='end_date']">
        <endDate type="xs:date"><xsl:value-of select="." /></endDate>
    </xsl:template>    
    
    <xsl:template match="field[@name='election_date']">
        <electionDate type="xs:date">
            <xsl:value-of select="." />
        </electionDate>
    </xsl:template>
    
    <xsl:template match="field[@name='dissolution_date']">
        <dissolutionDate type="xs:date">
            <xsl:value-of select="." />
        </dissolutionDate>
    </xsl:template>  
    
    <xsl:template match="field[@name='dissolution_date']">
        <resultsDate type="xs:date">
            <xsl:value-of select="." />
        </resultsDate>
    </xsl:template>     
    
    <xsl:template match="field[@name='num_members']">
        <numMembers type="xs:integer">
            <xsl:value-of select="." />
        </numMembers>
    </xsl:template>    
    
    <xsl:template match="field[@name='acronym']">
        <acronym isA="TLCTerm">
            <value type="xs:string"><xsl:value-of select="." /></value>
        </acronym>
    </xsl:template>      
    
    <xsl:template match="field[@name='quorum']">
        <quorum type="xs:integer">
            <xsl:value-of select="." />
        </quorum>
    </xsl:template>
    
    <xsl:template match="field[@name='body_text']">
        <body>
            <xsl:value-of select="." />
        </body>
    </xsl:template>    
    
    <xsl:template match="field[@name='status']">
        <status isA="TLCTerm">
            <value type="xs:string">
                <xsl:value-of select="." />
            </value>
        </status>
    </xsl:template>
    
    <xsl:template match="field[@name='question_number']">
        <itemNumber type="xs:integer">
            <xsl:value-of select="." />
        </itemNumber>
    </xsl:template>
    
    <xsl:template match="field[@name='question_id']">
        <itemId>
            <xsl:value-of select="." />
        </itemId>
    </xsl:template>
    
    <xsl:template match="field[@name='registry_number']">
        <registryNumber type="xs:string">
            <xsl:value-of select="." />
        </registryNumber>
    </xsl:template>
    
    <xsl:template match="field[@name='parliamentary_item_id']">
        <legislativeItemId type="xs:integer">
            <xsl:value-of select="." />
        </legislativeItemId>
    </xsl:template>
    
    <xsl:template match="field[@name='country_code']">
        <country>
            <xsl:attribute name="code">
                <xsl:value-of select="$country-code"/>
            </xsl:attribute>
            <!-- !+FIX_THIS (ao, 24-Apr-2012) Is country name
                available set correct, else remove it altogether 
            -->
            <xsl:text>Kenya</xsl:text>
        </country>
    </xsl:template>
    
    <xsl:template match="field[@name='short_name']">
        <shortName>
            <xsl:value-of select="." />
        </shortName>
    </xsl:template>
    
    <xsl:template match="field[@name='full_name']">
        <fullName type="xs:string">
            <xsl:value-of select="." />
        </fullName>
    </xsl:template>
    
    <xsl:template match="logo_data">
        <logoData isA="TLCObject">
            <xsl:apply-templates />
        </logoData>
    </xsl:template>     
    
    <xsl:template match="field[@name='saved_file']">
        <savedFile type="xs:string">
            <xsl:value-of select="." />
        </savedFile>
    </xsl:template>      
    
    <xsl:template match="field[@name='img_hash']">
        <imageHash type="xs:string">
            <xsl:value-of select="." />
        </imageHash>
    </xsl:template>    
    
    <xsl:template match="field[@name='description']">
        <description>
            <xsl:value-of select="." />
        </description>
    </xsl:template>     
    
    <xsl:template match="field[@name='language']">
        <!-- !+RENDERED NOW as xml:lang on the legislativeItem
            <language type="xs"><xsl:value-of select="." /></language>
        -->
    </xsl:template>   
    
    <xsl:template match="field[@name='status_date']">
        <xsl:variable name="status_date" select="." />
        <statusDate type="xs:dateTime">
            <xsl:value-of select="bdates:parse-date($status_date)" />
        </statusDate>
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
    
    
    <xsl:template match="permissions">
        <permissions id="groupPermissions">
            <xsl:apply-templates />
        </permissions>
    </xsl:template>
    
    <xsl:template match="permission">
        <permission
            setting="{field[@name='setting']}"
            name="{field[@name='permission']}"
            role="{field[@name='role']}" />
    </xsl:template>
    
    <xsl:template match="versions">
        <versions>
            <xsl:apply-templates />
        </versions>
    </xsl:template>
    
    <xsl:template match="group_address[parent::group_addresses]">
        <groupAddress isA="TLCObject">
            <xsl:apply-templates />
        </groupAddress>
    </xsl:template>      
    
    <xsl:template match="members">
        <members id="groupMembers">
            <xsl:apply-templates />
        </members>
    </xsl:template>
    
    <xsl:template match="member[parent::members]">
        <member isA="TLCObject">
            <xsl:apply-templates />
        </member>
    </xsl:template>    
    
    <!-- add node() test to check if the element has children, suppress empty container nodes -->
    <xsl:template match="group_addresses">
        <xsl:if test="node()">
            <groupAddresses id="groupAddresses">
                <xsl:apply-templates />
            </groupAddresses>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="contained_groups">
        <xsl:if test="node()">
            <xsl:copy>
                <xsl:apply-templates />
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="field[@name='group_continuity']">
        <groupContinuity isA="TLCTerm">
            <xsl:attribute name="showAs" select="@displayAs"/>
            <xsl:value-of select="."/>
        </groupContinuity>
    </xsl:template> 
    
    <xsl:template match="field[@name='proportional_representation']">
        <proportionalRepresentation type="xs:string">
            <xsl:value-of select="."/>
        </proportionalRepresentation>
    </xsl:template>     
    
    <xsl:template match="field[@name='sub_type']">
        <subType isA="TLCTerm">
            <xsl:attribute name="showAs" select="@displayAs"/>
            <xsl:value-of select="."/>
        </subType>
    </xsl:template>    
    
    <xsl:template match="field[@name='address_id']">
        <addressId type="xs:integer">
            <xsl:value-of select="."/>
        </addressId>
    </xsl:template>    

    <xsl:template match="field[@name='country_id']">
        <countryId type="TLCTerm">
            <value type="xs:string">
                <xsl:value-of select="."/>                
            </value>
        </countryId>
    </xsl:template>  
    
    <xsl:template match="field[@name='city']">
        <city type="xs:string">
            <xsl:value-of select="."/>
        </city>
    </xsl:template> 
    
    <xsl:template match="field[@name='fax']">
        <fax type="xs:string">
            <xsl:value-of select="."/>
        </fax>
    </xsl:template>     
    
    <xsl:template match="field[@name='email']">
        <email type="xs:string">
            <xsl:value-of select="."/>
        </email>
    </xsl:template>      
    
    <xsl:template match="field[@name='phone']">
        <phone type="xs:string">
            <xsl:value-of select="."/>
        </phone>
    </xsl:template>      
    
    <xsl:template match="field[@name='street']">
        <street type="xs:string">
            <xsl:value-of select="."/>
        </street>
    </xsl:template>      
    
    <xsl:template match="field[@name='postal_address_type']">
        <postalAddressType isA="TLCTerm">
            <xsl:attribute name="showAs" select="@displayAs"/>
            <xsl:value-of select="."/>
        </postalAddressType>
    </xsl:template>
    
    <xsl:template match="field[@name='logical_address_type']">
        <logicalAddressType isA="TLCTerm">
            <xsl:attribute name="showAs" select="@displayAs"/>
            <xsl:value-of select="."/>
        </logicalAddressType>
    </xsl:template>      
    
    <xsl:template match="field[@name='zipcode']">
        <zipCode type="xs:integer">
            <xsl:value-of select="."/>
        </zipCode>
    </xsl:template>      
    
    <xsl:template match="field[@name='numcode']">
        <numCode type="xs:integer">
            <xsl:value-of select="." />
        </numCode>
    </xsl:template>    
    
    <xsl:template match="field[@name='membership_type']">
        <membershipType isA="TLCTerm">
            <value type="xs:string">
                <xsl:value-of select="." />                
            </value>
        </membershipType>
    </xsl:template>      
    
</xsl:stylesheet>