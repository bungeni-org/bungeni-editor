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
            <xd:p><xd:b>Created on:</xd:b> Oct 26, 2011</xd:p>
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
    
    <xsl:template match="field[@name='active_p']">
        <status>
            <xsl:variable name="field_active" select="." />
            <xsl:choose >
                <xsl:when test="$field_active eq 'A'">active</xsl:when>
                <xsl:otherwise>inactive</xsl:otherwise>
            </xsl:choose>
        </status>
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
    
    <xsl:template match="field[@name='user_id']">
        <userId type="xs:integer">
            <xsl:value-of select="." />
        </userId>
    </xsl:template>    
    
    <xsl:template match="field[@name='description']">
        <description>
            <xsl:value-of select="." />
        </description>
    </xsl:template>   
    
    <xsl:template match="field[@name='gender']">
        <xsl:variable name="field_gender" select="." />
        <!--gender>
            <xsl:choose >
                <xsl:when test="$field_gender eq 'M'">male</xsl:when>
                <xsl:when test="$field_gender eq 'F'">female</xsl:when>
                <xsl:otherwise>unknown</xsl:otherwise>
            </xsl:choose>
        </gender-->
        
        <gender isA="TLCTerm" vdex="org.bungeni.metadata.vocabularies.gender" >
            <xsl:value-of select="." />
        </gender>
        
    </xsl:template>
    
    <xsl:template match="field[@name='date_of_birth']">
        <dateOfBirth type="xs:date">
            <xsl:value-of select="." />
        </dateOfBirth>
    </xsl:template>  
    
    <xsl:template match="field[@name='title']">
        <title type="xs:string">
            <xsl:value-of select="." />
        </title>
    </xsl:template>  
    
    <xsl:template match="field[@name='salutation']">
        <salutation type="xs:string">
            <xsl:value-of select="." />
        </salutation>
    </xsl:template>         
    
    <xsl:template match="field[@name='birth_country']">
        <birthCountry type="xs:string">
            <xsl:value-of select="." />
        </birthCountry>
    </xsl:template>
    
    <xsl:template match="field[@name='national_id']">
        <nationalId type="xs:string">
            <xsl:value-of select="." />
        </nationalId>
    </xsl:template>   
    
    <xsl:template match="field[@name='login']">
        <login type="xs:string">
            <xsl:value-of select="." />
        </login>
    </xsl:template>    
    
    <xsl:template match="field[@name='password']">
        <password type="xs:string">
            <xsl:value-of select="." />
        </password>
    </xsl:template> 
    
    <xsl:template match="field[@name='salt']">
        <salt type="xs:string">
            <xsl:value-of select="." />
        </salt>
    </xsl:template>    
    
    <xsl:template match="field[@name='email']">
        <email type="xs:string">
            <xsl:value-of select="." />
        </email>
    </xsl:template>  
    
    <xsl:template match="field[@name='birth_nationality']">
        <birthNationality type="xs:string">
            <xsl:value-of select="." />
        </birthNationality>
    </xsl:template>    
    
    <xsl:template match="field[@name='current_nationality']">
        <currentNationality type="xs:string">
            <xsl:value-of select="." />
        </currentNationality>
    </xsl:template>      
    
    <xsl:template match="image">
        <image isA="TLCObject">
            <xsl:apply-templates />
        </image>
    </xsl:template>       
    
    <xsl:template match="field[@name='saved_file']">
        <savedFile type="xs:string">
            <xsl:value-of select="." />
        </savedFile>
    </xsl:template>  
    
    <xsl:template match="field[@name='img_uuid']">
        <imageUuid type="xs:string">
            <xsl:value-of select="." />
        </imageUuid>
    </xsl:template>
    
    <xsl:template match="user_addresses">
        <userAddresses>
            <xsl:apply-templates />
        </userAddresses>
    </xsl:template>    
    
    <xsl:template match="user_address">
        <userAddress isA="TLCObject">
            <xsl:apply-templates />
        </userAddress>
    </xsl:template>   
    
    <xsl:template match="field[@name='status']">
        <addressStatus type="xs:string">
            <xsl:value-of select="." />
        </addressStatus>
    </xsl:template>    
    
    <xsl:template match="field[@name='city']">
        <city type="xs:string">
            <xsl:value-of select="."/>
        </city>
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
    
    <xsl:template match="field[@name='fax']">
        <fax type="xs:string">
            <xsl:value-of select="."/>
        </fax>
    </xsl:template>      
    
    <xsl:template match="field[@name='zipcode']">
        <zipCode type="xs:integer">
            <xsl:value-of select="."/>
        </zipCode>
    </xsl:template>
    
    <xsl:template match="field[@name='marital_status']">
        
        <maritalStatus isA="TLCTerm" vdex="org.bungeni.metadata.vocabularies.marital_status">
            <xsl:value-of select="." />
        </maritalStatus>
        
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
    
</xsl:stylesheet>