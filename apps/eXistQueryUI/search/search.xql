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
let $data-collection := '/db/xmlcontent'

let $q := request:get-parameter('q', '')
let $hits := collection($data-collection)//*[ft:query(., $q)]
let $count_hits := count($hits)

return

<html>
    <head>
        <title>{$title}</title>
    </head>
    <body>
        <h1>Search Results</h1>
         <p> <b> Searching for : </b> {$q} in collection : {$data-collection} </p>
         <p> count = {$count_hits} </p>
    </body>
</html>