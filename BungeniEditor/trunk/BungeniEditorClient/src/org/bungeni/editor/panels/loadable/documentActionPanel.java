package org.bungeni.editor.panels.loadable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.bungeni.editor.config.DocumentActionsReader;
import org.bungeni.editor.actions.EditorActionFactory;
import org.bungeni.editor.actions.IEditorActionEvent;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.panels.impl.BaseClassForITabbedPanel;
import org.bungeni.editor.panels.toolbar.BungeniToolbarActionElement;
import org.bungeni.editor.panels.toolbar.BungeniToolbarLoader;
import org.bungeni.editor.panels.toolbar.BungeniToolbarLoader.toolbarStyleType;
import org.bungeni.editor.panels.toolbar.buttonContainerPanel;
import org.bungeni.editor.panels.toolbar.buttonPanel;
import org.bungeni.editor.panels.toolbar.scrollPanel;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.editor.toolbar.target.BungeniToolbarTargetProcessor;
import org.bungeni.editor.toolbar.conditions.BungeniToolbarConditionProcessor;
import org.bungeni.editor.config.BungeniEditorPropertiesHelper;
import org.bungeni.extutils.CommonUIFunctions;
import org.bungeni.ooo.OOComponentHelper;
import org.japura.gui.CollapsiblePanel;
import org.japura.gui.CollapsibleRootPanel;
import org.jdom.Element;

/**
 * This is the floating panel implementation for the Editor's action bar
 * @author  Ashok Hariharan
 */
public class documentActionPanel extends  BaseClassForITabbedPanel {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(documentActionPanel.class.getName());
    //private BungeniClientDB instance;
    private String m_currentSelectedSectionName;
    private Timer toolbarTimer = null;
    //cache for conditions
    HashMap<String, BungeniToolbarConditionProcessor> conditionMap = new HashMap<String, BungeniToolbarConditionProcessor>();

    /** Creates new form holderUIPanel */
    public documentActionPanel() {
        initComponents();
    }


    public documentActionPanel(OOComponentHelper ooDocument, JFrame parentFrame) {
        this();
        this.parentFrame = parentFrame;
        this.ooDocument = ooDocument;
    }


    private synchronized OOComponentHelper getOODocument() {
        return this.ooDocument;
    }


    public IEditorActionEvent getEventClass(toolbarAction subAction) {
        IEditorActionEvent event = EditorActionFactory.getEventClass(subAction);
        return event;
    }

    // !+ACTION_RECONF (rm, jan 2012) - toolbarAction has been deprecated and all
    // functionality moved to toolbarSubAction
    /**
    public IEditorActionEvent getEventClass(toolbarAction action) {
        IEditorActionEvent event = EditorActionFactory.getEventClass(action);
        return event;
    }
    **/
    
    public void setParentWindowHandle(JFrame c) {
        this.parentFrame = c;
    }

    /**
     *
     */
    @Override
    public void initialize() {
        super.initialize();
        CommonUIFunctions.compOrientation(this);
        this.initDB();
        this.initToolbarTabs();
        this.initMouseListener();

        //this.initUIAttributes();
    }

    
    private BungeniToolbarLoader toolbarLoader = null;
    private Color toolbarTabBgColor = null , toolbarTabSelectedBgColor = null;

    private void initDB(){
        //instance = new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
    }

    private void initToolbarTabs() {
        toolbarLoader = new BungeniToolbarLoader(new BungeniToolbarCommandListener());
        toolbarLoader.loadToolbar(this.toolbarTabs);
        toolbarTabBgColor = toolbarTabs.getSelectedComponent().getBackground();
        toolbarTabSelectedBgColor = toolbarTabBgColor.darker();
        //toolbar update timer
        toolbarTimer = new javax.swing.Timer(3000, new BungeniToolbarTimerListener());
        toolbarTimer.setInitialDelay(3000);
        toolbarTimer.start();
        //toolbar tab change listener
        toolbarTabs.addChangeListener(new BungeniToolbarPostTabChangeListener(toolbarTabs));
        toolbarTabs.getModel().addChangeListener(new BungeniToolbarPreTabChangeListener(toolbarTabs));
        toolbarTabs.getSelectedComponent().setBackground(toolbarTabSelectedBgColor);
        for (int i = 0; i < toolbarTabs.getTabCount(); i++) {
            // if tabbed
            if (this.toolbarLoader.getToolbarStyle().equals(toolbarStyleType.tabbed)) {
              JTabbedPane grpTab =   (JTabbedPane) toolbarTabs.getComponentAt(i);
                grpTab.addChangeListener(new BungeniToolbarPostTabChangeListener(grpTab));
                grpTab.getModel().addChangeListener(new BungeniToolbarPreTabChangeListener(grpTab));
                if (grpTab.getSelectedIndex() != -1)
                    grpTab.getSelectedComponent().setBackground(toolbarTabSelectedBgColor);
            }
            // if collapsible
        }
        //set the structural tab as the current tab
        toolbarTabs.setSelectedIndex(0);
    }




