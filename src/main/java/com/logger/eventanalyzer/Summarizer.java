package com.logger.eventanalyzer;

import com.logger.eventanalyzer.event.Event;
import com.logger.eventanalyzer.event.EventEntry;
import com.logger.eventanalyzer.event.State;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;

public class Summarizer implements Function<EventEntry, Event> {

    private final ConcurrentSkipListMap<String, EventEntry> unpairedEvents = new ConcurrentSkipListMap<>();
    private long thresholdDuration;

    public Summarizer(long thresholdDuration){
        this.thresholdDuration = thresholdDuration;
    }

    @Override
    public Event apply(EventEntry eventEntry) {
        String id = eventEntry.getId();
        EventEntry unpairedEventEntry = unpairedEvents.get(id);
        if (unpairedEventEntry == null) {
            unpairedEvents.put(id, eventEntry);
            return null;
        } else {
            unpairedEvents.remove(id);
            return createSummary(eventEntry, unpairedEventEntry);
        }
    }

    private Event createSummary(EventEntry eventEntry, EventEntry relatedEventEntry) {
        return (eventEntry.getState() == State.STARTED) ?
                createSummaryFromAlignedPair(eventEntry, relatedEventEntry) :
                createSummaryFromAlignedPair(relatedEventEntry, eventEntry);
    }

    private Event createSummaryFromAlignedPair(EventEntry startEventEntry, EventEntry finishEventEntry) {
        Long duration = finishEventEntry.getTimestamp() - startEventEntry.getTimestamp();
        return new Event(
                startEventEntry.getId(),
                duration,
                startEventEntry.getType(),
                startEventEntry.getHost(),
                duration > thresholdDuration
        );
    }

}
