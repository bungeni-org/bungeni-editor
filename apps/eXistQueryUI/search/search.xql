xquery version "1.0";

(: eXist namespaces :)
declare namespace request = "http://exist-db.org/xquery/request" ;
declare namespace ft = "http://exist-db.org/xquery/lucene" ;

(: user namespaces :)
declare namespace an = "http://www.akomantoso.org-1.0"; 


declare option exist:serialize "method=xhtml media-type=text/html";

(: Imports :)
import module namespace kwic="http://exist-db.org/xquery/kwic";


let $title := 'AkomaNtoso Search'
let $data-collection := '/db/xmlcontent/akomaNtoso'

let $q := request:get-parameter('q', '')

return

<html>
    <head>
        <title>{$title}</title>
    </head>
    <body>
        <h1>Search Results</h1>
         <p> <b> Searching for : </b> {$q} in collection : {$data-collection} </p>
         <ol> {
             for $hit in collection($data-collection)//*[ft:query(.,$q)]
                 return
                     <li>{data($hit)}</li>
         }</ol>
    </body>
</html>