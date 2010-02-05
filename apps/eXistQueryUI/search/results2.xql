xquery version "1.0";

(: Headers :)

(: declare copy-namespaces no-preserve, no-inherit; :)
declare namespace an="http://www.akomantoso.org/1.0";
declare namespace request="http://exist-db.org/xquery/request";
declare namespace util="http://exist-db.org/xquery/util";
declare namespace xmldb="http://exist-db.org/xquery/xmldb";
declare namespace transform="http://exist-db.org/xquery/transform";

import module namespace anfunc = "http://exist.bungeni.org/anfunctions" at "modules/anfunctions.xqm";

declare option exist:serialize "method=html media-type=text/html";  



(: Functions :)


(: Page Variables :)

let $doc_xslt := xs:string(request:get-parameter("xslt_id", ""))
let $doc_collection := "/db/testdata"
let $personURI := xs:string(request:get-parameter("uri_id", ""))
let $personName := xs:string(request:get-parameter("user_id", ""))
let $page_title := concat("Search Results for:" , $personName)


(: First get the id of the TLCPerson record corresponding to the URI :)

let $personNode := collection($doc_collection)/an:akomaNtoso/an:debateRecord/child::node()/descendant::an:TLCPerson[@href=$personURI]
(: Get the string representation of the person id :)
let $personNodeId := $personNode/@id/string()
(: Get a list of speech nodes matching the person id ?:)
let $speechNodes := collection($doc_collection)/an:akomaNtoso/an:debateRecord/child::node()/descendant::an:speech[@by=$personNodeId]

let $results :=
(: Iterate through the speech nodes :)
    for $speechNode in $speechNodes
		  return
            <doc>
				<source>
				{$speechNode/ancestor::an:debateRecord/an:meta/descendant::an:FRBRManifestation/an:FRBRthis/@value/string()}
				</source>
				
				<file>{util:collection-name($speechNode)}/{util:document-name($speechNode)}</file>
				{ anfunc:copy-speech-with-path($speechNode) }
			</doc>

let $full_results := <docs>{$results}</docs>

(: Here we group the list of matching speches by source document :)
let $for_html := <matches> <by> <uri>{$personURI}</uri><name> {$personName}</name> </by> {
  (: First iterate with the <source> element, then iterate within that by grouping speeches :)
  for $source-link in distinct-values($full_results/doc/source)
     let $speeches-in-group := $full_results/doc[source=$source-link]
     return 
           <debate from="{$source-link}"> {
             for $speech in $speeches-in-group
               return
                $speech
           }</debate>
   }
   </matches>

let $for_html_nons := anfunc:strip-namespace($for_html)

return 
		<html>
		<head>
			<title>	{$page_title} </title>
			<link type="text/css" rel="stylesheet" href="/exist/rest/db/search/custom_css/search_bungeni.css" />
		</head>
		<body>
			<div class="container">
				<div class="main">
				<h1>Search Results for : {$personName}</h1>
				<h2>Matching results found in {count($for_html_nons//debate)} Debates, in {count($for_html_nons//speech)} speech(es)</h2>
		 		{
				(: Use XSLT to transform the input XML :)
				transform:transform($for_html_nons, xs:anyURI(concat("xmldb:exist://", $doc_xslt)), ()) 
				}
				</div>
			</div>
 	    </body>
		</html> 