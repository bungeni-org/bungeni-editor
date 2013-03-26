<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:bungeni="http://editor.bungeni.org/1.0/anx/"
    xmlns:xmeta="http://meta.w3.org/1999/XSL/Transform"
    version="2.0">
 
    <!--
        These are static templates - not auto generated but these are injected into the main stylesheet 
        for full-render
        
        NOTE: This template is not called directly - but is instead injected into the type_transform_XXXXX.xsl 
        auto generated XSLT
    -->

    <!--

    <mcontainer name="meta">
    <mcontainer name="identification">
        <mcontainer name="all">
            <meta name="BungeniDocType" value=""/>
            <meta name="BungeniLanguageCode" value="en"/>
            <meta name="BungeniCountryCode" value="ke"/>
            <meta name="BungeniParliamentID" value="33333"/>
        
    -->

    <xsl:template  match="*[@name='identification']">
        <identification>
            <xsl:if test="@source">
                <xsl:attribute name="source"><xsl:value-of select="@source"/></xsl:attribute>
            </xsl:if>
            
            <xsl:apply-templates/>
        </identification>
    </xsl:template>
    
    <xsl:template match="*[@name='frbrwork']">
        <!--
            This is what the template intends to match : 
            <mcontainer name="frbrwork">
            <meta name="uri" value="/ke/debaterecord/2012-10-27"/>
            <meta name="author" value="user.Ashok"/>
            <meta name="date" value="2012-10-27"/>
            <meta name="date_name" value="workDate"/>
            </mcontainer>
        -->
        <FRBRWork>
            <xsl:apply-templates select="./*[@name='uri']" mode="meta_gen" />
            <xsl:call-template name="frbrauthor_generator" />
            <xsl:call-template name="frbrdate_generator" />
        </FRBRWork>
    </xsl:template>
    
    
    <xsl:template match="*[@name='frbrexpression']">
        <FRBRExpression>
            <xsl:apply-templates select="./*[@name='uri']" mode="meta_gen" />
            <xsl:call-template name="frbrauthor_generator" />
            <xsl:call-template name="frbrdate_generator" />
        </FRBRExpression>
    </xsl:template>
    
    
    
    <xsl:template match="*[@name='frbrmanifestation']">
        <FRBRManifestation>
            <xsl:apply-templates select="./*[@name='uri']" mode="meta_gen" />
            <xsl:call-template name="frbrauthor_generator" />
            <xsl:call-template name="frbrdate_generator" />
        </FRBRManifestation>
    </xsl:template>
    
    
    <xsl:template match="*[@name='uri']" mode="meta_gen">
        <FRBRthis value="{@value}/main" />
        <FRBRuri value="{@value}" />
    </xsl:template>
    
     <xsl:template name="frbrauthor_generator">
        <FRBRauthor href="#{./*[@name='author']/@value}" as="#{./*[@name='as']/@value}" /> 
    </xsl:template>
    
    <xsl:template name="frbrdate_generator">
        <FRBRdate date="{./*[@name='date']/@value}" name="{./*[@name='date_name']/@value}" /> 
    </xsl:template>
    
    <xsl:template match="*[@name='frbrthis']">
        <FRBRthis>
            <xsl:if test="@value">
                <xsl:attribute name="value"><xsl:value-of select="@value"/></xsl:attribute>
            </xsl:if>
            
            <xsl:apply-templates/>
        </FRBRthis>
    </xsl:template>
    
    <xsl:template match="*[@name='frbruri']">
        <FRBRuri>
            <xsl:if test="@value">
                <xsl:attribute name="value"><xsl:value-of select="@value"/></xsl:attribute>
            </xsl:if>
            
            <xsl:apply-templates/>
        </FRBRuri>
    </xsl:template>
    
    <xsl:template match="*[@name='frbrdate']">
        <FRBRdate>
            <xsl:if test="@date">
                <xsl:attribute name="date"><xsl:value-of select="@date"/></xsl:attribute>
            </xsl:if>
            <xsl:attribute name="name"><xsl:value-of select="@contentName"/></xsl:attribute>
            
            <xsl:apply-templates/>
        </FRBRdate>
    </xsl:template>
    
    <xsl:template  match="*[@name='frbrauthor']">
        <FRBRauthor>
            <xsl:if test="@href">
                <xsl:attribute name="href">
                    <xsl:value-of select="@href"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="@as">
                <xsl:attribute name="as">
                    <xsl:value-of select="@as"/>
                </xsl:attribute>
            </xsl:if>
            
            <xsl:apply-templates/>
        </FRBRauthor>
    </xsl:template>


    <!--
          <mcontainer name="publication">
            <meta name="name" value="ddeddd"/>
            <meta name="date" value="2012-10-17"/>
            <meta name="number" value="33"/>
         </mcontainer>
    -->
