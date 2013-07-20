package org.birzeit.editor.metadata;

//~--- non-JDK imports --------------------------------------------------------
import org.bungeni.editor.metadata.*;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooDocMetadata;

/**
 * Extended metadata model for the debaterecord document type
 *
 * @author undesa
 */
public class JudgementMetadataModel extends BaseEditorDocMetaModel {

    public static final String[] GROUPED_METADATA = {};
    private String BungeniMainDocID = "";
    private String BungeniLanguageCode = "";
    private String BungeniCourtType = "";
    private String BungeniCourtTypeID = "";
    private String BungeniFamily = "";
    private String BungeniFamilyID = "";
    private String BungeniDomain = "";
    private String BungeniDomainID = "";
    private String BungeniRegion = "";
    private String BungeniRegionID = "";
    private String BungeniCaseType = "";
    private String BungeniCaseTypeID = "";
    private String BungeniCity = "";
    private String BungeniCityID = "";
    private String BungeniCaseNo = "";
    private String BungeniIssuedOn = "";
    private String BungeniYear = "";
    private String BungeniImportance = "";
    public static final String[] THIS_METAMODEL = {
        "BungeniMainDocID",
        "BungeniLanguageCode",
        "BungeniCourtType",
        "BungeniCourtTypeID",
        "BungeniFamily",
        "BungeniFamilyID",
        "BungeniDomain",
        "BungeniDomainID",
        "BungeniCaseType",
        "BungeniCaseTypeID",
        "BungeniRegion",
        "BungeniRegionID",
        "BungeniCity",
        "BungeniCityID",
        "BungeniCaseNo",
        "BungeniIssuedOn",
        "BungeniYear",
        "BungeniImportance"
    };

    public JudgementMetadataModel() {
        super();
    }

    @Override
    public void setup() {
        super.setup();
        this.docMeta.put("BungeniMainDocID", BungeniMainDocID);
        this.docMeta.put("BungeniLanguageCode", BungeniLanguageCode);
        this.docMeta.put("BungeniCourtType", BungeniCourtType);
        this.docMeta.put("BungeniFamily", BungeniFamily);
        this.docMeta.put("BungeniRegion", BungeniRegion);
        this.docMeta.put("BungeniDomain", BungeniDomain);
        this.docMeta.put("BungeniCaseType", BungeniCaseType);
        this.docMeta.put("BungeniCity", BungeniCity);
        this.docMeta.put("BungeniCourtTypeID", BungeniCourtTypeID);
        this.docMeta.put("BungeniFamilyID", BungeniFamilyID);
        this.docMeta.put("BungeniRegionID", BungeniRegionID);
        this.docMeta.put("BungeniDomainID", BungeniDomainID);
        this.docMeta.put("BungeniCaseTypeID", BungeniCaseTypeID);
        this.docMeta.put("BungeniCityID", BungeniCityID);
        this.docMeta.put("BungeniCaseNo", BungeniCaseNo);
        this.docMeta.put("BungeniIssuedOn", BungeniIssuedOn);
        this.docMeta.put("BungeniYear", BungeniYear);
        this.docMeta.put("BungeniImportance", BungeniImportance);
        
        
        // FRBR Items
        // work
        docMeta.put("BungeniWorkAuthor", "");
        docMeta.put("BungeniWorkAuthorAs", "");
        docMeta.put("BungeniWorkAuthorURI", "");
        docMeta.put("BungeniWorkDate", "");
        docMeta.put("BungeniWorkDateName", "");
        docMeta.put("BungeniWorkURI", "");

        // expression
        docMeta.put("BungeniExpAuthor", "");
        docMeta.put("BungeniExpAuthorAs", "");
        docMeta.put("BungeniExpAuthorURI", "");
        docMeta.put("BungeniExpDate", "");
        docMeta.put("BungeniExpDateName", "");
        docMeta.put("BungeniExpURI", "");

        // manifestation
        docMeta.put("BungeniManAuthor", "");
        docMeta.put("BungeniManAuthorAs", "");
        docMeta.put("BungeniManAuthorURI", "");
        docMeta.put("BungeniManDate", "");
        docMeta.put("BungeniManDateName", "");
        docMeta.put("BungeniManURI", "");
    }

    public String getBungeniLanguageCode() {
        return BungeniLanguageCode;
    }

    public void setBungeniLanguageCode(String BungeniLangCode) {
        this.BungeniLanguageCode = BungeniLangCode;
    }

    public void setBungeniCourtType(String BungeniCourtType) {
        this.BungeniCourtType = BungeniCourtType;
    }

    public String getBungeniCourtType() {
        return BungeniCourtType;
    }

    public String getBungeniDomain() {
        return BungeniDomain;
    }

    public void setBungeniDomain(String BungeniDomain) {
        this.BungeniDomain = BungeniDomain;
    }
    
     public String getBungeniImportance() {
        return BungeniDomain;
    }

    public void setBungeniImportance(String BungeniImportance) {
        this.BungeniImportance = BungeniImportance;
    }

    public String getBungeniRegion() {
        return BungeniRegion;
    }

    public void setBungeniRegion(String BungeniRegion) {
        this.BungeniRegion = BungeniRegion;
    }

    public String getBungeniCaseType() {
        return BungeniCaseType;
    }

    public void setBungeniCaseType(String BungeniCaseType) {
        this.BungeniCaseType = BungeniCaseType;
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
        return BungeniIssuedOn;
    }

    public void setBungeniDate(String BungeniDate) {
        this.BungeniIssuedOn = BungeniDate;
    }

    public String getBungeniYear() {
        return BungeniYear;
    }

    public void setBungeniYear(String BungeniYear) {
        this.BungeniYear = BungeniYear;
    }
}
