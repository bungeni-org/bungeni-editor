xquery version "1.0";

(: eXist namespaces :)
declare namespace request = "http://exist-db.org/xquery/request" ;
declare namespace ft = "http://exist-db.org/xquery/lucene" ;

(: user namespaces :)
declare namespace an = "http://akomantoso.org-1.0"; 

declare option exist:serialize "method=xhtml media-type=text/html";

(: Imports :)
import module namespace kwic="http://exist-db.org/xquery/kwic";



let $title := 'AkomaNtoso Search'

return

<html>
    <head>
        <title>{$title}</title>
 		<!--CSS file (default YUI Sam Skin) -->
<link type="text/css" rel="stylesheet" href="http://yui.yahooapis.com/2.8.0r4/build/autocomplete/assets/skins/sam/autocomplete.css">

<!-- Dependencies -->
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.0r4/build/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.0r4/build/datasource/datasource-min.js"></script>

<!-- OPTIONAL: Get (required only if using ScriptNodeDataSource) -->
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.0r4/build/get/get-min.js"></script>

<!-- OPTIONAL: Connection (required only if using XHRDataSource) -->
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.0r4/build/connection/connection-min.js"></script>

<!-- OPTIONAL: Animation (required only if enabling animation) -->
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.0r4/build/animation/animation-min.js"></script>

<!-- OPTIONAL: JSON (enables JSON validation) -->
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.0r4/build/json/json-min.js"></script>

<!-- Source file -->
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.0r4/build/autocomplete/autocomplete-min.js"></script>

    </head>
    <body>
        <h1>{$title}</h1>
        <form method="GET" action="search.xql">
        <p>
             <strong>Keyword Search:</strong>
             <input name="q" type="text" />
        </p>
        <p>
            <input type="submit" value="Search" />
        </p>
        </form>
    </body>
</html>