<!-- 

Does a contextual Display of the search results context xml

--><xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:an="http://www.akomantoso.org/1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs an" version="2.0"><xsl:output method="html"/>
    <!-- global variable to capture matched speech node depth --><xsl:variable name="speech-node-depth">
         <!-- this is usually of the form 'speech[32]' --><xsl:value-of select="/context/position-in-parent"/></xsl:variable><xsl:variable name="speech-id"><xsl:value-of select="/context/matching-id"/></xsl:variable>
    
    <!-- global template --><xsl:template match="/"><div class="contextresults"><xsl:apply-templates/></div></xsl:template><xsl:template match="position-in-parent">
        <!-- do nothing, we dont want to output this--></xsl:template><xsl:template match="matching-id">
		<!--do nothing --></xsl:template><xsl:template match="preceding"><div class="preceding"><xsl:apply-templates/></div></xsl:template><xsl:template match="parent"><div class="parent"><xsl:apply-templates/></div></xsl:template><xsl:template match="following"><div class="following"><xsl:apply-templates/></div></xsl:template><xsl:template match="subdivision"><xsl:apply-templates/></xsl:template><xsl:template match="heading">
            <!-- when the parent is a subdivision use a h2 for a heading
                otherwise use h1 --><xsl:choose><xsl:when test="current()/parent::subdivision"><h2><xsl:value-of select="."/></h2></xsl:when><xsl:otherwise><h1><xsl:value-of select="."/></h1></xsl:otherwise></xsl:choose></xsl:template><xsl:template match="question"><div class="question"><xsl:apply-templates/></div></xsl:template><xsl:template match="answer"><div class="answer"><xsl:apply-templates/></div></xsl:template><xsl:template match="scene"><div class="scene"><xsl:apply-templates/></div></xsl:template><xsl:template match="speech">
        <!-- get the number of preceding speeches --><xsl:variable name="speech-preceding-count"><xsl:value-of select="count(preceding-sibling::speech)"/></xsl:variable>
        <!-- create a string of the form speech[1] using the preceding speech count --><xsl:variable name="speech-index">speech[<xsl:value-of select="$speech-preceding-count+1"/>]</xsl:variable>
        <!-- check if the contextual speech node is the current one ... if yes 
        use a different class for the speech --><xsl:choose>
            <!-- if the ancestor of the speech is a <parent> element then it may contain the current context speech --><xsl:when test="../ancestor::parent"><xsl:element name="div"><xsl:choose>
                        <!-- when the speech index equals the speech node depth variable it is the contextual speech --><xsl:when test="$speech-index eq $speech-node-depth"><xsl:attribute name="class">speech highlight</xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="class">speech parent</xsl:attribute></xsl:otherwise></xsl:choose><xsl:if test="$speech-index eq $speech-node-depth"><a name="{$speech-id}"/></xsl:if><xsl:apply-templates/></xsl:element></xsl:when><xsl:otherwise><div class="speech"><xsl:apply-templates/></div></xsl:otherwise></xsl:choose></xsl:template><xsl:template match="ul | li"><xsl:copy-of select="."/></xsl:template><xsl:template match="p"><p><xsl:apply-templates/></p></xsl:template><xsl:template match="noteRef">
        <!-- do nothing --></xsl:template><xsl:template match="from"><em><xsl:text>From: </xsl:text><xsl:value-of select="."/></em></xsl:template></xsl:stylesheet><!-- 
    Sample XML translated by this XSLT
    
    <?xml version="1.0" encoding="UTF-8"?>
    <context>
    <position-in-parent>speech[1]</position-in-parent>
    <preceding>
    <heading>ORAL ANSWERS TO QUESTIONS</heading>
    </preceding>
    <parent>
    <subdivision name="question" id="qaa1-que1">
    <heading>BANDITRY MENACE IN GALOLE</heading>
    
    <question by="kofa" to="ms">
    <from>Mr. Kofa</from>
    <p>Mr. Kofa asked the Minister of State, Office of the President</p>
    <ul id="qaa1-que1-ul1">
    <li>whether he is aware that Councillor Abdi Sole of Asako Location was
    seriously injured and two homeguards were shot dead and their guns
    stolen while pursuing bandits on 20th May, 2005;</li>
    <li>who instructed these people to pursue the heavily armed bandits;
    and,</li>
    <li>if the Ministry can ensure that the families of the deceased
    homeguards are compensated and that Councillor Sole's medical
    expenses are paid in full.</li>
    
    </ul>
    </question>
    <speech by="smith" as="speaker">
    <from>Mr. Speaker</from>
    <p>
    <noteRef num="2" href="#not02"/>Anybody from the Office of the President?</p>
    </speech>
    <answer by="manga" as="ams">
    
    <from>Mr. Manga</from>
    <p> Mr. Speaker, Sir, the answer is being prepared. The Minister is coming
    to give the answer.</p>
    </answer>
    </subdivision>
    </parent>
    <following>
    <subdivision name="question" id="qaa1-que2">
    
    <heading>REVIVAL OF E.A. COMMUNITY</heading>
    <question by="makonyango" to="mfa">
    <from>Mr. Mak'Onyango</from>
    <p>Mr. Mak'Onyango asked the Minister for Foreign Affairs and International
    Co-operation</p>
    <ul id="qaa1-que2-ul1">
    <li>what bottlenecks are delaying the revival of the East African
    Community as mapped out in the Arusha Accord of the 1st December,
    1994, between the Heads of State of Kenya, Uganda and Tanzania; and,</li>
    <li>to what extent is Kenya, as a country, to blame for the delay.</li>
    
    </ul>
    </question>
    <answer by="manduku" as="amfa">
    <from>Mr. Manduku</from>
    <p> Mr. Speaker, Sir, I beg to request the House to allow us to answer this
    Question on Thursday, 2nd of November, 2005.</p>
    <p>Thank you.</p>
    </answer>
    
    <speech by="smith" as="speaker">
    <from>Mr. Speaker</from>
    <p>Mr. Mak'Onyango, are you objecting?</p>
    </speech>
    <speech by="makonyango">
    <from>Mr. Mak'Onyango</from>
    <p> Yes, Mr. Speaker, Sir, I will object very strongly because this Question
    was due here last week, and I was told that the Question be pushed to
    this week. How long are we going to be pushed before we get an
    answer?</p>
    
    </speech>
    <speech by="smith" as="speaker">
    <from>Mr. Speaker</from>
    <p> Finally, to Thursday; no more. </p>
    </speech>
    <scene>Question deferred</scene>
    </subdivision>
    
    </following>
    </context>


-->