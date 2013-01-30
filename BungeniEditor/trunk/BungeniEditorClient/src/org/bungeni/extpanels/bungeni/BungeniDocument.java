/*
 *  Copyright (C) 2012 Africa i-Parliaments
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.bungeni.extpanels.bungeni;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Parses a Bungeni JSoup Document into a Document Object
 *
 * @author Ashok Hariharan
 */
public class BungeniDocument {

    private final Document doc;
    private String title = "";
    private String description = "";
    private String status = "";
    private String type = "";
    private List<Attachment> attachments = new ArrayList<Attachment>(0);
    private List<Transition> transitions = new ArrayList<Transition>(0);
    private String url = "";

    public class Attachment {
        //listed attributes

        public String title;
        public String url;
        public String downloadUrl;
        public boolean isSelected;
        //document attributes
        public String attType;
        public String fileName;
        public String mimeType;
        public String status;
        public String statusDate;
        public String language;
        public String description;
        public List<Transition> transitions = new ArrayList<Transition>(0);

        public Attachment() {
            title = "";
            url = "";
            downloadUrl = "";
            isSelected = false;
        }

        public Attachment(String title, String url, String downloadUrl) {
            this.title = title;
            this.url = url;
            this.downloadUrl = downloadUrl;

        }

        public void parseAttachment(Document attDoc) {
            parseMimeType(attDoc);
            parseFileName(attDoc);
            parseTitle(attDoc);
            parseDescription(attDoc);
            parseType(attDoc);
            parseStatus(attDoc);
            parseTransitions(attDoc);
            parseStatusDate(attDoc);
            parseLanguage(attDoc);
        }

        private void parseMimeType(Document attDoc) {
            Elements elemMimeType = attDoc.select("div#mimetype > div.content-right-column > div.widget");
            if (elemMimeType.size() > 0) {
                this.mimeType = elemMimeType.get(0).text();
            } else {
                this.mimeType = "";
            }
        }

        private void parseTransitions(Document attDoc) {
            Elements trans = attDoc.select("[id^=workflow-]");
            for (int i = 0; i < trans.size(); i++) {
                Element elem = trans.get(i);
                Transition tr = new Transition();
                tr.title = elem.text();
                tr.url = elem.attributes().get("href");
                this.transitions.add(tr);
            }

        }

        
        private void parseType(Document attDoc) {
            Elements elemType = attDoc.select("div#type > div.content-right-column > div.widget");
            if (elemType.size() > 0) {
                this.attType = elemType.get(0).text();
            } else {
                this.attType = "";
            }
        }
        
        private void parseFileName(Document attDoc) {
            Elements elemFileName = attDoc.select("div#name > div.content-right-column > div.widget");
            if (elemFileName.size() > 0) {
                this.fileName = elemFileName.get(0).text();
            } else {
                this.fileName = "";
            }
        }

        private void parseStatus(Document attDoc) {
            Elements elem = attDoc.select("div#status > div.content-right-column > div.widget");
            if (elem.size() > 0) {
                this.status = elem.get(0).text();
            } else {
                this.status = "";
            }
        }

        private void parseStatusDate(Document attDoc) {
            Elements elem = attDoc.select("div#status_date > div.content-right-column > div.widget");
            if (elem.size() > 0) {
                this.statusDate = elem.get(0).text();
            } else {
                this.statusDate = "";
            }
        }

        private void parseLanguage(Document attDoc) {
            Elements elem = attDoc.select("div#language > div.content-right-column > div.widget");
            if (elem.size() > 0) {
                this.language = elem.get(0).text();
            } else {
                this.language = "";
            }
        }

        private void parseDescription(Document attDoc) {
            Elements elem = attDoc.select("div#description > div.content-right-column > div.widget");
            if (elem.size() > 0) {
                this.description = elem.get(0).text();
            } else {
                this.description = "";
            }
        }

        private void parseTitle(Document attDoc) {
            Elements elem = attDoc.select("div#title > div.content-right-column > div.widget");
            if (elem.size() > 0) {
                this.title = elem.get(0).text();
            } else {
                this.title = "";
            }
        }

        @Override
        public String toString() {
            return this.title;
        }
    }

    public BungeniDocument(String url, Document doc) {
        this.url = url;
        this.doc = doc;
        init();
    }

    private void init() {
        parseType();
        parseTitle();
        parseDesc();
        parseStatus();
        parseAttachments();
        parseTransitions();
    }

    private void parseType() {
        // e.g. /ke/workspace/my-documents/external/bill-23/ 
        String[] urlParts = this.url.split("/");
        // bill-23
        String sLast = urlParts[urlParts.length - 1];
        String[] sTypeAndID = sLast.split("-");
        this.type = sTypeAndID[0];
    }

    private void parseTitle() {
        Elements titles = doc.select("h2.title");
        if (titles.size() > 0) {
            title = titles.get(0).text();
        } else {
            title = "";
        }
    }

    private void parseDesc() {
        Elements descs = doc.select("p.documentDescription");
        if (descs.size() > 0) {
            this.description = descs.get(0).text();
        } else {
            this.description = "";
        }
    }

    private void parseStatus() {
        Elements elemStatus = doc.select("div#status > div.content-right-column > div.widget");
        if (elemStatus.size() > 0) {
            this.status = elemStatus.get(0).text();
        } else {
            this.status = "";
        }
    }

    private void parseAttachments() {
        Elements attlist = doc.select("dd#fieldset-attachments a");
        for (int i = 0; i < attlist.size(); i++) {
            Element elem = attlist.get(i);
            Attachment att = new Attachment();
            att.title = elem.text();
            att.url = elem.attr("href");
            att.downloadUrl = att.url + "/download";
            getAttachments().add(att);
        }
    }

    private void parseTransitions() {
        Elements trans = doc.select("[id^=workflow-]");
        for (int i = 0; i < trans.size(); i++) {
            Element elem = trans.get(i);
            Transition tr = new Transition();
            tr.title = elem.text();
            tr.url = "";
            this.transitions.add(tr);
        }
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return the attachments
     */
    public List<Attachment> getAttachments() {
        return attachments;
    }

    /**
     * @return the transitions
     */
    public List<Transition> getTransitions() {
        return transitions;
    }

    /**
     * Returns the selected Attachment
     *
     * @return
     */
    public Attachment getSelectedAttachment() {
        for (Attachment attachment : attachments) {
            if (attachment.isSelected) {
                return attachment;
            }
        }
        return null;
    }

    public String getURL() {
        return this.url;
    }
}
