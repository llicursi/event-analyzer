package com.logger.eventanalyzer;

import com.logger.eventanalyzer.Summarizer;
import com.logger.eventanalyzer.event.Event;
import com.logger.eventanalyzer.event.EventSummary;
import com.logger.eventanalyzer.event.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SummarizerTest {

    private Summarizer summarizer;

    @BeforeEach
    void setup() {
        summarizer = new Summarizer();
    }

    @Test
    void givenSingleEvent_thenReturnNull() {
        Event event = new Event("some-id-1", State.STARTED, null, null, 18888983L);
        EventSummary eventSummary = summarizer.apply(event);
        assertThat(eventSummary).isNull();
    }

    @Test
    void givenPairedEvent_withSameIds_thenReturnEventSummaryOnSecondCall() {
        summarizer.apply(new Event("some-id-1", State.STARTED, null, null, 18888983L));
        Event event = new Event("some-id-1", State.FINISHED, null, null, 18888984L);
        EventSummary eventSummary = summarizer.apply(event);
        assertThat(eventSummary).isNotNull();
        assertThat(eventSummary.getId()).isEqualTo("some-id-1");
        assertThat(eventSummary.getStartTime()).isEqualTo(18888983L);
        assertThat(eventSummary.getDuration()).isEqualTo(1L);
    }

    @Test
    void givenUnorderedPairedEvent_withSameIds_thenReturnEventSummaryOnSecondCall() {
        summarizer.apply(new Event("some-id-2", State.FINISHED, null, null, 18888984L));
        Event event = new Event("some-id-2", State.STARTED, "APPLICATION_LOG", "unknown", 18888983L);
        EventSummary eventSummary = summarizer.apply(event);
        assertThat(eventSummary).isNotNull();
        assertThat(eventSummary.getId()).isEqualTo("some-id-2");
        assertThat(eventSummary.getStartTime()).isEqualTo(18888983L);
        assertThat(eventSummary.getDuration()).isEqualTo(1L);
        assertThat(eventSummary.getType()).isEqualTo("APPLICATION_LOG");
        assertThat(eventSummary.getHost()).isEqualTo("unknown");
    }
}
