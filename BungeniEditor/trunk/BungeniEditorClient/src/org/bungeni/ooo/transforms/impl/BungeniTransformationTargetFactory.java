/*
 * BungeniTransformationTargetFactory.java
 *
 * Created on June 3, 2008, 4:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.ooo.transforms.impl;

import java.util.Vector;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
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
