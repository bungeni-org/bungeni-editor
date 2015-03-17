# Introduction #

Every document marked up in the Bungeni Editor is associated with a document type. A document type has metadata associated to it.
The metadata is stored as ODF document properties - and the Editor provides an API and interface to edit this metadata.

## Metadata Model Editors ##

The metadata model for a document is editable via a model editor.

Model editors are defined per doc-type in the settings table `METADATA_MODEL_EDITORS`

|DOC\_TYPE|METADATA\_MODEL\_EDITOR|METADATA\_EDITOR\_TITLE|ORDER\_OF\_LOADING|
|:--------|:----------------------|:----------------------|:-----------------|
|judgement|	org.bungeni.editor.metadata.editors.GeneralMetadata|	General|	1|
|judgement|	org.bungeni.editor.metadata.editors.JudgementMetadata3|	Judgement|	3|
|debaterecord|	org.bungeni.editor.metadata.editors.ParliamentMetadata|	Parliament|	2|
|debaterecord|	org.bungeni.editor.metadata.editors.GeneralMetadata|	General|	1|
|bill|	org.bungeni.editor.metadata.editors.GeneralMetadata|	General	|1 |
|bill|	org.bungeni.editor.metadata.editors.ParliamentMetadata|	Parliament	|2 |
|bill|	org.bungeni.editor.metadata.editors.BillMetadata|	Bill	|3 |

In the above example - the `judgement` type defines 2 metadata model editors. These are JPanel classes which are loaded as tabs in the order defined in the `ORDER_OF_LOADING` column.

Every editor class is a JPanel form which implements the `IEditorDocMetadataDialog` :
The metadata model itself is captured in an object of type `IEditorDocMetaModel` .

For instance the `judgement` metadata model editor class defines a model as follows :

```
public class JudgementMetadata3 extends BaseEditorDocMetadataDialog {
....
    JudgementMetadataModel docMetaModel = new JudgementMetadataModel();
...
```

In the above example `JudgementMetadata3` is a JPanel form class that describes the UI for the metadata editor.
The `BaseEditorDocMetadataDialog` is a helper class that extends `JPanel` and implements the `IEditorDocMetadataDialog` interface :

```
public interface IEditorDocMetadataDialog {
   public void initVariables(OOComponentHelper ooDoc, JFrame parentFrm, SelectorDialogModes dlgMode) ;
   public void initialize();
   public Component getPanelComponent();
   public Dimension getFrameSize();
   public void setTabTitle(String sTitle);
   public String getTabTitle();
   public boolean applySelectedMetadata(BungeniFileSavePathFormat spf);
   public ArrayList<String> validateSelectedMetadata(BungeniFileSavePathFormat spf);
}
```

The `JudgementMetadataModel` class sets up the actual metadata model and also provides methods to save the metadata into the document.

```
public class JudgementMetadataModel extends BaseEditorDocMetaModel {
....
}
```

The `BaseEditorDocMetaModel` defines a `setup()` method which sets up the metadata. For instance - the following is the setup method from the base class:

```

public class BaseEditorDocMetadataModel  .... {
.......
........
    public void setup() {

        // document related
        docMeta.put("BungeniDocType", "");
        docMeta.put("BungeniPublicationName", "");
        docMeta.put("BungeniPublicationNameFull", "");
        docMeta.put("BungeniPublicationDate", "");
        docMeta.put("BungeniDocPart", "");
        docMeta.put("BungeniCountryCode", "");
        docMeta.put("BungeniLanguageCode", "");

        // FRBR Items
        // work
        docMeta.put("BungeniWorkAuthor", "");
        docMeta.put("BungeniWorkAuthorURI", "");
        docMeta.put("BungeniWorkDate", "");
        docMeta.put("BungeniWorkDateName", "");
        docMeta.put("BungeniWorkURI", "");

        // expression
        docMeta.put("BungeniExpAuthor", "");
        docMeta.put("BungeniExpAuthorURI", "");
        docMeta.put("BungeniExpDate", "");
        docMeta.put("BungeniExpDateName", "");
        docMeta.put("BungeniExpURI", "");

        // manifestation
        docMeta.put("BungeniManAuthor", "");
        docMeta.put("BungeniManAuthorURI", "");
        docMeta.put("BungeniManDate", "");
        docMeta.put("BungeniManDateName", "");
        docMeta.put("BungeniManURI", "");
	}
}

```

The base class defines only the bare minimum metadata common to all Editor documents.
Specific metadata models may derive from the base metadata model and extend it.

For example, looking again at the `JudgementMetadataModel` , it extends the base model's `setup()` method , and provides three additional metadata attributes of its own.

```
public class JudgementMetadataModel extends BaseEditorDocMetaModel {
    public static final String[] THIS_METAMODEL   = { "BungeniJudgementNo", "BungeniCaseNo", "BungeniJudgementDate" };

  
    @Override
    public void setup() {
        super.setup();

        for (String sMeta : THIS_METAMODEL) {
            this.docMeta.put(sMeta, "");
        }
    }

......
```

The metadata attribute can be set using the model's `updateItem` API :
```
public boolean applySelectedMetadata(BungeniFileSavePathFormat spf){
    .....
    	String sJudgementNo = this.txtJudgementNo.getText();
        docMetaModel.updateItem("BungeniJudgementNo", sJudgementNo);
    .......
}
```




