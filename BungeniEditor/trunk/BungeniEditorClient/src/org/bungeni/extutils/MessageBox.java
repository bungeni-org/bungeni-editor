package org.bungeni.extutils;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.plaf.OptionPaneUI;


public  class MessageBox extends Object {

    public static void OK(Component parent, String msg){
        JOptionPane.showMessageDialog(parent, msg);
    }

    public static void OK(Component parent, Object[] msgs) {
        JOptionPane.showMessageDialog(parent, msgs);
    }
    public static void OK(String msg){
        JOptionPane.showMessageDialog(null, msg);
    }
    
    public static void OK(Component parent, String msg, String title,  int type){
        
        JOptionPane.showMessageDialog(parent, msg, title, type);
    }
    
    public static int Confirm(Component parent, String msg, String title) {
        int ret = JOptionPane.showConfirmDialog(parent, msg, title, JOptionPane.YES_NO_OPTION );
        return ret;
    }

    public static int Confirm(Component parent, String msg, String title, Object[] buttonTexts, int nOption) {
       int ret = JOptionPane.showOptionDialog(parent,msg,title, nOption, JOptionPane.QUESTION_MESSAGE, null,
                buttonTexts, buttonTexts[0]);
        return ret;
    }

    /** 
     * This method displays a JOptionPane with a custom Panel
     * @param parent
     * @param messsage
     * @param title
     * @param dialogPanel
     * @return
     */
    public static int OptionsConfirm(Component parent, JPanel dialogPanel, String title,
            Object [] buttonTexts) {

        int ret = JOptionPane.showOptionDialog(parent, dialogPanel, title, JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, buttonTexts, buttonTexts[0]);

        return ret ;
    }

 
}

