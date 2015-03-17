

# 1 Define a toolbar action in toolbar xml #

## Elements in a toolbar xml config ##

  * _toolbar - this is a required root element in all action configurations
  *_root_- All the actions are contained and grouped within the root element.  The root element suports a 'xsd:lang' attribute - and this allows localization of the action configurations.
  *_actionGroup_- this element allows grouping contained actions into UI tabs
  *_blockAction_- this element is rendered as a tab panel and contains actions
  *_action/subAction_- these elements map to actual actions and appear as buttons on the UI._

_Figure : Toolbar XML Actions mapped to the rendered UI_
<img src='http://bungeni-editor.googlecode.com/files/Toolbar_Actions.png' alt='Toolbar XML Actions mapped to the rendered UI' />


## Adding a actionGroup ##

The actionGroup element is container block that encloses multiple blockActions.

The actionGroup gets rendered as a parent level tab. The tab structure is controlled via the **uimodel** parameter.

Example :

```

 <actionGroup name="tabFavourites" visible="true" uimodel="wrap">
            <title xml:lang="eng">Favourites</title>

```


`uimodel` - can take 2 values either `wrap` or `scroll`

## Localization ##

The `title` attribute has been deprecated for `root`, `blockAction`, `action` and `subaction` as it is not suitable for localization.

Instead now `title` is an element which takes a `xml:lang` attribute.

The below example shows a `blockAction` with titles in different languages.

```
    <blockAction name="QA" tooltip="Create a QA Section" title="Papers" 
    	target="null" 
    	visible="true" 
    	condition="none" >
		<title xml:lang="eng">Papers</title>
		<title xml:lang="swa">Karatasi</title>
		<title xml:lang="fra">Carta</title>
		
```

Additionally the `tooltip` attribute has been deprecated and replaced with a `tooltip` element.

The editor property `GENERAL_EDITOR_PROPERTIES.locale.Language.iso639-2` is used to retrieve the title for the appropriate language.



## Adding a blockAction ##

Add a block action for the action.  A complete block action is illustrated below.

Example:
```

    <blockAction name="QA" tooltip="Create a QA Section" title="Papers" 
    	target="null" 
    	visible="true" 
    	condition="none" >
    	
        <action name="QA.selection" 
		title="From Selection" 
		mode="TEXT_SELECTED_INSERT" 
		target="toolbarSubAction.makeQA.section_creation" 
		condition="sectionNotExists:qa :and: textSelected:true" 
		visible="true" showChildren="true" >
		
                    <subaction name="QA.selection.title" 
				tooltip="Markup selection as title" 
				title="Markup Title" 
				mode="TEXT_SELECTED_INSERT" 
				target="toolbarSubAction.makeQASection.apply_style:qa" 
				visible="true" 
				condition="cursorInSection:qa :and: textSelected:true" />
				
                 </action>
    </blockAction>
```

The blockAction element is described here.

```

    <blockAction name="QA" tooltip="Create a QA Section" title="Papers" 
    	target="null" 
    	visible="true" 
    	condition="none" >
```
`name` - the name attribute

~~`tooltip` - the tooltip text to display when mousing over the block action in the toolbar~~ (deprecated)

~~`title` - the title text displayed in the toolbar~~ (deprecated)

`visible` - true / false to display / hide the element

`target` - the address of the handler that is invoked upon clicking the toolbar item. Use _null_  - to indicate no action.

