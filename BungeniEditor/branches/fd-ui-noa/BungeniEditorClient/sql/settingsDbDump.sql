SET CLUSTER '';
SET DEFAULT_TABLE_TYPE 0;
SET TRACE_MAX_FILE_SIZE 1;
SET WRITE_DELAY 500;
SET DEFAULT_LOCK_TIMEOUT 1000;
SET CACHE_SIZE 16384;
;
CREATE USER IF NOT EXISTS SA SALT 'c3459d68b885f882' HASH 'd7d9a1cd14b6fd5d090f41690066738f7e5de0aace6f763130953acd3204687f' ADMIN;
CREATE CACHED TABLE PUBLIC.DOCUMENT_TYPES(
    DOC_TYPE VARCHAR(100) NOT NULL,
    DESCRIPTION VARCHAR(100),
    TEMPLATE_PATH VARCHAR(250),
    METADATA_MODEL_EDITOR CHAR(100),
    METADATA_EDITOR_TITLE CHAR(50),
    WORK_URI CHAR(150),
    EXP_URI CHAR(150),
    FILE_NAME_SCHEME CHAR(150),
    STATE INT,
    UI_PROPERTIES CHAR(500) DEFAULT '' NOT NULL
);
-- 7 +/- SELECT COUNT(*) FROM PUBLIC.DOCUMENT_TYPES;
INSERT INTO PUBLIC.DOCUMENT_TYPES(DOC_TYPE, DESCRIPTION, TEMPLATE_PATH, METADATA_MODEL_EDITOR, METADATA_EDITOR_TITLE, WORK_URI, EXP_URI, FILE_NAME_SCHEME, STATE, UI_PROPERTIES) VALUES
('judgement', 'Judgement', 'workspace/templates/judgement.ott', 'org.bungeni.editor.metadata.editors.JudgementMetadata3', 'Judgement Metadata', '~CountryCode~DocumentType~Year-Month-Day', '~CountryCode~DocumentType~Year-Month-Day~LanguageCode', 'CountryCode~DocumentType~Year-Month-Day~LanguageCode', 1, ''),
('report', 'Report', 'workspace/templates/report.ott', '', NULL, NULL, NULL, NULL, 0, ''),
('act', 'Act', 'workspace/templates/act.ott', '', NULL, NULL, NULL, NULL, 0, ''),
('debaterecord', 'Debate Record', 'workspace/templates/hansard.ott', 'org.bungeni.editor.metadata.editors.GeneralMetadata', 'DebateRecord Metadata', '~CountryCode~DocumentType~Year-Month-Day', '~CountryCode~DocumentType~Year-Month-Day~LanguageCode', 'CountryCode~DocumentType~Year-Month-Day~LanguageCode', 1, ''),
('document', 'Document', 'workspace/templates/defaultdoc.ott', 'org.bungeni.editor.metadata.editors.GeneralMetadata', 'General Metadata', '~CountryCode~DocumentType~Year-Month-Day', '~CountryCode~DocumentType~Year-Month-Day~LanguageCode', 'CountryCode~DocumentType~Year-Month-Day~LanguageCode', 0, ''),
('bill', 'Bill', 'workspace/templates/bill.ott', 'org.bungeni.editor.metadata.editors.BillMetadata', 'Bill Metadata', '~CountryCode~DocumentType~Year-Month-Day', '~CountryCode~DocumentType~Year-Month-Day~LanguageCode', 'CountryCode~DocumentType~Year-Month-Day~LanguageCode', 1, ''),
('internal', 'Internal Usage', '', '', '', '', '', '', 0, '');
CREATE PRIMARY KEY PUBLIC.PRIMARY_KEY_265 ON PUBLIC.DOCUMENT_TYPES(DOC_TYPE);
CREATE CACHED TABLE PUBLIC.CONDITIONAL_OPERATORS(
    CONDITION_NAME CHAR(10) NOT NULL,
    CONDITION_SYNTAX CHAR(10) NOT NULL,
    CONDITION_CLASS VARCHAR(100) NOT NULL
);
-- 2 +/- SELECT COUNT(*) FROM PUBLIC.CONDITIONAL_OPERATORS;
INSERT INTO PUBLIC.CONDITIONAL_OPERATORS(CONDITION_NAME, CONDITION_SYNTAX, CONDITION_CLASS) VALUES
('and', ':and:', 'org.bungeni.editor.toolbar.conditions.operators.andOperator'),
('or', ':or:', 'org.bungeni.editor.toolbar.conditions.operators.orOperator');
CREATE PRIMARY KEY PUBLIC.PRIMARY_KEY_8 ON PUBLIC.CONDITIONAL_OPERATORS(CONDITION_NAME);
CREATE CACHED TABLE PUBLIC.TOOLBAR_CONDITIONS(
    DOCTYPE VARCHAR(50) NOT NULL,
    CONDITION_NAME CHAR(20) NOT NULL,
    CONDITION_CLASS CHAR(100) NOT NULL
);
-- 29 +/- SELECT COUNT(*) FROM PUBLIC.TOOLBAR_CONDITIONS;
INSERT INTO PUBLIC.TOOLBAR_CONDITIONS(DOCTYPE, CONDITION_NAME, CONDITION_CLASS) VALUES
('debaterecord', 'fieldNotExists', 'org.bungeni.editor.toolbar.conditions.runnable.fieldNotExists'),
('debaterecord', 'fieldExists', 'org.bungeni.editor.toolbar.conditions.runnable.fieldExists'),
('bill', 'sectionHasChildType', 'org.bungeni.editor.toolbar.conditions.runnable.sectionHasChildType'),
('debaterecord', 'sectionExists', 'org.bungeni.editor.toolbar.conditions.runnable.sectionExists'),
('debaterecord', 'sectionNotExists', 'org.bungeni.editor.toolbar.conditions.runnable.sectionNotExists'),
('debaterecord', 'textSelected', 'org.bungeni.editor.toolbar.conditions.runnable.textSelected'),
('debaterecord', 'cursorInSection', 'org.bungeni.editor.toolbar.conditions.runnable.cursorInSection'),
('debaterecord', 'imageSelected', 'org.bungeni.editor.toolbar.conditions.runnable.imageSelected'),
('debaterecord', 'imageSelectedIsNot', 'org.bungeni.editor.toolbar.conditions.runnable.imageSelectedIsNot'),
('bill', 'fieldNotExists', 'org.bungeni.editor.toolbar.conditions.runnable.fieldNotExists'),
('bill', 'fieldExists', 'org.bungeni.editor.toolbar.conditions.runnable.fieldExists'),
('bill', 'sectionExists', 'org.bungeni.editor.toolbar.conditions.runnable.sectionExists'),
('bill', 'sectionNotExists', 'org.bungeni.editor.toolbar.conditions.runnable.sectionNotExists'),
('bill', 'textSelected', 'org.bungeni.editor.toolbar.conditions.runnable.textSelected'),
('bill', 'cursorInSection', 'org.bungeni.editor.toolbar.conditions.runnable.cursorInSection'),
('bill', 'imageSelected', 'org.bungeni.editor.toolbar.conditions.runnable.imageSelected'),
('bill', 'imageSelectedIsNot', 'org.bungeni.editor.toolbar.conditions.runnable.imageSelectedIsNot'),
('debaterecord', 'cursorInSectionType', 'org.bungeni.editor.toolbar.conditions.runnable.cursorInSectionType'),
('debaterecord', 'sectionHasChild', 'org.bungeni.editor.toolbar.conditions.runnable.sectionHasChild'),
('judgement', 'sectionHasChild', STRINGDECODE('\torg.bungeni.editor.toolbar.conditions.runnable.sectionHasChild')),
('judgement', 'cursorInSection', 'org.bungeni.editor.toolbar.conditions.runnable.cursorInSection'),
('judgement', 'cursorInSectionType', 'org.bungeni.editor.toolbar.conditions.runnable.cursorInSectionType'),
('judgement', 'fieldExists', 'org.bungeni.editor.toolbar.conditions.runnable.fieldExists'),
('judgement', 'fieldNotExists', 'org.bungeni.editor.toolbar.conditions.runnable.fieldNotExists'),
('judgement', 'imageSelected', 'org.bungeni.editor.toolbar.conditions.runnable.imageSelected'),
('judgement', 'imageSelectedIsNot', 'org.bungeni.editor.toolbar.conditions.runnable.imageSelectedIsNot'),
('judgement', 'sectionExists', 'org.bungeni.editor.toolbar.conditions.runnable.sectionExists'),
('judgement', 'sectionNotExists', 'org.bungeni.editor.toolbar.conditions.runnable.sectionNotExists'),
('judgement', 'textSelected', 'org.bungeni.editor.toolbar.conditions.runnable.textSelected');
CREATE PRIMARY KEY PUBLIC.PRIMARY_KEY_1 ON PUBLIC.TOOLBAR_CONDITIONS(DOCTYPE, CONDITION_NAME);
CREATE CACHED TABLE PUBLIC.ACTION_SETTINGS(
    DOC_TYPE VARCHAR(100) NOT NULL,
    ACTION_NAME VARCHAR(100) NOT NULL,
    ACTION_ORDER INT NOT NULL,
    ACTION_STATE INT NOT NULL,
    ACTION_CLASS VARCHAR(200),
    ACTION_TYPE VARCHAR(50),
    ACTION_NAMING_CONVENTION VARCHAR(100),
    ACTION_NUMBERING_CONVENTION VARCHAR(50),
    ACTION_PARENT VARCHAR(50),
    ACTION_ICON VARCHAR(50),
    ACTION_DISPLAY_TEXT VARCHAR(100),
    ACTION_DIMENSION VARCHAR(50),
    ACTION_SECTION_TYPE CHAR(50),
    ACTION_EDIT_DLG_ALLOWED INT DEFAULT 0,
    ACTION_DIALOG_CLASS CHAR(100)
);
-- 66 +/- SELECT COUNT(*) FROM PUBLIC.ACTION_SETTINGS;
INSERT INTO PUBLIC.ACTION_SETTINGS(DOC_TYPE, ACTION_NAME, ACTION_ORDER, ACTION_STATE, ACTION_CLASS, ACTION_TYPE, ACTION_NAMING_CONVENTION, ACTION_NUMBERING_CONVENTION, ACTION_PARENT, ACTION_ICON, ACTION_DISPLAY_TEXT, ACTION_DIMENSION, ACTION_SECTION_TYPE, ACTION_EDIT_DLG_ALLOWED, ACTION_DIALOG_CLASS) VALUES
('debaterecord', 'makeProcMotionBlockSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'section', 'serial', 'parent', '', 'Markup as Procedural Motion', '', 'ProceduralMotion', 1, STRINGDECODE('\torg.bungeni.editor.selectors.debaterecord.procmotion.Main')),
('bill', 'makeBillSectionSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'section', 'serial', 'parent', '', 'dummy', '', 'Section', 1, ''),
('debaterecord', 'makePrayerMarkup', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'markup', 'prayer', 'none', 'makePrayerSection', ' ', 'Markup as Prayer', ' ', 'Preface', 0, NULL),
('debaterecord', 'makePaperMarkup', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'markup', 'papers', 'none', 'makePaperSection', ' ', 'Markup as Paper', ' ', '', 0, NULL),
('debaterecord', 'makePaperDetailsMarkup', 2, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'markup', 'paper-details', 'none', 'makePaperSection', ' ', 'Markup as Paper Details', ' ', '', 0, NULL),
('debaterecord', 'general_action', 0, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'debaterecord', 'none', STRINGDECODE('\u00a0'), STRINGDECODE('\u00a0'), 'place holder for general action', STRINGDECODE('\u00a0'), 'None', 0, NULL),
('debaterecord', 'makeSpeechBlockSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'speech', 'serial', 'makeQASection', ' ', 'Speech Section', ' ', 'Speech', 1, NULL),
('debaterecord', 'makeQuestionBlockSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'question', 'serial', 'makeQASection', 'makeQASection', 'Create a numbered Question Section', ' ', 'QuestionContainer', 1, NULL),
('debaterecord', 'makeQuestionTextMarkup', 2, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'markup', 'question-text', 'none', 'makeQuestionBlockSection', '', 'Markup as Question Text', '', '', 0, NULL),
('debaterecord', 'makeQuestionTitleMarkup', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'markup', 'question-title', 'none', 'makeQuestionBlockSection', ' ', 'Markup as Question Title', ' ', '', 0, NULL),
('debaterecord', 'makeSpeechMarkup', 2, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'markup', 'speech-text', 'none', 'makeQuestionBlockSection', ' ', 'Markup as Speech', ' ', '', 0, NULL),
('debaterecord', 'makeQATitleMarkup', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'markup', 'qa-title', 'none', 'makeQASection', '', 'Markup as QA Title', '', '', 0, NULL),
('debaterecord', 'makeNoticeMarkup', 2, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'markup', 'notice', 'none', 'makeNoticeOfMotionSection', ' ', 'Markup as Notice', ' ', '', 0, NULL),
('debaterecord', 'makeNoticeDetailsMarkup', 3, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'markup', 'notice-details', 'none', 'makeNoticeDetailsMarkup', '', 'Markup as Notice Details', '', '', 0, NULL),
('debaterecord', 'makeNoticeOfMotionMarkup', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'markup', 'notice-of-motion', 'none', 'makeNoticeOfMotionSection', '', 'Markup as Notice-of-Motion', '', '', 0, NULL),
('debaterecord', 'makePaperSection', 2, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'papers', 'single', 'parent', ' ', 'Create a Paper Section', ' ', 'Paper', 0, 'org.bungeni.editor.selectors.InitPapers'),
('debaterecord', 'makePrayerSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'masthead', 'single', 'parent', ' ', 'Create a Preface', ' ', 'Preface', 1, 'org.bungeni.editor.selectors.InitDebateRecord'),
('debaterecord', 'makeQASection', 4, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'qa', 'serial', 'parent', ' ', 'Create a Question-Answer section', ' ', 'QAContainer', 0, 'org.bungeni.editor.selectors.InitQAsection');
INSERT INTO PUBLIC.ACTION_SETTINGS(DOC_TYPE, ACTION_NAME, ACTION_ORDER, ACTION_STATE, ACTION_CLASS, ACTION_TYPE, ACTION_NAMING_CONVENTION, ACTION_NUMBERING_CONVENTION, ACTION_PARENT, ACTION_ICON, ACTION_DISPLAY_TEXT, ACTION_DIMENSION, ACTION_SECTION_TYPE, ACTION_EDIT_DLG_ALLOWED, ACTION_DIALOG_CLASS) VALUES
('debaterecord', 'makeProcMotionGroupSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'none', 'none', 'parent', STRINGDECODE('\u00a0'), 'Create a Procedural Motions Container ', STRINGDECODE('\u00a0'), 'PMotionsContainer', 1, STRINGDECODE('\u00a0')),
('debaterecord', 'makeDebateMotionBlockSectionEdit', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'motion', 'serial', 'parent', STRINGDECODE('\u00a0'), 'Create Motion Section', STRINGDECODE('\u00a0'), 'NoticeOfMotion', 1, 'org.bungeni.editor.selectors.debaterecord.motions.MainEdit'),
('bill', 'makeBillPrefaceSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'preface', 'single', 'parent', '', 'dummy', '', 'Preface', 1, ''),
('debaterecord', 'makePointOfOrder', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'porder', 'serial', 'parent', '', 'Create a point of order', '', 'PointOfOrder', 1, ''),
('debaterecord', 'makePapersLaidDataEntry', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'tbldocs', 'serial', 'parent', '', 'Markup a Document Link', '', 'PapersLaidList', 1, 'org.bungeni.editor.selectors.debaterecord.tableddocuments.MainDataEntry'),
('bill', 'makeBillArticleSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'article', 'serial', 'parent', '', 'dummy', '', 'Article', 1, 'null'),
('bill', 'makeBillClauseSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'clause', 'serial', 'parent', '', 'dummy', '', 'Clause', 1, ''),
('bill', 'makeBillPartSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'part', 'serial', 'parent', '', 'dummy', '', 'Part', 1, ''),
('debaterecord', 'makeDebateQuestionBlockSectionEdit', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'qa', 'serial', 'parent', STRINGDECODE('\u00a0'), 'Create a Question', STRINGDECODE('\u00a0'), 'QuestionAnswer', 1, 'org.bungeni.editor.selectors.debaterecord.question.MainEdit'),
('bill', 'makeBillChapterSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'chapter', 'serial', 'parent', '', 'dummy', '', 'Chapter', 1, ''),
('bill', 'makeBillSubChapterSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'subchapter', 'serial', 'parent', STRINGDECODE('\u00a0'), 'dummy', STRINGDECODE('\u00a0'), 'SubChapter', 1, STRINGDECODE('\u00a0')),
('bill', 'makeBillParaSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'para', 'serial', 'parent', STRINGDECODE('\u00a0'), 'dummy', STRINGDECODE('\u00a0'), 'Paragraph', 1, STRINGDECODE('\u00a0')),
('bill', 'makeBillSubParaSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'subpara', 'serial', 'parent', STRINGDECODE('\u00a0'), 'dummy', STRINGDECODE('\u00a0'), 'SubParagraph', 1, STRINGDECODE('\u00a0')),
('bill', 'makeBillSubSectionSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'subsection', 'serial', 'parent', STRINGDECODE('\u00a0'), 'dummy', STRINGDECODE('\u00a0'), 'SubSection', 1, STRINGDECODE('\u00a0')),
('debaterecord', 'makeDebateMastheadSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'masthead', 'single', 'parent', STRINGDECODE('\u00a0'), 'Metadata for the Preface', STRINGDECODE('\u00a0'), 'Preface', 1, 'org.bungeni.editor.selectors.debaterecord.masthead.Main'),
('bill', 'makeBillGeneralAction', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'gen', 'serial', 'parent', '', 'General Action', '', '', 1, ''),
('debaterecord', 'makeDebateQuestionSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'sque', 'serial', 'parent', STRINGDECODE('\u00a0'), 'marks up the actual question text', STRINGDECODE('\u00a0'), 'Question', 1, STRINGDECODE('\u00a0'));
INSERT INTO PUBLIC.ACTION_SETTINGS(DOC_TYPE, ACTION_NAME, ACTION_ORDER, ACTION_STATE, ACTION_CLASS, ACTION_TYPE, ACTION_NAMING_CONVENTION, ACTION_NUMBERING_CONVENTION, ACTION_PARENT, ACTION_ICON, ACTION_DISPLAY_TEXT, ACTION_DIMENSION, ACTION_SECTION_TYPE, ACTION_EDIT_DLG_ALLOWED, ACTION_DIALOG_CLASS) VALUES
('debaterecord', 'makeDebateQuestionBlockSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'qa', 'serial', 'parent', STRINGDECODE('\u00a0'), 'contains a question and related speeches', STRINGDECODE('\u00a0'), 'QuestionAnswer', 1, 'org.bungeni.editor.selectors.debaterecord.question.Main'),
('debaterecord', 'makeMinisterialStatement', 1, 1, STRINGDECODE('\torg.bungeni.editor.actions.EditorActionHandler'), 'section', 'mstatemnt', 'serial', 'parent', '', 'Ministerial Statement', '', 'MinisterialStatement', 1, ''),
('debaterecord', 'makeDebateQuestionGroupSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'questions', 'serial', 'parent', STRINGDECODE('\u00a0'), STRINGDECODE('creates \u00a0a question group'), STRINGDECODE('\u00a0'), 'QuestionsContainer', 1, STRINGDECODE('\u00a0')),
('debaterecord', 'makeDebateSpeechBlockSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'speech', 'serial', 'parent', STRINGDECODE('\u00a0'), 'creates a speech', STRINGDECODE('\u00a0'), 'Speech', 1, 'org.bungeni.editor.selectors.debaterecord.speech.Main'),
('debaterecord', 'makeObservationAction', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'comment', 'serial', 'parent', '', 'creates an observation', '', 'Observation', 1, 'org.bungeni.editor.selectors.debaterecord.question.Main'),
('debaterecord', 'makeGroupActivityAction', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'comment', 'serial', 'parent', STRINGDECODE('\u00a0'), 'creates a communication', STRINGDECODE('\u00a0'), 'GroupActivity', 1, 'org.bungeni.editor.selectors.debaterecord.question.Main'),
('debaterecord', 'makeCommunicationAction', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'comnctn', 'serial', 'parent', STRINGDECODE('\u00a0'), 'creates a communication', STRINGDECODE('\u00a0'), 'Communication', 1, 'org.bungeni.editor.selectors.debaterecord.question.Main'),
('debaterecord', 'makeActionEvent', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'dummy', 'serial', 'parent', '', '', '', 'ActionEvent', 1, ''),
('debaterecord', 'makeAdjournmentAction', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'actionevt', 'serial', 'parent', '', 'creates an adjournment section', '', 'ActionEvent', 1, 'org.bungeni.editor.selectors.debaterecord.question.Main'),
('debaterecord', 'makeConclusionSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'concl', 'serial', 'parent', STRINGDECODE('\u00a0'), 'Conclusion Actions', STRINGDECODE('\u00a0'), 'Conclusion', 1, 'org.bungeni.editor.selectors.debaterecord.conclusion.Main'),
('debaterecord', 'makeNoticeOfMotionAction', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'actionevt', 'serial', 'parent', '', 'creates a notice of motion', '', 'ActionEvent', 1, 'org.bungeni.editor.selectors.debaterecord.question.Main'),
('judgement', 'makeOmissisSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'omissis', 'serial', 'parent', '', 'Omissis', '', 'Omissis', 1, ''),
('debaterecord', 'makeMetadataReference', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'dummy', 'serial', 'parent', '', 'placeholder for metadata markup', '', 'ActionEvent', 1, 'org.bungeni.editor.selectors.debaterecord.question.Main'),
('debaterecord', 'makeDebateMotionGroupSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'none', 'none', 'parent', STRINGDECODE('\u00a0'), 'Create a Notices of Motion container', STRINGDECODE('\u00a0'), 'NMotionsContainer', 1, STRINGDECODE('\u00a0')),
('debaterecord', 'makeDebateMotionBlockSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'none', 'none', 'parent', STRINGDECODE('\u00a0'), 'Create a Notice of Motion Section', STRINGDECODE('\u00a0'), 'NoticeOfMotion', 1, 'org.bungeni.editor.selectors.debaterecord.motions.Main');
INSERT INTO PUBLIC.ACTION_SETTINGS(DOC_TYPE, ACTION_NAME, ACTION_ORDER, ACTION_STATE, ACTION_CLASS, ACTION_TYPE, ACTION_NAMING_CONVENTION, ACTION_NUMBERING_CONVENTION, ACTION_PARENT, ACTION_ICON, ACTION_DISPLAY_TEXT, ACTION_DIMENSION, ACTION_SECTION_TYPE, ACTION_EDIT_DLG_ALLOWED, ACTION_DIALOG_CLASS) VALUES
('debaterecord', 'makeSelectCommittee', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'markup', 'dummy', 'none', 'none', STRINGDECODE('\u00a0'), 'Markup Committee', STRINGDECODE('\u00a0'), STRINGDECODE('\u00a0'), 1, 'org.bungeni.editor.selectors.debaterecord.committees.Main'),
('debaterecord', 'makePersonalStatement', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'pstatemnt', 'serial', 'parent', '', '', '', 'PersonalStatement', 1, ''),
('debaterecord', 'makePapersLaidBlockSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'papers', 'serial', 'parent', STRINGDECODE('\u00a0'), 'Import Tabled Documents', STRINGDECODE('\u00a0'), 'PapersLaid', 1, 'org.bungeni.editor.selectors.debaterecord.tableddocuments.Main'),
('debaterecord', 'makeSelectBill', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'markup', 'dummu', 'none', 'none', STRINGDECODE('\u00a0'), 'Markup Bill', STRINGDECODE('\u00a0'), STRINGDECODE('\u00a0'), 1, 'org.bungeni.editor.selectors.debaterecord.bills.Main'),
('debaterecord', 'makePapersLaidListSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'tbldocs', 'serial', 'parent', STRINGDECODE('\u00a0'), 'Import Tabled Documents', STRINGDECODE('\u00a0'), 'PapersLaidList', 1, 'org.bungeni.editor.selectors.debaterecord.tableddocuments.Main'),
('judgement', 'makeJudgementHeader', 1, 1, 'org.bungeni.editor.actions.EditorActionHander', 'section', 'header', 'single', 'parent', '', '', '', 'Header', 1, ''),
('judgement', 'makeMetadataReference', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'dummy', 'serial', 'parent', '', 'placeholder for metadata markup', '', 'ActionEvent', 1, 'org.bungeni.editor.selectors.debaterecod.question.Main'),
('judgement', 'makeIntroduction', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'intro', 'single', 'parent', STRINGDECODE('\u00a0'), STRINGDECODE('\u00a0'), STRINGDECODE('\u00a0'), 'Introduction', 1, STRINGDECODE('\u00a0')),
('judgement', 'makeBackground', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'bg', 'single', 'parent', '', '', '', 'Background', 1, ''),
('judgement', 'makeMotivation', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'motive', 'single', 'parent', '', '', '', 'Motivation', 1, ''),
('judgement', 'makeDecision', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'decision', 'single', 'parent', '', '', '', 'Decision', 1, ''),
('judgement', 'makeConclusion', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'concl', 'single', 'parent', '', '', '', 'Conclusion', 1, ''),
('debaterecord', 'makePapersLaidMarkupSingle', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'tbldocs', 'serial', 'parent', '', 'Selectsingle tabled document', '', 'PapersLaidList', 1, 'org.bungeni.editor.selectors.debaterecord.tableddocuments.MainSingleSelect'),
('debaterecord', 'makePetitionsMarkupSingle', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'petitlist', 'serial', 'parent', '', 'Markup single petition', '', 'PetitionsList', 1, 'org.bungeni.editor.selectors.debaterecord.petitions.MainSingleSelect'),
('debaterecord', 'makePetitionsBlockSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'petit', 'serial', 'parent', STRINGDECODE('\u00a0'), 'Petitions', STRINGDECODE('\u00a0'), 'Petitions', 1, 'org.bungeni.editor.selectors.debaterecord.petition.Main'),
('debaterecord', 'makePetitionsListSection', 1, 1, 'org.bungeni.editor.actions.EditorActionHandler', 'section', 'petitlist', 'serial', 'parent', STRINGDECODE('\u00a0'), 'Petitions', STRINGDECODE('\u00a0'), 'PetitionsList', 1, 'org.bungeni.editor.selectors.petitions.Main');
CREATE PRIMARY KEY PUBLIC.PRIMARY_KEY_4 ON PUBLIC.ACTION_SETTINGS(DOC_TYPE, ACTION_NAME);
CREATE INDEX PUBLIC.TAS_ACTIONPARENT_IDX ON PUBLIC.ACTION_SETTINGS(ACTION_PARENT);
CREATE INDEX PUBLIC.CONSTRAINT_INDEX_0 ON PUBLIC.ACTION_SETTINGS(ACTION_SECTION_TYPE);
CREATE CACHED TABLE PUBLIC.TOOLBAR_XML_CONFIG(
    DOC_TYPE VARCHAR(100) NOT NULL,
    TOOLBAR_XML VARCHAR(100) NOT NULL
);
-- 3 +/- SELECT COUNT(*) FROM PUBLIC.TOOLBAR_XML_CONFIG;
INSERT INTO PUBLIC.TOOLBAR_XML_CONFIG(DOC_TYPE, TOOLBAR_XML) VALUES
('bill', 'settings/toolbar_bill.xml'),
('judgement', 'settings/toolbar_judgement.xml'),
('debaterecord', 'settings/toolbar_debate.xml');
CREATE PRIMARY KEY PUBLIC.PRIMARY_KEY_B ON PUBLIC.TOOLBAR_XML_CONFIG(DOC_TYPE);
CREATE CACHED TABLE PUBLIC.NUMBER_DECORATORS(
    DECORATOR_NAME CHAR(25) NOT NULL,
    DECORATOR_DESC CHAR(50) NOT NULL,
    DECORATOR_CLASS CHAR(100) NOT NULL
);
-- 6 +/- SELECT COUNT(*) FROM PUBLIC.NUMBER_DECORATORS;
INSERT INTO PUBLIC.NUMBER_DECORATORS(DECORATOR_NAME, DECORATOR_DESC, DECORATOR_CLASS) VALUES
('flowerBracket', 'Flower Bracket ( {1}, {2}...)', 'org.bungeni.numbering.decorators.flowerBracketDecorator'),
('hashPrefix', 'Hash Prefix (#1, #2, ...)', 'org.bungeni.numbering.decorators.hashPrefixDecorator'),
('postDashed', 'Dash Suffix (1 - , 2 - , 3 - ..)', 'org.bungeni.numbering.decorators.postDashedDecorator'),
('postDotted', 'Dot Suffix (1. , 2., 3. ...)', 'org.bungeni.numbering.decorators.postDottedDecorator'),
('parens', 'Parens ( (1), (2), (3)....)', 'org.bungeni.numbering.decorators.parensDecorator'),
('square', 'Square ( [1],[2],[3]... )', 'org.bungeni.numbering.decorators.squareDecorator');
CREATE PRIMARY KEY PUBLIC.PRIMARY_KEY_A9 ON PUBLIC.NUMBER_DECORATORS(DECORATOR_NAME);
CREATE CACHED TABLE PUBLIC.TRANSFORM_CONFIGURATIONS(
    DOC_TYPE VARCHAR(100) NOT NULL,
    CONFIG_NAME VARCHAR(100) NOT NULL,
    CONFIG_FILE VARCHAR(500) NOT NULL
);
-- 2 +/- SELECT COUNT(*) FROM PUBLIC.TRANSFORM_CONFIGURATIONS;
INSERT INTO PUBLIC.TRANSFORM_CONFIGURATIONS(DOC_TYPE, CONFIG_NAME, CONFIG_FILE) VALUES
('bill', 'billCommon', 'odttoakn/minixslt/bill/pipeline.xsl'),
('debaterecord', 'debateRecordCommon', 'odttoakn/minixslt/debaterecord/pipeline.xsl');
CREATE PRIMARY KEY PUBLIC.PRIMARY_KEY_E ON PUBLIC.TRANSFORM_CONFIGURATIONS(DOC_TYPE, CONFIG_NAME);
CREATE CACHED TABLE PUBLIC.RESOURCE_MESSAGE_BUNDLES(
    BUNDLE_NAME CHAR(100) NOT NULL,
    DOC_TYPE CHAR(100) NOT NULL
);
-- 12 +/- SELECT COUNT(*) FROM PUBLIC.RESOURCE_MESSAGE_BUNDLES;
INSERT INTO PUBLIC.RESOURCE_MESSAGE_BUNDLES(BUNDLE_NAME, DOC_TYPE) VALUES
('DocMetaNames', 'bill'),
('SectionMetaNames', 'bill'),
('SectionTypeNames', 'bill'),
('SectionTypeNames', 'debaterecord'),
('SectionMetaNames', 'debaterecord'),
('DocMetaNames', 'debaterecord'),
('ErrorMessages', 'judgement'),
('SectionMetaNames', 'judgement'),
('SectionTypeNames', 'judgement'),
('DocMetaNames', 'judgement'),
('ErrorMessages', 'debaterecord'),
('ErrorMessages', 'bill');
CREATE PRIMARY KEY PUBLIC.PRIMARY_KEY_EB ON PUBLIC.RESOURCE_MESSAGE_BUNDLES(DOC_TYPE, BUNDLE_NAME);
CREATE CACHED TABLE PUBLIC.GENERAL_EDITOR_PROPERTIES(
    PROPERTY_NAME CHAR(50) NOT NULL,
    PROPERTY_VALUE CHAR(500),
    PROPERTY_DESCRIPTION VARCHAR(100)
);
-- 42 +/- SELECT COUNT(*) FROM PUBLIC.GENERAL_EDITOR_PROPERTIES;
INSERT INTO PUBLIC.GENERAL_EDITOR_PROPERTIES(PROPERTY_NAME, PROPERTY_VALUE, PROPERTY_DESCRIPTION) VALUES
('structuralRulesRootPath', 'settings/structural_rules', 'path to structural rules root'),
('pluginsPath', 'plugins', ''),
('activeProfile', 'withBackend', 'Current Active Profile'),
('attributeDateFormat', 'yyyy-MM-dd', 'Date format used in AkomaNtoso Attributes'),
('defaultWorkURI', 'CountryCode.FullDate.PartName', ''),
('defaultHierarchyView', 'VIEW_PRETTY_SECTIONS', 'Other options are VIEW_SECTIONS, VIEW_PARAGRAPHS'),
('registryJDBCdriver', 'org.h2.Driver', 'jdbc driver to use for registry'),
('registryDB', 'registry.db', 'registry db name'),
('registryJDBCdriverPrefix', 'jdbc:h2:', 'prefix for registry jdbc connection string'),
('logoPath', 'settings/logos', 'path to logo'),
('activeDocumentMode', 'debaterecord', 'editor client is set to edit a document of this type'),
('root:debaterecord', 'debaterecord', 'name of root sectionfor debaterecord'),
('localRegistry', 'yes', 'registry is a local database '),
('registryUser', 'sa', 'user name to connect to registry db'),
('registryPassword', '', 'password to connect to registry db'),
('localRegistryFolder', 'registry', 'path to local registry (sub folder under main installation)'),
('textMetadataPropertyBeginMarker', '{{', 'used to demarcate beginning of inline metadata'),
('textMetadataPropertyEndMarker', '}}', 'used to demarcate ending of inline metadata '),
('toolbarXmlConfig', 'settings/toolbar.xml', 'used to load editor toolbar'),
('iconPath', '/gui', 'path to icons used by editor'),
('defaultExportPath', 'workspace/export', 'default export path'),
('root:bill', 'bill', STRINGDECODE('\u00a0')),
('pathToLogFile', 'logs/log.txt', 'Path to Log file'),
('truncateLogOnStartup', 'true', 'Truncates log file on startup'),
('metadataDateFormat', 'yyyy-MM-dd', 'metadata date format'),
('metadataTimeFormat', 'HH:mm', 'metadata time format'),
('popupDialogBackColor', '#C5DD45', 'popup dialog background color'),
('messageBundlesPath', 'settings/bundles', 'message bundles path'),
('defaultSavePath', 'workspace/files', 'default file save path'),
('defaultSaveFormat', 'CountryCode.DocumentType.Year.Month.Day.LanguageCode.PartName.FileName', 'default save format'),
('transformerServerName', 'localhost', 'Host name of transformer server'),
('transformerServerPort', '8182', 'Transformer server port'),
('root:judgement', 'judgement', 'name of root section for judgement'),
('transformerWorkingDir', 'transformer', ''),
('transformerJar', 'transformer/jar/odttransformer.jar', ''),
('genericPanelBackColor', '#FFFFCC', 'generic color used by panels'),
('toolbarNodeEnabledColor', '#0000FF', 'Node enabeld color '),
('toolbarNodeDisabledColor', '#aaaaaa', STRINGDECODE('node disabled color\u00a0')),
('toolbarNodeNoTargetColor', '#CC9900', 'Node without target color'),
('locale.Country.iso3166-1-a2', 'US', 'country code use for java locales'),
('locale.Language.iso639-1', 'en', 'language code used for java locales'),
('locale.Language.iso639-2', 'eng', 'iso language mode for editor');
CREATE PRIMARY KEY PUBLIC.PRIMARY_KEY_E9 ON PUBLIC.GENERAL_EDITOR_PROPERTIES(PROPERTY_NAME);
CREATE CACHED TABLE PUBLIC.TRANSFORM_TARGETS(
    TARGET_NAME CHAR(100) NOT NULL,
    TARGET_DESC CHAR(100) NOT NULL,
    TARGET_EXT CHAR(100) NOT NULL,
    TARGET_CLASS CHAR(100) NOT NULL
);
ALTER TABLE PUBLIC.TRANSFORM_TARGETS ADD CONSTRAINT PUBLIC.CONSTRAINT_F PRIMARY KEY(TARGET_NAME);
-- 4 +/- SELECT COUNT(*) FROM PUBLIC.TRANSFORM_TARGETS;
INSERT INTO PUBLIC.TRANSFORM_TARGETS(TARGET_NAME, TARGET_DESC, TARGET_EXT, TARGET_CLASS) VALUES
('ODT', 'OpenDocument File', 'odt', 'org.bungeni.ooo.transforms.loadable.ODTSaveTransform'),
('HTML', 'HTML File', 'html', 'org.bungeni.ooo.transforms.loadable.HTMLTransform'),
('AN-XML', 'AkomaNtoso XML File', 'xml', 'org.bungeni.ooo.transforms.loadable.AnXmlTransform'),
('PDF', 'PDF file', 'pdf', 'org.bungeni.ooo.transforms.loadable.PDFTransform');
CREATE CACHED TABLE PUBLIC.DOCUMENT_METADATA(
    DOC_TYPE VARCHAR(100) NOT NULL,
    METADATA_NAME CHAR(50) NOT NULL,
    METADATA_DATATYPE CHAR(50) NOT NULL,
    METADATA_NAMESPACE CHAR(50),
    METADATA_TYPE CHAR(30),
    DISPLAY_ORDER INT,
    VISIBLE INT,
    DISPLAY_NAME CHAR(100),
    TABULAR_CONFIG CHAR(200)
);
-- 54 +/- SELECT COUNT(*) FROM PUBLIC.DOCUMENT_METADATA;
INSERT INTO PUBLIC.DOCUMENT_METADATA(DOC_TYPE, METADATA_NAME, METADATA_DATATYPE, METADATA_NAMESPACE, METADATA_TYPE, DISPLAY_ORDER, VISIBLE, DISPLAY_NAME, TABULAR_CONFIG) VALUES
('judgement', 'BungeniPartyName', 'string', 'bungeni', 'document', 11, 0, 'Party Name', 'PartyId~PartyUri~PartyName~PartyType'),
('judgement', 'BungeniPublicationDate', 'string', 'bungeni', 'document', 11, 1, 'Publication Date', ''),
('judgement', 'BungeniPublicationName', 'string', 'bungeni', 'document', 13, 1, 'Publication Name', ''),
('judgement', 'BungeniJudgementDate', 'datetime', 'bungeni', 'document', 4, 1, 'Judgement Date', ''),
('debaterecord', 'BungeniLanguageCode', 'string', 'bungeni', 'document', 2, 1, 'Language ID', ''),
('debaterecord', 'BungeniCountryCode', 'string', 'bungeni', 'document', 3, 1, 'Country Code', ''),
('debaterecord', 'BungeniParliamentSession', 'string', 'bungeni', 'document', 8, 1, 'Parliament Session', ''),
('debaterecord', 'BungeniParliamentSitting', 'string', 'bungeni', 'document', 9, 1, 'Parliament Sitting', ''),
('judgement', 'BungeniJudgeName', 'string', 'bungeni', 'document', 11, 0, 'Judge Names', 'JudgeId~JudgeFirstName~JudgeLastName~JudgeURI'),
('debaterecord', 'BungeniDocAuthor', 'string', 'bungeni', 'document', 5, 1, 'Author', ''),
('debaterecord', 'BungeniParliamentID', 'string', 'bungeni', 'document', 4, 0, 'Parliament ID', ''),
('debaterecord', 'BungeniDocType', 'string', 'bungeni', 'document', 1, 1, 'Document Type', ''),
('debaterecord', 'BungeniOfficialDate', 'datetime', 'bungeni', 'document', 6, 1, 'Official Date', STRINGDECODE('\u00a0')),
('debaterecord', 'BungeniOfficialTime', 'datetime', 'bungeni', 'document', 7, 1, 'Official Time', STRINGDECODE('\u00a0')),
('bill', 'BungeniBillNo', 'string', 'bungeni', 'document', 4, 1, 'Bill No', ''),
('bill', 'BungeniDateOfAssent', 'string', 'bungeni', 'document', 5, 1, 'Date of Assent', ''),
('bill', 'BungeniDateOfCommencement', 'string', 'bungeni', 'document', 6, 1, 'Date of Commencement', ''),
('bill', 'BungeniDocPart', 'string', 'bungeni', 'document', 11, 1, 'Document Part', ''),
('bill', 'BungeniBillOfficialDate', 'datetime', 'bungeni', 'document', 6, 1, 'Official Date', ''),
('bill', 'BungeniDocAuthor', 'string', 'bungeni', 'document', 5, 1, 'Author', ''),
('bill', 'BungeniLanguageCode', 'string', 'bungeni', 'document', 2, 1, 'Language ID', ''),
('bill', 'BungeniParliamentID', 'string', 'bungeni', 'document', 4, 1, 'Parliament ID', ''),
('bill', 'BungeniParliamentSession', 'string', 'bungeni', 'document', 8, 1, 'Parliament Session', ''),
('bill', 'BungeniParliamentSitting', 'string', 'bungeni', 'document', 9, 1, 'Parliament Sitting', ''),
('bill', 'BungeniWorkURI', 'string', 'bungeni', 'document', 7, 1, 'Work URI', ''),
('bill', 'BungeniExpURI', 'string', 'bungeni', 'document', 8, 1, 'Expression URI', ''),
('bill', 'BungeniManURI', 'string', 'bungeni', 'document', 10, 1, 'Manifestation URI', ''),
('bill', 'BungeniExpDate', 'string', 'bungeni', 'document', 12, 0, 'Expression Date', ''),
('bill', 'BungeniManDate', 'string', 'bungeni', 'document', 12, 0, 'Expression Date', ''),
('bill', 'BungeniPublicationDate', 'string', 'bungeni', 'document', 11, 0, 'Publication Date', ''),
('bill', 'BungeniWorkDate', 'string', 'bungeni', 'document', 10, 0, 'Work Date', ''),
('bill', 'BungeniCountryCode', 'string', 'bungeni', 'document', 3, 1, 'Country Code', ''),
('bill', 'BungeniPublicationName', 'string', 'bungeni', 'document', 13, 0, 'Publication Name', ''),
('bill', 'BungeniDocType', 'string', 'bungeni', 'document', 1, 0, 'Document Type', ''),
('bill', 'BungeniBillOfficialTime', 'datetime', 'bungeni', 'document', 7, 0, 'Official Time', ''),
('debaterecord', 'BungeniWorkDate', 'string', 'bungeni', 'document', 10, 1, 'Work Date', ''),
('debaterecord', 'BungeniDocPart', 'string', 'bungeni', 'document', 11, 1, 'Document Part', ''),
('debaterecord', 'BungeniPublicationDate', 'string', 'bungeni', 'document', 11, 1, 'Publication Date', ''),
('debaterecord', 'BungeniPublicationName', 'string', 'bungeni', 'document', 13, 1, 'Publication Name', ''),
('debaterecord', 'BungeniExpDate', 'string', 'bungeni', 'document', 12, 1, 'Expression Date', '');
INSERT INTO PUBLIC.DOCUMENT_METADATA(DOC_TYPE, METADATA_NAME, METADATA_DATATYPE, METADATA_NAMESPACE, METADATA_TYPE, DISPLAY_ORDER, VISIBLE, DISPLAY_NAME, TABULAR_CONFIG) VALUES
('debaterecord', 'BungeniManDate', 'string', 'bungeni', 'document', 12, 1, 'Expression Date', ''),
('judgement', 'BungeniCountryCode', 'string', 'bungeni', 'document', 3, 1, 'Country Code', ''),
('judgement', 'BungeniJudgementNo', 'string', 'bungeni', 'document', 2, 1, 'Judgement No', ''),
('judgement', 'BungeniCaseNo', 'string', 'bungeni', 'document', 3, 1, 'Case No', ''),
('judgement', 'BungeniDocAuthor', 'string', 'bungeni', 'document', 5, 1, 'Author', ''),
('judgement', 'BungeniDocPart', 'string', 'bungeni', 'document', 11, 1, 'Document Part', ''),
('judgement', 'BungeniDocType', 'string', 'bungeni', 'document', 1, 1, 'Document Type', ''),
('judgement', 'BungeniParliamentID', 'string', 'bungeni', 'document', 4, 0, 'Parliament ID', ''),
('judgement', 'BungeniParliamentSession', 'string', 'bungeni', 'document', 8, 0, 'Parliament Session', STRINGDECODE('\u00a0')),
('judgement', 'BungeniParliamentSitting', 'string', 'bungeni', 'document', 9, 0, 'Parliament Sitting', STRINGDECODE('\u00a0')),
('judgement', 'BungeniWorkDate', 'string', 'bungeni', 'document', 10, 0, 'Work Date', STRINGDECODE('\u00a0')),
('judgement', 'BungeniManDate', 'string', 'bungeni', 'document', 12, 0, 'Expression Date', STRINGDECODE('\u00a0')),
('judgement', 'BungeniLanguageCode', 'string', 'bungeni', 'document', 2, 0, 'Language ID', STRINGDECODE('\u00a0')),
('judgement', 'BungeniExpDate', 'string', 'bungeni', 'document', 12, 0, 'Expression Date', STRINGDECODE('\u00a0'));
CREATE PRIMARY KEY PUBLIC.PRIMARY_KEY_FB ON PUBLIC.DOCUMENT_METADATA(DOC_TYPE, METADATA_NAME);
CREATE CACHED TABLE PUBLIC.EXTERNAL_PLUGINS(
    PLUGIN_NAME CHAR(100) NOT NULL,
    PLUGIN_LOADER CHAR(200) NOT NULL,
    PLUGIN_DESC CHAR(200) NOT NULL,
    PLUGIN_ENABLED CHAR(2) NOT NULL,
    PLUGIN_JAR CHAR(200) NOT NULL
);
ALTER TABLE PUBLIC.EXTERNAL_PLUGINS ADD CONSTRAINT PUBLIC.CONSTRAINT_90 PRIMARY KEY(PLUGIN_NAME, PLUGIN_JAR, PLUGIN_LOADER);
-- 1 +/- SELECT COUNT(*) FROM PUBLIC.EXTERNAL_PLUGINS;
INSERT INTO PUBLIC.EXTERNAL_PLUGINS(PLUGIN_NAME, PLUGIN_LOADER, PLUGIN_DESC, PLUGIN_ENABLED, PLUGIN_JAR) VALUES
('SectionRefactorPlugin', 'org.bungeni.editor.section.refactor.integrate.SectionRefactorExt', 'Plugin to Refactor sections', '1', 'sectionrefactor.jar');
CREATE CACHED TABLE PUBLIC.METADATA_MODEL_EDITORS(
    DOC_TYPE CHAR(100) NOT NULL,
    METADATA_MODEL_EDITOR CHAR(255) NOT NULL,
    METADATA_EDITOR_TITLE CHAR(100) NOT NULL,
    ORDER_OF_LOADING INT NOT NULL
);
-- 7 +/- SELECT COUNT(*) FROM PUBLIC.METADATA_MODEL_EDITORS;
INSERT INTO PUBLIC.METADATA_MODEL_EDITORS(DOC_TYPE, METADATA_MODEL_EDITOR, METADATA_EDITOR_TITLE, ORDER_OF_LOADING) VALUES
('judgement', 'org.bungeni.editor.metadata.editors.GeneralMetadata', 'General', 1),
('judgement', 'org.bungeni.editor.metadata.editors.JudgementMetadata3', 'Judgement', 3),
('debaterecord', 'org.bungeni.editor.metadata.editors.ParliamentMetadata', 'Parliament', 2),
('debaterecord', STRINGDECODE('\torg.bungeni.editor.metadata.editors.GeneralMetadata'), 'General', 1),
('bill', 'org.bungeni.editor.metadata.editors.GeneralMetadata', 'General', 1),
('bill', 'org.bungeni.editor.metadata.editors.ParliamentMetadata', 'Parliament', 2),
('bill', 'org.bungeni.editor.metadata.editors.BillMetadata', 'Bill', 3);
CREATE PRIMARY KEY PUBLIC.PRIMARY_KEY_39 ON PUBLIC.METADATA_MODEL_EDITORS(DOC_TYPE, METADATA_MODEL_EDITOR);
CREATE CACHED TABLE PUBLIC.DOCUMENT_SECTION_TYPES(
    DOC_TYPE VARCHAR(100) NOT NULL,
    SECTION_TYPE_NAME VARCHAR(50) NOT NULL,
    SECTION_NAME_PREFIX VARCHAR(100),
    SECTION_NUMBERING_STYLE CHAR(10),
    SECTION_BACKGROUND CHAR(255),
    SECTION_INDENT_LEFT CHAR(10),
    SECTION_INDENT_RIGHT CHAR(10),
    SECTION_VISIBILITY CHAR(10),
    NUMBERING_SCHEME CHAR(30),
    NUMBER_DECORATOR CHAR(20)
);
-- 48 +/- SELECT COUNT(*) FROM PUBLIC.DOCUMENT_SECTION_TYPES;
INSERT INTO PUBLIC.DOCUMENT_SECTION_TYPES(DOC_TYPE, SECTION_TYPE_NAME, SECTION_NAME_PREFIX, SECTION_NUMBERING_STYLE, SECTION_BACKGROUND, SECTION_INDENT_LEFT, SECTION_INDENT_RIGHT, SECTION_VISIBILITY, NUMBERING_SCHEME, NUMBER_DECORATOR) VALUES
('debaterecord', 'NoticeOfMotion', 'noticeofmotion', 'serial', 'url:/settings/sectiontypes/bg/shade-4.png', '.5', '0', 'user', 'none', 'none'),
('debaterecord', 'Preface', 'preface', 'single', 'url:/settings/sectiontypes/bg/shade-1.png', '.3', '0', 'user', 'none', 'none'),
('debaterecord', 'PointOfOrder', 'porder', 'serial', 'url:/settings/sectiontypes/bg/shade-5.png', '.4', '0', 'user', 'none', 'none'),
('debaterecord', 'MinisterialStatement', 'mstatemnt', 'serial', 'url:/settings/sectiontypes/bg/shade-4.png', '0.4', '0', 'user', 'none', 'none'),
('debaterecord', 'Petitions', 'petit', 'serial', ' url:/settings/sectiontypes/bg/shade-4.png', '0', '0', 'user', 'none', 'none'),
('debaterecord', 'NMotionsContainer', 'noticesmotion', 'serial', STRINGDECODE('\t url:/settings/sectiontypes/bg/shade-3.png'), '.3', '0', 'user', 'none', 'none'),
('debaterecord', 'PMotionsContainer', 'procmotions', 'serial', 'url:/settings/sectiontypes/bg/shade-5.png', '.3', '0', 'user', 'none', 'none'),
('debaterecord', 'ProceduralMotion', 'procmotion', 'serial', ' url:/settings/sectiontypes/bg/shade-4.png', '.5', '0', 'user', 'none', 'none'),
('debaterecord', 'Speech', 'speech', 'serial', 'url:/settings/sectiontypes/bg/shade-1.png', '.6', '0', 'user', 'none', 'none'),
('debaterecord', 'QuestionsContainer', 'questions', 'serial', 'url:/settings/sectiontypes/bg/shade-7.png', '.3', '0', 'user', 'none', 'none'),
('debaterecord', 'QuestionAnswer', 'qa', 'serial', 'url:/settings/sectiontypes/bg/shade-7.png', '.5', '0', 'user', 'none', 'none'),
('debaterecord', 'Question', 'sque', 'serial', 'url:/settings/sectiontypes/bg/shade-6.png', '.6', '0', 'user', 'none', 'none'),
('debaterecord', 'Motion', 'motion', 'serial', 'url:/settings/sectiontypes/bg/shade-3.png', '.5', '0', 'user', 'none', 'none'),
('debaterecord', 'Conclusion', 'concl', 'serial', 'url:/settings/sectiontypes/bg/shade-4.png', '.4', '0', 'user', 'none', 'none'),
('judgement', 'Conclusion', 'concl', 'serial', STRINGDECODE('\t url:/settings/sectiontypes/bg/orange.png'), '0.25', '0', 'user', 'none', 'none'),
('judgement', 'Decision', 'decision', 'single', STRINGDECODE('\t url:/settings/sectiontypes/bg/orange-yellow.png'), '0.55', '0', 'user', 'none', 'none'),
('judgement', 'Header', 'header', 'single', STRINGDECODE('\t url:/settings/sectiontypes/bg/light-orange.png'), '0.25', '0', 'user', 'none', 'none'),
('judgement', 'Motivation', 'motive', 'single', STRINGDECODE('\t url:/settings/sectiontypes/bg/light-green.png'), '0.55', '0', 'user', 'none', 'none'),
('judgement', 'Omissis', 'omissis', 'serial', 'url:/settings/sectiontypes/bg/sand.png', '0.65', '0', 'user', 'none', 'none'),
('debaterecord', 'PersonalStatement', 'pstatemnt', 'serial', STRINGDECODE('\t url:/settings/sectiontypes/bg/shade-4.png'), '0.4', '0', 'user', 'none', 'none'),
('debaterecord', 'body', 'debaterecord', 'single', '0xffffff', '0', '0', 'user', 'none', 'none'),
('debaterecord', 'SystemMemberMetadata', '', '', '', '0', '0', 'user', 'none', 'none'),
('debaterecord', 'NoticeOfMotionDetails', 'motiondtls', 'serial', 'url:/settings/sectiontypes/bg/shade-4.png', '0', '0', 'user', 'none', 'none'),
('debaterecord', 'MetadataContainer', '', '', '', '0', '0', 'user', 'none', 'none'),
('debaterecord', 'None', '', '', '', '0', '0', 'user', 'none', 'none'),
('debaterecord', 'RootSection', '', '', '', '0', '0', 'user', 'none', 'none'),
('debaterecord', 'SystemDateTimeMetadata', '', '', '', '0', '0', 'user', 'none', 'none'),
('bill', 'Article', 'article', 'serial', '0xf0f0f0', '.3', '0', 'user', 'NUMERIC', 'flowerBracket'),
('bill', 'Clause', 'clause', 'serial', '0xffdfdf', '.5', '0', 'user', 'ROMAN', 'hashPrefix'),
('bill', 'MastHead', 'preface', 'single', '0xe8eef7', '0', '0', 'user', 'none', 'postDashed'),
('bill', 'NumberedContainer', 'num_', 'single', '0xffffe1', '0', '0', 'system', 'none', 'postDotted');
INSERT INTO PUBLIC.DOCUMENT_SECTION_TYPES(DOC_TYPE, SECTION_TYPE_NAME, SECTION_NAME_PREFIX, SECTION_NUMBERING_STYLE, SECTION_BACKGROUND, SECTION_INDENT_LEFT, SECTION_INDENT_RIGHT, SECTION_VISIBILITY, NUMBERING_SCHEME, NUMBER_DECORATOR) VALUES
('bill', 'Part', 'part', 'serial', '0xffffa1', '.3', '0', 'user', 'ALPHABETICAL', 'postDashed'),
('bill', 'Section', 'section', 'serial', '0xffaa00', '.6', '0', 'user', 'ALPHABETICAL-Lower', 'parens'),
('bill', 'SubSection', 'subsection', 'serial', '0x00ffff', '.8', '0', 'user', 'ROMAN-Upper', 'square'),
('bill', 'Chapter', 'chapter', 'serial', '0xe8ee88', '.4', '0', 'user', 'ROMAN-Upper', 'none'),
('bill', 'SubChapter', 'subchapter', 'serial', '0xffaa00', '.5', '0', 'user', 'ROMAN-Upper', 'postDotted'),
('bill', 'Paragraph', 'para', 'serial', '0xffff00', '.7', '0', 'user', 'ALPHABETICAL-Lower', 'parens'),
('bill', 'SubParagraph', 'subpara', 'serial', '0xffff00', '.8', '0', 'user', 'ROMAN-Lower', 'parens'),
('debaterecord', 'GroupActivity', 'comment', 'serial', ' url:/settings/sectiontypes/bg/shade-3.png', '0', '0', 'user', 'none', 'none'),
('debaterecord', 'Observation', 'comment', 'serial', 'url:/settings/sectiontypes/bg/shade-5.png', '.8', '0', 'user', 'none', 'none'),
('debaterecord', 'Communication', 'comnctn', 'serial', STRINGDECODE('\u00a0url:/settings/sectiontypes/bg/shade-5.png'), '.4', '0', 'user', 'none', 'none'),
('debaterecord', 'ActionEvent', 'actionevt', 'serial', STRINGDECODE('\t url:/settings/sectiontypes/bg/sand.png'), '0', '0', 'user', 'none', 'none'),
('debaterecord', 'MotionsContainer', 'allmotions', 'serial', STRINGDECODE('\turl:/settings/sectiontypes/bg/shade-3.png'), '.3', '0', 'user', 'none', 'none'),
('debaterecord', 'PapersLaid', 'papers', 'serial', 'url:/settings/sectiontypes/bg/shade-2.png', '.4', '0', 'user', 'none', 'none'),
('debaterecord', 'PapersLaidList', 'tbldocs', 'serial', 'url:/settings/sectiontypes/bg/shade-2.png', '.6', '0', 'user', 'none', 'none'),
('judgement', 'Introduction', 'intro', 'single', STRINGDECODE('\t url:/settings/sectiontypes/bg/lighter-green.png'), '0.55', '0', 'user', 'none', 'none'),
('judgement', 'Background', 'bg', 'single', STRINGDECODE('\t url:/settings/sectiontypes/bg/sand.png'), '0.55', '0', 'user', 'none', 'none'),
('bill', 'Preface', 'preface', 'single', '0xffffff', '.1', '0', 'user', 'none', 'none');
CREATE PRIMARY KEY PUBLIC.PRIMARY_KEY_F6 ON PUBLIC.DOCUMENT_SECTION_TYPES(DOC_TYPE, SECTION_TYPE_NAME);
CREATE CACHED TABLE PUBLIC.COUNTRY_CODES(
    COUNTRY_CODE CHAR(10) NOT NULL,
    COUNTRY_NAME CHAR(50) NOT NULL
);
ALTER TABLE PUBLIC.COUNTRY_CODES ADD CONSTRAINT PUBLIC.CONSTRAINT_A PRIMARY KEY(COUNTRY_CODE);
-- 3 +/- SELECT COUNT(*) FROM PUBLIC.COUNTRY_CODES;
INSERT INTO PUBLIC.COUNTRY_CODES(COUNTRY_CODE, COUNTRY_NAME) VALUES
('ke', 'Kenya'),
('ug', 'Uganda'),
('tz', 'Tanzania');
CREATE CACHED TABLE PUBLIC.LANGUAGE_CODES(
    LANG_CODE CHAR(10) NOT NULL,
    LANG_NAME CHAR(50) NOT NULL
);
ALTER TABLE PUBLIC.LANGUAGE_CODES ADD CONSTRAINT PUBLIC.CONSTRAINT_B PRIMARY KEY(LANG_CODE);
-- 3 +/- SELECT COUNT(*) FROM PUBLIC.LANGUAGE_CODES;
INSERT INTO PUBLIC.LANGUAGE_CODES(LANG_CODE, LANG_NAME) VALUES
('eng', 'English'),
('fra', 'French'),
('por', 'Portoguese');
CREATE CACHED TABLE PUBLIC.DOCUMENT_PART(
    DOC_TYPE VARCHAR(100) NOT NULL,
    DOC_PART CHAR(50) NOT NULL,
    PART_NAME CHAR(100) NOT NULL
);
ALTER TABLE PUBLIC.DOCUMENT_PART ADD CONSTRAINT PUBLIC.CONSTRAINT_1 PRIMARY KEY(DOC_TYPE, DOC_PART);
-- 8 +/- SELECT COUNT(*) FROM PUBLIC.DOCUMENT_PART;
INSERT INTO PUBLIC.DOCUMENT_PART(DOC_TYPE, DOC_PART, PART_NAME) VALUES
('debaterecord', 'main', 'Main'),
('debaterecord', 'annex', 'Annex'),
('judgement', 'main', 'Main'),
('judgement', 'annex', 'Annex'),
('bill', 'main', 'Main'),
('bill', 'annex', 'Annex'),
('act', 'main', 'Main'),
('act', 'annex', 'Annex');
CREATE CACHED TABLE PUBLIC.EDITOR_PANELS(
    DOCTYPE VARCHAR(100) NOT NULL,
    PANEL_TYPE VARCHAR(50) NOT NULL,
    PANEL_CLASS VARCHAR(100) NOT NULL,
    PANEL_LOAD_ORDER CHAR(2) NOT NULL,
    PANEL_TITLE VARCHAR(100) NOT NULL,
    PANEL_DESC VARCHAR(100),
    STATE INT,
    PANEL_NAME CHAR(20)
);
-- 21 +/- SELECT COUNT(*) FROM PUBLIC.EDITOR_PANELS;
INSERT INTO PUBLIC.EDITOR_PANELS(DOCTYPE, PANEL_TYPE, PANEL_CLASS, PANEL_LOAD_ORDER, PANEL_TITLE, PANEL_DESC, STATE, PANEL_NAME) VALUES
('debaterecord', 'tabbed', 'org.bungeni.editor.panels.loadable.validateAndCheckPanel2', '4', 'validate', 'Validate', 1, 'validate'),
('debaterecord', 'tabbed', 'org.bungeni.editor.panels.loadable.documentNotesPanel', '6', 'notes', 'Document notes and document switcher', 1, NULL),
('debaterecord', 'tabbed', 'org.bungeni.editor.panels.loadable.transformXMLPanel', '5', 'publish', 'Transform Debate', 1, NULL),
('internal', 'tabbed', 'org.bungeni.editor.panels.loadable.documentMetadataPanel', '1', 'Document', 'Document Metadata', 1, 'docmeta'),
('internal', 'tabbed', 'org.bungeni.editor.panels.loadable.sectionTreeMetadataPanel', '2', 'Section', 'Section Metadata', 1, 'sectionmeta'),
('debaterecord', 'tabbed', 'org.bungeni.editor.panels.loadable.MetadataPanel', '2', 'metadata', 'All Metadata', 1, 'allmeta'),
('debaterecord', 'tabbed', 'org.bungeni.editor.panels.loadable.documentStructurePanel', '3', 'structure', 'Structure', 1, 'structure'),
('debaterecord', 'tabbed', 'org.bungeni.editor.panels.loadable.documentActionPanel', '1', 'markup', 'Actions', 1, 'actions'),
('judgement', 'tabbed', 'org.bungeni.editor.panels.loadable.documentActionPanel', '1', 'markup', 'Actions', 1, 'actions'),
('judgement', 'tabbed', 'org.bungeni.editor.panels.loadable.documentStructurePanel', '3', 'structure', 'Structure', 1, 'structure'),
('judgement', 'tabbed', 'org.bungeni.editor.panels.loadable.MetadataPanel', '2', 'metadata', 'Document metadata', 1, 'allmeta'),
('judgement', 'tabbed', 'org.bungeni.editor.panels.loadable.validateAndCheckPanel2', '4', 'validate', 'validate', 0, 'validate'),
('judgement', 'tabbed', 'org.bungeni.editor.panels.loadable.transformXMLPanel', '5', 'publish', 'Transform Debate', 1, 'publish'),
('judgement', 'tabbed', 'org.bungeni.editor.panels.loadable.documentNotesPanel', '6', 'notes', 'notes', 1, NULL),
('bill', 'tabbed', STRINGDECODE('\torg.bungeni.editor.panels.loadable.documentActionPanel'), '1', 'markup', 'markup panel', 1, 'actions'),
('bill', 'tabbed', 'org.bungeni.editor.panels.loadable.documentMetadataPanel', '2', 'metadata', 'Bill Metadata', 1, NULL),
('bill', 'tabbed', 'org.bungeni.editor.panels.loadable.documentNotesPanel', '6', 'notes', 'Document notes and document ', 1, NULL),
('bill', 'tabbed', 'org.bungeni.editor.panels.loadable.documentStructurePanel', '3', 'structure', 'Bill Section Metadata', 1, 'sectionmeta'),
('bill', 'tabbed', 'org.bungeni.editor.panels.loadable.sectionNumbererPanel', '4', 'numbering', 'Bill Numbering', 1, NULL),
('bill', 'tabbed', 'org.bungeni.editor.panels.loadable.transformXMLPanel', '7', 'publish', 'Transform', 1, NULL),
('bill', 'tabbed', 'org.bungeni.editor.panels.loadable.validateAndCheckPanel2', '5', 'validate', 'Validate', 1, '');
CREATE PRIMARY KEY PUBLIC.PRIMARY_KEY_26 ON PUBLIC.EDITOR_PANELS(DOCTYPE, PANEL_TYPE, PANEL_CLASS);
CREATE CACHED TABLE PUBLIC.SELECTOR_DIALOGS(
    PARENT_DIALOG CHAR(200) NOT NULL,
    SELECTOR_DIALOG CHAR(200) NOT NULL,
    PROFILES CHAR(255)
);
ALTER TABLE PUBLIC.SELECTOR_DIALOGS ADD CONSTRAINT PUBLIC.CONSTRAINT_8A PRIMARY KEY(PARENT_DIALOG, SELECTOR_DIALOG);
-- 24 +/- SELECT COUNT(*) FROM PUBLIC.SELECTOR_DIALOGS;
INSERT INTO PUBLIC.SELECTOR_DIALOGS(PARENT_DIALOG, SELECTOR_DIALOG, PROFILES) VALUES
('org.bungeni.editor.selectors.debaterecord.speech.Main', 'org.bungeni.editor.selectors.debaterecord.speech.SpeechAs', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.conclusion.Main', 'org.bungeni.editor.selectors.debaterecord.conclusion.HouseClosingTime', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.masthead.Main', 'org.bungeni.editor.selectors.debaterecord.masthead.DebateRecordLogo', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.motions.Main', 'org.bungeni.editor.selectors.debaterecord.motions.MotionSelect', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.motions.Main', 'org.bungeni.editor.selectors.debaterecord.motions.MotionTitle', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.motions.Main', 'org.bungeni.editor.selectors.debaterecord.motions.MotionNameAndURI', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.motions.Main', 'org.bungeni.editor.selectors.debaterecord.motions.MotionText', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.question.Main', 'org.bungeni.editor.selectors.debaterecord.question.QuestionAddressedTo', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.question.Main', 'org.bungeni.editor.selectors.debaterecord.question.QuestionSelect', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.tableddocuments.MainSingleSelect', 'org.bungeni.editor.selectors.debaterecord.tableddocuments.SingleSelectTabledDocuments', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.speech.Main', 'org.bungeni.editor.selectors.debaterecord.speech.PersonSelector', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.speech.Main', 'org.bungeni.editor.selectors.debaterecord.speech.PersonURI', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.speech.Main', 'org.bungeni.editor.selectors.debaterecord.speech.SpeechBy', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.question.MainEdit', 'org.bungeni.editor.selectors.debaterecord.question.QuestionerNameAndURI', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.question.MainEdit', STRINGDECODE('\torg.bungeni.editor.selectors.debaterecord.question.QuestionAddressedTo'), 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.question.MainEdit', 'org.bungeni.editor.selectors.debaterecord.question.QuestionNo', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.motions.MainEdit', 'org.bungeni.editor.selectors.debaterecord.motions.MotionNameAndURI', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.tableddocuments.MainDataEntry', 'org.bungeni.editor.selectors.debaterecord.tableddocuments.EnterTabledDocument', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.tableddocuments.Main', 'org.bungeni.editor.selectors.debaterecord.tableddocuments.TabledDocuments', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.question.Main', 'org.bungeni.editor.selectors.debaterecord.question.QuestionTitle', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.question.Main', 'org.bungeni.editor.selectors.debaterecord.question.QuestionerNameAndURI', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.question.Main', 'org.bungeni.editor.selectors.debaterecord.question.QuestionText', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.committees.Main', 'org.bungeni.editor.selectors.debaterecord.committees.Committees', 'withBackend'),
('org.bungeni.editor.selectors.debaterecord.bills.Main', STRINGDECODE('\torg.bungeni.editor.selectors.debaterecord.bills.Bills'), 'withBackend');
CREATE CACHED TABLE PUBLIC.SUB_ACTION_SETTINGS(
    DOC_TYPE VARCHAR(100) NOT NULL,
    PARENT_ACTION_NAME VARCHAR(100) NOT NULL,
    SUB_ACTION_NAME VARCHAR(100) NOT NULL,
    SUB_ACTION_ORDER INT NOT NULL,
    SUB_ACTION_STATE INT NOT NULL,
    ACTION_TYPE VARCHAR(50),
    ACTION_DISPLAY_TEXT VARCHAR(100),
    ACTION_FIELDS VARCHAR(100),
    ACTION_CLASS VARCHAR(70),
    SYSTEM_CONTAINER VARCHAR(50),
    VALIDATOR_CLASS CHAR(100),
    ROUTER_CLASS VARCHAR(100),
    DIALOG_CLASS VARCHAR(100),
    COMMAND_CHAIN VARCHAR(100)
);
-- 107 +/- SELECT COUNT(*) FROM PUBLIC.SUB_ACTION_SETTINGS;
INSERT INTO PUBLIC.SUB_ACTION_SETTINGS(DOC_TYPE, PARENT_ACTION_NAME, SUB_ACTION_NAME, SUB_ACTION_ORDER, SUB_ACTION_STATE, ACTION_TYPE, ACTION_DISPLAY_TEXT, ACTION_FIELDS, ACTION_CLASS, SYSTEM_CONTAINER, VALIDATOR_CLASS, ROUTER_CLASS, DIALOG_CLASS, COMMAND_CHAIN) VALUES
('debaterecord', 'makeProcMotionBlockSection', 'section_creation', 1, 1, 'section_create', 'create procedural motion', '', STRINGDECODE('\torg.bungeni.editor.actions.EditorSelectionActionHandler'), '', 'org.bungeni.editor.actions.validators.defaultValidator', STRINGDECODE('\torg.bungeni.editor.actions.routers.routerCreateSection'), 'org.bungeni.editor.selectors.debaterecord.motions.MotionSelect', ''),
('debaterecord', 'makeDebateMotionBlockSection', 'section_creation', 1, 1, 'section_create', 'create a motion section', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerMotionSelectMotion', 'org.bungeni.editor.selectors.debaterecord.motions.MotionSelect', STRINGDECODE('\u00a0')),
('debaterecord', 'makeProcMotionBlockSection', 'markup_motion_title', 1, 1, 'mark_as_title', 'create proc motion title', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerApplyStyle', 'org.bungeni.editor.selectors.debaterecord.motions.MotionSelect', STRINGDECODE('\u00a0')),
('debaterecord', 'makeDebateMotionBlockSectionEdit', 'section_creation', 1, 1, 'section_create', 'create a motion sectipn', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerMotionSelectMotion', '', ''),
('debaterecord', 'makeDebateQuestionBlockSectionEdit', 'section_creation', 1, 1, 'section_create', 'create a question section', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerDebateRecordSelectQuestion', STRINGDECODE('\u00a0'), STRINGDECODE('\u00a0')),
('debaterecord', 'makeDebateMastheadSection', 'create_root_section', 1, 1, 'create_root', 'createroot section', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateRoot', '', ''),
('bill', 'makeBillPrefaceSection', 'section_creation', 1, 1, 'section_create', 'dummy', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('judgement', 'makeJudgementHeader', 'workflow_action', 1, 1, 'workflow_action', 'create a workflow action', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerMarkWorkflowAction', 'org.bungeni.editor.actions.routers.routerMarkWorkflowAction_panel', ''),
('bill', 'makeBillClauseSection', 'section_creation', 1, 1, 'section_create', 'dummy', ' ', 'org.bungeni.editor.actions.EditorSelectionActionHandler', ' ', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.InitBillPreface', ' '),
('bill', 'makeBillArticleSection', 'section_creation', 1, 1, 'section_create', 'dummy', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('bill', 'makeBillPartSection', 'section_creation', 1, 1, 'section_create', 'dummy', ' ', 'org.bungeni.editor.actions.EditorSelectionActionHandler', ' ', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.InitBillPreface', ' ');
INSERT INTO PUBLIC.SUB_ACTION_SETTINGS(DOC_TYPE, PARENT_ACTION_NAME, SUB_ACTION_NAME, SUB_ACTION_ORDER, SUB_ACTION_STATE, ACTION_TYPE, ACTION_DISPLAY_TEXT, ACTION_FIELDS, ACTION_CLASS, SYSTEM_CONTAINER, VALIDATOR_CLASS, ROUTER_CLASS, DIALOG_CLASS, COMMAND_CHAIN) VALUES
('debaterecord', 'makePointOfOrder', 'section_creation', 1, 1, 'section_create', 'Create Point of Order', '', STRINGDECODE('\torg.bungeni.editor.actions.EditorSelectionActionHandler'), '', STRINGDECODE('\torg.bungeni.editor.actions.validators.defaultValidator'), STRINGDECODE('\torg.bungeni.editor.actions.routers.routerCreateSection'), '', ''),
('debaterecord', 'general_action', 'markup_heading', 1, 1, 'markup_heading', 'Create a Named Heading', '', STRINGDECODE('\torg.bungeni.editor.actions.EditorSelectionActionHandler'), '', 'org.bungeni.editor.actions.validators.defaultValidator', STRINGDECODE('\torg.bungeni.editor.actions.routers.routerApplyStyle'), '', ''),
('bill', 'makeBillPartSection', 'apply_numbered_heading', 1, 1, 'apply_numbered_heading', 'dummy', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateNumberedHeading', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('bill', 'makeBillSectionSection', 'section_creation', 1, 1, 'section_create', 'dummy', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.InitBillPreface', '.'),
('bill', 'makeBillSectionSection', 'apply_numbered_heading', 1, 1, 'apply_numbered_heading', 'dummy', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateNumberedHeading', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('judgement', 'makeMotivation', 'make_motivation_heading', 1, 1, 'apply_style', 'create motivation heading', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerApplyStyle', '', ''),
('judgement', 'makeMotivation', 'make_motivation_subheading', 1, 1, 'apply_style', 'crete motivation subheading', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerApplyStyle', '', ''),
('judgement', 'makeDecision', 'make_decision_heading', 1, 1, 'apply_style', 'create decision heading', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerApplyStyle', '', ''),
('judgement', 'makeBackground', 'make_background_heading', 1, 1, 'apply_style', 'create background heding', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerApplyStyle', '', ''),
('judgement', 'makeIntroduction', 'make_introduction_heading', 1, 1, 'apply_style', 'create introduction heading', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerApplyStyle', '', ''),
('judgement', 'makeJudgementHeader', 'make_judgement_title', 1, 1, 'apply_style', 'create judgement title', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerApplyStyle', '', ''),
('bill', 'makeBillChapterSection', 'section_creation', 1, 1, 'section_create', 'dummy', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('bill', 'makeBillSubChapterSection', 'section_creation', 1, 1, 'section_create', 'dummy', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.InitBillPreface', STRINGDECODE('\u00a0'));
INSERT INTO PUBLIC.SUB_ACTION_SETTINGS(DOC_TYPE, PARENT_ACTION_NAME, SUB_ACTION_NAME, SUB_ACTION_ORDER, SUB_ACTION_STATE, ACTION_TYPE, ACTION_DISPLAY_TEXT, ACTION_FIELDS, ACTION_CLASS, SYSTEM_CONTAINER, VALIDATOR_CLASS, ROUTER_CLASS, DIALOG_CLASS, COMMAND_CHAIN) VALUES
('bill', 'makeBillParaSection', 'section_creation', 1, 1, 'section_create', 'dummy', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('bill', 'makeBillSubParaSection', 'section_creation', 1, 1, 'section_create', 'dummy', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('bill', 'makeBillSubSectionSection', 'section_creation', 1, 1, 'section_create', 'dummy', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('bill', 'makeBillChapterSection', 'apply_numbered_heading', 1, 1, 'apply_numbered_heading', 'dummy', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateNumberedHeading', 'org.bungeni.editor.selectors.InitBillPreface', STRINGDECODE('\u00a0')),
('bill', 'makeBillParaSection', 'apply_numbered_heading', 1, 1, 'apply_numbered_heading', 'dummy', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateNumberedHeading', 'org.bungeni.editor.selectors.InitBillPreface', STRINGDECODE('\u00a0')),
('bill', 'makeBillSubParaSection', 'apply_numbered_heading', 1, 1, 'apply_numbered_heading', 'dummy', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateNumberedHeading', 'org.bungeni.editor.selectors.InitBillPreface', STRINGDECODE('\u00a0')),
('bill', 'makeBillSubChapterSection', 'apply_numbered_heading', 1, 1, 'apply_numbered_heading', 'dum', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateNumberedHeading', 'org.bungeni.editor.selectors.InitBillPreface', STRINGDECODE('\u00a0')),
('bill', 'makeBillSubSectionSection', 'apply_numbered_heading', 1, 1, 'apply_numbered_heading', 'dummy', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateNumberedHeading', 'org.bungeni.editor.selectors.InitBillPreface', STRINGDECODE('\u00a0')),
('bill', 'makeBillChapterSection', 'composite_section_creation', 1, 1, 'composite_section_create', 'dummy', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateCompositeSection', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('bill', 'makeBillSubChapterSection', 'composite_section_creation', 1, 1, 'composite_section_create', 'dummy', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateCompositeSection', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('bill', 'makeBillSectionSection', 'composite_section_creation', 1, 1, 'composite_section_create', 'dummy', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateCompositeSection', 'org.bungeni.editor.selectors.InitBillPreface', '');
INSERT INTO PUBLIC.SUB_ACTION_SETTINGS(DOC_TYPE, PARENT_ACTION_NAME, SUB_ACTION_NAME, SUB_ACTION_ORDER, SUB_ACTION_STATE, ACTION_TYPE, ACTION_DISPLAY_TEXT, ACTION_FIELDS, ACTION_CLASS, SYSTEM_CONTAINER, VALIDATOR_CLASS, ROUTER_CLASS, DIALOG_CLASS, COMMAND_CHAIN) VALUES
('bill', 'makeBillSubSectionSection', 'composite_section_creation', 1, 1, 'composite_section_create', 'dummy', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateCompositeSection', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('debaterecord', 'makeDebateMastheadSection', 'section_creation', 1, 1, 'section_create', 'create masthead section', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.InitBillPreface', STRINGDECODE('\u00a0')),
('debaterecord', 'makeCommunicationAction', 'apply_style', 1, 1, 'apply_style', 'Apply Style', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', STRINGDECODE('\torg.bungeni.editor.actions.routers.routerApplyStyle'), '', ''),
('debaterecord', 'makeDebateMastheadSection', 'debatedate', 1, 1, 'debatedate', 'dummy', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerDebateRecordDateEntry', 'org.bungeni.editor.selectors.debaterecord.masthead.DebateRecordDate', STRINGDECODE('\u00a0')),
('debaterecord', 'makeDebateMastheadSection', 'debatetime', 1, 1, 'debatetime', 'dummy', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerDebateRecordTimeEntry', 'org.bungeni.editor.selectors.debaterecord.masthead.DebateRecordTime', STRINGDECODE('\u00a0')),
('debaterecord', 'makeDebateMastheadSection', 'markup_logo', 1, 1, 'debatelogo', 'dummy', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerMarkupLogo', 'org.bungeni.editor.selectors.debaterecord.masthead.DebateRecordLogo', STRINGDECODE('\u00a0')),
('debaterecord', 'makeDebateQuestionBlockSection', 'section_creation', 1, 1, 'section_create', 'creates a section for the question block', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerDebateRecordSelectQuestion', 'org.bungeni.editor.selectors.debaterecord.question.QuestionSelect', STRINGDECODE('\u00a0')),
('debaterecord', 'makeDebateQuestionBlockSection', 'markup_question_no', 1, 1, 'markup_reference', 'mark a block of text as a question reference', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateQuestionReference', 'org.bungeni.editor.selectors.debaterecord.question.QuestionSelect', STRINGDECODE('\u00a0')),
('debaterecord', 'makeDebateQuestionSection', 'section_creation', 1, 1, 'section_create', '', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.debaterecord.question.QuestionSelect', ''),
('debaterecord', 'makeDebateQuestionGroupSection', 'section_creation', 1, 1, 'section_create', STRINGDECODE('\u00a0'), STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.debaterecord.question.QuestionSelect', STRINGDECODE('\u00a0'));
INSERT INTO PUBLIC.SUB_ACTION_SETTINGS(DOC_TYPE, PARENT_ACTION_NAME, SUB_ACTION_NAME, SUB_ACTION_ORDER, SUB_ACTION_STATE, ACTION_TYPE, ACTION_DISPLAY_TEXT, ACTION_FIELDS, ACTION_CLASS, SYSTEM_CONTAINER, VALIDATOR_CLASS, ROUTER_CLASS, DIALOG_CLASS, COMMAND_CHAIN) VALUES
('debaterecord', 'makeDebateQuestionBlockSection', 'markup_question_by', 1, 1, 'markup_reference', 'mark a block of text as a question by reference', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateQuestionByReference', 'org.bungeni.editor.selectors.debaterecord.question.QuestionSelect', ''),
('debaterecord', 'makeDebateQuestionBlockSection', 'markup_question_to', 1, 1, 'markup_reference', 'mark a block of text as a question to reference', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateQuestionToReference', 'org.bungeni.editor.selectors.debaterecord.question.QuestionSelect', ''),
('debaterecord', 'makeDebateQuestionBlockSection', 'apply_questiontext_style', 1, 1, 'apply_style', 'mark the selected text with a style', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerApplyStyle', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('debaterecord', 'makeDebateSpeechBlockSection', 'section_creation', 1, 1, 'section_create', 'create a speech section', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerDebateRecordSelectSpeech', 'org.bungeni.editor.selectors.InitBillPreface', STRINGDECODE('\u00a0')),
('debaterecord', 'makeDebateSpeechBlockSection', 'speech_by', 1, 1, 'speech_by', 'create speech to reference', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSpeechByReference', 'org.bungeni.editor.selectors.InitBillPreface', STRINGDECODE('\u00a0')),
('debaterecord', 'makeObservationAction', 'section_creation', 1, 1, 'section_create', 'create an observation section', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateObservation', 'org.bungeni.editor.selectors.InitBillPreface', STRINGDECODE('\u00a0')),
('debaterecord', 'makeGroupActivityAction', 'section_creation', 1, 1, 'section_create', 'create a group activity section', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('debaterecord', 'makeCommunicationAction', 'section_creation', 1, 1, 'section_create', 'create a communication section', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('debaterecord', 'makePointOfOrder', 'apply_style', 1, 1, 'apply_style', 'Create Porde heading', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerApplyStyle', '', ''),
('debaterecord', 'makeAdjournmentAction', 'mark_as_action', 1, 1, 'mark_as_action', 'crearte an action event', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerMarkAsAction', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('debaterecord', 'makeConclusionSection', 'section_creation', 1, 1, 'section_create', 'create a conclusion section', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.InitBillPreface', '');
INSERT INTO PUBLIC.SUB_ACTION_SETTINGS(DOC_TYPE, PARENT_ACTION_NAME, SUB_ACTION_NAME, SUB_ACTION_ORDER, SUB_ACTION_STATE, ACTION_TYPE, ACTION_DISPLAY_TEXT, ACTION_FIELDS, ACTION_CLASS, SYSTEM_CONTAINER, VALIDATOR_CLASS, ROUTER_CLASS, DIALOG_CLASS, COMMAND_CHAIN) VALUES
('debaterecord', 'makeConclusionSection', 'mark_house_closing', 1, 1, 'mark_house_closing', 'create a house closing time reference', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerMarkHouseClosing', 'org.bungeni.editor.selectors.debaterecord.conclusion.HouseClosingTime', STRINGDECODE('\u00a0')),
('debaterecord', 'makeNoticeOfMotionAction', 'mark_as_action', 1, 1, 'mark_as_action', 'create an action event', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerMarkAsAction', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('judgement', 'makeJudgementHeader', 'scale_section', 1, 1, 'section_create', 'create a scaled section', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateScaledSection', 'org.bungeni.editor.actions.routers.routerCreateScaledSection_panel', STRINGDECODE('\u00a0')),
('debaterecord', 'makePapersLaidBlockSection', 'section_creation', 1, 1, 'section_create', 'create a papers laid container', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', '', STRINGDECODE('\u00a0')),
('debaterecord', 'makeMetadataReference', 'mark', 1, 1, 'mark', 'create a document metadata reference', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateUniqueReference', 'org.bungeni.editor.selectors.InitBillPreface', STRINGDECODE('\u00a0')),
('bill', 'makeBillGeneralAction', 'create_sidenote', 1, 1, 'sidenote_create', 'create sidenote', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSidenote', 'org.bungeni.editor.actions.routers.routerCreateSideNote_panel', STRINGDECODE('\u00a0')),
('debaterecord', 'makeDebateMotionBlockSection', 'markup_motion_title', 1, 1, 'mark_as_title', 'create a motion title', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerApplyStyle', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('debaterecord', 'makeDebateMotionGroupSection', 'section_creation', 1, 1, 'section_create', 'create motion container ', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('debaterecord', 'makePapersLaidBlockSection', 'markup_papers_heading', 1, 1, 'apply_style', 'create a papers heading', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerApplyStyle', '', STRINGDECODE('\u00a0')),
('debaterecord', 'makeDebateMastheadSection', 'markup_preface_heading', 1, 1, 'apply_style', 'create a papers heading', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerApplyStyle', 'org.bungeni.editor.selectors.InitBillPreface', '');
INSERT INTO PUBLIC.SUB_ACTION_SETTINGS(DOC_TYPE, PARENT_ACTION_NAME, SUB_ACTION_NAME, SUB_ACTION_ORDER, SUB_ACTION_STATE, ACTION_TYPE, ACTION_DISPLAY_TEXT, ACTION_FIELDS, ACTION_CLASS, SYSTEM_CONTAINER, VALIDATOR_CLASS, ROUTER_CLASS, DIALOG_CLASS, COMMAND_CHAIN) VALUES
('debaterecord', 'makePapersLaidBlockSection', 'select_documents', 1, 1, 'select_documents', 'select documents', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerTabledDocuments', 'org.bungeni.editor.selectors.debaterecord.tableddocuments.TabledDocuments', STRINGDECODE('\u00a0')),
('debaterecord', 'makePersonalStatement', 'section_creation', 1, 1, 'section_create', 'Create a Personal Statement section', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', '', ''),
('debaterecord', 'makePapersLaidListSection', 'section_creation', 1, 1, 'section_create', 'create papers list section', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', '', ''),
('judgement', 'makeJudgementHeader', 'section_creation', 1, 1, 'section_create', 'create judgement header section', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', '', ''),
('judgement', 'makeMetadataReference', 'mark', 1, 1, 'mark', 'create a document metadata reference', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateUniqueReference', '', ''),
('judgement', 'makeMotivation', 'section_creation', 1, 1, 'section_create', 'create judgement header section', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', '', ''),
('judgement', 'makeDecision', 'section_creation', 1, 1, 'section_create', 'create judgement header section', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', '', ''),
('judgement', 'makeBackground', 'section_creation', 1, 1, 'section_create', 'create judgement header section', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', '', ''),
('judgement', 'makeConclusion', 'section_creation', 1, 1, 'section_create', 'create judgement header section', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', '', ''),
('judgement', 'makeIntroduction', 'section_creation', 1, 1, 'section_create', 'create a communication section', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', '', ''),
('judgement', 'makeIntroduction', 'scale_section', 1, 1, 'section_create', 'create a scaled section', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateScaledSection', 'org.bungeni.editor.actions.routers.routerCreateScaledSection_panel', STRINGDECODE('\u00a0')),
('debaterecord', 'makePapersLaidMarkupSingle', 'markup_doc_link', 1, 1, 'markup_link', 'Markup a single tabled document', '', STRINGDECODE('\torg.bungeni.editor.actions.EditorSelectionActionHandler'), '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerTabledDocuments', '', '');
INSERT INTO PUBLIC.SUB_ACTION_SETTINGS(DOC_TYPE, PARENT_ACTION_NAME, SUB_ACTION_NAME, SUB_ACTION_ORDER, SUB_ACTION_STATE, ACTION_TYPE, ACTION_DISPLAY_TEXT, ACTION_FIELDS, ACTION_CLASS, SYSTEM_CONTAINER, VALIDATOR_CLASS, ROUTER_CLASS, DIALOG_CLASS, COMMAND_CHAIN) VALUES
('judgement', 'makeJudgementHeader', 'tabular_judge_reference', 1, 1, 'tabular_reference', 'create a tabular reference', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateTabularMetadataReference', 'org.bungeni.editor.actions.routers.routerCreateBungeniJudgeName_panel', STRINGDECODE('\u00a0')),
('bill', 'makeBillArticleSection', 'apply_numbered_heading', 1, 1, 'section_create', 'dummy', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateNumberedHeading', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('bill', 'makeBillClauseSection', 'apply_numbered_heading', 1, 1, 'section_create', 'dummy', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateNumberedHeading', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('debaterecord', 'makeQuestionBlockSection', 'section_creation', 1, 1, 'section_create', 'Create Question Block', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.InitQuestionBlock', ''),
('debaterecord', 'makeQASection', 'section_creation', 1, 1, 'section_create', 'Create QA Section', ' ', 'org.bungeni.editor.actions.EditorSelectionActionHandler', ' ', 'org.bungeni.editor.actions.validators.validateCreateSection', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.InitQASection', ''),
('debaterecord', 'makeDebateSpeechBlockSection', 'speech_as', 1, 1, 'speech_as', 'create speech as reference', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSpeechAsReference', 'org.bungeni.editor.selectors.InitBillPreface', STRINGDECODE('\u00a0')),
('debaterecord', 'makeDebateSpeechBlockSection', 'lock', 1, 1, 'lock', 'lock heading', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateHeadingLock', 'org.bungeni.editor.selectors.InitBillPreface', STRINGDECODE('\u00a0')),
('debaterecord', 'makeDebateSpeechBlockSection', 'unlockHead', 1, 1, 'unlock', 'unlock heading ', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateHeadingUnlock', 'org.bungeni.editor.selectors.InitBillPreface', ''),
('debaterecord', 'makePaperSection', 'import_tabled_documents', 2, 1, 'field_action', 'Import tabled documents', ' ', 'org.bungeni.editor.actions.EditorSelectionActionHandler', ' ', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerTabledDocuments', 'org.bungeni.editor.selectors.InitPapers', 'debaterecord:importTabledDocuments'),
('debaterecord', 'makePrayerSection', 'section_creation', 0, 1, 'section_create', 'Create emtpy masthead', ' ', 'org.bungeni.editor.actions.EditorSelectionActionHandler', ' ', 'org.bungeni.editor.actions.validators.validateCreateSection', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.InitDebateRecord', ''),
('debaterecord', 'makePrayerSection', 'debatedate_entry', 1, 1, 'field_action', 'Markup debate date', 'dt:initdebate_hansarddate', 'org.bungeni.editor.actions.EditorSelectionActionHandler', 'int:masthead_datetime', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerDebateDateEntry', 'org.bungeni.editor.selectors.InitDebateRecord', '');
INSERT INTO PUBLIC.SUB_ACTION_SETTINGS(DOC_TYPE, PARENT_ACTION_NAME, SUB_ACTION_NAME, SUB_ACTION_ORDER, SUB_ACTION_STATE, ACTION_TYPE, ACTION_DISPLAY_TEXT, ACTION_FIELDS, ACTION_CLASS, SYSTEM_CONTAINER, VALIDATOR_CLASS, ROUTER_CLASS, DIALOG_CLASS, COMMAND_CHAIN) VALUES
('debaterecord', 'general_action', 'create_root_section', 1, 1, 'root_create', 'create root section', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateRoot', '', ''),
('debaterecord', 'makePaperSection', 'apply_style', 1, 1, 'markup', 'Apply papers style', ' ', 'org.bungeni.editor.actions.EditorSelectionActionHandler', ' ', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerApplyStyle', 'org.bungeni.editor.selectors.InitPapers', ''),
('debaterecord', 'makePrayerSection', 'markup_logo', 3, 1, 'field_action', 'Apply logo', 'btn:initdebate_selectlogo', 'org.bungeni.editor.actions.EditorSelectionActionHandler', ' ', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerMarkupLogo', 'org.bungeni.editor.selectors.InitDebateRecord', ''),
('debaterecord', 'makePrayerSection', 'debatetime_entry', 2, 1, 'field_action', 'Markup debate time', 'dt:initdebate_timeofhansard', 'org.bungeni.editor.actions.EditorSelectionActionHandler', 'int:masthead_datetime', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerDebateTimeEntry', 'org.bungeni.editor.selectors.InitDebateRecord', ''),
('debaterecord', 'makePaperSection', 'section_creation', 0, 1, 'section_create', 'Create empty Paper Section', ' ', 'org.bungeni.editor.actions.EditorSelectionActionHandler', ' ', 'org.bungeni.editor.actions.validators.validateCreateSection', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.InitPapers', ''),
('judgement', 'makeJudgementHeader', 'tabular_party_reference', 1, 1, 'tabular_reference', 'create tabular party reference', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateTabularMetadataReference', 'org.bungeni.editor.actions.routers.routerCreateBungeniPartyName_panel', STRINGDECODE('\u00a0')),
('debaterecord', 'makePapersLaidDataEntry', 'markup_doc_link', 1, 1, 'markup_link', 'Markup document link', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', STRINGDECODE('\torg.bungeni.editor.actions.validators.defaultValidator'), 'org.bungeni.editor.actions.routers.routerTabledDocuments', '', ''),
('judgement', 'makeMotivation', 'make_motivation_num', 1, 1, 'apply_style', 'create num reference', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateGenericJudgementNumberReference', '', ''),
('debaterecord', 'makeProcMotionGroupSection', 'section_creation', 1, 1, 'section_create', 'create container for procedural motions', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', 'org.bungeni.editor.selectors.debaterecord.motions.MotionSelect', ''),
('debaterecord', 'makeMinisterialStatement', 'section_creation', 1, 1, 'section_create', 'Create a Ministerial Statement', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', STRINGDECODE('\torg.bungeni.editor.actions.routers.routerCreateSection'), '', ''),
('debaterecord', 'makeActionEvent', 'section_creation', 1, 1, 'section_create', 'Create Action Event', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateEvent', '', ''),
('judgement', 'makeBackground', 'scale_section', 1, 1, 'section_create', 'create a scaled section', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateScaledSection', 'org.bungeni.editor.actions.routers.routerCreateScaledSection_panel', '');
INSERT INTO PUBLIC.SUB_ACTION_SETTINGS(DOC_TYPE, PARENT_ACTION_NAME, SUB_ACTION_NAME, SUB_ACTION_ORDER, SUB_ACTION_STATE, ACTION_TYPE, ACTION_DISPLAY_TEXT, ACTION_FIELDS, ACTION_CLASS, SYSTEM_CONTAINER, VALIDATOR_CLASS, ROUTER_CLASS, DIALOG_CLASS, COMMAND_CHAIN) VALUES
('judgement', 'makeMotivation', 'scale_section', 1, 1, 'section_create', 'create a scaled section', '', STRINGDECODE('\torg.bungeni.editor.actions.EditorSelectionActionHandler'), '', STRINGDECODE('\torg.bungeni.editor.actions.validators.defaultValidator'), 'org.bungeni.editor.actions.routers.routerCreateScaledSection', 'org.bungeni.editor.actions.routers.routerCreateScaledSection_panel', ''),
('judgement', 'makeDecision', 'scale_section', 1, 1, 'section_create', 'create a scaled section', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateScaledSection', 'org.bungeni.editor.actions.routers.routerCreateScaledSection_panel', ''),
('judgement', 'makeOmissisSection', 'section_creation', 1, 1, 'section_create', 'create omissis section', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateSection', '', ''),
('debaterecord', 'makeSelectCommittee', 'markup_committee', 1, 1, 'markup_committee', 'associate committe with section', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.EditorSelectionActionHandler', STRINGDECODE('\u00a0'), 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerSelectCommittee', 'org.bungeni.editor.selectors.debaterecord.committees.Committees', STRINGDECODE('\u00a0')),
('debaterecord', 'makeSelectBill', 'markup_bill', 1, 1, 'markup_bill', 'associate bill with a section', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', STRINGDECODE('\torg.bungeni.editor.actions.routers.routerSelectBill'), 'org.bungeni.editor.selectors.debaterecord.bills.Bills', ''),
('judgement', 'makeMotivation', 'make_motivation_numheading', 1, 1, 'apply_style', 'create numbered heading', '', 'org.bungeni.editor.actions.EditorSelectionActionHandler', '', 'org.bungeni.editor.actions.validators.defaultValidator', 'org.bungeni.editor.actions.routers.routerCreateNumberedHeadingJudgement', '', '');
CREATE PRIMARY KEY PUBLIC.PRIMARY_KEY_C0 ON PUBLIC.SUB_ACTION_SETTINGS(DOC_TYPE, PARENT_ACTION_NAME, SUB_ACTION_NAME);
CREATE INDEX PUBLIC.SUBACTIONORDER_IDX ON PUBLIC.SUB_ACTION_SETTINGS(SUB_ACTION_ORDER);
CREATE CACHED TABLE PUBLIC.EDITOR_PROFILES(
    PROFILE_NAME CHAR(50) NOT NULL,
    PROFILE_DESC CHAR(200) NOT NULL
);
ALTER TABLE PUBLIC.EDITOR_PROFILES ADD CONSTRAINT PUBLIC.CONSTRAINT_E PRIMARY KEY(PROFILE_NAME);
-- 3 +/- SELECT COUNT(*) FROM PUBLIC.EDITOR_PROFILES;
INSERT INTO PUBLIC.EDITOR_PROFILES(PROFILE_NAME, PROFILE_DESC) VALUES
('withoutBackend', 'Editor with a minimal backend'),
('withBackend', 'Editor with full backend'),
('withBungeniBackend', 'Editor with full buneni backend');
CREATE CACHED TABLE PUBLIC.EVENT_ONTOLOGIES(
    DOC_TYPE VARCHAR(100) NOT NULL,
    ONTOLOGY VARCHAR(255) NOT NULL,
    EVENT_NAME CHAR(100) NOT NULL,
    EVENT_DESC CHAR(200)
);
ALTER TABLE PUBLIC.EVENT_ONTOLOGIES ADD CONSTRAINT PUBLIC.CONSTRAINT_C PRIMARY KEY(DOC_TYPE, ONTOLOGY);
-- 9 +/- SELECT COUNT(*) FROM PUBLIC.EVENT_ONTOLOGIES;
INSERT INTO PUBLIC.EVENT_ONTOLOGIES(DOC_TYPE, ONTOLOGY, EVENT_NAME, EVENT_DESC) VALUES
('debaterecord', '/ontology/event/question/proposed', 'QuestionProposed', 'Question Proposed'),
('debaterecord', '/ontology/event/question/accepted', 'QuestionAccepted', 'Question Accepted'),
('debaterecord', '/ontology/event/question/withdrawn', 'QuestionWithdrawn', 'Question Withdrawn'),
('debaterecord', '/ontology/event/question/deferred', 'QuestionDeferred', 'Question Deferred'),
('debaterecord', '/ontology/event/debate/interruption', 'DebateInterruption', 'Debate Interruption'),
('debaterecord', '/ontology/event/debate/resumption', 'DebateResumption', 'Debate Resumption'),
('debaterecord', '/ontology/event/bill/reading/1', 'BillFirstReading', 'First Reading'),
('debaterecord', '/ontology/event/bill/reading/2', 'BillSecondReading', 'Second Reading'),
('debaterecord', '/ontology/event/bill/reading/3', 'BillThirdReading', 'Third Reading');
ALTER TABLE PUBLIC.EDITOR_PANELS ADD CONSTRAINT PUBLIC.FK_EDITOR_PANELS_DOCTYPE FOREIGN KEY(DOCTYPE) REFERENCES PUBLIC.DOCUMENT_TYPES(DOC_TYPE) NOCHECK;
ALTER TABLE PUBLIC.EVENT_ONTOLOGIES ADD CONSTRAINT PUBLIC.CONSTRAINT_CE FOREIGN KEY(DOC_TYPE) REFERENCES PUBLIC.DOCUMENT_TYPES(DOC_TYPE) NOCHECK;
ALTER TABLE PUBLIC.EDITOR_PANELS ADD CONSTRAINT PUBLIC.CONSTRAINT_26 FOREIGN KEY(DOCTYPE) REFERENCES PUBLIC.DOCUMENT_TYPES(DOC_TYPE) NOCHECK;
ALTER TABLE PUBLIC.DOCUMENT_METADATA ADD CONSTRAINT PUBLIC.CONSTRAINT_FB FOREIGN KEY(DOC_TYPE) REFERENCES PUBLIC.DOCUMENT_TYPES(DOC_TYPE) NOCHECK;
ALTER TABLE PUBLIC.SUB_ACTION_SETTINGS ADD CONSTRAINT PUBLIC.CONSTRAINT_C0 FOREIGN KEY(DOC_TYPE) REFERENCES PUBLIC.DOCUMENT_TYPES(DOC_TYPE) NOCHECK;
ALTER TABLE PUBLIC.DOCUMENT_SECTION_TYPES ADD CONSTRAINT PUBLIC.CONSTRAINT_F6 FOREIGN KEY(DOC_TYPE) REFERENCES PUBLIC.DOCUMENT_TYPES(DOC_TYPE) NOCHECK;
ALTER TABLE PUBLIC.TOOLBAR_XML_CONFIG ADD CONSTRAINT PUBLIC.CONSTRAINT_B0 FOREIGN KEY(DOC_TYPE) REFERENCES PUBLIC.DOCUMENT_TYPES(DOC_TYPE) NOCHECK;
ALTER TABLE PUBLIC.DOCUMENT_SECTION_TYPES ADD CONSTRAINT PUBLIC.CONSTRAINT_2 FOREIGN KEY(DOC_TYPE) REFERENCES PUBLIC.DOCUMENT_TYPES(DOC_TYPE) NOCHECK;
ALTER TABLE PUBLIC.METADATA_MODEL_EDITORS ADD CONSTRAINT PUBLIC.CONSTRAINT_3 FOREIGN KEY(DOC_TYPE) REFERENCES PUBLIC.DOCUMENT_TYPES(DOC_TYPE) NOCHECK;
ALTER TABLE PUBLIC.SUB_ACTION_SETTINGS ADD CONSTRAINT PUBLIC.CONSTRAINT_10 FOREIGN KEY(DOC_TYPE, PARENT_ACTION_NAME) REFERENCES PUBLIC.ACTION_SETTINGS(DOC_TYPE, ACTION_NAME) NOCHECK;
ALTER TABLE PUBLIC.TOOLBAR_CONDITIONS ADD CONSTRAINT PUBLIC.TBC_FK_DOCTYPE FOREIGN KEY(DOCTYPE) REFERENCES PUBLIC.DOCUMENT_TYPES(DOC_TYPE) NOCHECK;
ALTER TABLE PUBLIC.TOOLBAR_XML_CONFIG ADD CONSTRAINT PUBLIC.CONSTRAINT_5 FOREIGN KEY(DOC_TYPE) REFERENCES PUBLIC.DOCUMENT_TYPES(DOC_TYPE) NOCHECK;
ALTER TABLE PUBLIC.TOOLBAR_CONDITIONS ADD CONSTRAINT PUBLIC.CONSTRAINT_15 FOREIGN KEY(DOCTYPE) REFERENCES PUBLIC.DOCUMENT_TYPES(DOC_TYPE) NOCHECK;
ALTER TABLE PUBLIC.DOCUMENT_METADATA ADD CONSTRAINT PUBLIC.CONSTRAINT_9 FOREIGN KEY(DOC_TYPE) REFERENCES PUBLIC.DOCUMENT_TYPES(DOC_TYPE) NOCHECK;
ALTER TABLE PUBLIC.RESOURCE_MESSAGE_BUNDLES ADD CONSTRAINT PUBLIC.CONSTRAINT_EB FOREIGN KEY(DOC_TYPE) REFERENCES PUBLIC.DOCUMENT_TYPES(DOC_TYPE) NOCHECK;
ALTER TABLE PUBLIC.TRANSFORM_CONFIGURATIONS ADD CONSTRAINT PUBLIC.CONSTRAINT_EA FOREIGN KEY(DOC_TYPE) REFERENCES PUBLIC.DOCUMENT_TYPES(DOC_TYPE) NOCHECK;
ALTER TABLE PUBLIC.ACTION_SETTINGS ADD CONSTRAINT PUBLIC.CONSTRAINT_0 FOREIGN KEY(DOC_TYPE) REFERENCES PUBLIC.DOCUMENT_TYPES(DOC_TYPE) NOCHECK;
ALTER TABLE PUBLIC.DOCUMENT_PART ADD CONSTRAINT PUBLIC.CONSTRAINT_1A FOREIGN KEY(DOC_TYPE) REFERENCES PUBLIC.DOCUMENT_TYPES(DOC_TYPE) NOCHECK;
ALTER TABLE PUBLIC.SUB_ACTION_SETTINGS ADD CONSTRAINT PUBLIC.CONSTRAINT_C08 FOREIGN KEY(DOC_TYPE, PARENT_ACTION_NAME) REFERENCES PUBLIC.ACTION_SETTINGS(DOC_TYPE, ACTION_NAME) NOCHECK;        