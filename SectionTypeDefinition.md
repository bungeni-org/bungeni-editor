

# Introduction #

A **sectionType** in the Bungeni Editor gets translated to a container element in XML.
Metadata and Translation aspects can be associated with a sectionType via configuration and it also supports applying a limited set of visual properties via configuration.

A sectionType is internally implemented in the Editor using a ODF text:section element embellished with RDF metadata.


# Section Type Configuration #

Section types are defined in their own configuration files within the [configuration folder](ConfigFolders.md) where every Document Type describes its available Section Types. The folder structure within the configuration folder is `section_types/<type-name>.xml`.

The following describes a configuration for the Bill sectionType 'CoverPage' without additional metadata :
```
<sectionTypes for="bill" xmlns:an="#an">

<sectionType name="CoverPage"
    prefix="coverpage"
    numstyle="single"
    background="0xfaf8cb"
    indent-left=".1"
    indent-right="0"
    visibility="user" >

 </sectionType>

.....
</sectionTypes>
```

The line `xmlns:an="#an"` specifies the namespace prefix for the output XML.  Note that in this configuration, the prefix is significant as it has to be pre-defined in the [doc types configuration](DocTypeDefinition.md). See [doc types definition - outputs section](DocTypeDefinition#Outputs.md). The middleware configuration automatically selects the correct namespace-uri from the doc types configuration based on the prefix

The individual parameters are described below :

  * **sectionType/@name - REQUIRED** - the name of the sectionType this is how the system will internally the type of the section.
  * **sectionType/@prefix - REQUIRED** -  the internal name of the section created using the sectionType. e.g. If we create a instance of a sectionType named "CoverPage" , a section called "coverpage" will be generated in the document.
  * **sectionType/@numstyle - REQUIRED** -  takes either 'single' or 'serial'. 'single' - will allow only one instance of the sectionType in the document. 'serial' will creation of multiple instance which will follow a serial number naming convention : coverpage1, coverpage2...etc.
  * **sectionType/@indent-left , sectionType@indent-right - OPTIONAL** - this specifies the indentation of an instance of the sectionType on the document. Indentations are specified in inches.
  * **sectionType/@background - OPTIONAL** - defaults to white. if specified you can either point to a background image (png or jpg ) by setting for e.g.:  `background="url:backgrounds/gray.png" ` where `backgrounds` is a folder located in the [configuration folder](ConfigFolders.md). Otherwise you can also specify a hex color code for e.g. : `background="0x00ff00"` to specify to specify a green background.
  * **sectionType/@visibility - OPTIONAL** - defaults to 'user' - not implemented yet, for future use.

# Metadata #

Section Types can be associated with metadata.

The metadata list is defined in the `metadatas` block. The following is an example of metadata associated with a Speech section type.

```
<sectionType name="Speech"
   .... >

     <metadatas>
         <metadata name="BungeniSpeechBy" />
         <metadata name="BungeniSpeechByURI" />
         <metadata name="BungeniSpeechAs" />
         <metadata name="BungeniSpeechAsDesc" />
         <metadata name="BungeniSpeechAsURI" />
         <metadata name="BungeniPersonId" />
         <metadata name="BungeniPersonRole" />
     </metadatas>

</sectionType>

```

The metadata names are declarative - they don't prescribe anything during markup but are more prescriptive from the point of view of semantically describing what information is captured in a particular section - and is also used during the conversion of the openoffice section to XML.

The metadata name declares that the action using the section type as a metadata source will set these metadata values in the section .i.e. it is upto the action associated with the sectionType to set these metadata.


# Output #

The output block describes how the section in the openoffice document will be
converted for XML output.  The default support in the packaged document types is to
output Akoma Ntoso XML.

## Separation of metadata and content ##

The output block supports seperation of content and metadata in the transformed
XML. This is done by separately defining output `meta` headers and output `content`
tags :

```
     <output>
         <meta>
            <references>
             <an:TLCPerson id="$BungeniPersonID" href="$BungeniSpeechByURI" 
                showAs="$BungeniSpeechBy" />
             <an:TLCRole id="$BungeniSpeechAs" href="$BungeniSpeechAsURI" 
                showAs="$BungeniSpeechAsDesc" />
             </references>
         </meta>
         <content>
            <an:speech by="#$BungeniPersonID" as="#$BungeniSpeechAs" /> 
         </content>
     </output>
```

In the above example, the `output/content` block describes how the speech section will be rendered in XML.
The configuration describes in a verbose way the structure of the final output xml :

```
            <an:speech by="#$BungeniPersonID" as="#$BungeniSpeechAs" /> 
```

Note that the output content definition needs to be prefixed with the namespace declared in the section type.

This description is enough for the Editor's translation middleware to convert the typed ODT speech section to XML form,
which looks like this :

```
   <meta>
     <references>
	   <TLCPerson id="JohnSmith" href="/ontology/person/ke/JohnSmith" 
                showAs="John Smith" />
           <TLCRole id="Speaker" href="/ontology/roles/Speaker/" 
                showAs="Speaker" />
          
     </references>
   </meta>
 
   ....
   <debateBody>
       ....
            <speech by="#JohnSmith" as="#Speaker" >
	      <from>mr. John Smith</from>
              <p>I would like to say ...blah... blah ..</p>
            </speech> 
       ....
   </debateBody>
```

_(Note: the `<from>` is marked up using a `BungeniInlineType` markup instead of a `BungeniSectionType`
markup and is rendered on that basis)_

The actual metadata is embedded into the output meta and content templates by
refering to them by name :

`$BungeniSpeechByURI` renders the value of the BungeniSpeechByURI metadata in the
output XML and also the header metadata.

## Proprietary Namespaces ##

Proprietary namespaces are a special case of metadata where the metadata resides
in a different namespace from the main metadata. These are supported by a
`proprietary` element in configuration :

In the following example, a proprietary metadata is set by the Speech :

```
    <output>
         <meta>
             <references>
               <an:TLCPerson id="$BungeniPersonID" href="$BungeniSpeechByURI" showAs="$BungeniSpeechBy" />
               <an:TLCRole id="$BungeniSpeechAs" href="$BungeniSpeechAsURI" showAs="$BungeniSpeechAsDesc" />
             </references>
             <proprietary xmlns:bg="#bg" >
                 <bg:meta person="$BungeniSpeechBy" />
             </proprietary>
         </meta>
         <content>
            <an:speech by="#$BungeniPersonID" as="#$BungeniSpeechAs" /> 
         </content>
     </output>
```

The namespace prefix `bg` needs to be predefined in the
[doc types outputs configuration](DocTypeDefinition#Outputs.md) ,the local namespace uri for the prefix in the configuration
(in this example : #bg ) is dropped by the middleware and swapped with the one defined in the doc-type outputs namespace configuration.


# Configuration Examples #

## Example 1 ##

Default configuration specifying minimal parameters and basic output template

```
<sectionType name="Speech"
    prefix="speech"
    numstyle="serial"
    >
    <output>
        <content>
         <an:speech />
        </content>
    </output>
</sectionType>
```

## Example 2 ##

Detailed configuration with output and metadata configuration

```
<sectionType name="Speech"
    prefix="speech"
    numstyle="serial"
    background="url:backgrounds/shade-1.png"
    indent-left=".6"
    indent-right="0"
    visibility="user" >
     <metadatas>
         <metadata name="BungeniSpeechBy" />
         <metadata name="BungeniSpeechByURI" />
         <metadata name="BungeniSpeechAs" />
         <metadata name="BungeniSpeechAsDesc" />
         <metadata name="BungeniSpeechAsURI" />
         <metadata name="BungeniPersonId" />
         <metadata name="BungeniPersonRole" />
     </metadatas>
     <output>
         <meta>
	    <references>
             <an:TLCPerson id="$BungeniPersonID" href="$BungeniSpeechByURI" showAs="$BungeniSpeechBy" />
             <an:TLCRole id="$BungeniSpeechAs" href="$BungeniSpeechAsURI" showAs="$BungeniSpeechAsDesc" />
            </references>
         </meta>
         <content>
            <an:speech by="#$BungeniPersonID" as="#$BungeniSpeechAs" /> 
         </content>
     </output>
 </sectionType>

```


# References #

  * [OpenOffice document sections](http://wiki.services.openoffice.org/wiki/Documentation/DevGuide/Text/Text_Sections)
  * [ODF text:section](http://docs.oasis-open.org/office/v1.2/os/OpenDocument-v1.2-os-part1.html#__RefHeading__1415162_253892949)
  * [ODF Toolkit Section implementation](http://incubator.apache.org/odftoolkit/simple/document/javadoc/org/odftoolkit/simple/text/Section.html)