package com.logger.eventanalyzer;

import com.logger.eventanalyzer.source.StringSourceStream;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventAnalyzerTest {


    @Test
    void givenSource_whenAllEventsAreValid_thenReturnEventSummary() {
        String[] array = {
                "{\"id\":\"random6dd1a7\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1602769319555}\n",
                "{\"id\":\"random6dd1a7\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1602769319559}\n"};
        EventAnalyzer eventAnalyzer = new EventAnalyzer();
        eventAnalyzer.analyze(new StringSourceStream(array));

    }
}
