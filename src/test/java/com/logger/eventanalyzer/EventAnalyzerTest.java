package com.logger.eventanalyzer;

import com.logger.eventanalyzer.config.AnalyzerConfig;
import com.logger.eventanalyzer.event.Event;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.logger.eventanalyzer.config.AnalyzerConfigUtil.getParallelAnalyzerConfig;
import static com.logger.eventanalyzer.config.AnalyzerConfigUtil.getSequentialAnalyzerConfig;
import static org.assertj.core.api.Assertions.assertThat;

class EventAnalyzerTest {

    @Test
    void givenSourceWith2Events_whenAllEventsAreValid_thenReturnEventSummary() {
        String[] array = {
                "{\"id\":\"random6dd1a7\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1602769319555}\n",
                "{\"id\":\"random6dd1a7\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1602769319559}\n"};
        assertEventAnalyzerProducedExpectedCount(getSequentialAnalyzerConfig(array), 1);
        assertEventAnalyzerProducedExpectedCount(getParallelAnalyzerConfig(array), 1);
    }

    @Test
    void givenSourceWith3Events_whenAllEventsAreUnpaired_thenReturnZeroSummaries() {
        String[] array = {
                "{\"id\":\"random6dd1a7\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1602769319555}\n",
                "{\"id\":\"random6dd1a4\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1602769319561}\n",
                "{\"id\":\"random6dd1a5\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1602769319559}\n"};
        assertEventAnalyzerProducedExpectedCount(getSequentialAnalyzerConfig(array), 0);
        assertEventAnalyzerProducedExpectedCount(getParallelAnalyzerConfig(array), 0);
    }

    private void assertEventAnalyzerProducedExpectedCount(AnalyzerConfig config, int expectedCount) {
        List<Event> events = new ArrayList<>();
        new EventAnalyzer().analyze(config, events::add);
        assertThat(events.size()).isEqualTo(expectedCount);
    }

    @Test
    void givenSourceWith3Events_whenOneRecordIsInvalid_thenReturnEventSummary() {
        String[] array = {
                "{\"id\":\"random6dd1a7\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1602769319555}\n",
                "some-invalid-record",
                "{\"id\":\"random6dd1a7\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1602769319559}\n"};

        assertEventAnalyzerProducedExpectedCount(getSequentialAnalyzerConfig(array), 1);
        assertEventAnalyzerProducedExpectedCount(getParallelAnalyzerConfig(array), 1);
    }

    @Test
    void givenSourceWith4Events_whenAllEventsAreValid_thenReturn2EventSummary() {
        String[] array = {
                "{\"id\":\"random6dd1a7\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1602769319555}\n",
                "{\"id\":\"random6dd1a8\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1602769319554}\n",
                "{\"id\":\"random6dd1a8\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1602769319553}\n",
                "{\"id\":\"random6dd1a7\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1602769319559}\n"};
        assertEventAnalyzerProducedExpectedCount(getSequentialAnalyzerConfig(array), 2);
        assertEventAnalyzerProducedExpectedCount(getParallelAnalyzerConfig(array), 2);
    }


}
