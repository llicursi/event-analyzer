package com.logger.eventanalyzer;

import com.logger.eventanalyzer.event.Event;
import com.logger.eventanalyzer.source.SourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    private static final Logger LOG = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventAnalyzer eventAnalyzer;

    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public void processEvents(SourceStream source) {
        LOG.info("Processing events");
        eventAnalyzer.analyze(source, 4L, this::saveEventSummary);
    }

    private void saveEventSummary(Event event) {
        eventRepository.save(event);
    }
}
