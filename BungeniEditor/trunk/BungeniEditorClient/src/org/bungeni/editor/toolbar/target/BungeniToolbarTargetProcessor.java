package org.bungeni.editor.toolbar.target;

/**
 * valid subAction reference = toolbarSubAction.makePrayerSection.debatedate_entry
 * valid action reference = toolbarAction.makePrayerSection
 * valid subAction reference = toobarSubAction.makrPrayerSection.debatedate_entry:value
 * @author Administrator
 */
public class BungeniToolbarTargetProcessor {
    
    /** different representations of a target value **/
      // !+ACTION_RECONF (ah, 24-01-2012) - simplifying action names removing "." based names
    // disabling arrTarget and using targetValue attribute instead
    //public String[] arrTarget ;
    public String targetValue ;

    public static enum TARGET { ACTION, SUB_ACTION, GENERAL_ACTION, UNDEFINED};
    public TARGET target_type = TARGET.SUB_ACTION;;
  
      // !+ACTION_RECONF (ah, 24-01-2012) - simplifying action names removing "." based names
    // removing parentAction name -- not used anymore 
    //private String parentActionName = "";
    private String subActionName = "";
    private String actionValue = "";
       // !+ACTION_RECONF (ah, 24-01-2012) - simplifying action names removing "." based names
    //so the below are not required since there is no action name processing
    /**
    private static int ACTION_OFFSET = 0;
    private static int SUB_ACTION_OFFSET = ACTION_OFFSET+1;
     */
    /** Creates a new instance of BungeniToolbarTargetProcessor */
    public BungeniToolbarTargetProcessor(String strTarget) {
    // !+ACTION_RECONF (ah, 24-01-2012) - simplifying action names removing "." based names
     // to just unique strings
     // this.arrTarget =  strTarget.split("[.]");
      this.targetValue = strTarget;
      if (this.targetValue.indexOf(":") != -1) {
          String[] targetParameters = this.targetValue.split(":");
          this.subActionName = targetParameters[0];
          this.actionValue = targetParameters[1];
      } else {
          this.subActionName = this.targetValue;
      }



      //get action type
        // !+ACTION_RECONF (ah, 24-01-2012) - simplifying action names removing "." based names
        // set target type directly and not based on condition
        //if (this.arrTarget[0].equals("generalAction"))
         //  target_type = TARGET.GENERAL_ACTION;
      // !+ACTION_RECONF (rm, jan 2012) - toolbarAction class has been deprecated,
      // all functionality has been moved to toolbarSubAction
      /**
       else
       if (this.strTarget[0].equals("toolbarAction")) {
              target_type = TARGET.ACTION;
       }
      
       else
       if (this.arrTarget[0].equals("toolbarSubAction")) {
              target_type = TARGET.SUB_ACTION;
       } else {
          target_type = TARGET.UNDEFINED;
       }
       *  **/
      //get action name
      
      // setActionValue();
    }
     //actiontype.parentaciton.sub_aciton_name:sub_action_value
     //the below will give us sub_action_name:sub_action_value
       // !+ACTION_RECONF (ah, 24-01-2012) - simplifying action names removing "." based names
    // We dont process the target name , we just map the target name to <action @name> attribute.
    /*
    private void setActionValue() {
        int nLength= this.arrTarget.length ;
        String lastElem = this.arrTarget[nLength - 1];
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
   }*/

    public String getSubActionName(){
        return this.subActionName;
    }

       // !+ACTION_RECONF (ah, 24-01-2012) - simplifying action names removing "." based names
    /*
    public String getParentActionName(){
        return this.parentActionName;
    }*/

    public String getActionValue() {
        return this.actionValue;
    }

    public static void main(String[] args) {
        BungeniToolbarTargetProcessor ttp = new BungeniToolbarTargetProcessor("toolbarSubAction.makePrayerSection.make_logo:test");
        System.out.println("action value = " + ttp.actionValue);
        System.out.println("sub action name  = " + ttp.subActionName);
        
    }
}
