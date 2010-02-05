(:-
: common akoma ntoso functions
: 
: 
:)


module namespace anfunctions = "http://exist.bungeni.org/anfunctions";


(: Strips the namespace information from the node passed in as a parameter 
and all child nodes :)

declare function anfunctions:strip-namespace($e as element()) as element() {
  
   element {QName((),local-name($e))} {
    for $child in $e/(@*,node())
    return
      if ($child instance of element())
      then
        anfunctions:strip-namespace($child)
      else
        $child
  }
};


(: return a deep copy of  the  speech element and all sub elements 
This variation generates a nodepath attribute for specific elements in the
hierarchy. The node path contains the absolute path to root for the filtered 
element :)


declare function anfunctions:copy-speech-with-path($element as element()) as element() {
   element {node-name($element)}
      { if (name($element) = "speech") 
			then attribute nodepath {anfunctions:path-from-root($element)}
			else()  ,
       $element/@*,
		   for $child in $element/node()
              return
               if ($child instance of element())
                 then anfunctions:copy-speech-with-path($child)
                 else $child
      }
};


(: Calculates the local path from root for an element X :)
 declare function anfunctions:path-from-root($x as node()) {
    if ($x/parent::*) then
      concat( anfunctions:path-from-root($x/parent::*), "/", anfunctions:name-and-position($x) )
    else
      concat( "/", node-name($x) )
  };

(: helper function for path-from-root -- calculates relative hierarchy to preceding 
nodes :)
declare function anfunctions:name-and-position($n as element()) as xs:string {
	concat(name($n), '[', 1+count($n/preceding-sibling::*[node-name(.) = node-name($n)]), ']')
};