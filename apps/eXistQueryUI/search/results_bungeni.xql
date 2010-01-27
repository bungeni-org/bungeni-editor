xquery version "1.0";
declare namespace an="http://www.akomantoso.org/1.0";
declare namespace request="http://exist-db.org/xquery/request";
declare namespace util="http://exist-db.org/xquery/util";
declare option exist:serialize "method=xhtml media-type=text/html";

let $personURI := xs:string(request:get-parameter("uri_id", ""))
let $personName := xs:string(request:get-parameter("user_id", ""))
let $personNode := collection("/db/testdata")/an:akomaNtoso/an:debateRecord/child::node()/descendant::an:TLCPerson[@href=$personURI]
let $personNodeId := $personNode/@id/string()

let $speechNodes := collection("/db/testdata")/an:akomaNtoso/an:debateRecord/child::node()/descendant::an:speech[@by=$personNodeId]

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

return 
       <html>
       <head>
       </head>
       <body>
       <h1>
       Search Results for Member of Parliament : {$personName}       </h1>
 		{
 		(: loop for for all debate elements :)
 		for $debate in $for_html/debate 
 			let $debate_uri := $debate/@from/string()
			let $uri_components := tokenize($debate_uri, '/')
			let $date-debate := item-at($uri_components, 4)
            return 
				<p><a href="{$debate_uri}">Debate -- {$date-debate}</a>
				{$uri_components}
				</p>
				
 	    }
       </body>
       </html>