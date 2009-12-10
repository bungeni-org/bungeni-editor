xquery version "1.0";
 
declare namespace an="http://akomantoso.org-1.0"; 
declare option exist:serialize "method=xhtml media-type=text/html";

import module namespace kwic="http://exist-db.org/xquery/kwic";

let $q := xs:string(request:get-parameter("q", ""))
let $filtered-q := replace($q, "[&amp;&quot;-*;-`~!@#$%^*()_+-=\[\]\{\}\|';:/.,?(:]", "")
let $scope := 
    ( 
        collection('/db/xmlcontent')/an:akomaNtoso 
    )
let $search-string := concat('$scope', '[. &amp;= "', $filtered-q, '"]')
let $hits := util:eval($search-string)
let $sorted-hits :=
    for $hit in $hits
    let $keyword-matches := text:match-count($hit)
    let $hit-node-length := string-length($hit)
    let $score := $keyword-matches div $hit-node-length
    order by $score descending
    return $hit
let $perpage := xs:integer(request:get-parameter("perpage", "10"))
let $start := xs:integer(request:get-parameter("start", "0"))
let $total-result-count := count($hits)
let $end := 
    if ($total-result-count lt $perpage) then 
        $total-result-count
    else 
        $start + $perpage
let $results := 
    for $hit in $sorted-hits[position() = ($start to $end)]
    let $collection := util:collection-name($hit)
    let $document := util:document-name($hit)
    let $config := <config xmlns="" width="60"/>
    let $base-uri := replace(request:get-url(), 'search.xq$', '')
    return 
            let $title := doc(concat($collection, '/', $document))//an:preface/text()
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
     
let $number-of-pages := 
    xs:integer(ceiling($total-result-count div $perpage))
let $current-page := xs:integer(($start + $perpage) div $perpage)
let $url-params-without-start := replace(request:get-query-string(), '&amp;start=\d+', '')
let $pagination-links := 
    if ($number-of-pages le 1) then ()
    else
        <ul>
            {
            (: Show 'Previous' for all but the 1st page of results :)
                if ($current-page = 1) then ()
                else
                    <li><a href="{concat('?', $url-params-without-start, '&amp;start=', $perpage * ($current-page - 2)) }">Previous</a></li>
            }
 
            {
            (: Show links to each page of results :)
                let $max-pages-to-show := 20
                let $padding := xs:integer(round($max-pages-to-show div 2))
                let $start-page := 
                    if ($current-page le ($padding + 1)) then
                        1
                    else $current-page - $padding
                let $end-page := 
                    if ($number-of-pages le ($current-page + $padding)) then
                        $number-of-pages
                    else $current-page + $padding - 1
                for $page in ($start-page to $end-page)
                let $newstart := $perpage * ($page - 1)
                return
                    (
                    if ($newstart eq $start) then 
                        (<li>{$page}</li>)
                    else
                        <li><a href="{concat('?', $url-params-without-start, '&amp;start=', $newstart)}">{$page}</a></li>
                    )
            }
 
            {
            (: Shows 'Next' for all but the last page of results :)
                if ($start + $perpage ge $total-result-count) then ()
                else
                    <li><a href="{concat('?', $url-params-without-start, '&amp;start=', $start + $perpage)}">Next</a></li>
            }
        </ul>
let $how-many-on-this-page := 
    (: provides textual explanation about how many results are on this page, 
     : i.e. 'all n results', or '10 of n results' :)
    if ($total-result-count lt $perpage) then 
        concat('all ', $total-result-count, ' results')
    else
        concat($start + 1, '-', $end, ' of ', $total-result-count, ' results')
return
 
<html>
<head>
    <title>Keyword Search</title>
    <style>
        body {{ 
            font-family: arial, helvetica, sans-serif; 
            font-size: small 
            }}
        div.result {{ 
            margin-top: 1em;
            margin-bottom: 1em;
            border-top: 1px solid #dddde8;
            border-bottom: 1px solid #dddde8;
            background-color: #f6f6f8; 
            }}
        #search-pagination {{ 
            display: block;
            float: left;
            text-align: center;
            width: 100%;
            margin: 0 5px 20px 0; 
            padding: 0;
            overflow: hidden;
            }}
        #search-pagination li {{
            display: inline-block;
            float: left;
            list-style: none;
            padding: 4px;
            text-align: center;
            background-color: #f6f6fa;
            border: 1px solid #dddde8;
            color: #181a31;
            }}
        span.hi {{ 
            font-weight: bold; 
            }}
        span.title {{ font-size: medium; }}
        span.url {{ color: green; }}
    </style>
</head>
<body>
    <h1>Keyword Search</h1>
    <div id="searchform">
        <form method="GET">
            <p>
                <strong>Keyword Search:</strong>
                <input name="q" type="text" value="{$q}"/>
            </p>
            <p>
                <input type="submit" value="Search"/>
            </p>
        </form>
    </div>
 
    {
    if (empty($hits)) then ()
    else
        (
        <h2>Results for keyword search &quot;{$q}&quot;.  Displaying {$how-many-on-this-page}.</h2>,
        <div id="searchresults">{$results}</div>,
        <div id="search-pagination">{$pagination-links}</div>
        )
    }
</body>
</html>
