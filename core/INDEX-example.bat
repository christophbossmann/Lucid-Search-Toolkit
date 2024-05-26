@ECHO OFF
java -Dlog4j.configurationFile=log4j2_windows.xml -jar target\lucidsearchtoolkit-core-1.0.0-SNAPSHOT.jar --outputcharset UTF-8 --inputcharset cp850 --mode index --indexpath %cd%/documents/index --docspath %cd%/documents/input --outpath %cd%/documents/output  --numberofresults 50
PAUSE