# Introduction #

BungeniEditor uses openoffice templates for each document type (bill, debaterecord etc.). These templates contain the root section for the document type and also the named styles used for headings


# Customizing Templates #

The templates can be found under :

workspace/templates

e.g. the template for bills is bill.ott, the template for debaterecords is hansard.ott

You can edit the templates by opening them in Openoffice writer. Go to File->Templates->Edit and browse to the template to edit.

# Template registration #

The template is associated with the content type via a settings parameter. This needs to be done just once for each template.

To view / edit the settings parameter open the settings editor by launching settingsEditor.sh from the root folder of your installation.

Log in to the settings UI using the default password.

Click DOCUMENT\_TYPES in left hand panel. Then click 'Run' in the right hand panel, and then click edit on the displayed table listing.

The parameter for the template is called TEMPLATE\_PATH.

Also see RegisteringNewDocTypeForBungeniEditor