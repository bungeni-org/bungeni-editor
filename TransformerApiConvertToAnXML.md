# /convert\_to\_anxml #

## Usage ##

To use this API post an ODT file to the http://server_name:server_port/convert_to_anxml
The following parameters are mandatory for the HTTP post :
  * an ODT file
  * a custom header with the name "X-Odt-File" containing the name of the input file

It is mandatory to call TransformerApiSetConvertParams before calling TransformerApiConvertToAnXML

## Return Response ##

The API returns an XML message response containing both transformation errors, and transformation output. Transformation errors are returned in a CDATA block under //errors Transformation output is returned in a CDATA block under //output