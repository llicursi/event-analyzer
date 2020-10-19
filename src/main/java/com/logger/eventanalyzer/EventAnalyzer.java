package com.logger.eventanalyzer;

import com.logger.eventanalyzer.config.AnalyzerConfig;
import com.logger.eventanalyzer.event.Event;
import com.logger.eventanalyzer.event.Validatable;
import com.logger.eventanalyzer.mapper.LogEntryJsonMapper;
import com.logger.eventanalyzer.mapper.ParallelLogEntryJsonMapper;
import com.logger.eventanalyzer.mapper.SequentialLogEntryJsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Component
public class EventAnalyzer {

    private static final Logger LOG = LoggerFactory.getLogger(EventAnalyzer.class);

    public EventAnalyzer() {
    }

    public void analyze(AnalyzerConfig config, Consumer<? super Event> eventsConsumer) {
        if (config.isParallel()) {
            LOG.debug("Analyzing events in parallel");
            analyze(config.getSourceStream().stream().parallel(), new ParallelLogEntryJsonMapper(), config.getThresholdDuration(), eventsConsumer);
        } else {
            LOG.debug("Analyzing events sequentially");
            analyze(config.getSourceStream().stream(), new SequentialLogEntryJsonMapper(), config.getThresholdDuration(), eventsConsumer);
        }
    }

    private void analyze(Stream<String> stream, LogEntryJsonMapper logEntryJsonMapper, long thresholdDuration, Consumer<? super Event> eventConsumer) {
        EventParser logEntryToEventParser = new EventParser(thresholdDuration);
        stream
                .map(logEntryJsonMapper)
                .filter(excludeInvalid())
                .map(logEntryToEventParser)
                .filter(excludeInvalid())
                .forEach(eventConsumer);

        logFailedParsing(logEntryJsonMapper);
    }

    private void logFailedParsing(LogEntryJsonMapper logEntryJsonMapper) {
        if (logEntryJsonMapper.getErrorCount() > 0) {
            LOG.warn("Unable to parse {} events", logEntryJsonMapper.getErrorCount());
        }
    }

    private Predicate<Validatable> excludeInvalid() {
        return e -> e != null && e.isValid();
    }


}
