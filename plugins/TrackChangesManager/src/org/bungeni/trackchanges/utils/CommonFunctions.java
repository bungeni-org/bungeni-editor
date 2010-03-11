package org.bungeni.trackchanges.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

/**
 *
 * @author Ashok Hariharan
 */
public class CommonFunctions {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CommonFunctions.class.getName());


    public static String getRootPath() {    
        String sRootPath = "";
        try {
            File fJar  =  new File(CommonFunctions.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            sRootPath = fJar.getParent();
        } catch (URISyntaxException ex) {
            log.error("getRootPath : " + ex.getMessage(), ex);
        }
        return sRootPath;
    }
    private static final ResourceBundle bundle = ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle");

    private static String __REVIEW_WORKSPACE_NAME__ = bundle.getString("review_workspace.name");
    private static String __BILL_FOLDER_PREFIX__ = bundle.getString("bill_folder_prefix.name");

    public static String getWorkspaceForBill(String billId) {
       return  System.getProperty("user.dir") + File.separator + __REVIEW_WORKSPACE_NAME__ + File.separator + __BILL_FOLDER_PREFIX__ +"-" +billId;
    }

    
}
