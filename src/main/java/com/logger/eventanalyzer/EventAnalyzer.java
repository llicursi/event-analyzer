package com.logger.eventanalyzer;

import com.logger.eventanalyzer.event.EventSummary;
import com.logger.eventanalyzer.json.EventJsonMapper;
import com.logger.eventanalyzer.json.SequentialEventJsonMapper;
import com.logger.eventanalyzer.source.SourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventAnalyzer {

    private static final Logger LOG = LoggerFactory.getLogger(EventAnalyzer.class);

    public EventAnalyzer() {
    }

    public List<EventSummary> analyze(SourceStream sourceStream) {
        EventJsonMapper eventJsonMapper = new SequentialEventJsonMapper();
        LOG.debug("Analyzing events sequentially");
        return analyze(sourceStream.stream(), eventJsonMapper);
    }

    private List<EventSummary> analyze(Stream<String> stream, EventJsonMapper eventJsonMapper) {

        List<EventSummary> collect = stream
                .map(eventJsonMapper)
                .filter(excludeInvalid())
                .map(new Summarizer())
                .filter(excludeInvalid())
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


}
