External libraries used for building the Editor are located here :

http://bungeni-portal.googlecode.com/svn/BungeniOOo/external_libs/

You need to import the library mappings into netbeans so that editor project can recognize the correct location of the libraries, these mappings are located here as XML files:

http://bungeni-portal.googlecode.com/svn/BungeniOOo/external_libs/netbeans_support

After checking out the mappings folder, open a terminal window and switch that folder.
You need to set the absolute path to the path where your external\_libs folder is located.
If your external libs folder is under : /home/undesa/projects/external\_libs

then you need to prefix that path to the library path, do this using the following command

```
find -name '*.xml' -print0 | xargs -0 perl -pi -e 's/lib_path/\/home\/undesa\/projects/g'                                                
```

In the above command, lib\_path is the prefix to be replaced with the actual path.
You also need to set the prefix path for the openoffice libraries (located within the openoffice installation on your computer. To do this run the following command:

```
find -name '*.xml' -print0 | xargs -0 perl -pi -e 's/oo_path/\/opt\/openoffice/g'                                                
```

The above command uses oo\_path as the prefix to replace with the path to the OOo installation under /opt/openoffice.

Finally copy the mapping XML files to your :

> 

&lt;home&gt;

/

&lt;username&gt;

/.netbeans/<version number>/config/org-netbeans-api-project-libraries/Libraries

Now if you restart netbeans the project external library mappings should be correctly set.











Add your content here.


# Details #

Add your content here.  Format your content with:
  * Text in **bold** or _italic_
  * Headings, paragraphs, and lists
  * Automatic links to other wiki pages