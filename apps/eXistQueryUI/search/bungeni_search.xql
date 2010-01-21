xquery version "1.0";

declare namespace request="http://exist-db.org/xquery/request";
declare namespace util="http://exist-db.org/xquery/util";
declare option exist:serialize "method=xhtml media-type=application/xhtml+html";


<html><head>
<!--CSS file (default YUI Sam Skin) --><link type="text/css" rel="stylesheet" href="http://yui.yahooapis.com/2.8.0r4/build/autocomplete/assets/skins/sam/autocomplete.css"/>

<!-- Dependencies --><script type="text/javascript" src="http://yui.yahooapis.com/2.8.0r4/build/yahoo-dom-event/yahoo-dom-event.js"/><script type="text/javascript" src="http://yui.yahooapis.com/2.8.0r4/build/datasource/datasource-min.js"/>

<!-- OPTIONAL: Get (required only if using ScriptNodeDataSource) --><script type="text/javascript" src="http://yui.yahooapis.com/2.8.0r4/build/get/get-min.js"/>

<!-- OPTIONAL: Connection (required only if using XHRDataSource) --><script type="text/javascript" src="http://yui.yahooapis.com/2.8.0r4/build/connection/connection-min.js"/>

<!-- OPTIONAL: Animation (required only if enabling animation) --><script type="text/javascript" src="http://yui.yahooapis.com/2.8.0r4/build/animation/animation-min.js"/>

<!-- OPTIONAL: JSON (enables JSON validation) --><script type="text/javascript" src="http://yui.yahooapis.com/2.8.0r4/build/json/json-min.js"/>

<!-- Source file --><script type="text/javascript" src="http://yui.yahooapis.com/2.8.0r4/build/autocomplete/autocomplete-min.js"/>


</head><body class="yui-skin-sam"> 
// An outer container contains an input element and
// a empty results container, both of which are passed
// to the constructor
<div id="myAutoComplete"><input id="myInput_id" type="hidden" name="myInput_id"/><input id="myInput" type="text"/><div id="myContainer"/></div>
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

var myHiddenField = YAHOO.util.Dom.get("myInput_id");
 var myHandler = function(sType, aArgs) {{
           var myAC = aArgs[0]; // reference back to the AC instance
           var elLI = aArgs[1]; // reference to the selected LI element
           var oData = aArgs[2]; // object literal of selected item's result data
            
           // update hidden form field with the selected item's ID
           myHiddenField.value = oData.id;
       }};
  myAutoComp.itemSelectEvent.subscribe(myHandler); 

</script><input type="button" value="viewHidden" id="viewHidden" onClick="viewHidden();"/></body></html>