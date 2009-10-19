Set WshShell=WScript.CreateObject("WScript.Shell")
obj = WshShell.Run(".\win\windows.bat",  0 )
set WshShell = Nothing
