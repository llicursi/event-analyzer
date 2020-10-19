package com.logger.eventanalyzer;

import com.logger.eventanalyzer.event.Event;
import com.logger.eventanalyzer.json.EventJsonMapper;
import com.logger.eventanalyzer.json.SequentialEventJsonMapper;
import com.logger.eventanalyzer.source.SourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class EventAnalyzer {

    private static final Logger LOG = LoggerFactory.getLogger(EventAnalyzer.class);

    public EventAnalyzer() {
    }

    public List<Event> analyze(SourceStream sourceStream, long thresholdDuration) {
        EventJsonMapper eventJsonMapper = new SequentialEventJsonMapper();
        LOG.debug("Analyzing events sequentially");
        return analyze(sourceStream.stream(), eventJsonMapper, thresholdDuration);
    }

    private List<Event> analyze(Stream<String> stream, EventJsonMapper eventJsonMapper, long thresholdDuration) {

        List<Event> collect = stream
                .map(eventJsonMapper)
                .filter(excludeInvalid())
                .map(new Summarizer(thresholdDuration))
                .filter(excludeInvalid())
                .peek(eventAlertConsumer(thresholdDuration))
                .collect(Collectors.toList());

        logFailedParsing(eventJsonMapper);
        return collect;
    }

    private void logFailedParsing(EventJsonMapper eventJsonMapper) {
        if (eventJsonMapper.getErrorCount() > 0) {
            LOG.warn("Unable to parse {} events", eventJsonMapper.getErrorCount());
        }
    }

    private Predicate<Validatable> excludeInvalid() {
        return e -> e != null && e.isValid();
    }

    private Consumer<Event> eventAlertConsumer(long thresholdDuration) {
        return event -> {
            if (event.getAlert()){
                LOG.info("Event {} is longer than {}ms", event.getId(), thresholdDuration);
            }
        };
    }


}
