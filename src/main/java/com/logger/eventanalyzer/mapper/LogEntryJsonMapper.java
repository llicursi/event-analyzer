package com.logger.eventanalyzer.mapper;

import com.logger.eventanalyzer.event.LogEntry;

import java.util.function.Function;

public interface LogEntryJsonMapper extends Function<String, LogEntry> {

    int getErrorCount();
}
