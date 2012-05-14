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
            <xd:p><xd:b>Created on:</xd:b> Jan 24, 2012</xd:p>
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
    
    <xsl:template match="field[@name='user_id']">
        <userId type="xs:integer">
            <xsl:value-of select="." />
        </userId>
    </xsl:template> 
    
    <xsl:template match="permissions">
        <permissions id="addressPermissions">
            <xsl:apply-templates />
        </permissions>
    </xsl:template>
    
    <xsl:template match="permission">
        <permission 
            setting="{field[@name='setting']}" 
            name="{field[@name='permission']}"  
            role="{field[@name='role']}" />
    </xsl:template>    
    
    <xsl:template match="field[@name='membership_id']">
        <membershipId type="xs:integer">
            <xsl:value-of select="." />
        </membershipId>
    </xsl:template>  
    
    <xsl:template match="field[@name='group_principal_id']">
        <groupPrincipalId isA="TLCReference">
            <value type="xs:string">
                <xsl:value-of select="." />                
            </value>
        </groupPrincipalId>
    </xsl:template>      
    
    <xsl:template match="field[@name='group_id']">
        <groupId type="xs:integer">
            <xsl:value-of select="." />
        </groupId>
    </xsl:template>    
    
    <xsl:template match="field[@name='national_id']">
        <nationalId type="xs:string">
            <xsl:value-of select="." />
        </nationalId>
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
    
    <xsl:template match="field[@name='min_num_members']">
        <minNumMembers type="xs:integer">
            <xsl:value-of select="." />
        </minNumMembers>
    </xsl:template>
    
    <xsl:template match="field[@name='num_researchers']">
        <numResearchers type="xs:integer">
            <xsl:value-of select="." />
        </numResearchers>
    </xsl:template>    
    
    <xsl:template match="field[@name='start_date']">
        <startDate type="xs:dateTime">
            <xsl:variable name="start_date" select="." />
            <xsl:value-of select="bdates:parse-date($start_date)" />
        </startDate>
    </xsl:template>    
    
    <xsl:template match="field[@name='num_members']">
        <numMembers>
            <xsl:value-of select="." />
        </numMembers>
    </xsl:template>    

    <xsl:template match="field[@name='body_text']">
        <body>
            <xsl:value-of select="." />
        </body>
    </xsl:template>

    <xsl:template match="field[@name='parliamentary_item_id']">
        <legislativeItemId>
            <xsl:value-of select="." />
        </legislativeItemId>
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
    
    <xsl:template match="field[@name='country_name']">
        <countryName type="xs:string">
            <xsl:value-of select="." />
        </countryName>
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
    
    <xsl:template match="field[@name='titles']">
        <titles type="xs:string">
            <xsl:value-of select="."/>
        </titles>
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
        <postalAddressType type="xs:string">
            <xsl:value-of select="."/>
        </postalAddressType>
    </xsl:template>
    
    <xsl:template match="field[@name='logical_address_type']">
        <logicalAddressType type="xs:string">
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
    
    <xsl:template match="field[@name='iso3']">
        <iso3 type="xs:string">
            <xsl:value-of select="." />
        </iso3>
    </xsl:template>    
    
    <xsl:template match="field[@name='iso_name']">
        <isoName type="xs:string">
            <xsl:value-of select="." />
        </isoName>
    </xsl:template>    
    
    <xsl:template match="field[@name='first_name']">
        <firstName type="xs:string">
            <xsl:value-of select="." />
        </firstName>
    </xsl:template>  
    
    <xsl:template match="field[@name='last_name']">
        <lastName type="xs:string">
            <xsl:value-of select="." />
        </lastName>
    </xsl:template>      

    <xsl:template match="field[@name='short_name']">
        <shortName type="xs:string">
            <xsl:value-of select="." />
        </shortName>
    </xsl:template>

    <xsl:template match="field[@name='full_name']">
        <fullName type="xs:string">
            <xsl:value-of select="." />
        </fullName>
    </xsl:template>
    
    <xsl:template match="field[@name='description']">
        <description><xsl:value-of select="." /></description>
    </xsl:template>  
    
    <xsl:template match="field[@name='notes']">
        <notes><xsl:value-of select="." /></notes>
    </xsl:template>     
    
    <xsl:template match="field[@name='language']">
        <!-- !+RENDERED NOW as xml:lang on the legislativeItem
            <language type="xs"><xsl:value-of select="." /></language>
        -->
    </xsl:template>   
    
    <xsl:template match="field[@name='birth_country']">
        <birthCountry type="xs:string">
            <xsl:value-of select="." />
        </birthCountry>
    </xsl:template>     
    
    <xsl:template match="field[@name='birth_nationality']">
        <birthNationality type="xs:string">
            <xsl:value-of select="."/>
        </birthNationality>
    </xsl:template>
    
    <xsl:template match="field[@name='current_nationality']">
        <currentNationality type="xs:string">
            <xsl:value-of select="."/>
        </currentNationality>
    </xsl:template>    
    
    <xsl:template match="field[@name='gender']">
        <xsl:variable name="field_gender" select="." />
        <gender type="xs:string">
            <xsl:choose >
                <xsl:when test="$field_gender eq 'M'">male</xsl:when>
                <xsl:when test="$field_gender eq 'F'">female</xsl:when>
                <xsl:otherwise>unknown</xsl:otherwise>
            </xsl:choose>
        </gender>
    </xsl:template>   
    
    <xsl:template match="field[@name='active_p']">
        <activeP type="xs:string">
            <xsl:value-of select="."/>
        </activeP>
    </xsl:template>   
    
    <xsl:template match="field[@name='date_of_birth']">
        <dateOfBirth type="xs:date">
            <xsl:value-of select="."/>
        </dateOfBirth>
    </xsl:template> 

    <xsl:template match="field[@name='office_role']">
        <officeRole isA="TLCRole">
            <value type="xs:string">
                <xsl:value-of select="." />                
            </value>
        </officeRole>
    </xsl:template>
    
    <xsl:template match="field[@name='office_id']">
        <officeId type="xs:integer">
            <xsl:value-of select="." />
        </officeId>
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
            <xsl:variable name="misc_dates" select="." />
            <xsl:attribute name="name" select="@name" />      
            <xsl:value-of select="bdates:parse-date($misc_dates)" />
        </xsl:element>
    </xsl:template>   
    
    <xsl:template match="field[@name='status']">
        <status isA="TLCTerm">
            <value type="xs:string">
                <xsl:value-of select="."/>
            </value>
        </status>
    </xsl:template>     
    
    <xsl:template match="field[@name='login']">
        <login type="xs:string">
            <xsl:value-of select="."/>
        </login>
    </xsl:template>    

    <xsl:template match="field[@name='password']">
        <password type="xs:string">
            <xsl:value-of select="."/>
        </password>
    </xsl:template>

    <xsl:template match="field[@name='salt']">
        <salt type="xs:string">
            <xsl:value-of select="."/>
        </salt>
    </xsl:template>
    
    <xsl:template match="field[@name='email']">
        <email type="xs:string">
            <xsl:value-of select="."/>
        </email>
    </xsl:template>    

    <xsl:template match="field[@name='receive_notification']">
        <receiveNotification type="xs:boolean">
            <xsl:value-of select="."/>
        </receiveNotification>
    </xsl:template>
</xsl:stylesheet>