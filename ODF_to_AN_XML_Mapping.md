

# Introduction #

**=**THIS PAGE IS OUT OF DATE - THIS IS MANAGED INTERNALLY VIA CONFIGURATION. See SectionTypeDefinition document !**=**

This page describes how metadata is represented in Bungeni Editor ODF documents and how that is mapped to Akoma Ntoso

Bungeni Editor uses a couple of different mechanisms to record metadata in a ODF document.

Document level metadata is recorded in the `office:document-meta` element as user defined (`meta:user-defined`).

Inline and container level metadata is recorded in the  RDF namespace (`rdf:RDF`) using a custom namespace for Bungeni metadata.

# Document Level Metadata #

Document level metadata can be found in `./meta.xml` within the ODF package.

Bungeni Editor writes document level metadata as ODF user-defined properties :
```
     <meta:user-defined meta:name="BungeniCountryCode">ke</meta:user-defined>
        <meta:user-defined meta:name="BungeniDocAuthor"/>
        <meta:user-defined meta:name="BungeniDocPart">main</meta:user-defined>
    ...........................
    ...........................
```



# Content Metadata #

## Section level metadata ##

Akoma Ntoso defines different Element containers. These containers are of course not supported in ODF. We represent AN element containers in ODF using the `text:section` element.

For e.g. -- the following is representative markup in ODF of a `speech` in a debaterecord (`content.xml`):

```
<text:section text:style-name="Sect10" xml:id="id81842116" text:name="speech1">
    <text:p text:style-name="Standard">
        <text:meta xml:id="id394891649">Mr. Francis Acheka: </text:meta>
    </text:p>
    <text:section text:style-name="Sect9" xml:id="id1636983406" text:name="spbody1">
        <text:p text:style-name="Standard">Mr. Speaker, Sir, I beg to reply.</text:p>
        <text:p text:style-name="Standard">
            <text:meta xml:id="id415166567">(a)</text:meta>
            <text:s/>
            <text:span text:style-name="an-list-item">The Arabia Airstrip is of gravel surface, and
                that is how it was intended to be. At no time has any bitumen work ever been
                undertaken in 1978 and stopped, as the question implies. The airstrip was last
                graded in June, 1988.</text:span>
        </text:p>
        <text:p text:style-name="Standard">
            <text:meta xml:id="id590467019">(b)</text:meta>
            <text:s/>
            <text:span text:style-name="an-list-item">According to the Ministry’s current
                Development Plan there is no provision to bituminize Arabia Airstrip.</text:span>
        </text:p>
    </text:section>
</text:section>
```

This speech section has ODF metadata ; the RDF metadata for the section is recorded in a metadata RDF file ; the link to the metadata RDF file is made via the `manifest.rdf` file in the ODF package --

```
<?xml version="1.0" encoding="utf-8"?>
<rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <ns1:anx xmlns:ns1="http://editor.bungeni.org/1.0/" rdf:about="meta/meta.rdf">
    <rdf:type rdf:resource="http://docs.oasis-open.org/ns/office/1.2/meta/pkg#MetadataFile"/>
  </ns1:anx>
  <ns2:Document xmlns:ns2="http://docs.oasis-open.org/ns/office/1.2/meta/pkg#" rdf:about="">
    <ns2:hasPart rdf:resource="content.xml"/>
    <ns2:hasPart rdf:resource="meta/meta.rdf"/>
    <ns2:hasPart rdf:resource="styles.xml"/>
  </ns2:Document>
  <ns3:StylesFile xmlns:ns3="http://docs.oasis-open.org/ns/office/1.2/meta/odf#" rdf:about="styles.xml"/>
  <ns4:ContentFile xmlns:ns4="http://docs.oasis-open.org/ns/office/1.2/meta/odf#" rdf:about="content.xml"/>
</rdf:RDF>
```

In the case of the Bungeni Editor the RDF metadata is located in `meta/meta.rdf`, and uses a custom namespace, in our case `http://editor.bungeni.org/1.0/` namespace :

