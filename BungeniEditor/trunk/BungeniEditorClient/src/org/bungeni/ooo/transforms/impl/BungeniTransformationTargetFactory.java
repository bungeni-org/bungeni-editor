/*
 *  Copyright (C) 2012 UN/DESA Africa i-Parliaments Action Plan
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
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

package org.bungeni.ooo.transforms.impl;

import org.bungeni.editor.config.TransformTargetsReader;
import org.jdom.Element;

/**
 *
 * @author Administrator
 */
public class BungeniTransformationTargetFactory {
        private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniTransformationTargetFactory.class.getName());

    /** Creates a new instance of BungeniTransformationTargetFactory */
    public BungeniTransformationTargetFactory() {
    }
    
    public static IBungeniDocTransform getDocTransform(String targetName) {
        IBungeniDocTransform idocTrans = null;
        try {
           BungeniTransformationTarget ttg = getTransformationTarget(targetName);
           idocTrans = getTransformClass(ttg);
        } catch (Exception ex) {
            log.error("getDocTransform : " + ex.getMessage());
        } finally {
            return idocTrans;
        }
    }
    
    public static BungeniTransformationTarget getTransformationTarget(String targetName) {
        BungeniTransformationTarget btTarget = null;
        try {
              Element elemTarget =  TransformTargetsReader.getInstance().getTransformTarget(targetName);
              /**
               *     <transformTarget name="ODT"
               *     desc="OpenDocument File"
               *     extension="odt"
               *     class="org.bungeni.ooo.transforms.loadable.ODTSaveTransform" />
               */
              btTarget = new BungeniTransformationTarget(
                        elemTarget.getAttributeValue("name"),
                        elemTarget.getAttributeValue("desc"),
                        elemTarget.getAttributeValue("extension"),
                        elemTarget.getAttributeValue("class")
                    );
            /***
             BungeniClientDB db =  new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
             db.Connect();
             QueryResults qr = db.QueryResults(SettingsQueryFactory.Q_FETCH_TRANSFORM_TARGETS(targetName));
                if (qr.hasResults()) {
                   Vector<Vector<String>> resultRows  = new Vector<Vector<String>>();
                   resultRows = qr.theResults();
                   String targetExt, targetDesc, targetClass;
                   for (Vector<String> resultRow: resultRows) {
                      // targetName = resultRow.elementAt(qr.getColumnIndex("TARGET_NAME")-1);
                       targetExt = resultRow.elementAt(qr.getColumnIndex("TARGET_EXT")-1);
                       targetDesc = resultRow.elementAt(qr.getColumnIndex("TARGET_DESC")-1);
                       targetClass = resultRow.elementAt(qr.getColumnIndex("TARGET_CLASS")-1);
                       btTarget = new BungeniTransformationTarget(targetName, targetDesc, targetExt, targetClass);
                       break;
                   }
                } **/

        } catch (Exception ex) {
            log.error("getTransformationTarget : " +ex.getMessage());
        }  finally {
            return btTarget;
        }
    }
    public static IBungeniDocTransform getTransformClass(BungeniTransformationTarget aTarget) {
         IBungeniDocTransform aTransform = null;
       try {
           Class transformClass;
             transformClass = Class.forName(aTarget.targetClass);
             aTransform = (IBungeniDocTransform) transformClass.newInstance();

       } catch (ClassNotFoundException ex) {
           log.error("getTransformClass:"+ ex.getMessage());
        } finally {
             return aTransform;
        }
    }


    
}
