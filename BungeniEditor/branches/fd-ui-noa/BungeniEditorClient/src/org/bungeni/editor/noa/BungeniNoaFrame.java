package org.bungeni.editor.noa;

import ag.ion.bion.officelayer.application.OfficeApplicationException;
import ag.ion.bion.officelayer.document.DocumentException;
import ag.ion.noa.NOAException;
import java.awt.Dimension;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import ag.ion.bion.officelayer.document.DocumentDescriptor;
import ag.ion.bion.officelayer.document.IDocument;
import ag.ion.bion.officelayer.text.ITextDocument;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.bungeni.editor.dialogs.editorTabbedPanel;
import org.bungeni.editor.noa.ext.BungeniLocalOfficeApplication;
import org.bungeni.extutils.BungeniFrame;
import org.bungeni.extutils.CommonFileFunctions;
import org.bungeni.extutils.CommonTreeFunctions;
import org.bungeni.ooo.OOComponentHelper;

/**
 * This is an extended BungeniFrame which acts as a frame for :
 *  -- container NOA panel
 *  -- editorTabbedPanel
 * 16-05-2011 - Converted to a Singleton
 * (formerly BungeniFrameEmbedded)
 * @author ashok, fdraicchio
 */
public class BungeniNoaFrame extends BungeniFrame {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniNoaFrame.class.getName());
    
    private static BungeniNoaFrame thisBungeniNoaFrame = null;

    private BungeniLocalOfficeApplication officeApplication = null;

    private List<DocumentComposition> officeDocuments = new ArrayList<DocumentComposition>(0);

    /**
     * The structure of this Frame is as follows :
     *
     * BungeniNoaFrame
     *       |
     *       |---{content-pane}--|
     *                           basePanel (JPanel)
     *                           |
     *                           |---- BungeniNoaTabbedPanel
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
    private  BungeniNoaTabbedPane noaTabbedPane = null;

    public static BungeniNoaFrame getInstance(){
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
        setSize(800, 600);
        pack();
        //We set it to do_nothing_on_close since we want to add
        //an exit handler and exit cleanly
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                  JFrame aFrame = (JFrame) windowEvent.getSource();
                  int confirm = JOptionPane.showOptionDialog(aFrame, "Really Exit? This will close all Editor panels",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                        null, null);
                  if (confirm == JOptionPane.YES_OPTION) {
                      /**
                       * We probably need to check if all the OOo documents have been saved
                       */
                        // to do 

                      /**
                       * Clean up the main tabbed panel
                       */

                      if (!editorTabbedPanel.isInstanceNull()) {
                          editorTabbedPanel.getInstance().cleanup();
                      }

                      /***
                       * Clean up the openoffice handles 
                       */

                      try {
                          //iterate through the document composition list and close every document
                          for (DocumentComposition document : officeDocuments) {
                              //XXXX-TODO-XXX check if document has been saved, warn etc.
                              if (document.getDocument() != null) {
                                 document.getDocument().close();
                                }
                              document.setDocument(null);
                              officeDocuments.remove(document);
                          }
                          //then shutdown OOo completely
                          if (officeApplication != null) {
                                officeApplication.deactivate();
                                officeApplication.dispose();
                                BungeniNoaApp.getInstance().setOfficeApp(null);
                            }
                        } catch (Throwable ex) {
                            log.error("Error while closing window", ex);
                        }

                      /**
                       * Dispose the main JFrame
                       */

                      dispose();

                      /**
                       *Finally exit the system
                       */

                      System.exit(0);
                      }
            }
        });

    }

    
    /**
     * Sets up the root panel and adds the NOA panel to it
     * We use the MigLayout here (see http://www.miglayout.org ), since it supports
     * dynamic resizing - and specifying fixed panels 
     *
     */
    private void setupRootPanelAndAddNoaPanel(){
        //flowx = flow the layout on the x axis
        //column config = first panel - grow and fill on both axes
        //                second panel - use local preference
        //now row config
        MigLayout ml = new MigLayout("flowx", "[0:0,grow,fill][pref!]", "");
        getBasePanel().setLayout(ml);
        //add the NOA panel to the content pane of the frame
        //the NOA panel is a singleton -- this is the panel that contains
        //the openoffice window
        this.noaTabbedPane = BungeniNoaTabbedPane.getInstance();
        //add the noa panel with a grow on y axis directive
        getBasePanel().add(noaTabbedPane.getTabbedPane(), "growy");
        //AH-13-05-11 comment the below for now
        //noaTabbedPane.getTabbedPane().addTab("openOffice",
        //        BungeniNoaPanel.getInstance().getPanel());
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
        //if the Office XFrame does not exist, construct it
        DocumentComposition dc =  constructOOoFrame(noapanel);
        //now load the document - if its a template, create a new instance from 
        DocumentDescriptor ddObject = null;
        ITextDocument loadedDocument = null;
        if (isTemplate) {
            ddObject = BungeniNoaDocumentDescriptor.forTemplate(pathToDocumentOrTemplate);
            loadedDocument = (ITextDocument) officeApplication.getDocumentService().
                    constructNewDocument(dc.getFrame().getFrame(),IDocument.WRITER, ddObject);
        } else {
            ddObject = BungeniNoaDocumentDescriptor.forDocument(pathToDocumentOrTemplate);
            loadedDocument = (ITextDocument) officeApplication.getDocumentService().
                    loadDocument(dc.getFrame().getFrame(), pathToDocumentOrTemplate, ddObject);

        }
        String tabTitle = OOComponentHelper.getFrameTitle(loadedDocument.getXTextDocument());
        //set the loaded document into the document composition object
        dc.setDocument(loadedDocument);
        BungeniNoaTabbedPane.getInstance().getTabbedPane().validate();
        return dc;
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
            BungeniNoaNativeView nativeView = BungeniNoaNativeView.getInstance();
            //create noa panel
            //XXXXX BungeniNoaPanel noaPanel = new BungeniNoaPanel();
            //attach the native view to that paenl
            nativeView.attachContainerToNativeView(noaPanel.getPanel());
            //now we create a OOo Frame and attach that to the native view
            BungeniNoaOfficeFrame oooFrame = new BungeniNoaOfficeFrame();
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

        /**
         *Create a DocumentComposition object
         * @param frame
         * @param nativeView
         * @param panel
         * @param doc
         */
        public DocumentComposition(BungeniNoaOfficeFrame frame, BungeniNoaNativeView nativeView,
                BungeniNoaPanel panel, ITextDocument doc ) {
            this.frame = frame;
            this.nativeView = nativeView;
            this.panel = panel;
            this.document = doc;
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
        public void setFrame(BungeniNoaOfficeFrame frame) {
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
        public void setNativeView(BungeniNoaNativeView nativeView) {
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
        public void setPanel(BungeniNoaPanel panel) {
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
        public void setDocument(ITextDocument document) {
            this.document = document;
        }



    }
    
}

