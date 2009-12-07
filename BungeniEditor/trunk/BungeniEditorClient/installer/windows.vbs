Set WshShell=WScript.CreateObject("WScript.Shell")
obj = WshShell.Run(".\scripts\windows.bat",  0 )
set WshShell = Nothing
