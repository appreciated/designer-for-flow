@echo off
net session >nul 2>&1
if %errorLevel% == 0 (
    cd %~dp0
    mvn clean install -Pproduction
    ubuntu run dos2unix.exe zip-darwin-windows.sh
    ubuntu run bash zip-darwin-windows.sh
) else (
    echo Please run as admin
    PAUSE
)
