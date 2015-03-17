

# Introduction #

Since OpenOffice.org 3.3 it is possible to apply RDF metadata on ODF elements.
There is API support for this [RDF Metadata](http://wiki.services.openoffice.org/wiki/Documentation/DevGuide/OfficeDev/RDF_metadata), but the documentation is very scanty on details. This page attempts to document the API using examples.

## Apply simple RDF comment on a section ##

This example applies a RDF Comment on the contents of a section called "Section1" which contains some text.

```
Sub Main

  Dim odoc
  odoc = thisComponent

  Dim osection 
  osection = odoc.getTextSections().getByName("Section1")

  Dim sectionRange
  sectionRange = osection.getAnchor()

  Dim docText
  docText= odoc.getText()

  'Apply an instance of InContentMetadata to the contents of the section
  Dim icmeta
  icmeta = odoc.createInstance("com.sun.star.text.InContentMetadata")
  docText.insertTextContent(sectionRange, icmeta, true)

  UriService = com.sun.star.rdf.URI
  UriSService = com.sun.star.rdf.URIs
  LiteralService = com.sun.star.rdf.Literal

  'Generate a RDF comment
  Dim uriComment
  uriComment = UriService.createKnown(UriSService.RDFS_COMMENT)
  Dim rdfLiteral
  rdfLiteral =LiteralService.create("Simple RDF Comment")

  'The RDF comment is captured in RDF metadata file
  Dim rdfDoc 
  rdfDoc = UriService.create( "http://akomantoso.org/ontologies/section/KE/ontology")
  
  Dim rdfMetafile
  rdfMetafile = odoc.addMetadataFile("akn/akn.rdf", Array(rdfDoc))

  'Get a RDF graph for the RDF metadata file 
  Dim rdfGraph
  rdfGraph = odoc.getRDFRepository().getGraph(rdfMetafile)
  'Add the metadata comment statement
  rdfGraph.addStatement(icmeta, uriComment, rdfLiteral)

  ' You will need to save the document manual after running this

End Sub
```

After saving the document -- examine the internals of of the ODF file.
The metadata is captured in a file component `akn/akn.rdf` inside the ODF file :

```
<?xml version="1.0" encoding="utf-8"?>
<rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="../content.xml#id862116040">
    <ns1:comment xmlns:ns1="http://www.w3.org/2000/01/rdf-schema#">Simple RDF Comment</ns1:comment>
  </rdf:Description>
</rdf:RDF>
```

The identifer in `rdf:Description/@rdf:about` ...

```
<rdf:Description rdf:about="../content.xml#id862116040">
```

... is the identifier  of the fragment of text in `Section1`

```

<text:section text:style-name="Sect1" text:name="Section1">
   <text:p text:style-name="Standard">
	<text:meta xml:id="id1200662435">This is some text</text:meta> <=== 
   </text:p>
</text:section>
```

## Apply metadata on a section ##

Metadata can also be applied directly on the section as a whole.
In this example -- we add metadata triples in a foreign namespace to the section rdf metadata :

```
Dim odoc
odoc = thisComponent

Dim osection 
osection = odoc.getTextSections().getByName("Section1")

Dim docText
docText= odoc.getText()

UriService = com.sun.star.rdf.URI
UriSService = com.sun.star.rdf.URIs
LiteralService = com.sun.star.rdf.Literal

Dim uriAnx 
uriAnx = UriService.create("http://editor.bungeni.org/1.0/anx" )

Dim rdfMetafile
rdfMetafile = odoc.addMetadataFile("meta/meta.rdf", Array(uriAnx))

Dim rdfGraph
rdfGraph = odoc.getRDFRepository().getGraph(rdfMetafile)

Dim uriSecType, litSecType
uriSecType = UriService.create("http://editor.bungeni.org/1.0/anx/Type")
litSecType = LiteralService.create("Clause")
rdfGraph.addStatement(osection, uriSecType, litSecType)

Dim uriSecUUID, litSecUUID
uriSecUUID = UriService.create("http://editor.bungeni.org/1.0/anx/UUID")
litSecUUID = LiteralService.create("1723-18388-38383-38383")
rdfGraph.addStatement(osection, uriSecUUID, litSecUUID)

End Sub
```

The rdf statements appear like this :

```
<?xml version="1.0" encoding="utf-8"?>
<rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="../content.xml#id780566046">
    <ns1:Type xmlns:ns1="http://editor.bungeni.org/1.0/anx/">Clause</ns1:Type>
    <ns2:UUID xmlns:ns2="http://editor.bungeni.org/1.0/anx/">1723-18388-38383-38383</ns2:UUID>
  </rdf:Description>
</rdf:RDF>

```

There is also a RDF manifest generated for the ODF file :

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

Note: Due to an issue in the RDF serialization used in OOo 3.3 -- there are some restrictions on the format of the namespace used, See [http://www.openoffice.org/issues/show\_bug.cgi?id=116443](http://www.openoffice.org/issues/show_bug.cgi?id=116443)


## Read metadata on a section ##

Metadata can be read back for a Section by retrieving the RDF graph and getting the statement with the Subject of the statement as the section with the metadata.

```

Dim uriAnx 
uriAnx = UriService.create("http://editor.bungeni.org/1.0/anx" )


Dim rdfGraphs
rdfGraphs = odoc.getRDFRepository().getGraphNames()

For each graphName in rdfGraphs
    if graphName.LocalName = "meta.rdf" then
 	  
 	  dim secsMeta
 	  
      secsMeta = odoc.getRDFRepository().getGraph(graphName)
      
      dim secMeta, secTypeURI
      
      secTypeURI = UriService.create("http://editor.bungeni.org/1.0/anx/Type")
      
      secMeta = secsMeta.getStatements(odoc.getTextSections().getByName("Section1"), secTypeURI, null )
      
      do while (secMeta.hasMoreElements() ) 
        Dim astatement
      	astatement = secMeta.nextElement()
      	' The statement contains the metadata triple
      loop
      
    end if
Next graphName

```


To check if the section has any RDF Metadata, you can use something like :

```
  secMeta = secsMeta.getStatements(odoc.getTextSections().getByName("Section1"), secTypeURI, null )
  if (secMeta.hasMoreElements() ) then 
     Print "Section has RDF metadata" 
  else
     Print "Section has no RDF metadata"

  end if
```  secMeta = secsMeta.getStatements(odoc.getTextSections().getByName("Section1"), secTypeURI, null )
  if (secMeta.hasMoreElements() ) then 
     Print "Section has RDF metadata" 
  else
     Print "Section has no RDF metadata"

  end if
}}}```