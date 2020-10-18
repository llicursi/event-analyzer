package com.logger.eventanalyzer.json;

import com.logger.eventanalyzer.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SequentialEventJsonMapperTest {

    private SequentialEventJsonMapper parallelJsonMapper;

    @BeforeEach
    void setup(){
        parallelJsonMapper = new SequentialEventJsonMapper();
    }

    @Test
    void givenValidString_thenReturnEvent() {
        String jsonAsString = "{\"id\":\"random6dd1a7\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1602769319555}";
        Event event = parallelJsonMapper.apply(jsonAsString);
        assertThat(event).isNotNull();
        assertThat(event.getId()).isEqualTo("random6dd1a7");
        assertThat(event.getHost()).isNull();
        assertThat(event.getType()).isNull();
        assertThat(event.getState()).isEqualTo("STARTED");
        assertThat(event.getTimestamp()).isEqualTo(1602769319555L);
    }

    @Test
    void givenValidJsonString_whenMissingFields_thenReturnEvent() {
        String jsonAsString = "{\"id\":\"random6dd1a7\",\"state\":\"STARTED\",\"timestamp\":1602769319555}";
        Event event = parallelJsonMapper.apply(jsonAsString);
        assertThat(event).isNotNull();
        assertThat(event.getId()).isEqualTo("random6dd1a7");
        assertThat(event.getHost()).isNull();
        assertThat(event.getType()).isNull();
        assertThat(event.getState()).isEqualTo("STARTED");
        assertThat(event.getTimestamp()).isEqualTo(1602769319555L);
    }

    @Test
    void givenUnParsableString_thenReturnNull() {
        String jsonAsString = "\"id\":\"random6dd1a7\"";
        Event event = parallelJsonMapper.apply(jsonAsString);
        assertThat(event).isNull();
    }

    @Test
    void givenValidJsonString_thenReturnZeroErrorCount() {
        String jsonAsString = "{\"id\":\"random6dd1a7\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1602769319555}";
        parallelJsonMapper.apply(jsonAsString);
        assertThat(parallelJsonMapper.getErrorCount()).isEqualTo(0);
    }

    @Test
    void givenUnParsableString_thenReturnOneErrorCount() {
        String jsonAsString = "\"id\":\"random6dd1a7\"";
        parallelJsonMapper.apply(jsonAsString);
        assertThat(parallelJsonMapper.getErrorCount()).isEqualTo(1);
    }

    @Test
    void givenUnParsableString_whenProcessedMultipleTimes_thenReturnSameAmountOfError() {
        String jsonAsString = "\"id\":\"random6dd1a7\"";
        parallelJsonMapper.apply(jsonAsString);
        parallelJsonMapper.apply(jsonAsString);
        parallelJsonMapper.apply(jsonAsString);
        parallelJsonMapper.apply(jsonAsString);
        parallelJsonMapper.apply(jsonAsString);
        assertThat(parallelJsonMapper.getErrorCount()).isEqualTo(5);
    }


}
