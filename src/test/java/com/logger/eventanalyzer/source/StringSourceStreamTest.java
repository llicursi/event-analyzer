package com.logger.eventanalyzer.source;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class StringSourceStreamTest {

    @Test
    void givenEmptyArray_thenReturnEmptyStream(){
        StringSourceStream stringSourceStream = new StringSourceStream();
        List<String> collectValues = stringSourceStream.stream().collect(Collectors.toList());
        assertThat(collectValues.size()).isEqualTo(0);
    }


    @Test
    void givenNomEmptyArray_thenReturnStreamWithSameSize(){
        String[] inputArray = {"1", "2", "3", "4"};
        StringSourceStream stringSourceStream = new StringSourceStream(inputArray);
        List<String> collectValues = stringSourceStream.stream().collect(Collectors.toList());
        assertThat(collectValues.size()).isEqualTo(inputArray.length);
    }
}