xquery version "1.0";

(: Headers :)

(: declare copy-namespaces no-preserve, no-inherit; :)
declare namespace an="http://www.akomantoso.org/1.0";
declare default element namespace "http://www.akomantoso.org/1.0";

declare namespace request="http://exist-db.org/xquery/request";
declare namespace util="http://exist-db.org/xquery/util";
declare namespace xmldb="http://exist-db.org/xquery/xmldb";
declare namespace transform="http://exist-db.org/xquery/transform";

import module namespace anfunc = "http://exist.bungeni.org/anfunctions" at "modules/anfunctions.xqm";



declare option exist:serialize "method=html media-type=text/html";  

(: Page Variables :)
let $page_title := "View Speech in Context"
let $doc_xslt := "/db/xslt/contextdisplay.xsl"

let $str_dbpref := "xmldb:exist://" 
let $str_xmlfilepath := xs:string(request:get-parameter("xmlfile", ""))
let $doc_str := concat( $str_dbpref, $str_xmlfilepath)
let $nodepath := xs:string(request:get-parameter("nodepath", ""))
let $speech-id := xs:string(request:get-parameter("cid", ""))

(: Get last index of  matching speech :)
let $matching_path_arr := tokenize($nodepath, "/")
let $matching_node_index := count($matching_path_arr)
let $matching_node_path := $matching_path_arr[$matching_node_index]

(: use eval() to XPath to the speech :)
let $search_string := concat("doc('", $doc_str,"')", $nodepath)
let $matching_speech := util:eval($search_string)
let $speech_post := $matching_speech/position()

(: get the parent container of the matching speech :)
let $parent_speech := $matching_speech/..
(: Now we highlight the speech within the parent container :)


let $parent_prev_sibling := $parent_speech/preceding-sibling::node()[1]
let $parent_foll_sibling := $parent_speech/following-sibling::node()[1]

let $result_xml := 
		<context>
			<position-in-parent>{$matching_node_path}</position-in-parent>
			<matching-id>{$speech-id}</matching-id>
			<preceding>{$parent_prev_sibling}</preceding>
			<parent>{$parent_speech}</parent>
			<following>{$parent_foll_sibling}</following>
		</context>

let $normalize_result_xml := anfunc:strip-namespace($result_xml)

return 
	<html>
		<head>
			<title>	{$page_title} </title>
			<link type="text/css" rel="stylesheet" href="/exist/rest/db/search/custom_css/search_bungeni.css" />
		</head>
		<body>
			<div class="container">
				<div class="main">
				Viewing
				{
				(: Use XSLT to transform the input XML :)
			    transform:transform($normalize_result_xml , xs:anyURI(concat("xmldb:exist://", $doc_xslt)), ()) 
		
				}
		
				</div>
			</div>
 	    </body>
		</html> 
