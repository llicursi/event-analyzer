package com.logger.eventanalyzer;

import com.logger.eventanalyzer.event.Event;
import com.logger.eventanalyzer.event.EventSummary;
import com.logger.eventanalyzer.event.State;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;

public class Summarizer implements Function<Event, EventSummary> {

    private final ConcurrentSkipListMap<String, Event> unpairedEvents = new ConcurrentSkipListMap<>();

    @Override
    public EventSummary apply(Event event) {
        String id = event.getId();
        Event unpairedEvent = unpairedEvents.get(id);
        if (unpairedEvent == null) {
            unpairedEvents.put(id, event);
            return null;
        } else {
            unpairedEvents.remove(id);
            return createSummary(event, unpairedEvent);
        }
    }

    private EventSummary createSummary(Event event, Event relatedEvent) {
        return (event.getState() == State.STARTED) ?
                createSummaryFromAlignedPair(event, relatedEvent) :
                createSummaryFromAlignedPair(relatedEvent, event);
    }

    private EventSummary createSummaryFromAlignedPair(Event startEvent, Event finishEvent) {
        Long duration = finishEvent.getTimestamp() - startEvent.getTimestamp();
        return new EventSummary(
                startEvent.getId(),
                duration,
                startEvent.getTimestamp(),
                startEvent.getType(),
                startEvent.getHost()
        );
    }

}