```
 <ns1:anx xmlns:ns1="http://editor.bungeni.org/1.0/" rdf:about="meta/meta.rdf">
```

The speech section is connected to RDF metadata by the `xml:id` attribute of the speech section (i.e. the section is the "subject" of the RDF triple )  :

```
<?xml version="1.0" encoding="utf-8"?>
<rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
.....
 <rdf:Description rdf:about="../content.xml#id81842116">
    <ns10:BungeniPersonID xmlns:ns10="http://editor.bungeni.org/1.0/anx/">4</ns10:BungeniPersonID>
    <ns11:BungeniPersonRole xmlns:ns11="http://editor.bungeni.org/1.0/anx/">4</ns11:BungeniPersonRole>
    <ns12:BungeniSectionID xmlns:ns12="http://editor.bungeni.org/1.0/anx/">Yw6x+ZPjTV2m2YjkwG//tQ</ns12:BungeniSectionID>
    <ns13:BungeniSectionType xmlns:ns13="http://editor.bungeni.org/1.0/anx/">Speech</ns13:BungeniSectionType>
    <ns14:BungeniSpeechAs xmlns:ns14="http://editor.bungeni.org/1.0/anx/">MemberOfParliament</ns14:BungeniSpeechAs>
    <ns15:BungeniSpeechAsDesc xmlns:ns15="http://editor.bungeni.org/1.0/anx/">Member of Parliament</ns15:BungeniSpeechAsDesc>
    <ns16:BungeniSpeechAsURI xmlns:ns16="http://editor.bungeni.org/1.0/anx/">/ontology/roles/ke/member</ns16:BungeniSpeechAsURI>
    <ns17:BungeniSpeechBy xmlns:ns17="http://editor.bungeni.org/1.0/anx/">Francis Acheka</ns17:BungeniSpeechBy>
    <ns18:BungeniSpeechByURI xmlns:ns18="http://editor.bungeni.org/1.0/anx/">ken.person.Francis.Acheka.1951-12-10</ns18:BungeniSpeechByURI>
    <ns19:hiddenBungeniMetaEditable xmlns:ns19="http://editor.bungeni.org/1.0/anx/">true</ns19:hiddenBungeniMetaEditable>
    <ns20:hiddenBungeniMetaEditor xmlns:ns20="http://editor.bungeni.org/1.0/anx/">toolbarSubAction.makeDebateSpeechBlockSection.section_creation</ns20:hiddenBungeniMetaEditor>
  </rdf:Description>
 ......
</rdf:RDF>
```

This maps to the RDF triple --

|Subject | Predicate | Object |
|:-------|:----------|:-------|
| URL to section : "../content.xml#id81842116" | BungeniPersonID | 4 |

## Inline metadata ##

Inline markup is done using inline metadata e.g. the `<from>` part of the speech is marked up as follows using `text:meta` :

```
<text:p text:style-name="Standard">
    <text:meta xml:id="id394891649">Mr. Francis Acheka: </text:meta>
</text:p>
```

This gets represented in RDF as :
```
  <rdf:Description rdf:about="../content.xml#id394891649">
    <ns9:InlineType xmlns:ns9="http://editor.bungeni.org/1.0/anx/">from</ns9:InlineType>
  </rdf:Description>
```

This maps to the RDF triple --

|Subject | Predicate | Object |
|:-------|:----------|:-------|
| URL to inline meta : "../content.xml#id394891649" | InlineType | from |

## ODF to AN xml metadata mappings ##

### Document level metadata mappings ###

The Mapping for this is provided below --


