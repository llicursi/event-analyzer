package com.logger.eventanalyzer.source;

import java.util.Arrays;
import java.util.stream.Stream;

public class StringSourceStream implements SourceStream{

    private String[] sourceArray;

    public StringSourceStream(String...sourceArray) {
        this.sourceArray = sourceArray;
    }

    @Override
    public Stream<String> stream() {
        return Arrays.stream(sourceArray);
    }
}
