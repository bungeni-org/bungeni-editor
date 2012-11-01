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

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniAttachment {

    private Document doc ;
    private String url ;
    private String title ;
    private String type ;
    private String attachmentName ; 
    private String downloadURL;
    private String mimeType ;
    private String status ;
    private String statusDate ;
    private String language;
    
    public BungeniAttachment(String sDocURL, Document doc ){
       this.url = sDocURL;
       this.doc = doc;
       init();
    }


    private void init(){
        this.mimeType = parseField("mimetype");
        this.attachmentName = parseField("name");
        this.title = parseField("title");
        this.language = parseField("language");
        this.status = parseField("status");
        this.type = parseField("type");
        this.statusDate = parseField("status_date");
        this.downloadURL = this.url + "/download";
    }


  private String divParseByID(String elementId){
      return "div#" + elementId + " > div.content-right-column > div.widget";
  }


  private String parseField(String fieldId){
      Elements elemType = doc.select(divParseByID(fieldId));
      if (elemType.size() > 0 ) {
           return elemType.get(0).text();
        } else {
           return "";
        }
  }

  




}
