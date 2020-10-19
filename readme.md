# Event Log Analyzer
[![travis](https://api.travis-ci.org/llicursi/eventEntry-analyzer.svg?branch=main)](https://travis-ci.org/llicursi/eventEntry-analyzer)
[![codecov](https://codecov.io/gh/llicursi/eventEntry-analyzer/branch/main/graph/badge.svg)](https://codecov.io/gh/llicursi/eventEntry-analyzer)    
This project analyzes log files with several events occurrences and persists events with duration longer than 4 ms.

## Compile
```
./gradlew build
```
Assembled jar will be generated on the standard folder `build/libs`

## Run 
To execute and process a series of eventEntry logs run the following command:
```
java -jar build/libs/event-analyzer-0.0.1.jar <FILE> [--parallel]
```
