<?xml version="1.0" encoding="UTF-8"?><xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:bp="http://www.bungeni.org/pipeline/1.0" xmlns:bungeni="http://editor.bungeni.org/1.0/anx/" xmlns:ml="http://www.metalex.org/1.0" xmlns:bodf="http://editor.bungeni.org/1.0/odf/" exclude-result-prefixes="bp" version="2.0">
    <xsl:output indent="yes" method="xml"/>

    <!--

    Default templates :
    These templates are required

    -->

    <xsl:template match="/">

        
        
    <xsl:apply-templates/></xsl:template>

    <xsl:template match="*">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="text()">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template>


    <xsl:template match="bungeni:*">
      <!--We dont want to output the bungeni namespace metadata -->
    </xsl:template>

    <!--

    PIPELINE TEMPLATES FOLLOW

    -->

    
    <xsl:template bp:name="root" match="*[@name='root']">
        <akomaNtoso>
            <act>
                <xsl:apply-templates select="//*[@name='meta']"/>
                <xsl:apply-templates select="//*[@name='Preface']"/>
                <xsl:apply-templates select="//*[@name='Preamble']"/>
                <!-- !+PIPELINE(ah, feb-2012), added explicit matcher for body
                otherwise meta gets matched twice -->
                <xsl:apply-templates select="//*[@name='body']"/>
                <xsl:apply-templates select="//*[@name='Conclusion']"/>
            </act>
        </akomaNtoso>    
    </xsl:template>
    
    
    <xsl:template bp:name="meta" match="*[@name='meta']">
        <meta>
            <xsl:apply-templates/>
        </meta>
    </xsl:template>
    
    <xsl:template bp:name="identification" match="*[@name='identification']">
        <identification>
            <xsl:if test="@source">
                    <xsl:attribute name="source"><xsl:value-of select="@source"/></xsl:attribute>
            </xsl:if>

            <xsl:apply-templates/>
        </identification>
    </xsl:template>
    
    <xsl:template bp:name="FRBRWork" match="*[@name='FRBRWork']">
        <FRBRWork>
            <xsl:apply-templates/>
        </FRBRWork>
    </xsl:template>
    
    <xsl:template bp:name="FRBRExpression" match="*[@name='FRBRExpression']">
        <FRBRExpression>
 
            <xsl:apply-templates/>
        </FRBRExpression>
    </xsl:template>
    
    <xsl:template bp:name="FRBRManifestation" match="*[@name='FRBRManifestation']">
        <FRBRManifestation>
            <xsl:apply-templates/>
        </FRBRManifestation>
    </xsl:template>
    
    <xsl:template bp:name="this" match="*[@name='this']">
        <FRBRthis>
            <xsl:if test="@value">
                    <xsl:attribute name="value"><xsl:value-of select="@value"/></xsl:attribute>
            </xsl:if>

            <xsl:apply-templates/>
        </FRBRthis>
    </xsl:template>
    
    <xsl:template bp:name="uri" match="*[@name='uri']">
        <FRBRuri>
            <xsl:if test="@value">
                    <xsl:attribute name="value"><xsl:value-of select="@value"/></xsl:attribute>
            </xsl:if>

            <xsl:apply-templates/>
        </FRBRuri>
    </xsl:template>
    
    <xsl:template bp:name="date" match="*[@name='date']">
        <FRBRdate>
            <xsl:if test="@date">
                    <xsl:attribute name="date"><xsl:value-of select="@date"/></xsl:attribute>
            </xsl:if>
            <xsl:attribute name="name"><xsl:value-of select="@contentName"/></xsl:attribute>

            <xsl:apply-templates/>
        </FRBRdate>
    </xsl:template>
    
    <xsl:template bp:name="author" match="*[@name='author']">
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
    
    <xsl:template bp:name="publication_mcontainer" match="*[@name='publication_mcontainer']">
            <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template bp:name="publication" match="*[@name='publication']">
        <publication>
                <xsl:if test="@date">
                        <xsl:attribute name="date"><xsl:value-of select="@date"/></xsl:attribute>
                </xsl:if>
	        <xsl:attribute name="name"><xsl:value-of select="@contentName"/></xsl:attribute>
		    <xsl:attribute name="showAs"><xsl:value-of select="@showAs"/></xsl:attribute>
		    <xsl:apply-templates/>
        </publication>
    </xsl:template>
    
    <xsl:template bp:name="references" match="*[@name='references']">
        <references>
            <xsl:if test="@source">
                    <xsl:attribute name="source"><xsl:value-of select="@source"/></xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </references>
    </xsl:template>
    
    <xsl:template bp:name="TLCOrganization" match="*[@name='TLCOrganization']">
        <TLCOrganization>
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
        </TLCOrganization>
    </xsl:template>
    
    <xsl:template bp:name="TLCPerson" match="*[@name='TLCPerson']">
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
    
    <xsl:template bp:name="TLCEvent" match="*[@name='TLCEvent']">
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
    
    <xsl:template bp:name="TLCRole" match="*[@name='TLCRole']">
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
    
    <xsl:template bp:name="TLCReference" match="*[@name='TLCReference']">
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
    
    <xsl:template bp:name="body" match="*[@name='body']">
        <body>
            <xsl:if test="@id">
                    <xsl:attribute name="bodf:sourceId" select="@id"/>
            </xsl:if>
            
             <xsl:apply-templates select="*[@name != 'Preface' and @name != 'Preamble' and @name != 'Conclusion' and @name != 'meta']"/>
        </body>
    </xsl:template>
    
    <xsl:template bp:name="Preface" match="*[@name='Preface']">
        <preface>
            <xsl:attribute name="bodf:sourceId" select="@id"/>
            <xsl:apply-templates/>
        </preface>
    </xsl:template>
    
    <xsl:template match="*[@name='docTitle']" bp:name="docTitle">
        <docTitle>
            <xsl:if test="@id">
                    <xsl:attribute name="id"><xsl:value-of select="@id" /></xsl:attribute>
            </xsl:if>
            <xsl:apply-templates />
        </docTitle>
    </xsl:template>
    
     <xsl:template match="*[@name='docNumber']" bp:name="docNumber">
        <docNumber>
            <xsl:if test="@id">
                    <xsl:attribute name="id"><xsl:value-of select="@id" /></xsl:attribute>
            </xsl:if>
            <xsl:apply-templates />
        </docNumber>
    </xsl:template>
    
    <xsl:template match="*[@name='docDate']" bp:name="docDate">
        <docDate>
            <xsl:if test="@id">
                    <xsl:attribute name="id"><xsl:value-of select="@id" /></xsl:attribute>
            </xsl:if>
            <xsl:apply-templates />
        </docDate>
    </xsl:template>
    
     <xsl:template match="*[@name='docType']" bp:name="docType">
        <docType>
            <xsl:if test="@id">
                    <xsl:attribute name="id"><xsl:value-of select="@id" /></xsl:attribute>
            </xsl:if>
            <xsl:apply-templates />
        </docType>
    </xsl:template>
    
     <xsl:template bp:name="Preamble" match="*[@name='Preamble']">
        <preamble>
            <xsl:attribute name="bodf:sourceId" select="@id"/>
            <xsl:apply-templates/>
        </preamble>
    </xsl:template>
    
     <xsl:template match="*[@name='Book']" bp:name="Book">
        <book>
            <xsl:if test="@id">
                    <xsl:attribute name="id"><xsl:value-of select="@id" /></xsl:attribute>
            </xsl:if>
            <xsl:apply-templates />
        </book>
    </xsl:template>
    
    <xsl:template match="*[@name='Part']" bp:name="Part">
        <part>
            <xsl:if test="@id">
                    <xsl:attribute name="id"><xsl:value-of select="@id" /></xsl:attribute>
            </xsl:if>
            <xsl:apply-templates />
        </part>
    </xsl:template>
    
    <xsl:template match="*[@name='Chapter']" bp:name="Chapter">
        <chapter>
			<xsl:if test="@id">
				<xsl:attribute name="id"><xsl:value-of select="@id" /></xsl:attribute>
			</xsl:if>

            <xsl:apply-templates />
        </chapter>
    </xsl:template>
    
     <xsl:template match="*[@name='Section']" bp:name="Section">
        <section>
			<xsl:if test="@id">
				<xsl:attribute name="id"><xsl:value-of select="@id" /></xsl:attribute>
			</xsl:if>

            <xsl:apply-templates />
        </section>
    </xsl:template>
    
    <xsl:template match="*[@name='Article']"  bp:name="Article">
        <article>
            <xsl:if test="@id">
                    <xsl:attribute name="id"><xsl:value-of select="@id" /></xsl:attribute>
            </xsl:if>
            <xsl:apply-templates />
        </article>
    </xsl:template>
    
    <xsl:template match="*[@name='num']" bp:name="num">
        <num>
            <xsl:apply-templates />
        </num>
    </xsl:template>
    
    <xsl:template match="*[@name='heading']" bp:name="heading">
        <heading>
            <xsl:apply-templates />
        </heading>
    </xsl:template>
    
     <xsl:template match="*[@name='content']" bp:name="content">
        <content>
            <xsl:apply-templates />
        </content>
    </xsl:template>
    
    <xsl:template bp:name="Speech" match="*[@name='Speech']">
        <speech>
            <xsl:if test="@id">
                <xsl:attribute name="id">
                    <xsl:value-of select="@id"/>
                </xsl:attribute>
                <xsl:attribute name="bodf:sourceId" select="@id"/>
            </xsl:if>
            <xsl:if test="./bungeni:bungenimeta/bungeni:BungeniSpeechBy">
                <xsl:attribute name="by">
                    <xsl:text>#p</xsl:text>
                    <xsl:value-of select="./bungeni:bungenimeta/bungeni:BungeniPersonID"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="./bungeni:bungenimeta/bungeni:BungeniSpeechTo">
                <xsl:attribute name="to">
                    <xsl:value-of select="./bungeni:bungenimeta/bungeni:BungeniSpeechTo"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="./bungeni:bungenimeta/bungeni:BungeniSpeechAs">
                <xsl:attribute name="as">
                     <xsl:text>#</xsl:text>
                    <xsl:value-of select="./bungeni:bungenimeta/bungeni:BungeniSpeechAs"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </speech>
    </xsl:template>


    <xsl:template match="*[@name='from']">
        <from><xsl:apply-templates /></from>
    </xsl:template>

    <xsl:template bp:name="PersonalStatement" match="*[@name='PersonalStatement']">
        <subdivision>
            <xsl:if test="@id">
                    <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
                    <xsl:attribute name="bodf:sourceId" select="@id"/>
            </xsl:if>
            <!--<xsl:if test="@name">
                    <xsl:attribute name="name"><xsl:value-of select="@name" /></xsl:attribute>
            </xsl:if> -->

            <xsl:apply-templates/>
        </subdivision>
    </xsl:template>
    
    <xsl:template bp:name="MinisterialStatement" match="*[@name='MinisterialStatement']">
        <subdivision>
            <xsl:if test="@id">
                    <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
                    <xsl:attribute name="bodf:sourceId" select="@id"/>
            </xsl:if>
            <!--<xsl:if test="@name">
                    <xsl:attribute name="name"><xsl:value-of select="@name" /></xsl:attribute>
            </xsl:if> -->

            <xsl:apply-templates/>
        </subdivision>
    </xsl:template>
   
    
    <xsl:template bp:name="Person" match="*[@name='Person']">
        <!-- is this ever used ? the 'from' element is rendered
        from the 'ref' template, this isnt used anymore
        <from>
           <xsl:apply-templates />
        </from> -->
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template bp:name="ActionEvent" match="*[@name='ActionEvent']">
        <subdivision>
            <xsl:if test="@id">
                <xsl:attribute name="id">
                    <xsl:value-of select="@id"/>
                </xsl:attribute>
                <xsl:attribute name="bodf:sourceId" select="@id"/>
            </xsl:if>
            <xsl:if test="@name">
                <xsl:attribute name="name">
                    <xsl:value-of select="./bungeni:bungenimeta/bungeni:BungeniOntologyName"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </subdivision>
    </xsl:template>
    
    <xsl:template bp:name="Communication" match="*[@name='Communication']">
        <communication>
            <xsl:if test="@id">
                    <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
                    <xsl:attribute name="bodf:sourceId" select="@id"/>
            </xsl:if>

            <xsl:apply-templates/>
        </communication>
    </xsl:template>
    
    <xsl:template bp:name="Conclusion" match="*[@name='Conclusion']">
        <conclusions>
            <xsl:if test="@id">
                    <xsl:attribute name="id" select="@id"/>
                    <xsl:attribute name="bodf:sourceId" select="@id"/>
            </xsl:if>

            <xsl:apply-templates/>
        </conclusions>
    </xsl:template>
    
    <xsl:template bp:name="span" match="*[@name='span']">
        <span>
            <xsl:apply-templates/>
        </span>
    </xsl:template>
    
    <xsl:template bp:name="p" match="*[@name='p']">
        <!-- when the descendant is a ref within a paragraph, its usually a <from>,
        if its a from we dont decoreate the template with a paragraph 'p' element -->
        <xsl:choose>
            <xsl:when test="descendant::*[@name='ref' and @class='BungeniSpeechBy']">
                <xsl:apply-templates/>    
            </xsl:when>
            <xsl:otherwise>
                <p>
                    <xsl:apply-templates/>
                </p>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template bp:name="ref" match="*[@name='ref']">
		<xsl:if test="@href">
			<xsl:if test="@class">
				<xsl:choose>
					<!--check if speechBy attribute exists then generate a 'from' -->
					<xsl:when test="@class='BungeniSpeechBy'">
						<from>
							<xsl:value-of select="."/>
						</from>
					</xsl:when>
					<!--sBillRef : section Bill Ref -->
					<xsl:when test="@class='sBillRef'">
						<entity xsl:exclude-result-prefixes="#all">
							<!-- eg. BungeniBill.1;#1 -->
							<xsl:variable name="strHref"><xsl:value-of select="@href"/></xsl:variable>
							<xsl:variable name="tokenizedHref" select="tokenize($strHref,';')"/>
							<!-- "BungeniBill.1" -->
							<xsl:variable name="refersToBillSectionMeta" select="$tokenizedHref[1]"/>
							<!-- now do a select of the bill meta from the section meta , -->
							<!-- BillSectionMeta will have Finance Bill;/ke/bill/2008-02-03/12;/ontology/Expression/ke.bill.2008-02-03.12 now -->
							<xsl:variable name="BillSectionMeta" select="ancestor::ml:container/attribute::node()[name() = $refersToBillSectionMeta][1]"/> 
							<!-- Now tokenize the section metadata -->
							<xsl:variable name="tokenizedBillSectionMeta" select="tokenize($BillSectionMeta,';')"/>
							<xsl:variable name="BillUri" select="$tokenizedBillSectionMeta[2]"/> 
							<xsl:variable name="BillOntology" select="$tokenizedBillSectionMeta[3]"/> 
							<xsl:attribute name="id">
								<xsl:value-of select="generate-id(node())"/>
							</xsl:attribute>
							<xsl:attribute name="refersTo">
								 <xsl:text>#</xsl:text>
								 <xsl:value-of select="translate($BillUri, '/', '')"/>
							</xsl:attribute>
							<ref>
							  	<xsl:attribute name="href">
							  		<xsl:value-of select="$BillUri"/>
							  	</xsl:attribute>
							    <xsl:value-of select="."/>
							</ref>
						</entity>
					</xsl:when>
                                        <!-- just output the text contained by house closing time
                                            the markup needs to be fixed to set more information about house closing -->
                                        <xsl:when test="@class='BungeniHouseClosingTime'">
							<xsl:value-of select="."/>
					</xsl:when>
				<!-- otherwise generate normal reference -->
					<xsl:otherwise>
					     <ref>
					       <xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
					     </ref>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
		</xsl:if>
		<!-- <xsl:apply-templates/> -->
	</xsl:template>
    
    <xsl:template bp:name="heading" match="*[@name='heading']">
        <heading>
            <xsl:apply-templates/>
        </heading>
    </xsl:template>
    
    <xsl:template bp:name="subheading" match="*[@name='subheading']">
        <subheading>
            <xsl:apply-templates/>
        </subheading>
    </xsl:template>
    
    <xsl:template bp:name="list" match="*[@name='list']">
        <list>
            <xsl:if test="@id">
                    <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
            </xsl:if>

            <xsl:apply-templates/>
        </list>
    </xsl:template>
    
    <xsl:template bp:name="item" match="*[@name='item']">
        <item>
            <xsl:if test="@id">
                    <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
                    <xsl:attribute name="bodf:sourceId" select="@id"/>
            </xsl:if>

            <xsl:apply-templates/>
        </item>
    </xsl:template>
    
    <xsl:template bp:name="a" match="*[@name='a']">
        <a>
            <xsl:if test="@id">
                    <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
            </xsl:if>
            <xsl:if test="@href">
                    <xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
            </xsl:if>

            <xsl:apply-templates/>
        </a>
    </xsl:template>
    
    
    <!--

    PIPELINE TEMPLATES END 

    -->

    
</xsl:stylesheet>