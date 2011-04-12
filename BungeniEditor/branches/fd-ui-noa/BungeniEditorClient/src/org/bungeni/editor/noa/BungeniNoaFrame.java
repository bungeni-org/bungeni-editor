package org.bungeni.editor.noa;

import ag.ion.bion.officelayer.application.OfficeApplicationException;
import ag.ion.bion.officelayer.document.DocumentException;
import ag.ion.noa.NOAException;
import java.awt.Dimension;
import java.awt.GridLayout;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import ag.ion.bion.officelayer.desktop.IFrame;
import ag.ion.bion.officelayer.document.DocumentDescriptor;
import ag.ion.bion.officelayer.document.IDocument;
import ag.ion.bion.officelayer.text.ITextDocument;
import java.awt.GridBagLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.bungeni.editor.noa.ext.BungeniLocalOfficeApplication;
import org.bungeni.extutils.BungeniFrame;
import org.bungeni.extutils.CommonTreeFunctions;

/**
 * This is an extended BungeniFrame which acts as a frame for :
 *  -- container NOA panel
 *  -- editorTabbedPanel
 * 
 * (formerly BungeniFrameEmbedded)
 * @author ashok, fdraicchio
 */
public class BungeniNoaFrame extends BungeniFrame {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniNoaFrame.class.getName());
    private BungeniLocalOfficeApplication officeApplication = null;

    /**
     * The structure of this Frame is as follows :
     *
     * BungeniNoaFrame
     *       |
     *       |---{content-pane}--|
     *                           basePanel (JPanel)
     *                           |
     *                           |---- BungeniNoaPanel (contains Openoffice Window)
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
    private BungeniNoaPanel noaPanel = null;
    private IFrame openofficeFrame = null;
    private ITextDocument document = null;

    public ITextDocument getDocument() {
        return document;
    }

    public BungeniNoaFrame() {
        super();
        init();
    }

    public BungeniNoaFrame(String titleStr) {
        super(titleStr);
        setIconForFrame();
        init();
    }

    public BungeniNoaFrame(String titleStr, Dimension frmSize) {
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
        //perhaps handle this ?
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                try {
                    if (document != null) {
                        document.close();
                    }
                    document = null;
                    if (officeApplication != null) {
                        officeApplication.deactivate();
                        officeApplication.dispose();
                        BungeniNoaApp.getInstance().setOfficeApp(null);
                    }
                } catch (Throwable ex) {
                    log.error("Error while closing window", ex);
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
        this.noaPanel = BungeniNoaPanel.getInstance();
        //add the noa panel with a grow on y axis directive
        getBasePanel().add(noaPanel.getPanel(), "growy");
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
    public void loadDocumentInPanel(String pathToDocumentOrTemplate, boolean isTemplate) throws OfficeApplicationException, NOAException, DocumentException {
        //if the Office XFrame does not exist, construct it
        if (null == this.openofficeFrame) {
            constructOOoFrame();
        }
        //now load the document - if its a template, create a new instance from 
        DocumentDescriptor ddObject = null;
        ITextDocument loadedDocument = null;
        if (isTemplate) {
            ddObject = BungeniNoaDocumentDescriptor.forTemplate(pathToDocumentOrTemplate);
            loadedDocument = (ITextDocument) officeApplication.getDocumentService().
                    constructNewDocument(openofficeFrame, IDocument.WRITER, ddObject);
        } else {
            ddObject = BungeniNoaDocumentDescriptor.forDocument(pathToDocumentOrTemplate);
            loadedDocument = (ITextDocument) officeApplication.getDocumentService().
                    loadDocument(openofficeFrame, pathToDocumentOrTemplate, ddObject);

        }
        this.document = loadedDocument;
        
        this.noaPanel.getPanel().setVisible(true);
    }

    
    /**
     * Creates a OpenOffice XFrame , the document is loaded in a XFrame
     * @return
     * @throws Throwable
     */
    private void constructOOoFrame() {
        try {
            //get a handle to the native view & attach it to the NOA Panel
            BungeniNoaNativeView nativeView = BungeniNoaNativeView.getInstance();
            nativeView.attachContainerToNativeView(BungeniNoaPanel.getInstance().getPanel());
            //now we create a OOo Frame and attach that to the native view
            IFrame oooFrame = BungeniNoaOfficeFrame.getInstance().getFrame();
            //finally validate the NOA panel
            BungeniNoaPanel.getInstance().getPanel().validate();
            //this is an empty OpenOffice XFrame -- it will be used to load a document
            this.openofficeFrame = oooFrame;
        } catch (Exception ex) {
            log.error("There was an error constructing the Openoffice frame", ex);
        }
    }

    /**
     * Returns the IFrame object of the created OpenOffice frame
     * @return
     */
    public IFrame getOpenOfficeFrame() {
        return this.openofficeFrame;
    }
}

