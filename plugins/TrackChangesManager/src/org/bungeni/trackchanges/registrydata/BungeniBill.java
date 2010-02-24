package org.bungeni.trackchanges.registrydata;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniBill {
    String billUri;
    String billTitle;
    String billId;
    String billDate;

    public BungeniBill (String uri, String title, String billId, String billDate) {
        this.billUri = uri;
        this.billTitle = title;
        this.billId = billId;
        this.billDate = billDate;
    }

    public String getTitle() {
        return billTitle;
    }

    public String getDate() {
        return this.billDate;
    }

    public String getURI() {
        return this.billUri;
    }

    public String getID(){
        return this.billId;
    }
    
    @Override
    public String toString() {
        return getTitle();
    }
}