    @Override
    public void refreshPanel() {

    }


    class BungeniToolbarPreTabChangeListener implements ChangeListener {

        private JTabbedPane forThisTab = null;
        
        public BungeniToolbarPreTabChangeListener(JTabbedPane thisTab) {
                forThisTab = thisTab;
        }

        public void stateChanged(ChangeEvent e) {
            Component selectedComponent = forThisTab.getSelectedComponent();
            selectedComponent.setBackground(toolbarTabBgColor);
            //now set it for the selected sub-tab - if any
        }

    }

    /***
     * This event is invoked whenever the Action tabs are switched - it is invoked after the tabs have changed.
     * The toolbar action states are refreshed for the current tab since the timer delay
     * is 3 seconds.
     */
    class BungeniToolbarPostTabChangeListener implements ChangeListener {

        private JTabbedPane forThisTab = null;

        public BungeniToolbarPostTabChangeListener(JTabbedPane thisTab) {
                forThisTab = thisTab;
        }

        public void stateChanged(ChangeEvent e) {
            processActionStatesForSelectedTab();
            Component selectedComponent = forThisTab.getSelectedComponent();
            selectedComponent.setBackground(toolbarTabSelectedBgColor);
        }
    }

        class buttonPanelState {
            buttonPanel btnPanel;
            boolean panelState;
            public buttonPanelState(buttonPanel bPanel, boolean bState) {
                this.btnPanel = bPanel;
                this.panelState = bState;
            }
        }

        class buttonActionStateRunner extends SwingWorker<List<buttonPanelState>, buttonPanelState> {

          public buttonActionStateRunner () {
          }

         @Override
          public List<buttonPanelState> doInBackground() {
                    boolean bState = false;
                   ArrayList<buttonPanelState> panelStates = new ArrayList<buttonPanelState>();
                    try {
                        Component[] components = getButtonPanelsForSelectedTab();
                        for (Component c : components) {
                            //process only the child components that are button panels
                            if (c.getClass().getName().equals(buttonPanel.class.getName())) {
                                buttonPanel buttonPanel = (buttonPanel) c;
                                BungeniToolbarActionElement actionElement = buttonPanel.getActionElement();
                                bState = processActionCondition(actionElement.getCondition());
                                buttonPanelState panelState = new buttonPanelState(buttonPanel, bState);
                                panelStates.add(panelState);
                                publish(panelState);

                            }
                        }
                    } catch (Exception ex) {
                        log.error("doInBackround : " + ex.getMessage());
                        log.error("doInBackground : ", ex);
                    } finally {
                        return panelStates;
                    }
            }

        @Override
            protected void process(List<buttonPanelState> panelStates) {
                for (buttonPanelState bps : panelStates) {
                    bps.btnPanel.enableActionButton(bps.panelState);
                }
            }
        }


    class BungeniToolbarTimerListener implements ActionListener {
        buttonActionStateRunner actionStateRunner = null;

        public BungeniToolbarTimerListener(){
            actionStateRunner = new buttonActionStateRunner();
        }


        public void actionPerformed(ActionEvent e) {
            (new buttonActionStateRunner()).execute();
         //   processActionStatesForSelectedTab();
        }
    }

