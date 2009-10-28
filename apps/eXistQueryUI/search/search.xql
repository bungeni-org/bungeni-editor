xquery version "1.0";

(: eXist namespaces :)
declare namespace request = "http://exist-db.org/xquery/request" ;
declare namespace ft = "http://exist-db.org/xquery/lucene" ;

(: user namespaces :)
declare namespace an = "http://www.akomantoso.org-1.0"; 


declare option exist:serialize "method=xhtml media-type=text/html";

(: Imports :)
import module namespace kwic="http://exist-db.org/xquery/kwic";

(: Page title :)
let $title := 'AkomaNtoso Search'

(: define the search scope, add more collections here to widen the search :)
let $search_scope := 
	(
		collection('/db/xmlcontent')
	)

(: get the search parameter :)
let $q := request:get-parameter('q', '')

(: remove illegal characters to prevent injection attacks :)
let $filtered-q := replace($q, "[&amp;&quot;-*;-`~!@#$%^*()_+-=\[\]\{\}\|';:/.,?(:]", "")

(: build the search string - by searching across all the collections in the scope :)
let $search_string := concat('$search_scope', '//*[ft:query(., $filtered-q)]')

(: run the search :)
let $hits := util:eval($search_string)

(: get the hit count :)
let $count_hits := count($hits)

(: evaluate the hits one by one :)
let $results := 
	for $hit in $hits
		let $document := util:document-name($hit)
		let $base-uri := util:collection-name($hit)
		let $rel-uri-to-doc := concat($base-uri, '/', $document)
		let $full-uri-to-doc := concat(replace(request:get-url(),'search/search.xql$',''), $rel-uri-to-doc)
		let $hit_summary := kwic:summarize($hit, <config width="40" />)
		return
			<div class="result">
				<p>
				<span class="title"><a href="{$full-uri-to-doc}">hit </a> </span><br />
				{$hit_summary} <br />
				<span class="url">{$full-uri-to-doc} </span>
				</p>
			</div>


return

<html>
    <head>
        <title>{$title}</title>
		<link href="css/form.css" type="text/css" rel="stylesheet" />
    </head>
    <body>
        <h1>Search Results</h1>
         <p> <b> Searching for : </b> {$filtered-q}  </p>
         <p> Matching Documents Found = {$count_hits} </p>
		 {$results}
    </body>
</html>