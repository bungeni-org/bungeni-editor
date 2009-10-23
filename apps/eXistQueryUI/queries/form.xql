xquery version "1.0";
(: $Id: guess.xql 9959 2009-09-01 13:01:21Z wolfgang_m $ :)

import module namespace request="http://exist-db.org/xquery/request";
import module namespace session="http://exist-db.org/xquery/session";
import module namespace util="http://exist-db.org/xquery/util";

declare option exist:serialize "method=xhtml media-type=text/html";

declare function local:random($max as xs:integer) 
as empty()
{
    let $r := ceiling(util:random() * $max) cast as xs:integer
    return (
        session:set-attribute("random", $r),
        session:set-attribute("guesses", 0)
    )
};

declare function local:guess($guess as xs:integer,
$rand as xs:integer) as element()
{
    let $count := session:get-attribute("guesses") + 1
    return (
        session:set-attribute("guesses", $count),
        if ($guess lt $rand) then
            <p>Your number is too small!</p>
        else if ($guess gt $rand) then
            <p>Your number is too large!</p>
        else
            let $newRandom := local:random(100)
            return
                <p>Congratulations! You guessed the right number with
                {$count} tries. Try again!</p>
    )
};

declare function local:main() as node()?
{
    session:create(),
    let $rand := session:get-attribute("random"),
        $guess := xs:integer(request:get-parameter("guess", ()))
    return
		if ($rand) then 
			if ($guess) then
				local:guess($guess, $rand)
			else
				<p>No input!</p>
		else 
		    local:random(100)
};

<html>
    <head>
    <link rel="stylesheet" href="css/form.css" />
    <title>Hansard Search</title>
    </head>
    <body>
    <form id="form1" name="frmHansardSearch" method="post" action="">
  <h1>Hansard Debates Search</h1>
  <table width="90%" border="0">
  <tbody>
  <tr>
    <td><label for="fldKeyword">Keyword</label>
      <input type="text" name="fldKeyword" id="fldKeyword" />
      </td>
    <td><label for="fldParliament">Parliament</label>
      <select name="fldParliament" id="fldParliament">
        <option value="all" selected="selected">All</option>
        <option value="8">8th Parliament (2008-)</option>
        <option value="7">7th Parliament (2005-2008)</option>
        <option value="6">6th Parliament (2001-2005)</option>
      </select>      </td>
  </tr>
  <tr>
    <td><label for="fldDate">Date </label>
      <select name="fldDate" id="fldDate">
        <option value="all" selected="selected">All</option>
        <option value="3days">In the Last 3 days</option>
        <option value="1week">In the Last Week</option>
        <option value="1month">In the Last Month</option>
        <option value="3months">In the Last 3 Months</option>
        <option value="1year">In the Last Year</option>
      </select>
      </td>
    <td><label for="fldMP">Member of Parliament</label>
      <select name="fldMP" id="fldMP">
        <option value="1">John Aazul</option>
        <option value="2">Alfred Abigail</option>
        <option value="3">Lee Buthefleki</option>
        <option value="4">Umar Gul</option>
        <option value="0" selected="selected">All</option>
                  </select></td>
  </tr>
  <tr>
    <td><label for="fldMP"></label></td>
    <td><input type="submit" name="btnSubmit" id="btnSubmit" value="Go" /></td>
  </tr>
  </tbody>
</table>
</form>
<div id="results">
{local:main()}
</div>
    </body>
</html>
