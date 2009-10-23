xquery version "1.0";
 
declare namespace ansearch="http://akomantoso.org"; 
declare option exist:serialize "method=xhtml media-type=text/html";

import module namespace kwic="http://exist-db.org/xquery/kwic";



(: get the search parameter :)
let $q := xs:string(request:get-parameter("q", ""))
(: filter the search parameter to prevent injection attacks :)
let $filtered-q := replace($q, "[&amp;&quot;-*;-`~!@#$%^*()_+-=\[\]\{\}\|';:/.,?(:]", "")
(: search only within the xml content collection :)
let $scope := 
    ( 
        collection('/db/xmlcontent')/akomaNtoso
    )
(: consturct the actual search string :)
let $search-string := concat('$scope', '[. &amp;= "', $filtered-q, '"]')
(: execute the constructed query :)
let $hits := util:eval($search-string)
(: sort the hits using a relevance algorithm :
the number of keyword matches divided by the string length of the matching node :)
let $sorted-hits :=
    for $hit in $hits
    let $keyword-matches := text:match-count($hit)
    let $hit-node-length := string-length($hit)
    let $score := $keyword-matches div $hit-node-length
    order by $score descending
    return $hit
(: how many resyults per page :)
let $perpage := xs:integer(request:get-parameter("perpage", "10"))
(: starting point parameter :)
let $start := xs:integer(request:get-parameter("start", "0"))
(: total results count :)
let $total-result-count := count($hits)
(: end point for number of hits :)
let $end := 
    if ($total-result-count lt $perpage) then 
        $total-result-count
    else 
        $start + $perpage
(: build search results :)        
let $results := 
    for $hit in $sorted-hits[position() = ($start to $end)]
    let $collection := util:collection-name($hit)
    let $document := util:document-name($hit)
    let $config := <config xmlns="" width="60"/>
    let $base-uri := replace(request:get-url(), 'search.xq$', '')
    return 
        if ($collection = '/db/test/articles') then
            let $title := doc(concat($collection, '/', $document))//test:title/text()
            let $summary := kwic:summarize($hit, $config)
            let $url := concat('view-article.xq?article=', $document)
            return 
                <div class="result">
                    <p>
                        <span class="title"><a href="{$url}">{$title}</a></span><br/>
                        {$summary/*}<br/>
                        <span class="url">{concat($base-uri, $url)}</span>
                    </p>
                </div>
        else if ($collection = '/db/test/people') then
            let $title := doc(concat($collection, '/', $document))//test:name/text()
            let $summary := kwic:summarize($hit, $config)
            let $url := concat('view-person.xq?person=', $document)
            return 
                <div class="result">
                    <p>
                        <span class="title"><a href="{$url}">{$title}</a></span><br/>
                        {$summary/*}<br/>
                        <span class="url">{concat($base-uri, $url)}</span>
                    </p>
                </div>
        else 
            let $title := concat('Unknown result. Collection: ', $collection, '. Document: ', $document, '.')
            let $summary := kwic:summarize($hit, $config)
            let $url := concat($collection, '/', $document)
            return 
                <div class="result">
                    <p>
                        <span class="title"><a href="{$url}">{$title}</a></span><br/>
                        {$summary/*}<br/>
                        <span class="url">{concat($base-uri, $url)}</span>
                    </p>
                </div>
        


    

<html>
<head><title>Keyword Search</title></head>
<body>
    <h1>Keyword Search</h1>
    <form method="GET">
        <p>
            <strong>Keyword Search:</strong>
            (: show the last searched keyword :)
            <input name="q" type="text" value="{$q}"/>
        </p>
        <p>
            <input type="submit" value="Search"/>
        </p>
    </form>
</body>
</html>
