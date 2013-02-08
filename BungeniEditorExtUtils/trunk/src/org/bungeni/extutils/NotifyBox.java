package org.bungeni.extutils;

import ch.swingfx.twinkle.NotificationBuilder;
import ch.swingfx.twinkle.style.INotificationStyle;
import ch.swingfx.twinkle.style.theme.DarkDefaultNotification;
import ch.swingfx.twinkle.style.theme.LightDefaultNotification;
import ch.swingfx.twinkle.window.Positions;
import javax.swing.UIManager;

/**
 * Twinkle based notification class
 * Requires init() to be called once on app startup
 * after thant 
 * @author Ashok Hariharan
 */
public class NotifyBox {

    private static NotificationBuilder infoBuilder = null;
    private static NotificationBuilder errorBuilder = null;
    
    
    /**
     * Initializes notification system
     */
    private static void init(){
            INotificationStyle infoStyle = new LightDefaultNotification()
                    .withWidth(400) // Optional
                    .withAlpha(0.9f) // Optional
                    ;
            INotificationStyle errorStyle = new DarkDefaultNotification()
                    .withWidth(400) // Optional
                    .withAlpha(0.9f) // Optional
                    ;
            
            // Now lets build the notification
            infoBuilder = new NotificationBuilder()
                    .withStyle(infoStyle) // Required. here we set the previously set style
                    .withDisplayTime(2000) // Optional
                    .withIcon(UIManager.getIcon("OptionPane.informationIcon"))
                    .withPosition(Positions.CENTER); // Optional. Show it at the center of the screen
            
            errorBuilder = new NotificationBuilder()
                    .withStyle(infoStyle) // Required. here we set the previously set style
                    .withIcon(UIManager.getIcon("OptionPane.errorIcon"))
                    .withDisplayTime(3000) // Optional
                    .withPosition(Positions.CENTER); // Optional. Show it at the center of the screen
    }
    
    public static void info(String title, String message) {
            infoBuilder
                    .withTitle(title)
                    .withMessage(message)
                    .showNotification();
    }

    public static void error(String message, String title) {
            errorBuilder
                    .withTitle(title)
                    .withMessage(message)
                    .showNotification();
    }
    
    public static void error(String message) {
          error("Error Notification", message);
    }
    

    public static void info(String message) {
          info("Info Notification", message);
    }

}