`condition` - conditions under which the action is displayed. If the condition evaluates to true - the button is enabled for clicking, if the condition evaluates to false, the button is in a disabled state. For condition processor documentation and syntax see: [Condition Processors](http://code.google.com/p/bungeni-editor/wiki/ConditionProcessorsInBungeniEditor)

## Adding a Action element ##

blockActions can contain `<action>` elements.

The action element is described here.

```
   <action name="QA.selection" 
		mode="TEXT_SELECTED_INSERT" 
		target="toolbarSubAction.makeQA.section_creation" 
		condition="sectionNotExists:qa :and: textSelected:true" 
		visible="true" showChildren="true" >
```

`name` - the name of the action element

~~`title` - the title text displayed in the toolbar for the action~~

`mode` - applicable modes for this action. For modes and their description, see BungeniEditorModes

`target` - same as blockAction. in the example below, the target is :
```
target="toolbarSubAction.makeQA.section_creation"
```

The target syntax is a combination of "action\_type.parent\_action\_name.sub\_action\_name" , out of these action\_type can either be toolbarAction or toolbarSubAction, the other two values correspond to parent action, and sub action entries for the  target in the SUB\_ACTION\_SETTINGS table.

> (There are 2 kinds of target actions possible, those that are defined in 'ACTION\_SETTINGS' in the BungeniEditorSettingsDatabase and those that are defined in 'SUB\_ACTION\_SETTINGS'.
For differences between the two action types see BungeniEditorActionTypes).



`showChildren`  - true / false enables/disables dispaly of child actions



## Adding a subAction element ##

`<action>` elements can contain `<subAction>` elements.
The subAction element is described here:

```
<subaction name="QA.selection.title" 
	tooltip="Markup selection as title" 
	title="Markup Title" 
	mode="TEXT_SELECTED_INSERT" 
	target="toolbarSubAction.makeQASection.apply_style:qa" 
	visible="true" 
	condition="cursorInSection:qa :and: textSelected:true" />
```

The attributes function exactly as described for the `<action>` element


## Schema for toolbar xml config ##

See this [link](http://code.google.com/p/bungeni-editor/source/browse/BungeniEditor/trunk/BungeniEditorClient/src/xsd/toolbar.xsd)
```
<?xml version="1.0" encoding="UTF-8"?>

<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" elementFormDefault="qualified">
  <xs:import namespace="http://www.w3.org/XML/1998/namespace"
   schemaLocation="http://www.w3.org/2001/03/xml.xsd"/>
  <xs:element name="toolbar">
    <xs:complexType>
      <xs:sequence>
        <xs:element ref="root"/>
      </xs:sequence>
      <xs:attribute name="doctype" use="required" type="xs:NCName"/>
    </xs:complexType>
  </xs:element>
  <xs:element name="root">
    <xs:complexType>
      <xs:sequence>
        <xs:element ref="title" minOccurs="1"/>
        <xs:element ref="tooltip" minOccurs="0"/>
        <xs:element ref="actionGroup"/>
      </xs:sequence>
      <xs:attribute name="visible" use="required" type="xs:boolean"/>
    </xs:complexType>
  </xs:element>
  <xs:element name="actionGroup">
    <xs:complexType>
      <xs:sequence>
        <xs:element ref="title" minOccurs="1"/>
        <xs:element ref="tooltip" minOccurs="0"/>
        <xs:element maxOccurs="unbounded" ref="blockAction"/>
      </xs:sequence>
      <xs:attribute name="name" use="required" type="xs:NCName"/>
        <xs:attribute name="uimodel" use="required" >
          <xs:simpleType>
            <xs:restriction base="xs:string">
              <xs:enumeration value="wrap"/>
              <xs:enumeration value="scroll"/>
            </xs:restriction>
          </xs:simpleType>
        </xs:attribute>
      <xs:attribute name="visible" use="required" type="xs:boolean"/>
    </xs:complexType>
  </xs:element>
  <xs:element name="blockAction">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="1" ref="title"/>
        <xs:element ref="tooltip" minOccurs="0"/>
        <xs:element maxOccurs="unbounded" ref="subaction"/>
      </xs:sequence>
      <xs:attribute name="condition" use="required" type="xs:NCName"/>
      <xs:attribute name="name" use="required" type="xs:NCName"/>
      <xs:attribute name="target" use="required" type="xs:NCName"/>
      <xs:attribute name="visible" use="required" type="xs:boolean"/>
    </xs:complexType>
  </xs:element>
  <xs:element name="subaction">
    <xs:complexType>
      <xs:sequence>
        <xs:element ref="title" minOccurs="1"/>
        <xs:element ref="tooltip" minOccurs="0"/>
      </xs:sequence>
      <xs:attribute name="condition" use="required" type="xs:anyURI"/>
      <xs:attribute name="mode" use="required" type="xs:NCName"/>
      <xs:attribute name="name" use="required" type="xs:NCName"/>
      <xs:attribute name="target" use="required"/>
      <xs:attribute name="title"/>
      <xs:attribute name="tooltip" use="required"/>
      <xs:attribute name="visible" use="required" type="xs:boolean"/>
    </xs:complexType>
  </xs:element>
  <xs:element name="title">
    <xs:complexType mixed="true">
      <xs:attribute ref="xml:lang"/>
    </xs:complexType>
  </xs:element>
  <xs:element name="tooltip">
    <xs:complexType mixed="true">
      <xs:attribute ref="xml:lang"/>
    </xs:complexType>
  </xs:element>
</xs:schema>

```



# 2 Calling a router action in response to a toolbar action #

Typically router actions, act on the document without requesting user interaction.
The router action is defined for the subAction in the ROUTER\_CLASS column for the subAction in the 'SUB\_ACTION\_SETTINGS' table.
For information on how to write a router action, see HowToWriteaRouterAction.

# 3. Invoking dialogs in response to a toolbar action #

Clicking a toolbar action, can invoke the dialog that interacts with the currently selected openoffice document. This dialog (a JPanel) needs to extend the `selectorTemplatePanel` class and implement the `IBungeniForm` interface:

```
public  class InitPapers extends selectorTemplatePanel implements IBungeniForm {
.......
}
```

At runtime the Bungeni Editor invokes the panel by creating JDialog object and adding the panel to the JDialog.

for e.g. :
```
     dlg= new JDialog();
     dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
     /*
     dialog panel class loaded and initialized here. the class name is retrieved from the toolbarSubAction object
    */
     IDialogSelector panel = DialogSelectorFactory.getDialogClass(subAction.dialog_class());
     panel.initObject(ooDocument, dlg, action, subAction);
     dlg.getContentPane().add(panel.getPanel());
     dlg.pack();
     dlg.setLocationRelativeTo(null);
     dlg.setVisible(true);
     dlg.setAlwaysOnTop(true);   
```

## Dialog class implementation requirements ##

### Over-ride methods ###

The dialog class must necessarily extend the following methods :

This is used to initialize the dialog class :

```
public void initObject(OOComponentHelper ooDoc, JDialog dlg, toolbarAction act, toolbarSubAction subAct) {
    super.initObject( ooDoc, dlg, act, subAct);
    init();
    setControlModes();
}
```

This is invoked by the dialog class:

```
public void init(){
    super.init();
    initComponents();
    initFields();
}
```

This sets the formContext used by the form's command chain.  This is invoked by the base class init() function.

```
public void createContext(){
      super.createContext();
      formContext.setBungeniForm(this);
  }

```

This needs to be overriden by the derived class so the form's command chain handler can retrieve the appropriate command catalog based on the dialog class hierarchy.

```
public String getClassName(){
    return this.getClass().getName();
}
```

### Naming form controls ###

For the contextual enabling/disabling of form controls, the form controls need to be explicitly named. This can be done using the dialog editor interface in netbeans (set the name attribute for the controls.).  The table 'ACTION\_MODES' allows setting of control states based on mode.

|DOC\_TYPE| ACTION\_NAME |ACTION\_MODE |MODE\_HIDDEN\_FIELD |SUB\_ACTION\_NAME|
|:--------|:-------------|:------------|:-------------------|:----------------|
|debaterecord |makePrayerSection |TEXT\_SELECTED\_INSERT |txt\_initdebate\_selectlogo |debatedate\_entry|
|debaterecord |makePrayerSection |TEXT\_SELECTED\_INSERT |lbl\_initdebate\_selectlogo |debatedate\_entry|
|debaterecord |makePrayerSection |TEXT\_SELECTED\_INSERT |txt\_initdebate\_selectlogo |debatetime\_entry|
|debaterecord |makePrayerSection |TEXT\_SELECTED\_INSERT |lbl\_initdebate\_selectlogo |debatetime\_entry|
|debaterecord |makePrayerSection |TEXT\_SELECTED\_INSERT |dt\_initdebate\_timeofhansard |debatedate\_entry|
|debaterecord |makePrayerSection |TEXT\_SELECTED\_INSERT |dt\_initdebate\_hansarddate |debatetime\_entry|
|debaterecord |makePaperSection |TEXT\_SELECTED\_INSERT |lbl\_initpapers\_title |none|
|debaterecord |makePaperSection |TEXT\_SELECTED\_INSERT |txt\_initpapers\_title |none|

For e.g. the following settings:

|DOC\_TYPE| ACTION\_NAME |ACTION\_MODE |MODE\_HIDDEN\_FIELD |SUB\_ACTION\_NAME|
|:--------|:-------------|:------------|:-------------------|:----------------|
|debaterecord |makePrayerSection |TEXT\_SELECTED\_INSERT |txt\_initdebate\_selectlogo |debatedate\_entry|
|debaterecord |makePrayerSection |TEXT\_SELECTED\_INSERT |lbl\_initdebate\_selectlogo |debatedate\_entry|
|debaterecord |makePrayerSection |TEXT\_SELECTED\_INSERT |dt\_initdebate\_timeofhansard |debatedate\_entry|

indicate that the controls named,

|txt\_initdebate\_selectlogo |
|:---------------------------|
|lbl\_initdebate\_selectlogo |
|dt\_initdebate\_timeofhansard |

for the sub\_action named 'debatedate\_entry', will be hidden in TEXT\_SELECTED\_INSERT mode.

### Handling form Submissions ###

Form submission is handled in the forms via a standard call back mechanism.  The base class 'selectorTemplatePanel' provides the following overridable API to trap events in specific modes :

```

 /** events invoked for TEXT_EDIT mode **/ 
  public boolean preFullEdit();
  public boolean postFullEdit();
  public boolean processFullEdit();

 /** events invoked for TEXT_INSERTION mode **/  
  public boolean preFullInsert();
  public boolean postFullInsert();
  public boolean processFullInsert();
  
 /** events invoked for TEXT_SELECTED_INSERT mode **/
  public boolean preSelectInsert();
  public boolean postSelectInsert();
  public boolean processSelectInsert();
 
```

The API functions are always invoked in the following order :

pre<mode function>() ==> process<mode function>() ==> post<mode function>()




## Connecting the dialog class to the toolbar action ##

The dialog class is invoked via a standard interface by the bungeni editor. The above class for example needs to be specified in the `DIALOG_CLASS` column in the appropriate entry for the subAction in SUB\_ACTION\_SETTINGS table.