package com.logger.eventanalyzer.config;

import com.logger.eventanalyzer.source.SourceStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class AnalyzerConfig {

    private long thresholdDuration;
    private SourceStream sourceStream;
    private boolean parallel;

}
