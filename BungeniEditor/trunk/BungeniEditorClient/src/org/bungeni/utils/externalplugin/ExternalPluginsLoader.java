/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.utils.externalplugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;

/**
 *
 * @author undesa
 */
public class ExternalPluginsLoader {
   
    ArrayList<ExternalPlugin> listExtPlugins = new ArrayList<ExternalPlugin>(0);
    
    public ExternalPluginsLoader(){
           BungeniClientDB instance = new BungeniClientDB (DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
           instance.Connect();
           String actionQuery = SettingsQueryFactory.Q_FETCH_EXTERNAL_PLUGINS();
           QueryResults qr = instance.QueryResults(actionQuery);
           if (qr.hasResults()) {
              Iterator<Vector<String>> resultsIterator = qr.theResultsIterator();
                while (resultsIterator.hasNext()) {
                    Vector<String> resultRow = resultsIterator.next();
                    String pluginName = qr.getField(resultRow, "PLUGIN_NAME");
                    String pluginLoader = qr.getField(resultRow, "PLUGIN_LOADER");
                    String pluginJar = qr.getField(resultRow, "PLUGIN_JAR");
                    String pluginDesc = qr.getField(resultRow, "PLUGIN_DESC");
                    String pluginEnabled = qr.getField(resultRow, "PLUGIN_ENABLED");
                    Integer intEnabled = Integer.parseInt(pluginEnabled);
                    ExternalPlugin ep = new ExternalPlugin(pluginName, pluginJar, pluginLoader, pluginDesc, intEnabled);
                    listExtPlugins.add(ep);
                }
           }        
    }
    
    public final ArrayList<ExternalPlugin> getExternalPlugins(){
        return listExtPlugins;
    }
}
