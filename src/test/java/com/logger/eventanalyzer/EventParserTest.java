package com.logger.eventanalyzer;

import com.logger.eventanalyzer.event.Event;
import com.logger.eventanalyzer.event.LogEntry;
import com.logger.eventanalyzer.event.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventParserTest {

    private EventParser eventParser;

    @BeforeEach
    void setup() {
        eventParser = new EventParser(4L);
    }

    @Test
    void givenSingleEvent_thenReturnNull() {
        LogEntry logEntry = new LogEntry("some-id-1", State.STARTED, null, null, 18888983L);
        Event event = eventParser.apply(logEntry);
        assertThat(event).isNull();
    }

    @Test
    void givenPairedEvent_withSameIds_thenReturnEventSummaryOnSecondCall() {
        eventParser.apply(new LogEntry("some-id-1", State.STARTED, null, null, 18888983L));
        LogEntry logEntry = new LogEntry("some-id-1", State.FINISHED, null, null, 18888984L);
        Event event = eventParser.apply(logEntry);
        assertThat(event).isNotNull();
        assertThat(event.getId()).isEqualTo("some-id-1");
        assertThat(event.getDuration()).isEqualTo(1L);
    }

    @Test
    void givenUnorderedPairedEvent_withSameIds_thenReturnEventSummaryOnSecondCall() {
        eventParser.apply(new LogEntry("some-id-2", State.FINISHED, null, null, 18888984L));
        LogEntry logEntry = new LogEntry("some-id-2", State.STARTED, "APPLICATION_LOG", "unknown", 18888983L);
        Event event = eventParser.apply(logEntry);
        assertThat(event).isNotNull();
        assertThat(event.getId()).isEqualTo("some-id-2");
        assertThat(event.getDuration()).isEqualTo(1L);
        assertThat(event.getType()).isEqualTo("APPLICATION_LOG");
        assertThat(event.getHost()).isEqualTo("unknown");
    }
}
