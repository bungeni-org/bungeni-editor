/*
 * Copyright (C) 2012 Africa i-Parliaments Action Plan
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
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
package org.bungeni.ext.integration.bungeniportal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *This class processes the JSON feed of Bungeni documents available for Editing by the Clerk within 
 * the BungeniEditor 
 * @author Ashok Hariharan
 */
public class BungeniListDocuments {

        private static org.apache.log4j.Logger log =
        org.apache.log4j.Logger.getLogger(BungeniListDocuments.class.getName());


        final String doc;
        final String url;
        
    ContainerFactory containerFactory = new ContainerFactory() {
        @Override
        public List creatArrayContainer() {
            return new LinkedList();
        }

        @Override
        public Map createObjectContainer() {
            return new LinkedHashMap();
        }
    };

    public BungeniListDocuments(String sListUrl, String responseBody) {
        this.url = sListUrl;
        this.doc = responseBody;
        init();
    }

    public class BungeniListDocument {
        public final String title;
        public final String idBase;
        public final String statusDate;
        public final  String status; 
        
        public BungeniListDocument(String title,  String idBase, String statusDate, String status) {
            this.title = title;
            this.idBase = idBase;
            this.statusDate = statusDate;
            this.status = status;
        }
        
        @Override
        public String toString(){
            return title + " - [ " +  status + " ] " ;
        }
        
    }
    
    List<BungeniListDocument> listDocuments = new ArrayList<BungeniListDocument>(0);
    
    private void init() {
        JSONParser prs = new JSONParser();
        Map json = null;
        try {
           json = (Map) prs.parse(doc, containerFactory);
        } catch (ParseException ex) {
            log.error("Error while parsing json listing !", ex);
        }
        Long recordsReturned = (Long) json.get("recordsReturned");
        LinkedList nodes = (LinkedList) json.get("nodes");
        
        Iterator nodesIter = nodes.iterator();
        /***
         * {"recordsReturned": 1, 
            "start": 0, 
            "nodes": [
            {"status": "received by clerk", 
            "status_date": "Dec 20, 2012 2:22:56 PM", 
            "type": "bill", 
            "object_id": "bill-68/", 
            "title": "Bill - cosignatory - status draft p1_01"}
            ], 
            "length": 1} 
        **/
        while(nodesIter.hasNext()) {
            Map aNode = (Map) nodesIter.next();
            BungeniListDocument aDoc = new BungeniListDocument(  
                    (String) aNode.get("title"),
                    (String) aNode.get("object_id"),
                    (String) aNode.get("status_date"),
                    (String) aNode.get("status")
                    );
            listDocuments.add(aDoc);
        }
        
            
    }
    
    public List<BungeniListDocument> getListDocuments(){
        return this.listDocuments;
    }
}
