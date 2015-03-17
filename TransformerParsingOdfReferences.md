# Introduction #
The Bungeni Editor uses ODF text-references for inline markup.
These need to be selectively transformed into elements based on their naming convention.
An example of how this transformation is done is provided below -- further variations are possible using the same method.

# Details #

Meta header generation from embedded metadata.
This metadata appears in ODF metadata header in the following key-value form :

"BungeniPartyName:p1", "p1;Fourway Haulage ltd" - this is parsed using the XSLT 2.0 tokenize() function.

```

                <xsl:for-each select="//meta:user-defined[starts-with(@meta:name, 'BungeniPartyName')]">
                  	<xsl:variable name="strHref"><xsl:value-of select="." /></xsl:variable>
				    <xsl:variable name="tokenizedHref" select="tokenize($strHref,'~')"/>
					<meta name="TLCPerson"  id="{$tokenizedHref[1]}" href="{generate-id()}" showAs="{$tokenizedHref[2]}" /> 
                </xsl:for-each> 
```

Inline reference parsing :

All reference parsing happens in ref.xsl, to parse your specific refernece

We again use the XSLT 2.0 tokenize() function but this time on the reference href.
```
	<xsl:when test="@class='BungeniPartyName'">
						<party>
						<!-- we use the tokenize() function to extract the refersTo attrib 
						from the reference href the id is a generated one -->
						<xsl:variable name="strHref"><xsl:value-of select="@href" /></xsl:variable>
						<xsl:variable name="tokenizedHref" select="tokenize($strHref,';')"/>
						<xsl:attribute name="refersTo">
						   <xsl:value-of select="$tokenizedHref[1]" />
						</xsl:attribute>
						<xsl:attribute name="id">
						   <xsl:value-of select="generate-id()" />
						</xsl:attribute>
						 <xsl:value-of select="."/>
						</party>
```