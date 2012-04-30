package org.bungeni.editor.metadata;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooDocMetadata;

/**
 * Extended metadata model for the debaterecord document type
 * @author undesa
 */
public class JudgementMetadataModel extends BaseEditorDocMetaModel {
    
    
    public static final String[] GROUPED_METADATA = {  };
    
    private String BungeniCaseNo = "";
    private String BungeniDomain = "";
    private String BungeniCourtType = "";
    private String BungeniIssuedOn = "";
    
    
    private String BungeniLitigationType = "";
    
   
    
    public static final String[] THIS_METAMODEL   = {  
        "BungeniCaseNo",
        "BungeniDomain",
        "BungeniCourtType",
        "BungeniIssuedOn",
        "BungeniLitigationType"
    };

    public JudgementMetadataModel() {
        super();
    }

    @Override
    public void setup() {
        super.setup();
         this.docMeta.put("BungeniCaseNo", BungeniCaseNo);
         this.docMeta.put("BungeniDomain", BungeniDomain);
         this.docMeta.put("BungeniCourtType", BungeniCourtType);
         this.docMeta.put("BungeniIssuedOn", BungeniIssuedOn);
         
         this.docMeta.put("BungeniLitigationType", BungeniDomain);
         
        for (String sMeta : THIS_METAMODEL) {
            this.docMeta.put(sMeta, "");
        }
    }

    @Override
    public void saveModel(OOComponentHelper ooDocument) {
        ooDocMetadata docM = new ooDocMetadata(ooDocument);

        for (String sMeta : THIS_METAMODEL) {
            docM.AddProperty(sMeta, docMeta.get(sMeta));
        }

        
    }
    
     public String getBungeniCaseNo() {
        return BungeniCaseNo;
    }

    public void setBungeniCaseNo(String BungeniCaseNo) {
        this.BungeniCaseNo = BungeniCaseNo;
    }
    
     public String getBungeniDomain() {
        return BungeniDomain;
    }

    public void setBungeniDomain(String BungeniDomain) {
        this.BungeniDomain = BungeniDomain;
    }
    
      public String getBungeniCourtType() {
        return BungeniCourtType;
    }
     public void setBungeniCourtType(String BungeniCourtType) {
        this.BungeniCourtType = BungeniCourtType;
    }
     
      public void setBungeniIssuedOn(String BungeniIssuedOn) {
        this.BungeniIssuedOn = BungeniIssuedOn;
    }
    
     
    
    
     public String getBungeniLitigationType() {
        return BungeniLitigationType;
    }

    public void setBungeniLitigationType(String BungeniLitigationType) {
        this.BungeniLitigationType = BungeniLitigationType;
    }
    
    
}
