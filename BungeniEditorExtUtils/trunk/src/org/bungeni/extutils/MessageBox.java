package org.bungeni.extutils;

import ch.swingfx.twinkle.NotificationBuilder;
import ch.swingfx.twinkle.event.NotificationEventAdapter;
import ch.swingfx.twinkle.style.INotificationStyle;
import ch.swingfx.twinkle.style.theme.LightDefaultNotification;
import ch.swingfx.twinkle.window.Positions;
import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MessageBox extends Object {

    
    private static NotificationBuilder builder = null;
    public static void notification(String title, String message) {
        if (builder == null ) {
            // AA the text
            //System.setProperty("swing.aatext", "true");
            // First we define the style/theme of the window.
            // Note how we override the default values
            INotificationStyle style = new LightDefaultNotification() //	.withWidth(400) // Optional
                    //	.withAlpha(0.9f) // Optional
                    ;
            // Now lets build the notification
            builder = new NotificationBuilder()
                    .withStyle(style) // Required. here we set the previously set style
                    .withTitle(title) // Required.
                    .withMessage(message) // Optional
                    .withDisplayTime(3000) // Optional
                    .withPosition(Positions.CENTER); // Optional. Show it at the center of the screen
            
        }
        builder.showNotification();
        }

    public static void Notify(String message, String title) {
        //to do 
    }

    public static void Notify(String message) {
        //to do 
    }

    public static void OK(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg);
    }

    public static void OK(Component parent, Object[] msgs) {
        JOptionPane.showMessageDialog(parent, msgs);
    }

    public static void OK(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    public static void OK(Component parent, String msg, String title, int type) {

        JOptionPane.showMessageDialog(parent, msg, title, type);
    }

    public static int Confirm(Component parent, String msg, String title) {
        int ret = JOptionPane.showConfirmDialog(parent, msg, title, JOptionPane.YES_NO_OPTION);
        return ret;
    }

    public static int Confirm(Component parent, String msg, String title, Object[] buttonTexts, int nOption) {
        int ret = JOptionPane.showOptionDialog(parent, msg, title, nOption, JOptionPane.QUESTION_MESSAGE, null,
                buttonTexts, buttonTexts[0]);
        return ret;
    }

    /**
     * This method displays a JOptionPane with a custom Panel rather than the
     * message added
     *
     * @param parent
     * @param messsage
     * @param title
     * @param dialogPanel
     * @return
     */
    public static int OptionsConfirm(Component parent, JPanel dialogPanel, String title,
            Object[] buttonTexts) {

        int ret = JOptionPane.showOptionDialog(parent, dialogPanel, title, JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, buttonTexts, buttonTexts[0]);

        return ret;
    }
}
