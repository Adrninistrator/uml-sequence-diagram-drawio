set dir=%~dp0
%dir:~,1%:
cd %~dp0

java -Dfile.encoding=UTF-8 -cp .;./jar/run_usddi.jar com.adrninistrator.usddi.runner.RunnerGenUmlSequenceDiagram %1