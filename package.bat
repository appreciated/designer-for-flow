@echo off
net session >nul 2>&1
if %errorLevel% == 0 (
    cd %~dp0
    mvn clean install -Pproduction -DskipTests
    PAUSE
) else (
    echo Please run as admin
    PAUSE
)