<!--    <xsl:template  match="*[@name='publication']">
        <publication>
            <xsl:if test="./*[@name='date']">
                <xsl:attribute name="date"><xsl:value-of select="./*[@name='date']/@value"/></xsl:attribute>
            </xsl:if>
            <xsl:attribute name="name"><xsl:value-of select="./*[@name='name']/@value" /></xsl:attribute>
            <xsl:attribute name="number"><xsl:value-of select="./*[@name='number']/@value"/></xsl:attribute>
            <xsl:apply-templates/>
        </publication>
    </xsl:template>-->
    
    <xsl:template  match="*[@name='references']">
        <references>
            <xsl:if test="@source">
                <xsl:attribute name="source"><xsl:value-of select="@source"/></xsl:attribute>
            </xsl:if>
            <!-- we use copyof instead of applytemplates because the children are rendered 
                natively as akomaNtoso -->
            <xsl:copy-of select="child::*" />
        </references>
    </xsl:template>
    
    <xsl:template match="*[@name='proprietary']">
      <xsl:copy-of select="child::*" />
    </xsl:template>
  

    
<!--    <mcontainer name="TLCOrganization">
            <meta name="id" value="pna"/>
            <meta name="href" value="/ontology/organization/ps/palestinianNationalAuthority"/>
            <meta name="showAs" value="Palestinian National Authority"/>
    </mcontainer>-->
    
<!--     <mcontainer name="TLCOrganization">
            <meta name="id" value="IoL"/>
            <meta name="href" value="/ontology/organization/ps/IoL"/>
            <meta name="showAs" value="Institute of Law"/>
    </mcontainer>-->
    
         
<!--    <xsl:template match="*[@name='TLCOrganization']">
        <TLCOrganization>
            <xsl:if test="./*[@name='id']/@value">
                <xsl:attribute name="id"><xsl:value-of select="./*[@name='id']/@value"/></xsl:attribute>
           </xsl:if>
            <xsl:if test="./*[@name='href']/@value">
                <xsl:attribute name="href"><xsl:value-of select="./*[@name='href']/@value"/></xsl:attribute>
            </xsl:if>
            <xsl:if test="./*[@name='showAs']/@value">
                <xsl:attribute name="showAs"><xsl:value-of select="./*[@name='showAs']/@value"/></xsl:attribute>
            </xsl:if>
            
            <xsl:apply-templates/>
        </TLCOrganization>
    </xsl:template>-->

    <!--
    <xsl:template match="*[@name='TLCPerson']">
        <TLCPerson>
            <xsl:if test="@id">
                <xsl:attribute name="id"><xsl:text>p</xsl:text><xsl:value-of select="@id"/></xsl:attribute>
            </xsl:if>
            <xsl:if test="@href">
                <xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
            </xsl:if>
            <xsl:if test="@showAs">
                <xsl:attribute name="showAs"><xsl:value-of select="@showAs"/></xsl:attribute>
            </xsl:if>
            
            <xsl:apply-templates/>
        </TLCPerson>
    </xsl:template>
    -->
    <xsl:template match="*[@name='TLCEvent']">
        <TLCEvent>
            <xsl:if test="@id">
                <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
            </xsl:if>
            <xsl:if test="@href">
                <xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
            </xsl:if>
            <xsl:if test="@showAs">
                <xsl:attribute name="showAs"><xsl:value-of select="@showAs"/></xsl:attribute>
            </xsl:if>
            
            <xsl:apply-templates/>
        </TLCEvent>
    </xsl:template>

    <!--
    <xsl:template  match="*[@name='TLCRole']">
        <TLCRole>
            <xsl:if test="@id">
                <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
            </xsl:if>
            <xsl:if test="@href">
                <xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
            </xsl:if>
            <xsl:if test="@showAs">
                <xsl:attribute name="showAs"><xsl:value-of select="@showAs"/></xsl:attribute>
            </xsl:if>
            
            <xsl:apply-templates/>
        </TLCRole>
    </xsl:template>
    -->
    
    <xsl:template match="*[@name='TLCReference']">
        <TLCReference>
            <xsl:if test="@id">
                <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
            </xsl:if>
            <xsl:if test="@href">
                <xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
            </xsl:if>
            <xsl:if test="@showAs">
                <xsl:attribute name="showAs"><xsl:value-of select="@showAs"/></xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </TLCReference>
    </xsl:template>
    
    
    <!-- ODF document static template matchers -->
    
    <xsl:template match="*[@name='span']">
        <span><xsl:apply-templates/></span>
    </xsl:template>
    
    <xsl:template match="*[@name='p']">
        <p><xsl:apply-templates/></p>
    </xsl:template>
    
    <xsl:template match="*[local-name() eq 'table']">
        <table>
            <xsl:if test="@id">
                <xsl:attribute name="id" select="@id" />
            </xsl:if>
            <xsl:apply-templates />
        </table>
    </xsl:template>
    
    <xsl:template match="*[local-name() eq 'tr']">
        <tr>
            <xsl:if test="@id">
                <xsl:attribute name="id" select="@id" />
            </xsl:if>
            <xsl:apply-templates />
        </tr>
    </xsl:template>
    
    <xsl:template match="*[local-name() eq 'td']">
        <td>
            <xsl:if test="@id">
                <xsl:attribute name="id" select="@id" />
            </xsl:if>
            <xsl:apply-templates />
        </td>
    </xsl:template>
    <!-- Prevents emitting metadata content, since metadata is
        "pulled" inline into the content during transformation -->
    <xsl:template match="bungeni:bungenimeta">
    </xsl:template>
    
    
</xsl:stylesheet>