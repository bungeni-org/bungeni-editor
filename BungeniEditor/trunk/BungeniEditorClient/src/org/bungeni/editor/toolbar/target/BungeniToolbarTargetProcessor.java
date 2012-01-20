package org.bungeni.editor.toolbar.target;

/**
 * valid subAction reference = toolbarSubAction.makePrayerSection.debatedate_entry
 * valid action reference = toolbarAction.makePrayerSection
 * valid subAction reference = toobarSubAction.makrPrayerSection.debatedate_entry:value
 * @author Administrator
 */
public class BungeniToolbarTargetProcessor {
    public String[] strTarget ;
    public static enum TARGET { ACTION, SUB_ACTION, GENERAL_ACTION, UNDEFINED};
    public TARGET target_type; 
    public String actionValue = "";
    public String actionName = "";
    public String subActionName = "";
    private static int ACTION_OFFSET = 0;
    private static int SUB_ACTION_OFFSET = ACTION_OFFSET+1;
    /** Creates a new instance of BungeniToolbarTargetProcessor */
    public BungeniToolbarTargetProcessor(String strTarget) {
      this.strTarget =  strTarget.split("[.]");
      //get action type
       if (this.strTarget[0].equals("generalAction"))
              target_type = TARGET.GENERAL_ACTION;
      // !+ACTION_RECONF (rm, jan 2012) - toolbarAction class has been deprecated,
      // all functionality has been moved to toolbarSubAction
      /**
       else
       if (this.strTarget[0].equals("toolbarAction")) {
              target_type = TARGET.ACTION;
       }
       **/
       else
       if (this.strTarget[0].equals("toolbarSubAction")) {
              target_type = TARGET.SUB_ACTION;
       } else {
          target_type = TARGET.UNDEFINED;
       }
      //get action name
      actionName = this.strTarget[ACTION_OFFSET+1];
      if (target_type == TARGET.SUB_ACTION ) {
          //get sub_action name
          subActionName = this.strTarget[SUB_ACTION_OFFSET+1];
      }
      //set action value
      setActionValue();
    }
     //actiontype.parentaciton.sub_aciton_name:sub_action_value
     //the below will give us sub_action_name:sub_action_value
    private void setActionValue() {
        int nLength= strTarget.length ;
        String lastElem = strTarget[nLength - 1];
        if (lastElem.indexOf(":") != -1) {
            //contains a value
            String strActionValue[] = lastElem.split(":");
            this.actionValue = strActionValue[1];
            if (this.target_type == TARGET.SUB_ACTION)
                this.subActionName = strActionValue[0];
        } else  {
            this.actionValue = "";
            if (this.target_type == TARGET.SUB_ACTION)
                this.subActionName = lastElem;
        }
   }
    
    public static void main(String[] args) {
        BungeniToolbarTargetProcessor ttp = new BungeniToolbarTargetProcessor("toolbarSubAction.makePrayerSection.make_logo:test");
        System.out.println("action name = " +
                ttp.actionName );
        System.out.println("action value = " + ttp.actionValue);
        System.out.println("sub action name  = " + ttp.subActionName);
        
    }
}
