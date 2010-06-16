package org.bungeni.trackchanges.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import org.bungeni.trackchanges.registrydata.BungeniBill;
import org.bungeni.trackchanges.rss.BungeniBillDataProvider;

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
            if (fJar.getPath().endsWith(".jar"))
                 sRootPath = fJar.getParent();
            else
                sRootPath = fJar.getPath();
        } catch (URISyntaxException ex) {
            log.error("getRootPath : " + ex.getMessage(), ex);
        }
        return sRootPath;
    }
    private static final ResourceBundle bundle = ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle");

    private static String __REVIEW_WORKSPACE_NAME__ = bundle.getString("review_workspace.name");
    private static String __BILL_FOLDER_PREFIX__ = bundle.getString("bill_folder_prefix.name");
    private static String __TEMPLATE_FOLDER_NAME__ = bundle.getString("template_folder.name");

    public static String getWorkspaceForBill(String billId) {
       return  System.getProperty("user.dir") + File.separator + __REVIEW_WORKSPACE_NAME__ + File.separator + __BILL_FOLDER_PREFIX__ +"-" +billId;
    }

    public static String getWorkspace(){
        return System.getProperty("user.dir") + File.separator + __REVIEW_WORKSPACE_NAME__ ;
    }

    public static String getTemplateFolder() {
        return getWorkspace() + File.separator +__TEMPLATE_FOLDER_NAME__;
    }
    
    public static String normalizeName(String name) {
        return name.replaceAll(" ", "_").toLowerCase();
    }

    public static List<String> getAvailableReportReferences(){
        return RuntimeProperties.getSectionProp("reports", "report.name");
    }

    public static String getCurrentBillID(){
        return AppProperties.getProperty("CurrentBillID").toString();
    }

    public static  String getCurrentBillName() {
        String            aBillId      = (String) AppProperties.getProperty("CurrentBillID");
        String            billTitle    = "";
        List<BungeniBill> bungeniBills = BungeniBillDataProvider.getData();

        for (BungeniBill bungeniBill : bungeniBills) {
            if (bungeniBill.getID().equals(aBillId)) {
                billTitle = bungeniBill.getTitle();
            }
        }

        if (billTitle.length() == 0) {
            return "Unknown Bill Title";
        }

        return billTitle;
    }

    /**
     * Removes duplicates from a String array
     * @param inputArr
     * @return
     */
    public static String[] removeDuplicatesStringArray(String[] inputArr) {
            //we use LinkedHashSet to preserve insertion order
            Set<String> set = new LinkedHashSet<String>(Arrays.asList(inputArr));
            String[] array2 = (String[])(set.toArray(new String[set.size()]));
            return array2;
    }

}
