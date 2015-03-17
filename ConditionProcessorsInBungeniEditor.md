

# Introduction #

The Bungeni editor has a contextual toolbar driven editing interface that changes state based on the cursor position in the document. The behaviour of the toolbar is determined by a set of rule-based conditions defined in the settings, and activated via entries in toolbar.xml


# Operators #

Currently the ":and:" and ":or:" operators are supported by the toolbar xml syntax, and allow chaining of conditions.
e.g. you can chain as follows :

```
cursorInSection:mainsection :and: sectionExists:sectionname
```

A negation operator ("!") is also allowed to negate conditions :

```
!cursorInSection:mainsection :and: !sectionExists:asection
```


# Writing a new Conditon #

To write a new condition, the class must implement the IBungeniToolbarCondition interface, and implement 2 methods:
`setOOComponentHelper()` - which allows passing the openoffice context object to the condition processor, and a ;
`processCondition()` - function that returns a boolean value indicating whether the condition was true or false. THe processCondition() function receives a BungeniToolbarCondition object which is created and passed in by the editor.
for e.g. if the condition in toolbar.xml was: tableExists:tblFigures
here the condition name is = tableExists
and the condition value is = tblFigures
this value is used by the processCondition() function to evaluate the condition.

See the package: org.bungeni.editor.toolbar.conditions.runnable in the BungeniEditor to see examples of conditions.

# Registering a Conditon #

A condition needs to be registered once the condition has been written and compiled.
This is done in the settings database in the table: `toolbar_conditions`

The new condition must be registered here witht he following parameters.
`DocType` - which doctype is this condition registered for
`ConditionName` - this is the class name of the condition (for example if the condition class was called tableExists, then the condition name must be the same)
`ConditonClass` - this is the full canoncial class path to the condition class,
for example if the condition tableExists was created in a package called :

`org.bungeni.plugins.toolbarconditions`

then the Condition Class for the conditon would be:

`org.bungeni.plugins.toolbarconditions.tableExists`

Once a condition has been registered in the settings database it is available for use in toolbar.xml, and can be chained with other conditions using operators.

# Currently Available Conditions #

## cursorInSection ##

**parameter** - comma separated list of section names sectionName,sectionNameAnother,...

example:
```
cursorInSection:sectionQuestion
```

the above returns true if the cursor is in a section called sectionQuestion.

sectionName can be a regular expression pattern:

example:
```
cursorInSection:sectionQ.
```

or

```
cursorInSection:section[0-9]*,question[0-9]*
```

will match "section1", "section2" ...or "question1", "question2" ... etc


the above returns true for all section names matching the sectionQ. pattern :
sectionQuestion, sectionQuestionAnswer, sectionQA...etc..


## fieldExists / fieldNotExists ##

**parameter** - fieldName

example:
```
fieldExists:fieldName
```

## imageSelected ##

**parameter** - imageName

example:
```
imageSelected:imageName
```
evaluates to true if imageSelected is `imageName`


## imageSelectedIsNot ##

**parameter** - imageName

example:
```
imageSelectedIsNot:imageName
```
evaluates to true if image Selected is not `imageName`


## sectionExists / sectionNotExists ##

**parameter** - sectionName

example:
```
sectionExists:sectionName
```
evaluates to true if the section `sectionName` exists in the document
sectionName can be a regular expression pattern.

## textSelected ##

**parameter** - true or false

example:
```
textSelected:true
```
evaluates to true if text was selected

## Condition usage examples ##

Chained conditions :

```
condition="cursorInSection:masthead :and: textSelected:true :and: fieldNotExists:debaterecord_official_date"
```


## Example action xml file ##

