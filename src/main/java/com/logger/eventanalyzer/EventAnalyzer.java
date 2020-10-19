package com.logger.eventanalyzer;

import com.logger.eventanalyzer.event.Event;
import com.logger.eventanalyzer.event.Validatable;
import com.logger.eventanalyzer.json.EventJsonMapper;
import com.logger.eventanalyzer.json.SequentialEventJsonMapper;
import com.logger.eventanalyzer.source.SourceStream;
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

    public void analyze(SourceStream sourceStream, long thresholdDuration, Consumer<? super Event> eventsConsumer) {
        EventJsonMapper eventJsonMapper = new SequentialEventJsonMapper();
        LOG.debug("Analyzing events sequentially");
        analyze(sourceStream.stream(), eventJsonMapper, thresholdDuration, eventsConsumer);
    }

    private void analyze(Stream<String> stream, EventJsonMapper eventJsonMapper, long thresholdDuration, Consumer<? super Event> consumer) {

        stream
                .map(eventJsonMapper)
                .filter(excludeInvalid())
                .map(new Summarizer(thresholdDuration))
                .filter(excludeInvalid())
                .forEach(consumer);

        logFailedParsing(eventJsonMapper);
    }

    private void logFailedParsing(EventJsonMapper eventJsonMapper) {
        if (eventJsonMapper.getErrorCount() > 0) {
            LOG.warn("Unable to parse {} events", eventJsonMapper.getErrorCount());
        }
    }

    private Predicate<Validatable> excludeInvalid() {
        return e -> e != null && e.isValid();
    }



}
