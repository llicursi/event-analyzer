package com.logger.eventanalyzer;

import com.logger.eventanalyzer.event.Event;
import com.logger.eventanalyzer.event.LogEntry;
import com.logger.eventanalyzer.event.State;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;

public class EventParser implements Function<LogEntry, Event> {

    private final ConcurrentSkipListMap<String, LogEntry> unpairedLogEntries = new ConcurrentSkipListMap<>();
    private long thresholdDuration;
    private static final Event TO_BE_REMOVED = null;

    public EventParser(long thresholdDuration){
        this.thresholdDuration = thresholdDuration;
    }

    @Override
    public Event apply(LogEntry logEntry) {
        String id = logEntry.getId();
        if (!unpairedLogEntries.containsKey(id)) {
            unpairedLogEntries.put(id, logEntry);
            return TO_BE_REMOVED;
        } else {
            return createSummary(logEntry, unpairedLogEntries.remove(id));
        }
    }

    private Event createSummary(LogEntry logEntry, LogEntry relatedLogEntry) {
        return (logEntry.getState() == State.STARTED) ?
                createEventFromAlignedPair(logEntry, relatedLogEntry) :
                createEventFromAlignedPair(relatedLogEntry, logEntry);
    }

    private Event createEventFromAlignedPair(LogEntry startLogEntry, LogEntry finishLogEntry) {
        Long duration = finishLogEntry.getTimestamp() - startLogEntry.getTimestamp();
        return new Event(
                startLogEntry.getId(),
                duration,
                startLogEntry.getType(),
                startLogEntry.getHost(),
                duration > thresholdDuration
        );
    }

}