|ODF Meta Name | Example ODF Meta Value | AN XML Representation |
|:-------------|:-----------------------|:----------------------|
|BungeniCountryCode|ke|  part of FRBR URIs|
|BungeniDocAuthor|  | ` FRBRManifestation/FRBRauthor ` |
|BungeniDocPart|main| ` FRBRWork/components/component[@name="main"] ` |
|BungeniDocType|debaterecord| ` <debateRecord> , <debate> `  |
|BungeniExpAuthor|user.Ashok|  ` FRBRExpression/FRBRauthor/@as `|
|BungeniExpAuthorURI|user.Ashok| ` FRBRExpression/FRBRauthor/@href ` |
|BungeniExpDate|2011-02-25| ` FRBRExpression/FRBRdate/@date `|
|BungeniExpDateName|expDate|` FRBRExpression/FRBRdate/@name `|
|BungeniExpURI| ` /ke/debaterecord/2011-2-26/eng@ `| ` FRBRExpression/FRBRuri/@value `|
|BungeniLanguageCode|en| ` FRBRExpression/FRBRlanguage/@language ` |
|BungeniManAuthor|user.Ashok| `FRBRManifestation/FRBRauthor/@as ` |
|BungeniManAuthorURI|user.Ashok| `FRBRManifestation/FRBRauthor/@href ` |
|BungeniManDate|2011-02-25| `FRBRManifestation/FRBRdate/@date ` |
|BungeniManDateName|manDate| `FRBRManifestation/FRBRdate/@name ` |
|BungeniManURI|/ke/debaterecord/2011-2-26/en.xml| `FRBRManifestation/FRBuri/@value ` |
|BungeniOfficialDate|2011-02-26| ??? |
|BungeniOfficialTime|17:01| ??? |
|BungeniParliamentID|2 | ??? |
|BungeniParliamentSession|3 | ???? |
|BungeniParliamentSitting|4 |  ???? |
|BungeniPublicationDate|2011-02-25| ` meta/publication/@date ` |
|BungeniPublicationName|debaterecord| ` meta/publication/@name ` |
|BungeniPublicationNameFull|  | ` meta/publication/@showAs `  |
|BungeniWorkAuthor|user.Ashok| ` FRBRWork/FRBRauthor/@showAs ` |
|BungeniWorkAuthorURI|user.Ashok| {{FRBRWork/FRBRauthor/@href }}} |
|BungeniWorkDate|2011-02-26| ` FRBRWork/FRBRdate/@date `|
|BungeniWorkDateName|workDate| ` FRBRWork/FRBRdate/@name `   |
|BungeniWorkURI|/ke/debaterecord/2011-2-26| ` FRBRwork/FRBRuri/@value ` |



### Content metadata mappings ###

#### Container Element Identification ####

The `anx:BungeniSectionType` metadata predicate is used to identify a container element.

For e.g. the following specifies a the `anx:BungeniSectionType` for a `text:section` as `Question` --

```
  <rdf:Description rdf:about="../content.xml#id151718213">
    <ns23:BungeniSectionID xmlns:ns23="http://editor.bungeni.org/1.0/anx/">a3kXAao7Qau3BdZwckaRBQ</ns23:BungeniSectionID>
    <ns24:BungeniSectionType xmlns:ns24="http://editor.bungeni.org/1.0/anx/">Question</ns24:BungeniSectionType>
    <ns25:hiddenBungeniMetaEditable xmlns:ns25="http://editor.bungeni.org/1.0/anx/">false</ns25:hiddenBungeniMetaEditable>
  </rdf:Description>
```

The following table specifies the mapping between sectionType and Akoma Ntoso element --

|ODF Section Type| AN XML Element |
|:---------------|:---------------|
|Preface|preface|
|Prayers`*`|prayers|
|Speech|speech|
|SpeechBody|All content after `<from>` in a `speech`|
|Question|question|
|QuestionBody|All content after `<from>` in a `question`|
|Narrative|narrative|
|MinisterialStatement|ministerialStatements|
|PersonalStatement|personalStatements|
|QuestionsContainer|questions|
|PMotionsContainer|proceduralMotions|
|NMotionsContainer|noticesOfMotion|
|PointOfOrder|PointOfOrder|
|Communication|Communication|
|PapersLaid|papersLaid|
|Observation|observation|

_TODO -- add missing_

#### `<speech>` element ####

##### In Akoma Ntoso #####

This is the speech element --

