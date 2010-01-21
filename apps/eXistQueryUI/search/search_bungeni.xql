xquery version "1.0";

declare namespace request="http://exist-db.org/xquery/request";
declare namespace util="http://exist-db.org/xquery/util";
declare option exist:serialize "method=xhtml media-type=text/html";

<html>
<head>
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
// An outer container contains an input element and
// a empty results container, both of which are passed
// to the constructor
<script type="text/javascript">
function dispHidden() {{
var fieldHidden = document.getElementById('myInput_id');
alert(fieldHidden.value);
}}


</script>

<form action="{request:get-uri()}" method="get">

<div id="myAutoComplete">

<input id="myInput_id" type="hidden" name="myInput_id" value="{request:get-parameter("myInput_id",0)}"/>

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
{{key: "object_id"}}
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
    YAHOO.util.Dom.get("myInput_id").value = args[2][1];
 }}
myAutoComp.itemSelectEvent.subscribe(fnCallback);


</script>
<input type="submit" value="Submit" />
<input type="button" value="View Hidden" id="viewHidden" onClick="dispHidden();" />
</form>

</body>

</html>