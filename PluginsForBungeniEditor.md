# Introduction #

The Bungeni Editor supports adding of functionality via plugins. As it is likely that plugins may use the same libraries used by the editor or in some instances different versions of libraries used by the editor -- they are loaded and instantiated using a customized class-loader.

A post-delegation classloader is used to load the plugin and its required libraries. In most cases, if the plugin uses the same version of the library as the editor it is safe to not include the same library for the plugin as the post-delegated classloader uses the instance of the library from the parent (i.e. the Editor's ) classloader.

# Plugin Definition #

## Plugin XML configuration ##

Plugins are described in xml configuration files within the 'plugins' subfolder of the editor installation. The following is an example plugin config file :

```
<plugin name="ConvertToPlain" base="convert_to_plain" >
  <main class="org.bungeni.plugins.convertsection.BungeniSectionConvertToPlain">BungeniSectionPlain.jar</main>
  <required>
	<lib>bungeniodfdom.jar</lib>
	<lib>odfdom.jar</lib>
	<lib>log4j.jar</lib>
  </required>	
</plugin>
```

The description of the individual components of the file is provided below :

  * `<plugin name="ConvertToPlain" base="convert_to_plain" >` - 'name' is the descriptive name of the plugin used by the system ; 'base' is the name of the folder containing the plugin jar files.
  * `<main class="org.bungeni.plugins.convertsection.BungeniSectionConvertToPlain" >BungeniSectionPlain.jar</main>` - 'class' is the entry point class for the plugin ; and the `<main>` element stores the name of the plugin jar file.
  * `<required><lib>......</lib>...` - this contains the name of the required libraries for the plugin and which reside in the `base` folder. If a library from the parent classloader can be used by the plugin, do not include it here.

Plugin configuration files use the following naming scheme : **plugin\_anytexthere.xml**

## Required Interfaces ##

The plugin is required to implement the [IEditorPlugin interface](http://code.google.com/p/bungeni-editor/source/browse/plugins/BungeniEditorPluginInterface/src/org/bungeni/plugins/IEditorPlugin.java).

The plugin methods are invoked by the editor using reflection.