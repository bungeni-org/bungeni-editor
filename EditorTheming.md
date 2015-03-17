

# Introduction #
The UI of the editor is fully “skinnable”. The Bungeni Editor uses a skinning engine called [“Substance” https://substance.dev.java.net/] - there are various default skins provided, and can be switched via a configuration file.
Additional themes can be created using the Java Look-and-Feel API.

# Changing the Active theme #

The active theme can be changed by editing the bungenitheme.properties configuration file in the 'settings' folder under the main installation folder  and changing the 'DefaultTheme' property :
```
# org.bungeni.editor.themes.BusinessBlackSteelLAF
# org.bungeni.editor.themes.BusinessBlueSteelLAF
# org.bungeni.editor.themes.BusinessLAF
# org.bungeni.editor.themes.CafeCremeLAF
# org.bungeni.editor.themes.CremeLAF
# org.bungeni.editor.themes.NebulaLAF
# org.bungeni.editor.themes.NebulaBrickWallLAF
# org.bungeni.editor.themes.SaharaLAF
# org.bungeni.editor.themes.MotifLAF
# org.bungeni.editor.themes.ModerateLAF
# org.bungeni.editor.themes.GtkLAF
# org.bungeni.editor.themes.OfficeBlue2007LAF
# org.bungeni.editor.themes.AutumnLAF
# org.bungeni.editor.themes.MagmaLAF
#
# You can change the default theme by setting it to 
# one of the themes specified above
####################################################
DefaultTheme = org.bungeni.editor.themes.SaharaLAF
```