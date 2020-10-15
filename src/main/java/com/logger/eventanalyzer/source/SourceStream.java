package com.logger.eventanalyzer.source;

import java.util.stream.Stream;

public interface SourceStream {

    Stream<String> stream();
}
