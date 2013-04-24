package org.birzeit.editor.metadata;

//~--- non-JDK imports --------------------------------------------------------
import org.bungeni.editor.metadata.BaseEditorDocMetaModel;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooDocMetadata;

/**
 * Extended metadata model for the debaterecord document type
 *
 * @author undesa
 */
public class CourtJudgementMetadataModel extends BaseEditorDocMetaModel {

    public static final String[] GROUPED_METADATA = {};
    private String BungeniLanguageCode = "";
    private String BungeniRegion = "";
    private String BungeniDomain = "";
    private String BungeniCaseType = "";
    private String BungeniCourtType = "";
    private String BungeniCity = "";
    private String BungeniCaseNo = "";
    private String BungeniDate = "";
    private String BungeniYear = "";
    private String BungeniImportance = "";
    public static final String[] THIS_METAMODEL = {
        "BungeniLanguageCode",
        "BungeniRegion",
        "BungeniDomain",
        "BungeniCaseType",
        "BungeniCourtType",
        "BungeniCity",
        "BungeniCaseNo",
        "BungeniDate",
        "BungeniYear",
        "BungeniImportance"
    };

    public CourtJudgementMetadataModel() {
        super();
    }

    @Override
    public void setup() {
        super.setup();
        this.docMeta.put("BungeniLanguageCode", BungeniLanguageCode);
        this.docMeta.put("BungeniRegion", BungeniRegion);
        this.docMeta.put("BungeniDomain", BungeniDomain);
        this.docMeta.put("BungeniCaseType", BungeniCaseType);
        this.docMeta.put("BungeniCourtType", BungeniCourtType);
        this.docMeta.put("BungeniCity", BungeniCity);
        this.docMeta.put("BungeniCaseNo", BungeniCaseNo);
        this.docMeta.put("BungeniDate", BungeniDate);
        this.docMeta.put("BungeniYear", BungeniYear);
        this.docMeta.put("BungeniImportance", BungeniImportance);
    }

    public String getBungeniLanguageCode() {
        return BungeniLanguageCode;
    }

    public void setBungeniLanguageCode(String BungeniLangCode) {
        this.BungeniLanguageCode = BungeniLangCode;
    }

    public void setBungeniRegion(String BungeniRegion) {
        this.BungeniRegion = BungeniRegion;
    }

    public String getBungeniRegion() {
        return BungeniRegion;
    }

    public String getBungeniDomain() {
        return BungeniDomain;
    }

    public void setBungeniDomain(String BungeniDomain) {
        this.BungeniDomain = BungeniDomain;
    }

    public String getBungeniCaseType() {
        return BungeniCaseType;
    }

    public void setBungeniCaseType(String BungeniCaseType) {
        this.BungeniCaseType = BungeniCaseType;
    }

    public String getBungeniCourtType() {
        return BungeniCourtType;
    }

    public void setBungeniCourtType(String BungeniCourtType) {
        this.BungeniCourtType = BungeniCourtType;
    }

    public String getBungeniCity() {
        return BungeniCity;
    }

    public void setBungeniCity(String BungeniCity) {
        this.BungeniCity = BungeniCity;
    }

    public String getBungeniCaseNo() {
        return BungeniCaseNo;
    }

    public void setBungeniCaseNo(String BungeniCaseNo) {
        this.BungeniCaseNo = BungeniCaseNo;
    }

    public String getBungeniDate() {
        return BungeniDate;
    }

    public void setBungeniDate(String BungeniDate) {
        this.BungeniDate = BungeniDate;
    }

    public String getBungeniYear() {
        return BungeniYear;
    }

    public void setBungeniYear(String BungeniYear) {
        this.BungeniYear = BungeniYear;
    }
    
     public String getBungeniImportance() {
        return BungeniImportance;
    }

    public void setBungeniImportance(String BungeniImportance) {
        this.BungeniImportance = BungeniImportance;
    }
}
