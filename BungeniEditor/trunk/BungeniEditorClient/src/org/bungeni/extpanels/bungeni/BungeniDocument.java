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
    private List<BungeniAttachment> attachments = new ArrayList<BungeniAttachment>(0);
    private List<Transition> transitions = new ArrayList<Transition>(0);
    private String url = "";

    
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
            BungeniAttachment att = new BungeniAttachment();
            att.title = elem.text();
            att.url = elem.attr("href");
            att.downloadUrl = att.url + "/download";
            attachments.add(att);
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
    public List<BungeniAttachment> getAttachments() {
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
    public BungeniAttachment getSelectedAttachment() {
        for (BungeniAttachment attachment : attachments) {
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