    /***** API for conditionally enabling / disabling command buttons *****/
    public Component[] getButtonPanelsForSelectedTab() {
        toolbarStyleType toolbarType = this.toolbarLoader.getToolbarStyle();
        if (toolbarType.equals(toolbarStyleType.tabbed)) {
            int nSelectedTab = toolbarTabs.getSelectedIndex();
            if ((-1 != nSelectedTab) && (toolbarTabs.getTabCount() > 0)) {
                Component couter = toolbarTabs.getSelectedComponent();
                log.debug("group component class = " + couter.getClass().getName());
                JTabbedPane groupTab = (JTabbedPane) couter;
                int nInner = groupTab.getSelectedIndex();
                if ((-1 != nInner) && (groupTab.getTabCount() > 0)) {
                    Component cSelected = groupTab.getSelectedComponent();
                    log.debug("selected component class = " + cSelected.getClass().getName());
                    scrollPanel panel = (scrollPanel) cSelected;
                    buttonContainerPanel buttonsPanel = panel.getViewPortPanel();
                    Component[] components = buttonsPanel.getComponents();
                    return components;
                }
            }
        } else {
            int nSelectedTab = toolbarTabs.getSelectedIndex();
            if ((-1 != nSelectedTab) && (toolbarTabs.getTabCount() > 0)) {
                Component couter = toolbarTabs.getSelectedComponent();
                CollapsibleRootPanel crp = (CollapsibleRootPanel) couter;
                List<CollapsiblePanel> panels = crp.getCollapsiblePanels();
                for (CollapsiblePanel panel : panels) {
                   if (panel.isVisible()) {
                        if (panel.isExpanded()) {
                            Component c = panel.getComponent(0);
                            String s = c.getClass().getCanonicalName();
                            System.out.println("Class NAME XXXXX : " + s);
                            buttonContainerPanel buttonsPanel = (buttonContainerPanel) panel.getComponent(0);
                            Component[] components = buttonsPanel.getComponents();
                            return components;
                        }
                    }
                }
            }
        }
        return null;
    }

    /***
     * This function is 'synchronized' becuase it is called from within the timer thread
     * and also on the onChange event for the tab
     */
    synchronized void processActionStatesForSelectedTab() {
        Component[] components = getButtonPanelsForSelectedTab();
        for (Component c : components) {
            //process only the child components that are button panels
            if (c.getClass().getName().equals(buttonPanel.class.getName())) {
                buttonPanel buttonPanel = (buttonPanel) c;
                BungeniToolbarActionElement actionElement = buttonPanel.getActionElement();
                boolean bState = processActionCondition(actionElement.getCondition());
                buttonPanel.enableActionButton(bState);
            }
        }
    }

    boolean processActionCondition(String conditionValue) {
        boolean bAction = true;

        if (!conditionMap.containsKey(conditionValue)) {
            //already has conditionprocessor object, get cached object and reset...
            //disabled conditionMap.get(conditionValue).setOOComponentHandle(getOODocument());
            //} else {
            BungeniToolbarConditionProcessor condProc = new BungeniToolbarConditionProcessor(conditionValue);
            conditionMap.put(conditionValue, condProc);
        }
        bAction = conditionMap.get(conditionValue).evaluate(getOODocument());
        System.out.println("bAction : " + bAction + " value = " + conditionValue);
        return bAction;
    }

    /*****
     This Class implements the action listener which gets executed
     * when you click a button on the action toolbar
     *****/
    class BungeniToolbarCommandListener implements ActionListener {

        /**
         * Run the button action in a swingworker thread in the background
         */
        class buttonActionRunner extends SwingWorker<Boolean, Object> {
          JButton sourceButton ;

          public buttonActionRunner (JButton origButton) {
              this.sourceButton = origButton;
          }

          protected Boolean doInBackground() throws Exception {
                         Container parentContainer = sourceButton.getParent();
                        buttonPanel containerPanel = (buttonPanel) parentContainer;
                        //get the action element
                        BungeniToolbarActionElement actionElement = containerPanel.getActionElement();
                        executeToolbarAction(actionElement);
                        return new Boolean(true);
            }

            @Override
          public void done(){

          }

        }
        public  synchronized void actionPerformed(ActionEvent e) {
            //get the button originating the event
            final JButton sourceButton = (JButton) e.getSource();
            //disable the button immediately
            sourceButton.setEnabled(false);
            //call the swingworker thread for the button event
            (new buttonActionRunner(sourceButton)).execute();
        }

