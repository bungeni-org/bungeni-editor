# Introduction #

The Bungeni Editor supports multiple configurations and allows switching between these configurations at startup.

# Configuration Folder #

The Configuration folder can be anywhere in the system, but must be a folder write-able by the Editor.

Configurations are located based on the information in `settings/configs.xml` , the example below lists two
different configurations for the Editor.

```
<configs>
  <!--
  config/@url - can be either http://file.zip , file:/folder
  -->
  <config name="allTypesDev"
    title="All Types Development"
    url="http://bungeni-editor.googlecode.com/svn/BungeniEditor_configs/alltypesdev.zip"
    folder-base="../../Editor_configs/development_configs"
    receiver="org.bungeni.editor.input.FSDocumentReceiver"
	default="true"
    />
  <config name="Bill-Bungeni"
    title="Bill Bungeni Integration"
    url="http://bungeni-editor.googlecode.com/svn/BungeniEditor_configs/bill-bungeni.zip"
    folder-base="../../Editor_configs/bungeni_configs"
    
    receiver="org.bungeni.editor.input.BungeniDocumentReceiver"
    >
    <custom>
       <bungeni user="clerk.p1_01"
        password="member"
        host="10.0.2.2"
        port="8081"
        loginuri="login" />
    </custom>
    </config>
</configs>

```

The config/@folder-base attribute points to the folder where the configuration is located, and the editor
loads up the configuration at startup. a

![http://bungeni-editor.googlecode.com/files/editor-config-select.png](http://bungeni-editor.googlecode.com/files/editor-config-select.png)

The config folder needs to have a particular structure. Example configurations can be viewed on this URL :

[Bungeni Editor configs](http://bungeni-editor.googlecode.com/svn/BungeniEditor_configs/)

These can be used as a basis for creating your own configs.