```
<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Action configuration for debate-records
-->
<toolbar xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:noNamespaceSchemaLocation="../../src/xsd/toolbar.xsd"
 doctype="debaterecord">
    <root visible="true">
        <actionGroup name="tabFavourites" 
            visible="true" 
            uimodel="wrap">
            <title xml:lang="eng">generic</title>
            <blockAction name="Speeches" 
                target="null" 
                visible="true" 
                condition="none">
                <title xml:lang="eng">Speech</title>
                <title xml:lang="fra">FR-Speech</title>
                <title xml:lang="spa">Discurso</title>
                <action name="PersonalStatement.create" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makePersonalStatement.section_creation" 
                    visible="true"
                    condition="textSelected:true :and:
                                !cursorInSection:speech[0-9]* :and: 
                                cursorInSection:debaterecord">
                    <title xml:lang="eng">personal statement</title>
                    <tooltip xml:lang="eng">Personal Statement </tooltip>
                </action>

                <subaction name="Heading.PersonalStatement" 
                    tooltip="Heading for personal statement"
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeCommunicationAction.apply_style:heading_pstatement"
                    visible="true"
                    condition="cursorInSection:pstatemnt[0-9]* :and: 
                                textSelected:true">
                    <title xml:lang="eng">---- heading</title>
                    <tooltip xml:lang="eng">Mark selected text as Heading </tooltip>
                </subaction>
                <action name="MinStatement.create" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeMinisterialStatement.section_creation"
                    visible="true"
                    condition="textSelected:true :and: 
                                !cursorInSection:speech[0-9]* :and: 
                                cursorInSection:debaterecord">
                    <title xml:lang="eng">ministerial statement</title>
                    <tooltip xml:lang="eng">Mark selected text as Ministerial Statement </tooltip>
                </action>
                <subaction name="Heading.MinStatement" 
                    tooltip="Heading for ministerial statement"
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeCommunicationAction.apply_style:heading_mstatement"
                    visible="true"
                    condition="cursorInSection:mstatemnt[0-9]* :and: 
                                textSelected:true">
                    <title xml:lang="eng">---- heading</title>
                    <tooltip xml:lang="eng">Mark selected text as Heading </tooltip>
                </subaction>
                <action name="Speech.create" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateSpeechBlockSection.section_creation"
                    visible="true" 
                    condition="textSelected:true :and: 
                                !cursorInSection:speech[0-9]*">
                    <title xml:lang="eng">speech section</title>
                    <tooltip xml:lang="eng">Mark selection as a Speech </tooltip>
                </action>

                 <action name="Speech.listitem"
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateSpeechBlockSection.char_style:an-list-item"
                    visible="true"
                    condition="textSelected:true :and:
                                cursorInSection:speech[0-9]*,spbody[0-9]*">
                    <title xml:lang="eng">---- list-item</title>
                    <tooltip xml:lang="eng">Identify selected text as speech author's name
                    </tooltip>
                </action>

                <action name="Speech.SpeechBy" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateSpeechBlockSection.speech_by" 
                    visible="true"
                    condition="textSelected:true :and:
                                cursorInSection:speech[0-9]*">
                    <title xml:lang="eng">---- by</title>
                    <tooltip xml:lang="eng">Identify selected text as speech author's name
                    </tooltip>
                </action>
  
                <action name="Speech.SpeechBody" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateSpeechBlockSection.speech_body" 
                    visible="true"
                    condition="textSelected:true :and:
                               cursorInSection:speech[0-9]* :and:
                               !sectionHasChild:spbody[0-9]*">
                    <title xml:lang="eng">---- body</title>
                    <tooltip xml:lang="eng">Select and mark speech body
                    </tooltip>
                </action>
                <!-- markup a list item number in a speech, these action target names need to rationalized -->
                <action name="Speech.body.item_number"
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateQuestionBlockSection.markup_question_no"
                    visible="true"
                    condition="textSelected:true :and: 
                    cursorInSection:spbody[0-9]*">
                    <title xml:lang="eng">---- item num</title>
                    <tooltip xml:lang="eng">Item Number </tooltip>
                </action>
                
            </blockAction>
            <blockAction name="Communication" 
                    target="null" 
                    visible="true" 
                    condition="none">
                <title xml:lang="eng">communication</title>
                <subaction name="Communication.fromTheChair" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeCommunicationAction.section_creation"
                    visible="true" 
                    condition="cursorInSection:debaterecord :and: 
                              textSelected:true">
                    <title xml:lang="eng">communication from the chair</title>
                    <tooltip xml:lang="eng">Mark selection as communication from the chair
                    </tooltip>
                </subaction>
                <subaction name="Heading.Communication.fromTheChair" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeCommunicationAction.apply_style:heading_comchair"
                    visible="true" 
                    condition="cursorInSection:comnctn[0-9]* :and: 
                                textSelected:true">
                    <title xml:lang="eng">---- heading</title>
                    <tooltip xml:lang="eng">Mark selection as Heading </tooltip>
                </subaction>
                <subaction name="Communication.pointOfOrder" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makePointOfOrder.section_creation" 
                    visible="true"
                    condition="cursorInSection:debaterecord :and: 
                                textSelected:true">
                    <title xml:lang="eng">point of order</title>
                    <tooltip xml:lang="eng">Mark selection as Point of Order </tooltip>
                </subaction>
                <subaction name="Heading.Communication.pointOfOrder" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makePointOfOrder.apply_style:heading_pointoforder"
                    visible="true" 
                    condition="cursorInSection:porder[0-9]* :and: 
                                textSelected:true">
                    <title xml:lang="eng">---- heading</title>
                    <tooltip xml:lang="eng">Mark Selection as Heading </tooltip>
                </subaction>
            </blockAction>
            <blockAction name="GeneralActions" 
                target="null" 
                visible="true" 
                condition="none">
                <title xml:lang="eng">general actions</title>
                <subaction name="Root" 
                    visible="true"
                    condition="sectionNotExists:debaterecord :and: textSelected:true"
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateMastheadSection.create_root_section:debaterecord">
                    <title xml:lang="eng">create root section</title>
                    <tooltip xml:lang="eng">Add root section to the Document </tooltip>
                </subaction>

                <action name="Observation.create" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeObservationAction.section_creation" 
                    visible="true"
                    condition="textSelected:true">
                    <title xml:lang="eng">observation</title>
                    <tooltip xml:lang="eng">Mark selection as Observation </tooltip>
                </action>
            </blockAction>

            <blockAction name="EventActions" 
                target="null" 
                visible="true" 
                condition="none">
                <title xml:lang="eng">event actions</title>
                <subaction name="QuestionProposed" 
                    visible="true"
                    condition="textSelected:true :and: 
                                !cursorInSection:actionevt[0-9]*"
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeActionEvent.section_creation:QuestionProposed">
                    <title xml:lang="eng">question proposed</title>
                    <tooltip xml:lang="eng">Mark event : Question Proosed </tooltip>
                </subaction>
                <subaction name="QuestionAccepted" 
                    visible="true"
                    condition="textSelected:true :and: 
                                !cursorInSection:actionevt[0-9]*"
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeActionEvent.section_creation:QuestionAccepted">
                    <title xml:lang="eng">question accepted</title>
                    <tooltip xml:lang="eng">Mark event : Question Accepted </tooltip>
                </subaction>
                <subaction name="QuestionWithdrawn" 
                    visible="true"
                    condition="textSelected:true :and: 
                                !cursorInSection:actionevt[0-9]*"
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeActionEvent.section_creation:QuestionWithdrawn">
                    <title xml:lang="eng">question withdrawn</title>
                    <tooltip xml:lang="eng">Mark event : Question Withdrawn </tooltip>
                </subaction>
                <subaction name="QuestionDeferred" 
                    visible="true"
                    condition="textSelected:true :and: 
                                !cursorInSection:actionevt[0-9]*"
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeActionEvent.section_creation:QuestionDeferred">
                    <title xml:lang="eng">question deferred</title>
                    <tooltip xml:lang="eng">Mark event : Question Deferred </tooltip>
                </subaction>
                <subaction name="DebateInterruption" 
                    visible="true"
                    condition="textSelected:true :and: 
                                !cursorInSection:actionevt[0-9]*"
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeActionEvent.section_creation:DebateInterruption">
                    <title xml:lang="eng">debate interruption</title>
                    <tooltip xml:lang="eng">Mark event : Debate Interruption </tooltip>
                </subaction>
                <subaction name="DebateResumption" 
                    visible="true"
                    condition="textSelected:true :and: 
                                !cursorInSection:actionevt[0-9]*"
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeActionEvent.section_creation:DebateResumption">
                    <title xml:lang="eng">debate resumption</title>
                    <tooltip xml:lang="eng">Mark event : Debate Resumption </tooltip>
                </subaction>
                <subaction name="BillFirstReading" 
                    visible="true"
                    condition="textSelected:true :and: 
                                !cursorInSection:actionevt[0-9]*"
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeActionEvent.section_creation:BillFirstReading">
                    <title xml:lang="eng">bill 1st reading</title>
                    <tooltip xml:lang="eng">Mark Event : Bill 1st Reading </tooltip>
                </subaction>
                <subaction name="BillSecondReading" 
                    visible="true"
                    condition="textSelected:true :and: 
                                !cursorInSection:actionevt[0-9]*"
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeActionEvent.section_creation:BillSecondReading">
                    <title xml:lang="eng">bill 2nd reading</title>
                    <tooltip xml:lang="eng">Mark Event : Bill 2nd Reading </tooltip>
                </subaction>
                <subaction name="BillThirdReading" 
                    visible="true"
                    condition="textSelected:true :and: 
                                !cursorInSection:actionevt[0-9]*"
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeActionEvent.section_creation:BillThirdReading">
                    <title xml:lang="eng">bill 3nd reading</title>
                    <tooltip xml:lang="eng">Mark Event : Bill 3rd Reading </tooltip>
                </subaction>
            </blockAction>
        </actionGroup>

        <actionGroup name="tabOthers" 
            visible="true" 
            uimodel="wrap">
            
            <title xml:lang="eng">Structural</title>
            
            <blockAction name="Masthead.Selection" 
                tooltip="Masthead for debaterecord" 
                target="null"
                visible="true" 
                condition="none" 
                showChildren="true">
                <title xml:lang="eng">preface</title>
                <subaction name="Masthead.selection"
                    tooltip="Select some text to mark it up as a Part" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateMastheadSection.section_creation"
                    visible="true"
                    condition="cursorInSection:debaterecord :and: 
                                textSelected:true :and: 
                                sectionNotExists:preface">
                    <title xml:lang="eng">preface section</title>
                    <tooltip xml:lang="eng">Mark selection as Preface </tooltip>
                </subaction>
                <subaction name="Masthead.heading" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateMastheadSection.markup_preface_heading:heading_preface"
                    visible="true" 
                    condition="cursorInSection:preface :and: 
                                textSelected:true">
                    <title xml:lang="eng">---- heading</title>
                    <tooltip xml:lang="eng">Mark selection as Heading </tooltip>
                </subaction>
                <subaction name="Masthead.subheading" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateMastheadSection.markup_preface_heading:subheading_preface"
                    visible="true" 
                    condition="cursorInSection:preface :and: 
                                textSelected:true">
                    <title xml:lang="eng">---- -- sub-heading</title>
                    <tooltip xml:lang="eng">Mark selection as Sub Heading </tooltip>
                </subaction>
                <subaction name="Masthead.selection.logo"
                    tooltip="Select some text to mark it up as a preface logo"
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateMastheadSection.markup_logo:main_logo"
                    visible="true" 
                    condition="imageSelectedIsNot:main_logo">
                    <title xml:lang="eng">---- logo</title>
                    <tooltip xml:lang="eng">Select an Image and mark it as the preface logo
                    </tooltip>
                </subaction>
            </blockAction>

            <blockAction name="Papers" 
                target="null" 
                visible="true" 
                condition="none">
                <title xml:lang="eng">papers laid</title>
                <action name="Papers.new" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makePapersLaidBlockSection.section_creation"
                    visible="true" 
                    condition="textSelected:true :and: 
                                cursorInSection:debaterecord">
                    <title xml:lang="eng">papers laid section</title>
                    <tooltip xml:lang="eng">Mark selection to identify papers laid during the debate
                    </tooltip>
                </action>
                <action name="Papers.heading" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makePapersLaidBlockSection.markup_papers_heading:heading_papers"
                    visible="true" 
                    condition="textSelected:true :and: 
                                cursorInSection:papers[0-9]*">
                    <title xml:lang="eng">---- heading</title>
                    <tooltip xml:lang="eng">Mark selection as Heading</tooltip>
                </action>
                <action name="Papers.subheading" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makePapersLaidBlockSection.markup_papers_heading:subheading_papers"
                    visible="true" 
                    condition="textSelected:true :and: 
                                cursorInSection:papers[0-9]*">
                    <title xml:lang="eng">---- -- sub-heading</title>
                    <tooltip xml:lang="eng">Mark selection as Sub Heading</tooltip>
                </action>
                <action name="Papers.doclist" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makePapersLaidListSection.section_creation"
                    visible="true" 
                    condition="textSelected:true :and: 
                                cursorInSection:papers[0-9]*">
                    <title xml:lang="eng">---- documents sub-section</title>
                    <tooltip xml:lang="eng">Mark selection as a list of presented documents
                    </tooltip>
                </action>
                <action name="Papers.select" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makePapersLaidBlockSection.select_documents"
                    visible="true" 
                    condition="cursorInSection:tbldocs[0-9]*">
                    <title xml:lang="eng">---- -- import listing</title>
                    <tooltip xml:lang="eng">Import listing from a DB </tooltip>
                </action>
                <action name="Papers.selectSingle" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makePapersLaidMarkupSingle.markup_doc_link"
                    visible="true" 
                    condition="textSelected:true :and: 
                                cursorInSection:tbldocs[0-9]*">
                    <title xml:lang="eng">---- -- select/markup link</title>
                    <tooltip xml:lang="eng">Mark selection as hyperlink to an existing tabled
                        document </tooltip>
                </action>
                <action name="Papers.selectSingleData" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makePapersLaidDataEntry.markup_doc_link" 
                    visible="true"
                    condition="textSelected:true :and: 
                                cursorInSection:tbldocs[0-9]*">
                    <title xml:lang="eng">---- -- add/markup link</title>
                    <tooltip xml:lang="eng">Add hyperlink to document </tooltip>
                </action>
            </blockAction>

            <blockAction name="Notices of Motion" 
                target="null" 
                visible="true" 
                condition="none">
                <title xml:lang="eng">notices of motions</title>
                <action name="MotionsGroup.new" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateMotionGroupSection.section_creation"
                    visible="true" 
                    condition="textSelected:true :and: 
                                cursorInSection:debaterecord">
                    <title xml:lang="eng">motions section</title>
                    <tooltip xml:lang="eng">Mark selection as a motion</tooltip>
                </action>
                <action name="AllMotions.heading" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateMotionBlockSection.markup_motion_title:heading_noticesofmotion"
                    visible="true"
                    condition="textSelected:true :and: 
                                cursorInSection:noticesmotion[0-9]*">
                    <title xml:lang="eng">---- heading</title>
                    <tooltip xml:lang="eng">Mark selection as a heading </tooltip>
                </action>
                <action name="Motions.new" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateMotionBlockSection.section_creation"
                    visible="true"
                    condition="textSelected:true :and: 
                                cursorInSection:noticesmotion[0-9]*">
                    <title xml:lang="eng">import motion</title>
                    <tooltip xml:lang="eng">Import Motion from DB</tooltip>
                </action>
                <action name="Motions.new" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateMotionBlockSectionEdit.section_creation"
                    visible="true"
                    condition="textSelected:true :and: 
                                cursorInSection:noticesmotion[0-9]*">
                    <title xml:lang="eng">add motion</title>
                    <tooltip xml:lang="eng">Personal Statement </tooltip>
                </action>
                <action name="Motions.heading" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateMotionBlockSection.markup_motion_title:heading_motion"
                    visible="true"
                    condition="textSelected:true :and: 
                                cursorInSection:noticeofmotion[0-9]*">
                    <title xml:lang="eng">---- heading</title>
                    <tooltip xml:lang="eng">Personal Statement </tooltip>
                </action>
                <action name="Motions.subheading" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateMotionBlockSection.markup_motion_title:subheading_motion"
                    visible="true"
                    condition="textSelected:true :and: 
                                cursorInSection:noticeofmotion[0-9]*">
                    <title xml:lang="eng">---- -- subheading</title>
                    <tooltip xml:lang="eng">Personal Statement </tooltip>
                </action>
                <!---org.bungeni.editor.selectors.debaterecord.committees.Main == missing -->
                <action name="Motions.markCommittee" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeSelectCommittee.markup_committee" 
                    visible="true"
                    condition="textSelected:true :and: 
                                cursorInSection:noticeofmotion[0-9]*">
                    <title xml:lang="eng">mark committee</title>
                    <tooltip xml:lang="eng">Personal Statement </tooltip>
                </action>
                <!-- org.bungeni.editor.selectors.debaterecord.bills.Main == missing -->
                <action name="Motions.markBill" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeSelectBill.markup_bill" 
                    visible="true"
                    condition="textSelected:true :and: 
                                cursorInSection:noticeofmotion[0-9]*">
                    <title xml:lang="eng">mark bill</title>
                    <tooltip xml:lang="eng">Personal Statement </tooltip>
                </action>

            </blockAction>

            <blockAction name="Procedural Motions" 
                target="null" 
                visible="true" 
                condition="none">
                <title xml:lang="eng">procedural motions</title>
                <action name="MotionsGroup.new" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeProcMotionGroupSection.section_creation"
                    visible="true" 
                    condition="textSelected:true :and: cursorInSection:debaterecord">
                    <title xml:lang="eng">motions section</title>
                    <tooltip xml:lang="eng">Personal Statement </tooltip>
                </action>
                <action name="AllMotions.heading" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeProcMotionBlockSection.markup_motion_title:heading_procmotions"
                    visible="true"
                    condition="textSelected:true :and: 
                                cursorInSection:procmotions[0-9]*">
                    <title xml:lang="eng">---- heading</title>
                    <tooltip xml:lang="eng">Personal Statement </tooltip>
                </action>
                <action name="Motions.new" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeProcMotionBlockSection.section_creation"
                    visible="true"
                    condition="textSelected:true :and: 
                                cursorInSection:procmotions[0-9]*">
                    <title xml:lang="eng">markup motion</title>
                    <tooltip xml:lang="eng">Personal Statement </tooltip>
                </action>
                <action name="Motions.heading" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeProcMotionBlockSection.markup_motion_title:heading_motion"
                    visible="true"
                    condition="textSelected:true :and: 
                                cursorInSection:procmotion[0-9]*">
                    <title xml:lang="eng">---- heading</title>
                    <tooltip xml:lang="eng">Personal Statement </tooltip>
                </action>
                <action name="Motions.subheading" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeProcMotionBlockSection.markup_motion_title:subheading_motion"
                    visible="true"
                    condition="textSelected:true :and: 
                                cursorInSection:procmotion[0-9]*">
                    <title xml:lang="eng">---- -- subheading</title>
                    <tooltip xml:lang="eng">Personal Statement </tooltip>
                </action>
            </blockAction>

            <blockAction name="Questions" 
                tooltip="Create a questions section" 
                target="null"
                visible="true" condition="none">
                <title xml:lang="eng">questions</title>
                <!-- create a narrative -->
                <action name="Question.narrative"
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeObservationAction.narrative"
                    visible="true"
                    condition="textSelected:true :and:
                                !cursorInSection:debaterecord :and:
                                !cursorInSection:narr[0-9]*">
                    <title xml:lang="eng">narrative</title>
                    <tooltip xml:lang="eng">Mark selection as a Narrative</tooltip>
                </action>
                <!--creates a question container -->
                <action name="QuestionsGroup.new" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateQuestionGroupSection.section_creation"
                    visible="true" 
                    condition="textSelected:true :and: 
                                cursorInSection:debaterecord">
                    <title xml:lang="eng">questions section</title>
                    <tooltip xml:lang="eng">Personal Statement </tooltip>
                </action>
                <action name="Questions.heading0" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateQuestionBlockSection.apply_questiontext_style:heading_allquestions"
                    visible="true"
                    condition="textSelected:true :and: 
                                cursorInSection:questions[0-9]*.">
                    <title xml:lang="eng">---- heading</title>
                    <tooltip xml:lang="eng">Personal Statement </tooltip>
                </action>
                <!-- pops up a selector dialog for a questions - creates a questionanswer container -->
                <action name="Questions.new" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateQuestionBlockSection.section_creation"
                    visible="true"
                    condition="textSelected:true :and: 
                                cursorInSection:questions[0-9]*">
                    <title xml:lang="eng">import question</title>
                    <tooltip xml:lang="eng">Import a question </tooltip>
                </action>
                <action name="Questions.new" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateQuestionBlockSectionEdit.section_creation"
                    visible="true"
                    condition="textSelected:true :and: 
                                cursorInSection:questions[0-9]*">
                    <title xml:lang="eng">add question</title>
                    <tooltip xml:lang="eng">Add Question </tooltip>
                </action>
                 <!-- markup text as question no. -->
                <action name="Questions.question_number"
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateQuestionBlockSection.markup_question_no"
                    visible="true"
                    condition="textSelected:true :and: 
                                cursorInSection:qa[0-9]*">
                    <title xml:lang="eng">---- number</title>
                    <tooltip xml:lang="eng">Question Number </tooltip>
                </action>
                <action name="Questions.heading1" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateQuestionBlockSection.apply_questiontext_style:heading_question"
                    visible="true" 
                    condition="textSelected:true :and: 
                                cursorInSection:qa[0-9]*.">
                    <title xml:lang="eng">---- heading</title>
                    <tooltip xml:lang="eng">Heading </tooltip>
                </action>
                <action name="Questions.subheading" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateQuestionBlockSection.apply_questiontext_style:subheading_question"
                    visible="true" 
                    condition="textSelected:true :and: 
                                cursorInSection:qa[0-9]*.">
                    <title xml:lang="eng">---- -- sub-heading</title>
                    <tooltip xml:lang="eng">Sub Heading </tooltip>
                </action>
                <action name="QuestionsSection.Q.new" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateQuestionSection.section_creation"
                    visible="true"
                    condition="textSelected:true :and: 
                                cursorInSection:qa[0-9]* :and: 
                                !sectionHasChild:sque[0-9]*">
                    <title xml:lang="eng">spoken question</title>
                    <tooltip xml:lang="eng">Mark Spoken Question </tooltip>
                </action>

                <!-- markup question bodyy-->
                <action name="Questions.body.markup" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateQuestionBlockSection.question_body"
                    visible="true" 
                    condition="textSelected:true :and: 
                    cursorInSection:sque[0-9]*">
                    <title xml:lang="eng">-- body</title>
                    <tooltip xml:lang="eng">Question Body </tooltip>
                </action>
                
                
                <!-- markup question by-->
                <action name="Questions.by.markup" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateQuestionBlockSection.markup_question_by"
                    visible="true" 
                    condition="textSelected:true :and: 
                                cursorInSection:quebody[0-9]*">
                    <title xml:lang="eng">---- by</title>
                    <tooltip xml:lang="eng">Question By </tooltip>
                </action>
                
                <action name="Questions.spoken_question.item_number"
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateQuestionBlockSection.markup_question_no"
                    visible="true"
                    condition="textSelected:true :and: 
                    cursorInSection:quebody[0-9]*">
                    <title xml:lang="eng">---- item num</title>
                    <tooltip xml:lang="eng">Item Number </tooltip>
                </action>

                <action name="Question.listitem"
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateSpeechBlockSection.char_style:an-list-item"
                    visible="true"
                    condition="textSelected:true :and:
                                cursorInSection:quebody[0-9]*,sque[0-9]*,qa[0-9]*">
                    <title xml:lang="eng">---- list-item</title>
                    <tooltip xml:lang="eng">Identify selected text as speech author's name
                    </tooltip>
                </action>

                <!-- markup quetion to 
                <action name="Questions.to.markup" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeDebateQuestionBlockSection.markup_question_to"
                    visible="true" 
                    condition="textSelected:true :and: 
                                cursorInSection:sque[0-9]*">
                    <title xml:lang="eng">++++ to</title>
                    <tooltip xml:lang="eng">Question To </tooltip>
                </action>
                -->
            </blockAction>
            
            <blockAction name="Conclusion Markup" 
                target="null" 
                visible="true" 
                condition="none">
                <title xml:lang="eng">adjournment</title>
                <action name="Conclusion.create" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeConclusionSection.section_creation" 
                    visible="true"
                    condition="cursorInSection:debaterecord :and: 
                                textSelected:true">
                    <title xml:lang="eng">adjournment section</title>
                    <tooltip xml:lang="eng">Personal Statement </tooltip>
                </action>
                <action name="Conclusion.markHouseClosing" 
                    mode="TEXT_SELECTED_INSERT"
                    target="toolbarSubAction.makeConclusionSection.mark_house_closing"
                    visible="true" 
                    condition="cursorInSection:concl[0-9]* :and: 
                                textSelected:true">
                    <title xml:lang="eng">---- house adjournment time</title>
                    <tooltip xml:lang="eng">Personal Statement </tooltip>
                </action>
            </blockAction>
            
        </actionGroup>
    </root>
</toolbar>


```