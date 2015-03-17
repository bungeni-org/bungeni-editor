

# Introduction #

The BungeniEditor supports multiple document types.  Additional document types can be added by adding configuration entries.

The following steps describe the config entries required to register a new document type in the editor.

## Adding the DocType configuration ##

Assuming you have created your [custom configuration folder](ConfigFolders.md).

See [Doc Type Definition](DocTypeDefinition.md)

## Create the template for the document type ##

The templates for the different document types are stored under the `<your_config_folder>/templates folder`.  Templates are standard OpenOffice ott templates, with some default mandatory content.

The easiest way to setup a new doctype template is to copy an existing one and modify it.

Lets say you added a new type called `Petition`, and so to create a new doctype template for this, do the following :

  1. Copy an existing template e.g. `development_configs/templates/debaterecord.ott` as `<your_config_folder>/templates/petition.ott`
  1. Open the template for editing in OpenOffice ; To do that:
    * Launch OpenOffice
    * Go to File->Templates->Edit and browse to `<your_config_folder>/templates/petition.ott` and open it
    * Then go to Format->Sections, to open the `Edit Sections` screen. Click on the top level section, which should read `debaterecord`, rename it to petition , click OK and save the document.
  1. The template is ready for use now.

_Note:_
  * The root section has the SectionType property set to `body`. This is neccessary for the system to function correctly.
  * Also remember that the section name you specify must match with the `doctype/@root` attribute in the doctype definition.
  * DO NOT delete the existing root section and add a new one. You should just RENAME the existing section, otherwise the SectionType property set on the root section will be lost.


Create a template for the new document type (you can modify and renmae the templates for the hansard or bill). The root section of the document must be renamed to the name of the document type. e.g. the root section of the bill document type is called 'bill' ; for the debaterecord document type it is called 'debaterecord'


## Create a Metadata Editor for the document type ##

The Metadata Editor is the screen that pops up when you create new instance of a document type. This screen captures the initial metadata of the document its name, its publication date, its country, language etc. This information is used to identify the document as a whole.

In typical circumstances you can use the GeneralMetadata editor class which provides a generic metadata editor UI for typically used AkomaNtoso metadata. Custom metadata editors can be written for a document type if you want to provide an editor for additional metadata variables. To do this you will have to write a JPanel UI class which extends `BaseEditorDocMetadataDialog` e.g :
```

public class MyDocTypeMetadata extends BaseEditorDocMetadataDialog {

....
}

```
A generic metadata metadata model is provided by the `GeneralMetadataModel` class. You can extend the general metadata model by writing your own class either by extending GeneralMetadataModel or BaseEditorDocMetaModel.  The new class needs to be registered for the document type using the document\_types.metadata\_model\_editor config setting (see below).

The metadata editor is registered in the `doc_types.xml` configuration which was described earlier, here is an example of the how the metadata editors are registered
for a doctype.

```
<doctype ...>
....
        <metadata-editors>
            <title xml:lang="eng">Bill Metadata</title>
            <metadata-editor class="org.bungeni.editor.metadata.editors.GeneralMetadata" >
                <title xml:lang="eng">General</title>
            </metadata-editor>
            <metadata-editor class="org.bungeni.editor.metadata.editors.BillMetadata" >
                <title xml:lang="eng">Bill</title>
            </metadata-editor>
        </metadata-editors>
....
</doctype>
```



## Registering Panels for the document type ##

The Editor's right hand pane has tabbed panels that allow the user to effect actions on the document. The available panels are packaged into the Editor application, but what panels are available for a doc type and in what order they are shown is done via panel configuration.

panels are managed in the  `settings/configs/panels` folder - here a panel config document per doc\_type is required. The name of the config panel document should be the same as the name of the doctype. Setting `state=0` will disable a panel , remove a panel definition completely willl also have the same effect.

```
<panels for="debaterecord">
    <panel name="actions" class="org.bungeni.editor.panels.loadable.documentActionPanel" state="1">
           <title xml:lang="eng">markup</title>
           <title xml:lang="spa">sugerir</title>
           <title xml:lang="ara">التقطيع</title>
    </panel>
    <panel name="allmeta" class="org.bungeni.editor.panels.loadable.MetadataPanel" state="1">
           <title xml:lang="eng">metadata</title>
           <title xml:lang="spa">metadatos</title>
           <title xml:lang="ara">واصفات البيانات</title>
    </panel>
    <panel name="structure" class="org.bungeni.editor.panels.loadable.documentStructurePanel" state="1">
           <title xml:lang="eng">structure</title>
           <title xml:lang="spa">estructura</title>
           <title xml:lang="ara">الهيكلية</title>
    </panel>
    <panel name="validate" class="org.bungeni.editor.panels.loadable.validateAndCheckPanel2" state="1">
           <title xml:lang="eng">validate</title>
           <title xml:lang="spa">validar</title>
           <title xml:lang="ara">التحقق من صحة المعلومات</title>
    </panel>
    <panel name="transform" class="org.bungeni.editor.panels.loadable.transformXMLPanel" state="1">
           <title xml:lang="eng">publish</title>
           <title xml:lang="spa">publicar</title>
           <title xml:lang="ara">النشر</title>
    </panel>
    <panel name="notes" class="org.bungeni.editor.panels.loadable.documentNotesPanel" state="1">
           <title xml:lang="eng">notes</title>
           <title xml:lang="spa">notas</title>
           <title xml:lang="ara">ملاحظات</title>
    </panel>
</panels>

```

## Enable available action conditions for the document type ##

Action Conditions are used for dynamically processing a document action conditionally. These Action Condition processors are used for evaluating whether a particular condition should be available on a document or not depending on the position of the cursor in the document.

For a list of available condition processors see this link : ConditionProcessorsInBungeniEditor

Condition processors are activated per document type, and this done in the `settings/configs/actions/` folder, where condition processors are specified per document type , e.g, the below activates the following condition processors for the `debaterecord` document type :
```
<?xml version="1.0" encoding="UTF-8"?>
<conditions for="debaterecord">
    <condition name="cursorInSection"
        class="org.bungeni.editor.toolbar.conditions.runnable.cursorInSection"/>
    <condition name="cursorInSectionType"
        class="org.bungeni.editor.toolbar.conditions.runnable.cursorInSectionType"/>
    <condition name="fieldExists"
        class="org.bungeni.editor.toolbar.conditions.runnable.fieldExists"/>
    <condition name="fieldNotExists"
        class="org.bungeni.editor.toolbar.conditions.runnable.fieldNotExists"/>
    <condition name="imageSelected"
        class="org.bungeni.editor.toolbar.conditions.runnable.imageSelected"/>
    <condition name="imageSelectedIsNot"
        class="org.bungeni.editor.toolbar.conditions.runnable.imageSelectedIsNot"/>
    <condition name="sectionExists"
        class="org.bungeni.editor.toolbar.conditions.runnable.sectionExists"/>
    <condition name="sectionHasChild"
        class="org.bungeni.editor.toolbar.conditions.runnable.sectionHasChild"/>
    <condition name="sectionNotExists"
        class="org.bungeni.editor.toolbar.conditions.runnable.sectionNotExists"/>
    <condition name="textSelected"
        class="org.bungeni.editor.toolbar.conditions.runnable.textSelected"/>
</conditions>

```