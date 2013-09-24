@echo off
echo [INFO] copy the dependencies to lib folder

cd %~dp0
set "CURRENT=%~dp0"
cd ..
mvn dependency:copy-dependencies -DoutputDirectory=%CURRENT%lib
cd %CURRENT%
pause