```

<meta>
.....
<references ...>
    ...
	<TLCPerson id="FrancisAcheka" href="/ontology/person/ken/Francis.Acheka.1951-12-10" showAs="Francis Acheka" />
    <TLCRole id="MemberOfParliament" href="/ontology/roles/ken/mp" showAs="Member of Parliament"/>
    ...
</references>            
.....
</meta>
....
<speech by="#FrancisAcheka" as="#MemberOfParliament">
    <from>Mr Francis Acheka:</from>
    <p>Mr. Speaker, Sir, I beg to reply.</p>
    <list id="lst-speech-1">
        <item id="it1-lst-speech-1">
        <num>(a)</num>
        <p>
            The Arabia Airstrip is of gravel surface, and
            that is how it was intended to be. At no time has any bitumen work ever been
            undertaken in 1978 and stopped, as the question implies. The airstrip was last
            graded in June, 1988.
        </p>
        </item>
        <item id="it2-lst-speech-1">
        <num>(b)</num>
        <p>
            According to the Ministry’s current
            Development Plan there is no provision to bituminize Arabia Airstrip.
        </p>
        </item>
    </list>

</speech>

```


##### In ODF #####

The content markup:

See the ODF markup for the speech in [Section Level Metadata](#Section_level_metadata.md)

The RDF metadata :

```
<?xml version="1.0" encoding="utf-8"?>
<rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
.....
 <rdf:Description rdf:about="../content.xml#id81842116">
   ......
    <ns17:BungeniSpeechBy xmlns:ns17="http://editor.bungeni.org/1.0/anx/">Francis Acheka</ns17:BungeniSpeechBy>
    <ns18:BungeniSpeechByURI xmlns:ns18="http://editor.bungeni.org/1.0/anx/">/ontology/person/ken/Francis.Acheka.1951-12-10</ns18:BungeniSpeechByURI>
    <ns14:BungeniSpeechAs xmlns:ns14="http://editor.bungeni.org/1.0/anx/">MemberOfParliament</ns14:BungeniSpeechAs>
    <ns15:BungeniSpeechAsDesc xmlns:ns15="http://editor.bungeni.org/1.0/anx/">Member of Parliament</ns15:BungeniSpeechAsDesc>
    <ns16:BungeniSpeechAsURI xmlns:ns16="http://editor.bungeni.org/1.0/anx/">/ontology/roles/ken/mp</ns16:BungeniSpeechAsURI>
  ......
  </rdf:Description>
 ......
</rdf:RDF>
```

So, the mapping from ODF to Akoma Ntoso happens as follows --

|BungeniPersonID|TLCPerson/@id|
|:--------------|:------------|
|BungeniSpeechBy|TLCPerson/@showAs|
|BungeniSpeechByURI|TLCPerson/@href|
|BungeniSpeechAs|TLCRole/@id|
|BungeniSpeechAsDesc|TLCRole/@showAs|
|BungeniSpeechAsURI|TLCRole/@href|

#### `<from>` element ####

##### In Akoma Ntoso #####
```
<from>Mr. Francis Acheka:</from>
```

##### In ODF #####

In the content marked up as :

```
<text:p text:style-name="Standard">
    <text:meta xml:id="id394891649">The Assistant Minister for Transport and Communications (Dr.
        Momanyi): </text:meta>
</text:p>
```

In the rdf as :

```
  <rdf:Description rdf:about="../content.xml#id394891649">
    <ns9:InlineType xmlns:ns9="http://editor.bungeni.org/1.0/anx/">from</ns9:InlineType>
  </rdf:Description>
```


#### `<num>` element ####

##### In Akoma Ntoso #####
```
<num>(a)</num>
```

##### In ODF #####

In the content marked up as :

```
<text:p text:style-name="Standard">
    <text:meta xml:id="id415166567">(a)</text:meta>
</text:p>
```

In the RDF with 'InlineType' predicate :

```
  <rdf:Description rdf:about="../content.xml#id415166567">
    <ns9:InlineType xmlns:ns9="http://editor.bungeni.org/1.0/anx/">num</ns9:InlineType>
  </rdf:Description>
```



#### listitem element ####

The listitem is identified by style markup --
```
 <text:span text:style-name="an-list-item"> item text </text:span>
```