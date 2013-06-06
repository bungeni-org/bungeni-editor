/*
 * Copyright (C) 2012 UN/DESA Africa i-Parliaments Action Plan
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.bungeni.ext.integration.bungeniportal.docimpl;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class BungeniAttachment {
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

        public BungeniAttachment() {
            title = "";
            url = "";
            downloadUrl = "";
            isSelected = false;
        }

        public BungeniAttachment(String title, String url, String downloadUrl) {
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
