<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
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
        <type>
            <xsl:value-of select="." />
        </type>
    </xsl:template> 
    
    <xsl:template match="field[@name='tag']">
        <tag>
            <xsl:value-of select="." />
        </tag>
    </xsl:template>    
    
    <xsl:template match="field[@name='address_id']">
        <addressId><xsl:value-of select="."/></addressId>
    </xsl:template>     
    
    <xsl:template match="field[@name='country_id']">
        <countryId><xsl:value-of select="."/></countryId>
    </xsl:template>    
    
    <xsl:template match="field[@name='user_id']">
        <userId>
            <xsl:value-of select="." />
        </userId>
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
    
    <xsl:template match="field[@name='membership_id']">
        <membershipId>
            <xsl:value-of select="." />
        </membershipId>
    </xsl:template>  
    
    <xsl:template match="field[@name='group_principal_id']">
        <groupPrincipalId>
            <xsl:value-of select="." />
        </groupPrincipalId>
    </xsl:template>      
    
    <xsl:template match="field[@name='group_id']">
        <groupId>
            <xsl:value-of select="." />
        </groupId>
    </xsl:template>    
    
    <xsl:template match="field[@name='national_id']">
        <nationalId><xsl:value-of select="." /></nationalId>
    </xsl:template>     
    
    <xsl:template match="field[@name='parliament_id']">
        <parliamentId>
            <xsl:value-of select="." />
        </parliamentId>
    </xsl:template>      
    
    <xsl:template match="field[@name='parent_group_id']">
        <parentGroupId>
            <xsl:value-of select="." />
        </parentGroupId>
    </xsl:template>
    
    <xsl:template match="parent_group">
        <parentGroup>
            <xsl:apply-templates />
        </parentGroup>
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
    
    <xsl:template match="field[@name='election_date']">
        <electionDate type="xs:date">
            <xsl:value-of select="." />
        </electionDate>
    </xsl:template>
    
    <xsl:template match="field[@name='dissolution_date']">
        <dissolutionDate>
            <xsl:value-of select="." />
        </dissolutionDate>
    </xsl:template>  
    
    <xsl:template match="field[@name='dissolution_date']">
        <resultsDate>
            <xsl:value-of select="." />
        </resultsDate>
    </xsl:template>     
    
    <xsl:template match="field[@name='num_members']">
        <numMembers>
            <xsl:value-of select="." />
        </numMembers>
    </xsl:template>    
    
    <xsl:template match="field[@name='quorum']">
        <quorum>
            <xsl:value-of select="." />
        </quorum>
    </xsl:template>

    <xsl:template match="field[@name='body_text']">
        <body>
            <xsl:value-of select="." />
        </body>
    </xsl:template>    

    <xsl:template match="field[@name='question_number']">
        <itemNumber>
            <xsl:value-of select="." />
        </itemNumber>
    </xsl:template>

    <xsl:template match="field[@name='question_id']">
        <itemId>
            <xsl:value-of select="." />
        </itemId>
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
    
    <xsl:template match="field[@name='city']">
        <city><xsl:value-of select="."/></city>
    </xsl:template> 
    
    <xsl:template match="field[@name='fax']">
        <fax><xsl:value-of select="."/></fax>
    </xsl:template>  
    
    <xsl:template match="field[@name='country_name']">
        <countryName><xsl:value-of select="." /></countryName>
    </xsl:template>     
    
    <xsl:template match="field[@name='country_code']">
        <country>
            <xsl:attribute name="code">
                <xsl:value-of><xsl:text>KE</xsl:text></xsl:value-of>
            </xsl:attribute>
            <xsl:text>Kenya</xsl:text>
        </country>
    </xsl:template>  
    
    <xsl:template match="field[@name='titles']">
        <titles><xsl:value-of select="."/></titles>
    </xsl:template>      
    
    <xsl:template match="field[@name='phone']">
        <phone><xsl:value-of select="."/></phone>
    </xsl:template>      
    
    <xsl:template match="field[@name='street']">
        <street><xsl:value-of select="."/></street>
    </xsl:template>      
    
    <xsl:template match="field[@name='postal_address_type']">
        <postalAddressType><xsl:value-of select="."/></postalAddressType>
    </xsl:template>
    
    <xsl:template match="field[@name='logical_address_type']">
        <logicalAddressType><xsl:value-of select="."/></logicalAddressType>
    </xsl:template>      
    
    <xsl:template match="field[@name='zipcode']">
        <zipCode><xsl:value-of select="."/></zipCode>
    </xsl:template>      
    
    <xsl:template match="field[@name='numcode']">
        <numCode><xsl:value-of select="." /></numCode>
    </xsl:template>
    
    <xsl:template match="field[@name='iso3']">
        <iso3><xsl:value-of select="." /></iso3>
    </xsl:template>    
    
    <xsl:template match="field[@name='iso_name']">
        <isoName><xsl:value-of select="." /></isoName>
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
        <shortName><xsl:value-of select="." /></shortName>
    </xsl:template>

    <xsl:template match="field[@name='full_name']">
        <fullName><xsl:value-of select="." /></fullName>
    </xsl:template>
    
    <xsl:template match="field[@name='description']">
        <description><xsl:value-of select="." /></description>
    </xsl:template>  
    
    <xsl:template match="field[@name='notes']">
        <notes><xsl:value-of select="." /></notes>
    </xsl:template>     
    
    <xsl:template match="field[@name='language']">
        <language><xsl:value-of select="." /></language>
    </xsl:template>   
    
    <xsl:template match="field[@name='birth_country']">
        <birthCountry><xsl:value-of select="." /></birthCountry>
    </xsl:template>     
    
    <xsl:template match="field[@name='birth_nationality']">
        <birthNationality><xsl:value-of select="."/></birthNationality>
    </xsl:template>
    
    <xsl:template match="field[@name='current_nationality']">
        <currentNationality><xsl:value-of select="."/></currentNationality>
    </xsl:template>    
    
    <xsl:template match="field[@name='gender']">
        <gender><xsl:value-of select="."/></gender>
    </xsl:template>   
    
    <xsl:template match="field[@name='active_p']">
        <activeP><xsl:value-of select="."/></activeP>
    </xsl:template>   
    
    <xsl:template match="field[@name='date_of_birth']">
        <dateOfBirth><xsl:value-of select="."/></dateOfBirth>
    </xsl:template>   
    
    <xsl:template match="field[@name='membership_type']">
        <membershipType><xsl:value-of select="." /></membershipType>
    </xsl:template>     

    <xsl:template match="field[@name='office_role']">
        <officeRole><xsl:value-of select="." /></officeRole>
    </xsl:template>
    
    <xsl:template match="field[@name='office_id']">
        <officeId><xsl:value-of select="." /></officeId>
    </xsl:template>      

    <xsl:template match="field[@name='status_date']">
        <xsl:variable name="status_date" select="." />
        <xsl:variable name="arrStatusDate" select="tokenize($status_date,'\s+')" />
        <statusDate type="xs:dateTime"><xsl:value-of select="concat($arrStatusDate[1],'T',$arrStatusDate[2])" /></statusDate>
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
    
    <xsl:template match="field[@name='status']">
        <status><xsl:value-of select="."/></status>
    </xsl:template>     
    
    <xsl:template match="field[@name='login']">
        <login><xsl:value-of select="."/></login>
    </xsl:template>    

    <xsl:template match="field[@name='password']">
        <password><xsl:value-of select="."/></password>
    </xsl:template>

    <xsl:template match="field[@name='salt']">
        <salt><xsl:value-of select="."/></salt>
    </xsl:template>
    
    <xsl:template match="field[@name='email']">
        <email><xsl:value-of select="."/></email>
    </xsl:template>    

    <xsl:template match="field[@name='receive_notification']">
        <receiveNotification><xsl:value-of select="."/></receiveNotification>
    </xsl:template>
</xsl:stylesheet>