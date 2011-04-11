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
import javax.swing.ImageIcon;
import org.bungeni.editor.noa.ext.BungeniLocalOfficeApplication;
import org.bungeni.extutils.BungeniFrame;
import org.bungeni.extutils.CommonTreeFunctions;

/**
 * This is an extended BungeniFrame which acts as a frame for container NOA panel.
 * The NOA Panel is a singleton BungeniNoaPanel
 * (formerly BungeniFrameEmbedded)
 * @author ashok, fdraicchio
 */
public class BungeniNoaFrame extends BungeniFrame {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniNoaFrame.class.getName());
    private BungeniLocalOfficeApplication officeApplication = null;
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

    private void init() {
        try {
            this.officeApplication = BungeniNoaApp.getInstance().getOfficeApp();
        } catch (Throwable ex) {
            log.error("Error getting officeApplication object", ex);
        }
        //init layouts
        getContentPane().setLayout(new GridLayout());
        //add the NOA panel to the content pane of the frame
        //the NOA panel is a singleton
        this.noaPanel = BungeniNoaPanel.getInstance();
        getContentPane().add(noaPanel.getPanel());
        //this needs to be calculated ?
        setSize(270, 655);
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
    private void fillNOAPanel() {
        try {
            //prepare the frame for loading a document
            constructOOoFrame();
            //TODO-XXX-ASHOK -- this should either be loadDocument() or constructNewDocument()
            document = (ITextDocument) officeApplication.getDocumentService().
                    constructNewDocument(anIframe,
                    IDocument.WRITER,
                    DocumentDescriptor.DEFAULT);

            noaPanel.getPanel().setVisible(true);
        } catch (Throwable ex) {
            Logger.getLogger(BungeniNoaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    **/

    
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

            this.openofficeFrame = oooFrame;
        } catch (Exception ex) {
            log.error("There was an error constructing the Openoffice frame", ex);
        }
    }

    public IFrame getOpenOfficeFrame() {
        return this.openofficeFrame;
    }
}
/***
private IOfficeApplication startOOO() throws Throwable {
IApplicationAssistant applicationAssistant = new ApplicationAssistant(System.getProperty("user.dir") + "\\lib\\noa");
ILazyApplicationInfo[] appInfos = applicationAssistant.getLocalApplications();
if (appInfos.length < 1) {
throw new Throwable("No OpenOffice.org Application found.");
}
HashMap configuration = new HashMap();
System.out.println(appInfos[0].getHome());
configuration.put(IOfficeApplication.APPLICATION_HOME_KEY, appInfos[0].getHome());
configuration.put(IOfficeApplication.APPLICATION_TYPE_KEY, IOfficeApplication.LOCAL_APPLICATION);
IOfficeApplication officeAplication = OfficeApplicationRuntime.getApplication(configuration);

officeAplication.setConfiguration(configuration);
officeAplication.activate();
return officeAplication;
}
 ***/
