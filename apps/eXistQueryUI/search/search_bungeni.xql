xquery version "1.0";

declare namespace request="http://exist-db.org/xquery/request";
declare namespace util="http://exist-db.org/xquery/util";
declare namespace xmldb="http://exist-db.org/xquery/xmldb";
declare option exist:serialize "method=xhtml media-type=text/html";


let $page_title := "Search Debaterecords for Members of Parliament's speeches"
let $xslt_id := "/db/xslt/speechbymp.xsl"

return
<html>
<head>
<title>{$page_title}</title>
<!--CSS file (default YUI Sam Skin) -->

<link type="text/css" rel="stylesheet" href="/exist/rest/db/yui/css/autocomplete.css"/>
<link type="text/css" rel="stylesheet" href="/exist/rest/db/search/custom_css/search_bungeni.css" />
<!-- Dependencies -->

<script type="text/javascript" src="/exist/rest/db/yui/js/yahoo-dom-event.js"/>

<script type="text/javascript" src="/exist/rest/db/yui/js/datasource-min.js"/>

<!-- OPTIONAL: Get (required only if using ScriptNodeDataSource) -->

<script type="text/javascript" src="/exist/rest/db/yui/js/get-min.js"/>

<!-- OPTIONAL: Connection (required only if using XHRDataSource) -->

<script type="text/javascript" src="/exist/rest/db/yui/js/connection-min.js"/>

<!-- OPTIONAL: Animation (required only if enabling animation) -->

<script type="text/javascript" src="/exist/rest/db/yui/js/animation-min.js"/>

<!-- OPTIONAL: JSON (enables JSON validation) -->

<script type="text/javascript" src="/exist/rest/db/yui/js/json-min.js"/>

<!-- Source file -->

<script type="text/javascript" src="/exist/rest/db/yui/js/autocomplete-min.js"/>

<!--begin custom header content for this example-->


</head>
<body class="yui-skin-sam"> 

<script type="text/javascript">
function dispHidden() {{
var fieldHidden = document.getElementById('uri_id');
alert(fieldHidden.value);
}}


</script>
<div class="container">

<div class="main">

<form action="results_bungeni.xql" method="get" class="cmxform">
<fieldset>
<legend>Search Debaterecords for Speechs by a Member of Parliament</legend>
<div id="myAutoComplete">

<input id="uri_id" type="hidden" name="uri_id" value="{request:get-parameter("uri_id","0")}"/>
<input id="user_id" type="hidden" name="user_id" value="{request:get-parameter("user_id","0")}"/>
<input id="xslt_id" type="hidden" name="xslt_id" value="{$xslt_id}" />
<label for="myInput">Type Member's Name</label>
<input id="myInput" type="text"/>

<div id="myContainer"/>

</div>

<script type="text/javascript">



var jsonDS = new YAHOO.util.XHRDataSource("http://localhost:8080/exist/rest/db/jsonfeed/members.json");
jsonDS.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;
jsonDS.responseSchema = {{
resultsList : "nodes",
fields: [
{{key: "user_id"}},
{{key: "uri"}}
]

}};
var myAutoComp = new YAHOO.widget.AutoComplete("myInput","myContainer", jsonDS);
myAutoComp.prehighlightClassName = "yui-ac-prehighlight";
myAutoComp.useShadow = true;
myAutoComp.allowBrowserAutocomplete = false; 
// Container will expand and collapse vertically
myAutoComp.animVert = true;
// Container will expand and collapse horizontally
//myAutoComp.animHoriz = true;
// Container animation will take 2 seconds to complete
myAutoComp.minQueryLength = 1; 
// Enable force selection 
myAutoComp.forceSelection = true; 
myAutoComp.typeAhead = true;
myAutoComp.autoHighlight=true;
myAutoComp.applyLocalFilter = true;

function fnCallback(e, args) {{
    YAHOO.util.Dom.get("uri_id").value = args[2][1];
    YAHOO.util.Dom.get("user_id").value = args[2][0];
 }}
myAutoComp.itemSelectEvent.subscribe(fnCallback);


</script>
<input type="submit" value="Submit" /> <!--
<input type="button" value="View Hidden" id="viewHidden" onClick="dispHidden();" />
-->
</fieldset>
</form>

</div>

</div>


</body>

</html>