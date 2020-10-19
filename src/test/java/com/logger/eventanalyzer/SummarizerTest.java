package com.logger.eventanalyzer;

import com.logger.eventanalyzer.event.Event;
import com.logger.eventanalyzer.event.EventEntry;
import com.logger.eventanalyzer.event.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SummarizerTest {

    private Summarizer summarizer;

    @BeforeEach
    void setup() {
        summarizer = new Summarizer(4L);
    }

    @Test
    void givenSingleEvent_thenReturnNull() {
        EventEntry eventEntry = new EventEntry("some-id-1", State.STARTED, null, null, 18888983L);
        Event event = summarizer.apply(eventEntry);
        assertThat(event).isNull();
    }

    @Test
    void givenPairedEvent_withSameIds_thenReturnEventSummaryOnSecondCall() {
        summarizer.apply(new EventEntry("some-id-1", State.STARTED, null, null, 18888983L));
        EventEntry eventEntry = new EventEntry("some-id-1", State.FINISHED, null, null, 18888984L);
        Event event = summarizer.apply(eventEntry);
        assertThat(event).isNotNull();
        assertThat(event.getId()).isEqualTo("some-id-1");
        assertThat(event.getDuration()).isEqualTo(1L);
    }

    @Test
    void givenUnorderedPairedEvent_withSameIds_thenReturnEventSummaryOnSecondCall() {
        summarizer.apply(new EventEntry("some-id-2", State.FINISHED, null, null, 18888984L));
        EventEntry eventEntry = new EventEntry("some-id-2", State.STARTED, "APPLICATION_LOG", "unknown", 18888983L);
        Event event = summarizer.apply(eventEntry);
        assertThat(event).isNotNull();
        assertThat(event.getId()).isEqualTo("some-id-2");
        assertThat(event.getDuration()).isEqualTo(1L);
        assertThat(event.getType()).isEqualTo("APPLICATION_LOG");
        assertThat(event.getHost()).isEqualTo("unknown");
    }
}
