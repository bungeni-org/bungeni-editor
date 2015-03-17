

# Introduction #

The Editor supports defining document types and providing them with different structures and different kinds of markup actions.

# Configuration #

The doctype configuration resides within the [Configuration Folder](ConfigFolders.md) in a file called doc\_types.xml.

The following is an example doctype configuration for the Bill document type :

```
<doctypes>
    <outputs>
	   <namespace prefix="an" type="main" desc="Akoma Ntoso" uri="http://www.akomantoso.org/2.0" />
		....
	</outputs>

....

    <doctype name="bill"
        template="templates/bill.ott"
        state="1"
        root="bill"
        default-part="main">
        <title xml:lang="eng">Bill</title>
        <title xml:lang="spa">proyceto</title>
        <uri type="work">~CountryCode~DocumentType~Year-Month-Day</uri>
        <uri type="expression">~CountryCode~DocumentType~Year-Month-Day~LanguageCode</uri>
        <file-name-scheme>CountryCode~DocumentType~Year-Month-Day~LanguageCode</file-name-scheme>
        <metadata-editors>
            <title xml:lang="eng">Bill Metadata</title>
            <metadata-editor class="org.bungeni.editor.metadata.editors.GeneralMetadata" >
                <title xml:lang="eng">General</title>
            </metadata-editor>
            <metadata-editor class="org.bungeni.editor.metadata.editors.BillMetadata" >
                <title xml:lang="eng">Bill</title>
            </metadata-editor>
        </metadata-editors>
        <parts>
            <part name="main">
                <title xml:lang="eng">Main</title>
                <title xml:lang="spa">principal</title>
            </part>
            <part name="annex">
                <title xml:lang="eng">Annex</title>
                <title xml:lang="spa">adjuntar</title>
            </part>
        </parts>
    </doctype>

    <doctype name="debaterecord" ... 
.....
</doctypes>
```

The different configuration options are described below :

### doctype element ###

The _name_ attribute provides the name of the document type

```
 <doctype name="bill"
```

The _template_ attribute provides path to the document template used by the document type.

```
        template="templates/bill.ott"
```
This path is always relative to the [Configuration Folder](ConfigFolders.md) of the Editor. For e.g. if your config
folder is located on `c:/editor_configs/custom_config` , the templates path will be `c:/editor_configs/custom_config/templates/bill.ott`.


The _state\_attribute indicates whether the doctype is enabled or not. `state="0"` disables the document type, and it will
not appear in any visual selections in the Editor.
```
        state="1"
```_

The _root_ attribute names the root section of the documen type. The root section with this name must be present in the template
specified in the _template_ attribute.
```
        root="bill"
```

The _default-part_ attribute indicates the default part of the document
```
        default-part="main">
```

THe concept behind parts is to support segmented documents - usually documents like legislative bills are not one big document, but
a set of composite documents (a "package") - which can include the main document, the annexes, attachments and reports.

# Outputs #

The doc types configuration file also allows specifying global namespaces in the `<outputs>` block :

```
<doctypes>
    <outputs>
	   <namespace prefix="an" type="main" desc="Akoma Ntoso" uri="http://www.akomantoso.org/2.0" />
	   <namespace prefix="bg" type="proprietary" desc="Proprietary" uri="http://www.proprietary.org" />
	   
	</outputs>
....
</doctypes>

```

The output XML namespace and any other proprietary namespaces which appear in the output XML need to be listed here.
This information is used during output generation to produce the final XML. The `proprietary` namespace is used to specify namespaces for proprietary metadata blocks.
