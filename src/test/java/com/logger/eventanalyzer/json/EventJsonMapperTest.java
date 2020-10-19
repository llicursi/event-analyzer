package com.logger.eventanalyzer.json;

import com.logger.eventanalyzer.event.EventEntry;
import com.logger.eventanalyzer.event.State;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class EventJsonMapperTest {

    @ParameterizedTest
    @MethodSource("eventJsonMapperGenerator")
    void givenValidString_thenReturnEvent(EventJsonMapper eventJsonMapper) {
        String jsonAsString = "{\"id\":\"random6dd1a7\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1602769319555}";
        EventEntry eventEntry = eventJsonMapper.apply(jsonAsString);
        assertThat(eventEntry).isNotNull();
        assertThat(eventEntry.getId()).isEqualTo("random6dd1a7");
        assertThat(eventEntry.getHost()).isNull();
        assertThat(eventEntry.getType()).isNull();
        assertThat(eventEntry.getState()).isEqualTo(State.STARTED);
        assertThat(eventEntry.getTimestamp()).isEqualTo(1602769319555L);
    }

    @ParameterizedTest
    @MethodSource("eventJsonMapperGenerator")
    void givenValidJsonString_whenMissingFields_thenReturnEvent(EventJsonMapper eventJsonMapper) {
        String jsonAsString = "{\"id\":\"random6dd1a7\",\"state\":\"FINISHED\",\"timestamp\":1602769319555}";
        EventEntry eventEntry = eventJsonMapper.apply(jsonAsString);
        assertThat(eventEntry).isNotNull();
        assertThat(eventEntry.getId()).isEqualTo("random6dd1a7");
        assertThat(eventEntry.getHost()).isNull();
        assertThat(eventEntry.getType()).isNull();
        assertThat(eventEntry.getState()).isEqualTo(State.FINISHED);
        assertThat(eventEntry.getTimestamp()).isEqualTo(1602769319555L);
    }

    @ParameterizedTest
    @MethodSource("eventJsonMapperGenerator")
    void givenValidJsonString_whenMissingState_thenReturnInvalidEvent(EventJsonMapper eventJsonMapper) {
        String jsonAsString = "{\"id\":\"random6dd1a7\",\"timestamp\":1602769319555}";
        EventEntry eventEntry = eventJsonMapper.apply(jsonAsString);
        assertThat(eventEntry).isNotNull();
        assertThat(eventEntry.getId()).isEqualTo("random6dd1a7");
        assertThat(eventEntry.getHost()).isNull();
        assertThat(eventEntry.getType()).isNull();
        assertThat(eventEntry.getState()).isNull();
        assertThat(eventEntry.getTimestamp()).isEqualTo(1602769319555L);
        assertThat(eventEntry.isValid()).isFalse();
    }


    @ParameterizedTest
    @MethodSource("eventJsonMapperGenerator")
    void givenUnParsableString_thenReturnNull(EventJsonMapper eventJsonMapper) {
        String jsonAsString = "\"id\":\"random6dd1a7\"";
        EventEntry eventEntry = eventJsonMapper.apply(jsonAsString);
        assertThat(eventEntry).isNull();
    }

    @ParameterizedTest
    @MethodSource("eventJsonMapperGenerator")
    void givenValidJsonString_thenReturnZeroErrorCount(EventJsonMapper eventJsonMapper) {
        String jsonAsString = "{\"id\":\"random6dd1a7\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1602769319555}";
        eventJsonMapper.apply(jsonAsString);
        assertThat(eventJsonMapper.getErrorCount()).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("eventJsonMapperGenerator")
    void givenUnParsableString_thenReturnOneErrorCount(EventJsonMapper eventJsonMapper) {
        String jsonAsString = "\"id\":\"random6dd1a7\"";
        eventJsonMapper.apply(jsonAsString);
        assertThat(eventJsonMapper.getErrorCount()).isEqualTo(1);
    }

    @ParameterizedTest
    @MethodSource("eventJsonMapperGenerator")
    void givenValidJsonString_whenWrongState_thenReturnNullAndErrorCount(EventJsonMapper eventJsonMapper) {
        String jsonAsString = "{\"id\":\"random6dd1a7\",\"state\":\"BANANA\",\"timestamp\":1602769319555}";
        EventEntry eventEntry = eventJsonMapper.apply(jsonAsString);
        assertThat(eventEntry).isNull();
        assertThat(eventJsonMapper.getErrorCount()).isEqualTo(1);
    }

    @ParameterizedTest
    @MethodSource("eventJsonMapperGenerator")
    void givenUnParsableString_whenProcessedMultipleTimes_thenReturnSameAmountOfError(EventJsonMapper eventJsonMapper) {
        String jsonAsString = "\"id\":\"random6dd1a7\"";
        eventJsonMapper.apply(jsonAsString);
        eventJsonMapper.apply(jsonAsString);
        eventJsonMapper.apply(jsonAsString);
        eventJsonMapper.apply(jsonAsString);
        eventJsonMapper.apply(jsonAsString);
        assertThat(eventJsonMapper.getErrorCount()).isEqualTo(5);
    }

    private static Stream<Arguments> eventJsonMapperGenerator() {

        return Stream.of(
                Arguments.of(new SequentialEventJsonMapper()),
                Arguments.of(new ParallelEventJsonMapper()));
    }

}
