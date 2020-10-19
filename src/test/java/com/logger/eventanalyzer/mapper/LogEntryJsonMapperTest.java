package com.logger.eventanalyzer.mapper;

import com.logger.eventanalyzer.event.LogEntry;
import com.logger.eventanalyzer.event.State;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LogEntryJsonMapperTest {

    @ParameterizedTest
    @MethodSource("eventJsonMapperGenerator")
    void givenValidString_thenReturnEvent(LogEntryJsonMapper logEntryJsonMapper) {
        String jsonAsString = "{\"id\":\"random6dd1a7\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1602769319555}";
        LogEntry logEntry = logEntryJsonMapper.apply(jsonAsString);
        assertThat(logEntry).isNotNull();
        assertThat(logEntry.getId()).isEqualTo("random6dd1a7");
        assertThat(logEntry.getHost()).isNull();
        assertThat(logEntry.getType()).isNull();
        assertThat(logEntry.getState()).isEqualTo(State.STARTED);
        assertThat(logEntry.getTimestamp()).isEqualTo(1602769319555L);
    }

    @ParameterizedTest
    @MethodSource("eventJsonMapperGenerator")
    void givenValidJsonString_whenMissingFields_thenReturnEvent(LogEntryJsonMapper logEntryJsonMapper) {
        String jsonAsString = "{\"id\":\"random6dd1a7\",\"state\":\"FINISHED\",\"timestamp\":1602769319555}";
        LogEntry logEntry = logEntryJsonMapper.apply(jsonAsString);
        assertThat(logEntry).isNotNull();
        assertThat(logEntry.getId()).isEqualTo("random6dd1a7");
        assertThat(logEntry.getHost()).isNull();
        assertThat(logEntry.getType()).isNull();
        assertThat(logEntry.getState()).isEqualTo(State.FINISHED);
        assertThat(logEntry.getTimestamp()).isEqualTo(1602769319555L);
    }

    @ParameterizedTest
    @MethodSource("eventJsonMapperGenerator")
    void givenValidJsonString_whenMissingState_thenReturnInvalidEvent(LogEntryJsonMapper logEntryJsonMapper) {
        String jsonAsString = "{\"id\":\"random6dd1a7\",\"timestamp\":1602769319555}";
        LogEntry logEntry = logEntryJsonMapper.apply(jsonAsString);
        assertThat(logEntry).isNotNull();
        assertThat(logEntry.getId()).isEqualTo("random6dd1a7");
        assertThat(logEntry.getHost()).isNull();
        assertThat(logEntry.getType()).isNull();
        assertThat(logEntry.getState()).isNull();
        assertThat(logEntry.getTimestamp()).isEqualTo(1602769319555L);
        assertThat(logEntry.isValid()).isFalse();
    }


    @ParameterizedTest
    @MethodSource("eventJsonMapperGenerator")
    void givenUnParsableString_thenReturnNull(LogEntryJsonMapper logEntryJsonMapper) {
        String jsonAsString = "\"id\":\"random6dd1a7\"";
        LogEntry logEntry = logEntryJsonMapper.apply(jsonAsString);
        assertThat(logEntry).isNull();
    }

    @ParameterizedTest
    @MethodSource("eventJsonMapperGenerator")
    void givenValidJsonString_thenReturnZeroErrorCount(LogEntryJsonMapper logEntryJsonMapper) {
        String jsonAsString = "{\"id\":\"random6dd1a7\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1602769319555}";
        logEntryJsonMapper.apply(jsonAsString);
        assertThat(logEntryJsonMapper.getErrorCount()).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("eventJsonMapperGenerator")
    void givenUnParsableString_thenReturnOneErrorCount(LogEntryJsonMapper logEntryJsonMapper) {
        String jsonAsString = "\"id\":\"random6dd1a7\"";
        logEntryJsonMapper.apply(jsonAsString);
        assertThat(logEntryJsonMapper.getErrorCount()).isEqualTo(1);
    }

    @ParameterizedTest
    @MethodSource("eventJsonMapperGenerator")
    void givenValidJsonString_whenWrongState_thenReturnNullAndErrorCount(LogEntryJsonMapper logEntryJsonMapper) {
        String jsonAsString = "{\"id\":\"random6dd1a7\",\"state\":\"BANANA\",\"timestamp\":1602769319555}";
        LogEntry logEntry = logEntryJsonMapper.apply(jsonAsString);
        assertThat(logEntry).isNull();
        assertThat(logEntryJsonMapper.getErrorCount()).isEqualTo(1);
    }

    @ParameterizedTest
    @MethodSource("eventJsonMapperGenerator")
    void givenUnParsableString_whenProcessedMultipleTimes_thenReturnSameAmountOfError(LogEntryJsonMapper logEntryJsonMapper) {
        String jsonAsString = "\"id\":\"random6dd1a7\"";
        logEntryJsonMapper.apply(jsonAsString);
        logEntryJsonMapper.apply(jsonAsString);
        logEntryJsonMapper.apply(jsonAsString);
        logEntryJsonMapper.apply(jsonAsString);
        logEntryJsonMapper.apply(jsonAsString);
        assertThat(logEntryJsonMapper.getErrorCount()).isEqualTo(5);
    }

    private static Stream<Arguments> eventJsonMapperGenerator() {

        return Stream.of(
                Arguments.of(new SequentialLogEntryJsonMapper()),
                Arguments.of(new ParallelLogEntryJsonMapper()));
    }

}
