# Introduction #

This page documents how to package and distribute the Editor in a installer package.

  * You will need to download and install [http://www.izpack.org](IzPack.md). Currently IzPack 4.x series is supported.
  * You will also need a developer install of Bungeni Editor, which is upto date and has been successfully built

# Preparing #

Within the IzPack installation folder :
  * create a folder called `bungeni` ;
    * Go to the Editor's source folder and copy the contents of the `installer` folder into `bungeni` folder
    * within `bungeni` create a folder called `base` ;
      * Go to the Editor's `dist` folder and copy its contents into the `base` folder
    * within `bungeni` create a folder called `editor_workspace`.
      * leave this folder empty
    * within `bungeni` create a folder called `editor_configs`
      * Go to your config folder and copy its contents into the `editor_configs` folder


At this point the folder structure within IzPack should look like :

```
+---base
|   +---lib
|   +---logs
|   +---plugins
|   +---settings
|   +---tmp
|   +---transformer
+---editor_configs
|   +---actions
|   +---backgrounds
|   +---bundles
|   +---custom
|   +---inline_types
|   +---locales
|   +---metadata
|   +---panels
|   +---section_types
|   +---system
|   +---templates
|   \---transform
+---editor_workspace
+---izpack_tmpl
+---linux
+---scripts
\---win
```

Edit the install.xml to set the suitable application version number, modify the messages as desired.

# Building the Package #

Open a windows command prompt.

Cd to the `IzPack\bungeni` folder.
Run the following command :
```
..\bin\compile install.xml
```

This will generate an `install.jar`.

# Testing #

Run the install.jar, it will prompt you for a target directory
and also make you select directories for the configuration and the workspace.

(Note: the editor config and workspace folders need to be in a user writable directory)

Once installed, run `windows.vbs` from the target folder where you installed it.