

# Introduction #

The Bungeni Transformer uses XSLT to convert a ODT document to AkomaNtoso.
Structural markup & metadata is transformed to akomantoso elements and attributes.

# Architecture #

The Bungeni Transformer does multiple transformations to convert the ODF document into AkomaNtoso.

The primary system components involved in the transformation process are as follows :

  * ODFDOM - this is the open source ODF processing library provided by Sun
  * Saxon - this is the open source XSLT 2.0 transformer written by Michael Kay
  * Xerces - this is the DOM processor
  * Restlet - this is used to provide a REST based API layer on the transformer. Client integration of the transformer is done over http using the restlet http client API.

# How the Transformer Works #

## General Overview ##

The transformation from ODF to AkomaNtoso happens over multiple transformations. The different stages are bulleted below

  1. ODF merging - the ODF schema separates content / presentation and metadata. These are stored in 3 separate files content.xml / styles.xml and meta.xml. In the ODF merging stage the 3 files are merged into one document so that the ODF document can be transformed as one unit.

  1. ODF transformation to meta language - In this stage the ODF document is transformed into an intermediate format based on MetaLEX 1.0. This is done because MetaLEX  provides generic containers and metadata representation support which makes it easier transformation to a higher level format (like AkomaNtoso).

  1. Meta language to Akomantoso - The metalex converted document is now converted to AkomaNtoso using a XSLT "pipeline". The "pipeline" (example [here](http://code.google.com/p/bungeni-editor/source/browse/apps/BungeniTransformer/trunk/resources/odttoakn/minixslt/debaterecord/pipeline.xsl)  ) is simply a xslt template with template-matchers for specific elements, the template-matcher routes the template processing to a mini-xslt file (example [MiniXSLT for tabledDocuments](http://code.google.com/p/bungeni-editor/source/browse/apps/BungeniTransformer/trunk/resources/odttoakn/minixslt/debaterecord/TabledDocuments.xsl) ). This makes it possible to customization transformation for specific elements in isolation.


## Interface to Translator ##

The translator provides a direct API interface (via standard Java API) and a REST API via an inbuilt REST server. The REST server interface is provided because the transformer imposes some class library requirements which may not be compatible with the caller application if the standard Java API were to be used. With the REST api -- the only integration requirement is for a REST / HTTP client.

## The Input Document ##

The input document is a marked up ODT document. The documents are
marked up using the Bungeni Editor. Markup appears in the ODF in the following form ; The following is the ODF source for a marked up speech. The speech is marked out as a named section. Metadata is associated via the style-properties of the section :

```
                    <text:section name="speech4" style-name="Sect12">
                        <text:p style-name="Standard"><text:reference-mark-start
                                name="BungeniSpeechBy:GideunMusiku8670"/>The Vice-President and
                            Minister for Home Affairs (Mr. Musyoka):<text:reference-mark-end
                                name="BungeniSpeechBy:GideunMusiku8670"/>
                        </text:p>
                        <text:p style-name="Standard">Mr. Speaker, Sir, I undertake to communicate
                            that concern by the hon. Member to the Minister for Local Government.
                        </text:p>
                    </text:section>
```

The style properties appear within the automatic-styles block in the header of the same document :

```
<style:style family="section" name="Sect12">
            <style:section-properties anx:BungeniPersonID="559" anx:BungeniPersonRole="559"
                anx:BungeniSectionType="Speech" anx:BungeniSpeechAs="Minister"
                anx:BungeniSpeechAsDesc="Minister"
                anx:BungeniSpeechAsURI="/ontology/roles/ke/minister"
                anx:BungeniSpeechBy="Gideun Musiku"
                anx:BungeniSpeechByURI="ken.person.Gideun.Musiku.1951-10-21"
                anx:__BungeniMetaEditable="true"
                anx:__BungeniMetaEditor="toolbarSubAction.makeDebateSpeechBlockSection.section_creation"
                background-color="transparent" margin-left="0.6in" margin-right="0in"
                editable="false">
                <style:columns column-count="1" column-gap="0in"/>
                <style:background-image filter-name="PNG - Portable Network Graphic"
                    actuate="onLoad" href="Pictures/1000000000000004000000045A636FA8.png"
                    type="simple"/>
            </style:section-properties>
        </style:style>
```

The marked up custom metadata for the speech is captured in the anx: namespace which is defined as follows in the ODF header :

```
xmlns:anx="http://anx.akomantoso.org/1.0"
```


## Translator Configuration ##

The Bungeni translator uses a sequence of chained XSLT to convert a ODF document into ANxml. The translator configuration is passed as input to the ODT translator. The translator config file looks like as shown below, and can be found in the configfiles/odttoakn folder of the translator. Translator configs are defined per document type. The following is what a translator configuration for the debate looks like  :
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
	<!-- debaterecord -->
	<entry key="metalexConfigPath">odttometalex/debaterecord/config.xml</entry>
	<entry key="akomantosoSchemaPath">odttoakn/schema/akomantoso10.xsd</entry>
	<entry key="akomantosoAddNamespaceXSLTPath">odttoakn/defaultvalues/debaterecord/OdtToAknAddNamespace.xsl</entry>
	<entry key="resourceBundlePath">localisation.messages.LabelsBundle</entry> 
</properties>
```


TODO