@echo off
echo [INFO] Package the jar in target dir.

cd %~dp0
set "CURRENT=%~dp0"
cd ..
call mvn package -Dmaven.test.skip=true
cd %CURRENT%
pause