xquery version "1.0";

(: Headers :)

(: declare copy-namespaces no-preserve, no-inherit; :)
declare namespace an="http://www.akomantoso.org/1.0";
declare namespace request="http://exist-db.org/xquery/request";
declare namespace util="http://exist-db.org/xquery/util";
declare namespace transform="http://exist-db.org/xquery/transform";

declare option exist:serialize "method=html media-type=text/html"; 



(: Functions :)

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




(: Page Variables :)

let $doc_xslt := "/db/xslt/speechbymp.xsl"
let $doc_collection := "/db/testdata"
let $personURI := xs:string(request:get-parameter("uri_id", ""))
let $personName := xs:string(request:get-parameter("user_id", ""))
let $page_title := concat("Search Results for:" , $personName)



let $personNode := collection($doc_collection)/an:akomaNtoso/an:debateRecord/child::node()/descendant::an:TLCPerson[@href=$personURI]
let $personNodeId := $personNode/@id/string()

let $speechNodes := collection($doc_collection)/an:akomaNtoso/an:debateRecord/child::node()/descendant::an:speech[@by=$personNodeId]

let $results :=
    for $speechNode in $speechNodes
         return
            <doc><source>
{$speechNode/ancestor::an:debateRecord/an:meta/descendant::an:FRBRManifestation/an:FRBRthis/@value/string()}</source>
<file>{util:document-name($speechNode)}</file>
{$speechNode}</doc>

let $full_results := <docs>{$results}</docs>


let $for_html := <matches> <by> <uri>{$personURI}</uri><name> {$personName}</name> </by> {
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
				transform:transform($for_html_nons, xs:anyURI(concat("xmldb:exist://", $doc_xslt)), ()) 
				}
				</div>
			</div>
 	    </body>
		</html>