        /*** class APIs ***/
        public void executeToolbarAction(BungeniToolbarActionElement actionElement) {
            BungeniToolbarTargetProcessor targetObj = new BungeniToolbarTargetProcessor(actionElement.getTarget());
            SelectorDialogModes selectedMode = actionElement.getMode();
            // toolbarAction tbAction = null;
            toolbarAction tbSubAction = null;
            switch (targetObj.target_type) {
                /**
                case ACTION:
                    tbAction = processInsertion(targetObj);
                    tbAction.setSelectorDialogMode(selectedMode);
                    processAction(tbAction);
                    break;
                 **/
                case SUB_ACTION:
                    tbSubAction = processSelection(targetObj);
                    tbSubAction.setSelectorDialogMode(selectedMode);
                    // processSubAction(tbSubAction);
                    processAction(tbSubAction);
                    break;
            }

        }

      
        // !+ACTION_RECONF (rm, jan 2012) - toolbarAction is deprecated
        /**
        private void processSubAction(toolbarSubAction action) {
            log.debug("processSubAction:" + action.sub_action_name());
            IEditorActionEvent event = getEventClass(action);
            event.doCommand(getOODocument(), action, parentFrame);
        }
        **/

        // private void processAction(toolbarAction action) {
        private void processAction(toolbarAction action) {
            // log.debug("processAction:" + action.action_name());
            log.debug("processAction:" + action.sub_action_name());
            if (action.isTopLevelAction()) {
                log.info("toolbar: processAction: not processing topLevelAction type");
                return;
            }
            IEditorActionEvent event = getEventClass(action);
            event.doCommand(getOODocument(), action, parentFrame);
        }

    

       private toolbarAction __processTarget(BungeniToolbarTargetProcessor targetObj) {
            String documentType = BungeniEditorPropertiesHelper.getCurrentDocType();
            //instance.Connect();
             Element subActionElement = null;
             toolbarAction subActionObj = null;
            try {
                    subActionElement = DocumentActionsReader.getInstance().getDocumentActionByName(
                            documentType,
                            targetObj.getSubActionName()
                            );

                if (subActionElement != null) {
                    subActionObj = new toolbarAction(subActionElement);
                    subActionObj.setActionValue(targetObj.getActionValue());
                } else {
                    log.error("subActionElement was null !");
                }
             } catch (Exception ex) {
                log.error("Unable to create subActionElement or toolbarSubAction !", ex);
            }
             return subActionObj;
       }

        private toolbarAction processSelection(BungeniToolbarTargetProcessor targetObj) {
            return this.__processTarget(targetObj);
        }

        private toolbarAction processInsertion(BungeniToolbarTargetProcessor targetAction) {
            return this.__processTarget(targetAction);
        }


    }

    
    JPopupMenu sectionStructureMenu;
    private static HashMap<String, String> POPUP_ACTIONS = new HashMap<String, String>() {

        {
            put("GO_TO", "Go To");
            put("NL_AFTER", "New Line After");
            put("NL_BEFORE", "New Line Before");
        }
    };





  

    private documentActionPanel self() {
        return this;
    }

   private void initMouseListener() {
        //first move focus to frame on mouse over
        parentFrame.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent arg0) {
                log.debug("mouseListener : entered holderUIPanel");
                parentFrame.requestFocus();
            }
        });
        //after moving focus to frame, shift focus to the toolbar tree
        parentFrame.addWindowFocusListener(new WindowAdapter() {

            @Override
            public void windowGainedFocus(WindowEvent e) {
                /////fix this                toolbarTree.requestFocusInWindow();
            }
        });

    }



    private static final Font TREE_LABEL_FONT = new Font("Tahoma", Font.PLAIN, 11);

    /**
     *
     */



    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupSwitchView = new javax.swing.ButtonGroup();
        toolbarTabs = new javax.swing.JTabbedPane();

        toolbarTabs.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolbarTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolbarTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGroupSwitchView;
    private javax.swing.JTabbedPane toolbarTabs;
    // End of variables declaration//GEN-END:variables

}
