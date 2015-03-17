# Installing the Bungeni Editor on Ubuntu Linux and Windows #



## Introduction ##
These installation instructions were tested on ~~Ubuntu 8.04~~ / Windows XP and the same should apply for subsequent OS releases.  Currently, the Editor works only on windows due to a bug in the Gnome 2 windowing implementation.

## Installing using the IzPack installer ##

  * install the sun jre 6 (see below for installations steps for windows and ubuntu) -- note that the editor has been tested **only** with the Sun jdk / jre release.
  * install openoffice.org 3.3.0 (see below for installations steps for windows and ubuntu). (Note : It will not work with any other version of OpenOffice.org, also the editor has not been tested with other OOo implementations like GoOO)
  * run the izpack installer provided on the downloads page.

## Quick Install Steps for Ubuntu ##

**UPDATE 14-Nov-2011 - EDITOR WILL NOT INSTALL ON UBUNTU GNOME, please try it on KUBUNTU see [https://issues.apache.org/ooo/show\_bug.cgi?id=97007](https://issues.apache.org/ooo/show_bug.cgi?id=97007)**

**NOTE** : Bungeni Editor is not compatible with OpenOffice installation packaged with Ubuntu since it is a different distirbution of OpenOffice called 'LibreOffice'.  Bungeni Editor is only compatible with the OpenOffice.org distribution on the the [OpenOffice.org webiste](http://www.openoffice.org)

If you dont want to bother with reading the detailed installation instructions, the following instructions will setup a working installation 95% of the time :
  * install the sun jre 6 : `sudo apt-get install sun-java6-jre`
  * uninstall the openoffice.org installation packaged with ubuntu
  * download install openoffice by downloading OO 3.3.0 from [http://downloads.openoffice.org](http://www.mirrorservice.org/sites/download.openoffice.org/stable/3.3.0/OOo_3.3.0_Linux_x86_install-deb_en-US.tar.gz)
  * launch openoffice at least once and accept the license agreement
  * unpack the editor application into its own folder
  * run the script linux.sh to start the application


## Quick Install steps for Windows ##

  * download and install openoffice.org from [http://downloads.openoffice.org](http://www.mirrorservice.org/sites/download.openoffice.org/stable/3.3.0/OOo-SDK_3.3.0_Win_x86_install_en-US.exe)
  * download and install the sun java6 jre (from [http://java.sun.com](http://java.sun.com))
  * unpack the editor, and open runEditor.bat in notepad. Edit java\_home and log4j\_properties to appropriate paths.
  * run runEditor.bat to launch the editor.


## Prerequisites ##


### Installing the Java Runtime ###

**OpenJVM / Sun Java JRE** - Ubuntu by default comes packaged with the GCJ open source JRE. The EditorClient will not work on this JRE as it requires the Swing UI libraries that come packaged with the Sun JVM / OpenJVM release. Install the Sun JRE on the Ubuntu box either through package manager or download the RPM package from [http://java.sun.com](http://java.sun.com/). The EditorClient requires at least JRE / JDK version 1.6 +

To install the Sun Java JRE on Ubuntu in the terminal window run the following:
```
sudo apt-get install sun-java6-jre
```

To install the full JDK 1.6 on Ubuntu (useful for running a development instance of the editor)  in the terminal window, run the following :
```
sudo apt-get install sun-java6-jdk
```

For windows, download and install the java 6 JRE using the instructions provided [here](http://java.sun.com/javase/6/webnotes/install/jre/install-windows.html)

### Installing OpenOffice.org ###

**OpenOffice.org 3.3.0** - Ubuntu comes packaged with an OpenOffice release. Unfortunately this OpenOffice installation is a repackaged installation produced by Ubuntu and it changes the folder structure of the OpenOffice installation. This change makes it incompatible with the cross platform UNO class loader used by the BungeniEditor. As a preliminary step, uninstall the packaged OpenOffice provided by ubuntu by uninstalling it using package manager.
Download the OpenOffice 3.3.0 debian package from [http://download.openoffice.org](http://www.mirrorservice.org/sites/download.openoffice.org/stable/3.3.0/OOo_3.3.0_Linux_x86_install-deb_en-US.tar.gz) . Download the OpenOffice installation package and extract it a folder on your computer.

First, uninstall the existing openoffice installation (say 'yes' when it prompts for confirmation)
```
sudo apt-get autoremove openoffice.org*.*
```

Install the package by running the following command from the openoffice-extracted-folder/DEBS path :

```
sudo dpkg -i *.deb
```

Install the desktop integration package :

```
cd desktop-integration
sudo dpkg -i *.deb
```

On windows, download and install openoffice.org 3.3.0 from the [openoffice.org download page](http://download.openoffice.org)

#### Setting Nautilus to appear as the default File->Open dialog ####

Launch Openoffice go to tools->options->OpenOffice.org->General
Uncheck "Use OpenOffice dialogs in file/open dialogs"

#### Enabling TTF fonts in openoffice ####

Open a terminal window and install the core TTF fonts :
```
	sudo apt-get install msttcorefonts
```


#### Installing OpenOffice.org from a RPM package ####

_NOTE: the below steps using Alien are required only if you want to install the openoffice rpm package on ubuntu_

**Alien** - This is required to convert the RPM installer of the JRE / JDK downloaded from the Sun.com website. Install Alien by running the following command from the command line:

```
sudo aptitude install alien
```

If you did not use package manager to install the JRE or JDK you will need to convert the downloaded RPM package to a debian package with the following command:

```
sudo alien -d --scripts <RPM file name>
```

then install the debian package with :

```
sudo dpkg -i *.deb
```


## Installing BungeniEditor Client ##

1) Download the latest release of the Editor Client from:

[BungeniEditor client download](http://code.google.com/p/bungeni-portal/downloads/list?q=label:BungeniEditorClient)

Download the 2 listed files, one is a .jar installer application, the second is .oxt OpenOffice basic extension library.

2) Now launch OpenOffice. Go to Tools->Extensions, click _Add ..._, and select the BungeniLibs\_revXXX.oxt file that you downloaded in step (1), and click _OK_.


3) Close OpenOffice.


4) Right click on the jar file downloaded in step (1) and in Nautilus select open with "sun java runtime 6.0" (in windows explorer double click on the jar file).

5) Follow the on-screen instructions of the installer.

6) In ubuntu : double click on _runEditor.sh_ and select _Run_ to launch the editor.
> In windows : double click on _runEditor.bat_ to launch the editor.

### Logging and error messages ###

The editor logs error and debug messages into the log/logs.txt (path relative to the installation folder).

### Modifying Editor settings ###

The editor settings can be modified by accesing the configuration database. The configuration database is an embedded java database accessible via the web browser.

## Installation Screencast ##

> <a href='http://www.youtube.com/watch?feature=player_embedded&v=EULs0f-kSmo' target='_blank'><img src='http://img.youtube.com/vi/EULs0f-kSmo/0.jpg' width='425' height=344 /></a>

> [Click here for Fullscreen HD video](http://www.youtube.com/watch?v=EULs0f-kSmo&fmt=22)