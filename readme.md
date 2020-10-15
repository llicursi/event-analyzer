#Event Log Analyzer

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