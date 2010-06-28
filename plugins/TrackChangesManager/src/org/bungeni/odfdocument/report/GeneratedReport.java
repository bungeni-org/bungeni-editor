
package org.bungeni.odfdocument.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.codec.binary.Base64;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.odfdom.utils.BungeniOdfDateHelper;
import org.bungeni.reports.process.reportEditableChangesByOrder_Queries;

/**
 *
 * @author Ashok Hariharan
 */
public class GeneratedReport {
        private static org.apache.log4j.Logger log =
            org.apache.log4j.Logger.getLogger(GeneratedReport.class.getName());
        private String reportId;
    private String reportName;
    private Date reportGenerationDate;
    private String billId;
    private Boolean userEditedName;

        public GeneratedReport(String rId, String rName, String rgDate, String bId, String ueName) {
            try {
                this.reportId = rId;
                this.reportName = rName;
                SimpleDateFormat sdfformat = new SimpleDateFormat(BungeniOdfDateHelper.DEFAULT_JAVA_DATE_FORMAT);
                this.reportGenerationDate = sdfformat.parse(rgDate);
                this.billId = bId;
                this.userEditedName = Boolean.parseBoolean(ueName);
            } catch (ParseException ex) {
               log.error("GeneratedReport" + ex, ex);
            }

        }


        /***
         * This function is identical to org.bungeni.extutils.BungeniUUID ... this needs to be included from there than paste it here 
         * @return
         */
       public static String getShortUUID(){
        UUID uuid = UUID.randomUUID();
        byte[] uuidBytes = toByteArray(uuid);
        //convert UUID to base64 encoded string
        Base64 encoder = new Base64();
        String sbyteArrAsB64 = new String(encoder.encode(uuidBytes));
        //strip the ==
        sbyteArrAsB64 = sbyteArrAsB64.split("=")[0];
        return sbyteArrAsB64;
    }

     private static byte[] toByteArray(UUID uuid) {

        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        byte[] buffer = new byte[16];

        for (int i = 0; i < 8; i++) {
                buffer[i] = (byte) (msb >>> 8 * (7 - i));
        }
        for (int i = 8; i < 16; i++) {
                buffer[i] = (byte) (lsb >>> 8 * (7 - i));
        }

        return buffer;
    }


    /**
     * @return the reportId
     */
    public String getReportId() {
        return reportId;
    }

    /**
     * @return the reportName
     */
    public String getReportName() {
        return reportName;
    }

    /**
     * @return the reportGenerationDate
     */
    public Date getReportGenerationDate() {
        return reportGenerationDate;
    }

    /**
     * @param reportGenerationDate the reportGenerationDate to set
     */
    public void setReportGenerationDate(Date reportGenerationDate) {
        this.reportGenerationDate = reportGenerationDate;
    }

    /**
     * @return the billId
     */
    public String getBillId() {
        return billId;
    }

    /**
     * @return the userEditedName
     */
    public Boolean getUserEditedName() {
        return userEditedName;
    }

    /**
     * @param userEditedName the userEditedName to set
     */
    public void setUserEditedName(Boolean userEditedName) {
        this.userEditedName = userEditedName;
    }



    public static GeneratedReport newGeneratedReport(String reportName, String billId ) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(BungeniOdfDateHelper.DEFAULT_JAVA_DATE_FORMAT);
            String sDateNow = sdf.format(cal.getTime());
            GeneratedReport genReport  = new GeneratedReport (getShortUUID(), reportName, sDateNow, billId, "false");
            BungeniClientDB db = BungeniClientDB.defaultConnect();
            db.Connect();
            String updQuery = reportEditableChangesByOrder_Queries.ADD_NEW_REPORT_FOR_BILL(
                    genReport.reportId,
                    genReport.reportName,
                    genReport.reportGenerationDate,
                    genReport.billId,
                    genReport.userEditedName);
            log.info("newGeneratedReport :" + updQuery);
            db.Update(updQuery);
            db.EndConnect();
            return genReport;
    }
}
