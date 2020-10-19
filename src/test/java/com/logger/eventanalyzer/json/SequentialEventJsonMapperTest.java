package com.logger.eventanalyzer.json;

import com.logger.eventanalyzer.event.EventEntry;
import com.logger.eventanalyzer.event.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SequentialEventJsonMapperTest {

    private SequentialEventJsonMapper sequentialEventJsonMapper;

    @BeforeEach
    void setup(){
        sequentialEventJsonMapper = new SequentialEventJsonMapper();
    }

    @Test
    void givenValidString_thenReturnEvent() {
        String jsonAsString = "{\"id\":\"random6dd1a7\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1602769319555}";
        EventEntry eventEntry = sequentialEventJsonMapper.apply(jsonAsString);
        assertThat(eventEntry).isNotNull();
        assertThat(eventEntry.getId()).isEqualTo("random6dd1a7");
        assertThat(eventEntry.getHost()).isNull();
        assertThat(eventEntry.getType()).isNull();
        assertThat(eventEntry.getState()).isEqualTo(State.STARTED);
        assertThat(eventEntry.getTimestamp()).isEqualTo(1602769319555L);
    }

    @Test
    void givenValidJsonString_whenMissingFields_thenReturnEvent() {
        String jsonAsString = "{\"id\":\"random6dd1a7\",\"state\":\"FINISHED\",\"timestamp\":1602769319555}";
        EventEntry eventEntry = sequentialEventJsonMapper.apply(jsonAsString);
        assertThat(eventEntry).isNotNull();
        assertThat(eventEntry.getId()).isEqualTo("random6dd1a7");
        assertThat(eventEntry.getHost()).isNull();
        assertThat(eventEntry.getType()).isNull();
        assertThat(eventEntry.getState()).isEqualTo(State.FINISHED);
        assertThat(eventEntry.getTimestamp()).isEqualTo(1602769319555L);
    }

    @Test
    void givenValidJsonString_whenMissingState_thenReturnInvalidEvent() {
        String jsonAsString = "{\"id\":\"random6dd1a7\",\"timestamp\":1602769319555}";
        EventEntry eventEntry = sequentialEventJsonMapper.apply(jsonAsString);
        assertThat(eventEntry).isNotNull();
        assertThat(eventEntry.getId()).isEqualTo("random6dd1a7");
        assertThat(eventEntry.getHost()).isNull();
        assertThat(eventEntry.getType()).isNull();
        assertThat(eventEntry.getState()).isNull();
        assertThat(eventEntry.getTimestamp()).isEqualTo(1602769319555L);
        assertThat(eventEntry.isValid()).isFalse();
    }

    @Test
    void givenUnParsableString_thenReturnNull() {
        String jsonAsString = "\"id\":\"random6dd1a7\"";
        EventEntry eventEntry = sequentialEventJsonMapper.apply(jsonAsString);
        assertThat(eventEntry).isNull();
    }

    @Test
    void givenValidJsonString_thenReturnZeroErrorCount() {
        String jsonAsString = "{\"id\":\"random6dd1a7\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1602769319555}";
        sequentialEventJsonMapper.apply(jsonAsString);
        assertThat(sequentialEventJsonMapper.getErrorCount()).isEqualTo(0);
    }

    @Test
    void givenUnParsableString_thenReturnOneErrorCount() {
        String jsonAsString = "\"id\":\"random6dd1a7\"";
        sequentialEventJsonMapper.apply(jsonAsString);
        assertThat(sequentialEventJsonMapper.getErrorCount()).isEqualTo(1);
    }

    @Test
    void givenValidJsonString_whenWrongState_thenReturnNullAndErrorCount() {
        String jsonAsString = "{\"id\":\"random6dd1a7\",\"state\":\"BANANA\",\"timestamp\":1602769319555}";
        EventEntry eventEntry = sequentialEventJsonMapper.apply(jsonAsString);
        assertThat(eventEntry).isNull();
        assertThat(sequentialEventJsonMapper.getErrorCount()).isEqualTo(1);
    }

    @Test
    void givenUnParsableString_whenProcessedMultipleTimes_thenReturnSameAmountOfError() {
        String jsonAsString = "\"id\":\"random6dd1a7\"";
        sequentialEventJsonMapper.apply(jsonAsString);
        sequentialEventJsonMapper.apply(jsonAsString);
        sequentialEventJsonMapper.apply(jsonAsString);
        sequentialEventJsonMapper.apply(jsonAsString);
        sequentialEventJsonMapper.apply(jsonAsString);
        assertThat(sequentialEventJsonMapper.getErrorCount()).isEqualTo(5);
    }


}
