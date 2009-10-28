xquery version "1.0";
declare option exist:serialize "method=xhtml media-type=text/html indent=yes";

let $title := 'Simple Search RESTful Service'
let $data-collection := '/db/test/data'

(: get the search query string from the URL parameter :)
let $q := request:get-parameter('q', '')

return
 <html>
    <head>
       <title>{$title}</title>
     </head>
     <body>
        <h1>Search Results</h1>
        <p><b>Searching for: </b>{$q} in collection: {$data-collection}</p>
        <ol>{
           for $hit in collection($data-collection)/item[. &= $q]
             return
                <li>{data($hit)}</li>
        }</ol>
   </body>
 </html>

