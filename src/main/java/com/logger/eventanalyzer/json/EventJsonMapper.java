package com.logger.eventanalyzer.json;

import com.logger.eventanalyzer.event.EventEntry;

import java.util.function.Function;

public interface EventJsonMapper extends Function<String, EventEntry> {

    int getErrorCount();
}
