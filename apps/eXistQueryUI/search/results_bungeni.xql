xquery version "1.0";

(: Headers :)

(: declare copy-namespaces no-preserve, no-inherit; :)
declare namespace an="http://www.akomantoso.org/1.0";
declare namespace request="http://exist-db.org/xquery/request";
declare namespace util="http://exist-db.org/xquery/util";
declare namespace xmldb="http://exist-db.org/xquery/xmldb";
declare namespace transform="http://exist-db.org/xquery/transform";


declare option exist:serialize "method=html media-type=text/html"; 



(: Functions :)

(: Strips the namespace information from the node passed in as a parameter 
and all child nodes :)
declare function local:strip-namespace($e as element()) as element() {
  
   element {QName((),local-name($e))} {
    for $child in $e/(@*,node())
    return
      if ($child instance of element())
      then
        local:strip-namespace($child)
      else
        $child
  }
};


(: return a deep copy of  the  speech element and all sub elements 
This variation generates a nodepath attribute for specific elements in the
hierarchy. The node path contains the absolute path to root for the filtered 
element :)
declare function local:copy-speech-with-path($element as element()) as element() {
   element {node-name($element)}
      { if (name($element) = "speech") 
			then attribute nodepath {local:path-from-root($element)}
			else()  ,
       $element/@*,
		   for $child in $element/node()
              return
               if ($child instance of element())
                 then local:copy-speech-with-path($child)
                 else $child
      }
};


(: Calculates the local path from root for an element X :)
 declare function local:path-from-root($x as node()) {
    if ($x/parent::*) then
      concat( local:path-from-root($x/parent::*), "/", local:name-and-position($x) )
    else
      concat( "/", node-name($x) )
  };

(: helper function for path-from-root -- calculates relative hierarchy to preceding 
nodes :)
declare function local:name-and-position($n as element()) as xs:string {
	concat(name($n), '[', 1+count($n/preceding-sibling::*[node-name(.) = node-name($n)]), ']')
};


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
				(: capture the URI of the matched debate document :)
				{$speechNode/ancestor::an:debateRecord/an:meta/descendant::an:FRBRManifestation/an:FRBRthis/@value/string()}
				</source>
				(: get the file name :)
				<file>{util:document-name($speechNode)}</file>
				(: Add a copy of the speech node here - a new attribute called @nodepath is injected into the speech node
					This attribute records the absolute path of the speech node with respect to the xml document in which it
					was found :)
				{ local:copy-speech-with-path($speechNode) }
				(: <speech-node-path>{ local:path-from-root($speechNode)}</speech-node-path> :)
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

let $for_html_nons := local:strip-namespace($for_html)


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