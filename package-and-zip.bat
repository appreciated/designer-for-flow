@echo off
net session >nul 2>&1
setlocal enableDelayedExpansion
if !ERRORLEVEL! EQU 0 (
    cd %~dp0
    cd src\main\resources
    dir /b /a-d "i18n_de.properties" | find "i18n_de.properties" >nul
    if !ERRORLEVEL! EQU 0 (
      cd ../../..
      mvn clean install -Pproduction
      ubuntu2004 run dos2unix.exe zip-darwin-windows.sh
      ubuntu2004 run bash zip-darwin-windows.sh
    ) else (
      echo "i18n_de.properties" does not exist  
    )
) else (
    echo Please run as admin
)
PAUSE