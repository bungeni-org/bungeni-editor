# /set\_convert\_params #

## Usage ##

To use this API POST a form to http://server_name:server_port/set_convert_params
The following parameters are mandatory for the HTTP form post :
  * DocType - the document type of the document (debaterecord, bill, judgement)
  * Plugin Mode - currently 2 modes are supported "odt2akn" and "akn2html"

## Return Response ##

The API returns a SUCCESS NO CONTENT status message when successful