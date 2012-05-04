package org.bungeni.editor.noa;

import ag.ion.bion.officelayer.application.OfficeApplicationException;
import ag.ion.bion.officelayer.document.DocumentException;
import ag.ion.noa.NOAException;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import ag.ion.bion.officelayer.document.DocumentDescriptor;
import ag.ion.bion.officelayer.text.ITextDocument;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import com.sun.star.container.XIndexAccess;
import com.sun.star.container.XNamed;
import com.sun.star.frame.XController;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.text.XTextSection;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.view.XSelectionChangeListener;
import com.sun.star.view.XSelectionSupplier;
import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Timer;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import net.miginfocom.swing.MigLayout;
import org.bungeni.connector.server.DataSourceServer;
import org.bungeni.ds.DataSourceFactory;
import org.bungeni.editor.dialogs.editorTabbedPanel;
import org.bungeni.editor.noa.ext.BungeniLocalOfficeApplication;
import org.bungeni.extutils.*;
import org.bungeni.ooo.BungenioOoHelper;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.api.tabbed.VetoableTabCloseListener;

/**
 * This is an extended BungeniFrame which acts as a frame for :
 *  -- container NOA panel
 *  -- editorTabbedPanel
 * 16-05-2011 - Converted to a Singleton
 *
 * This class is important because it handles all events for closing windows
 *   - when a document is closed
 *   - when the editor itself is closed
 *
 * (formerly BungeniFrameEmbedded)
 * @author ashok, fdraicchio
 */
