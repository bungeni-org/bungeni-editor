package org.bungeni.extutils;

import ch.swingfx.color.ColorUtil;
import ch.swingfx.twinkle.NotificationBuilder;
import ch.swingfx.twinkle.style.INotificationStyle;
import ch.swingfx.twinkle.style.closebutton.RectangleCloseButton;
import ch.swingfx.twinkle.style.overlay.BorderOverlay;
import ch.swingfx.twinkle.style.overlay.GradientOverlay;
import ch.swingfx.twinkle.style.overlay.NullOverlay;
import ch.swingfx.twinkle.style.overlay.OverlayPaintMode;
import ch.swingfx.twinkle.style.theme.DarkDefaultNotification;
import ch.swingfx.twinkle.style.theme.LightDefaultNotification;
import ch.swingfx.twinkle.window.Positions;
import java.awt.Color;
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
    private final static int INFO_WAIT_TIME = 2000;
    private final static int ERR_WAIT_TIME = 3000;
    
    /**
     * Initializes notification system
     */
    public static void init(){
            INotificationStyle infoStyle = new LightDefaultNotification()
                    .withCloseButton(new RectangleCloseButton(ColorUtil.withAlpha(Color.WHITE, 0.8f), Color.BLACK).withPosition(9, 9))
                    .withWidth(400) // Optional
                    .withOverlay(new BorderOverlay(0, Color.BLACK, OverlayPaintMode.ALWAYS,
				new GradientOverlay(ColorUtil.withAlpha(Color.BLACK, 0f), ColorUtil.withAlpha(Color.BLACK, 0.1f), OverlayPaintMode.MOUSE_OVER)) )
                    .withAlpha(0.9f) // Optional
                    ;
            INotificationStyle errorStyle = new DarkDefaultNotification()
                    .withCloseButton(new RectangleCloseButton(ColorUtil.withAlpha(Color.WHITE, 0.8f), Color.BLACK).withPosition(9, 9))
                    .withOverlay(new GradientOverlay(ColorUtil.withAlpha(Color.RED, 0f), ColorUtil.withAlpha(Color.BLACK, 0.1f), OverlayPaintMode.MOUSE_OVER))
                    .withWidth(400) // Optional
                    .withAlpha(0.9f) // Optional
                    ;
            
            // Now lets build the notification
            infoBuilder = new NotificationBuilder()
                    .withStyle(infoStyle) // Required. here we set the previously set style
                    .withDisplayTime(INFO_WAIT_TIME) // Optional
                    .withIcon(UIManager.getIcon("OptionPane.informationIcon"))
                    .withPosition(Positions.SOUTH_WEST); // Optional. Show it at the center of the screen
            
            errorBuilder = new NotificationBuilder()
                    .withStyle(infoStyle) // Required. here we set the previously set style
                    .withIcon(UIManager.getIcon("OptionPane.errorIcon"))
                    .withDisplayTime(ERR_WAIT_TIME) // Optional
                    .withPosition(Positions.SOUTH_WEST); // Optional. Show it at the center of the screen
    }
    
    public static void info(String title, String message) {
            infoBuilder
                    .withTitle(title)
                    .withMessage(message)
                    .showNotification();
    }

    public static void infoTimed(String title, String message, int nTime) {
            
            infoBuilder
                    .withTitle(title)
                    .withMessage(message)
                    .withDisplayTime(nTime)
                    .showNotification();
    }
    
    public static void error(String title, String message) {
            errorBuilder
                    .withTitle(title)
                    .withMessage(message)
                    .showNotification();
    }
    
    public static void error(String message) {
          error("Error Notification", message);
    }
    
    public static void errorTimed(String title, String message, int nTime) {
            errorBuilder
                    .withTitle(title)
                    .withMessage(message)
                    .withDisplayTime(nTime)
                    .showNotification();
    }
    

    
    public static void info(String message) {
          info("Info Notification", message);
    }

    
    public static void main(String[] args) {
        NotifyBox.init();
        NotifyBox.error("world world world  world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world world");
    }
}
