set dir=%~dp0
%dir:~,1%:
cd %~dp0

SETLOCAL ENABLEDELAYEDEXPANSION
set CLASSPATH=
FOR %%C IN (lib\*.jar) DO set CLASSPATH=!CLASSPATH!;%%C
echo %CLASSPATH%

java -Dfile.encoding=UTF-8 -cp .;./jar/run_usddi.jar;%CLASSPATH% com.adrninistrator.usddi.runner.RunnerGenUmlSequenceDiagram %1