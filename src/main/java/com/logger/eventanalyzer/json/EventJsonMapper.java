package com.logger.eventanalyzer.json;

import com.logger.eventanalyzer.event.Event;

import java.util.function.Function;

public interface EventJsonMapper extends Function<String, Event> {

    int getErrorCount();
}
