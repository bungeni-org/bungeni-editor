xquery version "1.0";
 
declare namespace an="http://akomantoso.org-1.0"; 
declare option exist:serialize "method=xhtml media-type=text/html";

import module namespace kwic="http://exist-db.org/xquery/kwic";

let $title := 'AkomaNtoso Search'

return

<html>
    <head>
        <title>{$title}</title>
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