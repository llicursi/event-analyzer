package com.logger.eventanalyzer.config;

import com.logger.eventanalyzer.source.SourceStream;
import com.logger.eventanalyzer.source.StringSourceStream;

public final class AnalyzerConfigUtil {

    private static final long DEFAULT_THRESHOLD_DURATION = 4L;

    public static AnalyzerConfig getSequentialAnalyzerConfig(String... sourceArray) {
        SourceStream sourceStream = new StringSourceStream(sourceArray);
        return AnalyzerConfig.builder()
                .parallel(false)
                .sourceStream(sourceStream)
                .thresholdDuration(DEFAULT_THRESHOLD_DURATION)
                .build();
    }


    public static AnalyzerConfig getParallelAnalyzerConfig(String... sourceArray) {
        SourceStream sourceStream = new StringSourceStream(sourceArray);
        return AnalyzerConfig.builder()
                .parallel(false)
                .sourceStream(sourceStream)
                .thresholdDuration(DEFAULT_THRESHOLD_DURATION)
                .build();
    }
}