public class BungeniNoaFrame extends BungeniFrame {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniNoaFrame.class.getName());
    private static final ResourceBundle bundle = ResourceBundle.getBundle("org/bungeni/editor/noa/Bundle");
    private static BungeniNoaFrame thisBungeniNoaFrame = null;
    private BungeniLocalOfficeApplication officeApplication = null;
    private String LAST_DOCUMENT_OPEN_MESSAGE = bundle.getString("bungeninoaframe.last_document_open");
    private DataSourceServer dss = null;
    private Timer paneDocTimer = null;
    /**
     * We use the glazed list library here for declaring the officeDocuments as a EventList.
     * The EventList provides Events aware combo box model which received notifications on event list
     * changes and updates lists dynamically
     */
    private EventList<DocumentComposition> officeDocuments = new BasicEventList<DocumentComposition>();
    // this stores the unsaved document compositions for closing before the application is closed
    private ArrayList<DocumentComposition> unsavedDocumentCompositions = new ArrayList<DocumentComposition>();
    /**
     * The structure of this Frame is as follows :
     *
     * BungeniNoaFrame
     *       |
     *       |---{content-pane}--|
     *                           basePanel (JPanel)
     *                           |
     *                           |---- BungeniNoaTabbedPane
     *                           |       +
     *                           |       +-----tab BungeniNoaPanel (contains Openoffice Window)
     *                           |       +
     *                           |       +-----tab BungeniNoaPanel (contains Openoffice Window)
     *                           |
     *                           |---- editorTabbedPanel (contains the action panel)
     */
    /**
     * This is the panel that contains the NOA Panel and the tabbed panel
     */
    private JPanel basePanel = null;
    /**
     * This is the JPanel with the embdedded OpenOffice 
     */
    private BungeniNoaTabbedPane noaTabbedPane = null;

    public static BungeniNoaFrame getInstance() {
        if (null == thisBungeniNoaFrame) {
            thisBungeniNoaFrame = new BungeniNoaFrame();
        }
        return thisBungeniNoaFrame;
    }

    private BungeniNoaFrame() {
        super();
        init();
    }

    private BungeniNoaFrame(String titleStr) {
        super(titleStr);
        setIconForFrame();
        init();
    }

    private BungeniNoaFrame(String titleStr, Dimension frmSize) {
        super(titleStr);
        setIconForFrame();
        this.setSize(frmSize);
        init();
    }

    private void setIconForFrame() {
        ImageIcon iconApp = CommonTreeFunctions.loadIcon("bungeni-icon.png");
        setIconImage(iconApp.getImage());

    }

    public JPanel getBasePanel() {
        if (this.basePanel == null) {
            this.basePanel = new JPanel();
        }
        CommonUIFunctions.compOrientation(basePanel);
        return this.basePanel;
    }

    private void init() {
        try {
            this.officeApplication = BungeniNoaApp.getInstance().getOfficeApp();
        } catch (Throwable ex) {
            log.error("Error getting officeApplication object", ex);
        }
        //init layouts
        setupRootPanelAndAddNoaPanel();
        this.getContentPane().add(getBasePanel());
        //this needs to be calculated ?
        setResizable(true);
        // setSize(800, 600);
        pack();
        //We set it to do_nothing_on_close since we want to add
        //an exit handler and exit cleanly
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);

        //!+BUNGENI_CONNECTOR(AH,2011-09-20) Starting BungeniConnector server
        startDataSourceServer();
        // This is the listener for the main window containing the tabs
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent windowEvent) {

                // check if documents in tabs need to be saved
                // check if open documents need to be saved -
                // find which documents need to be saved

                // the buttons with the options to be selected
                Object[] buttonTexts = {
                    bundle.getString("bungeninoaframe.save_and_close"),
                    bundle.getString("bungeninoaframe.cancel_close")
                };
                JFrame aFrame = (JFrame) windowEvent.getSource();
                CommonUIFunctions.compOrientation(aFrame);
       
                ArrayList<DocumentComposition> unsavedDCompositions = getUnsavedDocuments();
                if (unsavedDCompositions.size() > 0) {
                    // prompt a dialog which has document name and corresponding check box
                    // if 2 tabs are open :
                    //  +---------------------------------------------------------+
                    //  | document name [x]                                       |
                    //  | document name [x]                                       |
                    //  | [Save the selected documents and close] [cancel close]  |
                    //  +---------------------------------------------------------+

                    // create the panel with the checkboxes
                    JPanel dialogPane = createSaveDialogPane(unsavedDCompositions);

                    // create the JOptionPane with the dialogPane attached

                    int confirm = MessageBox.OptionsConfirm(aFrame, dialogPane,
                            bundle.getString("bungeninoaframe.save_multiple_docs"), buttonTexts);

                    // if 'save the selected documents' is clicked
                    // selected documents are saved and editor is shutdown
                    // if 'cancel close is clicked - nothing happens
                    if (confirm == JOptionPane.YES_OPTION) {
                        // save the documents selected in the various checkBoxes
                        for (DocumentComposition dComposition : unsavedDocumentCompositions) {
                            if (dComposition.getDocument() != null) {
                                OOComponentHelper ooSave = new OOComponentHelper(
                                        dComposition.getDocument().getXComponent(),
                                        BungenioOoHelper.getInstance().getComponentContext());
                                ooSave.saveDocument();
                            }
                        }
                        // now unset the modified bit on the other documents which we dont want to save
                        for (DocumentComposition dDoc : officeDocuments) {
                            if (!unsavedDocumentCompositions.contains(dDoc)) {
                                OOComponentHelper oodoc = new OOComponentHelper(
                                        dDoc.getDocument().getXComponent(),
                                        BungenioOoHelper.getInstance().getComponentContext());
                                oodoc.setModified(false);
                            }
                        }

                        shutdownEditor();
                    } else {
                        // clear the list of unsaved documents
                        unsavedDocumentCompositions.clear();
                    }
                } else {
                    // all documents are saved, close the editor
                    if (JOptionPane.YES_OPTION == MessageBox.Confirm(aFrame,
                            bundle.getString("bungeninoaframe.really_exist_close_all"),
                            bundle.getString("bungeninoaframe.close_document"))) {
                        shutdownEditor();
                    }
                }
            }
        });

        // !+TAB_CLOSE(ah, feb-2012) add tab closer buttons and action listener
        addCloseButtonsToDocumentTabs();

    }

    /**
     * This method creates a JPanel that is added to the JOptionPane
     * which is displayed in the JOptionPane that asks the user to 
     * save modified documents
     */
    private JPanel createSaveDialogPane(ArrayList<DocumentComposition> unsavedDCompositions) {
        JPanel dialogPane = new JPanel();

        dialogPane.setLayout(new MigLayout("wrap 2"));

        // create the Jlabel for document and checkboxes for
        // the unsaved documents and attach to JPanel
        for (int i = 0; i < unsavedDCompositions.size(); i++) {
            // get the current name of the document 
            String currDocName = unsavedDCompositions.get(i).toString();

            // get the unsavedDocName and Checkbox
            JLabel unsavedDocName = new JLabel(currDocName);
            JCheckBox docCBox = new JCheckBox(new CheckBoxAction(unsavedDCompositions.get(i)));
            docCBox.setSelected(true); // set the JCheckBox selcted by default

            // add the panel to the container panel
            dialogPane.add(docCBox);
            dialogPane.add(unsavedDocName);
        }
        return dialogPane;
    }

    /**
     * This class notes which check box has been selected
     * and notes the corresponding document that is to be saved
     */
    private class CheckBoxAction extends AbstractAction {

        private DocumentComposition currDocumentComposition = null; // stores the document composition to the
        // affilited JCheckBox

        public CheckBoxAction(DocumentComposition dComposition) {
            currDocumentComposition = dComposition;
        }

        public void actionPerformed(ActionEvent e) {
            // get the selected document
            JCheckBox selectedDoc = (JCheckBox) e.getSource();

            if (selectedDoc.isSelected() && null != currDocumentComposition) {
                // add the documentComposition to the
                // list of documents to be saved
                unsavedDocumentCompositions.add(currDocumentComposition);
            } else {
                unsavedDocumentCompositions.remove(currDocumentComposition);
            }
        }
    }

    /**
     * This method checks for unsaved documents and returns an ArrayList of
     * unsaved DocumentCompositions
     * @return
     */
    private ArrayList<DocumentComposition> getUnsavedDocuments() {
        ArrayList<DocumentComposition> unsavedDCompositions = new ArrayList<DocumentComposition>(0);

        for (DocumentComposition document : officeDocuments) {
            if (document.getDocument() != null) {
                // check if the document is saved and if not
                // add it to the EventList
                if (document.getDocument().isModified()) {
                    unsavedDCompositions.add(document);
                    unsavedDocumentCompositions.add(document); // add all the modified docs by default
                }
            }
        }
        return unsavedDCompositions;
    }

    public void startDataSourceServer() {
        try {
            dss = DataSourceServer.getInstance();
            Properties dsProps = DataSourceFactory.getDataSourceProperties();
            dss.loadProperties(dsProps);
            dss.startServer();
        } catch (IOException ex) {
            log.error("Error while starting up datasource server", ex);
        }
    }

    /**
     * Sets up the root panel and adds the NOA panel to it
     * We use the MigLayout here (see http://www.miglayout.org ), since it supports
     * dynamic resizing - and specifying fixed panels 
     *
     */
    private void setupRootPanelAndAddNoaPanel() {
        //flowx = flow the layout on the x axis
        //column config = first panel - grow and fill on both axes
        //                second panel - use local preference
        //now row config
        // MigLayout ml = new MigLayout("flowx", "[0:0,grow,fill][pref!]", "");
        // (rm. feb 2012) -let the noaPanel fill all available space (minus the preferred
        // size for the JtabbedPane) and the Jtabbed Pane grow within its desired dimensions
        MigLayout ml = new MigLayout("flowx", "[max][grow]", "[grow]");
        getBasePanel().setLayout(ml);
        //add the NOA panel to the content pane of the frame
        //the NOA panel is a singleton -- this is the panel that contains
        //the openoffice window
        this.noaTabbedPane = BungeniNoaTabbedPane.getInstance();
        //add the noa panel with a grow on y axis directive
        // (rm, feb 2012) - let the noaPanel grow to fill all available space
        getBasePanel().add(noaTabbedPane.getTabbedPane(), "grow");
        //AH-13-05-11 comment the below for now
        //noaTabbedPane.getTabbedPane().addTab("openOffice",
        //        BungeniNoaPanel.getInstance().getPanel());
    }

    private void shutdownEditor() {
        System.out.println("Cleaning up panels");
        if (!editorTabbedPanel.isInstanceNull()) {
            editorTabbedPanel.getInstance().cleanup();
        }

        /***
         * Clean up the openoffice handles
         */
        System.out.println("Cleaning up openoffice handles");
        try {
            System.out.println("Closing component handles");
            //iterate through the document composition list and close every document
            for (DocumentComposition document : officeDocuments) {
                if (document.getDocument() != null) {
                    document.getDocument().close();
                }
                document.setDocument(null);
                officeDocuments.remove(document);
            }
            //then shutdown OOo completely
            System.out.println("Closing OpenOffice completely");
            if (officeApplication != null) {
                officeApplication.deactivate();
                officeApplication.dispose();
                BungeniNoaApp.getInstance().setOfficeApp(null);
            }
        } catch (Throwable ex) {
            log.error("Error while closing window", ex);
        }
        System.out.println("Stopping Bungenic Connector Data Server");
        //!+BUNGENI_CONNECTOR(AH, 2011-09-20) stopping bungeni connector
        if (dss != null) {
            log.info("Stopping connector server");
            dss.stopServer();
        } else {
            log.info("Connector server was null");
        }
        /**
         * Dispose the main JFrame
         */
        System.out.println("Dispose and exit window");
        dispose();

        /**
         *Finally exit the system
         */
        System.exit(0);
    }

    /***
     * This loads a document or a document template in the BungeniNoaFrame
     * it constructs an OOOframe if it does not exist.
     * @param pathToDocumentOrTemplate
     * @param isTemplate
     * @throws OfficeApplicationException
     * @throws NOAException
     * @throws DocumentException
     */
    public DocumentComposition loadDocumentInPanel(String pathToDocumentOrTemplate, boolean isTemplate) throws OfficeApplicationException, NOAException, DocumentException {

        /**
         * AH-18-05-2011
         * For some reason we need to attach the NoaPanel i.e. the holder of the IFrame
         * to the tab and make it visible before creating the IFrame, otherwise, IFrame
         * attachment fails
         * This means we have to update the tab title after attaching the document
         */
        BungeniNoaPanel noapanel = new BungeniNoaPanel();
        String fileNameForTab = CommonFileFunctions.getFileNameFromPath(pathToDocumentOrTemplate, false);
        noaTabbedPane.getTabbedPane().addTab(fileNameForTab, noapanel.getPanel());
        noapanel.getPanel().setVisible(true);
        
        // !+ (rm, feb 2012) - determine if the number or tabs is greater than 1 and
        // then allow for a close button to be added to the tab

        // add close button to all the tabs
        // !+TAB_CLOSE(ah, feb-2012) do this when the tab is initialized
        // calling addCloseButtonsToTabs() here is *wrong* because it will get
        // called everytime a document is loaded - we do it once on the tabbed pane
        // thats all that is required
        //addCloseButtonsToDocumentTabs();

        //if the Office XFrame does not exist, construct it the openoffice document will
        //be loaded into the frame
        DocumentComposition dc = constructOOoFrame(noapanel);
        //now load the document - if its a template, create a new instance from 
        DocumentDescriptor ddObject = null;
        ITextDocument loadedDocument = null;
        if (isTemplate) {
            ddObject = BungeniNoaDocumentDescriptor.forTemplate(pathToDocumentOrTemplate);
        } else {
            ddObject = BungeniNoaDocumentDescriptor.forDocument(pathToDocumentOrTemplate);
        }
        loadedDocument = (ITextDocument) officeApplication.getDocumentService().
                loadDocument(dc.getFrame().getFrame(), pathToDocumentOrTemplate, ddObject);

        String tabTitle = OOComponentHelper.getFrameTitle(loadedDocument.getXTextDocument());
        //set the loaded document into the document composition object
        dc.setDocument(loadedDocument);
        //add it to the oficedocument list
        addOfficeDocument(dc);

        // (rm, feb 2012) - run a timer to determine if the document has been
        // modified in which case the title of the doc at the bar is set to bold
        // (and normal if doc is not modified)
        //paneDocTimer = new Timer (300, new CheckDocumentModified());
        BungeniNoaTabbedPane.getInstance().getTabbedPane().validate();
        return dc;
    }

    /**
     * This class is responsible for changing the font type of
     * an unsaved document to ensure that unsaved documents are easily
     * visble
     */
    /**
    class CheckDocumentModified implements  ActionListener{
    public void actionPerformed(ActionEvent e) {
    throw new UnsupportedOperationException("Not supported yet.");
    }

    }
     **/
    /**
     * (rm, feb 2012) - This method determines on whether or not
     * the tabs (for noaTabbedPane) have a close button or not
     */
    /**
     * !+TAB_CLOSE(ah, feb-2012) - Tab closing user story is as follows
     * When the user tries to close a tab , we have to
     *      1 - check if the document needs saving
     *      2 - if the document needs saving we prompt the user to 'save and close' or 'cancel close'
     *      3 - if the user clicks save and close , we save the document, close the document, dispose it and close the tab
     *      4 - if the user clicks cancel close, we veto the close
     **/
    public void addCloseButtonsToDocumentTabs() {

        //!+TAB_CLOSE(ah, feb-2012) -
        // all tabs have the close button, the last tab when its closed, should
        // close the editor

        noaTabbedPane.getTabbedPane().putClientProperty(
                SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY,
                Boolean.TRUE);

        // !+TAB_CLOSE(ah, feb-2012) switch to vetoable tab close listener - we
        // want to know when the tab is closing , and prompt the user if they dont
        // want to close.
        // !+WARNING , !+FIX_THIS - this is registered for ALL TABS which have the SUBSTANCE close
        // buttons property - presently only the document tabs have it so it isnt a
        // problem
        SubstanceLookAndFeel.registerTabCloseChangeListener(new VetoableTabCloseListener() {

            /**
             * Returns the DocumentComposition object matching the tab being closed.
             */
            private DocumentComposition __getDocumentCompositionForTab(Component tabComponent) {
                DocumentComposition docClosing = null;
                for (DocumentComposition dc : officeDocuments) {
                    if (dc.equalsByNoaPanel((JPanel) tabComponent)) {
                        docClosing = dc;
                        break;
                    }
                }
                return docClosing;
            }

            public void tabClosing(JTabbedPane tabbedPane,
                    Component tabComponent) {
                DocumentComposition docClosing = __getDocumentCompositionForTab(tabComponent);
                // close the document tab
                closeDocumentTab(docClosing);
            }

            public void tabClosed(JTabbedPane tabbedPane, Component tabComponent) {
                // if the tab count is 0 - we shutdown the editor
                if (tabbedPane.getTabCount() == 0) {
                    shutdownEditor();
                }
            }

            /***
             * This event allows us to veto tab closes
             */
            public boolean vetoTabClosing(JTabbedPane tabbedPane, Component tabComponent) {
                DocumentComposition docClosing = __getDocumentCompositionForTab(tabComponent);
                if (docClosing != null) {
                    //check if document needs to be saved
                    if (docClosing.document.isModified()) {
                        //yes it does
                        //prompt with save&close,cancel-close
                        Object[] buttonTexts = {
                            bundle.getString("bungeninoaframe.save_and_close"),
                            bundle.getString("bungeninoaframe.cancel_close")
                        };
                        StringBuilder message = new StringBuilder(bundle.getString("bungeninoaframe.document_not_saved"));
                        if (tabbedPane.getTabCount() == 1) {
                            //this is the last tab !!
                            message.append("\n");
                            message.append(LAST_DOCUMENT_OPEN_MESSAGE);
                        }
                        int nOption = MessageBox.Confirm(
                                tabComponent,
                                message.toString(),
                                bundle.getString("bungeninoaframe.closing"), buttonTexts,
                                JOptionPane.YES_NO_OPTION);
                        if (nOption == JOptionPane.YES_OPTION) {
                            //save the document
                            OOComponentHelper ooSave = new OOComponentHelper(
                                    docClosing.document.getXComponent(),
                                    BungenioOoHelper.getInstance().getComponentContext());
                            ooSave.saveDocument();
                            //now close the tab in tabClosing()
                            return false;
                        } else {
                            //veto close
                            return true;
                        }
                    } else {
                        //no doc is already saved
                        //prompt with confirmation, cancel-close
                        StringBuilder message = new StringBuilder(bundle.getString("bungeninoaframe.close_document"));
                        if (tabbedPane.getTabCount() == 1) {
                            message.append("\n");
                            message.append(LAST_DOCUMENT_OPEN_MESSAGE);
                        }

                        int nOption = MessageBox.Confirm(tabComponent, message.toString(), bundle.getString("bungeninoaframe.closing"));
                        if (nOption == JOptionPane.YES_OPTION) {
                            return false;
                        } else {
                            //veto close
                            return true;
                        }
                    }
                }
                // veto close by default
                return true;
            }
        });
    }

    /***
     * This function encapsulates the logic of how a document in a tab is closed.
     * It doesnt remove or close the Tab pane containing the loaded document - thats
     * the responsibility of the caller
     * @param docClosing
     */
    private void closeDocumentTab(DocumentComposition docClosing) {
        // !+TAB_CLOSE(ah, feb-2012) - we need to check for nulls because
        // substance may be playing mr.smartypants by closing tabs by itself,
        // when we dont veto
        if (docClosing != null) {
            log.info("Closing document tab for " + docClosing.toString());
            if (docClosing.document != null) {
                docClosing.document.close();
            }
            docClosing.setDocument(null);
            docClosing.getNativeView().closeNativeView();
            // !+TAB_CLOSE(ah, feb-2012) - substance seems to remove the tab by itself ??
            //tabbedPane.remove(tabComponent);
            officeDocuments.remove(docClosing);
        }
    }

    /**
     * Creates a OpenOffice XFrame , the document is loaded in a XFrame
     * A native view is attached to an XFrame
     * @return
     * @throws Throwable
     */
    private DocumentComposition constructOOoFrame(BungeniNoaPanel noaPanel) {
        DocumentComposition dc = null;
        try {
            //get a handle to the native view & attach it to the NOA Panel
            BungeniNoaNativeView nativeView = new BungeniNoaNativeView();
            //create noa panel
            //AH-18-05-2011 - Warning, creating the Noa panel here doesnt seem to work
            //it needs to be createad, attached and initialized before attaching it to the
            //noaofficeframe
            //XXXXX BungeniNoaPanel noaPanel = new BungeniNoaPanel();
            //attach the native view to that paenl
            nativeView.attachContainerToNativeView(noaPanel.getPanel());
            //now we create a OOo Frame and attach that to the native view
            BungeniNoaOfficeFrame oooFrame = new BungeniNoaOfficeFrame(nativeView);
            //finally validate the NOA panel
            noaPanel.getPanel().validate();
            //this is an empty OpenOffice XFrame -- it will be used to load a document
            //this.openofficeFrame = oooFrame;
            dc = new DocumentComposition(oooFrame, nativeView, noaPanel, null);
        } catch (Exception ex) {
            log.error("There was an error constructing the Openoffice frame", ex);
        }
        return dc;
    }

    /**
     * A document in Bungeni Editor is displayed using the 3 components :
     *  -- openoffice frame
     *  -- noa panel
     *  -- noa native view
     * This class is a holder for all these related objects for  text document
     */
    public class DocumentComposition {

        private BungeniNoaOfficeFrame frame;
        private BungeniNoaNativeView nativeView;
        private BungeniNoaPanel panel;
        private ITextDocument document;
        protected OOComponentHelper ooComponentHelper;

        /**
         *Create a DocumentComposition object
         * @param frame
         * @param nativeView
         * @param panel
         * @param doc
         */
        public DocumentComposition(BungeniNoaOfficeFrame frame, BungeniNoaNativeView nativeView,
                BungeniNoaPanel panel, ITextDocument doc) {
            this.frame = frame;
            this.nativeView = nativeView;
            this.panel = panel;
            this.document = doc;
            if (null == doc) {
                this.ooComponentHelper = null;
            } else {
                this.ooComponentHelper = new OOComponentHelper(
                        doc.getXComponent(),
                        BungenioOoHelper.getInstance().getComponentContext());
                this.addSelectionListener();
            }
        }
        String documentTitle = "";

        @Override
        public String toString() {
            if (document == null) {
                return "Unknown Document";
            }
            documentTitle = OOComponentHelper.getFrameTitle(document.getXTextDocument());
            if (documentTitle != null)
                return documentTitle;
            else
                return "";
        }

        /**
         * @return the frame
         */
        public BungeniNoaOfficeFrame getFrame() {
            return frame;
        }

        /**
         * @param frame the frame to set
         */
        public final void setFrame(BungeniNoaOfficeFrame frame) {
            this.frame = frame;
        }

        /**
         * @return the nativeView
         */
        public BungeniNoaNativeView getNativeView() {
            return nativeView;
        }

        /**
         * @param nativeView the nativeView to set
         */
        public final void setNativeView(BungeniNoaNativeView nativeView) {
            this.nativeView = nativeView;
        }

        /**
         * @return the panel
         */
        public BungeniNoaPanel getPanel() {
            return panel;
        }

        /**
         * @param panel the panel to set
         */
        public final void setPanel(BungeniNoaPanel panel) {
            this.panel = panel;
        }

        /**
         * @return the document
         */
        public ITextDocument getDocument() {
            return document;
        }

        /**
         * @param document the document to set
         */
        public final void setDocument(ITextDocument document) {

            if (document != null) {
                this.ooComponentHelper = new OOComponentHelper(document.getXComponent(),
                        BungenioOoHelper.getInstance().getComponentContext());
                this.addSelectionListener();
            }
            this.document = document;
        }

        private void addSelectionListener() {
            XSelectionSupplier selSupplier = ooQueryInterface.XSelectionSupplier(
                    ooComponentHelper.getTextDocument().getCurrentController());
            selSupplier.addSelectionChangeListener(new SelectionChangeListener(ooComponentHelper));
        }

        public boolean equalsByNoaPanel(JPanel comparePanel) {
            if (this.panel.getPanel().equals(comparePanel)) {
                return true;
            } else {
                return false;
            }
        }

        public boolean equals(DocumentComposition dc) {
            if (dc.getDocument().equals(getDocument()) && dc.equalsByNoaPanel(dc.getPanel().getPanel())) {
                return true;
            }
            return false;
        }
    }

    public final EventList<DocumentComposition> getOfficeDocuments() {
        return this.officeDocuments;
    }

    public void addOfficeDocument(DocumentComposition dc) {
        this.officeDocuments.add(dc);
    }

    /**
     * This is the XSelectionChange event listener for the document
     * The idea is to use the selectionchangelistener to trigger toolbar events, which
     * are currently invoked via a Swing timer.
     */
    private class SelectionChangeListener implements XSelectionChangeListener {

        private OOComponentHelper cachedOOHelper;
        private String currentSection = "";
        private String previousSection = "";

        public SelectionChangeListener(OOComponentHelper ooHelper) {
            this.cachedOOHelper = ooHelper;
        }

        public void selectionChanged(EventObject aEvent) {
            XController aCtrl = (XController) UnoRuntime.queryInterface(
                    XController.class,
                    aEvent.Source);
            if (aCtrl != null) {
                XSelectionSupplier xSelectionSupplier = ooQueryInterface.XSelectionSupplier(aEvent.Source);
                //!+WARNING - calling things like ooComponentHelper.getCurrentSelection()
                // over here will CRASH the system !!
                if (xSelectionSupplier != null) {
                    // get the selectoin from the selection supplier
                    Object selection = xSelectionSupplier.getSelection();
                    if (selection != null) {
                        // query the selection for ranges
                        XServiceInfo xSelInfo = ooQueryInterface.XServiceInfo(selection);
                        if (xSelInfo.supportsService("com.sun.star.text.TextRanges")) {
                            try {
                                //if there is a range we access the single selection from the range
                                XIndexAccess xIndexAccess = ooQueryInterface.XIndexAccess(selection);
                                Object singleSelection = xIndexAccess.getByIndex(0);
                                // check if there is a section in the selection range
                                XTextSection aSection = cachedOOHelper.getSectionInSelection(singleSelection);
                                if (aSection != null) {
                                    // if there is a section, then get its name
                                    XNamed aSectionName = ooQueryInterface.XNamed(aSection);
                                    this.currentSection = aSectionName.getName();
                                    if (this.currentSection.equals(this.previousSection)) {
                                        System.out.println("Previous Section = Current Section "
                                                + this.currentSection);
                                        // do somehting here
                                    } else {
                                        System.out.println("Previous Section ("
                                                + this.previousSection
                                                + ")  <> Current Section " + this.currentSection);
                                        this.previousSection = this.currentSection;
                                        // do something else here
                                    }
                                }
                            } catch (IndexOutOfBoundsException ex) {
                                log.error("Unable to get selection range", ex);
                            } catch (WrappedTargetException ex) {
                                log.error("Unable to get selection range", ex);
                            }
                        }
                    }

                }
            }
        }

        /**
         * What to do when the selection listener is disposed ? Cleanup !
         * @param aSourceObj
         */
        public void disposing(EventObject aSourceObj) {
            System.out.println("Disposing Listener!!!");
            // stop listening for selection changes
            XSelectionSupplier aCtrl = (XSelectionSupplier) UnoRuntime.queryInterface(
                    XSelectionSupplier.class,
                    aSourceObj);
            if (aCtrl != null) {
                aCtrl.removeSelectionChangeListener(this);
            }

            // remove as dispose listener
            //XComponent aComp = (XComponent) UnoRuntime.queryInterface( XComponent.class, aSourceObj );
            //if( aComp != null )
            //    aComp.removeEventListener( this );

        }
    }
}
