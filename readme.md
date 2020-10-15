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
To execute and process a series of event logs run the following command;
```
java -jar event-logger-1.0.jar <FILE> [--parallel]
```