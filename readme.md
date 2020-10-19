# Event Log Analyzer
[![travis](https://api.travis-ci.org/llicursi/event-analyzer.svg?branch=main)](https://travis-ci.org/llicursi/event-analyzer)
[![codecov](https://codecov.io/gh/llicursi/event-analyzer/branch/main/graph/badge.svg)](https://codecov.io/gh/llicursi/event-analyzer)    
This project analyzes log files with several events occurrences and persists events with duration longer than 4 ms.

## Compile
```
./gradlew build
```
Assembled jar will be generated on the standard folder `build/libs`

## Run 
To execute and process a series of event logs run the following command:
```
java -jar build/libs/event-analyzer-0.0.1.jar <FILE> [--parallel]
```
Optionally you can use `--parallel` to process large files faster, by using 
multiple threads based on `number of processors - 1`.

### Expected output
   
    Analyzing events from : path-to-file/test-file-10.json
    Execution completed in 504 ms
    Alert report generated at :
    	out/reports/event-alerts.log
    	
### Generated reports
All reports shall be generated at `out/repors/event-alerts.log` and rotated 
to a limited size (*100MB*). With default format :

    Mon Oct 19 23:04:17 CEST 2020
    Inspecting events with duration bigger than 4 ms
    ===================================================
    Event randoma4a9b5 has duration of 7 ms  