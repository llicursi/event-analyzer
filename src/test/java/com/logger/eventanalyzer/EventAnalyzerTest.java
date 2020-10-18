package com.logger.eventanalyzer;

import com.logger.eventanalyzer.event.EventSummary;
import com.logger.eventanalyzer.source.StringSourceStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EventAnalyzerTest {

    private EventAnalyzer eventAnalyzer;

    @BeforeEach
    void setup() {
        eventAnalyzer = new EventAnalyzer();
    }

    @Test
    void givenSourceWith2Events_whenAllEventsAreValid_thenReturnEventSummary() {
        String[] array = {
                "{\"id\":\"random6dd1a7\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1602769319555}\n",
                "{\"id\":\"random6dd1a7\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1602769319559}\n"};
        List<EventSummary> events = eventAnalyzer.analyze(new StringSourceStream(array));
        assertThat(events.size()).isEqualTo(1);
    }

    @Test
    void givenSourceWith3Events_whenAllEventsAreUnpaired_thenReturnZeroSummaries() {
        String[] array = {
                "{\"id\":\"random6dd1a7\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1602769319555}\n",
                "{\"id\":\"random6dd1a4\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1602769319561}\n",
                "{\"id\":\"random6dd1a5\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1602769319559}\n"};
        List<EventSummary> events = eventAnalyzer.analyze(new StringSourceStream(array));
        assertThat(events.size()).isEqualTo(0);
    }

    @Test
    void givenSourceWith3Events_whenOneRecordIsInvalid_thenReturnEventSummary() {
        String[] array = {
                "{\"id\":\"random6dd1a7\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1602769319555}\n",
                "some-invalid-record",
                "{\"id\":\"random6dd1a7\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1602769319559}\n"};
        List<EventSummary> events = eventAnalyzer.analyze(new StringSourceStream(array));
        assertThat(events.size()).isEqualTo(1);
    }
}
