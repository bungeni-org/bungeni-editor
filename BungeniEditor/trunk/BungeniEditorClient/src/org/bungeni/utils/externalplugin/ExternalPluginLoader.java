package org.bungeni.utils.externalplugin;

//~--- non-JDK imports --------------------------------------------------------

import java.net.MalformedURLException;
import org.bungeni.editor.config.BungeniEditorProperties;

import org.jdom.input.SAXBuilder;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import org.bungeni.extutils.CommonEditorFunctions;
import org.bungeni.extutils.CommonXmlConfigParams;

/**
 * Loads external plugins from the file system
 * @author Ashok Hariharan
 */
public class ExternalPluginLoader {
    private static org.apache.log4j.Logger log =
        org.apache.log4j.Logger.getLogger(ExternalPluginLoader.class.getName());
    String     pathToPluginsRoot = "";
    SAXBuilder pluginBuilder     = null;

    /**
     * Static plugins cache -- load once - call multiple times philosophy
     */
    private static HashMap<String, ExternalPlugin> externalPluginCache = new HashMap<String,ExternalPlugin>();

    public ExternalPluginLoader() {
        pathToPluginsRoot = CommonEditorFunctions.getPathRelativeToRoot(BungeniEditorProperties.getEditorProperty("pluginsPath"));
        pluginBuilder     = new SAXBuilder(CommonXmlConfigParams.SAX_PARSER_DRIVER);
    }

    /**
     * Finds a plugin and its configuration file in the file system, called by loadPlugin()
     * @param pluginName
     * @return
     */
    public ExternalPlugin findPlugin(String pluginName) {
        ExternalPlugin foundPlugin = null;

        try {
            File fPluginDir = new File(pathToPluginsRoot);

            if (fPluginDir.isDirectory()) {
                File[] pluginConfigFiles = fPluginDir.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        if (name.startsWith("plugin_") && name.endsWith(".xml")) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                ExternalPluginParser ep = new ExternalPluginParser();

                for (File fPluginFile : pluginConfigFiles) {
                    if (ep.parsePluginFile(fPluginFile.getAbsolutePath())) {
                        String foundPluginName = ep.getPluginName();

                        if (foundPluginName.equals(pluginName)) {
                            foundPlugin = ep.getPluginObject();

                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error("findPlugin : " + ex.getMessage());
        } finally {
            return foundPlugin;
        }
    }

    /**
     * Builds an array of dependent jar files for the plugin
     * @param foundPlugin
     * @return
     */
    private ArrayList<URL> buildUrlListForPluginClassLoader (ExternalPlugin foundPlugin) {
        ArrayList<URL> urlList = new ArrayList<URL>(0);
        try {
            String pluginBaseFolder = this.pathToPluginsRoot + File.separator + foundPlugin.PluginBaseFolder + File.separator;
            File fpluginBase = new File(pluginBaseFolder);
            if (fpluginBase.exists()) {
                if (fpluginBase.isDirectory()) {
                    String pathToJar = pluginBaseFolder + foundPlugin.getJarFile();
                    URL pluginBaseJarURL = (new File(pathToJar )).toURI().toURL();
                   // String pluginBasePath = pluginBaseURL.toString();
                    urlList.add(pluginBaseJarURL);
                    for (String depJar : foundPlugin.getDependentJars()) {
                        //replace the file separator char with the platform independent separator char
                        File fDepPlugin = new File(pluginBaseFolder + depJar);
                        URL depURL = fDepPlugin.toURI().toURL();
                        urlList.add(depURL);
                    }
                } else {
                    log.error ("buildUrlListForPluginClassLoader : plugin base folder is NOT a folder");
                }
            } else {
                log.error("buildUrlListForPluginClassLoader : plugin base folder does not exist");
            }
        } catch (MalformedURLException ex) {
            log.error("buildUrlListForPluginClassLoader : " + ex.getMessage());
        } finally {
            return urlList;
        }
    }

    /**
     * Checks if the plugin has already been loaded into a static cache.
     * If it is in the cache returns the plugin from the cache, otherwise loads the plugin adds it to the cache and returns the newly instantiated plugin object
     * @param pluginName
     * @return
     */
    public ExternalPlugin loadPlugin(String pluginName) {
        ExternalPlugin aPlugin = null;
        try {
            if (!externalPluginCache.containsKey(pluginName)) {
                //find the plugin by name
                ExternalPlugin foundPlugin = findPlugin(pluginName);
                if (foundPlugin != null) {
                        //if found build the urls for the URL class loader
                         ArrayList<URL> pluginLibsClassPath = buildUrlListForPluginClassLoader(foundPlugin);
                         //build the classloader for the plugin
                         PostDelegationClassLoader classLoader = new PostDelegationClassLoader(pluginLibsClassPath.toArray(new URL[pluginLibsClassPath.size()]));
                         //instantiate the plugin
                         foundPlugin.setPluginClassLoader(classLoader);
                         foundPlugin.instantiatePlugin();
                         externalPluginCache.put(pluginName, foundPlugin);
                         aPlugin = externalPluginCache.get(pluginName);
                    } else {
                    log.error("loadPlugin : " + pluginName + " was not found");
                    }
            } else {
                aPlugin = externalPluginCache.get(pluginName);
            }
        } catch (Exception ex) {
          log.error("loadPlugin error : " + ex.getMessage());
        } finally {
            return aPlugin;
        }
    }

    /*
     *  URL url = (new File(System.getProperty("user.dir") + File.separator + "plugins/convert_to_plain/")).toURI().toURL();
     * URL[] classpath = new URL[]{
     *    new URL(url.toString() + "BungeniSectionPlain.jar"),
     *    new URL(url.toString() + "bungeniodfdom.jar"),
     *    new URL(url.toString() + "odfdom.jar"),
     *    new URL(url.toString() + "log4j.jar"),
     * //new URL(url.toString() + "xerces.jar")
     * };
     * PostDelegationClassLoader classLoader = new PostDelegationClassLoader(classpath);
     * Class convPlain = classLoader.loadClass("org.bungeni.plugins.convertsection.BungeniSectionConvertToPlain");
     * Class[] convCtorParams = {};
     * Constructor convPlainCtor = convPlain.getConstructor(convCtorParams);
     * Object convPlainObj = convPlainCtor.newInstance();
     *
     * Class[] mSetParamParams = {HashMap.class};
     * Method mSetParam = convPlain.getDeclaredMethod("setParams", mSetParamParams);
     * HashMap arg = new HashMap() {
     *    {
     *        put("OdfFileURL", ooDocument.getDocumentURL());
     *    }
     * };
     * Object[] mSetParamArgs = {arg};
     * mSetParam.invoke(convPlainObj, mSetParamArgs);
     * Class[] mExecParams = {};
     * Method mExec = convPlain.getDeclaredMethod("exec", mExecParams);
     * Object[] mExecParamArgs = {};
     * Object retValue = mExec.invoke(convPlainObj, mExecParamArgs);
     * if (retValue != null) {
     *    String outputFilePath = (String) retValue;
     *    MessageBox.OK(parentFrame, "A plain document was generated, it can be found at : \n" + outputFilePath, "Plain Document generation", JOptionPane.INFORMATION_MESSAGE);
     * }
     */
}

