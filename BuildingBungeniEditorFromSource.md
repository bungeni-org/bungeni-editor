# Pre-requisites #

  * Sun Java 6 JDK
  * Ant
  * OpenOffice.org 3.3.0

# Building from source #

## SVN checkout ##

Create a folder for the editor source, and then check the source out into it :
```
svn co http://bungeni-editor.googlecode.com/svn/BungeniEditor/trunk/BungeniEditorClient 
```

## Ant Build ##

We use ant for building the project.  The build script takes a parameter specifying the path to the openoffice installation :

```
ant -buildfile build-cmdline.xml -Dopenoffice.org.root=/opt/openoffice.org/basis3.3 jar
```

This creates the editor jar file 'BungeniEditorClient.jar' under the dist folder.

## Netbeans Build ##

You can also use netbeans to build / debug the source.

Netbeans build has been tested on Ubuntu 8.04/10.04 and Windows XP (It will **not** work on Mac OS X).

  * Install Netbeans 6.8
  * Install the OpenOffice.org SDK for OpenOffice.org 3.3.0
  * Install the Openoffice.org plugin from the netbeans update center
  * Check out the Bungeni Editor project from svn
  * Open the Bungeni Editor project from within netbeans - and run in debug mode